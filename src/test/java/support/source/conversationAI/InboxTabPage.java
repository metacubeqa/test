
package support.source.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.testng.internal.collections.Pair;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import support.source.commonpages.Dashboard;

public class InboxTabPage extends SeleniumBase {

	SeleniumBase seleniumBase = new SeleniumBase();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallsTabPage callsTabPage = new CallsTabPage();

	// Inbox Tab
	By waitReviewIcon  			= By.cssSelector(".glyphicon-dashboard.wait-review");
	By agentInboxFilter 		= By.cssSelector(".user-picker .items");
	By agentInboxInput 			= By.cssSelector(".user-picker input");
	By agentInboxDropDown 		= By.cssSelector(".user-picker .selectize-dropdown-content div span.highlight");
	By inboxCount 				= By.cssSelector(".inbox-badge");
	By inboxTab 				= By.cssSelector("[data-tab='inbox']");
	By caiInboxCall 			= By.cssSelector(".glyphicon-menu-right");
	By inboxMessage 			= By.cssSelector(".recording-item .x-panel");
	By inboxSupNotesMessageList = By.xpath("//*[@class='col-md-12'][contains(text(),'Supervisor Note')]");
	By inboxAnnotationList 		= By.xpath("//*[@class='col-md-12'][contains(text(),'Annotation:')]");
	By inboxExpandList 			= By.cssSelector(".recording-item .col-md-2.expandable");
	By inboxCallTime 			= By.cssSelector(".inbox-only .col-md-2.expandable, .inbox-only .col-md-2:not(.text-right)");
	By inboxMessageStatus		= By.xpath("//*[@class='recording-item']//*[contains(@class,'x-panel')]");
	By inboxCalls 				= By.cssSelector(".recording-item");
	
	String inboxMsgWithSimpleNote 	  = ".//*[text()='Note: $$simpleNote$$']/ancestor::div[contains(@class,'inbox-only')]//h5";
	String inboxMsgWithSuperVisorNote = ".//*[text()='Supervisor Note: $$superVisorNote$$']/ancestor::div[contains(@class,'inbox-only')]//h5";
	String navigateInboxMsgAccToNotes = ".//*[contains(text(),'$$note$$')]/parent::div//a[@class='view-conversation']/span";
	String inboxMsgWithSaveSearch 	  = ".//*[text()='You have a new call in \"$$saveSearch$$\".']";
	String inboxContactCloudIcon	  = ".//*[text()='$contact$']/..//a[@class='sf-link tooltips']";
	String inboxAccountIcon	 		  = ".//*[text()='$account$']/..//a[@class='sf-link tooltips']";
	
	By inbox_notification_type		 = By.cssSelector(".action-filter");
	By inbox_annotation_notification = By.xpath(".//*[@class='recording-item']//h5[contains(text(),'annotated one of your calls.') or contains(text(),'annotated a call.')]");
	By inbox_sup_notes_notification  = By.xpath(".//*[@class='recording-item']//h5[contains(text(),'supervisor note on one of your calls.') or contains(text(),'has added a supervisor note.')]");
	By inbox_share_call_notification = By.xpath(".//*[@class='recording-item']//h5[contains(text(),'shared a call with you.')]");
	By inbox_flagged_call_notification = By.xpath(".//*[@class='recording-item']//h5[contains(text(),'flagged a call for review.')]");
	By inbox_all_notification 		 = By.cssSelector(".inbox-only h5");

	String clickAnnotationCall 	 = ".//*[text()='$$annotation$$']/..//span[@class='glyphicon glyphicon-menu-right']";
	
	By reviewI 					 = By.xpath(".//*[contains(@class,'text-right')]/*[contains(@class,'review')]");
	By iconWaitReview 			 = By.cssSelector(".text-right .wait-review");
	By iconReviewed 			 = By.cssSelector(".text-right .reviewed");
	By waitForReviewIconList	 = By.xpath(".//*[contains(@class,'wait-review') and not(contains(@style,'display: none;'))]");
	By reviewedIconList		     = By.xpath(".//*[contains(@class,'reviewed') and not(contains(@style,'display: none;'))]");

	By unreadDeleteIcon			= By.cssSelector(".unread .glyphicon-trash");
	By readDeleteIcon 			= By.cssSelector(".read .glyphicon-trash");
	By deleteNotiOK				= By.cssSelector(".btn-primary");
	By viewUnreadIcon	    	= By.cssSelector(".unread .glyphicon-menu-right");
	By viewReadIcon		    	= By.cssSelector(".read .glyphicon-menu-right");
	
	// Enum for inbox notification options
	public static enum InboxNotificationTypes {
		All("All (Default)"), Supnotes("Supervisor Notes"), Flagged("Flagged for Review"), Annotations(
				"Call Annotations");
		private String displayName;

		InboxNotificationTypes(String displayName) {
			this.displayName = displayName;
			System.out.println(displayName);
		}

		public String displayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}

	}

	// Open Inbox tab
	public void clickInboxTab(WebDriver driver) {
		clickElement(driver, inboxTab);
		dashboard.isPaceBarInvisible(driver);
	}

	// Select Inbox Notification
	public void selectInboxNotification(WebDriver driver, String notification) {
		selectFromDropdown(driver, inbox_notification_type, SelectTypes.visibleText, notification);
		Select sel = new Select(findElement(driver, inbox_notification_type));
		sel.selectByVisibleText(notification);
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
	}

	// Clear agent filter on Inbox
	public void clearAgentInbox(WebDriver driver) {
		clickElement(driver, agentInboxFilter);
		findElement(driver, agentInboxInput).sendKeys(Keys.BACK_SPACE);
		dashboard.isPaceBarInvisible(driver);

	}

	// Set Agent filtre on Inbox tab
	public void selectAgentInbox(WebDriver driver, String user) {
		enterTextandSelect(driver, agentInboxInput, user);
		dashboard.isPaceBarInvisible(driver);
	}

	// Open first Conversation AI Recording from Inbox
	public void viewCAIInbox(WebDriver driver) {
		List<WebElement> caiList = getElements(driver, caiInboxCall);
		clickElement(driver, caiList.get(0));
		dashboard.isPaceBarInvisible(driver);
	}

	// Verify shared call displayed on top of Inbox
	public void viewInboxMessage(WebDriver driver) {
		List<WebElement> caiList = getElements(driver, inboxExpandList);
		clickElement(driver, caiList.get(0));
		idleWait(2);
	}
	
	//verify and click cloud icon of contact inbox
	public void verifyContactCloudIcon(WebDriver driver, String contact){
		By contactCloudLoc = By.xpath(inboxContactCloudIcon.replace("$contact$", contact));
		assertTrue(isElementVisible(driver, getInactiveElements(driver, contactCloudLoc).get(0), 5));
		hoverElement(driver, getInactiveElements(driver, contactCloudLoc).get(0));
		assertEquals(getElementsText(driver, getInactiveElements(driver, contactCloudLoc).get(0)), "Link to Salesforce");
		clickByJs(driver, getInactiveElements(driver, contactCloudLoc).get(0));
	}
	
	//verify and click cloud icon of account inbox
	public void verifyAccountCloudIcon(WebDriver driver, String account){
		By accountCloudLoc = By.xpath(inboxAccountIcon.replace("$account$", account));
		assertTrue(isElementVisible(driver, getInactiveElements(driver, accountCloudLoc).get(0), 5));
		hoverElement(driver, getInactiveElements(driver, accountCloudLoc).get(0));
		assertEquals(getElementsText(driver, getInactiveElements(driver, accountCloudLoc).get(0)), "Link to Salesforce");
		clickByJs(driver, getInactiveElements(driver, accountCloudLoc).get(0));
	}
	
	// Get status of inbox message whather its read or unread 
	public boolean inboxMessageUnread(WebDriver driver) {
		List<WebElement> caiList = getElements(driver, inboxMessage);
		String color = getCssValue(driver, caiList.get(0), CssValues.BackgroundColor);
		String hex = Color.fromString(color).asHex();
		if (caiList.get(0).getAttribute("class").contains("unread")) {
			assertEquals(hex.toLowerCase(), "#0e53c4".toLowerCase());
		} else {
			assertEquals(hex.toLowerCase(), "#ffffff".toLowerCase());
		}
		return caiList.get(0).getAttribute("class").contains("unread");
	}
	
	// Verify message is read or unread 
	public boolean statusInboxMessage(WebDriver driver) {
		List<WebElement> caiList = getElements(driver, inboxMessageStatus);
			return caiList.get(0).getAttribute("class").trim().contains("unread");
	}


	// Verify Supervisor Notes entry present in Inbox tab
	public boolean supNoteEntryInbox(WebDriver driver, String supNote) {
		List<WebElement> resultList = getElements(driver, inboxSupNotesMessageList);
		return isListContainsText(driver, resultList, supNote);
	}

	// Verify Annotation Notes entry present in Inbox tab
	public boolean annotationEntryInbox(WebDriver driver, String supNote) {
		List<WebElement> resultList = getElements(driver, inboxAnnotationList);
		return isListContainsText(driver, resultList, supNote);
	}

	// get Agent informaiton first item of inbox tab
	public Pair<Boolean, Boolean> getWaitReviewIconStatus(WebDriver driver) {

		List<WebElement> reviewIcon = getInactiveElements(driver, reviewI);
		// Check Availability for Wait for Review-RED ICON on first call of
		// Inbox
		boolean waitIconDisplayed = reviewIcon.get(0).getAttribute("class").trim().contains("wait-review")
				&& !reviewIcon.get(0).getAttribute("style").trim().contains("display: none;");

		// Check Availability for REVIEWED-GRAY ICON on first call of Inbox
		boolean reviewedIconDisplayed = reviewIcon.get(1).getAttribute("class").trim().contains("reviewed")
				&& !reviewIcon.get(1).getAttribute("style").contains("display: none;");

		return new Pair<Boolean, Boolean>(waitIconDisplayed, reviewedIconDisplayed);
	}

	// get call time informaiton first item of inbox tab
	public String getCallTimeInboxResult(WebDriver driver) {

		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.callTime);
		String callDuration = agentList.get(0).getText().trim();
		if (callDuration.contains("PM")) {
			callDuration = callDuration.replace("PM", "pm");
		} else {
			callDuration = callDuration.replace("AM", "am");
		}

		return callDuration;
	}

	// get Call duration informaiton first item of inbox tab
	public String getCallDurationInboxResult(WebDriver driver) {
		isListElementsVisible(driver, callsTabPage.callDuration, 5);
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.callDuration);
		String callDuration = agentList.get(0).getText().trim();
		if (callDuration.contains("PM")) {
			callDuration = callDuration.replace("PM", "pm");
		} else {
			callDuration = callDuration.replace("AM", "am");
		}

		return callDuration;
	}

	// get Review Call status if present
	public String getAgentInboxResult(WebDriver driver) {

		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.agentResult);
		return agentList.get(0).getText().trim();
	}
	
	// get supervisor notes icon active or inactive
	public boolean getSupNotesIconStatus(WebDriver driver) {

		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.supNotesIconStatus);
		return agentList.get(0).getAttribute("class").contains("active");
	}

	// get annotation icon active or inactive on Inbox
	public boolean getAnnotationIconStatus(WebDriver driver) {
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.annotationIconStatus);
		return agentList.get(0).getAttribute("class").contains("active");
	}

	// get call notes icon active or inactive on Inbox
	public boolean getCallNotesIconStatus(WebDriver driver) {
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.notesIconStatus);
		return agentList.get(0).getAttribute("class").contains("active");
	}

	// get call notes icon active or inactive on Inbox
	public void clickCallNotesIcon(WebDriver driver) {
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.notesIconActive);
		clickElement(driver, agentList.get(0));
		dashboard.isPaceBarInvisible(driver);
	}

	// get call notes icon active or inactive on Inbox
	public void clickAnnotationIcon(WebDriver driver) {
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.annotationIconActive);
		clickElement(driver, agentList.get(0));
		dashboard.isPaceBarInvisible(driver);
	}

	// get call notes icon active or inactive on Inbox
	public void clickSuperVisorNotesIcon(WebDriver driver) {
		List<WebElement> agentList = getInactiveElements(driver, callsTabPage.supNotesIconActive);
		clickElement(driver, agentList.get(0));
		dashboard.isPaceBarInvisible(driver);
	}

	// Verify Search Result Order on Inbox
	public void verifyInboxOrder(WebDriver driver, int page, String order) throws Exception {
		for (int j = 0; j < page && j < 2; j++) {
			List<WebElement> callTimeList1 = getElements(driver, inboxCallTime);
			// take call time in a list
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			ArrayList<Date> callTimeList = new ArrayList<>();
			for (int i = 0; i < callTimeList1.size(); i++) {
				callTimeList.add(dateTimeFormatter.parse(callTimeList1.get(i).getText()));
			}
			if (order == "Date - Oldest to Newest") {
				boolean sorted = Ordering.natural().isOrdered(callTimeList);
				assertTrue(sorted, "Sorting on Ascending order not works fine");
			} else {
				boolean sorted = Ordering.natural().reverse().isOrdered(callTimeList);
				assertTrue(sorted, "Sorting on Descending order not works fine");
			}
			callsTabPage.navigateToNextCAIPage(driver, page, j);
		}
	}

	// Verify search on inbox notificaiton type and agent combination
	public void verifyInboxNotificationFilter(WebDriver driver, int page, String order) {
		By locator = null;
		ArrayList<String> verificationList = new ArrayList<>();
		switch (order) {
		case "Supervisor Notes":
			locator = inbox_sup_notes_notification;
			verificationList.add(" has added a supervisor note on one of your calls.");
			verificationList.add(" has added a supervisor note.");
			break;
		case "Call Annotations":
			locator = inbox_annotation_notification;
			verificationList.add(" annotated one of your calls.");
			verificationList.add(" annotated a call.");
			break;
		case "Flagged for Review":
			locator = inbox_flagged_call_notification;
			verificationList.add(" has flagged a call for review.");
			break;
		default:
			break;
		}
		for (int j = 0; j < page && j < 1; j++) {
			List<WebElement> inboxList = getElements(driver, locator);
			// take call time in a list
			for (int i = 0; i < inboxList.size(); i++) {
				for (String verificationText : verificationList) {
					if (inboxList.get(i).getText().trim().contains(verificationText)) {
						assertTrue(inboxList.get(i).getText().trim().contains(verificationText));
					}
				}
			}
			callsTabPage.navigateToNextCAIPage(driver, page, j);
		}
	}

	// Verify search on inbox notificaiton type and agent combination
	public void verifyInboxNotificationAgentFilter(WebDriver driver, int page, String order, String agent)
			throws Exception {
		By locator = null;
		String verificationText = "";
		switch (order) {
		case "Supervisor Notes":
			locator = inbox_sup_notes_notification;
			verificationText = agent + " has added a supervisor note";
			break;
		case "Call Annotations":
			locator = inbox_annotation_notification;
			verificationText = agent + " annotated";
			break;
		case "Flagged for Review":
			locator = inbox_flagged_call_notification;
			verificationText = agent + " has flagged a call for review.";
			break;
		default:
			break;
		}
		for (int j = 0; j < page && j < 1; j++) {
			waitUntilVisible(driver, locator, 3);
			List<WebElement> inboxList = getElements(driver, locator);
			// take call time in a list
			for (int i = 0; i < inboxList.size(); i++) {
				assertTrue(inboxList.get(i).getText().trim().contains(verificationText));
			}
			callsTabPage.navigateToNextCAIPage(driver, page, j);
		}
	}

	// Verify notification filter on inbox with agent filter
	public void verifyInboxAgentFilter(WebDriver driver, int page, String agent) {

		for (int j = 0; j < page && j < 1; j++) {
			List<WebElement> inboxList = getElements(driver, inbox_all_notification);

			for (int i = 0; i < inboxList.size(); i++) {
				assertTrue(inboxList.get(i).getText().trim().contains(agent));
			}
			callsTabPage.navigateToNextCAIPage(driver, page, j);
		}
	}

	public String getInboxFirstMsg(WebDriver driver) {
		List<WebElement> inboxList = getElements(driver, inbox_all_notification);
		return getElementsText(driver, inboxList.get(0));
	}

	// Method to get count of Inbox unread notifications
	public int getInboxCount(WebDriver driver) {
		if (isElementVisible(driver, inboxCount, 5)) {
			return Integer.parseInt(getElementsText(driver, inboxCount));
		} else
			return 0;
	}

	// Open specific call from Inbox having specific supervisor notes or
	// annotation
	public void openCallFromInbox(WebDriver driver, String text) {
		By callLocator = By.xpath(clickAnnotationCall.replace("$$annotation$$", "Annotation: " + text));
		clickElement(driver, callLocator);
	}

	// Method to delete unread notification
	public void deleteUnreadNotification(WebDriver driver, int page) {
		for (int j = 0; j < page; j++) {
			if (isListElementsVisible(driver, unreadDeleteIcon, 2)) {
				List<WebElement> deleteUnRead = getElements(driver, unreadDeleteIcon);
				deleteUnRead.get(0).click();
				clickElement(driver, deleteNotiOK);
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				break;
			} else
				callsTabPage.navigateToNextCAIPage(driver, page, j);

		}
	}

	// Method to delete unread notification
	public void deleteReadNotification(WebDriver driver, int page) {
		for (int j = 0; j < page; j++) {
			if (isListElementsVisible(driver, readDeleteIcon, 2)) {
				List<WebElement> deleteRead = getElements(driver, readDeleteIcon);
				deleteRead.get(0).click();
				clickElement(driver, deleteNotiOK);
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				break;
			} else
				callsTabPage.navigateToNextCAIPage(driver, page, j);
		}

	}

	// Method to view an unread notification
	public void viewUnreadNotification(WebDriver driver, int page) {
		for (int j = 0; j < page; j++) {
			if (isListElementsVisible(driver, viewUnreadIcon, 2)) {
				List<WebElement> viewUnRead = getElements(driver, viewUnreadIcon);
				viewUnRead.get(0).click();
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				break;
			} else
				callsTabPage.navigateToNextCAIPage(driver, page, j);
		}

	}

	// Method to view a read notification
	public void viewReadNotification(WebDriver driver, int page) {
		for (int j = 0; j < page; j++) {
			if (isListElementsVisible(driver, viewReadIcon, 2)) {
				List<WebElement> viewRead = getElements(driver, viewReadIcon);
				viewRead.get(0).click();
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				break;
			} else
				callsTabPage.navigateToNextCAIPage(driver, page, j);
		}
	}

	public void waitReviewIconVisible(WebDriver driver) {
		List<WebElement> waitReviewIconList = getInactiveElements(driver, waitReviewIcon);
		assertTrue(isElementVisible(driver, waitReviewIconList.get(0), 5));
	}

	public String getInboxMsgWithSpecificNote(WebDriver driver, String note) {
		dashboard.isPaceBarInvisible(driver);
		By inboxMsgLoc = By.xpath(inboxMsgWithSimpleNote.replace("$$simpleNote$$", note));
		waitUntilVisible(driver, inboxMsgLoc);
		return getElementsText(driver, inboxMsgLoc);
	}

	public String getInboxMsgWithSuperVisorNote(WebDriver driver, String superVisorNote) {
		dashboard.isPaceBarInvisible(driver);
		By inboxMsgLoc = By.xpath(inboxMsgWithSuperVisorNote.replace("$$superVisorNote$$", superVisorNote));
		waitUntilVisible(driver, inboxMsgLoc);
		return getElementsText(driver, inboxMsgLoc);
	}

	public void goToMenuRightOfInboxMsg(WebDriver driver, String note) {
		dashboard.isPaceBarInvisible(driver);
		By moveRightInboxMsgLoc = By.xpath(navigateInboxMsgAccToNotes.replace("$$note$$", note));
		waitUntilVisible(driver, moveRightInboxMsgLoc);
		clickElement(driver, moveRightInboxMsgLoc);
	}

	public boolean verifySaveSearchInbox(WebDriver driver, String saveSearch) {
		By saveSearchloc = By.xpath(inboxMsgWithSaveSearch.replace("$$saveSearch$$", saveSearch));
		return isElementVisible(driver, saveSearchloc, 5);
	}

	// get call disposition first item of inbox tab
	public String getCallDispositionResult(WebDriver driver) {

		List<WebElement> dispositionList = getInactiveElements(driver, callsTabPage.dispositionResult);
		return dispositionList.get(0).getText().trim();
	}

	// get Contact name first item of inbox tab
	public String getContactNameResult(WebDriver driver) {

		List<WebElement> nameList = getInactiveElements(driver, callsTabPage.nameResult);
		return nameList.get(0).getText().trim();
	}
}