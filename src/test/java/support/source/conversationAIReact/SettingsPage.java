package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class SettingsPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();

	By header					= By.cssSelector("[data-testid='section-header'] h2");
	By label					= By.cssSelector("[data-testid='section-header'] h6");
	By settingsTab				= By.cssSelector("[href='/settings']");
	By customVocInputTab		= By.cssSelector("#newWord");
	By customVocAddBtn			= By.cssSelector("[data-testid='vocab-container.vocab-form'] button[type='submit']");
	By customVocErrorMsg		= By.cssSelector("#newWord-helper-text");
	
	By toastMsg					= By.cssSelector(".toast-message, #client-snackbar");
	By toastErrorMsg			= By.cssSelector(".toast.toast-error .toast-message,  #client-snackbar");
	
	By listCustomVocTexts		= By.cssSelector("li div.MuiListItemText-root span");
	
	By confirmDeleteBtn			= By.cssSelector("button[data-testid='ui-dialog.primary-btn']");
	By deleteCustomVocHeader	= By.xpath(".//*[text()='Delete Vocabulary']");
	By deleteCustomVocBodyMsg	= By.xpath(".//*[@role='dialog']//span[text()='Do you want to delete this custom vocabulary word?']");
	String deleteCustomVocBtn		= ".//*[@data-testid='vocab-list.grid-list']//span[text()='$customVoc$']/parent::div/following-sibling::button[@data-testid='vocab-list.delete-button']";
	String deleteCustomVocName	= ".//*[@role='dialog']//span[text()='Do you want to delete this custom vocabulary word?']/following-sibling::p[text()='$customVoc$']";
	
	public static String errorMsgCustomVoc = "{\"code\":\"\",\"message\":\"The term <b>cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzuqwr</b> already exists.\",\"httpStatus\":\"400\"}";
	public static String errorMsgKeyword   = "Only alphanumeric, space, and hyphen characters allowed";

	/////keyword/////////////////
	By keywordGroupTab			= By.cssSelector("[href='#smart-recordings/setup/keyword-group'] span, [name='/settings/keyword-group-settings'] span");
	By searchKeywordInputTab	= By.cssSelector("input#searchValue");
	By sortValueInputTab		= By.cssSelector("[data-testid = 'sortValue-input'] #ringdna-select");
	By listBoxItems				= By.cssSelector("ul[role='listbox'] li");
	By createNewGrpBtn			= By.cssSelector("[name='add-keyword-group']");
	By groupNameInput			= By.cssSelector("input#name");
	By groupNameHelperText		= By.cssSelector("#name-helper-text");
	By keywordNameInput			= By.cssSelector("textarea#keywords");
	By keywordNameParagraph		= By.cssSelector("p#keywords-helper-text");
	By keywordGroupFormList		= By.xpath("//*[@id='keywords-label']/..//span[contains(@class,'label')]");
	By createBtn				= By.xpath(".//button[@data-testid='ui-dialog.primary-btn' and text()='Create']");
	By updateBtn				= By.xpath(".//button[@data-testid='ui-dialog.primary-btn' and text()='Update']");
	By cancelBtn				= By.xpath("//*[@data-testid='ui-dialog.secondary-btn' and text()='Cancel']");
	By alertGroupNameMsg		= By.xpath(".//*[contains(@class,'MuiFormHelperText-filled') and text()='Group names must be 60 characters or less']");
	
	By allKeywordGroupsDeleteBtn   = By.cssSelector("[data-testid='keyword-group-list.keywords'] [aria-label='delete'] svg");
	By emptyListKeywordGrpHeaderH2 = By.xpath("//*[@data-testid='keyword-group-list.keywords']/ancestor::div//div[not(@data-testid)]/h2");
	By emptyKeywordGrpHeaderH5 	   = By.xpath("//*[@data-testid='keyword-group-list.keywords']/ancestor::div//div[not(@data-testid)]/h5");
	
	By groupListText			= By.cssSelector("[data-testid='keyword-group-list.keywords'] h4");
	
	//
	String deleteKeyword		   = "//*[@id='keywords-label']/parent::div//span[contains(@class,'label') and text()='$keyword$']/../*[name()='svg']";
	String updateAccToGroup		   = "//*[@data-testid='keyword-group-list.keywords']//h4[text()='$groupName$']/ancestor::div[contains(@class,'MuiPaper-elevation1')]//button[@aria-label='edit']";
	String deleteAccToGroup		   = "//*[@data-testid='keyword-group-list.keywords']//h4[text()='$groupName$']/ancestor::div[contains(@class,'MuiPaper-elevation1')]//button[@aria-label='delete']";
	String keywordCountAccToGroup  = "//*[@data-testid='keyword-group-list.keywords']//h4[text()='$groupName$']/following-sibling::span";
	String keywordListAccToGroup   = "//*[@data-testid='keyword-group-list.keywords']//h4[text()='$groupName$']/ancestor::div[contains(@class,'MuiPaper-elevation1')]//div[contains(@data-testid,'keyword.chip')]//span";
	
	static final String keywordHeaderFirstH2  = "Create Your First Keyword Group";
	static final String keywordHeaderSecondH2 = "Keyword groups allow you to easily organize and access targeted insights from within Conversation AI";
	static final String keywordHeaderH5 	  = "Simply create any custom keyword or phrase you want to track (such as features, competitors, and conversion terms), and instantly access data attached to that group directly from the call player.";
	
	//status change locators
	
	By statusChangeTab				= By.cssSelector("[href='#smart-recordings/setup/status-change'] span, [name='/settings/status-change-settings'] span");
	By leadStatusTab				= By.xpath(".//button[@role='tab']/span[text()='Lead Status']");
	By opportunityStatusTab			= By.xpath(".//button[@role='tab']/span[text()='Opportunity Status']");
	By statusChangeHeaderMsg		= By.xpath("//*[@role='tabpanel' and not(@hidden)]/div/span");
	By manageGrpBtn					= By.cssSelector("button[name='manage-group']");
	By statusGrpNameInputTab		= By.cssSelector("[data-testid='group-name-input'] input");
	By statusGrpTotalCharCount		= By.cssSelector("[data-testid='group-name-input'] div p");
	By modalCloseBtn				= By.xpath("//*[@data-testid='modal.close']//span[contains(@class,'label')]");
	
	By statusChangeGrpNameList		 = By.xpath(".//*[contains(@class,'MuiPaper-rounded')]//div[contains(@class,'MuiPaper-elevation')]/div/span");
	By statusFormAPIName			 = By.cssSelector("[data-testid='status-edit-form.api-name']");
	By statusCustomNameInputTab		 = By.cssSelector("[data-testid='name-input'] input#name");
	By statusGroupTab				 = By.cssSelector("#ringdna-select");
	By saveBtn						 = By.xpath(".//button[@data-testid='ui-dialog.primary-btn' and text()='Save']");
	
	By inactiveDragElement           = By.xpath("//div[@data-rbd-droppable-id='Inactive']/div[@draggable='false']");
	By inactiveDragBoard             = By.xpath("//div[@data-rbd-droppable-id='Inactive']");
	
	By inProgressDragElement         = By.xpath("//div[@data-rbd-droppable-id='InProgress']/div[@draggable='false']");
	By inProgressDragBoard           = By.xpath("//div[@data-rbd-droppable-id='InProgress']");
	
	By convertedDragElement          = By.xpath("//div[@data-rbd-droppable-id='Converted']/div[@draggable='false']");
	
	String editButtonAccToStatusGrpName	 = "*[@data-testid='status-row.group-chip']/span[text()='$groupName$']/ancestor::div[(@data-react-beautiful-dnd-draggable)]//button[@data-testid='status-row.edit-status']//*[name()='svg']";
	String editButtonAccToSFApiName	 = "//*[@data-testid='status-row.api-name' and text()='$apiName$']/parent::div//button[@data-testid='status-row.edit-status']//*[name()='svg']";
	String rdnaCustomNameAccToSFAPI	 = "//div[@data-testid='status-row.api-name' and text()='$apiName$']/following-sibling::div[@data-testid='status-row.rdna-name']";
	String groupNameAccToSFAPI	 	 = "//*[@data-testid='status-row.api-name' and text()='$apiName$']/../..//div[@data-testid='status-row.group-chip']/span";
	String statusGrpNameString		 = ".//*[contains(@class,'MuiPaper-rounded')]//span[text()='$groupName$']";
	String statusGrpNameEditString	 = ".//*[contains(@class,'MuiPaper-rounded')]//span[text()='$groupName$']/..//button[1]";
	String statusGrpNameDeleteString = ".//*[contains(@class,'MuiPaper-rounded')]//span[text()='$groupName$']/..//button[2]";
	String editStatusGrpNameInputTab = ".//*[@data-testid='group-name-input']//input[@value='$groupName$']";
	String expectedCountAccToStatusGrp	 = ".//*[@value='$groupName$']/following-sibling::div[contains(@class, '-positionEnd')]/p";
	String editStatusGrpNameInputValue   = ".//*[@data-testid='group-name-input']//input[contains(@value,'$groupName$')]";
	
	By deleteGrpNameHeaderMsg  = By.xpath("//*[@data-testid='modal.close']/..//h4[contains(text(), 'Delete')]");
	By deleteGrpNameParagraph  = By.xpath("//*[@data-testid='modal.close']/..//span[contains(text(), 'deletes')]");
	
	By expandCollapseLS         = By.xpath("//span[contains(text(), 'Track lead status changes')]/following-sibling::div//div/*[local-name()='svg' and not(@data-testid)]");
	By sfdcApiLSHeader          = By.xpath("//span[contains(text(), 'Track lead status changes')]/following-sibling::div//div[@class = 'MuiCollapse-wrapper']//*[text() = 'Salesforce API Name']");
	
	static final String deleteGrpPara = "This action permanently deletes the group. The status will remain but it will no longer be associated to any group. All data related to this group will be erased.";
	static final String opportunityStatusHeaderMsg = "Track changes in opportunity stage to determine the progress of leads. If the order of the stages are incorrect, update the stages in Salesforce and refresh page after.";
	
	//Opportunites Status Change section

	By inProgressSections 	 = By.xpath("//*[@data-testid='status-category-container.title' and text()='In Progress']/../..//div[@data-react-beautiful-dnd-drag-handle]");
	By inProgressEditBtnList = By.xpath("//*[@data-testid='status-category-container.title' and text()='In Progress']/../..//div[@data-react-beautiful-dnd-drag-handle]//button[@data-testid='status-row.edit-status']//*[name()='svg']");

	By statusCategoryContainerTitle = By.xpath("//*[@role='tabpanel' and not(@hidden)]//span[@data-testid='status-category-container.title']");
	By statusCategoryDragHandle 	= By.cssSelector("[data-testid='status-row.drag-handle']");
	String apiNameListAccToType = "//*[@data-testid='status-category-container.title' and text()='$type$']/parent::div/parent::div//div[@data-testid = 'status-row.api-name']";
	
	By expandCollapseOS         = By.xpath("//span[contains(text(), 'Track changes in opportunity stage')]/following-sibling::div//div/*[local-name()='svg' and not(@data-testid)]");
	By sfdcApiOSHeader          = By.xpath("//span[contains(text(), 'Track changes in opportunity stage')]/following-sibling::div//div[@class = 'MuiCollapse-wrapper']//*[text() = 'Salesforce API Name']");
	
	// Ring Dna Support
	By ringDnaSupportTab	 = By.cssSelector("[href='/settings/ringdna-support'] span, [name='/settings/ringdna-support-settings'] span"); 
	By dayInputTab           = By.cssSelector("input[name = 'Upload Recordings for Processing']");
	By errorMessage          = By.cssSelector(".MuiAlert-message span");
	By errorCloseButton      = By.cssSelector(".rdna-button.outlined");
	
	By uploadButton          = By.cssSelector(".rdna-button.contained");
	
	
	//Notification Settings
	By notificationLink			= By.cssSelector("[name='/settings/notifications-settings']");
	public static By notificationSettingsHeader = By.xpath("//h2[text()='Notification Settings']");
	
	//In App check box Lists
	String inappCheckBoxListAccToTableHeader = "//div[contains(@class,'ringdna-table-cell') and text()='$tableHeader$']/../..//div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')][1]//input";
	String emailCheckBoxListAccToTableHeader = "//div[contains(@class,'ringdna-table-cell') and text()='$tableHeader$']/../..//div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')][2]//input";
	
	String notificationAccToTableHeader = "(//div[contains(@class,'ringdna-table-cell') and text()='$tableHeader$']/../..//span[@data-testid='notificationDisplayName'])[$$Index$$]";
	String frequencyAccToTableHeader 	= "(//div[contains(@class,'ringdna-table-cell') and text()='$tableHeader$']/../..//div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell')]/select)[$$Index$$]";
	
	By totalInAppCheckBoxList = By.xpath(".//*[contains(@class, 'ringdna-table-cell  centered')][1]//input");
	By totalEmailCheckBoxList = By.xpath(".//*[contains(@class, 'ringdna-table-cell  centered')][2]//input");
	By saveSearchImages  	  = By.cssSelector("[src='images/cai/icon-cai-share-public.svg']");
	
	String inAppCheckBox = "//*[text()='$notification$']/ancestor::div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')][1]//input";
	String emailCheckBox = "//*[text()='$notification$']/ancestor::div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')][2]//input";
	
	String inAppCheckBoxParent = "(//*[text()='$notification$']/ancestor::div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')]//input)[1]/parent::span/parent::span";
	String emailCheckBoxParent = "(//*[text()='$notification$']/ancestor::div[@data-testid='ringdna-table-row']//div[contains(@class, 'ringdna-table-cell  centered')]//input)[2]/parent::span/parent::span";
	
	public static enum CAINotificationsHeaders {
		AdvancedInsightsNotifications("Advanced Insights Notifications"),
		UserNotifications("User Notifications"),
		PublicSavedSearches("Public Saved Searches");
		
		private String value;

		CAINotificationsHeaders(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum NotificationsFrequency {
		None,
		Immediate,
		Daily,
		Weekly;
		
	}
	
	public static enum SortValueInput {
		AlphabeticalAToZ("Alphabetical (A-Z)"),
		AlphabeticalZToA("Alphabetical (Z-A)"),
		NewestToOldest("Newest to Oldest"),
		OldestToNewest("Oldest to Newest");
		private String value;

		SortValueInput(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}

	public void openSettingsPage(WebDriver driver) {
		waitUntilVisible(driver, settingsTab);;
		clickElement(driver, settingsTab);
	}

	public void verifyCustomVocHeadersLabel(WebDriver driver) {
		waitUntilVisible(driver, header);
		assertEquals(getElementsText(driver, header), "Custom Vocabulary");
		waitUntilVisible(driver, label);
		assertEquals(getElementsText(driver, label), "Add words and phrases that you would like the transcription engine to recognize, and correct words that are displayed incorrectly.");
	}
	
	public void enterTextInCustomVocInputTab(WebDriver driver,  String customVoc) {
		enterText(driver, customVocInputTab, customVoc);
		clickCustomVocAddBtn(driver);
	}
	
	public void createCustomVocabulary(WebDriver driver, String customVoc) {
		enterTextInCustomVocInputTab(driver, customVoc);
		assertEquals(getToastMsg(driver), "Your custom vocabulary list has been updated");
		assertTrue(Strings.isNullOrEmpty(getAttribue(driver, customVocInputTab, ElementAttributes.value)));
		verifyCustomVocAddedAtLastIndex(driver, customVoc);
	}
	
	public void selectSortValueFilter(WebDriver driver, String sortText) {
		clickAndSelectFromDropDown(driver, sortValueInputTab, listBoxItems, sortText);
		dashboard.isPaceBarInvisible(driver);
	}

	public String getCustomVocInputTabValue(WebDriver driver) {
		waitUntilVisible(driver, customVocInputTab);
		return getAttribue(driver, customVocInputTab, ElementAttributes.value);
	}
	
	public void clickCustomVocAddBtn(WebDriver driver) {
		waitUntilVisible(driver, customVocAddBtn);
		clickElement(driver, customVocAddBtn);
	}
	
	public void verifyCustomVocErrorMsg(WebDriver driver) {
		clickElement(driver, customVocInputTab);
		clickCustomVocAddBtn(driver);
		waitUntilVisible(driver, customVocErrorMsg);
		assertEquals(getElementsText(driver, customVocErrorMsg), "This field is required.");
	}
	
	/**get toast message
	 * @param driver
	 * @return
	 */
	public String getToastMsg(WebDriver driver) {
		String text = "";
		waitUntilVisible(driver, toastMsg);
		text = getElementsText(driver, toastMsg);
		waitUntilInvisible(driver, toastMsg);
		dashboard.isPaceBarInvisible(driver);
		return text;
	}
	
	public void deleteCustomVoc(WebDriver driver, String customVoc) {
		By customVocLocDeleteBtn = By.xpath(deleteCustomVocBtn.replace("$customVoc$", customVoc));
		By customVocLocDeletedName = By.xpath(deleteCustomVocName.replace("$customVoc$", customVoc));
		waitUntilVisible(driver, customVocLocDeleteBtn);
		clickElement(driver, customVocLocDeleteBtn);
		waitUntilVisible(driver, deleteCustomVocHeader);
		waitUntilVisible(driver, deleteCustomVocBodyMsg);
		waitUntilVisible(driver, customVocLocDeletedName);
		clickConfirmDeleteBtn(driver);
		assertEquals(getToastMsg(driver), "Your custom vocabulary term has been deleted");
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, customVocLocDeleteBtn, 5));
	}
	
	public void verifyToastErrorMsg(WebDriver driver, String actualMsg) {
		waitUntilVisible(driver, toastErrorMsg);
		System.out.println(getElementsText(driver, toastErrorMsg));
		assertEquals(getElementsText(driver, toastErrorMsg), actualMsg);
		waitUntilInvisible(driver, toastErrorMsg);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyCustomVocAddedAtLastIndex(WebDriver driver, String customVoc) {
		isListElementsVisible(driver, listCustomVocTexts, 5);
		int lastIndex = getElements(driver, listCustomVocTexts).size() - 1;
		assertEquals(getTextListFromElements(driver, listCustomVocTexts).get(lastIndex), customVoc);
	}
	
	public void verifyCustomVocAscendingOrder(WebDriver driver) {
		isListElementsVisible(driver, listCustomVocTexts, 5);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(getTextListFromElements(driver, listCustomVocTexts)));
	}
	
	//////////////////keyword////////////////////
	
	public void clickKeywordGroupTab(WebDriver driver) {
		waitUntilVisible(driver, keywordGroupTab);
		clickElement(driver, keywordGroupTab);
	}
	
	public void enterTextInSearchKeywordTab(WebDriver driver, String keyword) {
		waitUntilVisible(driver, searchKeywordInputTab);
		enterText(driver, searchKeywordInputTab, keyword);
	}
	
	public void verifyKeywordGroupsHeadersLabel(WebDriver driver) {
		waitUntilVisible(driver, header);
		assertEquals(getElementsText(driver, header), "Keyword and Key Phrase Groups");
		waitUntilVisible(driver, label);
		assertEquals(getElementsText(driver, label), "Create keyword and key phrase groups, and add keywords and key phrases to them.");
	}
	
	public List<String> createNewGroup(WebDriver driver, String groupName, int keywordCount) {
		String keywordName = "";
		waitUntilVisible(driver, createNewGrpBtn);
		clickElement(driver, createNewGrpBtn);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(getElementsText(driver, keywordNameParagraph), "Type a keyword in the input above and hit enter");
		assertTrue(isElementDisabled(driver, createBtn, 5));
		waitUntilVisible(driver, groupNameInput);
		clearAll(driver, groupNameInput);
		enterText(driver, groupNameInput, groupName);
		assertTrue(isElementDisabled(driver, createBtn, 5));
		waitUntilVisible(driver, keywordNameInput);
		clearAll(driver, keywordNameInput);
		for (int i = 1; i <= keywordCount; i++) {
			keywordName = "AutoKeyword".concat(HelperFunctions.GetRandomString(5));
			appendText(driver, keywordNameInput, keywordName);
			clickEnter(driver, keywordNameInput);
			assertFalse(isElementDisabled(driver, createBtn, 5));
		}
		List<String> listKeywords = getKeywordNamesInGroup(driver);
		clickElement(driver, createBtn);
		waitUntilInvisible(driver, createBtn);
		dashboard.isPaceBarInvisible(driver);
		return listKeywords;
	}
	
	public void verifyValidationMsgForSpecialCharacter(WebDriver driver, String groupName) {
		waitUntilVisible(driver, createNewGrpBtn);
		clickElement(driver, createNewGrpBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, groupNameInput);
		clearAll(driver, groupNameInput);
		enterText(driver, groupNameInput, groupName);
		waitUntilVisible(driver, keywordNameInput);
		clearAll(driver, keywordNameInput);
		String keywordName1 = "test-123";
		appendText(driver, keywordNameInput, keywordName1);
		clickEnter(driver, keywordNameInput);
		String keywordName2 = "test@abc";
		appendText(driver, keywordNameInput, keywordName2);
		clickEnter(driver, keywordNameInput);
		clickElement(driver, createBtn);
		verifyToastErrorMsg(driver, errorMsgKeyword);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public List<String> getGroupNamesList(WebDriver driver){
		isListElementsVisible(driver, groupListText, 5);
		return getTextListFromElements(driver, groupListText);
	}
	
	public void verifyGroupNameListAscendingOrder(WebDriver driver) {
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(getGroupNamesList(driver)));
	}

	public void verifyGroupNameListDescendingOrder(WebDriver driver) {
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(getGroupNamesList(driver)));
	}

	public List<String> getKeywordNameListAfterCreating(WebDriver driver, String groupName) {
		By keywordListLoc = By.xpath(keywordListAccToGroup.replace("$groupName$", groupName));
		isListElementsVisible(driver, keywordListLoc, 5);
		return getTextListFromElements(driver, keywordListLoc);
	}
	
	public void verifyKeywordNameListAscendingOrder(WebDriver driver, String groupName) {
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(getKeywordNameListAfterCreating(driver, groupName)));
	}
	
	public void verifyKeywordListCount(WebDriver driver, String groupName, int expectedCount) {
		By keywordListLoc = By.xpath(keywordListAccToGroup.replace("$groupName$", groupName));
		isListElementsVisible(driver, keywordListLoc, 5);
		assertEquals(getElements(driver, keywordListLoc).size(), expectedCount);
		By keywordCountLoc = By.xpath(keywordCountAccToGroup.replace("$groupName$", groupName));
		assertEquals(Integer.parseInt(getElementsText(driver, keywordCountLoc)), expectedCount);
	}
	
	public String getGroupNameInputTab(WebDriver driver) {
		waitUntilVisible(driver, groupNameInput);
		return getAttribue(driver, groupNameInput, ElementAttributes.value);
	}
	
	public List<String> getKeywordNamesInGroup(WebDriver driver){
		isListElementsVisible(driver, keywordGroupFormList, 5);
		return getTextListFromElements(driver, keywordGroupFormList);
	}
	
	public void deleteKeywordsInGroup(WebDriver driver, String keyWord) {
		By deleteKeywordLoc = By.xpath(deleteKeyword.replace("$keyword$", keyWord));
		waitUntilVisible(driver, deleteKeywordLoc);
		clickElement(driver, deleteKeywordLoc);
		waitUntilInvisible(driver, deleteKeywordLoc);
	}
	
	public List<String> editKeyWordGroup(WebDriver driver, String oldGroupName, String newGroupName, List<String> expectedKeyWordList, List<String> keyWordRemoveList, int newKeywordCount) {
		String keywordName = "";
		By updateGroupLoc = By.xpath(updateAccToGroup.replace("$groupName$", oldGroupName));
		waitUntilVisible(driver, updateGroupLoc);
		clickElement(driver, updateGroupLoc);
		assertEquals(getGroupNameInputTab(driver), oldGroupName);
		assertTrue(getKeywordNamesInGroup(driver).containsAll(expectedKeyWordList));
		assertTrue(isElementDisabled(driver, updateBtn, 5));
		waitUntilVisible(driver, groupNameInput);
		clearAll(driver, groupNameInput);
		enterText(driver, groupNameInput, newGroupName);
		assertFalse(isElementDisabled(driver, updateBtn, 5));
		for(String keyWord: keyWordRemoveList) {
			deleteKeywordsInGroup(driver, keyWord);
		}
		for (int i = 1; i <= newKeywordCount; i++) {
			keywordName = "AutoKeyword".concat(HelperFunctions.GetRandomString(5));
			appendText(driver, keywordNameInput, keywordName);
			clickEnter(driver, keywordNameInput);
		}
		List<String> listKeywords = getKeywordNamesInGroup(driver);
		clickElement(driver, updateBtn);
		waitUntilInvisible(driver, updateBtn);
		dashboard.isPaceBarInvisible(driver);
		return listKeywords;
	}

	public void deleteGroupKeyword(WebDriver driver, String groupName) {
		By deleteGroupLoc = By.xpath(deleteAccToGroup.replace("$groupName$", groupName));
		waitUntilVisible(driver, deleteGroupLoc);
		clickElement(driver, deleteGroupLoc);
		clickConfirmDeleteBtn(driver);
		waitUntilInvisible(driver, deleteGroupLoc);
	}
	
	public void deleteAllKeywordGroups(WebDriver driver) {
		if (isListElementsVisible(driver, allKeywordGroupsDeleteBtn, 5)) {
			int i = getElements(driver, allKeywordGroupsDeleteBtn).size() - 1;
			while (i >= 0) {
				clickElement(driver, getElements(driver, allKeywordGroupsDeleteBtn).get(i));
				clickConfirmDeleteBtn(driver);
				idleWait(1);
				i--;
			}
		}
	}
	
	public void verifyEmptyKeywordsSectionHeaders(WebDriver driver) {
		isListElementsVisible(driver, emptyListKeywordGrpHeaderH2, 5);
		assertEquals(getTextListFromElements(driver, emptyListKeywordGrpHeaderH2).get(0), keywordHeaderFirstH2);
		assertEquals(getTextListFromElements(driver, emptyListKeywordGrpHeaderH2).get(1), keywordHeaderSecondH2);
		assertEquals(getElementsText(driver, emptyKeywordGrpHeaderH5), keywordHeaderH5);
	}
	
	public void clickCancelKeyword(WebDriver driver){
		waitUntilVisible(driver, cancelBtn);
		clickElement(driver, cancelBtn);
	}

	public void verifyAlertMsgGroupName55OrMore(WebDriver driver, String group54Char, String group55Char){
		waitUntilVisible(driver, createNewGrpBtn);
		clickElement(driver, createNewGrpBtn);
		waitUntilVisible(driver, groupNameInput);
		enterText(driver, groupNameInput, group54Char);
		assertFalse(isElementVisible(driver, alertGroupNameMsg, 5));
		enterText(driver, groupNameInput, group55Char);
		assertTrue(isElementVisible(driver, alertGroupNameMsg, 5));
		clickCancelKeyword(driver);
	}
	
	public void verifyKeywordGroupNameMoreThan60NotAccept(WebDriver driver, String groupMoreThan60Char, String group60Char){
		waitUntilVisible(driver, createNewGrpBtn);
		clickElement(driver, createNewGrpBtn);
		waitUntilVisible(driver, groupNameInput);
		enterText(driver, groupNameInput, groupMoreThan60Char);
		assertNotEquals(getAttribue(driver, groupNameInput, ElementAttributes.value), groupMoreThan60Char);
		enterText(driver, groupNameInput, group60Char);
		assertEquals(getAttribue(driver, groupNameInput, ElementAttributes.value), group60Char);
		clearAll(driver, groupNameInput);
		waitUntilVisible(driver, groupNameHelperText);
		assertEquals(getElementsText(driver, groupNameHelperText), "A valid group name is required");
		clickCancelKeyword(driver);
	}
	
	///////////////////////////status change/////////////////////////////////////
	
	public void clickStatusChangeTab(WebDriver driver) {
		waitUntilVisible(driver, statusChangeTab);
		clickElement(driver, statusChangeTab);
		waitUntilVisible(driver, statusChangeHeaderMsg);
	}
	
	public void clickStatusChangeManageGrpBtn(WebDriver driver) {
		waitUntilVisible(driver, manageGrpBtn);
		clickElement(driver, manageGrpBtn);
	}
	
	public boolean isGrpNameVisibleManageGrp(WebDriver driver, String groupName) {
		By statusGrpNameLoc = By.xpath(statusGrpNameString.replace("$groupName$", groupName));
		return isElementVisible(driver, statusGrpNameLoc, 5);
	}

	public boolean isGrpEditBtnVisibleManageGrp(WebDriver driver, String groupName) {
		By statusGrpEditLoc = By.xpath(statusGrpNameEditString.replace("$groupName$", groupName));
		return isElementVisible(driver, statusGrpEditLoc, 5);
	}
	
	public void clickGroupEditBtnManageGrp(WebDriver driver, String groupName) {
		By statusGrpEditLoc = By.xpath(statusGrpNameEditString.replace("$groupName$", groupName));
		clickElement(driver, statusGrpEditLoc);
	}
	
	public String getExpectedCountAccToStatusGroup(WebDriver driver, String groupName) {
		By expectedCountLoc = By.xpath(expectedCountAccToStatusGrp.replace("$groupName$", groupName));
		return getElementsText(driver, expectedCountLoc);
	}
	
	public String enterUpdateGrpNameInputTab(WebDriver driver, String groupName) {
		By editGrpNameInputTabLoc = By.xpath(editStatusGrpNameInputTab.replace("$groupName$", groupName));
		appendText(driver, editGrpNameInputTabLoc, HelperFunctions.GetRandomString(4));
		By editGrpNameInputValueLoc = By.xpath(editStatusGrpNameInputValue.replace("$groupName$", groupName));
		String updatedGrpName = getAttribue(driver, editGrpNameInputValueLoc, ElementAttributes.value);
		editGrpNameInputTabLoc = By.xpath(editStatusGrpNameInputTab.replace("$groupName$", updatedGrpName));
		clickEnter(driver, editGrpNameInputTabLoc);
		return updatedGrpName;
	}
	
	public String updateExistingGroupUnderManageGroup(WebDriver driver, String groupName) {
		clickStatusChangeManageGrpBtn(driver);
		assertTrue(isGrpEditBtnVisibleManageGrp(driver, groupName));
		assertTrue(isGrpDeleteBtnVisibleManageGrp(driver, groupName));
		clickGroupEditBtnManageGrp(driver, groupName);
		assertFalse(isGrpEditBtnVisibleManageGrp(driver, groupName));
		assertFalse(isGrpDeleteBtnVisibleManageGrp(driver, groupName));
		String expectedCount = String.valueOf(groupName.length()).concat("/25");
		assertEquals(getExpectedCountAccToStatusGroup(driver, groupName), expectedCount);
		String updatedGrpName = enterUpdateGrpNameInputTab(driver, groupName);
		assertTrue(isGrpEditBtnVisibleManageGrp(driver, updatedGrpName));
		assertTrue(isGrpDeleteBtnVisibleManageGrp(driver, updatedGrpName));	
		closeModalWindow(driver);
		return updatedGrpName;
	}
	
	public boolean isGrpDeleteBtnVisibleManageGrp(WebDriver driver, String groupName) {
		By statusGrpDeleteLoc = By.xpath(statusGrpNameDeleteString.replace("$groupName$", groupName));
		return isElementVisible(driver, statusGrpDeleteLoc, 5);
	}
	
	public void clickConfirmDeleteBtn(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteGroupStatusChange(WebDriver driver, String groupName) {
		clickStatusChangeManageGrpBtn(driver);
		By statusGrpDeleteLoc = By.xpath(statusGrpNameDeleteString.replace("$groupName$", groupName));
		waitUntilVisible(driver, statusGrpDeleteLoc);
		clickElement(driver, statusGrpDeleteLoc);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, deleteGrpNameHeaderMsg);
		assertEquals(getElementsText(driver, deleteGrpNameHeaderMsg), "Delete ".concat(groupName));
		assertEquals(getElementsText(driver, deleteGrpNameParagraph), deleteGrpPara);
		clickConfirmDeleteBtn(driver);
		closeModalWindow(driver);
	}
	
	public void createStatusChangeGroup(WebDriver driver, String groupName) {
		clickStatusChangeManageGrpBtn(driver);
		waitUntilVisible(driver, statusGrpNameInputTab);
		enterText(driver, statusGrpNameInputTab, groupName);
		String expectedCount = String.valueOf(groupName.length()).concat("/25");
		assertEquals(getElementsText(driver, statusGrpTotalCharCount), expectedCount);
		clickEnter(driver, statusGrpNameInputTab);
		assertTrue(isGrpNameVisibleManageGrp(driver, groupName));
		assertTrue(isGrpEditBtnVisibleManageGrp(driver, groupName));
		assertTrue(isGrpDeleteBtnVisibleManageGrp(driver, groupName));
		verifyStatusChangeGrpNameAscending(driver);
		closeModalWindow(driver);
	}
	
	/**
	 * @param driver
	 */
	public void closeModalWindow(WebDriver driver) {
		waitUntilVisible(driver, modalCloseBtn);
		clickElement(driver, modalCloseBtn);
	}

	/**
	 * @param driver
	 * @param groupMoreThan25Char
	 */
	public void verifyStatusChangeGrpNameMoreThan25NotAccept(WebDriver driver, String groupMoreThan25Char){
		clickStatusChangeManageGrpBtn(driver);
		waitUntilVisible(driver, statusGrpNameInputTab);
		enterText(driver, statusGrpNameInputTab, groupMoreThan25Char);
		assertNotEquals(getAttribue(driver, statusGrpNameInputTab, ElementAttributes.value), groupMoreThan25Char);
		assertEquals(getElementsText(driver, statusGrpTotalCharCount), "25/25");
		closeModalWindow(driver);
	}
	
	/**
	 * @param driver
	 */
	public void verifyStatusChangeGrpNameAscending(WebDriver driver) {
		isListElementsVisible(driver, statusChangeGrpNameList, 5);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(getTextListFromElements(driver, statusChangeGrpNameList)));
	}
	
	/**
	 * @param driver
	 * @param apiName
	 * @return
	 */
	public String getRDNACustomNameAccToAPI(WebDriver driver, String apiName) {
		By rdnaCustomNameLoc = By.xpath(rdnaCustomNameAccToSFAPI.replace("$apiName$", apiName));
		waitUntilVisible(driver, rdnaCustomNameLoc);
		return getElementsText(driver, rdnaCustomNameLoc);
	}
	
	/**
	 * @param driver
	 * @param apiName
	 * @return
	 */
	public String getGroupNameAccToAPI(WebDriver driver, String apiName) {
		By groupNameLoc = By.xpath(groupNameAccToSFAPI.replace("$apiName$", apiName));
		waitUntilVisible(driver, groupNameLoc);
		return getElementsText(driver, groupNameLoc);
	}

	/**
	 * @param driver
	 * @param groupName
	 * @return
	 */
	public String selectStatusGroupName(WebDriver driver, String groupName) {
		waitUntilVisible(driver, statusGroupTab);
		clickElement(driver, statusGroupTab);
		List<String> list = getTextListFromElements(driver, listBoxItems);
		list.remove(0);
		List<String> listUpdated = new ArrayList<String>(list);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(listUpdated));
		if (Strings.isNullOrEmpty(groupName)) {
			groupName = getElementsText(driver, getElements(driver, listBoxItems).get(1));
			clickElement(driver, getElements(driver, listBoxItems).get(1));
		} else {
			pressEscapeKey(driver);
			clickAndSelectFromDropDown(driver, statusGroupTab, listBoxItems, groupName);
		}
		dashboard.isPaceBarInvisible(driver);
		return groupName;
	}

	/**
	 * @param driver
	 * @param groupName
	 * @return
	 */
	public boolean isEditPencilAccToGrpNameVisible(WebDriver driver, String groupName) {
		By editPencilStatusGrpLoc = By.xpath(editButtonAccToStatusGrpName.replace("$groupName$", groupName));
		return isElementVisible(driver, editPencilStatusGrpLoc, 5);
	}
	
	/**
	 * @param driver
	 * @param groupName
	 */
	public void editPencilAccToStatusGroupName(WebDriver driver, String groupName) {
		By editStatusGrpLoc = By.xpath(editButtonAccToStatusGrpName.replace("$groupName$", groupName));
		waitUntilVisible(driver, editStatusGrpLoc);
		clickElement(driver, editStatusGrpLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param apiName
	 * @param oldCstomName
	 * @param newCustomName
	 * @param editGroupField
	 * @param groupName
	 * @return
	 */
	public String editAccToSFAPIName(WebDriver driver, String apiName, String oldCstomName, String newCustomName, boolean editGroupField, String groupName) {
		By editSFBtnLoc = By.xpath(editButtonAccToSFApiName.replace("$apiName$", apiName));
		waitUntilVisible(driver, editSFBtnLoc);
		clickElement(driver, editSFBtnLoc);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(findElement(driver, statusFormAPIName).getTagName(), "p");
		assertEquals(getAttribue(driver, statusCustomNameInputTab, ElementAttributes.value), oldCstomName);
		clearAll(driver, statusCustomNameInputTab);
		enterText(driver, statusCustomNameInputTab, newCustomName);
		if (editGroupField)
			groupName = selectStatusGroupName(driver, groupName);
		else
			assertFalse(isElementVisible(driver, statusGroupTab, 5));
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(getRDNACustomNameAccToAPI(driver, apiName), newCustomName);
		return groupName;
	}
	
	/**
	 * @param driver
	 */
	public void verifyInProgressSectionsEditIconPresent(WebDriver driver) {
		waitUntilVisible(driver, inProgressSections);
		waitUntilVisible(driver, inProgressEditBtnList);
		assertEquals(getElements(driver, inProgressSections).size(), getElements(driver, inProgressEditBtnList).size());
	}

	/**
	 * @param driver
	 */
	public void clickOpportunityStatusTab(WebDriver driver) {
		waitUntilVisible(driver, opportunityStatusTab);
		clickElement(driver, opportunityStatusTab);
		assertEquals(getElementsText(driver, statusChangeHeaderMsg), opportunityStatusHeaderMsg);
		assertFalse(isElementVisible(driver, manageGrpBtn, 1));
		assertEquals(getElements(driver, statusCategoryContainerTitle).size(), 3);
		assertFalse(isListElementsVisible(driver, statusCategoryDragHandle, 0));
	}
	
	/**
	 * @param driver
	 * @param type
	 * @return
	 */
	public List<String> getApiNameListAccToType(WebDriver driver, String type) {
		By apiListLoc = By.xpath(apiNameListAccToType.replace("$type$", type));
		waitUntilVisible(driver, apiListLoc);
		return getTextListFromElements(driver, apiListLoc);
	}
	
	/**
	 * @param driver
	 */
	public void verifyNotificationMsgSetting(WebDriver driver){
		waitUntilVisible(driver, toastMsg);
		assertEquals(getElementsText(driver, toastMsg), "Notification settings updated.");
		waitUntilInvisible(driver, toastMsg);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickNotificationSettings(WebDriver driver) {
		waitUntilVisible(driver, notificationLink);
		clickElement(driver, notificationLink);
	}
	
	/**
	 * @param driver
	 * @param tableHeader
	 * @param index
	 * @return
	 */
	public String getNotificationNameAccToTableHeader(WebDriver driver, CAINotificationsHeaders tableHeader, int index) {
		By notificationNameLoc = By.xpath(notificationAccToTableHeader.replace("$tableHeader$", tableHeader.getValue()).replace("$$Index$$", String.valueOf(index + 1)));
		if (isElementVisible(driver, notificationNameLoc, 5)) {
			return getElementsText(driver, notificationNameLoc);
		}
		return null;
	}
	
	public boolean isFrequencyVisibleAccToTableHeader(WebDriver driver, CAINotificationsHeaders tableHeader, int index) {
		By selectFreqLoc = By.xpath(frequencyAccToTableHeader.replace("$tableHeader$", tableHeader.getValue()).replace("$$Index$$", String.valueOf(index + 1)));
		return isElementVisible(driver, selectFreqLoc, 5);
	}
	
	public void selectFrequencyAccToTableHeader(WebDriver driver, CAINotificationsHeaders tableHeader, int index, NotificationsFrequency freq) {
		By selectFreqLoc = By.xpath(frequencyAccToTableHeader.replace("$tableHeader$", tableHeader.getValue()).replace("$$Index$$", String.valueOf(index + 1)));
		if (isFrequencyVisibleAccToTableHeader(driver, tableHeader, index)) {
			selectFromDropdown(driver, selectFreqLoc, SelectTypes.value, freq.name());
		}
	}
	
	public boolean isInAppNotificationDisabledAccToNotification(WebDriver driver, String notification){
		By inAppNotificationLoc = By.xpath(inAppCheckBox.replace("$notification$", notification));
		return isAttributePresent(driver, inAppNotificationLoc, ElementAttributes.disabled.name());
	}
	
	public boolean isEmailNotificationDisabledAccToNotification(WebDriver driver, String notification){
		By emailNotificationLoc = By.xpath(emailCheckBox.replace("$notification$", notification));
		return isAttributePresent(driver, emailNotificationLoc, ElementAttributes.disabled.name());
	}
	
	/**
	 * @param driver
	 * @param headerEnum TODO
	 * @return
	 */
	public boolean isInboxNotificationListDisabledAccToHeader(WebDriver driver, CAINotificationsHeaders headerEnum){
		By inAppCheckBoxNotificationList = By.xpath(inappCheckBoxListAccToTableHeader.replace("$tableHeader$", headerEnum.getValue()));
		isListElementsVisible(driver, inAppCheckBoxNotificationList, 5);
		return isAttributePresentInList(driver, inAppCheckBoxNotificationList, ElementAttributes.disabled.name());
	}
	
	/**
	 * @param driver
	 * @param header TODO
	 * @return
	 */
	public boolean isEmailNotificationListDisabledAccToHeader(WebDriver driver, CAINotificationsHeaders headerEnum){
		By emailCheckBoxNotificationList = By.xpath(emailCheckBoxListAccToTableHeader.replace("$tableHeader$", headerEnum.getValue()));
		isListElementsVisible(driver, emailCheckBoxNotificationList, 5);
		return isAttributePresentInList(driver, emailCheckBoxNotificationList, ElementAttributes.disabled.name());
	}

	/**
	 * @param driver
	 * @param notification
	 */
	public void selectInAppCheckBox(WebDriver driver, String notification) {
		By inAppCheckBoxLoc = By.xpath(inAppCheckBox.replace("$notification$", notification));
		checkCheckBox(driver, inAppCheckBoxLoc);
	}
	
	/**
	 * @param driver
	 * @param notification
	 */
	public void selectEmailCheckBox(WebDriver driver, String notification) {
		By emailCheckBoxLoc = By.xpath(emailCheckBox.replace("$notification$", notification));
		checkCheckBox(driver, emailCheckBoxLoc);
	}
	
	/**
	 * @param driver
	 * @param notification
	 */
	public void unSelectInAppCheckBox(WebDriver driver, String notification) {
		By inAppCheckBoxLoc = By.xpath(inAppCheckBox.replace("$notification$", notification));
		unCheckCheckBox(driver, inAppCheckBoxLoc);
	}
	
	/**
	 * @param driver
	 * @param notification
	 */
	public void unSelectEmailCheckBox(WebDriver driver, String notification) {
		By emailCheckBoxLoc = By.xpath(emailCheckBox.replace("$notification$", notification));
		unCheckCheckBox(driver, emailCheckBoxLoc);
	}
	
	/**
	 * @param driver
	 * @param notification
	 * @return
	 */
	public boolean isEmailNotificationChecked(WebDriver driver, String notification) {
		By emailNotificationLoc = By.xpath(emailCheckBoxParent.replace("$notification$", notification));
		waitUntilVisible(driver, emailNotificationLoc);
		return getAttribue(driver, emailNotificationLoc, ElementAttributes.Class).contains("Mui-checked");
	}
	
	/**
	 * @param driver
	 * @param notification
	 * @return
	 */
	public boolean isInAppNotificationChecked(WebDriver driver, String notification) {
		By inAppNotificationLoc = By.xpath(inAppCheckBoxParent.replace("$notification$", notification));
		waitUntilVisible(driver, inAppNotificationLoc);
		return getAttribue(driver, inAppNotificationLoc, ElementAttributes.Class).contains("Mui-checked");
	}
	
	/**
	 * @param driver
	 */
	public void verifyUserImagesVisibleSaveSearch(WebDriver driver) {
		assertTrue(isListElementsVisible(driver, saveSearchImages, 5));
	}
	
	/**
	 * @param driver
	 */
	public void clickExpandCollapseOnStatusChange(WebDriver driver) {
		if (isElementVisible(driver, expandCollapseLS, 6)) {
			for (int index = 0; index < getElements(driver, expandCollapseLS).size(); index++) {
				List<WebElement> element = getElements(driver, expandCollapseLS);
				element.get(index).click();
			}
		}
		if (isElementVisible(driver, expandCollapseOS, 6)) {
			for (int index = 0; index < getElements(driver, expandCollapseOS).size(); index++) {
				List<WebElement> element = getElements(driver, expandCollapseOS);
				element.get(index).click();
			}
		}
	}
	
	/**
	 * @param driver
	 * @return Salesforce api header visible
	 */
	public boolean verifySalesforceApiHeaderVisible(WebDriver driver) {
		boolean answer = false;
		if (isElementVisible(driver, sfdcApiLSHeader, 6)) {
			List<WebElement> element = getElements(driver, sfdcApiLSHeader);
			for (int i = 0; i < element.size(); i++) {
				assertTrue(isElementVisible(driver, element.get(i), 5));
				answer = true;
			}

		}

		if (isElementVisible(driver, sfdcApiOSHeader, 6)) {
			List<WebElement> element = getElements(driver, sfdcApiOSHeader);
			for (int i = 0; i < element.size(); i++) {
				assertTrue(isElementVisible(driver, element.get(i), 5));
				answer = true;
			}

		}

		if (!isElementVisible(driver, sfdcApiOSHeader, 6) && !isElementVisible(driver, sfdcApiLSHeader, 6)) {
			answer = false;
		}
		return answer;
	}
	
	//////////// ring DNA support /////////////
	
	public void clickRingDnaSupportTab(WebDriver driver) {
		waitUntilVisible(driver, ringDnaSupportTab);
		clickElement(driver, ringDnaSupportTab);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enterDaysPriorToToday(WebDriver driver, String text) {
		waitUntilVisible(driver, dayInputTab);
		enterText(driver, dayInputTab, text);
	}
	
	/**
	 * @param driver
	 * @return error message
	 */
	public String getErrorMessage(WebDriver driver) {
		waitUntilVisible(driver, errorMessage);
		String message = getElementsText(driver, errorMessage);
		waitUntilVisible(driver, errorCloseButton);
		clickElement(driver, errorCloseButton);
		return message;
	}
	
	public void clickUploadButton(WebDriver driver) {
		waitUntilVisible(driver, uploadButton);
		clickElement(driver, uploadButton);
	}
	
	public void dragAndDropInactiveToInProgress(WebDriver driver) {
		List<WebElement> inactive = getElements(driver, inactiveDragElement);
		List<WebElement> inProgress = getElements(driver, inProgressDragElement);
		
		dragAndDropElementWithOffSet(driver, inactive.get(0), findElement(driver, inProgressDragBoard), 0, -50);
		dashboard.isPaceBarInvisible(driver);
		
		assertEquals(inactive.size(), (getElements(driver, inactiveDragElement).size()+1));
		assertEquals(inProgress.size(), (getElements(driver, inProgressDragElement).size()-1));
		
		List<WebElement> inProgress2 = getElements(driver, inProgressDragElement);
		dragAndDropElement(driver, inProgress2.get(0),  findElement(driver, inactiveDragBoard));
		dashboard.isPaceBarInvisible(driver);
		
		assertEquals(inactive.size(), (getElements(driver, inactiveDragElement).size()));
		assertEquals(inProgress.size(), (getElements(driver, inProgressDragElement).size()));
	}
	
	/**
	 * @param driver
	 * @return is Converted Status Draggable
	 */
	public boolean isConvertedStatusDraggable(WebDriver driver) {
		return isElementVisible(driver, convertedDragElement, 6);
	}
}
