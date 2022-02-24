/**
 * 
 */
package support.source.users;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.admin.AccountCallRecordingTab.CallRecordingOverrideOptions;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class UserIntelligentDialerTab extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	AccountIntelligentDialerTab accountDialer = new AccountIntelligentDialerTab();
		
	By overviewTabHeading						= By.xpath("//*[@id='main']//h1[contains(text(),'User')]");
	By intelligentDialerTabLink 				= By.cssSelector("[data-testid='usersettings-tab.dialer']");
	By generalSettingsHeader					= By.xpath("//*[@id='main']//h2[text()='General Settings']");
	By outBoundNumbersHeader					= By.xpath("//*[@id='main']//h2[text()='Outbound Numbers']");
	By sendEmailUsingSalesForceCheckbox			= By.cssSelector(".emailSendViaSalesforce");
	By sendEmailUsingSalesForceToggleButton		= By.xpath(".//input[contains(@class, 'emailSendViaSalesforce')]/parent::div//*[contains(@class,'toggle-off')]");
	By webLeadsCheckbox							= By.cssSelector(".webLeads.toggle-switch");
	By webLeadsToggleOffButton					= By.xpath("//input[contains(@class, 'webLeads')]/..//label[contains(@class,'toggle-off')]");
	By webLeadsToggleOnButton					= By.xpath("//input[contains(@class, 'webLeads')]/..//label[contains(@class,'toggle-on')]");
	By webLeadsLockText							= By.cssSelector(".webLeadsLockedBy span");
	By saveAccountsSettingButton				= By.cssSelector("button.save");
	By saveAccountsSettingMessage 				= By.className("toast-message");
	By unavailableCallFlowTextBox 				= By.cssSelector(".callFlowPicker input");
	By unavailableCallFlowDropDown 				= By.cssSelector(".callFlowPicker .selectize-control");
	By unavailableCallFlowSelectedItem			= By.cssSelector(".callFlowPicker .has-items .item");
	By unavailableCallFlowSelectBox				= By.cssSelector(".callFlowPicker select");
	By unavailableCallFlowLockText				= By.cssSelector(".unavailableFlowLockedBy span");
	By searchedFlowInDropDown					= By.cssSelector(".selectize-dropdown-content .option.active");
	By webRTCLabel								= By.xpath(".//*[text()='WebRTC']");
	By callForwardingCheckBox					= By.cssSelector(".callForwarding.toggle-switch");
	By callForwardingToggleOffBtn				= By.xpath("//input[contains(@class, 'callForwarding toggle-switch')]/..//label[contains(@class,'toggle-off')]");
	By callForwardingToggleOnBtn				= By.xpath("//input[contains(@class, 'callForwarding toggle-switch')]/..//label[contains(@class,'toggle-on')]");
	By callForwardingPromptCheckBox				= By.cssSelector(".callForwardingPrompt.toggle-switch");
	By callForwardingPromptToggleOffBtn			= By.xpath("//input[@class='callForwardingPrompt toggle-switch']/..//label[contains(@class,'toggle-off')]");
	By callForwardingPromptToggleOnBtn			= By.xpath("//input[@class='callForwardingPrompt toggle-switch']/..//label[contains(@class,'toggle-on')]");
	By callForwardingPromptLockText				= By.cssSelector(".callForwardingPromptLockedBy span");
	By callForwardingInputBox					= By.cssSelector(".callForwardingNumber");
	By callForwardingSmartNoCheckBox			= By.cssSelector(".callForwardingUseSmartNumber");
	By callForwardingSmartNoToggleOffBtn		= By.xpath("//input[contains(@class, 'callForwardingUseSmartNumber')]/..//label[contains(@class,'toggle-off')]");
	By callForwardingSmartNoToggleOnBtn			= By.xpath("//input[contains(@class, 'callForwardingUseSmartNumber')]/..//label[contains(@class,'toggle-on')]");
	By callForwardingLockText					= By.cssSelector(".callForwardingLockedBy span");
	By disableOfflineForwardingCheckBox			= By.cssSelector(".callForwardingDisabledOffline.toggle-switch");
	By disableOfflineForwardingToggleOffBtn		= By.xpath("//input[contains(@class, 'callForwardingDisabledOffline toggle-switch')]/..//label[contains(@class,'toggle-off')]");
	By disableOfflineForwardingToggleOnBtn		= By.xpath("//input[contains(@class, 'callForwardingDisabledOffline toggle-switch')]/..//label[contains(@class,'toggle-off')]");
	
	By disableOfflineForwardingLockText			= By.cssSelector(".callForwardingDisabledOfflineLockedBy span");
	
	// Custom Greetings selectors
	By addCustomGreetingIcon 					= By.cssSelector(".voicemail-greeting .glyphicon-plus-sign");
	By newRecordingLink 						= By.className("record-voicemail");
	By inputLabelTab 							= By.cssSelector(".voicemail-modals-container .file-label");
	By chooseFolderLink 						= By.cssSelector(".voicemail-modals-container .glyphicon-folder-open");
	By uploadFilePlayIcon 						= By.cssSelector(".voicemail-modals-container .glyphicon-play-circle");
	By outOfOfficeCheckbx 						= By.cssSelector(".out-of-office");
	By saveCustomGreetingRecord 				= By.cssSelector(".voicemail-modals-container  button.save-record");
	By saveBtn 									= By.cssSelector(".page-title .btn-success");
	String greetingOOOLabel						= ".//*[@class='voicemail-greeting']//td[text() ='$$GreetingName$$']/following-sibling::td[contains(@class,'outOfOffice')]";
	
	String updateCustomGreeting  				= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//button[@class='btn btn-primary update-voicemail']";
	
	// OutBound Number Selectors
	By addOutboundNumberIcon					= By.cssSelector(".outbound-numbers .glyphicon-plus-sign");
	By inputPhoneNumberTab						= By.cssSelector(".outbound-numbers .phone-number");
	By inputLabelNameTab						= By.cssSelector(".outbound-numbers .phone-label");
	By outBoundAddBtn							= By.cssSelector(".outbound-numbers .btn-success.add");
	
	String outBoundCheckBox						= ".//*[text()=\"$$RecordName$$\"]/ancestor::tr//input[@type='checkbox']";
	String outBoundNumberDelete					= ".//*[contains(text(),'$number$')]/ancestor::tr//a/span[contains(@class,'remove-sign')]";
	
	//call recording Setting
	By callRecordLockedMessage			 		= By.cssSelector(".callRecordingLockedBy");
	By callRecordOverrideDropDownDisabled 		= By.cssSelector(".callRecordingOption:disabled");
	By callRecordingOverrideOptionsDropDown		= By.cssSelector(".callRecordingOption");
	
	//voicemail drops
	By orderDownVoicemailDrops					= By.cssSelector(".glyphicon.glyphicon-menu-down");
	By voicemailNameList						= By.cssSelector(".voicemail-drops td.name");
	
	//user overviewtab settings > user ID settings
	//smart number selectors
	By addSmartNoIcon				= By.cssSelector(".add-smart-number .glyphicon-plus-sign");
	By defaultNumber				= By.xpath(".//*[text()='Default']/ancestor::tr//td[contains(@class,'is-deleted') and text()='false']/ancestor::tr[not(contains(@class,'hidden'))]//a[contains(@href,'smart-numbers')]");
	By additionalNumberList			= By.xpath(".//*[text()='Additional']/ancestor::tr//td[contains(@class,'is-deleted') and text()='false']/ancestor::tr[not(contains(@class,'hidden'))]//a[contains(@href,'smart-numbers')]");
	By confirmDeleteBtn 			= By.cssSelector("[data-bb-handler='confirm']");
	By confirmDeleteDangerBtn		= By.cssSelector(".btn-danger.confirm-delete");
	By deleteTeamIcon				= By.cssSelector(".userTeam .glyphicon-remove-sign");
		
	String defaultSmartNoPage       = ".//*[text()='Default']/ancestor::tr//a[contains(text(),'$$smartNumber$$')]";
	String additionalSmartNoPage    = ".//*[text()='Additional']/ancestor::tr/td/a[contains(text(),'$$smartNumber$$')]";
	String deleteSmartNoPage		= ".//*[text()='$$smartNumber$$']/ancestor::tr/td/a[contains(@class,'remove-smart-number')]";
	String isDeletedNoPage			= ".//*[text()='$$smartNumber$$']/ancestor::tr/td[@class='is-deleted']";
		
	//call queue selectors
	By addCallQueueIcon				= By.cssSelector(".userCallQueue .glyphicon-plus-sign");
	By userToCallQueueInput			= By.cssSelector(".call-queues-picker input");
	By userToHeader            		= By.cssSelector("h4.modal-title");
	By userToCallQueueDropDown		= By.cssSelector(".call-queue-picker .selectize-dropdown-content .option");
	By saveUserCallQueueBtn			= By.cssSelector(".btn-success.save-user-call-queues");
	By saveUserPageBtn 				= By.cssSelector("button.save");
	By callQueuePickerItems			= By.cssSelector(".call-queues-picker .has-items .item");
	By queueNameList				= By.xpath(".//*[@class='userCallQueue']//td//a[contains(@href,'#call-queues')]");
	By addedUserToCallQueues		= By.cssSelector(".call-queues-picker .items .item");
	String callQueueName			= ".//*[text()='$callQueue$']/ancestor::tr//td//a[contains(@href,'#call-queues')]";
	String callQueueDeletePage		= ".//*[text()='$callQueue$']/ancestor::tr//a[@class='delete']/span";
	String callQueueSubscribed		= ".//*[text()='$callQueue$']/ancestor::tr//td[3][text()='Yes']";
	String userToQueueDelete        = ".//*[@class='call-queue-picker']//div[contains(@class,'has-items')]/div[text()='$$queueName$$']/a[@class='remove']";
	
	//Call processing
	By inOnCallValue				= By.xpath(".//*[text()='Is On Call?']/following-sibling::span");
	By liveCalls					= By.xpath(".//*[text()='Live Calls']/following-sibling::span");
	By bridgeReference				= By.xpath(".//*[text()='Bridge Reference']/following-sibling::span");
	By lastBridgeCall				= By.xpath(".//*[text()='Last Bridge Call']/following-sibling::span");
	By continuosBridge				= By.xpath(".//*[text()='Continuous Bridge?']/following-sibling::span");
	By removeLivecallBtn			= By.xpath(".//*[text()='Live Calls']/following-sibling::span//a//span[contains(@class,'glyphicon-remove-sign')]");
	By removeBridgeRefBtn			= By.xpath(".//*[text()='Bridge Reference']/following-sibling::span//a//span[contains(@class,'glyphicon-remove-sign')]");
	By missedCallCount				= By.xpath(".//label[text()='Missed Call Count']/following-sibling::span");
	By accountMsgTitle				= By.className("toast-title");
	
	//yoda ai
	By yodaAiNotificationCheckBox   = By.cssSelector(".realtimeCallAlertsEnabled.toggle-switch");
	By yodaAiNotificationToggleOff	= By.xpath(".//input[@class= 'realtimeCallAlertsEnabled toggle-switch']/parent::div//*[contains(@class,'toggle-off')]");
	By yodaAiNotificationToggleOn	= By.xpath(".//input[@class= 'realtimeCallAlertsEnabled toggle-switch']/parent::div//*[contains(@class,'toggle-on')]");
	
	//headers
	By callQueuesHeader             = By.xpath("//h2[text() = 'Call Queues']");
	By callProcessingHeader         = By.xpath("//h2[text() = 'Call Processing']");
	By smartNumbersHeader           = By.xpath("//h2[text() = 'Smart Numbers']");
	
	public boolean isOverviewTabHeadingPresent(WebDriver driver){
		waitUntilInvisible(driver, dashboard.paceBar);
		return findElement(driver, overviewTabHeading).isDisplayed();
	}
	
	public void openIntelligentDialerTab(WebDriver driver){
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, intelligentDialerTabLink);
		clickByJs(driver, intelligentDialerTabLink);
		dashboard.isPaceBarInvisible(driver);
		findElement(driver, generalSettingsHeader);
	}
	
	public void enableSendEmailUsingSalesforce(WebDriver driver){
		scrollToElement(driver, sendEmailUsingSalesForceToggleButton);
		if(!findElement(driver, sendEmailUsingSalesForceCheckbox).isSelected()) {
			System.out.println("Checking Send Email Using Salesforce Setting checkbox");
			clickElement(driver, sendEmailUsingSalesForceToggleButton);			
		}else {
			System.out.println("Send Email Using Salesforce Setting already enable");
		} 
	}

	public void enableWebLeadsSetting(WebDriver driver){
		if(!findElement(driver, webLeadsCheckbox).isSelected()) {
			clickElement(driver, webLeadsToggleOffButton);
			System.out.println("enables web lead setting");
		} else {
			System.out.println("Web lead setting is already enabled");
		}
	}
	
	public void disableWebLeadsSetting(WebDriver driver){
		if(findElement(driver, webLeadsCheckbox).isSelected()) {
			clickElement(driver, webLeadsToggleOnButton);
			System.out.println("disables web lead setting");
		} else {
			System.out.println("Web lead setting is already disabled");
		}
	}
	
	public boolean isCallForwardingToggleBtnEditable(WebDriver driver){
		return !isAttributePresent(driver, callForwardingCheckBox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isDisableOfflineForwardingToggleBtnEditable(WebDriver driver){
		return !isAttributePresent(driver, disableOfflineForwardingCheckBox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isDisableOfflineForwardingToggleBtnOff(WebDriver driver){
		return findElement(driver, disableOfflineForwardingCheckBox).findElement(By.xpath("..")).getAttribute("class").contains("off");
	}
	
	public boolean isCallForwardingPromptToggleBtnEditable(WebDriver driver){
		return !isAttributePresent(driver, callForwardingPromptCheckBox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isCallForwardingPromptToggleBtnOff(WebDriver driver){
		return findElement(driver, callForwardingPromptCheckBox).findElement(By.xpath("..")).getAttribute("class").contains("off");
	}
	
	public boolean isWebLeadsToggleBtnEditable(WebDriver driver){
		return !isAttributePresent(driver, webLeadsCheckbox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isWebLeadsToggleBtnOff(WebDriver driver){
		return findElement(driver, webLeadsCheckbox).findElement(By.xpath("..")).getAttribute("class").contains("off");
	}
	
	public boolean isWebLeadsSectionVisible(WebDriver driver){
		return isElementVisible(driver, webLeadsCheckbox, 5);
	}
	
	public void verifyCallForwardingPromptLockedText(WebDriver driver){
		waitUntilVisible(driver, callForwardingPromptLockText);
		assertEquals(getElementsText(driver, callForwardingPromptLockText), "Locked by Account Admin");
	}
	
	public void verifyCallForwardingLockedText(WebDriver driver){
		waitUntilVisible(driver, callForwardingLockText);
		assertEquals(getElementsText(driver, callForwardingLockText), "Locked by Account Admin");
	}
	
	public void verifyDisableOfflineForwardingLockedText(WebDriver driver){
		waitUntilVisible(driver, disableOfflineForwardingLockText);
		assertEquals(getElementsText(driver, disableOfflineForwardingLockText), "Locked by Account Admin");
	}
	
	public void verifyWebLeadsLockedText(WebDriver driver){
		waitUntilVisible(driver, webLeadsLockText);
		assertEquals(getElementsText(driver, webLeadsLockText), "Locked by Account Admin");
	}
	
	public void disableCallForwarding(WebDriver driver) {
		System.out.println("Setting call forwarding to OFF");
		if (findElement(driver, callForwardingCheckBox).isSelected()) {
			clickElement(driver, callForwardingToggleOnBtn);
		} else {
			System.out.println("Call Forwarding is already OFF");
		}
		dashboard.isPaceBarInvisible(driver);
	}

	public void enableCallForwarding(WebDriver driver) {
		System.out.println("Setting call forwarding to ON");
		if (!findElement(driver, callForwardingCheckBox).isSelected()) {
			clickElement(driver, callForwardingToggleOffBtn);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding is already ON");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isCallForwardingEnabled(WebDriver driver) {
		return (findElement(driver, callForwardingCheckBox).isSelected());
	}
	
	/**
	 * @param driver
	 * @return boolean
	 */
	public boolean isDisableCallForwardingEnabled(WebDriver driver) {
		return (findElement(driver, disableOfflineForwardingCheckBox).isSelected());
	}
	
	public void disableCallForwardingPrompt(WebDriver driver) {
		System.out.println("Setting call forwarding prompt to OFF");
		if (findElement(driver, callForwardingPromptCheckBox).isSelected()) {
			clickElement(driver, callForwardingPromptToggleOnBtn);
		} else {
			System.out.println("Call Forwarding Prompt is already OFF");
		}
	}

	public void enableCallForwardingPrompt(WebDriver driver) {
		System.out.println("Setting call forwarding to OFF");
		if (findElement(driver, callForwardingPromptCheckBox).isSelected()) {
			clickElement(driver, callForwardingPromptToggleOffBtn);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding Prompt is already ON");
		}
	}

	public void enableOfflineForwardingDisableSetting(WebDriver driver) {
		System.out.println("Setting offline forwarding disable setting to ON");
		if (!findElement(driver, disableOfflineForwardingCheckBox).isSelected()) {
			clickElement(driver, disableOfflineForwardingToggleOffBtn);
		} else {
			System.out.println("Disable offline forwarding is already enabled");
		}
	}
	
	public void disableOfflineForwardingDisableSetting(WebDriver driver) {
		System.out.println("Setting offline forwarding disable setting to OFF");
		if (findElement(driver, disableOfflineForwardingCheckBox).isSelected()) {
			clickElement(driver, disableOfflineForwardingToggleOnBtn);
		} else {
			System.out.println("Disable offline forwarding is already disabled");
		}
	}
	
	public void enableCallForwardingUsingSmartNumber(WebDriver driver) {
		System.out.println("Setting call forwarding to ON");
		if (!findElement(driver, callForwardingSmartNoCheckBox).isSelected()) {
			clickElement(driver, callForwardingSmartNoToggleOffBtn);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding Using smart number is already ON");
		}
	}
	
	public void disableCallForwardingUsingSmartNumber(WebDriver driver) {
		System.out.println("Setting call forwarding to ON");
		if (!findElement(driver, callForwardingSmartNoCheckBox).isSelected()) {
			clickElement(driver, callForwardingToggleOnBtn);
			idleWait(2);
		} else {
			System.out.println("Call Forwarding Using Smart Number is already OFF");
		}
	}
	
	public boolean isCallForwardingNumberBoxDisabled(WebDriver driver){
		return isAttributePresent(driver, callForwardingInputBox, ElementAttributes.disabled.displayName());
	}
	
	public boolean isCallForwardingNumberBoxEmpty(WebDriver driver){
		return getAttribue(driver, callForwardingInputBox, ElementAttributes.value).isEmpty();
	}
	
	public boolean isCallForwardingUsingSmartNumberDisabled(WebDriver driver){
		return isAttributePresent(driver, callForwardingSmartNoCheckBox, ElementAttributes.disabled.displayName());
	}
	
	public void enterCallForwardingNumber(WebDriver driver, String number){
		waitUntilVisible(driver, callForwardingInputBox);
		enterText(driver, callForwardingInputBox, number);
	}
	
	/**
	 * @param driver
	 * @param number
	 * @return call forwarding number
	 */
	public String getCallForwardingNumber(WebDriver driver){
		waitUntilVisible(driver, callForwardingInputBox);
		return getAttribue(driver, callForwardingInputBox, ElementAttributes.value);
	}
	
	public void selectUnavailableCallFlow(WebDriver driver, String callFlow){
		if(isElementVisible(driver, webRTCLabel, 5)){
			scrollToElement(driver, webRTCLabel);
		}
		else{
			scrollToElement(driver, outBoundNumbersHeader);
		}
		clickElement(driver, unavailableCallFlowDropDown);
		enterBackspace(driver, unavailableCallFlowTextBox);
		enterText(driver, unavailableCallFlowTextBox, callFlow);
		idleWait(2);
		waitUntilVisible(driver, searchedFlowInDropDown);
		clickElement(driver, searchedFlowInDropDown);
	}
	
	public void selectNoUnvailableFlow(WebDriver driver){
		clickElement(driver, unavailableCallFlowDropDown);
		enterBackspace(driver, unavailableCallFlowTextBox);
	}
	
	public String getUnavailableCallFlowSelectedFlow(WebDriver driver){
		waitUntilVisible(driver, unavailableCallFlowSelectedItem);
		return getElementsText(driver, unavailableCallFlowSelectedItem);
	}
	
	public boolean isUnavailableCallFlowEditable(WebDriver driver){
		return !isAttributePresent(driver, unavailableCallFlowSelectBox, ElementAttributes.disabled.displayName());
	}
	
	public void verifyUnavailableCallFlowLockedText(WebDriver driver){
		waitUntilVisible(driver, unavailableCallFlowLockText);
		assertEquals(getElementsText(driver, unavailableCallFlowLockText), "Locked by Account Admin");
	}
	
	public void isSaveAccountsSettingMessageDisappeared(WebDriver driver){
		isElementVisible(driver, saveAccountsSettingMessage, 1);
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void saveAcccountSettings(WebDriver driver){
		scrollToElement(driver, saveAccountsSettingButton);
		clickElement(driver, saveAccountsSettingButton);
		isSaveAccountsSettingMessageDisappeared(driver);		
	}
	
	/*******Custom Greeting section starts here*******/

	public void clickAddCustomGreetingBtn(WebDriver driver) {
		waitUntilVisible(driver, addCustomGreetingIcon);
		clickElement(driver, addCustomGreetingIcon);
	}

	public void clickRecordCustomGreetingLink(WebDriver driver) {
		waitUntilVisible(driver, newRecordingLink);
		clickElement(driver, newRecordingLink);
		waitUntilVisible(driver, inputLabelTab);
	}
	
	public void enterCustomGreetingLabelName(WebDriver driver, String customGreetingName) {
		waitUntilVisible(driver, inputLabelTab);
		enterText(driver, inputLabelTab, customGreetingName);
	}

	public void createCustomGreetingByRecording(WebDriver driver, String customGreetingName, int duration) {
		scrollToElement(driver, addCustomGreetingIcon);
		clickAddCustomGreetingBtn(driver);
		accountDialer.clickRecordVoicemailLink(driver);
		accountDialer.enterVoiceMailName(driver, customGreetingName);
		accountDialer.clickVoiceMailRecordButton(driver);
		idleWait(duration);
		accountDialer.clickVoiceMailStopButton(driver);
		checkCheckBox(driver, outOfOfficeCheckbx);
		clickSaveCustomGreetingButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettingsDialer(driver);
	}  	
	
	public void createCustomGreeting(WebDriver driver, String customGreetingName) {
		idleWait(1);
		scrollToElement(driver, addCustomGreetingIcon);
		clickAddCustomGreetingBtn(driver);
		enterCustomGreetingLabelName(driver, customGreetingName);
		accountDialer.clickChooseFileLink(driver);
		HelperFunctions.uploadWavFileRecord();
		waitUntilVisible(driver, uploadFilePlayIcon);
		checkCheckBox(driver, outOfOfficeCheckbx);
		clickSaveCustomGreetingButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettingsDialer(driver);
	}

	public void saveAcccountSettingsDialer(WebDriver driver){
		scrollToElement(driver, saveBtn);
		clickElement(driver, saveBtn);
	}
	
	public String getSaveAccountsSettingMessage(WebDriver driver){
		return getElementsText(driver, saveAccountsSettingMessage);
	}
	
	public void clickSaveCustomGreetingButton(WebDriver driver) {
		waitUntilVisible(driver, saveCustomGreetingRecord);
		clickElement(driver, saveCustomGreetingRecord);
	}
	
	public String getGreetingOOOLabelText(WebDriver driver, String greetingName){
		By oooLabelLoc = By.xpath(greetingOOOLabel.replace("$$GreetingName$$", greetingName));
		waitUntilVisible(driver, oooLabelLoc);
		return getElementsText(driver, oooLabelLoc);
	}
	
	public void updateCustomGreetingRecordsByUploadingFile(WebDriver driver, String oldcustomGreetingName, String newcustomGreetingName) {
		By updateRecordLoc = By.xpath(updateCustomGreeting.replace("$$RecordName$$", oldcustomGreetingName));
		waitUntilVisible(driver, updateRecordLoc);
		clickElement(driver, updateRecordLoc);
		enterCustomGreetingLabelName(driver, newcustomGreetingName);
		accountDialer.clickChooseFileLink(driver);
		HelperFunctions.uploadWavFileRecord();
		waitUntilVisible(driver, uploadFilePlayIcon);
		clickSaveCustomGreetingButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettingsDialer(driver);
	}
	
	public boolean isCustomGreetingVisible(WebDriver driver, String greetingName) {
		By updateRecordLoc = By.xpath(updateCustomGreeting.replace("$$RecordName$$", greetingName));
		return isElementVisible(driver, updateRecordLoc, 5);
	}
	
	/*******Custom Greeting section ends here*******/
	
	/*******OutBound Number section starts here*******/
	
	public void createOutBoundNumber(WebDriver driver, String phoneNumber, String labelName) {
		scrollToElement(driver, addOutboundNumberIcon);
		clickAddOutBoundNumberBtn(driver);
		enterPhoneNumber(driver, phoneNumber);
		enterLabelName(driver, labelName);
		clickSaveOutBoundButton(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
		saveAcccountSettingsDialer(driver);
	}

	public void clickAddOutBoundNumberBtn(WebDriver driver) {
		waitUntilVisible(driver, addOutboundNumberIcon);
		clickElement(driver, addOutboundNumberIcon);
	}
	public void clickSaveOutBoundButton(WebDriver driver) {
		waitUntilVisible(driver, outBoundAddBtn);
		clickElement(driver, outBoundAddBtn);
	}

	public void enterLabelName(WebDriver driver, String labelName) {
		waitUntilVisible(driver, inputLabelNameTab);
		enterText(driver, inputLabelNameTab, labelName);
	}

	public void enterPhoneNumber(WebDriver driver, String phoneNumber) {
		waitUntilVisible(driver, inputPhoneNumberTab);
		enterText(driver, inputPhoneNumberTab, phoneNumber);
	}
	
	public void selectOutBoundCheckBoxAccToLabel(WebDriver driver, String labelName) {
		By checkBoxLoc = By.xpath(outBoundCheckBox.replace("$$RecordName$$", labelName));
		checkCheckBox(driver, checkBoxLoc);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		isSaveAccountsSettingMessageDisappeared(driver);
	}
	
	public void deleteOutBoundNumber(WebDriver driver, String number){
		By outboundDeleteLoc = By.xpath(outBoundNumberDelete.replace("$number$", number));
		if(isElementVisible(driver, outboundDeleteLoc, 5)){
			clickElement(driver, outboundDeleteLoc);
			accountDialer.confirmDeleteAction(driver);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/*******OutBound Number section ends here*******/
	
	/*******Call Recording section Starts here*******/
	public enum LockedByOptions{
		Admin,
		Team;
	}
	
	public void verifyCallRecordOverrideDisabled(WebDriver driver, LockedByOptions lockedBy, String lockByTeam) {
		String lockMessge = null;
		if(lockedBy.equals(LockedByOptions.Admin)){
			 lockMessge = "Locked by Account Admin";
		}else {
			lockMessge = "Locked by Team: " + lockByTeam;
		}
		assertEquals(getElementsText(driver, callRecordLockedMessage), lockMessge);
		waitUntilVisible(driver, callRecordOverrideDropDownDisabled);
	}
	
	public void verifyCallRecordOverrideDisabledByTeam(WebDriver driver) {
		waitUntilVisible(driver, callRecordLockedMessage);
		waitUntilVisible(driver, callRecordOverrideDropDownDisabled);
	}
	
	public void verifyCallRecordOverrideEnabled(WebDriver driver) {
		waitUntilVisible(driver, callRecordingOverrideOptionsDropDown);
		waitUntilInvisible(driver, callRecordLockedMessage);
	}
	
	public void verifyRecordingOverrideValue(WebDriver driver, CallRecordingOverrideOptions expectedOverrideValue) {
		waitUntilVisible(driver, callRecordingOverrideOptionsDropDown);
		String text = getAttribue(driver, callRecordingOverrideOptionsDropDown, ElementAttributes.value);
		assertEquals(text, expectedOverrideValue.toString());
	}
	
	/**
	 * @param driver
	 * @param expectedOverrideValue
	 * select call recording value from drop down
	 */
	public void selectCallRecordingValue(WebDriver driver, CallRecordingOverrideOptions expectedOverrideValue) {
		selectFromDropdown(driver, callRecordingOverrideOptionsDropDown, SelectTypes.value, expectedOverrideValue.toString());
	}
	/*******Call Recording section ends here*******/
	
	/*******Voicemail drop section Starts here*******/
	
	public List<String> orderDownVoiceMailDrop(WebDriver driver){
		waitUntilVisible(driver, orderDownVoicemailDrops);
		clickElement(driver, getElements(driver, orderDownVoicemailDrops).get(0));
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		return getTextListFromElements(driver, voicemailNameList);
	}
	
	/*******Voicemail drop section ends here*******/
	
	/****User overview tab settings >> user ID settings*****/
	
	public boolean isUserOnCall(WebDriver driver){
		if(getElementsText(driver, inOnCallValue).equals("true")){
			return true;
		}else if(getElementsText(driver, inOnCallValue).equals("false")){
			return false;
		}else{
			Assert.fail();
			return false;
		}
	}
	
	public void verifyCallProcessingDetailsDuringLiveCall(WebDriver driver) {
		assertTrue(isUserOnCall(driver));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, bridgeReference)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, liveCalls)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, lastBridgeCall)));
	}
	
	public void verifyRemoveLiveCallMsgDuringActiveCall(WebDriver driver) {
		waitUntilVisible(driver, removeLivecallBtn);
		clickElement(driver, removeLivecallBtn);
		clickConfirmDeleteBtn(driver);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "The ActiveCall is still in progress.");
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void verifyRemoveBridgeRefMsgDuringActiveCall(WebDriver driver) {
		waitUntilVisible(driver, removeBridgeRefBtn);
		clickElement(driver, removeBridgeRefBtn);
		clickConfirmDeleteBtn(driver);
		assertEquals(getElementsText(driver, accountMsgTitle), "Job Started");
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "Refresh this page in a minute or two.");
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void verifyRemoveLiveCallMsgAfterActiveCall(WebDriver driver) {
		waitUntilVisible(driver, removeLivecallBtn);
		clickElement(driver, removeLivecallBtn);
		clickConfirmDeleteBtn(driver);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "The ActiveCall is no longer live.");
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}
	
	public void verifyCallProcessingDetailsBlank(WebDriver driver) {
		assertFalse(isUserOnCall(driver));
		assertTrue(Strings.isNullOrEmpty(getElementsText(driver, bridgeReference)));
		assertTrue(Strings.isNullOrEmpty(getElementsText(driver, liveCalls)));
		assertTrue(Strings.isNullOrEmpty(getElementsText(driver, lastBridgeCall)));
	}

	public String getMissedCallCount(WebDriver driver){
		waitUntilVisible(driver, missedCallCount);
		scrollIntoView(driver, missedCallCount);
		return getElementsText(driver, missedCallCount);
	}
	
	//Call Queues section
	
	public boolean isAddCallQueueIconVisible(WebDriver driver) {
		return isElementVisible(driver, addCallQueueIcon, 2);
	}

	public void clickAddCallQueueIcon(WebDriver driver) {
		waitUntilVisible(driver, addCallQueueIcon);
		clickElement(driver, addCallQueueIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, userToCallQueueInput);
	}

	public boolean isCallQueuePresent(WebDriver driver, String callQueue) {
		By callQueueLoc = By.xpath(callQueueName.replace("$callQueue$", callQueue));
		return isElementVisible(driver, callQueueLoc, 5);
	}

	public List<String> getQueueMemberList(WebDriver driver) {
		return getTextListFromElements(driver, queueNameList);
	}

	public void selectUserFromCallQueue(WebDriver driver, String callQueueName) {
		clickAndSelectFromDropDown(driver, userToCallQueueInput, userToCallQueueDropDown, callQueueName);
		waitUntilVisible(driver, userToHeader);
		clickElement(driver, userToHeader);
	}

	public void clickSaveUserQueueBtn(WebDriver driver) {
		waitUntilVisible(driver, saveUserCallQueueBtn);
		clickElement(driver, saveUserCallQueueBtn);
		waitUntilInvisible(driver, saveUserCallQueueBtn);
		dashboard.isPaceBarInvisible(driver);
	}

	public void addUserToCallQueue(WebDriver driver, String callQueueName) {
		selectUserFromCallQueue(driver, callQueueName);
		clickSaveUserQueueBtn(driver);
	}

	public void clickCallQueueLink(WebDriver driver, String callQueue) {
		By callQueueLoc = By.xpath(callQueueName.replace("$callQueue$", callQueue));
		waitUntilVisible(driver, callQueueLoc);
		clickElement(driver, callQueueLoc);
		dashboard.isPaceBarInvisible(driver);
	}

	public void deleteCallQueue(WebDriver driver, String callQueue) {
		idleWait(1);
		By callQueueLoc = By.xpath(callQueueDeletePage.replace("$callQueue$", callQueue));
		waitUntilVisible(driver, callQueueLoc);
		scrollIntoView(driver, callQueueLoc);
		clickElement(driver, callQueueLoc);
		clickConfirmDeleteBtn(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public boolean isCallQueueSubscribed(WebDriver driver, String callQueue) {
		By queueSubscribedLoc = By.xpath(callQueueSubscribed.replace("$callQueue$", callQueue));
		if (isElementVisible(driver, queueSubscribedLoc, 5)) {
			return true;
		}
		return false;
	}

	public boolean isCallQueueSavedInAddUserToCallQueues(WebDriver driver, String expectedCallQueue) {
		clickAddCallQueueIcon(driver);
		for (String actualCallQueue : getTextListFromElements(driver, addedUserToCallQueues)) {
			if (actualCallQueue.contains(expectedCallQueue)) {
				return true;
			}
		}
		return false;
	}

	public void deleteUserToCallQueue(WebDriver driver, String queueName) {
		By userToQueueLoc = By.xpath(userToQueueDelete.replace("$$queueName$$", queueName));
		waitUntilVisible(driver, userToQueueLoc);
		clickElement(driver, userToQueueLoc);
		assertFalse(isElementVisible(driver, userToQueueLoc, 5));
		clickElement(driver, userToHeader);
		waitUntilVisible(driver, saveUserCallQueueBtn);
		clickElement(driver, saveUserCallQueueBtn);
		waitUntilInvisible(driver, saveUserCallQueueBtn);
	}

	public void savedetails(WebDriver driver) {
		waitUntilVisible(driver, saveUserPageBtn);
		clickElement(driver, saveUserPageBtn);
	}

	public void deleteAllQueues(WebDriver driver) {
		if (isListElementsVisible(driver, deleteTeamIcon, 5)) {
			int i = getElements(driver, deleteTeamIcon).size() - 1;
			while (i >= 0) {
				// delete field
				clickElement(driver, getElements(driver, deleteTeamIcon).get(i));
				clickConfirmDeleteBtn(driver);
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				i--;
			}
			savedetails(driver);
		}
	}

	// smart number section

	public boolean isAddSmartNoIconVisible(WebDriver driver) {
		return isElementVisible(driver, addSmartNoIcon, 2);
	}

	public void clickSmartNoIcon(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addSmartNoIcon);
		clickElement(driver, addSmartNoIcon);
	}

	public String getDefaultNo(WebDriver driver) {
		scrollToElement(driver, defaultNumber);
		waitUntilVisible(driver, defaultNumber);
		return getElementsText(driver, defaultNumber);
	}

	public boolean isDefaultSmartNumberPresent(WebDriver driver) {
		return isElementVisible(driver, defaultNumber, 5);
	}

	public void verifyDefaultSmartNoPage(WebDriver driver, String defaultSmartNo) {
		By defaultSmartLoc = By.xpath(defaultSmartNoPage.replace("$$smartNumber$$", defaultSmartNo));
		assertTrue(isElementVisible(driver, defaultSmartLoc, 7));
	}

	public void clickDefaultSmartNo(WebDriver driver) {
		waitUntilVisible(driver, defaultNumber);
		clickElement(driver, defaultNumber);
	}

	public List<String> getAdditionalNumberList(WebDriver driver) {
		return getTextListFromElements(driver, additionalNumberList);
	}

	public void clickAdditionalSmartNo(WebDriver driver, int index) {
		getElements(driver, additionalNumberList).get(0).click();
	}

	public void verifyAdditionalTypePage(WebDriver driver, String additionalTypeNo) {
		By additionalSmartLoc = By.xpath(additionalSmartNoPage.replace("$$smartNumber$$", additionalTypeNo));
		assertTrue(isElementVisible(driver, additionalSmartLoc, 7));
	}

	public void deleteSmartNumber(WebDriver driver, String smartNumber) {
		By smartNoDeleteLoc = By.xpath(deleteSmartNoPage.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNoDeleteLoc);
		clickElement(driver, smartNoDeleteLoc);
		clickConfirmDeleteBtn(driver);
		waitUntilInvisible(driver, saveAccountsSettingMessage);
	}

	public boolean isNumberDeleted(WebDriver driver, String smartNumber) {
		By smartNoDeleteLoc = By.xpath(isDeletedNoPage.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNoDeleteLoc);
		return Boolean.parseBoolean(getElementsText(driver, smartNoDeleteLoc));
	}

	public boolean isDeleteIconDisabled(WebDriver driver, String smartNumber) {
		By smartNoDeleteLoc = By.xpath(deleteSmartNoPage.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNoDeleteLoc);
		return findElement(driver, smartNoDeleteLoc).getAttribute("class").contains("disabled");
	}

	public void clickConfirmDeleteBtn(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
	}
		
	//yoda ai
	public void disableYodaAINotifications(WebDriver driver){
		if(findElement(driver, yodaAiNotificationCheckBox).isSelected()) {
			System.out.println("Checking yoda AI Setting checkbox");
			scrollToElement(driver, yodaAiNotificationToggleOn);
			clickElement(driver, yodaAiNotificationToggleOn);			
		}else {
			System.out.println(" yoda AI Setting already disable");
		} 
	}
	
	public void enableYodaAINotifications(WebDriver driver){
		if(!findElement(driver, yodaAiNotificationCheckBox).isSelected()) {
			System.out.println("Checking  yoda AI Setting checkbox");
			scrollToElement(driver, yodaAiNotificationToggleOff);
			clickElement(driver, yodaAiNotificationToggleOff);			
		}else {
			System.out.println(" yoda AI Setting already enabled");
		} 
	}
	
	//headers
	/**
	 * @param driver
	 * @return call queue header
	 */
	public boolean isCallQueuesHeadervisible(WebDriver driver) {
		return isElementVisible(driver, callQueuesHeader, 6);
	}
	
	/**
	 * @param driver
	 * @return call Processing header
	 */
	public boolean isCallProcessingHeadervisible(WebDriver driver) {
		return isElementVisible(driver, callProcessingHeader, 6);
	}
	
	/**
	 * @param driver
	 * @return Smart Numbers header
	 */
	public boolean isSmartNumbersHeadervisible(WebDriver driver) {
		return isElementVisible(driver, smartNumbersHeader, 6);
	}
		
}