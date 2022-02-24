package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import base.SeleniumBase;
import base.TestBase;
import softphone.source.sip.SipCallingPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import utility.HelperFunctions;

public class SoftPhoneSettingsPage extends SeleniumBase {

	SoftPhoneCalling softPhoneCalling 	= new SoftPhoneCalling();
	SipCallingPage sipCallingPage		= new SipCallingPage();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	 
	By settingsIcon 					= By.cssSelector(".settings");
	static By activeSettingIcon 		= By.cssSelector(".settings.active");
	By settingPageHeading 				= By.xpath("//*[text()='Settings']");
	By warningMessage					= By.cssSelector(".alert .message-text");
	By errorCloseButton 				= By.cssSelector(".alert-danger .close-error");
	By notificationError                = By.cssSelector(".error-alert.notification-errors");
	By generalSettingLink 				= By.cssSelector("[data-target='#general-settings']");
	By generalSettingHeading 			= By.xpath(".//*[text()='General Settings']");
	By voicemalDropTabLink				= By.cssSelector("[data-target='#automated-voicemail-settings']");
	By customGreetingTabLink 			= By.cssSelector("[data-target='#custom-greeting-settings']");  
	By logoutButton						= By.cssSelector("button[data-action='logout']");
	By toolTipsImagesLoc				= By.cssSelector("#general-settings tr:not([style='display: none;']) [src='images/icon-help.svg']");
	By toolTipText						= By.cssSelector("[role='tooltip'] .tooltip-inner");
	
	By clicktoCallCheckBox				= By.id("clickToCall");
	By clicktoCallOnToggleButton 		= By.cssSelector(".bootstrap-switch-id-clickToCall .bootstrap-switch-handle-on");
	By clicktoCallOffToggleButton 		= By.cssSelector(".bootstrap-switch-id-clickToCall .bootstrap-switch-handle-off");

	By dialNextCheckBox					= By.id("nextCaller");
	By dialNextOnToggleButton 			= By.cssSelector(".bootstrap-switch-id-nextCaller .bootstrap-switch-handle-on");
	By dialNextOffToggleButton 			= By.cssSelector(".bootstrap-switch-id-nextCaller .bootstrap-switch-handle-off");
	By dialNextPreviewCheckBox 			= By.cssSelector("#dialNextPreviewEnabled");
	
	By localPresenceOffToggleButton 	= By.cssSelector(".bootstrap-switch-id-localPresence .bootstrap-switch-handle-off");
	By localPresenceSMSCheckBox			= By.id("localPresenceSMS");
	By recordCallCheckBox 				= By.cssSelector("tr:not([style='display: none;']) #recordCalls");
	By recordsCallOffToggleButton 		= By.cssSelector(".bootstrap-switch-id-recordCalls .bootstrap-switch-handle-off");
	By recordsCallOnToggleButton 		= By.cssSelector(".bootstrap-switch-id-recordCalls .bootstrap-switch-handle-on");
	By recordCallText					= By.xpath(".//td[text()='Record Calls ']");
	By callForwardingLabel				= By.xpath(".//*[text()='Call Forwarding']");
	By callForwardingCheckBox 			= By.id("callForwarding");
	By callForwardingOnToggleButton 	= By.cssSelector(".bootstrap-switch-id-callForwarding .bootstrap-switch-handle-on");
	By callForwardingOffToggleButton 	= By.cssSelector(".bootstrap-switch-id-callForwarding .bootstrap-switch-handle-off");
	By callForwardingNumberLabel 		= By.cssSelector(".call-forwarding-selectize .selectize-input span");
	By callForwardingNumberTextBox 		= By.cssSelector(".call-forwarding-selectize .selectize-input input");
	By callForwardingNumberSelectize 	= By.cssSelector(".call-forwarding-selectize .selectize-input");
	By callForwardingNumbersInDropDown 	= By.cssSelector(".option.pull-left");
	By callForwardingNumberDeleteBtn	= By.cssSelector("[title='Delete Forwarding Number']");
	By callForwardingValidationMsg		= By.cssSelector("#callForwardingAlert .modal-body");
	By callForwardingCloseModalMsgBtn	= By.cssSelector("#callForwardingAlert button[data-dismiss='modal']");
	By callForwardingUpdationButton     = By.cssSelector(".option.pull-left.selected.active + div > .update-forwarding-option");
	By clickIntDialerForwarding         = By.xpath("//label/../input[@class='disabled-offline']");
	By addNewForwardginNumberListItem 	= By.cssSelector(".call-forwarding-selectize .selectize-dropdown-header-label");
	By newForwardingNumberNameTxtBox	= By.cssSelector("#call-forwarding-dialog-modal [placeholder='Name']");
	By newForwardingNumberTextBox 		= By.cssSelector(".call-forwarding-container .number");
	By verifyForwardingNumberButton 	= By.cssSelector(".btn.pull-right.verify");
	By saveForwardingNumberButton 		= By.cssSelector("#settingsCallForwardingModal .btn.save");
	By cancelForwardingNumberButton 	= By.cssSelector("#settingsCallForwardingModal .btn.cancel");
	By doNotForwardWhenLogoutCheckBox	= By.xpath(".//*[@class='call-forwarding-label']/preceding-sibling::input[@type='checkbox']");
	By verifyingForwardingNotificationText = By.cssSelector("#settingsCallForwardingModal .pull-left.notification");
	By verificationCompleteMessage 		= By.xpath("//*[text()='Verification complete']");
	By stayConnectedLabel				= By.xpath(".//*[text()=' Stay Connected']");
	By clickToVoicemailLabel            = By.xpath(".//*[text()='Voicemail']");
	By stayConnectdCheckBox		 		= By.id("continuousBridgeEnabled");
	By callNotificationLabel			= By.xpath(".//*[text()='Call Notification']");
	By sipClientLabel			        = By.xpath("//span[@class = 'showSipSettings']/parent::td");
	By callNotificationCheckBox 		= By.id("callNotification");
	By callNotificationOnToggleButton 	= By.cssSelector(".bootstrap-switch-id-callNotification .bootstrap-switch-handle-on");
	By sipCheckBox						= By.id("sipEnabled");
	By sipOnToggleButton				= By.cssSelector(".bootstrap-switch-id-sipEnabled .bootstrap-switch-handle-on");
	By sipOffToggleButton				= By.cssSelector(".bootstrap-switch-id-sipEnabled .bootstrap-switch-handle-off");
	By noAnswerNumberCheckBox 			= By.id("noAnswerNumberEnabled");
	By noAnswerNumberOnToggleButton 	= By.cssSelector(".bootstrap-switch-id-noAnswerNumberEnabled .bootstrap-switch-handle-on");
	By noAnswerNumberOffToggleButton 	= By.cssSelector(".bootstrap-switch-id-noAnswerNumberEnabled .bootstrap-switch-handle-off");
  	By noAnswerNumberTextBox 			= By.cssSelector("[data-info-section='no-answer-number'] .selectize-input input");
  	By noAnswerNumberLoc 				= By.cssSelector("[data-info-section='no-answer-number'] .item");
  	By noAnswerDropDownButton		 	= By.cssSelector("[data-info-section='no-answer-number'] .selectize-input");
  	By noAnswerDeleteButton 			= By.cssSelector("[data-info-section='no-answer-number'] .remove-option");
  	String FwdNumberEditBtn				= ".//div[@data-value='$$Number$$']//following-sibling::div//img[@class='update-forwarding-option']";
  	String FwdNumberDelBtn				= ".//div[@data-value='$$Number$$']//following-sibling::div//img[@class='remove-forwarding-option btn-delete']";
  	String FwdNumberHelpIcon			= ".//div[@data-value='$$Number$$']//following-sibling::div//img[@class='help-forwarding-option']";
  	String FwdNumberToolTipText			= ".//div[@data-value='$$Number$$']//following-sibling::div//div[@class='tooltip-inner']";
  
  	//Outbound Numbers
  	By outboundNumer					= By.id("outboundNumber");
  	By outboundNumberTabLink 			= By.cssSelector("[data-target='#outbound-number-settings']");  
  	By addOutboundNumberButton 			= By.cssSelector("button[data-target='#settingsOutboundModal']");
  	By outboundNumberSelectCheckbox 	= By.xpath("//*[not(@style='display: none;') and contains(@class,'outbound-number-delete')]/img[@src='images/btn-delete.svg' and @title='Delete Outbound Number']/ancestor::tr//input[@class='outbound-number-selected']");
  	By outboundNumberListLoc 			= By.xpath("//*[not(@style='display: none;') and contains(@class,'outbound-number-delete')]/img[@src='images/btn-delete.svg' and @title='Delete Outbound Number']/ancestor::tr//*[@class='outbound-number']");
  	By outboundNumberDeleteButton 		= By.cssSelector(".delete.outbound-number-delete:not([style='display: none;'])");
  	By addOutboundNumberLabel 			= By.id("phoneLabel");
  	By addOutboundPhoneNumber 			= By.id("phoneNumber");
  	By addOuboundNumberVerifyButton 	= By.id("verify");
  	By addOutboundNumberVeriCode 		= By.id("verificationCode");
  	By addOutboundNumberMessage			= By.id("message");
  	By addOutboundNumberCloseButton 	= By.cssSelector(".btn.btn-primary.save");
  	By additionalNumbersSelectCheckbox 	= By.xpath("//*[(@style='display: none;') and contains(@class,'outbound-number-delete')]/ancestor::tr//input[@class='outbound-number-selected']");
  	By additionalNumberListLoc 			= By.xpath("//*[(@style='display: none;') and contains(@class,'outbound-number-delete')]/ancestor::tr//*[@class='outbound-number']");
  	By additionalNumberLabelLoc			= By.xpath("//*[(@style='display: none;') and contains(@class,'outbound-number-delete')]/ancestor::tr//*[@class='outbound-number-label']");
  	By defaultNumberCheckBoxLoc			= By.xpath(".//*[@id='outbound-section']//strong[@class='outbound-number-label' and (contains(text(),'Smart Number (Default)'))]/ancestor::tr//input[@type='checkbox']");
  	String deleteNumberIcon				= ".//*[@id = 'outbound-number-settings']//span[text()='$$Number$$']/../..//div[contains(@class, 'delete')]";
  	String additionalNumberAreaCodeLoc  = ".//*[@id='outbound-section']//strong[@class='outbound-number-label' and not(contains(text(),'Smart Number (Default)'))]/following-sibling::span[contains(text(),'($areaCode$)')]/../parent::tr//input[@type='checkbox']";
  	String additionalNumberLabel		= ".//*[@class='outbound-number-label' and following-sibling::span[text()='$$SmartNumber$$']]";
  	
  	//VoiceMailTab
  	By addVoiceMailButton 			= By.cssSelector("button[data-action='create-voicemail']");
  	By voiceMailNameInput			= By.cssSelector(".voicemail-label input");
  	By recordVoiceMailButton 		= By.cssSelector("button.record-voicemail");
  	By stopVoceMailRecordButton 	= By.cssSelector("button.stop-recording-voicemail");
  	By voiceMailPlayButton			= By.cssSelector("#settingsVoicemailModal .play-voicemail");
  	By saveVoiceMailButton 			= By.cssSelector("#settingsVoicemailModal button.save");
  	By voiceMailNameList			= By.cssSelector("#voicemail-section table .voicemail-label");
  	By voiceMailPlayButtonList		= By.cssSelector("#voicemail-section table .play-voicemail");
  	By voiceMailStopButtonList		= By.cssSelector("#voicemail-section table .stop-voicemail");
  	By voiceMailDeleteButtonList	= By.cssSelector("#voicemail-section table [title='Delete Voicemail Drop']");
  	By voiceMailMoveUpButton		= By.cssSelector("#automated-voicemail-settings .voicemail-up");
  	By voiceMailMoveDownButton		= By.cssSelector("#automated-voicemail-settings .voicemail-down");
  	By sendVMToTop					= By.xpath(".//*[@class='popover-content']//*[text()='Send to top']");
  	By sendVMToBottom				= By.xpath(".//*[@class='popover-content']//*[text()='Send to bottom']");
  	String playVoiceMailIcon		= ".//*[text()='$$VoiceMail$$']/ancestor::tr//button[contains(@class, 'play-voicemail') and not(@style='display: none;')]";
  	String stopVoiceMailIcon		= ".//*[text()='$$VoiceMail$$']/ancestor::tr//button[contains(@class, 'stop-voicemail') and not(@style='display: none;')]";
  	String globalVoiceMailIcon		= ".//*[text()='$$VoiceMail$$']/ancestor::tr//img[contains(@class,'global-img')]";
  	String deleteVoiceMailIcon		= ".//*[text()='$$VoiceMail$$']/ancestor::tr//img[contains(@class,'delete-img')]";
  	String updateVoiceMailIcon		= ".//*[text()='$$VoiceMail$$']/ancestor::tr//button[contains(@class,'update-voicemail')]";
  	
  	//custom greeting tab
  	By addCustomGreetingButton 		= By.cssSelector("[data-action='create-greeting']");
  	By customGreetingNameInput		= By.id("greetingInput");
  	By customGreetingPlayButton		= By.cssSelector("#settingsGreetingModal .play-voicemail");
  	By customGreetingRecordButton	= By.cssSelector("#settingsGreetingModal button.record-voicemail");
  	By stopCustomGreetingRecordBtn	= By.cssSelector("#settingsGreetingModal button.stop-recording-voicemail");
  	By oooCustomGreetingCheckBox	= By.className("out-of-office");
  	By oooCustomGreetingToolTipIcon	= By.xpath(".//input[@class='out-of-office']/following-sibling::img[@data-toggle='tooltip']");
	By oooCustomGreetingToolTipText	= By.cssSelector(".tooltip-inner");
  	By customGreetingSaveButton		= By.cssSelector("#settingsGreetingModal button.save");
  	By customGreetingUpdateButtons	= By.cssSelector("[data-action='manage-greeting']");
  	By customGreetingDeleteButtons	= By.cssSelector("div:not([style=\"display: none;\"])>[title='Delete Custom Greeting']");
  	By customGreetingListName		= By.className("greeting-name");
  	By customGreetingCheckBox		= By.cssSelector("input[class*='custom-greeting']");
  	By customGreetinListPlayBtn		= By.cssSelector("#greeting-section table button.play-greeting");
  	By extenalGreetingCheckBox		= By.cssSelector("#greetingNumber[type='checkbox']");
	By externalGreetingNoTextBox	= By.cssSelector("input.greeting-number");
	By addExternalGreetingNumberBtn	= By.cssSelector("button[data-action='create-greeting-number']");
	By externalGreetingToggleOn		= By.cssSelector(".bootstrap-switch-id-greetingNumber .bootstrap-switch-handle-on");
	By externalGreetingToggleOff	= By.cssSelector(".bootstrap-switch-id-greetingNumber .bootstrap-switch-handle-off");
	By addedExternalGreetingNumbers	= By.cssSelector("span.greeting-number");
	By externalGreetingDelBtns		= By.cssSelector("#greeting-number-section .btn-delete");
	By externalNumberToolTipImage	= By.cssSelector(".add-greeting-number [data-toggle='tooltip']");
	By externalNumberToolTipText	= By.cssSelector(".add-greeting-number .tooltip");
  	By customGreetingDialogueBoxCloseButton = By.cssSelector("#greeting-dialog-modal .close");
  	By voicemailDialogueBoxCloseButton 		= By.cssSelector("#settingsVoicemailModal .close");
  	
  	By buildInfo					= By.id("buildInfo");
  	
  	String playCustomGreeting		= ".//*[@class='greeting-name' and text()= '$$GreetingName$$']/ancestor::tr//button[contains(@class,'play-greeting') and not(@style='display: none;')]";
  	String oooGreetingIcon			= ".//*[@class='greeting-name' and text()= '$$GreetingName$$']/preceding-sibling::img[@class='icon-out-office']";

	public void clickSettingIcon(WebDriver driver) {
		if(!isElementVisible(driver, settingsIcon, 5)) {
			switchToTab(driver, 1);
			reloadSoftphone(driver);
		}
		isElementVisible(driver, settingsIcon, 10);
		clickByJs(driver, settingsIcon);
		waitUntilVisible(driver, settingPageHeading);
		isElementVisible(driver, spinnerWheel, 1);
		waitUntilInvisible(driver, spinnerWheel);
		closeErrorMessage(driver);
		scrollToElement(driver, buildInfo);
	}

	public boolean isSettingsIconVisible(WebDriver driver) {
		return isElementVisible(driver, settingsIcon, 5) || isElementVisible(driver, activeSettingIcon, 5); 
	}
	
	public void settingIconNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, activeSettingIcon);
	}

	public void closeErrorMessage(WebDriver driver) {
		if (isElementVisible(driver, errorCloseButton, 1)) {
			clickElement(driver, errorCloseButton);
		}
	}
	
	/**
	 * @param driver
	 * @return notification error visible
	 */
	public boolean IsNotificationErrorMessageVisible(WebDriver driver) {
		return isElementVisible(driver, notificationError, 6);
	}

	public void navigateToVoicemailDropTab(WebDriver driver) {
		clickElement(driver, voicemalDropTabLink);
		waitUntilVisible(driver, addVoiceMailButton);
	}

	public void navigateToCustomGreetingTab(WebDriver driver) {
		clickElement(driver, customGreetingTabLink);
		waitUntilVisible(driver, addCustomGreetingButton);
	}

	
	public void disableclickToCallSetting(WebDriver driver) {
		if (findElement(driver, clicktoCallCheckBox).isSelected()) {
			clickElement(driver, clicktoCallOnToggleButton);
		} else {
			System.out.println("click to call is aleady OFF");
		}
	}
	
	public void enableclickToCallSetting(WebDriver driver) {
		if (!findElement(driver, clicktoCallCheckBox).isSelected()) {
			clickElement(driver, clicktoCallOffToggleButton);
		} else {
			System.out.println("click to call is aleady On");
		}
	}
	
	public void disableDialNextSetting(WebDriver driver) {
		if (findElement(driver, dialNextCheckBox).isSelected()) {
			clickElement(driver, dialNextOnToggleButton);
		} else {
			System.out.println("Dial Next is aleady OFF");
		}
	}
	
	public void enableDialNextSetting(WebDriver driver) {
		if (!findElement(driver, dialNextCheckBox).isSelected()) {
			clickElement(driver, dialNextOffToggleButton);
		} else {
			System.out.println("Dial Next is aleady On");
		}
		
		checkCheckBox(driver, dialNextPreviewCheckBox);
	}

	public void enableLocalPresenceSetting(WebDriver driver) {
		if (!softPhoneCalling.isLocalPresenceSelected(driver)) {
			 softPhoneCalling.selectOutboundNumFromDropdown(driver, "Local presence");
			 softPhoneCalling.verifyLocalPresenceNumIconSelected(driver);
		} else {
			System.out.println("Local Presence is aleady ON");
		}
	}
	
	public void disableLocalPresenceSetting(WebDriver driver) {
		if (softPhoneCalling.isLocalPresenceSelected(driver)) {
			softPhoneCalling.selectOutboundNumUsingIndex(driver, 0);
		} else {
			System.out.println("Local Presence is aleady OFF");
		}
	}
	
	public void verifyLocalPresenctOptionNotPresent(WebDriver driver){
		assertFalse(softPhoneCalling.verifyLocalPresenctOptionNotPresent(driver));
	}
	
	public void verifyLocalPresenceSettingNotPresent(WebDriver driver){
		waitUntilInvisible(driver, localPresenceOffToggleButton);
		waitUntilInvisible(driver, localPresenceSMSCheckBox);
	}
	
	/**
	 * @param driver
	 * @Desc checking local Presence option is visible
	 * @return boolean
	 */
	public boolean verifyLocalPresenctOptionIsPresent(WebDriver driver){
		return !softPhoneCalling.verifyLocalPresenctOptionNotPresent(driver);
	}
	
	public boolean isLocalPresenceEnable(WebDriver driver){
		return softPhoneCalling.isLocalPresenceSelected(driver);
	}

	public void enableRecordCallsSetting(WebDriver driver) {
		System.out.println("Setting Record Calls to ON");
		if (!findElement(driver, recordCallCheckBox).isSelected()) {
			clickElement(driver, recordsCallOffToggleButton);
		} else {
			System.out.println("Record calls is already ON");
		}
	}

	public void disableRecordCallsSetting(WebDriver driver) {
		System.out.println("Setting Record Calls to OFF");
		if (findElement(driver, recordCallCheckBox).isSelected()) {
			clickElement(driver, recordsCallOnToggleButton);
		} else {
			System.out.println("Record calls is already OFF");
		}
	}

	public void verifyRecordOptionIsInvisible(WebDriver driver) {
		waitUntilInvisible(driver, recordCallText);
	}

	public void scrollToNoAnswerSetting(WebDriver driver) {
		scrollToElement(driver, noAnswerNumberOnToggleButton);
	}
	
	public void verifyCallForwardingIsVisible(WebDriver driver){
		waitUntilVisible(driver, callForwardingLabel);
	}
	
	public void verifyCallForwardingIsInvisible(WebDriver driver){
		waitUntilInvisible(driver, callForwardingLabel);
	}

	public void disableCallForwardingSettings(WebDriver driver) {
		System.out.println("Setting call forwarding to OFF");
		if (findElement(driver, callForwardingCheckBox).isSelected()) {
			clickByJs(driver, callForwardingOnToggleButton);
		} else {
			System.out.println("Call Forwarding is already OFF");
		}
	}

	public void enableCallForwardingSettings(WebDriver driver) {
		System.out.println("Setting call forwarding to ON");
		if (!findElement(driver, callForwardingCheckBox).isSelected()) {
			clickByJs(driver, callForwardingOffToggleButton);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding is already ON");
		}
	}
	
	public void verifyStayConnectedIsVisible(WebDriver driver){
		waitUntilVisible(driver, stayConnectedLabel);
	}
	
	public void verifyStayConnectedIsInvisible(WebDriver driver){
		waitUntilInvisible(driver, stayConnectedLabel);
	}
	
	public void verifyVoicemailIsVisible(WebDriver driver){
		waitUntilVisible(driver, clickToVoicemailLabel);
	}
	
	public void verifyVoicemailIsInvisible(WebDriver driver){
		waitUntilInvisible(driver, clickToVoicemailLabel);
	}
	
	public void verifySipClientLabelVisible(WebDriver driver){
		waitUntilVisible(driver, sipClientLabel);
	}
	
	public void verifySipClientLabelIsInvisible(WebDriver driver){
		waitUntilInvisible(driver, sipClientLabel);
	}

	public void enableStayConnectedSetting(WebDriver driver) {
		if (!findElement(driver, stayConnectdCheckBox).isSelected()) {
			clickElement(driver, stayConnectdCheckBox);
		} else {
			System.out.print("Stay connected setting already enabled");
		}
	}

	public void disableStayConnectedSetting(WebDriver driver) {
		if (findElement(driver, stayConnectdCheckBox).isSelected()) {
			clickElement(driver, stayConnectdCheckBox);
		} else {
			System.out.print("Stay connected setting already disabled");
		}
	}

	public void disableCallNotificationsSettings(WebDriver driver) {
		System.out.println("Setting call notification to OFF");
		if (findElement(driver, callNotificationCheckBox).isSelected()) {
			clickElement(driver, callNotificationOnToggleButton);
		} else {
			System.out.println("call notification is already OFF");
		}
	}
	
	public void enableSipSettings(WebDriver driver) {
		System.out.println("Setting call notification to OFF");
		if (!findElement(driver, sipCheckBox).isSelected()) {
			clickElement(driver, sipOffToggleButton);
		} else {
			System.out.println("sip setting is already ON");
		}
	}
	
	public void disableSipSettings(WebDriver driver) {
		System.out.println("Setting call notification to OFF");
		if (findElement(driver, sipCheckBox).isSelected()) {
			clickElement(driver, sipOnToggleButton);
		} else {
			System.out.println("sip setting is already off");
		}
	}
	
	public void verifyCallNotificationIsInvisible(WebDriver driver) {
		waitUntilInvisible(driver, callNotificationLabel);
	}

	public void verifyCallNotificationIsVisible(WebDriver driver) {
		waitUntilVisible(driver, callNotificationLabel);
	}
	
	public void disableNoAnswerSettings(WebDriver driver) {
		System.out.println("Setting No Answer setting to OFF");
		if (findElement(driver, noAnswerNumberCheckBox).isSelected()) {
			clickElement(driver, noAnswerNumberOnToggleButton);
		} else {
			System.out.println("No Answer setting is already OFF");
		}
	}

	public void enableNoAnswerSettings(WebDriver driver) {
		System.out.println("Setting No Answer setting to OFF");
		idleWait(2);
		if (!findElement(driver, noAnswerNumberCheckBox).isSelected()) {
			clickElement(driver, noAnswerNumberOffToggleButton);
		} else {
			System.out.println("No Answer setting is already On");
		}
	}

	public void scrollToTabsTop(WebDriver driver) {
		scrollToElement(driver, generalSettingLink);
	}

	// Outbound Number Tab section starts here
	public String getSelectedOutboundNumber(WebDriver driver){
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, outboundNumer));
	}
	
	public void clickOutboundNumbersTab(WebDriver driver) {
		clickElement(driver, outboundNumberTabLink);
		findElement(driver, addOutboundNumberButton);
	}

	public String selectAdditionalNumberUsingIndex(WebDriver driver, int index) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		selectFirstAdditionalNumberAsDefault(driver);
		List<WebElement> additionalNumbersCheckBoxList = getElements(driver, additionalNumbersSelectCheckbox);
		WebElement additionalNumberToSelect = additionalNumbersCheckBoxList.get(index);
		if (!additionalNumberToSelect.isSelected()) {
			additionalNumberToSelect.click();
		}
		return HelperFunctions.getNumberInSimpleFormat(getElements(driver, additionalNumberListLoc).get(index).getText());
	}
	
	public int getAdditionalNumbersIndex(WebDriver driver, String Number) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		selectFirstAdditionalNumberAsDefault(driver);
		List<WebElement> additionalNumbersList = getElements(driver, additionalNumberListLoc);
		for (int i = 0; i < additionalNumbersList.size(); i++) {
			String additionalNumberText = HelperFunctions.getNumberInSimpleFormat(additionalNumbersList.get(i).getText());
			if (Number.contains(additionalNumberText))
				return i;
		}
		return -1;
	}	
	
	public void verifyAdditionalNumberLabel(WebDriver driver, String additionalNumber, String label){
		waitUntilTextPresent(driver, By.xpath(additionalNumberLabel.replace("$$SmartNumber$$", additionalNumber)), label);
	}
	
	public String getSelectedOutBoundNumberOnSoftPhone(WebDriver driver) {
		String selectedNumber = null;
		List<WebElement> selectedNumbersCheckBoxList = getElements(driver, additionalNumbersSelectCheckbox);
		for (int i = 0; i < selectedNumbersCheckBoxList.size(); i++) {
			if (selectedNumbersCheckBoxList.get(i).isSelected()) {
				selectedNumber = HelperFunctions.getNumberInSimpleFormat(getElements(driver, additionalNumberListLoc).get(i).getText());
			}
		}
		return selectedNumber;
	}

	public List<String> getLocalPresenceAdditionalNumbers(WebDriver driver, String areaCode) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		selectFirstAdditionalNumberAsDefault(driver);
		List<String> localPresenceNumbersList = new ArrayList<>();
		List<WebElement> additionalNumbersList = getElements(driver, additionalNumberListLoc);
		for (int i = 0; i < additionalNumbersList.size(); i++) {
			String additionalNumberText = HelperFunctions.getNumberInSimpleFormat(additionalNumbersList.get(i).getText());
			if (areaCode.equals(additionalNumberText.substring(0, 3)))
				localPresenceNumbersList.add(additionalNumberText);
		}
		return localPresenceNumbersList;
	}

	public void selectFirstAdditionalNumberAsDefault(WebDriver driver) {
		WebElement firstAdditionalNumberCheckBox = getElements(driver, additionalNumbersSelectCheckbox).get(0);
		if (!firstAdditionalNumberCheckBox.isSelected()) {
			firstAdditionalNumberCheckBox.click();
			assertTrue(firstAdditionalNumberCheckBox.isDisplayed());
		}
	}
	
	public void selectAdditionalNumberAccToAreaCode(WebDriver driver, String areaCode) {
		By additionalLoc = By.xpath(additionalNumberAreaCodeLoc.replace("$areaCode$", areaCode));
		if (!getElements(driver, additionalLoc).get(0).isSelected()) {
			getElements(driver, additionalLoc).get(0).click();
		}
	}
	
	public void selectDefaultNumber(WebDriver driver) {
		waitUntilVisible(driver, defaultNumberCheckBoxLoc);
		checkCheckBox(driver, defaultNumberCheckBoxLoc);
	}	

	public List<WebElement> getOutboundNumbersList(WebDriver driver) {
		return getElements(driver, outboundNumberListLoc);
	}

	public void selectOutboundNumberUsingIndex(WebDriver driver, int index) {
		List<WebElement> outboundNumbersCheckBoxList = getElements(driver, outboundNumberSelectCheckbox);
		WebElement outboundNumberToSelect = outboundNumbersCheckBoxList.get(index);
		if (!outboundNumberToSelect.isSelected()) {
			outboundNumberToSelect.click();
		}
	}

	public List<String> getLocalPresenceOutboundNumbers(WebDriver driver, String areaCode) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		selectFirstAdditionalNumberAsDefault(driver);
		List<String> localPresenceNumbersList = new ArrayList<>();
		List<WebElement> OutboundNumbersList = getElements(driver, outboundNumberListLoc);
		for (int i = 1; i < OutboundNumbersList.size(); i++) {
			String outboundNumberText = HelperFunctions.getNumberInSimpleFormat(OutboundNumbersList.get(i).getText());
			if (areaCode.equals(outboundNumberText.substring(0, 3)))
				localPresenceNumbersList.add(outboundNumberText);
		}
		return localPresenceNumbersList;
	}

	public String selectLastOutboundNumber(WebDriver driver) {
		int listSize = getOutboundNumbersList(driver).size();
		assertTrue(listSize >= 1);
		String outBoundNumber = HelperFunctions.getNumberInSimpleFormat(getOutboundNumbersList(driver).get(listSize - 1).getText());
		selectOutboundNumberUsingIndex(driver, listSize - 1);
		return outBoundNumber;
	}

	public String selectLastOutboundNumberAndGetNumber(WebDriver driver) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		selectFirstAdditionalNumberAsDefault(driver);
		return selectLastOutboundNumber(driver);
	}

	public int isNumberExistInOutboundNumber(WebDriver driver, String number) {
		selectFirstAdditionalNumberAsDefault(driver);
		List<WebElement> outboundNumbersCheckBoxList = getOutboundNumbersList(driver);
		for (int i = 0; i < outboundNumbersCheckBoxList.size(); i++) {
			if (number.contains(HelperFunctions.getNumberInSimpleFormat(outboundNumbersCheckBoxList.get(i).getText()))) {
				return i;
			}
		}
		return -1;
	}

	public void clickAddOutboundNumerbutton(WebDriver driver) {
		clickElement(driver, addOutboundNumberButton);
		waitUntilVisible(driver, addOuboundNumberVerifyButton);
		waitUntilVisible(driver, addOutboundNumberLabel);
	}

	public void deleteOutboundNumberIfExist(WebDriver driver, String number) {
		int deleteButtonIndex = isNumberExistInOutboundNumber(driver, number);
		if (deleteButtonIndex >= 0) {
			getElements(driver, outboundNumberDeleteButton).get(deleteButtonIndex).click();
			System.out.println("Outbound Number Deleted");
		} else {
			System.out.println("Outbound Number Does not Exist");
		}
	}
	
	public void deleteOutboundNumber(WebDriver driver, String number) {
		By deleteButton = By.xpath(deleteNumberIcon.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(number)));
		clickElement(driver, deleteButton);
	}
	
	public void enterOutboundNumberDetails(WebDriver driver, WebDriver outBoundNumberDriver, String number, String numberLabel){
		clickAddOutboundNumerbutton(driver);
		enterText(driver, addOutboundNumberLabel, numberLabel);
		enterText(driver, addOutboundPhoneNumber, HelperFunctions.getNumberInSimpleFormat(number));
		clickElement(driver, addOuboundNumberVerifyButton);
		if (isElementVisible(driver, addOutboundNumberVeriCode, 5)) {
			String verificationCode = getElementsText(driver, addOutboundNumberVeriCode);
			softPhoneCalling.pickupIncomingCall(outBoundNumberDriver);
			softPhoneCalling.openDialPad(outBoundNumberDriver);
			for (int i = 0; i < verificationCode.length(); i++) {
				softPhoneCalling.enterNumberInDialpad(outBoundNumberDriver, Character.toString(verificationCode.toCharArray()[i]));
			}
			softPhoneCalling.isCallBackButtonVisible(outBoundNumberDriver);
			softPhoneCalling.clickDialPadIcon(outBoundNumberDriver);
		}
	}

	public void addOutboundNumber(WebDriver driver, WebDriver outBoundNumberDriver, String number, String numberLabel) {
		clickSettingIcon(driver);
		clickOutboundNumbersTab(driver);
		//deleteOutboundNumberIfExist(driver, number);
		//assertTrue(isNumberExistInOutboundNumber(driver, number) == -1);
		enterOutboundNumberDetails(driver, outBoundNumberDriver, number, numberLabel);
		clickElement(driver, addOutboundNumberCloseButton);
		idleWait(2);
	}
	
	public void verifyOutboundMessagPresent(WebDriver driver, String message){
		clickElement(driver, addOuboundNumberVerifyButton);
		waitUntilTextPresent(driver, addOutboundNumberMessage, message);
		clickElement(driver, addOutboundNumberCloseButton);
		waitUntilInvisible(driver, addOutboundNumberCloseButton);
	}
	// Outbound Number Tab section ends here

	// Voicemail Tab
	public void clickAddVoicemailButton(WebDriver driver) {
		waitUntilVisible(driver, addVoiceMailButton);
		clickByJs(driver, addVoiceMailButton);
		waitUntilVisible(driver, voiceMailNameInput);
	}

	public void enterVoiceMailName(WebDriver driver, String voiceMailName) {
		enterText(driver, voiceMailNameInput, voiceMailName);
	}

	public boolean isVoiceMailRecordButtonEnable(WebDriver driver) {
		waitUntilVisible(driver, recordVoiceMailButton);
		return findElement(driver, stopVoceMailRecordButton).isEnabled();
	}
	
	public void clickVoiceMailRecordButton(WebDriver driver) {
		clickElement(driver, recordVoiceMailButton);
		waitUntilVisible(driver, stopVoceMailRecordButton);
	}

	public void clickVoiceMailStopButton(WebDriver driver) {
		clickElement(driver, stopVoceMailRecordButton);
		waitUntilVisible(driver, voiceMailPlayButton);
	}
	
	public void clickVoiceMailPlayButton(WebDriver driver) {
		clickElement(driver, voiceMailPlayButton);
	}

	public void clickSaveVoiceMailButton(WebDriver driver) {
		clickElement(driver, saveVoiceMailButton);
		waitUntilInvisible(driver, recordVoiceMailButton);
	}
	
	public void closeVoicemailDialogueBox(WebDriver driver) {
		clickElement(driver, voicemailDialogueBoxCloseButton);
		waitUntilInvisible(driver, voicemailDialogueBoxCloseButton);
	}

	public void createVoiceMail(WebDriver driver, String voiceMailName, int duration) {
		clickAddVoicemailButton(driver);
		enterVoiceMailName(driver, voiceMailName);
		clickVoiceMailRecordButton(driver);
		idleWait(duration);
		clickVoiceMailStopButton(driver);
		clickVoiceMailPlayButton(driver);
		clickSaveVoiceMailButton(driver);
	}
	
	public List<String> getVoiceMailList(WebDriver driver) {
		return getTextListFromElements(driver, voiceMailNameList);
	}

	public int getVoiceMailIndex(WebDriver driver, String voiceMailName) {
		if (isListElementsVisible(driver, voiceMailNameList, 5)) {
			List<WebElement> voiceMailList = getElements(driver, voiceMailNameList);
			for (int i = 0; i < voiceMailList.size(); i++) {
				if (voiceMailList.get(i).getText().equals(voiceMailName)) {
					voiceMailList.get(i).click();
					return i;
				}
			}
		}
		return -1;
	}

	public boolean isVoiceMailVisible(WebDriver driver, String voiceMailName) {
		By voiceMailLoc = By.xpath(playVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		scrollTillEndOfPage(driver);
		return isElementVisible(driver, voiceMailLoc, 5);
	}
	
	public void playVoiceMail(WebDriver driver, String voiceMailName) {
		By voiceMailLoc = By.xpath(playVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		waitUntilVisible(driver, voiceMailLoc);
		scrollToElement(driver, voiceMailLoc);
		clickByJs(driver, voiceMailLoc);
	}
	
	public void stopVoiceMailPlay(WebDriver driver, String voiceMailName) {
		int index = getVoiceMailIndex(driver, voiceMailName);
		assertNotEquals(-1, index);
		getInactiveElements(driver, voiceMailStopButtonList).get(index).click();
	}

	public void deleteVoiceMail(WebDriver driver, String voiceMailName) {
		int index = getVoiceMailIndex(driver, voiceMailName);
		while (index != -1) {
			getInactiveElements(driver, voiceMailDeleteButtonList).get(index).click();
			index = getVoiceMailIndex(driver, voiceMailName);
		}
		assertEquals(-1, index);
	}

	public void moveUpVoiceMail(WebDriver driver, String voiceMailName, int moveUpToSteps) {
		int currentIndex = getVoiceMailIndex(driver, voiceMailName);
		if (currentIndex - moveUpToSteps >= 0) {
			for (int i = 0; i < moveUpToSteps; i++) {
				getMoveUpbuttonsList(driver).get(currentIndex - i).click();
			}
		} else {
			Assert.fail();
		}
		assertEquals(getVoiceMailIndex(driver, voiceMailName), currentIndex - moveUpToSteps);
	}

	public void moveDownVoiceMail(WebDriver driver, String voiceMailName, int moveDownToSteps) {
		int currentIndex = getVoiceMailIndex(driver, voiceMailName);
		if (currentIndex <= getElements(driver, voiceMailNameList).size() - 1) {
			for (int i = 0; i < moveDownToSteps; i++) {
				getMoveDownbuttonsList(driver).get(currentIndex + i).click();
			}
		} else {
			Assert.fail();
		}
		assertEquals(getVoiceMailIndex(driver, voiceMailName), getElements(driver, voiceMailNameList).size() - 1);
	}
	
	public void verifyGlobalVoicemaildropOnSoftphone(WebDriver driver, String voiceMailName) {
		By globalIconLoc = By.xpath(globalVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertTrue((isElementVisible(driver, globalIconLoc, 5)));
		By updateIconLoc = By.xpath(updateVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertFalse((isElementVisible(driver, updateIconLoc, 5)));
	}
	
	public void verifyGroupVoicemailDropOnDialer(WebDriver driver, String voiceMailName) {
		By globalIconLoc = By.xpath(globalVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertFalse((isElementVisible(driver, globalIconLoc, 5)));
		By deleteIconLoc = By.xpath(deleteVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertFalse((isElementVisible(driver, deleteIconLoc, 5)));
		By updateIconLoc = By.xpath(updateVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertFalse((isElementVisible(driver, updateIconLoc, 5)));
	}
	
	public void verifyUsersVoicemailDropOnDialer(WebDriver driver, String voiceMailName) {
		By deleteIconLoc = By.xpath(deleteVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertTrue((isElementVisible(driver, deleteIconLoc, 5)));
		By updateIconLoc = By.xpath(updateVoiceMailIcon.replace("$$VoiceMail$$", voiceMailName));
		assertTrue((isElementVisible(driver, updateIconLoc, 5)));
	}
	
	public void verifyFirstMoveUpButtonDisabled(WebDriver driver){
		List<WebElement> moveUpButtonsList = getMoveUpbuttonsList(driver);
		assertEquals(getCssValue(driver, moveUpButtonsList.get(0), CssValues.Color), "rgba(153, 153, 153, 1)");
		assertEquals(getCssValue(driver, moveUpButtonsList.get(0), CssValues.PointerEvents), "none");
	}
	
	public void verifyLastMoveDownButtonDisabled(WebDriver driver){
		List<WebElement> moveDownButtonsList = getMoveDownbuttonsList(driver);
		assertEquals(getCssValue(driver, moveDownButtonsList.get(moveDownButtonsList.size() - 1), CssValues.Color), "rgba(153, 153, 153, 1)");
		assertEquals(getCssValue(driver, moveDownButtonsList.get(moveDownButtonsList.size() - 1), CssValues.PointerEvents), "none");
	}

	public List<WebElement> getMoveUpbuttonsList(WebDriver driver) {
		List<WebElement> moveUpButtonsList = getElements(driver, voiceMailMoveUpButton);
		return moveUpButtonsList;
	}

	public List<WebElement> getMoveDownbuttonsList(WebDriver driver) {
		List<WebElement> moveDownButtonsList = getElements(driver, voiceMailMoveDownButton);
		return moveDownButtonsList;
	}

	public void sendVMToTop(WebDriver driver, String voiceMailName) {
		int currentIndex = getVoiceMailIndex(driver, voiceMailName);
		if (currentIndex != 0) {
			clickAndHold(driver, getMoveUpbuttonsList(driver).get(currentIndex));
			waitUntilVisible(driver, sendVMToTop);
			clickElement(driver, sendVMToTop);
		} else {
			System.out.println("voicemail is already on the top");
		}
		assertEquals(getVoiceMailIndex(driver, voiceMailName), 0);
	}

	public void sendVMToBottom(WebDriver driver, String voiceMailName) {
		int currentIndex = getVoiceMailIndex(driver, voiceMailName);
		if (currentIndex != getElements(driver, voiceMailNameList).size() - 1) {
			clickAndHold(driver, getMoveDownbuttonsList(driver).get(currentIndex));
			waitUntilVisible(driver, sendVMToBottom);
			clickElement(driver, sendVMToBottom);
		} else {
			System.out.println("voicemail is already on the bottom");
		}
		assertEquals(getVoiceMailIndex(driver, voiceMailName), getElements(driver, voiceMailNameList).size() - 1);
	}
	// VoiceMail Tab Section ends Here

	/******* Custom Greeting Section startes here *******/
	public void clickAddNewCustomGreetingButton(WebDriver driver) {
		waitUntilInvisible(driver, backGroundFade);
		clickElement(driver, addCustomGreetingButton);
	}

	public void closeCustomGreetingDialogueBox(WebDriver driver) {
		clickElement(driver, customGreetingDialogueBoxCloseButton);
		waitUntilInvisible(driver, customGreetingDialogueBoxCloseButton);
	}

	public void clickAddCustomGreetingbtn(WebDriver driver) {
		waitUntilVisible(driver, addCustomGreetingButton);
		clickElement(driver, addCustomGreetingButton);
		waitUntilVisible(driver, customGreetingNameInput);
	}

	public void enterCustomGreetingName(WebDriver driver, String customGreetingName) {
		waitUntilVisible(driver, customGreetingNameInput);
		enterText(driver, customGreetingNameInput, customGreetingName);
	}

	public boolean isCustomGreetingRecordBtnEnable(WebDriver driver) {
		waitUntilVisible(driver, customGreetingRecordButton);
		return findElement(driver, stopCustomGreetingRecordBtn).isEnabled();
	}
	
	public void clickCustomGreetingRecordBtn(WebDriver driver) {
		clickElement(driver, customGreetingRecordButton);
		waitUntilVisible(driver, stopCustomGreetingRecordBtn);
	}

	public void clickCustomGreetingStopButton(WebDriver driver) {
		clickElement(driver, stopCustomGreetingRecordBtn);
		waitUntilVisible(driver, customGreetingPlayButton);
	}
	
	public void clickCustomGreetingDialoguePlayButton(WebDriver driver){
		waitUntilVisible(driver, customGreetingPlayButton);
		clickElement(driver, customGreetingPlayButton);
		waitUntilVisible(driver, customGreetingPlayButton);
	}
	
	public void checkOOOGreetingCheckBox(WebDriver driver){
		idleWait(1);
		if(!findElement(driver, oooCustomGreetingCheckBox).isSelected()){
			clickElement(driver, oooCustomGreetingCheckBox);
		}else{
			System.out.println("out of office greeting is already selected");
		}
	}
	
	public void uncheckOOOGreetingCheckBox(WebDriver driver){
		idleWait(1);
		if(findElement(driver, oooCustomGreetingCheckBox).isSelected()){
			clickElement(driver, oooCustomGreetingCheckBox);
		}else{
			System.out.println("out of office greeting is already disabled");
		}
	}

	public void clickSaveCustomGreetingButton(WebDriver driver) {
		clickElement(driver, customGreetingSaveButton);
		waitUntilInvisible(driver, customGreetingRecordButton);
	}
	
	public void selectCustomGreeting(WebDriver driver, String customGreetingName){
		int index = getCustomGreetingIndex(driver, customGreetingName);
		if(!getElements(driver, customGreetingCheckBox).get(index).isSelected()){
			getElements(driver, customGreetingCheckBox).get(index).click();	
		}
	}
	
	public int getCustomGreetingIndex(WebDriver driver, String customGreetingName) {
		List<WebElement> customGreetingList = getElements(driver, customGreetingListName);
		for (int i = 0; i < customGreetingList.size(); i++) {
			if (customGreetingList.get(i).getText().equals(customGreetingName)) {
				customGreetingList.get(i).click();
				return i;
			}
		}
		return -1;
	}
	
	public boolean isCustomGreetingSelected(WebDriver driver, String customGreetingName){
		int index = getCustomGreetingIndex(driver, customGreetingName);
		return getElements(driver, customGreetingCheckBox).get(index).isSelected();
	}
	
	public void verifyDefaultGreetingUpdateDeleteButton(WebDriver driver){
		assertFalse(getInactiveElements(driver, customGreetingUpdateButtons).get(0).isDisplayed());
		assertFalse(getInactiveElements(driver, By.cssSelector("[title='Delete Custom Greeting'")).get(0).isDisplayed());
	}
	
	public boolean isCustomGreetingVisible(WebDriver driver, String customGreetingName) {
		By customGreetingLoc = By.xpath(playCustomGreeting.replace("$$GreetingName$$", customGreetingName));
		scrollTillEndOfPage(driver);
		return isElementVisible(driver, customGreetingLoc, 5);
	}
	
	public void playCustomGreeting(WebDriver driver, String customGreetingName) {
		By customGreetingLoc = By.xpath(playCustomGreeting.replace("$$GreetingName$$", customGreetingName));
		waitUntilVisible(driver, customGreetingLoc);
		scrollIntoView(driver, customGreetingLoc);
		clickByJs(driver, customGreetingLoc);
	}
	
	public void createCustomGreeting(WebDriver driver, String customGreetingName, int duration) {
		clickAddNewCustomGreetingButton(driver);
		assertFalse(findElement(driver, customGreetingPlayButton).isEnabled());
		assertTrue(findElement(driver, customGreetingRecordButton).isEnabled());
		assertFalse(findElement(driver, customGreetingPlayButton).isEnabled());
		enterCustomGreetingName(driver, customGreetingName);
		clickCustomGreetingRecordBtn(driver);
		idleWait(duration);
		clickCustomGreetingStopButton(driver);
		clickCustomGreetingDialoguePlayButton(driver);
		clickSaveCustomGreetingButton(driver);
	}
	
	public void updateCustomGreeting(WebDriver driver, String oldCustomGreetingName, String newCustomGreetingName, int duration) {
		clickUpdateCustomGreeting(driver, oldCustomGreetingName);
		enterCustomGreetingName(driver, newCustomGreetingName);
		clickCustomGreetingRecordBtn(driver);
		idleWait(duration);
		clickCustomGreetingStopButton(driver);
		clickCustomGreetingDialoguePlayButton(driver);
		clickSaveCustomGreetingButton(driver);
	}
	
	public void createOOOCustomGreeting(WebDriver driver, String customGreetingName, int duration) {
		clickAddNewCustomGreetingButton(driver);
		assertFalse(findElement(driver, customGreetingPlayButton).isEnabled());
		assertTrue(findElement(driver, customGreetingRecordButton).isEnabled());
		assertFalse(findElement(driver, customGreetingPlayButton).isEnabled());
		idleWait(1);
		verifyOOOGrettingToolTip(driver);
		enterCustomGreetingName(driver, customGreetingName);
		clickCustomGreetingRecordBtn(driver);
		idleWait(duration);
		clickCustomGreetingStopButton(driver);
		clickCustomGreetingDialoguePlayButton(driver);
		checkOOOGreetingCheckBox(driver);
		clickSaveCustomGreetingButton(driver);
	}
	
	public void verifyOOOGrettingToolTip(WebDriver driver){		
		hoverElement(driver, oooCustomGreetingToolTipIcon);
		waitUntilTextPresent(driver, oooCustomGreetingToolTipText, "When this is selected, a reminder will appear on the intelligent dialer to inform your \"Out of Office\" greeting is active.");
	}
	
	public void verifyOOOGreetingCreated(WebDriver driver, String customGreetingName){
		By oooGreetingIconLoc = By.xpath(oooGreetingIcon.replace("$$GreetingName$$", customGreetingName));
		waitUntilVisible(driver, oooGreetingIconLoc);
	}
	
	public void verifyGreetingIsNotOOO(WebDriver driver, String customGreetingName){
		By oooGreetingIconLoc = By.xpath(oooGreetingIcon.replace("$$GreetingName$$", customGreetingName));
		waitUntilInvisible(driver, oooGreetingIconLoc);
	}
	
	public void clickUpdateCustomGreeting(WebDriver driver, String customGreetingName) {
		int index = getCustomGreetingIndex(driver, customGreetingName);
		assertNotEquals(-1, index);
		getInactiveElements(driver, customGreetingUpdateButtons).get(index).click();
	}
	
	public void deleteCustomGreeting(WebDriver driver, String customGreetingName) {
		int index = getCustomGreetingIndex(driver, customGreetingName);
		while (index != -1) {
			getInactiveElements(driver, customGreetingDeleteButtons).get(index - 1).click();
			index = getCustomGreetingIndex(driver, customGreetingName);
		}
		assertEquals(-1, index);
	}
	
	public void deleteAllCustomGreetings(WebDriver driver) {
		while(isElementVisible(driver, customGreetingDeleteButtons, 0)){
			WebElement deleteBtn = findElement(driver, customGreetingDeleteButtons);
			deleteBtn.click();
			waitUntilInvisible(driver, deleteBtn);
		}
	}
	
	public void enableExternalGreeting(WebDriver driver, String externalNumber){
		if(!isExternalGreetingNumberAdded(driver, externalNumber)){
			addExternalNumber(driver, externalNumber);
		}
		if(!findElement(driver, extenalGreetingCheckBox).isSelected()){
			clickElement(driver, externalGreetingToggleOff);
			return;
		}else{
			System.out.println("External number is already enabled");
			return;
		}
	}
	
	public boolean isExternalGreetingNumberAdded(WebDriver driver, String externalNumber){
		if(isElementVisible(driver, addedExternalGreetingNumbers, 2)) {
			List<WebElement> externalNumbers = getElements(driver, addedExternalGreetingNumbers);
			for (WebElement externalNumberElement : externalNumbers) {
				if(HelperFunctions.getNumberInSimpleFormat(externalNumberElement.getText()).equals(HelperFunctions.getNumberInSimpleFormat(externalNumber))){
					return true;
				}
			}
		}
		return false;
	}
	
	public void addExternalNumber(WebDriver driver, String externalNumber){
		enterText(driver, externalGreetingNoTextBox, externalNumber);
		clickElement(driver, addExternalGreetingNumberBtn);
	}
	
	public void disableExternalNumber(WebDriver driver){
		if(findElement(driver, extenalGreetingCheckBox).isSelected()){
			clickElement(driver, externalGreetingToggleOn);
			return;
		}else{
			System.out.println("External number is already disabled");
			return;
		}
	}
	
	public void deleteAllExternalNumbers(WebDriver driver){
		while(isElementVisible(driver, externalGreetingDelBtns, 0)){
			WebElement deleteBtn = findElement(driver, externalGreetingDelBtns);
			deleteBtn.click();
			waitUntilInvisible(driver, deleteBtn);
		}
	}
	
	public void verifyExtenalNumberToolTip(WebDriver driver){		
		hoverElement(driver, externalNumberToolTipImage);
		waitUntilTextPresent(driver, externalNumberToolTipText, "Allow calls from external number to update custom greeting.");
	}
	
	public void verifyOOOGreetingMessagePresent(WebDriver driver){
		waitUntilTextPresent(driver, warningMessage, "An Out of Office Greeting is currently Enabled.");
	}
	
	public void warningMessageInvisble(WebDriver driver){
		waitUntilInvisible(driver, warningMessage);
	}
	/******* Custom Greeting Section ends here *******/

	public void logoutSoftphone(WebDriver driver) {
		clickSettingIcon(driver);
		clickElement(driver, logoutButton);
		waitUntilVisible(driver, softPhoneLoginPage.salesforceLoginButton);
	}

	// Setting Call Forwarding Number Section
	public Boolean isCallForwardingNumberExist(WebDriver driver) {
		if (getWebelementIfExist(driver, callForwardingNumberLabel) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void enterCallForardingNumberSearch(WebDriver driver, String numToSearch){
		clearCallForardingNumberSearch(driver);
		enterText(driver, callForwardingNumberTextBox, numToSearch);
	}
	
	public void clearCallForardingNumberSearch(WebDriver driver){
		clickByJs(driver, callForwardingNumberTextBox);
		do {
			enterBackspace(driver, callForwardingNumberTextBox);
		}while(!getElementsText(driver, callForwardingNumberSelectize).trim().isEmpty());
	}

	public String getCallForwardingNumberInSimpleFormat(WebDriver driver) {
		return HelperFunctions.getNumberInSimpleFormat(getAttribue(driver, callForwardingNumberLabel, ElementAttributes.dataValue));
	}

	public List<WebElement> getCallForwardingNumbersFromDropdown(WebDriver driver) {
		return getElements(driver, callForwardingNumbersInDropDown);
	}

	public void deleteAllCallForwardingNumbers(WebDriver driver) {
		clickSettingIcon(driver);
		disableCallForwardingSettings(driver);
		if (isCallForwardingNumberExist(driver)) {
			clickElement(driver, callForwardingNumberSelectize);
			List<WebElement> deleteForwardingNumbers = getElements(driver, callForwardingNumberDeleteBtn);
			clickElement(driver, callForwardingNumberSelectize);
			for (int i = 0; i < deleteForwardingNumbers.size(); i++) {
				clickElement(driver, callForwardingNumberSelectize);
				if (!getElements(driver, callForwardingNumberDeleteBtn).get(0)
						.getAttribute(ElementAttributes.dataValue.displayName())
						.equals(HelperFunctions.getNumberInRDNAFormat(TestBase.CONFIG.getProperty("skype_number")))) {
					getElements(driver, callForwardingNumberDeleteBtn).get(0).click();
				}
			}
		}
	}
	
	public void deleteCallForwardingNumber(WebDriver driver, String frwrdingNumberToDelete) {
		clickSettingIcon(driver);
		disableCallForwardingSettings(driver);
		if (isCallForwardingNumberExist(driver)) {
			clickElement(driver, callForwardingNumberSelectize);
			List<WebElement> deleteForwardingNumbers = getElements(driver, callForwardingNumberDeleteBtn);
			for (int i = 0; i < deleteForwardingNumbers.size(); i++) {
				if (getElements(driver, callForwardingNumberDeleteBtn).get(i)
						.getAttribute(ElementAttributes.dataValue.displayName())
						.equals(HelperFunctions.getNumberInRDNAFormat(frwrdingNumberToDelete))) {
					getElements(driver, callForwardingNumberDeleteBtn).get(i).click();
				}
			}
		}
	}

	public void editCallForwardingNumbers(WebDriver driver, String fwdNumber) {
		clickSettingIcon(driver);
		disableCallForwardingSettings(driver);
		openCallForwardingDrpDwn(driver);
		By editButton = By.xpath(FwdNumberEditBtn.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
		clickElement(driver, editButton);
		waitUntilVisible(driver, newForwardingNumberTextBox);
	}
	
  	public void verifyToolTipAndBtnsSelectedFrwdnNum(WebDriver driver, String fwdNumber) {
  		By editButton	= By.xpath(FwdNumberEditBtn.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		By delButton	= By.xpath(FwdNumberDelBtn.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		By helpIcon		= By.xpath(FwdNumberHelpIcon.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		By toolTipText	= By.xpath(FwdNumberToolTipText.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		
  		scrollToElement(driver, buildInfo);
  		hoverElement(driver, helpIcon);
		waitUntilTextPresent(driver, toolTipText, "To delete or edit your call forwarding number, select a new number or turn off call forwarding.");
		waitUntilInvisible(driver, editButton);
		waitUntilInvisible(driver, delButton);
  	}
  	
  	public void verifyToolTipAndBtnsFwdNum(WebDriver driver, String fwdNumber) {
  		By editButton	= By.xpath(FwdNumberEditBtn.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		By delButton	= By.xpath(FwdNumberDelBtn.replace("$$Number$$", HelperFunctions.getNumberInRDNAFormat(fwdNumber)));
  		
		waitUntilVisible(driver, editButton);
		waitUntilVisible(driver, delButton);
  	}
	
	public String getForwardingNumberLabel(WebDriver driver){
		waitUntilVisible(driver, newForwardingNumberNameTxtBox);
		return getAttribue(driver, newForwardingNumberNameTxtBox, ElementAttributes.value);
	}
	
	public void validateBlankCallForwardingEnable(WebDriver driver) {
		clickSettingIcon(driver);
		enableCallForwardingSettings(driver);
		waitUntilVisible(driver, callForwardingValidationMsg);
		assertTrue(getElementsText(driver, callForwardingValidationMsg).trim().equals("Please enter a call forwarding number"));
		clickElement(driver, callForwardingCloseModalMsgBtn);

	}

	public void selectForwardingNumberAccToIndex(WebDriver driver, int index) {
		if (getWebelementIfExist(driver, callForwardingNumbersInDropDown) != null) {
			List<WebElement> existingForwardingNumbers = getCallForwardingNumbersFromDropdown(driver);
			existingForwardingNumbers.get(index).click();
		}
	}

	public boolean selectForwardingNameFromDropDown(WebDriver driver, String name) {
		if (getWebelementIfExist(driver, callForwardingNumbersInDropDown) != null) {
			List<WebElement> existingForwardingNumbers = getCallForwardingNumbersFromDropdown(driver);
			for (int i = 0; i < existingForwardingNumbers.size(); i++) {
				if (getElementsText(driver, existingForwardingNumbers.get(i)).contains(name)) {
					existingForwardingNumbers.get(i).click();
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public boolean selectForwardingNumberFromDropDown(WebDriver driver, String forwardingNumber) {
		if (getWebelementIfExist(driver, callForwardingNumbersInDropDown) != null) {
			List<WebElement> existingForwardingNumbers = getCallForwardingNumbersFromDropdown(driver);
			for (int i = 0; i < existingForwardingNumbers.size(); i++) {
				if (forwardingNumber.contains(HelperFunctions.getNumberInSimpleFormat(getAttribue(driver, existingForwardingNumbers.get(i), ElementAttributes.dataValue)))) {
					existingForwardingNumbers.get(i).click();
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public void openCallForwardingDrpDwn(WebDriver driver){
		scrollToElement(driver, noAnswerDropDownButton);
		clickElement(driver, callForwardingNumberSelectize);
	}
	
	public void cancelCallForwardingModal(WebDriver driver){
		waitUntilVisible(driver, cancelForwardingNumberButton);
		clickElement(driver, cancelForwardingNumberButton);
		waitUntilInvisible(driver, verifyForwardingNumberButton);
	}
	
	public void verifyAddFrwdNumBtnInvisible(WebDriver driver){
		waitUntilInvisible(driver, addNewForwardginNumberListItem);
	}
	
	public void enterForwardingNumberDetails(WebDriver driver, String fwdNumberLabel, String forwardingNumber){
		clickElement(driver, addNewForwardginNumberListItem);
		waitUntilVisible(driver, verifyForwardingNumberButton);
		enterText(driver, newForwardingNumberNameTxtBox, fwdNumberLabel);
		enterText(driver, newForwardingNumberTextBox, forwardingNumber);
		clickElement(driver, verifyForwardingNumberButton);
	}
	
	public void checkDoNotFrwrdWhenLogoutCheckBox(WebDriver driver) {
		waitUntilVisible(driver, doNotForwardWhenLogoutCheckBox);
		checkCheckBox(driver, doNotForwardWhenLogoutCheckBox);
		waitUntilVisible(driver, saveForwardingNumberButton);
		clickElement(driver, saveForwardingNumberButton);
	}

	public void unCheckDoNotFrwrdWhenLogoutCheckBox(WebDriver driver) {
		waitUntilVisible(driver, doNotForwardWhenLogoutCheckBox);
		unCheckCheckBox(driver, doNotForwardWhenLogoutCheckBox);
		waitUntilVisible(driver, saveForwardingNumberButton);
		clickElement(driver, saveForwardingNumberButton);
	}
	
	public void addNewForwardingNumber(WebDriver driver, WebDriver forwardingNumberDriver, String fwdNumberLabel, String forwardingNumber, boolean checkLogout) {
		enterForwardingNumberDetails(driver, fwdNumberLabel, forwardingNumber);
		assertTrue(getElementsText(driver, verifyingForwardingNotificationText).contains("Verifying ..."));
		softPhoneCalling.pickupIncomingCall(forwardingNumberDriver);
		softPhoneCalling.openDialPad(forwardingNumberDriver);
		softPhoneCalling.enterNumberInDialpad(forwardingNumberDriver, "1");
		softPhoneCalling.isCallBackButtonVisible(forwardingNumberDriver);
		softPhoneCalling.clickDialPadIcon(forwardingNumberDriver);
		findElement(driver, saveForwardingNumberButton);
		assertTrue(getElementsText(driver, verificationCompleteMessage).contains("Verification complete"));
		if(checkLogout){
			clickElement(driver, doNotForwardWhenLogoutCheckBox);
		}
		clickElement(driver, saveForwardingNumberButton);
	}
	
	public void addNewSipForwardingNumber(WebDriver driver, WebDriver sipDriver, String fwdNumberLabel, String forwardingNumber, boolean checkLogout) {
		enterForwardingNumberDetails(driver, fwdNumberLabel, forwardingNumber);
		assertTrue(getElementsText(driver, verifyingForwardingNotificationText).contains("Verifying ..."));
		sipCallingPage.clickDialPadNum1(sipDriver);
		findElement(driver, saveForwardingNumberButton);
		assertTrue(getElementsText(driver, verificationCompleteMessage).contains("Verification complete"));
		if(checkLogout){
			clickElement(driver, doNotForwardWhenLogoutCheckBox);
		}
		clickElement(driver, saveForwardingNumberButton);
	}
	
	public void verifyForwardingMessagPresent(WebDriver driver, String message){
		waitUntilTextPresent(driver, verifyingForwardingNotificationText, message);
		cancelCallForwardingModal(driver);
	}

	public void clickGeneralTab(WebDriver driver) {
		clickElement(driver, generalSettingLink);
	}

	public void setCallForwardingNumber(WebDriver driver, WebDriver forwardingNumberDriver, String fwdNumbLabel, String forwardingNumber) {
		clickSettingIcon(driver);
		if (isCallForwardingNumberExist(driver)&& forwardingNumber.contains(getCallForwardingNumberInSimpleFormat(driver))) {
			enableCallForwardingSettings(driver);
		} else {
			openCallForwardingDrpDwn(driver);
			if (!selectForwardingNumberFromDropDown(driver, forwardingNumber)) {
				if(forwardingNumber.startsWith("sip")) {
					addNewSipForwardingNumber(driver, forwardingNumberDriver, fwdNumbLabel, forwardingNumber, false);
				}else {
					addNewForwardingNumber(driver, forwardingNumberDriver, fwdNumbLabel, forwardingNumber, false);
				}
			}
		}
		disableStayConnectedSetting(driver);
	}
	
	public void setCallForwardingNumberDisableOnLogout(WebDriver driver, WebDriver forwardingNumberDriver, String fwdNumbLabel, String forwardingNumber) {
		clickSettingIcon(driver);
		clickElement(driver, callForwardingNumberSelectize);
		if (!selectForwardingNumberFromDropDown(driver, forwardingNumber)) {
			deleteAllCallForwardingNumbers(driver);
			openCallForwardingDrpDwn(driver);
		}
		addNewForwardingNumber(driver, forwardingNumberDriver, fwdNumbLabel, forwardingNumber, true);
	}

	public String getNoAnswerNumberinSimpleFormat(WebDriver driver) {
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, noAnswerNumberLoc));
	}

	public void setNoAnswerNumber(WebDriver driver, String noAnswerNumber) {
		clickSettingIcon(driver);
		if (isElementVisible(driver, noAnswerNumberLoc, 5) && noAnswerNumber.contains(getNoAnswerNumberinSimpleFormat(driver))) {
			enableNoAnswerSettings(driver);
		} else {
			if (isElementVisible(driver, noAnswerNumberLoc, 0)) {
				clickElement(driver, noAnswerDropDownButton);
				waitUntilVisible(driver, noAnswerDeleteButton);
				clickElement(driver, noAnswerDeleteButton);
			}
			enterNoAnswerNumber(driver, noAnswerNumber);
		}
	}
	
	public void enterNoAnswerNumber(WebDriver driver, String noAnswerNumber) {
		enterText(driver, noAnswerNumberTextBox, noAnswerNumber);
		clickEnter(driver, noAnswerNumberTextBox);
	}
	
	public void verifyAllToolTips(WebDriver driver){
		ArrayList<String> toolTipsTextList = new ArrayList<>();
		toolTipsTextList.add("Automatically dial upon clicking any phone number in Salesforce.");
		toolTipsTextList.add("Launches the Next Call button when clicking on phone numbers in Salesforce contact lists.");
		toolTipsTextList.add("Mute audio notifications when receiving incoming calls directly or in a queue.");
		toolTipsTextList.add("Receive desktop notifications on inbound calls, messages, and new hot leads.");
		toolTipsTextList.add("Automatic call recording in Salesforce.");
		toolTipsTextList.add("Enable Email to Salesforce to associate emails you send from ringDNA to records in Salesforce.");
		toolTipsTextList.add("Instantly route calls to any device or number.");
		toolTipsTextList.add("Ring this number when I receive a call");
		toolTipsTextList.add("Session Initiation Protocol should be turned off unless authorized by your supervisor.");
		toolTipsTextList.add("Number to connect callers to when you are unavailable or decline a call. Include commas in the number to pause for dialing an extension.");
		
		List<WebElement> toolTipImages = getElements(driver, toolTipsImagesLoc);
		int i=0;
		for (String toolTip : toolTipsTextList) {
			System.out.println(toolTip);
			hoverElement(driver, toolTipImages.get(i));
			waitUntilTextPresent(driver, toolTipText, toolTip);
			i++;
		}
	}

	public void setDefaultSoftPhoneSettings(WebDriver driver) {
	  clickSettingIcon(driver); 
	  disableLocalPresenceSetting(driver);
	  enableRecordCallsSetting(driver); 
	  disableCallForwardingSettings(driver);
	  disableSipSettings(driver); 
	  disableCallNotificationsSettings(driver);
	  disableNoAnswerSettings(driver);
	  clickOutboundNumbersTab(driver);
	  selectFirstAdditionalNumberAsDefault(driver); 
	  clickGeneralTab(driver);
		 
	}
	
	/**
	 * @param driver
	 * @Desc : enable call forward when intelligent dialer offline feature
	 */
	public void enableForwardWhenIntelligentDialerOffline(WebDriver driver) {
		openCallForwardingDrpDwn(driver);
		clickElement(driver, callForwardingUpdationButton);
		clickElement(driver, clickIntDialerForwarding);
		clickElement(driver, saveForwardingNumberButton);
	}
}
