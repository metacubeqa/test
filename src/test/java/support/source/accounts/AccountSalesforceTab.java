package support.source.accounts;

import static org.testng.Assert.assertEquals;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class AccountSalesforceTab extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	By salesforceTab 						= By.cssSelector("[data-tab='salesforce']");
	By salesForceTabParent 					= By.xpath(".//*[@data-tab='salesforce']/ancestor::li");
	By salesforceTabHeading 				= By.xpath("//h2[text()='Salesforce Settings']");
	
	By ringDNASFDCId						= By.cssSelector("input.sfAccountId");
	
	By createLeadEveryIncCallCheckBox	 	= By.cssSelector("input.sfdcCreateLeadOnTrackingNumber");
	By createLeadEveryIncCallToggleOffBtn	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnTrackingNumber')]/..//label[contains(@class,'toggle-off')]");
	By createLeadEveryIncCallToggleOnBtn 	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnTrackingNumber')]/..//label[contains(@class,'toggle-on')]");
	
	By createLeadEveryContactCheckBox 		= By.cssSelector("input.sfdcCreateLeadOnContactOnly");
	By createLeadEveryContactToggleOffBtn	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnContactOnly')]/..//label[contains(@class,'toggle-off')]");
	By createLeadEveryContactToggleOnBtn 	= By.xpath("//input[contains(@class,'sfdcCreateLeadOnContactOnly')]/..//label[contains(@class,'toggle-on')]");

	By selectClassSalesForceField			= By.cssSelector(".form-control.sfdc-name");
	By nameField							= By.cssSelector(".form-control.name");
	
	By salesForceFieldLabel					= By.xpath(".//*[contains(@class, 'bbm-modal')]//*[contains(text(),'Salesforce Field')]");
	By salesForceFieldsDropDown       		= By.cssSelector(".salesforce-integration .selectize-control");
	By salesForceFieldsDropDownItems  		= By.cssSelector(".salesforce-integration .selectize-dropdown-content .option");
	By salesForceFieldsTextBox		  		= By.cssSelector(".salesforce-integration .selectize-input input");
	By salesForceFieldsTextBoxItems			= By.cssSelector(".salesforce-integration .selectize-input.has-items div");
	By salesForceNameTab			  		= By.cssSelector(".salesforce-integration .form-control.name");
	By salesForceSaveBtn			  		= By.cssSelector(".btn-success.save");
	By confirmDeleteBtn				  		= By.cssSelector("[data-bb-handler='confirm']");
	String salesForceField					= ".salesforce-integration .selectize-dropdown-content [data-value='$field$']";
	
	By settingsTabBtn				  		= By.cssSelector(".sfdc-field-settings-tabs .settings");
	By accountFields						= By.cssSelector(".sfdc-field-settings-tabs .accountFields");
	By campaignFields						= By.cssSelector(".sfdc-field-settings-tabs .campaignFields");
	By caseFields							= By.cssSelector(".sfdc-field-settings-tabs .caseFields");
	By opportunityFields					= By.cssSelector(".sfdc-field-settings-tabs .opportunityFields");
	By leadFieldsTabBtn				  		= By.cssSelector(".sfdc-field-settings-tabs .leadFields");
	By fieldsAddBtn			 		  		= By.cssSelector(".sfdc-tabs .add .glyphicon-plus-sign");
	By disabledFieldsAddBtn					= By.cssSelector(".sfdc-tabs  .glyphicon-plus-sign.muted");
	By allViewCheckbox						= By.xpath(".//*[text()=' View']/preceding-sibling::input");
	By allAlwaysVisibleCheckbox				= By.xpath(".//*[text()=' Always Visible']/preceding-sibling::input");
	By allEditableCheckbox					= By.xpath(".//*[text()=' Editable']/preceding-sibling::input");
	By editableToolTip						= By.xpath(".//label[text()[normalize-space()='Editable']]//following-sibling::span");
	By allRequiredCheckbox					= By.xpath(".//*[text()=' Required']/preceding-sibling::input");
	By additionalFieldsSaveBtn				= By.cssSelector(".btn-success.save-btn");
	
	By customFieldsNameList					= By.xpath(".//span[contains(@class,'remove-sign')]/ancestor::tr//a[@class = 'name'] | .//span[contains(@class,'remove-sign')]/ancestor::tr/td[contains(@class, 'salesforceField.name')]");
	String additionalFieldsList				= ".//*[contains(text(),'$fieldName$')]/ancestor::tr//span[contains(@class,'remove-sign')]";
	String selectViewCheckBoxByName         = "//a[text()='%name%']/parent::td/following-sibling::td/div//input";
	
	//salesforce connect user section
	By notificationsToolTip					= By.xpath(".//label[text()[normalize-space()='Notifications']]//parent::label//following-sibling::span");
	By sendToAllAdminsCheckBox				= By.cssSelector(".sfdcInvalidNotificationsAll");
	By notificationUserList					= By.xpath(".//*[@class='notification-selector selectized']/following-sibling::div//div[@class='item']");
	By notificationInputTab					= By.xpath(".//*[@class='notification-selector selectized']/following-sibling::div//input");
	By notificationUserDropdown				= By.xpath(".//*[@class='notification-selector selectized']/following-sibling::div//div[@class='selectize-dropdown-content']//div[contains(@class,'option')]");
	
	//contact fields section
	By contactFieldsTabBtn			  		= By.cssSelector(".sfdc-field-settings-tabs .contactFields");
	String alwaysVisibleField				= "//a[text()='$field$']/ancestor::tr//td[@class= 'string-cell renderable']";
	String editableField					= ".//td/div[text()='$field$']/ancestor::tr//td[7][text()='Yes']";
	String requiredField					= ".//td/div[text()='$field$']/ancestor::tr//td[contains(@class,'required') and text()='Yes']";
	String updatedDateField					= ".//td/a[text()='$field$']/ancestor::tr//td[contains(@class,'updatedDate')]";
	
	//task fields section
	By taskFiedlHeading 					= By.xpath(".//*[text()='Task Fields']");
	By taskFieldTabBtn						= By.cssSelector(".sfdc-field-settings-tabs .taskFields");
																			  
	
	//events fields section
	By eventFiedlHeading 					= By.xpath(".//*[text()='Event Fields']");
	By eventFieldTabBtn						= By.cssSelector(".sfdc-field-settings-tabs .eventFields");
	
	By nextButton							= By.cssSelector(".btn.btn-success.next");
	By saveAccountsSettingButton	 		= By.cssSelector("button.save");
	By saveAccountsSettingMessage	 		= By.className("toast-message");
	
	By sfAddedFieldsNameList 				= By.cssSelector(".fields-backgrid-container tbody tr td:nth-child(1) a");
	By sfAddedFieldsVisibleList 			= By.cssSelector("td.visible");
	By sfAddedFieldsEditableList 			= By.cssSelector(".fields-backgrid-container tbody tr td:nth-child(7)");
	By deleteFieldsList						= By.cssSelector(".delete .glyphicon-remove-sign");
	
	By sfAdditionalFieldList				= By.xpath(".//*[contains(@class, 'order-cell')]/following-sibling::td[1]/div");
	By deleteButtonList						= By.cssSelector(".glyphicon-remove-sign");
	By sendToTopBtn							= By.xpath(".//*[@class='popover-content']/a[text()='Send to top']");
	String sortUpDownField					= ".//*[text()='$FieldName$']/ancestor::tr//td//span[contains(@class,'glyphicon-menu-up') or contains(@class,'glyphicon-menu-down')]";
	String updateField			 			= ".//*[text()='$FieldName$']/ancestor::tr//*[@class='glyphicon glyphicon-pencil edit-field']";
	String deleteField			 			= ".//*[text()='$FieldName$']/ancestor::tr//span[@class='glyphicon glyphicon-remove-sign']";

	String sortUpField						= ".//*[text()='$FieldName$']/ancestor::tr//td//span[contains(@class,'glyphicon-menu-up')]";
	
	// tool tip
	By enhancedAnalyticsToolTip				= By.xpath(".//label[text()[normalize-space()='Enhanced Analytics']]//parent::label//following-sibling::span");
	By smartNumberCampaignsToolTip			= By.xpath(".//label[text()[normalize-space()='Create Smart Number Campaigns']]//parent::label//following-sibling::span");
	
	public static final String notificationsToolTipText = "Select the recipient(s) for the 'Invalid Salesforce Connect User' emails";
	static final String editableToolTipText				= "This field is read-only within SFDC. Contact your Salesforce admin to update permissions.";
	
	public static enum defaultContacts {
		Lead,
		Contact
	}
	
	public static enum salesforceFieldsType{
		picklist,
		Boolean,
		Double,
		datetime,
		url,
		string,
		email,
		address,
		time
	}
	
	public static enum SalesForceFields{
		CreatedByEmail("CreatedBy.Email"),
		CreatedByUserName("CreatedBy.Username"),
		CompanyName("Owner.companyName"),
		OwnerFirstName("Owner.FirstName"),
		OwnerLastName("Owner.LastName"),
		TotalOpportunityQuantity("TotalOpportunityQuantity"),
		AccountSite("Site"),
		AccountFax("Fax"),
		AccountType("Type"),
		AccountCity("Account.Account_City__c"),
		PhysicalCity("Physical_City__c"),
		AccountName("Account Name"),
		AccountPhone("Account Phone"),
		AssistantName("AssistantName"),
		AsstPhone("Asst. Phone"),
		CaseNumber("Case Number"),
		CaseOrigin("Origin"),
		Subject("Subject"),
		SuppliedName("SuppliedName"),
		Name("Name"),
		FirstName("First Name"),
		LastName("Last Name"),
		Title("Title"),
		Company("Company"),
		Email("Email"),
		LeadSource("Lead Source"),
		LeadStatus("Lead Status"),
		HomePhone("Home Phone"),
		OtherPhone("Other Phone"),
		MobilePhone("Mobile Phone"),
		Phone("Phone"),
		AboutMe("CreatedBy.AboutMe"),
		Industry("Industry"),
		Stage("StageName"),
		Status("Status"),
		City("CreatedBy.City"),
		PhoneNo("Phone_No__c"),
		Random("ab");
		
		private String displayName;
		
		SalesForceFields(String displayName){
			this.displayName = displayName;
		};

		public String displayName() { return displayName; }
		
		@Override
		public String toString(){return displayName;};
	}
	
	public void openSalesforceTab(WebDriver driver){
		if (!findElement(driver, salesForceTabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, salesforceTab);
			clickElement(driver, salesforceTab);
			dashboard.isPaceBarInvisible(driver);
			findElement(driver, salesforceTabHeading);
		}
	}
	
	/*******Settings Fields Section starts here*******/
	public void enterRingDNASFDCAccountId(WebDriver driver, String id){
		waitUntilVisible(driver, ringDNASFDCId);
		enterText(driver, ringDNASFDCId, id);
	}
	
	public String getRingDNASFDCAccountId(WebDriver driver){
		return getAttribue(driver, ringDNASFDCId, ElementAttributes.value);
	}
	
	public boolean isUserOnSalesforcePage(WebDriver driver){
		return isElementVisible(driver, salesforceTabHeading, 0);
	}
	
	public void enableCreateLeadEveryInboundCallSetting(WebDriver driver){
		if(!findElement(driver, createLeadEveryIncCallCheckBox).isSelected()) {
			System.out.println("Checking Create lead on every incoming call checkbox");
			clickElement(driver, createLeadEveryIncCallToggleOffBtn);				
		}else {
			System.out.println("Create lead on every incoming call setting is already enable");
		}
	}

	public void disableCreateLeadEveryInboundCallSetting(WebDriver driver){
		if(findElement(driver, createLeadEveryIncCallCheckBox).isSelected()) {
			System.out.println("disabling Create lead on every incoming call checkbox");
			clickElement(driver, createLeadEveryIncCallToggleOnBtn);				
		}else {
			System.out.println("Create lead on every incoming call setting is already disabled");
		}
	}
	
	public void enablecreateLeadEveryContactSetting(WebDriver driver){
		if(!findElement(driver, createLeadEveryContactCheckBox).isSelected()) {
			System.out.println("Checking Create lead on every incoming call from contact only checkbox");
			clickElement(driver, createLeadEveryContactToggleOffBtn);				
		}else {
			System.out.println("Create lead on every incoming call from contact only setting is already enable");
		}
	}
	
	public void disablecreateLeadEveryContactSetting(WebDriver driver){
		if(findElement(driver, createLeadEveryContactCheckBox).isSelected()) {
			System.out.println("disabling Create lead on every incoming call from contact only checkbox");
			clickElement(driver, createLeadEveryContactToggleOnBtn);				
		}else {
			System.out.println("Create lead on every incoming call from contact only setting is already disabled");
		}
	}
	
	public void selectNewDefaultContact(WebDriver driver, String itemToSearch){
		enterTextAndSelectFromDropDown(driver, salesForceFieldsDropDown, salesForceFieldsTextBox, salesForceFieldsDropDownItems, itemToSearch);
		saveAcccountSettings(driver);
	}
	
	public void navigateToSettingsSection(WebDriver driver) {
		if(!findElement(driver, settingsTabBtn).getAttribute("class").contains("active")){
			waitUntilVisible(driver, settingsTabBtn);
			clickElement(driver, settingsTabBtn);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	public boolean isSalesForceFieldDisabled(WebDriver driver){
		waitUntilVisible(driver, selectClassSalesForceField);
		return isAttributePresent(driver, selectClassSalesForceField, "disabled");
	}
	
	public boolean isNameFieldEditable(WebDriver driver){
		waitUntilVisible(driver, nameField);
		return !isAttributePresent(driver, nameField, "disabled");
	}

	/*******Settings Fields Section ends here*******/
	
	/*******Account Fields Section starts here*******/
	public void navigateToAccountFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, accountFields);
		clickElement(driver, accountFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isAlwaysVisibleCheckboxVisible(WebDriver driver){
		return isElementVisible(driver, allAlwaysVisibleCheckbox, 5);
	}
	
	public void checkViewCheckBox(WebDriver driver){
		waitUntilVisible(driver, allViewCheckbox);
		checkCheckBox(driver, allViewCheckbox);
	}
	
	public void checkAlwaysVisibleCheckBox(WebDriver driver){
		waitUntilVisible(driver, allAlwaysVisibleCheckbox);
		checkCheckBox(driver, allAlwaysVisibleCheckbox);
	}
	
	public void checkEditableCheckBox(WebDriver driver){
		waitUntilVisible(driver, allEditableCheckbox);
		checkCheckBox(driver, allEditableCheckbox);
	}
	
	public void unCheckEditableCheckBox(WebDriver driver){
		waitUntilVisible(driver, allEditableCheckbox);
		unCheckCheckBox(driver, allEditableCheckbox);
	}
	
	public boolean isAlwaysVisibleCheckBoxEnable(WebDriver driver) {
		return findElement(driver, allAlwaysVisibleCheckbox).isEnabled();
	}
	
	public boolean isEditableCheckBoxEnable(WebDriver driver) {
		return findElement(driver, allEditableCheckbox).isEnabled();
	}
	
	public void checkRequiredCheckBox(WebDriver driver){
		idleWait(1);
		waitUntilVisible(driver, allRequiredCheckbox);
		if(!findElement(driver, allRequiredCheckbox).isSelected()) {
			checkEditableCheckBox(driver);
		}
		checkCheckBox(driver, allRequiredCheckbox);
	}
	
	public String createAccountSalesForceField(WebDriver driver, Enum<?> field, String accountFieldName, boolean isVisible, boolean isEditable, boolean isRequired) {
		clickAdditionalLeadFieldsBtn(driver);
		selectSalesForceField(driver, field);
		enterAdditionalFieldName(driver, accountFieldName);
		clickElement(driver, salesForceFieldLabel);
		clickNextButton(driver);
		if(isVisible)
			checkViewCheckBox(driver);  //for now we click view checkbox
			//checkAlwaysVisibleCheckBox(driver);
			
		if(isEditable)
			checkEditableCheckBox(driver);
		
		if(isRequired)
			checkRequiredCheckBox(driver);
		
		//getting current date time before saving
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		if (createdDate.contains("PM")) {
			createdDate = createdDate.replace("PM", "pm");
		} else {
			createdDate = createdDate.replace("AM", "am");
		}
		
		clickFieldsSaveButton(driver);
		
		return createdDate;
	}
	
	/**
	 * @param driver
	 * @param field
	 * @param accountFieldName
	 * @param isVisible
	 * @param isEditable
	 * @param isRequired
	 * @param name
	 * @return time of creation
	 */
	public String createAccountSalesForceField(WebDriver driver, Enum<?> field, String accountFieldName, boolean isVisible, boolean isEditable, boolean isRequired, String name) {
		By additionalFieldsLoc = null;
		clickAdditionalLeadFieldsBtn(driver);
		selectSalesForceField(driver, field);
		enterAdditionalFieldName(driver, accountFieldName);
		clickNextButton(driver);
		if(isVisible)
			additionalFieldsLoc = By.xpath(selectViewCheckBoxByName.replace("%name%", name));
			if(!isAttributePresent(driver, additionalFieldsLoc, "disabled")) {
				clickElement(driver, additionalFieldsLoc);	
			}else {
				return null;
			}
			
		if(isEditable)
			checkEditableCheckBox(driver);
		
		if(isRequired)
			checkRequiredCheckBox(driver);
		
		//getting current date time before saving
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		if (createdDate.contains("PM")) {
			createdDate = createdDate.replace("PM", "pm");
		} else {
			createdDate = createdDate.replace("AM", "am");
		}
		
		clickFieldsSaveButton(driver);
		
		return createdDate;
	}
	
	/*******Account Fields Section ends here*******/
	
	/*******Lead Fields Section starts here*******/
	
	public void cleanAdditionalFieldsSection(WebDriver driver, String fieldName) {
		By additionalFieldsLoc = By.xpath(additionalFieldsList.replace("$fieldName$", fieldName));
		if (isListElementsVisible(driver, additionalFieldsLoc, 5)) {
			int i = getElements(driver, additionalFieldsLoc).size() - 1;
			while (i >= 0) {
				// delete field
				scrollTillEndOfPage(driver);
				clickElement(driver, getElements(driver, additionalFieldsLoc).get(i));
				waitUntilVisible(driver, confirmDeleteBtn);
				clickElement(driver, confirmDeleteBtn);
				waitUntilInvisible(driver, confirmDeleteBtn);
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				i--;
			}
		}
	}
	
	public void navigateToLeadFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, leadFieldsTabBtn);
		clickElement(driver, leadFieldsTabBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickAdditionalLeadFieldsBtn(WebDriver driver) {
		waitUntilVisible(driver, fieldsAddBtn);
		clickElement(driver, fieldsAddBtn);
	}
	
	public boolean isFieldsAddBtnDisabled(WebDriver driver){
		return isElementVisible(driver, disabledFieldsAddBtn, 5);
	}
	
	public void enterAdditionalFieldName(WebDriver driver, String fieldName) {
		waitUntilVisible(driver, salesForceNameTab);
		clearAll(driver, salesForceNameTab);
		enterText(driver, salesForceNameTab, fieldName);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectSalesForceField(WebDriver driver, Enum<?> field){
		waitUntilVisible(driver, salesForceFieldsTextBox);
		enterText(driver, salesForceFieldsTextBox, field.toString());
		idleWait(2);
		for(WebElement element: getElements(driver, salesForceFieldsDropDownItems)){
			if(getAttribue(driver, element, ElementAttributes.dataValue).contains(field.toString())){
				clickElement(driver, element);
				break;
			}
		}
	}
	
	public void clickNextButton(WebDriver driver) {
		clickElement(driver, nextButton);
	}
	
	public void selectSalesForceField(WebDriver driver, String field){
		waitUntilVisible(driver, salesForceFieldsTextBox);
		enterText(driver, salesForceFieldsTextBox, field);
		idleWait(2);
		for(WebElement element: getElements(driver, salesForceFieldsDropDownItems)){
			if(getAttribue(driver, element, ElementAttributes.dataValue).equals(field)){
				clickElement(driver, element);
				break;
			}
		}
	}
	
	public void selectUpdatedSalesForceField(WebDriver driver, Enum<?> field){
		waitUntilVisible(driver, salesForceFieldsTextBoxItems);
		clickElement(driver, salesForceFieldsTextBoxItems);
		idleWait(1);
		clickElement(driver, salesForceFieldsTextBox);
		enterBackspace(driver, salesForceFieldsTextBox);
		idleWait(1);
		enterText(driver, salesForceFieldsTextBox, field.toString());
		for(WebElement element: getElements(driver, salesForceFieldsDropDownItems)){
			if(getAttribue(driver, element, ElementAttributes.dataValue).equals(field.toString())){
				clickElement(driver, element);
				break;
			}
		}
	}
	
	public boolean clickFieldsSaveButton(WebDriver driver) {
		boolean answer = false;
		waitUntilVisible(driver, additionalFieldsSaveBtn);
		
		if(isElementDisabled(driver, additionalFieldsSaveBtn, 6)) {
			answer = true;
			return answer;
		}
		
		clickElement(driver, additionalFieldsSaveBtn);
		waitUntilInvisible(driver, additionalFieldsSaveBtn);
		dashboard.isPaceBarInvisible(driver);
		isSaveAccountsSettingMessageDisappeared(driver);
		return answer;
	}
	
	public void clickSalesForceSaveBtn(WebDriver driver){
		waitUntilVisible(driver, salesForceSaveBtn);
		clickElement(driver, salesForceSaveBtn);
		waitUntilInvisible(driver, salesForceSaveBtn);
		dashboard.isPaceBarInvisible(driver);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void createAdditionalLeadField(WebDriver driver, Enum<?> field, String leadFieldName) {
		navigateToLeadFieldsSection(driver);
		clickAdditionalLeadFieldsBtn(driver);
		selectSalesForceField(driver, field);
		enterAdditionalFieldName(driver, leadFieldName);
		clickSalesForceSaveBtn(driver);
	}
	
	public void clickUpddateBtn(WebDriver driver, String fieldName){
		By locator = By.xpath(updateField.replace("$FieldName$", fieldName));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	public void updateAdditionalField(WebDriver driver, String fieldName, String leadFieldName) {
		clickUpddateBtn(driver, fieldName);
		enterAdditionalFieldName(driver, leadFieldName);
		clickSalesForceSaveBtn(driver);
	}
	
	public boolean isDeleteIconVisible(WebDriver driver, String fieldName){
		By locator = By.xpath(deleteField.replace("$FieldName$", fieldName));
		return isElementVisible(driver, locator, 5);
	}
	
	public void deleteAdditionalField(WebDriver driver, String fieldName) {
		By locator = By.xpath(deleteField.replace("$FieldName$", fieldName));
		waitUntilVisible(driver, locator);
		scrollTillEndOfPage(driver);
		clickElement(driver, locator);
		acceptDeleteConfirmation(driver);
		waitUntilInvisible(driver, backGroundFade);
	}
	
	public void acceptDeleteConfirmation(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteAllFieldsIfExists(WebDriver driver) {
		if (isElementVisible(driver, deleteButtonList, 6)) {
			List<WebElement> elements = getElements(driver, deleteButtonList);
			for (int i = 0; i < elements.size(); i++) {
				List<WebElement> element = getElements(driver, deleteButtonList);
				dashboard.isPaceBarInvisible(driver);
				scrollTillEndOfPage(driver);
				clickElement(driver, element.get(0));
				acceptDeleteConfirmation(driver);
			}
		}
	}
	
	public int getNumberOfDeleteAllFields(WebDriver driver) {
		if (isElementVisible(driver, deleteButtonList, 6)) {
			List<WebElement> elements = getElements(driver, deleteButtonList);
			return elements.size();
		} else {
			return 0;
		}
	}
	
	public boolean isAddditionalFieldCreated(WebDriver driver, String fieldName) {
		By locator = By.xpath(updateField.replace("$FieldName$", fieldName));
		return isElementVisible(driver, locator, 5);
	}
	
	public String getSelectedSfField(WebDriver driver) {
		return getElementsText(driver, salesForceFieldsTextBoxItems);
	}
	
	public String getSelectedSfFieldName(WebDriver driver) {
		return getAttribue(driver, salesForceNameTab, ElementAttributes.value);
	}
	
	public int getAddedSfFieldIndex(WebDriver driver, String sfFieldName) {
		return getTextListFromElements(driver, sfAddedFieldsNameList).indexOf(sfFieldName);
	}
	
		public List<String> getAddedSfdcFieldsList(WebDriver driver) {
		return getTextListFromElements(driver, sfAddedFieldsNameList);
	}														   
																
	public List<String> getSfdcFieldsList(WebDriver driver){
		return getTextListFromElements(driver, sfAdditionalFieldList);
	}
	
	public void moveSfdcFieldDown(WebDriver driver, String sfFieldName) {
		By sortFields = By.xpath(sortUpDownField.replace("$FieldName$", sfFieldName));
		getElements(driver, sortFields).get(1).click();
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isAddedSfFiedlVisible(WebDriver driver, String sfFieldName) {
		int index = getAddedSfFieldIndex(driver, sfFieldName);
		if(getElementsText(driver, getElements(driver, sfAddedFieldsVisibleList).get(index)).equals("Yes")){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isAddedSfFiedlEditable(WebDriver driver, String sfFieldName) {
		int index = getAddedSfFieldIndex(driver, sfFieldName);
		if(getElementsText(driver, getElements(driver, sfAddedFieldsEditableList).get(index)).equals("Yes")){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isAddedSfFiedlRequired(WebDriver driver, String sfFieldName) {
		By sfAddedFieldsRequiredList = By.cssSelector("td.required");
		int index = getAddedSfFieldIndex(driver, sfFieldName);
		if(getElementsText(driver, getElements(driver, sfAddedFieldsRequiredList).get(index)).equals("Yes")){
			return true;
		}else {
			return false;
		}
	}
	/*******Lead Fields Section ends here*******/
	
	/*******Contact Fields Section starts here*******/
	
	public void navigateToContactFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, contactFieldsTabBtn);
		clickElement(driver, contactFieldsTabBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void createAdditionalContactField(WebDriver driver, Enum<?> field, String contactFieldName) {
		navigateToContactFieldsSection(driver);
		clickAdditionaContactFieldsBtn(driver);
		selectSalesForceField(driver, field);
		enterAdditionalFieldName(driver, contactFieldName);
		clickSalesForceSaveBtn(driver);
	}
	
	public void clickAdditionaContactFieldsBtn(WebDriver driver) {
		waitUntilVisible(driver, fieldsAddBtn);
		clickElement(driver, fieldsAddBtn);
	}
	
	public boolean isAlwaysVisibleFieldYes(WebDriver driver, String fieldName){
		By alwaysVisibleLoc = By.xpath(alwaysVisibleField.replace("$field$", fieldName));
		return isElementVisible(driver, alwaysVisibleLoc, 5);
	}
	
	public boolean isEditableFieldYes(WebDriver driver, String fieldName){
		By editableLoc = By.xpath(editableField.replace("$field$", fieldName));
		return isElementVisible(driver, editableLoc, 5);
	}
	
	public boolean isRequiredFieldYes(WebDriver driver, String fieldName){
		By requiredLoc = By.xpath(requiredField.replace("$field$", fieldName));
		return isElementVisible(driver, requiredLoc, 5);
	}
	
	public String getUpdatedDate(WebDriver driver, String fieldName){
		By updatedDateLoc= By.xpath(updatedDateField.replace("$field$", fieldName));
		return getElementsText(driver, updatedDateLoc);
	}
	
	/*******Contact Fields Section ends here*******/
	
	/*******Campaign Fields Section Starts here*******/	
	
	public void navigateToCampaignFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, campaignFields);
		clickElement(driver, campaignFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/*******Campaign Fields Section ends here*******/	
	
	/*******Case Fields Section Starts here*******/	
	
	public void navigateToCaseFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, caseFields);
		clickElement(driver, caseFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/*******Case Fields Section ends here*******/	
	
	/*******Opportunity Fields Section Starts here*******/	
	
	public void navigateToOpportunityFieldsSection(WebDriver driver) {
		waitUntilVisible(driver, opportunityFields);
		clickElement(driver, opportunityFields);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/*******Opportunity Fields Section ends here*******/	

	/*******Task Fields Section Starts here*******/	
	public void navigateToTaskFieldsSection(WebDriver driver) {
		if(isElementVisible(driver, taskFiedlHeading, 0)) {
			System.out.println("Already on Task Fields Section");
		}else {
			waitUntilVisible(driver, taskFieldTabBtn);
			clickElement(driver, taskFieldTabBtn);
			dashboard.isPaceBarInvisible(driver);			
		}
	}
	
	public void navigateToEventFieldsSection(WebDriver driver) {
		if(isElementVisible(driver, eventFiedlHeading, 0)) {
			System.out.println("Already on Event Fields Section");
		}else {
			waitUntilVisible(driver, eventFieldTabBtn);
			clickElement(driver, eventFieldTabBtn);
			dashboard.isPaceBarInvisible(driver);			
		}
	}
	
	public void enableTaskSubjectRequiredSetting(WebDriver driver) {
		By locator = By.xpath(updateField.replace("$FieldName$", "Subject"));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, allRequiredCheckbox);
		if(findElement(driver, allRequiredCheckbox).isSelected()) {
			System.out.println("task subject required setting is already on");
		}else {
			clickElement(driver, allRequiredCheckbox);
			System.out.println("Enabled task subject required setting");
		}
		clickSalesForceSaveBtn(driver);
	}
	
	public void disableTaskSubjectRequiredSetting(WebDriver driver) {
		By locator = By.xpath(updateField.replace("$FieldName$", "Subject"));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, allRequiredCheckbox);
		if(findElement(driver, allRequiredCheckbox).isSelected()) {
			clickElement(driver, allRequiredCheckbox);
			System.out.println("Disabled task subject required setting");
		}else {
			System.out.println("Task subject required setting is already disabled");
		}
		clickSalesForceSaveBtn(driver);
	}
	
	public void enableTaskDueDateRequiredSetting(WebDriver driver) {
		By locator = By.xpath(updateField.replace("$FieldName$", "Due Date"));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, allRequiredCheckbox);
		if(findElement(driver, allRequiredCheckbox).isSelected()) {
			System.out.println("task due date required setting is already on");
		}else {
			clickElement(driver, allRequiredCheckbox);
			System.out.println("Enabled task due date required setting");
		}
		clickSalesForceSaveBtn(driver);
	}
	
	public void disableTaskDueDateRequiredSetting(WebDriver driver) {
		By locator = By.xpath(updateField.replace("$FieldName$", "Due Date"));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, allRequiredCheckbox);
		if(findElement(driver, allRequiredCheckbox).isSelected()) {
			clickElement(driver, allRequiredCheckbox);
			System.out.println("Disabled task due date required setting");
			
		}else {
			System.out.println("Task subject due date setting is already disabled");
		}
		clickSalesForceSaveBtn(driver);
	}	
	/*******Task Fields Section ends here*******/				
	By AddNewDefaultDropdown = By.cssSelector(".sfdcDefaultMatchTypeOption.selector");
	By AddNewDefaultDropdownOptions = By.cssSelector(".sfdcDefaultMatchTypeOption.selector option");
	
	public void selectContactAsAddNewDefault(WebDriver driver) {	
		clickAndSelectFromDropDown(driver, AddNewDefaultDropdown, AddNewDefaultDropdownOptions, "Contact");
	}
	
	public void selectLeadAsAddNewDefault(WebDriver driver) {	
		clickAndSelectFromDropDown(driver, AddNewDefaultDropdown, AddNewDefaultDropdownOptions, "Lead");
	}
	
	public String getSaveAccountsSettingMessage(WebDriver driver){
		return getElementsText(driver, saveAccountsSettingMessage);
	}
	
	public void isSaveAccountsSettingMessageDisappeared(WebDriver driver){
		isElementVisible(driver, saveAccountsSettingMessage, 2);
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void saveAcccountSettings(WebDriver driver){
		isSaveAccountsSettingMessageDisappeared(driver);											   
		scrollToElement(driver, saveAccountsSettingButton);
		clickElement(driver, saveAccountsSettingButton);
		isSaveAccountsSettingMessageDisappeared(driver);		
	}
	
	public void setDefaultSalesforceSettings(WebDriver driver){
		openSalesforceTab(driver);	
		//disablecreateLeadEveryContactSetting(driver);
		disableCreateLeadEveryInboundCallSetting(driver);															   
		saveAcccountSettings(driver);
		navigateToTaskFieldsSection(driver);
		enableTaskSubjectRequiredSetting(driver);	
	}
	
	public void verifyToolTipEditableField(WebDriver driver){
		waitUntilVisible(driver, editableToolTip);
		scrollToElement(driver, editableToolTip);
		hoverElement(driver, editableToolTip);
		assertEquals(getAttribue(driver, editableToolTip, ElementAttributes.dataOriginalTitle), editableToolTipText);
	}
	
	public boolean isEditableCheckBoxDisabled(WebDriver driver){
		waitUntilVisible(driver, allEditableCheckbox);
		return isAttributePresent(driver, allEditableCheckbox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isRequiredCheckBoxDisabled(WebDriver driver){
		waitUntilVisible(driver, allRequiredCheckbox);
		return isAttributePresent(driver, allRequiredCheckbox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isSortFieldsVisible(WebDriver driver, String field){
		By sortFieldLoc = By.xpath(sortUpDownField.replace("$field$", field));
		return isListElementsVisible(driver, sortFieldLoc, 5);
	}

	public void deleteFieldsAccToIndex(WebDriver driver, int index) {
		if (isListElementsVisible(driver, deleteFieldsList, 5)) {
			clickElement(driver, getElements(driver, deleteFieldsList).get(index));
			waitUntilVisible(driver, confirmDeleteBtn);
			clickElement(driver, confirmDeleteBtn);
			waitUntilInvisible(driver, confirmDeleteBtn);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	public void pressMenuUpIconSfField(WebDriver driver, String sfFieldName){
		By pressUpLoc = By.xpath(sortUpField.replace("$FieldName$", sfFieldName));
		waitUntilVisible(driver, pressUpLoc);
		clickElement(driver, pressUpLoc);
	}

	public void longPressMenuUpIconSfField(WebDriver driver, String sfFieldName){
		By longPressUpLoc = By.xpath(sortUpField.replace("$FieldName$", sfFieldName));
		waitUntilVisible(driver, longPressUpLoc);
		longPressElement(driver, longPressUpLoc);
		waitUntilVisible(driver, sendToTopBtn);
		clickElement(driver, sendToTopBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/******* Salesforce Connect User Section starts here *******/
	public void verifyToolTipNotificationSalesforceConnectUser(WebDriver driver){
		waitUntilVisible(driver, notificationsToolTip);
		scrollToElement(driver, notificationsToolTip);
		hoverElement(driver, notificationsToolTip);
		assertEquals(getAttribue(driver, notificationsToolTip, ElementAttributes.dataOriginalTitle), notificationsToolTipText);
	}
	
	public void unCheckSendToAllAdmins(WebDriver driver){
		waitUntilVisible(driver, sendToAllAdminsCheckBox);
		scrollToElement(driver, sendToAllAdminsCheckBox);
		unCheckCheckBox(driver, sendToAllAdminsCheckBox);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean verifyNotificationTabDisabled(WebDriver driver){
		return isElementDisabled(driver, notificationInputTab, 5);
	}
	
	public void checkSendToAllAdmins(WebDriver driver){
		waitUntilVisible(driver, sendToAllAdminsCheckBox);
		scrollToElement(driver, sendToAllAdminsCheckBox);
		checkCheckBox(driver, sendToAllAdminsCheckBox);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectSalesforceConnectUser(WebDriver driver, String user) {
		enterTextAndSelectFromDropDown(driver, notificationInputTab, notificationInputTab, notificationUserDropdown, user);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isUserSavedInSalesforceNotifications(WebDriver driver, String user){
		scrollToElement(driver, notificationInputTab);
		return isListContainsText(driver, getElements(driver, notificationUserList), user);
	}
	
	public boolean isUserInSalesforceNotificationsDropdown(WebDriver driver, String user){
		waitUntilVisible(driver, notificationInputTab);
		scrollToElement(driver, notificationInputTab);
		enterBackspace(driver, notificationInputTab);
		return isTextPresentInList(driver, getInactiveElements(driver, notificationUserDropdown), user);
	}
	
	/**get custom fields name list
	 * @param driver
	 * @return
	 */
	public List<String> getCustomFieldsNameList(WebDriver driver) {
		isListElementsVisible(driver, customFieldsNameList, 5);
		return getTextListFromElements(driver, customFieldsNameList);
	}

	/*******Salesforce Connect User Section ends here*******/	
	
	/**
	 * @param driver
	 * verify all salesforce tab tool tips
	 */
	public void verifyAllSalesforceTabToolTip(WebDriver driver){
		waitUntilVisible(driver, enhancedAnalyticsToolTip);
		scrollToElement(driver, enhancedAnalyticsToolTip);
		hoverElement(driver, enhancedAnalyticsToolTip);
		scrollToElement(driver, smartNumberCampaignsToolTip);
		hoverElement(driver, smartNumberCampaignsToolTip);
		scrollToElement(driver, notificationsToolTip);
		hoverElement(driver, notificationsToolTip);
	}
}
