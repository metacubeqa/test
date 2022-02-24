/**
 * 
 */
package softphone.source.salesforce;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import base.SeleniumBase;
import softphone.source.CallScreenPage;

/**
 * @author Abhishek
 *
 */
public class SalesforceHomePage extends SeleniumBase{
	
	TaskDetailPage taskDetailPage = new TaskDetailPage();
	salesforceCampaign sfCampaignPage = new salesforceCampaign();
	CallScreenPage callScreenPage = new CallScreenPage();
	
	By pageHeader 				= By.className("pageDescription");
	By pageType					= By.className("pageType");
	By homeTabLink				= By.cssSelector("[href='/home/home.jsp']");
	By createNewBtn				= By.id("createNewButton");
	By taskListIcon				= By.className("listViewIcon");
	By userNavButton			= By.cssSelector("#userNavButton");
	By setupLink				= By.cssSelector("#setupLink");
	By setupLinkInMenuDrop		= By.cssSelector("a.menuButtonMenuLink[title='Setup']");
	
	By setupSearchInput			= By.cssSelector("#setupSearch");
	
	By activityFilterDropdown	= By.cssSelector("select.title");
	String tasklinkLocator		= ".//div[contains(@class,'TASK_SUBJECT')]/a[text()='$$Subject$$']";
	
	By salesForceTitle			= By.cssSelector("[title='Salesforce.com']");
	By lightningPopUp			= By.xpath(".//lightning-icon[contains(@class,'closeIcon')]//*[name()='svg']");
	By lightningProfile			= By.cssSelector(".photoContainer.forceSocialPhoto");
	By switchToClassicLink		= By.xpath(".//*[contains(@class, 'switch-to-aloha') and text()='Switch to Salesforce Classic']");
	By switchToLightningLink	= By.cssSelector(".switch-to-lightning");
	By recentItemsList			= By.cssSelector(".mruList .mruItem a");
	By lightningDialogueCloseButton = By.cssSelector("#tryLexDialog [title='Close']");
	
	By profileEditBtn			= By.xpath(".//*[text()='Profile Detail']/../following-sibling::td/input[@value=' Edit ']");
	By profileSaveBtn			= By.xpath(".//*[text()='Profile Edit']/../following-sibling::td/input[@title='Save']");
	By profileNameTab			= By.cssSelector("input#Name[type='text'][name='Name']");
	String profileNameListGrid	= ".//*[@class='listBody']//div//td[contains(@class,'x-grid3-td-ProfileName')]//span[contains(text(),'$profileName$')]";
	
	String childWithoutMainContainerSetup	= ".//*[@class='childContainer']//a[text()='$container$']";
	String childWithMainContainerSetup		= ".//div[@class='childContainer' and @id='$mainContainer$"+"_child']/div/a[text()='$subContainer$']";

	//Opportunities Fields Section
	String opportunityFieldLabel = "//a[contains(@href, 'setupid=OpportunityFields') and text()='$fieldLabel$']";
	String apiNameListAccToType  = "//td[text()='$type$']//preceding::td[1]";
	
	//recycle bin
	By recycleLink				 = By.cssSelector(".recycleText");
	By recycleBinRowChkBxs 		 = By.cssSelector("tr.dataRow input[type='checkbox']");
	By undeleteBtn				 = By.cssSelector("[title='Undelete']");
	
	//developer console section
	By headerNameDropDown		 = By.cssSelector("#globalHeaderNameMink, #userNavLabel");
	By devConsole				 = By.cssSelector("[title='Developer Console (New Window)']");
	
	By debugMenu				 = By.cssSelector("#debugMenuEntry");
	By executeAnonyWindow		 = By.cssSelector("#openExecuteAnonymousWindow-textEl");
	By executeButton			 = By.xpath("//button//span[text()='Execute']");
	
	public void verifyPageHeadingType(WebDriver driver, String pageType, String pageHeading) {
		waitUntilVisible(driver, this.pageHeader);
		assertEquals(getElementsText(driver, this.pageType), pageType);
		assertEquals(getElementsText(driver, this.pageHeader), pageHeading);
	}

	public void switchToClassicMode(WebDriver driver){
		//switch back to classic if in lightning
		if(isElementVisible(driver, lightningProfile, 0)){
			clickElement(driver, lightningProfile);
			waitUntilVisible(driver, switchToClassicLink);
			clickElement(driver, switchToClassicLink);
			waitUntilVisible(driver, salesForceTitle);
			waitUntilInvisible(driver, lightningProfile);
		}
		isSpinnerWheelInvisible(driver);
	}
	
	public void closeLightningPopUp(WebDriver driver) {
		if(isElementVisible(driver, lightningProfile, 5)) {
			if(isElementVisible(driver, lightningPopUp, 10)) {
				int count = 0 ;
				do{
					clickElement(driver, lightningPopUp);
					idleWait(1);
					count++;
				}while(isElementVisible(driver, lightningPopUp, 0) && count < 2);
			}
		}
	}
	
	public void closeLightningDialogueBoxForFirstTime(WebDriver driver){
		if(isElementVisible(driver, lightningDialogueCloseButton, 5)){
			clickElement(driver, lightningDialogueCloseButton);
		}
	}
	
	public void switchToLightningMode(WebDriver driver){
		//switch back to lightning if in classic
		if(isElementVisible(driver, switchToLightningLink, 5)){
			clickElement(driver, switchToLightningLink);
			waitUntilVisible(driver, lightningProfile);
			waitUntilInvisible(driver, salesForceTitle);
		}
		isSpinnerWheelInvisible(driver);
	}
	
	/**method to click setup link on sf page
	 * @param driver
	 */
	public void clikSetupLinkOption(WebDriver driver) {
		if (isElementVisible(driver, setupLink, 5)) {
			clickElement(driver, setupLink);
		} else {
			waitUntilVisible(driver, userNavButton);
			clickElement(driver, userNavButton);
			waitUntilVisible(driver, setupLinkInMenuDrop);
			clickElement(driver, setupLinkInMenuDrop);
		}
	}
	
	/**
	 * method to setup search and select container 
	 * @param driver
	 * @param container
	 */
	public void enterSetupSearchAndSelectContainer(WebDriver driver, String container){
		clikSetupLinkOption(driver);
		enterText(driver, setupSearchInput, container);
		By childContainerLoc = By.xpath(childWithoutMainContainerSetup.replace("$container$", container));
		waitUntilVisible(driver, childContainerLoc);
		clickElement(driver, childContainerLoc);
	}
	
	/**
	 * method to setup search and select sub container
	 * @param driver
	 * @param searchName
	 * @param mainContainer
	 * @param subContainer
	 */
	public void enterSetupSearchAndSelectSubContainer(WebDriver driver, String searchName, String mainContainer, String subContainer){
		clikSetupLinkOption(driver);
		enterText(driver, setupSearchInput, searchName);
		By childContainerLoc = By.xpath(childWithMainContainerSetup.replace("$mainContainer$", mainContainer).replace("$subContainer$", subContainer));
		waitUntilVisible(driver, childContainerLoc);
		clickElement(driver, childContainerLoc);
	}
	
	public void clickProfileName(WebDriver driver, String profileName){
		By profileLoc = By.xpath(profileNameListGrid.replace("$profileName$", profileName));
		waitUntilVisible(driver, profileLoc);
		clickElement(driver, profileLoc);
	}
	
	public void clickEditProfileDetailBtn(WebDriver driver){
		waitUntilVisible(driver, profileEditBtn);
		clickElement(driver, profileEditBtn);
	}
	
	public void editProfileName(WebDriver driver, String profileName){
		clickEditProfileDetailBtn(driver);
		waitUntilVisible(driver, profileNameTab);
		enterText(driver, profileNameTab, profileName);
		waitUntilVisible(driver, profileSaveBtn);
		clickElement(driver, profileSaveBtn);
		waitUntilInvisible(driver, profileSaveBtn);
		waitUntilVisible(driver, pageHeader);
		assertEquals(getElementsText(driver, pageHeader), profileName);
	}
	
	public boolean isSFPageInLightningMode(WebDriver driver) {
		return isElementVisible(driver, lightningProfile, 5);
	}
	
	public void openCampaignAndSwitchToLightningSF(WebDriver driver) {
		sfCampaignPage.openSalesforceCampaignPage(driver);
		sfCampaignPage.waitForPageLoaded(driver);
		switchToLightningMode(driver);
		sfCampaignPage.closeTab(driver);
		sfCampaignPage.switchToTab(driver, sfCampaignPage.getTabCount(driver));
	}
	
	public void openCampaignAndSwitchToClassicSF(WebDriver driver) {
		sfCampaignPage.openSalesforceCampaignPage(driver);
		sfCampaignPage.waitForPageLoaded(driver);
		switchToClassifcSF(driver);
	}
	
	public void switchToClassifcSF(WebDriver driver) {
		closeLightningPopUp(driver);
		closeLightningDialogueBoxForFirstTime(driver);
		switchToClassicMode(driver);
		clickHomeTabLink(driver);
		sfCampaignPage.closeTab(driver);
		sfCampaignPage.switchToTab(driver, sfCampaignPage.getTabCount(driver));
	}
	
	public void clickHomeTabLink(WebDriver driver){
		waitUntilVisible(driver, homeTabLink);
		clickElement(driver, homeTabLink);
		waitUntilVisible(driver, createNewBtn);
		closeLightningDialogueBoxForFirstTime(driver);
	}
	
	public void clicktaskListIcon(WebDriver driver){
		waitUntilVisible(driver, taskListIcon);
		clickElement(driver, taskListIcon);
		waitUntilVisible(driver, activityFilterDropdown);
	}
	
	public void openTaskBySubject(WebDriver driver, String subject){
		By taskLink	= By.xpath(tasklinkLocator.replace("$$Subject$$", subject));
		waitUntilVisible(driver, taskLink);
		clickElement(driver, taskLink);
		taskDetailPage.verifyAssignedTofieldVisible(driver);
	}

	public void openTaskFromTaskList(WebDriver driver, String subject){
		clickHomeTabLink(driver);
		clicktaskListIcon(driver);
		openTaskBySubject(driver, subject);
	}
	
	public void clickOpportunityFieldLabel(WebDriver driver, String fieldLabel) {
		By fieldLabelLoc = By.xpath(opportunityFieldLabel.replace("$fieldLabel$", fieldLabel));
		waitUntilVisible(driver, fieldLabelLoc);
		clickByJs(driver, fieldLabelLoc);
		waitUntilVisible(driver, pageHeader);
		assertEquals(getElementsText(driver, pageHeader), fieldLabel);
	}

	public List<String> getApiNameListAccToType(WebDriver driver, String type) {
		By apiListLoc = By.xpath(apiNameListAccToType.replace("$type$", type));
		waitUntilVisible(driver, apiListLoc);
		return getTextListFromElements(driver, apiListLoc);
	}
	
	public void clickRecyCleBinLink(WebDriver driver) {
		clickElement(driver, recycleLink);
		waitUntilVisible(driver, undeleteBtn);
	}
	
	public void undeleteRecycleBinItem(WebDriver driver, int index) {
		getElements(driver, recycleBinRowChkBxs).get(index).click();
		clickElement(driver, undeleteBtn);
		waitForPageLoaded(driver);
	}
	
	///developer console section starts here
	
	public void navigateToDevConsole(WebDriver driver) {
		waitUntilVisible(driver, headerNameDropDown);
		clickElement(driver, headerNameDropDown);
		String parentWindow = driver.getWindowHandle();
		waitUntilVisible(driver, devConsole);
		clickElement(driver, devConsole);
		switchToWindowTitleContains("Developer Console", driver);
		driver.manage().window().maximize();
		waitForPageLoaded(driver);
	}
	
	public void executeCodeInAnonymousWindow(WebDriver driver, String code) {
		navigateToDevConsole(driver);
		waitUntilVisible(driver, debugMenu);
		clickElement(driver, debugMenu);
		waitUntilVisible(driver, executeAnonyWindow);
		clickElement(driver, executeAnonyWindow);
		waitForPageLoaded(driver);
		enterCodeInExecuteWindow(driver, code);
	}
	
	public void enterCodeInExecuteWindow(WebDriver driver, String code) {
		Actions actions = new Actions(driver);

		By linesBy = By.cssSelector("#ExecAnon .CodeMirror-code pre>span");    
		List<WebElement> lines = driver.findElements(linesBy);

		System.out.println("");
		while (lines.size() > 0) {//deleting old Apex code 
			waitUntilVisible(driver, lines.get(0));
		    clickByJs(driver, lines.get(0));
		    actions.sendKeys(lines.get(0),Keys.chord(Keys.CONTROL, "A")).click().perform();
		    actions.sendKeys(Keys.BACK_SPACE).perform();
		    lines = driver.findElements((linesBy));
		}
		
		sendCodeWindow(driver, "Contact");
		
		waitUntilVisible(driver, executeButton);
		clickElement(driver, executeButton);
	}
	
	public void sendCodeWindow(WebDriver driver, String type) {
		Actions actions = new Actions(driver);
		
		switch(type) {
		
		case "Contact":
			String firstName = "AutoFirstContact";
			String lastName = "AutoLastContact";
			idleWait(1);
			actions.sendKeys(" Contact c = new Contact(FirstName = "+firstName+", LastName = "+lastName+");\n").perform();
			idleWait(1);
			actions.sendKeys("insert c;\n").perform();
		}
	}
	
}
