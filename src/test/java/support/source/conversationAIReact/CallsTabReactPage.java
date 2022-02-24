/**
 * @author Abhishek Gupta
 *
 */
package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.internal.collections.Pair;

import com.google.common.collect.Range;

import base.SeleniumBase;
import report.source.ReportMetabaseCommonPage;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.SalesforceAccountPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallsTabReactPage extends SeleniumBase{
	Dashboard dashboard = new Dashboard();
	ConversationDetailPage converstaionDetailPage 		= new ConversationDetailPage();
	ContactDetailPage	contactDetailPage				= new ContactDetailPage();
	SalesforceAccountPage sfAccountPage					= new SalesforceAccountPage();
	public static String prospectName 					= null;
	public static String prospectCompany 				= null;
	public static String prospectTitle					= null;
	public static Calendar customStartDateCalendar		= null;
	public static List<String> tagSentimentImageList	= new ArrayList<>();
	public static List<String> agentNameList			= new ArrayList<>();
	public int elasticSearchPageCount					= -1;
	public String elasticSearchStringValue				=  null;
	
	String hyphenCharacterUnicode						= "\u2013";
	
	//*******outer html of the elements starts here*******//
	String outboundCallHTML				= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\"><path d=\"M9 6L18 6V15\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M18 6L6 18\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path></svg>";
	String inboundCallHTML				= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\"><path d=\"M15 18L6 18L6 9\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M6 18L18 6\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path></svg>";
	String callNotesHTML				= "<svg width=\"15\" height=\"17\" viewBox=\"0 0 15 17\" fill=\"none\"><path d=\"M11 9v6.7a.3.3 0 01-.3.3H1.3a.3.3 0 01-.3-.3V2.3a.3.3 0 01.3-.3H6\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M11.485.707l2.122 2.121-5.22 5.22-3.182 1.06 1.06-3.181 5.22-5.22z\" stroke=\"#FFFFFF\" stroke-linejoin=\"round\"></path><path fill=\"#FFFFFF\" d=\"M10.071 1.414L12.9 4.242l-.708.708L9.364 2.12z\"></path></svg>";
	String supervisorHTML				= "<svg width=\"15\" height=\"17.142857142857142\" viewBox=\"0 0 14 16\" fill=\"none\"><path d=\"M3 11v3.7a.3.3 0 00.3.3h9.4a.3.3 0 00.3-.3V4.152a.3.3 0 00-.087-.21l-2.825-2.853A.3.3 0 009.875 1H3.3a.3.3 0 00-.3.3v1.34\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M2.735 4.503a.3.3 0 01.53 0l.583 1.104a.3.3 0 00.214.156l1.23.213a.3.3 0 01.165.505l-.87.895a.3.3 0 00-.082.252l.177 1.236a.3.3 0 01-.429.312l-1.12-.55a.3.3 0 00-.265 0l-1.121.55a.3.3 0 01-.43-.312l.178-1.236a.3.3 0 00-.081-.252l-.87-.895a.3.3 0 01.163-.505l1.23-.213a.3.3 0 00.215-.156l.583-1.104z\" fill=\"#FFFFFF\"></path></svg>";
	String annotationHTML				= "<svg width=\"18\" height=\"15\" viewBox=\"0 0 18 15\" fill=\"none\"><path clip-rule=\"evenodd\" d=\"M6.176 1h5.647a5.176 5.176 0 110 10.353l-2.352 2.353v-2.353H6.176A5.176 5.176 0 116.176 1z\" stroke=\"#FFFFFF\" stroke-linejoin=\"round\"></path><path d=\"M9 4v4M11 6H7\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String toggleImage					= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" style=\"transform: rotate(90deg);\"><path d=\"M15 4L7.21213 11.7879C7.09497 11.905 7.09497 12.095 7.21213 12.2121L15 20\" stroke=\"#000000\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	//*******outer html of the elements ends here*******//
	
	public static By callsTabLink						= By.cssSelector("[href='/#smart-recordings/calls'], [href='/conversations']");
	
	By clearAllLink 					= By.cssSelector(".clear-button");
	By progressCircle					= By.cssSelector("[role='progressbar']");
	
	//*******Search box locators starts here*******//
	By sortDropDown						= By.cssSelector("[data-testid='sort-input']");
	By searchTextBox					= By.cssSelector("input[placeholder='Search']");
	By saveSearchButton					= By.xpath(".//button[text()='Save Search']");
	By savedSearchHeading				= By.xpath(".//h4[text()='Saved Search']");
	//*******Search box locators ends here*******//
	
	//*******Filter Section Header Locators starts here*******//
	By callDetailsRow					= By.xpath(".//h6[text() = 'Call Details']/../..");
	By engagementRow					= By.xpath(".//h6[text() = 'Engagement']/../..");
	By ParticipantsRow					= By.xpath(".//h6[text() = 'Participants']/../..");
	By agentCallMatricesRow				= By.xpath(".//h6[text() = 'Agent Call Metrics']/../..");
	By overallCallMatricesRow			= By.xpath(".//h6[text() = 'Overall Call Metrics']/../..");
	
	By toggleButton						= By.xpath("//*[@class='rdna-accordion-toggle-caret']//*[local-name()='svg']");
	//*******Filter Section Header Locators ends here*******//
	
	//*******side nav filters locators starts here*******//
	//Time Filters
	By timeFrameDropDown				= By.cssSelector("[data-testid='timeframe-input'] [id='ringdna-select']");
	By timeFrameDropDownLabel			= By.cssSelector("label[for='timeframe-select']");
	
	//Participant filters
	By labelAgentDropDownTab			= By.cssSelector("label[for='agents-select']");
	By agentDropdown					= By.cssSelector("[id='calls.autocomplete-filter-agents']");
	protected By dropdownOption			= By.cssSelector("[role='listbox'] li");
	By teamDropdown						= By.cssSelector("[id='calls.autocomplete-filter-teams']");
	
	//prospect filters
	By prospectNameLabel				= By.xpath(".//span[@color='neutral' and text() = 'Prospect Name']");
	By prospectNameDropDown				= By.cssSelector("#name");
	By prospectCompanyLabel				= By.xpath(".//span[@color='neutral' and text() = 'Prospect Company']");
	By prospectCompanyDropDown			= By.cssSelector("input[placeholder='Company']");
	By ProspectTitleLabel				= By.xpath(".//span[@color='neutral' and text() = 'Prospect Title']");
	By prospectTitleDropDown			= By.cssSelector("input[placeholder='Title']");
	
	//react drop down filters
	By dispositionFilterInput 			= By.cssSelector("input[placeholder='Call Dispositions']");
	By leadStatusFilterInput 			= By.cssSelector("input[placeholder='Lead Status']");
	By opportunityStageFilterInput 		= By.cssSelector("input[placeholder='Opportunity Stages']");
	By tagsFilterInput 					= By.cssSelector("input[placeholder='Tags']");
	By LibraryFilerInput				= By.cssSelector("input[placeholder='Libraries']");
	
	By inboundCallCheckbox				= By.cssSelector("input[value='Inbound']");
	By outboundCallCheckbox				= By.cssSelector("input[value='Outbound']");
	By hasNotesCheckbox					= By.cssSelector("input[value='Has Notes']");
	By hasSupervisorNotesCheckbox		= By.cssSelector("input[value='Has Supervisor Notes']");
	By hasAnnotationCheckbox			= By.cssSelector("input[value='Has Annotations']");
	By coachingFlaggedCheckbox			= By.cssSelector("input[value='Flagged for Coaching']");
	By sharedWithOthersCheckbox			= By.cssSelector("input[value='Shared with Others']");
	
	//show all filters
	By tagsShowAll						= By.xpath(".//*[text()='Tags']/following-sibling::div/button");
	By dispositionShowAll				= By.xpath(".//*[text()='Dispositions']/following-sibling::div/button");
	By leadStatusShowAll				= By.xpath(".//*[text()='Lead Status']/following-sibling::div/button");
	By oppStagesShowAll					= By.xpath(".//*[text()='Opportunity Stages']/following-sibling::div/button");
	By librariesShowAll					= By.xpath(".//*[text()='Libraries']/following-sibling::div/button");
	
	String showAllListCheckBox 			= ".MuiDialog-root .MuiFormControlLabel-root:nth-child($$Index$$) input";
	String showAllListItemName 			= ".MuiDialog-root .MuiFormControlLabel-root:nth-child($$Index$$) .MuiFormControlLabel-label span";
	String showAllThumbIcon				= ".MuiDialog-root .MuiFormControlLabel-root:nth-child($$Index$$) .thumb-container svg";
	By showAllListApplyBtn				= By.xpath(".//button[text()='Apply']");
	
	//Slider Filters
	//slider common locators
	By SlideBar							= By.xpath("..//*[@class = 'MuiSlider-track']");
	By FromSliderPoint					= By.xpath("..//*[contains(@class, 'MuiSlider-thumb')][1]");
	By ToSliderPoint					= By.xpath("..//*[contains(@class, 'MuiSlider-thumb')][2]");
	By MinValueLabel					= By.xpath("..//*[contains(@class, 'MuiSlider-markLabel')][1]");
	By MaxValueLabel					= By.xpath("..//*[contains(@class, 'MuiSlider-markLabel')][2]");
	
	//Time Filter
	By callDurationSlideFilter			= By.xpath(".//*[text()='Duration (minutes)']");
	By customStartDateBox				=  By.id("custom-start");
	By customEndDateBox					=  By.id("custom-end");
	
	By monthChangeLeftBtn				= By.xpath(".//*[contains(@class, 'MuiPickersCalendarHeader-iconButton')][1]");
	By monthSelectedDay					= By.cssSelector("button.MuiPickersDay-daySelected p");
	By monthFirstDay					= By.xpath(".//button[contains(@class, 'MuiPickersDay-day') and @tabindex='0']//p[text() = '1']");
	
	By changeToYearPicker				= By.cssSelector("button.MuiPickersToolbarButton-toolbarBtn:nth-child(1)");
	By changeToMonthPicker				= By.cssSelector("button.MuiPickersToolbarButton-toolbarBtn:nth-child(2)");
	
	By yearPickerContainer				= By.cssSelector("div.MuiPickersYearSelection-container");
	By precedingYear					= By.xpath(".//*[contains(@class, 'MuiPickersYear-yearSelected')]/preceding-sibling::div[1]");
	
	By monthHeaderText					= By.cssSelector(".MuiPickersCalendarHeader-switchHeader p");
	By monthDayPickerContainer			= By.cssSelector("div.MuiPickersCalendar-transitionContainer");
	
	//call Details
	By callRatingsSlideFilter			= By.xpath(".//*[text()='Call Rating (stars)']");
	By videoCallFilter                  = By.xpath("//input[@value='Video Call']/ancestor::span[2]");
	
	//Matrics Filter
	By agentTalkTimeSlideFilter			= By.xpath(".//*[text()='Talk Time (%)']");
	By agentAvgTalkStrkSlideFilter		= By.xpath(".//h6[text()='Agent Call Metrics']/../..//*[text()='Average Talk Streak (min)']");
	By agentLongTalkStrkSlideFilter		= By.xpath(".//h6[text()='Agent Call Metrics']/../..//*[text()='Longest Talk Streak (min)']");
	By ovrAvgTalkStrkSlideFilter		= By.xpath(".//h6[text()='Overall Call Metrics']/../..//*[text()='Average Talk Streak (min)']");
	By ovrLongTalkstrkSlideFilter		= By.xpath(".//h6[text()='Overall Call Metrics']/../..//*[text()='Longest Talk Streak (min)']");
	
	//*******side nav filters locators ends here*******//
	
	//*******filters chip locators starts here*******//
	//search box
	By serachResultContainer			= By.cssSelector("[data-testid='list-controller-container']");
	By searchResultRows 				= By.xpath(".//*[@data-testid='list-controller-container']//a[@title='View Conversation']/../span[1]");
	By searchResultsNotificationRow		= By.xpath(".//*[@data-testid='list-controller-container']//a[@title='View Conversation']/../span[1]/../..//p[@color='link']/parent::div");
	By searchRowsCallDate				= By.xpath("following-sibling::span");
	
	By selectedFilterNameList			= By.cssSelector(".chip-container span[color='primary']");
	By selectedFilterChipList			= By.cssSelector(".chip-container");
	By selectedFilterStatusIcon			= By.cssSelector(".MuiChip-avatar");
	By selectedFilterTrueBooleanStatus	= By.cssSelector("div.MuiChip-avatarSmall path:nth-child(2)");

	String plusSignOuterHtml 			= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\"><path d=\"M17 12L7 12\" stroke=\"currentColor\" stroke-linecap=\"round\"></path><path d=\"M12 17.0001L12 7.00012\" stroke=\"currentColor\" stroke-linecap=\"round\"></path></svg>";
	String minusSignOuterHtml 			= "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\"><path d=\"M17 12L7 12\" stroke=\"currentColor\" stroke-linecap=\"round\"></path></svg>";

	String removeFilterIcon				= ".//*[@class = 'chip-container']//span[@color='primary' and text() = '$$FilterText$$']/../following-sibling::*[contains(@class, 'MuiChip-deleteIconSmall')]";
	String tagSentimentIcon				= "(.//*[@class = 'chip-container']//span[@color='primary' and text() = '$$Tag$$']//following-sibling::div/*[local-name()='svg'])[last()]";
	//*******filters chip locators starts here*******//
	
	//*******filtered result locators starts here*******//
	By openedCallEntry					= By.cssSelector(".MuiCollapse-container.MuiCollapse-entered");
	By callDirectionImage				= By.xpath("../preceding-sibling::*//*[local-name()='svg']");
	By openConverstaionDetailIcon		= By.xpath("//*[contains(@aria-label, 'View Conversation')]");
	By prospectDetailsHeading			= By.xpath("../preceding-sibling::*//button[//*[local-name()='svg']]/following-sibling::div");
	By calledAgent						= By.xpath("//p[@color='link'][1]");
	By ProspectNameHeading				= By.xpath("//p[@color='link'][2]");
	By ProspectCompanyHeading			= By.xpath("//p[@color='link'][3]");
	By calledAgentDirectLocator			= By.xpath(".//button[//*[local-name()='svg']]/following-sibling::div//p[@color='link'][1]");
	String searchResultRow 				= "(.//*[@data-testid='list-controller-container']//a[@title='View Conversation']/../span[1])[$$Index$$]";
	
	By agenDetailResult					= By.xpath(".//*[text() = 'Agent Detail']/following-sibling::div/span");
	By agentDetailCallDate				= By.xpath(".//*[text() = 'Agent Detail']/following-sibling::div[2]");
	By agentCallDispostion 				= By.xpath(".//*[text() = 'Agent Detail']/..//div[contains(text(), 'Call Disposition:')]//span");
	By prospectNameResult				= By.xpath("(.//*[text() = 'Prospect Detail']/following-sibling::div/span)[1]");
	By prospectTitleResult				= By.xpath("(.//*[text() = 'Prospect Detail']/following-sibling::div/span)[2]");
	By prospectCompanyResult			= By.xpath("(.//*[text() = 'Prospect Detail']/following-sibling::div/span[@color='link'])[2]");
	By dispositionResult				= By.xpath(".//*[text() = 'Call Disposition: ']/span");
	By leadStatusResult					= By.xpath(".//*[text() = 'Lead Status: ']/span");
	By OpportunityStageResult			= By.xpath(".//*[text() = 'Opportunity Stage: ']/span");
	By tagsResult						= By.xpath(".//*[contains(@class, 'MuiCollapse-container')]//span[text() = 'Tags']/..//span[@color='primary']");
	By tagsResultSentiment				= By.xpath("../following-sibling::*[local-name() = 'svg']");
	By librariesResult					= By.xpath(".//*[contains(@class, 'MuiCollapse-container')]//span[text() = 'Libraries']/..//span[@color='primary']");
	By noLibraryAvailable				= By.xpath("//*[@data-testid='list-controller-container']//span[text()='Libraries']/..//span[text()='No libraries available.']");
	By supervisorImageinResult			= By.xpath(".//*[@title='Supervisor Notes']//div[@shape='square']");
	By callNotesImageinResult			= By.xpath(".//*[@aria-label='Call Notes']//div[@shape='square']");
	By AnnotationImageinResult			= By.xpath(".//*[@aria-label='Annotation']//div[@shape='square']");
	By cloudIcon						= By.xpath("following-sibling::a/*/*");
	//*******filtered result locators ends here*******//
	
	//*******other locators starts here*******//
	By totalPage 						= By.xpath(".//*[contains(text(), 'Page')]");
	By nextPageBtn						= By.cssSelector(".pagination-btn-active:not(.right-btn)");
	By previousPageBtn					= By.cssSelector(".right-btn.pagination-btn-active");
	//*******other locators ends here*******//
	
	// enum for Call detail
	public static enum CallDataFilterType {
		TimeFrame, Agent, Teams, Company, Name, Title, Disposition, LeadStatus, OppStage, CallDate, CallDuration, CallRatings, Tag, Library, HasNotes, HasSupNotes, VideoCalls, SharedWithOthers, FlaggedForCoaching, HasAnnotations, callDirectionInbound, callDirectionOutbound, ShowAllTags, ShowAllLibraries, ShowAllLeadStatus, ShowAllCallDisposition, ShowAllOppStage, AgentTalkTime, AvgAgentTalkStreak, LongAgentTalkStreak, OvrAvgTalkStreak, OvrLongTalkStreak, ElasticSearch 
	}
	
	//enum for time frame values
	public static enum TimeFrame{
		AllTime("All Time"),
		Today("Today"),
		OneWeek("1 week"),
		OneMonth("1 month"),
		ThreeMonth("3 months"),
		OneYear("1 year"),
		Custom("Custom");
		
		private String displayValue ;
		
		TimeFrame(String displayValue) {
			this.displayValue = displayValue;
		}
		
		public String displayValue() {return displayValue;}
	}
	
	//enum for Sorting Types values
		public static enum SortOptions{
			NewtoOld("Newest to Oldest"),
			OldtoNew("Oldest to Newest"),
			AgenNameAZ("Agent Name (A-Z)"),
			AgenNameZA("Agent Name (Z-A)"),
			LongtoShort("Longest to Shortest"),
			ShorttoLong("Shortest to Longest"),
			libSavedSearchAZ("Alphabetically (A-Z)"),
			libSavedSearchZA("Alphabetically (Z-A)");
			
			private String displayValue ;
			
			SortOptions(String displayValue) {
				this.displayValue = displayValue;
			}
			
			public String displayValue() {return displayValue;}
		}
		
	public boolean isUserOnCallsTab(WebDriver driver) {
		return isElementVisible(driver, callsTabLink, 0) && isElementVisible(driver, clearAllLink, 0);
	}
	
	/**
	 * use this method to navigate to calls page on conversation AI
	 * User should be already on Conversation ai section
	 * @param driver
	 */
	public void navigateToCallsPage(WebDriver driver) {
		waitUntilVisible(driver, callsTabLink);
		clickElement(driver, callsTabLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to verify that user is on the Calls Tab
	 * @param driver
	 */
	public void verifyUserOnCallsPage(WebDriver driver) {
		assertEquals(getCssValue(driver, findElement(driver, callsTabLink), CssValues.BorderColor), "rgb(0, 0, 238)");
	}
	
	/**
	 * use this method to wait for the progress circle to disappear
	 * it waits for 2 seconds for progress circle to appear before disappearing
	 * @param driver
	 */
	public void waitTillProgressCircleInvisible(WebDriver driver) {
		isElementVisible(driver, progressCircle, 2);
		waitUntilInvisible(driver, progressCircle);
	}
	
	/**
	 * use this method to take a screenshot to search result section
	 * it is helpful in comparing the default search results
	 * @param driver
	 * @return the image of the search results in File format
	 */
	public File getSearchFilterContainerScreenshot(WebDriver driver) {
		clearAllFilters(driver);
		scrollToElement(driver, sortDropDown);
		return HelperFunctions.captureElementScreenShot(driver, findElement(driver, serachResultContainer));
	}
	
	/**
	 * use this method to find if boolean sign present for the filter
	 * provide the status name to get it's status
	 * @param driver
	 * @param statusName
	 * @return boolean
	 */
	public boolean isFilterBooleanStatusPresent(WebDriver driver, String statusName) {
		int index =  getTextListFromElements(driver, selectedFilterNameList).indexOf(statusName);
		if(isChildElementPresent(driver, getElements(driver, selectedFilterChipList).get(index), selectedFilterStatusIcon)){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * use this method to get the status of the applied filter
	 * provide the status name to get it's status
	 * @param driver
	 * @param statusName
	 * @return status, if true filter is inclusive, false mean filter is exclusive
	 */
	public boolean getSelectFilterBooleanStatus(WebDriver driver, String statusName) {
		int index =  getTextListFromElements(driver, selectedFilterNameList).indexOf(statusName);
		if(isChildElementPresent(driver, getElements(driver, selectedFilterChipList).get(index), selectedFilterTrueBooleanStatus)){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * use this method to change the boolean status of the applied filter
	 * provide the filter name to change it's status
	 * @param driver
	 * @param filterName  
	 */
	public void clickFilterBooleanStatus(WebDriver driver, String filterName) {
		int pageCount = getTotalPage(driver);
		int rowCounts = getInactiveElements(driver, searchResultRows).size();
		int index =  getTextListFromElements(driver, selectedFilterNameList).indexOf(filterName);
		getElements(driver, selectedFilterChipList).get(index).findElement(selectedFilterStatusIcon).click();
		waitTillProgressCircleInvisible(driver);
		boolean isPageCountEquals = getTotalPage(driver) == pageCount;
		boolean isRowCountEquals = getInactiveElements(driver, searchResultRows).size() == rowCounts;
		assertFalse(isPageCountEquals && isRowCountEquals, "Page counts are Equals: " + isPageCountEquals + ", Row Counts are Equals: " + isRowCountEquals);
	}
	
	
	/**
	 * use this method to verify if selected filter is appearing in chips
	 * @param driver
	 * @param filterName
	 * @return boolean if chip present or not
	 */
	public boolean isSelectedFilterChipPresent(WebDriver driver, String filterName) {
		if(getTextListFromElements(driver, selectedFilterNameList) != null)
			return getTextListFromElements(driver, selectedFilterNameList).contains(filterName);
		else
			return false;
	}
	
	/**
	 * use this method to select sorting
	 * @param driver
	 */
	public void selectSearchResultSort(WebDriver driver, SortOptions sortBy) {
		clickAndSelectFromDropdownFilter(driver, sortDropDown, dropdownOption, sortBy.ordinal());
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * use this method to get the selected sort type in the Sort list
	 * @param driver
	 * @return String the selected option text
	 */
	public String  getSelectedSortType(WebDriver driver) {
		return getElementsText(driver, sortDropDown).trim();
	}
	
	/**
	 * use this method to enter search text into the Search box
	 * @param driver
	 * @param searchText
	 */
	public void enterSearchText(WebDriver driver, String searchText) {
		enterText(driver, searchTextBox, searchText);
		clickEnter(driver, searchTextBox);
		waitTillProgressCircleInvisible(driver);
	}
	
	public void clickSaveSearchButton(WebDriver driver) {
		clickElement(driver,  saveSearchButton);
		waitUntilVisible(driver, savedSearchHeading);
	}
	/**
	 * use this method to remove search filter
	 * by clicking on delete icon on chip
	 * @param driver
	 * @param filterName
	 */
	public void removeFilter(WebDriver driver, String filterName) {
		By removeIcon = By.xpath(removeFilterIcon.replace("$$FilterText$$", filterName));
		if (isElementVisible(driver, removeIcon, 0)) {
			clickElement(driver, removeIcon);
			waitTillProgressCircleInvisible(driver);
		} else {
			System.out.println("No such filter exist");
		}
	}
	
	/**
	 * use this method to clear all filters by clicking clear all
	 * @param driver
	 */
	public void clearAllFilters(WebDriver driver) {
		navigateToCallsPage(driver);
		selectSearchResultSort(driver, SortOptions.NewtoOld);
		clickByJs(driver, clearAllLink);
		waitTillProgressCircleInvisible(driver);
	}

	/**
	 * use this method to expand Call Details section on left nav tab
	 * @param driver
	 */
	public void expandCallDetails(WebDriver driver) {
		if(!getAttribue(driver, callDetailsRow, ElementAttributes.Class).contains("open")) {
			clickElement(driver, callDetailsRow);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * use this method to expand participant Details section on left nav tab
	 * @param driver
	 */
	public void expandParticipantsDetails(WebDriver driver) {
		if(!getAttribue(driver, ParticipantsRow, ElementAttributes.Class).contains("open")) {
			clickElement(driver, ParticipantsRow);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * use this method to expand Agent Call Matrics Details section on left nav tab
	 * @param driver
	 */
	public void expandAgentCallMatrcsDetails(WebDriver driver) {
		if(!getAttribue(driver, agentCallMatricesRow, ElementAttributes.Class).contains("open")) {
			clickElement(driver, findElement(driver, agentCallMatricesRow));
			assertTrue(getAttribue(driver, agentCallMatricesRow, ElementAttributes.Class).contains("open"));
			assertEquals((driver.findElement(agentCallMatricesRow).findElement(toggleButton)).getAttribute(ElementAttributes.outerHTML.toString()), toggleImage);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * use this method to expand Overall Call Matrics Details section on left nav tab
	 * @param driver
	 */
	public void expandOverallCallMatrcsDetails(WebDriver driver) {
		if(!getAttribue(driver, overallCallMatricesRow, ElementAttributes.Class).contains("open")) {
			clickElement(driver, findElement(driver, overallCallMatricesRow));
			assertTrue(getAttribue(driver, overallCallMatricesRow, ElementAttributes.Class).contains("open"));
			assertEquals(getAttribue(driver, findElement(driver, overallCallMatricesRow).findElement(toggleButton), ElementAttributes.outerHTML), toggleImage);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * use this function to get selected time frame from the Agent dropdown
	 * @param driver
	 * @return
	 */
	public String getTimeIntervalLabel(WebDriver driver) {
		return getElementsText(driver, timeFrameDropDownLabel);
	}
	
	/**
	 * use this method to verify the time frame dropdown options
	 * @param driver
	 */
	public void verifyTimeFrameDropDownOptions(WebDriver driver) {
		waitUntilVisible(driver, timeFrameDropDown);
		clickElement(driver, timeFrameDropDown);
		idleWait(1);
		List<String> searchedDropDownItems = getTextListFromElements(driver, dropdownOption);
		int i=0;
		for (TimeFrame time : TimeFrame.values()) {
			assertEquals(time.displayValue(), searchedDropDownItems.get(i));
			i++;
		}
		reloadSoftphone(driver);
	}
	
	
	/**
	 * Use this method to verify that start and end Time Custom box is empty
	 * @param driver
	 */
	public void verifyDefaultStartEndCustomBox(WebDriver driver) {
		clickAndSelectFromDropdownFilter(driver, timeFrameDropDown, dropdownOption, 6);
		assertTrue(getElementsText(driver, customStartDateBox).isEmpty());
		assertTrue(getElementsText(driver, customEndDateBox).isEmpty());
	}
	
	/**
	 * use this function to get selected agent name from the Agent dropdown
	 * @param driver
	 * @return
	 */
	public String getSelectedAgentName(WebDriver driver) {
		return getElementsText(driver, agentDropdown);
	}
	
	/**
	 * use this function to get selected team name from the team dropdown
	 * @param driver
	 * @return
	 */
	public String getSelectedTeamName(WebDriver driver) {
		return getElementsText(driver, teamDropdown);
	}
	
	/**
	 * use this method to find if Agent Team filter is present or not under Participant Details section
	 * it expands participants Details before verifying the Agent Team Filter is present or not
	 * @param driver
	 * @return true if filter present else return false
	 */
	public boolean isTeamFilterPresent(WebDriver driver) {
		expandParticipantsDetails(driver);
		return isElementVisible(driver, teamDropdown, 0);
	}
	
	/**
	 * use this method to find if team exists in Agent Team filter dropdown list
	 * it expands participants Details before verifying
	 * @param driver
	 * @param team
	 * @return team index if team is present in dropdown list else -1
	 */
	public int getTeamIndexInTeamFilter(WebDriver driver, String team) {
		expandParticipantsDetails(driver);
		if(!isElementVisible(driver, dropdownOption, 0))
			clickElement(driver, teamDropdown);
		return getTextListFromElements(driver, dropdownOption).indexOf(team);
	}
	
	/**
	 * use this method to get number of teams in the Team Dropdown
	 * @param driver
	 * @return
	 */
	public int getTeamCountInTeamFilter(WebDriver driver) {
		expandParticipantsDetails(driver);
		if(!isElementVisible(driver, dropdownOption, 0))
			clickElement(driver, teamDropdown);
		return getElements(driver, dropdownOption).size();
	}
	
	/**
	 * use this method to verify that prospect filters are visible
	 * it expands the participant Filter section before verifying
	 * @param driver
	 */
	public void verifyProspectFilters(WebDriver driver) {
		expandParticipantsDetails(driver);
		waitUntilVisible(driver, prospectNameLabel);
		waitUntilVisible(driver, prospectNameDropDown);
		clickElement(driver, prospectNameDropDown);
		assertEquals(getElementsText(driver, ReportMetabaseCommonPage.startTypingValidation), "Start typing...");
		
		//company
		waitUntilVisible(driver, prospectCompanyLabel);
		waitUntilVisible(driver, prospectCompanyDropDown);
		clickElement(driver, prospectCompanyDropDown);
		assertEquals(getElementsText(driver, ReportMetabaseCommonPage.startTypingValidation), "Start typing...");
		
		//title
		waitUntilVisible(driver, ProspectTitleLabel);
		waitUntilVisible(driver, prospectTitleDropDown);
		clickElement(driver, prospectTitleDropDown);
		assertEquals(getElementsText(driver, ReportMetabaseCommonPage.startTypingValidation), "Start typing...");

		
	}
	
	/**
	 * use this method to expand Engagement section if left nav
	 * @param driver
	 */
	private void expandEngagementSection(WebDriver driver) {
		if(!getAttribue(driver, engagementRow, ElementAttributes.Class).contains("open")) {
			clickElement(driver, engagementRow);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * use this method to get number of rows
	 * @param driver
	 * @return row number in integer
	 */
	public int getSearchPageRowCount(WebDriver driver) {
		if (!isListElementsVisible(driver, searchResultRows, 5)) {
			return 0;
		}
		return getInactiveElements(driver, searchResultRows).size();
	}
	
	/**
	 * This method sets Converstaion AI filters
	 * then checks that selected filter displayed as search filter above search in chip
	 * also verifies if chip has remove image for the circle
	 * @param driver
	 * @param callFilterType "type of filters predefined in enum CallDataFilterType"
	 * @param index "index of the element to be selected. starts from 0 and for drop down only. keep it null for other filters"
	 * @param sliderRange "range defined for the slider filters. keep it null if any other filter is used"
	 * @return the filter text which is selected
	 */
	public String setConversationAIFilters(WebDriver driver, CallDataFilterType callFilterType, int index, Range<Integer> sliderRange) {
		String selectedValue = null;
		int pageCount = getTotalPage(driver);
		switch (callFilterType) {
		case TimeFrame:
			selectedValue = clickAndSelectFromDropdownFilter(driver, timeFrameDropDown, dropdownOption, index);
			if (index == 6) selectedValue = selectCustomTimeFrame(driver);																							//for custom time frame
			pageCount = (index >= 5) ? 0 : pageCount;																												//Currently we do not have CAI records for more than a year so page count remains same on filter
			break;
		case CallDuration:
			expandCallDetails(driver);
			selectedValue = selectSliderRange(driver, callDurationSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case Disposition:
			expandCallDetails(driver);
			selectedValue = setReactDropdownOption(driver, dispositionFilterInput, index);
			break;
		case LeadStatus:
			expandCallDetails(driver);
			selectedValue = setReactDropdownOption(driver, leadStatusFilterInput, index);
			break;
		case OppStage:
			expandCallDetails(driver);
			selectedValue = setReactDropdownOption(driver, opportunityStageFilterInput, index);
			pageCount = 0;
			break;
		case Tag:
			expandCallDetails(driver);
			selectedValue = setReactDropdownOption(driver, tagsFilterInput, index);
			tagSentimentImageList.add(getAttribue(driver, By.xpath(tagSentimentIcon.replace("$$Tag$$", selectedValue)), ElementAttributes.innerHTML));
			break;
		case Library:
			expandCallDetails(driver);
			selectedValue = setReactDropdownOption(driver, LibraryFilerInput, index);
			break;
		case callDirectionInbound:
			expandCallDetails(driver);
			selectedValue = selectCheckBoxFilterOption(driver, inboundCallCheckbox);
			break;
		case callDirectionOutbound:
			expandCallDetails(driver);
			selectedValue = selectCheckBoxFilterOption(driver, outboundCallCheckbox);
			break;
		case CallRatings:
			expandCallDetails(driver);
			selectedValue = selectSliderRange(driver, callRatingsSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case VideoCalls:
			expandCallDetails(driver);
			selectedValue = selectCheckBoxFilterOption(driver, videoCallFilter);
			break;
		case Agent:
			expandParticipantsDetails(driver);
			selectedValue = clickAndSelectFromDropdownFilter(driver, agentDropdown, dropdownOption, index);
			break;
		case Teams:
			expandParticipantsDetails(driver);
			selectedValue = clickAndSelectFromDropdownFilter(driver, teamDropdown, dropdownOption, index);
			break;
		case Name:
			expandParticipantsDetails(driver);
			selectedValue = setReactDropdownOption(driver, prospectNameDropDown, prospectName, index);
			break;
		case Company:
			expandParticipantsDetails(driver);
			enterBackspace(driver, prospectCompanyDropDown);
			selectedValue = setReactDropdownOption(driver, prospectCompanyDropDown, prospectCompany, index);
			break;
		case Title:
			expandParticipantsDetails(driver);
			selectedValue = setReactDropdownOption(driver, prospectTitleDropDown, prospectTitle, index);
			break;
		case HasNotes:
			expandEngagementSection(driver);
			selectedValue = selectCheckBoxFilterOption(driver, hasNotesCheckbox);
			break;
		case HasSupNotes:
			expandEngagementSection(driver);
			selectedValue = selectCheckBoxFilterOption(driver, hasSupervisorNotesCheckbox);
			break;
		case HasAnnotations:
			expandEngagementSection(driver);
			selectedValue = selectCheckBoxFilterOption(driver, hasAnnotationCheckbox);
			break;
		case FlaggedForCoaching:
			expandEngagementSection(driver);
			selectedValue = selectCheckBoxFilterOption(driver, coachingFlaggedCheckbox);
			break;
		case SharedWithOthers:
			expandEngagementSection(driver);
			selectedValue = selectCheckBoxFilterOption(driver, sharedWithOthersCheckbox);
			break;
		case AgentTalkTime:
			expandAgentCallMatrcsDetails(driver);
			selectedValue = selectSliderRange(driver, agentTalkTimeSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case AvgAgentTalkStreak:
			expandAgentCallMatrcsDetails(driver);
			selectedValue = selectSliderRange(driver, agentAvgTalkStrkSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case LongAgentTalkStreak:
			expandAgentCallMatrcsDetails(driver);
			selectedValue = selectSliderRange(driver, agentLongTalkStrkSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case OvrAvgTalkStreak:
			expandOverallCallMatrcsDetails(driver);
			selectedValue = selectSliderRange(driver, ovrAvgTalkStrkSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case OvrLongTalkStreak:
			expandOverallCallMatrcsDetails(driver);
			selectedValue = selectSliderRange(driver, ovrLongTalkstrkSlideFilter, sliderRange.lowerEndpoint(), sliderRange.upperEndpoint());
			break;
		case ElasticSearch:
			pageCount = elasticSearchPageCount;
			selectedValue = elasticSearchStringValue;
			break;
		default:
			break;
		}
		waitTillProgressCircleInvisible(driver);
		dashboard.isPaceBarInvisible(driver);
		
		// Check that selected filter displayed as search filter above search in chip
		//also verifies if chip has remove image for the circle
		if (selectedFilterNameList != null) {
			List<WebElement> filterList = getElements(driver, selectedFilterNameList);
			assertTrue(isTextPresentInList(driver, filterList, selectedValue));
			
			By removeIcon = By.xpath(removeFilterIcon.replace("$$FilterText$$", selectedValue));
			assertTrue(isElementVisible(driver, removeIcon, 0));
		}
		
		//verify that pages count is not same before and after applying filter
		assertNotEquals(getTotalPage(driver), pageCount);  
		
		return selectedValue;
	}
	
	/**
	 * use this method to set the check box type filters
	 * @param driver
	 * @param checkboxLocator
	 * @return the text of the selected filter
	 */
	private String selectCheckBoxFilterOption(WebDriver driver, By checkboxLocator) {
		if(!findElement(driver, checkboxLocator).isSelected()) {
			clickByJs(driver, checkboxLocator);
		}
		return getAttribue(driver, checkboxLocator, ElementAttributes.value);
	}
	
	/**
	 * use this function to set the slide bar filter
	 * @param driver
	 * @param sliderBarLocator
	 * @param fromRangeSlider
	 * @param toRangeSlider
	 * @param fromRange
	 * @param toRange
	 * @return the selected range in format "fromRange + " – " + toRange"
	 */
	private String selectSliderRange(WebDriver driver, By sliderBarFilterLocator, int fromRange, int toRange) {
		WebElement sliderBarLocator	= findElement(driver, sliderBarFilterLocator).findElement(SlideBar);
		WebElement fromRangeSlider	= findElement(driver, sliderBarFilterLocator).findElement(FromSliderPoint);
		WebElement toRangeSlider	= findElement(driver, sliderBarFilterLocator).findElement(ToSliderPoint);
		slideElement(driver, sliderBarLocator, fromRangeSlider, toRangeSlider, fromRange, toRange);
		return fromRange + " " + hyphenCharacterUnicode + " " + toRange;
	}
	
	
	/**
	 * use this method to get minimum and maximum values of Slider filter 
	 * @param driver
	 * @param filterType slider filter for which the values are needed
	 * @return Pair<String, String> type
	 */
	public Pair<String, String> getSliderRanges(WebDriver driver, CallDataFilterType filterType){
		By filterTypeLocator = null;
		
		//Switch on filter type to get slider filter locator acordingly
		switch (filterType) {
			
		case CallRatings:
			expandCallDetails(driver);
			filterTypeLocator = callRatingsSlideFilter;
			break;
			
		case CallDuration:
			filterTypeLocator = callDurationSlideFilter;
			break;
			
		case AgentTalkTime:
			expandAgentCallMatrcsDetails(driver);
			filterTypeLocator = agentTalkTimeSlideFilter;
			break;
			
		case AvgAgentTalkStreak:
			expandAgentCallMatrcsDetails(driver);
			filterTypeLocator = agentAvgTalkStrkSlideFilter;
			break;
			
		case LongAgentTalkStreak:
			expandAgentCallMatrcsDetails(driver);
			filterTypeLocator = agentLongTalkStrkSlideFilter;
			break;
			
		case OvrAvgTalkStreak:
			expandOverallCallMatrcsDetails(driver);
			filterTypeLocator = ovrAvgTalkStrkSlideFilter;
			break;
			
		case OvrLongTalkStreak:
			expandOverallCallMatrcsDetails(driver);
			filterTypeLocator = ovrLongTalkstrkSlideFilter;
			break;
			
		default:
			break;
		}
		
		//get minimum and maximum values for the filter type set above
		String lowerPoint = findElement(driver, filterTypeLocator).findElement(MinValueLabel).getText();
		String upperPoint = findElement(driver, filterTypeLocator).findElement(MaxValueLabel).getText();
		
		return new Pair<String, String>(lowerPoint, upperPoint);
	}
	
	/**
	 * use this method to set the dropdown type filters
	 * @param driver
	 * @param reactDropDownLocator
	 * @param indexToSelect
	 * @return the selected option as string from the dropdown
	 */
	private String setReactDropdownOption(WebDriver driver, By reactDropDownLocator, int indexToSelect) {
		clickElement(driver, reactDropDownLocator);
		WebElement selectedOptionElement = getReactDropdownOptions(driver, reactDropDownLocator).get(indexToSelect);
		String selectedOption = selectedOptionElement.getText();
		selectedOptionElement.click();
		
		//verify the dropdown is closed as soon as element is selected
		assertFalse(isElementVisible(driver, selectedOptionElement, 0));
		return selectedOption;
	}
	
	/**
	 * use this method to set the dropdown type filters by entering a search text in the input
	 * @param driver
	 * @param reactDropDownLocator
	 * @param indexToSelect
	 * @return the selected option as string from the dropdown
	 */
	private String setReactDropdownOption(WebDriver driver, By reactDropDownLocator, String itemToSearch, int indexToSelect) {
		enterText(driver, reactDropDownLocator, itemToSearch);
		WebElement selectedOptionElement = getReactDropdownOptions(driver, reactDropDownLocator).get(indexToSelect);
		String selectedOption = selectedOptionElement.getText();
		selectedOptionElement.click();
		
		//verify the dropdown is closed as soon as element is selected
		assertFalse(isElementVisible(driver, selectedOptionElement, 0));
		return selectedOption;
	}
	
	/**
	 * use this function to get the element list of the option available in react dropdown
	 * @param driver
	 * @param reactDropDownLocator
	 * @return
	 */
	private List<WebElement> getReactDropdownOptions(WebDriver driver, By reactDropDownLocator) {
		//Since the dropdown locator is dynamic so we are getting it's value and replacing it with option to get the dropdown options locator
		String dropwDownOptionName = getAttribue(driver, reactDropDownLocator, ElementAttributes.ariaControls).replace("popup", "option");
		return getElements(driver, By.xpath(".//*[contains(@id, '" + dropwDownOptionName + "')]"));
	}
	
	/**
	 * use this method to set non react type of dropdown filters
	 * @param driver
	 * @param dropDownLocator
	 * @param dropdownOptionsLocator
	 * @param index
	 * @return the text of the selected option
	 */
	protected String clickAndSelectFromDropdownFilter(WebDriver driver, By dropDownLocator, By dropdownOptionsLocator, int index) {
		waitUntilVisible(driver, dropDownLocator);
		clickElement(driver, dropDownLocator);
		idleWait(1);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, dropdownOptionsLocator);
		String agentName = searchedDropDownItems.get(index).getText();
		searchedDropDownItems.get(index).click();
		waitUntilInvisible(driver, dropdownOptionsLocator);
		if (getElementsText(driver, dropDownLocator) == null || getElementsText(driver, dropDownLocator).isEmpty()) {
			assertEquals(getAttribue(driver, dropDownLocator, ElementAttributes.value), agentName);
		} else {
			assertEquals(getElementsText(driver, dropDownLocator), agentName);
		}
		return agentName;
	}
	
	/**
	 * use this method to select agent name from filter
	 * @param driver
	 * @param agentName
	 */
	public void selectAgentNameFromFilter(WebDriver driver, String agentName) {
		int pageCount = getTotalPage(driver);
		expandParticipantsDetails(driver);
		clickAndSelectFromDropDown(driver, agentDropdown, dropdownOption, agentName);
		dashboard.isPaceBarInvisible(driver);
		
		// Check that selected filter displayed as search filter above search in chip
		// also verifies if chip has remove image for the circle
		List<WebElement> filterList = getElements(driver, selectedFilterNameList);
		assertTrue(isTextPresentInList(driver, filterList, agentName));

		By removeIcon = By.xpath(removeFilterIcon.replace("$$FilterText$$", agentName));
		assertTrue(isElementVisible(driver, removeIcon, 0));
		
		//verify that pages count is not same before and after applying filter
		assertNotEquals(getTotalPage(driver), pageCount);  
	}
	
	/**
	 * method to verify auto complete for agentNames
	 * @param driver
	 * @param partialAgentName
	 */
	public void verifyAutocompleteAgentNames(WebDriver driver, String partialAgentName, String appendText) {
		expandParticipantsDetails(driver);
		clickElement(driver, agentDropdown);
		clearAll(driver, agentDropdown);
		enterText(driver, agentDropdown, partialAgentName);
		List<String> agentList = getTextListFromElements(driver, dropdownOption);
		for (String agent : agentList) {
			Pattern.compile(Pattern.quote(appendText), Pattern.CASE_INSENSITIVE).matcher(agent).find();
		}
		int sizeBefore = agentList.size();

		//apending text to narrow down contents
		partialAgentName = partialAgentName.concat(appendText);
		appendText(driver, agentDropdown, appendText);
		agentList.clear();
		agentList = getTextListFromElements(driver, dropdownOption);
		for (String agent : agentList) {
			Pattern.compile(Pattern.quote(appendText), Pattern.CASE_INSENSITIVE).matcher(agent).find();
		}
		assertTrue(agentList.size() < sizeBefore);
	}

	/**
	 * method to verify auto complete for agentTeams
	 * @param driver
	 * @param partialTeamName
	 */
	public void verifyAutocompleteAgentTeams(WebDriver driver, String partialTeamName, String appendText) {
		expandParticipantsDetails(driver);
		clickElement(driver, teamDropdown);
		clearAll(driver, teamDropdown);
		enterText(driver, teamDropdown, partialTeamName);
		List<String> agentTeamList = getTextListFromElements(driver, dropdownOption);
		for (String agentTeam : agentTeamList) {
			Pattern.compile(Pattern.quote(partialTeamName), Pattern.CASE_INSENSITIVE).matcher(agentTeam).find();
		}
		int sizeBefore = agentTeamList.size();

		partialTeamName = partialTeamName.concat(appendText);
		appendText(driver, teamDropdown, appendText);
		agentTeamList.clear();
		agentTeamList = getTextListFromElements(driver, dropdownOption);
		for (String agentTeam : agentTeamList) {
			Pattern.compile(Pattern.quote(partialTeamName), Pattern.CASE_INSENSITIVE).matcher(agentTeam).find();
		}
		assertTrue(agentTeamList.size() < sizeBefore);
	}

	/**
	 * @param driver
	 * @return
	 */
	public List<String> getAgentListInDropDown(WebDriver driver) {
		expandParticipantsDetails(driver);
		waitUntilVisible(driver, agentDropdown);
		//verify placeholder
		clearAll(driver, agentDropdown);
		dashboard.isPaceBarInvisible(driver);
		assertEquals(getAttribue(driver, agentDropdown, ElementAttributes.Placeholder), "Name");
		clickElement(driver, agentDropdown);
		return getTextListFromElements(driver, dropdownOption);
	}

	/**
	 * use this function to set the Custom Time Frame
	 * it will set the start date with an year and an month difference
	 * it will set the end date as todays
	 * it will verify that the dates are correctly set in the start and end date picker
	 * it will verify the picker change from year to month and month to year on clicking the year and month
	 * @param driver
	 * @return String filter value that needs to be verified in filter chip
	 */
	private String selectCustomTimeFrame(WebDriver driver) {
		//Set start date in the calendar instance
		Calendar previousCal = Calendar.getInstance();
		previousCal.add(Calendar.YEAR, -1);
		previousCal.add(Calendar.MONTH, -1);
		previousCal.set(Calendar.DAY_OF_MONTH, 1);
		customStartDateCalendar = previousCal;													//Set the custom status start calendar global variable to use it in verification method isCallInTimeRange
		
		//Set date formats
		SimpleDateFormat datedFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat chipDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Open Start Date calendar Picker and verify year month picker changer
		clickElement(driver, customStartDateBox);
		clickElement(driver, changeToYearPicker);
		waitUntilVisible(driver, yearPickerContainer);
		assertTrue(isElementVisible(driver, yearPickerContainer, 5));
		clickElement(driver, changeToMonthPicker);
		assertTrue(isElementVisible(driver, monthDayPickerContainer, 5));
		clickElement(driver, changeToYearPicker);
		
		//Select preceding year from the year list picker
		String getYearToBeSelected = getElementsText(driver, precedingYear);
		clickElement(driver, precedingYear);
		assertTrue(getElementsText(driver, monthHeaderText).contains(getYearToBeSelected));
		
		//Navigate to previous month and select first day of the month
		idleWait(1);
		clickElement(driver, monthChangeLeftBtn);
		idleWait(1);
		clickElement(driver, monthFirstDay);
		
		//Select custom End date as tdays
		clickElement(driver, customEndDateBox);
		clickByJs(driver, monthSelectedDay);
		waitTillProgressCircleInvisible(driver);
		
		//Get the values of custom Start and End date set in above steps
		String actualCustomStartDate = getAttribue(driver, customStartDateBox, ElementAttributes.value);
		String actualCustomEndtDate = getAttribue(driver, customEndDateBox, ElementAttributes.value);
		
		//get the expected Start and End date
		String previousDate = datedFormat.format(previousCal.getTime());
		String currentDate = datedFormat.format(Calendar.getInstance().getTime());
		
		//Assert that the Start and End date are set correctly
		assertEquals(actualCustomStartDate, previousDate);
		assertEquals(actualCustomEndtDate, currentDate);
		
		//Return the filter value the format that needs to be verified in the filter chip
		return chipDateFormat.format(previousCal.getTime()) + " " + hyphenCharacterUnicode + " " + chipDateFormat.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * use this method to set this filter using Show All list
	 * it set's multiple number of filter values
	 * then checks that selected filter displayed as search filter above search in chip
	 * also verifies if chip has remove image for the circle
	 * @param driver
	 * @param callFilterType
	 * @param startIndex "index from where the values has to be selected. Starts from 0"
	 * @param totalValuesToSelect "total number of values to select"
	 * @return the text list of the selected values
	 */
	public List<String>setFilterFromShowAllList(WebDriver driver, CallDataFilterType callFilterType, int startIndex, int totalValuesToSelect) {
		List<String> selectedValues = null;
		switch (callFilterType) {
		case Disposition:
			expandCallDetails(driver);
			selectedValues = selectShowAllListFilters(driver, dispositionShowAll, startIndex ,totalValuesToSelect);
			break;
		case LeadStatus:
			expandCallDetails(driver);
			selectedValues = selectShowAllListFilters(driver, leadStatusShowAll, startIndex ,totalValuesToSelect);
			break;
		case OppStage:
			expandCallDetails(driver);
			selectedValues = selectShowAllListFilters(driver, oppStagesShowAll, startIndex ,totalValuesToSelect);
			break;
		case Tag:
			expandCallDetails(driver);
			selectedValues = selectShowAllListFilters(driver, tagsShowAll, startIndex ,totalValuesToSelect);
			break;
		case Library:
			expandCallDetails(driver);
			selectedValues = selectShowAllListFilters(driver, librariesShowAll, startIndex ,totalValuesToSelect);
			break;
		default:
			break;
		}
		waitTillProgressCircleInvisible(driver);

		// Check that selected filter displayed as search filter above search result table
		List<WebElement> filterList = getElements(driver, selectedFilterNameList);
		for (String selectedValue : selectedValues) {
			assertTrue(isTextPresentInList(driver, filterList, selectedValue));
		}
		return selectedValues;
	}
		
	/**
	 * @param driver
	 * @param locator
	 * @param startIndex
	 * @param totalValuesToSelect
	 * @return
	 */
	private List<String> selectShowAllListFilters(WebDriver driver, By locator, int startIndex, int totalValuesToSelect) {
		tagSentimentImageList.clear();
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		waitUntilVisible(driver, showAllListApplyBtn);
		
		List<String> tokens = new ArrayList<String>();
		
		for(int i = startIndex; i < (startIndex + totalValuesToSelect); i++) {
			if(!findElement(driver, By.cssSelector(showAllListCheckBox.replace("$$Index$$", String.valueOf(i + 1)))).isSelected())
				findElement(driver, By.cssSelector(showAllListCheckBox.replace("$$Index$$", String.valueOf(i + 1)))).click();
			String token = getElementsText(driver, By.cssSelector(showAllListItemName.replace("$$Index$$", String.valueOf(i + 1)))).trim();
			tokens.add(token);
			if (isElementVisible(driver, By.cssSelector(showAllThumbIcon.replace("$$Index$$", String.valueOf(i + 1)).trim()), 0))
				tagSentimentImageList.add(getAttribue(driver, By.cssSelector(showAllThumbIcon.replace("$$Index$$", String.valueOf(i + 1)).trim()), ElementAttributes.innerHTML));
		}
		clickElement(driver, showAllListApplyBtn);
		return tokens;
	}
	
	/**
	 * use this method to set elastic filter based on call Filter type and index of the row
	 * @param driver
	 * @param callFilterType it is of CallDataFilterType enum. type of the filter
	 * @param index row index from which elastic filter is need to be set
	 * @return
	 */
	public String setElasticFilters(WebDriver driver, CallDataFilterType callFilterType, int index) {
		
		elasticSearchPageCount = -1; 
		elasticSearchStringValue = null;
		tagSentimentImageList.clear();
		
		String selectedValue = null;
		elasticSearchPageCount = getTotalPage(driver);
		
		expandSearchRows(driver, index + 1);	
		
		switch (callFilterType) {
		case Disposition:
			selectedValue = getElementsText(driver, dispositionResult);
			clickElement(driver, dispositionResult);
			break;
		case LeadStatus:
			selectedValue = getElementsText(driver, leadStatusResult);
			clickElement(driver, leadStatusResult);
			break;
		case Agent:
			WebElement agentLocator = findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index + 1)))).findElement(calledAgent);
			selectedValue = getElementsText(driver, agentLocator);
			clickElement(driver, agentLocator);
			break;
		case Company:
			WebElement companyLocator = findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index + 1)))).findElement(ProspectCompanyHeading);
			selectedValue = getElementsText(driver, companyLocator);
			clickElement(driver, companyLocator);
			break;
		case Name:
			WebElement prosNameLocator = findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index + 1)))).findElement(ProspectNameHeading);
			selectedValue = getElementsText(driver, prosNameLocator);
			clickElement(driver, prosNameLocator);
			break;
		case Tag:
			selectedValue = getElementsText(driver, findElement(driver, tagsResult));
			clickElement(driver, tagsResult);
			waitTillProgressCircleInvisible(driver);
			tagSentimentImageList.add(getAttribue(driver, By.xpath(tagSentimentIcon.replace("$$Tag$$", selectedValue)), ElementAttributes.innerHTML));
			break;
		case Library:
			selectedValue = getElementsText(driver, findElement(driver, librariesResult));
			clickElement(driver, librariesResult);
			return selectedValue;
		default:
			break;
		}
		
		//pass the selected value and verify that it is set as an filter
		elasticSearchStringValue = selectedValue;
		setConversationAIFilters(driver, CallDataFilterType.ElasticSearch, -1, null);
		
		return elasticSearchStringValue;
	}
	
	/**
	 * use this method to validate that each row contains the filter value which is selected
	 * It takes the multiple values for a single filter and verify the search result for "Or Logic"
	 * it verifies if result is inclusive or exclusive based on the status provided for the filter
	 * returns nothing if the verification is successful
	 * it fails if any row fails the verification
	 * @param driver
	 * @param tokenList "list of all the filter(single) applied"
	 * @param rangeFilterValue "for slider filters only. use null for other filter types"
	 * @param callFilterType "use for all other filters then slider. use null for slider filter"
	 * @param status "true if the filter is inclusive else false"
	 */
	public void verifySearchResult(WebDriver driver, List<String> tokenList, Range<Integer> rangeFilterValue, CallDataFilterType callFilterType, boolean status) {
		int page = getTotalPage(driver);
		int maxPageToVerify = 2;
		int maxRowToVerify 	= 10;
		boolean condition1Output;
		boolean condition2Output;
		for (int j = 0; j < page && j < maxPageToVerify; j++) {	
			//Loop for 3 pages
			List<WebElement> resultList = getInactiveElements(driver, searchResultRows);
			for (int i = 0; i < resultList.size() && i < maxRowToVerify; i++) {											//loop to verify only first 2 records rows
				System.out.println("Search Tokens are : "  + rangeFilterValue + " " + tokenList);
				
				WebElement reusltEntry = findElement(driver, 
						By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(i + 1))));

				//expands the conversation AI entry
				clickByJs(driver, reusltEntry);
				idleWaitInMs(500);

				boolean isTokenFound = false;
				switch (callFilterType) {																	//Switch on applied filter types to call methods related to the applied filters
				case TimeFrame:
					maxRowToVerify = 10;
					assertTrue(resultList != null);
					isTokenFound = isCallInTimeRange(driver, TimeFrame.valueOf(tokenList.get(0)), reusltEntry);
					break;
				case CallDuration:
					isTokenFound = isCallDurationInFilterrange(driver, rangeFilterValue, reusltEntry);
					break;
				case Disposition:
					isTokenFound = assertNonTagLibraryFilter(driver, tokenList, dispositionResult);
					break;
				case LeadStatus:
					isTokenFound = assertNonTagLibraryFilter(driver, tokenList, leadStatusResult);
					break;
				case OppStage:
					isTokenFound = assertNonTagLibraryFilter(driver, tokenList, OpportunityStageResult);
					break;
				case Tag:
					isTokenFound = isTagPresentInResults(driver, i, tokenList);
					break;
				case Library:
					isTokenFound = isLibraryPresentInResults(driver, i, tokenList);
					break;
				case callDirectionInbound:
					isTokenFound = isIconPresentInResults(driver, reusltEntry.findElement(callDirectionImage), inboundCallHTML);
					break;
				case callDirectionOutbound:
					isTokenFound = isIconPresentInResults(driver, reusltEntry.findElement(callDirectionImage), outboundCallHTML);
					break;
				case Teams:
				case Agent:
					condition1Output = assertNonTagLibraryFilter(driver, tokenList, agenDetailResult);
					condition2Output = tokenList.contains(reusltEntry.findElement(prospectDetailsHeading).findElement(calledAgent).getText());
					isTokenFound = (condition1Output ^ condition2Output) == true ? !status : condition1Output;		// If both conditions are not same then fail it by making token found opposite of the expected otherwise return the output output logic
					break;
				case Name:
					condition1Output = assertNonTagLibraryFilter(driver, tokenList, prospectNameResult);
					condition2Output = tokenList.contains(reusltEntry.findElement(prospectDetailsHeading).findElement(ProspectNameHeading).getText());
					isTokenFound = (condition1Output ^ condition2Output) == true ? !status : condition1Output;		// If both conditions are not same then fail it by making token found opposite of the expected otherwise return the output output logic
					break;
				case Company:
					condition1Output = assertNonTagLibraryFilter(driver, tokenList, prospectCompanyResult);
					condition2Output = tokenList.contains(reusltEntry.findElement(prospectDetailsHeading).findElement(ProspectCompanyHeading).getText());
					isTokenFound = (condition1Output ^ condition2Output) == true ? !status : condition1Output;		// If both conditions are not same then fail it by making token found opposite of the expected otherwise return the output output logic
					break;
				case Title:
					condition1Output = assertNonTagLibraryFilter(driver, tokenList, prospectTitleResult);
					condition2Output = reusltEntry.findElement(prospectDetailsHeading).getText().contains(", " + tokenList.get(0));
					isTokenFound = (condition1Output ^ condition2Output) == true ? !status : condition1Output;		// If both conditions are not same then fail it by making token found opposite of the expected otherwise return the output output logic
					break;
				case HasNotes:
					condition1Output = getAttribue(driver, callNotesImageinResult, ElementAttributes.color).equals("#7549EA");
					condition2Output = isIconPresentInResults(driver, findElement(driver, callNotesImageinResult).findElement(By.cssSelector("svg")), callNotesHTML);
					isTokenFound	 = condition1Output && condition2Output;	
					break;
				case HasSupNotes:
					condition1Output = getAttribue(driver, supervisorImageinResult, ElementAttributes.color).equals("#7549EA");
					condition2Output = isIconPresentInResults(driver, findElement(driver, supervisorImageinResult).findElement(By.cssSelector("svg")), supervisorHTML);
					isTokenFound	 = condition1Output && condition2Output;
					break;
				case HasAnnotations:
					condition1Output = getAttribue(driver, AnnotationImageinResult, ElementAttributes.color).equals("#7549EA");
					condition2Output = isIconPresentInResults(driver, findElement(driver, AnnotationImageinResult).findElement(By.cssSelector("svg")), annotationHTML);
					isTokenFound	 = condition1Output && condition2Output;
					break;
				case AgentTalkTime:
					maxRowToVerify = 4;
					idleWait(1);
					openConversationDetails(driver, i);
					String agentTalkTime = converstaionDetailPage.getAgentTalkTime(driver);
					isTokenFound = isMatricsInFilterrange(driver, rangeFilterValue, agentTalkTime);
					break;
				case AvgAgentTalkStreak:
					maxRowToVerify = 4;
					openConversationDetails(driver, i);
					String avgAgentTalkStreak = converstaionDetailPage.getAgentAvgTalkStreak(driver);
					isTokenFound = isMatricsInFilterrange(driver, rangeFilterValue, avgAgentTalkStreak);
					break;
				case LongAgentTalkStreak:
					maxRowToVerify = 4;
					idleWait(1);
					openConversationDetails(driver, i);
					String longAgentTalkStreak = converstaionDetailPage.getAgentLongTalkStreak(driver);
					isTokenFound = isMatricsInFilterrange(driver, rangeFilterValue, longAgentTalkStreak);
					break;
				case OvrAvgTalkStreak:
					maxRowToVerify = 4;
					openConversationDetails(driver, i);
					avgAgentTalkStreak = converstaionDetailPage.getAgentAvgTalkStreak(driver);
					String custAvgTalkStreak = converstaionDetailPage.getCustomerAvgTalkStreak(driver);
					isTokenFound = (isMatricsInFilterrange(driver, rangeFilterValue, avgAgentTalkStreak) || isMatricsInFilterrange(driver, rangeFilterValue, custAvgTalkStreak));
					break;
				case OvrLongTalkStreak:
					maxRowToVerify = 4;
					idleWait(1);
					openConversationDetails(driver, i);
					longAgentTalkStreak = converstaionDetailPage.getAgentLongTalkStreak(driver);
					String custLongTalkStreak = converstaionDetailPage.getCustomerLongStreak(driver);
					isTokenFound = (isMatricsInFilterrange(driver, rangeFilterValue, longAgentTalkStreak) || isMatricsInFilterrange(driver, rangeFilterValue, custLongTalkStreak));
					break;
				default:
					break;
				}
				
				if (status) {																				// Verify if search token is found or not according to the status of the filter
					assertTrue(isTokenFound);
				} else {
					assertFalse(isTokenFound);
				}
				
				if (isElementVisible(driver, openedCallEntry, 0)) {											// collapse the result of the conversation
					reusltEntry.click();
					idleWaitInMs(500);
				}
			}
			if (j < 1)
				navigateToNextCAIPage(driver, page, j); 													// navigates to next page till second page
			else if (j == 1)
				selectSearchResultSort(driver, SortOptions.OldtoNew);										//	select sort order from oldest to newest after 2 page	
		}
		selectSearchResultSort(driver, SortOptions.NewtoOld);
	}
	
	/**
	 * use this method to verify if the expanded conversation AI entry has on of the search values
	 * it is used for non tags and library type of filters.
	 * @param driver
	 * @param tokenList
	 * @param locator
	 * @return true if the conversation AI contains the search value else false
	 */
	private boolean assertNonTagLibraryFilter(WebDriver driver, List<String> tokenList, By locator) {
		System.out.println("Found value for CAI : " + getElementsText(driver, locator));
		return isTextPresentInStringList(driver, tokenList, getElementsText(driver, locator));
	}
	
	/**
	 * use this method to verify if Conversation AI contains tag type filter
	 * @param driver
	 * @param resultRowIndex
	 * @param tagsList
	 * @return true if the conversation AI contains the search value else false
	 */
	private boolean isTagPresentInResults(WebDriver driver, int resultRowIndex, List<String> tagsList) {
		List<WebElement> associatedTagsList = getElements(driver, tagsResult);
		System.out.println("Found Tag List : " + associatedTagsList);
		
		if (associatedTagsList == null || associatedTagsList.isEmpty()) {
			return false;
		}
		
		for (WebElement associatedTagElement : associatedTagsList) { 									//if the tag is visible in the expanded section then return true, else continue
			String associatedTag = associatedTagElement.getText();
			if (tagsList.indexOf(associatedTag) >= 0){
				String tagElementSentiment = associatedTagElement.findElement(tagsResultSentiment).getAttribute(ElementAttributes.innerHTML.toString());
				if(tagElementSentiment.equals(tagSentimentImageList.get(tagsList.indexOf(associatedTag)))) {
					System.out.println("Tag " + associatedTag + " found");
					return true;
				}
			}
		}

		if(associatedTagsList.get(associatedTagsList.size() - 1).getText().contains("More")) {			//if expanded conversation AI has more tags which are not visible here
			openConversationDetails(driver, resultRowIndex);											//then open the conversation AI details
			converstaionDetailPage.switchToAnnotationTab(driver);										//switch to annotation tab
			Boolean isTagFound = converstaionDetailPage.isTagPeresent(driver, tagsList, tagSentimentImageList);				//verify if tag is present there
			navigateToCallsPage(driver);
			return isTagFound;																			//returns true if tag is found in the conversation AI detail page else false
		}else {
			System.out.println("Tag is not found");
			return false;
		}
	}
	
	/**verify no Library Available text present or not
	 * @param driver
	 * @return
	 */
	public boolean isNoLibraryPresntTextVisible(WebDriver driver) {
		return isElementVisible(driver, noLibraryAvailable, 5);
	}
	
	/**
	 * use this method to verify if Conversation AI contains library type filter
	 * @param driver
	 * @param resultRowIndex
	 * @param LibraryList
	 * @return true if the conversation AI contains the search value else false
	 */
	public boolean isLibraryPresentInResults(WebDriver driver, int resultRowIndex, List<String> LibraryList) {
		List<String> associatedLibrarysList = getTextListFromElements(driver, librariesResult);
		System.out.println("Found Library List : " + associatedLibrarysList);
		
		if (associatedLibrarysList == null || associatedLibrarysList.isEmpty()) {
			return false;
		}
		
		for (String associatedLibrary : associatedLibrarysList) {										//if the library is visible in the expanded section then return true, else continue
			if (isTextPresentInStringList(driver, LibraryList, associatedLibrary)){
				System.out.println("Library " + associatedLibrary + " found");
				return true;
				}
			}

		if(associatedLibrarysList.get(associatedLibrarysList.size() - 1).contains("More")) {			//if expanded conversation AI has more libraries associated which are not visible here
			openConversationDetails(driver, resultRowIndex);											//then open the conversation AI details
			Boolean isLibraryFound = converstaionDetailPage.isLibraryAdded(driver, LibraryList);		//verify if library is present there
			navigateToCallsPage(driver);
			return isLibraryFound;																		//returns true if library is found in the conversation AI detail page else false
		}else {
			System.out.println("Library is not found");
			return false;
		}
	}
	
	/**
	 * use this filter to verify icons on the conversation AI
	 * it compares the outer html of the element provided
	 * @param driver
	 * @param iconToVerify
	 * @param htmlOfIcon
	 * @return true if icon is present else false
	 */
	private boolean isIconPresentInResults(WebDriver driver, WebElement iconToVerify, String htmlOfIcon) {
 		return getAttribue(driver, iconToVerify, ElementAttributes.outerHTML).equals(htmlOfIcon);
	}
	
	/**
	 * use this function to verify if call duration is present in the selected range
	 * @param driver
	 * @param callDurationRange
	 * @param row for which the duration has to be checked
	 * @return true if the duration is in the range else false
	 */
	@SuppressWarnings("deprecation")
	private boolean isCallDurationInFilterrange(WebDriver driver, Range<Integer> callDurationRange, WebElement row) {
		String callDuation = row.getText().replace("Duration: ", "");
		System.out.println("Call Duration is : " + callDuation);
		Date durationObj = HelperFunctions.parseStringToDateFormat(callDuation, "hh:mm:ss");						//gets the duration of the conversation AI in hh:mm:ss format
		return callDurationRange.contains(durationObj.getMinutes());												//verify if the duration of conversation AI is in the range call duration 
	}
	
	/**
	 * use this function to verify if call duration is present in the selected range
	 * @param driver
	 * @param matrixRange
	 * @param matrixValue
	 * @param index for which the duration has to be checked
	 * @return true if the duration is in the range else false
	 */
	private boolean isMatricsInFilterrange(WebDriver driver, Range<Integer> matrixRange, String matrixValue) {
		navigateToCallsPage(driver);
		String callMatrix = matrixValue.replace(" min", "").replace("%", "").split("\\.")[0];	
		return matrixRange.contains(Integer.parseInt(callMatrix));													//verify if the value of matrix is in the expected range 
	}
	
	/**
	 * use this method to verify that the CAI date is in the defined range
	 * @param driver
	 * @param timeInterval time frame in which the call entry date has to be verified 
	 * @param searchRow CAI call row for which the date has to be verified
	 * @return boolean
	 */
	private boolean isCallInTimeRange(WebDriver driver, TimeFrame timeInterval, WebElement searchRow) {
			
		//Defining the date formats for call dates
		String dateFormat = "MM/dd/yyyy hh:mm a";
		String rowDayFormat = "MMM d";
		String rowHourFormat = "h:mm a";
		
		//Initializing calendar instance for the time interval
		Calendar previousCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		currentCal.add(Calendar.DAY_OF_YEAR, 1);															//added another day in the upper range since the comparison is "<" not "<="
		previousCal.set(Calendar.HOUR_OF_DAY, 0);
		previousCal.set(Calendar.MINUTE, 0);
		previousCal.set(Calendar.SECOND, 0);
		
		//switching to the TimeFrame enum and subtracting respective number of days
		switch (timeInterval) {
		case Today:
			//previous day is already one day behind
			break;
		case OneWeek:
			previousCal.add(Calendar.DAY_OF_YEAR, -7);
			previousCal.add(Calendar.DAY_OF_YEAR, 1);														//added one day in the lower range since the comparison is ">" not ">="
			break;
		case OneMonth:
			previousCal.add(Calendar.MONTH, -1);
			//previousCal.add(Calendar.DAY_OF_YEAR, 1);														//added one day in the lower range since the comparison is ">" not ">="
			break;
		case ThreeMonth:
			previousCal.add(Calendar.MONTH, -3);
			//previousCal.add(Calendar.DAY_OF_YEAR, 1);														//added one day in the lower range since the comparison is ">" not ">="
			break;
		case OneYear:
			previousCal.add(Calendar.YEAR, -1);
			//previousCal.add(Calendar.DAY_OF_YEAR, 1);														//added one day in the lower range since the comparison is ">" not ">="
			break;
		case Custom:
			//setting the custom previous date as custom start date calendar which was set in function selectCustomTimeFrame
			previousCal = customStartDateCalendar;
			previousCal.add(Calendar.DAY_OF_YEAR, -1);
			break;
		default:
			break;
		}
		
		//Getting the call's date from the call entry
		String actualCallDate = getConverstaionCallDate(driver).replace("a.m.", "AM").replace("p.m.", "PM");
		Date callDate = HelperFunctions.parseStringToDateFormat(actualCallDate, dateFormat);
		String dayFormatDate = new SimpleDateFormat(rowDayFormat).format(callDate).toUpperCase();
		String hourFomratDate = new SimpleDateFormat(rowHourFormat).format(callDate).toUpperCase();
		
		//verifying that it is same outside on heading row as well
		String callDateOnHeaderRow = searchRow.findElement(searchRowsCallDate).getText();
		assertTrue(callDateOnHeaderRow.equals(dayFormatDate) || callDateOnHeaderRow.equals(hourFomratDate), "Call Date = : " + callDateOnHeaderRow + "Hour Date = : " + hourFomratDate + "Day Date = : " + dayFormatDate);
		
		//verifying that if call date is between the time frame
		System.out.println(previousCal.getTime() + " " + callDate + " " + currentCal.getTime());
		
		return (callDate.after(previousCal.getTime()) && callDate.before(currentCal.getTime()));
	}
	
	
	/**
	 * use this method to get the call date from the already opened entry
	 * @param driver
	 * @return Call date in String
	 */
	public String getConverstaionCallDate(WebDriver driver) {
		waitUntiTextAppearsInListElements(driver, agentDetailCallDate);
		return getElementsText(driver, agentDetailCallDate);
	}
	
	/**
	 * opens the conversation AI detail for the row number provided
	 * @param driver
	 * @param index
	 */
	public void openConversationDetails(WebDriver driver, int index) {
		getElements(driver, openConverstaionDetailIcon).get(index).click();
		dashboard.isPaceBarInvisible(driver);
		converstaionDetailPage.verifyRecordingPlayerVisible(driver);
	}
	
	/**
	 * use this method to get the sorted call list based on the value by which it is sorted
	 * @param driver
	 * @param sortBy it is of SortOptions enum type. Returned list is provided based on this only.
	 * @return List of date type.
	 */
	public List<Date> getSortedCaiList(WebDriver driver, SortOptions sortBy) {
		agentNameList.clear();
		List<Date> returnList = new ArrayList<>();
		
		int page = getTotalPage(driver);
		int maxPageToVerify = 2;																					//maximum number of pages for which it has to be verified
		
		for (int j = 0; j < page && j < maxPageToVerify; j++) {														//Loop for pages
			waitTillProgressCircleInvisible(driver);
			switch (sortBy) {
			case NewtoOld:
			case OldtoNew:
				returnList.addAll(getCallsDateList(driver));
				break;
			case AgenNameAZ:
			case AgenNameZA:
				waitUntilVisible(driver, calledAgentDirectLocator);
				agentNameList.addAll(getTextListFromElements(driver, calledAgentDirectLocator));
				break;
			case LongtoShort:
			case ShorttoLong:
				List<String>  callDurationList= getTextListFromElements(driver, searchResultRows);
				callDurationList.replaceAll(result->result.replace("Duration: ", ""));
				//Collections.replaceAll(callDurationList, "Duration: ", "");
				returnList.addAll(HelperFunctions.getDateListFromStringList(callDurationList, "hh:mm:ss"));
				break;
			default:
				break;
			}
		if(j != (maxPageToVerify-1)) navigateToNextCAIPage(driver, page, j); 										// navigates to next page till second page
		}
		
		return returnList;
	}
	
	//this method is used to get the list of date on which the call is made from cai
	private List<Date> getCallsDateList(WebDriver driver){
		String dateFormat = "MM/dd/yyyy hh:mm a";
		String rowDayFormat = "MMM d";
		String rowHourFormat = "h:mm a";
		
		List<Date> callsList = new ArrayList<Date>();
		
		List<WebElement> resultList = getInactiveElements(driver, searchResultRows);
		for (int i = 0; i < resultList.size(); i++) {																//loop for records rows
			WebElement reusltEntry = findElement(driver, 
					By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(i + 1))));
			
			//expands the conversation AI entry
			clickByJs(driver, reusltEntry);
			waitUntilVisible(driver, openedCallEntry);
			
			//get the list from the conversation AI detail and replace a.m. p.m. to match with the parse format
			String callDateString = getConverstaionCallDate(driver).replace("a.m.", "AM").replace("p.m.", "PM");
			Date callDate = HelperFunctions.parseStringToDateFormat(callDateString, dateFormat);
			
			String dayFormatDate = new SimpleDateFormat(rowDayFormat).format(callDate).toUpperCase();
			String hourFomratDate = new SimpleDateFormat(rowHourFormat).format(callDate).toUpperCase();
			
			//verifying that it is same outside on heading row as well
			String callDateOnHeaderRow = reusltEntry.findElement(searchRowsCallDate).getText();
			assertTrue(callDateOnHeaderRow.equals(dayFormatDate) || callDateOnHeaderRow.equals(hourFomratDate), "Call Date = : " + callDateOnHeaderRow + "Hour Date = : " + hourFomratDate + "Day Date = : " + dayFormatDate);
			
			callsList.add(callDate);
			
			reusltEntry.click();
			waitUntilInvisible(driver, agenDetailResult);
		}

		return callsList;
	}
	
	/**enter text and find in CAI result row
	 * @param driver
	 * @param text
	 * @param index
	 * @return
	 */
	public String getTextToFindInCAIRow(WebDriver driver, String text, int index) {
		WebElement reusltEntry = findElement(driver, 
				By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index + 1))));
		
		By textLoc = By.xpath("../..//span[contains(text(), '$text$')]".replace("$text$", text));
		waitUntilVisible(driver, reusltEntry.findElement(textLoc));
		return reusltEntry.findElement(textLoc).getText();
	}
	
	/**get result match text in CAI result acc to index
	 * @param driver
	 * @param index
	 * @return
	 */
	public String getResultMatchTextCAIRow(WebDriver driver, int index) {
		WebElement reusltEntry = findElement(driver, 
				By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index + 1))));
		
		By textLoc = By.xpath("../..//span[contains(text(), 'Supervisor Notes') or contains(text(), 'Call Notes')  or contains(text(), 'Annotations')]");
		return reusltEntry.findElement(textLoc).getText(); 
	}
	
	/**
	 * use this method to expand Searched CAI row.
	 * @param driver
	 * @param index index in integer of the row which needs to to be expanded
	 */
	public void expandSearchRows(WebDriver driver, int index) {
		clickByJs(driver, By.xpath(searchResultRow.replace("$$Index$$", String.valueOf(index))));
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, openedCallEntry);
	}
	
	/**click library acc to index
	 * @param driver
	 * @param index
	 * @return
	 */
	public String clickLibrary(WebDriver driver, int index) {
		String library = getElements(driver, librariesResult).get(index).getText();
		getElements(driver, librariesResult).get(index).click();
		return library;
	}
	
	/**
	 * Use this method to hover over the prospect Name's cloud icon and verify it's color changes
	 * Click the cloud icon and verify that it is opened properly in SFDC
	 * @param driver
	 */
	public void verifyProspectAgentInSFDC(WebDriver driver) {
		WebElement prospectNameCloudIcon = findElement(driver, prospectNameResult).findElement(cloudIcon);
		String prospectName = getElementsText(driver, prospectNameResult);
		
		//Hover over the cloud icon and verify it's color changes to blue
		hoverElement(driver, prospectNameCloudIcon);
		assertEquals(prospectNameCloudIcon.getAttribute("fill"), "#139CD8");
		
		//click the element and verify that the same agent detail is opened in sfdc
		clickElement(driver, prospectNameCloudIcon);
		switchToTab(driver, getTabCount(driver));
		assertEquals(contactDetailPage.getHeaderRowName(driver), prospectName);
		
		closeTab(driver);
		switchToTab(driver, getTabCount(driver));
	}
	
	/**
	 * Use this method to hover over the prospect Company's cloud icon and verify it's color changes
	 * Click the cloud icon and verify that it is opened properly in SFDC
	 * @param driver
	 */
	public void verifyProspectCompanyInSFDC(WebDriver driver) {
		WebElement prospectCompanyCloudIcon = findElement(driver, prospectCompanyResult).findElement(cloudIcon);
		String prospectCompanyName = getElementsText(driver, prospectCompanyResult);
		
		//Hover over the cloud icon and verify it's color changes to blue
		hoverElement(driver, prospectCompanyCloudIcon);
		assertEquals(prospectCompanyCloudIcon.getAttribute("fill"), "#139CD8");
		
		//click the element and verify that the same company detail is opened in sfdc
		clickElement(driver, prospectCompanyCloudIcon);
		switchToTab(driver, getTabCount(driver));
		sfAccountPage.verifyAccountsNameHeading(driver, prospectCompanyName);
		
		closeTab(driver);
		switchToTab(driver, getTabCount(driver));
	}
	
	/**verify tool tip of view conversation icon
	 * @param driver
	 * @param index TODO
	 */
	public void verifyToolTipViewConversation(WebDriver driver, int index) {
		WebElement conversationViewElement = getElements(driver, openConverstaionDetailIcon).get(index);
		assertFalse(isAttributePresent(driver, conversationViewElement, ElementAttributes.AriaDescribedBy.displayName()));
		hoverElement(driver, conversationViewElement);
		assertTrue(isAttributePresent(driver, conversationViewElement, ElementAttributes.AriaDescribedBy.displayName()));
	}
	 
	 /**
	  * use to get the total number of pages
	 * @param driver
	 * @return
	 */
	public int getTotalPage(WebDriver driver) {
		waitTillProgressCircleInvisible(driver);
		waitUntilVisible(driver, totalPage);
		String totalPageCount = getElementsText(driver, totalPage);
		return Integer.parseInt(totalPageCount.substring(totalPageCount.indexOf(" of ") + 4));
	}
	
	/**
	 * use to get the starting page number
	 * 
	 * @param driver
	 * @return
	 */
	public int getStartingPageNumber(WebDriver driver) {
		waitTillProgressCircleInvisible(driver);
		String totalPageCount = getTotalPageText(driver);
		return Integer.parseInt(HelperFunctions.getValueAccToRegex(totalPageCount, "[0-9]"));
	}
	 
	/**
	 * use to get the total page text
	 * 
	 * @param driver
	 * @return
	 */
	public String getTotalPageText(WebDriver driver) {
		waitUntilVisible(driver, totalPage);
		return getElementsText(driver, totalPage);
	}
	
	/**
	 * use this method to navigate to next page
	 * @param driver
	 * @param page
	 * @param index
	 */
	public void navigateToNextCAIPage(WebDriver driver, int page, int index) {
		if (index < page - 1) {
			waitTillProgressCircleInvisible(driver);
			waitUntilVisible(driver, nextPageBtn);
			clickByJs(driver, nextPageBtn);
			waitUntilVisible(driver, previousPageBtn);
			dashboard.isPaceBarInvisible(driver);
			waitForPageLoaded(driver);
		}
	}

	/**
	 * Retries first two words from search list i.e, caller name
	 * @param driver
	 * @return
	 */
	public String getCallerNameFromDurationSubjectList(WebDriver driver) {
		return findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", "1"))).findElement(calledAgent).getText();
	}
	
	/**
	 * Retrieves last two words from search list i.e, receiver name
	 * @param driver
	 * @return
	 */
	public String getReceiverNameFromDurationSubjectList(WebDriver driver) {
		String receviverName = findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", "1"))).findElement(prospectDetailsHeading).getText(); 
		if(receviverName.endsWith("Unknown Caller")) {
			return "Unknown Caller";
		}
		return findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", "1"))).findElement(ProspectNameHeading).getText();
	}
	
	/**
	 * get account name from subject list 
	 * @param driver
	 * @return
	 */
	public String getAccountNameFromDurationSubjectList(WebDriver driver) {
		return findElement(driver, By.xpath(searchResultRow.replace("$$Index$$", "1"))).findElement(ProspectCompanyHeading).getText();
	}
	
	/**get call duration from search list
	 * @param driver
	 * @return
	 */
	public String getCallDurationFromSearchList(WebDriver driver) {
		isListElementsVisible(driver, searchResultRows, 5);
		return getTextListFromElements(driver, searchResultRows).get(0);
	}
	
	/**get notification text acc to row
	 * @param driver
	 * @param index
	 * @return
	 */
	public String getNotificationTextAccToRow(WebDriver driver, int index) {
		isListElementsVisible(driver, searchResultsNotificationRow, 5);
		return getElementsText(driver, getInactiveElements(driver, searchResultsNotificationRow).get(index)).trim();
	}

	/**verify prospect details result
	 * @param driver
	 * @param tokenList
	 */
	public void verifyProspectDetailsResult(WebDriver driver, List<String> tokenList) {
		assertTrue(assertNonTagLibraryFilter(driver, tokenList, agenDetailResult));
		assertTrue(assertNonTagLibraryFilter(driver, tokenList, prospectNameResult));
		assertTrue(assertNonTagLibraryFilter(driver, tokenList, prospectCompanyResult));
		assertTrue(assertNonTagLibraryFilter(driver, tokenList, prospectTitleResult));
		assertNonTagLibraryFilter(driver, tokenList, dispositionResult);
	}

	/**get agent call disposition search row
	 * @param driver
	 * @return
	 */
	public String getAgentCallDispositionSearchRow(WebDriver driver) {
		waitUntilVisible(driver, agentCallDispostion);
		return getElementsText(driver, agentCallDispostion);
	}
}
