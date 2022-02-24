package support.cases.callCases;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.calls.CallInspector;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallCases extends SupportBase {

	CallInspector callsPage = new CallInspector();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	CallScreenPage callScreenPage = new CallScreenPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	Dashboard dashboard  = new Dashboard();
	Random  random = new Random();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();

	private String contactFirstName;
	private String contactAccountName;
	
	@BeforeClass(groups = {"Regression", "MediumPriority", "Product Sanity"})
	public void beforeClass() {
		contactFirstName = CONFIG.getProperty("contact_first_name");
		contactAccountName = CONFIG.getProperty("contact_account_name");	
	}
	
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_call_functionality() {
		System.out.println("Test case --verify_call_functionality-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		callsPage.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		//hanging up call of admin driver
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		callsPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//creating recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		String toNumber = CONFIG.getProperty("qa_admin_user_number");
		String fromNumber = CONFIG.getProperty("qa_support_user_number");
		
		softPhoneCalling.softphoneAgentCall(supportDriver, toNumber);
		softPhoneCalling.isCallHoldButtonVisible(supportDriver);

		softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		callToolsPanel.enterCallNotes(supportDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(supportDriver, randomIndex);
		callToolsPanel.selectDisposition(supportDriver, 2);
		//callToolsPanel.selectDispositionUsingText(supportDriver, CONFIG.getProperty("qa_cai_disposition"));
		callsPage.idleWait(2);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			callToolsPanel.callNotesSectionVisible(supportDriver);
			callScreenPage.addCallerAsContact(supportDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}
		
		//Fetching details from Call screen page in Softphone
		Map<String, String> contactDetails = new HashMap<String, String>();
		contactDetails = callScreenPage.getCallerDetails(supportDriver);
		contactDetails.put("Rating", callScreenPage.getCallerRating(supportDriver));
		contactDetails.put("Owner", CONFIG.getProperty("qa_support_user_email"));
		contactDetails.put("ToNumber", toNumber);
		contactDetails.put("FromNumber", fromNumber);
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(supportDriver);
		softPhoneActivityPage.openTaskInSalesforce(supportDriver, callSubject);
		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
		
		//Fetching details from sfdc task page
		String callKeyId = sfTaskDetailPage.getCallObjectId(supportDriver);
		contactDetails.put("CallKeyId", callKeyId);
		contactDetails.put("Direction", sfTaskDetailPage.getCallDirection(supportDriver));
		contactDetails.put("Status", sfTaskDetailPage.getTaskStatus(supportDriver));
		contactDetails.put("Duration", sfTaskDetailPage.getCallDuration(supportDriver));
		
		callsPage.closeTab(supportDriver);
		callsPage.switchToTab(supportDriver, 2);
		
		//Opening Calls Page
		dashboard.openCallInspectorPage(supportDriver);
		callsPage.getCallData(supportDriver, callKeyId);

		//verifying details on call inspector page
		assertEquals(callsPage.getCallKey(supportDriver), contactDetails.get("CallKeyId"));
		assertEquals(callsPage.getAccountName(supportDriver), contactDetails.get("Company"));
		assertEquals(callsPage.getRating(supportDriver), contactDetails.get("Rating"));
		assertEquals(callsPage.getOwner(supportDriver), contactDetails.get("Owner"));
		assertEquals(callsPage.getToNumber(supportDriver), contactDetails.get("ToNumber"));
		assertEquals(callsPage.getFromNumber(supportDriver), contactDetails.get("FromNumber"));
		assertEquals(callsPage.getDirection(supportDriver), contactDetails.get("Direction"));
		assertEquals(callsPage.getStatus(supportDriver), contactDetails.get("Status"));
		
		//verifying salesforce details
		callsPage.verifySalesForceDetailsVisible(supportDriver);
		
		//opening papertrail and verify
		callsPage.clickPaperTrailBtn(supportDriver);
		callsPage.switchToTab(supportDriver, callsPage.getTabCount(supportDriver));
		
		// verifying page opened is papertrail
		assertTrue(supportDriver.getCurrentUrl().contains("papertrailapp.com"));
		callsPage.closeTab(supportDriver);
		callsPage.switchToTab(supportDriver, 2);
		
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_functionality-- passed ");
	}
	
	@Test(groups = {  "SupportOnly", "MediumPriority" })
	public void verify_call_recording_section_details() {
		System.out.println("Test case --verify_call_recording_section_details-- started ");
		
		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		callsPage.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		//hanging up call of admin driver
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		callsPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		//creating recording
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));
		String toNumber = CONFIG.getProperty("qa_admin_user_number");
		
		softPhoneCalling.softphoneAgentCall(supportDriver, toNumber);
		softPhoneCalling.isCallHoldButtonVisible(supportDriver);
		callsPage.switchToTab(adminDriver, 1);
		softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		callsPage.switchToTab(supportDriver, 1);
		callToolsPanel.enterCallNotes(supportDriver, callSubject, callNotes);
		int randomIndex = random.nextInt((5-1)+1)+1;
		callToolsPanel.giveCallRatings(supportDriver, randomIndex);
		callToolsPanel.selectDisposition(supportDriver, 2);
		callsPage.idleWait(2);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			callToolsPanel.callNotesSectionVisible(supportDriver);
			callScreenPage.addCallerAsContact(supportDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}
		
		softPhoneActivityPage.openTaskInSalesforce(supportDriver, callSubject);
		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
		
		//Fetching details from sfdc task page
		String callKeyId = sfTaskDetailPage.getCallObjectId(supportDriver);
		callsPage.closeTab(supportDriver);
		callsPage.switchToTab(supportDriver, 2);
		
		//Opening Calls Page
		dashboard.openCallInspectorPage(supportDriver);
		callsPage.getCallData(supportDriver, callKeyId);

		//verifying salesforce details
		callsPage.verifyCallRecordingSection(supportDriver);
		
		//verifying call details section
		callsPage.verifyCallDetailsSection(supportDriver);
				
		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_recording_section_details-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_joined_call_recording_section_url_accessible() {
		System.out.println("Test case --verify_joined_call_recording_section_url_accessible-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening Calls Page
		String callKey = "rc1256349";
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openCallInspectorPage(supportDriver);
		callsPage.getCallData(supportDriver, callKey);

		callsPage.clickAndVerifyJoinedRecordingUrl(supportDriver);
		callsPage.switchToTab(supportDriver, 2);
		
		callsPage.verifyConversationAISection(supportDriver, callKey);
		callsPage.switchToTab(supportDriver, 2);

		// updating the driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_joined_call_recording_section_url_accessible-- passed ");
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Product Sanity" }, alwaysRun = true)
	public void afterClass() {
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		driverUsed.put("supportDriver", false);
	}
}
