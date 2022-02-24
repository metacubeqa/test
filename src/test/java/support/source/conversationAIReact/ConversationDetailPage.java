/**
 * @author Abhishek Gupta
 *
 */
package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class ConversationDetailPage extends SeleniumBase{
	Dashboard dashboard = new Dashboard();
	
	//*******outer html of the elements starts here*******//
	String selectedLibraryHTML		= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" style=\"overflow: visible;\"><path d=\"M12 24c6.627 0 12-5.373 12-12S18.627 0 12 0 0 5.373 0 12s5.373 12 12 12z\" stroke=\"#7549EA\" fill=\"#7549EA\"></path><path d=\"M6.485 13.521l3.394 3.395 7.636-7.637\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String unSelectedLibraryHTML	= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"#FFFFFF\"><path d=\"M23.5 12c0 6.351-5.149 11.5-11.5 11.5S.5 18.351.5 12 5.649.5 12 .5 23.5 5.649 23.5 12z\" stroke=\"#5C5C5C\"></path><path d=\"M12 6v12M18 12H6\" stroke=\"#5C5C5C\" stroke-linecap=\"round\"></path></svg>";
	//*******outer html of the elements ends here*******//
	
	//*******Action Drop down locators starts here*******//
	By actionDropdownBtn			= By.xpath(".//button[text()='Actions']");
	By actionDropdownItems			= By.cssSelector("ul.dropdown-menu:not(.pull-right) button, ul.MuiMenu-list[role='menu'] li");
	By reviewerDropDownTextBox 		= By.cssSelector("#review-recording-modal input[type='text']");
	By reviewerDropDownEmailList    = By.xpath("//*[contains(@data-testid, 'autocomplete-option')]/span[2]");
	
	By confirmDeleteButton          = By.xpath("//button[text()= 'Delete']");

	By recipientDropDownTextBox 	= By.id("recipients");
	By noOptionDropdown 			= By.xpath("//div[@role='presentation']//div[contains(@class, 'noOptions')]");
	By recipientDropdownFirstOption	= By.id("recipients-option-0");

	By userImageAgentDropDown		= By.xpath("//img[contains(@src,'https://c.ap15.content.force.com/profilephoto/729') and @alt = 'user avatar']");
	
	By usersDropDownNameList 		= By.xpath("//*[contains(@data-testid, 'autocomplete-option')]/span[1]");
	By selectReviewCheckBox			= By.cssSelector("input.select-review-checkbox");
	By shareMessageTextBox		 	= By.id("message");
	By shareCallHeader				= By.xpath("//h4[text()='Share Call']");
	By shareCallButton				= By.xpath("//*[@data-testid='ui-dialog.primary-btn' and text()='Share Call']");
	By sendButton					= By.cssSelector("[name='buttons-container-submit']");
	By recipientHelperText			= By.cssSelector("#recipients-helper-text");
	String agentToDeleteSharedWith  = "//span[contains(text(),'$agentName$')]/parent::div[contains(@class, 'MuiChip-deletable')]/*[name()='svg' and contains(@class, 'deleteIcon')]/*[name()='path']";
	
	//*******Action Drop down locators ends here*******//
	
	//*******library modal page locators starts here*******//
	By libraryNamesLabel			= By.cssSelector(".MuiDialog-root .contentContainer");
	By librarySecltionContainer		= By.cssSelector(".MuiDialog-root .selectContainer svg");
	By saveButton					= By.xpath(".//button[text()='Save']");
	By cancelButton					= By.xpath(".//button[text()='Cancel']");
	String selectLibrary 			= "//div[@class='contentContainer']/span[text()='$libraryName$']/../..//div[@class='selectContainer']/*[local-name()='svg']";
	
	//*******library modal page locators ends here*******//
	
	//superviosr notes
	By addSuperVisorNotesIcon		= By.cssSelector("#supervisor-notes a");
	By editSuperVisorNotesButton	= By.cssSelector("#supervisor-notes button[name='edit-notes'] svg");
	By superVisorNotesText			= By.cssSelector("#supervisor-notes span");
	By notesInputTextArea			= By.cssSelector("#notes");
	By expandIconSuperVisNotes 		= By.cssSelector("#supervisor-notes [name='expand-note']");
	By collapseIconSuperVisNotes 	= By.cssSelector("#supervisor-notes [name='collapse-note']");
	By expandIconCallNotes 			= By.cssSelector("#call-notes [name='expand-note']");
	By collapseIconCallNotes 		= By.cssSelector("#call-notes [name='collapse-note']");
	
	//notes locators
	By addCallNotesIcon				= By.cssSelector("#call-notes a");
	By editCallNotesButton			= By.cssSelector("#call-notes button[name='edit-notes'] svg");
	By callnotesInputTextArea		= By.cssSelector("#notes");

	//*******other page locators starts here*******//
	By toastMsg						= By.cssSelector(".toast-message, #client-snackbar");

	//*******Talk Matrices page locators starts here*******//
	By agentMatrix					= By.xpath("(.//p[@color='primary'])[1]");
	By customerMatrix				= By.xpath("(.//p[@color='primary'])[2]");
	By agentMatrixMinute			= By.xpath("(.//span[text() = ' min']/..)[1]");
	By customerMatrixMinute			= By.xpath("(.//span[text() = ' min']/..)[2]");
	
	By talkRatioSection				= By.xpath(".//*[text()='Talk Ratio']/../../..");
	By avgTalkStreakSection			= By.xpath(".//*[text()='Average Talk Streak']/../../..");
	By longTalkStreakSection		= By.xpath(".//*[text()='Longest Talk Streak']/../../..");
	//*******Talk Matrices page locators ends here*******//
	
	//*******Assign tags locators starts here*******//
	By annotateBtn					= By.xpath(".//button[text()='Annotate']");
	By annotationAddField			= By.xpath(".//textarea[@placeholder='Add your annotation here.']");
	By addTopicsArrowIcon			= By.xpath(".//span[text()='Add Topics']/following-sibling::*");
	By backToCreateAnnotationIcon	= By.xpath(".//*[text()='Back to Create Annotation']");
	By createAnnotationBtn			= By.xpath(".//button[text()='Create']");
	By editAnnotationBtn			= By.xpath(".//button[text()='Edit']");
	By noAnnotationHeader1			= By.xpath(".//*[@id='annotations']//h1[text()='No Annotations']");
	By noAnnotationHeader2 			= By.xpath(".//*[@id='annotations']//span[text()='Annotations add value to a call and will appear here.']");
	By noAnnotationImage  			= By.xpath(".//*[@id='annotations']//h1[text()='No Annotations']/ancestor::div[contains(@style,'display: flex;')]//*[name()='path' and @fill = 'url(#prefix__pattern0)']");
	By seeMoreAnnotationLink		= By.xpath("//h5[@color='link' and text()='See More']");
	By seeLessAnnotationLink		= By.xpath("//h5[@color='link' and text()='See Less']");
	By pencilAnnotationIcons		= By.xpath("//div[@name='edit-annotation']");
	
	String tagThumbIcon				= ".//*[text()='$$TagName$$']/following-sibling::div/div[$$Index$$]";
	String createdAnnotationText	= ".//*[@id='annotations']//span[text()='$annotationName$']";
	String annotationCreatedUser	= "( .//*[@id='annotations']//span[text()='$annotationName$']/ancestor::div[contains(@style, 'background-color: rgb')]//span[@color='primary'])[$$Index$$]";
	String editCreatedAnnotation	= ".//*[@id='annotations']//span[text()='$annotationName$']/parent::span/parent::div/../../..//div[@name='edit-annotation']";
	String topicThumbsIconOuterHTML = ".//*[@id='annotations']//span[text()='$annotationName$']/ancestor::div[contains(@style, 'background-color: rgb')]//span[@color='primary' and text()='$topic$']/following-sibling::div/*[local-name()='svg']";
	
	String annotateThumbsUpOuterHTML	= "<svg width=\"13\" height=\"12\" viewBox=\"0 0 13 12\" fill=\"none\"><path d=\"M0.5 6.5498C0.5 6.41173 0.611928 6.2998 0.75 6.2998L2.25 6.29981C2.38807 6.29981 2.5 6.41173 2.5 6.54981L2.5 11.0498C2.5 11.1879 2.38807 11.2998 2.25 11.2998L0.75 11.2998C0.611929 11.2998 0.5 11.1879 0.5 11.0498L0.5 6.5498Z\" fill=\"#33CC99\" stroke=\"#33CC99\"></path><path d=\"M4.5 10.5996C4.5 10.8757 4.72386 11.0996 5 11.0996L10.0909 11.0996C10.2957 11.0996 10.4798 10.9747 10.5555 10.7844L12.1922 6.66918C12.4534 6.01258 11.9697 5.29961 11.263 5.29961L8.30019 5.29961L8.30019 2.09961C8.30019 1.54733 7.85248 1.09961 7.30019 1.09961L6.94476 1.09961C6.52983 1.09961 6.15801 1.35584 6.01028 1.74358L4.69658 5.19163C4.56662 5.53273 4.5 5.8947 4.5 6.25972L4.5 10.5996Z\" fill=\"#33CC99\" stroke=\"#33CC99\" stroke-linejoin=\"round\"></path></svg>";
	String annotateThumbsDownOuterHTML	= "<svg width=\"13\" height=\"12\" viewBox=\"0 0 13 12\" fill=\"none\"><path d=\"M12.5 5.84961C12.5 5.98768 12.3881 6.09961 12.25 6.09961H10.75C10.6119 6.09961 10.5 5.98768 10.5 5.84961L10.5 1.34961C10.5 1.21154 10.6119 1.09961 10.75 1.09961H12.25C12.3881 1.09961 12.5 1.21154 12.5 1.34961L12.5 5.84961Z\" fill=\"#FF9933\" stroke=\"#FF9933\"></path><path d=\"M8.5 1.7998C8.5 1.52366 8.27614 1.2998 8 1.2998H2.90908C2.70427 1.2998 2.52017 1.42471 2.44448 1.61502L0.807755 5.73024C0.546608 6.38684 1.03034 7.0998 1.73696 7.0998L4.6998 7.0998L4.6998 10.2998C4.6998 10.8521 5.14752 11.2998 5.6998 11.2998H6.05524C6.47017 11.2998 6.84199 11.0436 6.98972 10.6558L8.30342 7.20778C8.43338 6.86668 8.5 6.50471 8.5 6.13969V1.7998Z\" fill=\"#FF9933\" stroke=\"#FF9933\" stroke-linejoin=\"round\"></path></svg>";

	//*******Assign tags locators ends here*******//
	
	//*******other page locators starts here*******//
	By annotationTabLink			= By.cssSelector("button[data-testid='annotation-tab']");
	By annotationSpeakerDetail      = By.xpath("//button/span[not((@data-testid))]");
	By annotationCountOnTab			= By.xpath("//button[@data-testid='annotation-tab']//span[@color='contrast']");
	By callNotesText				= By.xpath(".//*[text() ='Notes']/following-sibling::div[2]//span");
	By markAsReviewedIcon			= By.xpath(".//button[text()='Mark as Reviewed']");
	String tagLocator 				= ".//*[text()='$$TagName$$']/..";
	String tagLocatorSentiment		= ".//*[text()='$$TagName$$']/following-sibling::div/*[local-name()='svg']";
	
	//transcript
	By transcriptSpeakerDetail      = By.cssSelector("span[data-testid='speaker-row-detail-seconds-undefined']");
	//*******other page locators ends here*******//
	
	//Details Section
	By callerDetailTab 		= By.xpath("//button[@data-testid='caller detail-tab']//h6[text()='Caller Detail']");
	By activityDetailTab 	= By.xpath("//button[@data-testid='activity detail-tab']//h6[text()='Activity Detail']");
	By opportunityDetailTab = By.xpath("//button[@data-testid='opportunity detail-tab']//h6[text()='Opportunity Detail']");
	By caseDetailTab 		= By.xpath("//button[@data-testid='case detail-tab']//h6[text()='Case Detail']");
	By accountDetailTab		= By.xpath("//button[@data-testid='account detail-tab']//h6[text()='Account Detail']");
	
	String valueOfDetailField    = "//span[@color='neutral' and text()='$field$']/following-sibling::div//span[@color='primary' or @color='link']";
	By activityDetailsFieldsList = By.xpath("(//*[contains(@data-testid , 'detail-tab')]/ancestor::div[@data-testid = 'horizontal-tab-header']/..//div[@style and not(contains(@style, 'left') or contains(@style, 'opacity') )]/div[not(@color)])[2]//span[@color='neutral']");
	By relatedFieldsDetailsList  = By.xpath("(//*[contains(@data-testid , 'detail-tab')]/ancestor::div[@data-testid = 'horizontal-tab-header']/..//div[@style and not(contains(@style, 'left') or contains(@style, 'opacity') )]/div[not(@color)])[3]//span[@color='neutral']");
	
	By subjectLink      	   = By.xpath("//span[text()='Subject']/..//a");
	
	String detailsFieldList    = "//*[text()='$fieldValue$']/../../../..//span[@color='neutral']";
	
	//player details
	By callsTabHeading				= By.xpath("(//h2[@color='primary'])[1]");
	By recordingPlayButton			= By.cssSelector("button.play-button");
	By recordingPlayerProgressBar	= By.id("scrubber-container");
	By forwardAudioButton           = By.xpath("//button[@data-testid='audio-play-btn']/following-sibling::button[1]");
	By rewindAudioButton            = By.xpath("//button[@data-testid='rewind-audio-btn']");
	
	By progressBarStartTime         = By.xpath("//div[@data-testid='recording-player.call-visualization']/following-sibling::div/span[1]");
	By progressBarEndTime           = By.xpath("//div[@data-testid='recording-player.call-visualization']/following-sibling::div/span[2]");
	
	 //player  -->  /*talk metrics*/
	
	String talkMetricsHeading 		= "//h6[text() = '$name$']/ancestor::div[2]"; 
	String talkMetricsData 			= "//h6[text() = '$name$']/ancestor::div[2]/following-sibling::div/div";
	
	
	public enum talkMetricsItems{
		Interruptions("Interruptions"),
		TalkRates("Talk Rates"),
		AverageTalkStreak("Average Talk Streak"),
		Vocabulary("Vocabulary"),
		VoiceEnergy("Voice Energy");
		
		final String displayName;
		
		talkMetricsItems(String displayName){
			this.displayName = displayName;
		}
		
		public String displayName() {
			return displayName;
		}
	}
	
	public enum actionsDropdownItems{
		ShareCall("Share Call"),
		AddtoLibrary("Add to Library"),
		AddReviewers("Add Reviewers"),
		DownloadFile("Download"),
		Reprocess("Reprocess"),
		Delete("Delete");
		final String displayName;
		
		actionsDropdownItems(String displayName){
			this.displayName = displayName;
		}
		
		public String displayName() {
			return displayName;
		}
	}
	
	/**get calls tab page heading
	 * @param driver
	 * @return
	 */
	public String getCallsTabPageHeading(WebDriver driver) {
		System.out.println("Getting call heading");
		waitUntilVisible(driver, callsTabHeading);
		return getElementsText(driver, callsTabHeading).trim();
	}
	
	/**
	 * this method wait until recording player is loaded
	 * @param driver
	 */
	public void verifyRecordingPlayerVisible(WebDriver driver) {
		waitUntilVisible(driver, recordingPlayerProgressBar);
	}
	
	/**
	 * this method wait until recording play button is loaded
	 * @param driver
	 */
	public void verifyRecordingPlayButtonVisible(WebDriver driver) {
		waitUntilVisible(driver, recordingPlayButton);
	}
	
	/**
	 * @param driver
	 * @return progress bar start time
	 */
	public String getProgressBarStartTime(WebDriver driver) {
		waitUntilVisible(driver, progressBarStartTime);
		return getElementsText(driver, progressBarStartTime).replaceAll(" ", "");
	}
	
	/**
	 * @param driver
	 * @return progress bar start time
	 */
	public int getProgressBarEndTime(WebDriver driver) {
		waitUntilVisible(driver, progressBarEndTime);
		return Integer.parseInt(getElementsText(driver, progressBarEndTime).replaceAll(" ", ""));
	}
	

	/**
	 * this method click 10 sec forward button
	 * @param driver
	 */
	public void clickTenSecForwardPlayButton(WebDriver driver) {
		waitUntilVisible(driver, forwardAudioButton);
		clickElement(driver, forwardAudioButton);
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
	}
	
	/**
	 * this method click 10 sec rewind button
	 * @param driver
	 */
	public void clickTenSecRewindPlayButton(WebDriver driver) {
		waitUntilVisible(driver, rewindAudioButton);
		clickElement(driver, rewindAudioButton);
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
	}
	
	
	/**
	 * use this method to switch to annotation tab
	 * @param driver
	 */
	public void switchToAnnotationTab(WebDriver driver) {
		waitUntilVisible(driver, annotationTabLink);
		clickElement(driver, annotationTabLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**get annotation count on cai player
	 * @param driver
	 * @return
	 */
	public String getAnnotationCountOnTab(WebDriver driver) {
		waitUntilVisible(driver, annotationCountOnTab);
		return getElementsText(driver, annotationCountOnTab);
	}
	
	/**
	 * use this method to verify if the one of the tags is present from the provided tags list
	 * also it verified that tag has a border which is rounded and its color
	 * @param driver
	 * @param tagsList
	 * @return true if tag is present else false
	 */
	public boolean isTagPeresent(WebDriver driver, List<String> tagsList, List<String> tagsSentimentList) {
		switchToAnnotationTab(driver);
		for (String tag : tagsList) {
			System.out.println("Found Tag List : " + tagsList); 
			if (isElementVisible(driver, By.xpath(tagLocator.replace("$$TagName$$", tag)), 0)) {									// if tag is visible then verify than tag has the sentiment, a border, its color and it rounder in shape
				List<String> actualTagSentiment = getAttributeValueList(driver, By.xpath(tagLocatorSentiment.replace("$$TagName$$", tag)), ElementAttributes.innerHTML);
				assertTrue(actualTagSentiment.contains(tagsSentimentList.get(tagsList.indexOf(tag))));
				WebElement element = findElement(driver, By.xpath(tagLocator.replace("$$TagName$$", tag)));
				assertEquals(getCssValue(driver, element, CssValues.BorderColor), "rgb(253, 216, 53)");
				assertEquals(getCssValue(driver, element, CssValues.BorderRadius), "18px");
				System.out.println("Tag " + tagsList + " found");
				return true;
			}
		}
		System.out.println("Tag is not found");
		return false;
	}
	
	/**
	 * use this method to select a tag for a CAI entry based on the tag name and sentiment
	 * @param driver
	 * @param tagName String variable, takes tag name to be selected
	 * @param sentimentIndex integer type, 0 for thumbs up and 1 for thumbs down
	 */
	public void setTag(WebDriver driver, String tagName, int sentimentIndex) {
		clickElement(driver, annotateBtn);
		clickElement(driver, addTopicsArrowIcon);
		
		By tagLocator = By.xpath(tagThumbIcon.replace("$$TagName$$", tagName).replace("$$Index$$", String.valueOf(sentimentIndex + 1)));
		waitUntilVisible(driver, tagLocator);
		clickElement(driver, tagLocator);
		clickElement(driver, backToCreateAnnotationIcon);

		clickElement(driver, createAnnotationBtn);
		waitUntilInvisible(driver, createAnnotationBtn);
	}
	
	/**verify no annoation image/details
	 * @param driver
	 */
	public void verifyNoAnnotationImagePresent(WebDriver driver) {
		waitUntilVisible(driver, noAnnotationHeader1);
		waitUntilVisible(driver, noAnnotationHeader2);
		waitUntilVisible(driver, noAnnotationImage);
	}
	
	
	/**method for adding annotation and tag both
	 * @param driver
	 * @param annotationName
	 * @param tagName
	 * @param sentimentIndex
	 */
	public void enterAnnotation(WebDriver driver, String annotationName) {
		clickElement(driver, annotateBtn);
		enterText(driver, annotationAddField, annotationName);
		waitUntilVisible(driver, createAnnotationBtn);
		clickElement(driver, createAnnotationBtn);
		waitUntilInvisible(driver, createAnnotationBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**boolean returns whether see more link visible or not
	 * @param driver
	 * @return
	 */
	public boolean isSeeMoreLinkVisible(WebDriver driver) {
		return isElementVisible(driver, seeMoreAnnotationLink, 5);
	}
	
	/**enter short annotations on see more links
	 * @param driver
	 */
	public void  removeExistingSeeMoreLink(WebDriver driver) {
		clickElement(driver, annotationTabLink);
		if(isListElementsVisible(driver, seeMoreAnnotationLink, 5)) {
			List<WebElement> seeMoreElements = getElements(driver, seeMoreAnnotationLink);
			for (int i = 0; i < seeMoreElements.size(); i++) {
				clickElement(driver, seeMoreElements.get(i));
				editAndEnterAnnotationBody(driver, "AutoAnnotation".concat(HelperFunctions.GetRandomString(4)));
			}
		}
	}
	
	/**click see more annotation link
	 * @param driver
	 */
	public void clickSeeMoreAnnotationLink(WebDriver driver) {
		waitUntilVisible(driver, seeMoreAnnotationLink);
		clickElement(driver, seeMoreAnnotationLink);
		waitUntilVisible(driver, seeLessAnnotationLink);
		clickElement(driver, seeLessAnnotationLink);
		waitUntilVisible(driver, seeMoreAnnotationLink);
	}
	
	/**method for adding annotation without tag
	 * @param driver
	 * @param annotationName
	 */
	public void editAndEnterAnnotationBody(WebDriver driver, String annotationName) {
		waitUntilVisible(driver, annotationAddField);
		enterText(driver, annotationAddField, annotationName);
		clickElement(driver, editAnnotationBtn);
		waitUntilInvisible(driver, editAnnotationBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**method for adding annotation without tag
	 * @param driver
	 * @param annotationName
	 */
	public void enterAnnotationWithoutTag(WebDriver driver, String annotationName, String userName) {
		waitUntilVisible(driver, annotateBtn);
		clickElement(driver, annotateBtn);
		enterText(driver, annotationAddField, annotationName);
		clickElement(driver, createAnnotationBtn);
		waitUntilInvisible(driver, createAnnotationBtn);
		dashboard.isPaceBarInvisible(driver);
		switchToAnnotationTab(driver);
		verifyAnnotationCreated(driver, annotationName, userName);
	}
	
	/** verify Annotation Created
	 * @param driver
	 * @param annotationName
	 */
	public void verifyAnnotationCreated(WebDriver driver, String annotationName, String userName) {
		By annotationLoc = By.xpath(createdAnnotationText.replace("$annotationName$", annotationName));
		By editAnnotationIcon = By.xpath(editCreatedAnnotation.replace("$annotationName$", annotationName));
		By userNameAnnotation = By.xpath(annotationCreatedUser.replace("$annotationName$", annotationName).replace("$$Index$$", String.valueOf(1)));
		waitUntilVisible(driver, annotationLoc);
		waitUntilVisible(driver, editAnnotationIcon);
		waitUntilVisible(driver, userNameAnnotation);
		assertEquals(getElementsText(driver, userNameAnnotation), userName);
	}
	
	/**verify annotation tag with thumbs up icon
	 * @param driver
	 * @param annotationName
	 * @param topic
	 * @return
	 */
	public boolean verifyAnnotationTopicWithThumbsUp(WebDriver driver, String annotationName, String topic) {
		switchToAnnotationTab(driver);
		boolean value = false;
		By annotationThumbsLoc = By.xpath(topicThumbsIconOuterHTML.replace("$annotationName$", annotationName).replace("$topic$", topic));
		value = getAttribue(driver, annotationThumbsLoc, ElementAttributes.outerHTML)
				.equals(annotateThumbsUpOuterHTML);
		return value;
	}
	
	/**verify annotation tag with thumbs up icon
	 * @param driver
	 * @param annotationName
	 * @param topic
	 * @return
	 */
	public boolean verifyAnnotationTopicWithThumbsDown(WebDriver driver, String annotationName, String topic) {
		boolean value = false;
		By annotationThumbsLoc = By.xpath(topicThumbsIconOuterHTML.replace("$annotationName$", annotationName).replace("$topic$", topic));
		value = getAttribue(driver, annotationThumbsLoc, ElementAttributes.outerHTML)
				.equals(annotateThumbsDownOuterHTML);
		return value;
	}
	
	/**click first pencil annotation icon
	 * @param driver
	 */
	public void clickFirstPencilAnnotationIcon(WebDriver driver) {
		isListElementsVisible(driver, pencilAnnotationIcons, 2);
		clickElement(driver, getInactiveElements(driver, pencilAnnotationIcons).get(0));
	}
	
	/**Edit annotation created
	 * @param driver
	 * @param oldAnnotationName
	 */
	public void editAnnotationCreated(WebDriver driver, String oldAnnotationName, String newAnnotationName) {
		By editAnnotationIcon = By.xpath(editCreatedAnnotation.replace("$annotationName$", oldAnnotationName));
		waitUntilVisible(driver, editAnnotationIcon);
		clickElement(driver, editAnnotationIcon);
		enterText(driver, annotationAddField, newAnnotationName);
		waitUntilVisible(driver, editAnnotationBtn);
		clickElement(driver, editAnnotationBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to select option from the action dropdown
	 * @param driver
	 * @param actionToSelect
	 */
	public boolean selectActionDropdownOption(WebDriver driver, actionsDropdownItems actionToSelect) {
		boolean actionVisible = false;
		waitUntilVisible(driver, actionDropdownBtn);
		clickElement(driver, actionDropdownBtn);
		waitUntilVisible(driver, actionDropdownItems);

		List<WebElement> actionDropdownItemsList = getElements(driver, actionDropdownItems);
		for (WebElement actionDropdownItem : actionDropdownItemsList) {
			if (actionDropdownItem.getText().equals(actionToSelect.displayName())) {
				actionVisible = true;
				clickElement(driver, actionDropdownItem);
				break;
			}
		}
		return actionVisible;
	}

	/**clear recipients input tab
	 * @param driver
	 */
	public void clearAllReceipientInputTab(WebDriver driver) {
		clickElement(driver, recipientDropDownTextBox);
		clearAll(driver, recipientDropDownTextBox);
	}
	
	/**clear reviewers input tab
	 * @param driver
	 */
	public void clearAllReviewerInputTab(WebDriver driver) {
		clickElement(driver, reviewerDropDownTextBox);
		clearAll(driver, reviewerDropDownTextBox);
	}
	
	
	/**is agent is present in drop down
	 * @param driver
	 * @param agentName
	 * @param actionItems TODO
	 * @param actionItem TODO
	 * @return
	 */
	public boolean isAgentExistsInDropDown(WebDriver driver, String agentName, actionsDropdownItems actionItems){
		By headerLoc = null;
		actionsDropdownItems actionHeader = (actionsDropdownItems) actionItems;

		switch (actionHeader) {
		case ShareCall:
			headerLoc = recipientDropDownTextBox;
			break;
		case AddReviewers:
			headerLoc = reviewerDropDownTextBox;
			break;
		default:
			break;
		}
		
		waitUntilVisible(driver, headerLoc);
		clickElement(driver, headerLoc);
		clearAll(driver, headerLoc);
		enterText(driver, headerLoc, agentName);
		idleWait(2);
		if (!isListElementsVisible(driver, usersDropDownNameList, 2)) {
			if (isElementVisible(driver, noOptionDropdown, 2))
				assertEquals(getElementsText(driver, noOptionDropdown), "No options");
			return false;
		} else {
			return isAllWebElementsListContainsText(getElements(driver, usersDropDownNameList), agentName);
		}
	}
	
	/**get user name from reviewer list
	 * @param driver
	 * @param index
	 * @return
	 */
	public String getUserNameFromReviewerList(WebDriver driver, int index) {
		isListElementsVisible(driver, usersDropDownNameList, 5);
		return getTextListFromElements(driver, usersDropDownNameList).get(index);
	}
	
	
	/**select reviewer from drop down
	 * @param driver
	 * @param agentName
	 */
	public void selectReviewerToAgentFromDropDown(WebDriver driver, String agentName) {
		clearAllReviewerInputTab(driver);
		
		//method to slower down sending text in input tab
		if(agentName.contains(" ")) {
			String[] agentNameSplit = agentName.split(" ");
			for(String name: agentNameSplit) {
				appendText(driver, reviewerDropDownTextBox, name);
				dashboard.isPaceBarInvisible(driver);
				appendText(driver, reviewerDropDownTextBox, " ");
			}
		}
		isListElementsVisible(driver, usersDropDownNameList, 5);
		assertEquals(getTextListFromElements(driver, usersDropDownNameList).size(), 1);
		assertEquals(getTextListFromElements(driver, usersDropDownNameList).get(0), agentName);
		clickElement(driver, getElements(driver, usersDropDownNameList).get(0));
	}
	
	/**verify error msg when shared empty
	 * @param driver
	 * @param agentName
	 */
	public void verifyErrorMsgWhenSharedEmpty(WebDriver driver) {
		clearAllReceipientInputTab(driver);
		waitUntilVisible(driver, sendButton);
		clickElement(driver, sendButton);
		waitUntilVisible(driver, recipientHelperText);
		assertEquals(getElementsText(driver, recipientHelperText), "This field is required.");
		clickCancelButton(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param agentName
	 */
	public void enterRecipientInputTab(WebDriver driver, String agentName) {
		clickElement(driver, shareCallHeader);
		clearAllReceipientInputTab(driver);
		enterText(driver, recipientDropDownTextBox, agentName);
	}
	
	/**
	 * @param driver
	 * @param agentName
	 */
	public void selectShareToAgentFromDropDown(WebDriver driver, String agentName) {
		enterRecipientInputTab(driver, agentName);
		waitUntilVisible(driver, recipientDropdownFirstOption);
		clickElement(driver, recipientDropdownFirstOption);
		clickElement(driver, shareCallHeader);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param agentName
	 */
	public void verifyShareAgentImageVisible(WebDriver driver, String agentName) {
		clearAllReceipientInputTab(driver);
		enterText(driver, recipientDropDownTextBox, agentName);
		waitUntilVisible(driver, recipientDropdownFirstOption);
		waitUntilVisible(driver, userImageAgentDropDown);
	}

	/**
	 * @param driver
	 * @param agentName
	 * @return
	 */
	public boolean isAgentVisibleAfterShared(WebDriver driver, String agentName) {
		clickElement(driver, shareCallHeader);
		By sharedAgentDeleteLoc = By.xpath(agentToDeleteSharedWith.replace("$agentName$", agentName));
		return isElementVisible(driver, sharedAgentDeleteLoc, 5);
	}
	
	/**delete selected shared agent
	 * @param driver
	 * @param agentName
	 */
	public void deleteSelectedSharedAgent(WebDriver driver, String agentName) {
		clickElement(driver, shareCallHeader);
		By sharedAgentDeleteLoc = By.xpath(agentToDeleteSharedWith.replace("$agentName$", agentName));
		waitUntilVisible(driver, sharedAgentDeleteLoc);
		clickElement(driver, sharedAgentDeleteLoc);
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, sharedAgentDeleteLoc, 2));
	}
	
	/**
	 * use this method to the conversation AI with other users
	 * @param driver
	 * @param agentName
	 * @param shareMessage
	 */
	public void shareCall(WebDriver driver, String agentName, String shareMessage) {
		selectActionDropdownOption(driver, actionsDropdownItems.ShareCall);
		selectShareToAgentFromDropDown(driver, agentName);
		clickElement(driver, shareCallHeader);
		enterText(driver, shareMessageTextBox, shareMessage);
		assertTrue(isAgentVisibleAfterShared(driver, agentName));
		clickElement(driver, sendButton);
		waitUntilInvisible(driver, shareMessageTextBox);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to the conversation AI with other users
	 * @param driver
	 * @param agentName
	 */
	public void addReviewers(WebDriver driver, String agentName) {
		selectActionDropdownOption(driver, actionsDropdownItems.AddReviewers);
		assertFalse(HelperFunctions.hasDuplicateItems(getTextListFromElements(driver, usersDropDownNameList)));
		selectReviewerToAgentFromDropDown(driver, agentName);
		clickElement(driver, shareCallButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to verify if the library is checked by opening action drop down and selecting library option
	 * @param driver
	 * @param libraryList
	 * @return
	 */
	public boolean isLibraryAdded(WebDriver driver, List<String> libraryList) {
		boolean value = false;
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		List<String> libraryListFromDropdown = getTextListFromElements(driver, libraryNamesLabel);
		if (libraryListFromDropdown == null || libraryListFromDropdown.isEmpty()) {
			clickCancelButton(driver);
			return false;
		}

		for (String library : libraryList) {
			int libraryIndex = libraryListFromDropdown.indexOf(library);
			if (libraryIndex < 0) {
				clickCancelButton(driver);
				return false;
			}
			String librarySelectionContainerHTML = getElements(driver, librarySecltionContainer).get(libraryIndex)
					.getAttribute(ElementAttributes.outerHTML.displayName());
			if (libraryIndex >= 0 && librarySelectionContainerHTML.equals(selectedLibraryHTML)) {
				System.out.println("Library " + libraryList + " selected");
				value = true;
			} else if (libraryIndex >= 0 && librarySelectionContainerHTML.equals(unSelectedLibraryHTML)) {
				break;
			}
		}
		System.out.println("Library is not selected");
		clickCancelButton(driver);
		return value;
	}
	
	/**
	 * use this method to select a library for a CAI entry based on library index
	 * @param driver
	 * @param libraryIndex integer. Use it to mention the index of the library to be slected.
	 * @return String. Library name
	 */
	public String setLibrary(WebDriver driver, int libraryIndex) {
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		
		String libraryFromDropdown = getTextListFromElements(driver, libraryNamesLabel).get(libraryIndex);
		
		String librarySelectionContainerHTML = getElements(driver, librarySecltionContainer).get(libraryIndex).getAttribute(ElementAttributes.outerHTML.displayName());
		if (!librarySelectionContainerHTML.equals(selectedLibraryHTML)) {
			getElements(driver, librarySecltionContainer).get(libraryIndex).click();
			clickSaveButton(driver);
		}else {
			clickCancelButton(driver);
		}
		return libraryFromDropdown;
	}
	
	
	/**select and add new library
	 * @param driver
	 * @param libraryList
	 */
	public void selectandAddNewLibrary(WebDriver driver, List<String> libraryList) {
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		for (String libraryName : libraryList) {
			By libraryLoc = By.xpath(selectLibrary.replace("$libraryName$", libraryName));
			String libraryUnSelectedContainerHTML = findElement(driver, libraryLoc)
					.getAttribute(ElementAttributes.outerHTML.displayName());
			assertEquals(libraryUnSelectedContainerHTML, unSelectedLibraryHTML);
			waitUntilVisible(driver, libraryLoc);
			clickElement(driver, libraryLoc);
			dashboard.isPaceBarInvisible(driver);
			String librarySelectedContainerHTML = findElement(driver, libraryLoc)
					.getAttribute(ElementAttributes.outerHTML.displayName());
			assertEquals(librarySelectedContainerHTML, selectedLibraryHTML);
		}
		clickSaveButton(driver);
	}
	
	/**unselect add library
	 * @param driver
	 * @param libraryName
	 */
	public void unSelectAddedLibrary(WebDriver driver, String libraryName) {
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		By libraryLoc = By.xpath(selectLibrary.replace("$libraryName$", libraryName));
		String libraryContainerHTML = findElement(driver, libraryLoc)
				.getAttribute(ElementAttributes.outerHTML.displayName());
		
		if(libraryContainerHTML.equals(selectedLibraryHTML)) {
			//unselect library
			waitUntilVisible(driver, libraryLoc);
			clickElement(driver, libraryLoc);
			dashboard.isPaceBarInvisible(driver);
		}
		
		libraryContainerHTML = findElement(driver, libraryLoc)
				.getAttribute(ElementAttributes.outerHTML.displayName());
		assertEquals(libraryContainerHTML, unSelectedLibraryHTML);
		clickSaveButton(driver);
	}
	
	
	/**unselect all libraries
	 * @param driver
	 */
	public void unSelectAllLibraries(WebDriver driver) {
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		for (WebElement element : getElements(driver, librarySecltionContainer)) {
			String librarySelectionContainerHTML = element.getAttribute(ElementAttributes.outerHTML.displayName());
			if (librarySelectionContainerHTML.equals(selectedLibraryHTML)) {
				element.click();
			}
		}
		clickSaveButton(driver);
	}

	/**select multiple libraries
	 * @param driver
	 * @param libraryCount
	 */
	public List<String> selectMultipleLibraries(WebDriver driver, int libraryCount) {
		List<String> libraryList = new ArrayList<String>();
		selectActionDropdownOption(driver, actionsDropdownItems.AddtoLibrary);
		for (int i = 0; i < libraryCount; i++) {
			List<WebElement> libraryListElements = getElements(driver, librarySecltionContainer);
			String librarySelectionContainerHTML = libraryListElements.get(i)
					.getAttribute(ElementAttributes.outerHTML.displayName());
			if (!librarySelectionContainerHTML.equals(selectedLibraryHTML)) {
				libraryListElements.get(i).click();
				String libraryName = getElementsText(driver, getInactiveElements(driver, librarySecltionContainer).get(i).findElement(By.xpath("../..")));
				libraryList.add(libraryName);
			}
		}
		clickSaveButton(driver);
		return libraryList;
		
	}
	
	/**
	 * use this method to select option from the action dropdown
	 * @param driver
	 * @param dropDownItem 
	 * @param actionToSelect
	 */
	public boolean isActionDropDownItemVisible(WebDriver driver, actionsDropdownItems dropDownItem) {
		waitUntilVisible(driver, actionDropdownBtn);
		clickElement(driver, actionDropdownBtn);
		waitUntilVisible(driver, actionDropdownItems);
		
		List<String> actionDropdownItemsList = getTextListFromElements(driver, actionDropdownItems);
		pressEscapeKey(driver);
		return actionDropdownItemsList.contains(dropDownItem.displayName());
	}
	
	/**
	 * use this method to get call notes
	 * 
	 * @param driver
	 * @return
	 */
	public String getCallNotes(WebDriver driver) {
		waitUntilVisible(driver, callNotesText);
		return getElementsText(driver, callNotesText);
	}
	
	/**
	 * use this method to click save button of action drop down items
	 * @param driver
	 */
	public void clickSaveButton(WebDriver driver) {
		clickElement(driver, saveButton);
		waitUntilInvisible(driver, saveButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to click cancel button of action drop down items
	 * @param driver
	 */
	public void clickCancelButton(WebDriver driver) {
		clickElement(driver, cancelButton);
		waitUntilInvisible(driver, cancelButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyAddEditCallNotesIconNotVisible(WebDriver driver) {
		scrollTillEndOfPage(driver);
		assertFalse(isElementVisible(driver, addCallNotesIcon, 5));
		assertFalse(isElementVisible(driver, editCallNotesButton, 5));
	}
	
	public void verifyAddEditSupervisorNotesIconNotVisible(WebDriver driver) {
		scrollTillEndOfPage(driver);
		assertFalse(isElementVisible(driver, addSuperVisorNotesIcon, 5));
		assertFalse(isElementVisible(driver, editSuperVisorNotesButton, 5));
	}
	
	/**
	 * @param driver
	 */
	public void clickAddEditSuperVisorNotesIcon(WebDriver driver) {
		if (isElementVisible(driver, addSuperVisorNotesIcon, 2)) {
			clickElement(driver, addSuperVisorNotesIcon);
		} else {
			clickElement(driver, editSuperVisorNotesButton);
		}
		waitUntilVisible(driver, notesInputTextArea);
	}
	
	/**find expand supervisor notes visible
	 * @param driver
	 * @return
	 */
	public boolean isExpandIconSuperVisNotesVisible(WebDriver driver) {
		return isElementVisible(driver, expandIconSuperVisNotes, 5);
	}
	
	/**find expand call notes visible
	 * @param driver
	 * @return
	 */
	public boolean isExpandIconCallNotesVisible(WebDriver driver) {
		return isElementVisible(driver, expandIconCallNotes, 5);
	}
	
	/**expand supervisor notes
	 * @param driver
	 */
	public void clickExpandIconSuperVisNotes(WebDriver driver) {
		waitUntilVisible(driver, expandIconSuperVisNotes);
		clickElement(driver, expandIconSuperVisNotes);
		waitUntilVisible(driver, collapseIconSuperVisNotes);
	}

	/**collapse supervisor notes
	 * @param driver
	 */
	public void clickCollapseIconSuperVisNotes(WebDriver driver) {
		waitUntilVisible(driver, collapseIconSuperVisNotes);
		clickElement(driver, collapseIconSuperVisNotes);
		waitUntilVisible(driver, expandIconSuperVisNotes);
	}
	
	/**expand call notes
	 * @param driver
	 */
	public void clickExpandIconCallNotes(WebDriver driver) {
		waitUntilVisible(driver, expandIconCallNotes);
		clickElement(driver, expandIconCallNotes);
		waitUntilVisible(driver, collapseIconCallNotes);
	}

	/**collapse call notes
	 * @param driver
	 */
	public void clickCollapseIconCallNotes(WebDriver driver) {
		waitUntilVisible(driver, collapseIconCallNotes);
		clickElement(driver, collapseIconCallNotes);
		waitUntilVisible(driver, expandIconCallNotes);
	}

	/**add or edit call notes
	 * @param driver
	 */
	public void clickAddEditCallNotesNotesIcon(WebDriver driver) {
		if(isElementVisible(driver, addCallNotesIcon, 2)) {
			clickElement(driver, addCallNotesIcon);
		}
		else {
			clickElement(driver, editCallNotesButton);
		}
		waitUntilVisible(driver, notesInputTextArea);
	}
	
	/**add or edit supervisor notes
	 * @param driver
	 * @param notes
	 */
	public void addOrEditSuperVisorNotes(WebDriver driver, String notes) {
		clickAddEditSuperVisorNotesIcon(driver);
		clearAll(driver, notesInputTextArea);
		enterText(driver, notesInputTextArea, notes);
		clickElement(driver, sendButton);
		
		assertEquals(getToastMsg(driver), "Supervisor notes saved.");
	}
	
	/**add or edit call notes
	 * @param driver
	 * @param notes
	 */
	public void addOrEditCallNotes(WebDriver driver, String notes) {
		clickAddEditCallNotesNotesIcon(driver);
		clearAll(driver, notesInputTextArea);
		enterText(driver, notesInputTextArea, notes);
		clickElement(driver, sendButton);
		
		assertEquals(getToastMsg(driver), "Call notes saved.");
	}

	/**find opportunity section visible or not
	 * @param driver
	 * @return
	 */
	public boolean isOpportunityDetailTabVisible(WebDriver driver) {
		return isElementVisible(driver, opportunityDetailTab, 5);
	}
	
	/**find caller section visible or not
	 * @param driver
	 * @return
	 */
	public boolean isCallerDetailTabVisible(WebDriver driver) {
		return isElementVisible(driver, callerDetailTab, 5);
	}
	
	/**click subject link
	 * @param driver
	 */
	public void clickSubjectLink(WebDriver driver) {
		waitUntilVisible(driver, activityDetailTab);
		clickElement(driver, activityDetailTab);
		waitUntilVisible(driver, subjectLink);
		clickElement(driver, subjectLink);
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
	}
	
	/**click cakker detail link
	 * @param driver
	 */
	public void clickCallerDetailTab(WebDriver driver) {
		waitUntilVisible(driver, callerDetailTab);
		scrollIntoView(driver, callerDetailTab);
		clickElement(driver, callerDetailTab);
		waitForPageLoaded(driver);
	}
	
	/**click account link
	 * @param driver
	 */
	public void clickAccountDetailTab(WebDriver driver) {
		waitUntilVisible(driver, accountDetailTab);
		scrollIntoView(driver, accountDetailTab);
		clickElement(driver, accountDetailTab);
		waitForPageLoaded(driver);
	}
	
	/**click opportunity detail tab
	 * @param driver
	 */
	public void clickOpportunityDetailTab(WebDriver driver) {
		waitUntilVisible(driver, opportunityDetailTab);
		scrollIntoView(driver, opportunityDetailTab);
		clickElement(driver, opportunityDetailTab);
		waitForPageLoaded(driver);
	}
	
	/**click case detail tab
	 * @param driver
	 */
	public void clickCaseDetailTab(WebDriver driver) {
		waitUntilVisible(driver, caseDetailTab);
		scrollIntoView(driver, callerDetailTab);
		clickElement(driver, caseDetailTab);
		waitForPageLoaded(driver);
	}
	
	/**get toast message
	 * @param driver
	 * @return
	 */
	public String getToastMsg(WebDriver driver) {
		String text = "";
		waitUntilVisible(driver, toastMsg);
		text = getElementsText(driver, toastMsg);
		waitUntilInvisible(driver, toastMsg);
		dashboard.isPaceBarInvisible(driver);
		return text;
	}
	
	/**method to get super visor notes
	 * @param driver
	 * @return
	 */
	public String getSuperVisorNotesText(WebDriver driver) {
		waitUntilVisible(driver, superVisorNotesText);
		return getElementsText(driver, superVisorNotesText).trim();
	}
	
	/**method to get call notes
	 * @param driver
	 * @return
	 */
	public String getCallNotesText(WebDriver driver) {
		waitUntilVisible(driver, callNotesText);
		return getElementsText(driver, callNotesText).trim();
	}
	
	/**compare actual and expected supervisor notes
	 * @param driver
	 * @param notes
	 */
	public void verifySuperVisorNotesSaved(WebDriver driver, String notes) {
		assertEquals(getSuperVisorNotesText(driver), notes.trim());
	}
	
	/**compare actual and expected call notes
	 * @param driver
	 * @param notes
	 */
	public void verifyCallNotesSaved(WebDriver driver, String notes) {
		assertEquals(getCallNotesText(driver), notes.trim());
	}

	/**
	 * use this function to get agent's talk Time
	 * @param driver
	 * @return
	 */
	public String getAgentTalkTime(WebDriver driver) {
		if (isChildElementPresent(driver, talkRatioSection, agentMatrix))
			return findElement(driver, talkRatioSection).findElement(agentMatrix).getText();
		else
			return "0";
	}
	
	/**
	 * use this function to get agent's average talk Streak
	 * @param driver
	 * @return
	 */
	public String getAgentAvgTalkStreak(WebDriver driver) {
		if(isChildElementPresent(driver, avgTalkStreakSection, agentMatrixMinute))
			return findElement(driver, avgTalkStreakSection).findElement(agentMatrixMinute).getText();
		else
			return "0";
	}
	
	/**
	 * use this function to get agent's Longest Talk Streak
	 * @param driver
	 * @return
	 */
	public String getAgentLongTalkStreak(WebDriver driver) {
		if(isChildElementPresent(driver, longTalkStreakSection, agentMatrixMinute))
			return findElement(driver, longTalkStreakSection).findElement(agentMatrixMinute).getText();
		else
			return "0";
	}
	
	/**
	 * use this function to get customer's average talk Streak
	 * @param driver
	 * @return
	 */
	public String getCustomerAvgTalkStreak(WebDriver driver) {
		if(isChildElementPresent(driver, avgTalkStreakSection, customerMatrixMinute))
			return findElement(driver, avgTalkStreakSection).findElement(customerMatrixMinute).getText();
		else
			return "0";
	}
	
	/**
	 * use this function to get customer's Longest talk Streak
	 * @param driver
	 * @return
	 */
	public String getCustomerLongStreak(WebDriver driver) {
		if(isChildElementPresent(driver, longTalkStreakSection, customerMatrixMinute))
			return findElement(driver, longTalkStreakSection).findElement(customerMatrixMinute).getText();
		else
			return "0";
	}
	
	/**find mark reviwed icon visible or not
	 * @param driver
	 * @return
	 */
	public boolean isMarkAsReviewedIconVisible(WebDriver driver) {
		return isElementVisible(driver, markAsReviewedIcon, 5);
	}
	
	/**get caller details fields list
	 * @param driver
	 */
	public List<String> getCallerDetailsFieldsList(WebDriver driver, String fieldValue) {
		By callerDetailsFieldLoc = By.xpath(detailsFieldList.replace("$fieldValue$", fieldValue));
		isListElementsVisible(driver, callerDetailsFieldLoc, 5);
		return getTextListFromElements(driver, callerDetailsFieldLoc);
	}
	
	/**get value of detailed field
	 * @param driver
	 * @param field
	 * @return
	 */
	public String getValueOfDetailField(WebDriver driver, String field) {
		By valueFieldLoc = By.xpath(valueOfDetailField.replace("$field$", field));
		waitUntilVisible(driver, valueFieldLoc);
		return getElementsText(driver, valueFieldLoc);
	}
	
	/**get custom details map values in <fields, value> on basis of fieldlist passed as param
	 * @param driver
	 * @param customFieldTextList
	 * @return
	 */
	public Map<String, String> getCustomCAIDetailsFieldsMapValue(WebDriver driver, List<String> customFieldTextList) {
		Map<String, String> customFieldValueMap = new HashMap<String, String>();
		String value = " ";
		for (String customFieldText : customFieldTextList) {
			By valueFieldLoc = By.xpath(valueOfDetailField.replace("$field$", customFieldText));

			if (getTextListFromElements(driver, valueFieldLoc) != null) {
				if (getInactiveElements(driver, valueFieldLoc).size() > 1) {
					int size = getInactiveElements(driver, valueFieldLoc).size();
					if(getAttribue(driver, callerDetailTab, ElementAttributes.color).equals("link")) {
						size = 1;
					}
					String valueOfDetailField1 = "(//span[@color='neutral' and text()='$field$']/following-sibling::div//span[@color='primary' or @color='link'])[size]";
					valueFieldLoc = By.xpath(valueOfDetailField1.replace("$field$", customFieldText).replace("size",
							Integer.toString(size)));
				}
			}
			
			value = getElementsText(driver, valueFieldLoc).trim();
			
			//using the value obtained above for further operation
			if (customFieldText.equals("Created Date")) {
				value = HelperFunctions.changeDateTimeFormat(value, "MM/dd/yyyy, hh:mm a", "MM/dd/yy hh:mm a");
			}
			if (customFieldText.equals("Probability (%)")) {
				int floatValue = (int) Double.parseDouble(value);
				value = Integer.toString(floatValue);
			}
			customFieldValueMap.put(customFieldText, value);
		}
		return customFieldValueMap;
	}

	/**get Account details list fields
	 * @param driver
	 * @param relatedValue
	 * @return
	 */
	public List<String> getAccountDetailsNameList(WebDriver driver, String relatedValue){
		clickAccountDetailTab(driver);
		isListElementsVisible(driver, relatedFieldsDetailsList, 5);
		return getTextListFromElements(driver, relatedFieldsDetailsList);
	}
	
	/**get Opportunity details list fields
	 * @param driver
	 * @return
	 */
	public List<String> getOpportunityDetailsNameList(WebDriver driver){
		clickOpportunityDetailTab(driver);
		isListElementsVisible(driver, relatedFieldsDetailsList, 5);
		return getTextListFromElements(driver, relatedFieldsDetailsList);
	}
	
	/**get case details list fields
	 * @param driver
	 * @param relatedValue
	 * @return
	 */
	public List<String> getCaseDetailsNameList(WebDriver driver, String relatedValue){
		clickCaseDetailTab(driver);
		isListElementsVisible(driver, relatedFieldsDetailsList, 5);
		return getTextListFromElements(driver, relatedFieldsDetailsList);
	}
	
	/**click COnfirm delete button to call deletion
	 * @param driver
	 */
	public void clickConfirmDeleteButton(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteButton);
		clickElement(driver, confirmDeleteButton);
	}
	
	/**
	 * @param driver
	 * click on call player
	 */
	public void clickInMidOfCallPlayer(WebDriver driver) {
		waitUntilVisible(driver, recordingPlayerProgressBar);
		clickElementCordinates(driver, recordingPlayerProgressBar, 700, 300);
	}
	
	/**
	 * @param driver
	 * get annotation time stamp by index 
	 */
	public List<String> getAnnotatioSpeakerDetail(WebDriver driver) {
		return getTextListFromElements(driver, annotationSpeakerDetail);	
	}
	

	/**
	 * @param driver
	 * get transcript time stamp by index 
	 */
	public List<String> getTranscriptSpeakerDetail(WebDriver driver) {
		return getTextListFromElements(driver, transcriptSpeakerDetail);	
	}
	
	/**
	 * @param driver
	 * @param item
	 * @return is talk metric item heading visible
	 */
	public boolean isTalkMetricsItemsVisible(WebDriver driver, talkMetricsItems item) {
		By locator = By.xpath(talkMetricsHeading.replace("$name$", item.displayName()));
		if(isElementVisible(driver, locator, 10)) {
			By dataLocator = By.xpath(talkMetricsData.replace("$name$", item.displayName()));
			assertTrue(isListElementsVisible(driver, dataLocator, 10));
			return true;
		}else {
			return false;
		}
	}
}
