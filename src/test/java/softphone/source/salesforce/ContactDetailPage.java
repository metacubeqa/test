package softphone.source.salesforce;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.util.Strings;

import base.SeleniumBase;
import softphone.source.CallScreenPage;
import utility.HelperFunctions;
import utility.SalesForceAPIUtility;

public class ContactDetailPage extends SeleniumBase{

	TaskDetailPage taskDetailPage = new TaskDetailPage();

	By headerRowName				= By.cssSelector("#contactHeaderRow h2.topName");
	By contactPageHeading			= By.xpath(".//*[text()='My Contact Detail']|.//*[text()='Contact Detail']");
	By leadPageHeading				= By.xpath(".//*[text()='My Lead Detail']|.//*[text()='Lead Detail']");
	By deleteCaller 				= By.cssSelector("[value='Delete']");
	By leadOwner					= By.xpath("//td[@class='labelCol'][text()='Lead Owner']/following-sibling::td[1]//a[2]|//td[@class='labelCol'][text()='Lead Owner']/following-sibling::td[1]/div//a[2]");
	By leadOWnerChangeLink			= By.xpath("//td[@class='labelCol'][text()='Lead Owner']/following-sibling::td[1]//a[text()='[Change]']|//td[@class='labelCol'][text()='Lead Owner']/following-sibling::td[1]/div//a[text()='[Change]']");
	By callerName					= By.xpath("//td[@class='labelCol'][text()='Name']/following-sibling::td[1]|//td[@class='labelCol'][text()='Name']/following-sibling::td[1]/div");
	By callerCompanyName			= By.xpath("//td[@class='labelCol'][text()='Company']/following-sibling::td[1]|//td[@class='labelCol'][text()='Company']/following-sibling::td[1]/div");
	By callerAccountName			= By.xpath("//td[@class='labelCol'][text()='Account Name']/following-sibling::td[1]|//td[@class='labelCol'][text()='Account Name']/following-sibling::td[1]/div");
	By callerAccountEmail			= By.xpath("//td[@class='labelCol'][text()='Email']/following-sibling::td[1]|//td[@class='labelCol'][text()='Email']/following-sibling::td[1]/div");
	By callerPhone  				= By.xpath("//td[@class='labelCol'][text()='Phone']/following-sibling::td[1]|//td[@class='labelCol'][text()='Phone']/following-sibling::td[1]/div");
	By callerOtherPhone				= By.xpath("//td[@class='labelCol'][text()='Other Phone']/following-sibling::td[1]|//td[@class='labelCol'][text()='Other Phone']/following-sibling::td[1]/div");
	By callerTitle					= By.xpath("//td[@class='labelCol'][text()='Title']/following-sibling::td[1]|//td[@class='labelCol'][text()='Title']/following-sibling::td[1]/div");
	By callerEmail					= By.xpath("//td[@class='labelCol'][text()='Email']/following-sibling::td[1]|//td[@class='labelCol'][text()='Email']/following-sibling::td[1]/div");
	By callerLeadStatus				= By.xpath("//td[@class='labelCol'][text()='Lead Status']/following-sibling::td[1]/div|//td[@class='labelCol'][text()='Lead Status']/following-sibling::td[1]");
	By callerRecentCallEntry 		= By.xpath("//*[contains(@id,'_RelatedHistoryList_body')]/table/tbody/tr[2]/th/a");
	By leftCompanyCheckBox			= By.xpath("//label[text()='Left Company']/..//following-sibling::td[1]/input|//label[text()='Left Company']/..//following-sibling::td[1]/input/div");
	By callerActivityList			= By.xpath("//*[contains(@id,'_RelatedHistoryList_body')]/table/tbody/tr/th/a");
	String callerActivitySubject	= "//*[contains(@id,'_RelatedHistoryList_body')]//a[text()='$$Subject$$']";
	String recentActivitySubject	= "//*[contains(@id,'_RelatedActivityList_body')]//a[text()='$$Subject$$']";
	By callsActivities				= By.xpath(".//h3[text()='Calls']/ancestor::div[contains(@class,'listRelatedObject')]//tr[contains(@class,'dataRow')]/th/a");
	By goToActivityList 			= By.cssSelector(".listRelatedObject.taskBlock [href*='RelatedActivityList&closed=0']");
	By activityPageNextLink 		= By.xpath(".//*[text()='Next Page>' and not(@class='greyedLink')]");
	By leadDescription 				= By.xpath("//td[@class='labelCol'][text()='Description']/following-sibling::td[1]/div|//td[@class='labelCol'][text()='Description']/following-sibling::td[1]");
	By leadTextBox	 				= By.xpath("//*[text()='Description']/..//following-sibling::td[1]/*");
	String activity 				= "//a[text()='$$Subject$$']";
	By myContactsHomePageHeader 	= By.xpath(".//*[@class='pageType' and (text() = 'My Contacts' or text() = 'My Leads' or text() = 'Contacts'  or text() = 'Leads')]");
	By goToactivityHistory			= By.cssSelector(".listRelatedObject.taskBlock [href*='RelatedHistoryList&closed=1']");
	By lastModColHeading			= By.cssSelector("[title*='Last Modified Date/Time']");
	By lastModDescColHeading		= By.cssSelector("[title='Last Modified Date/Time - Sorted descending']");
	By callerActivityHistoryList	= By.xpath("//*[contains(@class,'listRelatedObject ')]//table/tbody/tr[2]/th/a");
	By chatterPostedBy				= By.cssSelector(".cxfeedinnerwrapper .feeditembody .feeditemfirstentity a");
	By chatterComment				= By.cssSelector(".cxfeedinnerwrapper .feeditembody .feeditemtext.cxfeeditemtext");
	By createdBy					= By.xpath("//td[@class='labelCol'][text()='Created By']/following-sibling::td[1]");
	By readOnlywebLeadCheckbox		= By.xpath(".//*[text()='RingDNA WebLead']/following-sibling::td/img");
	
	By LeadContactEditButton		= By.xpath(".//*[text()='My Lead Detail' or text()='Lead Detail']/../following-sibling::td/input[@value=' Edit ']|.//*[text()='My Contact Detail']/../following-sibling::td/input[@value=' Edit ']");
	By firstNameEditInputTab		= By.cssSelector("#name_firstlea2");
	By lastNameEditInputTab			= By.cssSelector("#name_lastlea2");
	By companyNameEditInputTab		= By.xpath(".//*[text()='Company']/parent::td/..//div[@class='requiredInput']//input");
	By leadEmailEditInput			= By.xpath("//*[text()='Email']/../following-sibling::td/input|//*[text()='Email']/../following-sibling::td/inputdiv");
	
	By leadeditStatusDropdown		= By.cssSelector("select#lea13");
	By leadEditSaveButton			= By.cssSelector(".pbHeader input[value=' Save ']");
	By emailOptOutCheckBox			= By.xpath(".//*[text()='Email Opt Out']/../following-sibling::td/input");
	By doNotCallOutCheckBox			= By.xpath(".//*[text()='Do Not Call']/../following-sibling::td/input[@type='checkbox']");
	By ringDNAWebLeadsCheckBox		= By.xpath(".//*[text()='RingDNA WebLead']/../following-sibling::td/input[@type='checkbox']");
	By webLeadCheckBox				= By.xpath(".//*[text()='Web Lead']/../following-sibling::td/input[@type='checkbox']");
	By leadSourceDropDown			= By.xpath(".//*[text()='Lead Source']/../following-sibling::td//select");
	By leadCity						= By.xpath(".//*[text()='City']/../following-sibling::td/input");
	By leadSource					= By.xpath(".//*[text()='Lead Source']/following-sibling::td");
	
	//New Lead Owner page
	By newLeadOwnerTextBox			= By.id("newOwn");
	By saveButton					= By.cssSelector("input[value=' Save ']");
	
	//Contact Edit Locators
	By contactsTab					= By.id("Contact_Tab");
	By newContactButton				= By.cssSelector("input[value=' New ']");
	By contactFirstNameTextBox		= By.cssSelector("input[id *= name_first]");
	By contactLastNameTextBox		= By.cssSelector("input[id *= name_last]");
	By contactPhoneNumberTextBox	= By.xpath(".//label[text() = 'Phone']/../following-sibling::td/input");
	
	//phone match
	By phoneMatchOptions            = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-down']/parent::h4");
	By phoneMatchAccount            = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-down']/parent::h4[@class = 'search-header haveAccounts']");
	By phoneMatchLeads              = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-down']/parent::h4[@class = 'search-header haveLeads']");
	By phoneMatchContacts           = By.xpath("//span[@class = 'glyphicon pull-right glyphicon-chevron-down']/parent::h4[@class = 'search-header haveContacts']");
	
	String contactPhoneLink			= "//a[@class='ringdna-phone']/span[text()='$$ContactPhone$$']";
	By contactPhoneLinkExt			= By.xpath("//td[text() = 'Phone']/following-sibling::td//span | //lightning-formatted-phone//a[@lightning-formattedphone_formattedphone]");
	
	//sequence objects
	By sequenceId					= By.xpath(".//*[text()='Sequence ID']/following::td[1]//*| .//*[text()='Sequence Id']/following::td[1]//*");
	By participantActionsHeader	 	= By.xpath(".//h3[text()='Participant Actions']");
	By participantActionList		= By.xpath(".//th[text()='Participant Action Name']/../..//th[@scope='row']");
	By addToSequenceBtn				= By.cssSelector("td#topButtonRow [value='Add to Sequence']");
	By yesAssociateBtn              = By.xpath("//button[text()='Yes, Associate!']");
	
	By participantActionFrame		= By.xpath("//iframe[contains(@title, 'ParticipantActionsOverview')]");
	By participantActionFramelight	= By.xpath("//iframe[contains(@title,'accessibility title')]");
	
	By sequenceAssociateHeader 		= By.xpath("//*[text() = 'Select Sequence to Associate']");

	By noMatchingSeqHeader			= By.xpath("//h3[text()='Currently no matching sequences are present, please create sequence from the \"Sequences\" tab.']");
	String associateSeq 			= "//a/span[text()='$sequenceName$']//ancestor::span[contains(@class, 'RdnaSmartTable')]/following-sibling::span//button[text()='Associate']";
	String associatedSeqName 		= ".//*[text()='$sequenceName$']";
	
	By participantActionsHeaderNames    = By.xpath("//h3[text()='Participant Actions']/ancestor::div[contains(@class, 'listRelatedObject')]//th");
	String actionNameData 			    = "(//th/a[text()='$actionName$']/../..//td)[$index$]";
	//String actionNameData 						= "(//th[text()='$actionName$']/../..//td)[$index$]";
	
	String moduleSectionArrow 		= "//h3[text()='$module$']/parent::div/img";
	String moduleSectionArrowlight	= "//span[text()='$module$']/preceding-sibling::span/lightning-icon";
	
	String openParticipantActions   = "//th/a[text()='$actionName$']";
	
    By isActionInitiated 			= By.xpath("//td[text()='isActionPerformed']/following-sibling::td//img");
    
    By nylasMessageID				= By.xpath("//td[text()='Nylas Message Id']/following-sibling::td[1]/div");
    By emailSendMethod				= By.xpath("//td[text()='Email Send Method']/following-sibling::td[1]/div");
    By expectedExecutionDate		= By.xpath("//td[text()='Email Send Method']/following-sibling::td[1]/div");
    By actualExecutionDate			= By.xpath("//td[text()='Actual Execution Date']/following-sibling::td[1]/div");

    By taskSubType					= By.xpath("//*[text()='Task Subtype']/following-sibling::td[1]/div|//*[text()='Task Subtype']/following-sibling::td[1]");
    By contactNamePageHeadingLight		=  By.xpath("//force-aura-action-wrapper//div/span[1]");
    public enum	LeadDetailsFields{
		leadOwner,
		checkDoNotCall,
		uncheckDoNotCall,
		checkEmailOptOut,
		uncheckEmailOptOut,
		checkRingDNAWebLeads,
		uncheckRingDNAWebLeads,
		checkWebLead,
		leadSource,
		leadCity,
		leadStatus,
		firstName,
		lastName,
		company,
		email,
		description
	}
	
	public enum ContactDetailsFields{
		firstName,
		lastName,
		phoneNumber
	}
	
	public static enum SectionModuleNames {
		ReactRingDNAModuleContact("ringDNA Sequence Module"), CompanyInformation("Company Information"),
		ReactRingDNAModuleLead("ringDNA Sequence Module");

		private String value;

		SectionModuleNames(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public void verifyContactPageOpen(WebDriver driver) {
		waitUntilVisible(driver, contactPageHeading);
	}
	
	public void verifyLeadPageOpen(WebDriver driver) {
		waitUntilVisible(driver, leadPageHeading);
	}
	
	public String getHeaderRowName(WebDriver driver){
		return getElementsText(driver, headerRowName);
	}
	
	public void deleteContact(WebDriver driver) {
		clickElement(driver, deleteCaller);
		acceptAlert(driver);
		waitUntilVisible(driver, myContactsHomePageHeader);
	}

	public String getCallerName(WebDriver driver) {
		return getElementsText(driver, callerName);
	}
	
	public String getCallerNameLight(WebDriver driver) {
		return getElementsText(driver, contactNamePageHeadingLight);
	}
	
	public String getLeadOwner(WebDriver driver) {
		return getElementsText(driver, leadOwner);
	}
	
	public String setLeadOwner(WebDriver driver, String newLeadOwner) {
		clickElement(driver, leadOWnerChangeLink);
		waitUntilVisible(driver, newLeadOwnerTextBox);
		enterText(driver, newLeadOwnerTextBox, newLeadOwner);
		clickElement(driver, saveButton);
		waitUntilVisible(driver, leadOwner);
		return getElementsText(driver, leadOwner);
	}
	
	public String getCallerPhone(WebDriver driver) {
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, callerPhone));
	}
	
	public String getCallerOtherPhone(WebDriver driver) {
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, callerOtherPhone));
	}
	
	public String getCallerTitle(WebDriver driver) {
		return getElementsText(driver, callerTitle);
	}
	
	public String getCallerEmail(WebDriver driver) {
		return getElementsText(driver, callerEmail);
	}
	
	public String getCallerAccountName(WebDriver driver) {
		return getElementsText(driver, callerAccountName);
	}
	
	public String getCallerCompanyName(WebDriver driver) {
		return getElementsText(driver, callerCompanyName);
	}
	
	public String getCallerAccountEmail(WebDriver driver) {
		return getElementsText(driver, callerAccountEmail);
	}
	
	public void openCallerActivity(WebDriver driver, String subject) {
		By callerActivitySubject = By.xpath(this.callerActivitySubject.replace("$$Subject$$", subject));
    	waitUntilInvisible(driver, callerActivitySubject);
		clickElement(driver, callerActivitySubject);
		taskDetailPage.verifyAssignedTofieldVisible(driver);
	}
	
	public String getTaskSubtype(WebDriver driver){
		return getElementsText(driver, taskSubType);
	}

	public void openRecentCallEntry(WebDriver driver, String subject) {
		By callerActivitySubject = By.xpath(this.callerActivitySubject.replace("$$Subject$$", subject));
		if (isElementVisible(driver, callerActivitySubject, 0)) {
			clickElement(driver, callerActivitySubject);
			taskDetailPage.verifyAssignedTofieldVisible(driver);
		} else {
			clickElement(driver, goToactivityHistory);
			while (!isElementVisible(driver, lastModDescColHeading, 0)) {
				clickElement(driver, lastModColHeading);
			}
			clickElement(driver, callerActivityHistoryList);
			taskDetailPage.verifyAssignedTofieldVisible(driver);
		}
	}
	
	public void openRecentActivity(WebDriver driver, String subject) {
		By callerActivitySubject = By.xpath(this.recentActivitySubject.replace("$$Subject$$", subject));
		clickElement(driver, callerActivitySubject);
	}
	
	public void verifyRecentActivityNotPresent(WebDriver driver, String subject) {
		By callerActivitySubject = By.xpath(this.recentActivitySubject.replace("$$Subject$$", subject));
		waitUntilInvisible(driver, callerActivitySubject);
	}
	
	public void clickCallActivities(WebDriver driver, int index){
		getElements(driver, callsActivities).get(index).click();
	}
	
	public void openLatestCallActivities(WebDriver driver){
		int index = getElements(driver, callsActivities).size() - 1;
		clickCallActivities(driver, index);
	}
	
	public void openCallEntryByIndex(WebDriver driver, int index){
		getElements(driver, callerActivityList).get(index).click();
		taskDetailPage.verifyAssignedTofieldVisible(driver);
	}
	
	public boolean isActivityInvisible(WebDriver driver, String subject){
		return isElementInvisible(driver, By.xpath(activity.replace("$$Subject$$", subject)), 5);
	}
	
	public void openActivityFromList(WebDriver driver, String subject){
		if(isElementVisible(driver, goToActivityList, 5)){
			clickElement(driver, goToActivityList);
			while(getWebelementIfExist(driver, activityPageNextLink)!= null){
				clickElement(driver, activityPageNextLink);
			}
			scrollToElement(driver, By.xpath(activity.replace("$$Subject$$", subject)));
			clickElement(driver, By.xpath(activity.replace("$$Subject$$", subject)));	
		}
		else{
			scrollToElement(driver, By.xpath(activity.replace("$$Subject$$", subject)));
			clickElement(driver, By.xpath(activity.replace("$$Subject$$", subject)));
		}
	}
	
	public String getCreatedTime(WebDriver driver){
		String createdByText = getElementsText(driver, createdBy);
		return createdByText.substring(createdByText.indexOf(", ") + 2, createdByText.length());
	}
	
	public String getLeadStatus(WebDriver driver){
		return getElementsText(driver, callerLeadStatus);
	}
	
	public void clickContactLeadEditButton(WebDriver driver){
		waitUntilVisible(driver, LeadContactEditButton);
		clickElement(driver, LeadContactEditButton);
	}
	
	private void setLeadStatus(WebDriver driver, CallScreenPage.LeadStatusType leadStatus){
		waitUntilVisible(driver, leadeditStatusDropdown);
		selectFromDropdown(driver, leadeditStatusDropdown, SelectTypes.visibleText, leadStatus.toString());
	}
	
	public void clickSaveBtn(WebDriver driver){
		waitUntilVisible(driver, leadEditSaveButton);
		clickElement(driver, leadEditSaveButton);
		idleWait(1);
	}
	
	public Boolean isLeadWebLead(WebDriver driver) {
		if(getAttribue(driver, readOnlywebLeadCheckbox, ElementAttributes.src).contains("checkbox_unchecked.gif")) {
			return false;
		}else {
			return true;
		}
	}
	
	public void changeLeadStatus(WebDriver driver, CallScreenPage.LeadStatusType leadStatus){
		clickContactLeadEditButton(driver);
		setLeadStatus(driver, leadStatus);
		clickSaveBtn(driver);
	}
	
	public void selectLeadCompanyLeft(WebDriver driver){
		clickContactLeadEditButton(driver);
		checkLeftCompanyCheckBox(driver);
		clickSaveBtn(driver);
	}
	
	public void deselectLeadCompanyLeft(WebDriver driver){
		clickContactLeadEditButton(driver);
		uncheckLeftCompanyCheckBox(driver);
		clickSaveBtn(driver);
	}
	
	public String getLeadDescription(WebDriver driver){
		return getElementsText(driver, leadDescription);
	}
	
	public String getLeadSource(WebDriver driver){
		return getElementsText(driver, leadSource);
	}
	
	public void validateChatterData(WebDriver driver, String posterBy, String chatComment){
		assertTrue(getElements(driver, chatterPostedBy).get(0).getText().equals(posterBy));
		assertTrue(getElements(driver, chatterComment).get(0).getText().equals(chatComment));
	}
	
	public void checkLeftCompanyCheckBox(WebDriver driver){
		if(!findElement(driver, leftCompanyCheckBox).isSelected()){
			clickElement(driver, leftCompanyCheckBox);
		}
	}
	
	public void uncheckLeftCompanyCheckBox(WebDriver driver){
		if(findElement(driver, leftCompanyCheckBox).isSelected()){
			clickElement(driver, leftCompanyCheckBox);
		}
	}
	
	public void checkEmailOptOutCheckBox(WebDriver driver){
		if(!findElement(driver, emailOptOutCheckBox).isSelected()){
			clickElement(driver, emailOptOutCheckBox);
		}
	}
	
	public void uncheckEmailOptOutCheckBox(WebDriver driver){
		if(findElement(driver, emailOptOutCheckBox).isSelected()){
			clickElement(driver, emailOptOutCheckBox);
		}
	}
	
	public void checkDoNotCallCheckBox(WebDriver driver){
		if(!findElement(driver, doNotCallOutCheckBox).isSelected()){
			clickElement(driver, doNotCallOutCheckBox);
		}
	}
	
	public void uncheckDoNotCallCheckBox(WebDriver driver){
		if(findElement(driver, doNotCallOutCheckBox).isSelected()){
			clickElement(driver, doNotCallOutCheckBox);
		}
	}
	
	public void checkRingDNAWebLeadsCheckBox(WebDriver driver){
		if(!findElement(driver, ringDNAWebLeadsCheckBox).isSelected()){
			clickElement(driver, ringDNAWebLeadsCheckBox);
		}
	}
	
	public void uncheckRingDNAWebLeadsCheckBox(WebDriver driver){
		if(findElement(driver, ringDNAWebLeadsCheckBox).isSelected()){
			clickElement(driver, ringDNAWebLeadsCheckBox);
		}
	}
	
	public void checkWebLeadCheckBox(WebDriver driver){
		if(!findElement(driver, webLeadCheckBox).isSelected()){
			clickElement(driver, webLeadCheckBox);
		}
	}
	
	public void uncheckWebLeadCheckBox(WebDriver driver){
		if(findElement(driver, webLeadCheckBox).isSelected()){
			clickElement(driver, webLeadCheckBox);
		}
	}
	
	public void selectLeadSource(WebDriver driver, String option){
		selectFromDropdown(driver, leadSourceDropDown, SelectTypes.visibleText, option);
	}
	
	public void enterLeadCity(WebDriver driver, String city){
		enterText(driver, leadCity, city);
	}
	
	public void enterFirstName(WebDriver driver, String firstName){
		enterText(driver, firstNameEditInputTab, firstName);
	}
	
	public void enterLastName(WebDriver driver, String lastName){
		enterText(driver, lastNameEditInputTab, lastName);
	}
	
	public void enterCompany(WebDriver driver, String company){
		enterText(driver, companyNameEditInputTab, company);
	}
	
	public void enterEmail(WebDriver driver, String email){
		enterText(driver, leadEmailEditInput, email);
	}
	
	public void enterLeadDescription(WebDriver driver, String description){
		enterText(driver, leadTextBox, description);
	}
	
	public void updateSalesForceLeadDetails(WebDriver driver, HashMap<LeadDetailsFields, String> leadDetails){
		clickContactLeadEditButton(driver);
		
		if(leadDetails.get(LeadDetailsFields.leadOwner) != null){
			setLeadOwner(driver, leadDetails.get(LeadDetailsFields.leadOwner));
		}
		
		if(leadDetails.get(LeadDetailsFields.checkDoNotCall) != null){
			checkDoNotCallCheckBox(driver);	
		}
		
		if(leadDetails.get(LeadDetailsFields.uncheckDoNotCall) != null){
			uncheckDoNotCallCheckBox(driver);	
		}
		
		if(leadDetails.get(LeadDetailsFields.checkEmailOptOut) != null){
			checkEmailOptOutCheckBox(driver);	
		}
		
		if(leadDetails.get(LeadDetailsFields.uncheckEmailOptOut) != null){
			uncheckEmailOptOutCheckBox(driver);	
		}
		
		if(leadDetails.get(LeadDetailsFields.email) != null){
			enterEmail(driver, leadDetails.get(LeadDetailsFields.email));	
		}
		
		if(leadDetails.get(LeadDetailsFields.checkRingDNAWebLeads) != null){
			checkRingDNAWebLeadsCheckBox(driver);
		}
		
		if(leadDetails.get(LeadDetailsFields.uncheckRingDNAWebLeads) != null){
			uncheckRingDNAWebLeadsCheckBox(driver);	
		}
		
		if(leadDetails.get(LeadDetailsFields.checkWebLead) != null){
			checkWebLeadCheckBox(driver);
		}
		
		if(leadDetails.get(LeadDetailsFields.leadSource) != null){
			selectLeadSource(driver, leadDetails.get(LeadDetailsFields.leadSource));
		}
		
		if(leadDetails.get(LeadDetailsFields.leadStatus) != null){

			if(leadDetails.get(LeadDetailsFields.leadStatus).equals(CallScreenPage.LeadStatusType.Contacted.toString())) {
				setLeadStatus(driver, CallScreenPage.LeadStatusType.Contacted);
			}
			if(leadDetails.get(LeadDetailsFields.leadStatus).equals(CallScreenPage.LeadStatusType.Open.toString())) {
				setLeadStatus(driver, CallScreenPage.LeadStatusType.Open);
			}
			if(leadDetails.get(LeadDetailsFields.leadStatus).equals(CallScreenPage.LeadStatusType.Qualified.toString())) {
				setLeadStatus(driver, CallScreenPage.LeadStatusType.Qualified);
			}
			if(leadDetails.get(LeadDetailsFields.leadStatus).equals(CallScreenPage.LeadStatusType.Unqualified.toString())) {
				setLeadStatus(driver, CallScreenPage.LeadStatusType.Unqualified);
			}
		}
		
		if(leadDetails.get(LeadDetailsFields.leadCity) != null){
			enterLeadCity(driver, leadDetails.get(LeadDetailsFields.leadCity));
		}
		
		if(leadDetails.get(LeadDetailsFields.firstName) != null){
			enterFirstName(driver, leadDetails.get(LeadDetailsFields.firstName));
		}
		
		if(leadDetails.get(LeadDetailsFields.lastName) != null){
			enterLastName(driver, leadDetails.get(LeadDetailsFields.lastName));
		}
		
		if(leadDetails.get(LeadDetailsFields.company) != null){
			enterCompany(driver, leadDetails.get(LeadDetailsFields.company));
		}
		
		if(leadDetails.get(LeadDetailsFields.description) != null){
			enterLeadDescription(driver, leadDetails.get(LeadDetailsFields.description));
		}
		
		clickSaveBtn(driver);
	}
	
	public void clickContactPhoneLinkDetails(WebDriver driver, String searchPhone) {
		By contactPhoneDetails = By.xpath(contactPhoneLink.replace("$$ContactPhone$$", searchPhone));
		waitUntilVisible(driver, contactPhoneDetails);
		clickElement(driver, contactPhoneDetails);
	}

	public void clickContactPhoneLinkDetailsExt(WebDriver driver) {
		waitUntilVisible(driver, contactPhoneLinkExt);
		clickByJs(driver, contactPhoneLinkExt);
	}
	
	//*******New Contact Methods********//
	public void clickContactTab(WebDriver driver) {
		waitUntilVisible(driver, contactsTab);
		clickElement(driver, contactsTab);
		waitUntilVisible(driver, newContactButton);
	}
	
	public void clickAddNewButton(WebDriver driver) {
		waitUntilVisible(driver, newContactButton);
		clickElement(driver, newContactButton);
	}
	
	public void enterContactFirstName(WebDriver driver, String firstName) {
		waitUntilVisible(driver, contactFirstNameTextBox);
		enterText(driver, contactFirstNameTextBox, firstName);
	}
	
	public void enterContactLastName(WebDriver driver, String lastName) {
		enterText(driver, contactLastNameTextBox, lastName);
	}
	
	public void enterContactPhoneNumber(WebDriver driver, String phoneNumber) {
		enterText(driver, contactPhoneNumberTextBox, phoneNumber);
	}
	
	public void enterSalesForceContactDetails(WebDriver driver, HashMap<ContactDetailsFields, String> contactDetails){
		
		if(contactDetails.get(ContactDetailsFields.firstName) != null){
			enterContactFirstName(driver, contactDetails.get(ContactDetailsFields.firstName));
		}
		
		if(contactDetails.get(ContactDetailsFields.lastName) != null){
			enterContactLastName(driver, contactDetails.get(ContactDetailsFields.lastName));	
		}
		
		if(contactDetails.get(ContactDetailsFields.phoneNumber) != null){
			enterContactPhoneNumber(driver, contactDetails.get(ContactDetailsFields.phoneNumber));
		}
		
		clickSaveBtn(driver);
	}
	
	public void isphoneMatchOptionsAvailable(WebDriver driver) {
		assertTrue(isElementVisible(driver, phoneMatchAccount, 6));
		assertTrue(isElementVisible(driver, phoneMatchLeads, 6));
		assertTrue(isElementVisible(driver, phoneMatchContacts, 6));
	}
	
	public String getSequenceId(WebDriver driver) {
		refreshCurrentDocument(driver);
		String value = null;
		if (isElementVisible(driver, sequenceId, 5)) {
			scrollIntoView(driver, sequenceId);
			value = getElementsText(driver, sequenceId);
		}
		return value;
	}
	
	public void verifySequenceId(WebDriver driver, String sequence) {
		for (int i = 0; i < 4; i++) {
			if(getSequenceId(driver).equals(sequence)) {
				return;
			}
			idleWait(5);
		}
		Assert.fail();
	}

	public List<String> getParticipantActionsList(WebDriver driver) {
		waitUntilVisible(driver, participantActionsHeader);
		scrollIntoView(driver, participantActionsHeader);
		return getTextListFromElements(driver, participantActionList);
	}
	
	/**
	 * @param driver
	 * @param headerName
	 * @return
	 */
	public int getIndexOfParticipantActionsHeader(WebDriver driver, String headerName) {
		scrollIntoView(driver, participantActionsHeader);
		List<String> headerText = getTextListFromElements(driver, participantActionsHeaderNames);
		int index = headerText.indexOf(headerName);
		return index;
	}
	
	public void openParticipantActions (WebDriver driver, String participantActionsName) {
		waitUntilVisible(driver, participantActionsHeader);
		scrollIntoView(driver, participantActionsHeader);
		isSpinnerWheelInvisible(driver);
		By locator = By.xpath(openParticipantActions.replace("$actionName$", participantActionsName));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator );	
	}
	
	/**
	 * @param driver
	 * @param actionName
	 * @param index
	 */
	public boolean isActionChecked(WebDriver driver, String actionName, int index) {
		By actionNameDataLoc = By.xpath(actionNameData.replace("$actionName$", actionName).replace("$index$", String.valueOf(index)));
		WebElement element = findElement(driver, actionNameDataLoc).findElement(By.xpath("img"));
		String attribute = getAttribue(driver, element, ElementAttributes.src);
		return attribute.contains("checked");
	}
	
	public void clickAddToSeqBtn(WebDriver driver) {
		waitUntilVisible(driver, addToSequenceBtn);
		clickElement(driver, addToSequenceBtn);
		waitUntilInvisible(driver, addToSequenceBtn);
		isSpinnerWheelInvisible(driver);
	}
	
	public void LightclickAddToSeqBtn(WebDriver driver) {
		
		waitUntilVisible(driver, addToSequenceBtn);
		clickElement(driver, addToSequenceBtn);
		waitUntilInvisible(driver, addToSequenceBtn);
		isSpinnerWheelInvisible(driver);
	}
	
	
	/**
	 * @param driver
	 * @param sequenceName
	 */
	public void associateSequence(WebDriver driver, String sequenceName, SectionModuleNames sectionName) {
		isSpinnerWheelInvisible(driver);
		expandSection(driver, sectionName);

		switchToIframe(driver, participantActionFrame);
		waitUntilVisible(driver, sequenceAssociateHeader);
		scrollToElement(driver, sequenceAssociateHeader);
		
		By associateLoc = By.xpath(associateSeq.replace("$sequenceName$", sequenceName));
		waitUntilVisible(driver, associateLoc);
		clickElement(driver, associateLoc);
		
		waitUntilVisible(driver, yesAssociateBtn);
		clickElement(driver, yesAssociateBtn);
		waitUntilInvisible(driver, yesAssociateBtn);
		
		idleWait(10);
		refreshCurrentDocument(driver);
		
		switchToIframe(driver, participantActionFrame);
		isSpinnerWheelInvisible(driver);
		
		By associatedSeqNameLoc = By.xpath(associatedSeqName.replace("$sequenceName$", sequenceName));
		idleWait(10);
		waitUntilVisible(driver, associatedSeqNameLoc);
		driver.switchTo().defaultContent();
	}
	
	public void associateSequencelight (WebDriver driver, String sequenceName, SectionModuleNames sectionName) {
		isSpinnerWheelInvisible(driver);

		switchToIframe(driver, participantActionFramelight);
		waitUntilVisible(driver, sequenceAssociateHeader);
		scrollToElement(driver, sequenceAssociateHeader);
		
		By associateLoc = By.xpath(associateSeq.replace("$sequenceName$", sequenceName));
		waitUntilVisible(driver, associateLoc);
		clickElement(driver, associateLoc);
		
		waitUntilVisible(driver, yesAssociateBtn);
		clickElement(driver, yesAssociateBtn);
		waitUntilInvisible(driver, yesAssociateBtn);
		
		idleWait(10);
		refreshCurrentDocument(driver);
		
		switchToIframe(driver, participantActionFramelight);
		isSpinnerWheelInvisible(driver);
		
		By associatedSeqNameLoc = By.xpath(associatedSeqName.replace("$sequenceName$", sequenceName));
		idleWait(10);
		waitUntilVisible(driver, associatedSeqNameLoc);
		driver.switchTo().defaultContent();
	}
	
	/**
	 * @param driver
	 * @param moduleName
	 */
	public void expandSection(WebDriver driver, SectionModuleNames moduleName) {
		By moduleSectionArrowLoc = By.xpath(moduleSectionArrow.replace("$module$", moduleName.getValue()));

		waitUntilVisible(driver, moduleSectionArrowLoc);
		scrollToElement(driver, moduleSectionArrowLoc);

		if (getAttribue(driver, moduleSectionArrowLoc, ElementAttributes.Class).contains("showListButton")) {
			clickElement(driver, moduleSectionArrowLoc);
		}

		isSpinnerWheelInvisible(driver);
		assertTrue(getAttribue(driver, moduleSectionArrowLoc, ElementAttributes.Class).contains("hideListButton"));
	}
	
	
	
	public boolean verifyIsActionInitiated(WebDriver driver) {
		isSpinnerWheelInvisible(driver);
		boolean answer = false;
		waitUntilVisible(driver, isActionInitiated);
		String title = getAttribue(driver, isActionInitiated, ElementAttributes.Title);
		if (title.equals("Checked")) {

			answer = true;
		}
		if (title.equals("Not Checked")) {

			answer = false;
		}

		return answer;

	}
	
	public boolean textNotNull(WebDriver driver, String actionName, int index) {
		By actionNameDataLoc = By
				.xpath(actionNameData.replace("$actionName$", actionName).replace("$index$", String.valueOf(index)));
		if (isElementVisible(driver, actionNameDataLoc, 10))
			return true;
		else
		return false;// attribute.contains("checked");
	}

	public boolean notNullParam(WebDriver driver, String paramToVerify) {
		if(paramToVerify != "emailSendMethod") {
			return isElementVisible(driver, emailSendMethod, 10);
		}
		else if(paramToVerify != "expectedExecutionDate") {
			return isElementVisible(driver, expectedExecutionDate, 10);
			
		}
		else if(paramToVerify != "actualExecutionDate") {
			return isElementVisible(driver, actualExecutionDate, 10);
			
		}
		return false;
	} 
	
	public void  verifyEmailParticipantActionsfields(String bearerToken, String pActionsID, String salesForceUrl, String ExAction_Type, String ExEmail_Send_Method, String Owner_Id) {
		
		String Expected_Execution_Date =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Expected_Execution_Date__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Expected_Execution_Date));
		
		String Actual_Execution_Date__c =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Actual_Execution_Date__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Actual_Execution_Date__c));
		
		String Email_Sent =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Email_Sent__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Email_Sent));
		
		String isActionPerformed =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__isActionPerformed__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(isActionPerformed));
		
		String Email_Sent_Date =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Email_Sent_Date__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Email_Sent_Date));
		
		String Owner =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "OwnerId", "RDNACadence__Sequence_Action__c");
		assertEquals(Owner, Owner_Id);
		
		String Action_Type =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Cadence_Action_Type__c", "RDNACadence__Sequence_Action__c");
		assertEquals(Action_Type, ExAction_Type);
		
		String Email_Send_Method =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Email_Send_Method__c", "RDNACadence__Sequence_Action__c");
		assertEquals(Email_Send_Method, ExEmail_Send_Method);
	}
	
   public void  verifyParticipantActionsfields(String bearerToken, String pActionsID, String salesForceUrl, String ExAction_Type, String Owner_Id) {
		
		String Expected_Execution_Date =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Expected_Execution_Date__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Expected_Execution_Date));
		
		String Actual_Execution_Date__c =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Actual_Execution_Date__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(Actual_Execution_Date__c));
		
		String isActionPerformed =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__isActionPerformed__c", "RDNACadence__Sequence_Action__c");
		assertTrue(Strings.isNotNullAndNotEmpty(isActionPerformed));
		
		String Owner =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "OwnerId", "RDNACadence__Sequence_Action__c");
		assertEquals(Owner, Owner_Id);
		
		String Action_Type =SalesForceAPIUtility.verifyLeadObjfield(bearerToken, salesForceUrl, pActionsID, "RDNACadence__Cadence_Action_Type__c", "RDNACadence__Sequence_Action__c");
		assertEquals(Action_Type, ExAction_Type);
		
	}
	
}
