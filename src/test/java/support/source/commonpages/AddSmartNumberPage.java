package support.source.commonpages;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.internal.collections.Pair;
import org.testng.util.Strings;

import base.SeleniumBase;
import utility.HelperFunctions;

public class AddSmartNumberPage extends SeleniumBase{

	Dashboard dashboard = new Dashboard();
	//First window - choose from existing to new
	By addSmartNoHeader 				= By.xpath("//h4[text()='Search for Available Number']");
	By useExistingSmartNoRadio			= By.cssSelector("input[value='existing']");
	By createNewSmartNoRadio			= By.cssSelector("input[value='creating']");
	By nextButton						= By.cssSelector(".stepNext");
	By addSmartNoDialogueCloseButton	= By.cssSelector(".close");
	By toastMsg							= By.cssSelector(".toast-message");
	By legalText               			= By.xpath("//div[contains(text(), 'violation of federal law')]");

	//Second window - search number
	By phoneNoInputBox					= By.cssSelector("input.existing-number");
	By ownerNameInputBox				= By.cssSelector("input.owner-name");
	By phoneNoSearchButton				= By.cssSelector("button.search-existing-numbers");
	By numberHeaderType					= By.xpath(".//*[@class='existing-numbers-list']//th[contains(@class,'type')]");
	By numberHeaderOwner				= By.xpath(".//*[@class='existing-numbers-list']//th[contains(@class,'name')]");
	By numberHeaderLabel				= By.xpath(".//*[@class='existing-numbers-list']//th[contains(@class,'labelName')]");
	By numberHeaderNumber				= By.xpath(".//*[@class='existing-numbers-list']//th[contains(@class,'number')]");
	
	By phoneNoResultList				= By.xpath(".//*[@class='new-numbers-list' or @class='existing-numbers-list']//a[@class='ringdna-phone']/span | .//*[@class='new-numbers-list' or @class='existing-numbers-list']//td[contains(@class,'number') and string-length(text()) > 0]");
	By phoneNoResultType				= By.xpath(".//*[@class='new-numbers-list' or @class='existing-numbers-list']//td[contains(@class,'type')]");
	By phoneNoResultOwner				= By.xpath(".//*[@class='new-numbers-list' or @class='existing-numbers-list']//td[contains(@class,'name')]");
	By phoneNoResultLabel				= By.xpath(".//*[@class='new-numbers-list' or @class='existing-numbers-list']//td[contains(@class,'labelName')]");
	
	By addressVerificationText			= By.xpath(".//*[@class='modal-container']//div[@class='modal-body']/div[not(@class)]");
	String phoneNoReAssignButton		= ".//*[text()='$NUMBER$']/ancestor::tr//td//button[contains(@class,'reassign')]";
	String phoneNoRemoveButton			= ".//*[text()='$NUMBER$']/ancestor::tr//td//button[contains(@class,'remove')]";
	
	By backButton						= By.cssSelector("button.stepPrevious");
	By moreNumberArrow					= By.cssSelector("span.glyphicon-chevron-right");
	
	//Third window - Set label
	String numberLabelInput				= ".//*[text()='$NUMBER$']/ancestor::tr//input[contains(@class,'number-label')]";
	String removeNumberSign				= ".//*[text()='$NUMBER$']/ancestor::tr//button[contains(@class,'remove')]";
	By numberSource						= By.cssSelector(".number-source");
	By numberType						= By.cssSelector(".number-type-select");
	By saveNumber						= By.cssSelector("button.done");

	String searchNumberPage				= ".//*[text()='$NUMBER$']/ancestor::div[@class='existing-numbers-list' or @class='new-numbers-list']//td[contains(@class,'number')]";
	String searchOwnerPage				= ".//*[text()='$OWNER$']/ancestor::tr/td[contains(@class,'name')]";
	String deleteSmartNumberPage		= ".//*[text()='$NUMBER$']/ancestor::div[@class='existing-numbers-list' or @class='new-numbers-list']//span[contains(@class,'glyphicon-remove-sign')]";
	
	// ------------- For New Smart Number ----------- //
	//Second window - 
	By countrySelector					= By.cssSelector("select.countries");
	By areaCodeInput					= By.cssSelector("input.number-area-code");
	By digitsWantInput					= By.cssSelector("input.number-search-term");
	
	//Third Window
	By addBtnList						= By.cssSelector(".new-numbers-list td button.btn-primary.add");
	By existingTypeOfNumber				= By.cssSelector(".type-of-number");
	By existingTypeOfNumberOptions		= By.cssSelector(".type-of-number option");
	By addressRequirement				= By.xpath(".//*[@class='modal-body']//div[not(@class)]");
	By multipleNumberAddList			= By.xpath(".//*[text()='Remove' and not(contains(@style,'display: none'))]/ancestor::tr/td//a[contains(@class,'ringdna-phone')]");
	By removeBtnList					= By.xpath(".//*[text()='Remove' and not(contains(@style,'display: none'))]");
	By reassignBtnAdditionalNoList		= By.xpath(".//*[@class='existing-numbers-list']//td[contains(@class,'type') and text()='Additional']/parent::tr//button[contains(@class,'reassign')]");
	By additionalNumberListOfReassign	= By.xpath(".//*[@class='existing-numbers-list']//td[contains(@class,'type') and text()='Additional']/parent::tr//a[@class='ringdna-phone']/span | .//*[@class='existing-numbers-list']//td[contains(@class,'type') and text()='Additional']/parent::tr//td[contains(@class,'number') and string-length(text()) > 0]");
	String smartNoAddButton				= ".//*[text()='$NUMBER$']/ancestor::tr//button[contains(@class,'add')]";	
	
	// ------------- For Existing Smart Number ----------- //
	By reassign_Btn_List 					= By.xpath(".//*[contains(@class,'reassign-cell')]/button[contains(@class,'reassign')]");
	String existingNumberLabel				= ".//*[text()='$number$']/ancestor::tr//td[contains(@class,'labelName')]";
	String existingNumberType				= ".//*[text()='$number$']/ancestor::tr//td[contains(@class,'type')]";
	
	public static enum Type {
		Additional,
		Default,
		Tracking,
		CentralNumber,
		AdWords,
		LocalPresence,
		Unassigned
	}
	
	public static enum SmartNumberCount {
		Single,
		Multiple
	}
	
	public static enum SmartNumberHeaders {
		Type,
		Owner,
		Label,
		Number
	}
	
	public static enum Country {

	    CANADA("Canada"),
	    AUSTRALIA("Australia"),
	    UNITED_STATES("United States");

	    private String displayName;

	    Country(String displayName) {
	        this.displayName = displayName;
	    }
	    
	    public String displayName() { return displayName; }
	    // Optionally and/or additionally, toString.
	    @Override public String toString() { return displayName; }	
	};
	
	
	public enum SearchType {NEW,EXISTING}; 

	//This method is to select the smart number search type
	public void selectSmartNoSearchType(WebDriver driver, SearchType searchType) {
		if(searchType==SearchType.NEW) {
			clickElement(driver, createNewSmartNoRadio);
		} 
		if(searchType==SearchType.EXISTING) {
			clickElement(driver, useExistingSmartNoRadio);
		}
	}
	
		
	//This method is to click on next button
	public void clickNextButton(WebDriver driver) {
		waitUntilVisible(driver, nextButton);
		clickElement(driver, nextButton);
	}
	
	public List<String> getNumberList(WebDriver driver) {
		return getTextListFromElements(driver, phoneNoResultList);
	}
	
	public void enterSmartNoToSearch(WebDriver driver, String number) {
		waitUntilVisible(driver, phoneNoInputBox);
		enterText(driver, phoneNoInputBox, number);
	}
	
	public void enterOwnerNameToSearch(WebDriver driver, String ownerName) {
		waitUntilVisible(driver, ownerNameInputBox);
		enterText(driver, ownerNameInputBox, ownerName);
	}
	
	public void clickOnSearchButton (WebDriver driver) {
		waitUntilVisible(driver, phoneNoSearchButton);
		clickElement(driver, phoneNoSearchButton);
	}
	
	public boolean verifyAscendingHeaderType(WebDriver driver, Enum<?> headerType){
		By headerLoc = null;
		By headerListLoc = null;
		String headerText = null;
		SmartNumberHeaders smartNoHeader = (SmartNumberHeaders) headerType;
		
		switch(smartNoHeader){
		case Type:
			headerLoc = numberHeaderType;
			headerListLoc = phoneNoResultType;
			headerText = "type";
			break;
		case Owner:
			headerLoc = numberHeaderOwner;
			headerListLoc = phoneNoResultOwner;
			headerText = "name";
			break;
		case Label:
			headerLoc = numberHeaderLabel;
			headerListLoc = phoneNoResultLabel;
			headerText = "labelName";
			break;
		case Number:
			headerLoc = numberHeaderNumber;
			headerListLoc = phoneNoResultList;
			headerText = "number";
		default:
			break;
		}
		
		waitUntilVisible(driver, headerLoc);
		if(findElement(driver, headerLoc).getAttribute("class").equals("sortable renderable ".concat(headerText))){
			clickElement(driver, headerLoc);
		}
		else if (findElement(driver, headerLoc).getAttribute("class").contains("descending")){
			clickElement(driver, headerLoc);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, headerLoc);
		}
		
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, headerLoc).getAttribute("class").contains("ascending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, headerListLoc);
		return HelperFunctions.verifyAscendingOrderedList(afterSortElementsList);
	}
	
	public boolean verifyDescendingHeaderType(WebDriver driver, Enum<?> headerType){
		
		By headerLoc = null;
		By headerListLoc = null;
		String headerText = null;
		SmartNumberHeaders smartNoHeader = (SmartNumberHeaders) headerType;
		
		switch(smartNoHeader){
		case Type:
			headerLoc = numberHeaderType;
			headerListLoc = phoneNoResultType;
			headerText = "type";
			break;
		case Owner:
			headerLoc = numberHeaderOwner;
			headerListLoc = phoneNoResultOwner;
			headerText = "name";
			break;
		case Label:
			headerLoc = numberHeaderLabel;
			headerListLoc = phoneNoResultLabel;
			headerText = "labelName";
			break;
		case Number:
			headerLoc = numberHeaderNumber;
			headerListLoc = phoneNoResultList;
			headerText = "number";
			break;
		default:
			break;
		}
		
		waitUntilVisible(driver, headerLoc);
		if(findElement(driver, headerLoc).getAttribute("class").equals("sortable renderable ".concat(headerText))){
			clickElement(driver, headerLoc);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, headerLoc);
		}
		else if (findElement(driver, headerLoc).getAttribute("class").contains("ascending")){
			clickElement(driver, headerLoc);
		}
		
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, headerLoc).getAttribute("class").contains("descending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, headerListLoc);
		return HelperFunctions.verifyDescendingOrderedList(afterSortElementsList);
	}
	
	public void verifySmartNumberPresent(WebDriver driver, String number) {
		By numberSearchLoc = By.xpath(searchNumberPage.replace("$NUMBER$", number));
		assertTrue(isElementVisible(driver, numberSearchLoc, 15));
	}
	
	public void deleteSmartNumberPresent(WebDriver driver, String number) {
		By numberSearchLoc = By.xpath(searchNumberPage.replace("$NUMBER$", number));
		clickElement(driver, numberSearchLoc);
	}
	
	public void searchSmartNo(WebDriver driver, String number) {
		enterSmartNoToSearch(driver, number);
		clickOnSearchButton(driver);
		verifySmartNumberPresent(driver, number);
	}
	
	public void searchSmartNoByOwner(WebDriver driver, String ownerName) {
		enterOwnerNameToSearch(driver, ownerName);
		clickOnSearchButton(driver);
		idleWait(2);
		waitUntiTextAppearsInListElements(driver, phoneNoResultOwner);
		idleWait(2);
		assertTrue(isListContainsText(driver, getElements(driver, phoneNoResultOwner), ownerName));
	}

	//This method is to click on re assign button for assign it 
	public void clickOnReAssignForNumber(WebDriver driver, String number) {
		By phoneNoReAssignButton = By.xpath(this.phoneNoReAssignButton.toString().replace("$NUMBER$", number.trim()));
		By phoneNoRemoveButton = By.xpath(this.phoneNoRemoveButton.toString().replace("$NUMBER$", number.trim()));
		clickElement(driver, phoneNoReAssignButton);
		waitUntilVisible(driver, phoneNoRemoveButton, 5);
	}
	
	public boolean isReAssignBtnDisabled(WebDriver driver, String number){
		By phoneNoReAssignButton = By.xpath(this.phoneNoReAssignButton.toString().replace("$NUMBER$", number.trim()));
		return isElementDisabled(driver, phoneNoReAssignButton, 5);
	}
	
	public void enterSource(WebDriver driver, String source){
		waitUntilVisible(driver, numberSource);
		assertTrue(Strings.isNotNullAndNotEmpty(getAttribue(driver, numberSource, ElementAttributes.Placeholder)));
		enterText(driver, numberSource, source);
		assertEquals(getAttribue(driver, numberSource, ElementAttributes.value), source);
	}
	
	public void verifyDefaultSourceText(WebDriver driver){
		waitUntilVisible(driver, numberSource);
		assertEquals(getAttribue(driver, numberSource, ElementAttributes.Placeholder), "ringDNA");
	}
	
	public void enterLabel(WebDriver driver, String label, String number) {
		By numberLabelInput = By.xpath(this.numberLabelInput.replace("$NUMBER$", number.trim())); 
		waitUntilVisible(driver, numberLabelInput);
		enterText(driver, numberLabelInput, label);
	}
	
	public void enterLabelMultiple(WebDriver driver, String label, List<String> numberList) {
		for (String number : numberList) {
			By numberLabelInput = By.xpath(this.numberLabelInput.replace("$NUMBER$", number.trim()));
			waitUntilVisible(driver, numberLabelInput);
			enterText(driver, numberLabelInput, label);
			label = label.concat(HelperFunctions.GetRandomString(2));
		}
	}
	
	public void saveSmartNo(WebDriver driver) {
		waitUntilVisible(driver, saveNumber);
		clickElement(driver, saveNumber);
		waitUntilInvisible(driver, saveNumber);
	}
	
	public String getNumberType(WebDriver driver, String number) {
		searchSmartNo(driver, number);
		idleWait(2);
		return getElementsText(driver, getElements(driver, phoneNoResultType).get(0));
	}
	
	public boolean verifyNumberTypeListContainsText(WebDriver driver, String text){
		waitUntiTextAppearsInListElements(driver, phoneNoResultType);
		return isTextPresentInList(driver, getElements(driver, phoneNoResultType), text);
	}
	
	public boolean verifyReassignNumbersEnabled(WebDriver driver){
		return !isAttributePresentInList(driver, reassign_Btn_List, "disabled");
	}
	
	//Selecting country for new smart number 
	public void selectCountry(WebDriver driver, String country) {
		waitUntilVisible(driver, countrySelector, 10);
		clickElement(driver, countrySelector);
		idleWait(1);
		selectFromDropdown(driver, countrySelector, SelectTypes.visibleText, country);
	}
	
	public void enterAreaCode(WebDriver driver, String areaCode) {
		waitUntilVisible(driver, areaCodeInput);
		enterText(driver, areaCodeInput, areaCode);
	}
	
	public void enterDigitsWant(WebDriver driver, String digitsWant) {
		waitUntilVisible(driver, digitsWantInput);
		enterText(driver, digitsWantInput, digitsWant);
	}
	
	public void clickAddForNumber(WebDriver driver, String number) {
		By smartNoAddButton = By.xpath(this.smartNoAddButton.replace("$NUMBER$", number.trim()));
		By removeNumberSign = By.xpath(this.removeNumberSign.replace("$NUMBER$", number.trim()));
		clickElement(driver, smartNoAddButton);
		waitUntilVisible(driver, removeNumberSign, 5);
	}
	
	public List<String> selectMultipleNumber(WebDriver driver) {
		isListElementsVisible(driver, addBtnList, 5);
		List<WebElement> addList = getElements(driver, addBtnList);
		for(int i =0;i<2;i++) {
			clickElement(driver, addList.get(i));
		}
		return getTextListFromElements(driver, multipleNumberAddList);
	}
	
	public List<String> removeSelectedMultipleNo(WebDriver driver) {
		isListElementsVisible(driver, removeBtnList, 5);
		List<WebElement> removeBtnElements = getElements(driver, removeBtnList);
		for (WebElement removeBtn : removeBtnElements) {
			clickElement(driver, removeBtn);
		}
		return getTextListFromElements(driver, addBtnList);
	}
	
	public List<String> reassignMultipleAdditionalNumber(WebDriver driver) {
		List<WebElement> addList = getElements(driver, reassignBtnAdditionalNoList);
		for (int i = 0; i < 2; i++) {
			clickElement(driver, addList.get(i));
		}
		return getTextListFromElements(driver, multipleNumberAddList);
	}
	
	public String getAdditionalNumberFromReassignList(WebDriver driver){
		waitUntiTextAppearsInListElements(driver, additionalNumberListOfReassign);
		isListElementsVisible(driver, additionalNumberListOfReassign, 5);
		return getElements(driver, additionalNumberListOfReassign).get(0).getText();
	}
	
	public void closeSmartNumberWindow(WebDriver driver) {
		waitUntilVisible(driver, addSmartNoDialogueCloseButton);
		clickElement(driver, addSmartNoDialogueCloseButton);
		waitUntilInvisible(driver, addSmartNoDialogueCloseButton);
	}
	
	public String searchAndVerifyNumber_Owner(WebDriver driver, String ownerName) {
		selectSmartNoSearchType(driver, SearchType.EXISTING);
		clickNextButton(driver);
		idleWait(2);
		searchSmartNoByOwner(driver, ownerName);
		String number = getElements(driver, phoneNoResultList).get(0).getText();
		ownerName = getElements(driver, phoneNoResultOwner).get(0).getText();
		searchSmartNo(driver, number);
		return number;
	}
	
	public String getNumberWithoutSearchType(WebDriver driver, String ownerName) {
		searchSmartNoByOwner(driver, ownerName);
		String number = getElements(driver, phoneNoResultList).get(0).getText();
		ownerName = getElements(driver, phoneNoResultOwner).get(0).getText();
		searchSmartNo(driver, number);
		return number;
	}
	
	public void selectAndReassignNumber(WebDriver driver, String number, String label, String source, Enum<?> type) {
		selectSmartNoSearchType(driver, SearchType.EXISTING);
		clickNextButton(driver);
		searchSmartNo(driver, number);
		clickOnReAssignForNumber(driver, number);
		clickNextButton(driver);
		if(Strings.isNotNullAndNotEmpty(label))
			enterLabel(driver, label, number);

		if(Strings.isNotNullAndNotEmpty(source))
			enterSource(driver, source);

		if(type != null){
			waitUntilVisible(driver, numberType);
			selectFromDropdown(driver, numberType, SelectTypes.value, type.toString());
		}
		
		saveSmartNo(driver);
	}
	
	public List<String> getTypeOfNumberList(WebDriver driver) {
		waitUntilVisible(driver, existingTypeOfNumber);
		return getTextListFromElements(driver, existingTypeOfNumber);
	}
	
	public String getDefaultExistingTypeOfNumber(WebDriver driver){
		return getSelectClassDefaultValue(driver, existingTypeOfNumber);
	}
	
	public void selectExistingTypeOfNumber(WebDriver driver, Enum<?> existingNumberTypeEnum){
		Type existingNumberType = (Type) existingNumberTypeEnum;
		selectFromDropdown(driver, existingTypeOfNumber, SelectTypes.value, existingNumberType.name());
	}
	
	public String getExistingNumberLabelName(WebDriver driver, String smartNumber){
		By labelLoc = By.xpath(existingNumberLabel.replace("$number$", smartNumber));
		waitUntilVisible(driver, labelLoc);
		return getElementsText(driver, labelLoc);
	}
	
	public String getExistingNumberType(WebDriver driver, String smartNumber){
		By typeLoc = By.xpath(existingNumberType.replace("$number$", smartNumber));
		waitUntilVisible(driver, typeLoc);
		return getElementsText(driver, typeLoc);
	}

	public void selectAndRemoveMultipleNumbers(WebDriver driver){
		selectSmartNoSearchType(driver, SearchType.NEW);
		idleWait(1);
		clickNextButton(driver);
		waitUntilVisible(driver, countrySelector, 5);
		idleWait(1);
		clickNextButton(driver);
		selectMultipleNumber(driver);
		removeSelectedMultipleNo(driver);
		closeSmartNumberWindow(driver);
	}
	
	public void verifyValidationMsgForLongLabel(WebDriver driver, String label){
		selectSmartNoSearchType(driver, SearchType.NEW);
		clickNextButton(driver);
		idleWait(1);
		clickNextButton(driver);
		String number = getElementsText(driver, phoneNoResultList);
		clickAddForNumber(driver, number);
		clickNextButton(driver);
		enterLabel(driver, label, number);
		assertEquals(getToastmsg(driver), "");
		closeSmartNumberWindow(driver);
	}
	
	public void verifyAddressVerificationWindow(WebDriver driver, String country){
		selectSmartNoSearchType(driver, SearchType.NEW);
		clickNextButton(driver);
		selectCountry(driver, country);
		idleWait(1);
		clickNextButton(driver);
		String number = getElementsText(driver, phoneNoResultList);
		clickAddForNumber(driver, number);
		clickNextButton(driver);
		String expectedAddressText = String.format("To provision %s as a smart number, you need to provide a  local address.", number);
		waitUntilVisible(driver, addressRequirement);
		assertEquals(getElementsText(driver, addressRequirement), expectedAddressText);
		closeSmartNumberWindow(driver);
	}
	
	public String getToastmsg(WebDriver driver){
		waitUntilVisible(driver, toastMsg);
		return getElementsText(driver, toastMsg);
	}
	
	public Pair<String, List<String>> addNewSmartNumber(WebDriver driver, String country, String areaCode, String digitsWant, String label, String type, String smartNoCount) {
		List<String> multipleNumber = null;
		//selecting create new radio box
		System.out.println("Selecting  new search type");
		selectSmartNoSearchType(driver, SearchType.NEW);
		clickNextButton(driver);
		
		//Selecting country and Entering area code and digits want
		System.out.println("Giving details for new number to searchs");
		idleWait(1);
		if(Strings.isNotNullAndNotEmpty(country))
			selectCountry(driver, country);
		if(Strings.isNotNullAndNotEmpty(areaCode) && isElementVisible(driver, areaCodeInput, 5))
			enterAreaCode(driver, areaCode);
		if(Strings.isNotNullAndNotEmpty(digitsWant) && isElementVisible(driver, areaCodeInput, 5))
			enterDigitsWant(driver, digitsWant);
		
		idleWait(1);
		clickNextButton(driver);
		
		String sourceName = "AutoSource".concat(HelperFunctions.GetRandomString(3));
		String number = getElementsText(driver, phoneNoResultList);
		if (smartNoCount.equals(SmartNumberCount.Single.toString())) {
			// getting number list and adding first one
			System.out.println("Adding first number visible");
			clickAddForNumber(driver, number);
			
			if(driver.getCurrentUrl().contains("groups"))
				assertTrue(isAttributePresentInList(driver, addBtnList, "disabled"), "Button list is not disabled");
			clickNextButton(driver);
			
			if(isElementVisible(driver, numberType, 7)){
				selectFromDropdown(driver, numberType, SelectTypes.value, type);
				if(type.equals(Type.Default.toString())) {
					assertEquals(getToastmsg(driver), "Selecting a new Default Smart Number will replace your existing Default Smart Number, if present.");
					waitUntilInvisible(driver, toastMsg);
				}
			}
			
			if(isElementVisible(driver, numberSource, 5)){
				verifyDefaultSourceText(driver);
				enterSource(driver, sourceName);
			}
			
			enterLabel(driver, label, number);
		}
		else {
			multipleNumber = selectMultipleNumber(driver);
			clickNextButton(driver);
			if (isListElementsVisible(driver, numberType, 7)) {
				Select selectDropDown = selectFromDropdownList(driver, numberType, SelectTypes.value, type);
				if(type.equals(Type.Default.toString())) {
					selectDropDown = new Select(getElements(driver, numberType).get(0));
					assertTrue(selectDropDown.getFirstSelectedOption().getText().isEmpty());
					selectDropDown.selectByValue(Type.Additional.toString());
				}
			}
			//Entering label and saving the number 
			enterLabelMultiple(driver, label, multipleNumber);
		}
		System.out.println("Saving the number");
		saveSmartNo(driver);
		return new Pair<String, List<String>>(number, multipleNumber);
	} 
	
	/**
	 * @param driver
	 * @param areaCode
	 * @param labelName
	 */
	public void verifyLegalText(WebDriver driver) {
		selectSmartNoSearchType(driver, SearchType.NEW);
		clickNextButton(driver);
		waitUntilVisible(driver, legalText);
		assertTrue(getElementsText(driver, legalText).contains("ringDNA is not responsible for user manipulation of telephone numbers assigned to outbound calls. Manipulation of Caller ID information that misrepresents the location or identity of a caller is a violation of federal law."));
		closeSmartNumberWindow(driver);
	}
}