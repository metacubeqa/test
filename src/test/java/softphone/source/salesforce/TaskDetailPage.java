package softphone.source.salesforce;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

public class TaskDetailPage extends SeleniumBase{

	By taskAssignedTo		= By.xpath("//*[text()='Assigned To']/following-sibling::td[1]//a[@id]");
	By Name					= By.xpath("//*[text()='Name']/following-sibling::td[1]//a[@id]");
	By taskSubject			= By.xpath("//*[text()='Subject']/following-sibling::td[1]/div|//*[text()='Subject']/following-sibling::td[1]");
	By taskStartDate		= By.xpath("//*[text()='Start']/following-sibling::td[1]/div|//*[text()='Start']/following-sibling::td[1]");
	By taskEndDate		    = By.xpath("//*[text()='End']/following-sibling::td[1]/div|//*[text()='End']/following-sibling::td[1]");
	By taskReminderTime		= By.xpath("//*[text()='Reminder']/following-sibling::td[1]/div");
	By taskReminderCheckBox = By.xpath("//*[text()='Reminder']/following-sibling::td[1]//img[@title='Checked']");
	By taskReminderUnCheckBox = By.xpath("//*[text()='Reminder']/following-sibling::td[1]//img[@title='Not Checked']");
	By taskDescription		= By.xpath("//*[text()='Description']/following-sibling::td[1]/div|//*[text()='Description']/following-sibling::td[1]");
	By taskDueDate			= By.xpath("//*[text()='Due Date']/following-sibling::td[1]/div|//*[text()='Due Date']/following-sibling::td[1]");
	By callHourDayLocal		= By.xpath("//*[text()='Hour Of Day (Local)']/following-sibling::td[1]/div|//*[text()='Hour Of Day (Local)']/following-sibling::td[1]");
	By taskStatus			= By.xpath("//*[text()='Status']/following-sibling::td[1]/div|//*[text()='Status']/following-sibling::td[1]");
	By taskType				= By.xpath("//*[text()='Type']/following-sibling::td[1]/div|//*[text()='Type']/following-sibling::td[1]");
	By taskPriority			= By.xpath("//*[text()='Priority']/following-sibling::td[1]/div|//*[text()='Priority']/following-sibling::td[1]");
	By taskRelatedTo		= By.xpath("//*[text()='Related To']/following-sibling::td[1]/div/a|//*[text()='Related To']/following-sibling::td[1]/a");
	By taskComments			= By.xpath("//*[text()='Comments']/following-sibling::td[1]/div|//*[text()='Comments']/following-sibling::td[1]");
	By taskCallRating		= By.xpath("//*[text()='Rating']/following-sibling::td[1]/div|//*[text()='Rating']/following-sibling::td[1]");
	By taskCallDisposition	= By.xpath("//*[text()='Disposition']/following-sibling::td[1]/div|//*[text()='Disposition']/following-sibling::td[1]");
	By taskCallDuration		= By.xpath("//*[text() = 'Call Duration']/following-sibling::td[1]/div[not(contains(@id,'Seconds'))] |//*[text()='Call Duration']/following-sibling::td[1][not(contains(@id,'Seconds'))]");
	By taskCallDirection	= By.xpath("//*[text()='Direction' or text() = 'Call Direction']/following-sibling::td[1]/div|//*[text()='Direction' or text() = 'Call Direction']/following-sibling::td[1]");
	By taskSuperviosrNotes	= By.xpath("//*[text()='Supervisor Notes']/following-sibling::td[1]/div|//*[text()='Supervisor Notes']/following-sibling::td[1]");
	By taskVoicemailCheckbox= By.xpath("//*[@id='00N0a00000CFK27_ileinner']/img");
	By taskCallRecordingUrl	= By.xpath("//*[text()='Recording URL']/following-sibling::td[1]/div/a | //*[text()='Recording URL']/following-sibling::td[1]/a|//*[text()='Call Recording URL']/following-sibling::td[1]/div/a");
	By automatedVMUrl		= By.xpath("//*[text()='Automated Voicemail Link']/following-sibling::td[1]/div/a|//*[text()='Automated Voicemail Link']/following-sibling::td[1]/a");
	By toNumber				= By.xpath("//*[text()='To Number']/following-sibling::td[1]/div|//*[text()='To Number']/following-sibling::td[1]");
	By fromNumber			= By.xpath("//*[text()='From Number']/following-sibling::td[1]/div|//*[text()='From Number']/following-sibling::td[1]");
	
	By recordingsPlayer		= By.className("mejs-controls");
	By lightningDialogueCloseButton = By.cssSelector("#tryLexDialog [title='Close']");
	
	By callStatus						= By.xpath("//*[text()='Call Status']/following-sibling::td[1]/div|//*[text()='Call Status']/following-sibling::td[1]");
	By callFlow							= By.xpath("//*[text()='Call Flow']/following-sibling::td[1]/div|//*[text()='Call Flow']/following-sibling::td[1]");					
	By callQueue 						= By.xpath("//*[text()='Queue']/following-sibling::td[1]/div|//*[text()='Queue']/following-sibling::td[1]");
	By callQueueHoldTime 				= By.xpath("//*[text()='Queue Hold Time']/following-sibling::td[1]/div|//*[text()='Queue Hold Time']/following-sibling::td[1]");
	By abandonedCallUncheckedImage 		= By.xpath("//td[@class='labelCol'][text()='Abandoned Call']/following-sibling::td//img[@src='/img/checkbox_unchecked.gif']");
	By abandonedCallCheckImage			= By.xpath("//td[@class='labelCol'][text()='Abandoned Call']/following-sibling::td//img[@src='/img/checkbox_checked.gif']");
	By createdByRingDNAChckBox 			= By.cssSelector("[src='/img/checkbox_checked.gif']");
	By localPresenceCheckBox		 	= By.xpath("//*[@class='helpButton'][text()='Local Presence?']/../following-sibling::td//img[@src='/img/checkbox_checked.gif']");
	By localPresenceUncheck				= By.xpath("//*[@class='helpButton'][text()='Local Presence?']/../following-sibling::td//img[@src='/img/checkbox_unchecked.gif']");
	By localPresenceNumber 				= By.xpath("//*[@class='helpButton'][text()='Local Presence #']/../following-sibling::td|//*[@class='helpButton'][text()='Local Presence #']/../following-sibling::td/div");
	By voicemailCheckBox 				= By.xpath("//*[@class='helpButton'][text()='Automated Voicemail?']/../following-sibling::td//img[@src='/img/checkbox_checked.gif']");
	By voicemailUsed					= By.xpath("//*[@class='helpButton'][text()='Automated Voicemail Used']/../following-sibling::td|//*[@class='helpButton'][text()='Automated Voicemail Used']/../following-sibling::td/div");
	By voicemailActivityCheckBox 		= By.xpath("//*[@class='helpButton'][text()='Voicemail']/../following-sibling::td//img[@src='/img/checkbox_checked.gif']");
	By voicemailActivityUncheckedBox 	= By.xpath("//*[@class='helpButton'][text()='Voicemail']/../following-sibling::td//img[@src='/img/checkbox_unchecked.gif']");
	By isCallConnectedCheckedBox 		= By.xpath("//*[text()='Call Connected?']/following-sibling::td//img[@src='/img/checkbox_checked.gif']");
	By isCallConnectedUncheckedBox 		= By.xpath("//*[text()='Call Connected?']/following-sibling::td//img[@src='/img/checkbox_unchecked.gif']");
	By callObjectIdentifier				= By.xpath("//*[text()='Call Object Identifier']/following-sibling::td[1]/div|//*[text()='Call Object Identifier']/following-sibling::td[1]");
	
	By lpNumDialed						= By.xpath("//*[text()='Local Presence Num Dialed']/following-sibling::td[1]/div|//*[text()='Local Presence Num Dialed']/following-sibling::td[1]");
	By lpNumDialedType					= By.xpath("//*[text()='Local Presence Num Dialed Type']/following-sibling::td[1]/div|//*[text()='Local Presence Num Dialed Type']/following-sibling::td[1]");
	By lpRouting						= By.xpath("//*[text()='Local Presence Routing']/following-sibling::td[1]/div|//*[text()='Local Presence Routing']/following-sibling::td[1]");
	By callReportCheckBox				= By.xpath("//*[text()='Call Report']/following-sibling::td[1]//img[@src='/img/checkbox_checked.gif']");
	By callReportCategory				= By.xpath("//*[text()='Call Report Category']/following-sibling::td[1]/div|//*[text()='Call Report Category']/following-sibling::td[1]");
	By callReportNote					= By.xpath("//*[text()='Call Report Note']/following-sibling::td[1]/div|//*[text()='Call Report Note']/following-sibling::td[1]");
	By callReportTime					= By.xpath("//*[text()='Call Report Time']/following-sibling::td[1]/div|//*[text()='Call Report Time']/following-sibling::td[1]");
	
	By salesforceLeaveCheckBox 			= By.cssSelector("input[type='checkbox']");
	By salesforceLeaveButton   			= By.cssSelector("input[onclick='doRedirect();']");
	By createdBy					 	= By.xpath("//*[text()='Created By']/following-sibling::td[1]/div/a|//*[text()='Created By']/following-sibling::td[1]/a");	

	By taskSubjectPhone					= By.xpath("//*[text()='Outbound Call: ']//a[@class='ringdna-phone']/span | .//div[contains(@class,'itemBody')]//span[text()='Outbound Call: ']//a[@class='ringdna-phone']/span");
	By toPhoneNumber 					= By.xpath("//*[text()='To Number']/following-sibling::td[1]/a[@class='ringdna-phone']/span");
	
	public String getAssignedToUser(WebDriver driver) {
		return getElementsText(driver, taskAssignedTo);
	}
	
	public boolean isAssignedToUserVisible(WebDriver driver) {
		return isElementVisible(driver, taskAssignedTo, 5);
	}
	
	public String getName(WebDriver driver) {
		return getElementsText(driver, Name);
	}
	
	public void verifyAssignedTofieldVisible(WebDriver driver) {
		waitUntilVisible(driver, taskAssignedTo);
	}
	
	public String getSuperVisorNotes(WebDriver driver) {
		return getElementsText(driver, taskSuperviosrNotes);
	}
	
	public String getCallRecordingUrl(WebDriver driver) {
		return getElementsText(driver, taskCallRecordingUrl);
	}
	
	public String getCallRecordingHrefAttribute(WebDriver driver) {
		waitUntilVisible(driver, taskCallRecordingUrl);
		return getAttribue(driver, taskCallRecordingUrl, ElementAttributes.href);
	}
	
	public void isCallRecordingURLInvisible(WebDriver driver){
		waitUntilInvisible(driver, taskCallRecordingUrl);
	}
	
	public void clickRecordingURL(WebDriver driver){
		idleWait(3);
		clickElement(driver, taskCallRecordingUrl);
		switchToTab(driver, getTabCount(driver));
	}
	
	public void clickRecordingURLInSupport(WebDriver driver){
		clickElement(driver, taskCallRecordingUrl);
		switchToTab(driver, getTabCount(driver));
		if(isElementVisible(driver, salesforceLeaveCheckBox, 2)){
			clickElement(driver, salesforceLeaveCheckBox);
			clickElement(driver, salesforceLeaveButton);
		}
	}
	
	public void clickAutomatedVMURL(WebDriver driver){
		clickElement(driver, automatedVMUrl);
		switchToTab(driver, getTabCount(driver));
		idleWait(2);
	}
	
	public String getAutomatedVMURL(WebDriver driver){
		clickAutomatedVMURL(driver);
	    idleWait(1);
		String currentUrl = driver.getCurrentUrl();
		closeTab(driver);
		switchToTab(driver, getTabCount(driver));
		return currentUrl;
	}
	
	public String getComments(WebDriver driver) {
		return getElementsText(driver, taskComments);
	}
	

	public String getDescription(WebDriver driver) {
		return getElementsText(driver, taskDescription);
	}

	public String getCallHourDayLocal(WebDriver driver) {
		return getElementsText(driver, callHourDayLocal);
	}
	
	public String getRating(WebDriver driver) {
		return getElementsText(driver, taskCallRating);
	}
	
	public String getDisposition(WebDriver driver) {
		return getElementsText(driver, taskCallDisposition);
	}
	
	public String getSubject(WebDriver driver){
		return getElementsText(driver, taskSubject);
	}
	
	public String getCallObjectId(WebDriver driver){
		return getElementsText(driver, callObjectIdentifier);
	}
	
	public String getStartDate(WebDriver driver){
		return getElementsText(driver, taskStartDate);
	}
	
	public String getEndDate(WebDriver driver){
		return getElementsText(driver, taskEndDate);
	}
	
	public String getDueDate(WebDriver driver){
		return getElementsText(driver, taskDueDate);
	}
	
	public String getTaskRelatedTo(WebDriver driver){
		return isElementVisible(driver, taskRelatedTo, 5)? getElementsText(driver, taskRelatedTo):" ";
	}
	
	public void clickRelatedToCampaign(WebDriver driver){
		clickElement(driver, taskRelatedTo);
	}
	
	public boolean isTaskReminderCheckBoxChecked(WebDriver driver){
		return isElementVisible(driver, taskReminderCheckBox, 1);
	}
	
	public String getTaskReminderTime(WebDriver driver){
		return getElementsText(driver, taskReminderTime);
	}
	
	public String getTaskStatus(WebDriver driver){
		return getElementsText(driver, taskStatus);
	}
	
	public String getTaskType(WebDriver driver){
		return getElementsText(driver, taskType);
	}
	
	public String getTaskPriority(WebDriver driver){
		return getElementsText(driver, taskPriority);
	}
	
	public String getToNumber(WebDriver driver){
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, toNumber));
	}
	
	public String getFromNumber(WebDriver driver){
		return HelperFunctions.getNumberInSimpleFormat(getElementsText(driver, fromNumber));
	}
	
	public void verifyTaskReminderTime(WebDriver driver, String taskReminderDate, String taskReminderTime){
		closeLightningDialogueBox(driver);
		assertTrue(isTaskReminderCheckBoxChecked(driver), "Task Reminder Checkbox is not Checked");
		assertTrue(getTaskReminderTime(driver).equals( taskReminderDate + " " + taskReminderTime) || getTaskReminderTime(driver).equals( taskReminderDate + ", " + taskReminderTime), "Task Reminder time is not the same");
	}
	
	public void verifyTaskReminderCheckboxUnchecked(WebDriver driver){
		assertFalse(isTaskReminderCheckBoxChecked(driver), "Task Reminder Checkbox is not Checked");
	}
	
	public void closeLightningDialogueBox(WebDriver driver){
		if(isElementVisible(driver, lightningDialogueCloseButton, 5)){
			clickElement(driver, lightningDialogueCloseButton);
		}
	}
	
	public void verifyCallStatus(WebDriver driver, String expectedStatus){
		assertEquals(getElementsText(driver, callStatus), expectedStatus);
	}
	
	public String getCallStatus(WebDriver driver){
		waitUntilVisible(driver, callStatus);
		return getElementsText(driver, callStatus);
	}
	
	public String getCallFlow(WebDriver driver){
		waitUntilVisible(driver, callFlow);
		return getElementsText(driver, callFlow);
	}
	
	public String getCallQueue(WebDriver driver){
		return getElementsText(driver, callQueue);
	}
	
	public String getcallQueueHoldTime(WebDriver driver){
		return getElementsText(driver, callQueueHoldTime);
	}
	
	public String getCallDirection(WebDriver driver){
		return getElementsText(driver, taskCallDirection);
	}
	
	public String getCallDuration(WebDriver driver){
		return getElementsText(driver, taskCallDuration);
	}
	
	public void isCreatedByRingDNA(WebDriver driver){
		waitUntilVisible(driver, createdByRingDNAChckBox);
	}
	
	public void verifyTaskData(WebDriver driver, HashMap<SoftPhoneNewTask.taskFields, String> taskDetails){
		closeLightningDialogueBox(driver);
		if(taskDetails.get(SoftPhoneNewTask.taskFields.Subject)!=null){
			System.out.println("verifying Subject: " + taskDetails.get(SoftPhoneNewTask.taskFields.Subject));
			assertEquals(getSubject(driver),taskDetails.get(SoftPhoneNewTask.taskFields.Subject));
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.Comments)!=null){
			System.out.println("verifying Comments: " + taskDetails.get(SoftPhoneNewTask.taskFields.Comments));
			assertEquals(getComments(driver),taskDetails.get(SoftPhoneNewTask.taskFields.Comments));
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.DueDate)!=null){
			System.out.println("verifying Due Date: " + taskDetails.get(SoftPhoneNewTask.taskFields.DueDate));
			assertEquals(getDueDate(driver),taskDetails.get(SoftPhoneNewTask.taskFields.DueDate));
		} else if(taskDetails.get(SoftPhoneNewTask.taskFields.DueDateDisable)!=null && Boolean.parseBoolean(taskDetails.get(SoftPhoneNewTask.taskFields.DueDateDisable))){
			System.out.println("verifying that Due Date disable is: " + taskDetails.get(SoftPhoneNewTask.taskFields.DueDateDisable));
			assertEquals(getDueDate(driver)," ");
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.ReminderDate)!=null){
			System.out.println("verifying Reminder Time: " + taskDetails.get(SoftPhoneNewTask.taskFields.ReminderDate) + " " + taskDetails.get(SoftPhoneNewTask.taskFields.ReminderTime));
			verifyTaskReminderTime(driver, taskDetails.get(SoftPhoneNewTask.taskFields.ReminderDate), taskDetails.get(SoftPhoneNewTask.taskFields.ReminderTime));
		}else if(taskDetails.get(SoftPhoneNewTask.taskFields.DeleteReminder)!=null && Boolean.parseBoolean(taskDetails.get(SoftPhoneNewTask.taskFields.DeleteReminder))){
			System.out.println("Verifying Reminder Time is removed");
			verifyTaskReminderCheckboxUnchecked(driver);
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.Type)!=null){
			System.out.println("verifying Task Type: " + taskDetails.get(SoftPhoneNewTask.taskFields.Type));
			assertEquals(getTaskType(driver), taskDetails.get(SoftPhoneNewTask.taskFields.Type));
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.Priority)!=null){
			System.out.println("verifying Task priority: " + taskDetails.get(SoftPhoneNewTask.taskFields.Priority));
			assertEquals(getTaskPriority(driver),taskDetails.get(SoftPhoneNewTask.taskFields.Priority));
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.Status)!=null){
			System.out.println("verifying Task Status: " + taskDetails.get(SoftPhoneNewTask.taskFields.Status));
			assertEquals(getTaskStatus(driver),taskDetails.get(SoftPhoneNewTask.taskFields.Status));
		}
		
		if(taskDetails.get(SoftPhoneNewTask.taskFields.RelatedRecord)!=null){
			System.out.println("verifying Related Record: " + taskDetails.get(SoftPhoneNewTask.taskFields.RelatedRecord));
			assertEquals(getTaskRelatedTo(driver),taskDetails.get(SoftPhoneNewTask.taskFields.RelatedRecord));
		}		
	}
	
	public void verifyCallNotAbandoned(WebDriver driver){
		waitUntilVisible(driver, abandonedCallUncheckedImage);
	}
	
	public void verifyCallAbandoned(WebDriver driver){
		waitUntilVisible(driver, abandonedCallCheckImage);
	}
	
	public void verifyCallHaslocalPresence(WebDriver driver){
		waitUntilVisible(driver, localPresenceCheckBox);
	}
	
	public void verifyCallIsNotLocalPresence(WebDriver driver){
		waitUntilVisible(driver, localPresenceUncheck);
	} 
	
	public String getLocalPresenceNumber(WebDriver driver){
		return getElementsText(driver, localPresenceNumber);
	}
	
	public void verifyVoiceMalDroppped(WebDriver driver){
		waitUntilVisible(driver, voicemailCheckBox);
	} 
	
	public String getVoicemailUsed(WebDriver driver){
		return getElementsText(driver, voicemailUsed);
	}
	
	public void verifyVoicemailCreatedActivity(WebDriver driver){
		waitUntilVisible(driver, voicemailActivityCheckBox);
	} 	
	
	public void verifyNotVoicemailCreatedActivity(WebDriver driver){
		waitUntilVisible(driver, voicemailActivityUncheckedBox);
	}
	
	public void verifyCallConnected(WebDriver driver){
		waitUntilVisible(driver, isCallConnectedCheckedBox);
	} 	
	
	public void verifyCallNotConnected(WebDriver driver){
		waitUntilVisible(driver, isCallConnectedUncheckedBox);
	}
	
	public String getLPNumDialed(WebDriver driver){
		return getElementsText(driver, lpNumDialed);
	}
	
	public String getLPDialedType(WebDriver driver){
		return getElementsText(driver, lpNumDialedType);
	}
	
	public String getLPRouting(WebDriver driver){
		return getElementsText(driver, lpRouting);
	}
	
	public void isCallReportChecked(WebDriver driver){
		waitUntilVisible(driver, callReportCheckBox);
	}
	
	public String getCallReportCategory(WebDriver driver){
		return getElementsText(driver, callReportCategory);
	}
	
	public String getCallReportNote(WebDriver driver){
		return getElementsText(driver, callReportNote);
	}
	
	public String getCallReportTime(WebDriver driver){
		return getElementsText(driver, callReportTime);
	}
	
	public void clickTaskSubjectPhoneNumber(WebDriver driver){
		waitUntilVisible(driver, taskSubjectPhone);
		clickElement(driver, taskSubjectPhone);		  
	}
	
	public void clickToPhoneNumber(WebDriver driver) {
		waitUntilVisible(driver, toPhoneNumber);
		clickElement(driver, toPhoneNumber);
	}

	public String getCreatedByUser(WebDriver driver) {
		return getElementsText(driver, createdBy);
	}
}