package guidedSelling.source.pageClasses;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import base.SeleniumBase;
import utility.HelperFunctions;
import utility.GmailPageClass;
							   
/**
 * @author AbhishekT
 *
 */
public class ActionsPage extends SeleniumBase{
	GmailPageClass gmail = new GmailPageClass();
	
	
	String subjectText = "Email_subject_".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
	String emailBodytext = " Hello, Here myself GS QA member for testing email body based on the Text email template.";		
	public static enum ActionsTypesEnum
	{
		Call,
		NativeEmailManual,
		NativeEmailAutomatic,
		SMS, 
		Messages,
		Task,
		Email,
	}
	
	
	public static enum actionsFooters
	{
		Completed,
		Skipped,
		Overdue,
		AvgDaystoComplete,
		Email_Opened,
		Email_Replied,
	}

	By actionsTab 		     = By.xpath("//*[contains(@title, 'Actions Tab') and text()='Actions']|//a[@title='Actions'] | //a[@href='/lightning/n/RDNACadence__Actions']");
	By actionParent			 = By.xpath("//a[@title='Actions']/parent::*");
	By actionsDataColumn     = By.xpath("//th[@data-label='Action']//a"); 
	By createActionBtn	     = By.xpath("//button[text()='Create Action']");
	By actionNameInput	     = By.xpath("//label[text()='Action Name']/..//input");
	By nextBtn 			     = By.xpath("//button[text()='Next']");
	By saveBtn 			     = By.xpath("//button[text()='Save and Finish']");

	By templatePanelLists  = By.xpath("//*[@data-rbd-draggable-id]//div[contains(@class, 'TemplateLibraryPanel__IconAndTitle')]/..//h6");
	By buttontemplateIcon  = By.xpath("//*[@data-rbd-draggable-id]//div[contains(@class, 'TemplateLibraryPanel__IconAndTitle')]/..//button");
	By templateAdded	   = By.xpath("//span[text()='Template']/../..//span[@color='primary']");
	By removeTemplateBtn   = By.xpath("//span[text()='Template']/../..//button[text()='Remove']");

	String actionsTypeString 	 = "//*[text()='Action Type']/..//div[@role='radiogroup']//input[@value='$actionType$']/following-sibling::button/ancestor::span[1]";
	String actionHeaderString 	 = "//img[@alt='rdna-logo']/following-sibling::h1[text()='$actionName$']";
	
	String actionFooterString    = "//p[text()='$actionFooter$']/preceding-sibling::h1";
	
	//Task locators
	By valueFieldInput			 = By.xpath("//*[text()='Value']/..//input");
	
	//SMS locators
	By clickSMS					=	By.xpath("//*[contains(@value, 'SMS')]");
	
	//Email locators
	By clickEmail                = By.xpath("//*[contains(@value, 'Email')]");
    By emailSubject				 = By.xpath("//label[text()='Email Subject']/..//input");
    By emailBody				 = By.xpath(".//label[text()='Email Body']/..//div[contains(@class, 'sun-editor-editable')]|//fieldset/label[text()='Email Body']/following-sibling::div//div[contains(@class, 'sun-editor-editable')]");
    //By emailBody				 = By.xpath("//*[@class='se-wrapper']");
    By sfdcEmail				 = By.xpath("//*[@name='mui-96119'and @value='SFDC']");
    By automatic				 = By.xpath("//span[text()='Automatic']");
    
    
    
	//search Action
    By searchField				 = By.xpath("//input[@name='global-search-filter']");
    By downArrow				 = By.xpath("//*[@id='scroll-to-top-1']//span[7]//button");
    By deleteBtn				 = By.xpath("//*[text()='Delete']");
    By notFound					 = By.xpath("//*[text()='No Results Found.']");
    By confirmBtn        		 = By.xpath("//button[@data-testid='ui-dialog.primary-btn']");
    By Delete_Action_Info	   	 = By.xpath("//span[text()=' action as it is already associated with a sequence.']");														 
	By Actioniframe 			 = By.xpath("//*[@class='iframe-parent slds-template_iframe slds-card']//iframe[@title='accessibility title']");
	By Automaticddl				 = By.xpath("//div[contains(@id, 'deliveryPreference')]");
	By Immediateanyday			 = By.xpath("//li[text() = 'Immediate, any day']");
	By srequired_text 			 = By.xpath("//button[@data-command='required-text']");
	By SrequiredError			 = By.xpath("//p[@class='MuiFormHelperText-root Mui-error']");
    public void navigateToActionstab(WebDriver driver) {
		
		waitUntilVisible(driver, actionsTab);
		clickElement(driver, actionsTab);
		isSpinnerWheelInvisible(driver);
	} 
		
	 
	/**
	 * This method creates an action
	 * @param taskValue TODO
	 * @param key
	 * 
	 * @return
	 */
	public void createAnAction(WebDriver driver, String actionName, ActionsTypesEnum actionType, String taskValue) {

		if (!(isElementVisible(driver, Actioniframe, 5))) {
			navigateToActionstab(driver);
		}

		if (isElementVisible(driver, Actioniframe, 5)) {
			switchToIframe(driver, Actioniframe);
			waitUntilVisible(driver, createActionBtn);
			clickElement(driver, createActionBtn);
			isSpinnerWheelInvisible(driver);

			waitUntilVisible(driver, actionNameInput);
			enterText(driver, actionNameInput, actionName);
		} else {
			waitUntilVisible(driver, createActionBtn);
			clickElement(driver, createActionBtn);
			isSpinnerWheelInvisible(driver);

			waitUntilVisible(driver, actionNameInput);
			enterText(driver, actionNameInput, actionName);
		}
		By actionIconLoc = null;
		if (actionType == ActionsTypesEnum.NativeEmailAutomatic || actionType == ActionsTypesEnum.NativeEmailManual) {

			actionIconLoc = By.xpath(actionsTypeString.replace("$actionType$", "Email"));
		} else {

			actionIconLoc = By.xpath(actionsTypeString.replace("$actionType$", actionType.toString()));
		}
		waitUntilVisible(driver, actionIconLoc);
		clickElement(driver, actionIconLoc);

		String color = getCssValue(driver, findElement(driver, actionIconLoc), CssValues.Color);
		String hexColor = Color.fromString(color).asHex();
			
		assertEquals(hexColor, "#66a3ff");
		
		switch (actionType) {

		case Call:
			String callTemplate = getElements(driver, templatePanelLists).get(0).getText();
			hoverElement(driver, getElements(driver, templatePanelLists).get(0));
			waitUntilVisible(driver, buttontemplateIcon);
			hoverElement(driver, buttontemplateIcon);
			clickElement(driver, buttontemplateIcon);
			waitUntilVisible(driver, templateAdded);
			assertEquals(getElementsText(driver, templateAdded), callTemplate);
			waitUntilVisible(driver, removeTemplateBtn);
			break;
		case Task:
			waitUntilVisible(driver, valueFieldInput);
			enterText(driver, valueFieldInput, taskValue);
			break;
		case SMS:
			String SMSTemplate = getElements(driver, templatePanelLists).get(0).getText();
			hoverElement(driver, getElements(driver, templatePanelLists).get(0));
			waitUntilVisible(driver, buttontemplateIcon);
			hoverElement(driver, buttontemplateIcon);
			clickElement(driver, buttontemplateIcon);
			waitUntilVisible(driver, templateAdded);
			assertEquals(getElementsText(driver, templateAdded), SMSTemplate);
			waitUntilVisible(driver, removeTemplateBtn);
			break;
		case NativeEmailManual:
			waitUntilVisible(driver, emailSubject);
			enterText(driver, emailSubject, subjectText);
			waitUntilVisible(driver, emailBody);
			//enterText(driver, emailBody, emailBodytext);
			
			scrollTillEndOfPage(driver);
			clickElement(driver, emailBody);
			driver.findElement(emailBody).sendKeys(Keys.ENTER);
			driver.findElement(emailBody).sendKeys(emailBodytext);
			waitUntilTextPresent(driver, emailBody, emailBodytext.trim());
  
			break;
		case NativeEmailAutomatic:
			waitUntilVisible(driver, automatic);
			clickElement(driver, automatic);
			
			
			clickElement(driver, Automaticddl);
			waitUntilVisible(driver, Immediateanyday);
    		clickElement(driver, Immediateanyday);
			
			enterText(driver, emailSubject, subjectText);
			
			scrollTillEndOfPage(driver);
			clickElement(driver, emailBody);
			driver.findElement(emailBody).sendKeys(Keys.ENTER);
			driver.findElement(emailBody).sendKeys(emailBodytext);
			
			
			waitUntilTextPresent(driver, emailBody, emailBodytext.trim());
			break;

		default:
			break;
		}
		
		waitForElementEnabled(driver, saveBtn);
		idleWait(1);
		waitUntilClickable(driver, saveBtn);
		clickElement(driver, saveBtn);
  
  
	}


	/** verify action created
	 * @param driver
	 * @param actionName
	 */
	public void verifyActionCreated(WebDriver driver, String actionName) {
		isSpinnerWheelInvisible(driver);
		By actionHeaderLoc = By.xpath(actionHeaderString.replace("$actionName$", actionName));
		if (isElementVisible(driver, Actioniframe, 5)) {
			switchToIframe(driver, Actioniframe);
		waitUntilVisible(driver, actionHeaderLoc);
		}else {
			waitUntilVisible(driver, actionHeaderLoc);
		}
	}
	
	/** get action footer percentage
	 * @param driver
	 * @param actionName
	 * @param footer
	 * @return
	 */
	public String getActionFooterPercentge(WebDriver driver, actionsFooters footer) {
		By actionFooterLoc = null;
		if(footer == actionsFooters.AvgDaystoComplete) {
			actionFooterLoc = By.xpath(actionFooterString.replace("$actionFooter$", "Avg. days to complete"));
		}
		else if (footer==actionsFooters.Email_Opened){
			actionFooterLoc = By.xpath(actionFooterString.replace("$actionFooter$", "Email Opened"));
		}
		else if (footer==actionsFooters.Email_Replied){
			actionFooterLoc = By.xpath(actionFooterString.replace("$actionFooter$", "Email Replied"));
		}
		else
		actionFooterLoc = By.xpath(actionFooterString.replace("$actionFooter$", footer.toString()));
		
		waitUntilVisible(driver, actionFooterLoc);
		return getElementsText(driver, actionFooterLoc);
	}
	
	public void callSearchsubject(WebDriver driver) {
		gmail.searchsubject(driver, subjectText);					  
	}
	public void SearchsubjectAPI(WebDriver driver) {
		gmail.searchsubject(driver, "Email_subject_28/07/2021 17:33:24.321" );					  
	}
	
	public void findCreatedAction(WebDriver driver, String actionName) {
		navigateToActionstab(driver);
		isSpinnerWheelInvisible(driver);
		clickElement(driver, searchField);
		enterText(driver, searchField, actionName);
		
	}

	public void deleteAction (WebDriver driver, String actionName) {
		waitUntilVisible(driver, downArrow);
		clickElement(driver,downArrow); 
		    
       	waitUntilVisible(driver, deleteBtn);
		clickElement(driver,deleteBtn);
		waitUntilVisible(driver, confirmBtn);
    	clickElement(driver, confirmBtn);
		
	}
	
	public boolean verifyActionDeleted(WebDriver driver, String actionName) {
		driver.navigate().refresh();
		isSpinnerWheelInvisible(driver);
		clickElement(driver, searchField);
		enterText(driver, searchField, actionName);
		return isElementVisible(driver, notFound, 10);
		 
	}
	
	public String  verifyDeleteActionInformation(WebDriver driver, String actionName) {	
		return getElementsText(driver, Delete_Action_Info);	 
	}
	
	public String Verify_RTE (WebDriver driver, String actionName, ActionsTypesEnum actionType, String taskValue) {

		if (!(isElementVisible(driver, Actioniframe, 5))) {
			navigateToActionstab(driver);
		}

		if (isElementVisible(driver, Actioniframe, 5)) {
			switchToIframe(driver, Actioniframe);
			waitUntilVisible(driver, createActionBtn);
			clickElement(driver, createActionBtn);
			isSpinnerWheelInvisible(driver);

			waitUntilVisible(driver, actionNameInput);
			enterText(driver, actionNameInput, actionName);
		} else {
			waitUntilVisible(driver, createActionBtn);
			clickElement(driver, createActionBtn);
			isSpinnerWheelInvisible(driver);

			waitUntilVisible(driver, actionNameInput);
			enterText(driver, actionNameInput, actionName);
		}
		By actionIconLoc = null;
		if (actionType == ActionsTypesEnum.NativeEmailAutomatic || actionType == ActionsTypesEnum.NativeEmailManual) {

			actionIconLoc = By.xpath(actionsTypeString.replace("$actionType$", "Email"));
		} else {

			actionIconLoc = By.xpath(actionsTypeString.replace("$actionType$", actionType.toString()));
		}
		waitUntilVisible(driver, actionIconLoc);
		clickElement(driver, actionIconLoc);

		String color = getCssValue(driver, findElement(driver, actionIconLoc), CssValues.Color);
		String hexColor = Color.fromString(color).asHex();
			
		assertEquals(hexColor, "#66a3ff");
		
		switch (actionType) {
		case NativeEmailAutomatic:
			waitUntilVisible(driver, automatic);
			clickElement(driver, automatic);
			
			clickElement(driver, Automaticddl);
			waitUntilVisible(driver, Immediateanyday);
    		clickElement(driver, Immediateanyday);
			
			scrollTillEndOfPage(driver);
			clickElement(driver, emailBody);
			driver.findElement(emailBody).sendKeys(Keys.ENTER);
			driver.findElement(emailBody).sendKeys(emailBodytext);
			driver.findElement(emailBody).sendKeys(Keys.CONTROL, "a");
			clickElement(driver,srequired_text);
			driver.findElement(emailBody).sendKeys(Keys.ENTER);
			break;
		}
		String Text = getElementsText(driver, SrequiredError);
		return Text;
		
		}
 
}