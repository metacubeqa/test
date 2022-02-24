/**
 * 
 */
package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewEvent;
import softphone.source.callTools.SoftPhoneNewTask;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.admin.AccountCallRecordingTab;
import support.source.commonpages.CallRecordingPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CallRecording extends SupportBase{
	
	Dashboard dashboard 							= new Dashboard();
	SoftphoneBase softPhoneBase 					= new SoftphoneBase();
	SoftphoneCallHistoryPage softphoneCallHistory 	= new SoftphoneCallHistoryPage();
	SoftPhoneSettingsPage softphoneSetting			= new SoftPhoneSettingsPage();
	ContactDetailPage contactDetailPage 			= new ContactDetailPage();
	TaskDetailPage sfTaskDetailPage					= new TaskDetailPage();
	SoftPhoneContactsPage softPhoneContactsPage		= new SoftPhoneContactsPage();
	
	SoftPhoneNewEvent newEvent 						= new SoftPhoneNewEvent();
	SoftPhoneNewTask newTask 						= new SoftPhoneNewTask();
	CallToolsPanel callToolsPanel 					= new CallToolsPanel();
	CallScreenPage callScreenPage 					= new CallScreenPage();
	SoftPhoneCalling softPhoneCalling 				= new SoftPhoneCalling();
	CallRecordingPage callRecordingPage				= new CallRecordingPage();
	AccountCallRecordingTab callRecordingTab		= new AccountCallRecordingTab();
	SoftphoneBase softphoneBase						= new SoftphoneBase();
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String regexPatternDate = "([a-zA-Z]+) \\d{2}, \\d{4}, \\d{2}:\\d{2} ([a-zA-Z]+)";
	private static final String dateFormat = "MMM dd, yyyy, hh:mm a";
	

	//Verify "Changelog" tab is removed or not available in Account > Call Recording tab
	@Test(groups = { "Regression", "AdminOnly"})
	public void verify_call_recording_buttons_for_support() {
		System.out.println("Test case --verify_call_recording_buttons_for_support-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening Call Recording Tab
		callRecordingTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		callRecordingTab.openCallRecordingTab(supportDriver);
	
		//assert change log tab visible
		assertFalse(callRecordingTab.isChangeLogTabVisible(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	
	
//	@Test(groups = { "Regression" , "SupportOnly"})
//	public void verify_call_recording_buttons_for_support() {
//		System.out.println("Test case --verify_call_recording_buttons_for_support-- started ");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//		
//		// Opening Intelligent Dialer Tab
//	    callRecordingTab.switchToTab(supportDriver, 2);
//	    dashboard.clickAccountsLink(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//	    callRecordingTab.refreshCurrentDocument(supportDriver);
//		callRecordingTab.verifySupportDisabledSetttings(supportDriver);		
//		
//		// Setting supportDriver used to false as this test case is pass
//		driverUsed.put("supportDriver", false);
//		
//		System.out.println("Test case is pass");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void disable_call_recording(){
//		System.out.println("Test case --disable_call_recording-- started ");
//
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//		initializeSupport("webSupportDriver");
//	    driverUsed.put("webSupportDriver", true);
//	    
//	    callRecordingTab.switchToTab(webSupportDriver, 1);
//	    softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
//	    softphoneSetting.clickSettingIcon(webSupportDriver);
//	    softphoneSetting.disableCallForwardingSettings(webSupportDriver);
//	    
//	    String contactNumber = CONFIG.getProperty("qa_support_user_number");
//	    
//		// disable call recording setting
//	    callRecordingTab.switchToTab(supportDriver, 2);
//	    callRecordingTab.refreshCurrentDocument(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.disableCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		//switch to softphone tab
//	    callRecordingTab.switchToTab(supportDriver, 1);
//	    softPhoneCalling.hangupIfInActiveCall(supportDriver);
//	    softphoneSetting.clickSettingIcon(supportDriver);
//	    softphoneSetting.disableCallForwardingSettings(supportDriver);
//	    softPhoneContactsPage.addContactIfNotExist(supportDriver, contactNumber, CONFIG.getProperty("qa_support_user_name"));
//	    
//	    //Verify that recording option is not appearing for User
//	    callRecordingTab.reloadSoftphone(supportDriver);
//	    softphoneSetting.clickSettingIcon(supportDriver);
//	    softphoneSetting.verifyRecordOptionIsInvisible(supportDriver);
//
//		// Calling from Agent's SoftPhone
//		softPhoneCalling.softphoneAgentCall(supportDriver, contactNumber);
//		softPhoneCalling.isCallHoldButtonVisible(supportDriver);
//		
//		// receiving call from receiver
//		callRecordingTab.switchToTab(webSupportDriver, 1);
//		softPhoneCalling.isAcceptCallButtonVisible(webSupportDriver);
//		softPhoneCalling.pickupIncomingCall(webSupportDriver);
//		callRecordingTab.idleWait(4);
//
//		// verify call recording icon is red
//		callScreenPage.verifyRecordingisInactive(supportDriver);
//
//		// hanging up with caller 1
//		System.out.println("hanging up with caller 1");
//		softPhoneCalling.hangupActiveCall(webSupportDriver);
//
//		// Call is removing from softphone
//		System.out.println("Call is removing from softphone");
//		softPhoneCalling.isCallBackButtonVisible(supportDriver);
//
//		// Verifying Recent Calls Detail
//		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(supportDriver);
//		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
//		contactDetailPage.openRecentCallEntry(supportDriver, callSubject);
//	    sfTaskDetailPage.verifyCallNotAbandoned(supportDriver);
//	    sfTaskDetailPage.verifyCallStatus(supportDriver, "Connected");
//	    sfTaskDetailPage.isCallRecordingURLInvisible(supportDriver);
//	    callRecordingTab.closeTab(supportDriver);
//	    callRecordingTab.switchToTab(supportDriver, 1);
//	    
//	    callRecordingTab.switchToTab(supportDriver, callRecordingTab.getTabCount(supportDriver));
//	    callRecordingTab.switchToTab(webSupportDriver, callRecordingTab.getTabCount(supportDriver));
//	    
//	    //enable call recording
//	    callRecordingTab.enableCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//	    driverUsed.put("supportDriver", false);
//	    driverUsed.put("webSupportDriver", false);
//	    
//	    System.out.println("Test case is pass");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void enable_call_recording(){
//		System.out.println("Test case --enable_call_recording-- started ");
//
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//		initializeSupport("webSupportDriver");
//	    driverUsed.put("webSupportDriver", true);	
//	    
//	    String contactNumber = CONFIG.getProperty("qa_support_user_number");
//	    
//		// disable call recording setting
//	    callRecordingTab.switchToTab(supportDriver, callRecordingTab.getTabCount(supportDriver));
//	    supportDriver.navigate().refresh();
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.disableCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		supportDriver.navigate().refresh();
//		
//		//dismiss warning and enable call recording setting
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.dismissWarningLink(supportDriver);
//		callRecordingTab.enableCallRecordingSetting(supportDriver);
//		callRecordingTab.enableDisplayRecordingStatusSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		//switch to softphone tab
//	    callRecordingTab.switchToTab(supportDriver, 1);
//	    softPhoneContactsPage.addContactIfNotExist(supportDriver, contactNumber, CONFIG.getProperty("qa_support_user_name"));
//	    callRecordingTab.switchToTab(webSupportDriver, 1);
//	    
//	    //Verify that recording option is not appearing for User
//	    callRecordingTab.reloadSoftphone(supportDriver);
//	    softphoneSetting.clickSettingIcon(supportDriver);
//	    softphoneSetting.enableRecordCallsSetting(supportDriver);
//	    
//	    //open salesforce tab and close lightning box
//	    softphoneBase.closeLightningBox(supportDriver);
//
//		// Calling from Agent's SoftPhone
//		softPhoneCalling.softphoneAgentCall(supportDriver, contactNumber);
//
//		// receiving call from receiver
//		softPhoneCalling.pickupIncomingCall(webSupportDriver);
//		callRecordingTab.idleWait(4);
//		
//		// verify call recording icon is red
//		callScreenPage.verifyRecordingisActive(supportDriver);
//
//		// hanging up with caller 1
//		System.out.println("hanging up with caller 1");
//		softPhoneCalling.hangupActiveCall(webSupportDriver);
//
//		// Call is removing from softphone
//		System.out.println("Call is removing from softphone");
//		softPhoneCalling.isCallBackButtonVisible(supportDriver);
//
//		// Verifying Recent Calls Detail
//		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(supportDriver);
//		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
//		contactDetailPage.openRecentCallEntry(supportDriver, callSubject);
//	    sfTaskDetailPage.verifyCallNotAbandoned(supportDriver);
//	    sfTaskDetailPage.verifyCallStatus(supportDriver, "Connected");
//	    sfTaskDetailPage.clickRecordingURLInSupport(supportDriver);
//	    assertEquals(1, callRecordingPage.getNumberOfCallRecordings(supportDriver), "number of call recordings are not same");
//	    callRecordingTab.closeTab(supportDriver);
//	    callRecordingTab.switchToTab(supportDriver, callRecordingTab.getTabCount(supportDriver));
//	    callRecordingTab.closeTab(supportDriver);
//	    callRecordingTab.switchToTab(supportDriver, 1);
//	    
//	    callRecordingTab.switchToTab(supportDriver, callRecordingTab.getTabCount(supportDriver));
//	    callRecordingTab.switchToTab(webSupportDriver, callRecordingTab.getTabCount(supportDriver));
//	    
//	    driverUsed.put("supportDriver", false);
//	    driverUsed.put("webSupportDriver", false);
//	    
//	    System.out.println("Test case is pass");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void disabled_agent_call_recording(){
//		System.out.println("Test case --disabled_agent_call_recording-- started ");
//
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		initializeSupport("agentDriver");
//	    driverUsed.put("agentDriver", true);
//
//	    String contactNumber = CONFIG.getProperty("qa_agent_user_number");
//	    
//	    //enable call recording restrictions
//	    callRecordingTab.switchToTab(supportDriver, 2);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.enableRestrictCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//	    //open salesforce tab and close lightning box
//		callRecordingTab.switchToTab(supportDriver, 1);
//		softPhoneCalling.hangupIfInActiveCall(supportDriver);
//	    softphoneSetting.clickSettingIcon(supportDriver);
//	    softphoneSetting.disableCallForwardingSettings(supportDriver);
//	    
//	    callRecordingTab.switchToTab(agentDriver, 1);
//	    callRecordingTab.switchToTab(agentDriver, 1);
//		softPhoneCalling.hangupIfInActiveCall(agentDriver);
//	    softphoneSetting.clickSettingIcon(agentDriver);
//	    softphoneSetting.disableCallForwardingSettings(agentDriver);
//	    
//		// Calling from Agent's SoftPhone
//	    softPhoneCalling.hangupIfInActiveCall(supportDriver);
//		softPhoneCalling.softphoneAgentCall(supportDriver, contactNumber);
//		softPhoneCalling.isCallHoldButtonVisible(supportDriver);
//		
//		// receiving call from receiver
//		softPhoneCalling.pickupIncomingCall(agentDriver);
//		
//		// hanging up with caller 1
//		System.out.println("hanging up with caller 1");
//		softPhoneCalling.hangupActiveCall(agentDriver);
//
//		// Call is removing from softphone
//		System.out.println("Call is removing from softphone");
//		softPhoneCalling.isCallBackButtonVisible(supportDriver);
//
//		// Verifying Recent Calls Detail
//		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(agentDriver);
//		sfTaskDetailPage.closeLightningDialogueBox(agentDriver);
//		contactDetailPage.openRecentCallEntry(agentDriver, callSubject);
//	    sfTaskDetailPage.verifyCallNotAbandoned(agentDriver);
//	    sfTaskDetailPage.verifyCallStatus(agentDriver, "Connected");
//	    sfTaskDetailPage.clickRecordingURLInSupport(agentDriver);
//	    callRecordingPage.isDownloadRecordingButtonVisible(agentDriver);
//	    assertFalse(callRecordingPage.isDownloadRecordingButtonVisible(agentDriver));
//	    callRecordingTab.closeTab(agentDriver);
//	    callRecordingTab.switchToTab(agentDriver, callRecordingTab.getTabCount(agentDriver));
//	    callRecordingTab.closeTab(agentDriver);
//	    callRecordingTab.switchToTab(agentDriver, 1);
//	    
//	    //disable call recording restrictions
//	    callRecordingTab.switchToTab(supportDriver, 2);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.disableRestrictCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//	    callRecordingTab.switchToTab(supportDriver, callRecordingTab.getTabCount(supportDriver));
//	    callRecordingTab.switchToTab(agentDriver, callRecordingTab.getTabCount(agentDriver));
//	    
//	    driverUsed.put("supportDriver", false);
//	    driverUsed.put("agentDriver", false);
//	    
//	    System.out.println("Test case is pass");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void change_country_granular_setting(){
//		System.out.println("Test case --change_country_granular_setting-- started ");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		String dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
//		
//		String userName = CONFIG.getProperty("qa_admin_user_name").trim();
//		String userEmail = CONFIG.getProperty("qa_admin_user_email").trim();
//		
//	    //enable call recording by country granular setting
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.disableCountryGranularControlSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		callRecordingTab.enableCountryGranularControlSetting(supportDriver);
//		String timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		String changedLog = callRecordingTab.getChangelogFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		String expectedChangelog = userName + " (" + userEmail + ") enabled allow granular control by Country " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		
//		//disable call recording by country granular setting
//		callRecordingTab.disableCountryGranularControlSetting(supportDriver);
//		timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		changedLog = callRecordingTab.getChangelogFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		expectedChangelog = userName + " (" + userEmail + ") disabled allow granular control by Country " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case is pass");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void verify_changelog_when_select_country_from_table(){
//		System.out.println("Test case --verify_changelog_when_select_country_from_table-- started ");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		String dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
//		
//		String userName = CONFIG.getProperty("qa_admin_user_name").trim();
//		String userEmail = CONFIG.getProperty("qa_admin_user_email").trim();
//		
//	    //enable call recording by country granular setting
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.enableCountryGranularControlSetting(supportDriver);
//		callRecordingTab.expandCountyContinent(supportDriver, "NORTH AMERICA");
//		callRecordingTab.uncheckAreasForCallRecording(supportDriver, "Anguilla");
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		callRecordingTab.enableCountryGranularControlSetting(supportDriver);
//		callRecordingTab.expandCountyContinent(supportDriver, "NORTH AMERICA");
//		callRecordingTab.checkAreasForCallRecording(supportDriver, "Anguilla");
//		String timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		String changedLog = callRecordingTab.getChangelogByCountryFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		String expectedChangelog = userName + " (" + userEmail + ") enabled call recording for Anguilla " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		
//		//unchecking call recording by country granular setting
//		callRecordingTab.expandCountyContinent(supportDriver, "NORTH AMERICA");
//		callRecordingTab.uncheckAreasForCallRecording(supportDriver, "Anguilla");
//		timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		changedLog = callRecordingTab.getChangelogByCountryFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		expectedChangelog = userName + " (" + userEmail + ") disabled call recording for Anguilla " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		// disable call recording by country granular setting
//		callRecordingTab.disableCountryGranularControlSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_changelog_when_select_country_from_table-- passed");
//	}
//		
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void change_state_granular_setting(){
//		System.out.println("Test case --change_state_granular_setting-- started ");
//
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		String dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
//		
//		String userName = CONFIG.getProperty("qa_admin_user_name").trim();
//		String userEmail = CONFIG.getProperty("qa_admin_user_email").trim();
//		
//	    //enable call recording by country granular setting
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.disableAllowGranularControlSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		callRecordingTab.enableAllowGranularControlSetting(supportDriver);
//		String timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		String changedLog = callRecordingTab.getChangelogFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		String expectedChangelog = userName + " (" + userEmail + ") enabled allow granular control by US State " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		
//		//disable call recording by country granular setting
//		callRecordingTab.disableAllowGranularControlSetting(supportDriver);
//		timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		changedLog = callRecordingTab.getChangelogFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		expectedChangelog = userName + " (" + userEmail + ") disabled allow granular control by US State " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --change_state_granular_setting-- passed ");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void verify_changelog_when_select_US_state(){
//		System.out.println("Test case --verify_changelog_when_select_US_state-- started ");
//
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		String dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
//		
//		String userName = CONFIG.getProperty("qa_admin_user_name").trim();
//		String userEmail = CONFIG.getProperty("qa_admin_user_email").trim();
//		
//	    //enable call recording by selecting US state
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.enableAllowGranularControlSetting(supportDriver);
//		callRecordingTab.uncheckAreasForCallRecording(supportDriver, "Alaska");
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		callRecordingTab.checkAreasForCallRecording(supportDriver, "Alaska");
//		String timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		String changedLog = callRecordingTab.getChangelogByStateFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		String expectedChangelog = userName + " (" + userEmail + ") enabled call recording for Alaska " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		dateTime = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
//		
//		//enable call recording by unselecting US state
//		callRecordingTab.enableAllowGranularControlSetting(supportDriver);
//		callRecordingTab.uncheckAreasForCallRecording(supportDriver, "Alaska");
//		timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		changedLog = callRecordingTab.getChangelogByStateFirstLine(supportDriver);
//		timeString = findDiffStartEndTime(timeString, changedLog, userTimeZone);
//		
//		//verify that change log is correct
//		expectedChangelog = userName + " (" + userEmail + ") disabled call recording for Alaska " + timeString;
//		assertEquals(changedLog, expectedChangelog);
//		
//		// disable call recording by country granular setting
//		callRecordingTab.disableAllowGranularControlSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_changelog_when_select_US_state-- passed ");
//	}
//	
//	@Test(groups = { "Regression" , "AdminOnly"})
//	public void verify_restrict_recording_download_off(){
//		System.out.println("Test case --verify_restrict_recording_download_off-- started ");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//		String callRecordingUrl = "https://app-qa.ringdna.net/#call-player/rc864269";
//		
//	    //disable call recording
//		dashboard.clickAccountsLink(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.disableRestrictCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//		
//		initializeSupport("webSupportDriver");
//		driverUsed.put("webSupportDriver", true);
//		
//		//verifying on support driver with recording url
//		dashboard.switchToTab(webSupportDriver, 2);
//		webSupportDriver.get(callRecordingUrl);
//		dashboard.waitForPageLoaded(webSupportDriver);
//		HelperFunctions.deletingExistingFiles(downloadPath, ".wav");
//		assertFalse(callRecordingPage.isExtensionFileDownloaded(downloadPath, ".wav"));
//		callRecordingPage.downloadRecording(webSupportDriver, 0);
//		callRecordingPage.idleWait(3);
//		assertTrue(callRecordingPage.isExtensionFileDownloaded(downloadPath, ".wav"));
//		
//		// enable call recording
//		dashboard.clickAccountsLink(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.enableRestrictCallRecordingSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		//verifying on support driver with recording url
//		dashboard.switchToTab(webSupportDriver, 2);
//		dashboard.refreshCurrentDocument(webSupportDriver);
//		dashboard.waitForPageLoaded(webSupportDriver);
//		assertFalse(callRecordingPage.isDownloadRecordingButtonVisible(webSupportDriver));
//		
//		driverUsed.put("supportDriver", false);
//		driverUsed.put("webSupportDriver", false);
//		System.out.println("Test case --verify_restrict_recording_download_off-- passed");
//	}
//	
//	@Test(groups = { "AdminOnly", "MediumPriority"})
//	public void verify_special_consent_warning(){
//		System.out.println("Test case --verify_special_consent_warning-- started");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//
//	    //enable call recording by country granular setting
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickAccountsLink(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.enableAllowGranularControlSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		String[] stateArray = { "California", "Connecticut", "Delaware", "Florida", "Illinois", "Maryland",
//				"Massachusetts", "Montana", "Nevada", "New Hampshire", "Pennsylvania", "Vermont", "Washington" };
//		for (String state : stateArray) {
//			callRecordingTab.unCheckRecordCallsConsentState(supportDriver, state);
//			callRecordingTab.checkRecordCallsConsentState(supportDriver, state);
//			callRecordingTab.verifyConsentWarningMsg(supportDriver, state);
//			callRecordingTab.clickConfirmConsentBtn(supportDriver);
//		}
//		
//		// updating the supportDriver used
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_special_consent_warning-- passed");
//	}
//	
//	@Test(groups = { "AdminOnly", "MediumPriority"})
//	public void verify_dispaly_recording_status_intelligent_dialer(){
//		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- started");
//		
//		// updating the supportDriver used
//		initializeSupport();
//		driverUsed.put("supportDriver", true);
//		dashboard.switchToTab(supportDriver, 1);
//		softPhoneCalling.hangupIfInActiveCall(supportDriver);
//		
//		initializeSupport("webSupportDriver");
//		driverUsed.put("webSupportDriver", true);
//		dashboard.switchToTab(webSupportDriver, 1);
//		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
//
//	    //disabling call recording status setting
//		dashboard.switchToTab(supportDriver, 2);
//		dashboard.clickAccountsLink(supportDriver);
//		callRecordingTab.openCallRecordingTab(supportDriver);
//		callRecordingTab.unlockRecordingSetting(supportDriver);
//		callRecordingTab.disableDisplayRecordingStatusSetting(supportDriver);
//		callRecordingTab.disableCallRecordingPauseSetting(supportDriver);
//		callRecordingTab.saveCallRecordingTabSettings(supportDriver);
//
//		dashboard.switchToTab(supportDriver, 1);
//		softPhoneCalling.hangupIfInActiveCall(supportDriver);
//		dashboard.reloadSoftphone(supportDriver);
//		softPhoneCalling.softphoneAgentCall(supportDriver, CONFIG.getProperty("qa_support_user_number"));
//		softPhoneCalling.isCallHoldButtonVisible(supportDriver);
//		
//		//picking up call on web support driver
//		softPhoneCalling.switchToTab(webSupportDriver, 1);
//		softPhoneCalling.pickupIncomingCall(webSupportDriver);
//		
//		callScreenPage.switchToTab(supportDriver, 1);
//		assertTrue(callScreenPage.getInActiveRecordingColorSrc(supportDriver).contains("-green"));
//		assertFalse(callScreenPage.getInActiveRecordingColorSrc(supportDriver).contains("-red"));
//		softPhoneCalling.hangupIfInActiveCall(supportDriver);
//		
//		// updating the supportDriver used
//		driverUsed.put("supportDriver", false);
//		System.out.println("Test case --verify_dispaly_recording_status_intelligent_dialer-- passed");
//	}
//
//	/* This method is used to find difference in start end time at Class level only
//	 * return String;
//	 */
//	public String findDiffStartEndTime(String timeString, String changeLog, String userTimeZone) {
//		// finding start date
//		Date startDate = HelperFunctions.getDateTimeInDateFormat(timeString, dateFormat);
//
//		// finding end date
//		Date endDate = HelperFunctions.fetchDateTimeFromStringUsingRegex(changeLog, dateFormat, regexPatternDate);
//
//		// finding diff in minutes and updating new time
//		int diffInMinutes = HelperFunctions.getDateTimeDiffInMinutes(startDate, endDate, dateFormat);
//		if (diffInMinutes >0 && diffInMinutes <=2) {
//			System.out.println("updating difference in minutes="+diffInMinutes);
//			DateFormat df = new SimpleDateFormat(dateFormat);
//			String dateTime = df.format(HelperFunctions.addMinutesToDate(startDate, diffInMinutes)).toString();
//			timeString = HelperFunctions.getDateTimeInTimeZone(dateTime, userTimeZone, dateFormat);
//		}
//		return timeString;
//	}
}
