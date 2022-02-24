package support.source.conversationAIReact;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class InboxPage extends SeleniumBase{

	Dashboard dashBoard = new Dashboard();
	CallsTabReactPage callsTabReactPage = new CallsTabReactPage();
	
	By inboxLink 			= By.cssSelector(".rdnaLink[href='/#smart-recordings/inbox'], [href='/inbox']");
	By inboxCount			= By.cssSelector(".rdnaLink[href='/#smart-recordings/inbox'] span, [href='/inbox'] div.badge-container span");
	By sortFilterTab		= By.cssSelector("[data-testid = 'Sort-input'] #ringdna-select");
	By typeFilterTab		= By.cssSelector("[data-testid = 'Type-input'] #ringdna-select");
	By typeFilerTabDefault  = By.xpath(".//*[@for='Type-select' and text()='Filter by Type']");
	By listBoxItems			= By.cssSelector("ul[role='listbox'] li");
	By listBoxItemsSelected = By.cssSelector("ul[role='listbox'] li.Mui-selected");
	By menuBoxItems			= By.cssSelector("ul[role='menu'] li");
	
	By totalPage 			= By.xpath(".//span[contains(text(), 'Page')]");
	
	By clearFilterButton    = By.cssSelector("button[name='buttons-container-reset']");
	
	By notificationTextList  	  = By.xpath("//div[contains(text(),'a call')]");
	By sharednotificationTextList = By.xpath("//div[contains(text(),'Search')]");
	By dateInboxList 	 = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/preceding-sibling::span");
	By dateInboxListTodays   = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/preceding-sibling::span[contains(text(),'PM') or contains(text(),'AM')]");
	By dateInboxListPrevious = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/preceding-sibling::span[not(contains(text(), 'PM'))]");
	
	//filter by type drop down outer htmls
	String saveSearchFilterDropDownOuterHTML = "<svg width=\"14\" height=\"14.933333333333334\" viewBox=\"0 0 15 16\" fill=\"none\"><circle cx=\"6\" cy=\"6\" r=\"5\" stroke=\"#FFFFFF\"></circle><path d=\"M6 4v4M8 6H4\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M10.5 11.5L14 15\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path></svg>";
	String sharedCallFilterDropDownOuterHTML = "<svg width=\"13\" height=\"11.142857142857142\" viewBox=\"0 0 14 12\" fill=\"none\"><circle cx=\"12\" cy=\"2\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><circle cx=\"2\" cy=\"6\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><circle cx=\"12\" cy=\"10\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><path d=\"M12 2L2 6L12 10\" stroke=\"#FFFFFF\"></path></svg>";
	String superVisorNotesDropDownOuterHTML  = "<svg width=\"14\" height=\"16\" viewBox=\"0 0 14 16\" fill=\"none\"><path d=\"M3 11v3.7a.3.3 0 00.3.3h9.4a.3.3 0 00.3-.3V4.152a.3.3 0 00-.087-.21l-2.825-2.853A.3.3 0 009.875 1H3.3a.3.3 0 00-.3.3v1.34\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M2.735 4.503a.3.3 0 01.53 0l.583 1.104a.3.3 0 00.214.156l1.23.213a.3.3 0 01.165.505l-.87.895a.3.3 0 00-.082.252l.177 1.236a.3.3 0 01-.429.312l-1.12-.55a.3.3 0 00-.265 0l-1.121.55a.3.3 0 01-.43-.312l.178-1.236a.3.3 0 00-.081-.252l-.87-.895a.3.3 0 01.163-.505l1.23-.213a.3.3 0 00.215-.156l.583-1.104z\" fill=\"#FFFFFF\"></path></svg>";
	String annotationDropDownOuterHTML		 = "<svg width=\"17\" height=\"14.166666666666668\" viewBox=\"0 0 18 15\" fill=\"none\"><path clip-rule=\"evenodd\" d=\"M6.176 1h5.647a5.176 5.176 0 110 10.353l-2.352 2.353v-2.353H6.176A5.176 5.176 0 116.176 1z\" stroke=\"#FFFFFF\" stroke-linejoin=\"round\"></path><path d=\"M9 4v4M11 6H7\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String flaggedCallsDropDownOuterHTML	 = "<svg width=\"13\" height=\"14\" viewBox=\"0 0 13 14\" fill=\"none\"><path d=\"M1 2v10.7a.3.3 0 00.3.3h1.4a.3.3 0 00.3-.3V8m0 0V2.195a.3.3 0 01.178-.274l.041-.018a5 5 0 014.062 0L7.5 2l.387.172a4.587 4.587 0 003.726 0v0a.275.275 0 01.387.252v5.381a.3.3 0 01-.178.274l-.041.018a5 5 0 01-4.062 0L7.5 8l-.22-.097a5 5 0 00-4.06 0L3 8z\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	
	//filter list results outer htmls
	String saveSearchReadListOuterHTML	  	   = "<svg width=\"15.600000000000001\" height=\"16.64\" viewBox=\"0 0 15 16\" fill=\"none\"><circle cx=\"6\" cy=\"6\" r=\"5\" stroke=\"#99C2FF\"></circle><path d=\"M6 4v4M8 6H4\" stroke=\"#99C2FF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M10.5 11.5L14 15\" stroke=\"#99C2FF\" stroke-linecap=\"round\"></path></svg>";
	String saveSearchUnreadListOuterHTML	   = "<svg width=\"15.600000000000001\" height=\"16.64\" viewBox=\"0 0 15 16\" fill=\"none\"><circle cx=\"6\" cy=\"6\" r=\"5\" stroke=\"#FFFFFF\"></circle><path d=\"M6 4v4M8 6H4\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M10.5 11.5L14 15\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path></svg>";
	String sharedCallReadListOuterHTML	  	   = "<svg width=\"15.600000000000001\" height=\"13.371428571428572\" viewBox=\"0 0 14 12\" fill=\"none\"><circle cx=\"12\" cy=\"2\" r=\"1.5\" fill=\"#3385FF\" stroke=\"#3385FF\"></circle><circle cx=\"2\" cy=\"6\" r=\"1.5\" fill=\"#3385FF\" stroke=\"#3385FF\"></circle><circle cx=\"12\" cy=\"10\" r=\"1.5\" fill=\"#3385FF\" stroke=\"#3385FF\"></circle><path d=\"M12 2L2 6L12 10\" stroke=\"#3385FF\"></path></svg>";
	String sharedCallsUnreadListOuterHTML	   = "<svg width=\"15.600000000000001\" height=\"13.371428571428572\" viewBox=\"0 0 14 12\" fill=\"none\"><circle cx=\"12\" cy=\"2\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><circle cx=\"2\" cy=\"6\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><circle cx=\"12\" cy=\"10\" r=\"1.5\" fill=\"#FFFFFF\" stroke=\"#FFFFFF\"></circle><path d=\"M12 2L2 6L12 10\" stroke=\"#FFFFFF\"></path></svg>";
	String superVisorNotesReadListOuterHTML    = "<svg width=\"15.600000000000001\" height=\"17.82857142857143\" viewBox=\"0 0 14 16\" fill=\"none\"><path d=\"M3 11v3.7a.3.3 0 00.3.3h9.4a.3.3 0 00.3-.3V4.152a.3.3 0 00-.087-.21l-2.825-2.853A.3.3 0 009.875 1H3.3a.3.3 0 00-.3.3v1.34\" stroke=\"#002966\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M2.735 4.503a.3.3 0 01.53 0l.583 1.104a.3.3 0 00.214.156l1.23.213a.3.3 0 01.165.505l-.87.895a.3.3 0 00-.082.252l.177 1.236a.3.3 0 01-.429.312l-1.12-.55a.3.3 0 00-.265 0l-1.121.55a.3.3 0 01-.43-.312l.178-1.236a.3.3 0 00-.081-.252l-.87-.895a.3.3 0 01.163-.505l1.23-.213a.3.3 0 00.215-.156l.583-1.104z\" fill=\"#002966\"></path></svg>";
	String superVisorNotesUnreadListOuterHTML  = "<svg width=\"15.600000000000001\" height=\"17.82857142857143\" viewBox=\"0 0 14 16\" fill=\"none\"><path d=\"M3 11v3.7a.3.3 0 00.3.3h9.4a.3.3 0 00.3-.3V4.152a.3.3 0 00-.087-.21l-2.825-2.853A.3.3 0 009.875 1H3.3a.3.3 0 00-.3.3v1.34\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path><path d=\"M2.735 4.503a.3.3 0 01.53 0l.583 1.104a.3.3 0 00.214.156l1.23.213a.3.3 0 01.165.505l-.87.895a.3.3 0 00-.082.252l.177 1.236a.3.3 0 01-.429.312l-1.12-.55a.3.3 0 00-.265 0l-1.121.55a.3.3 0 01-.43-.312l.178-1.236a.3.3 0 00-.081-.252l-.87-.895a.3.3 0 01.163-.505l1.23-.213a.3.3 0 00.215-.156l.583-1.104z\" fill=\"#FFFFFF\"></path></svg>";
	String annotationReadListOuterHTML  	   = "<svg width=\"15.600000000000001\" height=\"13.000000000000002\" viewBox=\"0 0 18 15\" fill=\"none\"><path clip-rule=\"evenodd\" d=\"M6.176 1h5.647a5.176 5.176 0 110 10.353l-2.352 2.353v-2.353H6.176A5.176 5.176 0 116.176 1z\" stroke=\"#0052CC\" stroke-linejoin=\"round\"></path><path d=\"M9 4v4M11 6H7\" stroke=\"#0052CC\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String annotationUnreadListOuterHTML 	   = "<svg width=\"15.600000000000001\" height=\"13.000000000000002\" viewBox=\"0 0 18 15\" fill=\"none\"><path clip-rule=\"evenodd\" d=\"M6.176 1h5.647a5.176 5.176 0 110 10.353l-2.352 2.353v-2.353H6.176A5.176 5.176 0 116.176 1z\" stroke=\"#FFFFFF\" stroke-linejoin=\"round\"></path><path d=\"M9 4v4M11 6H7\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String flaggedCallsUnreadListOuterHTML	   = "<svg width=\"15.600000000000001\" height=\"16.8\" viewBox=\"0 0 13 14\" fill=\"none\"><path d=\"M1 2v10.7a.3.3 0 00.3.3h1.4a.3.3 0 00.3-.3V8m0 0V2.195a.3.3 0 01.178-.274l.041-.018a5 5 0 014.062 0L7.5 2l.387.172a4.587 4.587 0 003.726 0v0a.275.275 0 01.387.252v5.381a.3.3 0 01-.178.274l-.041.018a5 5 0 01-4.062 0L7.5 8l-.22-.097a5 5 0 00-4.06 0L3 8z\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String flaggedCallsReadListOuterHTML	   = "<svg width=\"15.600000000000001\" height=\"16.8\" viewBox=\"0 0 13 14\" fill=\"none\"><path d=\"M1 2v10.7a.3.3 0 00.3.3h1.4a.3.3 0 00.3-.3V8m0 0V2.195a.3.3 0 01.178-.274l.041-.018a5 5 0 014.062 0L7.5 2l.387.172a4.587 4.587 0 003.726 0v0a.275.275 0 01.387.252v5.381a.3.3 0 01-.178.274l-.041.018a5 5 0 01-4.062 0L7.5 8l-.22-.097a5 5 0 00-4.06 0L3 8z\" stroke=\"#FF9933\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	
	//filter by type drop down image
	By saveSearchFilterDropDownImage 	  = By.xpath(".//*[@data-value='SearchNotification']//*[local-name()='svg']");
	By sharedCallFilterDropDownImage	  = By.xpath(".//*[@data-value='Share']//*[local-name()='svg']");
	By superVisorNotesFilterDropDownImage = By.xpath(".//*[@data-value='SupervisorNote']//*[local-name()='svg']");
	By annotationFilterDropDownImage 	  = By.xpath(".//*[@data-value='Annotation']//*[local-name()='svg']");
	By flaggedCallsFilterDropDownImage 	  = By.xpath(".//*[@data-value='CallReview']//*[local-name()='svg']");
	
	By filterTypeListImages				  = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//div[@shape='square']/*[local-name()='svg' and not(@class)]");
	By unReadImageList					  = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//div[@shape='square']/*[local-name()='svg' and not(@class)]/*[local-name()='path' and (@stroke='#FFFFFF')][1]");
	By readImageList					  = By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//div[@shape='square']/*[local-name()='svg' and not(@class)]/*[local-name()='path' and not(@stroke='#FFFFFF') and not(@fill='#FFFFFF')][1]");
	
	String selectedFilterByType          = ".//*[contains(@class,'MuiChip-outlined')]/span[text()='Type: $typeFilter$']";
	String deleteIconSelectedFilterType  = ".//*[contains(@class,'MuiChip-outlined')]/span[text()='Type: $typeFilter$']/parent::div/*[contains(@class,'MuiChip-deleteIcon')]";
	
	String selectedFilterByUser      	 = ".//*[contains(@class,'MuiChip-outlined')]/span[text()='User: $userFilter$']";
	String deleteIconSelectedFilterUser  = ".//*[contains(@class,'MuiChip-outlined')]/span[text()='User: $userFilter$']/parent::div/*[contains(@class,'MuiChip-deleteIcon')]";
	
	By userNameInputDefault		= By.cssSelector("[for='User']#User-label");
	By userNameInput			= By.cssSelector("[data-testid = 'autocomplete-User-input'] #User");
	By userAutoComplete			= By.cssSelector("div[data-testid='autocomplete-User-input']");
	By userAutoCompleteInput	= By.cssSelector("div[data-testid='autocomplete-User-input'] input");
	By userListPopup			= By.id("User-popup");
	By userNameOption			= By.xpath(".//*[contains(@id, 'User-option')]");
	By userNameFirstOption		= By.id("User-option-0");

	By startTypingpopUp			= By.xpath(".//*[contains(@class,'MuiAutocomplete-popper')]//div[text()='Start typing...']");
	By userCrossIcon			= By.xpath(".//*[@id='User']/..//button[@title='Clear']");
	
	By prospectDetailsCloudIconList = By.xpath(".//*[text()='Prospect Detail']/..//a[not(contains(@href,'#'))]//*[local-name()='path']");
	By tagsList						= By.xpath(".//span[text()='Tags']/..//div[contains(@class,'MuiChip-outlined MuiChip-sizeSmall MuiChip-clickable')]");
	By libraryList					= By.xpath(".//span[text()='Libraries']/..//div[contains(@class,'MuiChip-outlined MuiChip-sizeSmall MuiChip-clickable')]");
	By extensionDialIconList		= By.cssSelector("img.ringdna-phone-icon");
	By prospectDetailsNameAccList	= By.xpath(".//*[text()='Prospect Detail']/..//a[not(contains(@href,'#'))]/../span");
	By prospectDetailsNumberLink	= By.xpath(".//*[text()='Prospect Detail']/..//a[(@data-phone)]");
	By callerInformationUnavailable = By.xpath(".//*[contains(@class, 'MuiGrid-item ')]/span[text()='Caller information unavailable.']");
	
	By agentDetailsName				= By.xpath(".//*[text()='Agent Detail']/..//div/span");
	By agentDetailsDateTime			= By.xpath(".//*[text()='Agent Detail']/..//div[contains(text(), 'p.m.') or contains(text(), 'a.m.')]");
	By agentDetailsDuration			= By.xpath(".//*[text()='Agent Detail']/..//div[contains(text(), 'Duration:')]");
	
	By superVisorNotesButton		= By.cssSelector("a[aria-label='Supervisor Notes'] svg");
	By callNotesButton			    = By.cssSelector("a[aria-label='Call Notes'] svg");
	By annotationBtn				= By.cssSelector("a[aria-label='Annotation'] svg");
	By annotationSize				= By.xpath("//a[@aria-label='Annotation']//div[contains(@class, 'annotation-size')]");
	String topicThumbsIconOuterHTML = "span[text()='Tags']/..//span[contains(@class, 'MuiChip-labelSmall')]//span[text()='$topic$']/../../*[local-name()='svg']";
	
	String annotateThumbsUpOuterHTML	= "<svg width=\"13\" height=\"12\" viewBox=\"0 0 13 12\" fill=\"none\"><path d=\"M0.5 6.5498C0.5 6.41173 0.611928 6.2998 0.75 6.2998L2.25 6.29981C2.38807 6.29981 2.5 6.41173 2.5 6.54981L2.5 11.0498C2.5 11.1879 2.38807 11.2998 2.25 11.2998L0.75 11.2998C0.611929 11.2998 0.5 11.1879 0.5 11.0498L0.5 6.5498Z\" fill=\"#33CC99\" stroke=\"#33CC99\"></path><path d=\"M4.5 10.5996C4.5 10.8757 4.72386 11.0996 5 11.0996L10.0909 11.0996C10.2957 11.0996 10.4798 10.9747 10.5555 10.7844L12.1922 6.66918C12.4534 6.01258 11.9697 5.29961 11.263 5.29961L8.30019 5.29961L8.30019 2.09961C8.30019 1.54733 7.85248 1.09961 7.30019 1.09961L6.94476 1.09961C6.52983 1.09961 6.15801 1.35584 6.01028 1.74358L4.69658 5.19163C4.56662 5.53273 4.5 5.8947 4.5 6.25972L4.5 10.5996Z\" fill=\"#33CC99\" stroke=\"#33CC99\" stroke-linejoin=\"round\"></path></svg>";
	String annotateThumbsDownOuterHTML	= "<svg width=\"13\" height=\"12\" viewBox=\"0 0 13 12\" fill=\"none\"><path d=\"M12.5 5.84961C12.5 5.98768 12.3881 6.09961 12.25 6.09961H10.75C10.6119 6.09961 10.5 5.98768 10.5 5.84961L10.5 1.34961C10.5 1.21154 10.6119 1.09961 10.75 1.09961H12.25C12.3881 1.09961 12.5 1.21154 12.5 1.34961L12.5 5.84961Z\" fill=\"#FF9933\" stroke=\"#FF9933\"></path><path d=\"M8.5 1.7998C8.5 1.52366 8.27614 1.2998 8 1.2998H2.90908C2.70427 1.2998 2.52017 1.42471 2.44448 1.61502L0.807755 5.73024C0.546608 6.38684 1.03034 7.0998 1.73696 7.0998L4.6998 7.0998L4.6998 10.2998C4.6998 10.8521 5.14752 11.2998 5.6998 11.2998H6.05524C6.47017 11.2998 6.84199 11.0436 6.98972 10.6558L8.30342 7.20778C8.43338 6.86668 8.5 6.50471 8.5 6.13969V1.7998Z\" fill=\"#FF9933\" stroke=\"#FF9933\" stroke-linejoin=\"round\"></path></svg>";

	//Bulk Management locators

	By menuTriggerIcon			= By.xpath(".//*[contains(@class, 'MuiCheckbox')]/../..//div[@aria-label='rdna-menu-trigger']");
	By menuTriggerCheckBox 		= By.xpath(".//div[@aria-label='rdna-menu-trigger']/../..//input[@type='checkbox']/parent::span");
	
	By notificationCheckBoxesList		= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/parent::div/parent::div//input[@type='checkbox']/parent::span");
	By notificationCheckBoxesParentList	= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/parent::div/parent::div//input[@type='checkbox']/parent::span/parent::span[contains(@class,'MuiCheckbox-root')]");
	
	By readCheckBoxesList			= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//*[local-name()='svg' and not(@class)]/*[local-name()='path' and not(@stroke='#FFFFFF') and not(@fill='#FFFFFF')][1]/ancestor::button/..//input[@type='checkbox']/parent::span");
	By readCheckBoxesParentList		= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//*[local-name()='svg' and not(@class)]/*[local-name()='path' and not(@stroke='#FFFFFF') and not(@fill='#FFFFFF')][1]/ancestor::button/..//span[contains(@class,'MuiCheckbox-root')]");
	By unReadCheckBoxesList			= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//*[local-name()='svg' and not(@class)]/*[local-name()='path' and (@stroke='#FFFFFF')][1]/ancestor::button/..//input[@type='checkbox']/parent::span");
	By unReadCheckBoxesParentList	= By.xpath(".//button[@title='View Conversation' or @title='View Saved Search']/../..//*[local-name()='svg' and not(@class)]/*[local-name()='path' and (@stroke='#FFFFFF')][1]/ancestor::button/..//span[contains(@class,'MuiCheckbox-root')]");
	
	By deleteInboxBtn			 = By.cssSelector("[name='delete-inbox']");
	By multipleDeleteItemHeader	 = By.xpath(".//*[@role='dialog']//h4[text()='Delete Inbox Items?']");
	By multipleDeleteItemBody	 = By.xpath(".//*[@role='dialog']//span[text()='Are you sure you want to delete these inbox items? This action cannot be undone.']");
	
	By singleDeleteItemHeader	 = By.xpath(".//*[@role='dialog']//h4[text()='Delete Inbox Item?']");
	By singleDeleteItemBody	 	 = By.xpath(".//*[@role='dialog']//span[text()='Are you sure you want to delete this item? This action cannot be undone.']");
	
	By confirmBtn				= By.cssSelector("[data-testid='ui-dialog.primary-btn']");
	By cancelBtn				= By.cssSelector("[data-testid='ui-dialog.secondary-btn']");
	By markAsReadBtn			= By.cssSelector("[name='mark-as-read']");
	By markAsUnreadBtn			= By.cssSelector("[name='mark-as-unread']");
	
	private static final String dateFormatToday    = "h:mm a";
	private static final String dateFormatPrevious = "MMM dd";
	
	public static enum SortInput {
		AgentNameAtoZ("Agent Name (A-Z)"),
		AgentNameZtoA("Agent Name (Z-A)"),
		OldestToNewest("Oldest to Newest"),
		NewestToOldest("Newest to Oldest"),
		LongestToShortest("Longest to Shortest"),
		ShortestToLongest("Shortest to Longest");

		private String value;

		SortInput(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum FilterType {
		FlaggedCalls("Flagged Calls"),
		Annotation("Annotation"),
		SavedSearch("Saved Search"),
		SharedCalls("Shared Calls"),
		SupervisorNotes("Supervisor Notes");

		private String value;

		FilterType(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum MenuTriggerOptions {

		All, None, Read, Unread;
	}
	
	public static enum SelectNotifications {
		Multiple, Single;
	}
	
	public void navigateToInboxTab(WebDriver driver) {
		waitUntilVisible(driver, inboxLink);
		clickElement(driver, inboxLink);
		dashBoard.isPaceBarInvisible(driver);
		waitForPageLoaded(driver);
	}
	
	public int getInboxCount(WebDriver driver) {
		waitUntilVisible(driver, inboxCount);
		scrollToElement(driver, inboxCount);
		String countText = getElementsText(driver, inboxCount);
		return Integer.parseInt(countText);
	}
	
	public void verifyDefaultFilters(WebDriver driver) {
		waitUntilVisible(driver, sortFilterTab);
		assertEquals(getElementsText(driver, sortFilterTab), SortInput.NewestToOldest.getValue());
		waitUntilVisible(driver, typeFilerTabDefault);
		waitUntilVisible(driver, userNameInputDefault);
		assertEquals(getElementsText(driver, userNameInputDefault), "Filter by User");
	}
	
	public void selectSortFilter(WebDriver driver, String sortText) {
		clickAndSelectFromDropDown(driver, sortFilterTab, listBoxItems, sortText);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public boolean verifyAllNotificationsContainsText(WebDriver driver, String text) {
		isListElementsVisible(driver, notificationTextList, 5);
		return verifyAllListItemsContains(driver, getTextListFromElements(driver, notificationTextList), text);
	}

	public void verifyNotificationTextListAscending(WebDriver driver) {
		List<String> notificationList = getTextListFromElements(driver, notificationTextList);
		List<String> newStringList = new ArrayList<>();

		// format for getting first two words
		String format = "\\w*\\s\\w*\\s";

		for (String text : notificationList) {
			newStringList.add(HelperFunctions.getValueAccToRegex(text, format));
		}
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(newStringList));
	}
	
	public void verifyNotificationTextListDescending(WebDriver driver) {
		List<String> notificationList = getTextListFromElements(driver, notificationTextList);
		List<String> newStringList = new ArrayList<>();

		// format for getting first two words
		String format = "\\w*\\s\\w*\\s";

		for (String text : notificationList) {
			newStringList.add(HelperFunctions.getValueAccToRegex(text, format));
		}
		assertTrue(HelperFunctions.verifyListDescendingCaseInsensitive(newStringList));
	}
	
	public void verifyUserDisplayedInNotifications(WebDriver driver, String userName) {
		List<String> notificationList = getTextListFromElements(driver, notificationTextList);

		// format for getting first two words
		String format = "\\w*\\s\\w*\\s";

		for (String text : notificationList) {
			assertEquals(HelperFunctions.getValueAccToRegex(text, format), userName);
		}
	}
	
	public String getNotificationTextInbox(WebDriver driver, int index) {
		isListElementsVisible(driver, notificationTextList, 5);
		return getTextListFromElements(driver, notificationTextList).get(index);
	}
	
	public String getSavedSearchNotificationTextInbox(WebDriver driver, int index) {
		isListElementsVisible(driver, sharednotificationTextList, 5);
		return getTextListFromElements(driver, sharednotificationTextList).get(index);
	}
	
	public void clickFirstNotificationTextList(WebDriver driver) {
		isListElementsVisible(driver, notificationTextList, 5);
		clickElement(driver, getElements(driver, notificationTextList).get(0));
	}
	
	public boolean selectTypeFilter(WebDriver driver, String typeFilter) {
		clickAndSelectFromDropDown(driver, typeFilterTab, listBoxItems, typeFilter);
		dashBoard.isPaceBarInvisible(driver);
		pressEscapeKey(driver);
		return isFilterTypesSelectedVisible(driver, typeFilter);
	}
	
	public String getFilterByTypeDropDownInputText(WebDriver driver) {
		waitUntilVisible(driver, typeFilterTab);
		return getElementsText(driver, typeFilterTab);
	}
	
	public List<String> getFilterByTypeDropDownSelectedValues(WebDriver driver) {
		List<String> list;
		waitUntilVisible(driver, typeFilterTab);
		clickElement(driver, typeFilterTab);
		list = getTextListFromElements(driver, listBoxItemsSelected);
		pressEscapeKey(driver);
		return list;
	}

	public boolean verifyFilterByTypeSelectedInDropDown(WebDriver driver, String filterByType) {
		return isTextPresentInStringList(driver, getFilterByTypeDropDownSelectedValues(driver), filterByType);
	}
	
	public void verifyColorHoverOnFilters(WebDriver driver, String typeFilter) {

		By selectedFilterLoc = By.xpath(selectedFilterByType.replace("$typeFilter$", typeFilter));

		waitUntilVisible(driver, selectedFilterLoc);
		idleWait(2);
		scrollToElement(driver, selectedFilterLoc);
		hoverElement(driver, selectedFilterLoc);
		
		String selectedFiltercolor = getCssValue(driver, findElement(driver, selectedFilterLoc), CssValues.Color);
		String selectedFilterHexColor = Color.fromString(selectedFiltercolor).asHex();
		assertEquals(selectedFilterHexColor, "#ac92f2");
	}
	
	public boolean isFilterTypesSelectedVisible(WebDriver driver, String typeFilter){

		//verifying filters selected and delete icon visible
		By selectedFilterLoc = By.xpath(selectedFilterByType.replace("$typeFilter$", typeFilter));
		By selectedFilterDeleteLoc = By.xpath(deleteIconSelectedFilterType.replace("$typeFilter$", typeFilter));

		return isElementVisible(driver, selectedFilterLoc, 5) && isElementVisible(driver, selectedFilterDeleteLoc, 5);
	}
	
	public void verifyAllNotificationListContainsValue(WebDriver driver, String value) {
		isListElementsVisible(driver, notificationTextList, 5);
		assertTrue(verifyAllListItemsContains(driver, getTextListFromElements(driver, notificationTextList), value));
	}
	
	public void clearTypeFilter(WebDriver driver, String typeText) {
		By selectedFilterDeleteLoc = By.xpath(deleteIconSelectedFilterType.replace("$typeFilter$", typeText));
		clickElement(driver, selectedFilterDeleteLoc);
		assertFalse(isElementVisible(driver, selectedFilterDeleteLoc, 5));
	}

	public void verifyDateInboxListAscending(WebDriver driver) {
		if (isListElementsVisible(driver, dateInboxListTodays, 5)) {
			List<String> dateStringList = getTextListFromElements(driver, dateInboxListTodays);
			assertTrue(HelperFunctions.verifyAscendingOrderedList(
					HelperFunctions.getStringListInDateFormat(dateFormatToday, dateStringList)));
		} 
		else if (isListElementsVisible(driver, dateInboxListPrevious, 5)) {
			List<String> dateStringList = getTextListFromElements(driver, dateInboxListPrevious);
			assertTrue(HelperFunctions.verifyAscendingOrderedList(
					HelperFunctions.getStringListInDateFormat(dateFormatPrevious, dateStringList)));
		}
	}

	public void verifyDateInboxListDescending(WebDriver driver) {
		if (isListElementsVisible(driver, dateInboxListTodays, 5)) {
			List<String> dateStringList = getTextListFromElements(driver, dateInboxListTodays);
			assertTrue(HelperFunctions.verifyDescendingOrderedList(
					HelperFunctions.getStringListInDateFormat(dateFormatToday, dateStringList)));
		} 
		else if (isListElementsVisible(driver, dateInboxListPrevious, 5)) {
			List<String> dateStringList = getTextListFromElements(driver, dateInboxListPrevious);
			assertTrue(HelperFunctions.verifyDescendingOrderedList(
					HelperFunctions.getStringListInDateFormat(dateFormatPrevious, dateStringList)));
		}
	}
	
	public String getDateInboxList(WebDriver driver, int index) {
		isListElementsVisible(driver, dateInboxList, 5);
		return getTextListFromElements(driver, dateInboxList).get(index);
	}

	public boolean verifySaveSearchImageDropDown(WebDriver driver) {
		boolean value = false;
		clickElement(driver, typeFilterTab);
		value = getAttribue(driver, saveSearchFilterDropDownImage, ElementAttributes.outerHTML)
				.equals(saveSearchFilterDropDownOuterHTML);
		pressEscapeKey(driver);
		return value;
	}

	/**verify filter type image list visible acc to outer html
	 * @param driver
	 * @param filterType
	 * @return
	 */
	public boolean verifyFilterTypeImageListVisible(WebDriver driver, FilterType filterType) {
		
		String outerReadHtml = null;
		String outerUnreadHtml = null;
		
		switch (filterType) {
		case Annotation:
			outerReadHtml 	= annotationReadListOuterHTML;
			outerUnreadHtml = annotationUnreadListOuterHTML;
			break;
		case FlaggedCalls:
			outerReadHtml 	= flaggedCallsReadListOuterHTML;
			outerUnreadHtml = flaggedCallsUnreadListOuterHTML;
			break;
		case SavedSearch:
			outerReadHtml 	= saveSearchReadListOuterHTML;
			outerUnreadHtml = saveSearchUnreadListOuterHTML;
			break;
		case SharedCalls:
			outerReadHtml 	= sharedCallReadListOuterHTML;
			outerUnreadHtml = sharedCallsUnreadListOuterHTML;
			break;
		case SupervisorNotes:
			outerReadHtml 	= superVisorNotesReadListOuterHTML;
			outerUnreadHtml = superVisorNotesUnreadListOuterHTML;
			break;
		default:
			break;
		}
		
		boolean value = false;
		waitForPageLoaded(driver);
		isListElementsVisible(driver, filterTypeListImages, 5);
		List<WebElement> list = getInactiveElements(driver, filterTypeListImages);
		for(WebElement filterListElemnt: list) {
			value = getAttribue(driver, filterListElemnt, ElementAttributes.outerHTML).equals(outerReadHtml) 
					|| getAttribue(driver, filterListElemnt, ElementAttributes.outerHTML).equals(outerUnreadHtml);
		}
		return value;
		
	}
	
	public boolean verifySharedCallImageDropDown(WebDriver driver) {
		boolean value = false;
		clickElement(driver, typeFilterTab);
		value = getAttribue(driver, sharedCallFilterDropDownImage, ElementAttributes.outerHTML)
				.equals(sharedCallFilterDropDownOuterHTML);
		pressEscapeKey(driver);
		return value;
	}
	
	public void verifySharedCallUnReadImage(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(sharedCallsUnreadListOuterHTML));
	}

	public void verifySharedCallImageRead(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(sharedCallReadListOuterHTML));
	}

	public boolean verifySuperVisorNotesImageDropDown(WebDriver driver) {
		boolean value = false;
		clickElement(driver, typeFilterTab);
		value = getAttribue(driver, superVisorNotesFilterDropDownImage, ElementAttributes.outerHTML).equals(superVisorNotesDropDownOuterHTML);
		pressEscapeKey(driver);
		return value;
	}
	
	public void verifySupervisorNotesCallUnReadImage(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(superVisorNotesUnreadListOuterHTML));
	}

	public void verifySuperVisorNotesCallImageRead(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(superVisorNotesReadListOuterHTML));
	}
	
	public void verifyAllSuperVisorNotesCallImageRead(WebDriver driver) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		for(WebElement element: getElements(driver, filterTypeListImages)) {
			assertTrue(getAttribue(driver, element, ElementAttributes.outerHTML).equals(superVisorNotesReadListOuterHTML));
		}
	}

	public void verifyAllSuperVisorNotesCallImageUnRead(WebDriver driver) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		for(WebElement element: getElements(driver, filterTypeListImages)) {
			assertTrue(getAttribue(driver, element, ElementAttributes.outerHTML).equals(superVisorNotesUnreadListOuterHTML));
		}
	}

	public boolean verifyAnnotationImageDropDown(WebDriver driver) {
		boolean value = false;
		clickElement(driver, typeFilterTab);
		value = getAttribue(driver, annotationFilterDropDownImage, ElementAttributes.outerHTML)
				.equals(annotationDropDownOuterHTML);
		pressEscapeKey(driver);
		return value;
	}
	
	public void verifyAnnotationUnReadImage(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(annotationUnreadListOuterHTML));
	}

	public void verifyAnnotationImageRead(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(annotationReadListOuterHTML));
	}

	public boolean verifyFlaggedCallsImageDropDown(WebDriver driver) {
		boolean value = false;
		clickElement(driver, typeFilterTab);
		value = getAttribue(driver, flaggedCallsFilterDropDownImage, ElementAttributes.outerHTML)
				.equals(flaggedCallsDropDownOuterHTML);
		pressEscapeKey(driver);
		return value;
	}
	
	public void verifyFlaggedCallUnReadImage(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(flaggedCallsUnreadListOuterHTML));
	}

	public void verifyFlaggedCallImageRead(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertTrue(getAttribue(driver, getElements(driver, filterTypeListImages).get(index), ElementAttributes.outerHTML).equals(flaggedCallsReadListOuterHTML));
	}

	public void clearUserNameFilter(WebDriver driver) {
		clickByJs(driver, userNameInput);
		clearAll(driver, userNameInput);
		dashBoard.isPaceBarInvisible(driver);
	}

	public void selectFilterByUser(WebDriver driver, String userName) {
		waitUntilVisible(driver, userNameInput);
		clearUserNameFilter(driver);
		enterText(driver, userNameInput, userName);
		idleWait(2);
		waitUntilVisible(driver, userAutoComplete);
		clickElement(driver, userAutoComplete);
		waitUntilVisible(driver, userAutoCompleteInput);
		clickElement(driver, userAutoCompleteInput);
		waitUntilVisible(driver, userListPopup);
		clickElement(driver, userNameFirstOption);
		waitUntilVisible(driver, startTypingpopUp);
		
		dashBoard.isPaceBarInvisible(driver);
		
		//verifying filters selected
		assertTrue(isFilterByUserSelectedVisible(driver, userName));
	}
	
	public boolean isFilterByUserSelectedVisible(WebDriver driver, String userName){

		//verifying filters selected and delete icon visible
		By selectedFilterLoc = By.xpath(selectedFilterByUser.replace("$userFilter$", userName));
		By selectedFilterDeleteLoc = By.xpath(deleteIconSelectedFilterUser.replace("$userFilter$", userName));

		return isElementVisible(driver, selectedFilterLoc, 5) && isElementVisible(driver, selectedFilterDeleteLoc, 5);
	}
	
	public void deleteSelectedFilterUser(WebDriver driver, String userName) {
		By selectedFilterDeleteLoc = By.xpath(deleteIconSelectedFilterUser.replace("$userFilter$", userName));
		waitUntilVisible(driver, selectedFilterDeleteLoc);
		clickElement(driver, selectedFilterDeleteLoc);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public boolean verifyUserExistsInFilterUserDropDown(WebDriver driver, String agentName) {
		waitUntilVisible(driver, userNameInput);
		clearUserNameFilter(driver);
		enterText(driver, userNameInput, agentName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownAgents(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, agentName);
		}
	}
	
	public boolean verifyPartialUserNameContainsInFilterUserDropDown(WebDriver driver, String agentName) {
		waitUntilVisible(driver, userNameInput);
		clearUserNameFilter(driver);
		enterText(driver, userNameInput, agentName);
		idleWait(1);
		List<WebElement> dropDownList = getListDropDownAgents(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isAllWebElementsListContainsText(dropDownList, agentName);
		}
	}
	
	public boolean verifyUserDisplayedWithoutEnteringText(WebDriver driver, String agentName) {
		waitUntilVisible(driver, userNameInput);
		clickElement(driver, userNameInput);
		
		//get text Start Typing and verify
		
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownAgents(driver);
		if (dropDownList == null) {
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, agentName);
		}
	}
	
	public List<WebElement> getListDropDownAgents(WebDriver driver) {
		waitUntilVisible(driver, userAutoComplete);
		clickElement(driver, userAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, userAutoCompleteInput);
		clickElement(driver, userAutoCompleteInput);
		if (isElementVisible(driver, userListPopup, 5)) {
			return getElements(driver, userNameOption);
		} else {
			return null;
		}
	}
	
	// verify and click cloud icon of contact inbox
	public void verifyContactCloudIcon(WebDriver driver) {
		assertTrue(isElementVisible(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(0), 5));
		assertNotEquals(getAttribue(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(0), ElementAttributes.fill), "#139CD8");
		hoverElement(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(0));
		assertEquals(getAttribue(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(0), ElementAttributes.fill), "#139CD8");
		clickElement(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(0));
	}

	// verify and click cloud icon of account inbox
	public void verifyAccountCloudIcon(WebDriver driver) {
		assertTrue(isElementVisible(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(1), 5));
		assertNotEquals(getAttribue(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(1), ElementAttributes.fill), "#139CD8");
		hoverElement(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(1));
		assertEquals(getAttribue(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(1), ElementAttributes.fill), "#139CD8");
		clickElement(driver, getInactiveElements(driver, prospectDetailsCloudIconList).get(1));
	}
	
	public void verifyTagsVisible(WebDriver driver) {
		assertTrue(isListElementsVisible(driver, tagsList, 5));
	}
	
	public void verifyLibrariesVisible(WebDriver driver) {
		assertTrue(isListElementsVisible(driver, libraryList, 5));
	}
	
	public String clickLibraryInbox(WebDriver driver) {
		String library = getElements(driver, libraryList).get(0).getText();
		clickElement(driver, getElements(driver, libraryList).get(0));
		return library;
	}
	
	
	public void verifyDialExensionVisible(WebDriver driver) {
		isListElementsVisible(driver, extensionDialIconList, 5);
		assertTrue(isElementVisible(driver, getInactiveElements(driver, extensionDialIconList).get(0), 5));
	}
	
	public void clearInboxSection(WebDriver driver) {
		waitUntilVisible(driver, clearFilterButton);
		clickElement(driver, clearFilterButton);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public String getProspectDetailsName(WebDriver driver) {
		isListElementsVisible(driver, prospectDetailsNameAccList, 5);
		return getElementsText(driver, getElements(driver, prospectDetailsNameAccList).get(0));
	}
	
	public String getProspectDetailsAccount(WebDriver driver) {
		isListElementsVisible(driver, prospectDetailsNameAccList, 5);
		return getElementsText(driver, getElements(driver, prospectDetailsNameAccList).get(1));
	}
	
	public String getProspectDetailsNumber(WebDriver driver) {
		waitUntilVisible(driver, prospectDetailsNumberLink);
		return findElement(driver, prospectDetailsNumberLink).getAttribute("data-phone");
	}
	
	public void verifyCallerInformationUnavailable(WebDriver driver) {
		waitUntilVisible(driver, callerInformationUnavailable);
	}
	
	public void clickProspectDetailsNumber(WebDriver driver) {
		waitUntilVisible(driver, prospectDetailsNumberLink);
		clickElement(driver, prospectDetailsNumberLink);
	}

	public String getAgentDetailsName(WebDriver driver) {
		isListElementsVisible(driver, agentDetailsName, 5);
		return getElementsText(driver, getElements(driver, agentDetailsName).get(0));
	}
	
	public String getAgentDetailsDateTime(WebDriver driver) {
		isListElementsVisible(driver, agentDetailsDateTime, 5);
		String dateTime = getElementsText(driver, getElements(driver, agentDetailsDateTime).get(0));
		return dateTime;
	}
	
	public String getAgentDetailsDuration(WebDriver driver) {
		isListElementsVisible(driver, agentDetailsDuration, 5);
		return getElementsText(driver, getElements(driver, agentDetailsDuration).get(0));
	}
	
	public void verifySuperVisorNotesBtnHoverText_Enabled(WebDriver driver) {
		waitUntilVisible(driver, superVisorNotesButton);
		assertFalse(isAttributePresent(driver, findElement(driver, superVisorNotesButton).findElement(By.xpath("ancestor::a[@aria-label='Supervisor Notes']")), ElementAttributes.AriaDescribedBy.displayName()));
		hoverElement(driver, superVisorNotesButton);
		assertTrue(isAttributePresent(driver, findElement(driver, superVisorNotesButton).findElement(By.xpath("ancestor::a[@aria-label='Supervisor Notes']")), ElementAttributes.AriaDescribedBy.displayName()));
		assertTrue(findElement(driver, superVisorNotesButton).isEnabled());
	}
	
	public void verifyCallNotesBtnHoverText_Enabled(WebDriver driver) {
		waitUntilVisible(driver, callNotesButton);
		assertFalse(isAttributePresent(driver, findElement(driver, callNotesButton).findElement(By.xpath("ancestor::a[@aria-label='Call Notes']")), ElementAttributes.AriaDescribedBy.displayName()));
		hoverElement(driver, callNotesButton);
		assertTrue(isAttributePresent(driver, findElement(driver, callNotesButton).findElement(By.xpath("ancestor::a[@aria-label='Call Notes']")), ElementAttributes.AriaDescribedBy.displayName()));
		assertTrue(findElement(driver, callNotesButton).isEnabled());
	}
	
	public void verifyAnnotationBtnHoverText_Enabled(WebDriver driver) {
		waitUntilVisible(driver, annotationBtn);
		assertFalse(isAttributePresent(driver, findElement(driver, annotationBtn).findElement(By.xpath("ancestor::a[@aria-label='Annotation']")), ElementAttributes.AriaDescribedBy.displayName()));
		hoverElement(driver, annotationBtn);
		assertTrue(isAttributePresent(driver, findElement(driver, annotationBtn).findElement(By.xpath("ancestor::a[@aria-label='Annotation']")), ElementAttributes.AriaDescribedBy.displayName()));
		assertTrue(findElement(driver, annotationBtn).isEnabled());
	}
	
	/**verify supervisor notes btn disabled
	 * @param driver
	 */
	public void verifySuperVisorNotesBtnDisabled(WebDriver driver) {
		waitUntilVisible(driver, superVisorNotesButton);
		assertTrue(isElementDisabled(driver, findElement(driver, superVisorNotesButton)
				.findElement(By.xpath("../ancestor::a[@title='Supervisor Notes']/button")), 2));

	}
	
	/**verify call notes btn disabled
	 * @param driver
	 */
	public void verifyCallNotesBtnDisabled(WebDriver driver) {
		waitUntilVisible(driver, callNotesButton);
		assertTrue(isElementDisabled(driver, findElement(driver, callNotesButton)
				.findElement(By.xpath("../ancestor::a[@title='Call Notes']/button")), 2));

	}
	
	/**verify annotation notes btn disabled
	 * @param driver
	 */
	public void verifyAnnotationBtnDisabled(WebDriver driver) {
		waitUntilVisible(driver, annotationBtn);
		assertTrue(isElementDisabled(driver, findElement(driver, annotationBtn)
				.findElement(By.xpath("../ancestor::a[@title='Annotation']/div/button")), 2));
	}
	
	public String getAnnotationSize(WebDriver driver, String annotation) {
		waitUntilVisible(driver, annotationSize);
		return getElementsText(driver, annotationSize);
	}
	
	/**
	 * @param driver
	 * @param annotationName
	 * @param topic
	 * @return
	 */
	public boolean verifyAnnotationTopicWithThumbsUp(WebDriver driver, String topic) {
		boolean value = false;
		By annotationThumbsLoc = By.xpath(topicThumbsIconOuterHTML.replace("$topic$", topic));
		value = getAttribue(driver, annotationThumbsLoc, ElementAttributes.outerHTML)
				.equals(annotateThumbsUpOuterHTML);
		return value;
	}
	
	/**
	 * @param driver
	 * @param annotationName
	 * @param topic
	 * @return
	 */
	public boolean verifyAnnotationTopicWithThumbsDown(WebDriver driver, String topic) {
		boolean value = false;
		By annotationThumbsLoc = By.xpath(topicThumbsIconOuterHTML.replace("$topic$", topic));
		value = getAttribue(driver, annotationThumbsLoc, ElementAttributes.outerHTML)
				.equals(annotateThumbsDownOuterHTML);
		return value;
	}
	
	public void clickCallNotesBtn(WebDriver driver) {
		waitUntilVisible(driver, callNotesButton);
		clickByJs(driver, findElement(driver, callNotesButton).findElement(By.xpath("..")));
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void clickSuperVisorNotesBtn(WebDriver driver) {
		waitUntilVisible(driver, superVisorNotesButton);
		clickByJs(driver, findElement(driver, superVisorNotesButton).findElement(By.xpath("..")));
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void clickAnnotationsBtn(WebDriver driver) {
		waitUntilVisible(driver, annotationBtn);
		clickByJs(driver, findElement(driver, annotationBtn).findElement(By.xpath("..")));
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void verifyFilterImages(WebDriver driver, FilterType filterType, boolean status) {
		int page = callsTabReactPage.getTotalPage(driver);
		for (int j = 0; j < page && j < 2; j++) {
			
			// Loop for pages
			boolean isTokenFound = false;
			switch (filterType) {
			case SharedCalls:
				isTokenFound = verifyFilterTypeImageListVisible(driver, FilterType.SharedCalls);
				break;
			case SavedSearch:
				isTokenFound = verifyFilterTypeImageListVisible(driver, FilterType.SavedSearch);
				break;
			case SupervisorNotes:
				isTokenFound = verifyFilterTypeImageListVisible(driver, FilterType.SupervisorNotes);
				break;
			case Annotation:
				isTokenFound = verifyFilterTypeImageListVisible(driver, FilterType.Annotation);;
				verifyAllNotificationListContainsValue(driver, "annotated a call");
				break;
			case FlaggedCalls:
				isTokenFound = verifyFilterTypeImageListVisible(driver, FilterType.FlaggedCalls);
				verifyAllNotificationListContainsValue(driver, "flagged a call");
				break;		
			default:
				break;
			}
			
			if (status) {																				
				assertTrue(isTokenFound);
			} else {
				assertFalse(isTokenFound);
			}
			
			callsTabReactPage.navigateToNextCAIPage(driver, page, j); // navigates to next page
		}
	}
	
	public void verifySortInputOnPages(WebDriver driver, SortInput sortInput) {
		int page = callsTabReactPage.getTotalPage(driver);
		for (int j = 0; j < page && j < 2; j++) {
			
			// Loop for pages
			switch (sortInput) {
			case AgentNameAtoZ:
				verifyNotificationTextListAscending(driver);
				break;
			case AgentNameZtoA:
				verifyNotificationTextListDescending(driver);
				break;
			case OldestToNewest:
				verifyDateInboxListAscending(driver);
				break;
			case NewestToOldest:
				verifyDateInboxListDescending(driver);
				break;
			default:
				break;
			}
			
			callsTabReactPage.navigateToNextCAIPage(driver, page, j); // navigates to next page
		}
	}

	/**
	 * use to get the total page text
	 * 
	 * @param driver
	 * @return
	 */
	public String getTotalPageText(WebDriver driver) {
		isListElementsVisible(driver, notificationTextList, 5);
		waitUntilVisible(driver, totalPage);
		return getElementsText(driver, totalPage);
	}
	
	//Bulk Management section
	public void selectMenuTriggerOption(WebDriver driver, Enum<?> menuOption) {
		clickAndSelectFromDropDown(driver, menuTriggerIcon, menuBoxItems, menuOption.name());
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsVisible(driver);
	}
	
	public int getUnReadImageListCount(WebDriver driver) {
		isListElementsVisible(driver, unReadImageList, 5);
		return getElements(driver, unReadImageList).size();
	}

	public int getReadImageListCount(WebDriver driver) {
		isListElementsVisible(driver, readImageList, 5);
		return getElements(driver, readImageList).size();
	}
	
	public void verifyNotificationMarkedReadAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertNotEquals(getAttribue(driver, getElements(driver, filterTypeListImages).get(index).findElement(By.xpath("*[local-name()='path'][1]")), ElementAttributes.stroke), "#FFFFFF");
	}
	
	public void verifyNotificationMarkedUnReadAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, filterTypeListImages, 5);
		assertEquals(getAttribue(driver, getElements(driver, filterTypeListImages).get(index).findElement(By.xpath("*[local-name()='path'][1]")), ElementAttributes.stroke), "#FFFFFF");
	}
	
	public void verifyAllNotificationsMarkedRead(WebDriver driver) {
		isListElementsVisible(driver, readImageList, 5);
		assertEquals(getElements(driver, readImageList).size(), 10);
	}

	public void verifyAllNotificationsMarkedUnRead(WebDriver driver) {
		isListElementsVisible(driver, unReadImageList, 5);
		assertEquals(getElements(driver, unReadImageList).size(), 10);
	}

	public void clickNotificationCheckBoxAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, notificationCheckBoxesList, 5);
		clickElement(driver, getElements(driver, notificationCheckBoxesList).get(index));
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsVisible(driver);
	}
	
	public void selectUnreadCheckBoxAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, unReadCheckBoxesList, 5);
		clickElement(driver, getElements(driver, unReadCheckBoxesList).get(index));
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsVisible(driver);
		assertTrue(isMinusSignMenuTriggerVisible(driver));
	}

	public boolean isUnreadCheckBoxSelected(WebDriver driver, int index) {
		boolean value = false;
		waitUntilVisible(driver, unReadCheckBoxesParentList);
		if (getElements(driver, unReadCheckBoxesParentList).get(index).getAttribute("class").contains("Mui-checked")) {
			value = true;
		}
		return value;
	}

	public void selectReadCheckBoxAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, readCheckBoxesList, 5);
		clickElement(driver, getElements(driver, readCheckBoxesList).get(index));
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsVisible(driver);
		assertTrue(isMinusSignMenuTriggerVisible(driver));
	}

	public void clickMenuTriggerCheckBox(WebDriver driver) {
		waitUntilVisible(driver, menuTriggerCheckBox);
		clickElement(driver, menuTriggerCheckBox);
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsVisible(driver);
		assertFalse(isMinusSignMenuTriggerVisible(driver));
	}
	
	public boolean isMinusSignMenuTriggerVisible(WebDriver driver) {
		waitUntilVisible(driver, menuTriggerCheckBox);
		return findElement(driver, menuTriggerCheckBox).findElement(By.xpath("input")).getAttribute(ElementAttributes.dataIndeterMinate.displayName()).equals("true");
	}
	
	public boolean isMenuTriggerCheckBoxSelected(WebDriver driver) {
		boolean value = false;
		waitUntilVisible(driver, menuTriggerCheckBox);
		if (findElement(driver, menuTriggerCheckBox).findElement(By.xpath("..")).getAttribute("class").contains("Mui-checked")) {
			value = true;
		}
		return value;
	}
	
	public boolean isNotificationCheckBoxesSelected(WebDriver driver) {
		boolean value = false;
		isListElementsVisible(driver, notificationCheckBoxesList, 5);
		List<WebElement> checkBoxParentList = getElements(driver, notificationCheckBoxesParentList);
		for(WebElement checkBoxElement: checkBoxParentList) {
			if(checkBoxElement.getAttribute("class").contains("Mui-checked")) {
				value = true;
				break;
			}
		}
		return value;
	}

	public void verifyBulkManagementBtnsVisible(WebDriver driver) {
		waitUntilVisible(driver, deleteInboxBtn);
		waitUntilVisible(driver, markAsReadBtn);
		waitUntilVisible(driver, markAsUnreadBtn);
	}
	
	public void verifyBulkManagementBtnsNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, deleteInboxBtn);
		waitUntilInvisible(driver, markAsReadBtn);
		waitUntilInvisible(driver, markAsUnreadBtn);
	}
	
	public void cancelDeleteInboxNotification(WebDriver driver) {
		waitUntilVisible(driver, deleteInboxBtn);
		clickElement(driver, deleteInboxBtn);
		waitUntilVisible(driver, cancelBtn);
		assertTrue(getElementsText(driver, cancelBtn).equals("No, keep"));
		clickElement(driver, cancelBtn);
		dashBoard.isPaceBarInvisible(driver);
	}
	
	public void deleteInboxNotification(WebDriver driver, SelectNotifications selectNotifications) {
		By deleteItemHeader = null;
		By deleteItemBody 	= null;
		waitUntilVisible(driver, deleteInboxBtn);
		clickElement(driver, deleteInboxBtn);
		switch(selectNotifications) {
		case Multiple:
			deleteItemHeader = multipleDeleteItemHeader;
			deleteItemBody   = multipleDeleteItemBody;
			break;
		case Single:
			deleteItemHeader = singleDeleteItemHeader;
			deleteItemBody   = singleDeleteItemBody;
			break;
		default:
			break;
			
		}
		waitUntilVisible(driver, deleteItemHeader);
		waitUntilVisible(driver, deleteItemBody);
		waitUntilVisible(driver, confirmBtn);
		assertEquals(getElementsText(driver, confirmBtn), "Yes, delete");
		clickElement(driver, confirmBtn);
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsNotVisible(driver);
	}
	
	public void markAsReadNotification(WebDriver driver) {
		waitUntilVisible(driver, markAsReadBtn);
		clickElement(driver, markAsReadBtn);
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsNotVisible(driver);
	}
	
	public void markAsUnreadNotification(WebDriver driver) {
		waitUntilVisible(driver, markAsUnreadBtn);
		clickElement(driver, markAsUnreadBtn);
		dashBoard.isPaceBarInvisible(driver);
		verifyBulkManagementBtnsNotVisible(driver);
	}
	
	public boolean isOnlyUnreadNotificationSelected(WebDriver driver) {
		boolean value = false;
		isListElementsVisible(driver, unReadCheckBoxesParentList, 5);
		if (getWebelementIfExist(driver, unReadCheckBoxesParentList) == null) {
			return false;
		}
		List<WebElement> unReadImageList = getElements(driver, unReadCheckBoxesParentList);
		for (WebElement element : unReadImageList) {
			waitUntilVisible(driver, element);
			value = element.getAttribute("class").contains("Mui-checked");
		}
		return value;
	}

	public boolean isOnlyReadNotificationSelected(WebDriver driver) {
		boolean value = false;
		isListElementsVisible(driver, readCheckBoxesParentList, 5);
		if (getWebelementIfExist(driver, readCheckBoxesParentList) == null) {
			return false;
		}
		List<WebElement> readImageList = getElements(driver, readCheckBoxesParentList);
		for (WebElement element : readImageList) {
			waitUntilVisible(driver, element);
			value = element.getAttribute("class").contains("Mui-checked");
		}
		return value;
	}
}
