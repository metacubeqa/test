package support.source.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import support.source.commonpages.Dashboard;

public class SetUpTabPage extends SeleniumBase {

	SeleniumBase seleniumBase = new SeleniumBase();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();

	By setUpTab = By.cssSelector(".cai-tabs [data-tab='setup']");
	By userProfileLink = By.cssSelector(".user-profile .username");
	By setUpViaProfile = By.cssSelector(".conversation-ai-setup");
	By userDetailViaProfile = By.cssSelector(".dropdown-usermenu .user-profile-url");

	// vocabulary
	By selectedLefNavSetting 	= By.cssSelector(".setting-list .setting-item.selected .item");
	By vocabularyTextBox 		= By.cssSelector(".MuiInput-input");
	By addButton 				= By.xpath(".//button[contains(@class,'MuiButton-containedPrimary')]//span[contains(text(),'Add')]");
	By vocabluaryList 			= By.cssSelector(".MuiListItemText-root span");
	String deleteVocabluaryItem = ".//div[@class='MuiListItemText-root']//span[contains(text(),'$$vocabluary$$')]//..//..//button";
	By deleteOk 				= By.xpath(".//button[contains(@class,'MuiButton-containedPrimary')]//span[contains(text(),'Delete')]");

	// Keyword
	By keywordGroupLeftNav   = By.cssSelector("[href='#smart-recordings/setup/keyword-group']>span:nth-child(1)");
	By AddkeywordGroupButton = By.xpath(".//*[@class='MuiButton-label' and text()='Create New Group']");
	By keywordGroupTextBox  = By.cssSelector("[name='name']");
	By alertGroupNameMsg	= By.xpath(".//*[contains(@class,'MuiFormHelperText-filled') and text()='Group names must be 60 characters or less']");
	By addGroupBtn 			= By.cssSelector(".create-group");
	By keyPhraseTextBox 	= By.cssSelector(".MuiPaper-rounded .MuiOutlinedInput-inputAdornedStart");
	By createButton 		= By.xpath(".//*[contains(@class,'MuiButton-label') and text()='Create']");
	By updateButton 		= By.xpath(".//*[contains(@class,'MuiButton-label') and text()='Update']");
	By cancelButton			= By.xpath(".//*[contains(@class,'MuiButton-label') and text()='Cancel']");
	By keyPhraseAddBtn 		= By.cssSelector(".create-keyword");
	By keywordGroupList 	= By.xpath(".//*[@data-testid='keyword-group-list.keywords']//h4");
	By toastMessage			= By.className("toast-message");
	By errorMessage			= By.cssSelector(".alert-danger");
	By keywordMaxLimitMsg 	= By.xpath("//*[@class='toast-message' and contains(text(),'cannot be greater than 64')]");
	By keywordPhraseList 	= By.cssSelector(".keyword-item-name .keyword-name");
	By deleteAllKeyPhraseList = By.xpath(".//span[@class='keyword-name' ]/..//a");
	String keywordGroupCount  = ".//*[contains(@class,'MuiCardHeader-root')]//h4[contains(text(),'$$keyword$$')]/../span";
	String groupPhraseList    = ".//*[contains(@class,'MuiCardHeader-root')]//h4[contains(text(),'$$keyword$$')]/../../../../..//*[contains(@class,'MuiCardContent-root')]/div/span";
	String selectKeywordGroup = ".//*[contains(@class,'MuiCardHeader-root')]//h4[contains(text(),'$$keywordGroupName$$')]/../../../..//*[contains(@class,'action')]//button[contains(@aria-label,'edit')]";
	String deleteKeywordGroup = ".//*[contains(@class,'MuiCardHeader-root')]//h4[contains(text(),'$$keywordGroupName$$')]/../../../..//*[contains(@class,'action')]//button[contains(@aria-label,'delete')]";
	String deleteKeyPhrase    = ".//*[contains(@class,'MuiChip-deletable')]/span[contains(text(),'$$keyPhraseName$$')]/..//*[contains(@class,'MuiChip-deleteIcon')]";

	// Lead Status--Opp Status Page
	By leadStatusLeftNav 		= By.cssSelector("[data-args='Lead Status Change']");
	By oppStatusLeftNav 		= By.cssSelector("[data-args='Opportunity Status Change']");
	By oldStatusFilter 			= By.cssSelector(".selectize-control.form-control.old-status.single");
	By oldStatusFilterValues 	= By.cssSelector(".selectize-control.form-control.old-status.single .selectize-dropdown-content div");
	By oldStatusFilterInput 	= By.cssSelector(".selectize-control.form-control.old-status.single input");
	By oldStatusFiltertSelected = By.cssSelector(".selectize-control.form-control.old-status.single .has-items .item");

	By newStatusFilter 			= By.cssSelector(".selectize-control.form-control.new-status.single");
	By newStatusFilterValues 	= By.cssSelector(".selectize-control.form-control.new-status.single .selectize-dropdown-content div");
	By newStatusFilterInput	 	= By.cssSelector(".selectize-control.form-control.new-status.single input");
	By newStatusFiltertSelected = By.cssSelector(".selectize-control.form-control.new-status.single .has-items .item");
	By duplicateLeadStatus 		= By.xpath("//*[@class='toast-message' and contains(text(),'Status Change already exists')]");
	By addStatusBtn 			= By.xpath(".//*[contains(@class,'status-change') and (@type='button')]");
	String deleteGivenStatusChange = ".//tbody//td[1]//span[text()='$$oldStatus$$']/../../td[2]/span[text()='$$newStatus$$']/../../td[5]/a/span";
	String updateGivenStatusChange = ".//tbody//td[1]//span[text()='$$oldStatus$$']/../../td[2]/span[text()='$$newStatus$$']/../../td[4]/button";
	By updateoldStatusFilter 	   = By.xpath(".//tbody//td[1]/select");
	By updateoldStatusFilterValues = By.xpath(".//tbody//td[1]/select/option");
	By updatenewStatusFilter 	   = By.xpath(".//tbody//td[2]/select");
	By updatenewStatusFilterValues = By.xpath(".//tbody//td[2]/select/option");
	By changeTypeFilter 		   = By.cssSelector(".selectize-control.form-control.change-value.single");
	By changeTypeFilterValues 	   = By.cssSelector(".selectize-control.form-control.change-value.single .selectize-dropdown-content div");
	By changeTypeInput 			   = By.cssSelector(".selectize-control.form-control.change-value.single input");
	By changeTypeSelected 		   = By.cssSelector(".selectize-control.form-control.change-value.single .has-items .item");
	By updateChangeTypeFilter 		= By.xpath(".//tbody//td[3]/select");
	By updateChangeTypeFilterValues = By.xpath(".//tbody//td[3]/select/option");
	By saveUpdateBtn 				= By.cssSelector(".btn-success[data-action='save']");

	By oldStatusList 		= By.xpath(".//tbody//td[1]");
	By newStatusList 		= By.xpath(".//tbody//td[2]");
	By oldStatus 			= By.cssSelector(".oldStatus");
	By newStatus 			= By.cssSelector(".newStatus");
	
	//Notification Settings
	By inboxCheckboxList			 = By.cssSelector("input.inbox");
	By emailCheckBoxList			 = By.cssSelector("input.email");
	String emailNotificationCheckBox = ".//*[text()='$notification$']/../following-sibling::div//input[@class='email']";

	// Enum to select Lead Status option
	public static enum leadStatusOptions {
		OpenNotContacted("Open - Not Contacted"), ClosedConverted("Closed - Converted"), ClosedNotConverted(
				"Closed - Not Converted"), Open(
						"Open"), Contacted("Contacted"), Qualified("Qualified"), UnQualified("Unqualified");
		private String displayName;

		leadStatusOptions(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	// Enum to select OppStatus option
	public static enum oppStatusOptions {
		NeedsAnalysis("Needs Analysis"), ValueProposition("Value Proposition"), PerceptionAnalysis(
				"Perception Analysis");
		private String displayName;

		oppStatusOptions(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	// Enum to select change type while adding lead/opp status
	public static enum statusChangeOptions {
		Positive("positive"), Negative("negative");
		private String displayName;

		statusChangeOptions(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	// Lead Status Change method
	public void clickleadStatusLeftNav(WebDriver driver) {
		clickElement(driver, leadStatusLeftNav);
		dashboard.isPaceBarInvisible(driver);
	}

	public void deleteStatusChange(WebDriver driver, String oldLStatus, String newLStatus) {
		// Check if Status combination already exist
		By locator = By.xpath(
				deleteGivenStatusChange.replace("$$oldStatus$$", oldLStatus).replace("$$newStatus$$", newLStatus));

		if (isElementVisible(driver, locator, 1)) {
			clickElement(driver, locator);
			waitUntilVisible(driver, deleteOk);
			clickElement(driver, deleteOk);
			waitUntilVisible(driver, oldStatusFilter);
		}
	}

	// Method to Add Lead Status or Opp Status
	public void addStatusChange(WebDriver driver, String oldLStatus, String newLStatus, String changeType) {
		// Delete if combination already exists
		// deleteStatusChange(driver, oldLStatus, newLStatus);
		By locator = By.xpath(
				deleteGivenStatusChange.replace("$$oldStatus$$", oldLStatus).replace("$$newStatus$$", newLStatus));
		if (isElementVisible(driver, locator, 1)) {
			clickAndSelectFromDropDown(driver, oldStatusFilter, oldStatusFilterValues, oldLStatus);
			clickAndSelectFromDropDown(driver, newStatusFilter, newStatusFilterValues, newLStatus);
			clickAndSelectFromDropDown(driver, changeTypeFilter, changeTypeFilterValues, changeType);
			clickElement(driver, addStatusBtn);
			waitUntilVisible(driver, duplicateLeadStatus);
			assertTrue(isElementVisible(driver, duplicateLeadStatus, 5));
		} else {
			clickAndSelectFromDropDown(driver, oldStatusFilter, oldStatusFilterValues, oldLStatus);
			clickAndSelectFromDropDown(driver, newStatusFilter, newStatusFilterValues, newLStatus);
			clickAndSelectFromDropDown(driver, changeTypeFilter, changeTypeFilterValues, changeType);
			clickElement(driver, addStatusBtn);
			locator = By.xpath(deleteGivenStatusChange.replace("$$oldStatus$$", oldLStatus)
					.replace("$$newStatus$$", newLStatus).replace("$$change$$", changeType));
			waitUntilVisible(driver, locator, 5);
		}

	}

	// Try to Add duplicate Lead Status or Opp Status
	public void addDupeStatusChange(WebDriver driver, String oldLStatus, String newLStatus, String changeType) {
		// Delete if combination already exists
		clickAndSelectFromDropDown(driver, oldStatusFilter, oldStatusFilterValues, oldLStatus);
		clickAndSelectFromDropDown(driver, newStatusFilter, newStatusFilterValues, newLStatus);
		clickAndSelectFromDropDown(driver, changeTypeFilter, changeTypeFilterValues, changeType);
		clickElement(driver, addStatusBtn);
		waitUntilVisible(driver, duplicateLeadStatus, 1);
		assertTrue(isElementVisible(driver, duplicateLeadStatus, 2));

	}

	// Method to Update given Lead Status or Opp Status
	public void updateStatusChange(WebDriver driver, String oldFromStatus, String oldToStatus, String oldChangeType,
			String newFromStatus, String newToStatus, String newChangeType) {
		// Delete if update combination already exists

		By locator = By.xpath(
				deleteGivenStatusChange.replace("$$oldStatus$$", oldFromStatus).replace("$$newStatus$$", oldToStatus));
		By updateLocator = By.xpath(
				updateGivenStatusChange.replace("$$oldStatus$$", newFromStatus).replace("$$newStatus$$", newToStatus));

		if (isElementVisible(driver, updateLocator, 1)) {
			updateLocator = By.xpath(updateGivenStatusChange.replace("$$oldStatus$$", oldFromStatus)
					.replace("$$newStatus$$", oldToStatus));
			clickElement(driver, updateLocator);
			clickAndSelectFromDropDown(driver, updateoldStatusFilter, updateoldStatusFilterValues, newFromStatus);
			clickAndSelectFromDropDown(driver, updatenewStatusFilter, updatenewStatusFilterValues, newToStatus);
			clickAndSelectFromDropDown(driver, updateChangeTypeFilter, updateChangeTypeFilterValues, newChangeType);
			clickElement(driver, saveUpdateBtn);
			waitUntilVisible(driver, duplicateLeadStatus);
			assertTrue(isElementVisible(driver, duplicateLeadStatus, 2));
			refreshCurrentDocument(driver);
			dashboard.isPaceBarInvisible(driver);
		} else {
			locator = By.xpath(updateGivenStatusChange.replace("$$oldStatus$$", oldFromStatus)
					.replace("$$newStatus$$", oldToStatus).replace("$$change$$", oldChangeType));
			clickElement(driver, locator);
			clickAndSelectFromDropDown(driver, updateoldStatusFilter, updateoldStatusFilterValues, newFromStatus);
			clickAndSelectFromDropDown(driver, updatenewStatusFilter, updatenewStatusFilterValues, newToStatus);
			clickAndSelectFromDropDown(driver, updateChangeTypeFilter, updateChangeTypeFilterValues, newChangeType);
			clickElement(driver, saveUpdateBtn);
			By delLocator = By.xpath(deleteGivenStatusChange.replace("$$oldStatus$$", newFromStatus)
					.replace("$$newStatus$$", newToStatus).replace("$$change$$", newChangeType));
			waitUntilVisible(driver, delLocator, 2);
		}

	}

	// Method to Update given Lead Status or Opp Status with duplicate value
	public void updateDupeStatusChange(WebDriver driver, String oldFromStatus, String oldToStatus, String oldChangeType,
			String newFromStatus, String newToStatus, String newChangeType) {
		// Delete if update combination already exists
		// deleteStatusChange(driver, newFromStatus, newToStatus,
		// newChangeType);
		By locator = By.xpath(updateGivenStatusChange.replace("$$oldStatus$$", oldFromStatus)
				.replace("$$newStatus$$", oldToStatus).replace("$$change$$", oldChangeType));
		clickElement(driver, locator);
		clickAndSelectFromDropDown(driver, updateoldStatusFilter, updateoldStatusFilterValues, newFromStatus);
		clickAndSelectFromDropDown(driver, updatenewStatusFilter, updatenewStatusFilterValues, newToStatus);
		clickAndSelectFromDropDown(driver, updateChangeTypeFilter, updateChangeTypeFilterValues, newChangeType);
		clickElement(driver, saveUpdateBtn);
		waitUntilVisible(driver, duplicateLeadStatus, 1);
		assertTrue(isElementVisible(driver, duplicateLeadStatus, 2));

	}

	// Open Opp Status Page
	public void clickOppStatusLeftNav(WebDriver driver) {
		clickElement(driver, oppStatusLeftNav);
		dashboard.isPaceBarInvisible(driver);
	}

	// Open Set up Page of CAI
	
	public boolean isSetUpTabVisible(WebDriver driver){
		return isElementVisible(driver, setUpTab, 5);
	}
	
	public void clickSetUpTab(WebDriver driver) {
		clickElement(driver, setUpTab);
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, toastMessage, 0));
		assertFalse(isElementVisible(driver, errorMessage, 0));
	}

	// Open Set up Page of CAI via User Profile icon
	public void clickSetUpTabViaProfile(WebDriver driver) {
		clickElement(driver, userProfileLink);
		waitUntilVisible(driver, setUpViaProfile);
		clickElement(driver, setUpViaProfile);
		dashboard.isPaceBarInvisible(driver);

	}

	public String getSelectedSetting(WebDriver driver) {
		return findElement(driver, selectedLefNavSetting).getAttribute("data-args").trim();
	}

	// Add Vocabluary
	public void addVocabluary(WebDriver driver, String notes) {
		clickElement(driver, vocabularyTextBox);
		enterText(driver, vocabularyTextBox, notes);
		clickElement(driver, addButton);
		idleWait(2);

	}

	// Add Vocabluary via Enter
	public void addVocabluaryVaiEnter(WebDriver driver, String notes) {
		clickElement(driver, vocabularyTextBox);
		enterText(driver, vocabularyTextBox, notes);
		clickEnter(driver, vocabularyTextBox);
		idleWait(2);

	}

	// Check Added Vocabluary exists in list
	public boolean vocabluaryExistInList(WebDriver driver, String notes) {

		List<WebElement> vocList = getElements(driver, vocabluaryList);
		return isTextPresentInList(driver, vocList, notes);
	}

	// Delete given Vocabluary Item
	public void deleteVocabluaryItem(WebDriver driver, String notes) {
		clickElement(driver, By.xpath(deleteVocabluaryItem.replace("$$vocabluary$$", notes)));
		waitUntilVisible(driver, deleteOk);
		clickElement(driver, deleteOk);
		idleWait(1);
	}

	// Open Keyword Group page
	public void clickKeywordGroupLeftNav(WebDriver driver) {
		waitUntilVisible(driver, keywordGroupLeftNav);
		clickElement(driver, keywordGroupLeftNav);
		dashboard.isPaceBarInvisible(driver);
	}

	public void addKeywordGroup(WebDriver driver, String notes, String keyPhrase1, String keyPhrase2,
			String newKeyPhrase) {
		waitUntilVisible(driver, AddkeywordGroupButton);
		clickElement(driver, AddkeywordGroupButton);
		waitUntilVisible(driver, keywordGroupTextBox);
		enterText(driver, keywordGroupTextBox, notes);
		enterTextandSelect(driver, keyPhraseTextBox, keyPhrase1);
		enterTextandSelect(driver, keyPhraseTextBox, keyPhrase2);
		enterTextandSelect(driver, keyPhraseTextBox, newKeyPhrase);
		idleWait(2);
		clickElement(driver, createButton);
	}
	
	public void clickCancelKeyword(WebDriver driver){
		waitUntilVisible(driver, cancelButton);
		clickElement(driver, cancelButton);
	}
	
	public void verifyKeywordGroupNameMoreThan60NotAccept(WebDriver driver, String groupMoreThan60Char, String group60Char){
		waitUntilVisible(driver, AddkeywordGroupButton);
		clickElement(driver, AddkeywordGroupButton);
		waitUntilVisible(driver, keywordGroupTextBox);
		enterText(driver, keywordGroupTextBox, groupMoreThan60Char);
		assertNotEquals(getAttribue(driver, keywordGroupTextBox, ElementAttributes.value), groupMoreThan60Char);
		enterText(driver, keywordGroupTextBox, group60Char);
		assertEquals(getAttribue(driver, keywordGroupTextBox, ElementAttributes.value), group60Char);
		clickCancelKeyword(driver);
	}
	
	public void verifyAlertMsgGroupName55OrMore(WebDriver driver, String group54Char, String group55Char){
		waitUntilVisible(driver, AddkeywordGroupButton);
		clickElement(driver, AddkeywordGroupButton);
		waitUntilVisible(driver, keywordGroupTextBox);
		enterText(driver, keywordGroupTextBox, group54Char);
		assertFalse(isElementVisible(driver, alertGroupNameMsg, 5));
		enterText(driver, keywordGroupTextBox, group55Char);
		assertTrue(isElementVisible(driver, alertGroupNameMsg, 5));
		clickCancelKeyword(driver);
	}
	
	// Add Keyword Group
	public void addKeywordMoreThenLimit(WebDriver driver) {
		waitUntilVisible(driver, vocabularyTextBox);
		clickElement(driver, vocabularyTextBox);
		enterText(driver, vocabularyTextBox, "Max Char limit for keyword is 64.We are trying to enter more then that");
		clickEnter(driver, vocabularyTextBox);
		idleWait(1);
		assertTrue(isElementVisible(driver, keywordMaxLimitMsg, 2));
	}

	// Add Keyword Group via Enter
	public void addKeywordGroupViaEnter(WebDriver driver, String notes) {
		waitUntilVisible(driver, keywordGroupTextBox);
		clickElement(driver, keywordGroupTextBox);
		enterText(driver, keywordGroupTextBox, notes);
		clickEnter(driver, keywordGroupTextBox);
		idleWait(2);
	}

	// Check Added Keyword Group exists in List
	public boolean keywordGroupExistInList(WebDriver driver, String notes) {
		isListElementsVisible(driver, keywordGroupList, 5);
		List<WebElement> keyGroupList = getElements(driver, keywordGroupList);
		return isTextPresentInList(driver, keyGroupList, notes);
	}

	// Check Added Keyword Group exists in List
	public String keywordPhraseCount(WebDriver driver, String notes) {
		By keyWordCountLoc = By.xpath(keywordGroupCount.replace("$$keyword$$", notes));
		waitUntilVisible(driver, keyWordCountLoc);
		String count = getElementsText(driver, keyWordCountLoc);
		return count;
	}

	// Check Added Keyword Phrase exists in List
	public boolean phraseExistInList(WebDriver driver, String notes) {
		isListElementsVisible(driver, keywordPhraseList, 5);
		List<WebElement> keyGroupList = getElements(driver, keywordPhraseList);
		return isTextPresentInList(driver, keyGroupList, notes);
	}

	// Select given Keyword group from available list
	public void clickKeywordGroup(WebDriver driver, String keywordGroup) {
		By keyWordGroupLoc =  By.xpath(selectKeywordGroup.replace("$$keywordGroupName$$", keywordGroup));
		waitUntilVisible(driver, keyWordGroupLoc);
		clickElement(driver, keyWordGroupLoc);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, keyPhraseTextBox);
	}

	// Add Keyphrase in given keyword group
	public void addKeyPhrase(WebDriver driver, String keyPhrase) {
		waitUntilVisible(driver, keyPhraseTextBox);
		clickElement(driver, keyPhraseTextBox);
		enterText(driver, keyPhraseTextBox, keyPhrase);
		clickElement(driver, keyPhraseAddBtn);
		idleWait(2);
	}

	// Add Keyphrase via Enter in given keyword group
	public void addKeyPhraseViaEnter(WebDriver driver, String keyPhrase) {
		waitUntilVisible(driver, keyPhraseTextBox);
		clickElement(driver, keyPhraseTextBox);
		enterText(driver, keyPhraseTextBox, keyPhrase);
		clickEnter(driver, keyPhraseTextBox);
		idleWait(2);
	}

	// Delete Keyphrase from given keyword group
	public void deleteKeyPhraseItem(WebDriver driver, String keyPhrase) {
		By loc = By.xpath(deleteKeyPhrase.replace("$$keyPhraseName$$", keyPhrase));
		waitUntilVisible(driver, loc);
		clickElement(driver, loc);
		idleWait(1);
		clickElement(driver, updateButton);
	}
	
	// Delete given keyword group from list
	public void deleteKeywordGroup(WebDriver driver, String keyWord) {
		By keyWordGroupLoc =  By.xpath(deleteKeywordGroup.replace("$$keywordGroupName$$", keyWord));
		waitUntilVisible(driver, keyWordGroupLoc);
		clickElement(driver, keyWordGroupLoc);
		waitUntilVisible(driver, deleteOk);
		clickElement(driver, deleteOk);
		idleWait(1);
	}

	public String clickToSortOldStatus(WebDriver driver) {
		waitUntilVisible(driver, oldStatus);
		clickElement(driver, oldStatus);
		return getOldStatusSortingStatus(driver, oldStatus);
	}

	public String clickToSortNewStatus(WebDriver driver) {
		waitUntilVisible(driver, newStatus);
		clickElement(driver, newStatus);
		return getOldStatusSortingStatus(driver, newStatus);
	}

	public List<String> getOldStatusList(WebDriver driver) {
		List<String> resultList = getTextListFromElements(driver, oldStatusList);
		return resultList;
	}

	public List<String> getNewStatusList(WebDriver driver) {
		List<String> resultList = getTextListFromElements(driver, newStatusList);
		return resultList;
	}

	public String getOldStatusSortingStatus(WebDriver driver, By locator) {

		if (findElement(driver, locator).getAttribute("class").contains("ascending")) {
			return "ascending";
		} else if (findElement(driver, locator).getAttribute("class").contains("descending")) {
			return "descending";
		} else
			return "none";
	}

	public boolean getSortOrder(WebDriver driver, List<String> stringList, String order) {

		if (order == "ascending") {
			boolean sorted = Ordering.natural().isOrdered(stringList);
			assertTrue(sorted, "Sorting on Ascending order not works fine");
			return true;
		} else {
			boolean sorted = Ordering.natural().reverse().isOrdered(stringList);
			assertTrue(sorted, "Sorting on Descending order not works fine");
			return true;
		}
	}

	//Notification Settings
	public boolean isInboxNotificationListDisabled(WebDriver driver){
		waitUntilVisible(driver, inboxCheckboxList);
		return isAttributePresentInList(driver, inboxCheckboxList, ElementAttributes.disabled.name());
	}
	
	public boolean isEmailNotificationListDisabled(WebDriver driver){
		waitUntilVisible(driver, emailCheckBoxList);
		return isAttributePresentInList(driver, emailCheckBoxList, ElementAttributes.disabled.name());
	}
	
	public void verifyNotificationMsgSetting(WebDriver driver){
		waitUntilVisible(driver, toastMessage);
		assertEquals(getElementsText(driver, toastMessage), "Notification settings updated.");
		waitUntilInvisible(driver, toastMessage);
	}

	public void verifyCheckedEmailNotification(WebDriver driver, String notification) {
		By emailNotificationLoc = By.xpath(emailNotificationCheckBox.replace("$notification$", notification));
		refreshCurrentDocument(driver);
		waitUntilVisible(driver, emailNotificationLoc);
		assertTrue(isAttributePresent(driver, emailNotificationLoc, ElementAttributes.checked.name()));
	}
	
	public void checkEmailNotification(WebDriver driver, String notification){
		By emailNotificationLoc = By.xpath(emailNotificationCheckBox.replace("$notification$", notification));
		waitUntilVisible(driver, emailNotificationLoc);
		checkCheckBox(driver, emailNotificationLoc);
	}
	
	public void unCheckEmailNotification(WebDriver driver, String notification){
		By emailNotificationLoc = By.xpath(emailNotificationCheckBox.replace("$notification$", notification));
		waitUntilVisible(driver, emailNotificationLoc);
		unCheckCheckBox(driver, emailNotificationLoc);
	}
	
	public void verifyUnCheckedEmailNotification(WebDriver driver, String notification) {
		By emailNotificationLoc = By.xpath(emailNotificationCheckBox.replace("$notification$", notification));
		refreshCurrentDocument(driver);
		waitUntilVisible(driver, emailNotificationLoc);
		assertFalse(isAttributePresent(driver, emailNotificationLoc, ElementAttributes.checked.name()));
	}
}