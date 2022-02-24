package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.source.salesforce.ContactDetailPage;
import utility.HelperFunctions;

public class SoftPhoneContactsPage extends SeleniumBase{

	CallScreenPage callScreenPage 		= new CallScreenPage();
	ContactDetailPage contactDetailPage = new ContactDetailPage();
	SoftPhoneCalling softPhoneCalling	= new SoftPhoneCalling();	
	
	By nonActiveContactsIcon 			= By.cssSelector(".contacts");
	By searchContactTextBox 			= By.cssSelector("#contact-search #search-container input.search-term");
	By searchRingDNARadioButton		 	= By.id("searchRingDNA");
	By searchSalesforceRadioButton 		= By.id("searchSalesforce");
	By searchButton 					= By.cssSelector("[data-action='search']");
	By searchContactsLink				= By.cssSelector(".search-tab.active");
	By favoriteContactsLink				= By.cssSelector(".favorite-tab.active");
	By addNewContactBtn					= By.className("add-record");
	
	//Salesforce Search Results
	By sfContactResultName 				= By.cssSelector("#contact-search-results .name.canOpen");
	By sfContactCallButton				= By.cssSelector("#contact-search-results .call[data-action='dial-phone'] img");
	By sfContactOpenCallScreenButton 	= By.cssSelector("#contact-search-results [data-action='view-crm']");
	By sfLeadOpenCallScreenButton 		= By.cssSelector("#lead-search-results [data-action='view-crm']");
	By sfLeadResultName					= By.cssSelector("#lead-search-results .name.canOpen");
	By sfLeadCallButton					= By.cssSelector("#lead-search-results .call[data-action='dial-phone'] img");
	By searchResultCompanyName 			= By.cssSelector("#search-results-container .company");
	By sfAccountName					= By.cssSelector("#account-search-results .name.canOpen");
	By sfContactInactiveFavIcon			= By.cssSelector("#contact-search-results .favorite-action .create");
	By sfContactActiveFavIcon			= By.cssSelector("#contact-search-results .favorite-action .delete");
	By sfLeadInactiveFavIcon			= By.cssSelector("#lead-search-results .favorite-action .create");
	By sfLeadActiveFavIcon				= By.cssSelector("#lead-search-results .favorite-action .delete");
	By sfAccountInactiveFavIcon			= By.cssSelector("#account-search-results .favorite-action .create");
	By sfAccountActiveFavIcon			= By.cssSelector("#account-search-results .favorite-action .delete");
	By noResultFoundText				= By.id("no-search-results");
	By searchedUserMsgIcon				= By.cssSelector("#search-results-container .quick-sms:not([style='display: none;']) img");
	By searchedLeadUserMsgIcon          = By.cssSelector("#lead-search-results .pull-right.simple-btn.nav-crm");
	String searchResultSmsIcon			= ".//*[@class='phone-number' and text() ='$$PhoneNumber$$']/..//*[@class='quick-sms']/img";
	By allResultName					= By.cssSelector("#all-search-results .name.canOpen");
	By allRdnaResultName				= By.cssSelector(".name:not(.canOpen)");
	String favContacttSmsIcon			= ".//*[@id='all-favorites-results']//*[@class='phone-number' and text() ='$$PhoneNumber$$']/..//*[@class='quick-sms']/img";
	String leadWithName					= ".//*[@id='lead-search-results']//*[contains(@class,'name') and contains(@class,'canOpen') and text() = '$$LeadName$$']";
	By otherNumber						= By.xpath(".//*[@data-content='O:']/following-sibling::a[@class='phone-number']");
	
	//RingDNA Search Results
	By ringDNAUserSearchResultName		= By.cssSelector("#user-search-results .name");
	By ringDNAUserSearchCallButton 		= By.cssSelector("#user-search-results .call[data-action='dial-phone'] img");
	By ringDNAUserGroupResultName		= By.cssSelector("#queue-search-results .name");
	By ringDNAUserGroupCallButton		= By.cssSelector("#queue-search-results .call[data-action='dial-phone'] img");
	By ringDNAUserInactiveFavIcon		= By.cssSelector("#user-search-results .favorite-action .create");
	By ringDNAUserActiveFavIcon			= By.cssSelector("#user-search-results .favorite-action .delete");
	By ringDNAUserGroupInactiveFavIcon	= By.cssSelector("#group-search-results .favorite-action .create");
	By ringDNAUserGroupActiveFavIcon	= By.cssSelector("#group-search-results .favorite-action .delete");
	By searchNames						= By.cssSelector("#search-results-container .name.canOpen");
	By resultStatus						= By.cssSelector(".record-users-icon:not([style='display: none;']) img");
	By resultActiveStatus				= By.cssSelector(".record-users-icon:not([style='display: none;']) img[data-original-title='Available']");
	By resultLoggedOutStatus			= By.cssSelector("img[data-original-title='Not logged in']");
	By resultOnCall						= By.cssSelector(".record-users-icon:not([style='display: none;']) img[data-original-title='Busy/On Call']");
	By contactSearchResultsRow			= By.cssSelector("#contact-search-results .contact-item.list-item");
	By toolTipText						= By.cssSelector(".tooltip-inner");

	By favConSearchTextBox				= By.className("favorite-search-term");
	By favConSearchBtn					= By.cssSelector(".favorite-search");
	By favoriteTabActiveFavIcon			= By.cssSelector("#favorites-results-container .favorite-action .delete");
	By favoriteTabContactNames			= By.cssSelector("#favorites-results-container .name.canOpen");
	By addFavoriteManuallyButton		= By.cssSelector(".glyphicon.glyphicon-plus");
	By addFavContactName				= By.cssSelector(".favorite-name");
	By addFavContactNumber				= By.cssSelector(".favorite-number");
	By addFavContactSaveBtn				= By.cssSelector(".btn.save");
	By favouriteContactRow				= By.cssSelector("#favorites-results-container .contact-item.list-item");
	By favConCallBtn					= By.cssSelector(".call img");
	By favConEmailBtn					= By.cssSelector(".hasEmail img[src='images/contact-btn-email-v2.svg']");
	By favOpenCallScreenButton 			= By.cssSelector("#all-favorites-results [data-action='view-crm']");
	By loadMoreFavContactBtn			= By.cssSelector(".favorite-more");
	
	By freqContactLeadOpenBtn			= By.xpath(".//*[@id='frequent-contacts']//div[div/*[contains(@class, 'isLead') and not(@style=\"display: none;\")]]/button[@data-action='view-crm']");
	By freqContactLeadname				= By.xpath(".//*[@id='frequent-contacts']//div[div/*[contains(@class, 'isLead') and not(@style=\"display: none;\")]]/div[@class='name canOpen']");
	By freqContactOpenBtn				= By.xpath(".//*[@id='frequent-contacts']//div[div/*[contains(@class, 'isContact') and not(@style=\"display: none;\")]]/button[@data-action='view-crm']");
	By freContactName					= By.xpath(".//*[@id='frequent-contacts']//div[div/*[contains(@class, 'isContact') and not(@style=\"display: none;\")]]/div[@class='name canOpen']");
	
	By fadeEffect	= By.cssSelector(".modal-backdrop.fade");
	By relatedRecordsIcon = By.cssSelector(".related-records-container");
	
	
	
	public static enum searchTypes{
		Contacts,
		Leads,
		Accounts,
		Users,
		Groups,
	}
	
	public void clickActiveContactsIcon(WebDriver driver){
		waitUntilVisible(driver, nonActiveContactsIcon);
		clickElement(driver, nonActiveContactsIcon);
		if(isElementVisible(driver, searchContactsLink, 0)){
			clickElement(driver, searchContactsLink);
		}
		findElement(driver, searchContactTextBox);
	}
	
	public void navigateToFavoritePage(WebDriver driver){
		if(isElementVisible(driver, favoriteContactsLink, 0)){
			clickByJs(driver, favoriteContactsLink);
		}
	}
	
	public void clickAddNewRecordButton(WebDriver driver) {
		waitUntilVisible(driver, addNewContactBtn);
		clickElement(driver, addNewContactBtn);
	}
	
	public void verifyAdvanceSearchOptionNotAvailable(WebDriver driver) {
		waitUntilInvisible(driver, searchRingDNARadioButton);
		waitUntilInvisible(driver, searchSalesforceRadioButton);
	}
	
	public void verifyAdvanceSearchOptionIsAvailable(WebDriver driver) {
		waitUntilVisible(driver, searchRingDNARadioButton);
		waitUntilVisible(driver, searchSalesforceRadioButton);
	}
	
	public void selectRingDNAOption(WebDriver driver){
		waitUntilVisible(driver, searchRingDNARadioButton);
		scrollToElement(driver, searchRingDNARadioButton);
		clickElement(driver, searchRingDNARadioButton);
	}
	
	public void selectSalesforceOption(WebDriver driver){
		waitUntilVisible(driver, searchSalesforceRadioButton);
		scrollToElement(driver, searchSalesforceRadioButton);
		clickByJs(driver, searchSalesforceRadioButton);
	}
	
	public void enterSearchContactText(WebDriver driver, String searchContact){
		waitUntilInvisible(driver, By.className("logo-loader"));
		enterText(driver, searchContactTextBox, searchContact);
	}
	
	public void clickSearchContactButton(WebDriver driver){
		clickElement(driver, searchButton);
	}
	
	public void verifySalesforceSearchString(WebDriver driver, String searchString){
		assertTrue(findElement(driver, searchSalesforceRadioButton).isSelected());
		assertTrue(getAttribue(driver, searchContactTextBox, ElementAttributes.value).contains(searchString));
	}
	
	public void verifyRingdnaSearchString(WebDriver driver, String searchString){
		assertTrue(findElement(driver, searchRingDNARadioButton).isSelected());
		assertEquals(getAttribue(driver, searchContactTextBox, ElementAttributes.value), searchString);
	}
	
	public void isRingDNASearchOptionSelected(WebDriver driver){
		assertTrue(findElement(driver, searchRingDNARadioButton).isSelected());
	}
	
	//Salesforce search results
	
	public List<WebElement> getSfdcContactsResultNames(WebDriver driver){
		waitUntilVisible(driver, sfContactResultName);
		return getElements(driver, sfContactResultName);
	}
	
	public List<WebElement> getSfdcLeadsResultNames(WebDriver driver){
		waitUntilVisible(driver, sfLeadResultName);
		return getElements(driver, sfLeadResultName);
	}
	
	public List<WebElement> getSfdcAccountResultNames(WebDriver driver){
		waitUntilVisible(driver, sfAccountName);
		return getElements(driver, sfAccountName);
	}
	
	public List<String> getAllResultNamesList(WebDriver driver){
		waitUntilVisible(driver, allResultName);
		return getTextListFromElements(driver, allResultName);
	}
	
	public List<String> getAllRingDnaNamesList(WebDriver driver){
		waitUntilVisible(driver, allRdnaResultName);
		return getTextListFromElements(driver, allRdnaResultName);
	}
	
	public int getSfdcResultsNameIndex(WebDriver driver, String contactName, String searchType){
		List<WebElement> nameInSearchResults = null;
		if(searchType.equals(searchTypes.Contacts.toString())){
			nameInSearchResults = getSfdcContactsResultNames(driver);
		}else if(searchType.equals(searchTypes.Leads.toString())){
			nameInSearchResults = getSfdcLeadsResultNames(driver);
		}else if(searchType.equals(searchTypes.Accounts.toString())){
			nameInSearchResults = getSfdcAccountResultNames(driver);
		}
		for(int i=0; i<nameInSearchResults.size();i++){
			if(nameInSearchResults.get(i).getText().contains(contactName)){
				return i;
			}
		}
		return -1;
	}	
	
	public void searchAndVerifyAccounts(WebDriver driver, String accountName){
		List<WebElement> searchResultsList = getElements(driver, searchResultCompanyName);
		for(int i=0; i<searchResultsList.size();i++){
			assertTrue(searchResultsList.get(i).getText().contains(accountName));
		}
	}
	
	public void clicksfdcResultsCallButton(WebDriver driver, int callIndex, String searchType){
		List<WebElement> searchResultsCallButtons = null;
		if(searchType.equals(searchTypes.Contacts.toString())){
			searchResultsCallButtons = getElements(driver, sfContactCallButton);
		}else if(searchType.equals(searchTypes.Leads.toString())){
			searchResultsCallButtons = getElements(driver, sfLeadCallButton);
		}
		searchResultsCallButtons.get(callIndex).click();
	}
	
	public void searchSalesForce(WebDriver driver, String searchString){
		// opening contact search
		System.out.println("Opening contact search page");
		clickActiveContactsIcon(driver);

		// Selecting RingDNA search
		System.out.println("Selecting RingDNA radio button ");
		selectSalesforceOption(driver);

		// searching a user
		System.out.println("Searching an user");
		enterSearchContactText(driver, searchString);
		clickSearchContactButton(driver);
	}
	
	public int salesforceSearch(WebDriver driver, String searchString, String searchType){
		searchSalesForce(driver, searchString);

		// verifying the results
		System.out.println("Verifying the results");
		if(searchType.equals(searchTypes.Accounts.toString())){
			searchAndVerifyAccounts(driver, searchString);
		}
		
		int contactIndex = getSfdcResultsNameIndex(driver, searchString, searchType);
		assertNotEquals(contactIndex, -1);
		return contactIndex;
	}
	
	public void openSfContactDetails(WebDriver driver, String searchString){
		int contactIndex = salesforceSearch(driver, searchString, searchTypes.Contacts.toString());
		getElements(driver, sfContactOpenCallScreenButton).get(contactIndex).click();
	}
	
	public void openSfLeadDetails(WebDriver driver, String searchString){
		int contactIndex = salesforceSearch(driver, searchString, searchTypes.Leads.toString());
		getElements(driver, sfLeadOpenCallScreenButton).get(contactIndex).click();
	}
	
	public void salesforceSearchAndCall(WebDriver driver, String searchString, String searchType){
		int contactIndex = salesforceSearch(driver, searchString, searchType);
		clicksfdcResultsCallButton(driver, contactIndex, searchType);
	}
	
	public void salesforceSetFavourite(WebDriver driver, String searchString, String searchType){
		int contactIndex = salesforceSearch(driver, searchString, searchType);
		List<WebElement> favIconResults = null;
		if(searchType.equals(searchTypes.Contacts.toString())){
			favIconResults = getInactiveElements(driver, sfContactInactiveFavIcon);
		}else if(searchType.equals(searchTypes.Leads.toString())){
			favIconResults = getInactiveElements(driver, sfLeadInactiveFavIcon);
		}else if(searchType.equals(searchTypes.Accounts.toString())){
			favIconResults = getInactiveElements(driver, sfAccountInactiveFavIcon);
		}
		if(favIconResults.get(contactIndex).isDisplayed()){
			clickElement(driver, favIconResults.get(contactIndex));
		}
	}
	
	public void salesforceRemoveFavourite(WebDriver driver, String searchString, String searchType){
		int contactIndex = salesforceSearch(driver, searchString, searchType);
		List<WebElement> favIconResults = null;
		if(searchType.equals(searchTypes.Contacts.toString())){
			favIconResults = getInactiveElements(driver, sfContactActiveFavIcon);
		}else if(searchType.equals(searchTypes.Leads.toString())){
			favIconResults = getInactiveElements(driver, sfLeadActiveFavIcon);
		}else if(searchType.equals(searchTypes.Accounts.toString())){
			favIconResults = getInactiveElements(driver, sfAccountActiveFavIcon);
		}
		if(favIconResults.get(contactIndex).isDisplayed()){
			clickElement(driver, favIconResults.get(contactIndex));
		}
	}
	
	public void clickLoadMorFavContacteBtn(WebDriver driver) {
		waitUntilVisible(driver, loadMoreFavContactBtn);
		clickByJs(driver, loadMoreFavContactBtn);
	}
	
	public boolean isLoadMorFavContacteBtnVisible(WebDriver driver) {
		return isElementVisible(driver, loadMoreFavContactBtn, 1);
	}
	
	public Boolean isSalesforceContactPresent(WebDriver driver, String searchString){
		searchSalesForce(driver, searchString);
		for(int i=0; i<20;i++){
			idleWait(1);
			if(isElementVisible(driver, searchNames, 0)){
				return true;
			}else if(isElementVisible(driver, noResultFoundText, 0)){
				return false;
			}
		}
		return false;
	}
	
	public String getOtherNumber(WebDriver driver, int index) {
		waitUntilVisible(driver, otherNumber);
		return getElementsText(driver, getElements(driver, otherNumber).get(index));
	}
	
	//Ring DNA search results
	public List<WebElement> getRdnaUserResultNames(WebDriver driver){
		waitUntilVisible(driver, ringDNAUserSearchResultName);
		return getElements(driver, ringDNAUserSearchResultName);
	}
	
	public List<WebElement> getRdnaUserGroupResultNames(WebDriver driver){
		waitUntilVisible(driver, ringDNAUserGroupResultName);
		return getElements(driver, ringDNAUserGroupResultName);
	}
	
	public void isResultStatusAvailable(WebDriver driver, String contactName, String searchType){
		int index = getRdnaResultsNameIndex(driver, contactName, searchType);
		assertTrue(getElements(driver, resultActiveStatus).get(index).isDisplayed());
		hoverElement(driver, getElements(driver, resultActiveStatus).get(index));
		assertEquals(getElementsText(driver, toolTipText), "Available");
	}
	
	public void isResultStatusLoggedOut(WebDriver driver, String contactName, String searchType){
		int index = getRdnaResultsNameIndex(driver, contactName, searchType);
		assertTrue(getElements(driver, resultLoggedOutStatus).get(index).isDisplayed());
		hoverElement(driver, getElements(driver, resultLoggedOutStatus).get(index));
		assertEquals(getElementsText(driver, toolTipText), "Not logged in");
	}
	
	public void isResultStatusBusyOnCall(WebDriver driver, String contactName, String searchType){
		int index = getRdnaResultsNameIndex(driver, contactName, searchType);
		assertTrue(getElements(driver, resultOnCall).get(index).isDisplayed());
		hoverElement(driver, getElements(driver, resultOnCall).get(index));
		assertEquals(getElementsText(driver, toolTipText), "Busy/On Call");
	}
	
	public void isResultStatusRed(WebDriver driver, String contactName, String searchType){
		int index = getRdnaResultsNameIndex(driver, contactName, searchType);
		assertTrue(getElements(driver, resultOnCall).get(index).isDisplayed());
	}
	
	public String getUsersStatus(WebDriver driver, String contactName, String searchType){
		int index = getRdnaResultsNameIndex(driver, contactName, searchType);
		assertTrue(getElements(driver, resultStatus).get(index).isDisplayed());
		hoverElement(driver, getElements(driver, resultStatus).get(index));
		return getElementsText(driver, toolTipText);
	}
	
	public int getRdnaResultsNameIndex(WebDriver driver, String contactName, String searchType){
		List<WebElement> nameInSearchResults = null;
		if(searchType.equals(searchTypes.Users.toString())){
			nameInSearchResults = getRdnaUserResultNames(driver);
		}else if(searchType.equals(searchTypes.Groups.toString())){
			nameInSearchResults = getRdnaUserGroupResultNames(driver);
		}
		for(int i=0; i<nameInSearchResults.size();i++){
			if(nameInSearchResults.get(i).getText().contains(contactName)){
				return i;
			}
		}
		return -1;
	}
	
	public void clickRdnaResultsCallButton(WebDriver driver, int callIndex, String searchType){
		List<WebElement> searchResultsCallButtons = null;
		if(searchType.equals(searchTypes.Users.toString())){
			searchResultsCallButtons = getElements(driver, ringDNAUserSearchCallButton);
		}else if(searchType.equals(searchTypes.Groups.toString())){
			searchResultsCallButtons = getElements(driver, ringDNAUserGroupCallButton);
		}
		searchResultsCallButtons.get(callIndex).click();
	}
	
	public int ringDNASearch(WebDriver driver, String userName, String searchType){
		// opening contact search
		System.out.println("�pening contact search page");
		clickActiveContactsIcon(driver);

		// Selecting RingDNA search
		System.out.println("Selecting RingDNA radio button ");
		selectRingDNAOption(driver);

		//searching a user
		System.out.println("Searching an user");
		enterSearchContactText(driver, userName);
		clickSearchContactButton(driver);

		// verifying the results
		System.out.println("Verifying the results");
		int contactIndex = getRdnaResultsNameIndex(driver, userName, searchType);
		assertNotEquals(contactIndex, -1);
		return contactIndex;
	}

	public void ringDNASearchAndCall(WebDriver driver, String userName, String searchType){
		int contactIndex = ringDNASearch(driver, userName, searchType);
		clickRdnaResultsCallButton(driver, contactIndex, searchType);
	}
	
	public void ringDNASetFavourite(WebDriver driver, String searchString, String searchType){
		int contactIndex = ringDNASearch(driver, searchString, searchType);
		List<WebElement> favIconResults = null;
		if(searchType.equals(searchTypes.Users.toString())){
			favIconResults = getInactiveElements(driver, ringDNAUserInactiveFavIcon);
		}else if(searchType.equals(searchTypes.Groups.toString())){
			favIconResults = getInactiveElements(driver, ringDNAUserGroupInactiveFavIcon);
		}
		if(favIconResults.get(contactIndex).isDisplayed()){
			clickElement(driver, favIconResults.get(contactIndex));
		}
	}
	
	public void ringDNARemoveFavourite(WebDriver driver, String searchString, String searchType){
		int contactIndex = ringDNASearch(driver, searchString, searchType);
		List<WebElement> favIconResults = null;
		if(searchType.equals(searchTypes.Users.toString())){
			favIconResults = getInactiveElements(driver, ringDNAUserActiveFavIcon);
		}else if(searchType.equals(searchTypes.Groups.toString())){
			favIconResults = getInactiveElements(driver, ringDNAUserGroupActiveFavIcon);
		}
		if(favIconResults.get(contactIndex).isDisplayed()){
			clickElement(driver, favIconResults.get(contactIndex));
		}
	}
	
	//Favorite Contacts
	public void searchFavContact(WebDriver driver, String favContactToSearch) {
		enterText(driver, favConSearchTextBox, favContactToSearch);
		clickElement(driver, favConSearchBtn);
	}
	
	public List<String> getFavoriteContactsList(WebDriver driver){
		return getTextListFromElements(driver, favoriteTabContactNames);
	}
	
	public void verifyFavContactInvisible(WebDriver driver, String favContact){
		for (int j = 0; j < 10; j++) {
			if(getTextListFromElements(driver, favoriteTabContactNames).indexOf(favContact) == -1)
				return;
			idleWaitInMs(500);
		}
		Assert.fail("Favourite contact " + favContact + " not removed");
	}
	
	public int getFavoriteContactIdex(WebDriver driver, String searchString){
		clickActiveContactsIcon(driver);
		idleWait(1);
		navigateToFavoritePage(driver);
		idleWait(2);
		List<WebElement> favoriteContacts = getElements(driver, favoriteTabContactNames);
		for(int i=0; i<favoriteContacts.size();i++){
			if(favoriteContacts.get(i).getText().contains(searchString)){
				return i;
			}
		}
		return -1;
	}
	
	public void clickFavoriteContactName(WebDriver driver, String searchString){
		List<WebElement> favoriteContacts = getElements(driver, favoriteTabContactNames);
		for (WebElement webElement : favoriteContacts) {
			if(getElementsText(driver, webElement).contains(searchString)) {
				clickElement(driver, webElement);
				return;
			}
		}
		Assert.fail();
	}
	
	public void openFavContactDetails(WebDriver driver, String searchString){
		int index = getFavoriteContactIdex(driver, searchString);
		getInactiveElements(driver, favOpenCallScreenButton).get(index).click();
	}
	
	public Boolean clickFavConCallBtn(WebDriver driver, String searchString){
		int index = getFavoriteContactIdex(driver, searchString);
		System.out.println(index);
		if(getElements(driver, favouriteContactRow).get(index).findElement(favConCallBtn).isDisplayed()) {
			getElements(driver, favouriteContactRow).get(index).findElement(favConCallBtn).click();
			return true;
		}else {
			return false;
		}
	}
	
	public Boolean clickConCallBtn(WebDriver driver, String searchString, searchTypes searchType){
		int index = getSfdcResultsNameIndex(driver, searchString, searchType.toString());
		System.out.println(index);
		if(getElements(driver, contactSearchResultsRow).get(index).findElement(favConCallBtn).isDisplayed()) {
			getElements(driver, contactSearchResultsRow).get(index).findElement(favConCallBtn).click();
			return true;
		}else {
			return false;
		}
	}
	
	public Boolean isContactEmailVisible(WebDriver driver, String searchString, searchTypes searchType){
		int index = getSfdcResultsNameIndex(driver, searchString, searchType.toString());
		System.out.println(index);
		if(getElements(driver, contactSearchResultsRow).get(index).findElement(favConEmailBtn).isDisplayed()) {
			return true;
		}else {
			return false;
		}
	}
	
	public Boolean isFavConEmailVisible(WebDriver driver, String searchString, searchTypes searchType){
		int index = getFavoriteContactIdex(driver, searchString);
		System.out.println(index);
		if(getElements(driver, favouriteContactRow).get(index).findElement(favConEmailBtn).isDisplayed()) {
			return true;
		}else {
			return false;
		}
	}
	
	public void isContactFavorite(WebDriver driver, String searchString){
		assertNotEquals(getFavoriteContactIdex(driver, searchString), -1);
	}
	
	public void isContactNotFavorite(WebDriver driver, String searchString){
		assertEquals(getFavoriteContactIdex(driver, searchString), -1);
	}
	
	public void removeContactFavorite(WebDriver driver, String searchString){
		int contactIndex = getFavoriteContactIdex(driver, searchString);
		clickActiveFavIcon(driver, contactIndex);
	}
	
	public void clickActiveFavIcon(WebDriver driver, int index) {
		clickByJs(driver, getElements(driver, favoriteTabActiveFavIcon).get(index));
	}
	
	public void addFavContactManually(WebDriver driver, String name, String Number){
		clickActiveContactsIcon(driver);
		navigateToFavoritePage(driver);
		clickAddFavContactBtn(driver);
		addFavContactManualDetails(driver, name, Number);
	}
	
	public void clickAddFavContactBtn(WebDriver driver) {
		clickElement(driver, addFavoriteManuallyButton);
	}
	
	public void addFavContactManualDetails(WebDriver driver, String name, String Number) {
		waitUntilVisible(driver, addFavContactName);
		enterText(driver, addFavContactName, name);
		enterText(driver, addFavContactNumber, Number);
		clickElement(driver, addFavContactSaveBtn);
		waitUntilVisible(driver, addFavContactName);
		waitUntilClickable(driver, addFavContactNumber);
		waitUntilInvisible(driver, addFavContactSaveBtn);
		waitUntilInvisible(driver, fadeEffect);
	}
	
	public void clickFavConMsgIconByNumber(WebDriver driver, String number){
		By msgIcon = By.xpath(favContacttSmsIcon.replace("$$PhoneNumber$$", number));
		clickElement(driver, msgIcon);
		waitUntilVisible(driver, callScreenPage.callerName);
	}
	
	public void verifyMsgIconOnFavConactVisible(WebDriver driver, String number){
		By msgIcon = By.xpath(favContacttSmsIcon.replace("$$PhoneNumber$$", number));
		waitUntilVisible(driver, msgIcon);
	}
	
	public void verifyMsgIconOnFavConactInvisible(WebDriver driver, String number){
		By msgIcon = By.xpath(favContacttSmsIcon.replace("$$PhoneNumber$$", number));
		waitUntilInvisible(driver, msgIcon);
	}

	//other Methods		
	public void searchUntilContactPresent(WebDriver driver, String searchTerm) {
		clickActiveContactsIcon(driver);
		selectSalesforceOption(driver);
		enterSearchContactText(driver, searchTerm);
		for(int i=0; i<5; i++) {
			clickSearchContactButton(driver);
			waitUntilInvisible(driver, spinnerWheel);
			if(!isElementVisible(driver, searchNames, 3) && !isElementVisible(driver, sfContactResultName, 0)) {
				idleWait(2);
				continue;
			} else {
				System.out.println("Contact is visible now");
				break;
			}
		}		
	}
	
	public void openContactDetailInSFDC(WebDriver driver, WebElement element) {
		clickByJs(driver, element);
		switchToSFTab(driver, getTabCount(driver));
		idleWait(1);
	}
	
	public void clickMessageIconByNumber(WebDriver driver, String number){
		By msgIcon = By.xpath(searchResultSmsIcon.replace("$$PhoneNumber$$", number));
		clickElement(driver, msgIcon);
		waitUntilVisible(driver, callScreenPage.callerName);
	}
	
	public void verifyMsgIconOnContactSearchVisible(WebDriver driver, String number){
		By msgIcon = By.xpath(searchResultSmsIcon.replace("$$PhoneNumber$$", number));
		waitUntilVisible(driver, msgIcon);
	}
	
	public void verifyMsgIconOnContactSearchInvisible(WebDriver driver, String number){
		By msgIcon = By.xpath(searchResultSmsIcon.replace("$$PhoneNumber$$", number));
		waitUntilInvisible(driver, msgIcon);
	}
	
	public void clickFirstContactsMsgIcon(WebDriver driver){
		getInactiveElements(driver, searchedUserMsgIcon).get(0).click();
		waitUntilVisible(driver, callScreenPage.callerName);
	}
	
	public void clickFirstLeadContactsMsgIcon(WebDriver driver){
		List<WebElement> leads = getElements(driver, searchedLeadUserMsgIcon);
		clickByJs(driver, leads.get(0));
		waitUntilVisible(driver, callScreenPage.callerName);
	}
	
	public Boolean isContactMultiple(WebDriver driver){
		if(getWebelementIfExist(driver, searchNames)!=null && getElements(driver, searchNames).size()>1) {
			return true;
		}
		return false;
	}
	
	public void convertMultipleToSingle(WebDriver driver, String searchTerm) {
		//Opening contact search window 
		clickActiveContactsIcon(driver);
		//selecting salesforce radio box
		selectSalesforceOption(driver);
		//entering search term
		enterSearchContactText(driver, searchTerm);
		//clicking on search button
		clickSearchContactButton(driver);
		
		//deleting contacts 
		if(getWebelementIfExist(driver, searchNames)!=null && getElements(driver, searchNames).size()>1) {
			switchToTab(driver, 1);
			for(int i=1; i<getElements(driver, searchNames).size(); i++) {
				openContactDetailInSFDC(driver, getElements(driver, searchNames).get(i));
				contactDetailPage.deleteContact(driver);
				driver.close();
				switchToTab(driver, 1);
			}
		}
	}
	
	public void searchUntilContacIsMultiple(WebDriver driver, String searchTerm) {
		clickActiveContactsIcon(driver);
		selectSalesforceOption(driver);
		enterSearchContactText(driver, HelperFunctions.getNumberInSimpleFormat(searchTerm));
		for(int i=0; i<10; i++) {
			clickSearchContactButton(driver);
			if(getWebelementIfExist(driver, searchNames)==null || !(getElements(driver, searchNames).size()>1)) {
				idleWait(2);
				continue;
			} else {
				System.out.println("Contact is multiple now");
				break;
			}
		}		
	}
	
	public void deleteContactUsingIndex(WebDriver driver, String searchTerm, int index) {
		// Opening contact search window
		clickActiveContactsIcon(driver);
		// selecting salesforce radio box
		selectSalesforceOption(driver);
		// entering search term
		enterSearchContactText(driver, searchTerm);
		// clicking on search button
		clickSearchContactButton(driver);

		deleteSFContact(driver, index);
	}
	
	public void deleteSFContact(WebDriver driver, int index){
		if(index > -1){
			openContactDetailInSFDC(driver, getElements(driver, searchNames).get(index));
			contactDetailPage.deleteContact(driver);
			driver.close();
			switchToTab(driver, 1);	
		}else{
			System.out.println("no salesforce contact present");
		}
	}
	
	public void addContactIfNotExist(WebDriver driver, String contactNumber, String contactFirstName){
		String contactName = contactFirstName.trim() + " Automation";
		String contactNumm = HelperFunctions.getNumberInSimpleFormat(contactNumber);
		if(isSalesforceContactPresent(driver, contactNumm)){
			if(isContactMultiple(driver)){
				convertMultipleToSingle(driver, contactNumm);
				isSalesforceContactPresent(driver, contactNumm);
				if(!getElementsText(driver, searchNames).equals(contactName)) {
					deleteSFContact(driver, 0);
					softPhoneCalling.softphoneAgentCall(driver, contactNumber);
					softPhoneCalling.isMuteButtonEnables(driver);
					softPhoneCalling.hangupActiveCall(driver);	
					callScreenPage.addCallerAsContact(driver, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
				}
			}else if(!getElementsText(driver, searchNames).equals(contactName)) {
				deleteSFContact(driver, 0);
				softPhoneCalling.softphoneAgentCall(driver, contactNumber);
				softPhoneCalling.isMuteButtonEnables(driver);
				softPhoneCalling.hangupActiveCall(driver);	
				callScreenPage.addCallerAsContact(driver, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
			}
		}else{
			// Calling from Agent's SoftPhone
			softPhoneCalling.softphoneAgentCall(driver, contactNumber);
			softPhoneCalling.isMuteButtonEnables(driver);
			softPhoneCalling.hangupActiveCall(driver);	
			callScreenPage.addCallerAsContact(driver, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
		}
		callScreenPage.closeErrorBar(driver);
	}
	
	public void deleteAndAddContact(WebDriver driver, String contactNumber, String contactFirstName){
		deleteAllContacts(driver, contactNumber, contactFirstName);
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver, contactNumber);
		softPhoneCalling.isMuteButtonEnables(driver);
		softPhoneCalling.hangupIfInActiveCall(driver);
		callScreenPage.addCallerAsContact(driver, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
		callScreenPage.closeErrorBar(driver);
		waitUntilVisible(driver, relatedRecordsIcon);
		searchUntilContactPresent(driver, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	}
	
	public void deleteAndAddLead(WebDriver driver, String contactNumber, String contactFirstName){
		deleteAllContacts(driver, contactNumber, contactFirstName);
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver, contactNumber);
		softPhoneCalling.isMuteButtonEnables(driver);
		softPhoneCalling.hangupActiveCall(driver);
		callScreenPage.addCallerAsLead(driver, contactFirstName, SoftphoneBase.CONFIG.getProperty("contact_account_name"));
		callScreenPage.closeErrorBar(driver);
		waitUntilVisible(driver, relatedRecordsIcon);
		searchUntilContactPresent(driver, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	}
	
	public void deleteAllContacts(WebDriver driver, String contactNumber, String contactFirstName) {
		if(isSalesforceContactPresent(driver, HelperFunctions.getNumberInSimpleFormat(contactNumber))){
			convertMultipleToSingle(driver, HelperFunctions.getNumberInSimpleFormat(contactNumber));
			openContactDetailInSFDC(driver, getElements(driver, searchNames).get(0));
			contactDetailPage.deleteContact(driver);
			driver.close();
			switchToTab(driver, 1);
		}
	}
	
	public void searchUntilLeadPresent(WebDriver driver, String number, String leadName){
		clickActiveContactsIcon(driver);
		selectSalesforceOption(driver);
		enterSearchContactText(driver, number);
		By leadNameList = By.xpath(leadWithName.replace("$$LeadName$$", leadName));
		for(int i=0; i<5; i++) {
			clickSearchContactButton(driver);
			waitUntilInvisible(driver, spinnerWheel);
			if(!isElementVisible(driver, leadNameList, 3)) {
				idleWait(2);
				continue;
			} else {
				System.out.println("Contact is visible now");
				break;
			}	
		}
	}
	
	public void deleteAllSFLeads(WebDriver driver, String leadName) {
		By leadNameList = By.xpath(leadWithName.replace("$$LeadName$$", leadName));
		if(getWebelementIfExist(driver, leadNameList)!=null) {
			for(int i=0; i<getElements(driver, leadNameList).size(); i++) {
				openContactDetailInSFDC(driver, getElements(driver, leadNameList).get(i));
				contactDetailPage.deleteContact(driver);
				driver.close();
				switchToTab(driver, 1);
			}
		}
	}
	
	public String openFreqCotactedLeadDetails(WebDriver driver) {
		waitUntilVisible(driver, freqContactLeadOpenBtn);
		scrollToElement(driver, getElements(driver, freqContactLeadOpenBtn).get(0));
		String name = getElements(driver, freqContactLeadname).get(0).getText();
		clickByJs(driver, getElements(driver, freqContactLeadOpenBtn).get(0));
		return name;
	}
	
	public String openFreqCotactedContactDetails(WebDriver driver) {
		waitUntilVisible(driver, freqContactOpenBtn);
		scrollToElement(driver, getElements(driver, freqContactOpenBtn).get(0));
		String name = getElements(driver, freContactName).get(0).getText();
		clickByJs(driver, getElements(driver, freqContactOpenBtn).get(0));
		return name;
	}
}
