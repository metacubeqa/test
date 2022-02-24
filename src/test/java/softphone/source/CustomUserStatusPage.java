package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.accounts.AccountIntelligentDialerTab.CustomStatusFields;
import utility.HelperFunctions;

public class CustomUserStatusPage extends SeleniumBase{
	
	By customStatusRow 			= By.xpath("(.//*[@data-testid='custom-user-status-modal.container']//div[@class='modal-body']//div)[2]/div");
	By customStatusModalBox 	= By.xpath("(.//*[@data-testid='custom-user-status-modal.container']");
	By customStatusBusy 		= By.cssSelector(".label");
	By customStatusName 		= By.cssSelector("span:nth-child(2)");
	By customStatusDuration 	= By.cssSelector("h6");
	By customStatusDescription 	= By.cssSelector("p");
	By customUserStatusToolTip 	= By.cssSelector(".tooltip-inner");
	By customStatusDeselctIcon	= By.cssSelector("[alt='Deselect Status']");
	
	By saveButton 				= By.xpath(".//button[text()='Save' and not(@style='display: none;')]");
	By cancelButton 			= By.xpath(".//button[text()='Cancel']");
	By selectButton			 	= By.xpath(".//button[text()='select']");
	
	By durationDropDown			= By.className("required-dropdown");
	By deselectStatusImg		= By.cssSelector("[alt='Deselect Status']");
	
	String customStatusExpMsg	= ".//*[text() = 'Your custom user status of $$CustomStatusName$$ has now expired. Please update your availability.']";
	By closeWarningMsgIcon		= By.cssSelector("button.close-rt-alert span");
	
	public static enum Duration{
		one(1),
		five(5),
		Fifteen(15),
		Thirty(30),
		Hour(60),
		NoExpiration(0),
		blank(-1);
		
		final int value;
		
		Duration(int value) {
			this.value = value;
		}
		
		public int getValue(){ return value;}
	}
	
	public void selectCustomStatus(WebDriver driver, String customStatus, HashMap<CustomStatusFields, String> customStatusDetails) {
		clickSelectCustomStatusBtn(driver, customStatus);	
		verifyAddedCustomStatusDetails(driver, customStatusDetails);
		waitUntilVisible(driver, customStatusDeselctIcon);
	}
	
	public String selectCustomStatusDuration(WebDriver driver, Duration duration) {
		String appendMessage = "";
		
		if(!duration.equals(Duration.blank)) {
			selectDuration(driver, duration);
		}else {
			assertFalse(isElementVisible(driver, durationDropDown, 1));
		}
		
		if(duration.equals(Duration.blank) || duration.equals(Duration.NoExpiration)) {
			appendMessage = "";
		}else {
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
			Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
			appendMessage = " since " + dateTimeFormatter.format(dateTime);
		}
		return appendMessage;
	}
	
	public String getCustomStatusToolTip(WebDriver driver) {
		return getElementsText(driver, customUserStatusToolTip); 
	}
	
	public void verifyCustomStatusToolTipInvisible(WebDriver driver) {
		assertFalse(isElementVisible(driver, customUserStatusToolTip, 5)); 
	}
	
	public void clickSelectCustomStatusBtn(WebDriver driver, String customStatus) {
		WebElement customStatusRowElement = getCustomStatusRow(driver, customStatus);
		customStatusRowElement.findElement(selectButton).click();
		waitUntilVisible(driver, deselectStatusImg);
	}
	
	public void selectDuration(WebDriver driver, Duration duration) {
		selectFromDropdown(driver, durationDropDown, SelectTypes.value, String.valueOf(duration.getValue()));
	}
	
	public void verifyAllDurationsInDropDown(WebDriver driver) {
		List<String> durationOptions = new ArrayList<String>();
		durationOptions.add("-Select-");
		durationOptions.add("1 minute");
		durationOptions.add("5 minutes");
		durationOptions.add("15 minutes");
		durationOptions.add("30 minutes");
		durationOptions.add("1 hour");
		durationOptions.add("no expiration");
		List<WebElement> durationDrowpdownOptions = getAllSelectOptions(driver, durationDropDown);
		for (int i = 0; i < durationDrowpdownOptions.size(); i++) {
			assertEquals(durationDrowpdownOptions.get(i).getText(), durationOptions.get(i));
		}
	}
	
	public String  getSelectedDuration(WebDriver driver) {
		return getSelectedValueFromDropdown(driver, durationDropDown);
	}
	
	public void clickCustomStatusSaveBtn(WebDriver driver) {
		clickElement(driver, saveButton);
	}
	
	public void clickCustomStatusCancelBtn(WebDriver driver) {
		clickElement(driver, cancelButton);
	}
	
	public WebElement getCustomStatusRow(WebDriver driver, String customStatus) {
		waitUntilVisible(driver, saveButton);
		isElementVisible(driver, selectButton, 5);
		List<WebElement> customStatusesRows = getInactiveElements(driver, customStatusRow);
		
		for (WebElement customStatusesRow : customStatusesRows) {
			if (customStatusesRow.findElement(customStatusName).getText().equals(customStatus))
				return customStatusesRow;
		}
		return null;
	}

	public void verifyAddedCustomStatusDetails(WebDriver driver, HashMap<CustomStatusFields, String> customStatusDetails) {	
		WebElement customStatusRowElement = getCustomStatusRow(driver, customStatusDetails.get(CustomStatusFields.StatusName));
		
		if(customStatusDetails.get(CustomStatusFields.StatusName) != null) 
			assertEquals(customStatusRowElement.findElement(customStatusName).getText(), customStatusDetails.get(CustomStatusFields.StatusName));
		
		if(customStatusDetails.get(CustomStatusFields.Busy) != null) {
			if(customStatusDetails.get(CustomStatusFields.Busy).equals("Yes")) {
				assertEquals(customStatusRowElement.findElement(customStatusBusy).getText(), "BUSY");
			}else {
				assertEquals(customStatusRowElement.findElement(customStatusBusy).getText(), "FREE");
			}
		}
			
		if(customStatusDetails.get(CustomStatusFields.Description) != null) 
			assertEquals(customStatusRowElement.findElement(customStatusDescription).getText(), customStatusDetails.get(CustomStatusFields.Description));
		
		if(customStatusDetails.get(CustomStatusFields.Time) != null) {
			String customStatusTimeValue = null;
			switch (customStatusDetails.get(CustomStatusFields.Time)) {
			case ("5 Minutes"):
				customStatusTimeValue = "5 minutes";
				break;
			case ("15 Minutes (Default)"):
				customStatusTimeValue = "15 minutes";
				break;
			case ("30 Minutes"):
				customStatusTimeValue = "30 minutes";
				break;
			case ("1 hour"):
				customStatusTimeValue = "1 hour";
				break;
			case ("Does Not Expire"):
				customStatusTimeValue = "no duration";
				break;
			default:
				break;
			}
			assertEquals(customStatusRowElement.findElement(customStatusDuration).getText(), customStatusTimeValue);
		}
	}
	
	public boolean isCustomStatusExpireMsg(WebDriver driver, String customStatusName) {
		return isElementVisible(driver, By.xpath(customStatusExpMsg.replace("$$CustomStatusName$$", customStatusName)), 5);
	}
	
	public boolean isCustomStatusModalBoxVisible(WebDriver driver) {
		return isElementVisible(driver, customStatusModalBox, 0);
	}
	
	
	//This method is to close bottom red error bar
		public void closeWarningBar(WebDriver driver) {
			if(getWebelementIfExist(driver, closeWarningMsgIcon)!=null && getWebelementIfExist(driver, closeWarningMsgIcon).isDisplayed()){
				clickElement(driver, closeWarningMsgIcon);
			}
		}
}
