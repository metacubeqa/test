/**
 * 
 */
package softphone.source.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import base.SeleniumBase;
import softphone.source.callTools.SoftPhoneNewTask.taskRelatedRecordsType;
import softphone.source.salesforce.TaskDetailPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallToolsPanel extends SeleniumBase{
	
	TaskDetailPage taskDetailPage = new TaskDetailPage();
	HelperFunctions helperFunctions = new HelperFunctions();
	
	//voicemail related locators
	By voicemailIcon 						= By.className("call-automated-voicemail");
	By firstVoiceMail 						= By.cssSelector(".selectize-dropdown.single.automated-voicemail .option.active");
	By voicemailNameList					= By.cssSelector(".selectize-dropdown.single.automated-voicemail .option");
	String voicemailByName					= ".//*[@id='automatedVoicemail'] //*[@class='option' and text()=\"$$VoiceMail$$\"]";
	By taskIcon 							= By.cssSelector(".new-task-container");

	//Call Notes selectors
	By callToolsMenuIcon					= By.className("call-tools-menu");
	By callNotesSection						= By.id("call-tools");
	By callNotesIcon 						= By.cssSelector(".call-notes-container");
	By callNotesTextBox 					= By.cssSelector("textarea.notes-text, textarea[placeholder=\"Enter your Notes...\"]");
	By callNotesTemplatePicker 				= By.cssSelector(".note-templates-control .selectize-input, [id *= 'CallNotesTemplate'] .selectize-input");
	By callNotesSelectedItem 				= By.cssSelector(".call-note-templates-select .item, [id *= 'CallNotesTemplate'] .item");
	By callNotesSubject 					= By.cssSelector("input.subject-select:not([style=\"display: none;\"]), input.subject-input:not([style=\"display: none;\"])");
	By postTochatterCheckBox 				= By.cssSelector("#post-chatter[type='checkbox']");
	By callNotesSaveButton 					= By.cssSelector(".ladda-button.save,button.save");
	By callNotesCancelButton 				= By.cssSelector(".cancel");
	By callNoteTemplatePickerDropDownItems 	= By.cssSelector(".note-templates-control .selectize-dropdown-content .option, [id *= 'CallNotesTemplate'] .selectize-dropdown .option");
	By callToolsUnsavedChangesText			= By.cssSelector(".unsaved-changes");
	
	//Custom Link Locators
	By customLinkIcon 						= By.className("call-custom-link");
	By customLinkInputTab					= By.cssSelector(".custom-link.single input");
	By customLinkList						= By.cssSelector(".custom-link.selectize-dropdown span");
	String customLinkLocText 				= ".//*[@id='customLink']//*[contains(@class, 'custom-link') and contains(@class, 'selectize-dropdown')]//span[text()='$$CustomLink$$']";
	
	//Related Record Locators
	By relatedRecordsIcon 					= By.cssSelector(".related-records-container");
	By relatedRecordSearchLink 				= By.cssSelector("[data-action='search-related-records']");
	By relatedRecordsSearchBox 				= By.cssSelector("#relatedRecords input.search-term");
	By relatedRecordsSearchButton 			= By.cssSelector("button[data-action='search']");
	By relatedRecordsSearchResults 			= By.cssSelector("#relatedRecords .search-results [data-action='select']");
	By salesforceCampaignHeader 			= By.className("pageDescription");
	By relatedRecordFavButton 				= By.cssSelector("#relatedRecords .create:not([style='display: none;']), #relatedRecords .delete:not([style='display: none;'])");
	By selectedRelatedRecord 				= By.cssSelector(".relatedRecordName.selected");
	String taskCaseRecordToSelect			= ".//*[@data-type='Case']/following-sibling::div//*[text()='$$Case$$']";
	String taskCampaignToSelect				= ".//*[@data-type='Campaign']/following-sibling::div//*[text()='$$Campaign$$']";
	String favBtnForRelatedCampaign 		= ".//*[@data-type='Campaign']/following-sibling::div//*[text()='$$Campaign$$']/preceding-sibling::div//*[@class='create']";
	String campaignSalesforceLink 			= ".//*[@data-type='Campaign']/following-sibling::div//*[text()='$$Campaign$$']/following-sibling::img[contains(@class, 'showSalesforceRecord')]";
	String taskRelatedFavoriteRecord 		= ".//*[@data-type='Favorites']/following-sibling::div//*[text()='$$FavoriteRecord$$']";
	String taskAccountRecordToSelect 		= ".//*[@data-type='Account']/following-sibling::div//*[text()='$$Account$$']";
	String taskPropertyRecordToSelect 		= ".//*[@data-type='PropertyQA__c']/following-sibling::div//*[text()='$$Account$$']";
	String taskOpportunityRecordToSelect 	= ".//*[@data-type='Opportunity']/following-sibling::div//*[text()='$$Opportunity$$']";
	String selectedfavBtnForRelatedCampaign = ".//*[@data-type='Favorites']/following-sibling::div//*[text()='$$Campaign$$']/preceding-sibling::div//*[@class='delete']";
	
	//Call Disposition locators
	By callDispositionIcon 					= By.cssSelector("[title='Disposition'].inactive");
	By callDispositionActiveIcon 			= By.cssSelector("[title='Disposition'].active");
	By dispositionList 						= By.cssSelector(".selectize-dropdown.single.call-disposition .option");
	By dispositionSearchBox 				= By.cssSelector("#disposition [placeholder='Disposition']");
	By callTooldDispositionDropDown			= By.cssSelector("[id*='Disposition'] div.sfdc-field-value-container");	
	String dispositionOption 				= ".selectize-dropdown.single.call-disposition .option[data-value='$$Disposition$$']";
	String callToolsDispositionOption		= "[id*='Disposition'] .option[data-value='$$Disposition$$']";
	
	//Call Ratings locators
	By callRatingsIcon 						= By.cssSelector("[title='Call Rating'].inactive");
	By callRatings 							= By.cssSelector("#rating a[data-action='select'], .rating-item");
	
	//Call Script Locators
	By callScriptIcon						= By.cssSelector(".call-scripts-container");
	By callScriptText						= By.cssSelector("#scripts .script");
	
	By eventTabTextBox 						= By.cssSelector(".custom-combobox input");
	
	//Coach section selectors
	By coachIcon 							= By.cssSelector(".cai-flag-call");

	// Call Notes Selectors
	By callNotesDropDownItems 				= By.cssSelector("ul[class*='subject-notes-options'] li");
	
	/*******VoiceMail section starts here*******/
	public void clickVoiceMailIcon(WebDriver driver){
		idleWait(2);
		waitUntilVisible(driver, voicemailIcon);
		clickElement(driver, voicemailIcon);
	}
	
	public List<String> getVoicemailDropLists(WebDriver driver){
		return getTextListFromElements(driver, voicemailNameList);
	}
	
	public void selectFirstVoiceMail(WebDriver driver){
		waitUntilVisible(driver, firstVoiceMail);
		clickElement(driver, firstVoiceMail);
		waitUntilInvisible(driver, firstVoiceMail);
	}
	
	public void selectVoicemailByName(WebDriver driver, String voiceMailName){
		By voiceMailLoc	= By.xpath(voicemailByName.replace("$$VoiceMail$$", voiceMailName));
		waitUntilVisible(driver, voiceMailLoc);
		clickElement(driver, voiceMailLoc);
		waitUntilInvisible(driver, voiceMailLoc);
	}
	
	public String dropFirstVoiceMail(WebDriver driver){
		clickVoiceMailIcon(driver);
		idleWait(2);
		String voicemailDropped = getElementsText(driver, firstVoiceMail);
		selectFirstVoiceMail(driver);
		return voicemailDropped;
	}
	
	public void dropVoiceMailByName(WebDriver driver, String voiceMailName){
		clickVoiceMailIcon(driver);
		idleWait(2);
		selectVoicemailByName(driver, voiceMailName);
	}
	/*******VoiceMail section ends here*******/
	
	/*******Call Notes section starts here*******/
	public void callNotesSectionVisible(WebDriver driver){
		waitUntilVisible(driver, callNotesSection);
	}
	
	public void clickCallNotesIcon(WebDriver driver){
		waitUntilVisible(driver, callNotesIcon);
		clickElement(driver, callNotesIcon);
	}
	
	public void enterCallNotesSubject(WebDriver driver, String notesSubject){
		waitUntilVisible(driver, callNotesSubject);
		enterText(driver, callNotesSubject, notesSubject);
	}
	
	public String getCallNotesSubject(WebDriver driver){
		waitUntilVisible(driver, callNotesSubject);
		return getAttribue(driver, callNotesSubject, ElementAttributes.value);
	}
	
	public void enterCallNotesText(WebDriver driver, String callNotes){
		waitUntilVisible(driver, callNotesTextBox);
		enterText(driver, callNotesTextBox, callNotes);
	}
	
	public void clearCallNotesText(WebDriver driver){
		clickElement(driver, callNotesTextBox);
		enterBackspace(driver, callNotesTextBox);
		findElement(driver, callNotesTextBox).clear();
	}
	
	public void appendCallNotesSubject(WebDriver driver, String notesSubject){
		appendText(driver, callNotesSubject, notesSubject);
	}
	
	public void appendCallNotesText(WebDriver driver, String callNotes){
		appendText(driver, callNotesTextBox, callNotes);
	}
	
	public String getCallNotesText(WebDriver driver){
		waitUntilVisible(driver, callNotesTextBox);
		return getAttribue(driver, callNotesTextBox, ElementAttributes.value);
	}
	
	public void checkPostToChatter(WebDriver driver){
		waitUntilVisible(driver, postTochatterCheckBox);
		if(!findElement(driver, postTochatterCheckBox).isSelected()){
			clickElement(driver, postTochatterCheckBox);
		}
	}
	
	public void unCheckPostToChatter(WebDriver driver){
		waitUntilVisible(driver, postTochatterCheckBox);
		if(findElement(driver, postTochatterCheckBox).isSelected()){
			clickElement(driver, postTochatterCheckBox);
		}
	}
	
	public boolean isCheckPostToChatterSelected(WebDriver driver){
		waitUntilVisible(driver, postTochatterCheckBox);
		return findElement(driver, postTochatterCheckBox).isSelected();
	}
	
	public void isCheckPostToChatterInVisible(WebDriver driver){
		waitUntilInvisible(driver, postTochatterCheckBox);
	}
	
	public void clickCallNotesSaveBtn(WebDriver driver){
		waitUntilVisible(driver, callNotesSaveButton);
		clickElement(driver, callNotesSaveButton);
	}
	
	public void verifyUnsavedChangesText(WebDriver driver) {
		waitUntilVisible(driver, callToolsUnsavedChangesText);
		assertEquals(getElementsText(driver, callToolsUnsavedChangesText), "You have unsaved changes.");
		assertEquals(getCssValue(driver, findElement(driver,callToolsUnsavedChangesText), CssValues.Color), "rgba(255, 0, 0, 1)");
	}
	
	public void verifyUnsavedChangesTextNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, callToolsUnsavedChangesText);
	}
	
	public boolean getCallNotesSaveBtnStatus(WebDriver driver){
		return findElement(driver, callNotesSaveButton).isEnabled();
	}
	
	public void clickCallNotesCancelBtn(WebDriver driver){
		waitUntilVisible(driver, callNotesCancelButton);
		clickElement(driver, callNotesCancelButton);
	}
	
	public boolean checkCallNotesExistsInTemplatePicker(WebDriver driver, String callNoteName) {
		clickCallNotesIcon(driver);
		clickTemplatePicker(driver);
		return isTextPresentInList(driver, getElements(driver, callNoteTemplatePickerDropDownItems), callNoteName);
	}
	
	public void selectCallTemplate(WebDriver driver, String callNoteName) {
		clickCallNotesIcon(driver);
		clickTemplatePicker(driver);
		List<WebElement> callNotesTemplates = getElements(driver, callNoteTemplatePickerDropDownItems);
		for (WebElement callNotesTemplate : callNotesTemplates) {
			if(callNotesTemplate.getText().equals(callNoteName)){
				callNotesTemplate.click();
				break;
			}
		}
	}
	
	public void selectCallTemplateFromCallTools(WebDriver driver, String callNoteName) {
		clickCallToolsIcon(driver);
		clickTemplatePicker(driver);
		List<WebElement> callNotesTemplates = getElements(driver, callNoteTemplatePickerDropDownItems);
		for (WebElement callNotesTemplate : callNotesTemplates) {
			if(callNotesTemplate.getText().equals(callNoteName)){
				callNotesTemplate.click();
				break;
			}
		}
	}
	
	public void selectDispositionFromCallTools(WebDriver driver, String dispositiontext) {
		System.out.println("Selecting disposition: " + dispositiontext);
		clickElement(driver, callTooldDispositionDropDown);
		By disposition = By.cssSelector(callToolsDispositionOption.replace("$$Disposition$$", dispositiontext));
		clickElement(driver, disposition);
	}
	
	public String getSelectedTemplate(WebDriver driver){
		waitUntilVisible(driver, callNotesSelectedItem);
		return getElementsText(driver, callNotesSelectedItem);
	}
	
	public void clickTemplatePicker(WebDriver driver) {
		waitUntilVisible(driver, callNotesTemplatePicker);
		clickElement(driver, callNotesTemplatePicker);
	}
	
	public boolean isTemplatePickerVisible(WebDriver driver)
	{
		clickCallNotesIcon(driver);
		return isElementVisible(driver, callNotesTemplatePicker, 2);
	}
	public void clickCallToolsIcon(WebDriver driver){
		waitUntilVisible(driver, callToolsMenuIcon);
		clickElement(driver, callToolsMenuIcon);
	}
	
	public void enterCallNotes(WebDriver driver, String notesSubject, String callNotes){
		idleWait(1);
		clickCallNotesIcon(driver);
		enterCallNotesSubject(driver, notesSubject);
		enterCallNotesText(driver, callNotes);
		idleWait(1);
		clickCallNotesSaveBtn(driver);
	}
	
	public void changeCallSubject(WebDriver driver, String notesSubject){
		idleWait(1);
		clickCallNotesIcon(driver);
		enterCallNotesSubject(driver, notesSubject);
		idleWait(1);
		clickCallNotesSaveBtn(driver);
	}
	
	public String changeAndGetCallSubject(WebDriver driver) {
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		changeCallSubject(driver, callSubject);
		return callSubject;
	}
	
	public void postToChatter(WebDriver driver, String notesSubject, String callNotes){
		idleWait(1);
		clickCallNotesIcon(driver);
		enterCallNotesSubject(driver, notesSubject);
		enterCallNotesText(driver, callNotes);
		idleWait(1);
		checkPostToChatter(driver);
		clickCallNotesSaveBtn(driver);
	}
	
	public boolean checkTask_NoteSubjectExistsInCallNotes(WebDriver driver, String taskNoteSubject) {
		idleWait(1);
		clickCallNotesIcon(driver);
		waitUntilVisible(driver, eventTabTextBox);
		clickElement(driver, eventTabTextBox);
		isListElementsVisible(driver, callNotesDropDownItems, 5);
		return isTextPresentInList(driver, getInactiveElements(driver, callNotesDropDownItems), taskNoteSubject);
	}
	/*******Call Notes section ends here*******/
	
	/*******custom Links section starts here*******/
	public void clickCustomLinkIcon(WebDriver driver){
		System.out.println("Clicking Custom Link Icon");
		waitUntilVisible(driver, relatedRecordsIcon);
		clickElement(driver, customLinkIcon);
	}
	
	public void enterCustomLink(WebDriver driver, String customLink){
		clickCustomLinkIcon(driver);
		waitUntilVisible(driver, customLinkInputTab);
		enterText(driver, customLinkInputTab, customLink);
	}
	
	public List<String> getCustomLinksList(WebDriver driver){
		return getTextListFromElements(driver, customLinkList);
	}
	
	public void selectCustomLinkByText(WebDriver driver, String customLink){
		System.out.println("Clicking on Custom Link " + customLink);
		By customLinkLoc = By.xpath(customLinkLocText.replace("$$CustomLink$$", customLink));
		waitUntilVisible(driver, customLinkLoc);
		idleWait(1);
		clickElement(driver, customLinkLoc);
	}
	
	public boolean checkCustomLinkExists(WebDriver driver, String customLink) {
		By customLinkLoc = By.xpath(customLinkLocText.replace("$$CustomLink$$", customLink));
		return isElementVisible(driver, customLinkLoc, 5);
	}
	
	public void verifyCustomLinkVisible(WebDriver driver) {
		waitUntilVisible(driver, customLinkIcon);
	}
	/*******Custom Links section ends here*******/
	
	/*******Related record section starts here*******/
	public void clickRelatedRecordsIcon(WebDriver driver){
		clickElement(driver, relatedRecordsIcon);
	}
	
	public void isRelatedRecordsIconVisible(WebDriver driver){
		waitUntilVisible(driver, relatedRecordsIcon);
	}
	
	public void selectCaseFromRelatedRecordSearchList(WebDriver driver, String relatedCase){
		By caseLocator = By.xpath(taskCaseRecordToSelect.replace("$$Case$$", relatedCase));
		waitUntilVisible(driver, caseLocator);
		clickElement(driver, caseLocator);
	}
	
	public void selectCampaignFromRelatedRecordSearchList(WebDriver driver, String relatedCampaign){
		By campaignLocator = By.xpath(taskCampaignToSelect.replace("$$Campaign$$", relatedCampaign));
		waitUntilVisible(driver, campaignLocator);
		clickElement(driver, campaignLocator);
	}
	
	public void selectCampaignFromFavRecords(WebDriver driver, String relatedCampaign){
		By favRecordLocator = By.xpath(taskRelatedFavoriteRecord.replace("$$FavoriteRecord$$", relatedCampaign));
		waitUntilVisible(driver, favRecordLocator);
		clickElement(driver, favRecordLocator);
	}
	
	public void selectOpportunityFromRelatedRecordSearchList(WebDriver driver, String relatedOpportunity){
		By opportunityLocator = By.xpath(taskOpportunityRecordToSelect.replace("$$Opportunity$$", relatedOpportunity));
		waitUntilVisible(driver, opportunityLocator);
		clickElement(driver, opportunityLocator);
	}
	
	public void verifyOpportunityInSuggestion(WebDriver driver, String relatedOpportunity){
		By opportunityLocator = By.xpath(taskOpportunityRecordToSelect.replace("$$Opportunity$$", relatedOpportunity));
		waitUntilVisible(driver, opportunityLocator);
	}
	
	public void selectAccountFromRelatedRecordSearchList(WebDriver driver, String relatedOpportunity){
		By accountLocator = By.xpath(taskAccountRecordToSelect.replace("$$Account$$", relatedOpportunity));
		waitUntilVisible(driver, accountLocator);
		clickElement(driver, accountLocator);
	}
	
	public void selectPropertyFromRelatedRecordSearchList(WebDriver driver, String relatedOpportunity){
		By propertyLocator = By.xpath(taskPropertyRecordToSelect.replace("$$Account$$", relatedOpportunity));
		waitUntilVisible(driver, propertyLocator);
		clickElement(driver, propertyLocator);
	}
	
	public void selectFavoriteButton(WebDriver driver, String relatedCampaign){
		By campaignFavButtonLocator = By.xpath(favBtnForRelatedCampaign.replace("$$Campaign$$", relatedCampaign));
		if(isElementVisible(driver, campaignFavButtonLocator, 5)){
			clickElement(driver, campaignFavButtonLocator);
		}
	}
	
	public void deselectFavoriteButton(WebDriver driver, String relatedCampaign){
		By campaignFavButtonLocator = By.xpath(selectedfavBtnForRelatedCampaign.replace("$$Campaign$$", relatedCampaign));
		if(isElementVisible(driver, campaignFavButtonLocator, 0)){
			waitUntilVisible(driver, campaignFavButtonLocator);
			clickElement(driver, campaignFavButtonLocator);
		}
	}
	
	public void isRecordFavorite(WebDriver driver, String favoriteRecord){
		By favRecordLocator = By.xpath(taskRelatedFavoriteRecord.replace("$$FavoriteRecord$$", favoriteRecord));
		waitUntilVisible(driver, favRecordLocator);
	}
	
	public void isRecordRemovedFromFavorite(WebDriver driver, String favoriteRecord){
		By favRecordLocator = By.xpath(taskRelatedFavoriteRecord.replace("$$FavoriteRecord$$", favoriteRecord));
		waitUntilInvisible(driver, favRecordLocator);
	}
	
	public void searchRelatedRecords(WebDriver driver, String recordType,String searchText){
		if(isElementVisible(driver, relatedRecordSearchLink, 0))
			clickElement(driver, relatedRecordSearchLink);
		enterText(driver, relatedRecordsSearchBox, searchText);
		clickElement(driver, relatedRecordsSearchButton);
	}
	
	public boolean isFavButtonVisible(WebDriver driver){
		return isElementVisible(driver, relatedRecordFavButton, 5);
	}	
	
	public void removeCampaignAsFavorite(WebDriver driver, String relatedCampaign){
		clickElement(driver, relatedRecordsIcon);
		if(isElementVisible(driver, relatedRecordSearchLink, 0))
			clickElement(driver, relatedRecordSearchLink);
		deselectFavoriteButton(driver, relatedCampaign);
	}
	
	public void clickDispositionIcon(WebDriver driver){
		if(isElementInvisible(driver, callDispositionActiveIcon, 0)) {
			clickElement(driver, callDispositionIcon);	
		}
	}
	
	public void closeDispositionMenu(WebDriver driver){
		clickElement(driver, callDispositionActiveIcon);
	}
	
	public String selectDisposition(WebDriver driver, int dispostionIndex){
		clickDispositionIcon(driver);
		String dispositiontext = getElements(driver, dispositionList).get(dispostionIndex).getText();
		getElements(driver, dispositionList).get(dispostionIndex).click();
		return dispositiontext;
	}
	
	public void selectDispositionUsingText(WebDriver driver, String dispositiontext){
		System.out.println("Selecting disposition: " + dispositiontext);
		clickDispositionIcon(driver);
		By disposition = By.cssSelector(dispositionOption.replace("$$Disposition$$", dispositiontext));
		clickElement(driver, disposition);
	}
	
	public List<String> getSoftphoneDispostionList(WebDriver driver){
		return getTextListFromElements(driver, dispositionList);
	}
	
	public void verifyDispositionSerchBoxVisible(WebDriver driver){
		waitUntilVisible(driver, dispositionSearchBox);
	}
	
	public void linkRelatedRecordFromFavorties(WebDriver driver, String campaign){
		clickRelatedRecordsIcon(driver);
		if(isElementVisible(driver, relatedRecordSearchLink, 0))
			clickElement(driver, relatedRecordSearchLink);
		isRecordFavorite(driver, campaign);
	    selectCampaignFromFavRecords(driver, campaign);
	}
	
	public void selectAccountFromSuggestions(WebDriver driver, String account){
		clickRelatedRecordsIcon(driver);
		selectAccountFromRelatedRecordSearchList(driver, account);
	}
	
	public void selectCampaignAsFavorite(WebDriver driver, String relatedCampaign){
		clickElement(driver, relatedRecordsIcon);
		if(isElementVisible(driver, relatedRecordSearchLink, 0))
			clickElement(driver, relatedRecordSearchLink);
		enterText(driver, relatedRecordsSearchBox, relatedCampaign);
		clickElement(driver, relatedRecordsSearchButton);
		selectFavoriteButton(driver, relatedCampaign);
	}
	
	public void linkRelatedRecords(WebDriver driver, String recordType, String recordName){
		clickElement(driver, relatedRecordsIcon);
		searchRelatedRecords(driver, recordType, recordName);
		if (recordType.equals(taskRelatedRecordsType.Case.toString())) {
			selectCaseFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Campaign.toString())) {
			selectCampaignFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Opportunity.toString())) {
			selectOpportunityFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Account.toString())) {
			selectAccountFromRelatedRecordSearchList(driver, recordName);
		} else if (recordType.equals(taskRelatedRecordsType.Property.toString())) {
			selectPropertyFromRelatedRecordSearchList(driver, recordName);
		}
	}
	
	public void verifyRelatedRecordIsSelected(WebDriver driver, String recordName){
		clickElement(driver, relatedRecordsIcon);
		waitUntilVisible(driver, selectedRelatedRecord);
		String expectedRelatedRecord = getElementsText(driver, selectedRelatedRecord);
		clickElement(driver, relatedRecordsIcon);
		assertEquals(expectedRelatedRecord, recordName);
	}
	
	public void verifyRelatedCampaignOnsalesforce(WebDriver driver, String campaignName){
		By campaignSalesforceLinkButton =  By.xpath(campaignSalesforceLink.replace("$$Campaign$$", campaignName));
		clickElement(driver, relatedRecordsIcon);
		searchRelatedRecords(driver, taskRelatedRecordsType.Campaign.toString(), campaignName);
		waitUntilVisible(driver, campaignSalesforceLinkButton);
		clickByJs(driver, campaignSalesforceLinkButton);
		switchToSFTab(driver, getTabCount(driver));
		assertEquals(getElementsText(driver, salesforceCampaignHeader), campaignName);
		closeTab(driver);
		switchToTab(driver, getTabCount(driver));
	}
	/*******Related record section ends here*******/
	
	/*******Rating section starts here*******/
	public void clickRatingIcon(WebDriver driver){
		clickElement(driver, callRatingsIcon);
	}
	
	public void giveCallRatings(WebDriver driver, int ratings){
		clickRatingIcon(driver);
		clickRatings(driver, ratings);
	}
	
	public void clickRatings(WebDriver driver, int ratings){
		getElements(driver, callRatings).get(ratings-1).click();
	}
	/*******Rating section ends here*******/
	
	/*******Call Scripts Starts here*******/	
	public String getCallScripts(WebDriver driver){
		isRelatedRecordsIconVisible(driver);
		waitUntilVisible(driver, callScriptIcon);
		clickElement(driver, callScriptIcon);
		waitUntilVisible(driver, callScriptText);
		String callScript = getAttribue(driver, callScriptText, ElementAttributes.value);
		clickElement(driver, callScriptIcon);
		return callScript;
	}
	
	public void verifyCallScriptsIcon(WebDriver driver) {
		File actualImage = HelperFunctions.captureElementScreenShot(driver, findElement(driver, callScriptIcon));
		File expectedImage = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\imageFiles\\CallScripts.png"));
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, expectedImage));

	}
	/*******Call Scripts ends here*******/
	
	/*******Coach Icon Function starts here*******/

	public void clickCoachIcon(WebDriver driver) {
		waitUntilVisible(driver, coachIcon);
		clickElement(driver, coachIcon);
	}

	public boolean isCoachIconVisible(WebDriver driver) {
		return isElementVisible(driver, coachIcon, 5);
	}

	public void verifyCoachIconColorSelected(WebDriver driver) {
		waitUntilVisible(driver, coachIcon);
		idleWait(2);
		String bckgroundColor = getCssValue(driver, findElement(driver, coachIcon), CssValues.BackgroundColor);
		String bckgroundHexColor = Color.fromString(bckgroundColor).asHex();
		System.out.println(bckgroundHexColor);
		assertEquals(bckgroundHexColor, "#0066ff", "color does not match");
	}
	
	public void verifyCoachIconColorNotSelected(WebDriver driver) {
		waitUntilVisible(driver, coachIcon);
		idleWait(2);
		String bckgroundColor = getCssValue(driver, findElement(driver, coachIcon), CssValues.BackgroundColor);
		String bckgroundHexColor = Color.fromString(bckgroundColor).asHex();
		System.out.println(bckgroundHexColor);
		assertNotEquals(bckgroundHexColor, "#0066ff", "color does not match");
	}
	
	/*******Coach Icon Function ends here*******/
	
	public void verifyTask_EventsVisible(WebDriver driver) {
		waitUntilVisible(driver, taskIcon);
	}
	
	/**
	 * @param driver
	 * @return is call tools icon visible
	 */
	public boolean isCallToolsIconVisible(WebDriver driver){
		return isElementVisible(driver, callToolsMenuIcon, 6);
	}
}
