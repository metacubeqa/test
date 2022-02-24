package support.source.callFlows;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;
						 

public class CallFlowPage extends SeleniumBase{

	Dashboard dashboard = new Dashboard();
	
	By deleteCallFlowBtn = By.cssSelector(".delete");
	By copyEditBtn		 = By.cssSelector(".copy");
	By deleteWarningMsg	 = By.cssSelector(".bootbox-body");
	By callFlowNameTab	 = By.cssSelector(".name.form-control");
	By callFlowDescTab	 = By.cssSelector(".description.form-control");
	By callFlowStatus	 = By.cssSelector(".status");
	By accountLink 		 = By.xpath("//label[text()='Account']/..//span/a");
	By closeBtn			 = By.cssSelector(".toast-close-button");			  
	By createdBy		 = By.xpath("//label[text()='Created By']/..//span/a");
	By updatedBy		 = By.xpath("//label[text()='Updated By']/..//span/a");
	By createdOn		 = By.xpath("//label[text()='Created On']/..//span");
	By updatedOn		 = By.xpath("//label[text()='Updated On']/..//span");
	By iconSpinner		 = By.cssSelector("span.icon-spinner");
	
	// Abandoned calls user section
	By userPickerItem 	  = By.cssSelector(".user-picker .item");
	By userPickerInputTab = By.cssSelector(".user-picker input");
	By userDropdownSelect = By.cssSelector(".user-picker .selectize-dropdown-content div");

	By abandonedCallsubject  = By.cssSelector(".abandoned-calls input.subject");
	By abandonedCallcomments = By.cssSelector(".abandoned-calls textarea.comments");

	By smartNumberTable	  = By.cssSelector(".smart-numbers td");
	By confirmDeleteBtnCallFlow = By.cssSelector("[data-bb-handler='ok'] , [data-bb-handler='confirm']");
	By noResultDisplay    = By.xpath("//h2[text()='No results to display']");
	
	//Call Flow search page
	By callFlowSearchTab 			= By.cssSelector(".call-flow-search .flow-name");
	By accountTextBoxSelectize  	= By.cssSelector(".account-picker div.selectize-input.items");
	By accountTextBoxInput      	= By.cssSelector(".account-picker div.selectize-input.items input");
	By accountTextBoxOptions    	= By.cssSelector(".account-picker .selectize-dropdown-content div[data-selectable] span:not(.picker-value)");
	By searchBtn		 			= By.cssSelector(".call-flow-search .btn-success");
	By addSmartNumberIcon 			= By.cssSelector("button.assign-smart-number");
	By deleteButton                 = By.xpath("//span[text()='Delete']/ancestor::li");
	By confirmDeleteBtn 	 		= By.xpath("//button[text()='Delete']");
	String callFlowNameLink	 		= ".//td/a[contains(@href,'#call-flows') and normalize-space(text()) = '$$callFlow$$']";
	
	//Call Flow Details
	By connectUserDropDown			= By.cssSelector(".connect-user .selectize-control");
	By connectUserDropDownInput		= By.cssSelector(".connect-user .selectize-control input");
	By connectUserDropDownOptions	= By.cssSelector(".connect-user .selectize-dropdown-content div[data-selectable='']");
	By saveCallFlowBtn				= By.cssSelector("button.btn-success");
	By saveCallFlowMessage			= By.className("toast-message");
	By errorMessage					= By.cssSelector(".alert-danger");
	By addLastStepCheckbox 			= By.cssSelector("input.finalStepDetails");
	By addLastStepToggleOnButton 	= By.xpath("//input[contains(@class,'finalStepDetails')]/..//label[contains(@class,'toggle-on')]");
	By addLastStepToggleOffButton 	= By.xpath("//input[contains(@class,'finalStepDetails')]/..//label[contains(@class,'toggle-off')]");
	By smartNumberDeleteList        = By.xpath("//div[@aria-label= 'rdna-menu-trigger']/button");
	String accountNamePage    		= ".//*[text()='$$accountName$$']/ancestor::div[@class='call-flow-overview']//a[contains(@href,'accounts')]";
	String smartNumberLink 	 		= ".//*[text()='$$smartNumber$$']/ancestor::tr/td[contains(@class,'renderable')]//*[contains(@href,'smart-numbers')] | //a[@class= 'ringdna-phone']/span[text()='$$smartNumber$$']";
	String smartNumberDeleteIcon	= ".//*[text()='$$smartNumber$$']/parent::a/parent::span/following-sibling::span//button";
	
	String smartNumberExtraInfo		= ".//*[text()='$$smartNumber$$']/ancestor::tr//td[contains(@class,'extraInfo')]";
	String smartNumberName			= ".//*[text()='$$smartNumber$$']/ancestor::tr//td[contains(@class,'name')]";
	String smartNumberCampaign		= ".//*[text()='$$smartNumber$$']/ancestor::tr//td[contains(@class,'campaign')]";
	
	//call Flow steps selectors
	By callFlowFunctionsArea		= By.cssSelector(".function-palette");
	By stepDetailTitle				= By.cssSelector(".step-detail .title.input");
	
	By voicmailWidget				= By.cssSelector("[src='images/call-flow/fullsize-voicemail.png']");
	By vmAudioTypeDropDown			= By.cssSelector(".audioType");
	By vmAudioText					= By.cssSelector("#audio-text");
	By vmUserGroupDropDown			= By.cssSelector(".block.voicemail [class='picker']  .selectize-control.ugv.single");
	By vmUserGroupTextBox			= By.cssSelector(".block.voicemail [class='picker']  .selectize-control.ugv.single input");
	By vmUserGroupDropDownOptions	= By.cssSelector(".block.voicemail [class='picker']  .selectize-dropdown-content div");
	
	By dialCallWidget		 	 	= By.cssSelector(".function-palette [title='Dial']");
	By dialToRDNAUsersRadioBtn		= By.cssSelector("input#dialUserQueueSkill");
	By dialCallToRDNACatDrppDown	= By.cssSelector(".block.dial .selectize-control.ugs-type");
	By dialCallToRDNACatInput		= By.cssSelector(".block.dial .selectize-control.ugs-type input");
	By dialCallToRDNACatOptions		= By.cssSelector(".block.dial .selectize-dropdown.ugs-type .option");
	By selectDialUserDropdown		= By.cssSelector(".block.dial .selectize-control.ugs-id");
	By selectDialUserInput			= By.cssSelector(".block.dial .selectize-control.ugs-id input");
	By selectDialUserDrpdwnOptions	= By.cssSelector(".block.dial .selectize-dropdown.ugs-id div[data-selectable='']");
	By timeOutTextBox				= By.cssSelector("input[data-key='timeout']");
	By queueTimeOut					= By.cssSelector(".queue-timeout");
	By dialCallSubject				= By.cssSelector(".block.dial [data-key='calllog-subject']");
	By dialCallComments				= By.cssSelector(".block.dial [data-key='calllog-comments']");
	By dialToIndividualRadioBtn		= By.cssSelector("input#dialManual");
	By individualNumberTextBox		= By.cssSelector(".block.dial [data-key='phoneNumbers']");
	By holdMusicFileUpload			= By.cssSelector(".fileupload.hold-music");
	By saveToLibraryCheckBox		= By.xpath(".//div[contains(@class,'audioTypePanel') and not(contains(@class,'hidden'))]//label[text()='Save this to library for future use']/../input");
	By selectLibrary				= By.cssSelector("#library-file");
	By recordCallCheckBox			= By.cssSelector("[type='checkbox'][data-key='record-call-flow']");
	By callDispoistionCheckBox		= By.cssSelector(".dial-call-disposition-not-required");
	
	//greeting widget
	By greetingWidget				= By.cssSelector("[src='images/call-flow/fullsize-greeting.png']");
	By greetingPhoneNumberBox		= By.cssSelector(".call-and-record-number");
	By callAndRecordBtn				= By.cssSelector("[data-action='call-and-record']");
	By calldiffNumberLoc			= By.cssSelector("[data-action='change-call-and-record']");
	By audioPlayerControlBar		= By.cssSelector(".audio-player-view .vjs-control-bar");
	By audioPlayerPlayControlBtn	= By.cssSelector(".vjs-play-control[type='button'][title='Play']");
	By audioPlayerPauseControlBtn	= By.cssSelector(".vjs-play-control[type='button'][title='Pause']");
	By retryCallAndRecordLoc		= By.cssSelector("[data-action='retry-call-and-record']");
	By libraryTitleInputBox			= By.cssSelector(".library-title");
	By fileUploadLink				= By.cssSelector(".audioTypePanel.audio-3");
	By selectedFile					= By.cssSelector(".selected-file");
	By changeFileLink				= By.cssSelector("[data-action='change-mp3']");
	
	//sms widget
	By smsWidget					= By.cssSelector("[src='images/call-flow/fullsize-sms.png']");
	By smsNumberCheckBox			= By.cssSelector(".block.sms #smsType");
	By smsNumberTextBox				= By.cssSelector(".block.sms [data-key='smsNumbers']");
	By smsTextBox					= By.cssSelector(".block.sms .sms-text.input");
	
	//loop widget
	By loopWidget					= By.cssSelector("[src='images/call-flow/fullsize-loop.png']");
	By loopListTextBox				= By.cssSelector(".block.loop input[data-key='loopList']");
	By loopVarTextBox				= By.cssSelector(".block.loop input[data-key='loopItemKey']");
	By forEachValueBlankNode		= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'For Each Value')]/parent::a/i");
	By forEachCompletedBlankNode	= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'Loop Completed')]/parent::a/i");
	
	//branch widget
	By branchWidget					= By.cssSelector("[src='images/call-flow/fullsize-branch.png']");
	By branchKeyTextBox				= By.cssSelector(".block.branch [data-key='branchKey']");
	By branchKeyEvaluationTextBox	= By.cssSelector(".branchAssignment .assignment");
	By branchValueNode				= By.xpath(".//*[@class='tree-container']//span[starts-with(text(),'Value')]/parent::a/i");
	
	//menu widget
	By menuWidget					= By.cssSelector("[src='images/call-flow/fullsize-menu.png']");
	By invalidAudioTypeLoc 			= By.cssSelector(".audioType[data-key='invalidAudioType']");
	By invalidAudioText 			= By.cssSelector("#audio-text[data-key='invalidAudioText']");
	String menuIconsBlankNode		= ".//*[@class='tree-container']//span[contains(text(), '$$Digit$$')]/parent::a/i";
	
	//call flow container
	By dialContainer				= By.xpath(".//*[@class='tree-container']//span[(text()= 'Dial')]/parent::a/i");
	By callFlowRootNode				= By.cssSelector(".tree-container [role='treeitem'][aria-level='1'] .jstree-anchor");
	By blankCallFlowRootNode		= By.cssSelector(".tree-container [role='treeitem'][aria-level='1']>.jstree-anchor>i[style*='images/call-flow/mini-placeholder.png']");
	By callFlowRootNodeLabel		= By.cssSelector(".tree-container [role='treeitem'][aria-level='1'] .jstree-anchor span");
	By dropFunctionCheckBox  		= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'Drop a function here')]/parent::a");
	By removeStepConfirmBtn			= By.cssSelector(".bootbox-confirm [data-bb-handler='confirm']");
	String callFlowNameHeader 		= ".//*[text()='$$callFlow$$']/ancestor::div[@class='tree-header']/span";
	
	//prompt widget
	By promptWidget					= By.cssSelector("[src='images/call-flow/fullsize-prompt.png']");
	By promptBlankNode				= By.xpath(".//*[@class='tree-container']//span[@class='keypress']//following-sibling::span[contains(text(), 'Drop a function here')]/parent::a/i");
	By promptSpacing				= By.cssSelector("input[data-key='spacing']");
	By promptMaxDigits				= By.cssSelector("input[data-key='maxDigits']");
	By promptAssignmentTextBox		= By.cssSelector(".promptAssignment .assignment");
	By branchAssignmentTextBox		= By.cssSelector(".branchAssignment .assignment");
	By anyResponseNode				= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'Any Response - Drop a function here')]/parent::a/i");
	By noResponseNode				= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'No Response - Drop a function here')]/parent::a/i");
	By noAnswerNode					= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'No Answer - Drop a function here')]/parent::a/i");
	
	By anyValueGreeting				= By.xpath(".//span[text()='Any Value - Greeting']");
	By noValueDial					= By.xpath(".//span[text()='No Value - Dial']");
	By promptAnyResponseGreeting    = By.xpath(".//span[text()='Any Response - Greeting']");
	By promptNoResponseGreeting     = By.xpath(".//span[text()='No Response - Greeting']");
	
	//Time widget
	By timeWidget					= By.cssSelector("[src='images/call-flow/fullsize-time.png']");
	By holidayScheduleNode			= By.xpath(".//*[@class='tree-container']//span[contains(text(), 'Holiday Schedule - Drop a function here')]/parent::a/i");
	By timeZoneDropDown				= By.cssSelector(".selectize-control.timezone .selectize-input");
	By timeZoneDropDownTxtBox		= By.cssSelector(".selectize-control.timezone .selectize-input input");
	By timeZoneDropDownOptions		= By.cssSelector(".selectize-dropdown.timezone .option");
	By timeWidAddTimeBtnList		= By.cssSelector(".block.time [data-action='add-time']");
	By timeWidStartTimeBoxList		= By.cssSelector(".block.time .open input[id*='-from']");
	By timeWidEndTimeBoxList		= By.cssSelector(".block.time .open input[data-key*='-to']");
	By useHoldayCheckBox			= By.cssSelector(".block.time input.use-holiday");
	By holidaySchDropDown			= By.cssSelector(".block.time .holiday-schedule .selectize-input");
	By holidaySchDropDownOptions	= By.cssSelector(".block.time .holiday-schedule .option");
	
	//Conference widget
	By conferenceWidget				= By.cssSelector("[src='images/call-flow/fullsize-conference.png']");
	By conferenceContainer			= By.xpath(".//*[@class='tree-container']//span[(text()= 'Container')]/parent::a/i");
	
	// Hangup widget
	By hangupWidget					= By.cssSelector("[src='images/call-flow/fullsize-hangup.png']");
	By hangupContainer				= By.xpath(".//*[@class='tree-container']//span[(text()= 'Hangup')]/parent::a/i");
	
	// Callout widget
	By callOutWidget				= By.cssSelector("[src='images/call-flow/fullsize-callout.png']");
	By calloutContainer				= By.xpath(".//*[@class='tree-container']//span[(text()= 'Callout')]/parent::a/i");
	
	//call flow error message
	By callFlowErrorMessage         = By.cssSelector(".alert.alert-danger.center.validation-error");
	
	public static enum CallFlowStatus{
		Active,
		Paused
	}
	
	public static enum VMAudioType{
		Texttospeech("Text-to-speech"),
		RecordMessage("Record Message"),
		UploadMp3("Upload MP3"),
		FromLibrary("From Library"),
		VoicemailGreeting("Voicemail Greeting");
		
		private String displayName;
		
		VMAudioType(String displayName){
			this.displayName = displayName;
		};
		
		public String displayName(){return displayName;};
		
		@Override
		public String toString(){return displayName;};
	}
	
	public static enum DialCallRDNATOCat {
		User("User"), CallQueue("Call Queue"), Skill("Skill");

		private final String displayName;

		DialCallRDNATOCat(final String display) {
			this.displayName = display;
		}

		@Override
		public String toString() {
			return this.displayName;
		}
	}
	
	public static enum CallFlowFunctions{
		Dial,
		SMS;
	}
	
	/**
	 * @param driver
	 * assert pagination of call flow page
	 */
	public void assertCallFlowPagination(WebDriver driver) {
		assertTrue(getElements(driver, smartNumberDeleteList).size() <= 10);
	}
	
	public void verifyOnCallFlowSearchPage(WebDriver driver) {
		assertTrue(isElementVisible(driver, callFlowSearchTab, 5));
	}

	public void dropSubFunctions(WebDriver driver, CallFlowFunctions function, By destinationLocator){
		switch (function) {
		case Dial: {
			dragAndDropElement(driver, dialCallWidget, destinationLocator);
			break;
		}
		case SMS: {
			dragAndDropElement(driver, smsWidget, destinationLocator);
			break;
		}
		default:
			Assert.fail();
			break;
		}
		isElementVisible(driver, dashboard.paceBar, 5);	
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String getAlertMsgText(WebDriver driver){
		return getElementsText(driver, errorMessage);
	}
	
	public boolean isAlertMsgVisible(WebDriver driver){
		return isElementVisible(driver, errorMessage, 5);
	}
	
	/*******Create Call Flow Page Functions starts here*******/
	
	public void createCallFlow(WebDriver driver, String callFlowName, String callFlowDesc) {
		enterText(driver, callFlowNameTab, callFlowName);
		enterText(driver, callFlowDescTab, callFlowDesc);
		saveCallFlowSettings(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public void enterAbandonedCalls_Subjects(WebDriver driver, String subject, String comments){
		enterText(driver, abandonedCallsubject, subject);
		enterText(driver, abandonedCallcomments, comments);
	}
	
	public void selectAbandonedUser(WebDriver driver, String userName){
		enterTextAndSelectFromDropDown(driver, userPickerItem, userPickerInputTab, userDropdownSelect, userName);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String getAbandonedUser(WebDriver driver){
		waitUntilVisible(driver, userPickerItem);
		return getElementsText(driver, userPickerItem);
	}
	
	public String getAbandonedSubject(WebDriver driver){
		waitUntilVisible(driver, abandonedCallsubject);
		return getAttribue(driver, abandonedCallsubject, ElementAttributes.value);
	}
	
	public String getAbandonedComments(WebDriver driver){
		waitUntilVisible(driver, abandonedCallcomments);
		return getAttribue(driver, abandonedCallcomments, ElementAttributes.value);
	}
	
	/*******Create Call Flow Page Functions ends here*******/
	
	/*******Search Call Flow Page Functions starts here*******/
	
	//This method is to select account for search
	public void selectAccount(WebDriver driver, String accountName) {
		if (Strings.isNotNullAndNotEmpty(accountName) && isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			clickElement(driver, accountTextBoxOptions);
		}
	}
	
	public void searchCallFlow(WebDriver driver, String callFlow, String accountName) {
		waitUntilVisible(driver, callFlowSearchTab);
		enterText(driver, callFlowSearchTab, callFlow);
		selectAccount(driver, accountName);
		waitUntilVisible(driver, searchBtn);
		clickElement(driver, searchBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isCallFlowPresent(WebDriver driver, String callFlow) {
		By callFlowLoc = By.xpath(callFlowNameLink.replace("$$callFlow$$", callFlow));
		return isElementVisible(driver, callFlowLoc, 7);
	}
	
	public void clickSelectedCallFlow(WebDriver driver, String callFlow) {
		By callFlowLoc = By.xpath(callFlowNameLink.replace("$$callFlow$$", callFlow));
		waitUntilVisible(driver, callFlowLoc);
		clickElement(driver, callFlowLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	/*******Search Call Flow Page Functions ends here*******/
	
	/*******Call Flow details Functions starts here*******/
	public void verifyCallFlowPageDetails(WebDriver driver, String callFlow, String accountName) {
		By callFlowLoc = By.xpath(callFlowNameHeader.replace("$$callFlow$$", callFlow));
		assertTrue(isElementVisible(driver, callFlowLoc, 7));
		By accountNameLoc = By.xpath(accountNamePage.replace("$$accountName$$", accountName));
		assertTrue(isElementVisible(driver, accountNameLoc, 7));
	}
	
	public void verifyNewCallFlowDetails(WebDriver driver, String userName){
		assertEquals(getCreatedOnUser(driver), "Invalid date");
		assertEquals(getUpdatedOnUser(driver), "Invalid date");
		assertTrue(isElementInvisible(driver, updatedBy, 5));
		assertEquals(getCreatedByUser(driver), userName);
	}
	
	public void clickCopyEditBtn(WebDriver driver) {
		waitUntilVisible(driver, copyEditBtn);
		clickElement(driver, copyEditBtn);
		isSaveCallFlowMsgDisappeared(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public String getCreatedByUser(WebDriver driver) {
		waitUntilVisible(driver, createdBy);
		return getElementsText(driver, createdBy);
	}

	public String getUpdatedByUser(WebDriver driver) {
		waitUntilVisible(driver, updatedBy);
		return getElementsText(driver, updatedBy);
	}

	public String getCreatedOnUser(WebDriver driver) {
		waitUntilVisible(driver, createdOn);
		return getElementsText(driver, createdOn);
	}

	public String getUpdatedOnUser(WebDriver driver) {
		waitUntilVisible(driver, updatedOn);
		return getElementsText(driver, updatedOn);
	}
	
	public String getTextCallFlowName(WebDriver driver) {
		waitUntilVisible(driver, callFlowNameTab);
		return getAttribue(driver, callFlowNameTab, ElementAttributes.value);
	}
	
	public String getTextCallFlowDesc(WebDriver driver) {
		waitUntilVisible(driver, callFlowDescTab);
		return getAttribue(driver, callFlowDescTab, ElementAttributes.value);
	}

	public String getCurrentStatus(WebDriver driver) {
		Select selectDropdown = new Select(findElement(driver, callFlowStatus));
		return selectDropdown.getFirstSelectedOption().getText();
	}
	
	public void selectStatus(WebDriver driver, String status) {
		waitUntilVisible(driver, callFlowStatus);
		selectFromDropdown(driver, callFlowStatus, SelectTypes.value, status);
	}
	
	public boolean isDeleteBtnDisabled(WebDriver driver) {
		return isAttributePresent(driver, deleteCallFlowBtn, "disabled");
	}
	
	public void disableAddLastStepSetting(WebDriver driver){
		if(findElement(driver, addLastStepCheckbox).isSelected()) {
			clickElement(driver, addLastStepToggleOnButton);	
			System.out.println("Disabled add last step setting");
		} else {
			System.out.println("restrict add last step setting already Disabled");
		}
	}
	
	public void enableAddLastStepSetting(WebDriver driver){
		if(!findElement(driver, addLastStepCheckbox).isSelected()) {
			clickElement(driver, addLastStepToggleOffButton);	
			System.out.println("enabled add last step download setting");
		} else {
			System.out.println("restrict add last step setting already enabled");
		}
	}
	
	public void deleteCallFlow(WebDriver driver){
		selectStatus(driver, CallFlowStatus.Paused.name());
		clickSaveBtn(driver);
		assertFalse(isDeleteBtnDisabled(driver), "Delete button is disabled");
		
		//deleting call flow
		clickDeleteCallFlowBtn(driver);
	}
	
	public void clickSaveBtn(WebDriver driver) {
		waitUntilVisible(driver, saveCallFlowBtn);
		clickElement(driver, saveCallFlowBtn);
		isSaveCallFlowMsgDisappeared(driver);
	}
	
	public void clickDeleteCallFlowBtn(WebDriver driver) {
		waitUntilVisible(driver, deleteCallFlowBtn);
		clickElement(driver, deleteCallFlowBtn);
		clickConfirmDeleteBtn(driver);
	}
	
	public void verifyCopyEditMsg(WebDriver driver) {
		waitUntilVisible(driver, saveCallFlowMessage);
		assertTrue(isElementVisible(driver, saveCallFlowMessage, 5));
		assertEquals(getSaveCallFLowMessage(driver), "Call Flow copied.", "Message do not match");
	}
	
	public void selectSFConnectUser(WebDriver driver, String user) {
		if (Strings.isNotNullAndNotEmpty(user)) {
			enterTextAndSelectFromDropDown(driver, connectUserDropDown, connectUserDropDownInput,
					connectUserDropDownOptions, user);
		} else {
			clickElement(driver, connectUserDropDown);
			enterBackspace(driver, connectUserDropDownInput);
			assertEquals(getElementsText(driver, connectUserDropDownInput), "");
			clickElement(driver, promptWidget);
		}
	}
	
	public void clickAddSmartNoIcon(WebDriver driver) {
		waitUntilVisible(driver, addSmartNumberIcon);
		clickElement(driver, addSmartNumberIcon);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyNoSmartNumbersPresent(WebDriver driver) {
		assertTrue(isElementVisible(driver, noResultDisplay, 6));
	}

	public String getSmartNumberExtraInfo(WebDriver driver, String smartNumber) {
		By smartNumberLoc = By.xpath(smartNumberExtraInfo.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNumberLoc);
		scrollToElement(driver, smartNumberLoc);
		return getElementsText(driver, smartNumberLoc);
	}
	
	public String getSmartNumberName(WebDriver driver, String smartNumber) {
		By smartNumberLoc = By.xpath(smartNumberName.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNumberLoc);
		scrollToElement(driver, smartNumberLoc);
		return getElementsText(driver, smartNumberLoc);
	}
	
	public String getSmartNumberCampaign(WebDriver driver, String smartNumber) {
		By smartNumberLoc = By.xpath(smartNumberCampaign.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNumberLoc);
		scrollToElement(driver, smartNumberLoc);
		return getElementsText(driver, smartNumberLoc);
	}
	
	public void clickSmartNumberLink(WebDriver driver, String smartNumber) {
		dashboard.isPaceBarInvisible(driver);
		By smartNumberLoc = By.xpath(smartNumberLink.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNumberLoc);
		scrollToElement(driver, smartNumberLoc);
		clickByJs(driver, smartNumberLoc);
	}
	
	public boolean isSmartNumberPresent(WebDriver driver, String smartNumber) {
		dashboard.isPaceBarInvisible(driver);
		By smartNumberLoc = By.xpath(smartNumberLink.replace("$$smartNumber$$", smartNumber));
		return isElementVisible(driver, smartNumberLoc, 5);
	}
	
	public void deleteSmartNumberOnPage(WebDriver driver, String smartNumber) {
		By smartNoLoc = By.xpath(smartNumberDeleteIcon.replace("$$smartNumber$$", smartNumber));
		waitUntilVisible(driver, smartNoLoc);
		scrollIntoView(driver, smartNoLoc);
		clickElement(driver, smartNoLoc);
		waitUntilVisible(driver, deleteButton);
		clickElement(driver, deleteButton);
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilInvisible(driver, confirmDeleteBtn);
		assertFalse(isElementVisible(driver, smartNoLoc, 5));
	}
	
	public void clickConfirmDeleteBtn(WebDriver driver) {
		waitUntilVisible(driver, deleteWarningMsg);
		waitUntilVisible(driver, confirmDeleteBtnCallFlow);
		clickElement(driver, confirmDeleteBtnCallFlow);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickAccountLink(WebDriver driver) {
		waitUntilVisible(driver, accountLink);
		scrollIntoView(driver, accountLink);
		clickElement(driver, accountLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	// call Flow steps starts here
	public String getSaveCallFLowMessage(WebDriver driver) {
		return getElementsText(driver, saveCallFlowMessage);
	}

	public void isSaveCallFlowMsgDisappeared(WebDriver driver){
		isElementVisible(driver, saveCallFlowMessage, 2);
		waitUntilInvisible(driver, saveCallFlowMessage);
	}
	
	public void saveCallFlowSettings(WebDriver driver){
		scrollToElement(driver, saveCallFlowBtn);
		clickElement(driver, saveCallFlowBtn);
		assertEquals(getSaveCallFLowMessage(driver), "Call Flow saved.");
		isSaveCallFlowMsgDisappeared(driver);		
	}
	
	public Boolean isCallFlowSaveBtnVisible(WebDriver driver){
		return isElementVisible(driver, saveCallFlowBtn, 0);
	}

	public void clickCloseBtn(WebDriver driver) {
		waitUntilVisible(driver, closeBtn);
		clickElement(driver, closeBtn);
	} 
	
	public void isIconSpinnerInvisible(WebDriver driver) {
		waitUntilInvisible(driver, iconSpinner);
	}
	
	/*******Call Flow details Functions ends here*******/
	
	/*******Call Flow Steps Configuration Functions Starts here*******/
	public void removeAllCallFlowSteps(WebDriver driver){
		if(!isElementVisible(driver, blankCallFlowRootNode, 5)  && isElementVisible(driver, saveCallFlowBtn, 0)){
			dragAndDropElement(driver, callFlowRootNode, callFlowFunctionsArea);
			if(isElementVisible(driver, removeStepConfirmBtn, 5)){
				clickElement(driver, removeStepConfirmBtn);
				waitUntilInvisible(driver, removeStepConfirmBtn);
			}
			disableAddLastStepSetting(driver);
			saveCallFlowSettings(driver);
			waitUntilVisible(driver, blankCallFlowRootNode);
		}
	}
	
	//Dial widget
	public void dragAndDropDialImage(WebDriver driver){
		idleWait(1);
		dragAndDropElement(driver, dialCallWidget, dropFunctionCheckBox);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyWidgetTitle(WebDriver driver, String text){
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), text);
	}

	public boolean selectGroupFromDialSection(WebDriver driver, DialCallRDNATOCat rdnaCategory, String userOrGroup) {
		scrollToElement(driver, dialToRDNAUsersRadioBtn);
		clickElement(driver, dialToRDNAUsersRadioBtn);
		selectDialCallToRDNACat(driver, rdnaCategory);
		clickElement(driver, selectDialUserInput);
		if (isListContainsText(driver, getInactiveElements(driver, selectDialUserDrpdwnOptions), userOrGroup)) {
			selectDialRDNAUserOrGroup(driver, userOrGroup);
			return true;
		}
		return false;
	}
	
	public boolean isDialTypeGroupsListEmpty(WebDriver driver, DialCallRDNATOCat rdnaCategory) {
		scrollToElement(driver, dialToRDNAUsersRadioBtn);
		clickElement(driver, dialToRDNAUsersRadioBtn);
		selectDialCallToRDNACat(driver, rdnaCategory);
		clickElement(driver, selectDialUserInput);
		if (getTextListFromElements(driver, selectDialUserDrpdwnOptions) == null
				|| getTextListFromElements(driver, selectDialUserDrpdwnOptions).isEmpty()) {
			return true;
		}
		return false;
	}
	
	public void selectDialCallToRDNACat(WebDriver driver, DialCallRDNATOCat rdnaCategory){
		enterTextAndSelectFromDropDown(driver, dialCallToRDNACatDrppDown, dialCallToRDNACatInput, dialCallToRDNACatOptions, rdnaCategory.displayName);
	}
	
	public void selectDialRDNAUserOrGroup(WebDriver driver, String userOrGroup){
		enterTextAndSelectFromDropDown(driver, selectDialUserDropdown, selectDialUserInput, selectDialUserDrpdwnOptions, userOrGroup);
	}
	
	public void addDialStepCallNotes(WebDriver driver, String callSubject, String callComment){
		enterText(driver, dialCallSubject, callSubject);
		enterText(driver, dialCallComments, callComment);
	}

	public void addDialStepToRDNAUsers(WebDriver driver,DialCallRDNATOCat rdnaCategory, String userOrGroup, String timeOut){
		dragAndDropDialImage(driver);
		selectGroupFromDialSection(driver, rdnaCategory, userOrGroup);
		enterText(driver, timeOutTextBox, timeOut);
	}
	
	public String getTimeOutInputText(WebDriver driver){
		waitUntilVisible(driver, timeOutTextBox);
		scrollToElement(driver, timeOutTextBox);
		return getAttribue(driver, timeOutTextBox, ElementAttributes.value);
	}
	
	public String getQueueTimeOut(WebDriver driver){
		waitUntilVisible(driver, queueTimeOut);
		scrollIntoView(driver, queueTimeOut);
		return getElementsText(driver, queueTimeOut);
	}
	
	public void configureDialStepIndividualUser(WebDriver driver, String number, String timeOut){
		clickElement(driver, dialToIndividualRadioBtn);
		enterText(driver, individualNumberTextBox, number);
		enterText(driver, timeOutTextBox, timeOut);
	}
	
	public void deleteAllNestedStepsOfDial(WebDriver driver) {
		dragAndDropElement(driver, dialCallWidget, dialContainer);
		waitUntilVisible(driver, confirmDeleteBtn);
		scrollToElement(driver, confirmDeleteBtn);
		clickByJs(driver, confirmDeleteBtn);
	}
	
	public boolean isRecordCallsDisabled(WebDriver driver) {
		waitUntilVisible(driver, recordCallCheckBox);
		scrollIntoView(driver, recordCallCheckBox);
		return isElementDisabled(driver, recordCallCheckBox, 5);
	}
	
	public void clickSaveToLibFutureUse(WebDriver driver) {
		waitUntilVisible(driver, saveToLibraryCheckBox);
		clickElement(driver, saveToLibraryCheckBox);
	}
	
	public void selectFromLibrary(WebDriver driver, String fileName) {
		selectFromDropdown(driver, selectLibrary, SelectTypes.visibleText, fileName);
	}
	
	public void checkCallDispositionCheckBox(WebDriver driver) {
		if(!findElement(driver, callDispoistionCheckBox).isSelected()) {
			clickElement(driver, callDispoistionCheckBox);
		}
	}
	
	public String selectHoldMusicFile(WebDriver driver, String pathOfMp3) {
		waitUntilVisible(driver, holdMusicFileUpload);
		clickElement(driver, holdMusicFileUpload);
		idleWait(1);
		HelperFunctions.uploadMp3FileRecord(pathOfMp3);
		idleWait(1);
		isIconSpinnerInvisible(driver);
		waitUntilVisible(driver, selectedFile);
		return getElementsText(driver, selectedFile);
	}
	
	//Voicemail Widget
	public void addVoicemailStep(WebDriver driver, VMAudioType audioType, String vmAduioTextData, String vmReceiverUser){
		dragAndDropVoicemailImage(driver);
		selectVMAudioType(driver, audioType);
		enterVMText(driver, vmAduioTextData);
		selectUserOrGroupForVM(driver, vmReceiverUser);
	}
	
	public void dragAndDropVoicemailImage(WebDriver driver){
		dragAndDropElement(driver, voicmailWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, vmAudioTypeDropDown);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Voicemail");
	}
	
	public void selectVMAudioType(WebDriver driver, VMAudioType audioType){
		selectFromDropdown(driver, vmAudioTypeDropDown, SelectTypes.visibleText, audioType.displayName().toString());
	}
	
	public void enterVMText(WebDriver driver, String vmAduioTextData){
		enterText(driver, vmAudioText, vmAduioTextData);
	}
	
	public void selectUserOrGroupForVM(WebDriver driver, String vmReceiverUser){
		enterTextAndSelectFromDropDown(driver, vmUserGroupDropDown, vmUserGroupTextBox, vmUserGroupDropDownOptions, vmReceiverUser);
	}
	
	//Loop Widget
	public void adddLoopStep(WebDriver driver, String loopList, String loopListVariable){
		dragAndDropLoopImage(driver);
		enterText(driver, loopListTextBox, loopList);
		enterText(driver, loopVarTextBox, loopListVariable);
	}
	
	public void dragAndDropLoopImage(WebDriver driver){
		dragAndDropElement(driver, loopWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, loopListTextBox);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Loop");
	}
	
	public void dropForEachValueFunction(WebDriver driver, CallFlowFunctions forEachFunction) {
		waitUntilVisible(driver, forEachValueBlankNode);
		dropSubFunctions(driver, forEachFunction, forEachValueBlankNode);
	}
	
	//Branch Widget
	public void adddBranchStep(WebDriver driver, String branchKey){
		dragAndDropBranchImage(driver);
		enterText(driver, branchKeyTextBox, branchKey);
	}
	
	public void dragAndDropBranchImage(WebDriver driver){
		dragAndDropElement(driver, branchWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, branchKeyTextBox);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Branch");
	}
	
	public void dropbranchValueFunction(WebDriver driver, CallFlowFunctions branchFunction, String branchKeyValue) {
		waitUntilVisible(driver, branchValueNode);
		dropSubFunctions(driver, branchFunction, branchValueNode);
		waitUntilVisible(driver, branchKeyEvaluationTextBox);
		findElement(driver, branchKeyEvaluationTextBox).clear();
		for(int i=0; i< branchKeyValue.length(); i++){
			appendText(driver, branchKeyEvaluationTextBox, Character.toString(branchKeyValue.charAt(i)));
		}
	}
	
	// Menu Widget
	public void adddMenuStep(WebDriver driver, VMAudioType validAudioType, VMAudioType invalidAudioType) {
		dragAndDropMenuImage(driver);
		selectVMAudioType(driver, validAudioType);
		enterVMText(driver, "This is a test valid menu response");
		selectInvalidAudioType(driver, invalidAudioType);
		enterInvalidAudioText(driver, "This is a test invalid menu response");
	}

	public void dragAndDropMenuImage(WebDriver driver) {
		dragAndDropElement(driver, menuWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, vmAudioTypeDropDown);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Menu");
	}
	
	public void selectInvalidAudioType(WebDriver driver, VMAudioType invalidAudioType){
		selectFromDropdown(driver, invalidAudioTypeLoc, SelectTypes.visibleText, invalidAudioType.displayName().toString());
	}
	
	public void enterInvalidAudioText(WebDriver driver, String invalidAduioTextData){
		enterText(driver, invalidAudioText, invalidAduioTextData);
	}

	public void dropMenuFunction(WebDriver driver, String digit, CallFlowFunctions branchFunction) {
		By dropMenuLocator	= By.xpath(menuIconsBlankNode.replace("$$Digit$$", digit));
		waitUntilVisible(driver, dropMenuLocator);
		dropSubFunctions(driver, branchFunction, dropMenuLocator);
	}
	
	//sms widget
	public void adddSMSStep(WebDriver driver, String smsNumber, String smsText) {
		dragAndDropMenuImage(driver);
		configureSMSStep(driver, smsNumber, smsText);
	}

	public void dragAndDropSMSImage(WebDriver driver) {
		dragAndDropElement(driver, smsWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, smsNumberCheckBox);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "SMS");
	}
	
	public void configureSMSStep(WebDriver driver, String smsNumber, String smsText){
		clickElement(driver, smsNumberCheckBox);
		enterText(driver, smsNumberTextBox, HelperFunctions.getNumberInSimpleFormat(smsNumber));
		enterText(driver, smsTextBox, smsText);
	}
	
	//prompt widget
	public void addPromptMaxDigits(WebDriver driver, String maxDigits) {
		waitUntilVisible(driver, promptMaxDigits);
		enterText(driver, promptMaxDigits, maxDigits);
		clickSaveBtn(driver);
		scrollToElement(driver, promptMaxDigits);
		assertTrue(getAttribue(driver, promptMaxDigits, ElementAttributes.value).equals("20"));
	}

	public void adddPromptStep(WebDriver driver, VMAudioType validAudioType, VMAudioType invalidAudioType, String spacing, String maxDigits) {
		dragAndDropPromptImage(driver);
		selectVMAudioType(driver, validAudioType);
		enterVMText(driver, "This is a test valid menu response");
		selectInvalidAudioType(driver, invalidAudioType);
		enterInvalidAudioText(driver, "This is a test invalid menu response");
		enterText(driver, promptSpacing, spacing);
		enterText(driver, promptMaxDigits, maxDigits);
	}

	public void dragAndDropPromptImage(WebDriver driver) {
		dragAndDropElement(driver, promptWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, vmAudioTypeDropDown);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Prompt");
	}

	public void dropPromptFunction(WebDriver driver, CallFlowFunctions promptFunction, String promptKeyValue) {
		waitUntilVisible(driver, promptBlankNode);
		dropSubFunctions(driver, promptFunction, promptBlankNode);
		findElement(driver, promptAssignmentTextBox).clear();
		for(int i=0; i< promptKeyValue.length(); i++){
			appendText(driver, promptAssignmentTextBox, Character.toString(promptKeyValue.charAt(i)));
		}
	}

	public void clickAnyValueGreeting(WebDriver driver) {
		waitUntilVisible(driver, anyValueGreeting);
		clickElement(driver, anyValueGreeting);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickNoValueDial(WebDriver driver) {
		waitUntilVisible(driver, noValueDial);
		clickElement(driver, noValueDial);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickAnyReponseGreeting(WebDriver driver) {
		waitUntilVisible(driver, promptAnyResponseGreeting);
		clickElement(driver, promptAnyResponseGreeting);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickNoReponseGreeting(WebDriver driver) {
		waitUntilVisible(driver, promptNoResponseGreeting);
		clickElement(driver, promptNoResponseGreeting);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isReponsePromptInputBoxDisabled(WebDriver driver){
		waitUntilVisible(driver, promptAssignmentTextBox);
		return isElementDisabled(driver, promptAssignmentTextBox, 5);
	}
	
	public boolean isBranchAssignmentInputBoxDisabled(WebDriver driver){
		waitUntilVisible(driver, branchAssignmentTextBox);
		return isElementDisabled(driver, branchAssignmentTextBox, 5);
	}
	
	public void dropNoResponseFunction(WebDriver driver, CallFlowFunctions promptFunction) {
		waitUntilVisible(driver, noResponseNode);
		dropSubFunctions(driver, promptFunction, noResponseNode);
	}
	
	public void dropAnyResponseFunction(WebDriver driver, CallFlowFunctions promptFunction) {
		waitUntilVisible(driver, anyResponseNode);
		dropSubFunctions(driver, promptFunction, anyResponseNode);
	}
	
	public void dropNoAnswerFunction(WebDriver driver, CallFlowFunctions promptFunction) {
		waitUntilVisible(driver, noAnswerNode);
		dropSubFunctions(driver, promptFunction, noAnswerNode);
	}
	
	public void addTimeStep(WebDriver driver, String holidaySchedule) {
		dragAndDropTimeImage(driver);
		enterTextAndSelectFromDropDown(driver, timeZoneDropDown, timeZoneDropDownTxtBox, timeZoneDropDownOptions, "IST");
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == 1){
			dayOfWeek = 6;
		}else{
			dayOfWeek = dayOfWeek - 2;
		}
		getElements(driver, timeWidAddTimeBtnList).get(dayOfWeek).click();
		enterText(driver, getInactiveElements(driver, timeWidStartTimeBoxList).get(dayOfWeek), "12:00 AM");
		enterText(driver, getInactiveElements(driver, timeWidEndTimeBoxList).get(dayOfWeek), "12:00 PM");
		clickElement(driver, useHoldayCheckBox);
		clickAndSelectFromDropDown(driver, holidaySchDropDown, holidaySchDropDownOptions, holidaySchedule);
	}

	public void dragAndDropTimeImage(WebDriver driver) {
		dragAndDropElement(driver, timeWidget, dropFunctionCheckBox);
		waitUntilVisible(driver, timeWidAddTimeBtnList);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Time");
	}
	
	public void dropHoldayScheduleFunction(WebDriver driver, CallFlowFunctions holidayFunction) {
		waitUntilVisible(driver, holidayScheduleNode);
		dropSubFunctions(driver, holidayFunction, holidayScheduleNode);
	}
	
	//Greeting widget
	public void dragAndDropGreeting(WebDriver driver) {
		dragAndDropElement(driver, greetingWidget, dropFunctionCheckBox);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enterPhoneNumberToRecord(WebDriver driver, String phoneNumber) {
		enterText(driver, greetingPhoneNumberBox, phoneNumber);
	}
	
	public boolean isCallAndRecordBtnVisible(WebDriver driver) {
		return isElementVisible(driver, callAndRecordBtn, 5);
	}
	
	public void clickCallAndRecordBtn(WebDriver driver) {
		waitUntilVisible(driver, callAndRecordBtn);
		clickElement(driver, callAndRecordBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, calldiffNumberLoc);
	}
	
	public void clickCallADifferentNumberLoc(WebDriver driver) {
		waitUntilVisible(driver, calldiffNumberLoc);
		clickElement(driver, calldiffNumberLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isAudioPlayerControlBarVisible(WebDriver driver) {
		return isElementVisible(driver, audioPlayerControlBar, 5);
	}
	
	public void playAudioPlayerBar(WebDriver driver) {
		waitUntilVisible(driver, audioPlayerPlayControlBtn);
		clickElement(driver, audioPlayerPlayControlBtn);
		waitUntilVisible(driver, audioPlayerPauseControlBtn);
	}
	
	public void retryCallAndRecord(WebDriver driver) {
		waitUntilVisible(driver, retryCallAndRecordLoc);
		clickElement(driver, retryCallAndRecordLoc);
	}
	
	public void enterLibraryTitle(WebDriver driver, String libraryName) {
		waitUntilVisible(driver, libraryTitleInputBox);
		enterText(driver, libraryTitleInputBox, libraryName);
	}
	
	public String uploadMp3File(WebDriver driver, String pathOfMp3) {
		waitUntilVisible(driver, fileUploadLink);
		clickElement(driver, fileUploadLink);
		isIconSpinnerInvisible(driver);
		idleWait(2);
		HelperFunctions.uploadMp3FileRecord(pathOfMp3);
		idleWait(2);
		waitUntilVisible(driver, selectedFile);
		waitUntilVisible(driver, changeFileLink);
		return getElementsText(driver, selectedFile);
	}
	
	public void changeFileLink(WebDriver driver) {
		waitUntilVisible(driver, changeFileLink);
		clickElement(driver, changeFileLink);
	}
	
	//Conference widget
	public void dragAndDropConference(WebDriver driver) {
		dragAndDropElement(driver, conferenceWidget, dropFunctionCheckBox);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Conference");
	}
	
	// Hangup widget
	public void dragAndDropHangup(WebDriver driver) {
		dragAndDropElement(driver, hangupWidget, dropFunctionCheckBox);
	}

	// Callout widget
	public void dragAndDropCallout(WebDriver driver) {
		dragAndDropElement(driver, callOutWidget, dropFunctionCheckBox);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(getAttribue(driver, stepDetailTitle, ElementAttributes.value), "Callout");
	}
	
	/**
	 * @param driver
	 * verify error message
	 */
	public void verifyCallFlowErrorMessage(WebDriver driver) {
		if(isElementVisible(driver, callFlowErrorMessage, 6)) {
			getElementsText(driver, callFlowErrorMessage).contains("This call flow is not valid. It cannot be assigned to a ringDNA number until the issues highlighted in red are addressed.");
		}
	}
}
