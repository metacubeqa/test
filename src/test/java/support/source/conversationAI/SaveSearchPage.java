package support.source.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class SaveSearchPage extends SeleniumBase{
	
		Dashboard dashboard = new Dashboard();

		//Create Save search create locators
		By saveSearchBtn				= By.cssSelector(".btn-primary.create-saved-search");
		By saveSearchNameTab			= By.cssSelector("#create-saved-search-modal .form-control.name");
		By createPublicVisibility		= By.cssSelector("#create-saved-search-modal [data-tab='Public']");
		By createPrivateVisibility		= By.cssSelector("#create-saved-search-modal [data-tab='Private']");
		By nextBtn						= By.cssSelector(".btn-success.next");
		By selectImmediateNotification	= By.cssSelector("#create-saved-search-modal [data-tab='Immediate']");
		By selectDailyNotification		= By.cssSelector("#create-saved-search-modal [data-tab='Daily']");
		By selectWeeklyNotification		= By.cssSelector("#create-saved-search-modal [data-tab='Weekly']");
		By skipNotifications			= By.cssSelector(".skip-notifications");
		By saveBtn						= By.cssSelector(".btn-success.save");
		By saveEditBtn					= By.cssSelector(".btn-primary.save");
	
		String libraryName 				= ".//*[@id='libs']//div[contains(@class,'name') and contains(text(),'$$libName$$')]";
		By skipAndSaveSearchBtn			= By.cssSelector(".btn-success.skip");
		
		//save search page locators
		By saveSearchHeading			= By.cssSelector(".saved-search-name");
		By backToListLink				= By.cssSelector(".back-to-list");
		By saveSearchVisibility			= By.cssSelector(".saved-search-visibility");
		By saveSearchNotifictn			= By.cssSelector(".saved-search-notification");
		By saveSearchLibrary			= By.cssSelector(".saved-search-libraries");
		By deleteSaveSearchLink			= By.cssSelector(".delete-saved-search");
		By cloneSavedSearchLink			= By.cssSelector(".clone-saved-search");
		By saveSearchEditBtn			= By.cssSelector(".saved-search-details .btn.edit-saved-search");
		By saveSearchShareBtn			= By.cssSelector(".saved-search-details .btn.share-saved-search");
		By shareMsgBox					= By.cssSelector("#share-saved-search-modal .message");
		By shareSendBtn					= By.cssSelector(".btn.btn-primary.send");
		By publicSearch					= By.cssSelector(".public-searches .search-jump");
		By trashIconList 				= By.cssSelector(".delete:not([style='display: none;']) span.glyphicon.glyphicon-trash");
		By borderedFilters				= By.xpath(".//*[@class='filter-details']//span[@class='bordered']");
		By viewsCount 					= By.cssSelector(".viewsCount");
		By searchOwner					= By.cssSelector(".search-owner");
		
		//Edit Page locators
		By privateRadioBtn				= By.cssSelector("input[type='radio'][value='Private']");
		By noneNotifictnRadioBtn		= By.cssSelector("input[type='radio'][value='None']");
		By editNameInputTab				= By.cssSelector("#edit-saved-search-modal .form-control.name");
		By addToLibInputTab				= By.cssSelector("#edit-saved-search-modal .selectize-input input");
		By editPublicVisibility			= By.cssSelector("#edit-saved-search-modal [value='Public']");
		By libDropDownList				= By.cssSelector(".multi.library-picker .selectize-dropdown-content div");
		By editPrivateVisibility		= By.cssSelector("#edit-saved-search-modal [value='Private']");
		By editNoneNotification			= By.cssSelector("#edit-saved-search-modal [value='None']");	
		By editImmediateNotification	= By.cssSelector("#edit-saved-search-modal [value='Immediate']");
		By editDailyNotification		= By.cssSelector("#edit-saved-search-modal [value='Daily']");
		By editWeeklyNotification		= By.cssSelector("#edit-saved-search-modal [value='Weekly']");
		By totalCalls					= By.cssSelector(".saved-search-details .total-calls");
		
 		By participantItems			    = By.xpath(".//*[@class='selectize-dropdown-content']/div/div");
		By participantBox 				= By.cssSelector(".selectize-control.share-picker input");
		
		By closeWindow					= By.cssSelector(".close span");
		By messageDisplay				= By.cssSelector(".toast-message");
		By confirmMessage				= By.cssSelector("[data-bb-handler='confirm']");

		String saveSearchIconImage		= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//img";
		String shareIconPage			= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//span[@title='Share']";
		String pencilIconPage			= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//span[@title='Edit']";
		String trashIconPage			= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//span[@title='Remove']";
		String viewResultsPage			= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//span[@title='View Search Results']";
		String totalCallsListPage		= ".//*[text()='$$saveSearchName$$']/ancestor::div[@class='saved-search-item']//div[contains(@class,'totalCalls')]";			
		
		String totalCallsNonListPage	= ".//*[text()='$$saveSearchName$$']/ancestor::div//h4[@class='total-calls']";
		String publicSearchPage         = ".//*[@class='saved-search-item']//h5/span[text()='$$saveSearchName$$']";
		String mySavedSearches			= ".//*[text()='My Saved Searches']/..//div[@class='saved-search-item']//span[text()='$$saveSearchName$$']";
		String publicSavedSearches		= ".//*[text()='Public Searches']/..//div[@class='saved-search-item']//span[text()='$$saveSearchName$$']";
		
		By visibleLoc;
		By notificationLoc;
		
	public static enum SaveSearchParams{
		Public,
		Private,
		Immediate,
		Daily,
		Weekly,
		None
	}

	public static enum LibraryOps{
		Remove,
		Multiple
	}
	/******* Save Search section starts here ******/
	public void clickSaveSearchOnPageBtn(WebDriver driver) {
		assertTrue(isElementVisible(driver, saveSearchBtn, 5));
		waitUntilVisible(driver, saveSearchBtn);
		clickElement(driver, saveSearchBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickNextBtn(WebDriver driver) {
		waitUntilVisible(driver, nextBtn);
		clickElement(driver, nextBtn);
	}
	
	public void clickSaveBtn(WebDriver driver) {
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
	}
	
	public void clickSaveEditBtn(WebDriver driver) {
		waitUntilVisible(driver, saveEditBtn);
		clickElement(driver, saveEditBtn);
	}
	
	public void clickEditBtn(WebDriver driver) {
		waitUntilVisible(driver, saveSearchEditBtn);
		clickElement(driver, saveSearchEditBtn);
	}
	
	public void selectNotifictn_Visibility(String searchParam) {
		switch (searchParam) {
		case "Public":
			visibleLoc = createPublicVisibility;
			break;
		case "Private":
			visibleLoc = createPrivateVisibility;
			break;
		case "None":
			notificationLoc = skipNotifications;
			break;
		case "Immediate":	
			notificationLoc = selectImmediateNotification;
			break;
		case "Daily":	
			notificationLoc = selectDailyNotification;
			break;
		case "Weekly":
			notificationLoc = selectWeeklyNotification;
			break;
		}
	}
	
	public void editNotifictn_Visibility(String searchParam) {
		switch (searchParam) {
		case "Public":
			visibleLoc = editPublicVisibility;
			break;
		case "Private":
			visibleLoc = editPrivateVisibility;
			break;
		case "None":
			notificationLoc = editNoneNotification;
			break;
		case "Immediate":	
			notificationLoc = editImmediateNotification;
			break;
		case "Daily":	
			notificationLoc = editDailyNotification;
			break;
		case "Weekly":
			notificationLoc = editWeeklyNotification;
			break;
		}
	}
	
	public void enterSaveSearchDetails(WebDriver driver, String saveSearchName, String visibiltyType, String notificationPrefernce, String library) {
		waitUntilVisible(driver, saveSearchNameTab);
		enterText(driver, saveSearchNameTab, saveSearchName);
		
		//entering visibility section
		selectNotifictn_Visibility(visibiltyType);
		waitUntilVisible(driver, visibleLoc);
		clickElement(driver, visibleLoc);
		clickNextBtn(driver);
		
		//entering notification section
		selectNotifictn_Visibility(notificationPrefernce);
		waitUntilVisible(driver, notificationLoc);
		clickElement(driver, notificationLoc);
		if (isElementVisible(driver, nextBtn, 5)) {
			clickNextBtn(driver);
		}
		
		//Clicking the selected library
		if (Strings.isNullOrEmpty(library)) {
			waitUntilVisible(driver, skipAndSaveSearchBtn);
			clickElement(driver, skipAndSaveSearchBtn);
		} else {
			By libLoc = By.xpath(libraryName.replace("$$libName$$", library));
			waitUntilVisible(driver, libLoc);
			clickElement(driver, libLoc);
			clickSaveBtn(driver);
		}
		
		//verifying save search details
		verifySaveSearchDetailsPresent(driver, saveSearchName, visibiltyType, notificationPrefernce, library);
	}
	
	public void verifySaveSearchDetailsPresent(WebDriver driver, String saveSearchName, String visibiltyType, String notificationPrefernce, String library){
		dashboard.isPaceBarInvisible(driver);
		isSaveSearchMessageDisappeared(driver);
		waitUntilVisible(driver, deleteSaveSearchLink);
		assertEquals(getElementsText(driver, saveSearchHeading), saveSearchName);
		assertEquals(getElementsText(driver, saveSearchVisibility), visibiltyType); 
		assertEquals(getElementsText(driver, saveSearchNotifictn), notificationPrefernce.concat(" Notifications"));
		if(Strings.isNotNullAndNotEmpty(library)) {
			assertEquals(getElementsText(driver, saveSearchLibrary), "New Recordings added to ".concat(library));
		}
		assertTrue(isElementVisible(driver, deleteSaveSearchLink, 5)); 
		assertTrue(isElementVisible(driver, cloneSavedSearchLink, 5));
		assertTrue(isElementVisible(driver, saveSearchEditBtn, 5));
		if(visibiltyType.equals(SaveSearchParams.Private.toString()))
			assertTrue(isElementVisible(driver, saveSearchShareBtn, 5));
	}
	
	public void verifySaveSearchDetailsForSharedViewer(WebDriver driver, String saveSearchName, String visibiltyType, String notificationPrefernce, String library, String agentName){
		dashboard.isPaceBarInvisible(driver);
		isSaveSearchMessageDisappeared(driver);
		assertEquals(getElementsText(driver, saveSearchHeading), saveSearchName);
		assertEquals(getElementsText(driver, saveSearchVisibility), visibiltyType); 
		assertEquals(getElementsText(driver, saveSearchNotifictn), notificationPrefernce.concat(" Notifications"));
		if(Strings.isNotNullAndNotEmpty(library)) {
			assertEquals(getElementsText(driver, saveSearchLibrary), "New Recordings added to ".concat(library));
		}
		assertTrue(isElementVisible(driver, cloneSavedSearchLink, 5));
		verifyCreatedBy(driver, agentName);
	}
	
	public boolean verifySaveSearchOnPage(WebDriver driver, String saveSearchName) {
		By publicSearchLoc = By.xpath(publicSearchPage.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, publicSearchLoc, 5);
	}
	
	public boolean verifyMySavedSearchOnPage(WebDriver driver, String saveSearchName) {
		By mySavedSearchLoc = By.xpath(mySavedSearches.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, mySavedSearchLoc, 5);
	}
	
	public boolean verifyPublicSaveSearchOnPage(WebDriver driver, String saveSearchName) {
		By publicSearchLoc = By.xpath(publicSavedSearches.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, publicSearchLoc, 5);
	}
	
	public void saveSearchButtonNotVisible(WebDriver driver) {
		assertFalse(isElementVisible(driver, saveSearchBtn, 5));
	}
	
	public boolean isShareIconVisible(WebDriver driver, String saveSearchName) {
		By shareIconLoc = By.xpath(shareIconPage.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, shareIconLoc, 5);
	}
	
	public void selectShareParticipant(WebDriver driver, String participant) {
		enterTextAndSelectFromDropDown(driver, participantBox, participantBox, participantItems, participant);
	}
	
	public void verifyShareParticipantNotVisible(WebDriver driver, String participant) {
		waitUntilVisible(driver, participantBox);
		enterText(driver, participantBox, participant);
		assertFalse(isListElementsVisible(driver, participantItems, 5));
		waitUntilVisible(driver, closeWindow);
		clickElement(driver, closeWindow);
	}
	
	public boolean isSaveSearchImageVisible(WebDriver driver, String saveSearchName) {
		By saveSearchIconLoc = By.xpath(saveSearchIconImage.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, saveSearchIconLoc, 5);
	}
	
	public void verifyCallerCanEditDetails(WebDriver driver, String saveSearchName) {
		//verifying pencil icon visible
		By pencilIconLoc = By.xpath(pencilIconPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, pencilIconLoc);
		assertTrue(isElementVisible(driver, pencilIconLoc, 5));
		
		//verifying trash icon visible
		By trashIconLoc =  By.xpath(trashIconPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, pencilIconLoc);
		assertTrue(isElementVisible(driver, trashIconLoc, 5));
	}
	
	public void verifyNonCallerCannotEditDetails(WebDriver driver, String saveSearchName) {
		// verifying pencil icon not visible
		By pencilIconLoc = By.xpath(pencilIconPage.replace("$$saveSearchName$$", saveSearchName));
		assertFalse(isElementVisible(driver, pencilIconLoc, 2));

		// verifying trash icon not visible
		By trashIconLoc = By.xpath(trashIconPage.replace("$$saveSearchName$$", saveSearchName));
		assertFalse(isElementVisible(driver, trashIconLoc, 2));
	}
	
	public void verifyTotalCalls(WebDriver driver, String saveSearchName) {
		By totalCallsListLoc = By.xpath(totalCallsListPage.replace("$$saveSearchName$$", saveSearchName));
		String totalCallsList = getElementsText(driver, totalCallsListLoc);
		
		//navigating to view results
		goToViewResultsPageSaveSearch(driver, saveSearchName);
		
		//getting total calls count
		By totalCallsNonListLoc = By.xpath(totalCallsNonListPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, totalCallsNonListLoc);
		String totalCallsNonList = getElementsText(driver, totalCallsNonListLoc);
		
		//verifying total count
		assertEquals(totalCallsList, totalCallsNonList, "Total calls count do not match");
	}
	
	public void goToListSection(WebDriver driver) {
		waitUntilVisible(driver, backToListLink);
		clickElement(driver, backToListLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyBorderedFiltersNonClickable(WebDriver driver) {
		for (WebElement borderedFilterLoc : getElements(driver, borderedFilters)) {
			assertNotEquals(borderedFilterLoc.getTagName(), "button", "Element is clickable");
			assertNotEquals(borderedFilterLoc.getTagName(), "a", "Element is clickable");
		}
	}
	
	public boolean isViewResultsPageVisible(WebDriver driver, String saveSearchName) {
		By viewResultLoc = By.xpath(viewResultsPage.replace("$$saveSearchName$$", saveSearchName));
		return isElementVisible(driver, viewResultLoc, 5);
	}
	
	public void goToViewResultsPageSaveSearch(WebDriver driver, String saveSearchName) {
		By viewResultLoc = By.xpath(viewResultsPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, viewResultLoc);
		clickElement(driver, viewResultLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickShareIconSaveSearch(WebDriver driver, String saveSearchName) {
		By shareIconLoc = By.xpath(shareIconPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, shareIconLoc);
		clickElement(driver, shareIconLoc);
	}
	
	public void clickPencilSaveSearch(WebDriver driver, String saveSearchName) {
		By pencilIconLoc = By.xpath(pencilIconPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, pencilIconLoc);
		clickElement(driver, pencilIconLoc);
	}
	
	public void clickTrashIconSaveSearch(WebDriver driver, String saveSearchName) {
		idleWait(1);
		By trashIconLoc = By.xpath(trashIconPage.replace("$$saveSearchName$$", saveSearchName));
		waitUntilVisible(driver, trashIconLoc);
		clickElement(driver, trashIconLoc);
		clickConfirmMessage(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickDeleteSearchLink(WebDriver driver) {
		waitUntilVisible(driver, deleteSaveSearchLink);
		clickElement(driver, deleteSaveSearchLink);
		clickConfirmMessage(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickCloneSearchLink(WebDriver driver) {
		waitUntilVisible(driver, cloneSavedSearchLink);
		waitUntilVisible(driver, totalCalls);
		clickElement(driver, cloneSavedSearchLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickConfirmMessage(WebDriver driver) {
		waitUntilVisible(driver, confirmMessage);
		clickElement(driver, confirmMessage);
		waitUntilInvisible(driver, confirmMessage);
	}
	
	public void verifyEditDetailsSaved(WebDriver driver, String saveSearchName) {
		assertEquals(getAttribue(driver, editNameInputTab, ElementAttributes.value), saveSearchName);
		waitUntilVisible(driver, closeWindow);
		clickElement(driver, closeWindow);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void isSaveSearchMessageDisappeared(WebDriver driver){
		waitUntilInvisible(driver, messageDisplay);
	}
	
	public String editLibraryDetails(WebDriver driver, String param) {
		String library = "";
		if (param.equals("Remove")) {
			waitUntilVisible(driver, addToLibInputTab);
			clickElement(driver, addToLibInputTab);
			enterBackspace(driver, addToLibInputTab);
		}
		else if(param.equals("Multiple")){
			waitUntilVisible(driver, addToLibInputTab);
			clickElement(driver, addToLibInputTab);
			waitUntilVisible(driver, libDropDownList);
			library = getElements(driver, libDropDownList).get(0).getText();
			getElements(driver, libDropDownList).get(0).click();
		}
		return library;
	}
	
	public String editSaveSearchDetails(WebDriver driver, String saveSearchName, String updatedSaveSearchName, String visibiltyType, String notificationPrefernce, String selectLib) {
		idleWait(2);
		enterText(driver, editNameInputTab, updatedSaveSearchName);
		String librarySelectd =  editLibraryDetails(driver, selectLib);
		if(Strings.isNotNullAndNotEmpty(visibiltyType)) {
			editNotifictn_Visibility(visibiltyType);
			waitUntilVisible(driver, visibleLoc);
			clickElement(driver, visibleLoc);
		}
		if(Strings.isNotNullAndNotEmpty(notificationPrefernce)) {
			editNotifictn_Visibility(notificationPrefernce);
			waitUntilVisible(driver, notificationLoc);
			clickElement(driver, notificationLoc);
		}
		clickSaveEditBtn(driver);
		dashboard.isPaceBarInvisible(driver);
		isSaveSearchMessageDisappeared(driver);
		return librarySelectd;
	}
	
	public void clickShareBtn(WebDriver driver) {
		waitUntilVisible(driver, saveSearchShareBtn);
		clickElement(driver, saveSearchShareBtn);
	}
	
	public void clickShareSendBtn(WebDriver driver) {
		waitUntilVisible(driver, shareSendBtn);
		clickElement(driver, shareSendBtn);
	}
	
	public void enterShareDetails(WebDriver driver, String shareMsg, String participant) {
		waitUntilVisible(driver, shareMsgBox);
		enterText(driver, shareMsgBox, shareMsg);
		selectShareParticipant(driver, participant);
		clickShareSendBtn(driver);
	}
	
	public int getViewCount(WebDriver driver){
		waitUntilVisible(driver, viewsCount);
		String arr[] = getElementsText(driver, viewsCount).split("views");
		return Integer.parseInt(arr[0].trim());
	}
	
	public void verifyViewCount(WebDriver driver, int beforeCount, int afterCount){
		assertEquals(afterCount, beforeCount+1, "After count not increased");
	}
	
	public void verifyCreatedBy(WebDriver driver, String name){
		waitUntilVisible(driver, searchOwner);
		assertEquals(getElementsText(driver, searchOwner), String.format("Created by: %s", name));
	}
	
	public void verifyAfterSharingSaveSearchOnPage(WebDriver driver) {
		assertFalse(isElementVisible(driver, deleteSaveSearchLink, 2));
		assertFalse(isElementVisible(driver, saveSearchEditBtn, 2));
		assertFalse(isElementVisible(driver, saveSearchShareBtn, 2));
	}
	
	public void deleteAllSaveSearch(WebDriver driver) {
		if (isListElementsVisible(driver, trashIconList, 5)) {
			int i = getElements(driver, trashIconList).size() - 1;
			while (i >= 0) {
				clickElement(driver, getElements(driver, trashIconList).get(i));
				clickConfirmMessage(driver);
				idleWait(2);
				i--;
			}
		}
	}
}
