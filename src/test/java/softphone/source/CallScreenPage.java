package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.ContactDetailPage;
import utility.HelperFunctions;

public class CallScreenPage extends SeleniumBase{

	ContactDetailPage contactDetailPage = new ContactDetailPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	HelperFunctions helperFunctions = new HelperFunctions();

	By callDetailSection			= By.className("call-crm");
	By foreGroundCallName			= By.cssSelector(".foreground-call");
	By callerName 					= By.cssSelector("div[data-action='view-contact']");
	By nameContainerLoc				= By.cssSelector(".name-container");
	By leadImage					= By.cssSelector(".call-crm [src=\"images/icon-sfdc-lead-v2.svg\"]:not([style=\"display: none;\"])");
	By callerCompany 				= By.cssSelector("[data-action='view-salesforce-account']");
	By callDisposition				= By.cssSelector(".call-crm .disposition");
	By callerTitle 					= By.className("title");
	By createFavoriteIcon 			= By.cssSelector("a.favorite-action>img.create");
	By deleteFavoriteIcon 			= By.cssSelector("a.favorite-action>img.delete");
	By callerLeadIcon				= By.cssSelector(".call-crm img.isLead");
	By callerContactIcon 			= By.cssSelector(".call-crm img.isContact");
	By callerOpportunityIcon 		= By.cssSelector("div.media-left>img.isOpportunity ");
	By callerBusinessAccountIcon 	= By.cssSelector("div.media-left>img.isBusinessAccount  ");
	By callerPersonAccountIcon 		= By.cssSelector("div.media-left>img.isPersonAccount ");
	By callerUserGroupIcon 			= By.cssSelector("div.media-left>img.isUserGroup  ");
	By callerNumber					= By.cssSelector(".call-crm .phone-item>[data-action='dial-phone'].phone-number, div.crm-create[style='display: block;'] .callerPhone, div.crm-multiple[style='display: block;'] .callerPhone");
	By callerotherNumber			= By.xpath(".//*[@class=\"call-crm\"]//*[@data-content='O:']/following-sibling::a[@class='phone-number']");
	By callerEmail 					= By.cssSelector(".emailAddress");
	By callerVoicemail				= By.className("voicemail");
	By callerRating 				= By.cssSelector(".active.recent-call-rating:not([style]");
	By emailButton					= By.cssSelector(".emailHref img");
	By callButton					= By.cssSelector(".crm-single .call[data-action='dial-phone'] img, div.crm-create[style='display: block;'] .call-button img, div.crm-multiple[style='display: block;'] .call-button img");
	By callerLocalTime				= By.className("localTime");
	public static By numberOfCallers = By.cssSelector(".call-crm .contact-item:not([style='display: none;']) .merge-state, .call-crm .crm-multiple:not([style='display: none;']) .merge-state, .call-crm .crm-create.crm-call-meta:not([style='display: none;']) .merge-state");
	By addNewButtton				= By.cssSelector(".crm-create:not([style='display: none;']) [data-action='create-new-lead-contact'], .update-crm-match:not([style='display: none;']) [data-action='create-new-lead-contact'], .crm-multiple:not([style='display: none;']) [data-action='create-new-lead-contact']");
	By createLeadRadioBox			= By.id("create-lead");				
	By createContactRadioBox		= By.id("create-contact");
	By firstNameInputBox			= By.cssSelector(".create-new-lead-contact-form:not([style='display: none;']) [placeholder='First Name'], .sfdc-field-list-on-update-match:not([style='display: none;']) [placeholder='First Name']");		
	By lastNameInputBox				= By.cssSelector(".create-new-lead-contact-form:not([style='display: none;']) [placeholder='Last Name'], .sfdc-field-list-on-update-match:not([style='display: none;']) [placeholder='Last Name']");
	By titleInputBox				= By.cssSelector(".create-new-lead-contact-form:not([style='display: none;']) [placeholder='Title'], .sfdc-field-list-on-update-match:not([style='display: none;']) [placeholder='Title']");				
	By leadCompanyInputBox			= By.cssSelector(".create-new-lead-contact-form:not([style='display: none;']) [placeholder='Company']");
	By leadStatusSelected			= By.cssSelector(".selectize-control.lead-status .item");
	By leadStatusDropDown			= By.xpath(".//*[text()='Lead Status:']/../following-sibling::div//*[contains(@class,'selectize-control')]|.//*[contains(@class,'lead-status')]/*[contains(@class,'selectize-control')]");
	By leadStatusTextBox			= By.xpath(".//*[text()='Lead Status:']/../following-sibling::div//*[contains(@class,'selectize-control')]//input|.//*[contains(@class,'lead-status')]//input*");
	
	By phoneNumberLabel				= By.cssSelector(".crm-single [data-content='P:']");
	By phoneNumberValue				= By.cssSelector(".crm-single [data-content=‘P:’] ~ a.phone-number");
	
	By addAdditionalNumbers 		= By.className("add-additional-phone");
	By phoneNumTextBox 				= By.cssSelector("[placeholder='Phone']");
	By mobNumTextBox 				= By.cssSelector("[placeholder='Mobile']");
	By HomeNumTextBox 				= By.cssSelector("[placeholder='Home']");
	By OtherNumTextBox 				= By.cssSelector("[placeholder='Other']");
	By AssistantNumTextBox 			= By.cssSelector("[placeholder='Assistant']");
	By leadStatusDrpDwnOption		= By.xpath(".//*[text()='Lead Status:']/../following-sibling::div//*[contains(@class,'selectize-control')]//*[@class='selectize-dropdown-content']/*[contains(@class,'option')]|.//*[contains(@class,'lead-status')]//*[@class='selectize-dropdown-content']/*[contains(@class,'option')]");
	By campaign						= By.className("campaign");
	By contactSource				= By.className("source");
	By contactAccountInputBox		= By.cssSelector(".create-new-lead-contact-form input.account");
	By contactAccountSearch			= By.cssSelector(".create-new-lead-contact-form .search");
	By addAccountOption				= By.cssSelector("[data-action='add-account']");
	By accountSelectOptions			= By.cssSelector(".tt-dataset-salesforceAccounts div.tt-selectable");
	By addNewAccountHeader			= By.xpath("//h4[text()='Add New Account']");
	By addNewAccountName			= By.className("accountName");
	By addAccountSaveButton			= By.cssSelector("[data-action='save-account']");
	By addAccountCancelButton		= By.cssSelector("button.cancel[data-dismiss='modal']");
	By saveAddContactButton			= By.cssSelector("[data-action='saveLeadContact'], .save-fields");
	By saveFieldsButton				= By.cssSelector(".save-fields");
	By cancelAddContactButton		= By.cssSelector("[data-action='cancel'], .cancel-updating-crm-match");
	By cancelAddToExistingBtn		= By.cssSelector(".update-crm-match .cancel-show-lookup");
	By relatedRecordHeading			= By.cssSelector(".related-records label");
	By relatedRecordNamesList		= By.cssSelector(".related-record span.name");
	By relatedRecordfilter			= By.cssSelector(".related-record .filter-related-record");

	By relatedRecordEdit			= By.cssSelector(".related-record .edit-related-record");
	By relatedRecordexpand			= By.cssSelector(".related-record .toggle-fields");
	By relatedRecordSaveBtn			= By.cssSelector(".save-related-records-fields");
	By relatedRecordCancelBtn		= By.cssSelector(".cancel-edit");
	By salesforceCampaignHeader 	= By.className("pageDescription");
	By allContactsLeadsFiedls		= By.cssSelector(".sfdc-field-label-container");
	By relatedAccountName 			= By.cssSelector("input[placeholder=\"Account Name\"]");
	String callerSMSIcon			= ".//*[@class='call-crm']//*[@class='phone-number' and text() ='$$PhoneNumber$$']/..//*[@class='quick-sms']/img";
	String relatedRecordLink		= ".//*[@class='related-record-container']//span[@class='name' and text() = '$$RelatedRecord$$']";
	String relatedRecordExpanIcon	= ".//*[@class='related-record-container']//span[@class='name' and contains(text(), '$$RelatedRecord$$')]/../following-sibling::div//*[@class= 'toggle-fields']";
	String relatedRecordCollapseIcn	= ".//*[@class='related-record-container']//span[@class='name' and text() =  '$$RelatedRecord$$']/../following-sibling::div//*[@class= 'toggle-fields expanded']";

	String relatedRecordNames		= ".//*[@class='related-record-container']//span[@class='name' and text() ='$$RelatedRecord$$']/ancestor::div[@class='related-record']//span[@class='display-name']";
	
	//Custom fields
	By customFieldList				= By.cssSelector("#sfdc-additional-fields-list .sfdc-field .display-name");
	By quickSmsIcon					= By.xpath("following-sibling::a[@class='custom-field-quick-sms']");
	String relatedRecordFieldsList	= ".//div[@class = 'related-record']//span[contains(text(),'$$RelatedRecord$$')]/../../..//*[@class='sfdc-field-name-container']//span[@class='display-name']";
	String relatedRecordField		= ".//div[div[@class = 'sfdc-fields-container collapse in']]//span[text()='$$CustomField$$']";
	String relatedRecordValue		= ".//*[contains(@class, 'sfdc-fields-container collapse in')]//div[contains(@class, 'sfdcFieldItem')]//span[text()='$$CustomField$$']/../following-sibling::div[@class='sfdc-field-item-value']//*[text()='$$Value$$' and not(contains(@style, 'display: none;'))]";
	String customField 				= ".//div[contains(@id, 'sfdc-additional-fields-list')]//span[text()='$$CustomField$$']";
	String customFieldValue			= ".//div[contains(@id, 'sfdc-additional-fields-list')]//span[text()='$$CustomField$$']/../following-sibling::div[@class='sfdc-field-item-value']//*[text()='$$Value$$' and not(contains(@style, 'display: none;'))]";
	String customFieldValueLoc		= ".//div[contains(@id, 'sfdc-additional-fields-list')]//span[text()='$$CustomField$$']/../following-sibling::div[@class='sfdc-field-item-value']/*[not(contains(@style, 'display: none;'))]";
	String customFieldCallBtn		= ".//div[contains(@id, 'sfdc-additional-fields-list')]//span[text()='$$CustomField$$']/../following-sibling::div[@class='sfdc-field-item-value']//*[text()='$$Value$$' and not(@style='display: none;')]/following-sibling::a[@class='custom-field-call']/img";
	String sfAdditionalField		= ".//div[contains(@id, 'sfdc-additional-fields-list')]//span[@class='display-name' and text()='$fieldName$']";
	String customFieldMsgIcon		= ".//*[@class='custom-field-number' and text() = '$$PhoneNumber$$']/following-sibling::a[@class='custom-field-quick-sms']";
	String customFieldEmailIcon		= ".//*[@class='sfdc-field-item-value']//a[text()='$$Email$$']/following-sibling::a[@class='custom-field-quick-email']";
	
	String relatedRecordValueList   = ".//div[@class = 'sfdc-field-name-container']/span[text()='$fieldName$']/../..//div[@class='sfdc-field-item-value']/div[@class='display-text']";
	
	//Call Disposition Warning
	By callDispositionWarning		= By.cssSelector(".crm-alert span");
	By callDispModalWindowHeading	= By.xpath(".//h3[text()='Missing Disposition']");
	By MultiMatchModalWindowHeading	= By.xpath(".//h3[text()='Unselected Callers']");
	By alertText					= By.cssSelector("#error p");
	By reviewCallButton				= By.cssSelector("[data-action='review-calls']");
	By warningCloseBtn				= By.cssSelector(".crm-alert-dismiss img");
	By unblockButton				= By.cssSelector(".crm-single:not([style='display: none;']) button.unblock,.crm-create:not([style='display: none;']) button.unblock,.crm-multiple:not([style='display: none;']) button.unblock");
	By infoCloseBtn					= By.cssSelector(".alert-info button.close");
	
	//Header elements
	By outboundNumberVisible 		= By.cssSelector(".outbound-number-view .outbound-number");
	By outboundBar					= By.className("outbound-number-view");
	By activeRecording				= By.cssSelector(".status-recording.blink");
	By activePauseRecording			= By.cssSelector(".status-recording-active");
	By inactiveRecording			= By.className("status-active");
	By recordinganouncementIcon		= By.cssSelector(".outbound-recording-announcement-play");
	
	//Add To Existing Contact
	By addToExistingButton			= By.cssSelector(".crm-create[style='display: block;'] [data-action='update-existing'], .update-crm-match:not([style='display: none;']) [data-action='update-existing']");
	By existingContactSearchBox		= By.cssSelector("#contact-search-header input.search-term");
	By existingContactSearchButton	= By.cssSelector("#contact-search-header [data-action='search']");
	By existingcontactNameList		= By.cssSelector("#search-results-container .name");
	By existingcontactSelectButtons	= By.cssSelector("#search-results-container .select");
	By existingcontactSearchedNums	= By.cssSelector("#search-results-container .number");
	By existingContactPencilIcon	= By.cssSelector("#search-results-container span.glyphicon-pencil");
	
	//Multiple associated contact
	By multipleDropdownSelectize	= By.cssSelector(".crm-multiple[style='display: block;'] .selectize-input.items, .updated-multiple[style='display: inline-block;'] .selectize-input.items"); 
	By multipleDropdownOptions		= By.xpath("//div[contains(@class,'crm-multiple')][@style='display: block;']//div[contains(@data-value, '00Q') or contains(@data-value, '003')]|//div[contains(@class,'updated-multiple')][@style='display: inline-block;']//div[contains(@class,'selectize-dropdown')]//div[contains(@data-value, '00Q') or contains(@data-value, '003')]");
	By multipleDropdownInput		= By.cssSelector(".crm-multiple[style='display: block;'] .selectize-input.items input, .updated-multiple[style='display: inline-block;'] .selectize-input.items input");
	By multipleDropdownSelected		= By.cssSelector(".crm-multiple[style='display: block;'] .selectize-input.items div, .updated-multiple[style='display: inline-block;'] .selectize-input.items div");
	
	//Customized messag
	By customizedMessage		= By.className("contact-notification-card");
	By customizedMsgCloseBtn	= By.xpath(".//*[@class = 'contact-notification-card']//*[text()='close-icon']/..");
	
	//User Image 
	static By userImageBusy 		= By.xpath("//li[@class='flip-container flip']//img[@class='user-image busy']");
	By userImageAvailable			= By.xpath("//li[@class='flip-container']//img[@class='user-image available']");
	By userImageWrapUpLoc			= By.className("wrap-up-progress-bar");
	By wrapUpTimer					= By.cssSelector(".wrap-up-timer");
	By wrapUpSelector				= By.cssSelector(".wrap-up-selector");
	By userWrapUpProgressBar		= By.cssSelector(".wrap-up-progress-bar svg path");
	By customStatusTimer			= By.xpath(".//*[@class='user-countdown-label' and not(text() = '...')]");

	//Bottom error 
	By errorCloseSpan				= By.cssSelector("button.close-error span");
	By errorText					= By.cssSelector(".alert-danger .message-text");
	
	//update link and buttons
	String callerDetailsUpdateLinkCss = ".call-crm .update-record span.glyphicon-pencil";
	By updateCancelLink				  = By.cssSelector(".cancel-update span");
	By updateAddNewButton			  = By.cssSelector(".update-crm-match [data-action='create-new-lead-contact']");
	
	By lightningDialogueCloseButton = By.cssSelector("#tryLexDialog [title='Close']");
	
	By frwrdingdeviceNotAvailableMsg = By.xpath(".//*[@class='message-text' and text()='Forwarding device not available. [status: busy]']");
	
	//phone match
	By phoneMatchOptions = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-up']/parent::h4");
	By phoneMatchAccount = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-up']/parent::h4[@class = 'search-header haveAccounts']");
	By phoneMatchLeads = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-up']/parent::h4[@class = 'search-header haveLeads']");
	By phoneMatchContacts = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-up']/parent::h4[@class = 'search-header haveContacts']");
	
	By dialNextBtn				= By.cssSelector(".dial-next");
	By dialNextUser				= By.cssSelector(".caller.can-open");
	
	public static enum LeadStatusType{
		  Open,
		  Contacted,
		  Qualified,
		  Unqualified
	  }
	  
	  public static enum WrapUpTimeExtensions{
		  Thirty("30"),
		  Sixty("60"),
		  Busy("-1");
		  
		  private String displayName;
		  
		  WrapUpTimeExtensions(String displayName) {
				this.displayName = displayName;
			}

			public String displayName() { return displayName; }

			@Override
			public String toString() { return displayName; }
	  }
	
	public void clickOnUpdateDetailLink(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		executeJavaSctipt(driver, "document.querySelector('" + callerDetailsUpdateLinkCss + "').click();");
		isElementVisible(driver, addToExistingButton, 5);
	}
	
	public File hoverOnNameContainer(WebDriver driver) {
		hoverElement(driver, nameContainerLoc);
		idleWait(1);
		return HelperFunctions.captureElementScreenShot(driver, findElement(driver, nameContainerLoc));
	}
	
	public void verifyUpdateDetailIconVisible(WebDriver driver) {
		hoverElement(driver, nameContainerLoc);
		idleWait(1);
		waitUntilVisible(driver, By.cssSelector(callerDetailsUpdateLinkCss));
	}
	
	public void verifyAllContactLeadFields(WebDriver driver, String contactType) {
		List<String> AllFields = getTextListFromElements(driver, allContactsLeadsFiedls);
		
		List<String> contactFields = new ArrayList<String>();
		contactFields.add("First Name:");
		contactFields.add("Last Name:");
		contactFields.add("Title:");
		contactFields.add("Company:");
		contactFields.add("Lead Status:");
		contactFields.add("Account Name:");
		contactFields.add("Phone");
		
		//verifying that the elements are visible
		getElements(driver, allContactsLeadsFiedls);
		if(contactType.equals("Contact")) {
			contactFields.remove("Company:");
			contactFields.remove("Lead Status:");
			assertEquals(AllFields, contactFields);
		}else {
			contactFields.remove("Account Name:");
			//contactFields.add("RingDNA WebLead:");
			assertEquals(AllFields, contactFields);
		}
	}
	
	public void waitForFrwrdingDeviceNotAvailableMsg(WebDriver driver) {
		waitUntilVisible(driver, frwrdingdeviceNotAvailableMsg);
	}
	
	public void isUpdateDetailLinkInvisible(WebDriver driver) {
		executeJavaSctipt(driver, "document.querySelector('" + callerDetailsUpdateLinkCss + "').click();");
		assertFalse(isElementVisible(driver, updateCancelLink, 5));
	}
	
	public void updateContact(WebDriver driver, String firstName, String company) {
		clickOnUpdateDetailLink(driver);
		addContactForExistingCaller(driver, firstName, company);
	}
	
	public void verifyCallerIsContact(WebDriver driver) {
		waitUntilVisible(driver, callerContactIcon);
	}
	
	public void verifyCallerIsLead(WebDriver driver) {
		waitUntilVisible(driver, callerLeadIcon);
	}

	public boolean isCallerMultiple(WebDriver driver) {
		if(!isElementVisible(driver, callerName, 5) && isElementVisible(driver, multipleDropdownSelectize, 0) && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public void selectFirstContactIfMultiple(WebDriver driver) {
		if(isElementVisible(driver, multipleDropdownSelectize, 0)) {
		  selectFirstContactFromMultiple(driver);
	    }
	}
	
	public List<String> getMultipleContactsName(WebDriver driver) {
		clickElement(driver, multipleDropdownSelectize);
		List<String> users = getTextListFromElements(driver, multipleDropdownOptions);
		List<String> usersList = new ArrayList<String>();
		
		for (String user : users) {
			usersList.add(user.split("\n")[0].trim());
		}
		return usersList;
	}
	
	public void selectFirstContactFromMultiple(WebDriver driver) {
		if(getWebelementIfExist(driver, multipleDropdownSelectize)!=null && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed()) {
			idleWait(2);
			clickElement(driver, multipleDropdownSelectize);
			clickElement(driver, multipleDropdownOptions);
			waitUntilInvisible(driver, multipleDropdownSelectize);
		}
	}
	
	public void selectSecondContactFromMultiple(WebDriver driver) {
		if(getWebelementIfExist(driver, multipleDropdownSelectize)!=null && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed()) {
			clickElement(driver, multipleDropdownSelectize);
			idleWait(1);
			clickByJs(driver, getElements(driver, multipleDropdownOptions).get(1));
		}
	}
	
	public int getContactsCountInMultiDropDown(WebDriver driver) {
		if(getWebelementIfExist(driver, multipleDropdownSelectize)!=null && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed()) {
			idleWait(2);
			clickElement(driver, multipleDropdownSelectize);
			return getElements(driver, multipleDropdownOptions).size();
		}
		return -1;
	}
	
	public String getSelectedMultipleContact(WebDriver driver){
		waitUntilVisible(driver, multipleDropdownSelected);
		return getElementsText(driver, multipleDropdownSelected);
	}
	
	public void clickUserImage(WebDriver driver) {
		clickElement(driver, userImageWrapUpLoc);
	}
	
	public void hoverOnUserImage(WebDriver driver) {
		hoverElement(driver, userImageWrapUpLoc);
	}
	
	public void setUserImageBusy(WebDriver driver) {
		if(getWebelementIfExist(driver, userImageAvailable)!=null) {
			clickElement(driver, userImageWrapUpLoc);
			waitUntilVisible(driver, userImageBusy);
			System.out.println("setting user image busy");
		} else {
			findElement(driver, userImageBusy);
			System.out.println("User Image already busy");
		}
	}
	
	public void verifyUserImageBusy(WebDriver driver){
		waitUntilVisible(driver, userImageBusy);
		assertTrue( isElementVisible(driver, userImageBusy, 2));
	}
	
	public void verifyUserImageAvailable(WebDriver driver){
		assertTrue( isElementVisible(driver, userImageAvailable, 2));
	}
	
	public void setUserImageAvailable(WebDriver driver) {
		if(getWebelementIfExist(driver, userImageBusy)!=null) {
			clickElement(driver, userImageWrapUpLoc);
			waitUntilVisible(driver, userImageAvailable);
		} else {
			findElement(driver, userImageAvailable);
			System.out.println("User Image already available");
		}
	}
	
	public boolean isWrapUpProgressBarVisible(WebDriver driver) {
		return isElementVisible(driver, userWrapUpProgressBar, 0);
	}
	
	public int getWrapUpTime(WebDriver driver){
		waitUntilVisible(driver, wrapUpTimer);
		String wrapUpTime = getElementsText(driver, wrapUpTimer).replace("s", "");
		return Integer.parseInt(wrapUpTime);
	}
	
	public int getCustomStatusTimeInSecs(WebDriver driver){
		waitUntilVisible(driver, customStatusTimer);
		String[] wrapUpTime = getElementsText(driver, customStatusTimer).split(" ");
		return Integer.parseInt(wrapUpTime[0].replace("m", "")) * 60 + Integer.parseInt(wrapUpTime[1].replace("s", ""));
	}
	
	public void extendWrapUpTime(WebDriver driver, WrapUpTimeExtensions time){
		selectFromDropdown(driver, wrapUpSelector, SelectTypes.value, time.displayName().toString());
	}
	
	public void verifyWrapUpTimeInvisible(WebDriver driver){
		assertTrue(isElementInvisible(driver, wrapUpTimer, 5));
	}
	
	public Boolean isAddNewButtonVisible(WebDriver driver) {
		if(getWebelementIfExist(driver, addNewButtton)!=null && getWebelementIfExist(driver, addNewButtton).isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public String getCallerName(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilClickable(driver, callerName);
		waitUntilVisible(driver, callerName);
		return getElementsText(driver, callerName);		
	}

	public String getForeGroundCallerName(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilClickable(driver, foreGroundCallName);
		waitUntilVisible(driver, foreGroundCallName);
		return getElementsText(driver, foreGroundCallName);		
	}
	
	public void verifyCallerNameIsInvisible(WebDriver driver, String callerFullName) {
		waitUntilTextNotPresent(driver, callerName, callerFullName);
	}
	
	public boolean isLeadImageVisible(WebDriver driver){
		return isElementVisible(driver, leadImage, 5);
	}
	
	public String getCallerVoicemail(WebDriver driver) {
		waitUntilClickable(driver, callerVoicemail);
		return getElementsText(driver, callerVoicemail);		
	}

	public String getCallerNumber(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callerNumber);
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, callerNumber));
	}
	
	public String getCallerNumberOriginalFormat(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callerNumber);
		return getElementsText(driver, callerNumber);
	}
	
	public String getCallerOtherNumber(WebDriver driver, int index) {
		waitUntilVisible(driver, callerotherNumber);
		return getElementsText(driver, getElements(driver, callerotherNumber).get(index));
	}

	public String getCallerTitle(WebDriver driver) {
		return getElementsText(driver, callerTitle);
	}
	
	public String getCallerRating(WebDriver driver) {
		return Integer.toString(getElements(driver, callerRating).size());
	}

	public String getCallerCompany(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callerCompany);
		return getElementsText(driver, callerCompany);
	}
	
	public String getCallDisposition(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callDisposition);
		return getElementsText(driver, callDisposition);
	}
	
	public void clickCallerCompany(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, callerCompany);
		clickElement(driver, callerCompany);
	}
	
	public String getCallerEmail(WebDriver driver) {
		waitUntilClickable(driver, callerEmail);
		return getElementsText(driver, callerEmail);
	}
	
	public void clickCallerName(WebDriver driver) {
		idleWait(2);
		if(isElementVisible(driver, multipleDropdownSelectize, 0)) {
		  selectFirstContactFromMultiple(driver);
	    }
		waitUntilVisible(driver, callerName);
		clickElement(driver, callerName);
	}
	
	public void verifyPhoneNumberNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, phoneNumberLabel);
		waitUntilInvisible(driver, phoneNumberValue);
	}

	public String getNumberOfCallers(WebDriver driver){
		waitUntilVisible(driver, numberOfCallers);
		return getElementsText(driver, numberOfCallers).replace("+", "");
	}
	
	public void verifyNumberOfCallersDisplayed(WebDriver driver){
		assertTrue(isElementVisible(driver, numberOfCallers, 0));
	}
	
	public void verifyNumberOfCallersNotDisplayed(WebDriver driver){
		assertFalse(isElementVisible(driver, numberOfCallers, 0));
	}
	
	public String getNumberOfCallersHoverText(WebDriver driver){
		return getAttribue(driver, numberOfCallers, ElementAttributes.Title);
	}
	
	public String getLeadStatus(WebDriver driver){
		return getElementsText(driver, leadStatusSelected);
	}
	
	public String getCallerSource(WebDriver driver){
		return getElementsText(driver, contactSource);
	}
	
	public String getCampaign(WebDriver driver){
		return getElementsText(driver, campaign);
	}
	
	public void verifyRelatedRecordPresent(WebDriver driver, String relatedRecord){
		By relatedRecordLoc = By.xpath(relatedRecordLink.replace("$$RelatedRecord$$", relatedRecord));
		waitUntilVisible(driver, relatedRecordLoc);
		assertEquals(getElements(driver, relatedRecordLoc).size(), 1);
	}
	
	public void openRelatedRecordInSfdc(WebDriver driver, String relatedRecord){
		By relatedRecordLoc = By.xpath(relatedRecordLink.replace("$$RelatedRecord$$", relatedRecord));
		waitUntilVisible(driver, relatedRecordLoc);
		clickElement(driver, relatedRecordLoc);
		switchToSFTab(driver, getTabCount(driver));
	}
	
	public void expandRelatedRecordDetails(WebDriver driver, String relatedRecord){
		By relatedRecordExpandLoc = By.xpath(relatedRecordExpanIcon.replace("$$RelatedRecord$$", relatedRecord));
		if(isElementVisible(driver, relatedRecordExpandLoc, 0)) {
			clickElement(driver, relatedRecordExpandLoc);
		}else{
			System.out.println(relatedRecord + "'s info already expanded");
		}
	}
	
	public void verifyRelatedRecordExpandIconNotPresent(WebDriver driver, String relatedRecord) {
		By relatedRecordExpandLoc = By.xpath(relatedRecordExpanIcon.replace("$$RelatedRecord$$", relatedRecord));
		waitUntilInvisible(driver, relatedRecordExpandLoc);
	}
	
	/**
	 * @param driver
	 * @param relatedRecord
	 * @return
	 */
	public List<String> getRelatedRecordNameList(WebDriver driver, String relatedRecord) {
		By relatedRecordListLoc = By.xpath(relatedRecordNames.replace("$$RelatedRecord$$", relatedRecord));
		isListElementsVisible(driver, relatedRecordListLoc, 5);
		return getTextListFromElements(driver, relatedRecordListLoc);
	}
	
	public void verifyRelatedRecordNotPresent(WebDriver driver, String relatedRecord){
		waitUntilInvisible(driver, By.xpath(relatedRecordLink.replace("$$RelatedRecord$$", relatedRecord)));
	}
	
	public int  getRelatedRecordIndex(WebDriver driver, String relatedRecord) {
		waitUntilInvisible(driver, By.cssSelector(".related-records-list.disabled-record-list"));
		List<String> relatedRecordsList = getTextListFromElements(driver, relatedRecordNamesList);
		return relatedRecordsList.indexOf(relatedRecord);
	}
	
	public void selectRelatedRecordToFilter(WebDriver driver, String relatedRecord){
		int index = getRelatedRecordIndex(driver, relatedRecord);
		clickByJs(driver, getInactiveElements(driver, relatedRecordfilter).get(index));
	}
	
	public void verifyEditRelatedRecordIconInvisible(WebDriver driver, int index) {
		waitUntilInvisible(driver, getInactiveElements(driver, relatedRecordEdit).get(index));
	}
	
	public void verifyAllRelatedRecordIconsVisible(WebDriver driver, String relatedRecord ) {
		int index = getRelatedRecordIndex(driver, relatedRecord);
		getInactiveElements(driver, relatedRecordfilter).get(index);
		waitUntilVisible(driver, getInactiveElements(driver, relatedRecordEdit).get(index));
		waitUntilVisible(driver, getInactiveElements(driver, relatedRecordexpand).get(index));
	}
	
	public void verifyEditedRelatedRecordIconsInvisible(WebDriver driver, int index) {
		waitUntilInvisible(driver, getInactiveElements(driver, relatedRecordfilter).get(index));
		waitUntilInvisible(driver, getInactiveElements(driver, relatedRecordEdit).get(index));
		waitUntilInvisible(driver, getInactiveElements(driver, relatedRecordexpand).get(index));
	}
	
	public void editRelatedRecord(WebDriver driver, String relatedRecord){
		int index = getRelatedRecordIndex(driver, relatedRecord);
		getInactiveElements(driver, relatedRecordEdit).get(index).click();
		waitUntilVisible(driver, relatedRecordSaveBtn);
	}
	
	public void cancelEditedRelatedRecord(WebDriver driver) {
		clickElement(driver, relatedRecordCancelBtn);
		waitUntilInvisible(driver, relatedRecordCancelBtn);
	}
	
	public void enterRelatedAccountName(WebDriver driver, String accountName) {
		waitUntilVisible(driver, relatedAccountName);
		enterText(driver, relatedAccountName, accountName);
		clickElement(driver, relatedRecordSaveBtn);
		verifyRelatedRecordPresent(driver, accountName);
		cancelInfoBar(driver);
	}
	
	
	public void removeOpportunityStage(WebDriver driver) {
		By opportunityStageLocator = By.xpath(".//*[text()='Stage:']/../following-sibling::div//div[@class='item']");
		By opportunityStageLocatorInput = By.xpath(".//*[text()='Stage:']/../following-sibling::div//div/input");
		clickElement(driver, opportunityStageLocator);
		enterBackspace(driver, opportunityStageLocatorInput);
		pressEscapeKey(driver);
		clickElement(driver, relatedRecordSaveBtn);
		getWebelementIfExist(driver, relatedRecordSaveBtn).click();
	}
	public void verifyCampaignOpensInSalesforce(WebDriver driver, String campaignName){
		int index = getRelatedRecordIndex(driver, campaignName);
		List<WebElement> relatedRecordsLinkList = getElements(driver, relatedRecordNamesList);
		relatedRecordsLinkList.get(index).click();
		switchToSFTab(driver, getTabCount(driver));
		assertEquals(getElementsText(driver, salesforceCampaignHeader), campaignName);
		closeTab(driver);
		switchToTab(driver, getTabCount(driver));
	}
	
	public void verifyRelatedRecordHeadingInvisible(WebDriver driver){
		waitUntilInvisible(driver, relatedRecordHeading);
	}
	
	public void selectLeadStatus(WebDriver driver, LeadStatusType leadStatus){
		clickAndSelectFromDropDown(driver, leadStatusDropDown, leadStatusDrpDwnOption, leadStatus.toString());
		//selectFromDropdown(driver, leadStatusDropDown, SelectTypes.visibleText, leadStatus.toString());
	}
	
	public void clickMsgIconByNumber(WebDriver driver, String number){
		By msgIcon = By.xpath(callerSMSIcon.replace("$$PhoneNumber$$", number));
		clickElement(driver, msgIcon);
		waitUntilVisible(driver, callerName);
	}
	
	public void verifyMsgIconVisible(WebDriver driver, String number){
		By msgIcon = By.xpath(callerSMSIcon.replace("$$PhoneNumber$$", number));
		waitUntilVisible(driver, msgIcon);
	}
	
	public void verifyMsgIconInvisible(WebDriver driver, String number){
		By msgIcon = By.xpath(callerSMSIcon.replace("$$PhoneNumber$$", number));
		waitUntilInvisible(driver, msgIcon);
	}
	
	public Map<String, String> getCallerDetails(WebDriver driver) {
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails.put("Name", getCallerName(driver));
		contactDetails.put("Number", getCallerNumber(driver));
		contactDetails.put("Title", getCallerTitle(driver));
		contactDetails.put("Company", getCallerCompany(driver));
		return contactDetails;
	}

	public String openCallerDetailPageWithCallNotes(WebDriver driver) {
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		idleWait(5);
		callToolsPanel.changeCallSubject(driver, callSubject);
		clickCallerName(driver);
		switchToSFTab(driver, getTabCount(driver));
		idleWait(2);
		reloadSoftphone(driver);
		return callSubject;
	}
	
	public void openCallerDetailPage(WebDriver driver) {
		idleWait(5);
		clickCallerName(driver);
		switchToSFTab(driver, getTabCount(driver));
		reloadSoftphone(driver);
	}
	
	public void openAccountDetailPage(WebDriver driver) {
		clickCallerCompany(driver);
		switchToSFTab(driver, getTabCount(driver));
		reloadSoftphone(driver);
	}
	
	public void deleteCallerObject(WebDriver driver) {
		openCallerDetailPage(driver);
		contactDetailPage.deleteContact(driver);
		driver.close();
		switchToTab(driver, 1);
	}
	
	public boolean isCallerUnkonwn(WebDriver driver) {
		if(getWebelementIfExist(driver, addNewButtton)!=null && getWebelementIfExist(driver, addNewButtton).isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public void clickAddNewButton(WebDriver driver){
		clickElement(driver, addNewButtton);
	}
	
	public void clickAddToExistingButton(WebDriver driver){
		clickElement(driver, addToExistingButton);
	}
	
	public void searchExistingContact(WebDriver driver, String existingContactName){
		enterTextAndExistingContact(driver, existingContactName);
		List<WebElement> existingContacts = getElements(driver, existingcontactNameList);
		for (WebElement existingContact : existingContacts) {
			assertTrue(existingContact.getText().contains(existingContactName), "Searched Existing contact "
					+ existingContactName + " names are not same " + existingContact.getText());
		}
	}
	
	public void enterTextAndExistingContact(WebDriver driver, String existingContactName){
		waitUntilVisible(driver, existingContactSearchBox);
		enterText(driver, existingContactSearchBox, existingContactName);
		clickElement(driver, existingContactSearchButton);
		waitUntilInvisible(driver, existingContactPencilIcon);
	}
	
	
	/**
	 * @param indexForNumberType 0 for primary, 1 for mobile, 2 for home, 3 for other
	 * @return
	 */
	public String getExistingContactSearchedNumbers(WebDriver driver, int indexForNumberType) {
		return getElementsText(driver, getInactiveElements(driver, existingcontactSearchedNums).get(indexForNumberType));
	}
	
	public void selectFirstExistingContact(WebDriver driver){
		getElements(driver, existingcontactSelectButtons).get(0).click();
	}
	
	public void addCallerAsLead(WebDriver driver, String firstName, String company) {
		if (getWebelementIfExist(driver, addNewButtton) != null && getWebelementIfExist(driver, addNewButtton).isDisplayed()) {
			addCallerToMultipleLead(driver, firstName, company);
		}
		if (getWebelementIfExist(driver, multipleDropdownSelectize) != null && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed()) {
			System.out.println("Caller is added as multiple");
			return;
		} else {
			System.out.println("Caller already added");
		}
	}
	
	public void addCallerToExistingContact(WebDriver driver, String existingContactName) {
		waitUntilInvisible(driver, spinnerWheel);
		idleWait(5);
		clickAddToExistingButton(driver);
		searchExistingContact(driver, existingContactName);
		waitUntilInvisible(driver, existingContactPencilIcon);
		getElements(driver, existingcontactSelectButtons).get(0).click();
		if(isElementVisible( driver, infoCloseBtn, 5)) {
			clickElement(driver, infoCloseBtn);
			waitUntilInvisible(driver, infoCloseBtn);
		}
	}
	
	public void addMobileCallerToExistingContact(WebDriver driver, String existingContactName) {
		waitUntilInvisible(driver, spinnerWheel);
		clickElement(driver, addToExistingButton);
		waitUntilVisible(driver, existingContactSearchBox);
		enterText(driver, existingContactSearchBox, existingContactName);
		clickElement(driver, existingContactSearchButton);
		getElements(driver, existingcontactSelectButtons).get(1).click();
	}
	
	public void addHomeCallerToExistingContact(WebDriver driver, String existingContactName) {
		waitUntilInvisible(driver, spinnerWheel);
		clickElement(driver, addToExistingButton);
		waitUntilVisible(driver, existingContactSearchBox);
		enterText(driver, existingContactSearchBox, existingContactName);
		clickElement(driver, existingContactSearchButton);
		getElements(driver, existingcontactSelectButtons).get(2).click();
	}
	

	public void addOtherCallerToExistingContact(WebDriver driver, String existingContactName) {
		waitUntilInvisible(driver, spinnerWheel);
		clickElement(driver, addToExistingButton);
		waitUntilVisible(driver, existingContactSearchBox);
		enterText(driver, existingContactSearchBox, existingContactName);
		clickElement(driver, existingContactSearchButton);
		getElements(driver, existingcontactSelectButtons).get(3).click();
	}
	
	public void addCallerAsContact(WebDriver driver, String firstName, String company) {
		if(getWebelementIfExist(driver, multipleDropdownSelectize)!=null && getWebelementIfExist(driver, multipleDropdownSelectize).isDisplayed())
		{
			System.out.println("Caller is added as multiple");
			return;
		}
		if(getWebelementIfExist(driver, addNewButtton)!=null && getWebelementIfExist(driver, addNewButtton).isDisplayed()) {
			addNewContact(driver, firstName, company);
		} else {
			System.out.println("Caller already added");
		}
	}
	
	public void addNewContact(WebDriver driver, String firstName, String company) {
		clickAddNewButton(driver);
		enterContactDetailsAndSave(driver, firstName, company);
	}
	
	public void enterContactDetailsAndSave(WebDriver driver, String firstName, String company){
		enterContactDetails(driver, firstName, company);
		clickSaveContactButton(driver);
	}
	
	public void enterContactDetails(WebDriver driver, String firstName, String company){
		clickElement(driver, createContactRadioBox);
		enterText(driver, firstNameInputBox, firstName);
		enterText(driver, lastNameInputBox, "Automation");
		enterText(driver, titleInputBox, "QA");
		enterText(driver, contactAccountInputBox, company);
		clickElement(driver, contactAccountSearch);
		getElements(driver, accountSelectOptions).get(1).click();
		waitUntilInvisible(driver, accountSelectOptions);
	}
	
	public void changeTitle(WebDriver driver, String Title) {
		enterText(driver, titleInputBox, Title);
	}
	
	public void enterContactDetailsWithNewAccount(WebDriver driver, String firstName, String company){
		clickElement(driver, createContactRadioBox);
		enterText(driver, firstNameInputBox, firstName);
		enterText(driver, lastNameInputBox, "Automation");
		enterText(driver, titleInputBox, "QA");
		enterText(driver, contactAccountInputBox, company);
		clickElement(driver, contactAccountSearch);
		getElements(driver, accountSelectOptions).get(0).click();
		waitUntilVisible(driver, addNewAccountName);
		enterText(driver, addNewAccountName, company);
		clickElement(driver, addAccountSaveButton);
		waitUntilInvisible(driver, addAccountSaveButton);
	}
	
	public String getSelectedAccount(WebDriver driver){
		return getAttribue(driver, findElement(driver, contactAccountInputBox), ElementAttributes.value);
	}
	
	public void enterAllAdditionalNumber(WebDriver driver, String phoneNumber, String mobNumber, String HomeNumber, String otherNumber, String AssistantNumber) {
		enterPhoneNumber(driver, phoneNumber);
		
		clickElement(driver, addAdditionalNumbers);
		waitUntilVisible(driver, mobNumTextBox);
		enterText(driver, mobNumTextBox, mobNumber);
		
		clickElement(driver, addAdditionalNumbers);
		waitUntilVisible(driver, HomeNumTextBox);
		enterText(driver, HomeNumTextBox, HomeNumber);
		
		clickElement(driver, addAdditionalNumbers);
		waitUntilVisible(driver, OtherNumTextBox);
		enterText(driver, OtherNumTextBox, otherNumber);
		
		clickElement(driver, addAdditionalNumbers);
		waitUntilVisible(driver, AssistantNumTextBox);
		enterText(driver, AssistantNumTextBox, AssistantNumber);
		
		waitUntilInvisible(driver, addAdditionalNumbers);
	}
	
	public void enterMobileNumberForContact(WebDriver driver, String mobNumber) {
		clickElement(driver, addAdditionalNumbers);
		waitUntilVisible(driver, mobNumTextBox);
		enterText(driver, mobNumTextBox, mobNumber);
	}
	
	public void enterPhoneNumber(WebDriver driver, String phoneNumber) {
		enterText(driver, phoneNumTextBox, phoneNumber);
	}
	
	public void clickSaveContactButton(WebDriver driver) {
		waitUntilVisible(driver, saveAddContactButton);
		clickElement(driver, saveAddContactButton);
		waitUntilInvisible(driver, saveAddContactButton);
		cancelInfoBar(driver);
	}
	
	public void clickSaveFieldsButton(WebDriver driver) {
		waitUntilVisible(driver, saveFieldsButton);
		clickElement(driver, saveFieldsButton);
		waitUntilInvisible(driver, saveFieldsButton);
	}
	
	public void clickCancelContactButton(WebDriver driver) {
		waitUntilVisible(driver, cancelAddContactButton);
		clickElement(driver, cancelAddContactButton);
		waitUntilInvisible(driver, cancelAddContactButton);
	}
	
	public void clickCancelAddToExstingButton(WebDriver driver) {
		waitUntilVisible(driver, cancelAddToExistingBtn);
		clickElement(driver, cancelAddToExistingBtn);
		waitUntilInvisible(driver, cancelAddContactButton);
	}
	
	public boolean isNewDefaultSelected(WebDriver driver, String contact) {
		By locator = null;
		switch(contact) {
		case "Lead":
			locator = createLeadRadioBox;
			break;
		case "Contact":
			locator = createContactRadioBox;
			break;
		}
		return findElement(driver, locator).isSelected();
	}
	
	public void selectLeadOption(WebDriver driver){
		clickElement(driver, createLeadRadioBox);
	}
	
	public boolean isLeadOptionSelected(WebDriver driver) {
		return findElement(driver, createLeadRadioBox).isSelected();
	}
	
	public boolean isContactOptionSelected(WebDriver driver) {
		return findElement(driver, createContactRadioBox).isSelected();
	}
	
	public void addCallerToMultipleLead(WebDriver driver, String firstName, String company) {
		waitUntilVisible(driver, addNewButtton);
		clickAddNewButton(driver);
		selectLeadOption(driver);
		addLeadDetailsAndSave(driver, firstName, company);
	}
	
	public void addLeadDetailsAndSave(WebDriver driver, String firstName, String company){
		addLeadDetails(driver, firstName, company);
		clickSaveContactButton(driver);
		Map<String, String> callerDetails = new HashMap<String, String>();
		callerDetails = getCallerDetails(driver);
		//assertEquals(callerDetails.get("Name").contains(firstName.trim()), true);
		assertEquals(callerDetails.get("Company").contains(company), true);
	}
	
	public void addLeadDetails(WebDriver driver, String firstName, String company){
		enterText(driver, firstNameInputBox, firstName);
		enterText(driver, lastNameInputBox, "Automation");
		enterText(driver, titleInputBox, "QA");
		enterText(driver, leadCompanyInputBox, company);
	}
	
	public void addContactForExistingCaller(WebDriver driver, String firstName, String company) {
		clickElement(driver, updateAddNewButton);
		enterContactDetailsAndSave(driver, firstName, company);
	}
	
	public void addContactWithDefaultValues(WebDriver callerDriver, WebDriver contactDriver, String contactNumber){
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(callerDriver, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(contactDriver);

		// Deleting contact and calling again
		if (!isCallerUnkonwn(callerDriver)) {
			System.out.println("deleting contact");
			deleteCallerObject(callerDriver);
			softPhoneCalling.hangupActiveCall(callerDriver);
			softPhoneCalling.softphoneAgentCall(callerDriver, contactNumber);
			softPhoneCalling.pickupIncomingCall(contactDriver);
		}

		// add caller as Contact
		addCallerAsContact(callerDriver, SoftphoneBase.CONFIG.getProperty("contact_first_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));

		// hangup Call
		softPhoneCalling.hangupActiveCall(callerDriver);

		// wait for contact to get sync
		idleWait(15);
		
		reloadSoftphone(callerDriver);
	}
	
	public void verifyDispositionReqWarningNotVisible(WebDriver driver){
		waitUntilInvisible(driver, callDispositionWarning);
	}
	
	public void verifyDispositionRequiredWarning(WebDriver driver){
		assertEquals(getElementsText(driver, callDispositionWarning), "You have not entered a Call Disposition.");
	}
	
	public void verifyMultiMatchRequiredWarning(WebDriver driver){
		assertEquals(getElementsText(driver, callDispositionWarning), "You have not selected a caller.");
	}
	
	public void verifyMultipleRequiredWarning(WebDriver driver){
		assertEquals(getElementsText(driver, callDispositionWarning), "Please select a Caller and Call Disposition.");
	}
	
	public void clickDispositionWarning(WebDriver driver){
		clickElement(driver, callDispositionWarning);
	}
	
	public void closeWarning(WebDriver driver) {
		clickElement(driver, warningCloseBtn);
		verifyDispositionReqWarningNotVisible(driver);
	}
	
	public void isMissingDispositionWindowVisible(WebDriver driver){
		waitUntilVisible(driver, callDispModalWindowHeading);
	}
	
	public boolean isCloseWarningbuttonVisible(WebDriver driver){
		return isElementVisible(driver, warningCloseBtn, 0);
	}
	
	public void isMultiMatchWindowVisible(WebDriver driver){
		waitUntilVisible(driver, MultiMatchModalWindowHeading);
	}
	
	public String getAlertText(WebDriver driver) {
		waitUntilVisible(driver, alertText);
		return getElementsText(driver, alertText).trim();
	}
	
	public void clickReviewCallsButton(WebDriver driver){
		clickElement(driver, reviewCallButton);
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void verifyUnblockBtnVisible(WebDriver driver){
		System.out.println("verifying that unblock button is visible");
		waitUntilVisible(driver, unblockButton);
	}
	
	public void verifyUnblockBtnInvisible(WebDriver driver){
		System.out.println("verify that unblock button is invisible");
		waitUntilInvisible(driver, unblockButton);
	}
	
	public void clickUnblockButton(WebDriver driver){
		if(isElementVisible(driver, unblockButton, 5)){
			clickElement(driver, unblockButton);
		}
	}
	
	public boolean isSalesForceAdditionalNamePresent(WebDriver driver, String fieldName){
		By fieldLoc = By.xpath(sfAdditionalField.replace("$fieldName$", fieldName));
		return isElementVisible(driver, fieldLoc, 5);
	}
	
	/*******Header element functions*******/
	public String getOutboundNumber(WebDriver driver){
		idleWait(3);
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, outboundNumberVisible));
	}

	public boolean isOutboundBarVisible(WebDriver driver){
		try {
			return getWebelementIfExist(driver, outboundBar).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void verifyRecordingisActive(WebDriver driver){
		assertTrue(isElementVisible(driver, activeRecording, 5) || isElementVisible(driver, activePauseRecording, 0));
		waitUntilInvisible(driver, inactiveRecording);
	}
	
	public void verifyRecordingisInactive(WebDriver driver){
		waitUntilVisible(driver, inactiveRecording);
		assertFalse(isElementVisible(driver, activeRecording, 5) && isElementVisible(driver, activePauseRecording, 0));
	}
	
	public void verifyOnlyPauseRecordingBtnVisible(WebDriver driver) {
		waitUntilVisible(driver, activePauseRecording);
		waitUntilInvisible(driver, inactiveRecording);
		waitUntilInvisible(driver, activeRecording);
	}
	
	public void verifyRecordingAnouncementIconPresent(WebDriver driver){
		assertTrue(isElementInvisible(driver, recordinganouncementIcon, 5));
	}
	
	public String getInActiveRecordingColorSrc(WebDriver driver){
		waitUntilVisible(driver, inactiveRecording);
		return getAttribue(driver, inactiveRecording, ElementAttributes.src);  
	}
	
	public void clickEmailButton(WebDriver driver){
		clickElement(driver, emailButton);
		switchToTab(driver, getTabCount(driver)-1);
	}
	
	public void verifyEmailButtonVisible(WebDriver driver){
		waitUntilVisible(driver, emailButton);
	}
	
	public void verifyEmailButtonInvisible(WebDriver driver){
		waitUntilInvisible(driver, emailButton);
	}
	
	public void verifyCallBackButtonInvisible(WebDriver driver){
		waitUntilInvisible(driver, callButton);
	}
	
	public void closeLightningDialogueBox(WebDriver driver){
		if(isElementVisible(driver, lightningDialogueCloseButton, 1)){
			clickElement(driver, lightningDialogueCloseButton);
		}
	}	
	
	//This method is to close bottom red error bar
	public void closeErrorBar(WebDriver driver) {
		if(getWebelementIfExist(driver, errorCloseSpan)!=null && getWebelementIfExist(driver, errorCloseSpan).isDisplayed()){
			clickElement(driver, errorCloseSpan);
		}
	}
	
	
	public void cancelInfoBar(WebDriver driver) {
		  if(isElementVisible(driver, infoCloseBtn, 5)){
			  clickByJs(driver, infoCloseBtn);
			  waitUntilInvisible(driver, infoCloseBtn);
		  }
	}
	
	//Verify error present on softphone 
	public boolean verifyErrorPresent(WebDriver driver) {
		if(getWebelementIfExist(driver, errorCloseSpan)!=null && getWebelementIfExist(driver, errorCloseSpan).isDisplayed()){
			return true;
		} else {
			return false;
		}
	}
	
	//getting error text 
	public String getErrorText(WebDriver driver) {
		if(getWebelementIfExist(driver, errorText)!=null && getWebelementIfExist(driver, errorText).isDisplayed()){
			return getElementsText(driver, errorText);
		} else {
			System.out.println("Element is not present or not visible");
			return null;
		}
	}
	
	public void setAsFavoriteContact(WebDriver driver){
		if(isElementVisible(driver, createFavoriteIcon, 5)){
			clickElement(driver, createFavoriteIcon);
			idleWait(3);
		}
	}
	
	public void removeFromFavoriteContact(WebDriver driver){
		if(isElementVisible(driver, deleteFavoriteIcon, 5)){
			clickElement(driver, deleteFavoriteIcon);
			idleWait(3);
		}
	}
	
	/**method to get related record field list
	 * @param driver
	 * @param relatedRecord
	 * @return
	 */
	public List<String> getRelatedRecordsFieldsList(WebDriver driver, String relatedRecord){
		expandRelatedRecordDetails(driver, relatedRecord);
		return getTextListFromElements(driver, By.xpath(relatedRecordFieldsList.replace("$$RelatedRecord$$", relatedRecord)));
	}
	
	public void verifyRelatedRecordFields(WebDriver driver, String relatedRecordFieldtext, String value){
		By relatedRecordElement = By.xpath(relatedRecordField.replace("$$CustomField$$", relatedRecordFieldtext));
		waitUntilVisible(driver, relatedRecordElement);
		By RelatedRecordFieldValueElement = By.xpath(relatedRecordValue.replace("$$CustomField$$", relatedRecordFieldtext).replace("$$Value$$", value));
		waitUntilVisible(driver, RelatedRecordFieldValueElement);
	}
	
	/**
	 * @param driver
	 * @param relatedRecordFieldtext
	 */
	public boolean isRelatedRecordVisible(WebDriver driver, String relatedRecordFieldtext) {
		By relatedRecordElement = By.xpath(relatedRecordField.replace("$$CustomField$$", relatedRecordFieldtext));
		return isElementVisible(driver, relatedRecordElement, 5);
	}
	
	/**method to get all related field value with fields and values according to custom fields on web app
	 * @param driver
	 * @param customFieldTextList
	 * @return
	 */
	public Map<String, String> getAllRelatedFieldsMapValue(WebDriver driver, String relatedRecord, List<String> customFieldTextList){
		expandRelatedRecordDetails(driver, relatedRecord);
		Map<String, String> relatedFieldValueMap = new HashMap<String, String>();
		idleWait(2);
		for(String customFieldText: customFieldTextList) {
			By customFieldValueElement = By.xpath(relatedRecordValueList.replace("$fieldName$", customFieldText));
			String value =  getElementsText(driver, customFieldValueElement).trim();
			relatedFieldValueMap.put(customFieldText, value);
		}
		return relatedFieldValueMap;
	}
	
	public void verifyCustomFields(WebDriver driver, String customFieldtext, String value){
		By customFieldElement = By.xpath(customField.replace("$$CustomField$$", customFieldtext));
		waitUntilVisible(driver, customFieldElement);
		By customFieldValueElement = By.xpath(customFieldValue.replace("$$CustomField$$", customFieldtext).replace("$$Value$$", value));
		waitUntilVisible(driver, customFieldValueElement);
	}
	
	public void clickCustomFieldSMS(WebDriver driver, String customFieldtext, String value) {
		By customFieldValueElement = By.xpath(relatedRecordValue.replace("$$CustomField$$", customFieldtext).replace("$$Value$$", value));
		findElement(driver, customFieldValueElement).findElement(quickSmsIcon).click();
		
	}
	
	/**get all custom field name list
	 * @param driver
	 * @return
	 */
	public List<String> getAllCustomFieldsNameList(WebDriver driver) {
		isListElementsVisible(driver, customFieldList, 5);
		return getTextListFromElements(driver, customFieldList);
	}
	
	/**get custom field value according to field
	 * @param driver
	 * @param customFieldtext
	 * @param valueType
	 * @return
	 */
	public String getCustomFieldValue(WebDriver driver, String customFieldtext){
		By customFieldValueElement = By.xpath(customFieldValueLoc.replace("$$CustomField$$", customFieldtext));
		return getElementsText(driver, customFieldValueElement);
	}
	
	/**get all custom field in map format <fields, values>
	 * @param driver
	 * @param customFieldTextList
	 * @param valueType
	 * @return
	 */
	public Map<String, String> getAllCustomFieldsMapValue(WebDriver driver, List<String> customFieldTextList){
		Map<String, String> customFieldValueMap = new HashMap<String, String>();
		
		for(String customFieldText: customFieldTextList) {
			By customFieldValueElement = By.xpath(customFieldValueLoc.replace("$$CustomField$$", customFieldText));
			customFieldValueMap.put(customFieldText, getElementsText(driver, customFieldValueElement).trim());
		}
		return customFieldValueMap;
	}
	
	/**
	 * Use this method to find sms icon next to related record additional field is visible or not
	 * @param driver
	 * @param phoneNumber phone number of related record for which the icon needs to be verified
	 */
	public void verifyRelatedRecordSmsIconVisible(WebDriver driver, String phoneNumber) {
		waitUntilVisible(driver, By.xpath(customFieldMsgIcon.replace("$$PhoneNumber$$", phoneNumber)));
	}
	
	/**
	 * Use this method to click on sms icon next to related record additional field
	 * @param driver
	 * @param phoneNumber phone number of related for which sms button needs to be clicked
	 */
	public void clickRelatedRecordSmsIcon(WebDriver driver, String phoneNumber) {
		clickElement(driver, By.xpath(customFieldMsgIcon.replace("$$PhoneNumber$$", phoneNumber)));
	}
	
	/**
	 * Use this method to click on email icon next to related record additional field
	 * @param driver
	 * @param emailAddress email address of related for which email button needs to be clicked
	 */
	public void clickRelatedRecordEmailIcon(WebDriver driver, String emailAddress) {
		clickElement(driver, By.xpath(customFieldEmailIcon.replace("$$Email$$", emailAddress)));
		switchToTab(driver, getTabCount(driver));
	}
	
	public void verifyCustomFieldsValueType(WebDriver driver, String customFieldtext, String valueType){
		By customFieldValueElement = By.xpath(customFieldValueLoc.replace("$$CustomField$$", customFieldtext));
		assertEquals(findElement(driver, customFieldValueElement).getTagName(), valueType);
	}
	
	public void verifyCustomFieldsInvisible(WebDriver driver, String customFieldtext){
		By customFieldElement = By.xpath(customField.replace("$$CustomField$$", customFieldtext));
		waitUntilInvisible(driver, customFieldElement);
	}
	
	public void verifyCustomFieldsVisible(WebDriver driver, String customFieldtext){
		By customFieldElement = By.xpath(customField.replace("$$CustomField$$", customFieldtext));
		waitUntilVisible(driver, customFieldElement);
	}
	
	public void callCustomFields(WebDriver driver, String customFieldtext, String value){
		By customFieldValueElement = By.xpath(customFieldCallBtn.replace("$$CustomField$$", customFieldtext).replace("$$Value$$", value));
		clickElement(driver, customFieldValueElement);
	}
	
	public void clickCallerPhone(WebDriver driver){
		idleWait(2);
		waitUntilVisible(driver, callerNumber);
		clickElement(driver, callerNumber);
	}
	
	public boolean isPhoneMatchAccountOptionAvailable(WebDriver driver) {
		return isElementVisible(driver, phoneMatchAccount, 6);
	}
	
	public boolean isPhoneMatchLeadsOptionAvailable(WebDriver driver) {
		return isElementVisible(driver, phoneMatchLeads, 6);
	}
	
	public boolean isPhoneMatchContactsOptionAvailable(WebDriver driver) {
		return isElementVisible(driver, phoneMatchContacts, 6);
	}
	
	public File getCallerDetailSectionScreenShot(WebDriver driver) {
		return HelperFunctions.captureElementScreenShot(driver, findElement(driver, callDetailSection));
	}
	
	public void verifyCustomizedMessageVisible(WebDriver driver) {
		waitUntilVisible(driver, customizedMsgCloseBtn);
	}
	
	public void closeCustomizedMessage(WebDriver driver) {
		clickElement(driver, customizedMsgCloseBtn);
	}
	
	public void verifyCustomizedMessageInvisible(WebDriver driver) {
		waitUntilInvisible(driver, customizedMsgCloseBtn);
	}
	
	public String getDialNextUser(WebDriver driver) {
		waitUntilVisible(driver, dialNextUser);
		return getElementsText(driver, dialNextUser);
	}

	public void clickDialNextBtn(WebDriver driver) {
		waitUntilVisible(driver, dialNextBtn);
		clickElement(driver, dialNextBtn);
		isSpinnerWheelInvisible(driver);
	}
}
