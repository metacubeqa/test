/**
 * 
 */
package support.source.smartNumbers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

/**
 * @author Abhishek
 *
 */
public class SmartNumbersPage extends SeleniumBase{
	
	Dashboard dashboard			= new Dashboard();

	By smartNumberTextBox		= By.cssSelector("input.phone-number");
	By accountNameTextBox		= By.cssSelector(".account-picker input");
	By searchButton			 	= By.cssSelector("button.search");
	By pageTitle				= By.cssSelector(".page-title h1");
	By totalRecords             = By.cssSelector(".total-records");
	
	By searchedTableNames		= By.xpath(".//td[contains(@class,'renderable')][1]");
	By searchedTableAccountList = By.xpath(".//*[@class='smart-number-search']//a[contains(@href,'accounts')]");
	By smartNumberName			= By.cssSelector("a");
	By searchedTableSmartNos	= By.cssSelector(".renderable [href*=smart-numbers]");
	By searchedTableStatus		= By.cssSelector("td.renderable.status");
	By serachedTableType		= By.cssSelector("td.renderable.type");
	By selectStatus				= By.cssSelector(".number-status");
	By selectTypeDropDown       = By.cssSelector(".form-control.type-of-number");
	
	//Number details
	By numberDetail				= By.xpath("//label[text()='Number']/../span");
	By typeDetail				= By.xpath("//label[text()='Type']/../span");
	By dateCreated				= By.xpath("//label[text()='Date Created']/../span");
	By dateLastUsed				= By.xpath("//label[text()='Date Last Used']/../span");
	By labelDetail				= By.xpath("//label[text()='Label']/..//input");
	By sidText					= By.xpath("//label[text()='SID']/../span");
	By deletedText				= By.xpath("//label[text()='Is Deleted']/../span");
	By accountLink				= By.xpath("//label[text()='Account']/../span/a");
	By nameLink					= By.xpath("//label[text()='Name']/../span/a");
	By insertionValue			= By.xpath("//label[text()='Insertion Value']/..//input");
	By extraInfo				= By.xpath("//label[text()='Extra Info']/parent::div//following-sibling::textarea");
	
	// Smart Numbers Headers
	By statusHeader				= By.xpath("//a[text()='Status']");
	
	//Add smart number
	By creatingRadioBtn			= By.cssSelector(".testClass[value='creating'][type='radio']");
	By existingRadioBtn			= By.cssSelector(".testClass[value='existing'][type='radio']");
	By nextBtn					= By.cssSelector(".next.stepNext");
	By areaCodeTab				= By.cssSelector(".number-area-code");
	By addSmartNoList			= By.cssSelector(".new-numbers-list .add");
	By labelTab					= By.cssSelector(".number-label");
	By addedSmartNumber			= By.cssSelector(".number-types-list td.number");
	By saveBtn					= By.cssSelector(".btn-success[href='#']");
	By saveBtnNumberPage		= By.cssSelector(".btn-success.save-number");
	
	By smartNumberDeleteButton	= By.cssSelector("button.delete-number");
	By smartNumberUndeleteBtn	= By.cssSelector(".undelete-number");
	
	// call flow locators
	By addCallFlowIcon 			= By.cssSelector(".call-flow-container .glyphicon-plus-sign");
	By addSalesForceCampIcon	= By.cssSelector(".campaign-view .glyphicon-plus-sign");
	By addCallScriptsIcon		= By.cssSelector(".call-script-container .glyphicon-plus-sign");
	
	By addcallFlowBox			= By.cssSelector(".add-call-flow .selectize-control.selector.single");
	By addcallFlowTextBox 		= By.cssSelector(".add-call-flow .selectize-control.selector.single input");
	By addcallFlowDropDownItems = By.cssSelector(".add-call-flow .selectize-control.selector.single .option");
	
	By addSalesForceBox 		= By.cssSelector(".campaign-container .selectize-control.selector.single");
	By addSalesForceTextBox 	= By.cssSelector(".campaign-container .selectize-control.selector.single input");
	By addSalesForceDropDownItems 	= By.cssSelector(".campaign-container .selectize-control.selector.single .option");
	
	By callScriptName			= By.cssSelector(".call-script-container .call-script td.name");
	By addCallScriptsBox 		= By.cssSelector(".call-script-container .selectize-control.selector.single");
	By addCallScriptTextBox 	= By.cssSelector(".call-script-container .selectize-control.selector.single input");
	By addCallScriptDropDownItems 	= By.cssSelector(".call-script-container .selectize-control.selector.single .option");
	By addCallSaveBtn			= By.cssSelector(".add-call-script .btn-success.save");
	
	By confirmDeleteBtn 		= By.cssSelector("[data-bb-handler='confirm']");
	By saveBtnFlow 				= By.cssSelector(".btn-success.save");

	By descriptionBox 			= By.cssSelector(".number-description");
	By tagsInputBox				= By.cssSelector(".tags.multi input");
	By tagsSelect    			= By.cssSelector(".tags.multi .selectize-dropdown-content div");
	By tagItem					= By.cssSelector(".tags.multi .item");
	By saveNumberBtn			= By.cssSelector(".btn.btn-success.save-number");
	By saveNumberMessage		= By.className("toast-message");
	By toastMsgCloseBtn			= By.cssSelector(".toast-close-button");
	
	By createNewSFCampaign		= By.xpath(".//*[@class='add' and text()='create a new Salesforce Campaign.']");
	By campaignNameInputTab		= By.cssSelector("input.campaignName");
	By campaignDeleteField		= By.cssSelector(".campaign-container .glyphicon-remove-sign");
	By callFlowDeleteField		= By.cssSelector(".call-flow .glyphicon-remove-sign");
	
	String typeNumber						= ".//*[text()='$number$']/ancestor::tr//td[contains(@class,'type')]";
	String typeCallFlow						= ".//*[text()='$number$']/ancestor::tr//td//a[contains(@href,'#call-flows/')]";
	String smartNumberLocator               = ".//*[contains(@href,'smart-numbers') and text() = '$smartNumber$']";
	String callFlowNamePage 				= ".//*[text() = '$$callFlowName$$']/ancestor::tr//a[contains(@href,'#call-flows')]";
	String campaign_callScriptNamePage		= ".//*[contains(text(), '$$campaign_callScriptName$$')]/ancestor::tr/td[contains(@class,'name')]";
	String deleteNamePage					= ".//*[text() = '$$recordName$$']/ancestor::tr//span[contains(@class,'remove-sign')]";
	
	static final String campaignMsg			= "You have insufficient privileges to create a campaign in Salesforce. Please contact your Salesforce administrator to resolve this issue.";
	
	public static enum smartNumberStatus{
		Active,
		Inactive,
		Deleted
	}
	
	public static enum smartNumberType{
		Default,
		Tracking,
		LocalPresence
	}
	
	public static enum SmartNumberFields{
		Campaign,
		CallFlows,
		CallScripts
	}
	
	public void verifySmartNumbersTitle(WebDriver driver){
		waitUntilVisible(driver, pageTitle);
		assertEquals(getElementsText(driver, pageTitle), "Smart Numbers");
	}
	
	public void searchSmartNumber(WebDriver driver, String smartNumber){
		waitUntilVisible(driver, smartNumberTextBox);
		enterText(driver, smartNumberTextBox, smartNumber);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void searchSmartNumberByUserName(WebDriver driver, String userName){
		waitUntilVisible(driver, smartNumberTextBox);
		enterText(driver, smartNumberTextBox, userName);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void searchSmartNumberByAccount(WebDriver driver, String accountName){
		waitUntilVisible(driver, accountNameTextBox);
		enterText(driver, accountNameTextBox, accountName);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public int getSmartNumbersIndex(WebDriver driver, String Name, String Number){
		List<WebElement> smartNoNamesList = getElements(driver, searchedTableNames);
		List<WebElement> smartNosList	= getElements(driver, searchedTableSmartNos);
		int index = 0;
		for (WebElement smartNo : smartNosList) {
			if (smartNo.getText().equals(Number)
					&& getElementsText(driver, smartNoNamesList.get(index)).equals(Name)
					&& getStatus(driver, index).equals(smartNumberStatus.Active.toString())) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public int getAllSmartNumbersIndex(WebDriver driver, String Number){
		List<WebElement> smartNosList	= getElements(driver, searchedTableSmartNos);
		int index = 0;
		for (WebElement smartNo : smartNosList) {
			if (smartNo.getText().contains(Number)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public int getSmartNumbersIndex(WebDriver driver, String Number){
		List<WebElement> smartNosList	= getElements(driver, searchedTableSmartNos);
		int index = 0;
		for (WebElement smartNo : smartNosList) {
			if (smartNo.getText().contains(Number)
					&& getStatus(driver, index).equals(smartNumberStatus.Active.toString())) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public String getTypeDetail(WebDriver driver){
		waitUntilVisible(driver, typeDetail);
		return getElementsText(driver, typeDetail);
	}
	
	public String getInsertionValue(WebDriver driver){
		waitUntilVisible(driver, insertionValue);
		return getAttribue(driver, insertionValue, ElementAttributes.value);
	}
	
	public String getExtraInfo(WebDriver driver){
		waitUntilVisible(driver, extraInfo);
		return getAttribue(driver, extraInfo, ElementAttributes.value);
	}
	
	public void verifySmartNumberDetails(WebDriver driver, String number, Enum<?> type, String account, String userName){
		assertEquals(getElementsText(driver, numberDetail), number);
		assertEquals(getElementsText(driver, typeDetail), type.toString());
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, dateCreated)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, dateLastUsed)));
		assertTrue(Strings.isNotNullAndNotEmpty(getDeletedValue(driver)));
		assertEquals(getElementsText(driver, accountLink), account);
		assertEquals(getElementsText(driver, nameLink), userName);
	}
	
	
	public String getStatus(WebDriver driver, int index){
		return getElements(driver, searchedTableStatus).get(index).getText();
	}
	
	public String getUserName(WebDriver driver, int index){
		return getElements(driver, searchedTableNames).get(index).getText();
	}
	
	public String getAccountName(WebDriver driver, int index){
		return getElements(driver, searchedTableAccountList).get(index).getText();
	}
	
	public boolean verifyAccountNameList(WebDriver driver, String accountName){
		return isListContainsText(driver, getElements(driver, searchedTableAccountList), accountName);
	}
	
	public String getTypeFromTable(WebDriver driver, int index){
		return getElements(driver, serachedTableType).get(index).getText();
	}
	
	public String clickSmartNoByIndex(WebDriver driver, int index){
		String smartNumber = getTextListFromElements(driver, searchedTableSmartNos).get(index);
		getElements(driver, searchedTableSmartNos).get(index).click();
		return smartNumber;
	}
	
	public void clickSmartNumber(WebDriver driver, String number) {
		By smartNumberLoc = By.xpath(smartNumberLocator.replace("$smartNumber$", number));
		waitUntilVisible(driver, smartNumberLoc);
		clickElement(driver, smartNumberLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteSmartNumber(WebDriver driver){
		idleWait(1);
		waitUntilVisible(driver, smartNumberDeleteButton);             
		clickElement(driver, smartNumberDeleteButton);
		waitUntilInvisible(driver, smartNumberDeleteButton);
	}
	
	public boolean isdeleteSmartNumberBtnVisible(WebDriver driver){
		idleWait(2);
		return isElementVisible(driver, smartNumberDeleteButton, 0);             
	}

	public void deleteSmartNumberComplete(WebDriver driver, int smartNoIndex) {
		idleWait(1);
		clickSmartNoByIndex(driver, smartNoIndex);
		deleteSmartNumber(driver);
	}
	
	public void verifyDeletedSmartNumberColor(WebDriver driver, String number) {
		By smartNumberLoc = By.xpath(smartNumberLocator.replace("$smartNumber$", number));
		String bckgroundColor = getCssValue(driver, findElement(driver, smartNumberLoc), CssValues.Color);
		String bckgroundHexColor = Color.fromString(bckgroundColor).asHex();
		assertEquals(bckgroundHexColor, "#000000", "color does not match");
	}
	
	public String addNewSmartNumber(WebDriver driver, String areaCode, String labelName) {
		waitUntilVisible(driver, creatingRadioBtn);
		clickElement(driver, creatingRadioBtn);
		clickElement(driver, nextBtn);
		waitUntilVisible(driver, areaCodeTab);
		enterText(driver, areaCodeTab, areaCode);
		clickElement(driver, nextBtn);
		getElements(driver, addSmartNoList).get(0).click();
		clickElement(driver, nextBtn);
		enterLabel(driver, labelName);
		String smartNumber = getElementsText(driver, addedSmartNumber); 
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		return smartNumber;
	}
	
	public void createCallFlow(WebDriver driver, String callFlowName) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addCallFlowIcon);
		clickElement(driver, addCallFlowIcon);
		enterTextAndSelectFromDropDown(driver, addcallFlowBox, addcallFlowTextBox, addcallFlowDropDownItems, callFlowName);
		waitUntilVisible(driver, saveBtnFlow);
		clickElement(driver, saveBtnFlow);
	}

	public boolean isCallFlowPresent(WebDriver driver, String callFlowName) {
		By callFlowLoc = By.xpath(callFlowNamePage.replace("$$callFlowName$$", callFlowName));
		return isElementVisible(driver, callFlowLoc, 7);
	}
	
	public boolean isCallFlowAddIconDisabled(WebDriver driver) {
		return findElement(driver, addCallFlowIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled");
	}
	
	public String getLabelName(WebDriver driver) {
		return getAttribue(driver, labelTab, ElementAttributes.value);
	}
	
	public void enterLabel(WebDriver driver, String labelName) {
		waitUntilVisible(driver, labelTab);
		enterText(driver, labelTab, labelName);	
	}
	
	public void enterDescription(WebDriver driver, String description) {
		waitUntilVisible(driver, descriptionBox);
		enterText(driver, descriptionBox, description);	
	}
	
	public void enterTag(WebDriver driver, String tag) {
		waitUntilVisible(driver, tagsInputBox);
		clearAll(driver, tagsInputBox);
		enterText(driver, tagsInputBox, tag);
		idleWait(1);
		waitUntilVisible(driver, tagsSelect);
		clickElement(driver, tagsSelect);
	}
	
	public void clickSaveBtn(WebDriver driver){
		waitUntilVisible(driver, saveNumberBtn);
		clickElement(driver, saveNumberBtn);
	}
	
	public void updateTagDescrLabel(WebDriver driver, String tag, String description, String labelName) {
		enterTag(driver, tag);
		enterDescription(driver, description);
		enterLabel(driver, labelName);
		clickSaveBtn(driver);
		isSaveNumberMessageDisappeared(driver);
		idleWait(2);
	}
	
	public void verifyTagDescLabelUpdated(WebDriver driver, String tag, String description, String label) {
		assertEquals(getElementsText(driver, tagItem), tag);
		assertEquals(getAttribue(driver, descriptionBox, ElementAttributes.value), description);
		assertEquals(getAttribue(driver, labelTab, ElementAttributes.value), label);
	}
	
	public void verifyCallFlowAfterTracking(WebDriver driver, String callFlowName) {
		scrollToElement(driver, addCallFlowIcon);
		assertTrue(isCallFlowPresent(driver, callFlowName));
		assertTrue(isCallFlowAddIconDisabled(driver));
		deleteOnSmartNumberPage(driver, callFlowName);
		assertFalse(isCallFlowAddIconDisabled(driver));
		createCallFlow(driver, callFlowName);
		assertTrue(isCallFlowPresent(driver, callFlowName));
	}
	
	public void createNewSFCampaign(WebDriver driver, String campaignName){
		waitUntilVisible(driver, addSalesForceCampIcon);
		clickElement(driver, addSalesForceCampIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, createNewSFCampaign);
		clickElement(driver, createNewSFCampaign);
		waitUntilVisible(driver, campaignNameInputTab);
		enterText(driver, campaignNameInputTab, campaignName);
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		assertEquals(getToastMsg(driver), campaignMsg);
	}
	
	public boolean isToastMsgVisible(WebDriver driver){
		return isElementVisible(driver, saveNumberMessage, 5);
	}
	
	public boolean isToastMsgCloseBtnVisible(WebDriver driver){
		return isElementVisible(driver, toastMsgCloseBtn, 5);
	}
	
	public String getToastMsg(WebDriver driver){
		waitUntilVisible(driver, saveNumberMessage);
		return getElementsText(driver, saveNumberMessage);
	}
	
	public void closeToastMsg(WebDriver driver){
		waitUntilVisible(driver, toastMsgCloseBtn);
		clickElement(driver, toastMsgCloseBtn);
	}
	
	public void addSalesForceCampaign(WebDriver driver, String campaignName) {
		waitUntilVisible(driver, addSalesForceCampIcon);
		clickElement(driver, addSalesForceCampIcon);
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
		enterTextAndSelectFromDropDown(driver, addSalesForceBox, addSalesForceTextBox, addSalesForceDropDownItems, campaignName);
		if (isElementVisible(driver, confirmDeleteBtn, 7)) {
			clickElement(driver, confirmDeleteBtn);
		}
		idleWait(1);
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		isSaveNumberMessageDisappeared(driver);
	}
	
	public boolean isSalesForceCampaignVisible(WebDriver driver, String campaignName){
		By campaignNameLoc = By.xpath(campaign_callScriptNamePage.replace("$$campaign_callScriptName$$", campaignName));
		return isElementVisible(driver, campaignNameLoc, 5);
	}
	
	public void verifySalesForceCampaignAfterTracking(WebDriver driver, String campaignName) {
		assertTrue(isSalesForceCampaignVisible(driver, campaignName));
		deleteOnSmartNumberPage(driver, campaignName);
		addSalesForceCampaign(driver, campaignName);
		assertTrue(isSalesForceCampaignVisible(driver, campaignName));
		assertTrue(findElement(driver, addSalesForceCampIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled"));
	}
	
	public void deleteFields(WebDriver driver, Enum<?> enumType) {
		By deleteFieldLoc = null;
		SmartNumberFields fields = (SmartNumberFields) enumType;
		switch (fields) {
		case Campaign:
			deleteFieldLoc = campaignDeleteField;
			break;
		case CallFlows:
			deleteFieldLoc = callFlowDeleteField;
		default:
			break;
		}
		if (isElementVisible(driver, deleteFieldLoc, 5)) {
			waitUntilVisible(driver, deleteFieldLoc);
			clickElement(driver, deleteFieldLoc);
			waitUntilVisible(driver, confirmDeleteBtn);
			clickElement(driver, confirmDeleteBtn);
			waitUntilInvisible(driver, confirmDeleteBtn);
			dashboard.isPaceBarInvisible(driver);
		}
	}

	public void deleteOnSmartNumberPage(WebDriver driver, String name){
		By deleteNameLoc = By.xpath(deleteNamePage.replace("$$recordName$$", name));
		waitUntilVisible(driver, deleteNameLoc);
		clickElement(driver, deleteNameLoc);
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, saveBtnNumberPage);
		clickElement(driver, saveBtnNumberPage);
		isSaveNumberMessageDisappeared(driver);
	}
	
	public void verifyCallScriptsAfterTracking(WebDriver driver, String callScriptName) {
		scrollToElement(driver, addCallScriptsIcon);
		waitUntilVisible(driver, addCallScriptsIcon);
		dashboard.isPaceBarInvisible(driver);
		assertTrue((isElementVisible(driver, addCallScriptsIcon, 5)));
		clickElement(driver, addCallScriptsIcon);
		dashboard.isPaceBarInvisible(driver);
		enterTextAndSelectFromDropDown(driver, addCallScriptsBox, addCallScriptTextBox, addCallScriptDropDownItems, callScriptName);
		waitUntilVisible(driver, addCallSaveBtn);
		clickElement(driver, addCallSaveBtn);
		By callScriptNameLoc = By.xpath(campaign_callScriptNamePage.replace("$$campaign_callScriptName$$", callScriptName));
		assertTrue((isElementVisible(driver, callScriptNameLoc, 7)));
		assertTrue(findElement(driver, addCallScriptsIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled"));
	}
	
	public void removeCampaign(WebDriver driver, String campaignName){
		By camapignNameLoc = By.xpath(campaign_callScriptNamePage.replace("$$campaign_callScriptName$$", campaignName));
		assertTrue((isElementVisible(driver, camapignNameLoc, 7)));
		assertTrue(findElement(driver, addSalesForceCampIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled"));
		
		//removing campaign
		deleteOnSmartNumberPage(driver, campaignName);
		assertFalse((isElementVisible(driver, camapignNameLoc, 7)));
		assertFalse(findElement(driver, addSalesForceCampIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled"));
	}
	
	public void removeCallScripts(WebDriver driver, String callScript){
		By callScriptLoc = By.xpath(campaign_callScriptNamePage.replace("$$campaign_callScriptName$$", callScript));
		assertTrue((isElementVisible(driver, callScriptLoc, 7)));
		assertTrue(findElement(driver, addCallScriptsIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled"));
		
		//removing call script
		deleteOnSmartNumberPage(driver, callScript);
		assertFalse((isElementVisible(driver, callScriptLoc, 7)));
		assertFalse(isAddCallScriptsIconDisabled(driver));
	}
	
	public boolean isAddCallScriptsIconDisabled(WebDriver driver){
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addCallScriptsIcon);
		return findElement(driver, addCallScriptsIcon).findElement(By.xpath("..")).getAttribute("class").contains("disabled");
	}
	
	public String getCallScriptName(WebDriver driver){
		waitUntilVisible(driver, callScriptName);
		return getElementsText(driver, callScriptName);
	}
	
	public boolean isStatusInactive(WebDriver driver) {
		waitUntilVisible(driver, selectStatus, 6);
		return isAttributePresent(driver, selectStatus, "disabled");
	}
	
	public boolean isUndeleteBtnDisabled(WebDriver driver) {
		waitUntilVisible(driver, smartNumberUndeleteBtn, 6);
		return isAttributePresent(driver, smartNumberUndeleteBtn, "disabled");
	}
	
	public boolean isUndeleteBtnVisible(WebDriver driver) {
		return isElementVisible(driver, smartNumberUndeleteBtn, 5);
	}
	
	public void clickUndeleteBtn(WebDriver driver) {
		waitUntilVisible(driver, smartNumberUndeleteBtn);
		clickElement(driver, smartNumberUndeleteBtn);
		waitUntilInvisible(driver, smartNumberUndeleteBtn);
		waitUntilVisible(driver, smartNumberDeleteButton);
	}
	
	public void isSaveNumberMessageDisappeared(WebDriver driver){
		isElementVisible(driver, saveNumberMessage, 1);
		waitUntilInvisible(driver, saveNumberMessage);
	}
	
	public String getSIDValue(WebDriver driver) {
		return getElementsText(driver, sidText);
	}
	
	public String getDeletedValue(WebDriver driver) {
		return getElementsText(driver, deletedText);
	}
	
	public void clickAccount(WebDriver driver) {
		waitUntilVisible(driver, accountLink);
		clickElement(driver, accountLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String getNumberType(WebDriver driver, String smartNumber){
		By typeLoc = By.xpath(typeNumber.replace("$number$", smartNumber));
		waitUntilVisible(driver, typeLoc);
		return getElementsText(driver, typeLoc);
	}
	
	public boolean isNumberCallFlowEmpty(WebDriver driver, String smartNumber){
		By callFLowLoc = By.xpath(typeCallFlow.replace("$number$", smartNumber));
		return !isElementVisible(driver, callFLowLoc, 5); 
	}
	
	public String getNumberCallFlow(WebDriver driver, String smartNumber){
		By callFLowLoc = By.xpath(typeCallFlow.replace("$number$", smartNumber));
		waitUntilVisible(driver, callFLowLoc);
		return getElementsText(driver, callFLowLoc);
	}
	
	public void selectStatusOfSmartNumber(WebDriver driver, String text) {
		selectFromDropdown(driver, selectStatus, SelectTypes.visibleText, text);
	}
	
	public void selectUnassignedTypeFromDropDown(WebDriver driver) {
		selectFromDropdown(driver, selectTypeDropDown, SelectTypes.visibleText, "Unassigned");
	}

	public void clickSearchButton(WebDriver driver) {
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
	}

	public String getTotalRecords(WebDriver driver) {
		waitUntilVisible(driver, totalRecords);
		return findElement(driver, totalRecords).getText();

	}
	
	public List<String> getStatusList(WebDriver driver){
		return getTextListFromElements(driver, searchedTableStatus);
	}
	
	/**
	 * @Desc: click Status header
	 * @param driver
	 */
	public void clickStatusHeader(WebDriver driver){
		waitUntilVisible(driver, statusHeader);
		clickElement(driver, statusHeader);
	}
	
}
