package softphone.source.salesforce;

					  

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import softphone.source.salesforce.ContactDetailPage.SectionModuleNames;

public class LeadDetailPage extends SeleniumBase{

	ContactDetailPage detailPage = new ContactDetailPage();
	
	By leadName    = By.xpath("//td[@class='labelCol'][text()='Name']/following-sibling::td[1]/div");
	By companyName = By.xpath("//td[@class='labelCol'][text()='Company']/following-sibling::td[1]/div");
	
	public By industry    = By.xpath("//td[contains(@class,'labelCol')][text()='Industry']/following-sibling::td[1]/div");
	
	By faxOptOut   = By.xpath(".//*[text()='Fax Opt Out']/following-sibling::td//img");
	
	//task 
	By newTask	   = By.xpath("//input[@value='New Task']");
	By continueBtn = By.xpath("//input[@value='Continue']");
	By addTask     = By.xpath("//button[contains(@class, 'slds-button--brand')]//span[text() = 'Add']");
			//cssSelector(".slds-button--brand.dummyButtonSubmitAction.uiButton");
			//xpath("//span[text() = 'Add']");
	
	By taskSubjectTab = By.xpath("//td/label[text()='Subject']/../following-sibling::td/div[@class='requiredInput']//input[not(@type='hidden')]");
	By taskSubjectTablight = By.xpath("//label[text() = 'Subject']/following-sibling::div//input[@type='text']");
	By saveBtn	   = By.xpath("//input[@title='Save']");
	By saveBtnlit  = By.xpath("//button[contains(@class, 'SMALL uiButton')]//span[text() = 'Save']");
	
	String editTask = "//a[text()='$taskSubject$']/../preceding-sibling::td/a[text()='Edit']";
	String tasksublit = "//a[@title='$taskSubject$']";
	
	//React ringDNA Sequence
	By activityHistoryH 	= By.xpath(".//h3[text()='Activity History']");
	String assignedTo	    = "//*[@id=\"$leadId$_RelatedHistoryList_body\"]//table//tr[2]//td[7]/a";
	By activityHistoryHeaderNames    = By.xpath("//h3[text()='Activity History']/ancestor::div[contains(@class, 'listRelatedObject')]//th");

	// React ringDNA Sequence Module
	By ringDNASequenceModule = By.xpath("//*[text()='ringDNA Sequence Module']");
	By ownerlocater			= By.xpath("//*[@data-testid='collapse-container']//*[contains(@class,'ringdna-table-cell')][3]");
	By ownerName			= By.xpath("//div[contains(@data-testid,'autocomplete-option')]/span[contains(@class, 'RdnaText__StyledText')]");
	String rdnaAvatar		= "//*[@data-testid='collapse-container']//*[contains(@class,'ringdna-table-cell')][3]//span[text()='$rdnaAvatar$']";
	By rdnaAvatarhover	= By.xpath("//div[contains(@class, 'RdnaAvatar__AvatarContainer')]");

	By avatartooltip			= By.xpath("//*[@role='tooltip']//div");
	By iframeParticipant		= By.xpath("//iframe[contains(@title, 'ParticipantActionsOverview')] | //iframe[contains(@title,'accessibility title')]");
	By participantActionFramelight	= By.xpath("//iframe[contains(@title,'accessibility title')]");
	String historySubjectName		 = "//th/a[text()='$actionName$']/../..//td[$index$]";
	
	By editLead 			= By.xpath("//td[@id='topButtonRow']//input[@value=' Edit ']");
	By saveLead				= By.xpath("//td[@id='topButtonRow']//input[@value=' Save ']");
	By leadNamePageHeadingLight		=  By.xpath("//lightning-formatted-name[@slot='primaryField']");
	public String getCompanyName(WebDriver driver){
		return getElementsText(driver, companyName);
	}
	
	public String getIndustry(WebDriver driver){
		detailPage.expandSection(driver, SectionModuleNames.CompanyInformation);
		return getElementsText(driver, industry);
	}
	
	public boolean isFaxOptOutChecked(WebDriver driver) {
		if (getAttribue(driver, faxOptOut, ElementAttributes.src).contains("checkbox_unchecked.gif")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @param driver
	 * @param taskSubject
	 */
	public void createNewTaskOpenActivity(WebDriver driver, String taskSubject) {
		waitUntilVisible(driver, newTask);
		scrollIntoView(driver, newTask);
		clickElement(driver, newTask);
		
		if(isElementVisible(driver, continueBtn, 5)){
			clickElement(driver, continueBtn);
		}
		
		waitUntilVisible(driver, taskSubjectTab);
		enterText(driver, taskSubjectTab, taskSubject);
		
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		waitUntilInvisible(driver, saveBtn);
		
		By editTaskLoc = By.xpath(editTask.replace("$taskSubject$", taskSubject));
		waitUntilVisible(driver, editTaskLoc);
		
	}
	
	/**
	 * @param driver
	 * @param taskSubject
	 */
	public void createNewTaskOpenActivitylight(WebDriver driver, String taskSubject) {
		waitUntilVisible(driver, addTask);
		
		clickByJs(driver, addTask);
		//clickElement(driver, addTask);
		
		if(isElementVisible(driver, continueBtn, 5)){
			clickElement(driver, continueBtn);
		}
		
		waitUntilVisible(driver, taskSubjectTablight);
		enterText(driver, taskSubjectTablight, taskSubject);
		
		waitUntilVisible(driver, saveBtnlit);
		clickByJs(driver, saveBtnlit);
		waitUntilInvisible(driver, saveBtnlit);
		
		By editTaskLoc = By.xpath(tasksublit.replace("$taskSubject$", taskSubject));
		waitUntilVisible(driver, editTaskLoc);
		
	}
	
	/**
	 * @param driver
	 * @param oldTaskSubject
	 * @param newTaskSubject
	 */
	public void editTaskOpenActivity(WebDriver driver, String oldTaskSubject, String newTaskSubject) {
		waitUntilVisible(driver, newTask);
		scrollIntoView(driver, newTask);
		
		By editTaskLoc = By.xpath(editTask.replace("$taskSubject$", oldTaskSubject));
		waitUntilVisible(driver, editTaskLoc);
		clickElement(driver, editTaskLoc);
		
		waitUntilVisible(driver, taskSubjectTab);
		enterText(driver, taskSubjectTab, newTaskSubject);
		
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		waitUntilInvisible(driver, saveBtn);
		
		editTaskLoc = By.xpath(editTask.replace("$taskSubject$", newTaskSubject));
		waitUntilVisible(driver, editTaskLoc);
	}
	
	public void editTaskOpenActivitylight(WebDriver driver, String oldTaskSubject, String newTaskSubject) {
		
		By editTaskLoc = By.xpath(tasksublit.replace("$taskSubject$", oldTaskSubject));
		waitUntilVisible(driver, editTaskLoc);
		clickElement(driver, editTaskLoc);
		
		waitUntilVisible(driver, taskSubjectTab);
		enterText(driver, taskSubjectTab, newTaskSubject);
		
		waitUntilVisible(driver, saveBtn);
		clickElement(driver, saveBtn);
		waitUntilInvisible(driver, saveBtn);
		
		editTaskLoc = By.xpath(editTask.replace("$taskSubject$", newTaskSubject));
		waitUntilVisible(driver, editTaskLoc);
	}
	
	public String getLeadName(WebDriver driver) {
		return getElementsText(driver, leadName);
	}
	
	public String getLeadNameLight(WebDriver driver) {
		return getElementsText(driver, leadNamePageHeadingLight);
	}
 
 public boolean verifActivityHistory (WebDriver driver, String participantActionsName, String leadId) {
		if(!leadId.isEmpty())	
			leadId = leadId.substring(0, 15);	
		waitUntilVisible(driver, activityHistoryH);
		scrollIntoView(driver, activityHistoryH);
		isSpinnerWheelInvisible(driver); 
		By locator = By.xpath(assignedTo.replace("$leadId$", leadId));
		return isElementPresent(driver, locator, 10);
	}
	
	public boolean verifyOwnerName (WebDriver driver, String participantActionsName) {
		waitUntilVisible(driver, ringDNASequenceModule);
		scrollIntoView(driver, ringDNASequenceModule);
		isSpinnerWheelInvisible(driver);
		
		switchToIframe(driver, iframeParticipant);
		waitUntilVisible(driver, ownerName);
		
		
		scrollIntoView(driver, ownerName);
		waitUntilVisible(driver, ownerName);
		List<String> names = getTextListFromElements(driver, ownerName); 
		driver.switchTo().defaultContent();
		return names.contains(participantActionsName);
		
	}
	
	public boolean verifydnaAvatar (WebDriver driver, String rdnaAvatarText ) {
		waitUntilVisible(driver, ringDNASequenceModule);
		scrollIntoView(driver, ringDNASequenceModule);
		
		waitUntilVisible(driver, iframeParticipant);
		switchToIframe(driver, iframeParticipant);
		
		By locator = By.xpath(rdnaAvatar.replace("$rdnaAvatar$", rdnaAvatarText));
		scrollIntoView(driver, locator);
		waitUntilVisible(driver, locator);
		
		List<String> names = getTextListFromElements(driver, locator); 
		driver.switchTo().defaultContent();
		return names.contains(rdnaAvatarText);
	}
	
	
	public void verifydnaAvatartooltip (WebDriver driver, String rdnaAvatarText, String toolTipText ) {
		isSpinnerWheelInvisible(driver);
		detailPage.expandSection(driver, SectionModuleNames.ReactRingDNAModuleLead);
		
		switchToIframe(driver, iframeParticipant);
		
		By locator = By.xpath(rdnaAvatar.replace("$rdnaAvatar$", rdnaAvatarText));
		waitUntilVisible(driver, locator);
		scrollToElement(driver, locator);
		
		waitUntilVisible(driver, rdnaAvatarhover);
		hoverElement(driver, rdnaAvatarhover);
		
		waitUntilTextPresent(driver, avatartooltip, toolTipText);
		
		driver.switchTo().defaultContent();
		
	}
	
	
	public void verifydnaAvatartooltipLight (WebDriver driver, String rdnaAvatarText, String toolTipText ) {
		isSpinnerWheelInvisible(driver);
		switchToIframe(driver, iframeParticipant);
		
		By locator = By.xpath(rdnaAvatar.replace("$rdnaAvatar$", rdnaAvatarText));
		waitUntilVisible(driver, locator);
		scrollToElement(driver, locator);
		
		waitUntilVisible(driver, rdnaAvatarhover);
		hoverElement(driver, rdnaAvatarhover);
		
		waitUntilTextPresent(driver, avatartooltip, toolTipText);
		
		driver.switchTo().defaultContent();
		
	}
	/**
	 * @param driver
	 * @param headerName
	 * @return 
	 */
	public int getIndexOfParticipantActionsHeader(WebDriver driver, String headerName) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, activityHistoryH);
		
		scrollIntoView(driver, activityHistoryH);
		List<String> headerText = getTextListFromElements(driver, activityHistoryHeaderNames);
		int index = headerText.indexOf(headerName);
		return index;
	}
	
	public boolean assignedTo(WebDriver driver, String actionName, int index,String participantActionsName) {
		By SubjectDataLoc = By.xpath(historySubjectName.replace("$actionName$", actionName).replace("$index$", String.valueOf(index)));
    	return isElementPresent(driver, SubjectDataLoc, 5);
	}
	
	public void editSavelead (WebDriver driver) {
		clickElement(driver, editLead);
		waitUntilVisible(driver, saveLead);
		clickElement(driver, saveLead);
		
	}
}