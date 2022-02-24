/**
 * 
 */
package support.source.commonpages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utility.HelperFunctions;
import base.SeleniumBase;

/**
 * @author Abhishek
 *
 */
public class CallRecordingPage extends SeleniumBase{
	By callRecordingPlayers 	= By.className("vjs-control-bar");
	By progressBar				= By.cssSelector(".vjs-control-bar .vjs-progress-holder");
	By playBtn					= By.cssSelector(".vjs-play-control");
	By pauseBtn					= By.cssSelector("[title='Pause']");
	By callerNameLoc			= By.className("caller-name");
	By agentNameLoc				= By.cssSelector(".agent");
	By callerCompany			= By.cssSelector(".caller-company");
	By callDirectionLoc			= By.cssSelector(".direction");
	By callerTitleLoc			= By.cssSelector(".caller-title");
	By callerEmailLoc			= By.cssSelector(".caller-email");
	By supervisorNotes			= By.className("supervisor-notes");
	By saveSupevisorNotesBtn	= By.cssSelector(".save-supervisor-notes");
	By callerLinkToSFDC			= By.cssSelector(".sf-link.caller-name-link");
	By taskLinkToSFDC			= By.cssSelector(".sf-link.sfdc-task-link");
	By downloadIcon				= By.className("download-records");
	By informationMessage		= By.cssSelector(".toast-message");
	By recordingDate			= By.cssSelector(".date");
	By recordingDurationLoc 	= By.cssSelector(".duration");
	
	public int getNumberOfCallRecordings(WebDriver driver){
		for (int i = 0; i < 5; i++) {
			if(isElementVisible(driver, callRecordingPlayers, 0) && getElements(driver, callRecordingPlayers).size() == 1){
				break;
			}
			reloadSoftphone(driver);
			idleWait(3);
		}
		return getElements(driver, callRecordingPlayers).size();
	}
	
	public boolean isDownloadRecordingButtonVisible(WebDriver driver){
		waitUntilVisible(driver, playBtn);
		return isElementVisible(driver, downloadIcon, 2);
	}
	
	public void clickPlayButton(WebDriver driver, int index){
		waitUntilVisible(driver, playBtn);
		idleWait(5);
		getElements(driver, playBtn).get(index).click();
		idleWait(1);
		isElementVisible(driver, pauseBtn,2);
		Double progressPercentage = Double.parseDouble(getAttribue(driver, getElements(driver, progressBar).get(index), ElementAttributes.AriaValueNow));
		assertTrue(progressPercentage > 0);
	}
	
	public void downloadRecording(WebDriver driver, int index){
		waitUntilVisible(driver, playBtn);
		getElements(driver, downloadIcon).get(index).click();
	}
	
	public void verifyCallerName(WebDriver driver, String callerName){
		waitUntilVisible(driver, callerNameLoc);
		waitUntilTextPresent(driver, callerNameLoc, callerName);
	}
	
	public void verifyCallerCompany(WebDriver driver, String companyName){
		waitUntilVisible(driver, callerCompany);
		waitUntilTextPresent(driver, callerCompany, companyName);
	}
	
	public void verifyAgentName(WebDriver driver, String agentName){
		waitUntilVisible(driver, agentNameLoc);
		waitUntilTextPresent(driver, agentNameLoc, agentName);
	}
	
	public void verifyCallDirection(WebDriver driver, String callDirection){
		waitUntilVisible(driver, callDirectionLoc);
		waitUntilTextPresent(driver, callDirectionLoc, callDirection);
	}
	
	public void verifyCallerTitle(WebDriver driver, String callerTitle){
		waitUntilVisible(driver, callerTitleLoc);
		waitUntilTextPresent(driver, callerTitleLoc, callerTitle);
	}
	
	public void verifyCallerEmail(WebDriver driver, String callerEmail){
		waitUntilVisible(driver, callerEmailLoc);
		waitUntilTextPresent(driver, callerEmailLoc, callerEmail);
	}
	
	public void verifySupervisorNotes(WebDriver driver, String supervisorNotesText){
		waitUntilTextPresent(driver, supervisorNotes, supervisorNotesText);
	}
	
	public void updateSuperVisorNotes(WebDriver driver, String newSupervisorNotes){
		clickElement(driver, supervisorNotes);
		enterText(driver, supervisorNotes, newSupervisorNotes);
		clickElement(driver, saveSupevisorNotesBtn);
		verifyToastMessageDissappeared(driver);
	}
	
	public String getRecordingDate(WebDriver driver){
		waitUntilVisible(driver, recordingDate);
		return getElementsText(driver, recordingDate);
	}
	
	public void verifyRecordingDateAsToday(WebDriver driver){
		String currentDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		assertTrue(getRecordingDate(driver).contains(currentDate));
	}
	
	public void verifyDurationNotZero(WebDriver driver){
		String recordingDuration = getElementsText(driver, recordingDurationLoc);
		int recordingSeconds = Integer.parseInt(recordingDuration.substring(Math.max(recordingDuration.length() - 2, 0)));
		assertTrue(recordingSeconds > 0);
	}
	
	public void openCallerSFDCLink(WebDriver driver){
		waitUntilVisible(driver, callerLinkToSFDC);
		clickElement(driver, callerLinkToSFDC);
		switchToTab(driver, getTabCount(driver));
	}
	
	public void openCallerTaskLink(WebDriver driver){
		waitUntilVisible(driver, taskLinkToSFDC);
		clickElement(driver, taskLinkToSFDC);
		switchToTab(driver, getTabCount(driver));
	}
	
	public void verifyToastMessageDissappeared(WebDriver driver){
		waitUntilVisible(driver, informationMessage);
		waitUntilInvisible(driver, informationMessage);
	}
	
	public void verifyRecordingDeleted(WebDriver driver){
		waitUntilVisible(driver, informationMessage);
		assertEquals(getElementsText(driver, informationMessage), "This voicemail has been deleted");
	}
	
	public void verifyAccessDeniedMessage(WebDriver driver){
		waitUntilVisible(driver, informationMessage);
		assertEquals(getElementsText(driver, informationMessage), "You do not have access to listen to the Voicemail Drop used.");
	}
	
	public void verifyAccessDeniedMessageOtherOrg(WebDriver driver){
		waitUntilVisible(driver, informationMessage);
		assertEquals(getElementsText(driver, informationMessage), "No access to call.");
	}
}