
package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.SeleniumBase;
import utility.HelperFunctions;

public class SoftPhoneCalling extends SeleniumBase{
	
	//*******Dial Pad locators starts here*******//
	By dialPadImage 			= By.cssSelector("img.keypad");
	By dialPadTextBox 			= By.cssSelector("input.number");
	By flagDropDown 			= By.cssSelector(".number-row .selectize-input");
	By dialPadZeroDigit			= By.cssSelector("img[data-id='0']");
	By dialPadCallButton 		= By.cssSelector("[data-id = 'call']");
	By dialPadSMSButton 		= By.cssSelector("[data-id='sms']");
	By outboundNumDropwdown		= By.cssSelector("div#outboundNumberSelector .selectize-control");
	By outboundNumDropwdownMenu	= By.cssSelector("div#outboundNumberSelector .selectize-dropdown-content");
	By outboundNumDropDownOpns	= By.xpath(".//div[@id='outboundNumberSelector']//*[@class='selectize-dropdown-content']/div");
	By outboundselectedNum		= By.cssSelector("#outboundNumberSelector .selectize-input .outboundNumber");
	By selectedOutboundNum		= By.cssSelector("#outboundNumberSelector .selectize-input span");
	By selectedAdditionNumIcon	= By.cssSelector("#outboundNumberSelector .selectize-input img.number-type[src='images/icon-outbound-dual.svg']");
	By selectedOutboundNumIcon	= By.cssSelector("#outboundNumberSelector .selectize-input img.number-type[src='images/icon-outbound-call.svg']");
	By lpNumberOptionImage 		= By.xpath(".//*[@class='selectize-dropdown-content']//span[contains(text(),'Local presence')]/preceding-sibling::img[@src='images/icon-outbound-local-presence.svg']");
	By selectedLocalPresencIcon	= By.cssSelector("#outboundNumberSelector .selectize-input img.number-type[src='images/icon-outbound-local-presence.svg']");
	By callDuration				= By.cssSelector("#status .timer.is-countdown");
	String outboundNumberOption	= ".//div[@id='outboundNumberSelector']//*[@class='selectize-dropdown-content']//span[contains(text(),'$$OutboundNumber$$')]";
	String dialPadBtn			= "#keypad button[data-id='$$Key$$']";
	//*******Dial Pad locators section ends here*******//

	//*******Direct Incoming Call Section Starts Here*******//
	By firstCallAcceptButton 	= By.className("accept");
	By declineCallButton 		= By.className("decline");
	By cancelCallButton			= By.cssSelector(".dialing-call button.cancel, .dialing-call button.cancel:disabled");
	public By hangUpButton 		= By.cssSelector(".hangup, .dialing-call .cancel");
	By enabledMuteBtn			= By.cssSelector(".mute:not(:disabled)");
	By muteButton 				= By.className("mute");
	By unmuteButton				= By.className("unmute");
	By holdButton 				= By.cssSelector(".ladda-button.hold");
	By activeCallHoldButton 	= By.cssSelector(".ladda-button.hold");
	//*******Direct Incoming Call Section Starts Here*******//
	
	//*******Transfer section starts here*******//
	By transferCallButton 		= By.className("transfer");
	By transferNumberTextBox 	= By.id("transfer-number");
	By transferNumberList 		= By.cssSelector(".tt-menu.tt-open .tt-suggestion.tt-selectable");
	By transferButtonModalWindow = By.cssSelector("[data-action='transfer-call']");
	By blanktransferNumberList 	= By.cssSelector(".tt-menu.tt-open div.empty-message");
	By transferWindowCloseIcon	= By.cssSelector("[id='transferCallModal'] button.close");
	//*******Transfer section starts here*******//

	//*******Conference buttons section starts here*******//
	public By onHoldButton 		= By.cssSelector(".held-calls");
	public By resumeHoldCallButton = By.cssSelector(".ladda-button.resume");
	By mergeHoldCallButton		= By.cssSelector(".ladda-button.merge");
	By conferenceButton 		= By.cssSelector(".conference-calls");
	By selectButton 			= By.cssSelector("button.ladda-button.select:not(.invisible)");
	By agentConferenceLeaveButton = By.cssSelector(".ladda-button.end[data-action='leave']");
	By participantsConferenceEndButton = By.cssSelector(".ladda-button.end[data-action='end']");
	//*******Conference section ends here*******//
	
	//*******Header incoming call starts here*******// 
	By acceptCallButtonHeader 	= By.cssSelector(".ladda-button.accept");
	By declineCallButtonHeader 	= By.cssSelector(".ladda-button.decline");
	By sendCallToVMHeader	    = By.cssSelector(".ladda-button.end-and-accept");
	By acceptButtonOnTop 		= By.cssSelector(".ladda-button.accept");
	By headerCallerName			= By.cssSelector(".incoming-call .headline  .caller");
	By headerCompanyName		= By.cssSelector(".incoming-call .headline  .company");
	By viewCallBtnHeader		= By.xpath(".//*[contains(@class, 'ladda-button') and contains(@class, 'accept')]//*[text()='View Call']");
	//*******Header incoming call ends here*******//
	
	By spinnerWheel 			= By.xpath("//*[contains(@id, 'spinner')]");
	By callerNumber 			= By.cssSelector("#foreground-call .crm-create .callerPhone, #next-call .crm-create .callerPhone");
	By callBackButton			= By.cssSelector(".crm-single .call[data-action='dial-phone'] img, div.crm-create[style='display: block;'] .call-button img, div.crm-multiple[style='display: block;'] .call-button img");

	//*******Dial Pad Functions Starts Here******//
	public String getOutboundNumberFromDropdown(WebDriver driver){
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, outboundselectedNum).substring(0, 13));
	}
	
	public void isOutboundDrowdownVisible(WebDriver driver){
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropwdownMenu);
	}
	
	public void verifyOutboundDrowdownNotVisible(WebDriver driver){
		waitUntilInvisible(driver, outboundNumDropwdown);
		assertTrue(isElementInvisible(driver, outboundNumDropwdownMenu, 5));
	}
	
	public String getOutboundNumberFromDropDown(WebDriver driver, String outboundNumber, String outboundNumberLabel) {
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropwdownMenu);
		String  outboundNumberOptionText = getElementsText(driver, By.xpath(outboundNumberOption.replace("$$OutboundNumber$$", outboundNumber)));
		clickElement(driver, outboundNumDropwdown);
		outboundNumberLabel = outboundNumberLabel == null ? "" : " (" + outboundNumberLabel + ")";
		assertEquals(outboundNumberOptionText, " " + outboundNumber + outboundNumberLabel);
		return outboundNumberOptionText;
	}
	
	public boolean isOutboundNumExistInDropdown(WebDriver driver, String outboundNumber){
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropwdownMenu);
		By  outboundNumberToSelcect = By.xpath(outboundNumberOption.replace("$$OutboundNumber$$", outboundNumber));
		boolean isNumberExit =isElementVisible(driver, outboundNumberToSelcect, 0);
		clickElement(driver, outboundNumDropwdown);
		return isNumberExit;
	}
	
	public boolean isLPOptionExistInDropdown(WebDriver driver){
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropwdownMenu);
		boolean isLpNumberExit =isElementVisible(driver, lpNumberOptionImage, 0);
		clickElement(driver, outboundNumDropwdown);
		return isLpNumberExit;
	}
	
	public void selectOutboundNumFromDropdown(WebDriver driver, String outboundNumber){
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropwdownMenu);
		By  outboundNumberToSelcect = By.xpath(outboundNumberOption.replace("$$OutboundNumber$$", outboundNumber));
		clickElement(driver, outboundNumberToSelcect);
	}
	
	public void selectOutboundNumUsingIndex(WebDriver driver, int index){
		waitUntilVisible(driver, outboundNumDropwdown);
		clickElement(driver, outboundNumDropwdown);
		waitUntilVisible(driver, outboundNumDropDownOpns);
		getElements(driver, outboundNumDropDownOpns).get(index).click();
		waitUntilInvisible(driver, outboundNumDropDownOpns);
	}
	
	public String getSelectedOutboundNumber(WebDriver driver){
		waitUntilVisible(driver, outboundNumDropwdown);
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, selectedOutboundNum));
	}
	
	public void verifyAdditionalNumIconSelected(WebDriver driver){
		waitUntilVisible(driver, selectedAdditionNumIcon);
	}
	
	public void verifyOutboundNumIconSelected(WebDriver driver){
		waitUntilVisible(driver, selectedOutboundNumIcon);
	}
	
	public void verifyLocalPresenceNumIconSelected(WebDriver driver){
		waitUntilVisible(driver, selectedLocalPresencIcon);
	}
	
	public boolean isLocalPresenceSelected(WebDriver driver) {
		return (getSelectedOutboundNumber(driver).equals("Localpresence") && isElementVisible(driver, selectedLocalPresencIcon, 0));
	}
	
	public boolean verifyLocalPresenctOptionNotPresent(WebDriver driver){
		return isOutboundNumExistInDropdown(driver, "Local presence") && isLPOptionExistInDropdown(driver);
	}
	
	public void openDialPad(WebDriver driver) {
		System.out.println("Opening Dial Pad");
		clickDialPadIcon(driver);
	}

	public void clickDialPadIcon(WebDriver driver){
		System.out.println("Clicking on Dialpad icon");
		waitUntilVisible(driver, dialPadImage);
		clickElement(driver, dialPadImage);
	}
	
	public void isDialPadButtonInvisible(WebDriver driver){
		waitUntilInvisible(driver, dialPadImage);
	}
	
	public boolean isDialPadButtonvisible(WebDriver driver){
		return isElementVisible(driver, dialPadImage, 3);
	}

	public void appentTextInDialpad(WebDriver driver, String text){
		idleWait(1);
		appendText(driver, dialPadTextBox, text);
	}
	
	public void enterNumberInDialpad(WebDriver driver, String number) {
		System.out.println("Entering number "+ number  +" in dialpad text box");
        int counter=0;
        	do{
                if(isElementVisible(driver, dialPadTextBox,0)){
                enterText(driver, dialPadTextBox, number);
                }
                counter++;
            }while(counter<5 && !(isElementVisible(driver, dialPadTextBox,0) && getAttribue(driver, dialPadTextBox, ElementAttributes.value).equals(number)));	
        idleWait(1);
    }
	
	public void enterNumberEachDigit(WebDriver driver, String number) {
		for(int i=0; i< number.length(); i++){
			clickElement(driver, By.cssSelector(dialPadBtn.replace("$$Key$$", Character.toString(number.charAt(i)))));
			idleWait(1);
		}
	}
	
	public void clickDialButton(WebDriver driver) {
		if(isElementVisible(driver, dialPadCallButton, 0))
		{
			System.out.println("clicking dial button");
			clickElement(driver, dialPadCallButton);	
		}
	}
	
	public void clickSMSButton(WebDriver driver) {
		if(isElementVisible(driver, dialPadSMSButton, 0))
		{
			System.out.println("clicking SMS button");
			clickElement(driver, dialPadSMSButton);	
		}
	}

	public void isLoadingIconVisible(WebDriver driver) {
		waitUntilVisible(driver, spinnerWheel);
	}
	
	public void softphoneAgentCall(WebDriver driver, String number) {
		number = number.replace(" ", "");
		System.out.println("making a call to number " + number );
		int loopCount = 0;
		do {
		waitUntilInvisible(driver, spinnerWheel);
		enterNumberAndDial(driver, number);
		loopCount++;
		System.out.println(isElementVisible(driver, cancelCallButton, 5));
		} while (!(isElementVisible(driver, cancelCallButton, 5)|| isElementVisible(driver, hangUpButton, 0)) && loopCount <= 3);
	}
	
	public void enterNumberAndDial(WebDriver driver, String number){
		number = number.replace(" ", "");
		openDialPad(driver);
		enterNumberInDialpad(driver, number);
		clickDialButton(driver);
	}
	
	public void softphoneAgentSMS(WebDriver driver, String number) {
		number = number.replace(" ", "");
		System.out.println("making a call to number " + number );
		int loopCount = 0;
		do {waitUntilInvisible(driver, spinnerWheel);			
			openDialPad(driver);
			enterNumberInDialpad(driver, number);
			clickSMSButton(driver);
			loopCount++;
		} while (!isElementVisible(driver, callBackButton, 3) && loopCount <= 3);
	}
	//*******Dial Pad Functions end Here******//

	//*******Calling Functions Starts here******//
	public void isHangUpButtonVisible(WebDriver driver) {
		System.out.println("verifying hangup button is visible");
		Assert.assertTrue(findElement(driver, hangUpButton).isDisplayed(), "Hangup button is not visible");
	}
	
	public void isAcceptCallButtonVisible(WebDriver driver){
		System.out.println("vrifyfing accept call button is visible");
		waitUntilVisible(driver, firstCallAcceptButton);
	}
	
	public void clickAcceptCallButton(WebDriver driver){
		System.out.println("accepting call");
		waitUntilVisible(driver, firstCallAcceptButton);
		clickElement(driver, firstCallAcceptButton);
	}
	
	public void declineCall(WebDriver driver){
		System.out.println("declining call");
		waitUntilVisible(driver, declineCallButton);
		clickElement(driver, declineCallButton);
	}
	
	public Boolean isDeclineButtonVisible(WebDriver driver){
		return isElementVisible(driver, declineCallButton, 5);
	}
	
	public boolean isCallHoldButtonVisible(WebDriver driver) {
		return isElementVisible(driver, activeCallHoldButton, 5);
	}
	
	public void verifyCallHoldButtonInvisible(WebDriver driver) {
		waitUntilInvisible(driver, activeCallHoldButton);
	}
	
	public void verifyDeclineButtonIsInvisible(WebDriver driver) {
		System.out.println("Verifying that decline button is invisible");
		waitUntilInvisible(driver, declineCallButton);
	}

	public boolean isActiveCallHoldButtonEnable(WebDriver driver) {
		System.out.println("getting the stateof call hold button is active");
		return findElement(driver, activeCallHoldButton).isEnabled();
	}

	public void clickHoldButton(WebDriver driver){
		System.out.println("clicking hold button");
		waitUntilVisible(driver, holdButton);
		clickElement(driver, holdButton);
		waitUntilVisible(driver, onHoldButton);
	}
	
	public Boolean isMuteButtonEnables(WebDriver driver){
		System.out.println("verifying if mute button is enabled");
		waitUntilVisible(driver, muteButton);
		return(findElement(driver, muteButton).isEnabled());
	}
	
	public void clickMuteButton(WebDriver driver){
		waitUntilVisible(driver, enabledMuteBtn);
		clickElement(driver, enabledMuteBtn);
	}
	
	public void isUnMuteButtonVisible(WebDriver driver){
		waitUntilInvisible(driver, muteButton);
		waitUntilVisible(driver, unmuteButton);
	}
	
	public void ClickUnMuteButton(WebDriver driver){
		waitUntilVisible(driver, unmuteButton);
		clickElement(driver, unmuteButton);
		waitUntilVisible(driver, muteButton);
		waitUntilInvisible(driver, unmuteButton);
	}
	
	public void isCallOnHold(WebDriver driver) {
		System.out.println("verifying on hold button is visible");
		Assert.assertEquals(true, findElement(driver, onHoldButton).isDisplayed(),"on hold button is not visible");
	}

	public void pickupIncomingCall(WebDriver driver) {
		System.out.println("picking up incoming call");
		clickAcceptCallButton(driver);
		waitUntilVisible(driver, hangUpButton);
		Assert.assertEquals(findElement(driver, hangUpButton).isEnabled(), true);
	}
	
	public void hangupActiveCall(WebDriver driver) {
		System.out.println("hanging up active call");
		waitUntilVisible(driver, hangUpButton);
		clickElement(driver, hangUpButton);
		findElement(driver, callBackButton);
	}

	public void hangupIfInActiveCall(WebDriver driver){
		System.out.println("hanging up if user is in active call");
		try{
			if(isElementVisible(driver, hangUpButton, 1)){
				getWebelementIfExist(driver, hangUpButton).click();
				findElement(driver, callBackButton);
			}
		}catch (Exception e){
			System.out.println("hangup button is not present");
		}
	}
	
	public void clickCallBackButton(WebDriver driver){
		waitUntilVisible(driver, callBackButton);
		clickElement(driver, callBackButton);
	}
	
	public String getCallDuration(WebDriver driver) {
		return getElementsText(driver, callDuration);
	}
	
	//*******Calling Functions ends here******//

	//*******Call Transfer functions Starts here*******//
	public boolean isTransferButtonEnable(WebDriver driver) {
		System.out.println("getting the state of transfer button");
		return findElement(driver, transferCallButton).isEnabled();
	}
	
	public void clickTranferButton(WebDriver driver){
		System.out.println("clicking on transfer button");
		clickElement(driver, transferCallButton);
	}

	public void enterTranferToText(WebDriver driver, String tranferToText){
		System.out.println("enter number or name to transfer call to");
		enterText(driver, transferNumberTextBox, tranferToText);
		clickEnter(driver, transferNumberTextBox);
	}

	public List<WebElement> callTranferSuggestionlist(WebDriver driver){
		System.out.println("getting tranfer to suggestion list");
		return getElements(driver, transferNumberList);
	}

	public void clickTranferButtonModalWindow(WebDriver driver){
		System.out.println("clicking on tranfer button to transfer the call");
		clickElement(driver, transferButtonModalWindow);
	}
	
	public void verifyTransferListBlank(WebDriver driver){
		System.out.println("Verifying that transfer to number list is blank");
		waitUntilVisible(driver, blanktransferNumberList);
		clickElement(driver, transferWindowCloseIcon);
		waitUntilInvisible(driver, transferWindowCloseIcon);
	}

	public void transferToNumber(WebDriver driver, String tranferToText){
		System.out.println("transferring call to " + tranferToText);
		clickTranferButton(driver);
		enterTranferToText(driver, tranferToText);
		callTranferSuggestionlist(driver).get(0).click();
		clickTranferButtonModalWindow(driver);
	}
	//*******Call Transfer functions Ends here*******//
	
	//*******Conference Functions Starts here*******//	
	public void clickOnHoldButton(WebDriver driver) {
		System.out.println("clicking on on hold button");
		waitUntilVisible(driver, onHoldButton);
		clickElement(driver, onHoldButton);
	}
	
	public void clickOnHoldButtonAndVerifyButtons(WebDriver driver) {
		System.out.println("verifying resume and  merge button are enabled after call is on hold");
		waitUntilVisible(driver, onHoldButton);
		clickOnHoldButton(driver);
		Assert.assertEquals(true, findElement(driver, mergeHoldCallButton).isEnabled());
		Assert.assertEquals(true, findElement(driver, resumeHoldCallButton).isEnabled());
	}	

	public void clickResumeButton(WebDriver driver) {
		System.out.println("click on call resume button");
		clickElement(driver, resumeHoldCallButton);
		waitUntilInvisible(driver, resumeHoldCallButton);
		waitUntilVisible(driver, hangUpButton);
	}
	
	public void isConferenceButtonDisplayed(WebDriver driver) {
		assertTrue(findElement(driver, conferenceButton).isDisplayed());
		assertFalse(isActiveCallHoldButtonEnable(driver));
		assertFalse(isTransferButtonEnable(driver));
		assertTrue(findElement(driver, hangUpButton).isEnabled());
		waitUntilVisible(driver, CallScreenPage.numberOfCallers);
	}
	
	public void verifyConferenceBtnDisappeared(WebDriver driver) {
		waitUntilInvisible(driver, conferenceButton);
		assertTrue(isActiveCallHoldButtonEnable(driver));
		assertTrue(isTransferButtonEnable(driver));
		assertTrue(findElement(driver, hangUpButton).isEnabled());
		waitUntilInvisible(driver, CallScreenPage.numberOfCallers);
	}

	public void isConferenceButtonInvisible(WebDriver driver){
		System.out.println("verifyig that conference button is invisible");
		waitUntilInvisible(driver, conferenceButton);
	} 
	
	public void clickConferenceButton(WebDriver driver) {
		System.out.println("clicking on conference button");
		clickElement(driver, conferenceButton);
	}
	
	public boolean isAgentLeaveButtonEnabled(WebDriver driver) {
		System.out.println("getting agent leave button's enabled state");
		return findElement(driver, agentConferenceLeaveButton).isEnabled();
	}
	
	public void clickAgentConferenceLeaveButton(WebDriver driver) {
		System.out.println("click on leave button on conference window");
		clickElement(driver, agentConferenceLeaveButton);
	}
	
	public List<WebElement> getParticipantConferenceEndButtons(WebDriver driver) {
		System.out.println("getting participant conference end buttons");
		return getElements(driver, participantsConferenceEndButton);
	}

	public List<WebElement> availableSelectButton(WebDriver driver) {
		System.out.println("getting the list of select buttons");
		List<WebElement> selectButtons = getElements(driver, selectButton);
		return selectButtons;
	}
	
	public void isConferenceWindowButtonsEnabled(WebDriver driver) {
		System.out.println("Opening Conference window and verifying buttons");
		waitUntilVisible(driver, conferenceButton);
		clickConferenceButton(driver);
		Assert.assertTrue(getParticipantConferenceEndButtons(driver).size() > 0, "There are no end caller buttons");
		Assert.assertTrue(isAgentLeaveButtonEnabled(driver), "Agent leave button is not there");
	}

	public void closeConferenceWindow(WebDriver driver) {
		System.out.println("closing conference window");
		clickConferenceButton(driver);
		Assert.assertTrue(getWebelementIfExist(driver, agentConferenceLeaveButton) == null);
	}

	public void clickMergeButton(WebDriver driver) {
		System.out.println("Merging the call");
		waitUntilVisible(driver, mergeHoldCallButton);
		clickElement(driver, mergeHoldCallButton);
		if(!isElementVisible(driver, conferenceButton, 5)){
			clickOnHoldButton(driver);
			clickOnHoldButton(driver);
			clickElement(driver, mergeHoldCallButton);
		}
		waitUntilVisible(driver, conferenceButton);
	}
	
	public void clickMergeWithoutLoop(WebDriver driver) {
		System.out.println("Merging the call");
		waitUntilVisible(driver, mergeHoldCallButton);
		clickElement(driver, mergeHoldCallButton);
		waitUntilInvisible(driver, resumeHoldCallButton);
	}

	public void endParticipantCallFromConference(WebDriver driver, int callerNumber) {
		System.out.println("end participant from conference window");
		List<WebElement> elements = getParticipantConferenceEndButtons(driver);
		System.out.println("end button size is = "+elements.size());
		clickElement(driver, elements.get(callerNumber-1));
	}
	
	public void mergeVerifyAndCloseConferenceWindow(WebDriver driver){
		clickOnHoldButtonAndVerifyButtons(driver);
		clickMergeButton(driver);
		isConferenceButtonDisplayed(driver);
		isConferenceWindowButtonsEnabled(driver); 
		assertEquals(getParticipantConferenceEndButtons(driver).size(), 2);
		assertEquals(availableSelectButton(driver).size(), 1);
		System.out.println("closing the conference window");
		closeConferenceWindow(driver);
	}

	public void mergeOnHoldCall(WebDriver driver, int holdCallNumber) {
		System.out.println("merge calls");
		clickElement(driver, onHoldButton);
		if(holdCallNumber<=0){
			System.out.println("Passed hold call number is not right");
			return;
		}
		List<WebElement> mergeButtons = new ArrayList<WebElement>();
		mergeButtons = getElements(driver, mergeHoldCallButton);
		clickElement(driver, mergeButtons.get(holdCallNumber-1));		
		idleWait(3);
		isConferenceButtonDisplayed(driver);
	}

	public void putActiveCallOnHold(WebDriver driver) {
		System.out.println("putting active call on hold");
		waitUntilVisible(driver, activeCallHoldButton);
		clickElement(driver, activeCallHoldButton);
		waitUntilVisible(driver, onHoldButton);
	}

	public void resumeHoldCall(WebDriver driver, int holdCallNumber) {
		System.out.println("resume call on hold");
		clickElement(driver, onHoldButton);
		if(holdCallNumber<=0){
			System.out.println("Passed hold call number is not right");
			return;
		}
		List<WebElement> resumeButtons = new ArrayList<WebElement>();
		resumeButtons = getElements(driver, resumeHoldCallButton);
		clickElement(driver, resumeButtons.get(holdCallNumber-1));
		isHangUpButtonVisible(driver);
	}
	
	public void hangupIfHoldCall(WebDriver driver) {
		System.out.println("hangup up if there is a call on hold");
		if (isElementVisible(driver, onHoldButton, 2)) {
			clickElement(driver, onHoldButton);
			clickResumeButton(driver);
			isHangUpButtonVisible(driver);
			hangupActiveCall(driver);
		}
	}
	
	public void onHoldButtonIsInvisible(WebDriver driver){
		waitUntilInvisible(driver, onHoldButton);
	}
	//*******Conference Functions Ends here*******//
	
	//*******Additional Calls On Header functions starts here*******//
	public void isAdditionalCallAcceptBtnEnable(WebDriver driver){
		Assert.assertEquals(findElement(driver, acceptCallButtonHeader).isEnabled(), true);
	}
	
	public void isAdditionalCallDeclineBtnEnable(WebDriver driver){
		Assert.assertEquals(findElement(driver, declineCallButtonHeader).isEnabled(), true);
	}
	
	public void isAdditionalCallSendToVMBtnEnable(WebDriver driver){
		Assert.assertEquals(findElement(driver, sendCallToVMHeader).isEnabled(), true);
	}
	
	public void isAdditionalCallSendToVMInvisible(WebDriver driver){
		System.out.println("verifying that reject button for additional income call is visible");
		waitUntilInvisible(driver, sendCallToVMHeader);
	}
	
	public void verifyAdditionalCall(WebDriver driver){
		System.out.println("Verifying that additional call showing on softphone");
		waitUntilVisible(driver, declineCallButtonHeader);
		waitUntilVisible(driver, acceptCallButtonHeader);
		waitUntilInvisible(driver, SoftPhoneSettingsPage.activeSettingIcon);
		waitUntilVisible(driver, CallScreenPage.userImageBusy);
	}
	
	public void verifyAdditionalCallIsInvisible(WebDriver driver){
		System.out.println("Verifying that additional call not showing on softphone");
		waitUntilInvisible(driver, declineCallButtonHeader);
		waitUntilInvisible(driver, acceptCallButtonHeader);
	}
	
	public void viewHeaderButtonVisible(WebDriver driver){
		waitUntilVisible(driver, viewCallBtnHeader);
	}
	
	public void viewHeaderButtonInvisible(WebDriver driver){
		waitUntilInvisible(driver, viewCallBtnHeader);
	}
	
	public void verifyAdditionCallUserDetail(WebDriver driver, String callerName, String companyName){
		System.out.println("Verifying that caller name and company name is correct");
		assertEquals(getElementsText(driver, headerCallerName), callerName);
		assertEquals(getElementsText(driver, headerCompanyName), companyName);
	}

	public void acceptCallFromTop(WebDriver driver){
		System.out.println("Accepting call during editing Object");
		waitUntilVisible(driver, acceptButtonOnTop);
		clickElement(driver, acceptButtonOnTop);
	}
	
	public void pickupIncomingCallFromHeaderCallNotification(WebDriver driver) {
		System.out.println("picking up incoming call from the header");
		isAdditionalCallAcceptBtnEnable(driver);
		isAdditionalCallDeclineBtnEnable(driver);
		idleWait(2);
		clickElement(driver, acceptCallButtonHeader);
		waitUntilInvisible(driver, declineCallButtonHeader);
		waitUntilVisible(driver, hangUpButton);
		Assert.assertEquals(findElement(driver, hangUpButton).isEnabled(),true);
	}

	public void declineAdditionalCall(WebDriver driver){
		System.out.println("declining additional call");
		waitUntilVisible(driver, declineCallButtonHeader);
		clickElement(driver, declineCallButtonHeader);
	}
	//*******Additional Calls On Header functions starts here*******//

	public void isCallBackButtonVisible(WebDriver driver) {
		System.out.println("verifying that call back button is visible");
		waitUntilVisible(driver, callBackButton);
	}
	
	public boolean isCallBackButtonVisible(WebDriver driver, int timeOut) {
		System.out.println("verifying that call back button is visible");
		return isElementVisible(driver, callBackButton, timeOut);
	}
	
}
