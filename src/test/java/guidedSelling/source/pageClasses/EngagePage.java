package guidedSelling.source.pageClasses;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.util.Strings;

import base.SeleniumBase;
import guidedSelling.source.pageClasses.ActionsPage.ActionsTypesEnum;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import utility.HelperFunctions;

public class EngagePage extends SeleniumBase{
	
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	
	By engageTab		 = By.xpath("//*[contains(@title, 'Engage Tab')]");
    By engagelight		 = By.xpath("//*[contains(@title, 'Engage')]");		 
	By searchOnEngage    = By.cssSelector("[name='global-search-filter']");
	By Engageiframe		 = By.xpath("//*[@class='iframe-parent slds-template_iframe slds-card']//iframe[@title='accessibility title']");

	By callsEngageTab 	 = By.xpath("//div[@data-testid='horizontal-tab-header']//h6[text()='Calls']");
	
	By taskStatusInput 		 = By.cssSelector("[data-analyticsid='task-status-input']");
	By submitBtn 			 = By.xpath("//button[text()='Submit']");
	By sendBtn				 = By.xpath("//button[text()='Send']");

	String selectFieldOption = "//li[contains(@class,'MuiListItem-button') and text()='$field$']";
	String actionBtnLoc = "//div[@class= 'main-name']//span[contains(text(),'$name$')]/ancestor::span/..//a[contains(@class, 'ActionThing')]/span";
	
	By enagageParticpantsNames = By.xpath("//div[contains(@class, 'EngageTable')]/div[@class='main-name']//span");
	By skipEngage 			   = By.xpath("//button[text()='Skip']");
	By enageCheckBoxes         = By.xpath("//*[@title='Toggle Row Selected']//input/..");

	By typeInputFilter 		   = By.xpath("//div[@data-testid='Type-input']");
	By headerList 			   = By.xpath("//span[@role='columnheader' and @title]");
	String columnLinkNamesList = "(//span[contains(@class, 'RdnaSmartTable') and @role='cell']//span[@color='link'])[$index]";

	By primaryBtnAccept        = By.xpath("//button[@data-testid='ui-dialog.primary-btn']");
	String ActionEngageTab		   = "//div[@data-testid='horizontal-tab-header']//h6[text()='$Engage$']";
	By noResultFound 			= By.xpath("//h2[text()='No Results Found.']");
	By toaster					= By.xpath("//*[@id='client-snackbar']");
	
	public enum FilterValuesEnum {

		Contact, Lead

	}
	
	public static enum EngageEnum
	{
		Calls,
		All,
		Tasks,
		Messages,
		Emails,
	}

	/**
	 * @param driver
	 */
	public void openEngageInNewTab(WebDriver driver) {
		Actions actions = new Actions(driver);
		WebElement engageTabLoc = findElement(driver, engageTab);

		// opening url in new tab and switching
		actions.keyDown(Keys.CONTROL).click(engageTabLoc).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
		isMaintananceMessageVisible(driver);
		isSpinnerWheelInvisible(driver);
	}
	
	public void isMaintananceMessageVisible(WebDriver driver) {
		if(isElementVisible(driver, By.xpath(".//span[text(]) ='We are down for maintenance.']"), 5)){
			refreshCurrentDocument(driver);
		}
	}

	/**
	 * @param driver
	 * @param participant
	 * @return
	 */
	public boolean verifyParticipantPresentOnEngage(WebDriver driver, String participant) {
		if (isElementVisible(driver, Engageiframe, 5)) {
			for (int i = 0; i < 4; i++) {
				refreshCurrentDocument(driver);
				switchToIframe(driver, Engageiframe);
				waitUntilVisible(driver, searchOnEngage);
				enterText(driver, searchOnEngage, participant);

				By actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", participant));
				if (isElementVisible(driver, actionPerformBtnLoc, 5)) {
					return true;
				}
			}

		} else {

			for (int i = 0; i < 4; i++) {
				refreshCurrentDocument(driver);
				waitUntilVisible(driver, searchOnEngage);
				enterText(driver, searchOnEngage, participant);

				By actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", participant));
				if (isElementVisible(driver, actionPerformBtnLoc, 5)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean verifyParticipantNotPresentOnEngage(WebDriver driver, String participant) {
		if (isElementVisible(driver, Engageiframe, 5)) {
		for(int i=0; i < 4; i++) {
			refreshCurrentDocument(driver);
			switchToIframe(driver, Engageiframe);
			waitUntilVisible(driver, searchOnEngage);
			enterText(driver, searchOnEngage, participant);

			By actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", participant));
			if(isElementInvisible(driver, actionPerformBtnLoc, 5)) {
				return true;
			}}}else {
				for(int i=0; i < 4; i++) {
					refreshCurrentDocument(driver);
					waitUntilVisible(driver, searchOnEngage);
					enterText(driver, searchOnEngage, participant);

					By actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", participant));
					if(isElementInvisible(driver, actionPerformBtnLoc, 5)) {
						return true;
					}
				
			}
			}
		return false;
	}
	
	public void clickOnCallsEngageTab(WebDriver driver, EngageEnum actionEngae ) {
		By actionEngageTab = By.xpath(ActionEngageTab.replace("$Engage$", actionEngae.name()));
		waitUntilVisible(driver, actionEngageTab);
		clickElement(driver, actionEngageTab);
		waitUntilAttribute(driver, actionEngageTab, ElementAttributes.color, "link");
		isSpinnerWheelInvisible(driver);
	}
	/**
	 * @param driver
	 * @param value
	 * @param actionType
	 */
	public int clickOnActionToBePerformedBtn(WebDriver driver, String value, ActionsTypesEnum actionType) {
		By actionPerformBtnLoc = null;
		if (isElementVisible(driver, Engageiframe, 5)) {
			for(int i=0; i < 4; i++) {
				refreshCurrentDocument(driver);
				switchToIframe(driver, Engageiframe);
				idleWait(20);
				
				waitUntilVisible(driver, searchOnEngage);
				
				enterText(driver, searchOnEngage, value);

				actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", value));
				if(isElementVisible(driver, actionPerformBtnLoc, 5)) {
					break;
				}
			}
			
		}else {
		for(int i=0; i < 4; i++) {
			refreshCurrentDocument(driver);
			waitUntilVisible(driver, searchOnEngage);
			enterText(driver, searchOnEngage, value);

			actionPerformBtnLoc = By.xpath(actionBtnLoc.replace("$name$", value));
			if(isElementVisible(driver, actionPerformBtnLoc, 5)) {
				break;
			}
		}
		}
		if (isElementVisible(driver, Engageiframe, 5)) {
			switchToIframe(driver, Engageiframe);
			assertEquals(getElementsText(driver, actionPerformBtnLoc), actionType.name());

			waitUntilVisible(driver, actionPerformBtnLoc);
			clickElement(driver, actionPerformBtnLoc);
			
		}else {
	
		assertEquals(getElementsText(driver, actionPerformBtnLoc), actionType.name());

		waitUntilVisible(driver, actionPerformBtnLoc);
		clickElement(driver, actionPerformBtnLoc);
		}
		return getElements(driver, actionPerformBtnLoc).size();
	
}
	
	/**
	 * @param driver
	 * @param value
	 * @param actionType
	 * @param driverToCall TODO
	 */
	public void performActionOnEngage(WebDriver driver, String value, ActionsTypesEnum actionType, WebDriver driverToCall) {
		String currentWindow = "";
		String extensionWindow = "";
		
		clickOnActionToBePerformedBtn(driver, value, actionType);

		switch (actionType) {
		case Task:
			waitUntilVisible(driver, taskStatusInput);
			clickElement(driver, taskStatusInput);
			isSpinnerWheelInvisible(driver);

			By selectFieldLoc = By.xpath(selectFieldOption.replace("$field$", "Completed"));
			waitUntilVisible(driver, selectFieldLoc);
			clickElement(driver, selectFieldLoc);

			waitUntilVisible(driver, submitBtn);
			clickElement(driver, submitBtn);
			break;
		case Email:
			isSpinnerWheelInvisible(driver);
			waitUntilVisible(driver, sendBtn);
			clickElement(driver, sendBtn);
			isSpinnerWheelInvisible(driver);
			break;
		case Call:
			currentWindow = driver.getWindowHandle();
			switchToExtension(driver);
			
			extensionWindow = driver.getWindowHandle();

			// Verify contact details (Name, phone number) on Softphone
			assertTrue(Strings.isNotNullAndNotEmpty(callScreenPage.getCallerName(driver)));
			
			callScreenPage.switchToTab(driverToCall, 1);
			callScreenPage.reloadSoftphone(driverToCall);
			
			softPhoneCalling.switchToTabWindow(driver, extensionWindow);
			softPhoneCalling.clickCallBackButton(driver);
			softPhoneCalling.waitUntilInvisible(driver, spinnerWheel);
			softPhoneCalling.isCallHoldButtonVisible(driver);

			callScreenPage.switchToTab(driverToCall, 1);
			softPhoneCalling.pickupIncomingCall(driverToCall);
			softPhoneCalling.isHangUpButtonVisible(driverToCall);
			idleWait(5);
			softPhoneCalling.hangupIfInActiveCall(driverToCall);
			
			//closing extension
			softPhoneCalling.switchToTabWindow(driver, extensionWindow);
			driver.close();
			
			//switch To engage tab
			softPhoneCalling.switchToTabWindow(driver, currentWindow);
			break;
			
		case SMS:
			currentWindow = driver.getWindowHandle();
			
			switchToExtension(driver);
			extensionWindow = driver.getWindowHandle();

			// send sms message on extension
			softPhoneActivityPage.sendMessage(driver, "GS_ActionSMS_MSg".concat(HelperFunctions.GetRandomString(4)), 0);			
			
			//closing extension
			softPhoneCalling.switchToTabWindow(driver, extensionWindow);
			driver.close();
			
			//switch To engage tab
			softPhoneCalling.switchToTabWindow(driver, currentWindow);
			
			break;

		default:
			break;

		}
	}
	
	public void skipParticipantsEngage(WebDriver driver, int count) {
		if (isElementVisible(driver, Engageiframe, 5)) {
			switchToIframe(driver, Engageiframe);
			List<String> beforeSkipList = getTextListFromElements(driver, enagageParticpantsNames);

		assertFalse(isElementVisible(driver, skipEngage, 5));

		List<WebElement> list = getElements(driver, enageCheckBoxes);
		for (int i = 0; i <= count; i++) {
			list.get(i).click();
		}
		
		waitUntilVisible(driver, skipEngage);
		clickElement(driver, skipEngage);
		waitUntilVisible(driver, primaryBtnAccept);
		clickElement(driver, primaryBtnAccept);
		waitUntilInvisible(driver, primaryBtnAccept);
		
		List<String> afterSkipList = getTextListFromElements(driver, enagageParticpantsNames);
		assertNotEquals(beforeSkipList, afterSkipList);
		}
		else {
			List<String> beforeSkipList = getTextListFromElements(driver, enagageParticpantsNames);

			assertFalse(isElementVisible(driver, skipEngage, 5));

			List<WebElement> list = getElements(driver, enageCheckBoxes);
			for (int i = 0; i <= count; i++) {
				list.get(i).click();
			}
			
			waitUntilVisible(driver, skipEngage);
			clickElement(driver, skipEngage);
			waitUntilVisible(driver, primaryBtnAccept);
			clickElement(driver, primaryBtnAccept);
			waitUntilInvisible(driver, primaryBtnAccept);
			
			List<String> afterSkipList = getTextListFromElements(driver, enagageParticpantsNames);
			assertNotEquals(beforeSkipList, afterSkipList);
			
		}
	}
 
	public void selectFilterByType(WebDriver driver, FilterValuesEnum filterValue) {
		if (isElementVisible(driver, Engageiframe, 5)) {
			switchToIframe(driver, Engageiframe);
		waitUntilVisible(driver, typeInputFilter);
		clickElement(driver, typeInputFilter);
		selectValueFromDropDown(driver, filterValue);
		} else {
			waitUntilVisible(driver, typeInputFilter);
			clickElement(driver, typeInputFilter);
			selectValueFromDropDown(driver, filterValue);
		}
			
	}

	public List<WebElement> getColumnListAccToHeader(WebDriver driver, String header) {
		if (isElementVisible(driver, Engageiframe, 5)) {
			switchToIframe(driver, Engageiframe);
			int index = getTextListFromElements(driver, headerList).indexOf(header);
			clickElement(driver, getElements(driver, headerList).get(index));
			clickElement(driver, getElements(driver, headerList).get(index));
			By columnLinkNamesListLoc = By.xpath(columnLinkNamesList.replace("$index", String.valueOf(index)));
			return getElements(driver, columnLinkNamesListLoc);
		}else {
		int index = getTextListFromElements(driver, headerList).indexOf(header);
		clickElement(driver, getElements(driver, headerList).get(index));
		clickElement(driver, getElements(driver, headerList).get(index));
		By columnLinkNamesListLoc = By.xpath(columnLinkNamesList.replace("$index", String.valueOf(index)));
		return getElements(driver, columnLinkNamesListLoc);
	}
}
	
	public void selectValueFromDropDown(WebDriver driver, FilterValuesEnum filterValue) {
   		By selectFieldLoc = By.xpath(selectFieldOption.replace("$field$", filterValue.name()));
		waitUntilVisible(driver, selectFieldLoc);
		clickElement(driver, selectFieldLoc);
		isSpinnerWheelInvisible(driver);
	}
	
	public boolean findAction (WebDriver driver) {
		return isElementVisible(driver, noResultFound, 10);		
	}
	
	public String toasterMessageDisplay(WebDriver driver, String value, ActionsTypesEnum actionType) {
		clickOnActionToBePerformedBtn(driver, value, actionType);
		isSpinnerWheelInvisible(driver);
		waitUntilVisible(driver, sendBtn);
		clickElement(driver, sendBtn);
		isSpinnerWheelInvisible(driver);
		return getElementsText(driver, toaster);

		
	}
	
}