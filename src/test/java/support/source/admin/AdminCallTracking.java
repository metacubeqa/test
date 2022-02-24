/**
 * 
 */
package support.source.admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.internal.collections.Pair;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;

/**
 * @author Abhishek
 *
 */
public class AdminCallTracking extends SeleniumBase{
	
	Dashboard dashBoard = new Dashboard();
	SmartNumbersPage smartNoPage = new SmartNumbersPage();
	
	//Account Select section
	By accountPickerItem		= By.cssSelector(".account-picker .item");
	By accountPickerInputTab	= By.cssSelector(".account-picker input");
	By accountDropdownSelect	= By.cssSelector(".account-picker .selectize-dropdown-content div");
	
	//Tracking Number Search tools
	By CallTrackNameTextBox		= By.cssSelector(".form-control.searchLabel");
	By callFlowDropdown			= By.cssSelector(".callflow-picker .selectize-control");
	By callFlowTextBox			= By.cssSelector(".callflow-picker .selectize-input input");
	By callFlowDropDownItems	= By.cssSelector(".callflow-picker .selectize-dropdown-content .option");
	By searchButton				= By.cssSelector("button.search");
	By domainNameTextBox		= By.cssSelector(".form-control.domain[placeholder='Destination Domain']");
	By offlineNameTextBox		= By.cssSelector(".form-control.searchName");
	By offlineSourceDropDown	= By.cssSelector(".source-picker");
	By offlineSourceTextBox		= By.cssSelector(".source-picker input");
	By offlineSourceDropItems	= By.cssSelector(".source-picker .selectize-dropdown-content .option");
	By channelDropDown			= By.cssSelector(".channel-picker .selectize-control");
	By channelTextBox			= By.cssSelector(".channel-picker .selectize-input input");
	By channelDropDownItems		= By.cssSelector(".channel-picker .selectize-dropdown-content .option");
	By toastMessage	    		= By.cssSelector("div.toast-message");
	
	//dynamic number insertion
	By dynamicNoInsertionHeading 	 = By.xpath(".//h1[text()='Dynamic Number Insertion']");
	By tagAdwordsDestionationHeading = By.xpath(".//h1[text()='Tag AdWords Destination URLs']");
	By copyToClipBoard				 = By.cssSelector("button.copy.pull-right");
	By tagAdwordDestinationUrlText	 = By.xpath(".//*[@id='main']/div//div[not(@class)]");
	
	//call tracking basic
	By totalRecordsAvailable	= By.cssSelector(".pager-container span.total-records");
	By downloadTotalRecords		= By.cssSelector(".glyphicon.glyphicon-download-alt");
	By addIconBasicCallTracking = By.cssSelector("[data-target='#add-basic-call-tracking-modal'] .glyphicon-plus-sign");
	By newBasicNextBtn			= By.cssSelector(".bbm-button.next.stepNext");
	By newBasicTrackName		= By.cssSelector(".form-control.domain[placeholder='ex.ringdna.com']");
	By customChannelTab			= By.cssSelector(".form-control.channel");
	By addCustomChannelBtn		= By.cssSelector(".btn.bbm-button.add-channel");
	By customBasicGoogle		= By.cssSelector("td.img-thumbnail.logos[data-name='Google']");
	By customBasicGoogleAd		= By.cssSelector("td.img-thumbnail.logos[data-name='Google AdWords']");
	By customBasicYahoo 		= By.cssSelector("td.img-thumbnail.logos[data-name='Yahoo']");
	By customBasicBing			= By.cssSelector("td.img-thumbnail.logos[data-name='Bing']");
	By customBasicFacebook		= By.cssSelector("td.img-thumbnail.logos[data-name='facebook.com']");
	By customBasicTwitter		= By.cssSelector("td.img-thumbnail.logos[data-name='twitter.com']");
	By customBasicLinkedIn		= By.cssSelector("td.img-thumbnail.logos[data-name='linkedin.com']");
	By customBasicInsta			= By.cssSelector("td.img-thumbnail.logos[data-name='instagram.com']");
	By customBasicGoogleSelcted	= By.cssSelector("td.img-thumbnail.logos.existed[data-name='Google']");
	By customBasicGoogleAdSelcted  = By.cssSelector("td.img-thumbnail.logos.existed[data-name='Google AdWords']");
	By customBasicYahooSelcted 	   = By.cssSelector("td.img-thumbnail.logos.existed[data-name='Yahoo']");
	By customBasicBingSelcted	   = By.cssSelector("td.img-thumbnail.logos.existed[data-name='Bing']");
	By customBasicFacebookSelcted  = By.cssSelector("td.img-thumbnail.logos.existed[data-name='facebook.com']");
	By customBasicTwitterSelcted   = By.cssSelector("td.img-thumbnail.logos.existed[data-name='twitter.com']");
	By customBasicLinkedInSelcted  = By.cssSelector("td.img-thumbnail.logos.existed[data-name='linkedin.com']");
	By customBasicInstaSelcted	= By.cssSelector("td.img-thumbnail.logos.existed[data-name='instagram.com']");
	By newBasicFinishBtn		= By.cssSelector(".bbm-button.next.persist.btn-success.stepNext");
	By customChannelList		= By.cssSelector("td.string-cell.sortable.renderable.channel");
	By customChannelDeleteBtn	= By.cssSelector("span.glyphicon.glyphicon-remove-sign");
	
	//call tracking custom
	By addIconCustomCallTracking = By.cssSelector("[data-target='#add-custom-call-tracking-modal'] .glyphicon-plus-sign");
	By customTrackNameTab		= By.cssSelector(".form-control.label-tracking-cell");
	By customTrackOrignUrlTab	= By.cssSelector(".form-control.origin-url-cell");
	By customTrackDestinatnTab  = By.cssSelector(".form-control.destination-url-cell");
	By customFinishBtn			= By.cssSelector(".btn.btn-success.persist.finish");
	By customCallTrackNameTextbox = By.cssSelector(".form-control.searchInfo");
	
	//call tracking offline
	By addIconOfflineCallTracking = By.cssSelector("[data-target='#add-offline-call-tracking-modal'] .glyphicon-plus-sign");
	By offlineTrackSourceTab	= By.cssSelector(".source.form-control");
	By offlineTrackSourceType	= By.cssSelector("#sources option");
	By updateSourceTab			= By.cssSelector(".form-group.source  input.form-control");
	By saveBtnUpdateSmartNo		= By.cssSelector(".ladda-button.btn.btn-success.save-number");
	By addAnotherOfflineNo		= By.cssSelector(".add-offline span");
	By offlineBackgridBody		= By.cssSelector(".backgrid tbody");
	By tableSource				= By.xpath(".//td[contains(@class, 'source')]");
	
	//call tracking offline
	By addIconGoogleAdwordsCallTracking = By.cssSelector("[data-target='#add-adwords-call-tracking-modal'] .glyphicon-plus-sign");

	//Tracking Number data Table cell
	By advNoTableName			= By.cssSelector("table tbody .name , table tbody .labelTracking");
	By advNoTableCustomPar		= By.cssSelector("table tbody td:nth-child(2)");
	By advNoTableCallFlow		= By.cssSelector("table tbody a[href*='call-flows']");
	By advNoTableSmartNos		= By.cssSelector("table tbody a[href*='smart-numbers']");
	By advNoTableCampaign		= By.cssSelector("table tbody td.campaign");
	By advNoTableCreatedDate	= By.cssSelector("table tbody td.createdDate");
	By advNoTableLastUsedDate	= By.cssSelector("table tbody .smartNumber\\.dateLastUsed");
	By basicCallDomainTable		= By.cssSelector("table tbody td:nth-child(5)");
	By customCallOrigntngUrl	= By.cssSelector("table tbody td.string-cell.sortable.renderable.name");
	
	//Tracking Number add new fields
	By addTrackingNumberIcon	= By.cssSelector("[data-target='#add-advanced-call-tracking-modal'] .glyphicon-plus-sign");
	By addAnotherAdvancedNo		= By.cssSelector(".add-advanced .glyphicon-plus-sign");
	By newadvTrackDeleteButton	= By.className("delete");
	By newAdvDeleteSmartNo      = By.cssSelector(".glyphicon-remove-sign.remove");
	By newAdvTrackName			= By.cssSelector(".form-control.label-tracking-cell");
	By newAdvTrackCustomParam	= By.cssSelector(".form-control.channel-cell");
	By newAdvTrackSmartNoType	= By.cssSelector(".form-control.number-type");
	By newAdvTrackSmartAssignNo = By.cssSelector(".btn-primary.reassign");
	By newAdvTrackAreaCode		= By.cssSelector(".form-control.area-code-cell");
	By newAdvTrackCallFlow		= By.cssSelector(".selectize-input.items.not-full.has-options input[placeholder='Call Flow']");
	By newadvTrackSfdcCampaign 	= By.cssSelector(".selectize-input.items.not-full.has-options input[placeholder='SFDC Campaign']");
	By newAdvTrackconfirmbtn	= By.cssSelector("[data-bb-handler='confirm']");
	By newAdvTrackFinishButton	= By.cssSelector("button.finish");
	By closeBtnForAddTrackNo	= By.cssSelector("button.close span");
	By campaignSelectDropDown 	= By.xpath(".//*[contains(@class,'campaign-cell')]//div[contains(@class,'not-full') and contains(@class, 'input-active')]/..//div[@class='selectize-dropdown-content']/div");
	By callFlowSelectDropDown	= By.cssSelector("div[class*='callflows'] .selectize-dropdown-content .option");
	By countryInputBox			= By.cssSelector(".country-cell .selectize-input.has-items .item");
	By countryInputTab			= By.cssSelector(".country-cell .selectize-input.has-items input");
	By enterCountryInputTab		= By.cssSelector(".country-cell .selectize-input.not-full input");
	By countryDropDownItems		= By.cssSelector(".country-cell .selectize-dropdown-content .option");
	
	By errorTrackNameTextBox	= By.cssSelector(".has-error .form-control.channel-cell");
	By errorTrackCustomParam	= By.cssSelector(".has-error .form-control.channel-cell");
	By errorTrackSmartNoType	= By.cssSelector(".has-error .form-control.number-type");
	By errorTrackAreaCode		= By.cssSelector(".has-error .form-control.area-code-cell");
	By errorTrackCallFlow		= By.cssSelector(".call-flow-cell .has-error");
	By errorTrackSfdcCampaign 	= By.cssSelector(".has-error .form-control.campaigns");
	By errorTrackCustomCallName = By.cssSelector(".has-error .form-control.label-tracking-cell");
	By errorTrackOriginatingUrl	= By.cssSelector(".has-error .form-control.origin-url-cell");
	By errorTrackDestinationUrl	= By.cssSelector(".has-error .form-control.destination-url-cell");
	By errorTrackSourceType		= By.cssSelector(".has-error .source.form-control");
	
	String smartNumberList          = ".//*[text()='$$domainName$$']/ancestor::tr/td/a[contains(@href,'smart-numbers')]";
	
	static final String tagAdwordsUrlText = "{websiteurl.com}/?rdnaCampaign={campaign}&rdnaAdGroup={adgroup}&rdnaKeyword={keyword}&rdnaKeywordType={keywordMatchType}";
	
	public enum newAdvTrackSmartNoTypes{
		TollFree("Toll Free"),
		Local("Local"),
		UseExisting("Use Existing");
	
	    private String displayName;

	    newAdvTrackSmartNoTypes(String displayName) {
	        this.displayName = displayName;
	    }
		    
	    public String displayName() { return displayName; }

	    @Override 
	    public String toString() { return displayName; }
	}
	
	public enum offlineTrackSourceTypes{
		Print,
		TV,
		Radio
	}
	
	public enum trackingNoFields{
		Name,
		CustomParameterValue,
		SmartNumberType,
		AreaCode,
		CallFlow,
		SfdcCampaign,
		NameCustom,
		OriginatingUrl,
		DestinationUrl,
		Source,
		Country
	}
		
	public static enum Channels{
		Google("Google"),
		GoogleAdWords("Google Adwords"),
		Yahoo("Yahoo"),
		Bing("Bing"),
		Facebook("facebook.com"),
		Twitter("twitter.com"),
		LinkedIn("linkedin.com"),
		Instagram("instagram.com");
		
		private final String channelType;

		Channels(String channelType) {
		    this.channelType = channelType;
		}

		@Override
		public String toString() {
			return channelType;
		}
		
	}
	
	/*******Account Section section start here*******/
	
	public String getAccountName(WebDriver driver){
		waitUntilVisible(driver, accountPickerItem);
		return getElementsText(driver, accountPickerItem);
	}
	
	public void selectAccount(WebDriver driver, String accountName){
		enterTextAndSelectFromDropDown(driver, accountPickerItem, accountPickerInputTab, accountDropdownSelect, accountName);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public boolean isAdvancedAddIconDisabled(WebDriver driver){
		return findElement(driver, addTrackingNumberIcon).findElement(By.xpath("..")).getAttribute("class").equals("add disabled");
	}
	
	public boolean isBasicAddIconDisabled(WebDriver driver){
		return findElement(driver, addIconBasicCallTracking).findElement(By.xpath("..")).getAttribute("class").equals("add disabled");
	}
	
	public boolean isCustomAddIconDisabled(WebDriver driver){
		return findElement(driver, addIconCustomCallTracking).findElement(By.xpath("..")).getAttribute("class").equals("add disabled");
	}
	
	public boolean isOfflineAddIconDisabled(WebDriver driver){
		return findElement(driver, addIconOfflineCallTracking).findElement(By.xpath("..")).getAttribute("class").equals("add disabled");
	}
	
	public boolean isGoogleAdwordsAddIconDisabled(WebDriver driver){
		return findElement(driver, addIconGoogleAdwordsCallTracking).findElement(By.xpath("..")).getAttribute("class").equals("add disabled");
	}
	
	/*******Account Section section ends here*******/
	
	/*******Dynamic NumberInsertion section start here*******/
	
	public void verifyDynamicNumberInsertionPageHeadings(WebDriver driver){
		assertTrue(isElementVisible(driver, dynamicNoInsertionHeading, 5));
		assertTrue(isElementVisible(driver, tagAdwordsDestionationHeading, 5));
		assertTrue(isElementVisible(driver, copyToClipBoard, 5));
		assertTrue(getElementsText(driver, tagAdwordDestinationUrlText).contains(tagAdwordsUrlText));
	}
	
	/*******Dynamic NumberInsertion section ends here*******/
	
	/*******Search Advance Tracking number functions start here*******/
	public void enterTrackNoNameToSearch(WebDriver driver, String trackingNumber){
		waitUntilVisible(driver, CallTrackNameTextBox);
		enterText(driver, CallTrackNameTextBox, trackingNumber);
	}
	
	public void enterDomainNameToSearch(WebDriver driver, String domainName) {
		waitUntilVisible(driver, domainNameTextBox);
		enterText(driver, domainNameTextBox, domainName);
	}
	
	public void selectCallFlowToSearch(WebDriver driver, String callFlow){
		waitUntilVisible(driver, callFlowDropdown);
		clickElement(driver, callFlowDropdown);
		enterBackspace(driver, callFlowTextBox);
		enterText(driver, callFlowTextBox, callFlow);
		idleWait(2);
		List<WebElement> searchedCallFlows = getElements(driver, callFlowDropDownItems);
		for (WebElement callFlowItem : searchedCallFlows) {
			if(callFlowItem.getText().equals(callFlow)){
				callFlowItem.click();
				break;
			}
		}
	}
	
	public void clickSearchButton(WebDriver driver){
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void searchAdvCallTrackNumber(WebDriver driver, String advNumberName, String callFlow){
		enterTrackNoNameToSearch(driver, advNumberName);
		selectCallFlowToSearch(driver, callFlow);
		clickSearchButton(driver);
		dashBoard.isPaceBarInvisible(driver);
		idleWait(2);
	}
	
	/*******Search Advance Tracking number functions end here*******/
	
	/******* Basic Call Tracking functions starts here  *******/
	
	public void downloadTotalRecords(WebDriver driver) {
		waitUntilVisible(driver, totalRecordsAvailable);
		int totalRecords = Integer.parseInt(getElementsText(driver, totalRecordsAvailable));
		if (totalRecords >=0) {
			waitUntilClickable(driver, downloadTotalRecords);
			clickElement(driver, downloadTotalRecords);
		}
	}

	public void searchBasicCallTrackNumber(WebDriver driver, String channelName, String domainName, String callFlow) {
		selectChannelToSearch(driver, channelName);
		enterDomainNameToSearch(driver, domainName);
		selectCallFlowToSearch(driver, callFlow);
		clickSearchButton(driver);
	}

	public void selectChannelToSearch(WebDriver driver, String channel){
		waitUntilInvisible(driver, newBasicFinishBtn);
		waitUntilInvisible(driver, newAdvTrackSmartNoType);
		waitUntilVisible(driver, channelDropDown);
		clickElement(driver, channelDropDown);
		enterBackspace(driver, channelTextBox);
		enterText(driver, channelTextBox, channel);
		idleWait(2);
		List<WebElement> searchedChannels = getElements(driver, channelDropDownItems);
		for (WebElement channelItem : searchedChannels) {
			if(channelItem.getText().equals(channel)){
				channelItem.click();
				break;
			}
		}
	}
	
	public int getBasicCallSmartNumberIndex(WebDriver driver, String domainName, String callFlowName) {

		List<WebElement> advTrackingCallFlows = getElements(driver, advNoTableCallFlow);
		List<WebElement> basicDomainTable = getElements(driver, basicCallDomainTable);

		for (int i = 0; i < advTrackingCallFlows.size(); i++) {
			if (basicDomainTable.get(i).getText().equals(domainName)
					&& advTrackingCallFlows.get(i).getText().equals(callFlowName)) {
				return i;
			}
		}
		return -1;
	}

	public void clickAddNewBasicTrackbtn(WebDriver driver) {
		waitUntilVisible(driver, addIconBasicCallTracking);
		waitUntilClickable(driver, addIconBasicCallTracking);
		clickElement(driver, addIconBasicCallTracking);
		waitUntilVisible(driver, newBasicTrackName);
	}

	public void enterDomainName(WebDriver driver, String domainName) {
		waitUntilInvisible(driver, toastMessage);
		waitUntilVisible(driver, newBasicTrackName);
		idleWait(1);
		enterText(driver, newBasicTrackName, domainName);
		waitUntilVisible(driver, newBasicNextBtn);
		clickElement(driver, newBasicNextBtn);
	}

	public void clickFinishButtonBasic(WebDriver driver){
		waitUntilVisible(driver, newBasicFinishBtn);
		clickElement(driver, newBasicFinishBtn);
	}
	
	public void clickNextButtonBasic(WebDriver driver){
		waitUntilVisible(driver, newBasicNextBtn);
		clickElement(driver, newBasicNextBtn);
	}
	
	public boolean isChannelUnSelectable(WebDriver driver, String channel) {
		By locator = null;
		switch (channel) {
		case "Google":
			locator = customBasicGoogleSelcted;
			break;
		case "GoogleAdWords":
			locator = customBasicGoogleAdSelcted;
			break;
		case "Yahoo":
			locator = customBasicYahooSelcted;
			break;
		case "Bing":
			locator = customBasicBingSelcted;
			break;
		case "Facebook":
			locator = customBasicFacebookSelcted;
			break;
		case "Twitter":
			locator = customBasicTwitterSelcted;
			break;
		case "LinkedIn":
			locator = customBasicLinkedInSelcted;
			break;
		case "Instagram":
			locator = customBasicInstaSelcted;
			break;
		default:
			break;
		}
		return isElementVisible(driver, locator, 2);
	}
	
	public void selectCustomChannel(WebDriver driver, List<Channels> list) {
		idleWait(3);
		if (list.contains(Channels.Google) && !isElementVisible(driver, customBasicGoogleSelcted, 0)) {
			clickElement(driver, customBasicGoogle);
		}

		if (list.contains(Channels.GoogleAdWords) && !isElementVisible(driver, customBasicGoogleAdSelcted, 0)) {
			clickElement(driver, customBasicGoogleAd);
		}

		if (list.contains(Channels.Yahoo) && !isElementVisible(driver, customBasicYahooSelcted, 0)) {
			clickElement(driver, customBasicYahoo);
		}

		if (list.contains(Channels.Bing) && !isElementVisible(driver, customBasicBingSelcted, 0)) {
			clickElement(driver, customBasicBing);
		}

		if (list.contains(Channels.Facebook) && !isElementVisible(driver, customBasicFacebookSelcted, 0)) {
			clickElement(driver, customBasicFacebook);
		}

		if (list.contains(Channels.Twitter) && !isElementVisible(driver, customBasicTwitterSelcted, 0)) {
			clickElement(driver, customBasicTwitter);
		}

		if (list.contains(Channels.LinkedIn) && !isElementVisible(driver, customBasicLinkedInSelcted, 0)) {
			clickElement(driver, customBasicLinkedIn);
		}

		if (list.contains(Channels.Instagram) && !isElementVisible(driver, customBasicInstaSelcted, 0)) {
			clickElement(driver, customBasicInsta);
		}
	}
	
	public void enterCustomChannel(WebDriver driver, String customChannelName) {
		waitUntilVisible(driver, customBasicGoogle);
		waitUntilVisible(driver, customChannelTab);
		enterText(driver, customChannelTab, customChannelName);
		if(findElement(driver, addCustomChannelBtn).isEnabled())
		   clickElement(driver, addCustomChannelBtn);
	}
	
	public String findErrorMessageInvalidUrl(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		String textMessageInvalid = getElementsText(driver,toastMessage);
		return textMessageInvalid;
	}

	public void waitUntilMessageDisappears(WebDriver driver){
		isElementVisible(driver, toastMessage, 0);
		waitUntilInvisible(driver, toastMessage);
	}
	
	public boolean downloadRecordsSuccessfullyOrNot(WebDriver driver, String path, String startFileName, String extension) {
		waitForFileToDownload(driver, path, startFileName, extension);
		return isFileDownloaded(path, startFileName, extension);
	}
	
	public void verifyMandatoryFieldsBasic(WebDriver driver){
		waitUntilVisible(driver, errorTrackCallFlow);
		waitUntilInvisible(driver, errorTrackAreaCode);
		waitUntilVisible(driver, errorTrackSmartNoType);
		waitUntilInvisible(driver, errorTrackSfdcCampaign);
		closeAddAdvTrackingWindow(driver);
	}
	
	public void deleteCustomChannelBasic(WebDriver driver, String customChannelName) {
		List<WebElement> customChannels = getElements(driver, customChannelList);
		List<WebElement> customChannelDeleteBtns = getElements(driver, customChannelDeleteBtn);
		for (int i = 0; i <= customChannels.size() - 1; i++) {
			if (customChannels.get(i).getText().equals(customChannelName)) {
				clickElement(driver, customChannelDeleteBtns.get(i));
				if (isElementVisible(driver, newAdvTrackconfirmbtn, 2)) {
					clickElement(driver, newAdvTrackconfirmbtn);
					waitUntilInvisible(driver, newAdvTrackconfirmbtn);
				}
			}
		}
	}
	
	/******* Basic Call Tracking functions ends here *******/
	
	/******* Custom Call Tracking functions starts here *******/
	
	public void clickAddNewCustomTrackNobtn(WebDriver driver) {
		waitUntilVisible(driver, addIconCustomCallTracking);
		waitUntilClickable(driver, addIconCustomCallTracking);
		clickElement(driver, addIconCustomCallTracking);
		waitUntilVisible(driver, customTrackNameTab);
		waitUntilClickable(driver, customTrackNameTab);
	}
	
	public void enterCustomTrackNameCustom(WebDriver driver, String name){
		waitUntilVisible(driver, customTrackNameTab);
		enterText(driver, customTrackNameTab, name);
	}
	
	public void enterCustomTrackOrigntngUrl(WebDriver driver, String name){
		waitUntilVisible(driver, customTrackOrignUrlTab);
		enterText(driver, customTrackOrignUrlTab, name);
	}
	
	public void enterCustomTrackDestintnUrl(WebDriver driver, String name){
		waitUntilVisible(driver, customTrackDestinatnTab);
		enterText(driver, customTrackDestinatnTab, name);
	}
	
	public void enterUrlTosearch(WebDriver driver, String url) {
		waitUntilInvisible(driver, customFinishBtn);
		waitUntilInvisible(driver, customTrackNameTab);
		waitUntilVisible(driver, customCallTrackNameTextbox);
		waitUntilClickable(driver, customCallTrackNameTextbox);
		enterText(driver, customCallTrackNameTextbox, url);
	}
	
	public void searchCustomCallTrackNumber(WebDriver driver, String url, String callFlow) {
		enterUrlTosearch(driver, url);
		selectCallFlowToSearch(driver, callFlow);
		clickSearchButton(driver);
	}
	
	public void clickFinishButtonCustom(WebDriver driver){
		waitUntilVisible(driver, customFinishBtn);
		waitUntilClickable(driver, customFinishBtn);
		clickElement(driver, customFinishBtn);
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void verifyMandatoryFieldsCustom(WebDriver driver){
		waitUntilVisible(driver, errorTrackCustomCallName);
		waitUntilVisible(driver, errorTrackOriginatingUrl);
		waitUntilVisible(driver, errorTrackDestinationUrl);
		waitUntilVisible(driver, errorTrackSmartNoType);
		waitUntilInvisible(driver, errorTrackAreaCode);
		waitUntilVisible(driver, errorTrackCallFlow);
		waitUntilInvisible(driver, errorTrackSfdcCampaign);
		closeAddAdvTrackingWindow(driver);
	}
	
	public int getCustomCallSmartNumberIndex(WebDriver driver, String orignatingUrl, String callFlowName) {

		List<WebElement> advTrackingCallFlows = getElements(driver, advNoTableCallFlow);
		List<WebElement> orignatingUrlTable = getElements(driver, customCallOrigntngUrl);

		for (int i = 0; i < advTrackingCallFlows.size(); i++) {
			if (orignatingUrlTable.get(i).getText().equals(orignatingUrl)
					&& advTrackingCallFlows.get(i).getText().equals(callFlowName)) {
				return i;
			}
		}
		return -1;
	}
	

	/******* Custom Call Tracking functions ends here *******/
	
	/******* Offline Call Tracking functions starts here *******/
	
	public void clickAddNewOfflineTrackNobtn(WebDriver driver) {
		waitUntilVisible(driver, addIconOfflineCallTracking);
		waitUntilClickable(driver, addIconOfflineCallTracking);
		clickElement(driver, addIconOfflineCallTracking);
		waitUntilVisible(driver, newAdvTrackCustomParam);
		waitUntilClickable(driver, newAdvTrackCustomParam);
	}
	
	public void searchOfflineCallTrackNumber(WebDriver driver, String name, String sourceType, String callFlow) {
		idleWait(3);
		enterNameTosearch(driver, name);
		enterSourceToSearch(driver, sourceType);
		selectCallFlowToSearch(driver, callFlow);
		clickSearchButton(driver);
	}
	
	public void enterSourceToSearch(WebDriver driver, String sourceType) {
		waitUntilVisible(driver, offlineSourceDropDown);
		clickElement(driver, offlineSourceDropDown);
		enterBackspace(driver, offlineSourceTextBox);
		enterText(driver, offlineSourceTextBox, sourceType);
		idleWait(2);
		List<WebElement> searchedSourceFlows = getElements(driver, offlineSourceDropItems);
		for (WebElement sourceItem : searchedSourceFlows) {
			if(sourceItem.getText().equals(sourceType)){
				sourceItem.click();
				break;
			}
		}
	}

	public void enterNameTosearch(WebDriver driver, String name) {
		waitUntilInvisible(driver, customFinishBtn);
		waitUntilInvisible(driver, newAdvTrackName);
		waitUntilVisible(driver, offlineNameTextBox);
		waitUntilClickable(driver, offlineNameTextBox);
		enterText(driver, offlineNameTextBox, name);
	}
	
	public void verifyMandatoryFieldsOffline(WebDriver driver){
		waitUntilVisible(driver, errorTrackNameTextBox);
		waitUntilVisible(driver, errorTrackSourceType);
		waitUntilVisible(driver, errorTrackSmartNoType);
		waitUntilInvisible(driver, errorTrackAreaCode);
		waitUntilVisible(driver, errorTrackCallFlow);
		waitUntilInvisible(driver, errorTrackSfdcCampaign);
		closeAddAdvTrackingWindow(driver);
	}
	
	public Pair<String,String> findBorderColorandInvalidMsg(WebDriver driver) {
		waitUntilVisible(driver, toastMessage);
		String textMessageInvalid = getElementsText(driver,toastMessage);
		String borderColor = findElement(driver, errorTrackSmartNoType).getCssValue("border-color").trim();
		String borderHexColor = Color.fromString(borderColor).asHex();
		Pair<String,String> pair = new Pair<String, String>(borderHexColor, textMessageInvalid);
		return pair;
	}
	
	public void upDateSourceFields(WebDriver driver, String sourceType) {
		clickElement(driver, getElements(driver, advNoTableSmartNos).get(0));
		waitUntilVisible(driver, updateSourceTab);
		waitUntilClickable(driver, updateSourceTab);
		enterText(driver, updateSourceTab, sourceType);
		waitUntilClickable(driver, saveBtnUpdateSmartNo);
		clickElement(driver, saveBtnUpdateSmartNo);
		waitUntilInvisible(driver, toastMessage);
	}
	
	public String verifyUpdateSource(WebDriver driver, String text) {
		waitUntilInvisible(driver, saveBtnUpdateSmartNo);
		waitUntilVisible(driver, offlineBackgridBody, 5);
		waitUntilVisible(driver, advNoTableCustomPar, 10);
		idleWait(3);
		return getElementsText(driver, getElements(driver, tableSource).get(0));
	}
	
	public void clickAddAnotherOfflineTrack(WebDriver driver){
		waitUntilVisible(driver, addAnotherOfflineNo);
		clickElement(driver, addAnotherOfflineNo);
		idleWait(2);
	}
	
	public Pair<String,String> getNameWithSmartNoFirstIndex(WebDriver driver) {
		waitUntilVisible(driver, offlineBackgridBody, 5);
		waitUntilVisible(driver, advNoTableCustomPar, 10);
		idleWait(3);
		String nameTable = driver.findElements(advNoTableName).get(0).getText();
		String smartNumber = driver.findElements(advNoTableSmartNos).get(0).getText();
		Pair<String,String> pair = new Pair<String, String>(nameTable, smartNumber);
		return pair;
	}
	
	/******* Offline Call Tracking functions ends here *******/
	
	/*******Verify Tracking number data start here*******/
	public String getAdvTrackNameFromTable(WebDriver driver, int index){
		waitUntilVisible(driver, advNoTableName);
		return getElements(driver, advNoTableName).get(index).getText();
	}
	
	public String getAdvTrackCampaignFromTable(WebDriver driver, int index){
		waitUntilVisible(driver, advNoTableCampaign);
		return getElements(driver, advNoTableCampaign).get(index).getText();
	}
	
	public String getAdvTrackNumberFromTable(WebDriver driver, int index){
		waitUntilVisible(driver, advNoTableSmartNos);
		return getElements(driver, advNoTableSmartNos).get(index).getText();
	}
	
	public int getAdvTrackNumberIndex(WebDriver driver, String Name, String callFlow){
		List<WebElement> advTrackingNames 	= getElements(driver, advNoTableName);
		List<WebElement> advTrackingCallFlows = getInactiveElements(driver, advNoTableCallFlow);
		for(int i = 0; i < advTrackingNames.size(); i++){
			if(advTrackingNames.get(i).getText().equals(Name) && advTrackingCallFlows.get(i).getText().equals(callFlow)){
				return i;
			}
		}
		return -1;
	}
	
	
	/*******Verify Tracking number data end here*******/
	
	/*******Add new Tracking number data Start here*******/
	public void enterNewAdvTrackName(WebDriver driver, String name){
		waitUntilVisible(driver, newAdvTrackName);
		enterText(driver, newAdvTrackName, name);
	}
	
	public void enterNewAdvTrackCustomParam(WebDriver driver, String param){
		waitUntilVisible(driver, newAdvTrackCustomParam);
		enterText(driver, newAdvTrackCustomParam, param);
	}
	
	public void enterNewAdvTrackAreaCode(WebDriver driver, String areaCode){
		waitUntilVisible(driver, newAdvTrackAreaCode);
		enterText(driver, newAdvTrackAreaCode, areaCode);
	}
	
	public void selectNewAdvTrackSmartNoType(WebDriver driver, String smartNoType){
		waitUntilVisible(driver, newAdvTrackSmartNoType);
		selectFromDropdown(driver, newAdvTrackSmartNoType, SelectTypes.visibleText, smartNoType);
		dashBoard.isPaceBarInvisible(driver);
		if(smartNoType.equals(AdminCallTracking.newAdvTrackSmartNoTypes.UseExisting.displayName())) {
			waitUntilVisible(driver, newAdvTrackSmartAssignNo);
			List<WebElement> assignNo = getElements(driver, newAdvTrackSmartAssignNo);
			waitUntilVisible(driver, assignNo.get(0));
			clickElement(driver, assignNo.get(0));
		}
	}
	
	public void selectSourceType(WebDriver driver, String sourceType){
		waitUntilVisible(driver, offlineTrackSourceTab);
		waitUntilClickable(driver, offlineTrackSourceTab);
		enterText(driver, offlineTrackSourceTab, sourceType);
	}
		
	public void selectNewAdvTrackCallFlow(WebDriver driver, String callFlow){
		waitUntilVisible(driver, newAdvTrackCallFlow);
		clickElement(driver, newAdvTrackCallFlow);
		enterBackspace(driver, newAdvTrackCallFlow);
		enterText(driver, newAdvTrackCallFlow, callFlow);
		idleWait(2);
		List<WebElement> searchedCallFlows = getElements(driver, callFlowSelectDropDown);
		for (WebElement callItem : searchedCallFlows) {
			if(callItem.getText().equals(callFlow)){
				callItem.click();
				break;
			}
		}
	}
	
	public void selectMultipleTrackAutoCompleteDropDown(WebDriver driver, String trackToSelect, String textToEnter) {
		By locatorList = null;
		By locatorListDropDown = null;
		try {
			switch (trackToSelect) {
			case "CallFlow":
				locatorList = newAdvTrackCallFlow;
				locatorListDropDown = callFlowSelectDropDown;
				break;
			case "SfdcCampaign":
				locatorList = newadvTrackSfdcCampaign;
				locatorListDropDown = campaignSelectDropDown;
				break;
			default:
				break;
			}
			List<WebElement> callflowList = getElements(driver, locatorList);
			for (int i = 0; i <= callflowList.size() - 1; i++) {
				clickElement(driver, callflowList.get(i));
				enterBackspace(driver, callflowList.get(i));
				enterText(driver, callflowList.get(i), textToEnter);
				idleWait(2);
				List<WebElement> searchedCallFlows =  driver.findElements(locatorListDropDown);
				for (WebElement callItem : searchedCallFlows) {
					if (callItem.getText().equals(textToEnter)) {
						callItem.click();
						if (trackToSelect.equals("SfdcCampaign"))
							confirmSFDCBtn(driver);
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void confirmSFDCBtn(WebDriver driver) {
		if (isElementVisible(driver, newAdvTrackconfirmbtn, 2)) {
			clickElement(driver, newAdvTrackconfirmbtn);
			waitUntilInvisible(driver, newAdvTrackconfirmbtn);
		}
	}
	
	public void selectNewAdvTrackSFDCCampaign(WebDriver driver, String sfdcCampaign) {
		waitUntilVisible(driver, newadvTrackSfdcCampaign);
		clickElement(driver, newadvTrackSfdcCampaign);
		enterBackspace(driver, newadvTrackSfdcCampaign);
		enterText(driver, newadvTrackSfdcCampaign, sfdcCampaign);
		idleWait(2);
		List<WebElement> campaignFlows = driver.findElements(campaignSelectDropDown);
		for (WebElement callItem : campaignFlows) {
			if (callItem.getText().equals(sfdcCampaign)) {
				callItem.click();
				break;
			}
		}
		confirmSFDCBtn(driver);
	}
	
	public void selectMultipleTrackSmartNoType(WebDriver driver, String smartNoType){
		waitUntilVisible(driver, newAdvTrackSmartNoType);
		selectFromDropdownList(driver,  newAdvTrackSmartNoType, SelectTypes.visibleText, smartNoType);
	}
	
	public void clickAddNewAdvTrackNobtn(WebDriver driver){
		waitUntilVisible(driver, addTrackingNumberIcon);
		clickElement(driver, addTrackingNumberIcon);
		waitUntilVisible(driver, newAdvTrackName);
	}
	
	public void clickAddAnotherTrackNoBtn(WebDriver driver){
		waitUntilVisible(driver, addAnotherAdvancedNo);
		clickElement(driver, addAnotherAdvancedNo);
		idleWait(2);
	}
	
	public void isRemoveButtonInvisible(WebDriver driver){
		waitUntilInvisible(driver, newadvTrackDeleteButton);
	}
	
	public int getRemoveButtonsCount(WebDriver driver){
		return getElements(driver, newadvTrackDeleteButton).size();
	}
	
	public void clickRemoveButton(WebDriver driver, int index){
		getElements(driver, newadvTrackDeleteButton).get(index).click();
		clickElement(driver, newAdvTrackconfirmbtn);
		waitUntilInvisible(driver, newAdvTrackconfirmbtn);
	}
	
	public void clickFinishButton(WebDriver driver){
		waitUntilVisible(driver, newAdvTrackFinishButton);
		clickElement(driver, newAdvTrackFinishButton);
		waitUntilInvisible(driver, newAdvTrackFinishButton);
		waitUntilInvisible(driver, newAdvTrackName);
		waitUntilVisible(driver, addTrackingNumberIcon);
		waitUntilInvisible(driver, toastMessage);
	}
	
	public void enterCountryName(WebDriver driver, String countryName) {
		assertEquals(getElementsText(driver, countryInputBox), "United States");
		waitUntilVisible(driver, countryInputBox);
		clickElement(driver, countryInputBox);
		enterBackspace(driver, countryInputTab);
		enterText(driver, enterCountryInputTab, countryName);
		idleWait(2);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, countryDropDownItems);
		for (WebElement searchedItem : searchedDropDownItems) {
			if (searchedItem.getText().trim().contains(countryName.trim())) {
				searchedItem.click();
				break;
			}
		}
	}
	
	public void isFieldDisabled(WebDriver driver) {
		waitUntilVisible(driver, newAdvTrackSmartNoType, 6);
		assertTrue(isAttributePresent(driver, newAdvTrackSmartNoType, "disabled"));
		assertEquals(getAttribue(driver, newAdvTrackSmartNoType, ElementAttributes.value), "Local", "Default value is not local");
		waitUntilVisible(driver, newAdvTrackAreaCode, 6);
		assertTrue(isAttributePresent(driver, newAdvTrackAreaCode, "disabled"));
	}
	
	public void enterNewAdvTrackingNumberData(WebDriver driver, HashMap<trackingNoFields, String> newTrackingNodata) {
		
		if (newTrackingNodata.get(trackingNoFields.NameCustom) != null) {
			enterCustomTrackNameCustom(driver, newTrackingNodata.get(trackingNoFields.NameCustom));
		}
		
		if (newTrackingNodata.get(trackingNoFields.Country) != null) {
			assertEquals(getElementsText(driver, countryInputBox), "United States");
			enterCountryName(driver,  newTrackingNodata.get(trackingNoFields.Country));
		}
		
		if (newTrackingNodata.get(trackingNoFields.OriginatingUrl) != null) {
			enterCustomTrackOrigntngUrl(driver, newTrackingNodata.get(trackingNoFields.OriginatingUrl));
		}
		
		if (newTrackingNodata.get(trackingNoFields.DestinationUrl) != null) {
			enterCustomTrackDestintnUrl(driver, newTrackingNodata.get(trackingNoFields.DestinationUrl));
		}
		
		if (newTrackingNodata.get(trackingNoFields.Name) != null) {
			enterNewAdvTrackName(driver, newTrackingNodata.get(trackingNoFields.Name));
		}
		
		if (newTrackingNodata.get(trackingNoFields.Source) != null) {
			idleWait(2);
			selectSourceType(driver, newTrackingNodata.get(trackingNoFields.Source));
		}

		if (newTrackingNodata.get(trackingNoFields.CustomParameterValue) != null) {
			enterNewAdvTrackCustomParam(driver, newTrackingNodata.get(trackingNoFields.CustomParameterValue));
		}
		

		if (newTrackingNodata.get(trackingNoFields.SmartNumberType) != null) {
			selectNewAdvTrackSmartNoType(driver, newTrackingNodata.get(trackingNoFields.SmartNumberType));
		}
		
		if (newTrackingNodata.get(trackingNoFields.AreaCode) != null) {
			enterNewAdvTrackAreaCode(driver, newTrackingNodata.get(trackingNoFields.AreaCode));
		}

		if (newTrackingNodata.get(trackingNoFields.CallFlow) != null) {
			selectNewAdvTrackCallFlow(driver, newTrackingNodata.get(trackingNoFields.CallFlow));
		}

		if (newTrackingNodata.get(trackingNoFields.SfdcCampaign) != null) {
			idleWait(2);
			selectNewAdvTrackSFDCCampaign(driver, newTrackingNodata.get(trackingNoFields.SfdcCampaign));
		}
	}

	public void selectMultipleTrackingNumberData(WebDriver driver, HashMap<trackingNoFields, String> trackingdata) {
		
		if (trackingdata.get(trackingNoFields.SmartNumberType) != null) {
			selectMultipleTrackSmartNoType(driver, trackingdata.get(trackingNoFields.SmartNumberType));
		}
		
		if (trackingdata.get(trackingNoFields.CallFlow) != null) {
			selectMultipleTrackAutoCompleteDropDown(driver, trackingNoFields.CallFlow.name(), trackingdata.get(trackingNoFields.CallFlow));
		}
		
		if (trackingdata.get(trackingNoFields.SfdcCampaign) != null) {
			selectMultipleTrackAutoCompleteDropDown(driver, trackingNoFields.SfdcCampaign.name(), trackingdata.get(trackingNoFields.SfdcCampaign));
		}
	}
	
	public void clickAddAddtionalNumberLink(WebDriver driver){	
		clickAddAnotherTrackNoBtn(driver);
		idleWait(5);
	}
	
	public void verifyMandatoryFields(WebDriver driver){
		clickAddNewAdvTrackNobtn(driver);
		waitUntilVisible(driver, newAdvTrackFinishButton);
		clickElement(driver, newAdvTrackFinishButton);
		waitUntilVisible(driver, errorTrackNameTextBox);
		waitUntilVisible(driver, errorTrackCustomParam);
		waitUntilVisible(driver, errorTrackCallFlow);
		waitUntilInvisible(driver, errorTrackAreaCode);
		waitUntilVisible(driver, errorTrackSmartNoType);
		waitUntilInvisible(driver, errorTrackSfdcCampaign);
		closeAddAdvTrackingWindow(driver);
	}
	
	public void closeAddAdvTrackingWindow(WebDriver driver){
		waitUntilVisible(driver, closeBtnForAddTrackNo);
		clickElement(driver, closeBtnForAddTrackNo);
		waitUntilInvisible(driver, closeBtnForAddTrackNo);
	}
	/*******Add new Tracking number data End here*******/
	
	/******* Clean Up Methods *******/

	public List<String> deleteSmartNumbersOfDomainName(WebDriver driver, String domainName) {

		By smartListLoc = By.xpath(smartNumberList.replace("$$domainName$$", domainName));
		List<WebElement> smartList = getElements(driver, smartListLoc);
		List<String> smartNoList = getTextListFromElements(driver, smartListLoc);
		int i = smartList.size() - 1;
			while (i >= 0) {
				smartNoPage.deleteSmartNumberComplete(driver, i);
				driver.navigate().back();
				dashBoard.isPaceBarInvisible(driver);
				idleWait(2);
				i--;
			}
			return smartNoList;
	}
	
	/**
	 * @param driver
	 * remove assigned smart number
	 */
	public void removeAssignedSmartNumber(WebDriver driver) {
		waitUntilVisible(driver, newAdvDeleteSmartNo);
		clickElement(driver, newAdvDeleteSmartNo);
	}
}
