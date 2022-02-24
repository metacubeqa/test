package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import softphone.source.callTools.CallToolsPanel;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class SoftphoneCallHistoryPage extends SeleniumBase {
	
	CallScreenPage callScreenPage = new CallScreenPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();

	//call navigation locators
	By callsHistoryInactiveImage 		= By.cssSelector(".history");
	By callsHistoryActiveImage 			= By.cssSelector(".history.active");
	By matchTypeFilter					= By.cssSelector(".primary-match-type-filter");
	By myCallsActiveLink 				= By.cssSelector(".my-calls-tab.active");
	By myCallsinActiveLink 				= By.cssSelector(".my-calls-tab:not(.active)");
	By groupCallsActiveLink 			= By.cssSelector(".queue-calls-tab.active");
	By groupCallsInactiveLink 			= By.className("queue-calls-tab");
	By groupCallsHeading				= By.xpath(".//*[text()='Queue Call History']");
	By allActiveTabLink 				= By.cssSelector(".filter.all.active");
	By allInactiveTabLink 				= By.cssSelector(".filter.all:not(.active)");
	By missedActiveTabLink 				= By.cssSelector(".filter.missed.active");
	By missedInactiveTabLink 			= By.cssSelector(".filter.missed:not(.active)");
	By voicemailActiveTabLink 			= By.cssSelector(".filter.voicemail.active");
	By voicemailInactiveTabLink		 	= By.cssSelector(".filter.voicemail:not(.active)");
	By activeAlertTab					= By.cssSelector(".filter.alerts.active");
	By inActiveAlertTab					= By.cssSelector(".filter.alerts:not(.active)");
	By disabledAlertTab					= By.cssSelector(".filter.alerts.disabled-tab");
	
	//counts
	By historyMissedCallCount 			= By.id("missedCallCount");
	By missedVoicemailCounts 			= By.cssSelector(".missed-voicemail-count");
	By alertsCount						= By.cssSelector(".alerts-count");
	By missedCallCount					= By.cssSelector(".missed-call-count");
	By missedGroupCallCount				= By.cssSelector(".queue-calls-tab .call-counts");
	By missedMyCallCount				= By.cssSelector(".my-calls-tab .call-counts");
	
	//call history lists
	By loadMorebutton 					= By.cssSelector("button[data-action='more']");
	By openCallerDetailButton 			= By.cssSelector(".pull-right.open");
	By recentHistoryWithContactsOpenButton = By.xpath("//*[@id='recent']//*[contains(@class,'caller') and string-length(text()) > 0]/following-sibling::button[@data-action='open']");
	By recentHistoryCallerName			= By.cssSelector(".recent-calls  [data-action='view-caller']");
	By recentHistoryOutboundCall		= By.cssSelector("img[title='Outgoing']");
	By recentHistoryInboundCall			= By.cssSelector("img[title='Incoming']");
	By recentHistoryCallerPhone			= By.cssSelector(".recent-calls .phone");
	By recentHistoryCallerTime			= By.cssSelector("div[class*='start-time']:not([style='display: none;']) small.start-time");
	By recentHistoryCallDisposition		= By.cssSelector("#recent .disposition .disposition-text"); 
	By recentHistoryDurationTitle		= By.cssSelector(".duration-title");
	By recentHistoryDuration			= By.cssSelector(".duration");
	By recentHistoryRelatedRecord		= By.cssSelector(".related-object-text");
	By dispositionAlertMessage			= By.cssSelector(".disposition-alert");
	By multiMatchAlertMessage			= By.cssSelector(".multi-match-alert span");
	By selectAllCheckBox				= By.cssSelector(".all-selected-calls");
	By selectCallHistoryDrpDwn			= By.cssSelector(".select-call-history");
	By markAsReadBtn					= By.cssSelector(".btn-sm.mark-read");
	By selectCallRowCheckBox			= By.cssSelector(".checkbox-call-history input[type='checkbox']:not(.all-selected-calls)");
	By callHistoryRow					= By.cssSelector(".row.recent-call-item");
	By unreadMissedCallPhone			= By.cssSelector("span.phone.unread-missed-call");
	By readMissedCallPhone				= By.cssSelector("span.phone.missed-call");
	By missedVoicemailList              = By.cssSelector(".phone.missed-call.unread-missed-call");
	String callerOpenHistoryButtonLocator = "//*[@class='caller' and text() = '$$Caller$$'/ancestor::*[@class='row']//button[@data-action='open']]";	
	String recentNewVMIcon 				= "#recent .recent-call-item:nth-child($$Index$$) .vmIsNew.new-voicemail";
	String recentPlayVMIcon 			= "#recent .recent-call-item:nth-child($$Index$$) .play-voicemail";
	String recentBlockedBtn 			= "#recent .recent-call-item:nth-child($$Index$$) .number-blocked";
	String recentDeleteVMIcon 			= "#recent .recent-call-item:nth-child($$Index$$) .delete-voicemail";
	String recentHistoryOpenButton		= "(//*[@id='recent']//*[contains(@class,'caller') and string-length(text()) > 0]/following-sibling::button[@data-action='open'])[$$index$$]";
	
	String recentHistoryCAILink			= "(//a[@class='cai-link' and text()='Review in Conversation AI' and not(@style)])[$$index$$]";
	String deleteVMQueue				= ".//*[@class='queue-name' and text()='$callQueue$']/ancestor::div[@class='row queue']/following-sibling::div[contains(@class,'player-container voicemail') and @style='display: block;']//img[@class='delete-voicemail btn-delete']";
	String playVMIconCallQueue			= ".//*[@class='queue-name' and text()='$callQueue$']/ancestor::div[contains(@class,'unread-new-voicemail')]//button[contains(@class,'play-voicemail')]";
	
	By salesForceErrorMsg 				= By.xpath("//*[@id='errors']//span[@class='message-text' and contains(text(), 'A Salesforce operation failed. (Error updating record: Supervisor Notes: data value too large:')]");
	
	public static enum MatchTypeFilters{
		All,
		Leads,
		Contacts,
		Unknown;
	}
	
	public static enum CallHistoryFiedls{
		callerName,
		callerPhone,
		dispositionAlert,
		MultiMatchAlert,
		OutgoingCall,
		IncomingCall
	}
	
	//*******Navigate to call history pages********//
	public void openCallsHistoryPage(WebDriver driver){
		if(!isElementVisible(driver, callsHistoryActiveImage, 0)){
			waitUntilVisible(driver, callsHistoryInactiveImage);
			clickElement(driver, callsHistoryInactiveImage);
			waitUntilVisible(driver, callsHistoryActiveImage);	
		}else {
			clickElement(driver, callsHistoryActiveImage);
			waitUntilVisible(driver, callsHistoryActiveImage);
		}
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	/**select match type filter to display (Contact, lead, unknown, all)
	 * @param driver
	 * @param filter
	 */
	public void selectMatchTypeFilter(WebDriver driver, MatchTypeFilters filter) {
		waitUntilVisible(driver, matchTypeFilter);
		selectFromDropdown(driver, matchTypeFilter, SelectTypes.value, filter.name());
		isElementVisible(driver, spinnerWheel, 1);
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, matchTypeFilter);
	}
	
	public void clickMyCallsLink(WebDriver driver){
		if(getWebelementIfExist(driver, myCallsinActiveLink)== null){
			clickElement(driver, myCallsActiveLink);
		}
		if(isElementVisible(driver, groupCallsInactiveLink, 5)){
			waitUntilVisible(driver, myCallsinActiveLink);
		}
		waitUntilInvisible(driver, spinnerWheel);
		hoverElement(driver, callsHistoryActiveImage);
	}
	
	public void clickGroupCallsLink(WebDriver driver){
		if(isElementVisible(driver, groupCallsActiveLink, 5)){
			clickByJs(driver, groupCallsActiveLink);
		}
		waitUntilVisible(driver, groupCallsInactiveLink);
		isElementVisible(driver, spinnerWheel, 1);
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, groupCallsHeading);
		if(isElementVisible(driver, callsHistoryActiveImage, 0))
			hoverElement(driver, callsHistoryActiveImage);
	}
	
	public void clickGroupsCalls(WebDriver driver) {
		clickElement(driver, groupCallsActiveLink);
	}
	
	public void verifyGroupCallsLinkInvisible(WebDriver driver){
		if(isElementVisible(driver, groupCallsActiveLink, 5)){
			waitUntilInvisible(driver, groupCallsActiveLink);
		}else{
			waitUntilInvisible(driver, groupCallsInactiveLink);
		}
	}
	//********Navigation to tabs********//
	public void switchToAllCallsTab(WebDriver driver){
		if(isElementVisible(driver, allInactiveTabLink, 0)){
			clickElement(driver, allInactiveTabLink);
		}
		waitUntilVisible(driver, allActiveTabLink);
	}
	
	public void switchToMissedCallsTab(WebDriver driver){
		if(getWebelementIfExist(driver, missedInactiveTabLink)!=null){
			clickElement(driver, missedInactiveTabLink);
		}
		waitUntilVisible(driver, missedActiveTabLink);
	}
	
	public void switchToVoiceMailTab(WebDriver driver){
		if(getWebelementIfExist(driver, voicemailInactiveTabLink)!=null){
			clickElement(driver, voicemailInactiveTabLink);
		}
		waitUntilVisible(driver, voicemailActiveTabLink);
	}
	
	public void switchToAlertsTab(WebDriver driver){
		if(getWebelementIfExist(driver, inActiveAlertTab)!=null){
			clickElement(driver, inActiveAlertTab);
		}
		waitUntilVisible(driver, activeAlertTab);
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void veifyAlertTabActive(WebDriver driver){
		waitUntilVisible(driver, activeAlertTab);
	}
	
	public boolean isAlertTabDisabled(WebDriver driver){
		return isElementVisible(driver, disabledAlertTab, 5);
	}
	//********Open call history entries*******//
	
	public boolean isCallerHistoryBlank(WebDriver driver){
		return !isListElementsVisible(driver, recentHistoryWithContactsOpenButton, 5);
	}
	
	public void openRecentContactCallHistoryEntry(WebDriver driver){
		openCallsHistoryPage(driver);
		clickMyCallsLink(driver);
		switchToAllCallsTab(driver);
		waitUntilInvisible(driver, spinnerWheel);
		openCallEntryByIndex(driver, 0);
	}
	
	public void openSecondRecentContactCallHistoryEntry(WebDriver driver){
		openCallsHistoryPage(driver);
		clickMyCallsLink(driver);
		switchToAllCallsTab(driver);
		openCallEntryByIndex(driver, 1);
	}
	
	public void openRecentContactCallHistoryEntryByIndex(WebDriver driver, int index){
		openCallsHistoryPage(driver);
		clickMyCallsLink(driver);
		switchToAllCallsTab(driver);
		openCallEntryByIndex(driver, index);
	}
	
	public void openRecentGroupCallEntry(WebDriver driver){
		openCallsHistoryPage(driver);
		clickGroupCallsLink(driver);
		switchToAllCallsTab(driver);
		waitUntilInvisible(driver, spinnerWheel);
		openCallEntryByIndex(driver, 0);
	}
	
	public void openSecondRecentGroupCallEntry(WebDriver driver){
		openCallsHistoryPage(driver);
		clickGroupCallsLink(driver);
		switchToAllCallsTab(driver);
		openCallEntryByIndex(driver, 1);
	}
	
	public void openCallEntryByIndex(WebDriver driver, int index){
		isElementVisible(driver, spinnerWheel, 1);
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, By.xpath(recentHistoryOpenButton.replace("$$index$$", String.valueOf(++index))));
		idleWaitInMs(300);
		clickByJs(driver, By.xpath(recentHistoryOpenButton.replace("$$index$$", String.valueOf(index))));
		if(!callScreenPage.isCallerMultiple(driver)){
			callToolsPanel.isRelatedRecordsIconVisible(driver);
		}
	}
	
	public void openHistoryCAICallEntryByIndex(WebDriver driver, int index){
		waitUntilInvisible(driver, spinnerWheel);
		waitUntilVisible(driver, By.xpath(recentHistoryCAILink.replace("$$index$$", String.valueOf(++index))));
		By historyCAIelement = By.xpath(recentHistoryCAILink.replace("$$index$$", String.valueOf(index)));
		clickByJs(driver, historyCAIelement);
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
	}
	
	public boolean isHistoryCAILinkVisible(WebDriver driver, int index) {
		By conversationAILinkLoc = By.xpath(recentHistoryCAILink.replace("$$index$$", String.valueOf(++index)));
		return isElementVisible(driver, conversationAILinkLoc, 3);
	}
	
	public void openCallerCallHistory(WebDriver driver, String callerName){
		By callerOpenHistoryButtons = By.xpath(callerOpenHistoryButtonLocator.replace("$$Caller$$", callerName));
		openCallsHistoryPage(driver);
		clickMyCallsLink(driver);
		switchToAllCallsTab(driver);
		waitUntilInvisible(driver, spinnerWheel);
		getElements(driver,callerOpenHistoryButtons).get(0).click();
	}
	
	public List<String> getHistoryCallerPhoneList(WebDriver driver){
		return getTextListFromElements(driver, recentHistoryCallerPhone);
	}
	
	public String getHistoryCallerName(WebDriver driver, int index){
		return getInactiveElements(driver, recentHistoryCallerName).get(index).getText();
	}
	
	public String getHistoryRelatedRecord(WebDriver driver, int index){
		return getInactiveElements(driver, recentHistoryRelatedRecord).get(index).getText();
	}
	
	public String getHistoryCallerPhone(WebDriver driver, int index){
		return HelperFunctions.getNumberInSimpleFormat(getElements(driver, recentHistoryCallerPhone).get(index).getText());
	}
	
	public String getHistoryCallerTime(WebDriver driver, int index){
		return getElements(driver, recentHistoryCallerTime).get(index).getText();
	}
	
	public String getHistoryTimeInHourWithoutSeconds(String timeStamp) {
		String[] time = timeStamp.split(",");
		String actualTime = HelperFunctions.getValueAccToRegex(time[1], "\\d{1,2}:\\d{1,2}");
		if(timeStamp.contains("PM")) {
			actualTime = actualTime.concat(" PM");
		}
		else {
			actualTime = actualTime.concat(" AM");
		}
		return actualTime;
	}
	
	public String getHistoryCallDisposition(WebDriver driver, int index){
		return getInactiveElements(driver, recentHistoryCallDisposition).get(index).getText();
	}
	
	public void verifyHistoCallDispNotVisible(WebDriver driver, int index){
		waitUntilInvisible(driver, getInactiveElements(driver, recentHistoryCallDisposition).get(index));
	}
	
	public List<String> getHistoryCallerTimeList(WebDriver driver){
		return getTextListFromElements(driver, recentHistoryCallerTime);
	}
	
	public void verifyHistoryDispositionAlert(WebDriver driver, int index){
		assertEquals(getElements(driver, dispositionAlertMessage).get(index).getText(), "Select a disposition for this call.");
	}
	
	public void verifyHistoryMultiMatchAlert(WebDriver driver, int index){
		assertEquals(getElements(driver, multiMatchAlertMessage).get(index).getText(), "Select a caller for this multi-match call.");
		assertEquals(getCssValue(driver, findElement(driver, multiMatchAlertMessage), CssValues.Color), "rgba(255, 69, 69, 1)");
	}
	
	public String getCallDuration(WebDriver driver, int index){
		if(isElementVisible(driver, getInactiveElements(driver, recentHistoryDurationTitle).get(index) , 0)) {
			return getInactiveElements(driver, recentHistoryDuration).get(index).getText();	
		}else {
			waitUntilInvisible(driver, getInactiveElements(driver, recentHistoryDurationTitle).get(index));
			return "00:00";
		}
	}
	
	public void verifyCallHistoryTabDataByIndex(WebDriver driver, int index, HashMap<CallHistoryFiedls, String> callHistoryData){
		// verify caller Name
		if(callHistoryData.get(CallHistoryFiedls.callerName)!=null){
			assertEquals(getHistoryCallerName(driver, index), callHistoryData.get(CallHistoryFiedls.callerName).toString());
		}
		
		// verify caller Phone
		if(callHistoryData.get(CallHistoryFiedls.callerPhone)!=null){
			assertEquals(getHistoryCallerPhone(driver, index), HelperFunctions.getNumberInSimpleFormat(callHistoryData.get(CallHistoryFiedls.callerPhone).toString()));
		}
		
		// verify disposition alert
		if(callHistoryData.get(CallHistoryFiedls.dispositionAlert)!=null){
			verifyHistoryDispositionAlert(driver, index);
		}
		
		// verify Multi Match alert
		if(callHistoryData.get(CallHistoryFiedls.MultiMatchAlert)!=null){
			verifyHistoryMultiMatchAlert(driver, index);
		}
		
		if(callHistoryData.get(CallHistoryFiedls.IncomingCall)!=null){
			assertTrue(isElementVisible(driver, getInactiveElements(driver, recentHistoryInboundCall).get(index), 0));
		}
		
		if(callHistoryData.get(CallHistoryFiedls.OutgoingCall)!=null){
			assertTrue(isElementVisible(driver, getInactiveElements(driver, recentHistoryOutboundCall).get(index), 0));
		}
	}
	
	public void verifyUnreadMissedCallRow(WebDriver driver, int index){
		List<WebElement> callHostryRows = getElements(driver, callHistoryRow);
		WebElement callRow = callHostryRows.get(index);
		assertEquals(getCssValue(driver, callRow.findElement(unreadMissedCallPhone), CssValues.FontWeight), "700");
		assertEquals(getCssValue(driver, callRow, CssValues.BackgroundColor), "rgba(229, 229, 229, 1)");
	}
	
	public void verifyReadMissedCallRow(WebDriver driver, int index){
		List<WebElement> callHostryRows = getElements(driver, callHistoryRow);
		WebElement callRow = callHostryRows.get(index);
		assertEquals(getCssValue(driver, callRow.findElement(readMissedCallPhone), CssValues.FontWeight), "400");
		assertEquals(getCssValue(driver, callRow, CssValues.BackgroundColor), "rgba(0, 0, 0, 0)");
	}
	
	public void verifyGroupUnreadMissedCallRow(WebDriver driver, int index){
		List<WebElement> callHostryRows = getElements(driver, callHistoryRow);
		WebElement callRow = callHostryRows.get(index);
		assertEquals(getCssValue(driver, callRow.findElement(unreadMissedCallPhone), CssValues.FontWeight), "700");
		assertEquals(getCssValue(driver, callRow, CssValues.BackgroundColor), "rgba(247, 247, 247, 1)");
	}
	
	//******Verify Counts********//
	//this method is to get the missed call count present to call history page
	public int getHistoryMissedCallCount(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		if(getWebelementIfExist(driver, historyMissedCallCount)!=null && getWebelementIfExist(driver, historyMissedCallCount).isDisplayed()) {
			return Integer.parseInt(getElementsText(driver, historyMissedCallCount));
		} else {
			System.out.println("No missed call count present");
			return 0;
		}
	}
	
	//verifying missed call count has increased
	public void isMissedCallCountIncreased(WebDriver driver, int previousCount) {
		waitUntilTextPresent(driver, historyMissedCallCount, Integer.toString(previousCount+1));
	}
	
	//this method is to get the missed voicemail count present to call history page
	public int getMissedVoicemailCount(WebDriver driver) {
		if(isElementVisible(driver, missedVoicemailCounts, 5)) {
			return Integer.parseInt(getElementsText(driver, missedVoicemailCounts));
		} else {
			System.out.println("No missed voicemail count present");
			return 0;
		}
	}
	
	public int getMissedCallCount(WebDriver driver) {
		if(isElementVisible(driver, missedCallCount, 5)) {
			return Integer.parseInt(getElementsText(driver, missedCallCount));
		} else {
			System.out.println("No missed call count present");
			return 0;
		}
	}
	
	public int getMissedGroupCallCount(WebDriver driver) {
		if(isElementVisible(driver, missedGroupCallCount, 5)) {
			return Integer.parseInt(getElementsText(driver, missedGroupCallCount));
		} else {
			System.out.println("No missed group calls count present");
			return 0;
		}
	}
	
	public int getMissedMyCallCount(WebDriver driver) {
		if(isElementVisible(driver, missedMyCallCount, 5)) {
			return Integer.parseInt(getElementsText(driver, missedMyCallCount));
		} else {
			System.out.println("No missed my calls count present");
			return 0;
		}
	}
	
	//verifying missed call count has increased
	public void isMissedVMCountIncreased(WebDriver driver, int previousCount) {
		if(previousCount >= 0) {
			waitUntilTextPresent(driver, missedVoicemailCounts, Integer.toString(previousCount+1));	
		}else {
			waitUntilInvisible(driver, missedVoicemailCounts);
		}
	}
	
	//verify alert tab counts
	public void isAlertCountIncreased(WebDriver driver, int previousCount){
		if(previousCount >= 0) {
			waitUntilTextPresent(driver, alertsCount, Integer.toString(previousCount+1));	
		}else {
			waitUntilInvisible(driver, alertsCount);
		}
	}
	
	//********Verify VM icons on Recent Call History Page*******//
	
	public void playVMByCallQueue(WebDriver driver, String callQueue){
		By vmPlayIconLoc = By.xpath(playVMIconCallQueue.replace("$callQueue$", callQueue));
		waitUntilVisible(driver, vmPlayIconLoc);
		clickElement(driver, vmPlayIconLoc);
	}

	public void isVMIconPresentByIndex(WebDriver driver, int index){
		By vmIconLoc = By.cssSelector(recentNewVMIcon.replace("$$Index$$", String.valueOf(index)));
		assertTrue(isElementVisible(driver, vmIconLoc, 5));
	}
	
	public Boolean isVMPlayPresentByIndex(WebDriver driver, int index){
		By vmPlayIconLoc = By.cssSelector(recentPlayVMIcon.replace("$$Index$$", String.valueOf(index)));
		return isElementVisible(driver, vmPlayIconLoc, 5);
	}
	
	public void playVMByIndex(WebDriver driver, int index){
		By vmPlayIconLoc = By.cssSelector(recentPlayVMIcon.replace("$$Index$$", String.valueOf(index)));
		clickElement(driver, vmPlayIconLoc);
		idleWait(3);
	}
	
	public void isVMDeleteIconPresentByIndex(WebDriver driver, int index){
		By vmDeleteIconLoc = By.cssSelector(recentDeleteVMIcon.replace("$$Index$$", String.valueOf(index)));
		assertTrue(isElementVisible(driver, vmDeleteIconLoc, 5));
	}
	
	public void isBlockBtnPresentByIndex(WebDriver driver, int index){
		By blockButtonLoc = By.cssSelector(recentBlockedBtn.replace("$$Index$$", String.valueOf(index)));
		assertTrue(isElementVisible(driver, blockButtonLoc, 5));
	}
	
	public void deleteVMByIndex(WebDriver driver, int index){
		By vmDeleteIconLoc = By.cssSelector(recentDeleteVMIcon.replace("$$Index$$", String.valueOf(index)));
		clickElement(driver, vmDeleteIconLoc);
	}
	
	public void deleteAllVMQueue(WebDriver driver, String callQueue) {
		By vmDeleteIconLoc = By.xpath(deleteVMQueue.replace("$callQueue$", callQueue));
		if (isListElementsVisible(driver, vmDeleteIconLoc, 2)) {
			int i = getElements(driver, vmDeleteIconLoc).size() - 1;
			while (i >= 0) {
				clickByJs(driver, getElements(driver, vmDeleteIconLoc).get(i));
				idleWait(2);
				i--;
			}
		}
	}
	
	public String getMissedVoicemailRecentNumber(WebDriver driver) {
		List<String> text = getTextListFromElements(driver, missedVoicemailList);
		return text.get(0);
	}
	
	//*******Load More Records*******//
	public void clickLoadMoreButton(WebDriver driver){
		waitUntilVisible(driver, loadMorebutton);
		clickByJs(driver, loadMorebutton);
		waitUntilInvisible(driver, spinnerWheel);
	}
	
	public void verifyHistoryCountsEquals(WebDriver driver, int expectedNumber){
		waitUntillNumberOfElementsAreEquals(driver, openCallerDetailButton, expectedNumber);
	}
	
	public void verifyHistoryCountsGreaterThan(WebDriver driver, int expectedNumber){
		waitUntillNumberOfElementsAreMore(driver, openCallerDetailButton, expectedNumber);
	}
	
	public void openRecentUnknownCallerEntry(WebDriver driver) {
		clickByJs(driver, getElements(driver, openCallerDetailButton).get(0));
	}
	//*******Load More Records End here*******//
	
	//*******Select Call History Entries starts here*******//
	public void selectAllHistoryEntry(WebDriver driver){
		waitUntilInvisible(driver, spinnerWheel);
		if(!findElement(driver, selectAllCheckBox).isSelected()){
			findElement(driver, selectAllCheckBox).click();
		}
	}
	
	public void unselectAllHistoryEntry(WebDriver driver){
		waitUntilInvisible(driver, spinnerWheel);
		if(findElement(driver, selectAllCheckBox).isSelected()){
			findElement(driver, selectAllCheckBox).click();
		}
	}
	
	public void selectCallEntryByIndex(WebDriver driver, int index){
		waitUntilInvisible(driver, spinnerWheel);
		getInactiveElements(driver, selectCallRowCheckBox).get(index).click();
	}
	
	public void verifyAllRecordsAreSelected(WebDriver driver){
		List<WebElement> callRowCheckBoxes = getInactiveElements(driver, selectCallRowCheckBox);
		for (WebElement callRowCheckBox : callRowCheckBoxes) {
			assertTrue(callRowCheckBox.isSelected());
		}
	}
	
	public void verifyNoRecordsAreSelected(WebDriver driver){
		List<WebElement> callRowCheckBoxes = getInactiveElements(driver, selectCallRowCheckBox);
		for (WebElement callRowCheckBox : callRowCheckBoxes) {
			assertFalse(callRowCheckBox.isSelected());
		}
	}
	
	public void markSelectedCallsRead(WebDriver driver){
		isElementVisible(driver, markAsReadBtn, 5);
		clickElement(driver, markAsReadBtn);
		waitUntilInvisible(driver, markAsReadBtn);
	}
	
	public void selectCallHistoryByDropDown(WebDriver driver, String allOrNone){
		selectFromDropdown(driver, selectCallHistoryDrpDwn, SelectTypes.visibleText, allOrNone);
	}
	
	public boolean isMarkSelectedCallsReadVisible(WebDriver driver){
		return isElementVisible(driver, markAsReadBtn, 5);
	}
	//*******Select Call History Entries ends here*******//
	
	/**verify whether salesforce supervisor error msg visible
	 * @param driver
	 * @return
	 */
	public boolean isSalesForceSuperVisorErrorMsgVisible(WebDriver driver) {
		return isElementVisible(driver, salesForceErrorMsg, 5);
	}
	
}
