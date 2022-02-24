/**
 * 
 */
package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

/**
 * @author Abhishek
 *
 */
public class AccountBlockedNumbersTab extends SeleniumBase{

	Dashboard dashBoard = new Dashboard();
	
	By blockedNumbersTab				= By.cssSelector("[data-tab='blocked-numbers']");
	By blockedNumbersHeading			= By.xpath(".//h2[text() = 'Blocked Numbers']");
	By phoneNumberTextBox				= By.cssSelector("input.phone-number");
	By searchBlockType					= By.cssSelector(".number-type");
	By searchUserNameTab				= By.cssSelector(".user-picker input");
	By searchBtn						= By.cssSelector("button.search");
	By searchedNumberList				= By.cssSelector("td.number");
	By confirmDeleteBtn					= By.cssSelector("[data-bb-handler='confirm']");
	By blockForDefault					= By.cssSelector(".blocked-number-editor .user-picker .selectize-control .has-items .item");
	By editNumberUserDrobDown			= By.cssSelector(".blocked-number-editor .user-picker .selectize-control");
	By editNumberUserTextBox			= By.cssSelector(".blocked-number-editor .user-picker .selectize-input input");
	By editNumberUserDropDownItems		= By.cssSelector(".blocked-number-editor .user-picker .selectize-dropdown-content div");
	By editNumberSaveBtn				= By.cssSelector(".blocked-number-editor .btn-success");
	By deleteBlockNumber				= By.cssSelector(".glyphicon-remove-sign");
	String deleteNumberBtn				= ".//*[text()= '$$Number$$']/ancestor::tr//td//a[@class='delete']/span";
	String editNumberBtn				= ".//*[text()='$$Number$$']/ancestor::tr//td//a[@class='edit']";
	String numberDirection				= ".//*[text()='$number$']/parent::tr//td[contains(@class,'direction')]";
	
	//Add Block Number section
	By blockCallByDispositionCheckBox   = By.cssSelector(".allow-block-call.toggle-switch");
	By blockCallDispositionToggleOn		= By.xpath("//input[@class='allow-block-call toggle-switch']/..//label[contains(@class,'toggle-on')]");
	By blockCallDispositionToggleOff	= By.xpath("//input[@class='allow-block-call toggle-switch']/..//label[contains(@class,'toggle-off')]");
	
	By addNumberIcon					= By.cssSelector(".glyphicon-plus-sign");
	By addNoInputTextBox				= By.cssSelector(".blocked-number-editor .number");
	By selectBlockType					= By.xpath("//label[text()='Block Type']/..//div/select");
	By selectDirection					= By.cssSelector(".form-control.direction");
	By closeWindow						= By.cssSelector(".close span");
	
	public static enum BlockType{
		All,
		Call,
		Message,
		Recording
	}
	
	public static enum Direction{
		Inbound,
		Outbound
	}
	
	public void navigateToBlockedNumbersTab(WebDriver driver){
		dashBoard.isPaceBarInvisible(driver);
		clickElement(driver, blockedNumbersTab);
		dashBoard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, blockedNumbersHeading);
	}
	
	public void addBlockedNumber(WebDriver driver, String numberToBlock, BlockType blockType, String blockUserName, String direction) {
		if(Strings.isNullOrEmpty(blockUserName)){
			blockUserName = "All";
		}
		idleWait(1);
		clickAddNoIcon(driver);
		waitUntilVisible(driver, addNoInputTextBox);
		enterText(driver, addNoInputTextBox, numberToBlock);
		selectFromDropdown(driver, selectBlockType, SelectTypes.visibleText, blockType.toString());
		if(blockType.toString().equals("Message")) {
			enterBlockForText(driver, blockUserName);
			selectFromDropdown(driver, selectDirection, SelectTypes.visibleText, direction);
		}
		clickElement(driver, editNumberSaveBtn);
		waitUntilInvisible(driver, editNumberSaveBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void verifyBlockForDetails(WebDriver driver, BlockType blockType){
		idleWait(2);
		clickAddNoIcon(driver);
		waitUntilVisible(driver, addNoInputTextBox);
		assertTrue(isElementVisible(driver, blockForDefault, 10));
		assertEquals(getElementsText(driver, blockForDefault), "All");
		selectFromDropdown(driver, selectBlockType, SelectTypes.visibleText, blockType.toString());
		if(blockType.toString().equals("Recording")){
			assertFalse(isElementVisible(driver, blockForDefault, 5));
		}
		clickElement(driver, closeWindow);
		waitUntilInvisible(driver, closeWindow);
	}
	
	public void enterBlockForText(WebDriver driver, String blockForUser) {
		waitUntilVisible(driver, editNumberUserDrobDown);
		clickElement(driver, editNumberUserDrobDown);
		enterText(driver, editNumberUserTextBox, blockForUser);
		clickEnter(driver, editNumberUserTextBox);
	}

	public void clickAddNoIcon(WebDriver driver) {
		waitUntilVisible(driver, addNumberIcon);
		clickElement(driver, addNumberIcon);
	}
	
	public void searchBlockedNumber(WebDriver driver, String Number){
		waitUntilVisible(driver, searchBtn);
		enterText(driver, phoneNumberTextBox, Number);
		clickElement(driver, searchBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void searchBlockedNumberByType(WebDriver driver, BlockType type){
		waitUntilVisible(driver, searchBtn);
		selectFromDropdown(driver, searchBlockType, SelectTypes.value, type.toString());
		clickElement(driver, searchBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void searchBlockedNumberByUserName(WebDriver driver, String userName){
		waitUntilVisible(driver, searchBtn);
		enterText(driver, searchUserNameTab, userName);
		clickEnter(driver, searchUserNameTab);
		clickElement(driver, searchBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public boolean isNumberBlocked(WebDriver driver, String Number){
		boolean value = false;
		searchBlockedNumber(driver, Number);
		if(isListElementsVisible(driver, searchedNumberList, 5)) {
			value = getTextListFromElements(driver, searchedNumberList).get(0).contains(Number.trim());
		}
		return value;
	}
	
	public List<String> getNumberDirectionList(WebDriver driver, String number) {
		By numberDirectionLoc = By.xpath(numberDirection.replace("$number$", number));
		return getTextListFromElements(driver, numberDirectionLoc);
	}
	
	public void deleteBlockedNumber(WebDriver driver, String Number){
		searchBlockedNumber(driver, Number);
		By deleteBlockedNumberBtn = By.xpath(deleteNumberBtn.replace("$$Number$$", Number.trim()));
		waitUntilVisible(driver, deleteBlockedNumberBtn);
		clickElement(driver, deleteBlockedNumberBtn);
		confirmDeleteAction(driver); 
	}
	
	public void deleteAllBlockedNumber(WebDriver driver) {
		scrollTillEndOfPage(driver);
		if (isListElementsVisible(driver, deleteBlockNumber, 2)) {
			int i = getElements(driver, deleteBlockNumber).size() - 1;
			while (i >= 0) {
				clickElement(driver, getElements(driver, deleteBlockNumber).get(i));
				confirmDeleteAction(driver);
				idleWait(2);
				i--;
			}
		}
	}
		
	public void confirmDeleteAction(WebDriver driver){
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void editBlockForNumber(WebDriver driver, String Number, String blockForUser){
		searchBlockedNumber(driver, Number);
		By editBlockedNumberBtn = By.xpath(editNumberBtn.replace("$$Number$$", Number.trim()));
		waitUntilVisible(driver, editBlockedNumberBtn);
		clickElement(driver, editBlockedNumberBtn);
		waitUntilVisible(driver, editNumberSaveBtn);
		waitUntilVisible(driver, editNumberUserDrobDown);
		clickElement(driver, editNumberUserDrobDown);
		enterBackspace(driver, editNumberUserTextBox);
		enterText(driver, editNumberUserTextBox, blockForUser);
		clickElement(driver, editNumberSaveBtn);
		waitUntilInvisible(driver, editNumberSaveBtn);
	}
	
	public void editBlockNumber(WebDriver driver, String oldNumber, String newNumber, String blockForUser, BlockType blockType) {
		By editBlockedNumberBtn = By.xpath(editNumberBtn.replace("$$Number$$", oldNumber.trim()));
		waitUntilVisible(driver, editBlockedNumberBtn);
		clickElement(driver, editBlockedNumberBtn);
		waitUntilVisible(driver, editNumberSaveBtn);
		waitUntilVisible(driver, addNoInputTextBox);
		enterText(driver, addNoInputTextBox, newNumber);
		selectFromDropdown(driver, selectBlockType, SelectTypes.visibleText, blockType.toString());
		if(isElementVisible(driver, editNumberUserDrobDown, 5)) {
			waitUntilVisible(driver, editNumberUserDrobDown);
			clickElement(driver, editNumberUserDrobDown);
			enterBackspace(driver, editNumberUserTextBox);
			enterText(driver, editNumberUserTextBox, blockForUser);
		}
		clickElement(driver, editNumberSaveBtn);
		waitUntilInvisible(driver, editNumberSaveBtn);
	}
	
	public void enableBlockCallByDispositionSetting(WebDriver driver){
		if(!findElement(driver, blockCallByDispositionCheckBox).isSelected()) {
			clickElement(driver, blockCallDispositionToggleOff);
			System.out.println("Enabled block call by disposition setting");
		} else {
			System.out.println("Already enabled block call by disposition setting");
		}
	}
	
	public void disableCallDispositionRequiredSetting(WebDriver driver){
		if(findElement(driver, blockCallByDispositionCheckBox).isSelected()) {
			clickElement(driver, blockCallDispositionToggleOn);
			System.out.println("Disabled block call by disposition setting");
		} else {
			System.out.println("Already disabled block call by disposition setting");
		}
	}
}
