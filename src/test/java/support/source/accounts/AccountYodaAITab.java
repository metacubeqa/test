package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import report.source.ReportMetabaseCommonPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class AccountYodaAITab extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	ReportMetabaseCommonPage reportPage = new ReportMetabaseCommonPage();
	
	By yodaAITab 							= By.cssSelector("[data-tab='yoda-ai-notifications']");
	By yodaAITabParent 						= By.xpath(".//*[@data-tab='yoda-ai-notifications']/ancestor::li");
	By yodaAITabHeading 					= By.xpath("//h2[contains(text(),'YODA AI Notification')]");
	By yodaAIHereText                       = By.xpath("//span[contains(text(),'Provide your reps with AI-powered alerts that help them overcome')]");
	By yodaAIHereLink                       = By.xpath("//a[text()='here']");
	By yodaHereLinkHeading                  = By.xpath("//h4[text()='YODA AI by ringDNA']");
	By yodaHereVideoButton                  = By.xpath("//button[@aria-label='Play Video: ringDNA YODA AI']");
	
	By videoPlayer                          = By.xpath("//div[contains(@class, 'vulcan--background')]");
	By videoPlayerBar                       = By.xpath("//div[@data-handle='playbar']");
	
	// seacrh notification
	By searchNotificationInput              = By.xpath("//input[@name= 'global-search-filter' and @placeholder='Search by Name, Team, Target']");
	By clearNotificationInput               = By.xpath("//input[@name= 'global-search-filter']/following-sibling::button");
	By targetInput                          = By.xpath("//div[contains(@id, 'Notification Targeting-select')]");
	By filterInput                          = By.xpath("//div[contains(@id, 'Type-select')]");
	By filterOptions                        = By.xpath("//ul[contains(@class, 'MuiMenu-list MuiList-padding')]/li");
	By noResultsText                        = By.xpath("//h2[text()= 'no results']");
	
	By yodaAIDescription                    = By.xpath("//span[contains(text(), 'Alert users')]");
	By EnabledNotificationText              = By.xpath("//span[contains(@class, 'Mui-checked')]/ancestor::div[contains(@class , 'ringdna-table-row')]/span[1]");
	//row headers
	By yodaName                             = By.xpath("//div[@role='row']/span[text()='Name']");
	By yodaNotificationTargeting            = By.xpath("//div[@role='row']/span[text()='Notification Targeting']");
	By yodaType                             = By.xpath("//div[@role='row']/span[text()='Type']");
	By yodaLastUpdated                      = By.xpath("//div[@role='row']/span[text()='Last Updated']");
	By yodaEnable                           = By.xpath("//div[@role='row']//span[text()='Enable']");
	By sortYodaEnable                       = By.xpath("//span[@title='Toggle SortBy']");
	
	//table data
	By nameData                             = By.xpath("//div[contains(@class,'ringdna-table-row')]/span[1]");
	By notificationTargetingData            = By.xpath("//div[contains(@class,'ringdna-table-row')]/span[2]");
	By typeData                             = By.xpath("//div[contains(@class,'ringdna-table-row')]/span[3]");
	By lastUpdatedData                      = By.xpath("//div[contains(@class,'ringdna-table-row')]/span[4]");
	By enableData                           = By.xpath("//div[contains(@class,'ringdna-table-row')]/span[5]");
	
	//action
	By actionButton                         = By.xpath("//div[@aria-label='rdna-menu-trigger']/button");
	By deleteOption                         = By.xpath("//li[@data-analyticsid='menu-item-Delete']");
	By deleteCancelButton                   = By.cssSelector(".rdna-button.text ");
	By confirmDeleteButton                  = By.xpath("//button[text()='Delete']");
	By editOption                           = By.xpath("//li[@data-analyticsid='menu-item-Edit']");
	By updateNotificationHeading            = By.xpath("//a[@class= 'back-link active']/following-sibling::h2");
	
	//create notification
	By createNotiButton                     = By.xpath("//button[text()= 'Create Notification']");
	By backToYodaAi                         = By.cssSelector(".back-link.active");
	By notificationName                     = By.cssSelector("input[name='name']");
	
	By teamInput                         	= By.cssSelector("input#teams");
	By teamAutoComplete                  	= By.cssSelector("div[data-testid='autocomplete-teams']");
	By teamAutoCompleteInput             	= By.cssSelector("div[data-testid='autocomplete-teams'] input");
	By teamListPopup                     	= By.id("teams-popup");
	By teamNameOption                    	= By.xpath(".//*[contains(@id, 'teams-option')]");
	By teamNameFirstOption               	= By.id("teams-option-0");
	
	By userInput                         	= By.cssSelector("input#users");
	By userAutoComplete                  	= By.cssSelector("div[data-testid='autocomplete-users']");
	By userAutoCompleteInput             	= By.cssSelector("div[data-testid='autocomplete-users'] input");
	By userListPopup                     	= By.id("users-popup");
	By userNameOption                    	= By.xpath(".//*[contains(@id, 'users-option')]");
	By userNameFirstOption               	= By.id("users-option-0");
	
	By salesforceProfileInput               = By.cssSelector("input#salesforceProfiles");
	By salesforceProfileAutoComplete        = By.cssSelector("div[data-testid='autocomplete-salesforceProfiles']");
	By salesforceProfileAutoCompleteInput   = By.cssSelector("div[data-testid='autocomplete-salesforceProfiles'] input");
	By salesforceProfileListPopup           = By.id("salesforceProfiles-popup");
	By salesforceProfileNameOption          = By.xpath(".//*[contains(@id, 'salesforceProfiles-option')]");
	By salesforceProfileNameFirstOption     = By.id("salesforceProfiles-option-0");
	
	By salesforceRoleInput               	= By.cssSelector("input#salesforceRoles");
	By salesforceRoleAutoComplete        	= By.cssSelector("div[data-testid='autocomplete-salesforceRoles']");
	By salesforceRoleAutoCompleteInput   	= By.cssSelector("div[data-testid='autocomplete-salesforceRoles'] input");
	By salesforceRoleListPopup           	= By.id("salesforceRoles-popup");
	By salesforceRoleNameOption          	= By.xpath(".//*[contains(@id, 'salesforceRoles-option')]");
	By salesforceRoleNameFirstOption     	= By.id("salesforceRoles-option-0");
	
	By notificationTargetInput              = By.xpath("//div[contains(@id , 'targetingType-select')]");
	String notificationTargetOption         = "//li[text()='Select']/following-sibling::li[@data-value = '%option%']";
	
	By notificationPhrases                  = By.xpath("//input[contains(@placeholder , 'Press') and contains(@class, 'MuiOutlinedInput')]");
	By phraseArrowButton                    = By.cssSelector(".MuiButtonBase-root.MuiIconButton-sizeSmall");
	By deletePhrases                        = By.xpath("//*[contains(@class, 'deleteIconSmall')]");
	By phrasesSelected                      = By.cssSelector(".MuiChip-label.MuiChip-labelSmall");
	By notificationText                     = By.xpath("//textarea[contains(@id, 'notifyText')]");
	By notificationTextCount                = By.xpath("//span[contains(text(), '/180')]");
	By enableNotification                   = By.xpath("//input[@type='checkbox']/ancestor::span[2]");
	By enableNotificationLabel              = By.cssSelector("[data-testid='rdna-toggle'] h6");
	By saveButton                           = By.xpath("//button[text()='Cancel']/following-sibling::button[text()='Save']");
	By previewButton                        = By.xpath("//button[text()= 'Preview' and not(contains[@disabled])]");
	By idPreviewImage                       = By.xpath("//img[@alt='generic ringDNA Intelligent Dialer background']");
	By closePreviewButton                   = By.xpath("//button[@data-testid = 'modal.close']");
	By cancelButton                         = By.xpath("//button[text()='Cancel']");
	By errorMessage                         = By.cssSelector(".MuiAlert-message span");
	By errorCloseButton                     = By.cssSelector(".rdna-button.outlined");
	By validationMessage                    = By.xpath("//p[contains(@id , 'helper-text')]");
	
	By notifyLinkUrl                        = By.xpath("//input[@name= 'notifyLinkUrl']");
	By notifyLinkLabel                      = By.xpath("//input[@name= 'notifyLinkLabel']");
	By phraseMatched                        = By.xpath("//input[contains(@value , 'phrases ARE mentioned')]");
	By pharseNotMatched                     = By.xpath("//input[contains(@value , 'phrases ARE NOT mentioned')]/ancestor::span[2]");
	By minuteInput                          = By.xpath("//input[@name='min']");
	By secondInput                          = By.xpath("//input[@name='sec']");
	
	By previewBgColor                       = By.xpath("//div[contains(@class, 'MuiPaper-rounded')]/div[contains(@class, 'MuiCardContent-root')]");
	By supervisorNotification               = By.xpath("//button[text()= 'View Supervisor Notification']");
	By agentNotification                    = By.xpath("//button[text()= 'View Agent Notification']");
	
	String previewLink                      = "//a[@href = '%text%']";
	
	//pagination
	By disabledPreviousPage                 = By.xpath("//button[contains(@class, 'disabled') and @data-testid='previous-page']");
	By previousPage                         = By.xpath("//button[@data-testid='previous-page']");
	By nextPage                             = By.xpath("//button[@data-testid='next-page']");
	By disabledNextPage                     = By.xpath("//button[contains(@class, 'disabled') and @data-testid='next-page']");
	
	By pageNumber                           = By.xpath("//span[contains(text(), 'Page')]");
	
	//expand notifcation
	By allUserLabel                         = By.xpath("//span[text()= 'All Users are targeted']");
	By teamsLabel                           = By.xpath("//span[text()= 'Target Teams']");
	By salesforceProfileLabel               = By.xpath("//span[text()= 'Target Salesforce Prolfiles ']");
	By salesforceRoleLabel                  = By.xpath("//span[text()= 'Target Salesforce Roles']");
	By individualUserLabel                  = By.xpath("//span[text()= 'Target Users']");
	
	By expandNotificationBody               = By.xpath("//span[@color= 'neutral']/following-sibling::span[@color= 'primary']");
	By notificationRows                     = By.xpath("//div[contains(@class , 'ringdna-table-row')]/span[1]");
	
	String notification                     = "//input[@name = 'notifyType' and @value = '%text%']/ancestor::span[2]";
	String notify                           = "//input[@name = 'notify' and @value = '%text%']/ancestor::span[2]";
	String enableDisableNotif               = "//span[text()= '%text%']/following-sibling::span//span[contains(@class, 'Mui-checked')]";
	String enableRtn                        = "//span[text()= '%text%']/following-sibling::span//span[not(contains(@class, 'Mui-checked')) and @aria-disabled='false']";
	String expandNotification               = "//span[text()= '%text%']/parent::div";
	
	
	
	public static enum NotificationType 
	{
		Information("Info"),
		Alert("Alert"),
		All("All"),
		Warning("Warning"),
		None("None"); 
	 
	    private String value;
	 
	    NotificationType(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	public static enum NotificationTarget 
	{
		AllUsers("All Users"),
		All("All"),
		Teams("Team(s)"),
		SalesforceProfiles("Users with a Specific Salesforce Profile"),
		SalesforceRoles("Users with a Specific Salesforce Role"),
		IndividualUsers("Individual Users"),
		None("None");
	 
	    private String value;
	 
	    NotificationTarget(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	public static enum NotifyType 
	{
		Agent("Agent"),
		Supervisors("Supervisors"),
		Both("Both"); 
	 
	    private String value;
	 
	    NotifyType(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	
	/**
	 * @param driver
	 * open yoda ai
	 */
	public void openYodaAiTab(WebDriver driver){
		if (!findElement(driver, yodaAITabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, yodaAITab);
			clickElement(driver, yodaAITab);
			dashboard.isPaceBarInvisible(driver);
			findElement(driver, yodaAITabHeading);
		}
	}
	
	/**
	 * @param driver
	 * verify description
	 */
	public void verifyYodaAiDescription(WebDriver driver) {
		waitUntilVisible(driver, yodaAIDescription);
		assertTrue(getElementsText(driver, yodaAIDescription).contains("Alert users when specified words and phrases are spoken by an agent, participant, or anyone on a call"));
	}
	
	/**
	 * @param driver
	 * headers visible
	 */
	public void isYodaAIHeadersVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, yodaName, 6));
		assertTrue(isElementVisible(driver, yodaNotificationTargeting, 3));
		assertTrue(isElementVisible(driver, yodaType, 3));
		assertTrue(isElementVisible(driver, yodaLastUpdated, 3));
		assertTrue(isElementVisible(driver, yodaEnable, 3));
	}
	
	/**
	 * @param driver
	 * verify edit and delete
	 */
	public void verifyDeleteAndEditOptions(WebDriver driver) {
		List<WebElement> action = getElements(driver, actionButton);
		waitUntilVisible(driver, action.get(0));
		clickElement(driver, action.get(0));
		assertTrue(action.size() <= 10);
		assertTrue(isElementVisible(driver, deleteOption, 6));
		assertTrue(isElementVisible(driver, editOption, 6));
	}
	
	/**
	 * @param driver
	 * delete notifcation
	 */
	public void deleteNotification(WebDriver driver) {
		List<WebElement> action = getElements(driver, actionButton);
		waitUntilVisible(driver, action.get(0));
		clickElement(driver, action.get(0));
		waitUntilVisible(driver, deleteOption);
		clickElement(driver, deleteOption);
		waitUntilVisible(driver, confirmDeleteButton);
		clickElement(driver, confirmDeleteButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * delete cancel notifcation
	 */
	public void deleteCancelNotification(WebDriver driver) {
		List<WebElement> action = getElements(driver, actionButton);
		waitUntilVisible(driver, action.get(0));
		clickElement(driver, action.get(0));
		waitUntilVisible(driver, deleteOption);
		clickElement(driver, deleteOption);
		waitUntilVisible(driver, deleteCancelButton);
		clickElement(driver, deleteCancelButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 */
	public void clickDeleteOption(WebDriver driver) {
		waitUntilVisible(driver, deleteOption);
		clickElement(driver, deleteOption);
		waitUntilVisible(driver, confirmDeleteButton);
		clickElement(driver, confirmDeleteButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * delete notifcation
	 */
	public void EditNotification(WebDriver driver) {
		List<WebElement> action = getElements(driver, actionButton);
		waitUntilVisible(driver, action.get(0));
		clickElement(driver, action.get(0));
		waitUntilVisible(driver, editOption);
		clickElement(driver, editOption);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, updateNotificationHeading);
	}
	
	/**
	 * @param driver
	 * click create notification button
	 */
	public void clickCreateNotification(WebDriver driver) {
		waitUntilVisible(driver, createNotiButton);
		clickElement(driver, createNotiButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * click Back To YodaAi
	 */
	public void clickBackToYodaAi(WebDriver driver) {
		waitUntilVisible(driver, backToYodaAi);
		clickElement(driver, backToYodaAi);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * enable notification in creation mode
	 */
	public void enableNotificationInCreationMode(WebDriver driver) {
		if (!getAttribue(driver, enableNotification, ElementAttributes.Class).contains("Mui-checked")) {
			clickElement(driver, enableNotification);
		}
	}
	
	/**
	 * @param driver
	 * enable notification in creation mode
	 */
	public boolean isNotificationEnabledInCreationMode(WebDriver driver) {
		if (getAttribue(driver, enableNotification, ElementAttributes.Class).contains("Mui-checked")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @param driver
	 * click on preview button
	 */
	public void clickOnPreview(WebDriver driver) {
		waitUntilVisible(driver, previewButton);
		clickElement(driver, previewButton);
		waitUntilVisible(driver, idPreviewImage);
	}
	
	/**
	 * @param driver
	 * click on preview link
	 */
	public void clickUrlLinkInPreview(WebDriver driver, String link) {
		By locator = By.xpath(previewLink.replace("%text%", link));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	/**
	 * @param driver
	 * close preview
	 */
	public void closePreview(WebDriver driver) {
		waitUntilVisible(driver, closePreviewButton);
		clickElement(driver, closePreviewButton);
	}
	
	/**
	 * @param driver
	 * @param notiName
	 * @param team
	 * @param phrase
	 * @param text
	 * create notifcation
	 */
	public void createNotificationInYodaAI(WebDriver driver, String notiName, NotificationTarget target, List<String> team, String phrase, String text, NotificationType type) {
		// enter name
		enterNotificationName(driver, notiName);
		//remove all team and phrase
		removeAllTeamsAndPhraseSelected(driver);
		//select notifcation target
		selectNotificationTargeting(driver, target);
		int i = 0;
		
		if(target.toString().equals("Teams")) {
			// enter team
			while(i<team.size()) {
				selectTeam(driver, team.get(i));
				i++;
			}
		}
		if(target.toString().equals("SalesforceProfiles")) {
			// enter Salesforce Profiles
			while(i<team.size()) {
			selectSalesforceProfiles(driver, team.get(i));
			i++;
			}
		}
		if(target.toString().equals("SalesforceRoles")) {
			// enter Salesforce Roles
			while(i<team.size()) {
			selectSalesforceRole(driver, team.get(i));
			i++;
			}
		}
		if(target.toString().equals("IndividualUsers")) {
			// enter Individual Users
			while(i<team.size()) {
			selectUser(driver, team.get(i));
			i++;
			}
		}

		// enter phrases
		clickAndEnterPhrase(driver, phrase);
		// enter notification text
		enterNotificationText(driver, text);
		//select notification type
		selectNotificationType(driver, type);
		// enable notification
		enableNotificationInCreationMode(driver);
		clickSaveButton(driver);
	}
	
	/**
	 * @param driver
	 * click save button
	 */
	public void clickSaveButton(WebDriver driver) {
		waitUntilVisible(driver, saveButton);
		clickElement(driver, saveButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 */
	public void removeAllTeamsAndPhraseSelected(WebDriver driver) {
		while(isElementVisible(driver, deletePhrases, 6)) {
			List<WebElement> delete = getElements(driver, deletePhrases);
			waitUntilVisible(driver, delete.get(0));
			clickElement(driver, delete.get(0));
		}
	}
	
	/**
	 * @param driver
	 * @param phrase
	 */
	public void clickAndEnterPhrase(WebDriver driver, String phrase) {
		waitUntilVisible(driver, notificationPhrases);
		enterText(driver, notificationPhrases, phrase);
		clickEnter(driver, notificationPhrases);
	}
	
	/**
	 * @param driver
	 * @param phrase
	 */
	public void enterPhraseByArrow(WebDriver driver, String phrase) {
		waitUntilVisible(driver, notificationPhrases);
		enterText(driver, notificationPhrases, phrase);
		clickElement(driver, phraseArrowButton);
	}
	
	/**
	 * @param driver
	 * @param phrase
	 */
	public void enterPhraseByClickingOutside(WebDriver driver, String phrase) {
		waitUntilVisible(driver, notificationPhrases);
		enterText(driver, notificationPhrases, phrase);
		waitUntilVisible(driver, notificationText);
		clickElement(driver, notificationText);
	}
	
	/**
	 * @param driver
	 * @param teamName
	 * select team name
	 */
	public void selectTeam(WebDriver driver, String accountName) {
		if (isElementVisible(driver, teamInput, 5)) {
			waitUntilVisible(driver, teamInput);
			clickElement(driver, teamInput);
			clearAll(driver, teamInput);
			dashboard.isPaceBarInvisible(driver);
			enterText(driver, teamInput, accountName);
			idleWait(2);
			waitUntilVisible(driver, teamAutoComplete);
			clickByJs(driver, teamAutoComplete);
			waitUntilVisible(driver, teamAutoCompleteInput);
			clickElement(driver, teamAutoCompleteInput);
			waitUntilVisible(driver, teamListPopup);
			clickElement(driver, teamNameFirstOption);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 * @param teamName
	 * select user name
	 */
	public void selectUser(WebDriver driver, String accountName) {
		if (isElementVisible(driver, userInput, 5)) {
			waitUntilVisible(driver, userInput);
			clickElement(driver, userInput);
			clearAll(driver, userInput);
			dashboard.isPaceBarInvisible(driver);
			enterText(driver, userInput, accountName);
			idleWait(2);
			waitUntilVisible(driver, userAutoComplete);
			clickByJs(driver, userAutoComplete);
			waitUntilVisible(driver, userAutoCompleteInput);
			clickElement(driver, userAutoCompleteInput);
			waitUntilVisible(driver, userListPopup);
			clickElement(driver, userNameFirstOption);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 * @param teamName
	 * select Salesforce Profile
	 */
	public void selectSalesforceProfiles(WebDriver driver, String accountName) {
		if (isElementVisible(driver, salesforceProfileInput, 5)) {
			waitUntilVisible(driver, salesforceProfileInput);
			clickElement(driver, salesforceProfileInput);
			clearAll(driver, salesforceProfileInput);
			dashboard.isPaceBarInvisible(driver);
			enterText(driver, salesforceProfileInput, accountName);
			idleWait(2);
			waitUntilVisible(driver, salesforceProfileAutoComplete);
			clickByJs(driver, salesforceProfileAutoComplete);
			waitUntilVisible(driver, salesforceProfileAutoCompleteInput);
			clickElement(driver, salesforceProfileAutoCompleteInput);
			waitUntilVisible(driver, salesforceProfileListPopup);
			clickElement(driver, salesforceProfileNameFirstOption);
			dashboard.isPaceBarInvisible(driver);
		}
	}
	
	/**
	 * @param driver
	 * @param teamName
	 * select Salesforce Role
	 */
	public void selectSalesforceRole(WebDriver driver, String accountName) {
		if (isElementVisible(driver, salesforceRoleInput, 5)) {
			waitUntilVisible(driver, salesforceRoleInput);
			clickElement(driver, salesforceRoleInput);
			clearAll(driver, salesforceRoleInput);
			dashboard.isPaceBarInvisible(driver);
			enterText(driver, salesforceRoleInput, accountName);
			idleWait(2);
			waitUntilVisible(driver, salesforceRoleAutoComplete);
			clickByJs(driver, salesforceRoleAutoComplete);
			waitUntilVisible(driver, salesforceRoleAutoCompleteInput);
			clickElement(driver, salesforceRoleAutoCompleteInput);
			waitUntilVisible(driver, salesforceRoleListPopup);
			clickElement(driver, salesforceRoleNameFirstOption);
			dashboard.isPaceBarInvisible(driver);
		}
	}

	/**
	 * @param driver
	 * select notification type
	 */
	public void selectNotificationType(WebDriver driver, NotificationType type) {
		By locator = By.xpath(notification.replace("%text%", type.getValue()));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * enter notification name
	 */
	public void enterNotificationName(WebDriver driver, String notiName) {
		waitUntilVisible(driver, notificationName);
		clickElement(driver, notificationName);
		clearAll(driver, notificationName);
		enterText(driver, notificationName, notiName);
	}
	
	/**
	 * @param driver
	 * enter notification url name
	 */
	public void enterNotificationUrlName(WebDriver driver, String notiName) {
		waitUntilVisible(driver, notifyLinkUrl);
		clickElement(driver, notifyLinkUrl);
		clearAll(driver, notifyLinkUrl);
		enterText(driver, notifyLinkUrl, notiName);
	}
	
	/**
	 * @param driver
	 * enter notification lable name
	 */
	public void enterNotificationLabelName(WebDriver driver, String notiName) {
		waitUntilVisible(driver, notifyLinkLabel);
		clickElement(driver, notifyLinkLabel);
		clearAll(driver, notifyLinkLabel);
		enterText(driver, notifyLinkLabel, notiName);
	}
	
	/**
	 * @param driver
	 * enter notification text
	 */
	public void enterNotificationText(WebDriver driver, String text) {
		waitUntilVisible(driver, notificationText);
		clickElement(driver, notificationText);
		clearAll(driver, notificationText);
		enterText(driver, notificationText, text);
	}
	
	/**
	 * @param driver
	 * is notification text visible
	 */
	public boolean isNotificationTextVisible(WebDriver driver) {
		return isElementVisible(driver, notificationText, 6);
	}
	
	/**
	 * @param driver
	 * @param text
	 * select notification targeting
	 */
	public void selectNotificationTargeting(WebDriver driver, NotificationTarget text) {
		waitUntilVisible(driver, notificationTargetInput);
		clickElement(driver, notificationTargetInput);
		
		By locator = By.xpath(notificationTargetOption.replace("%option%", text.toString()));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}
	
	/**
	 * @param driver
	 * @param notificationName
	 * @param teamName
	 */
	public void verifyNotificationIsPresent(WebDriver driver, String notificationName, NotificationTarget text, NotificationType type, boolean time) {
		dashboard.isPaceBarInvisible(driver);
		if(!notificationName.equals("None")) {
			assertTrue(isListContainsText(driver, getElements(driver, nameData), notificationName));	
		}
		if(!text.getValue().equals("None")) {
			assertTrue(isListContainsText(driver, getElements(driver, notificationTargetingData), text.getValue()));
		}
		if(!type.getValue().equals("None")) {
			assertTrue(isListContainsText(driver, getElements(driver, typeData), type.getValue()));
		}
		if(time == true) {
			//verify time
			String format = "h:mm a";
			Date startDate = HelperFunctions.getDateTimeInDateFormat(getTextListFromElements(driver, lastUpdatedData).get(0), format);
			Date endDate = HelperFunctions.getDateTimeInDateFormat(HelperFunctions.GetCurrentDateTime(format), format);
			int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(startDate, endDate, format);
			assertTrue(diffInMinutes >=-2 && diffInMinutes <=5);	
		}
		//verify enable
		//assertTrue(verifyNotificationIsEnabled(driver, notificationName));
	}
	
	/**
	 * @param driver
	 * @param notificationName
	 * @param teamName
	 */
	public void verifyNoNotificationIsPresent(WebDriver driver, String notificationName) {
		if(isElementVisible(driver, nameData, 6)) {
			assertFalse(isListContainsText(driver, getElements(driver, nameData), notificationName));
		}
	}
	
	/**
	 * @param driver
	 * @param text
	 */
	public void searchNotificationOnYodaAi(WebDriver driver, String text) {
		waitUntilVisible(driver, searchNotificationInput);
		clickElement(driver, searchNotificationInput);
		clearAll(driver, searchNotificationInput);
		enterText(driver, searchNotificationInput, text);
	}
	
	/**
	 * @param driver
	 * @return no result found text
	 */
	public boolean verifyNoNotificationFound(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return isElementVisible(driver, noResultsText, 6);
	}
	
	/**
	 * @param driver
	 */
	public void verifyNotificationIsPresent(WebDriver driver) {
		assertTrue(isElementVisible(driver, nameData, 6));
		assertTrue(isElementVisible(driver, notificationTargetingData, 6));
		assertTrue(isElementVisible(driver, typeData, 6));
		assertTrue(isElementVisible(driver, lastUpdatedData, 6));
	}
	
	/**
	 * @param driver
	 * click yoda ai here link
	 */
	public void clickYodaAiHereLink(WebDriver driver) {
		waitUntilVisible(driver, yodaAIHereLink);
		assertTrue(isElementVisible(driver, yodaAIHereText, 5));
		clickElement(driver, yodaAIHereLink);
		switchToTab(driver, dashboard.getTabCount(driver));
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, yodaHereLinkHeading);
		
		//play video
		waitUntilVisible(driver, yodaHereVideoButton);
		clickElement(driver, yodaHereVideoButton);
		
		//pause video
		waitUntilVisible(driver, videoPlayer);
		clickElement(driver, videoPlayer);
		assertTrue(isElementPresent(driver, videoPlayerBar, 10));
		
		closeTab(driver);
		switchToTab(driver, 2);
	}
	
	/**
	 * @param driver
	 * @param name
	 * @param filter
	 */
	public void searchAccountYodaAINotification(WebDriver driver, String name, NotificationTarget target , NotificationType filter){
		if(!name.equals("None")) {
			waitUntilVisible(driver, searchNotificationInput);
			clickElement(driver, searchNotificationInput);
			clearAll(driver, searchNotificationInput);
			enterText(driver, searchNotificationInput, name);
			dashboard.isPaceBarInvisible(driver);
		}
		if(!target.getValue().equals("None")) {
			clickAndSelectFromDropDown(driver, targetInput, filterOptions, target.getValue());	
			dashboard.isPaceBarInvisible(driver);
		}
		if(!filter.getValue().equals("None")) {
			clickAndSelectFromDropDown(driver, filterInput, filterOptions, filter.toString());	
			dashboard.isPaceBarInvisible(driver);
		}
		if(name.equals("None")) {
			if(isElementVisible(driver, clearNotificationInput, 2)){
				clickElement(driver, clearNotificationInput);
				dashboard.isPaceBarInvisible(driver);	
			}
		}
	}
	
	/**
	 * @param driver
	 * assert pagination of call flow page
	 */
	public int getTotalYodaAINotificationVisible(WebDriver driver) {
		waitUntilVisible(driver, nameData);
		return getElements(driver, nameData).size();
	}
	
	/**
	 * @param driver
	 * get list of all notification visible
	 */
	public List<String> getAllYodaAINotificationList(WebDriver driver) {
		waitUntilVisible(driver, nameData);
		return getTextListFromElements(driver, nameData);
	}
	
	/**
	 * @param driver
	 * @return default filter selected
	 */
	public String getDefaultTypeFilterSelected(WebDriver driver) {
		waitUntilVisible(driver, filterInput);
		return getElementsText(driver, filterInput);
	}
	
	/**
	 * @param driver
	 * @return default filter selected
	 */
	public String getDefaultTargetFilterSelected(WebDriver driver) {
		waitUntilVisible(driver, targetInput);
		return getElementsText(driver, targetInput);
	}
	
	/**
	 * @param driver
	 * @return error message
	 */
	public String getErrorMessage(WebDriver driver) {
		waitUntilVisible(driver, errorMessage);
		String message = getElementsText(driver, errorMessage);
		waitUntilVisible(driver, errorCloseButton);
		clickElement(driver, errorCloseButton);
		return message;
	}
	
	/**
	 * @param driver
	 * @param name
	 * enable notification
	 */
	public void enableNotificationByName(WebDriver driver, String name) {
		By locator = By.xpath(enableDisableNotif.replace("%text%", name));
		if(!isElementVisible(driver, locator, 10)){
			locator = By.xpath(enableRtn.replace("%text%", name));
			waitUntilVisible(driver, locator);
			clickElement(driver, locator);	
		}
	}
	
	/**
	 * @param driver
	 * @param name
	 * disable notification
	 */
	public void disableNotificationByName(WebDriver driver, String name) {
		By locator = By.xpath(enableDisableNotif.replace("%text%", name));
		if(isElementVisible(driver, locator, 10)){
			clickElement(driver, locator);	
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param name
	 * disable notification
	 */
	public boolean verifyNotificationIsEnabled(WebDriver driver, String name) {
		boolean answer = false;
		By locator = By.xpath(enableDisableNotif.replace("%text%", name));
		if(isElementVisible(driver, locator, 10)){
			answer= true;	
		}
		return answer;
	}
	
	/**
	 * @param driver
	 * @return notification name as input
	 */
	public String getNotificationNameEntered(WebDriver driver) {
		waitUntilVisible(driver, notificationName);
		return getAttribue(driver, notificationName, ElementAttributes.value);
	}
	
	/**
	 * @param driver
	 * @return is validation visible
	 */
	public boolean checkValidations(WebDriver driver) {
		return isListElementsVisible(driver, validationMessage, 10);
	}
	
	/**
	 * @param driver
	 * click Previous page
	 */
	public void clickPreviousPage(WebDriver driver) {
		if(!isElementVisible(driver, disabledPreviousPage, 6)) {
			waitUntilVisible(driver, previousPage);
			List<WebElement> prev = getElements(driver, previousPage);
			clickElement(driver, prev.get(0));
		}
	}
	
	/**
	 * @param driver
	 * click Next page
	 */
	public void clickNextPage(WebDriver driver) {
		if(!isElementVisible(driver, disabledNextPage, 6)) {
			waitUntilVisible(driver, nextPage);
			List<WebElement> prev = getElements(driver, nextPage);
			clickElement(driver, prev.get(0));
		}
	}
	
	/**
	 * @param driver
	 * @param number
	 */
	public int getPaginationCount(WebDriver driver) {
		waitUntilVisible(driver, pageNumber);
		List<String> page = getTextListFromElements(driver, pageNumber);
		String str = String.valueOf(page.get(0).charAt(5));
		int answer = Integer.valueOf(str);
		return answer;
	}
	
	/**
	 * @param driver
	 * @param number
	 */
	public int getTotalPageCount(WebDriver driver) {
		waitUntilVisible(driver, pageNumber);
		List<String> page = getTextListFromElements(driver, pageNumber);
		String str = String.valueOf(page.get(0).charAt(10));
		int answer = Integer.valueOf(str);
		return answer;
	}
	
	
	/**
	 * @param driver
	 * @return team exist or not
	 */
	public boolean verifyTeamExistsInDropDown(WebDriver driver, String teamName) {
		waitUntilVisible(driver, teamInput);
		clickElement(driver, teamInput);
		clearAll(driver, teamInput);
		dashboard.isPaceBarInvisible(driver);
		enterText(driver, teamInput, teamName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownTeams(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, teamName);
		}
	}
	
	/**
	 * @param driver
	 * @return get all teams
	 */
	public List<WebElement> getListDropDownTeams(WebDriver driver) {
		waitUntilVisible(driver, teamAutoComplete);
		clickElement(driver, teamAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, teamAutoCompleteInput);
		clickElement(driver, teamAutoCompleteInput);
		if (isElementVisible(driver, teamListPopup, 5)) {
			return getElements(driver, teamNameOption);
		} else {
			return null;
		}
	}
	
	
	/**
	 * @param driver
	 * @return user exist or not
	 */
	public boolean verifyUserExistsInDropDown(WebDriver driver, String userName) {
		waitUntilVisible(driver, userInput);
		clickElement(driver, userInput);
		clearAll(driver, userInput);
		dashboard.isPaceBarInvisible(driver);
		enterText(driver, userInput, userName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownUsers(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, userName);
		}
	}
	
	/**
	 * @param driver
	 * @return get all users
	 */
	public List<WebElement> getListDropDownUsers(WebDriver driver) {
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
	
	/**
	 * @param driver
	 * @return role exist or not
	 */
	public boolean verifyRoleExistsInDropDown(WebDriver driver, String userName) {
		waitUntilVisible(driver, salesforceRoleInput);
		clickElement(driver, salesforceRoleInput);
		clearAll(driver, salesforceRoleInput);
		dashboard.isPaceBarInvisible(driver);
		enterText(driver, salesforceRoleInput, userName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownRoles(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, userName);
		}
	}
	
	/**
	 * @param driver
	 * @return get all roles
	 */
	public List<WebElement> getListDropDownRoles(WebDriver driver) {
		waitUntilVisible(driver, salesforceRoleAutoComplete);
		clickElement(driver, salesforceRoleAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, salesforceRoleAutoCompleteInput);
		clickElement(driver, salesforceRoleAutoCompleteInput);
		if (isElementVisible(driver, salesforceRoleListPopup, 5)) {
			return getElements(driver, salesforceRoleNameOption);
		} else {
			return null;
		}
	}
	
	/**
	 * @param driver
	 * @return Profile exist or not
	 */
	public boolean verifyProfileExistsInDropDown(WebDriver driver, String userName) {
		waitUntilVisible(driver, salesforceProfileInput);
		clickElement(driver, salesforceProfileInput);
		clearAll(driver, salesforceProfileInput);
		dashboard.isPaceBarInvisible(driver);
		enterText(driver, salesforceProfileInput, userName);
		idleWait(2);
		List<WebElement> dropDownList = getListDropDownProfiles(driver);
		if (dropDownList == null) {
			assertEquals(reportPage.getStartTypingDropDownText(driver), "No options");
			return false;
		} else {
			return isTextPresentInList(driver, dropDownList, userName);
		}
	}
	
	/**
	 * @param driver
	 * @return get all Profile
	 */
	public List<WebElement> getListDropDownProfiles(WebDriver driver) {
		waitUntilVisible(driver, salesforceProfileAutoComplete);
		clickElement(driver, salesforceProfileAutoComplete);
		idleWait(1);
		waitUntilVisible(driver, salesforceProfileAutoCompleteInput);
		clickElement(driver, salesforceProfileAutoCompleteInput);
		if (isElementVisible(driver, salesforceProfileListPopup, 5)) {
			return getElements(driver, salesforceProfileNameOption);
		} else {
			return null;
		}
	}
	
	/**
	 * @param driver
	 * @return notify link placeholder
	 */
	public String getNotifyUrlPlaceholderValue(WebDriver driver) {
		waitUntilVisible(driver, notifyLinkUrl);
		return getAttribue(driver, notifyLinkUrl, ElementAttributes.Placeholder);
	}
	
	/**
	 * @param driver
	 * @return notify label placeholder
	 */
	public String getNotifyLabelPlaceholderValue(WebDriver driver) {
		waitUntilVisible(driver, notifyLinkLabel);
		return getAttribue(driver, notifyLinkLabel, ElementAttributes.Placeholder);
	}
	
	/**
	 * @param driver
	 * @param minute
	 * @param second
	 */
	public void selectPhrasesNotMentioned(WebDriver driver, String minute, String second) {
		waitUntilVisible(driver, pharseNotMatched);
		clickElement(driver, pharseNotMatched);
		
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, minuteInput);
		clickElement(driver, minuteInput);
		clearAll(driver, minuteInput);
		enterText(driver, minuteInput, minute);
		
		waitUntilVisible(driver, secondInput);
		clickElement(driver, secondInput);
		clearAll(driver, secondInput);
		enterText(driver, secondInput, second);
	}
	
	/**
	 * @param driver
	 * @return count of notification text
	 */
	public String getnotificationTextCount(WebDriver driver) {
		waitUntilVisible(driver, notificationTextCount);
		return getElementsText(driver, notificationTextCount);
	}
	
	/**
	 * @param driver
	 * @return get notification text
	 */
	public String getNotificationText(WebDriver driver) {
		waitUntilVisible(driver, notificationText);
		return getElementsText(driver, notificationText);
	}
	
	/**
	 * @param driver
	 * @param name
	 * expand notification by name
	 */
	public void expandNotificationByName(WebDriver driver, String name) {
		dashboard.isPaceBarInvisible(driver);
		idleWait(2);
		By locator = By.xpath(expandNotification.replace("%text%", name));
		waitUntilVisible(driver, locator);
		clickByJs(driver, locator);
	}
	
	/**
	 * @param driver
	 * @param body
	 * expand all notification for verification
	 */
	public void expandAllNotificationForVerify(WebDriver driver, String body) {
		for(WebElement row : getElements(driver, notificationRows)) {
			waitUntilVisible(driver, row);
			clickByJs(driver, row);
			assertTrue(getExpandNotificationBody(driver).contains(body));
			clickByJs(driver, row);
		}
	}
	
	/**
	 * @param driver
	 * @return is all users notification expanded
	 */
	public boolean isAllUserNotificationExpanded(WebDriver driver) {
		return isElementVisible(driver, allUserLabel, 6);
	}
	
	/**
	 * @param driver
	 * @return is teams notification expanded
	 */
	public boolean isTeamsNotificationExpanded(WebDriver driver) {
		return isElementVisible(driver, teamsLabel, 6);
	}
	
	/**
	 * @param driver
	 * @return is salesforce Roles notification expanded
	 */
	public boolean isSalesforceRolesNotificationExpanded(WebDriver driver) {
		return isElementVisible(driver, salesforceRoleLabel, 6);
	}
	
	/**
	 * @param driver
	 * @return is salesforce profiles notification expanded
	 */
	public boolean isSalesforceProfileNotificationExpanded(WebDriver driver) {
		return isElementVisible(driver, salesforceProfileLabel, 6);
	}
	
	/**
	 * @param driver
	 * @return is Individual Users notification expanded
	 */
	public boolean isIndividualUsersNotificationExpanded(WebDriver driver) {
		return isElementVisible(driver, individualUserLabel, 6);
	}
	
	/**
	 * @param driver
	 * @return get notification body text
	 */ 
	public List<String> getExpandNotificationBody(WebDriver driver) {
		waitUntilVisible(driver, expandNotificationBody);
		return getTextListFromElements(driver, expandNotificationBody);
	}
	
	/**
	 * @param driver
	 * @param index
	 */
	public void goToPageByNumber(WebDriver driver, int index) {
		for (int i = 1; i < index; i++) {
			if (!isElementVisible(driver, disabledNextPage, 6)) {
				waitUntilVisible(driver, nextPage);
				List<WebElement> prev = getElements(driver, nextPage);
				clickElement(driver, prev.get(0));
			}
		}
	}
	
	/**
	 * @param driver
	 * @return get phrases selected
	 */
	public List<String> getPharsesSelected(WebDriver driver) {
		if(isElementVisible(driver, phrasesSelected, 6)) {
			return getTextListFromElements(driver, phrasesSelected);
		}else {
			return null;
		}
	}
	
	/**
	 * @param driver
	 * cancel and save button
	 */
	public void verifyCancelSaveButton(WebDriver driver) {
		assertTrue(isElementVisible(driver, cancelButton, 6));
		assertTrue(isElementVisible(driver, saveButton, 6));
	}
	
	/**
	 * @param driver
	 * @return enable notification label
	 */
	public String getEnableNotificationLabel(WebDriver driver) {
		waitUntilVisible(driver, enableNotificationLabel);
		return getElementsText(driver, enableNotificationLabel);
	}
	
	/**
	 * @param driver
	 * @return select notify type - agent, supervisor
	 */
	public void selectNotifyType(WebDriver driver, NotifyType type) {
		//select notify type
		By locator = By.xpath(notify.replace("%text%", type.getValue()));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @return get Preview background color
	 */
	public String getPreviewBackgroundColor(WebDriver driver) {
		waitUntilVisible(driver, previewBgColor);
		return findElement(driver, previewBgColor).getAttribute("backgroundcolor");
	}
	
	/**
	 * @param driver
	 * click on supervisor and agent notification link
	 */
	public void clickOnSupervisorNotification(WebDriver driver) {
		waitUntilVisible(driver, supervisorNotification);
		clickElement(driver, supervisorNotification);
		waitUntilVisible(driver, agentNotification);
		clickElement(driver, agentNotification);
		waitUntilVisible(driver, supervisorNotification);
	}
	
	/**
	 * @param driver
	 * verify sorting
	 */
	public void verifyEnableSorting(WebDriver driver) {
		waitUntilVisible(driver, sortYodaEnable);
		clickElement(driver, sortYodaEnable);
		List<String> total = getTextListFromElements(driver, nameData);
		List<String> enabled = getTextListFromElements(driver, EnabledNotificationText);
		for (int i = 0; i < enabled.size(); i++) {
			assertTrue(total.get(i).equals(enabled.get(i)));
		}
		waitUntilVisible(driver, sortYodaEnable);
		clickElement(driver, sortYodaEnable);
		total = getTextListFromElements(driver, nameData);
		enabled = getTextListFromElements(driver, EnabledNotificationText);
		for (int i = 0; i < enabled.size(); i++) {
			assertFalse(total.get(i).equals(enabled.get(i)));
		}
		// search
		searchAccountYodaAINotification(driver, null, null, NotificationType.Information);
		total = getTextListFromElements(driver, nameData);
		enabled = getTextListFromElements(driver, EnabledNotificationText);
		for (int i = 0; i < enabled.size(); i++) {
			assertTrue(total.get(i).equals(enabled.get(i)));
		}
		// search
		searchAccountYodaAINotification(driver, null, NotificationTarget.AllUsers, null);
		total = getTextListFromElements(driver, nameData);
		enabled = getTextListFromElements(driver, EnabledNotificationText);
		for (int i = 0; i < enabled.size(); i++) {
			assertTrue(total.get(i).equals(enabled.get(i)));
		}
	}

}
