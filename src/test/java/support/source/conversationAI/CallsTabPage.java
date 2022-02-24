package support.source.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.testng.internal.collections.Pair;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallsTabPage extends SeleniumBase {

	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	LibraryTabPage libraryTabPage = new LibraryTabPage();

	// Text Search Filter
	By caiTextFilter 				= By.cssSelector(".field-term");
	By searchButton			 		= By.cssSelector(".ai-search");

	// Calendar
	public static By startDate				= By.cssSelector(".start-date");
	public static By endDate				= By.cssSelector(".end-date");
	public static By calendarPopUp 			= By.cssSelector(".datepicker-days");
	public static By oldMonthDateList 		= By.cssSelector(".datepicker-days .old");
	public static By currentMonthDateList 	= By.cssSelector(".datepicker-days .day:not(.disabled):not(.old)");
	public static By futureDateList 		= By.cssSelector(".datepicker-days .disabled");
	public static By previousMonthCalendar = By.cssSelector(".datepicker-days .prev");
	public static By currentMthDateInPreMonth 	= By.cssSelector(".datepicker-days .new.day");
	public static By preMthDateInPreMonth 		= By.cssSelector(".datepicker-days .day:not(.old):not(.new)");

	By caiNoAccessMessage			= By.xpath("//*[@class='toast-message' and contains(text(),'No access to call.')]");			
	By clearAllLink 				= By.cssSelector(".clear-all");
	By selectedFilterList 			= By.cssSelector(".selected-filter .filter-non-tag");
	By selectedTagFilterList 		= By.cssSelector(".selected-filter .filter-tag");
	By booleanIconList 				= By.cssSelector(".selected-filter .boolean-indicator");
	By plusIconList 				= By.cssSelector(".glyphicon-plus");
	By minusIconList 				= By.cssSelector(".glyphicon-minus");
	By removeFilterList 			= By.cssSelector(".selected-filter .glyphicon-remove-sign");
	By searchConvAITextBox 			= By.cssSelector(".search-term input");
	By searchConvAIBtn 				= By.cssSelector(".btn-primary.search");
	
	String selectedFilterPlus 		= ".//div[@class='filter-non-tag' and contains(text(),'$filterName$')]/parent::div//a[contains(@class,'glyphicon-plus')]";
	String selectedFilterMinus		= ".//div[@class='filter-non-tag' and contains(text(),'$filterName$')]/parent::div//a[contains(@class,'glyphicon-minus')]";
	
	// User Name on right corner
	By userName 					= By.cssSelector(".user-profile .username");

	// Calls Tab left Nav filters
	// Agent Name
	By agentFilter 					= By.cssSelector(".filter-agent.selectize-control.filter-agent.single");
	By agentFilterValues 			= By.cssSelector(".filter-agent.single .selectize-dropdown-content div");
	By agentFilterInput 			= By.cssSelector(".filter-agent.single input");
	By agentSelected 				= By.cssSelector(".filter-agent.single .has-items .item");

	//Team Filter
	
	By teamFilter 					= By.cssSelector(".filter-teams.selectize-control.filter-teams.single");
	By teamFilterValues 			= By.cssSelector(".filter-teams.single .selectize-dropdown-content div");
	By teamFilterInput 				= By.cssSelector(".filter-teams.single input");
	By teamSelected 				= By.cssSelector(".filter-teams.single .has-items .item");

	
	// Prospect-Company
	By companyFilter 				= By.cssSelector(".filter-company.selectize-control.filter-company.single");
	By companyFilterInput 			= By.cssSelector(".filter-company.single input");
	By companyFilterValues 			= By.cssSelector(".filter-company.single .selectize-dropdown-content div");
	By companySelected 				= By.cssSelector(".filter-company.single .has-items .item");

	// Prospect-Name
	By nameFilter 					= By.cssSelector(".filter-name.selectize-control.filter-name.single");
	By nameFilterInput 				= By.cssSelector(".filter-name.single input");
	By nameFilterValues 			= By.cssSelector(".filter-name.single .selectize-dropdown-content div");
	By nameSelected 				= By.cssSelector(".filter-name.single .has-items .item");

	// Prospect-Title
	By titleFilter 					= By.cssSelector(".filter-title.selectize-control.filter-title.single");
	By titleFilterValues 			= By.cssSelector(".filter-title.single .selectize-dropdown-content div");
	By titleSelected 				= By.cssSelector(".filter-title.single .has-items .item");
	By callDurationMinSlider 		= By.cssSelector(".call-duration .slider-horizontal .tooltip-min .tooltip-arrow");
	By callDurationRangeMin 		= By.cssSelector(".filter-non-tag");
	By callDurationMaxSlider 		= By.cssSelector(".call-duration .slider-horizontal .tooltip-max .tooltip-arrow");
	By callDurationRangeMax 		= By.cssSelector(".call-duration .tooltip-min .tooltip-inner");

	By agentTalkTimeMinSlider 		= By.cssSelector(".agent-talk-ratio .slider-horizontal .tooltip-min .tooltip-arrow");
	By agentTalkTimeRangeMin 		= By.cssSelector(".filter-non-tag");
	By agentTalkTimeMaxSlider 		= By.cssSelector(".agent-talk-ratio .slider-horizontal .tooltip-max .tooltip-arrow");
	By agentTalkTimeRangeMax 		= By.cssSelector(".call-duration .tooltip-min .tooltip-inner");
	By agentTalkTimePercentage		= By.xpath("//*[contains(@class,'talktime')]// label[contains(text(), 'Agent')]/..//span");
	
	String callerTalkTimePercentage = "//*[contains(@class,'talktime')]// label[contains(text(), '$$callername$$')]/..//span";
	
	// Tags
	By tagFilter 					= By.cssSelector(".tags.single");
	By tagFilterInput 				= By.cssSelector(".filter-selector.tags input");
	By tagFilterValues 				= By.cssSelector(".tags.single .selectize-dropdown-content div");
	By tagSelected 					= By.cssSelector(".tags.single .has-items div");
	By tagShowAll 					= By.cssSelector(".filter-tags .show-all img");
	By tagShowAllTagListCheckBox 	= By.cssSelector(".filter-tags .modal-body .recording-item input");
	By tagShowAllTagList 			= By.cssSelector(".filter-tags .modal-body .recording-item .tags span");
	By showAllApply 				= By.cssSelector(".apply");

	// Call Disposition
	By dispositionFilter 			= By.cssSelector(".filter-call-dispositions .selectize-control.callDispositions.single");
	By dispositionFilterInput 		= By.cssSelector(".filter-call-dispositions input");
	By dispositionSelected 			= By.cssSelector(".filter-call-dispositions .has-items div");
	By dispositionFilterValues 		= By.cssSelector(".callDispositions.single .selectize-dropdown-content div");
	By dispositionShowAll			= By.cssSelector(".filter-call-dispositions .show-all img");
	By dispositionShowAllListCheckBox = By.cssSelector(".filter-call-dispositions .modal-body .recording-item input");
	By dispositionShowAllList 		= By.cssSelector(".filter-call-dispositions .modal-body .recording-item .non-tags");
	
	// Lead Status
	By leadStatusFilter 			= By.cssSelector(".leadStatuses.single");
	By leadStatusFilterInput 		= By.cssSelector(".filter-lead-status input");
	By leadStatusSelected 			= By.cssSelector(".filter-lead-status .has-items div");
	By leadStatusFilterValues 		= By.cssSelector(".leadStatuses.single .selectize-dropdown-content div");
	By leadStatusShowAll			= By.cssSelector(".filter-lead-status .show-all img");
	By leadStatusShowAllListCheckBox = By.cssSelector(".filter-lead-status .modal-body .recording-item input");
	By leadStatusShowAllList 		= By.cssSelector(".filter-lead-status .modal-body .recording-item .non-tags");
	
	// Opportunity Stage
	By oppStageFilter 				= By.cssSelector(".opportunityStages.single");
	By oppStageFilterInput 			= By.cssSelector(".filter-opportunity-stage input");
	By oppStageSelected 			= By.cssSelector(".filter-opportunity-stage .has-items div");
	By oppStageFilterValues 		= By.cssSelector(".opportunityStages.single .selectize-dropdown-content div");
	By oppStageShowAll				= By.cssSelector(".filter-opportunity-stage .show-all img");
	By oppStageShowAllListCheckBox 	= By.cssSelector(".filter-opportunity-stage .modal-body .recording-item input");
	By oppStageShowAllList 			= By.cssSelector(".filter-opportunity-stage .modal-body .recording-item .non-tags");
	
	// Library
	By libraryFilter 				= By.cssSelector(".libraries.single");
	By libraryFilterInput 			= By.cssSelector(".filter-libraries input");
	By librarySelected 				= By.cssSelector(".filter-libraries .has-items div");
	By libraryFilterValues 			= By.cssSelector(".libraries.single .selectize-dropdown-content div");
	By libraryShowAll 				= By.cssSelector(".filter-libraries .show-all img");
	By libraryShowAllTagListCheckBox = By.cssSelector(".filter-libraries .modal-body .recording-item input");
	By libraryShowAllLibraryList 	= By.cssSelector(".filter-libraries .modal-body .recording-item .non-tags");

	// Has Notes Check box
	By hasNotesInput 				= By.cssSelector("input[value='hasNotes']");
	By hasSupNotesInput 			= By.cssSelector("input[value='hasSupervisorNotes']");
	By hasAnnotationInput 			= By.cssSelector("input[value='hasAnnotations']");
	By inboundDirection 			= By.cssSelector("input[value='inbound']");
	By outboundDirection 			= By.cssSelector("input[value='outbound']");
	By sharedWithOthers 			= By.cssSelector("input[value='sharedOthers']");
	By flaggedForCoaching 			= By.cssSelector("input[value='flagged']");

	// Call rating filter
	String callRatingSelect 		= ".//*[@data-type='callRatings']/input[contains(@value, '$$rating$$')]";

	//Keyword Filter
	By keywordsList					= By.cssSelector(".filter-keywords .sub-title");
	String keywordGroupPhrase 		= ".//div[@class='filter-keywords']//label[contains(text(),'$$keywordGroup$$')]/..//*[contains(@class,'not-full')]"; 
	String keywordGroupPhraseInput	=	".//div[@class='filter-keywords']//label[contains(text(),'$$keywordGroup$$')]/..//*[contains(@class,'not-full')]/input";
	String keywordGroupPhraseList   = ".//div[@class='filter-keywords']//label[contains(text(),'$$keywordGroup$$')]/..//*[contains(@class,'dropdown-content')]/div";
	
	//player transcript
	By transcriptionCount 			= By.xpath("//label[text()='Transcription:']/..//span[@class='count']");
	By callTranscriptWords 			= By.cssSelector(".vbs-turntimes .vbs-trans-word");
	String phraseInTranscription 	= ".//*[@class='listing__transcript__spans']//span[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ ','abcdefghijklmnopqrstuvwxyz') = '$$phrase$$']";
	
	// Search order filter
	By searchOrder 					= By.cssSelector(".search-direction");
	By searchOrderDisabled 			= By.cssSelector(".search-direction:disabled");
	By timeFrameFilter 				= By.cssSelector(".filter-timeframe.single .has-items");
	By timeFrameFilterValues 		= By.cssSelector(".filter-timeframe.single .selectize-dropdown .selectize-dropdown-content div");
	By timeFrameFilterInput 		= By.cssSelector(".filter-timeframe.single input");
	By timeFrameSelected 			= By.cssSelector(".filter-timeframe.single .has-items .item");

	// Conversation AI Player Call Detail
	By callTab 						= By.cssSelector("[data-tab='calls']");
	By activityTab 					= By.cssSelector("[data-tab='feed']");

	By viewCAI 						= By.cssSelector(".search-results .view-conversation");
	By libraryInput 				= By.cssSelector(".library-picker input");
	By libraryList 					= By.cssSelector("#libs button");
	By libraryBoxCloseBtn 			= By.cssSelector(".close");

	By caiCallAgent 				= By.xpath("//label[text()='Agent:']/..//span");
	By caiCallOppStage 				= By.xpath("//label[text()='Oppty Stage']/..//span");
	By caiCallDate 					= By.xpath("//label[text()='Date:']/..//span");
	By caiCallName 					= By.xpath("//label[text()='Name:']/..//span/span");
	By caiSFLinkSFDCTaskLine 		= By.cssSelector(".sf-link.sfdc-task-link.tooltips");
	By caiSFLinkCallerName 			= By.cssSelector(".sf-link.caller-name-link.tooltips");
	By caiSFLinkCompanyLink			= By.cssSelector(".sf-link.caller-company-link.tooltips");
	By caiCallTitle 				= By.xpath("//label[text()='Title:']/..//span");
	By caiCallPhone 				= By.xpath("//label[text()='Phone:']/..//span");
	By caiCallEmail 				= By.xpath("//label[text()='E-mail:']/..//span");
	By caiCallCompany 				= By.xpath("//label[text()='Company:']/..//span/span");
	By caiCallDuration 				= By.xpath("//label[text()='Call Duration:']/..//span");

	By caiPlayerDuration 			= By.cssSelector(".vbs-section-title .vbs-ftime");
	By caiPlayerStartDuration 		= By.cssSelector(".vbs-section-title .vbs-ctime");

	By caiCallDisposition 			= By.xpath("//label[text()='Call Disposition:']/..//span");

	By caiCallDirection				= By.xpath("//label[text()='Call Direction:']/..//span");
	By caiCallLeadStatus 			= By.xpath("//label[text()='Lead Status']/..//span");
	By caiCallAssociatedRecord 		= By.xpath("//label[text()='Associated Record:']/..//span/span");
	By caiCallNotes 				= By.cssSelector("#info-notes .call-notes");
	By caiCallNotesSave 			= By.xpath(".//*[@class='call-notes']/../*[contains(@class,'success')]");
	By noteSaveMessage 				= By.cssSelector(".toast-message");
	By caiSupNotes 					= By.cssSelector(".supervisor-notes");
	By caiSupNotesSave 				= By.xpath(".//*[@class='supervisor-notes']/parent::div//button[contains(@class,'btn-success')]/span");
	By annotateBtn 					= By.cssSelector(".annotate-button");
	By annotateBtnDisabled 			= By.cssSelector(".annotate-button:disabled");
	By annotateStopBtn 				= By.cssSelector(".annotate-stop");

	By caiMultipleCaller 			= By.xpath("//*[contains(@class,'talktime')]// label[contains(text(), 'Multiple')]");
	By caiUnknownCaller 			= By.xpath("//*[contains(@class,'talktime')]// label[contains(text(), 'Unknown Caller')]");
	By selectedTag 					= By.cssSelector(".tagger .item");
	By existingTag					= By.cssSelector(".smart-recording-tagger .multi-value-label");
	By newTagAdd 					= By.cssSelector(".tagger .selectize-dropdown-content .create");
	By tagRemove 					= By.cssSelector(".multi-value-container .multi-value-remove svg path");
	By calPlay 						= By.cssSelector("[data-title='Play']");
	By calPause 					= By.cssSelector("[data-title='Pause']");
	By playerProgress				= By.xpath(".//*[@vector-effect='non-scaling-stroke']");
	By playerSlider 				= By.cssSelector(".ant-layout-has-sider .ant-layout-content");
	By knowledgeSection				= By.xpath("//*[contains(@class,'ant-layout-footer')]//*[contains(@class,'ant-menu-item')]/span[normalize-space(text()) = 'Topics']");
	By keywordList					= By.cssSelector(".listing__keywords-of-topic .listing__keywords__keyword-name");
	By keywordMarker 				= By.cssSelector(".vbs-markers .vbs-marker");
	By keywordText 					= By.cssSelector(".vbs-word");
	By keywordComment 				= By.cssSelector(".vbs-markers .vbs-comment b");

	By caiCallDetailsTag 			= By.xpath("//label[@for='tag']/..//div[contains(@class,'value-container')]//input");

	// Annotation Section on CAI Call player
	By annotationSectionFilter 		= By.cssSelector(".annotation-list .filter-by");
	By annotationMessageList 		= By.cssSelector(".annotation-list .annotation-item .notes");
	String annotationNotesForUser 	= ".//*[contains(@class,'annotation-item')]//*[contains(text(),'$$username$$')]/../../*[@class='notes']";
	By annotationAgnetList 			= By.cssSelector(".annotation-list .annotation-item .user-name");
	String annotationMessagePage 	= ".//div[contains(@class,'notes') and text()='$$annotation$$']";
	String annotationDatePage 		= ".//div[contains(@class,'notes') and text()='$$annotation$$']/..//span[@class='created-at']";
	String annotationUserPage 		= ".//div[contains(@class,'notes') and text()='$$annotation$$']/..//div[@class='user-name']";
	String annotationHighlighted 	= ".//div[contains(@class,'notes') and text()='$$annotation$$']/../.";
	String annotationUserSegment 	= ".//*[contains(@class,'user-container')]//*[text()='$$username$$']/../..//*[contains(@class,'annotate-link')]";
	String annotationUserSegmentActive = ".//*[text()='$$username$$']/../..//*[contains(@class,'active annotate-link')]";
	String annotationSegmentColor 	= ".//*[text()='$$username$$']/../..//*[contains(@class,'annotate-link')]//*[name()='svg']/*[name()='rect']";
	
	// Annotate pop up Box
	By annotationPopUpBox 			= By.cssSelector(".annotate-overlay.active .notes");
	By annotateAgentName 			= By.cssSelector(".annotate-overlay.active strong");
	By annotationDate 				= By.cssSelector(".annotate-overlay div .created-at");
	By annotationMessage 			= By.cssSelector(".annotate-overlay.active .notes");
	By annotationMessageReadOnly 	= By.cssSelector(".annotate-overlay.active p");
	By annotationSaveBtn 			= By.cssSelector(".annotate-overlay.active .save");
	By annotationCancelBtn 			= By.cssSelector(".annotate-overlay.active .cancel");

	By annotationTag 				 = By.cssSelector(".annotation-tagger .value-container input");
	By existingAnnotationTag 	     = By.cssSelector(".annotation-tagger .multi-value-label");
	By existingAnnotationTagReadOnly = By.cssSelector(".annotate-overlay.active .tagger-container div");
	By annotationTagSelectedItem 	 = By.cssSelector(".annotation-tagger .multi-value-label");
	By callDetailsTagSelectdItem 	 = By.xpath("//label[@for='tag']/..//div[contains(@class,'multi-value-label')]");
	By tagSelectedText				 = By.xpath("//label[@for='tag']/..//div[contains(@class,'multi-value-label')][not(@button)]");
	By tagSentimentUpIcon  			 = By.cssSelector(".tag-sentiment .glyphicon.glyphicon-thumbs-up");
	By tagSentimentDownIcon			 = By.cssSelector(".tag-sentiment .glyphicon.glyphicon-thumbs-down");
	By tagDropDown 		   			 = By.xpath("//label[@for='tag']/..//div[contains(@class,'menu')]//div[contains(@class,'option')] | //div[@class='annotation-tagger']/..//div[contains(@class,'menu')]//div[contains(@class,'option')]");
	
	// ConversationAI-Player buttons

	By playerTimer 		  = By.cssSelector(".vbs-section-title .vbs-ctime");
	By playIcon             = By.cssSelector(".ant-btn-group .anticon-caret-right");
	By pauseIcon			= By.cssSelector(".anticon-pause");
	By backIcon				= By.cssSelector(".anticon-backward");
	
	// Share
	By shareCallButton 	  = By.cssSelector(".share-recording");
	By shareCallMessage   = By.cssSelector("#share-recording-modal .message");
	By callActionBtn 	  = By.cssSelector(".pull-right .static-width-button");
	By shareCallAction 	  = By.xpath(".//*[@class='react-call']//button[text()='Share Call']");
	By addLibraryAction   = By.xpath(".//*[text()='Add to Library']");;
	By downloadFileAction = By.cssSelector(".dropdown-menu [title='Download']");
	By shareCallAgent     = By.xpath("//*[@class='form-group share-container']//input");
	By shareEmailValue    = By.cssSelector(".selectize-dropdown.multi.share-picker .selectize-dropdown-content span");
	By existingShareCallEmailId = By.cssSelector(".share-picker .has-items div");
	By shareSendButton    = By.cssSelector(".send");
	By shareCancelButton  = By.cssSelector(".btn-default");

	// Review Recording

	By reviewCall				= By.xpath(".//*[text()='Add Reviewers']");
	By searchUserForReview 		= By.cssSelector(".search");
	By reviewCheckBox 			= By.cssSelector(".backgrid .checkbox-cell");
	String selectUserForReview 	= ".//*[text()='$$username$$']/../..//*[@class='checkbox-cell sortable renderable']";
	By reviewCallBtn 			= By.cssSelector(".review-call-button");
	By markAsReviewBtn 			= By.cssSelector(".static-width-button.reviewed");
	By reviewedDate 			= By.cssSelector(".reviewed span.review-date");
	By reviewersNameList		= By.cssSelector("td.name-cell b");
	By reviewersEmailList		= By.cssSelector("td.name-cell span");
	
	// Search Result-Agent Data-DropDown Filter
	By caiResultList 				= By.cssSelector(".recording-item");
	By recordingNotFoundMessage 	= By.xpath("//*[@class='toast-message' and contains(text(),'Recording not found')]");
	By caiWithMatchingTranscription	= By.cssSelector(".recording-item .transcription-count");
	By nextPage 					= By.cssSelector(".next-page:not(.disabled)");
	By nextPageDisabled 			= By.cssSelector(".next-page.disabled");
	By previousPage 				= By.cssSelector(".previous-page:not(.disabled)");
	By previousPageDisabled 		= By.cssSelector(".previous-page.disabled");
	By totalPage 					= By.cssSelector(".total-page");
	By currentPage 					= By.cssSelector(".current-page");
	By agentResult 					= By.cssSelector("[data-name='agents']");
	By companyResult 				= By.cssSelector("[data-name='company']:not([data-value =''])");
	By nameResult 					= By.cssSelector("[data-name='name']:not([data-value =''])");
	By titleResult 					= By.cssSelector("[data-name='title']:not([data-value = ''])");
	By dispositionResult 			= By.cssSelector("[data-name='callDispositions']:not([data-value = ''])");
	By leadStatusResult 			= By.cssSelector("[data-name='leadStatuses']:not([data-value = ''])");
	By oppStageResult 				= By.cssSelector("[data-name='opportunityStages']:not([data-value = ''])");
	By callTime 					= By.xpath("//*[@data-name='agents']/../../following-sibling::div[1]/span");
	By callDuration 				= By.xpath("//label[text()='Duration:']/..//span");
	String tagResult 				= ".//*[@class='recording-item'][$$Row$$]//*[@data-name='tags']";
	String libraryResult 			= ".//*[@class='recording-item'][$$Row$$]//*[@data-name='libraries']";

	// Search Result-Agent Data-CheckBox Filters
	By notesIconStatus				= By.xpath(".//*[@data-value='notes']/..");
	By notesIconInactive 			= By.cssSelector(".extra-info:not(.active) [data-value='notes']");
	By notesIconActive 				= By.cssSelector(".extra-info.active [data-value='notes']");
	By supNotesIconStatus			= By.xpath(".//*[@data-value='supervisor-notes']/..");
	By supNotesIconInactive 		= By.cssSelector(".extra-info:not(.active) [data-value='supervisor-notes']");
	By supNotesIconActive 			= By.cssSelector(".extra-info.active [data-value='supervisor-notes']");
	By annotationIconStatus			= By.xpath(".//*[@data-value='airlock']/..");
	By annotationIconInactive 		= By.cssSelector(".extra-info:not(.active) [data-value='airlock']");
	By annotationIconActive 		= By.cssSelector(".extra-info.active [data-value='airlock']");

	// Call performance section
	By talkRatioToolTip 	= By.xpath("//*[@class='talk-ratio-text']/..//b");
	By overTalkToolTip 		= By.xpath("//*[@class='overtalk-text']/..//b");
	By talkStreakToolTip 	= By.xpath("//*[@class='talk-streak-text']/..//b");

	// Enter Text to be Searched in CAI Calls
	public void enterTextToSearchCAI(WebDriver driver, String text) {

		clickElement(driver, caiTextFilter);
		enterText(driver, caiTextFilter, text);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);

	}

	// Enum to select order on Calls Tab
	public static enum SearchOrderOptions {
		Descending("Date - Newest to Oldest"), Ascending("Date - Oldest to Newest"), DurationDescending("Call Duration - Longest to Shortest"),DurationAscending("Call Duration - Shortest to Longest"),AgentAscending("Agent Name - A to Z"),AgentDescending("Agent Name - Z to A");

		private String displayName;

		SearchOrderOptions(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}

	}

	// Enum for Time Frame
	public static enum TimeFrameOptions {
		All("All Time"), Today ("Today"),Week("1 week"), Month("1 month"), Quarter("3 months"), Annual("1 year"), Set("Custom");
		private String displayName;

		TimeFrameOptions(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}

	}

	// Enum for Call detail
	public static enum CallData {
		Agent, Company, Name, Title, Disposition, LeadStatus, OppStage, CallDate, CallDuration, Tag, Library, HasNotes, HasSupNotes, SharedWithOthers, FlaggedForCoaching, HasAnnotations, callDirectionInbound, callDirectionOutbound, ShowAllTags, ShowAllLibraries,ShowAllLeadStatus,ShowAllCallDisposition,ShowAllOppStage
	}

	// Click Annotate Button
	public void clickAnnotateBtn(WebDriver driver) {
		waitUntilVisible(driver, annotateBtn);
		scrollToElement(driver, annotateBtn);
		clickElement(driver, annotateBtn);
	}
	
	// Is Annotation disabled
	public boolean isAnnotationBtnDisabled(WebDriver driver) {
		return isElementVisible(driver, annotateBtnDisabled, 5);
	}

	// Is Annotation enabled
	public boolean isAnnotationBtnEnabled(WebDriver driver) {
		return isElementVisible(driver, annotateBtn, 5);
	}
	
	// Click Stop to stop Annotation
	public void clickStopBtn(WebDriver driver) {
		clickElement(driver, annotateStopBtn);
		waitUntilVisible(driver, annotationSaveBtn);
	}

	// Click Stop to stop Annotation
	public void enterAnnotation(WebDriver driver, String annotation) {
		waitUntilVisible(driver, annotateAgentName);
		assertTrue(getElementsText(driver, annotateAgentName).contains(getElementsText(driver, userName)));
		clickElement(driver, annotationMessage);
		enterText(driver, annotationMessage, annotation);
		enterAnnotationTag(driver, annotation);
		String annodate1[] = getElementsText(driver, annotationDate).split(" ", 2);
		String annodate = annodate1[0];
		clickElement(driver, annotationSaveBtn);
		idleWait(2);
		assertTrue(annotationExistInList(driver, annotation), "Saved annotation not found of List");
		System.out.println(annodate);
		assertEquals(annotationMsgUser(driver, annotation), getElementsText(driver, userName));
	}

	//Cancel Annotation
	public void clickCancelAnnotation(WebDriver driver){
		clickElement(driver, annotationCancelBtn);
		assertFalse(isElementVisible(driver, annotationSaveBtn, 1));
	}
	
	// Check Availability of Marked as Reviewed button

	public boolean markAsReviewAvailable(WebDriver driver) {
		return isElementVisible(driver, markAsReviewBtn, 5);
	}

	// Click Marked as Reviewed button
	public void clickForReview(WebDriver driver) {

		String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		System.out.println(currentDate);
		clickElement(driver, markAsReviewBtn);
		waitUntilVisible(driver, reviewedDate);
		assertTrue(getElementsText(driver, reviewedDate).trim().contains(currentDate));
	}

	// Method to set Call Duration range filter on Calls Tab
	public Pair<String, String> getCallDurationRange(WebDriver driver) {
		String[] durationRange = getElementsText(driver, callDurationRangeMin).split("- ");
		return new Pair<String, String>(durationRange[0], durationRange[1]);
	}

	public static boolean valueInRange(float x, float min, float max) {
		if (Float.compare(x, min) >= 0 && Float.compare(x, max) <= 0) {
			return true;
		}
		return false;
	}

	// Method to click Add Review Icon on Call player from Action
	public void clickReviewIcon(WebDriver driver) {
		waitUntilVisible(driver, callActionBtn);
		clickElement(driver, callActionBtn);
		waitUntilVisible(driver, reviewCall);
		clickElement(driver, reviewCall);
		waitUntilVisible(driver, reviewCheckBox);
	}

	// Method to enter agent name in to whom we want to Review cai call
	public void enterReviewUser(WebDriver driver, String agent) {
		waitUntilVisible(driver, searchUserForReview);
		clickElement(driver, searchUserForReview);
		assertFalse(HelperFunctions.hasDuplicateItems(getTextListFromElements(driver, reviewersNameList)));
		assertFalse(HelperFunctions.hasDuplicateItems(getTextListFromElements(driver, reviewersEmailList)));
		enterText(driver, searchUserForReview, agent);
		idleWait(2);
		assertEquals(getElements(driver, By.xpath(selectUserForReview.replace("$$username$$", agent))).size(), 1);
		clickElement(driver, By.xpath(selectUserForReview.replace("$$username$$", agent)));
		clickElement(driver, reviewCallBtn);
	}

	// Method to Verify Call Duration range result based on filter on Calls Tab
	public void verifyCallDurationRange(WebDriver driver, float min, float max, int page) {
		for (int j = 0; j < page && j < 2; j++) {
			List<WebElement> resultList = getElements(driver, viewCAI);
			if (resultList.size() == 0) {
				System.out.println("No matching Result Found");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i += 2) {
					resultList = getElements(driver, viewCAI);
					List<WebElement> calldurationList = getElements(driver, callDuration);
					String[] callDurationString = calldurationList.get(i).getText().trim().split(":");
					String CallDurationMin = callDurationString[1];
					String CallDurationSec = callDurationString[2];
					String totalCallDuration = CallDurationMin + "." + CallDurationSec;
					float totalCall = Float.parseFloat(totalCallDuration);
					assertTrue(valueInRange(totalCall, min, max), "Error-Duration beyond given range");

					idleWait(1);
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}

		clearAllFilters(driver);
	}

	// Method to set call duration filter
	public void setDuration(WebDriver driver) {
		WebElement minSlider = findElement(driver, callDurationMinSlider);
		WebElement maxSlider = findElement(driver, callDurationMaxSlider);
		Actions move = new Actions(driver);
		Action action1 = move.dragAndDropBy(minSlider, 4, 0).build();
		idleWait(1);
		action1.perform();
		dashboard.isPaceBarInvisible(driver);
		Action action2 = move.dragAndDropBy(maxSlider, -120, 0).build();
		idleWait(1);
		action2.perform();
		dashboard.isPaceBarInvisible(driver);
	}

	// Method to set agent TalkTime filter
	public void setAgentTalkTime(WebDriver driver) {
		WebElement minSlider = findElement(driver, agentTalkTimeMinSlider);
		WebElement maxSlider = findElement(driver, agentTalkTimeMaxSlider);
		Actions move = new Actions(driver);
		Action action1 = move.dragAndDropBy(minSlider, 30, 0).build();
		idleWait(1);
		action1.perform();
		dashboard.isPaceBarInvisible(driver);
		Action action2 = move.dragAndDropBy(maxSlider, -80, 0).build();
		idleWait(1);
		action2.perform();
		dashboard.isPaceBarInvisible(driver);
	}

	// Method to get Agent talk time %

	public String getAgentTalkTimePercentage(WebDriver driver) {
		String[] talkTimeP = getElementsText(driver, agentTalkTimePercentage).split("/", 2);
		String tTP = talkTimeP[1].replace("%", "").trim();
		return tTP;
	}

	public String getCallerTalkTimePercentage(WebDriver driver) {
		By locator = null;
		if (!isElementVisible(driver, caiCallName, 1)) {
			if (!isElementVisible(driver, caiUnknownCaller, 1)) {
				String caller = "Multiple";
				locator = By.xpath(callerTalkTimePercentage.replace("$$callername$$", caller));
			} else {
				String caller = "Unknown Caller";
				locator = By.xpath(callerTalkTimePercentage.replace("$$callername$$", caller));
			}
		} else {
			locator = By.xpath(callerTalkTimePercentage.replace("$$callername$$", getCallerName(driver)));
		}
		String[] talkTimeP = getElementsText(driver, locator).split("/", 2);
		String tTP = talkTimeP[1].replace("%", "").trim();
		return tTP;
	}

	// Method to Verify Call Duration range result based on filter on Calls Tab
	public void verifyTalkTimePercentage(WebDriver driver, float min, float max, int page) {
		for (int j = 0; j < page && j < 1; j++) {
			List<WebElement> resultList = getElements(driver, viewCAI);
			if (resultList.size() == 0) {
				System.out.println("No matching Result Found");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i++) {
					resultList = getElements(driver, viewCAI);
					clickElement(driver, resultList.get(i));
					float agentTTP = Float.parseFloat(getAgentTalkTimePercentage(driver));
					assertTrue(valueInRange(agentTTP, min, max), "Error-Duration beyond given range");
					float callerTPP = Float.parseFloat(getCallerTalkTimePercentage(driver));
					float expectedCallerTPP = 100 - agentTTP;
					assertEquals(callerTPP, expectedCallerTPP);
					System.out.println("Agent Talk Time " + agentTTP + "% is within range of " + min + "% and " + max
							+ "%.Caller talk Time is " + callerTPP + "%");
					scrollToElement(driver, callTab);
					clickCallTab(driver);
					idleWait(1);
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}

		clearAllFilters(driver);
	}

	// Method to click Share Icon on Call player from Action
	public void clickShareIcon(WebDriver driver) {
		clickElement(driver, callActionBtn);
		waitUntilVisible(driver, shareCallAction);
		clickElement(driver, shareCallAction);
		waitUntilVisible(driver, shareCallAgent);
	}

	// Method to enter agent name in to whom we want to share cai call
	public String enterShareId(WebDriver driver, String agent) {

		String startDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		enterText(driver, shareCallMessage, startDate);
		findElement(driver, shareCallAgent).sendKeys(agent);
		idleWait(2);
		clickEnter(driver, shareCallAgent);
		return startDate;
	}

	// Method to click Send Icon after filling details to share call
	public void clickSendIcon(WebDriver driver) {
		clickElement(driver, shareSendButton);
		// waitUntilInvisible(driver, shareSendButton);
	}

	// Method to get detail about call direction from call detail page
	public String getCallDirection(WebDriver driver) {
		return getElementsText(driver, caiCallDirection);
	}

	// Method to get detail about agent name from call detail page
	public String getCallAgent(WebDriver driver) {
		return getElementsText(driver, caiCallAgent);
	}

	public String getCallerName(WebDriver driver) {
		return getElementsText(driver, caiCallName).trim();
	}

	// Method to get detail about name from call detail page
	public String getCallName(WebDriver driver) {
		return getElementsText(driver, caiCallName);
	}

	// Click SF link SFDC from call detail page
	public void clickSFLinkSFDC(WebDriver driver) {
		idleWait(2);
		waitUntilVisible(driver, caiSFLinkSFDCTaskLine);
		scrollToElement(driver, caiSFLinkSFDCTaskLine);
		clickElement(driver, caiSFLinkSFDCTaskLine);
	}
	
	public boolean isSFCompanyLinkVisible(WebDriver driver){
		return isElementVisible(driver, caiSFLinkCompanyLink, 5);
	}

	// Click SF link Caller Name from call detail page
	public void clickSFLinkCallerName(WebDriver driver) {
		waitUntilVisible(driver, caiSFLinkCallerName);
		assertTrue(isElementVisible(driver, caiSFLinkCallerName, 5));
		hoverElement(driver, caiSFLinkCallerName);
		assertEquals(getElementsText(driver, caiSFLinkCallerName), "Link to Salesforce");
		clickElement(driver, caiSFLinkCallerName);
	}

	// Method to get detail about name from call detail page
	public String getCallEmail(WebDriver driver) {
		return getElementsText(driver, caiCallEmail);
	}

	// Method to get detail about name from call detail page
	public String getCallDisposition(WebDriver driver) {
		return getElementsText(driver, caiCallDisposition);

	}

	// Method to get detail about name from call detail page
	public String getCallCompany(WebDriver driver) {
		return getElementsText(driver, caiCallCompany);
	}

	// Method to get detail about name from call detail page
	public String getCallNumber(WebDriver driver) {
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, caiCallPhone));
	}

	// Method to get detail about title from call detail page
	public String getCallTitle(WebDriver driver) {
		return getElementsText(driver, caiCallTitle);
	}

	// Method to get detail about call time from call detail page
	public String getCallTime(WebDriver driver) {
		String callDuration = getElementsText(driver, caiCallDate).trim();
		if (callDuration.contains("PM")) {
			callDuration = callDuration.replace("PM", "pm");
		} else {
			callDuration = callDuration.replace("AM", "am");
		}
		return callDuration;
	}

	// Method to get detail about call duration from call detail page
	public String getCallDuration(WebDriver driver) {
		String callDuration = getElementsText(driver, caiCallDuration).trim();
		if (callDuration.contains("PM")) {
			callDuration = callDuration.replace("PM", "pm");
		} else {
			callDuration = callDuration.replace("AM", "am");
		}
		return callDuration;
	}

	// Method to get detail about Lead Status from call detail page
	public String getLeadStatus(WebDriver driver) {
		return getElementsText(driver, caiCallLeadStatus);
	}

	// Method to get detail about Opp Stage from call detail page
	public String getOppStage(WebDriver driver) {
		return getElementsText(driver, caiCallOppStage);
	}

	// Method to get call duration of first call on calls tab

	public String getCallDurationCallTabResult(WebDriver driver) {

		List<WebElement> agentList = getInactiveElements(driver, callDuration);
		return agentList.get(0).getText().trim();
	}

	// Method to get detail about call duration from call detail page
	public String getPlayerCallDuration(WebDriver driver) {
		return getElementsText(driver, caiPlayerDuration);
	}

	// Method to get detail about call duration start timer from call detail
	// page
	public String getPlayerCallStartDuration(WebDriver driver) {
		return getElementsText(driver, caiPlayerStartDuration);
	}

	// Method to get detail about call notes from call detail page
	public String getCallNotes(WebDriver driver) {
		return getElementsText(driver, caiCallNotes);
	}

	// Method to get detail about lead status from call detail page
	public String getCallAssociatedRecord(WebDriver driver) {
		waitUntilVisible(driver, caiCallAssociatedRecord);
		return getElementsText(driver, caiCallAssociatedRecord);
	}

	// Verify that added library exist on player and library Tab
	public boolean verifyLibraryAddedOnCallTab(WebDriver driver, String libName) {

		dashboard.clickConversationAI(driver);
		viewCAI(driver);
		clickLibraryPicker(driver);
		// LibraryList on Player
		List<WebElement> callLibList = getElements(driver, libraryList);
		boolean a = isTextPresentInList(driver, callLibList, libName);
		clickElement(driver, libraryBoxCloseBtn);
		idleWait(1);
		libraryTabPage.clickLibraryTab(driver);
		List<WebElement> libTabList = getElements(driver, libraryTabPage.libraryTabList);
		boolean b = isTextPresentInList(driver, libTabList, libName);
		assertTrue(a, String.format("Added Library:%s not found in Call Tab List", libName));
		assertTrue(b, String.format("Added Library:%s not found in Library Tab List", libName));

		// Click Newly Created Library
		clickElement(driver, By.xpath(libraryTabPage.libraryItem.replace("$$library_name$$", libName)));
		idleWait(2);
		assertTrue(isElementVisible(driver, libraryTabPage.searchOrderDisabled, 5));
		assertTrue(getElementsText(driver, libraryTabPage.libraryNoResult).trim().contains("No recordings found."));

		return a && b;
	}

	// Verify that removed library not exist on player and library Tab
	public void verifyLibraryRemoved(WebDriver driver, String libName) {
		dashboard.clickConversationAI(driver);
		clearAllFilters(driver);
		viewCAI(driver);
		clickLibraryPicker(driver);
		// LibraryList on Player
		assertFalse(checkLibInList(driver, libName), "Added Library Not removed from List");
		// LibraryList on Library Tab
		libraryTabPage.clickLibraryTab(driver);
		assertFalse(libraryTabPage.isLibraryExistsOnLibraryTab(driver, libName), "Added Library Not removed from List");
	}

	// Open first Conversation AI Recording from Calls Tab
	public void viewCAI(WebDriver driver) {
		waitUntilVisible(driver, viewCAI, 5);
		List<WebElement> caiList = getElements(driver, viewCAI);
		clickElement(driver, caiList.get(0));
		dashboard.isPaceBarInvisible(driver);
	}

	// Added Library exist in List of Libraries
	public boolean checkLibInList(WebDriver driver, String lib) {
		if (!isElementVisible(driver, libraryList, 1)) {
			return false;
		} else {
			List<WebElement> libList = getElements(driver, libraryList);
			return isTextPresentInList(driver, libList, lib);
		}
	}

	// Set Library for a conversation ai recording
	public void setLibraryOnCall(WebDriver driver, String lib) {
		clickLibraryPicker(driver);
		List<WebElement> libList = getElements(driver, libraryList);
		for (int i = 0; i < libList.size(); i++) {
			if (libList.get(i).getText().trim().equals(lib.trim())) {
				clickElement(driver, libList.get(i));
				clickElement(driver, libraryBoxCloseBtn);
				break;
			}
		}
	}

	// Set Library filter on Calls tab
	public void setLibraryFilter(WebDriver driver, String lib) {
		clickElement(driver, libraryFilterInput);
		findElement(driver, libraryFilterInput).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		findElement(driver, libraryFilterInput).sendKeys(Keys.BACK_SPACE);
		clickElement(driver, libraryFilterInput);
		List<WebElement> libList = getElements(driver, libraryFilterValues);
		for (int i = 0; i < libList.size(); i++) {
			if (libList.get(i).getText().trim().equals(lib.trim())) {
				clickElement(driver, libList.get(i));
				// isElementVisible(driver, paceBar, 2);
				dashboard.isPaceBarInvisible(driver);
				idleWait(2);
				break;
			}
		}
	}

	// Set Library with cai recording
	public void clickLibraryOnLibraryTab(WebDriver driver, String lib) {
		List<WebElement> libList = getElements(driver, libraryTabPage.libraryTabList);
		for (int i = 0; i < libList.size(); i++) {
			if (libList.get(i).getText().trim().equals(lib.trim())) {
				clickElement(driver, libList.get(i));
				dashboard.isPaceBarInvisible(driver);
				idleWait(2);
				break;
			}
		}
	}

	// Verify associated library call displayed upon setting that library on
	// search filter
	public boolean verifyAssociatedLibraryCall(WebDriver driver, HashMap<CallsTabPage.CallData, String> callData1) {
		List<WebElement> agentList = getElements(driver, agentResult);
		List<WebElement> callTimeList = getElements(driver, callTime);
		List<WebElement> duration = getElements(driver, callDuration);
		for (int i = 0; i < agentList.size(); i++) {
			if (agentList.get(i).getText().trim().equals(callData1.get(CallData.Agent))
					|| callTimeList.get(i).getText().trim().equals(callData1.get(CallData.CallDate))
					&& duration.get(i).getText().trim().equals(callData1.get(CallData.CallDuration))) {
				return true;
			}
		}
		return false;
	}

	// Click Library Picker
	public void clickLibraryPicker(WebDriver driver) {
		clickElement(driver, callActionBtn);
		waitUntilVisible(driver, addLibraryAction);
		clickElement(driver, addLibraryAction);
		idleWait(1);

	}

	// Close Library Box
	public void closeLibraryBox(WebDriver driver) {
		clickElement(driver, libraryBoxCloseBtn);
		idleWait(1);
	}

	// Open Call tab
	public void clickCallTab(WebDriver driver) {
		clickElement(driver, callTab);
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
	}

	// Open Activity Feed Tab
	public void clickActivityTab(WebDriver driver) {
		clickElement(driver, activityTab);
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
	}

	// Method to click Clear All to remove any cai filter
	
	public boolean isClearAllFilterLinkVisible(WebDriver driver){
		return isElementVisible(driver, clearAllLink, 5);
	}
	
	public void clearAllFilters(WebDriver driver) {
		if (isElementVisible(driver, clearAllLink, 5)) {
			clickElement(driver, clearAllLink);
		}
		dashboard.isPaceBarInvisible(driver);
	}

	// Manually set agent filter for given agent
	public void setAgentFilter(WebDriver driver, String agentName) {
		waitUntilVisible(driver, agentFilterInput);
		clickElement(driver, agentFilterInput);
		enterText(driver, agentFilterInput, agentName);
		clickEnter(driver, agentFilterInput);
		dashboard.isPaceBarInvisible(driver);
		idleWait(3);
	}

	// Check that call Notes enabled
	public boolean callNotesEnabled(WebDriver driver) {
		if (isElementVisible(driver, caiCallNotes, 2) && isElementVisible(driver, caiCallNotesSave, 2))
			return true;
		else
			return false;
	}

	// Enter Call Notes on conversation ai call
	public void enterCallNotes(WebDriver driver, String notes) {
		enterText(driver, caiCallNotes, notes);
		clickElement(driver, caiCallNotesSave);
		waitUntilVisible(driver, noteSaveMessage);
		waitUntilInvisible(driver, noteSaveMessage);
	}

	public String getTextOfCallNotes(WebDriver driver) {
		waitUntilVisible(driver, caiCallNotes);
		return getElementsText(driver, caiCallNotes).trim();
	}

	public boolean isSuperVisorNoteEditable(WebDriver driver) {
		return isElementClickable(driver, caiSupNotes, 5);
	}

	public String getTextOfSuperVisorNotes(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		waitUntilVisible(driver, caiSupNotes);
		scrollToElement(driver, caiSupNotes);
		return getAttribue(driver, caiSupNotes, ElementAttributes.value);
	}

	// Enter Supervisor Notes on conversation ai call
	public void enterSupervisorNotes(WebDriver driver, String notes) {
		dashboard.isPaceBarInvisible(driver);
		scrollToElement(driver, caiSupNotesSave);
		waitUntilVisible(driver, caiSupNotes);
		enterText(driver, caiSupNotes, notes);
		waitUntilVisible(driver, caiSupNotesSave);
		clickElement(driver, caiSupNotesSave);
		waitUntilVisible(driver, noteSaveMessage);
		waitUntilInvisible(driver, noteSaveMessage);
		dashboard.isPaceBarInvisible(driver);
	}

	// Set Conversation AI Filter based on given filter value
	public String setFilter(WebDriver driver, String text) {
		By filterLocator = null;
		By valueLocator = null;
		By selectedFilterValue = null;
		By selectedFilter = null;
		switch (text) {
		case "Agent":
			filterLocator = agentFilter;
			valueLocator = agentFilterValues;
			selectedFilterValue = agentSelected;
			selectedFilter = selectedFilterList;
			break;
		case "Company":
			filterLocator = companyFilter;
			valueLocator = companyFilterValues;
			selectedFilterValue = companySelected;
			selectedFilter = selectedFilterList;
			break;
		case "Name":
			filterLocator = nameFilter;
			valueLocator = nameFilterValues;
			selectedFilterValue = nameSelected;
			selectedFilter = selectedFilterList;
			break;
		case "Title":
			filterLocator = titleFilter;
			valueLocator = titleFilterValues;
			selectedFilterValue = titleSelected;
			selectedFilter = selectedFilterList;
			break;
		case "Disposition":
			filterLocator = dispositionFilter;
			valueLocator = dispositionFilterValues;
			selectedFilterValue = dispositionSelected;
			selectedFilter = selectedFilterList;
			break;
		case "LeadStatus":
			filterLocator = leadStatusFilter;
			valueLocator = leadStatusFilterValues;
			selectedFilterValue = leadStatusSelected;
			selectedFilter = selectedFilterList;
			break;
		case "OppStage":
			filterLocator = oppStageFilter;
			valueLocator = oppStageFilterValues;
			selectedFilterValue = oppStageSelected;
			selectedFilter = selectedFilterList;
			break;
		case "Tag":
			filterLocator = tagFilter;
			valueLocator = tagFilterValues;
			selectedFilterValue = tagSelected;
			break;
		case "Library":
			filterLocator = libraryFilter;
			valueLocator = libraryFilterValues;
			selectedFilterValue = librarySelected;
			selectedFilter = selectedFilterList;
			break;
		case "ShowAllTags":
			filterLocator = tagShowAll;
			valueLocator = tagShowAllTagListCheckBox;
			selectedFilterValue = tagShowAllTagList;
			selectedFilter = selectedTagFilterList;
			break;
		default:
			break;
		}
		clickElement(driver, filterLocator);
		waitUntilVisible(driver, valueLocator, 2);
		List<WebElement> resultList = getElements(driver, valueLocator);
		String selectedValue = getElementsText(driver, resultList.get(1));
		clickElement(driver, resultList.get(1));
		dashboard.isPaceBarInvisible(driver);

		// Value Drop down auto close upon selection of value
		assertFalse(isElementVisible(driver, valueLocator, 1));
		idleWait(1);

		// Check that selected filter displayed as search filter above search
		// result table
		if (selectedFilter != null) {
			List<WebElement> filterList = getElements(driver, selectedFilter);
			assertTrue(isTextPresentInList(driver, filterList, selectedValue));
		}
		return getAttribue(driver, selectedFilterValue, ElementAttributes.dataValue).replace(":NONE", "");
	}

	// Open Show All box by clicking + icon
	public String setShowAllFilter(WebDriver driver, String text) {
		By filterLocator = null;
		By valueLocator = null;
		By selectedFilterValue = null;
		switch (text) {
		case "ShowAllTags":
			filterLocator = tagShowAll;
			valueLocator = tagShowAllTagListCheckBox;
			selectedFilterValue = tagShowAllTagList;
			break;
		case "ShowAllLibraries":
			filterLocator = libraryShowAll;
			valueLocator = libraryShowAllTagListCheckBox;
			selectedFilterValue = libraryShowAllLibraryList;
			break;
		case "ShowAllLeadStatus":
			filterLocator = leadStatusShowAll;
			valueLocator = leadStatusShowAllListCheckBox;
			selectedFilterValue = leadStatusShowAllList;
			break;
		case "ShowAllCallDisposition":
			filterLocator = dispositionShowAll;
			valueLocator = dispositionShowAllListCheckBox;
			selectedFilterValue = dispositionShowAllList;
			break;
		case "ShowAllOppStage":
			filterLocator = oppStageShowAll;
			valueLocator = oppStageShowAllListCheckBox;
			selectedFilterValue = oppStageShowAllList;
			break;

		default:
			break;
		}
		clickElement(driver, filterLocator);
		List<WebElement> resultList = getElements(driver, valueLocator);
		List<WebElement> tagList = getElements(driver, selectedFilterValue);
		clickElement(driver, resultList.get(0));
		String selectedTag = tagList.get(0).getText().trim();
		clickElement(driver, showAllApply);
		idleWait(2);
		return selectedTag;
	}

	public void verifyLibraryNotSelectedCAI(WebDriver driver) {
		assertFalse(isElementVisible(driver, By.xpath(libraryResult.replace("$$Row$$", "1")), 5));
	}

	// Verify conversation AI results based on given library or tab values
	public void verifyTabLibSearchResult(WebDriver driver, String token, int page, String text, boolean status) {
		String locator = null;
		switch (text) {
		case "Tag":
			locator = tagResult;
			break;
		case "Library":
			locator = libraryResult;
			break;
		default:
			break;
		}
		if (status) {
			for (int j = 0; j < page && j < 2; j++) {
				List<WebElement> resultList = getElements(driver, caiResultList);
				if (resultList.size() == 0) {
					System.out.println("No matching Library Found");
					break;
				} else {
					for (int i = 1; i < resultList.size() + 1; i++) {
						if (isElementVisible(driver, By.xpath(locator.replace("$$Row$$", String.valueOf(i))), 2)) {
							By tabLibLocator = By.xpath(locator.replace("$$Row$$", String.valueOf(i)));
							List<String> attributeList = getAttributeValueList(driver, tabLibLocator, ElementAttributes.dataValue);
							if (text.equals("Tag")) {

								assertTrue(isTextPresentInStringList(driver, attributeList, token),
										"Matching tab not exists");
							} else if (text.equals("Library")) {
								assertTrue(isTextPresentInStringList(driver, attributeList, token),
										"Matching Library not exists");
							}
						}
					}
				}
				navigateToNextCAIPage(driver, page, j);
			}
		} else {
			for (int j = 0; j < page && j < 1; j++) {
				List<WebElement> resultList = getElements(driver, caiResultList);
				if (resultList.size() == 0) {
					System.out.println("No matching Library Found");
					break;
				} else {
					for (int i = 1; i < resultList.size() + 1; i++) {
						if (isElementVisible(driver, By.xpath(locator.replace("$$Row$$", String.valueOf(i))), 2)) {
							By tabLibLocator = By.xpath(locator.replace("$$Row$$", String.valueOf(i)));
							List<String> attributeList = getAttributeValueList(driver, tabLibLocator, ElementAttributes.dataValue);
							if (text.equals("Tag")) {
								assertFalse(isTextPresentInStringList(driver, attributeList, token),
										"Matching tab exists for negetive filter");
							} else if (text.equals("Library")) {
								assertFalse(isTextPresentInStringList(driver, attributeList, token),
										"Matching tab exists for negetive filter");
							}
						}
					}
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}
		System.out.println("Search result fine for selected tag " + token);
	}

	// Verify conversation AI search result based on filter set-Boolean
	public void verifySearchResult(WebDriver driver, String token, int page, String text, boolean status) {
		By locator = null;
		switch (text) {
		case "Agent":
			locator = agentResult;
			break;
		case "Company":
			locator = companyResult;
			break;
		case "Name":
			locator = nameResult;
			break;
		case "Title":
			locator = titleResult;
			break;
		case "Disposition":
			locator = dispositionResult;
			break;
		case "LeadStatus":
			locator = leadStatusResult;
			break;
		case "OppStage":
			locator = oppStageResult;
			break;

		default:
			break;
		}
		if (status) {
			for (int j = 0; j < page && j < 2; j++) {
				if (isElementVisible(driver, locator, 1)){
				List<WebElement> resultList = getInactiveElements(driver, locator);
				for (int i = 0; i < resultList.size(); i++) {
					assertEquals(resultList.get(i).getText().toString().trim(), token.trim());
				}
				System.out.println("Test Case Passed. Match Found on this page for given: " + text + token);}
				
				navigateToNextCAIPage(driver, page, j);
			}

		} else {
			for (int j = 0; j < page && j < 2; j++) {
				if (isElementVisible(driver, locator, 1)){
				List<WebElement> resultList = getInactiveElements(driver, locator);

				for (int i = 0; i < resultList.size(); i++) {
					assertFalse(resultList.get(i).getText().toString().trim().equals(token.trim()));
				}
				System.out.println("Test Case Passed. Match Not Found on this page for given: " + text + token);
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}
	}
	
	// Verify conversation AI search result based on filter set-Boolean
	public void verifySearchResult1(WebDriver driver, String token, int page, String text, boolean status) {
		By locator = null;
		switch (text) {
		case "Agent":
			locator = agentResult;
			break;
		case "Company":
			locator = companyResult;
			break;
		case "Name":
			locator = nameResult;
			break;
		case "Title":
			locator = titleResult;
			break;
		case "Disposition":
			locator = dispositionResult;
			break;
		case "LeadStatus":
			locator = leadStatusResult;
			break;
		case "OppStage":
			locator = oppStageResult;
			break;

		default:
			break;
		}
		if (status) {
				
				List<WebElement> resultList = getInactiveElements(driver, locator);
				assertTrue(resultList.get(0).getText().toString().trim().equals(token.trim()));
				selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Ascending.toString());
				resultList = getInactiveElements(driver, locator);
				assertTrue(resultList.get(0).getText().toString().trim().equals(token.trim()));	
				selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Descending.toString());
				resultList = getInactiveElements(driver, locator);
				assertTrue(resultList.get(0).getText().toString().trim().equals(token.trim()));
		}
				
		else {

			List<WebElement> resultList = getInactiveElements(driver, locator);
			assertFalse(resultList.get(0).getText().toString().trim().equals(token.trim()));
			selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Ascending.toString());
			resultList = getInactiveElements(driver, locator);
			assertFalse(resultList.get(0).getText().toString().trim().equals(token.trim()));	
			selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Descending.toString());
			resultList = getInactiveElements(driver, locator);
			assertFalse(resultList.get(0).getText().toString().trim().equals(token.trim()));
		}
	}

	// Method for click Elastic Search Filter
	public String clickElasticSearchFilter(WebDriver driver, String text, int page) {
		By locator = null;
		String searchToken = "";
		switch (text) {
		case "Agent":
			locator = agentResult;
			break;
		case "Company":
			locator = companyResult;
			break;
		case "Name":
			locator = nameResult;
			break;
		case "Title":
			locator = titleResult;
			break;
		case "Disposition":
			locator = dispositionResult;
			break;
		case "LeadStatus":
			locator = leadStatusResult;
			break;
		case "OppStage":
			locator = oppStageResult;
			break;
		default:
			break;
		}
		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);
			for (int i = 1; i < resultList.size() + 1; i++) {
				if (isElementVisible(driver, locator, 1)) {
					List<WebElement> locatorList = getElements(driver, locator);
					searchToken = locatorList.get(0).getText().toString();
					clickElement(driver, locatorList.get(0));
					dashboard.isPaceBarInvisible(driver);
					idleWait(2);
					return searchToken;
				}
			}
			navigateToNextCAIPage(driver, page, j);
		}
		return searchToken;
	}

	// Method to set conversation AI search result order
	public void selectSearchOrder(WebDriver driver, String order) {
		selectFromDropdown(driver, searchOrder, SelectTypes.visibleText, order);
		Select sel = new Select(findElement(driver, searchOrder));
		sel.selectByVisibleText(order);
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
	}

	// Method to set time frame for given value
	public void selectTimeFrame(WebDriver driver, String time) {
		clickAndSelectFromDropDown(driver, timeFrameFilter, timeFrameFilterValues, time);
		dashboard.isPaceBarInvisible(driver);
	}

	// Method for Elastic Search on library/Tab
	public String clickTabLibrary(WebDriver driver, String text, int page) {
		String searchToken = "";
		String locator = null;
		switch (text) {
		case "Tag":
			locator = tagResult;
			break;
		case "Library":
			locator = libraryResult;
			break;
		default:
			break;
		}
		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);
			for (int i = 1; i < resultList.size() + 1; i++) {
				if (isElementVisible(driver, By.xpath(locator.replace("$$Row$$", String.valueOf(i))), 1)) {
					By tagLocator = By.xpath(locator.replace("$$Row$$", String.valueOf(i)));
					List<WebElement> tagList = getElements(driver, tagLocator);
					searchToken = tagList.get(0).getText().toString().replace("#", "");
					clickElement(driver, tagList.get(0));
					dashboard.isPaceBarInvisible(driver);
					idleWait(2);
					return searchToken;
				}
			}
			navigateToNextCAIPage(driver, page, j);
		}
		return searchToken;
	}

	// Method to get total pages of search result
	public String getTotalPage(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return getElementsText(driver, totalPage);
	}

	// Method to get current pages of search result
	public String getCurrentPage(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return getElementsText(driver, currentPage);
	}

	// Method to set Company Filter for given company
	public void setCompanyFilter(WebDriver driver, String company) {
		enterBackspace(driver, companyFilterInput);
		enterText(driver, companyFilterInput, company);
		clickEnter(driver, companyFilterInput);
		dashboard.isPaceBarInvisible(driver);
	}

	// Method to set Name Filter for given name
	public void setNameFilter(WebDriver driver, String name) {
		enterBackspace(driver, nameFilterInput);
		enterText(driver, nameFilterInput, name);
		clickEnter(driver, nameFilterInput);
		dashboard.isPaceBarInvisible(driver);
	}

	// Disposition Filter
	public void setDispositionFilter(WebDriver driver, String disposition) {
		enterText(driver, dispositionFilterInput, disposition);
		clickEnter(driver, dispositionFilterInput);
	}

	public String getDispositionFilter(WebDriver driver) {
		return getElementsText(driver, dispositionFilter);
	}

	public void clearDispositionFilter(WebDriver driver) {
		enterBackspace(driver, dispositionFilterInput);
	}

	// Lead Status Filter
	public void setLeadStatusFilter(WebDriver driver, String leadstatus) {
		enterText(driver, leadStatusFilterInput, leadstatus);
		clickEnter(driver, leadStatusFilterInput);
	}

	public String getLeadStatusFilter(WebDriver driver) {
		return getElementsText(driver, leadStatusFilter);
	}

	public void clearLeadStatusFilter(WebDriver driver) {
		enterBackspace(driver, leadStatusFilterInput);
	}

	// Opportunity Stage Status Filter
	public void setOppStageFilter(WebDriver driver, String oppstage) {
		enterText(driver, oppStageFilterInput, oppstage);
		clickEnter(driver, oppStageFilterInput);
	}

	public String getOppStageFilter(WebDriver driver) {
		return getElementsText(driver, oppStageFilter);
	}

	public void clearOppStageFilter(WebDriver driver) {
		enterBackspace(driver, oppStageFilterInput);
	}

	public void setEngagementFilter(WebDriver driver, String text) {
		By filterLocator = null;
		switch (text) {
		case "HasNotes":
			filterLocator = hasNotesInput;
			break;
		case "HasSupNotes":
			filterLocator = hasSupNotesInput;
			break;
		case "HasAnnotations":
			filterLocator = hasAnnotationInput;
			break;
		case "callDirectionInbound":
			filterLocator = inboundDirection;
			break;
		case "callDirectionOutbound":
			filterLocator = outboundDirection;
			break;
		case "SharedWithOthers":
			filterLocator = sharedWithOthers;
			break;
		case "FlaggedForCoaching":
			filterLocator = flaggedForCoaching;
			break;
		default:
			break;
		}
		waitUntilVisible(driver, filterLocator);
		clickElement(driver, filterLocator);
		dashboard.isPaceBarInvisible(driver);
		isListElementsVisible(driver, selectedFilterList, 5);
		idleWait(2);
	}

	public void clickFirstAgent(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		List<WebElement> agentList = getElements(driver, agentResult);
		clickElement(driver, agentList.get(0));
	}

	// Verify Engagement Filter
	public void verifyEngagementFilter(WebDriver driver, String text, int page, boolean status) {
		By filterLocator = null;
		By inactiveFilterLocator = null;

		switch (text) {
		case "HasNotes":
			filterLocator = notesIconActive;
			inactiveFilterLocator = notesIconInactive;
			break;
		case "HasSupNotes":
			filterLocator = supNotesIconActive;
			inactiveFilterLocator = supNotesIconInactive;
			break;
		case "HasAnnotations":
			filterLocator = annotationIconActive;
			inactiveFilterLocator = annotationIconInactive;
			break;
		default:
			break;
		}
		if (status) {
			for (int j = 0; j < page && j < 2; j++) {
				List<WebElement> resultList = getElements(driver, caiResultList);
				if (resultList.size() == 0) {
					break;
				} else {
					for (int i = 0; i < resultList.size(); i++) {
						assertTrue(resultList.get(i).findElement(filterLocator).isEnabled(),
								"Active icon not found for->" + text);
						/*
						 * assertFalse(resultList.get(i).findElement(
						 * inactiveFilterLocator).isEnabled(),
						 * "InActive icon  found for->" + text);
						 */
					}
					navigateToNextCAIPage(driver, page, j);
				}

			}
			System.out.println("For All results icons are active for->" + text);

		} else {
			for (int j = 0; j < page && j < 2; j++) {
				List<WebElement> resultList = getElements(driver, caiResultList);
				if (resultList.size() == 0) {
					break;
				} else {
					for (int i = 0; i < resultList.size(); i++) {
						// assertFalse(resultList.get(i).findElement(filterLocator).isEnabled(),
						// "Active icon found for->" + text);
						assertTrue(resultList.get(i).findElement(inactiveFilterLocator).isEnabled(),
								"InActive icon not found for->" + text);
					}
					navigateToNextCAIPage(driver, page, j);
				}

			}
			System.out.println("For All results icons are Inactive for->" + text);

		}
	}

	// Method to verify call direction
	public void verifyCallDirection(WebDriver driver, String text, int page) {
		String expectedCallDirection = "";
		switch (text) {
		case "callDirectionInbound":
			expectedCallDirection = "Inbound";
			break;
		case "callDirectionOutbound":
			expectedCallDirection = "Outbound";
			break;
		default:
			break;
		}
		for (int j = 1; j <= page && j <= 1; j++) {
			List<WebElement> resultList = getElements(driver, viewCAI);
			if (resultList.size() == 0) {
				System.out.println("No matching Result Found");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i += 2) {
					resultList = getElements(driver, viewCAI);
					clickElement(driver, resultList.get(i));
					idleWait(2);
					String actualCallDirection = getCallDirection(driver).trim();
					assertEquals(actualCallDirection, expectedCallDirection);
					clickCallTab(driver);
					idleWait(1);
				}
				navigateToNextCAIPage(driver, page, j);
			}

		}
		System.out.println("For All results icons are active for->" + text);
		clearAllFilters(driver);
	}

	// Search Result Section Method
	public void clickNextPage(WebDriver driver) {
		waitUntilVisible(driver, nextPage);
		clickElement(driver, nextPage);
	}

	public void clickPreviousPage(WebDriver driver) {
		waitUntilVisible(driver, previousPage);
		clickElement(driver, previousPage);
	}

	// Verify Disposition Search results for given two Disposition values
	public void verifyMultipleDispositionSearch(WebDriver driver, String disposition1, String disposition2, int page) {
		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);
			for (int i = 0; i < resultList.size(); i++) {
				// assertEquals(dispositionList.get(i).getText().toString().trim(),disposition1.trim());
				assert resultList.get(i).findElement(dispositionResult).getText().toString().trim()
						.equals(disposition1.trim())
						|| resultList.get(i).findElement(dispositionResult).getText().toString().trim()
								.equals(disposition2.trim());
			}
			navigateToNextCAIPage(driver, page, j);
		}
	}

	// Verify Lead Status Search results for given two Lead Status values
	public void verifyMultipleLeadStatusSearch(WebDriver driver, String leadstatus1, String leadstatus2, int page) {
		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);
			for (int i = 0; i < resultList.size(); i++) {
				// assertEquals(dispositionList.get(i).getText().toString().trim(),disposition1.trim());
				assert resultList.get(i).findElement(leadStatusResult).getText().toString().trim()
						.equals(leadstatus1.trim())
						|| resultList.get(i).findElement(leadStatusResult).getText().toString().trim()
								.equals(leadstatus2.trim());
			}
			navigateToNextCAIPage(driver, page, j);
		}
	}

	// Verify Lead Status Search results for given two Lead Status values
	public void verifyMultipleOppStageSearch(WebDriver driver, String oppstage1, String oppstage2, int page) {
		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);

			if (resultList.size() == 0) {
				System.out.println("No matching Opportunity Stage Found");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i++) {
					// assertEquals(dispositionList.get(i).getText().toString().trim(),disposition1.trim());
					assert resultList.get(i).findElement(oppStageResult).getText().toString().trim()
							.equals(oppstage1.trim())
							|| resultList.get(i).findElement(oppStageResult).getText().toString().trim()
									.equals(oppstage2.trim());
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}
	}

	// Verify Agent Search results in order
	public void verifySearchOrder(WebDriver driver, int page, String order,String sortType) throws Exception {
		
		switch (sortType) {
		case "callTime":
			for (int j = 0; j < page && j < 2; j++) {
				waitUntilVisible(driver, callTime, 2);
				List<WebElement> callTimeList1 = getElements(driver, callTime);
				// take call time in a list
				SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				ArrayList<Date> callTimeList = new ArrayList<>();
				for (int i = 0; i < callTimeList1.size(); i++) {
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(i).getText()));
				}
				System.out.println(order);
				if (order == "Date - Oldest to Newest") {
					boolean sorted = Ordering.natural().isOrdered(callTimeList);
					assertTrue(sorted, "Sorting on Ascending order not works fine");
				} else {
					boolean sorted = Ordering.natural().reverse().isOrdered(callTimeList);
					assertTrue(sorted, "Sorting on Descending order not works fine");
				}
				navigateToNextCAIPage(driver, page, j);
			}
			break;
		
		case "callDuration":
			for (int j = 0; j < page && j < 2; j++) {
				waitUntilVisible(driver, callDuration, 2);
				List<WebElement> callTimeList1 = getElements(driver, callDuration);
				// take call time in a list
				SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("hh:mm:ss");
				ArrayList<Date> callTimeList = new ArrayList<>();
				for (int i = 0; i < callTimeList1.size(); i++) {
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(i).getText()));
				}
				System.out.println(order);
				if (order == "Call Duration - Shortest to Longest") {
					boolean sorted = Ordering.natural().isOrdered(callTimeList);
					assertTrue(sorted, "Sorting on Ascending order not works fine");
				} else if (order == "Call Duration - Longest to Shortest"){
					boolean sorted = Ordering.natural().reverse().isOrdered(callTimeList);
					assertTrue(sorted, "Sorting on Descending order not works fine");
				}
				navigateToNextCAIPage(driver, page, j);
			}
			break;
			
		case "agentName":
			for (int j = 0; j < page && j < 2; j++) {
				waitUntilVisible(driver, agentResult, 2);
				List<WebElement> agentList1 = getElements(driver, agentResult);
				// take call time in a list
				
				ArrayList<String> agentList = new ArrayList<>();
				for (int i = 0; i < agentList1.size(); i++) {
					agentList.add(agentList1.get(i).getText());
				}
				System.out.println(order);
				if (order == "Agent Name - A to Z") {
					boolean sorted = Ordering.natural().isOrdered(agentList);
					assertTrue(sorted, "Sorting on Ascending order not works fine");
				} else if (order == "Agent Name - Z to A"){
					boolean sorted = Ordering.natural().reverse().isOrdered(agentList);
					assertTrue(sorted, "Sorting on Descending order not works fine");
				}
				navigateToNextCAIPage(driver, page, j);
			}
			break;
		}
		
	}
	// Verify Time frameSearch
	public void verifyTimeFrameSearch(WebDriver driver, int page, String time, boolean status) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String startDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
		Date date2 = dateTimeFormatter.parse(startDate);
		Date date1 = null;

		if (time == "1 week") {
			c.add(Calendar.DAY_OF_YEAR, -6);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 00);
			String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date1 = dateTimeFormatter.parse(lastDate);
		} else if (time == "1 month") {
			c.add(Calendar.MONTH, -1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 00);
			String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date1 = dateTimeFormatter.parse(lastDate);
		} else if (time == "3 months") {
			c.add(Calendar.MONTH, -3);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 00);
			String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date1 = dateTimeFormatter.parse(lastDate);
		} else if (time == "Custom") {
			c.add(Calendar.MONTH, -1);
			int min = c.getActualMinimum(Calendar.DAY_OF_MONTH);
			c.set(Calendar.DAY_OF_MONTH, min);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 00);
			String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date1 = dateTimeFormatter.parse(lastDate);
			c.add(Calendar.MONTH, 1);
			min = c.getActualMinimum(Calendar.DAY_OF_MONTH);
			c.set(Calendar.DAY_OF_MONTH, min);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date2 = dateTimeFormatter.parse(lastDate);
		}
		if (status) {
			for (int j = 0; j < page; j++) {
				List<WebElement> callTimeList1 = getElements(driver, callTime);
				ArrayList<Date> callTimeList = new ArrayList<>();
				for (int i = 0; i < callTimeList1.size(); i++) {
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(i).getText()));
					assertTrue(callTimeList.get(i).after(date1) && callTimeList.get(i).before(date2));
				}
				navigateToNextCAIPage(driver, page, j);
			}
		} else {
			for (int j = 0; j < page; j++) {
				List<WebElement> callTimeList1 = getElements(driver, callTime);
				ArrayList<Date> callTimeList = new ArrayList<>();
				for (int i = 0; i < callTimeList1.size(); i++) {
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(i).getText()));
					assertFalse(callTimeList.get(i).after(date1) && callTimeList.get(i).before(date2));
				}
				navigateToNextCAIPage(driver, page, j);
			}
		}
	}
	
	// Verify Time frameSearch
		public void verifyTimeFrameSearch1(WebDriver driver, int page, String time, boolean status) throws Exception {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			String startDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
			Date date2 = dateTimeFormatter.parse(startDate);
			Date date1 = null;

			if (time == "1 week") {
				c.add(Calendar.DAY_OF_YEAR, -6);
				String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
				date1 = dateTimeFormatter.parse(lastDate);
			} else if (time == "1 month") {
				c.add(Calendar.MONTH, -1);
				String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
				date1 = dateTimeFormatter.parse(lastDate);
			} else if (time == "3 months") {
				c.add(Calendar.MONTH, -3);
				//c.set(Calendar.HOUR_OF_DAY, 0);
				//c.set(Calendar.MINUTE, 00);
				String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
				date1 = dateTimeFormatter.parse(lastDate);
			} 
			else if (time=="Today") {
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 00);
			c.set(Calendar.SECOND, 00);
			startDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date1 = dateTimeFormatter.parse(startDate);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
			date2 = dateTimeFormatter.parse(lastDate);
	
			}
			
			else if (time == "Custom") {
				c.add(Calendar.MONTH, -1);
				int min = c.getActualMinimum(Calendar.DAY_OF_MONTH);
				c.set(Calendar.DAY_OF_MONTH, min);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 00);
				String lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
				date1 = dateTimeFormatter.parse(lastDate);
				c.add(Calendar.MONTH, 1);
				min = c.getActualMinimum(Calendar.DAY_OF_MONTH);
				c.set(Calendar.DAY_OF_MONTH, min);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				lastDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(c.getTime());
				date2 = dateTimeFormatter.parse(lastDate);
			}
			if (status) {
					List<WebElement> callTimeList1 = getElements(driver, callTime);
					ArrayList<Date> callTimeList = new ArrayList<>();
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(0).getText()));
					assertTrue(callTimeList.get(0).after(date1) && callTimeList.get(0).before(date2));
					//Apply sort
					selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Ascending.toString());
					idleWait(2);
					assertTrue(callTimeList.get(0).after(date1) && callTimeList.get(0).before(date2));
					//Reset sort
					selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Descending.toString());
					idleWait(2);
					}
			else {
					List<WebElement> callTimeList1 = getElements(driver, callTime);
					ArrayList<Date> callTimeList = new ArrayList<>();
					callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(0).getText()));
					assertFalse(callTimeList.get(0).after(date1) && callTimeList.get(0).before(date2));
					//Apply sort
					selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Ascending.toString());
					idleWait(2);
					assertFalse(callTimeList.get(0).after(date1) && callTimeList.get(0).before(date2));
					//Reset sort
					selectSearchOrder(driver, CallsTabPage.SearchOrderOptions.Descending.toString());
					idleWait(2);
					}
				
				
			
		}

	// Navigate to next cai page
	public void navigateToNextCAIPage(WebDriver driver, int page, int index) {
		if (index < page - 1) {
			clickElement(driver, nextPage);
			waitUntilVisible(driver, previousPage);
		}
	}

	// Navigate to previous cai page
	public void navigateToPreviousCAIPage(WebDriver driver, int page, int index) {
		if (index < page - 1) {
			clickElement(driver, previousPage);
			waitUntilVisible(driver, nextPage);
		}
	}

	// pagination cai
	public void paginationCAI(WebDriver driver) {

		if (isElementVisible(driver, nextPageDisabled, 2)) {
			System.out.println("Only one page results or no results. Pagination not possible");
			return;
		} else {
			int currentPage = Integer.valueOf(getCurrentPage(driver));
			clickElement(driver, nextPage);
			waitUntilVisible(driver, previousPage);
			assertEquals(Integer.parseInt(getCurrentPage(driver)), currentPage + 1);
			clickElement(driver, previousPage);
			waitUntilVisible(driver, nextPage);
			assertEquals(Integer.parseInt(getCurrentPage(driver)), currentPage);
		}
	}

	// Method to check that given annotation exists in annotation List

	public boolean annotationExistInList(WebDriver driver, String notes) {

		By annotationLocator = By.xpath(annotationMessagePage.replace("$$annotation$$", notes));
		return isElementVisible(driver, annotationLocator, 5);

	}
	
	
	// Method to check that given annotation exists opposite to given user

	public boolean annotationExistForAgent(WebDriver driver, String userName) {

		By annotationLocator = By.xpath(annotationUserSegmentActive.replace("$$username$$", userName));
		return isElementVisible(driver, annotationLocator, 1);

	}
	
	// Method to check that given annotation exists opposite to given user

	public boolean verifySavedAnnotation(WebDriver driver, String userName,String annotation) {

		By annotationLocator = By.xpath(annotationUserSegment.replace("$$username$$", userName));
		clickElement(driver, annotationLocator);
		idleWait(1);
		getAttribue(driver, annotationPopUpBox, ElementAttributes.value);
		return getAttribue(driver, annotationPopUpBox, ElementAttributes.value).trim().contains(annotation.trim()) && getElementsText(driver, existingAnnotationTag).trim().equals(annotation.trim());
		}


	public String annotationMsgDate(WebDriver driver, String notes) {

		By annotationLocator = By.xpath(annotationDatePage.replace("$$annotation$$", notes));
		System.out.println(getElementsText(driver, annotationLocator));
		return getElementsText(driver, annotationLocator);

	}

	public String annotationMsgUser(WebDriver driver, String notes) {

		By annotationLocator = By.xpath(annotationUserPage.replace("$$annotation$$", notes));
		System.out.println(getElementsText(driver, annotationLocator));
		return getElementsText(driver, annotationLocator);

	}

	public void annotationClickandVerifyDetails(WebDriver driver, String notes) {

		By highlighted = By.xpath(annotationHighlighted.replace("$$annotation$$", notes));
		if (isElementVisible(driver, annotationPopUpBox, 2)) {
			assertTrue(isElementVisible(driver, pauseIcon, 5));
			System.out.println(getElementsText(driver, annotateAgentName).trim());
			System.out.println(getElementsText(driver, userName).trim());
			System.out.println(findElement(driver, highlighted).getAttribute("class"));
			System.out.println(findElement(driver, annotationMessage).getAttribute("value").trim());
			System.out.println(notes);
			// System.out.println("Existing annotation
			// Tag-"+getElementsText(driver, existingAnnotationTag));

			assertTrue(getElementsText(driver, annotateAgentName).trim()
					.contains(getElementsText(driver, userName).trim()));
			assertTrue(findElement(driver, highlighted).getAttribute("class").contains("highlighted"));
			assertTrue(findElement(driver, annotationMessage).getAttribute("value").trim().equals(notes.trim()));
			// assertTrue(getElementsText(driver,
			// existingAnnotationTag).trim().equals(notes.trim()));
		} else {
			System.out.println(getElementsText(driver, annotationMessageReadOnly).trim());
			System.out.println("Existing annotation Tag-" + getElementsText(driver, existingAnnotationTagReadOnly));
			assertTrue(isElementVisible(driver, pauseIcon, 3));
			assertFalse(getElementsText(driver, annotationMessageReadOnly).contains(getElementsText(driver, userName)));
			assertFalse(isElementVisible(driver, annotationPopUpBox, 1));
			assertTrue(findElement(driver, highlighted).getAttribute("class").contains("highlighted"));
			assertTrue(getElementsText(driver, annotationMessageReadOnly).trim().equals(notes.trim()));
			assertTrue(getElementsText(driver, existingAnnotationTagReadOnly).trim().equals(notes.trim()));
		}

		// clickElement(driver, annotationLocator);
		openToEditAnnotation(driver, notes);
		assertFalse(isElementVisible(driver, annotationPopUpBox, 5));
		// waitUntilVisible(driver, playIcon, 10);

	}

	public void openToEditAnnotation(WebDriver driver, String notes) {

		By annotationLocator = By.xpath(annotationUserPage.replace("$$annotation$$", notes));
		clickElement(driver, annotationLocator);
	}

	public void clickSegment(WebDriver driver, String username) {

		By segmentLocator = By.xpath(annotationUserSegment.replace("$$username$$", username));
		By segmentLocatorColor = By.xpath(annotationSegmentColor.replace("$$username$$", username));
		clickElement(driver, segmentLocator);
		String color = findElement(driver, segmentLocatorColor).getCssValue("fill");
		String hex = Color.fromString(color).asHex();
		if (findElement(driver, segmentLocator).getAttribute("class").contains("active")) {
			assertEquals(hex.toLowerCase(), "#80b4ff".toLowerCase());
			System.out.println("Annotation open and selected.Color Verified. Its #80b4ff ");
		} else {
			System.out.println("Annotation Closed.Color Verified. Its 0e53c4 ");
			assertEquals(hex.toLowerCase(), "#0e53c4".toLowerCase());

		}
	}

	// Check for existing annotation in a recording

	public String getAnnotationMessage(WebDriver driver) {

		waitUntilVisible(driver, annotateBtn);

		if (isElementVisible(driver, annotationSectionFilter, 5)) {

			//List<WebElement> annotationeList = getElements(driver, annotationMessageList);
			List<WebElement> annotationeList = getElements(driver, By.xpath(annotationNotesForUser.replace("$$username$$", getElementsText(driver, userName))));
			System.out.println(annotationeList.get(0).getText().trim());
			return annotationeList.get(0).getText().trim();
		} else {
			String notes = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
			clickAnnotateBtn(driver);
			idleWait(10);
			clickStopBtn(driver);
			enterAnnotation(driver, notes);
			return notes;
		}

	}

	// Enter annotation Tag

	// Method to enter agent name in to whom we want to share cai call
	public void enterAnnotationTag(WebDriver driver, String tag) {
		clearAll(driver, annotationTag);
		findElement(driver, annotationTag).sendKeys(tag);
		List<WebElement> tagList = getElements(driver, tagDropDown);
		clickElement(driver, tagList.get(0));

		// verify tags and sentiments
		assertEquals(getElementsText(driver, annotationTagSelectedItem).trim(), tag);
		verifySentiments(driver);
	}

	// Method to enter agent name in to whom we want to share cai call
	public String enterCallDetailsTag(WebDriver driver, String tag) {
		clearTag(driver);
		findElement(driver, caiCallDetailsTag).sendKeys(tag);
		List<WebElement> tagList = getElements(driver, tagDropDown);
		clickElement(driver, tagList.get(0));

		// verifying tags and sentiments
		assertTrue(getElementsText(driver, callDetailsTagSelectdItem).trim().contains(tag.trim()));
		verifySentiments(driver);
		return getElementsText(driver, callDetailsTagSelectdItem);
	}
	
	public void verifyTagsForeMoreThan200CharNotAccept(WebDriver driver, String tagMoreThan200Char, String tag200Char) {
		clearTag(driver);
		findElement(driver, caiCallDetailsTag).sendKeys(tagMoreThan200Char);
		List<WebElement> tagList = getElements(driver, tagDropDown);
		clickElement(driver, tagList.get(0));
		assertNotEquals(getElementsText(driver, tagSelectedText), tagMoreThan200Char);
		clearTag(driver);
		findElement(driver, caiCallDetailsTag).sendKeys(tag200Char);
		tagList = getElements(driver, tagDropDown);
		clickElement(driver, tagList.get(0));
		assertEquals(getElementsText(driver, tagSelectedText), tag200Char);
	}

	public void verifySentiments(WebDriver driver) {

		// verify sentiments
		assertTrue(isElementVisible(driver, tagSentimentUpIcon, 5));
		assertTrue(isElementVisible(driver, tagSentimentDownIcon, 5));
	}

	// Method to get Annotation creation date

	public String getAnnotationCreationDate(WebDriver driver) {
		return getElementsText(driver, annotationDate);
	}

	public void verifyFilters(WebDriver driver, String agentName) {
		idleWait(2);
		assertTrue(isListContainsText(driver, getElements(driver, selectedFilterList), agentName));
	}
	
	public boolean isFilterListVisible(WebDriver driver){
		return isElementVisible(driver, selectedFilterList, 5);
	}

	// Remove search filters by clicking X icon one by one

	public void removeFilter(WebDriver driver) {

		List<WebElement> totalFilters = getElements(driver, removeFilterList);
		if (totalFilters.size() == 0) {
			System.out.println("No Filters applied");

		} else {

			for (int i = 0; i < totalFilters.size(); i++) {
				totalFilters = getElements(driver, removeFilterList);
				clickElement(driver, totalFilters.get(0));
				dashboard.isPaceBarInvisible(driver);
			}

		}

	}

	// Added Library exist in List of Libraries
	public boolean checkTagInList(WebDriver driver, String tag) {
		if (!isElementVisible(driver, tagFilter, 1)) {
			return false;
		} else {
			clickElement(driver, tagFilter);
			List<WebElement> libList = getElements(driver, tagFilterValues);
			return isTextPresentInList(driver, libList, tag);
		}
	}

	// Method to clear Tag

	public void clearTag(WebDriver driver) {
		if (isListElementsVisible(driver, callDetailsTagSelectdItem, 5)) {
			List<String> tagList = getTextListFromElements(driver, callDetailsTagSelectdItem);
			for (int i = 0; i <= tagList.size(); i++) {
				enterBackspace(driver, caiCallDetailsTag);
				idleWait(2);
				if (isListElementsVisible(driver, callDetailsTagSelectdItem, 5)){
				tagList = getTextListFromElements(driver, callDetailsTagSelectdItem);
				}
			}
		}
	}

	// Method to enter Tag from existing list

	public String enterTagFromExisting(WebDriver driver, String newTag) {
		clearAll(driver, caiCallDetailsTag);
		findElement(driver, caiCallDetailsTag).sendKeys(newTag);
		List<WebElement> tagList = getElements(driver, tagDropDown);
		clickElement(driver, tagList.get(0));
		List<WebElement> tagList1 = getElements(driver, existingTag);
		for (int i = 0; i < tagList1.size(); i++) {
			assertTrue(tagList1.get(i).getText().trim().toLowerCase().contains(newTag.trim().toLowerCase()));
		}
		System.out.println("all options contains given tag string");
		String token = tagList1.get(0).getText().trim();
		verifySentiments(driver);
		System.out.println("tag selected.thumbs up, thumbs down and remove icon also displayed");
		idleWait(1);
		return token;

	}

	// Method to enter new Tag

	public void enterTagByAdd(WebDriver driver, String newTag) {
		clearAll(driver, caiCallDetailsTag);
		findElement(driver, caiCallDetailsTag).sendKeys(newTag);
		waitUntilVisible(driver, newTagAdd);
		List<WebElement> tagList = getElements(driver, newTagAdd);
		clickElement(driver, tagList.get(0));
		System.out.println("Added new tag");
		waitUntilVisible(driver, tagSentimentUpIcon);
		assertTrue(isElementVisible(driver, tagSentimentUpIcon, 1));
		assertTrue(isElementVisible(driver, tagSentimentDownIcon, 1));
		assertTrue(isElementVisible(driver, tagRemove, 1));
		System.out.println("tag selected.thumbs up, thumbs down and remove icon also displayed");
		idleWait(1);

	}

	// Click Thumbs Up
	public void clickThumbsUpTag(WebDriver driver) {
		clickElement(driver, tagSentimentUpIcon);
		waitUntilInvisible(driver, tagSentimentDownIcon);
		assertFalse(isElementVisible(driver, tagSentimentDownIcon, 5));
		assertTrue(isElementVisible(driver, tagRemove, 5));
		assertTrue(isElementVisible(driver, tagSentimentUpIcon, 5));
		System.out.println("tag selected.thumbs up clicked, thumbs down and remove icon now not displayed");
	}

	// Click Thumbs down
	public void clickThumbsDownTag(WebDriver driver) {
		clickElement(driver, tagSentimentDownIcon);
		waitUntilInvisible(driver, tagSentimentUpIcon);
		assertFalse(isElementVisible(driver, tagSentimentUpIcon, 5));
		assertTrue(isElementVisible(driver, tagRemove, 5));
		assertTrue(isElementVisible(driver, tagSentimentDownIcon, 5));
		System.out.println("tag selected.thumbs down clicked, thumbs up and remove icon now not displayed");
	}

	// Click Remove Tag
	public void clickRemoveTag(WebDriver driver) {
		clickElement(driver, tagRemove);
		waitUntilInvisible(driver, tagRemove);
		assertTrue(isElementVisible(driver, tagSentimentUpIcon, 5));
		assertTrue(isElementVisible(driver, tagSentimentDownIcon, 5));
		assertFalse(isElementVisible(driver, tagRemove, 5));
		System.out.println("tag selected.remove tag clicked, thumbs up and thumbs down icon now displayed");
	}

	// Click Play icon
	public void clickPlayIcon(WebDriver driver) {
		waitUntilVisible(driver, playIcon);
		scrollToElement(driver, playIcon);
		waitUntilClickable(driver, playIcon);
		scrollToElement(driver, playIcon);
		clickByJs(driver, playIcon);
		idleWait(2);
		assertTrue(isElementInvisible(driver, playIcon, 2));
		assertTrue(isElementVisible(driver, pauseIcon, 2));

	}

	// Click Pause icon
	public void clickPauseIcon(WebDriver driver) {
		scrollToElement(driver, pauseIcon);
		clickByJs(driver, pauseIcon);
		idleWait(1);
		assertTrue(isElementInvisible(driver, pauseIcon, 2));
		assertTrue(isElementVisible(driver, playIcon, 2));
	}

	// Click Back to 15 seconds icon icon
	public void clickBackIcon(WebDriver driver) {
		scrollToElement(driver, backIcon);
		clickByJs(driver, backIcon);
	}

	// Click progress bar status
	public float getProgressBarStatus(WebDriver driver) {
/*		String arr[] = findElement(driver, playerProgress).getAttribute("style").split(":", 2);
		return Float.parseFloat(arr[1].replace("%;", ""));*/
		return Float.parseFloat(driver.findElement(By.xpath(".//*[@vector-effect='non-scaling-stroke']")).getAttribute("x1").replace("%", ""));
	}

	// Get Timer progress

	public int getPlayerTimerProgress(WebDriver driver) {

		String[] timerProgress = getElementsText(driver, playerTimer).split(":");
		int playerTimer = Integer.parseInt(timerProgress[2]);
		return playerTimer;
	}

	// Click Keyword
	public void clickKeyword(WebDriver driver, int page) {

		for (int j = 1; j <= page && j <= 3; j++) {
			List<WebElement> resultList = getElements(driver, viewCAI);
			if (resultList.size() == 0) {
				System.out.println("No matching Result Found.");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i += 2) {
					resultList = getElements(driver, viewCAI);
					clickElement(driver, resultList.get(i));
					dashboard.isPaceBarInvisible(driver);
					waitUntilVisible(driver, knowledgeSection);
					clickElement(driver, knowledgeSection);
					if (isElementVisible(driver, keywordList, 2)) {
						List<WebElement> keyWordList = getElements(driver, keywordList);
						for (int k = 0; k < keyWordList.size(); k++) {
							clickElement(driver, keyWordList.get(k));
							assertTrue(isElementInvisible(driver, playIcon, 1));
							assertTrue(isElementVisible(driver, pauseIcon, 1));
							
							
							/*	String keywordProgress[] = keyWordList.get(k).getAttribute("t").split(",");
							float progress = Float.parseFloat(keywordProgress[0]);
							System.out.println(progress);
							clickElement(driver, keyWordList.get(k));
							dashboard.isPaceBarInvisible(driver);
							assertEquals(Float.parseFloat(findElement(driver, keywordMarker).getAttribute("stime")),
									progress);
							assertTrue(isElementInvisible(driver, playIcon, 1));
							assertTrue(isElementVisible(driver, pauseIcon, 1));
							System.out.println(getElementsText(driver, keywordText).trim());
							idleWait(2);
							waitUntilVisible(driver, keywordMarker);
							hoverElement(driver, keywordMarker);
							System.out.println(getElementsText(driver, keywordComment));
							assertTrue(getElementsText(driver, keywordComment).toLowerCase().trim()
									.equals(keyWordList.get(k).getText().toLowerCase().trim()));
							System.out.println(getProgressBarStatus(driver));
							clickPauseIcon(driver);*/

						}
						return;
					} else {
						clickCallTab(driver);
					}

				}
				navigateToNextCAIPage(driver, page, j);
			}
		}
/*
		if (!isElementVisible(driver, keywordList, 2)) {
			System.out.println("No matching Result Found");
			return;

		} else {
			List<WebElement> resultList = getElements(driver, keywordList);
			for (int i = 0; i < resultList.size(); i++) {

				float progress = Float.parseFloat(resultList.get(i).getAttribute("t"));
				System.out.println(progress);
				clickElement(driver, resultList.get(i));
				dashboard.isPaceBarInvisible(driver);
				assertEquals(Float.parseFloat(findElement(driver, keywordMarker).getAttribute("stime")), progress);
				assertTrue(isElementInvisible(driver, playIcon, 1));
				assertTrue(isElementVisible(driver, pauseIcon, 1));
				System.out.println(getElementsText(driver, keywordText).trim());
				assertTrue(getElementsText(driver, keywordText).trim().equals(resultList.get(i).getText().trim()));
				assertTrue(getElementsText(driver, keywordComment).trim().equals(resultList.get(i).getText().trim()));
				idleWait(2);
				System.out.println(getProgressBarStatus(driver));
				assertTrue(getProgressBarStatus(driver) >= progress);
				clickPauseIcon(driver);

			}
		}*/
	}

	// Set custom timeframe

	// Click Start Date
	public void clickStartDate(WebDriver driver) {
		clickElement(driver, startDate);
		assertTrue(isElementVisible(driver, calendarPopUp, 2));
	}

	// Click End Date
	public void clickEndDate(WebDriver driver) {
		clickElement(driver, endDate);
		assertTrue(isElementVisible(driver, calendarPopUp, 2));
	}

	// Set current date as end date
	public void setEndDate(WebDriver driver) {
		List<WebElement> resultList = getElements(driver, currentMonthDateList);
		clickElement(driver, resultList.get(0));

	}

	// Set current date as custom date
	public void setStartDate(WebDriver driver) {

		// Go to previous month
		clickElement(driver, previousMonthCalendar);
		// Get list of dates of previous months
		List<WebElement> resultList = getElements(driver, preMthDateInPreMonth);
		clickElement(driver, resultList.get(0));
	}

	public void searchConvAI(WebDriver driver, String textToSearch) {
		dashboard.isPaceBarInvisible(driver);
		enterText(driver, searchConvAITextBox, textToSearch);
		waitUntilVisible(driver, searchConvAIBtn);
		clickElement(driver, searchConvAIBtn);
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
	}

	public boolean isDownloadFileActionVisible(WebDriver driver) {
		waitUntilVisible(driver, callActionBtn);
		clickElement(driver, callActionBtn);
		return isListElementsVisible(driver, downloadFileAction, 5);
	}

	public void clickDownLoadFileActionLink(WebDriver driver) {
		waitUntilVisible(driver, downloadFileAction);
		clickElement(driver, downloadFileAction);
	}

	public void setRatingFilter(WebDriver driver, String rating) {
		By callRatingLocator = By.xpath(callRatingSelect.replace("$$rating$$", rating));
		waitUntilVisible(driver, callRatingLocator);
		clickElement(driver, callRatingLocator);
		dashboard.isPaceBarInvisible(driver);
	}

	// Click Boolean Filter
	public void clickBooleanFilter(WebDriver driver) {

		// Check for state before click
		if (findElement(driver, booleanIconList).getAttribute("class").toString().contains("glyphicon-plus")) {
			clickElement(driver, booleanIconList);
			dashboard.isPaceBarInvisible(driver);
			assertTrue(
					findElement(driver, booleanIconList).getAttribute("class").toString().contains("glyphicon-minus"));
			return;
		} else if (findElement(driver, booleanIconList).getAttribute("class").toString().contains("glyphicon-mnius"))
			;
		{
			clickElement(driver, booleanIconList);
			dashboard.isPaceBarInvisible(driver);
			assertTrue(
					findElement(driver, booleanIconList).getAttribute("class").toString().contains("glyphicon-plus"));
			return;
		}
	}

	public boolean getBooleanFilterStatus(WebDriver driver) {

		return findElement(driver, booleanIconList).getAttribute("class").toString().contains("glyphicon-plus");
	}

	public void clickSelectedFilterPlus(WebDriver driver, String filterName) {
		dashboard.isPaceBarInvisible(driver);
		By selectedFilterPlusLoc = By.xpath(selectedFilterPlus.replace("$filterName$", filterName));
		waitUntilVisible(driver, selectedFilterPlusLoc);
		scrollToElement(driver, selectedFilterPlusLoc);
		clickElement(driver, selectedFilterPlusLoc);
		dashboard.isPaceBarInvisible(driver);
	}

	public void clickSelectedFilterMinus(WebDriver driver, String filterName) {
		dashboard.isPaceBarInvisible(driver);
		By selectedFilterMinusLoc = By.xpath(selectedFilterMinus.replace("$filterName$", filterName));
		waitUntilVisible(driver, selectedFilterMinusLoc);
		scrollToElement(driver, selectedFilterMinusLoc);
		clickElement(driver, selectedFilterMinusLoc);
		dashboard.isPaceBarInvisible(driver);
	}

	// keyword methods

	public boolean keywordsExistInList(WebDriver driver, String notes) {
		isListElementsVisible(driver, keywordsList, 5);
		List<WebElement> keywordList = getElements(driver, keywordsList);
		return isTextPresentInList(driver, keywordList, notes);
	}

	public boolean keyPhraseExistInList(WebDriver driver, String keywordGroupName, String keyPhrase) {
		By locator = By.xpath(keywordGroupPhrase.replace("$$keywordGroup$$", keywordGroupName));
		clickElement(driver, locator);
		By phraseListLocator = By.xpath(keywordGroupPhraseList.replace("$$keywordGroup$$", keywordGroupName));
		isListElementsVisible(driver, phraseListLocator, 5);
		List<WebElement> keyPhraseList = getElements(driver, phraseListLocator);
		return isTextPresentInList(driver, keyPhraseList, keyPhrase);
	}

	public void setKeyPhraseFilter(WebDriver driver, String keywordGroupName, String keyPhrase) {

		By phraseListLocator = By.xpath(keywordGroupPhraseList.replace("$$keywordGroup$$", keywordGroupName));
		By enterPhraseLocator = By.xpath(keywordGroupPhraseInput.replace("$$keywordGroup$$", keywordGroupName));
		clickElement(driver, enterPhraseLocator);
		assertTrue(isElementVisible(driver, phraseListLocator, 1));
		enterTextandSelect(driver, enterPhraseLocator, keyPhrase);
		dashboard.isPaceBarInvisible(driver);
		if (selectedFilterList != null) {
			List<WebElement> filterList = getElements(driver, selectedFilterList);
			assertTrue(isTextPresentInList(driver, filterList, keyPhrase));
		}
	}

	public void verifyKeyPhraseFilter(WebDriver driver, int page) {

		for (int j = 0; j < page; j++) {
			List<WebElement> resultList = getElements(driver, caiResultList);
			List<WebElement> keyMatchResultList = getElements(driver, caiWithMatchingTranscription);
			assertEquals(resultList.size(), keyMatchResultList.size());
			navigateToNextCAIPage(driver, page, j);
		}

	}

	public List<String> getFiltersList(WebDriver driver) {
		return getTextListFromElements(driver, selectedFilterList);
	}

	public void verifyKeyPhraseFilter1(WebDriver driver, int page, List<String> phraseList) {

		for (int j = 1; j <= page && j <= 1; j++) {
			List<WebElement> resultList = getElements(driver, viewCAI);
			if (resultList.size() == 0) {
				System.out.println("No matching Result Found");
				break;
			} else {
				for (int i = 0; i < resultList.size(); i++) {
					resultList = getElements(driver, viewCAI);
					String[] phrase = getElements(driver, transcriptionCount).get(i).getText().split(" ");
					int totalPhraseMatches = Integer.parseInt(phrase[0]);
					System.out.println("Total Phrase Matches " + totalPhraseMatches);
					clickElement(driver, resultList.get(i));
					if (!isElementVisible(driver, noteSaveMessage, 1)) {
						dashboard.isPaceBarInvisible(driver);
						int phraseTranscriptCount = 0;
						for (int l = 0; l < phraseList.size(); l++) {
							if (isElementVisible(driver,By.xpath(phraseInTranscription.replace("$$phrase$$", phraseList.get(l).trim())),2)) {
								By loc = By
										.xpath(phraseInTranscription.replace("$$phrase$$", phraseList.get(l).trim()));
								List<String> phraseCount = getTextListFromElements(driver, loc);
								System.out.println("Total Match for keyword-> " + phraseList.get(l).trim() + "   "
										+ phraseCount.size());
								phraseTranscriptCount = phraseTranscriptCount + phraseCount.size();

							}
						}
						System.out.println("Total Match for all Phrases in transcript-> " + phraseTranscriptCount);
						assertTrue(phraseTranscriptCount == totalPhraseMatches || phraseTranscriptCount >= 5);
						clickCallTab(driver);
					} else
						waitUntilInvisible(driver, noteSaveMessage);
				}
			}
			navigateToNextCAIPage(driver, page, j);
		}
	}

	// Method to set call duration filter
	public void clickToCancelAnnotation(WebDriver driver) {
	/*	WebElement minSlider = findElement(driver, playerSlider);
		Actions move = new Actions(driver);
		Action action1 = move.dragAndDropBy(minSlider, -50, 0).build();
		action1.perform();
		dashboard.isPaceBarInvisible(driver);*/
		clickBackIcon(driver);
		assertFalse(isElementVisible(driver, annotateStopBtn, 2));
		assertTrue(isElementVisible(driver, annotateBtn, 2));
	}

	// Method to set call duration filter
	public void clickToFastForwardAnnotation(WebDriver driver) {
		WebElement minSlider = findElement(driver, playerSlider);
		Actions move = new Actions(driver);
		Action action1 = move.dragAndDropBy(minSlider, 20, 0).build();
		action1.perform();
		dashboard.isPaceBarInvisible(driver);
	}

	public void verifyAnnotationCancel(WebDriver driver) {
		assertEquals(getElementsText(driver, noteSaveMessage), "Annotation creation cancelled.");
	}

	// Method to get looged in user name

	public String getLoggedInAgentName(WebDriver driver) {
		return getElementsText(driver, userName).trim();
	}

	// Method for team filter

	public boolean isTeamFilterDisplayed(WebDriver driver) {

		return isElementVisible(driver, teamFilter, 2);
	}

	public List<String> getTeamFilterValues(WebDriver driver) {
		clickElement(driver, teamFilterInput);
		return getTextListFromElements(driver, teamFilterValues);
	}

	// Set Team Filter with given team value

	// Manually set agent filter for given agent
	public void setTeamFilter(WebDriver driver, String teamName) {
		waitUntilVisible(driver, teamFilterInput);
		clickElement(driver, teamFilterInput);
		enterText(driver, teamFilterInput, teamName);
		clickEnter(driver, teamFilterInput);
		dashboard.isPaceBarInvisible(driver);
		idleWait(3);
	}

	// CAI Results found or NOT

	public boolean isCaiResultNOTAvailable(WebDriver driver) {
		return isElementVisible(driver, searchOrderDisabled, 5);
	}
	
	public boolean isCaiNotAccessible(WebDriver driver){
		return isElementVisible(driver, caiNoAccessMessage,5);
	}
}
