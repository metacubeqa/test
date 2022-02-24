package support.cases.conversationAI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.admin.AccountCallRecordingTab;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.CallsTabPage;
import support.source.conversationAI.CallsTabPage.CallData;
import support.source.conversationAI.DashBoardConversationAI;
import support.source.conversationAI.InboxTabPage;
import support.source.conversationAI.SaveSearchPage;
import support.source.conversationAI.SetUpTabPage;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CoachCases extends SupportBase{
	
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	DashBoardConversationAI dashBoardConversationAI = new DashBoardConversationAI();
	SetUpTabPage setUpPage = new SetUpTabPage();
	InboxTabPage inboxPage = new InboxTabPage();
	SoftphoneBase softphoneBase = new SoftphoneBase();
	SaveSearchPage saveSearchPage = new SaveSearchPage();
	CallsTabPage callsTabPage = new CallsTabPage();
	Dashboard dashBoard = new Dashboard();
	AccountCallRecordingTab adminCallRecordingTab = new AccountCallRecordingTab();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	ContactDetailPage contactDetailPage = new ContactDetailPage();
	Random random  = new Random();
	UsersPage usersPage = new UsersPage();
	
	private String contactFirstName;
	private String contactAccountName;
	private String leadFirstName;
	
	@BeforeClass(groups = {"Regression"})
	public void beforeClass() {
		contactFirstName = CONFIG.getProperty("contact_first_name");
		contactAccountName = CONFIG.getProperty("contact_account_name");
		leadFirstName = CONFIG.getProperty("lead_first_name");
	}
	
	@Test(groups = { "Regression" })
	public void verify_coach_icon_when_connected_end_before_one_min() {
		
		System.out.println("Test case --verify_coach_icon_when_connected_end_before_one_min-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		callsTabPage.switchToTab(caiCallerDriver, 1);
		
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		
		//Open dial pad of driver and dial to support driver
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.idleWait(1);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		callsTabPage.idleWait(10);
		softPhoneCalling.hangupActiveCall(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsLead(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		softphoneBase.reloadSoftphone(caiCallerDriver);
		softphoneBase.reloadSoftphone(caiVerifyDriver);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_coach_icon_when_connected_end_before_one_min-- passed ");
	}
	
//	@Test(groups = { "Regression" })
	public void continue_call_more_than_one_min_verify_inbox_entry_and_activity_feed() {
		
		System.out.println("Test case --continue_call_more_than_one_min_verify_inbox_entry_and_activity_feed-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiCallerDriver", true);
		driverUsed.put("caiVerifyDriver", true);

		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		
		callsTabPage.switchToTab(caiVerifyDriver, 2);
		dashBoard.clickConversationAI(caiVerifyDriver);
		int inboxCount = inboxPage.getInboxCount(caiVerifyDriver);
		
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		
		// Verifying coach icon not visible
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
	
		//creating conversation AI recording
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		callToolsPanel.enterCallNotes(caiCallerDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(caiCallerDriver, randomIndex);
		String disposition = CONFIG.getProperty("qa_cai_disposition");
		callToolsPanel.selectDispositionUsingText(caiCallerDriver, disposition);

		callsTabPage.idleWait(70);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		callToolsPanel.clickCoachIcon(caiCallerDriver);
		callToolsPanel.verifyCoachIconColorSelected(caiCallerDriver);
		
		if (callScreenPage.isCallerUnkonwn(caiCallerDriver)) {
			callToolsPanel.callNotesSectionVisible(caiCallerDriver);
			callScreenPage.addCallerAsContact(caiCallerDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(caiCallerDriver);
		contactDetails.put("Rating", callScreenPage.getCallerRating(caiCallerDriver));
		contactDetails.put("Email", callScreenPage.getCallerEmail(caiCallerDriver));
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(caiCallerDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		
		// Navigating to ALL activity and verify conversation ai link
		softPhoneActivityPage.navigateToAllActivityTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);

		// Navigating to Call notes tab and verify conversation ai link
		softPhoneActivityPage.navigateToCallNotesTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		// Navigating to Calls tab and verify conversation ai link
		softPhoneActivityPage.navigateToCallsTab(caiCallerDriver);
		softPhoneActivityPage.waitAndVerifyCAILinkVisible(caiCallerDriver, callSubject);
		
		callsTabPage.switchToTab(caiVerifyDriver, 2);
		dashBoard.clickConversationAI(caiVerifyDriver);
		int inboxCountAfter = inboxPage.getInboxCount(caiVerifyDriver);
		assertTrue(inboxCountAfter > inboxCount, "Count is same");

		//Verifying in Inbox section
		dashBoardConversationAI.navigateToInboxSection(caiVerifyDriver);
		inboxPage.selectInboxNotification(caiVerifyDriver, InboxTabPage.InboxNotificationTypes.Flagged.toString());
		inboxPage.viewInboxMessage(caiVerifyDriver);
		assertEquals(contactDetails.get("Name"), inboxPage.getContactNameResult(caiVerifyDriver));
		assertEquals(disposition, inboxPage.getCallDispositionResult(caiVerifyDriver));
		
		contactDetails.put("CallDate",  inboxPage.getCallTimeInboxResult(caiVerifyDriver));
		contactDetails.put("CallDuration", inboxPage.getCallDurationInboxResult(caiVerifyDriver));
		
		String msg = inboxPage.getInboxFirstMsg(caiVerifyDriver);
		assertEquals(msg, String.format("%s has flagged a call for review.", CONFIG.getProperty("qa_cai_caller_name")));
		inboxPage.waitReviewIconVisible(caiVerifyDriver);

		//Verifying in Activity Feeds section
		dashBoardConversationAI.navigateToActivityFeedSection(caiVerifyDriver);
		inboxPage.viewInboxMessage(caiVerifyDriver);
		assertEquals(contactDetails.get("Name"), inboxPage.getContactNameResult(caiVerifyDriver));
		assertEquals(disposition, inboxPage.getCallDispositionResult(caiVerifyDriver));
		assertEquals(contactDetails.get("CallDate"), inboxPage.getCallTimeInboxResult(caiVerifyDriver));
		assertEquals(contactDetails.get("CallDuration"), inboxPage.getCallDurationInboxResult(caiVerifyDriver));
		
		msg = inboxPage.getInboxFirstMsg(caiVerifyDriver);
		assertEquals(msg, String.format("%s has flagged a call for review.", CONFIG.getProperty("qa_cai_caller_name")));

		//Verifying notes are correct
		dashBoard.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);
		dashBoardConversationAI.navigateToCallsSection(caiCallerDriver);
		callsTabPage.setRatingFilter(caiCallerDriver, String.valueOf(randomIndex));
		callsTabPage.setEngagementFilter(caiCallerDriver, CallData.FlaggedForCoaching.name());
		
		//Clicking and verifying contact cloud icon 
		String parentWindow = caiCallerDriver.getWindowHandle();
		inboxPage.verifyContactCloudIcon(caiCallerDriver, contactDetails.get("Name"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertEquals(contactDetailPage.getCallerName(caiCallerDriver), contactDetails.get("Name"));
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Name")));
		
		//closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		//Clicking and verifying contact cloud icon
		inboxPage.verifyAccountCloudIcon(caiCallerDriver, contactDetails.get("Company"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Company")));
		
		//closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		callsTabPage.viewCAI(caiCallerDriver);
		
		//verifying on calls page
		assertEquals(contactDetails.get("Name"), callsTabPage.getCallName(caiCallerDriver));
		assertEquals(contactDetails.get("Company"), callsTabPage.getCallCompany(caiCallerDriver));
		assertEquals(contactDetails.get("Number"), callsTabPage.getCallNumber(caiCallerDriver));
		assertEquals(contactDetails.get("Title"), callsTabPage.getCallTitle(caiCallerDriver));
		assertEquals(contactDetails.get("Email"), callsTabPage.getCallEmail(caiCallerDriver));
		assertEquals(disposition, callsTabPage.getCallDisposition(caiCallerDriver));
		assertEquals(contactDetails.get("CallDate"), callsTabPage.getCallTime(caiCallerDriver));
		assertEquals(contactDetails.get("CallDuration"), callsTabPage.getCallDuration(caiCallerDriver));
		assertEquals("Outbound", callsTabPage.getCallDirection(caiCallerDriver));
		
		String expectedCallNotes = callsTabPage.getTextOfCallNotes(caiCallerDriver);
		assertEquals(callNotes, expectedCallNotes,  "Call notes do not match");
	
		parentWindow = caiCallerDriver.getWindowHandle();
		String associatedRecord = callsTabPage.getCallAssociatedRecord(caiCallerDriver);
		assertTrue(callsTabPage.isSFCompanyLinkVisible(caiCallerDriver));
		callsTabPage.clickSFLinkSFDC(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		sfTaskDetailPage.idleWait(1);
		assertTrue(caiCallerDriver.getCurrentUrl().contains(associatedRecord));
		assertEquals(sfTaskDetailPage.getRating(caiCallerDriver), contactDetails.get("Rating"));
		
		//Clicking recording url and modifying supervisor notes there
		String sfTaskWindow = caiCallerDriver.getWindowHandle();
		String superVisorNote = "AutoSuperVisorNotes".concat(HelperFunctions.GetRandomString(3));
		sfTaskDetailPage.clickRecordingURL(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.idleWait(1);
		callsTabPage.enterSupervisorNotes(caiCallerDriver, superVisorNote);
		callsTabPage.refreshCurrentDocument(caiCallerDriver);
		String expectedSuperVisorNotes = callsTabPage.getTextOfSuperVisorNotes(caiCallerDriver);
		assertEquals(superVisorNote, expectedSuperVisorNotes, "Super visor notes does not match");
		callsTabPage.closeTab(caiCallerDriver);

		//switching to sf task page and verifying supervisor notes there
		caiCallerDriver.switchTo().window(sfTaskWindow);
		callsTabPage.refreshCurrentDocument(caiCallerDriver);
		dashBoard.isPaceBarInvisible(caiCallerDriver);
		assertEquals(sfTaskDetailPage.getSuperVisorNotes(caiCallerDriver), superVisorNote);
		callsTabPage.closeTab(caiCallerDriver);

		caiCallerDriver.switchTo().window(parentWindow);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --continue_call_more_than_one_min_verify_inbox_entry_and_activity_feed-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_cloud_icon_details_inbox_calls_section() {
		System.out.println("Test case --verify_cloud_icon_details_inbox_calls_section-- started ");

		initializeSupport("caiCallerDriver");
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		 
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(caiCallerDriver);
		
		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);

		//Verifying cloud icon details in calls section
		dashBoardConversationAI.navigateToCallsSection(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.setEngagementFilter(caiCallerDriver, CallData.FlaggedForCoaching.name());
		callsTabPage.idleWait(2);
		
		//Clicking and verifying contact cloud icon 
		String parentWindow = caiCallerDriver.getWindowHandle();
		inboxPage.verifyContactCloudIcon(caiCallerDriver, contactDetails.get("Name"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertEquals(contactDetailPage.getCallerName(caiCallerDriver), contactDetails.get("Name"));
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Name")));
		
		//closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		//Clicking and verifying contact cloud icon
		inboxPage.verifyAccountCloudIcon(caiCallerDriver, contactDetails.get("Company"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Company")));
		
		//closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		//Verifying cloud icon details in Inbox section
		dashBoardConversationAI.navigateToInboxSection(caiCallerDriver);
		inboxPage.idleWait(2);
		inboxPage.viewInboxMessage(caiCallerDriver);

		// Clicking and verifying contact cloud icon
		inboxPage.verifyContactCloudIcon(caiCallerDriver, contactDetails.get("Name"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertEquals(contactDetailPage.getCallerName(caiCallerDriver), contactDetails.get("Name"));
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Name")));

		// closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);

		// Clicking and verifying contact cloud icon
		inboxPage.verifyAccountCloudIcon(caiCallerDriver, contactDetails.get("Company"));
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		sfTaskDetailPage.closeLightningDialogueBox(caiCallerDriver);
		assertTrue(contactDetailPage.getHeaderRowName(caiCallerDriver).contains(contactDetails.get("Company")));

		// closing and switching back to window
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_cloud_icon_details_inbox_calls_section-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void refresh_page_after_updating_details_in_sfdc() {
		
		System.out.println("Test case --refresh_page_after_updating_details_in_sfdc-- started ");
		
		initializeSupport("caiCallerDriver");
		callsTabPage.switchToTab(caiCallerDriver, 2);
		
		//setting filter for lead contacts
		dashBoard.clickConversationAI(caiCallerDriver);
		assertTrue(dashBoardConversationAI.navigateToCallsSection(caiCallerDriver));
		String prospectName = CONFIG.getProperty("lead_first_name");
		
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.idleWait(2);
		callsTabPage.setNameFilter(caiCallerDriver, prospectName);
		callsTabPage.verifyFilters(caiCallerDriver, prospectName);
		callsTabPage.viewCAI(caiCallerDriver);
		
		String parentWindow = caiCallerDriver.getWindowHandle();
		callsTabPage.clickSFLinkCallerName(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, callScreenPage.getTabCount(caiCallerDriver));
		callsTabPage.waitForPageLoaded(caiCallerDriver);
		
		//verifying details on salesforce tab
		HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
		leadDetails.put(ContactDetailPage.LeadDetailsFields.firstName, leadFirstName.concat(HelperFunctions.GetRandomString(2)));
		leadDetails.put(ContactDetailPage.LeadDetailsFields.lastName, "Automation".concat(HelperFunctions.GetRandomString(2)));
		leadDetails.put(ContactDetailPage.LeadDetailsFields.company, contactAccountName.concat(HelperFunctions.GetRandomString(2)));
		leadDetails.put(ContactDetailPage.LeadDetailsFields.leadStatus, CallScreenPage.LeadStatusType.Contacted.toString()); 
		contactDetailPage.updateSalesForceLeadDetails(caiCallerDriver, leadDetails);
		callsTabPage.closeTab(caiCallerDriver);
		caiCallerDriver.switchTo().window(parentWindow);
		
		//Refreshing page and verifying details
		callsTabPage.refreshCurrentDocument(caiCallerDriver);
		String fullNameAfterUpdate = leadDetails.get(ContactDetailPage.LeadDetailsFields.firstName).toString().concat(" ").concat(leadDetails.get(ContactDetailPage.LeadDetailsFields.lastName).toString());
		assertEquals(callsTabPage.getCallName(caiCallerDriver), fullNameAfterUpdate);
		assertEquals(callsTabPage.getCallCompany(caiCallerDriver), leadDetails.get(ContactDetailPage.LeadDetailsFields.company));
		assertEquals(callsTabPage.getLeadStatus(caiCallerDriver), leadDetails.get(ContactDetailPage.LeadDetailsFields.leadStatus));

		//verifying name updated on calls page
		dashBoard.clickConversationAI(caiCallerDriver);
		String expectedNameUpdated = callsTabPage.clickElasticSearchFilter(caiCallerDriver, CallData.Name.name(), 1);
		assertEquals(fullNameAfterUpdate, expectedNameUpdated, "Name not updated on calls tab page");
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --refresh_page_after_updating_details_in_sfdc-- passed");
	}

	@Test(groups = { "Regression" })
	public void verify_tooltip_and_tags_sentiments() {
		
		System.out.println("Test case --verify_tooltip_and_tags_sentiments-- started ");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		
		//selecting filters with no annotations
		callsTabPage.setEngagementFilter(caiCallerDriver, CallData.HasAnnotations.name());
		callsTabPage.clickSelectedFilterPlus(caiCallerDriver, "Has Annotations");
		
		callsTabPage.viewCAI(caiCallerDriver);
		
		// annotation and sentiment tag verification
		callsTabPage.clickAnnotateBtn(caiCallerDriver);
		callsTabPage.idleWait(10);
		callsTabPage.clickStopBtn(caiCallerDriver);

		String annotationTag = HelperFunctions.GetRandomString(30);
		callsTabPage.enterAnnotation(caiCallerDriver, annotationTag);
		String callDetailsTag = HelperFunctions.GetRandomString(30);
		callsTabPage.enterCallDetailsTag(caiCallerDriver, callDetailsTag);

		String tag200 = "oyxicmcfwgtnisdehoxpdkhrseytljdbsomzvonlbkdyrqceethnnzyydwsqwfyeloydycbuydudphittvmyheiaihdebuzzitzqeqobkcizymovvvrqmlgshrzacjtykelbcuopfxpulhasxoghtwpywoxlkjkiylgjcrpgsnyzvnlrvphuobtncxeyxkumkdwqquwa";
		String tagMoreThan200 = "oyxicmcfwgtnisdehoxpdkhrseytljdbsomzvonlbkdyrqceethnnzyydwsqwfyeloydycbuydudphittvmyheiaihdebuzzitzqeqobkcizymovvvrqmlgshrzacjtykelbcuopfxpulhasxoghtwpywoxlkjkiylgjcrpgsnyzvnlrvphuobtncxeyxkumkdwqquwaqwqws";
		
		// verifying with moreThan200 character and 200 char
		callsTabPage.verifyTagsForeMoreThan200CharNotAccept(caiCallerDriver, tagMoreThan200, tag200);
		
		// Verifying special characters in supervisor notes
		String superVisorNote = ";',./[]@!$@%$&*()_+-=<>:\"?";
		callsTabPage.enterSupervisorNotes(caiCallerDriver, superVisorNote);
		callsTabPage.refreshCurrentDocument(caiCallerDriver);
		String expectedSuperVisorNotes = callsTabPage.getTextOfSuperVisorNotes(caiCallerDriver);
		assertEquals(superVisorNote, expectedSuperVisorNotes, "Super visor notes does not match");
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_tooltip_and_tags_sentiments-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void coach_icon_gets_removed_when_user_makes_conference() {
		
		System.out.println("Test case --coach_icon_gets_removed_when_user_makes_conference-- started ");
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiVerifyDriver);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);

		callsTabPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfHoldCall(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		//Hanging up call if present and checking coach icon
		callsTabPage.switchToTab(caiCallerDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiCallerDriver);
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_cai_verify_smart_no"));
		softPhoneCalling.pickupIncomingCall(caiVerifyDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(caiCallerDriver));

		//making a call
		softPhoneCalling.softphoneAgentCall(caiCallerDriver, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.pickupIncomingCall(adminDriver);
		assertTrue(callToolsPanel.isCoachIconVisible(adminDriver));
		
		// checking hold call actions button resume and merge
		callsTabPage.idleWait(1);
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(caiCallerDriver);
		callsTabPage.idleWait(1);
		
		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(caiCallerDriver);
		assertFalse(callToolsPanel.isCoachIconVisible(caiCallerDriver));
		softPhoneCalling.hangupIfInActiveCall(caiCallerDriver);
		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickConversationAI(caiCallerDriver);

		// switching tab
		callsTabPage.switchToTab(caiVerifyDriver, 1);
		softPhoneCalling.hangupIfHoldCall(caiVerifyDriver);
		softPhoneCalling.hangupIfInActiveCall(caiVerifyDriver);
				
		//switching tab
		callsTabPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfHoldCall(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --coach_icon_gets_removed_when_user_makes_conference-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_restrict_recording_download() {
		
		System.out.println("Test case --verify_restrict_recording_download-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		callsTabPage.switchToTab(caiCallerDriver, 2);
		adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
		adminCallRecordingTab.enableRestrictCallRecordingSetting(caiCallerDriver);
		adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
		
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.clearAllFilters(caiCallerDriver);
		callsTabPage.viewCAI(caiCallerDriver);
		boolean result = callsTabPage.isDownloadFileActionVisible(caiCallerDriver);
		assertTrue(result);
		callsTabPage.clickDownLoadFileActionLink(caiCallerDriver);
		
		initializeSupport("caiSupportDriver");
		driverUsed.put("caiSupportDriver", true);
		
		dashBoard.clickConversationAI(caiSupportDriver);
		callsTabPage.viewCAI(caiSupportDriver);
		
		result = callsTabPage.isDownloadFileActionVisible(caiSupportDriver);
		assertFalse(result);

		//disabling call recording
		callsTabPage.switchToTab(caiCallerDriver, 2);
		adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
		adminCallRecordingTab.disableRestrictCallRecordingSetting(caiCallerDriver);
		adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
		
		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiSupportDriver", false);
		System.out.println("Test case --verify_restrict_recording_download-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_super_visor_notes_not_editable_when_no_member_in_team_section() {
		
		System.out.println("Test case  --verify_super_visor_notes_not_editable_when_no_member_in_team_section-- started ");
		// updating the driver used
		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		callsTabPage.switchToTab(caiCallerDriver, 2);
		dashBoard.clickOnUserProfile(caiCallerDriver);
		
		List <String> teamNameList = usersPage.getTeamNamesList(caiCallerDriver);
		
		//deleting the group members
		if ((teamNameList != null) && !teamNameList.isEmpty()) {
			for (String teamMember : teamNameList) {
				usersPage.deleteTeamMember(caiCallerDriver, teamMember);
			}
			usersPage.savedetails(caiCallerDriver);
		}
		
		// verifying super visor notes are not editable
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.viewCAI(caiCallerDriver);
		assertFalse(callsTabPage.isSuperVisorNoteEditable(caiCallerDriver), "Super visor notes are editable");
		
		//adding back the members
		dashBoard.clickOnUserProfile(caiCallerDriver);
		if ((teamNameList != null) && !teamNameList.isEmpty()) {
			for (String teamMember : teamNameList) {
				usersPage.clickAddTeamIcon(caiCallerDriver);
				usersPage.addUserToTeams(caiCallerDriver, teamMember);
			}
		} else {
			usersPage.clickAddTeamIcon(caiCallerDriver);
			usersPage.addUserToTeams(caiCallerDriver, CONFIG.getProperty("new_qa_automation_group"));
		}
		usersPage.savedetails(caiCallerDriver);
		
		// verifying super visor notes are editable
		dashBoard.clickConversationAI(caiCallerDriver);
		callsTabPage.viewCAI(caiCallerDriver);
		assertTrue(callsTabPage.isSuperVisorNoteEditable(caiCallerDriver), "Super visor notes are not editable");

		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case  --verify_super_visor_notes_not_editable_when_no_member_in_team_section-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_inbox_notification_disable_but_can_update_email_notifications_agent() {
		
		System.out.println("Test case  --verify_inbox_notification_disable_but_can_update_email_notifications_agent-- started ");
		// updating the driver used
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);
		
		dashBoard.switchToTab(caiVerifyDriver, 2);
		dashBoard.navigateToCAINotifications(caiVerifyDriver);
		
		assertTrue(setUpPage.isInboxNotificationListDisabled(caiVerifyDriver));
		assertFalse(setUpPage.isEmailNotificationListDisabled(caiVerifyDriver));
		
		setUpPage.checkEmailNotification(caiVerifyDriver, "Annotations");
		setUpPage.verifyCheckedEmailNotification(caiVerifyDriver, "Annotations");
		
		setUpPage.unCheckEmailNotification(caiVerifyDriver, "Annotations");
		setUpPage.verifyNotificationMsgSetting(caiVerifyDriver);
		setUpPage.verifyUnCheckedEmailNotification(caiVerifyDriver, "Annotations");
		
		setUpPage.checkEmailNotification(caiVerifyDriver, "Annotations");
		setUpPage.verifyNotificationMsgSetting(caiVerifyDriver);
		setUpPage.verifyCheckedEmailNotification(caiVerifyDriver, "Annotations");
		
		// Setting caiCallerDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case  --verify_inbox_notification_disable_but_can_update_email_notifications_agent-- passed ");
	}


//	@Test(groups = { "MediumPriority" })
	public void verify_msg_when_delete_all_keywords() {

		// updating the driver used
		initializeSupport("caiVerifyDriver");
		driverUsed.put("caiVerifyDriver", true);

		dashBoard.switchToTab(caiVerifyDriver, 2);
		dashBoard.clickConversationAI(caiVerifyDriver);
		setUpPage.clickSetUpTab(caiVerifyDriver);
		setUpPage.clickKeywordGroupLeftNav(caiVerifyDriver);
		
		//delete all keywords
		
		
		//verify keyword message

		driverUsed.put("caiVerifyDriver", false);
		System.out.println("Test case --verify_msg_when_delete_all_keywords--Passed");

	}	
	
	@AfterMethod(groups = {"Regression"})
	  public void afterMethod(ITestResult result){
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "verify_restrict_recording_download":
				System.out.println("In after method");
				callsTabPage.switchToTab(caiCallerDriver, 2);
				adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
				adminCallRecordingTab.refreshCurrentDocument(caiCallerDriver);
				adminCallRecordingTab.openCallRecordingTab(caiCallerDriver);
				adminCallRecordingTab.disableRestrictCallRecordingSetting(caiCallerDriver);
				adminCallRecordingTab.saveCallRecordingTabSettings(caiCallerDriver);
				break;
			default:
				break;
			}
		}
	}
}
