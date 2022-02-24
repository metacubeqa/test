package support.source.system;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Strings;

import base.SeleniumBase;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class SystemPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	By chooseFile 		  = By.cssSelector(".glyphicon-folder-open");
	By hardDeleteCheckBox = By.cssSelector("[type='checkbox'][name='hardDelete']");
	By uploadBtn 		  = By.cssSelector(".upload");
	By toastMsg 		  = By.cssSelector(".toast-message");
	By closeWindow		  = By.cssSelector(".close span");
	By accountDetailHead  = By.xpath("//h2[text() = 'Account Details']");
	By usersHeading       = By.xpath("//h2[text() = 'Users ']");
	By smartNumberHead    = By.xpath("//h2[text() = 'Smart Numbers ']");
	
	String deleteStatusNumberPage = ".//*[text()='$number$']/ancestor::div[@class='row number-item']//div[contains(@class,'status')]";
	
	//Number transfer selectors
	By phoneNumberInputBox  = By.cssSelector("input.phone-number");
	By userNameInputBox     = By.cssSelector("input[type='text'][placeholder='User name']");
	By userNameDropDown		= By.cssSelector(".user-picker .selectize-dropdown-content div");
	By accountNameInputBox  = By.cssSelector("input[placeholder='Account name']");
	By accountNameDropDown	= By.cssSelector(".account-picker .selectize-dropdown-content div");
	By userNameTable        = By.xpath(".//*[@class='number-transfer']//a[contains(@href, 'users') and string-length(text()) > 0]");
	By accountsTable        = By.xpath(".//*[@class='number-transfer']//a[contains(@href, 'accounts') and string-length(text()) > 0]");
	By numbersTable			= By.xpath(".//*[@class='number-transfer']//a[contains(@href, 'numbers') and string-length(text()) > 0]");
	By searchBtn            = By.cssSelector(".btn-success.search");
	By selectNewAccountBox  = By.cssSelector("#transfer-number-modal input[placeholder='Account name']");
	By accountDropDownItems = By.cssSelector("#transfer-number-modal .selectize-dropdown-content div");
	By entityNameBox		= By.cssSelector(".entity-name");
	By callFlowDropDownItems  = By.cssSelector(".callflow-picker .selectize-dropdown-content div");
	By callFlowTextBox		= By.cssSelector(".callflow-picker input");
	By nextBtn				= By.cssSelector(".btn-success.next");
	By selectTransferType	= By.cssSelector(".type-selector .type");
	By ownerNameBox			= By.cssSelector("#transfer-number-modal .user-picker input");
	By ownerNameContent		= By.cssSelector("#transfer-number-modal .user-picker .selectize-dropdown-content div");
	By transferBtn			= By.cssSelector(".btn-success.transfer");
	
	String editBtn		    		= ".//*[text()='$number$']/ancestor::tr//a[@class='edit']/span";
	String typeTransferNumber		= ".//*[text()='$number$']/ancestor::tr//td[contains(@class,'type')]";
	String accountTransferNumber	= ".//*[text()='$number$']/ancestor::tr//a[contains(@href,'accounts')]";
	String userTransferNumber		= ".//*[text()='$number$']/ancestor::tr//a[contains(@href,'users')]";
	
	//Number inspector selectors
	By numberInspectorBox    = By.cssSelector(".phone-number.form-control");
	By findBtn				 = By.cssSelector(".btn-success.go");
	By frwrdingNumberSection = By.xpath(".//h2[text()='Forwarding Number']");
	By outboundNumberSection = By.xpath(".//h2[text()='Outbound Number']");
	By callNotificationSection = By.xpath(".//h2[text()='Call Notification Number']");
	By usersLink			 = By.xpath("//label[text()='Users']/..//span//a");
	
	//DomainBlacklist
	By addDomainBtn			 = By.cssSelector(".page-title .create");
	By domainTextBox		 = By.cssSelector(".domain-blacklist-item input");
	By domainSaveCancel		 = By.cssSelector(".cancel");
	By domainSave			 = By.cssSelector(".save");
	By domainDelete			 = By.cssSelector(".delete");
	By domainTextBoxDisabled = By.cssSelector(".domain-blacklist-item input:disabled");
	
	//CleanupRecording
	By cleanUpRecording					= By.cssSelector("[href='#system/recordings']");
	By cleanUpAccountFilterInput 		= By.cssSelector(".account-picker input");
	By cleanUpAccountSelected 			= By.cssSelector(".account-picker .item");
	By cleanUpAccountFilterValues 		= By.cssSelector(".libraries.single .selectize-dropdown-content div");
	By getAccountInfoBtn				= By.cssSelector(".info");
	By cleanUpRecordingBtn				= By.cssSelector(".cleanup");
	By recordingCount					= By.xpath("//label[text()='Recordings Count']/..//span");
	By noteSaveMessage 					= By.cssSelector(".toast-message");
	By confirmMessage					= By.xpath("//*[contains(text(), 'Finish cleanup')]");
	
	//Number delete 
	By deleteNumberInputTab				= By.cssSelector(".bulk-number-deleter .input-group input");
	
	//split account
	By splitAccountInputTab				= By.cssSelector(".split-account-overview .account-picker input");
	By splitAccountDropdown				= By.cssSelector(".split-account-overview .account-picker .selectize-dropdown-content div");
	By splitGetDataBtn					= By.cssSelector("button.btn-info.get-data");
	By splitMoveDataBtn					= By.cssSelector("button.btn-info.split");
	By startedSplitting					= By.xpath(".//pre[@class='results']/p[@class='ok' and contains(text(),'Started splitting')]");
	By finishedSplitting				= By.xpath(".//pre[@class='results']/p[@class='ok' and contains(text(),'Finished splitting')]");
	By salesforceIdInput                = By.cssSelector(".form-control.sfOrgId");
	String userSplitData				= ".//td[contains(@class,'displayName') and text()='$userName$']//parent::tr//input";
	
	//Service user
	By accountID                        = By.xpath("//div[contains(text(),'Account')]/following-sibling::div");
	By salesforceID                     = By.xpath("//div[contains(text(),'Salesforce')]/following-sibling::div");
	By apiKey                           = By.xpath("//div[contains(text(),'API Key')]/following-sibling::div");
	By apiSecret                        = By.xpath("//div[contains(text(),'API Secret')]/following-sibling::div");
	
	//Downtime event
	By eventReasonList                  = By.cssSelector(".string-cell.sortable.renderable.reason");
	By addDowntimeEvent                 = By.cssSelector(".glyphicon.glyphicon-plus-sign");
	By endDateField                     = By.cssSelector("input.end");
	String selectDate                   = ("//td[@data-day='$Date$']");
	By downtimeReasonDropDown           = By.cssSelector(".form-control.reason");
	By eventAddButton                   = By.cssSelector(".btn.btn-success.save-downtime-event");
	By eventCloseButton                 = By.cssSelector(".btn.btn-default");
	By deleteDowntimeEvent              = By.cssSelector(".glyphicon.glyphicon-remove-sign");
	By confirmDeleteButton              = By.xpath("//button[@data-bb-handler='confirm']");
	
	//Deployments
	By githubLinksList                  = By.xpath("//a[contains(@title ,'https://github.com/ringdna/softphone')]");
	
	//sip routing
	By addSipRouting                    = By.xpath("//button[contains(@class, 'add')]");
	By domainName                       = By.cssSelector("#domain");
	By userName                         = By.cssSelector("#username");
	By saveButton                       = By.xpath("//button[contains(@class, 'rdna-button contained ')]");
	
	String editSipRouting               = "//div[text()='%text%']/preceding-sibling::div/button[contains(@class,'edit')]";
	String deleteSipRouting             = "//div[text()='%text%']/following-sibling::div/button[contains(@class,'delete')]";
	By confirmOkButton                  = By.xpath("//button[contains(@class,'rdna-button contained')]");
	
	public void clickChooseFile(WebDriver driver) {
		waitUntilVisible(driver, chooseFile);
		clickElement(driver, chooseFile);
	}
	
	public void selectSoftDeleteCheckBox(WebDriver driver) {
		if(findElement(driver, hardDeleteCheckBox).isSelected()) {
			clickElement(driver, hardDeleteCheckBox);
		}
	}

	public void selectHardDeleteCheckBox(WebDriver driver) {
		if(!findElement(driver, hardDeleteCheckBox).isSelected()) {
			clickElement(driver, hardDeleteCheckBox);
		}
	}
	
	public void clickUploadBtn(WebDriver driver) {
		waitUntilVisible(driver, uploadBtn);
		clickElement(driver, uploadBtn);
	}
	
	public void importCompletedMsgVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, toastMsg, 5));
		assertTrue(getElementsText(driver, toastMsg).equals("Import completed"));
		waitUntilInvisible(driver, toastMsg);
	}
	
	public void uploadSoftDeleteNumber(WebDriver driver, String pathOFCSV, String number) {
		clickChooseFile(driver);
		idleWait(2);
		HelperFunctions.uploadCSV(pathOFCSV);
		idleWait(3);
		waitForElementAttributeContainsString(driver, deleteNumberInputTab, ElementAttributes.value, "csv");
		selectSoftDeleteCheckBox(driver);
		clickUploadBtn(driver);
		importCompletedMsgVisible(driver);
		numberSoftDeleted(driver, number);
	}
	
	public void uploadHardDeleteNumber(WebDriver driver, String pathOFCSV, String number) {
		clickChooseFile(driver);
		idleWait(2);
		HelperFunctions.uploadCSV(pathOFCSV);
		idleWait(3);
		waitForElementAttributeContainsString(driver, deleteNumberInputTab, ElementAttributes.value, "csv");
		selectHardDeleteCheckBox(driver);
		clickUploadBtn(driver);
		importCompletedMsgVisible(driver);
		numberHardDeleted(driver, number);
	}
	
	public void numberSoftDeleted(WebDriver driver, String number) {
		By numberDeleteStatus = By.xpath(deleteStatusNumberPage.replace("$number$", number));
		assertTrue(getElementsText(driver, numberDeleteStatus).equals("Soft Deleted"));
	}
	
	public void numberHardDeleted(WebDriver driver, String number) {
		By numberDeleteStatus = By.xpath(deleteStatusNumberPage.replace("$number$", number));
		assertTrue(getElementsText(driver, numberDeleteStatus).equals("Hard Deleted"));
	}
	
	//Number transfer section
	
	public void enterUserName(WebDriver driver, String userName){
		enterTextAndSelectFromDropDown(driver, userNameInputBox, userNameInputBox, userNameDropDown, userName);
	}
	
	public void enterAccountName(WebDriver driver, String accountName){
		enterTextAndSelectFromDropDown(driver, accountNameInputBox, accountNameInputBox, accountNameDropDown, accountName);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void search_TransferSmartNumber(WebDriver driver, String smartNumber, String accountName, Enum<?> type, String ownerName){
		enterTransferPhoneNumber(driver, smartNumber);
		clickSearchBtn(driver);
		clickEditBtn(driver, smartNumber);
		selectNewAccount(driver, accountName);
		clickNextBtn(driver);
		selectTransferType(driver, type.name());
		if(type.name().equals(Type.Tracking.name())){
			enterEntityName(driver, "Tracking");
			selectCallFlow(driver, 0);
		}
		else{
			enterOwnerName(driver, ownerName);
		}
		clickTransferBtn(driver);
	}
	
	public void enterEntityName(WebDriver driver, String entity){
		waitUntilVisible(driver, entityNameBox);
		enterText(driver, entityNameBox, entity);
	}
	
	public void selectCallFlow(WebDriver driver, int index){
		waitUntilVisible(driver, callFlowTextBox);
		clickElement(driver, callFlowTextBox);
		clickElement(driver, getElements(driver, callFlowDropDownItems).get(index));
	}
	
	public String getTransferType(WebDriver driver, String smartNumber){
		By typeLoc = By.xpath(typeTransferNumber.replace("$number$", smartNumber));
		waitUntilVisible(driver, typeLoc);
		return getElementsText(driver, typeLoc);
	}
	
	public String getTransferAccount(WebDriver driver, String smartNumber){
		By accountLoc = By.xpath(accountTransferNumber.replace("$number$", smartNumber));
		waitUntilVisible(driver, accountLoc);
		return getElementsText(driver, accountLoc);
	}
	
	public String getTransferUser(WebDriver driver, String smartNumber){
		By userLoc = By.xpath(userTransferNumber.replace("$number$", smartNumber));
		return getElementsText(driver, userLoc);
	}
	
	public void verifyTransferedNumber(WebDriver driver, String smartNumber, String type, String account, String userName){
		dashboard.isPaceBarInvisible(driver);
		enterTransferPhoneNumber(driver, smartNumber);
		clickSearchBtn(driver);
		assertEquals(getTransferType(driver, smartNumber), type);
		assertEquals(getTransferAccount(driver, smartNumber), account);
		if(getTransferType(driver, smartNumber).equals("AdWords") || getTransferType(driver, smartNumber).equals("Tracking")){
			assertTrue(Strings.isNullOrEmpty(getTransferUser(driver, smartNumber)));
		}
		else{
			assertEquals(getTransferUser(driver, smartNumber), userName);
		}
	}
	
	public void verifyErrorTransfer(WebDriver driver){
		waitUntilVisible(driver, toastMsg);
		assertEquals(getElementsText(driver, toastMsg), "Cannot Transfer Smart Number To The Same Account");
	}
	
	public void closeWindow(WebDriver driver){
		waitUntilVisible(driver, closeWindow);
		clickElement(driver, closeWindow);
	}
	
	public void enterTransferPhoneNumber(WebDriver driver, String phoneNumber) {
		waitUntilVisible(driver, phoneNumberInputBox);
		idleWait(1);
		enterText(driver, phoneNumberInputBox, phoneNumber);
	}

	public String getUserNameFromTable(WebDriver driver) {
		waitUntiTextAppearsInListElements(driver, userNameTable);
		return getElementsText(driver, getElements(driver, userNameTable).get(0));
	}
	
	public String getNumberFromTable(WebDriver driver) {
		waitUntiTextAppearsInListElements(driver, numbersTable);
		return getElementsText(driver, getElements(driver, numbersTable).get(0));
	}
	
	public String getAccountFromTable(WebDriver driver) {
		waitUntiTextAppearsInListElements(driver, accountsTable);
		return getElementsText(driver, getElements(driver, accountsTable).get(0));
	}
	
	public void verifyUserSearchResult(WebDriver driver, String userName) {
		assertEquals(userName, getUserNameFromTable(driver));
	}
	
	public void verifyNumberSearchResult(WebDriver driver, String number) {
		assertEquals(number, getNumberFromTable(driver));
	}
	
	public void verifyAccountSearchResult(WebDriver driver, String account) {
		assertEquals(account, getAccountFromTable(driver));
	}
	
	public void clickSearchBtn(WebDriver driver) {
		waitUntilVisible(driver, searchBtn);
		clickElement(driver, searchBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isNumberVisible(WebDriver driver, String number) {
		By phoneNumberLoc = By.xpath(editBtn.replace("$number$", number));
		return isElementVisible(driver, phoneNumberLoc, 5);
	}
	
	public void clickEditBtn(WebDriver driver, String number) {
		By editBtnLoc = By.xpath(editBtn.replace("$number$", number));
		waitUntilVisible(driver, editBtnLoc);
		clickElement(driver, editBtnLoc);
	}
	
	public void selectNewAccount(WebDriver driver, String accountName) {
		enterTextAndSelectFromDropDown(driver, selectNewAccountBox, selectNewAccountBox, accountDropDownItems, accountName);
	}
	
	public void clickNextBtn(WebDriver driver) {
		waitUntilVisible(driver, nextBtn);
		clickElement(driver, nextBtn);
	}
	
	public void selectTransferType(WebDriver driver, String type) {
		selectFromDropdown(driver, selectTransferType, SelectTypes.visibleText, type);
	}
	
	public void enterOwnerName(WebDriver driver, String ownerName) {
		enterTextandSelect(driver, ownerNameBox, ownerName);
	}
	
	public void clickTransferBtn(WebDriver driver) {
		waitUntilVisible(driver, transferBtn);
		clickElement(driver, transferBtn);
	}
	
	//Number Inspector section
	public void isNumberInspectorSectionsVisible(WebDriver driver) {
		assertTrue(isListElementsVisible(driver, frwrdingNumberSection, 5));
		assertTrue(isListElementsVisible(driver, outboundNumberSection, 5));
		assertTrue(isListElementsVisible(driver, callNotificationSection, 5));
	}
	
	public void searchNumberInspector(WebDriver driver, String number) {
		enterText(driver, phoneNumberInputBox, number);
		clickFindBtn(driver);
	}
	
	public void clickFindBtn(WebDriver driver) {
		waitUntilVisible(driver, findBtn);
		clickElement(driver, findBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickUsersLink(WebDriver driver){
		waitUntilVisible(driver, usersLink);
		clickElement(driver, usersLink);
	}
	
	public boolean isUsersLinkEmpty(WebDriver driver, String userName){
		if(getTextListFromElements(driver, usersLink) == null){
			return true;
		}
		return !isTextPresentInStringList(driver, getTextListFromElements(driver, usersLink), userName);
	}
	
	//------------------------Methods for Domain Blacklist Starts here------------------------------

	// Method to click Cleanup Recording section
	public void clickAddDomain(WebDriver driver) {
		waitUntilVisible(driver, addDomainBtn);
		clickElement(driver, addDomainBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, domainTextBox);
	}

	// Method to enter domain
	public void enterDomain(WebDriver driver) {
		String notes = HelperFunctions.GetRandomString(5).concat(" ").concat(HelperFunctions.GetRandomString(3));
		enterText(driver, domainTextBox, notes);
		clickElement(driver, domainSave);
		waitUntilVisible(driver, domainDelete);
		assertTrue(isElementVisible(driver, domainTextBoxDisabled, 2));
	}

	// Method to Delete domain
	public void deleteDomain(WebDriver driver) {
		clickElement(driver, domainDelete);
		// waitUntilVisible(driver, domainDelete);
		acceptAlert(driver);
	}
	
	//------------------------Methods for Domain Blacklist ends here------------------------------
	
	//------------------------Methods for Clean up recordings Starts here------------------------------
	
	public void setAccountForCleanUpRecording(WebDriver driver, String accountName) {
		waitUntilVisible(driver, cleanUpAccountFilterInput);
		clickElement(driver, cleanUpAccountFilterInput);
		idleWait(1);
		enterText(driver, cleanUpAccountFilterInput, accountName);
		idleWait(1);
		clickEnter(driver, cleanUpAccountFilterInput);
		idleWait(1);
		clickElement(driver, getAccountInfoBtn);
		dashboard.isPaceBarInvisible(driver);
	}

	// Method to get recording count
	public String getRecordingCount(WebDriver driver) {
		return getElementsText(driver, recordingCount);
	}

	// Method to click Cleanup Recording section
	public void clickCleanUpRecordingBtn(WebDriver driver) {
		waitUntilVisible(driver, cleanUpRecordingBtn);
		clickElement(driver, cleanUpRecordingBtn);
		waitUntilVisible(driver, noteSaveMessage);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, confirmMessage);
	}
	
	//------------------------Methods for Clean up recordings ends here------------------------------//
	
	//------------------------Methods for Split Account Starts here------------------------------
	
	public void selectAccountSplitData(WebDriver driver, String accountName){
		enterTextAndSelectFromDropDown(driver, splitAccountInputTab, splitAccountInputTab, splitAccountDropdown, accountName);
	}
	
	public boolean isUserPresentForSplitData(WebDriver driver, String userName){
		By userLoc = By.xpath(userSplitData.replace("$userName$", userName));
		return isElementVisible(driver, userLoc, 5);	
	}
	
	public void selectUserForSplitData(WebDriver driver, String userName){
		By userLoc = By.xpath(userSplitData.replace("$userName$", userName));
		waitUntilVisible(driver, userLoc);
		checkCheckBox(driver, userLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickSplitGetDataBtn(WebDriver driver){
		waitUntilVisible(driver, splitGetDataBtn);
		clickElement(driver, splitGetDataBtn);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickMoveDataSplitBtn(WebDriver driver){
		waitUntilVisible(driver, splitMoveDataBtn);
		scrollIntoView(driver, splitMoveDataBtn);
		clickElement(driver, splitMoveDataBtn);
		waitUntilVisible(driver, toastMsg);
		assertTrue(getElementsText(driver, toastMsg).equals("Start splitting/moving."));
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, startedSplitting);
		waitUntilVisible(driver, finishedSplitting);
	}
	
	//service user
	
	public String getAccountId(WebDriver driver) {
		waitUntilVisible(driver, accountID);
		return findElement(driver, accountID).getText();
	}

	public String getSalesforceId(WebDriver driver) {
		waitUntilVisible(driver, salesforceID);
		return findElement(driver, salesforceID).getText();
	}
	
	public String getApiKey(WebDriver driver) {
		waitUntilVisible(driver, apiKey);
		return findElement(driver, apiKey).getText();
	}
	
	public String getApiSecret(WebDriver driver) {
		waitUntilVisible(driver, apiSecret);
		return findElement(driver, apiSecret).getText();
	}
	
	public void selectServiceUserNewAccount(WebDriver driver, String accountName) {
		enterTextAndSelectFromDropDown(driver, selectNewAccountBox, selectNewAccountBox, accountDropDownItems, accountName);
	}

	//DownTime Events
	
	/**
	 * @author Pranshu
	 * @Name Downtime reasons
	 */
	public enum DowntimeReason {
		ScheduledMaintenance("Scheduled Maintenance"),
		ScheduledDowntime("Scheduled Downtime"),
		UnscheduledDowntime("Unscheduled Downtime"),
		AWSDowntime("AWS Downtime"),
		TwilioDowntime("Twilio Downtime");
	
	    private String displayName;

	    DowntimeReason(String displayName) {
	        this.displayName = displayName;
	    }
		    
	    public String displayName() { return displayName; }

	    @Override 
	    public String toString() { return displayName; }

	};
	
	/**
	 * @param driver
	 * @param reason
	 * @Desc: add downTime event
	 */
	public void addDowntimeEvent(WebDriver driver, DowntimeReason reason) {
		clickElement(driver, addDowntimeEvent);
		selectDowntimeEventReason(driver, reason);
		
		String  currentDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", currentDate, 1, 0, 0);
		By locator = By.xpath(selectDate.replace("$Date$", endDate));
		clickElement(driver, endDateField);
		clickElement(driver, locator);
		
		clickElement(driver, eventAddButton);
		
	}
	
	/**
	 * @param driver
	 * @param reason
	 * @Desc: select downtime event reason from Drop Down
	 */
	public void selectDowntimeEventReason(WebDriver driver, DowntimeReason reason ) {
		clickElement(driver, downtimeReasonDropDown);
		selectFromDropdown(driver, downtimeReasonDropDown, SelectTypes.visibleText, reason.displayName());
	}
	
	/**
	 * @param driver
	 * @Desc: verify timedown reason drop down options
	 */
	public void verifyDownTimeReasonDropDownText(WebDriver driver) {
		waitUntilVisible(driver, addDowntimeEvent);
		clickByJs(driver, addDowntimeEvent);
		waitUntilVisible(driver, downtimeReasonDropDown);
		clickElement(driver, downtimeReasonDropDown);
		List<String> downTimeReason = getElementsTextFromDropDownList(driver, downtimeReasonDropDown);
		assertEquals(downTimeReason.get(0), "Scheduled Maintenance");
		assertEquals(downTimeReason.get(1), "Scheduled Downtime");
		assertEquals(downTimeReason.get(2), "Unscheduled Downtime");
		assertEquals(downTimeReason.get(3), "AWS Downtime");
		assertEquals(downTimeReason.get(4), "Twilio Downtime");
		clickElement(driver, eventCloseButton);
	}
	
	/**
	 * @param driver
	 * @Desc: delete downTime Event
	 */
	public void deleteDownTimeEvent(WebDriver driver) {
		List<WebElement> events = getElements(driver, deleteDowntimeEvent);
		clickByJs(driver, events.get(0));
		clickElement(driver, confirmDeleteButton);
	}
	
	/**
	 * @param driver
	 * @return String
	 */
	public String verifyCreatedEventReason(WebDriver driver) {
		List<String> events = getTextListFromElements(driver, eventReasonList);
		return events.get(0);
	}
	
	
	//deployments
	
	/**
	 * @param driver
	 * verify github links
	 */
	public void verifyGithubLinks(WebDriver driver) {
		waitUntilVisible(driver, githubLinksList);
		List<WebElement> events = getElements(driver, githubLinksList);
		clickElement(driver, events.get(0));
		assertEquals(getTabCount(driver), 3);
	}
	
	//sip routing 
	
	public void addSipRouting(WebDriver driver, String name) {
		waitUntilVisible(driver, addSipRouting);
		clickElement(driver, addSipRouting);
		waitUntilVisible(driver, domainName);
		enterText(driver, domainName, name);
		waitUntilVisible(driver, userName);
		enterText(driver, userName, name);
		waitUntilVisible(driver, saveButton);
		clickElement(driver, saveButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void editSipRouting(WebDriver driver, String name, String newName) {
		By locator = By.xpath(editSipRouting.replace("%text%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		
		waitUntilVisible(driver, domainName);
		enterText(driver, domainName, newName);
		waitUntilVisible(driver, userName);
		enterText(driver, userName, newName);
		waitUntilVisible(driver, saveButton);
		clickElement(driver, saveButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteSipRouting(WebDriver driver, String name) {
		By locator = By.xpath(deleteSipRouting.replace("%text%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	public void clickConfirmOkButton(WebDriver driver) {
		waitUntilVisible(driver, confirmOkButton);
		clickElement(driver, confirmOkButton);	
	}	
	
	/**
	 * @param driver
	 * verify items are on split page
	 */
	public void verifySplitAccountDataItems(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		assertTrue(isElementVisible(driver, accountDetailHead, 6));
		assertTrue(isElementVisible(driver, splitAccountInputTab, 6));
		assertTrue(isElementVisible(driver, salesforceIdInput, 6));
		scrollToElement(driver, usersHeading);
		assertTrue(isElementVisible(driver, usersHeading, 6));
	}
	
}
