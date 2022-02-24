package support.source.admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class AccountCallRecordingTab extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	String callRecordingUnlockImageHTML			= "<svg height=\"24\" width=\"24\" viewBox=\"0 0 40 40\"><circle fill=\"#003A7F\" cx=\"20\" cy=\"20\" r=\"20\"></circle><path fill=\"#FFFFFF\" d=\"M25.5,16.7h-9.6v-2.5c0.1-2.6,1.6-4.4,3.8-4.3l0.5,0c1.3,0,1.8,0.4,2.7,1.3c0.5,0.8,0.2,0.4,0.6,0.9c0.3,0.5,1,0.4,1.3,0.2c0.3-0.2,1-0.5,0.7-1.1c-0.6-1.5-1.4-2.1-2.9-2.7C21.9,8.1,21.1,8,20.2,8h-0.5c-0.8,0-1.6,0.2-2.4,0.5c-1.5,0.6-2.7,1.8-3.3,3.3c-0.3,0.8-0.5,1.6-0.5,2.4v2.5c0,0,0,0.1,0,0.1c-1.5,0.4-2.6,1.8-2.6,3.3v9.2c0,0.4,0.3,0.7,0.7,0.7l0,0h16.6c0.4,0,0.7-0.3,0.7-0.7c0,0,0,0,0,0v-9.2C29,18.2,27.4,16.7,25.5,16.7z\"></path></svg>";
	String callRecordingLockImageHTMl			= "<svg height=\"24\" width=\"24\" viewBox=\"0 0 40 40\"><circle fill=\"#003A7F\" cx=\"20\" cy=\"20\" r=\"20\"></circle><path fill=\"#FFFFFF\" d=\"M26.4,16.8c0,0,0-0.1,0-0.1v-2.5c0-0.8-0.2-1.6-0.5-2.4c-0.3-0.7-0.8-1.4-1.3-2c-0.6-0.6-1.3-1-2-1.3 C21.8,8.1,21.1,8,20.2,8h-0.5c-0.8,0-1.6,0.2-2.4,0.5c-0.7,0.3-1.4,0.8-2,1.3c-0.6,0.6-1,1.3-1.3,2c-0.3,0.8-0.5,1.6-0.5,2.4v2.5 c0,0,0,0.1,0,0.1c-1.5,0.4-2.5,1.7-2.5,3.3v9.2c0,0.4,0.3,0.7,0.7,0.7l0,0h16.6c0.4,0,0.7-0.3,0.7-0.7c0,0,0,0,0,0v-9.2 C29,18.5,27.9,17.2,26.4,16.8z M15.9,16.7v-2.5c0-0.6,0.1-1.3,0.4-1.9c0.2-0.6,0.6-1.1,1-1.5c0.9-0.9,1.9-1,2.4-1h0.5 c0.5,0,1.5,0,2.4,1c0.4,0.4,0.8,1,1,1.5c0.3,0.6,0.4,1.2,0.4,1.9v2.5L15.9,16.7z\"></path></svg>";
	
	By overviewTab = By.cssSelector("[data-testid='overview-tab']");
	By advancedTab = By.xpath(".//h6[text()='Advanced']");
	By unitedStatesTab	= By.cssSelector("[data-testid='united states-tab']");
	By intertaionalTab	= By.cssSelector("[data-testid='international-tab']");
	By changeLogTab 	= By.cssSelector("[data-testid='changelog-tab']");
	
	By callRecordingTabLink = By.cssSelector("[data-tab='call-recording']");
	
	//By callRecordingTabHeader = By.xpath("//h2[text()='Call Recording']");
	By callRecordingOverviewHeading = By.xpath("//span[contains(text() , 'Enable the creation of call recordings')]");
	
	By callRecordOverrideDropDownDisabled 	= By.cssSelector("#call-types-select:disabled");
	By callRecordingOverrideOptionsDropDown	= By.xpath(".//input[@id='call-types-select']/preceding-sibling::div");
	
	By callRecordingLockImg	= By.xpath(".//*[@data-testid='call-types-input']/../following-sibling::*//*[local-name()='svg']");
	By callRecordingLockDiv	= By.xpath(".//*[@data-testid='call-types-input']/../following-sibling::*/div");
	
	By confirmationMessage = By.cssSelector(".MuiPaper-root span[color='primary']");
	By lockConfirmBtn = By.xpath(".//button[text()='Lock' or text()='Unlock']");
	
	By callRecordingAgreementCheckBox = By.xpath(".//input[@value='I AGREE TO THESE TERMS']/..");
	By callRecordingAgreementButton = By.xpath(".//button[text()='Yes']");
	By callRecordingAgreementHeading = By.cssSelector(".MuiDialog-container h4");

	By dismissRecordWarningLink = By.cssSelector(".//span[text()='Dismiss this warning permanently']");	
	
	By inBoundCallRecordingCheckbox			= By.xpath(".//*[span[text()='Would you like to use an inbound call recording announcement?']]//input[@type='checkbox']");
	By inBoundCallRecordingToggleBtn		= By.xpath(".//*[span[text()='Would you like to use an inbound call recording announcement?']]//span[@class='MuiSwitch-root']");
	
	By outBoundCallRecordingCheckbox		= By.xpath(".//*[span[text()='Would you like to use an outbound call recording announcement?']]//input[@type='checkbox']");
	By outBoundCallRecordingToggleBtn		= By.xpath(".//*[span[text()='Would you like to use an outbound call recording announcement?']]//span[@class='MuiSwitch-root']");
	
	
	By restrictCallRecordingLabel 			= By.xpath(".//p[text()='Which users should be allowed to download recordings?']");	
	
	By restrictCallRecordingInputBox		= By.xpath(".//input[@id='status-enabled-select']/preceding-sibling::div");
	By dropDownOptoins						= By.cssSelector("[role=option]");
	
	By callRecordingPauseInputBox			= By.xpath(".//input[@id='pause-enabled-select']/preceding-sibling::div");
	
	By displayRecordingStatusCheckBox 		 = By.xpath(".//*[span[text()='Would you like to enable the display of the recording status on the Softphone?']]//input[@type='checkbox']");
	By displayRecordingStatusToggleBtn		 = By.xpath(".//*[span[text()='Would you like to enable the display of the recording status on the Softphone?']]//span[@class='MuiSwitch-root']");
	
	By singleAgentOnlyRecordingCheckbox		= By.xpath(".//*[span[text()='Would you like to enable single-channel Agent-only call recordings?']]//input[@type='checkbox']");
	By singleAgentOnlyRecordingToggleBtn	= By.xpath(".//*[span[text()='Would you like to enable single-channel Agent-only call recordings?']]//span[@class='MuiSwitch-root']");
	By singleAgentOnlyRecordingContainer	= By.xpath(".//*[span[text()='Would you like to enable single-channel Agent-only call recordings?']]//*[contains(@class, 'MuiButtonBase-root')]");
	
	By callRecordingVisibilityDropDown		= By.xpath(".//input[@id='record-visibility-select']/preceding-sibling::div");
	
	By allowGranularControlCheckBox 	  	= By.xpath(".//*[span[text()='Would you like to enable granular controls for call recordings in the US?']]//input[@type='checkbox']");
	By allowGranularControlToggleBtn 		= By.xpath(".//*[span[text()='Would you like to enable granular controls for call recordings in the US?']]/span[@class='MuiSwitch-root']");
	By callRecordingByStateContainer		= By.cssSelector("[data-testid='ringdna-table']");
	By confirmConsentButton 				= By.xpath(".//button[text()='Yes, enable']");
	String recordingDropDownOption			= "[role=option][data-value='$$RecordOption$$']"; 
	
	String areaCodeDropDownLocator 			= "(.//*[div[text()='$$Area$$']]//div[contains(@class, 'ringdna-table-cell')])//div[contains(@class, 'MuiSelect-root')]";
	String areadCodeConsentImageLocator 	= ".//*[div[text()='$$Area$$']]//h5[text()='All Party']";
	
	String areaCodePlayOutboundLocator 		= ".//*[div[text()='$$Area$$']]//span[contains(@class, 'MuiSwitch-root')]//input";
	
	By countryGranularCheckBox     			= By.xpath(".//*[span[text()='To record calls to and from international countries, please enable International Call Recording controls. When enabled, you can designate the type of recordings generated and countries requiring an outbound call recording announcement to be played. When International call recordings are disabled, ringDNA will not generate any recordings for calls to and from international countries.']]//input[@type='checkbox']");
	By countryGranularControlOnToggleBtn  	= By.xpath(".//*[span[text()='To record calls to and from international countries, please enable International Call Recording controls. When enabled, you can designate the type of recordings generated and countries requiring an outbound call recording announcement to be played. When International call recordings are disabled, ringDNA will not generate any recordings for calls to and from international countries.']]/span[@class='MuiSwitch-root']");
	By acceptButton							= By.xpath(".//button[text()='Accept']");
	By countryGranularContainer 			= By.xpath(".//*[span[text()='To record calls to and from international countries, please enable International Call Recording controls. When enabled, you can designate the type of recordings generated and countries requiring an outbound call recording announcement to be played. When International call recordings are disabled, ringDNA will not generate any recordings for calls to and from international countries.']]/div");
	By internationPageAllToggleBtns			= By.xpath(".//*[span[text()='To record calls to and from international countries, please enable International Call Recording controls. When enabled, you can designate the type of recordings generated and countries requiring an outbound call recording announcement to be played. When International call recordings are disabled, ringDNA will not generate any recordings for calls to and from international countries.']]//*[contains(@class, 'MuiButtonBase-root')]");
	
	String continentLink 					= ".//*[contains(@class, 'rdna-accordion-row')]//h6[text()='$$Continent$$']";
	String continentExpandedArea 			= ".//*[@class='rdna-accordion-row open overflow'][//h6[text()='$$Continent$$']]";
	
	By callRecordingTabSaveButton 			= By.cssSelector("[name='save-call-recording']");
	By saveConfiramtionMessage 				= By.className("toast-message");
	
	By callRecordChangeLogRows	    		= By.xpath(".//h6[text()='Changelog' and @color='primary']/following-sibling::div//div[@data-testid='ringdna-table-row']");
	
	String recordCallsConsentState	= ".//*[text()='$state$']/parent::tr//img[@class='consent']/ancestor::tr//input[@type='checkbox' and not(@class)]";
	By warningMsg					= By.cssSelector(".bootbox-body");
	
	//support disabled buttons
	By disabledEnableCallRecord		= By.xpath("//input[contains(@class,'recordingFeaturesEnabled')]//parent::div[contains(@class, 'toggle') and @disabled='disabled']");
	By disabledRestrictRecord		= By.xpath("//input[contains(@class,'restrictRecordingDownload')]//parent::div[contains(@class, 'toggle') and @disabled='disabled']");
	By disabledInboundRecordAnounce	= By.xpath("//input[contains(@class,'inboundCallRecordingAnnouncement')]//parent::div[contains(@class, 'toggle') and @disabled='disabled']");
	By disabledRecordStatus			= By.xpath("//input[contains(@class,'recordingStatusEnabled')]//parent::div[contains(@class, 'toggle') and @disabled='disabled']");
	By disabledGranularControl		= By.xpath("//input[contains(@class,'callRecordingByState ')]//parent::div[contains(@class, 'toggle') and @disabled='disabled']");
	By enabledAllowPauseResume		= By.xpath("//input[contains(@class,'recordingPauseVisible')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	
	//not disabled recording buttons
	By enabledEnableCallRecord		= By.xpath("//input[contains(@class,'recordingFeaturesEnabled')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	By enabledRestrictRecord		= By.xpath("//input[contains(@class,'restrictRecordingDownload')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	By enabledInboundRecordAnounce	= By.xpath("//input[contains(@class,'inboundCallRecordingAnnouncement')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	By enabledRecordStatus			= By.xpath("//input[contains(@class,'recordingStatusEnabled')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	By enabledGranularControl		= By.xpath("//input[contains(@class,'callRecordingByState ')]//parent::div[contains(@class, 'toggle') and not(@disabled='disabled')]");
	
	By loaderIcon					= By.cssSelector("[data-testid='loader'");
	
	public enum AreaRecordOptions{
		NoRecording("No Recording"),
		AllParties("Record All Parties"),
		AgentOnly("Record Agent Only"),
		NoConsent("No Consent");
		
		private String displayName;
		
		AreaRecordOptions(String displayName ) {
			this.displayName = displayName;
		}
		
		public String displayName() { return displayName; }
	}
	
	
	public void verifyChangelogs(WebDriver driver, String date, String user, String action, int index) {
		waitUntilVisible(driver, callRecordChangeLogRows);
		scrollToElement(driver, callRecordChangeLogRows);
		WebElement firstLine = getElements(driver, callRecordChangeLogRows).get(index);
		assertEquals(firstLine.findElements(By.xpath("div")).get(1).getText(), user);
		assertEquals(firstLine.findElements(By.xpath("div")).get(2).getText(), action);
		
		String actualDatetime = firstLine.findElements(By.xpath("div")).get(0).getText();
		String dateFormat = "MM/dd/yyyy, hh:mm a";
		Date startDate = HelperFunctions.getDateTimeInDateFormat(date, dateFormat);
		Date endDate = HelperFunctions.getDateTimeInDateFormat(actualDatetime, dateFormat);

		// finding diff in minutes and updating new time
		int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(startDate, endDate, dateFormat);
		assertTrue(diffInMinutes >=0 && diffInMinutes <=2);
	}
	
	public void swtichToOverviewTab(WebDriver driver) {
		clickElement(driver, overviewTab);
	}
	
	public void swtichToAdvancedTab(WebDriver driver) {
		clickElement(driver, advancedTab);
	}
	
	public void swtichToUnitedStateTab(WebDriver driver) {
		clickElement(driver, unitedStatesTab);
		waitUntilInvisible(driver, loaderIcon);
	}
	
	public void swtichToInternationalTab(WebDriver driver) {
		clickElement(driver, intertaionalTab);
		waitUntilInvisible(driver, loaderIcon);
	}
	
	public void swtichToChangeLogTab(WebDriver driver) {
		clickElement(driver, changeLogTab);
		waitUntilInvisible(driver, loaderIcon);
	}
	
	/**
	 * @param driver
	 * is change log tab visible
	 */
	public boolean isChangeLogTabVisible(WebDriver driver) {
		return isElementVisible(driver, changeLogTab, 6);
	}
	
	public void verifySupportDisabledSetttings(WebDriver driver){
		waitUntilVisible(driver, disabledRestrictRecord);
		waitUntilVisible(driver, disabledInboundRecordAnounce);
		waitUntilVisible(driver, callRecordOverrideDropDownDisabled);
		waitUntilVisible(driver, disabledRecordStatus);
		waitUntilVisible(driver, disabledGranularControl);
		//waitUntilVisible(driver, enabledAllowPauseResume);
	}
	
	public static enum CallRecordingOverrideOptions{
		All,
		Inbound,
		Outbound,
		None
	}
	
	public static enum RecordingVisibilityTypeOptions{
		All,
		AgentAndTeamListenIn
	}
	
	public void openCallRecordingTab(WebDriver driver){
		if(!isElementVisible(driver, callRecordingTabLink, 5)) {
			dashboard.clickHomeBars(driver);
			dashboard.clickAccountsLink(driver);
		}
		waitUntilVisible(driver, callRecordingTabLink);
		clickElement(driver, callRecordingTabLink);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, callRecordingOverviewHeading);
	}
	
	public void lockRecordingSetting(WebDriver driver) {
		String callRecordingState = getElementsText(driver, callRecordingOverrideOptionsDropDown);
		String expectedConfirmMessage = null;
		if(getAttribue(driver, callRecordingLockImg, ElementAttributes.outerHTML).equals(callRecordingUnlockImageHTML)) {
			clickElement(driver, callRecordingLockImg);
			if(!callRecordingState.equals(CallRecordingOverrideOptions.None.toString())) {
				String forCallType = null;
				forCallType = callRecordingState + " ";
				expectedConfirmMessage = "Call Recording will be turned ON for all " + forCallType + "calls and cannot be modified by teams or users.";
			}else {
				 expectedConfirmMessage = "Call Recording will be turned OFF for all None calls and cannot be modified by teams or users.";
			}
			waitUntilVisible(driver, confirmationMessage);
			assertEquals(getElementsText(driver, confirmationMessage), expectedConfirmMessage);
			clickElement(driver, lockConfirmBtn);
			waitUntilInvisible(driver, lockConfirmBtn);
			waitUntilInvisible(driver, backGroundFade);
		}
	}

	public void unlockRecordingSetting(WebDriver driver) {
		String callRecordingState = getElementsText(driver, callRecordingOverrideOptionsDropDown);
		String expectedConfirmMessage = null;
		if(getAttribue(driver, callRecordingLockImg, ElementAttributes.outerHTML).equals(callRecordingLockImageHTMl)) {
			clickElement(driver, callRecordingLockImg);
			if(!callRecordingState.equals(CallRecordingOverrideOptions.None.toString())) {
				String forCallType = null;
				forCallType = callRecordingState + " ";
				expectedConfirmMessage = "Teams and users will be able to set their own call recording preferences. By default, all teams and users will have Call Recording turned ON for all " + forCallType + "calls, but teams and users can set new preferences as desired.";
			}
			else {
				expectedConfirmMessage = "Teams and users will be able to set their own call recording preferences. By default, all teams and users will have Call Recording turned OFF for all None calls, but teams and users can set new preferences as desired.";
			}
			waitUntilVisible(driver, confirmationMessage);
			assertEquals(getElementsText(driver, confirmationMessage), expectedConfirmMessage);
			clickElement(driver, lockConfirmBtn);
			waitUntilInvisible(driver, lockConfirmBtn);
			if (callRecordingState.equals(CallRecordingOverrideOptions.None.toString())) {
				acceeptCallRecordingAgreement(driver);
			}
			waitUntilInvisible(driver, backGroundFade);
		}
	}
	
	public void verifyRecordingUnlockToolTip(WebDriver driver) {
		hoverElement(driver, callRecordingLockDiv);
		waitUntilTextPresent(driver, By.id(getAttribue(driver, callRecordingLockDiv, ElementAttributes.AriaDescribedBy)), "Click to lock this setting and disable teams and users from setting individual Call Recording preferences.");
	}
	
	public void verifyRecordingLockToolTip(WebDriver driver) {
		hoverElement(driver, callRecordingLockDiv);
		waitUntilTextPresent(driver, By.cssSelector(".MuiTooltip-tooltip"), "Click to unlock this setting and enable teams and users to set individual Call Recording preferences.");
	}
	
	public void acceeptCallRecordingAgreement(WebDriver driver) {
		clickElement(driver, callRecordingAgreementCheckBox);
		waitUntilVisible(driver, callRecordingAgreementButton);
		clickElement(driver, callRecordingAgreementButton);
		waitUntilInvisible(driver, callRecordingAgreementHeading);
	}
	
	public void dismissWarningLink(WebDriver driver){
		waitUntilVisible(driver, dismissRecordWarningLink);
		clickElement(driver, dismissRecordWarningLink);
	}

	public void enableCallRecordingSetting(WebDriver driver){
		unlockRecordingSetting(driver);
		selectCallRecordingOverrideType(driver, CallRecordingOverrideOptions.All);
		//waitUntilVisible(driver, enabledRestrictRecord);
		//waitUntilVisible(driver, enabledInboundRecordAnounce);
		//waitUntilVisible(driver, enabledRecordStatus);
		//waitUntilVisible(driver, enabledGranularControl);
		lockRecordingSetting(driver);
		saveCallRecordingTabSettings(driver);
		selectCallRecordingOverrideType(driver, CallRecordingOverrideOptions.None);
		unlockRecordingSetting(driver);
	}
	
	public void disableCallRecordingSetting(WebDriver driver){
		String callRecordingState = getElementsText(driver, callRecordingOverrideOptionsDropDown);
		if(callRecordingState.equals(CallRecordingOverrideOptions.None.toString()) && getAttribue(driver, callRecordingLockImg, ElementAttributes.outerHTML).equals(callRecordingLockImageHTMl)) {
			System.out.println("Call recording setting already disabled");
		} else {
			selectCallRecordingOverrideType(driver, CallRecordingOverrideOptions.None);
			lockRecordingSetting(driver);
			dismissWarningLink(driver);
			waitUntilVisible(driver, disabledRestrictRecord);
			waitUntilVisible(driver, disabledInboundRecordAnounce);
			waitUntilVisible(driver, disabledRecordStatus);
			waitUntilVisible(driver, disabledGranularControl);
		}
	}	
	
	public void selectCallRecordingOverrideType(WebDriver driver, CallRecordingOverrideOptions reocrdOverrideType){
		String callRecordingState = getElementsText(driver, callRecordingOverrideOptionsDropDown);
		clickElement(driver, callRecordingOverrideOptionsDropDown);
		getElements(driver, dropDownOptoins).get(reocrdOverrideType.ordinal()).click();
		if(getAttribue(driver, callRecordingLockImg, ElementAttributes.outerHTML).equals(callRecordingLockImageHTMl) && callRecordingState.equals(CallRecordingOverrideOptions.None.toString()) && !callRecordingState.equals(reocrdOverrideType.toString())){
			acceeptCallRecordingAgreement(driver);
		}
	}
	
	public void verifyRecordingOverrideValue(WebDriver driver, CallRecordingOverrideOptions expectedOverrideValue) {
		waitUntilVisible(driver, callRecordingOverrideOptionsDropDown);
		String text = getElementsText(driver, callRecordingOverrideOptionsDropDown);
		assertEquals(text, expectedOverrideValue.toString());
	}
	
	public void verifyCallRecordingDisabled(WebDriver driver) {
		waitUntilVisible(driver, callRecordOverrideDropDownDisabled);
	}
	
	public void restrictCallRecordingSettingVisible(WebDriver driver){
		waitUntilVisible(driver, restrictCallRecordingLabel);
	}
	
	
	public void disableRestrictCallRecordingSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if( !getElementsText(driver, restrictCallRecordingInputBox).equals("All Users (Default)")) {
			clickElement(driver, restrictCallRecordingInputBox);
			getElements(driver, dropDownOptoins).get(0).click();
			System.out.println("Disabled restrict recording download setting");
		} else {
			System.out.println("restrict recording download setting already Disabled");
		}
	}
	
	public void enableRestrictCallRecordingSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if(!getElementsText(driver, restrictCallRecordingInputBox).equals("Admins Only")) {
			clickElement(driver, restrictCallRecordingInputBox);
			getElements(driver, dropDownOptoins).get(1).click();
			System.out.println("Disabled restrict recording download setting");
		} else {
			System.out.println("restrict recording download setting already enabled");
		}
	}
	
	public void enableInboundCallRecordingAnnoncement(WebDriver driver){
		if(!findElement(driver,inBoundCallRecordingCheckbox).isSelected()) {
			clickElement(driver, inBoundCallRecordingToggleBtn);
			System.out.println("enabled inbound Call recording announcement setting");
		} else {
			System.out.println("Inbound Call recording Annoncement setting already enabled");
		}
	}
	
	public void disableInboundCallRecordingAnnoncement(WebDriver driver){
		if(findElement(driver,inBoundCallRecordingCheckbox).isSelected()) {
			clickElement(driver, inBoundCallRecordingToggleBtn);
			System.out.println("disabled inbound Call recording announcement setting");
		} else {
			System.out.println("Inbound Call recording Annoncement setting already disabled");
		}
	}
	
	public void enableOutboundCallRecordingAnnoncement(WebDriver driver){
		if(!findElement(driver,outBoundCallRecordingCheckbox).isSelected()) {
			clickElement(driver, outBoundCallRecordingToggleBtn);
			System.out.println("enabled outbound Call recording announcement setting");
		} else {
			System.out.println("Outbound Call recording Annoncement setting already enabled");
		}
	}
	
	public void disableOutboundCallRecordingAnnoncement(WebDriver driver){
		if(findElement(driver,outBoundCallRecordingCheckbox).isSelected()) {
			clickElement(driver, outBoundCallRecordingToggleBtn);
			System.out.println("disabled outbound Call recording announcement setting");
		} else {
			System.out.println("Outbound Call recording Annoncement setting already disabled");
		}
	}
	
	public void enableCallRecordingPauseSetting(WebDriver driver){
		if( !getElementsText(driver, callRecordingPauseInputBox).equals("false")) {
			clickElement(driver, callRecordingPauseInputBox);
			idleWait(1);
			getElements(driver, dropDownOptoins).get(2).click();
			System.out.println("enabled Call recording Resume Pause setting");
		} else {
			System.out.println("Call recording Resume Pause setting already enabled");
		}
	}
	
	public void disableCallRecordingPauseSetting(WebDriver driver){
		if( !getElementsText(driver, callRecordingPauseInputBox).equals("false")) {
			clickElement(driver, callRecordingPauseInputBox);
			idleWait(1);
			getElements(driver, dropDownOptoins).get(0).click();
			System.out.println("disabled Call recording Resume Pause setting");
		} else {
			System.out.println("Call recording Resume Pause setting already disabled");
		}
	}
	
	public void enableDisplayRecordingStatusSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if(!findElement(driver,displayRecordingStatusCheckBox).isSelected()) {
			clickElement(driver, displayRecordingStatusToggleBtn);
			System.out.println("enabled Display recording status setting");
		} else {
			System.out.println("Display recording status setting already enabled");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void disableDisplayRecordingStatusSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if(findElement(driver,displayRecordingStatusCheckBox).isSelected()) {
			clickElement(driver, displayRecordingStatusToggleBtn);
			System.out.println("disabled Display recording status setting");
		} else {
			System.out.println("Display recording status setting already disabled");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enableSingleChannelRecordingsSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if(!findElement(driver,singleAgentOnlyRecordingCheckbox).isSelected()) {
			clickElement(driver, singleAgentOnlyRecordingToggleBtn);
			System.out.println("enabled single-channel Agent-only call recordings setting");
		} else {
			System.out.println("single-channel Agent-only call recordings setting already enabled");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void disableSingleChannelRecordingsSetting(WebDriver driver){
		clickElement(driver, advancedTab);
		if(findElement(driver,singleAgentOnlyRecordingCheckbox).isSelected()) {
			clickElement(driver, singleAgentOnlyRecordingToggleBtn);
			System.out.println("disabled single-channel Agent-only call recordings status setting");
		} else {
			System.out.println("single-channel Agent-only call recordings setting already disabled");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public Boolean isSingleChannelRecordingDisabled(WebDriver driver) {
		return Boolean.parseBoolean(getAttribue(driver, singleAgentOnlyRecordingContainer, ElementAttributes.AriaDisable));
	}
	
	public void selectRecordingVisibility(WebDriver driver, RecordingVisibilityTypeOptions recordingVisibilityType){
		clickElement(driver, advancedTab);
		idleWait(2);
		waitUntilVisible(driver, callRecordingVisibilityDropDown);
		clickElement(driver, callRecordingVisibilityDropDown);
		getElements(driver, dropDownOptoins).get(recordingVisibilityType.ordinal()).click();
		System.out.println("Recording visibility set to " + recordingVisibilityType);
	}
	
	public void verifyBothRecordVisibilityOption(WebDriver driver) {
		idleWait(2);
		waitUntilVisible(driver, callRecordingVisibilityDropDown);
		clickElement(driver, callRecordingVisibilityDropDown);
		List<String> recordVisibilityOptins = getTextListFromElements(driver, dropDownOptoins);
		assertEquals(recordVisibilityOptins.size(), 2);
		assertTrue(recordVisibilityOptins.contains("All Users (Default)"));
		assertTrue(recordVisibilityOptins.contains("Agent and team members with listen-in capabilities"));
		pressEscapeKey(driver);
	}
	
	public String getSelectedRecordVisibilityOption(WebDriver driver) {
		clickElement(driver, advancedTab);
		return getElementsText(driver, callRecordingVisibilityDropDown);
	}
	
	public void enableAllowGranularControlSetting(WebDriver driver){
		clickElement(driver, unitedStatesTab);
		if(!findElement(driver, allowGranularControlCheckBox).isSelected()) {
			clickElement(driver, allowGranularControlToggleBtn);
			System.out.println("Enabled allow granular control setting");
		} else {
			System.out.println("allow granular control setting already enabled");
		}
		waitUntilVisible(driver, callRecordingByStateContainer);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void disableAllowGranularControlSetting(WebDriver driver){
		clickElement(driver, unitedStatesTab);
		if(findElement(driver, allowGranularControlCheckBox).isSelected()) {
			clickElement(driver, allowGranularControlToggleBtn);
			System.out.println("disabled allow granular control setting");
		} else {
			System.out.println("allow granular control setting already disabled");
		}
		waitUntilInvisible(driver, callRecordingByStateContainer);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyInterPageToggleButtonsDisabled(WebDriver driver) {
		List<WebElement> toggleButtons = getInactiveElements(driver, internationPageAllToggleBtns);
		for (WebElement toggleButton : toggleButtons) {
			assertEquals(getAttribue(driver, toggleButton, ElementAttributes.AriaDisable), "true");
		}
	}
	
	public void enableCountryGranularControlSetting(WebDriver driver){
		clickElement(driver, intertaionalTab);
		if(!findElement(driver, countryGranularCheckBox).isSelected()) {
			clickElement(driver, countryGranularControlOnToggleBtn);
			waitUntilVisible(driver, acceptButton);
			clickElement(driver, acceptButton);
			waitUntilVisible(driver, acceptButton);
			System.out.println("Enabled country granular control setting");
		} else {
			System.out.println("country granular control setting already enabled");
		}
		waitUntilVisible(driver, countryGranularContainer);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void disableCountryGranularControlSetting(WebDriver driver){
		clickElement(driver, intertaionalTab);
		if(findElement(driver, countryGranularCheckBox).isSelected()) {
			clickElement(driver, countryGranularControlOnToggleBtn);
			System.out.println("country allow granular control setting");
		} else {
			System.out.println("country granular control setting already disabled");
		}
		waitUntilInvisible(driver, countryGranularContainer);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void expandCountyContinent(WebDriver driver, String continentName){
		By continentLocator	= By.xpath(continentLink.replace("$$Continent$$", continentName));
		By continentExpandedAreaLocator = By.xpath(continentExpandedArea.replace("$$Continent$$", continentName));
		if(!isElementVisible(driver, continentExpandedAreaLocator, 0)){
			clickElement(driver, continentLocator);
			waitUntilVisible(driver, continentExpandedAreaLocator);
		}
		else{
			System.out.println("Continents countries already visible");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void collapseCountyContinent(WebDriver driver, String continentName){
		By continentLocator	= By.xpath(continentLink.replace("$$Continent$$", continentName));
		By continentExpandedAreaLocator = By.xpath(continentExpandedArea.replace("$$Continent$$", continentName));
		if(isElementVisible(driver, continentExpandedAreaLocator, 0)){
			clickElement(driver, continentLocator);
			waitUntilInvisible(driver, continentExpandedAreaLocator);
		}
		else{
			System.out.println("Continents section already collapsed");
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void selectAreaRecordingDropdownOption(WebDriver driver, String areaName, AreaRecordOptions recordingOption) {
		By areaLocator = By.xpath(areaCodeDropDownLocator.replace("$$Area$$", areaName));
		By areaConsentImageLocator = By.xpath(areadCodeConsentImageLocator.replace("$$Area$$", areaName));
		scrollToArea(driver, areaLocator);
		if(!getElementsText(driver, areaLocator).contains(recordingOption.displayName())){
			scrollToElement(driver, areaLocator);
			if(isElementVisible(driver, areaConsentImageLocator, 0) && recordingOption.displayName().equals("Record All Parties")){
				By consentText = By.cssSelector(".MuiDialog-paper span[color='primary']");
				clickElement(driver, areaLocator);
				clickByJs(driver, getInactiveElements(driver, By.cssSelector(recordingDropDownOption.replace("$$RecordOption$$", recordingOption.toString()))).get(0));
				waitUntilTextPresent(driver, consentText, areaName + " requires all-party consent to record calls. Do you want to continue enabling call recording?");
				clickElement(driver, confirmConsentButton);
				waitUntilInvisible(driver, confirmConsentButton);
			} else{
				clickElement(driver, areaLocator);
				clickByJs(driver, getInactiveElements(driver, By.cssSelector(recordingDropDownOption.replace("$$RecordOption$$", recordingOption.toString()))).get(0));
			}
		}
		dashboard.isPaceBarInvisible(driver);
		assertTrue(getElementsText(driver, areaLocator).equals(recordingOption.displayName()));
	}
	
	public void checkAreasForCallRecording(WebDriver driver, String areaName){
		selectAreaRecordingDropdownOption(driver, areaName, AreaRecordOptions.AllParties);
	}
	
	public void scrollToArea(WebDriver driver, By areaLocator) {
		int count = 0;
		while (!isElementPresent(driver, By.xpath(areaCodeDropDownLocator.replace("$$Area$$", "Alabama")), 0) && count < 70) {
			clickElement(driver, By.cssSelector(".rdna-accordion-row.open .ringdna-table, div:not(.rdna-accordion-body)>.ringdna-table"));
			pressUpArrowKey(driver);
			count++;
		}
		
		count = 0;
		do {
			clickElement(driver, By.cssSelector(".rdna-accordion-row.open .ringdna-table, div:not(.rdna-accordion-body)>.ringdna-table"));
			pressDownArrowKey(driver);
			count++;
		} while ((!isElementPresent(driver, areaLocator, 0)) && count < 70);
		pressDownArrowKey(driver);
	}
	
	public void uncheckAreasForCallRecording(WebDriver driver, String areaName){
		By areaLocator = By.xpath(areaCodeDropDownLocator.replace("$$Area$$", areaName));
		scrollToArea(driver, areaLocator);
		if(!getElementsText(driver, areaLocator).contains("No Recording")) {
			clickElement(driver, areaLocator);
			clickByJs(driver, getInactiveElements(driver, dropDownOptoins).get(0));
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void checkAreasForPlayOutboundRecording(WebDriver driver, String areaName){
		By areaLocator = By.xpath(areaCodePlayOutboundLocator.replace("$$Area$$", areaName));
		scrollToArea(driver, areaLocator);
		if(!findElement(driver, areaLocator).isSelected()){
			scrollToElement(driver, areaLocator);
			clickByJs(driver, areaLocator);
			}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void uncheckAreasForPlayOutboundRecording(WebDriver driver, String areaName){
		By areaLocator = By.xpath(areaCodePlayOutboundLocator.replace("$$Area$$", areaName));
		scrollToArea(driver, areaLocator);
		if(findElement(driver, areaLocator).isSelected()){
			scrollToElement(driver, areaLocator);
			clickByJs(driver, areaLocator);
			}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String getSaveConfirmationMessage(WebDriver driver){
		return getElementsText(driver, saveConfiramtionMessage);
	}
	
	public void isSaveConfirmationMessageDissappeared(WebDriver driver){
		isElementVisible(driver, saveConfiramtionMessage, 1);
		waitUntilInvisible(driver, saveConfiramtionMessage);
	}
	
	public void saveCallRecordingTabSettings(WebDriver driver){
		waitUntilVisible(driver, callRecordingTabSaveButton);
		scrollToElement(driver, callRecordingTabSaveButton);
		idleWait(1);
		if (findElement(driver, callRecordingTabSaveButton).isEnabled()) {
			clickElement(driver, callRecordingTabSaveButton);
			System.out.println(getSaveConfirmationMessage(driver));
			isSaveConfirmationMessageDissappeared(driver);
			dashboard.isPaceBarInvisible(driver);
			isSaveConfirmationMessageDissappeared(driver);
		}
	}
	
	public void setDefaultCallRecordingSettings(WebDriver driver){
		openCallRecordingTab(driver);
		//disableCallRecordingOverrideSetting(driver);
		enableCallRecordingSetting(driver);
		saveCallRecordingTabSettings(driver);
		disableRestrictCallRecordingSetting(driver);
		disableSingleChannelRecordingsSetting(driver);
		enableCallRecordingPauseSetting(driver);
		selectRecordingVisibility(driver, RecordingVisibilityTypeOptions.All);
		saveCallRecordingTabSettings(driver);
		enableAllowGranularControlSetting(driver);
		checkAreasForCallRecording(driver, "Alabama");
		checkAreasForCallRecording(driver, "New York");
		checkAreasForCallRecording(driver, "Iowa");
		checkAreasForCallRecording(driver, "California");
		checkAreasForCallRecording(driver, "North Dakota");
		checkAreasForCallRecording(driver, "Pennsylvania");
		checkAreasForCallRecording(driver, "New Jersey");
		checkAreasForCallRecording(driver, "Connecticut");
		saveCallRecordingTabSettings(driver);
	}
	
	public void checkRecordCallsConsentState(WebDriver driver, String state){
		By recordCallConsentLoc = By.xpath(recordCallsConsentState.replace("$state$", state));
		waitUntilVisible(driver, recordCallConsentLoc);
		if(!findElement(driver, recordCallConsentLoc).isSelected()){
			clickElement(driver, recordCallConsentLoc);
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void unCheckRecordCallsConsentState(WebDriver driver, String state){
		By recordCallConsentLoc = By.xpath(recordCallsConsentState.replace("$state$", state));
		waitUntilVisible(driver, recordCallConsentLoc);
		if(findElement(driver, recordCallConsentLoc).isSelected()){
			clickElement(driver, recordCallConsentLoc);
		}
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickConfirmConsentBtn(WebDriver driver) {
		waitUntilVisible(driver, confirmConsentButton);
		clickElement(driver, confirmConsentButton);
		waitUntilInvisible(driver, confirmConsentButton);
	}
	
	public void verifyConsentWarningMsg(WebDriver driver, String state){
		waitUntilVisible(driver, warningMsg);
		assertEquals(getElementsText(driver, warningMsg), String.format("%s requires all-party consent to record calls. Do you want to continue enabling call recording?", state));
	}
}
