package softphone.cases.callFlow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import base.SeleniumBase;
import softphone.base.SoftphoneBase;
import softphone.cases.softPhoneFunctional.MultiMatchRequired;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.callFlows.CallFlowPage.CallFlowStatus;
import support.source.callFlows.CallFlowPage.VMAudioType;
import utility.HelperFunctions;

public class CallFlowMediumPriority extends SoftphoneBase{
	
	static final String mp3File1 = "TestSampleMp3.mp3";
	static final String mp3File2 = "TestSample2Mp3.mp3";
	static final String mp3FilePath   = System.getProperty("user.dir").concat("\\src\\test\\resources\\audioFiles\\").concat(mp3File1);
	static final String mp3File2Path  = System.getProperty("user.dir").concat("\\src\\test\\resources\\audioFiles\\").concat(mp3File2);
	
	MultiMatchRequired multiMatchRequired = new MultiMatchRequired();
	
	@BeforeMethod(groups = { "MediumPriority", "Product Sanity" })
	public void beforeMethod() {
		SeleniumBase.timeOutInSecs = 30;
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		ArrayList<String> tabs = new ArrayList<String>(driver4.getWindowHandles());
		if (tabs.size() < 2) {
			loginSupport(driver4);
		}
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_details_after_adding_new_call_flow() {
		
		System.out.println("Test case --verify_details_after_adding_new_call_flow-- started ");
		driverUsed.put("driver4", true);
		
		//verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);
		
		callFlowPage.clickAddSmartNoIcon(driver4);
		assertEquals(callFlowPage.getSaveCallFLowMessage(driver4), "Please save call flow before assign smart numbers.");
		callFlowPage.isSaveCallFlowMsgDisappeared(driver4);
		
		// Creating call flow
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));
		
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		String createdDate1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a", true);
		
		if (createdDate.contains("PM")) {
			createdDate = createdDate.replace("PM", "pm");
		} else {
			createdDate = createdDate.replace("AM", "am");
		}
		
		if (createdDate1.contains("PM")) {
			createdDate1 = createdDate1.replace("PM", "pm");
		} else {
			createdDate1 = createdDate1.replace("AM", "am");
		}
		
		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		
		// verifying new call flow details
		assertEquals(callFlowPage.getTextCallFlowName(driver4), callFlowName, "call flow name not matching");
		assertEquals(callFlowPage.getTextCallFlowDesc(driver4), callFlowDesc, "description not matching");
		assertTrue(callFlowPage.isDeleteBtnDisabled(driver4), "Delete button is not disabled");
		assertTrue(callFlowPage.getCurrentStatus(driver4).equals("Active"));
		callFlowPage.verifyCallFlowPageDetails(driver4, callFlowName, CONFIG.getProperty("qa_user_account"));

		//verifying created details
		assertEquals(callFlowPage.getCreatedByUser(driver4), CONFIG.getProperty("qa_user_2_name"));
		assertTrue(callFlowPage.getCreatedOnUser(driver4).equals(createdDate)
				|| callFlowPage.getCreatedOnUser(driver4).equals(createdDate1));

		// selecting paused status and saving call flow
		callFlowPage.selectStatus(driver4, CallFlowStatus.Paused.name());
		callFlowPage.clickSaveBtn(driver4);
		assertFalse(callFlowPage.isDeleteBtnDisabled(driver4), "Delete button is disabled");

		String updatedDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		String updatedDate1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a", true);

		if(updatedDate.contains("PM")) {
			updatedDate = updatedDate.replace("PM", "pm");
		}
		else {
			updatedDate = updatedDate.replace("AM", "am");
		}
		
		if(updatedDate1.contains("PM")) {
			updatedDate1 = updatedDate1.replace("PM", "pm");
		}
		else {
			updatedDate1 = updatedDate1.replace("AM", "am");
		}
		
		//verifying updated details
		callFlowPage.refreshCurrentDocument(driver4);
		assertEquals(callFlowPage.getUpdatedByUser(driver4), CONFIG.getProperty("qa_user_2_name"));
		assertTrue(callFlowPage.getUpdatedOnUser(driver4).equals(updatedDate)
				|| callFlowPage.getUpdatedOnUser(driver4).equals(updatedDate1));
		
		// deleting call flow
		callFlowPage.clickDeleteCallFlowBtn(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_details_after_adding_new_call_flow-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_deleted_user_not_listed_in_dial_step() {
		
		System.out.println("Test case --verify_deleted_user_not_listed_in_dial_step-- started ");
		driverUsed.put("driver4", true);
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount	= CONFIG.getProperty("qa_user_account");
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		
		callFlowPage.dragAndDropDialImage(driver4);
		
		String deleted_user = CONFIG.getProperty("qa_deleted_user");
		String user_name 	= CONFIG.getProperty("qa_user_2_name");
		
		// selecting group for dial
		boolean userExists = callFlowPage.selectGroupFromDialSection(driver4,
				CallFlowPage.DialCallRDNATOCat.User, deleted_user);
		assertFalse(userExists, String.format("Deleted User:%s is present in dial tab", deleted_user));
		
		// searching valid user
		userExists = callFlowPage.selectGroupFromDialSection(driver4, CallFlowPage.DialCallRDNATOCat.User,
				user_name);
		assertTrue(userExists, String.format("User:%s is not present in dial tab", user_name));

		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_deleted_user_not_listed_in_dial_step-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_widgets_when_drag_and_dropped() {
		System.out.println("Test case --verify_widgets_when_drag_and_dropped-- started ");
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount = CONFIG.getProperty("qa_user_account");

		//enabling dynamic call flow settings
		loginSupport(webSupportDriver);
		dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.scrollToGeneralSettings(webSupportDriver);
		accountIntelligentDialerTab.enableDynamicCallFlows(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
		
		//opening call flow
		dashboard.navigateToManageCallFlow(webSupportDriver);
		callFlowPage.searchCallFlow(webSupportDriver, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(webSupportDriver, callFlowName);

		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropDialImage(webSupportDriver);
		callFlowPage.verifyWidgetTitle(webSupportDriver, "Dial");

		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropGreeting(webSupportDriver);
		callFlowPage.verifyWidgetTitle(webSupportDriver, "Greeting");
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropMenuImage(webSupportDriver);		
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropPromptImage(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropVoicemailImage(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropSMSImage(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropTimeImage(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropBranchImage(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropConference(webSupportDriver);

		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropCallout(webSupportDriver);
		
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropLoopImage(webSupportDriver);
		callFlowPage.adddLoopStep(webSupportDriver, "", "");
		
		//disabling dynamic call flow settings
		dashboard.clickAccountsLink(webSupportDriver);
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		accountIntelligentDialerTab.scrollToGeneralSettings(webSupportDriver);
		accountIntelligentDialerTab.disableDynamicCallFlows(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
		accountOverviewtab.closeTab(webSupportDriver);
		accountOverviewtab.switchToTab(webSupportDriver, 1);
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_widgets_when_drag_and_dropped-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_widgets_greeting_functionality() {
		System.out.println("Test case --verify_widgets_greeting_functionality-- started ");
		driverUsed.put("driver4", true);
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		loginSupport(webSupportDriver);
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount = CONFIG.getProperty("qa_user_account");

		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);

		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.dragAndDropGreeting(driver4);

		//selecting audio type and calling and recording
		callFlowPage.selectVMAudioType(driver4, VMAudioType.RecordMessage);
		callFlowPage.enterPhoneNumberToRecord(driver4, CONFIG.getProperty("qa_support_user_number"));
		callFlowPage.clickCallAndRecordBtn(driver4);
		
		//accepting call
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.clickAcceptCallButton(webSupportDriver);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(webSupportDriver));
		
		//clicking call a different number and verifying call is disconnected
		callFlowPage.clickCallADifferentNumberLoc(driver4);
		callFlowPage.idleWait(1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(webSupportDriver));
		
		//clicking on retry link
		callFlowPage.retryCallAndRecord(driver4);
		callFlowPage.idleWait(1);

		//verifying phone number box and call and record btn visible
		assertFalse(callFlowPage.isAudioPlayerControlBarVisible(driver4));
		callFlowPage.enterPhoneNumberToRecord(driver4, CONFIG.getProperty("qa_support_user_number"));
		assertTrue(callFlowPage.isCallAndRecordBtnVisible(driver4));
		
		callFlowPage.closeTab(webSupportDriver);
		callFlowPage.switchToTab(webSupportDriver, 1);
		
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);
		
		System.out.println("Test case --verify_widgets_greeting_functionality-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void upload_mp3_file_greeting_widget() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_mp3_file_greeting_widget-- started ");
		driverUsed.put("driver4", true);
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount = CONFIG.getProperty("qa_user_account");

		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);

		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.dragAndDropGreeting(driver4);
		
		// uploading mp3 file and playing
		callFlowPage.selectVMAudioType(driver4, VMAudioType.UploadMp3);
		callFlowPage.uploadMp3File(driver4, mp3FilePath);
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_mp3_file_greeting_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void upload_and_change_mp3_file_greeting_widget() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_and_change_mp3_file_greeting_widget-- started ");
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount = CONFIG.getProperty("qa_user_account");
		driverUsed.put("driver4", true);
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);

		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.dragAndDropGreeting(driver4);
		
		// uploading mp3 file and playing
		callFlowPage.selectVMAudioType(driver4, VMAudioType.UploadMp3);
		String filePath = callFlowPage.uploadMp3File(driver4, mp3FilePath);
		assertTrue(filePath.contains(mp3File1));
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		//uploading mp3 by changing link
		callFlowPage.changeFileLink(driver4);
		filePath = callFlowPage.uploadMp3File(driver4, mp3File2Path);
		assertTrue(filePath.contains(mp3File2));
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_and_change_mp3_file_greeting_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void upload_mp3_file_conference_widget() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_mp3_file_conference_widget-- started ");
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount = CONFIG.getProperty("qa_user_account");
		driverUsed.put("driver4", true);
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);

		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.dragAndDropConference(driver4);
		
		// uploading mp3 file and playing
		callFlowPage.selectVMAudioType(driver4, VMAudioType.UploadMp3);
		callFlowPage.uploadMp3File(driver4, mp3FilePath);
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_mp3_file_conference_widget-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void upload_mp3_file_voicemail_widget() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_mp3_file_voicemail_widget-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropVoicemailImage(driver4);
		
		// uploading mp3 file and playing
		callFlowPage.selectVMAudioType(driver4, VMAudioType.UploadMp3);
		callFlowPage.uploadMp3File(driver4, mp3FilePath);
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		//deleting call flow
		callFlowPage.deleteCallFlow(driver4);

		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_mp3_file_voicemail_widget-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_record_this_calls_option_in_dial_widget() {
		System.out.println("Test case --verify_record_this_calls_option_in_dial_widget-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow with admin user
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropDialImage(driver4);
		
		//verifying record calls not disabled for admin user
		assertFalse(callFlowPage.isRecordCallsDisabled(driver4));
	
		//deleting call flow
		callFlowPage.deleteCallFlow(driver4);

		// Creating call flow for support user
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		loginSupport(webSupportDriver);
		
		callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		dashboard.navigateToAddNewCallFlow(webSupportDriver);
		callFlowPage.createCallFlow(webSupportDriver, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropDialImage(webSupportDriver);

		//verifying record calls disabled for support user
		assertTrue(callFlowPage.isRecordCallsDisabled(webSupportDriver));

		//deleting call flow
		callFlowPage.deleteCallFlow(webSupportDriver);

		callFlowPage.closeTab(webSupportDriver);
		callFlowPage.switchToTab(webSupportDriver, 1);
		
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_record_this_calls_option_in_dial_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void upload_mp3_save_to_library_greeting_widget() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_mp3_save_to_library_greeting_widget-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow for admin user
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropGreeting(driver4);
		
		// uploading mp3 file and playing
		callFlowPage.selectVMAudioType(driver4, VMAudioType.UploadMp3);
		String filePath = callFlowPage.uploadMp3File(driver4, mp3FilePath);
		assertTrue(filePath.contains(mp3File1));
		callFlowPage.playAudioPlayerBar(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		callFlowPage.clickSaveToLibFutureUse(driver4);
		callFlowPage.saveCallFlowSettings(driver4);
		
		//chcking mp3 saved in library
		callFlowPage.selectVMAudioType(driver4, VMAudioType.FromLibrary);
		callFlowPage.selectFromLibrary(driver4, filePath);
		
		//deleting call flow
		callFlowPage.deleteCallFlow(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_mp3_save_to_library_greeting_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void upload_mp3_for_hold_music_queue_dial() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		System.out.println("Test case --upload_mp3_for_hold_music_queue_dial-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropDialImage(driver4);

		callFlowPage.selectGroupFromDialSection(driver4, CallFlowPage.DialCallRDNATOCat.CallQueue, CONFIG.getProperty("new_qa_automation_group"));
		callFlowPage.selectHoldMusicFile(driver4, mp3FilePath);
		callFlowPage.saveCallFlowSettings(driver4);
	
		//deleting call flow
		callFlowPage.deleteCallFlow(driver4);

		driverUsed.put("driver4", false);
		System.out.println("Test case --upload_mp3_for_hold_music_queue_dial-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void save_and_verify_recorded_audio_in_library_greeting_widget() {
		System.out.println("Test case --save_and_verify_recorded_audio_in_library_greeting_widget-- started ");
		driverUsed.put("driver4", true);
		
		// Creating call flow for support user
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		loginSupport(webSupportDriver);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow for admin user
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		callFlowPage.dragAndDropGreeting(driver4);

		//selecting audio type and calling and recording
		callFlowPage.selectVMAudioType(driver4, VMAudioType.RecordMessage);
		callFlowPage.enterPhoneNumberToRecord(driver4, CONFIG.getProperty("qa_support_user_number"));
		callFlowPage.clickCallAndRecordBtn(driver4);
		
		//accepting call
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.pickupIncomingCall(webSupportDriver);
		callFlowPage.idleWait(2);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		
		//entering library name
		String libraryName = "AutoLib".concat(HelperFunctions.GetRandomString(3));
		callFlowPage.idleWait(1);
		callFlowPage.enterLibraryTitle(driver4, libraryName);
		callFlowPage.clickSaveToLibFutureUse(driver4);
		callFlowPage.saveCallFlowSettings(driver4);

		// chcking mp3 saved in library
		callFlowPage.selectVMAudioType(driver4, VMAudioType.FromLibrary);
		callFlowPage.selectFromLibrary(driver4, libraryName);

		//deleting call flow
		callFlowPage.deleteCallFlow(driver4);
		
		callFlowPage.closeTab(webSupportDriver);
		callFlowPage.switchToTab(webSupportDriver, 1);
		
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);
		
		System.out.println("Test case --save_and_verify_recorded_audio_in_library_greeting_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void auto_save_call_flow_details_verify() {
		System.out.println("Test case --auto_save_call_flow_details_verify-- started ");
		driverUsed.put("driver4", true);
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);
		callFlowPage.verifyNewCallFlowDetails(driver4, CONFIG.getProperty("qa_user_2_name"));
		assertFalse(HelperFunctions.verifyStringContainsNumbers(driver4.getCurrentUrl()));

		// drag and drop dial icon
		callFlowPage.dragAndDropDialImage(driver4);
		
		String oldCurrentTime = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		String oldCurrentTime1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a", true);
		
		if (oldCurrentTime.contains("PM")) {
			oldCurrentTime = oldCurrentTime.replace("PM", "pm");
		} else {
			oldCurrentTime = oldCurrentTime.replace("AM", "am");
		}
		
		if (oldCurrentTime1.contains("PM")) {
			oldCurrentTime1 = oldCurrentTime1.replace("PM", "pm");
		} else {
			oldCurrentTime1 = oldCurrentTime1.replace("AM", "am");
		}
		
		//verifying auto save details
		assertEquals(callFlowPage.getSaveCallFLowMessage(driver4), "Call Flow saved.");
		callFlowPage.isSaveCallFlowMsgDisappeared(driver4);
		assertTrue(callFlowPage.getUpdatedOnUser(driver4).equals(oldCurrentTime)
				|| callFlowPage.getUpdatedOnUser(driver4).equals(oldCurrentTime1));
		assertTrue(callFlowPage.getCreatedOnUser(driver4).equals(oldCurrentTime)
				|| callFlowPage.getCreatedOnUser(driver4).equals(oldCurrentTime1));
		assertEquals(callFlowPage.getCreatedByUser(driver4), CONFIG.getProperty("qa_user_2_name"));
		assertEquals(callFlowPage.getUpdatedByUser(driver4), CONFIG.getProperty("qa_user_2_name"));
		assertTrue(HelperFunctions.verifyStringContainsNumbers(driver4.getCurrentUrl()));

		// Assigning name and details
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));
		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);

		String createdOnTime = callFlowPage.getCreatedOnUser(driver4);
		String updatedOnTime = callFlowPage.getUpdatedOnUser(driver4);
		////////////////////////////////////////////////////////////////////////////////
		
		//now searching on other user
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		loginSupport(webSupportDriver);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.navigateToManageCallFlow(webSupportDriver);
		callFlowPage.searchCallFlow(webSupportDriver, callFlowName, "");
		callFlowPage.clickSelectedCallFlow(webSupportDriver, callFlowName);
		
		//verifying details
		assertEquals(callFlowPage.getCreatedByUser(webSupportDriver), CONFIG.getProperty("qa_user_2_name"));
		assertEquals(callFlowPage.getUpdatedByUser(webSupportDriver), CONFIG.getProperty("qa_user_2_name"));
		assertTrue(callFlowPage.getCreatedOnUser(webSupportDriver).equals(createdOnTime));
		assertTrue(callFlowPage.getUpdatedOnUser(webSupportDriver).equals(updatedOnTime));

		//updating details
		callFlowPage.idleWait(59);
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		String newCurrentTime = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		String newCurrentTime1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a", true);
		
		if (newCurrentTime.contains("PM")) {
			newCurrentTime = newCurrentTime.replace("PM", "pm");
		} else {
			newCurrentTime = newCurrentTime.replace("AM", "am");
		}
		
		if (newCurrentTime1.contains("PM")) {
			newCurrentTime1 = newCurrentTime1.replace("PM", "pm");
		} else {
			newCurrentTime1 = newCurrentTime1.replace("AM", "am");
		}
		
		assertEquals(callFlowPage.getCreatedByUser(webSupportDriver), CONFIG.getProperty("qa_user_2_name"));
		assertEquals(callFlowPage.getUpdatedByUser(webSupportDriver), CONFIG.getProperty("qa_support_user_name"));
		assertTrue(callFlowPage.getCreatedOnUser(webSupportDriver).equals(oldCurrentTime)
				|| callFlowPage.getCreatedOnUser(webSupportDriver).equals(oldCurrentTime1));
		assertTrue(callFlowPage.getUpdatedOnUser(webSupportDriver).equals(newCurrentTime)
				|| callFlowPage.getUpdatedOnUser(webSupportDriver).equals(newCurrentTime1));
		assertFalse(callFlowPage.getUpdatedOnUser(webSupportDriver).equals(oldCurrentTime)
				|| callFlowPage.getUpdatedOnUser(webSupportDriver).equals(oldCurrentTime1));
		
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --auto_save_call_flow_details_verify-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_under_details_abandoned_section_saved_after_drag_drop() {
		System.out.println("Test case --verify_call_flow_under_details_abandoned_section_saved_after_drag_drop-- started ");
		driverUsed.put("driver4", true);
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Assigning name and details
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));
		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);

		//edit details
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		
		//verifying auto save details
		callFlowPage.dragAndDropDialImage(driver4);
		
		String currentTime = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a");
		String currentTime1 = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy hh:mm a", true);
		
		if (currentTime.contains("PM")) {
			currentTime = currentTime.replace("PM", "pm");
		} else {
			currentTime = currentTime.replace("AM", "am");
		}
		
		if (currentTime1.contains("PM")) {
			currentTime1 = currentTime1.replace("PM", "pm");
		} else {
			currentTime1 = currentTime1.replace("AM", "am");
		}
		
		assertTrue(callFlowPage.getUpdatedOnUser(driver4).equals(currentTime)
				|| callFlowPage.getUpdatedOnUser(driver4).equals(currentTime1));

		//updating call flow details
		String callFlowName2 = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc2 = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));
		
		String abandonedSubject = "AbandonedSubject".concat(HelperFunctions.GetRandomString(3));
		String abandonedComments = "AbandonedComments".concat(HelperFunctions.GetRandomString(3));
		
		callFlowPage.enterAbandonedCalls_Subjects(driver4, abandonedSubject, abandonedComments);
		callFlowPage.selectAbandonedUser(driver4, CONFIG.getProperty("qa_user_2_name"));
		callFlowPage.createCallFlow(driver4, callFlowName2, callFlowDesc2);
		
		assertEquals(callFlowPage.getTextCallFlowName(driver4), callFlowName2);
		assertEquals(callFlowPage.getTextCallFlowDesc(driver4), callFlowDesc2);
		assertEquals(callFlowPage.getAbandonedUser(driver4), CONFIG.getProperty("qa_user_2_name"));
		assertEquals(callFlowPage.getAbandonedSubject(driver4), abandonedSubject);
		assertEquals(callFlowPage.getAbandonedComments(driver4), abandonedComments);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_call_flow_under_details_abandoned_section_saved_after_drag_drop-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_with_description_more_than_255_char() {
		System.out.println("Test case --verify_call_flow_with_description_more_than_255_char-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);

		// Creating call flow for admin user
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDescMoreThan255Char = "AutoCallDesc".concat("dfxgjuqtmnscmggucakujhvcllbechzyvcwoifnmkeidqseygbvwmgffmknvqvilhavmhqjxpgkkorjyrkvcscavtzdfehaywvatdgjbibvcfnqwqfnoxcfqmmrkcjyfxrdnwndyzomvtuuouehnombymvdbkemzuvnazovgagqhxrgezjtwgwagfzhiviymeusdlyxqqacjpwimmffkvrqojboncrgzkonqanccohrhyxcfikgzytujojwdrqp");

		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDescMoreThan255Char);

		// verifying new call flow details
		assertEquals(callFlowPage.getTextCallFlowName(driver4), callFlowName, "call flow name not matching");
		assertNotEquals(callFlowPage.getTextCallFlowDesc(driver4), callFlowDescMoreThan255Char, "Description saved more than 255");
		assertEquals(callFlowPage.getTextCallFlowDesc(driver4).length(), 255, "Description length not 255");
		
		// deleting call flow
		callFlowPage.deleteCallFlow(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_call_flow_with_description_more_than_255_char-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_queue_timeout_after_selecting_acd_call_queue() {
		System.out.println("Test case --verify_queue_timeout_after_selecting_acd_call_queue-- started ");
		driverUsed.put("driver4", true);
		
		//creating call queue
		dashboard.switchToTab(driver4, 2);
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("HH:mm:ss.SSS"));
		
		//adding new group details
		callQueuesPage.openAddNewCallQueue(driver4);
		callQueuesPage.addNewCallQueueDetails(driver4, callQueueName, callQueueDescName);
		callQueuesPage.addMember(driver4, CONFIG.getProperty("qa_user_2_name"));
		callQueuesPage.selectLongestDistributationType(driver4);
		String timeout = callQueuesPage.getDialTimeout(driver4);
		callQueuesPage.saveGroup(driver4);
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, "");
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.dragAndDropDialImage(driver4);
		callFlowPage.selectGroupFromDialSection(driver4, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertEquals(callFlowPage.getQueueTimeOut(driver4), timeout);
		
		//deleting queue
		callQueuesPage.openCallQueueSearchPage(driver4);
		callQueuesPage.searchCallQueues(driver4, callQueueName, null);
		callQueuesPage.selectCallQueue(driver4, callQueueName);
		callQueuesPage.deleteCallQueue(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_queue_timeout_after_selecting_acd_call_queue-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_any_respone_no_reponse_disabled() {
		System.out.println("Test case --verify_call_flow_any_respone_no_reponse_disabled-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		String callFlow = CONFIG.getProperty("qa_call_flow_reponse_prompt");
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlow, "");
		callFlowPage.clickSelectedCallFlow(driver4, callFlow);
		
		callFlowPage.addPromptMaxDigits(driver4, "45");

		callFlowPage.clickAnyReponseGreeting(driver4);
		assertTrue(callFlowPage.isReponsePromptInputBoxDisabled(driver4));
		
		callFlowPage.clickNoReponseGreeting(driver4);
		assertTrue(callFlowPage.isReponsePromptInputBoxDisabled(driver4));

		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_call_flow_any_respone_no_reponse_disabled-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_time_widget() {
		System.out.println("Test case --verify_call_flow_time_widget-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		String callFlow = "AutomationCallFlowLPIST";
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlow, "");
		callFlowPage.clickSelectedCallFlow(driver4, callFlow);
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);

		String callFlowNumber = "+19046858476";
		
		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.enterNumberAndDial(driver4, callFlowNumber);
		
		dashboard.switchToTab(webSupportDriver, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(webSupportDriver));
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_call_flow_time_widget-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_any_value_no_value_branch_widget_disabled() {
		System.out.println("Test case --verify_call_flow_any_value_no_value_branch_widget_disabled-- started ");
		driverUsed.put("driver4", true);
		
		// verifying smart number click message
		String callFlow = CONFIG.getProperty("qa_call_flow_branch_widget");
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlow, "");
		callFlowPage.clickSelectedCallFlow(driver4, callFlow);
		
		callFlowPage.clickAnyValueGreeting(driver4);
		assertTrue(callFlowPage.isBranchAssignmentInputBoxDisabled(driver4));
		
		callFlowPage.clickNoValueDial(driver4);
		assertTrue(callFlowPage.isBranchAssignmentInputBoxDisabled(driver4));
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_call_flow_any_value_no_value_branch_widget_disabled-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_dial_to_number_with_timeout() {
		System.out.println("Test case --verify_dial_to_number_with_timeout-- started ");

		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);
		
//		String callFlow = "AutoCallFlowDialTimeout";
		String callFlowNumber = "+12057510170";
		
		//making call flow dial number
		softPhoneCalling.softphoneAgentCall(adminDriver, callFlowNumber);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(adminDriver));
		
		//verifying call received on dial number mentioned
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.isAcceptCallButtonVisible(webSupportDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(webSupportDriver));
		
		softPhoneCalling.declineCall(webSupportDriver);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		
		System.out.println("Test case --verify_dial_to_number_with_timeout-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_no_error_after_disable_dynamic_call_flow_setting() {
		System.out.println("Test case --verify_call_flow_no_error_after_disable_dynamic_call_flow_setting-- started ");
		
		// disabling call forwarding on user1
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		loginSupport(webSupportDriver);
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickAccountsLink(webSupportDriver);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		dashboard.isPaceBarInvisible(webSupportDriver);
		dashboard.scrollTillEndOfPage(webSupportDriver);
		accountIntelligentDialerTab.disableDynamicCallFlows(webSupportDriver);
		accountIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_low_priority");
		String userAccount	= CONFIG.getProperty("qa_user_account");
		
		dashboard.navigateToManageCallFlow(webSupportDriver);
		callFlowPage.searchCallFlow(webSupportDriver, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(webSupportDriver, callFlowName);

		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(webSupportDriver);
		callFlowPage.dragAndDropDialImage(webSupportDriver);

		callFlowPage.isIconSpinnerInvisible(webSupportDriver);
		callFlowPage.waitForPageLoaded(webSupportDriver);
		
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_call_flow_no_error_after_disable_dynamic_call_flow_setting-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void delete_user_and_verify_error_handling_in_call_flow() {
		System.out.println("Test case --delete_user_and_verify_error_handling_in_call_flow-- started ");
		driverUsed.put("driver4", true);
		
		//undelete user if deleted
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);

		loginSupport(webSupportDriver);
		dashboard.switchToTab(webSupportDriver, 2);

		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(webSupportDriver, "Akshita New 1", "akshita.bhargava@metacube.com", "0059000000To98VAAR");
		if(usersPage.isUnDeleteBtnVisible(webSupportDriver)){
			usersPage.clickUnDeleteBtn(webSupportDriver);
		}
		
		String callFlowName = "AutoCallFlowDialUser";
		String userAccount	= CONFIG.getProperty("qa_user_account");
		
		// disabling call forwarding on user1
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		
		// drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(driver4);
		callFlowPage.refreshCurrentDocument(driver4);
		assertFalse(callFlowPage.isAlertMsgVisible(driver4));
		callFlowPage.dragAndDropDialImage(driver4);
		callFlowPage.selectGroupFromDialSection(driver4, CallFlowPage.DialCallRDNATOCat.User, "Akshita New 1");
		
		//delete user
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettingsWithSalesForceId(driver4, "Akshita New 1", "akshita.bhargava@metacube.com", "0059000000To98VAAR");
		usersPage.deleteUser(driver4, "Akshita New 1");

		//verifying alert msg present
		dashboard.refreshCurrentDocument(driver4);
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, userAccount);
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		assertTrue(callFlowPage.isAlertMsgVisible(driver4));
		assertTrue(callFlowPage.getAlertMsgText(driver4).contains("This call flow is not valid. It cannot be assigned to a ringDNA number until the issues highlighted in red are addressed."));
		
		// undeleting user
		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(webSupportDriver, "Akshita New 1", "akshita.bhargava@metacube.com", "0059000000To98VAAR");
		usersPage.clickUnDeleteBtn(webSupportDriver);
				
		driverUsed.put("driver4", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --delete_user_and_verify_error_handling_in_call_flow-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_parameter_text_value_within_advance_tracking_smart_number() {
		
		System.out.println("Test case --verify_parameter_text_value_within_advance_tracking_smart_number-- started ");
		driverUsed.put("driver4", true);
		
		//verifying smart number click message
		dashboard.switchToTab(driver4, 2);
		dashboard.navigateToAddNewCallFlow(driver4);
		
		// Creating call flow
		String callFlowName = "AutoCallFlow".concat(HelperFunctions.GetRandomString(3));
		String callFlowDesc = "AutoCallDesc".concat(HelperFunctions.GetRandomString(3));
		
		callFlowPage.createCallFlow(driver4, callFlowName, callFlowDesc);
		
		//navigating to advanced call tracking
		dashboard.openAdvanceCallTrackingPage(driver4);
		
		// Adding advacnce call tracking number of Toll Free type
		String trackingName = "AutoTrackNumber " + helperFunctions.GetCurrentDateTime();
		String campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
		String customParamValue = "Automation".concat(HelperFunctions.GetRandomString(3));
		
		HashMap<AdminCallTracking.trackingNoFields, String> advTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.Name, trackingName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, customParamValue);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, callFlowName);
		advTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign, campaignName);
		adminCallTracking.clickAddNewAdvTrackNobtn(driver4);
		adminCallTracking.enterNewAdvTrackingNumberData(driver4, advTrackingNumberData);
		adminCallTracking.clickFinishButton(driver4);

		// getting the assigned smart number to advanced tracking number
		int trackingNumberIndex = adminCallTracking.getAdvTrackNumberIndex(driver4, trackingName, callFlowName);
		assertTrue(trackingNumberIndex >= 0);
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(driver4, trackingNumberIndex);
		assertEquals(adminCallTracking.getAdvTrackNameFromTable(driver4, trackingNumberIndex), trackingName);
		assertEquals(adminCallTracking.getAdvTrackCampaignFromTable(driver4, trackingNumberIndex), campaignName); 
		
		//navigate to call flow page
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		assertEquals(callFlowPage.getSmartNumberExtraInfo(driver4, trackingNumber), "{\"label\":\""+customParamValue+"\"}");
		assertEquals(callFlowPage.getSmartNumberName(driver4, trackingNumber), customParamValue);
		assertEquals(callFlowPage.getSmartNumberCampaign(driver4, trackingNumber), campaignName);
		
		//searching smart number of type trackingNumber
		dashboard.openSmartNumbersTab(driver4);
		smartNumbersPage.searchSmartNumber(driver4, trackingNumber);
		smartNumbersPage.clickSmartNumber(driver4, trackingNumber);

		assertEquals(smartNumbersPage.getInsertionValue(driver4), customParamValue);
		assertEquals(smartNumbersPage.getExtraInfo(driver4), "{\"label\":\""+customParamValue+"\"}");
		smartNumbersPage.deleteSmartNumber(driver4);
		
		//delete call flow
		dashboard.navigateToManageCallFlow(driver4);
		callFlowPage.searchCallFlow(driver4, callFlowName, CONFIG.getProperty("qa_user_account"));
		callFlowPage.clickSelectedCallFlow(driver4, callFlowName);
		callFlowPage.deleteCallFlow(driver4);
		
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_parameter_text_value_within_advance_tracking_smart_number-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_first_dial_to_cf_busy() {
		System.out.println("Test case --verify_call_flow_first_dial_to_cf_busy-- started ");

		// updating the driver used
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

		softPhoneSettingsPage.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);
		
		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);
		
		softPhoneSettingsPage.switchToTab(driver5, 1);
		softPhoneSettingsPage.clickSettingIcon(driver5);
		softPhoneSettingsPage.disableCallForwardingSettings(driver5);
		
		String frwardingNumberToSet = CONFIG.getProperty("prod_user_2_number");
		String callFlowNumber = "+19149357773";
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String acdQueueNumber = CONFIG.getProperty("qa_acd_group_lp_number");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		// Unsubscribe and subscribe acd queue from user 1
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);

		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.switchToTab(driver4, 1);
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);

		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);

		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//making dial first user busy on call with call forwarding set
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.deleteCallForwardingNumber(adminDriver, frwardingNumberToSet);
		softPhoneSettingsPage.setCallForwardingNumber(adminDriver, driver5, "", frwardingNumberToSet);

		// making acd call
		softPhoneActivityPage.switchToTab(webSupportDriver, 1);
		softPhoneCalling.softphoneAgentCall(webSupportDriver, CONFIG.getProperty("qa_admin_user_number"));
		softPhoneCalling.isCallHoldButtonVisible(webSupportDriver);		
		
		//verifying call goes to lwa and frwrding device
		softPhoneCalling.switchToTab(adminDriver, 1);
		callScreenPage.verifyUserImageBusy(adminDriver);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		
		//making frwrding caller and called busy
		softPhoneCalling.isAcceptCallButtonVisible(driver5);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver5));
		softPhoneCalling.pickupIncomingCall(driver5);
		
		// calling call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);	
		
		System.out.println(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
	
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.pickupIncomingCall(driverCallReceived2);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(driverCallReceived2));

		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case --verify_call_flow_first_dial_to_cf_busy-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_recording_in_call_flow_task() {
		System.out.println("Test case --verify_recording_in_call_flow_task-- started ");

		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String callFlowNumber = "+19149357773";
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));
		
		//making record calls disabled on user
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableRecordCallsSetting(adminDriver);
		
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		
		softPhoneCalling.pickupIncomingCall(adminDriver);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(adminDriver);
		callToolsPanel.enterCallNotesText(adminDriver, callNotes);
		callToolsPanel.appendCallNotesSubject(adminDriver, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(adminDriver);
		callToolsPanel.clickCallNotesSaveBtn(adminDriver);
		callToolsPanel.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(adminDriver);
		softPhoneActivityPage.openTaskInSalesforce(adminDriver, callSubject);
		softPhoneActivityPage.waitForPageLoaded(adminDriver);
		sfTaskDetailPage.closeLightningDialogueBox(adminDriver);

		assertTrue(sfTaskDetailPage.getCallRecordingUrl(adminDriver).contains("recordings"));
		sfTaskDetailPage.closeTab(adminDriver);
		sfTaskDetailPage.switchToTab(adminDriver, 1);
		
		//making record calls disabled on user
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.enableRecordCallsSetting(adminDriver);
				
		driverUsed.put("adminDriver", false);
		driverUsed.put("driver2", false);
		System.out.println("Test case --verify_recording_in_call_flow_task-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_caller_going_to_vm_with_noanswer_step_defined() {
		System.out.println("Test case --verify_caller_going_to_vm_with_noanswer_step_defined-- started ");

		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);

		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(adminDriver, 1);
		}
		
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//taking an inbound call on websupport driver
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("qa_support_user_number"));
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		softPhoneCalling.pickupIncomingCall(webSupportDriver);
		
		//making an outbound call from call flow number
		String callFlowNumber = "+19382223652";
		softPhoneCalling.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentCall(driver4, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		softPhoneCalling.isAdditionalCallSendToVMBtnEnable(webSupportDriver);
		softPhoneCalling.isAdditionalCallSendToVMInvisible(webSupportDriver);
		
		softPhoneCalling.idleWait(10);
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		
		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1));
	
		System.out.println("Test case --verify_caller_going_to_vm_with_noanswer_step_defined-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_dial_to_skill_no_answer_dial_another() {
		System.out.println("Test case --verify_dial_to_skill_no_answer_dial_another-- started ");
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		// disabling call forwarding on user1
		dashboard.switchToTab(adminDriver, 1);
		softPhoneCalling.reloadSoftphone(adminDriver);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
	
		//login as agent user
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//calling from agent user to call flow number
		String skillNumber = "+13345648827";
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, skillNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		//verifying call coming on first user
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		softPhoneCalling.declineCall(adminDriver);
		
		//verifying call present on user2
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.isAcceptCallButtonVisible(webSupportDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(webSupportDriver));
		
		//get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.pickupIncomingCall(webSupportDriver);
		
		//hold and resume call
		softPhoneCalling.clickHoldButton(webSupportDriver);
		softPhoneCalling.clickOnHoldButton(webSupportDriver);
		softPhoneCalling.clickResumeButton(webSupportDriver);
		softPhoneCalling.isHangUpButtonVisible(webSupportDriver);
		
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(webSupportDriver);
		callToolsPanel.enterCallNotesText(webSupportDriver, callNotes);
		callToolsPanel.appendCallNotesSubject(webSupportDriver, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(webSupportDriver);
		callToolsPanel.clickCallNotesSaveBtn(webSupportDriver);
		callToolsPanel.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);

		//saving contact if not already
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		if (callScreenPage.isCallerUnkonwn(webSupportDriver)) {
			callToolsPanel.callNotesSectionVisible(webSupportDriver);
			callScreenPage.addCallerAsLead(webSupportDriver, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		}

		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(webSupportDriver);
		softPhoneActivityPage.openTaskInSalesforce(webSupportDriver, callSubject);
		softPhoneActivityPage.waitForPageLoaded(webSupportDriver);
		sfTaskDetailPage.closeLightningDialogueBox(webSupportDriver);
		System.out.println(sfTaskDetailPage.getSubject(webSupportDriver));
		assertEquals(sfTaskDetailPage.getSubject(webSupportDriver), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(webSupportDriver), "Completed");
		sfTaskDetailPage.verifyCallStatus(webSupportDriver, "Connected");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(webSupportDriver).contains("recordings"));
		sfTaskDetailPage.verifyCallNotAbandoned(webSupportDriver);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(webSupportDriver);
		sfTaskDetailPage.closeTab(webSupportDriver);
		sfTaskDetailPage.switchToTab(webSupportDriver, 1);
		
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.hangupIfHoldCall(webSupportDriver);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_dial_to_skill_no_answer_dial_another-- passed ");
	}
		
//	@Test(groups = {"MediumPriority"})
	public void verify_group_voicemail_entry_when_call_flow_dials_to_user() {
		System.out.println("Test case --verify_group_voicemail_entry_when_call_flow_dials_to_user-- started ");
		driverUsed.put("driver4", true);
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String acdQueueNumber = "+13045841646";
		String callFlowNumber = "+19149357773";
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver3, 1);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.disableCallForwardingSettings(driver3);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.disableCallForwardingSettings(driver4);
		
		// Unsubscribe and subscribe acd queue from user 1
		softPhoneCallQueues.unSubscribeAllQueues(driver1);
		softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);

		// Unsubscribe and subscribe acd queue from user 2
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);

		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		// delete first voice mail	
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver4);
		softphoneCallHistoryPage.clickGroupCallsLink(driver4);
		softphoneCallHistoryPage.deleteAllVMQueue(driver4, acdQueueLPName);
		
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, acdQueueLPName);
		
		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);

		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, acdQueueNumber);
		callQueuesPage.idleWait(3);

		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// calling call flow number
		softPhoneActivityPage.switchToTab(driver2, 1);
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
		softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);

		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived1));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived1);
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived2));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived2);
		
		softPhoneCalling.isAcceptCallButtonVisible(driverCallReceived3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driverCallReceived3));
		softPhoneCalling.verifyDeclineButtonIsInvisible(driverCallReceived3);
		
		softPhoneCalling.idleWait(10);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		 //Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driverCallReceived2);
		softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived2);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived2);
	    softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived2, acdQueueLPName);
	    
	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived1);
	    softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived1);
	    softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived1, acdQueueLPName);

	    //Open call entry for the users to whom voicemail is dropped
	    softPhoneSettingsPage.clickSettingIcon(driverCallReceived3);
	    softphoneCallHistoryPage.openCallsHistoryPage(driverCallReceived3);
	    softphoneCallHistoryPage.clickGroupCallsLink(driverCallReceived3);
	    softphoneCallHistoryPage.playVMByCallQueue(driverCallReceived3, acdQueueLPName);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case --verify_group_voicemail_entry_when_call_flow_dials_to_user-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_send_to_voicemail_visible_when_no_answer_to_vm_after_dial_to_queue() {
		System.out.println("Test case --verify_send_to_voicemail_visible_when_no_answer_to_vm_after_dial_to_queue-- started ");
		
		driverUsed.put("driver4", true);

		String callFlowNumber = "+12164466818";
		String callFlowName = "AutomationCallFlowDialQueueDefaultLP";
		String callQueueName = "AutomationCallQueueDefault";
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(adminDriver, 1);
		}
		
		//subscribe a queue for caller 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, callQueueName);
	    
	    //subscribe a queue for caller 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, callQueueName);

	    //dialing to call flow
	    softPhoneCalling.switchToTab(driver4, 1);
	    softPhoneCalling.softphoneAgentCall(driver4, callFlowNumber);
	    
	    //call is visible to other caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    
	    softPhoneCallQueues.sendToVoiceMailFromQueue(driver1);
	    softPhoneCallQueues.idleWait(15);
	    
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1));
		
		//verifying sfdc task
		softphoneCallHistoryPage.openCallEntryByIndex(adminDriver, 0);
		softPhoneActivityPage.clickFirstSubject(adminDriver);
		softPhoneActivityPage.waitForPageLoaded(adminDriver);
		sfTaskDetailPage.closeLightningDialogueBox(adminDriver);
		sfTaskDetailPage.verifyVoicemailCreatedActivity(adminDriver);
		assertTrue(Strings.isNotNullAndNotEmpty(sfTaskDetailPage.getCallRecordingUrl(adminDriver)));
		assertEquals(sfTaskDetailPage.getCallFlow(adminDriver), callFlowName);
		sfTaskDetailPage.verifyCallStatus(adminDriver, "Missed");
		callQueuesPage.closeTab(adminDriver);
		callQueuesPage.switchToTab(adminDriver, 1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_send_to_voicemail_visible_when_no_answer_to_vm_after_dial_to_queue-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_send_to_voicemail_not_visible_when_no_answer_to_vm_after_dial_to_queue() {
		System.out.println("Test case --verify_send_to_voicemail_not_visible_when_no_answer_to_vm_after_dial_to_queue-- started ");
		driverUsed.put("driver4", true);

		String callFlowNumber = "+12056563707";
		String callFlowName = "AutomationCallFlowDialQueueDefaultLP2";
		String callQueueName = "AutomationCallQueueDefault";
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, callQueueName);

		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver3);
		softphoneCallHistoryPage.clickGroupCallsLink(driver3);
		softphoneCallHistoryPage.deleteAllVMQueue(driver3, callQueueName);

		//subscribe a queue for caller 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, callQueueName);
	    
	    //subscribe a queue for caller 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, callQueueName);

	    //dialing to call flow
	    softPhoneCalling.switchToTab(driver2, 1);
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    
	    //verifying call comes to user1
	    softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);
	    
	    //call is visible to other caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    assertTrue(softPhoneCallQueues.isdeclineCallVisibleFromQueue(driver1));
	    assertTrue(softPhoneCallQueues.isSendToVoiceMailDisabledFromQueue(driver1));
	    
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    
	    //verifying call comes to user2
	    softPhoneCalling.switchToTab(driver4, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(driver4);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(driver4);
	    
	    softPhoneCallQueues.idleWait(15);
	    
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.playVMByCallQueue(driver1, callQueueName);
		
		//verifying sfdc task
		softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
		softPhoneActivityPage.clickFirstSubject(driver1);
		softPhoneActivityPage.waitForPageLoaded(driver1);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);
		sfTaskDetailPage.verifyVoicemailCreatedActivity(driver1);
		assertTrue(Strings.isNotNullAndNotEmpty(sfTaskDetailPage.getCallRecordingUrl(driver1)));
		assertEquals(sfTaskDetailPage.getCallFlow(driver1), callFlowName);
		assertEquals(sfTaskDetailPage.getCallQueue(driver1), callQueueName.concat(" - ").concat(callFlowName));
		sfTaskDetailPage.verifyCallStatus(driver1, "Missed");
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		driverUsed.put("adminDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_send_to_voicemail_not_visible_when_no_answer_to_vm_after_dial_to_queue-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_multiple_dial_to_queue_send_to_voicemail_btn_visible() {
		System.out.println("Test case --verify_multiple_dial_to_queue_send_to_voicemail_btn_visible-- started ");
		driverUsed.put("driver4", true);

		String callFlowNumber = "+18329004749";
		String callFlowName = "AutomationCallFlowMultipleDialQueue";
		String callQueueName1 = "AutomationCallQueueDefault2";
		String callQueueName2 = "AutomationCallQueueDefault3";
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		softPhoneSettingsPage.switchToTab(driver1, 1);
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		softPhoneSettingsPage.switchToTab(driver2, 1);
		softPhoneSettingsPage.clickSettingIcon(driver2);
		softPhoneSettingsPage.disableCallForwardingSettings(driver2);

		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);

		softPhoneSettingsPage.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
		softphoneCallHistoryPage.clickMyCallsLink(webSupportDriver);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(webSupportDriver, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(webSupportDriver, 1);
		}
		 
		//subscribe a queue for caller 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, callQueueName1);
	    softPhoneCallQueues.isQueueSubscribed(driver1, callQueueName1);
	    
	    //subscribe a queue for caller 2
	    softPhoneCallQueues.switchToTab(driver4, 1);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, callQueueName2);
	    softPhoneCallQueues.isQueueSubscribed(driver4, callQueueName2);

	    //dialing to call flow
	    softPhoneCalling.switchToTab(driver2, 1);
	    softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver2);
	    
	    //verifying call comes to user1
	    softPhoneCalling.switchToTab(adminDriver, 1);
	    softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
	    softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);
	    
	    //call is visible to other caller
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    assertTrue(softPhoneCallQueues.isdeclineCallVisibleFromQueue(driver1));
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    
	    //verifying call comes to user2
	    softPhoneCallQueues.isPickCallBtnVisible(driver4);
	    softPhoneCallQueues.sendToVoiceMailFromQueue(driver4);
	    softPhoneCallQueues.idleWait(10);
	    
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(webSupportDriver);
		softphoneCallHistoryPage.clickMyCallsLink(webSupportDriver);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(webSupportDriver, 1));
		
		//verifying sfdc task
		softphoneCallHistoryPage.openCallEntryByIndex(webSupportDriver, 0);
		softPhoneActivityPage.clickFirstSubject(webSupportDriver);
		softPhoneActivityPage.waitForPageLoaded(webSupportDriver);
		sfTaskDetailPage.closeLightningDialogueBox(webSupportDriver);
		sfTaskDetailPage.verifyVoicemailCreatedActivity(webSupportDriver);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(webSupportDriver).contains("recordings"));
		assertEquals(sfTaskDetailPage.getCallFlow(webSupportDriver), callFlowName);
		sfTaskDetailPage.verifyCallStatus(webSupportDriver, "Missed");
		callQueuesPage.closeTab(webSupportDriver);
		callQueuesPage.switchToTab(webSupportDriver, 1);
		
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("adminDriver", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_multiple_dial_to_queue_send_to_voicemail_btn_visible-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_call_flow_with_dial_to_group_no_answer_vm() {
		System.out.println("Test case --verify_call_flow_with_dial_to_group_no_answer_vm()-- started ");

		// updating the driver used
		driverUsed.put("driver4", true);

		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callFlowNumber = "+12058283267";
		String acdQueueLPName = CONFIG.getProperty("qa_acd_group_lp");
		String accountName = CONFIG.getProperty("qa_user_account");
		List<String> acdDriversUsed = new ArrayList<String>();
		WebDriver driverCallReceived1 = null;
		WebDriver driverCallReceived2 = null;
		WebDriver driverCallReceived3 = null;

		 //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueLPName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueLPName);
	    
		// Unsubscribe and subscribe acd queue from user 2
	    softPhoneCallQueues.switchToTab(driver4, 1);
		softPhoneCallQueues.unSubscribeAllQueues(driver4);
		softPhoneCallQueues.subscribeQueue(driver4, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver4, acdQueueLPName);
		
		// Unsubscribe and subscribe acd queue from user 3
		softPhoneCallQueues.unSubscribeAllQueues(driver3);
		softPhoneCallQueues.subscribeQueue(driver3, acdQueueLPName);
		softPhoneCallQueues.isQueueSubscribed(driver3, acdQueueLPName);

		loginSupport(driver1);
		callQueuesPage.openCallQueueSearchPage(driver1);
		callQueuesPage.openCallQueueDetailPage(driver1, acdQueueLPName, accountName);
		callQueuesPage.deleteMember(driver1, CONFIG.getProperty("qa_agent_user_name"));
		callQueuesPage.selectLongestDistributationType(driver1);
		callQueuesPage.setMaxDials(driver1, "4");
		callQueuesPage.saveGroup(driver1);
		callQueuesPage.closeTab(driver1);
		callQueuesPage.switchToTab(driver1, 1);
		
		// making first call to ACD queue
		System.out.println("making first call to ACD queue");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		String driverCallReceivedUser1 = null;
		String driverCallReceivedNumber1 = null;
		
		// picking up with the first device
		System.out.println("picking up with the first device");
		if (softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived1 = driver1;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_1_name");
			driverCallReceivedNumber1 = CONFIG.getProperty("qa_user_1_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived1 = driver4;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_2_name");
			driverCallReceivedNumber1 = CONFIG.getProperty("qa_user_2_number");
		} else if (softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived1 = driver3;
			driverCallReceivedUser1 = CONFIG.getProperty("qa_user_3_name");
			driverCallReceivedNumber1 = CONFIG.getProperty("qa_user_3_number");
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived1);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived2 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived2 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived2 = driver3;
		} else {
			Assert.fail("No call received");
		}
		softPhoneCalling.declineCall(driverCallReceived2);

		System.out.println("picking up with the first device");
		if (!acdDriversUsed.contains("driver1") && softPhoneCalling.isDeclineButtonVisible(driver1)) {
			acdDriversUsed.add("driver1");
			driverCallReceived3 = driver1;
		} else if (!acdDriversUsed.contains("driver4") && softPhoneCalling.isDeclineButtonVisible(driver4)) {
			acdDriversUsed.add("driver4");
			driverCallReceived3 = driver4;
		} else if (!acdDriversUsed.contains("driver3") && softPhoneCalling.isDeclineButtonVisible(driver3)) {
			acdDriversUsed.add("driver3");
			driverCallReceived3 = driver3;
		} else {
			Assert.fail("No call received");
		}

		softPhoneCalling.declineCall(driverCallReceived3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		softPhoneContactsPage.addContactIfNotExist(driver2, driverCallReceivedNumber1, driverCallReceivedUser1);

		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);
		
		// get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));
		
		softPhoneCalling.pickupIncomingCall(driverCallReceived1);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(driverCallReceived1);
		callToolsPanel.enterCallNotesText(driverCallReceived1, callNotes);
		callToolsPanel.idleWait(1);
		callToolsPanel.appendCallNotesSubject(driverCallReceived1, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(driverCallReceived1);
		callToolsPanel.clickCallNotesSaveBtn(driverCallReceived1);
		callToolsPanel.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driverCallReceived1);
		softPhoneCalling.hangupIfInActiveCall(driver2);

		// Navigating to task detail page to check url
		softPhoneActivityPage.openTaskInSalesforce(driverCallReceived1, callSubject);
		softPhoneActivityPage.waitForPageLoaded(driverCallReceived1);
		sfTaskDetailPage.closeLightningDialogueBox(driverCallReceived1);

		assertEquals(sfTaskDetailPage.getCallStatus(driverCallReceived1), "Connected");
		assertEquals(sfTaskDetailPage.getSubject(driverCallReceived1), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(driverCallReceived1), "Completed");
		assertTrue(Strings.isNotNullAndNotEmpty(sfTaskDetailPage.getcallQueueHoldTime(driverCallReceived1)));
		sfTaskDetailPage.verifyCallNotAbandoned(driverCallReceived1);
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(driverCallReceived1);
		sfTaskDetailPage.closeTab(driverCallReceived1);
		sfTaskDetailPage.switchToTab(driverCallReceived1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		
		System.out.println("Test case --verify_call_flow_with_dial_to_group_no_answer_vm()-- started ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_lead_created_for_unknown_contact_to_tracking_number() {
		System.out.println("Test case --verify_lead_created_for_unknown_contact_to_tracking_number-- started ");
		
		// updating the driver used
		driverUsed.put("driver4", true);

		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		// Enable multiple lead creation for SFDC campaign and create lead on every
		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountSalesforceTab.openSalesforceTab(driver4);
		
		accountSalesforceTab.enableCreateLeadEveryInboundCallSetting(driver4);
		accountSalesforceTab.saveAcccountSettings(driver4);
		
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableDisableLeadCreationSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		accountSalesforceTab.switchToTab(driver4, 1);
	
		// deleting contact
		deleteContact(driver4, agentDriver, CONFIG.getProperty("qa_agent_user_number"),
				CONFIG.getProperty("qa_agent_user_name"));
	
		// adding campaign to call flow number
		String callFlowNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
		
		// calling to call flow so that new lead gets created
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		//verify header caller details
		String callSubject = "AutoSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
		assertTrue(callScreenPage.getForeGroundCallerName(driver4).contains("Unknown"));
		
		String user = CONFIG.getProperty("qa_user_2_name");
		softPhoneCalling.pickupIncomingCall(driver4);
		callScreenPage.idleWait(5);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		assertEquals(callScreenPage.getCallerName(driver4), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), "Unknown Unknown");
	
		callToolsPanel.enterCallNotes(driver4, callSubject, callNotes);
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupActiveCall(agentDriver);
		softPhoneCalling.hangupIfInActiveCall(driver4);

		// verify the data for lead is same as contact in sfdc
		callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		assertEquals(contactDetailPage.getLeadOwner(driver4), user);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		assertTrue(sfTaskDetailPage.getAssignedToUser(driver4).equals(user));
		assertTrue(sfTaskDetailPage.getCreatedByUser(driver4).equals(user));
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	
		// updating the driver used
		driverUsed.put("agentDriver", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_lead_created_for_unknown_contact_to_tracking_number-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_calls_into_call_flows_cannot_be_presented_to_the_same_call_queue_twice() {
		System.out.println("Test case --verify_calls_into_call_flows_cannot_be_presented_to_the_same_call_queue_twice-- started ");
		driverUsed.put("driver4", true);

		String callFlowNumber = "+12133489352";
		String callQueueName = "AutomationCallQueueDefault";
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softphoneCallHistoryPage.clickGroupCallsLink(driver1);
		softphoneCallHistoryPage.deleteAllVMQueue(driver1, callQueueName);

		 //get the counts of group calls
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    System.out.println(softphoneCallHistoryPage.getMissedGroupCallCount(driver1));
		
	    //get the counts of group calls
	    softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    System.out.println(softphoneCallHistoryPage.getMissedGroupCallCount(driver3));
	    
		//subscribe a queue for caller 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, callQueueName);
	    
	    //subscribe a queue for caller 2
	    softPhoneCallQueues.unSubscribeAllQueues(driver3);
	    softPhoneCallQueues.subscribeQueue(driver3, callQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver3, callQueueName);

	    //dialing to call flow
	    softPhoneCalling.switchToTab(driver4, 1);
	    softPhoneCalling.softphoneAgentCall(driver4, callFlowNumber);
	    softPhoneCalling.isCallHoldButtonVisible(driver4);
	    
	    //call is visible to queue members
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    
	    //waiting till call disappears
	    softPhoneCallQueues.isPickCallBtnInvisible(driver1);
	    softPhoneCallQueues.isPickCallBtnInvisible(driver3);
	    
	    //verifying call comes to user
	    softPhoneCalling.isAcceptCallButtonVisible(adminDriver);
	    assertTrue(softPhoneCalling.isDeclineButtonVisible(adminDriver));
	    softPhoneCalling.verifyDeclineButtonIsInvisible(adminDriver);
	    
	    //call is visible to queue members
	    softPhoneCallQueues.isPickCallBtnVisible(driver3);
	    softPhoneCallQueues.isPickCallBtnVisible(driver1);
	    
	    //waiting till call disappears
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    softPhoneCallQueues.pickCallFromQueue(driver3);
	    
	    softPhoneCalling.hangupActiveCall(driver4);

	    //get the counts of group calls
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    System.out.println(softphoneCallHistoryPage.getMissedGroupCallCount(driver1));
		
	    //get the counts of group calls
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    System.out.println(softphoneCallHistoryPage.getMissedGroupCallCount(driver3));
		
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_calls_into_call_flows_cannot_be_presented_to_the_same_call_queue_twice-- passed ");
	}

	@Test(groups = {"MediumPriority", "Product Sanity", "ExludeForProd"})
	public void verify_dial_to_call_flow_from_multimatch_contact_then_call_redirects_to_no_answer_step() {
		System.out.println("Test case --verify_dial_to_call_flow_from_multimatch_contact_then_call_redirects_to_no_answer_step-- started ");

		String callFlowNumber = CONFIG.getProperty("qa_greet_record_owner_call_flow_number");
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		// delete first voice mail
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		if (softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1)) {
			softphoneCallHistoryPage.deleteVMByIndex(adminDriver, 1);
		}
		    
	    //agent driver multiple
	    addCallerMultiple(driver1, agentDriver, CONFIG.getProperty("qa_agent_user_number"));
	    
		//calling to call flow number from multi match contact
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		softPhoneCalling.idleWait(20);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.idleWait(2);
		
		// Open call entry for the users to whom voicemail is dropped
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(adminDriver);
		softphoneCallHistoryPage.clickMyCallsLink(adminDriver);
		assertTrue(softphoneCallHistoryPage.isVMPlayPresentByIndex(adminDriver, 1));

		softphoneCallHistoryPage.openCallEntryByIndex(adminDriver, 0);
	 	callScreenPage.selectFirstContactFromMultiple(adminDriver);
	 	
	 	String callSubject = callToolsPanel.changeAndGetCallSubject(adminDriver);
		callScreenPage.idleWait(2);
		callScreenPage.openCallerDetailPage(adminDriver);
		softPhoneActivityPage.waitForPageLoaded(adminDriver);
		sfTaskDetailPage.closeLightningDialogueBox(adminDriver);
		contactDetailPage.openRecentCallEntry(adminDriver, callSubject);
	    
		//verifying sfdc task
		assertEquals(sfTaskDetailPage.getToNumber(adminDriver), HelperFunctions.getNumberInSimpleFormat(callFlowNumber));//agentnumber
		assertEquals(sfTaskDetailPage.getFromNumber(adminDriver), HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_agent_user_number")));
		sfTaskDetailPage.verifyVoicemailCreatedActivity(adminDriver);
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(adminDriver).contains("recordings"));
		sfTaskDetailPage.verifyCallStatus(adminDriver, "Missed");
		callQueuesPage.closeTab(adminDriver);
		callQueuesPage.switchToTab(adminDriver, 1);
		
	    driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_dial_to_call_flow_from_multimatch_contact_then_call_redirects_to_no_answer_step-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_dial_to_call_flow_from_single_lead_then_call_redirects_to_lead_owner() {
		System.out.println("Test case --verify_dial_to_call_flow_from_single_lead_then_call_redirects_to_lead_owner-- started ");

		String callFlowNumber = "+12133489853";
		String callFlowName = "AutomationFlowGreetingDialRecordOwner";
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
		
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		// Deleting contact if exist and adding agent driver as single lead
		deleteContact(adminDriver, agentDriver, CONFIG.getProperty("qa_agent_user_number"),
				CONFIG.getProperty("qa_agent_user_name"));
		
		callScreenPage.addCallerAsLead(adminDriver, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("contact_account_name"));

		callScreenPage.reloadSoftphone(adminDriver);
		
		//calling to call flow number
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		// get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));

		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(adminDriver);
		callToolsPanel.enterCallNotesText(adminDriver, callNotes);
		callToolsPanel.appendCallNotesSubject(adminDriver, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(adminDriver);
		callToolsPanel.clickCallNotesSaveBtn(adminDriver);
		callToolsPanel.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// Navigating to task detail page to check url
		softPhoneActivityPage.openTaskInSalesforce(adminDriver, callSubject);
		softPhoneActivityPage.waitForPageLoaded(adminDriver);
		sfTaskDetailPage.closeLightningDialogueBox(adminDriver);

		// verifying sfdc task
		assertEquals(sfTaskDetailPage.getCallStatus(adminDriver), "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(adminDriver), "Inbound");
		assertEquals(sfTaskDetailPage.getToNumber(adminDriver), HelperFunctions.getNumberInSimpleFormat(callFlowNumber));
		assertEquals(sfTaskDetailPage.getFromNumber(adminDriver), HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_agent_user_number")));
		sfTaskDetailPage.verifyNotVoicemailCreatedActivity(adminDriver);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(adminDriver).contains("recordings"));
		assertEquals(sfTaskDetailPage.getCallFlow(adminDriver), callFlowName);

	    seleniumBase.closeTab(adminDriver);
	    seleniumBase.switchToTab(adminDriver, 1);
	    
	    driverUsed.put("agentDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_dial_to_call_flow_from_single_lead_then_call_redirects_to_lead_owner-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_dial_to_call_flow_from_single_contact_then_call_redirects_to_lead_owner() {
		System.out.println("Test case --verify_dial_to_call_flow_from_single_contact_then_call_redirects_to_lead_owner-- started ");

		String callFlowNumber = "+12133489853";
		String callFlowName = "AutomationFlowGreetingDialRecordOwner";
		
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);

		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);

		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(adminDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(adminDriver);
		
		// Deleting contact if exist and adding agent driver as single contact
		deleteContact(adminDriver, agentDriver, CONFIG.getProperty("qa_agent_user_number"),
				CONFIG.getProperty("qa_agent_user_name"));
		
		softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("qa_agent_user_number"));
		softPhoneCalling.hangupActiveCall(adminDriver);

		//adding as contact for caller
		softPhoneContactsPage.addContactIfNotExist(adminDriver, CONFIG.getProperty("qa_agent_user_number"), CONFIG.getProperty("qa_agent_user_name"));
		softPhoneActivityPage.reloadSoftphone(adminDriver);
		
		//calling to call flow number
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
			
		// get sfdc id and verifying
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(4));
		
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.pickupIncomingCall(adminDriver);
		softPhoneCalling.idleWait(2);
		callToolsPanel.clickCallNotesIcon(adminDriver);
		callToolsPanel.enterCallNotesText(adminDriver, callNotes);
		callToolsPanel.appendCallNotesSubject(adminDriver, HelperFunctions.GetRandomString(4));
		callToolsPanel.idleWait(1);
		String callSubject = callToolsPanel.getCallNotesSubject(adminDriver);
		callToolsPanel.clickCallNotesSaveBtn(adminDriver);
		callToolsPanel.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// Navigating to task detail page to check url
		softPhoneActivityPage.openTaskInSalesforce(adminDriver, callSubject);
		softPhoneActivityPage.waitForPageLoaded(adminDriver);
		sfTaskDetailPage.closeLightningDialogueBox(adminDriver);

		assertEquals(sfTaskDetailPage.getSubject(adminDriver), callSubject);
		assertEquals(sfTaskDetailPage.getTaskStatus(adminDriver), "Completed");
		sfTaskDetailPage.verifyCallStatus(adminDriver, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(adminDriver), "Inbound");
		assertTrue(sfTaskDetailPage.getCallRecordingUrl(adminDriver).contains("recordings"));
		assertEquals(sfTaskDetailPage.getCallFlow(adminDriver), callFlowName);
		sfTaskDetailPage.closeTab(adminDriver);
		sfTaskDetailPage.switchToTab(adminDriver, 1);
		
	    softPhoneCalling.hangupIfInActiveCall(driver2);
	    
		driverUsed.put("agentDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_dial_to_call_flow_from_single_contact_then_call_redirects_to_lead_owner-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller() {
		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Deleting contact if exist driver4
		callScreenPage.switchToTab(driver1, 1);
		deleteContact(driver1, driver4, CONFIG.getProperty("qa_user_2_number"),
				CONFIG.getProperty("qa_user_2_name"));
		
		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		dashboard.switchToTab(driver4, 1);
		
		// calling to agents default smart number
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver1));
		assertEquals(callScreenPage.getForeGroundCallerName(driver1), "Incoming: Unknown");
		assertFalse(callScreenPage.isLeadImageVisible(driver1));
		
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.isCallHoldButtonVisible(driver1);
		
		assertFalse(callScreenPage.isLeadImageVisible(driver1));
		assertEquals(callScreenPage.getForeGroundCallerName(driver1), "Unknown");
		
		softPhoneCalling.hangupIfInActiveCall(driver1);

		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);

		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller_group_call() {
		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller_group_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//make a call flow with dial to lwa queue with one member driver4
		String queueNumber = "+12408235492";
		String acdQueue = "AutomationQueueOneUser";
		
		 //Unsubscribe and subscribe acd queue from user 1
		dashboard.switchToTab(driver4, 1);
	    softPhoneCallQueues.unSubscribeAllQueues(driver4);
	    softPhoneCallQueues.subscribeQueue(driver4, acdQueue);
	    softPhoneCallQueues.isQueueSubscribed(driver4, acdQueue);
	
		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		dashboard.switchToTab(driver4, 1);
		
		// Deleting contact if exist
		deleteContact(driver4, driver2, CONFIG.getProperty("prod_user_1_number"),
				CONFIG.getProperty("prod_user_1_name"));

		// calling to queue number so that new lead gets created
		softPhoneCalling.switchToTab(driver2, 1);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.switchToTab(driver4, 1);
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver2);

		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
		assertTrue(callScreenPage.getForeGroundCallerName(driver4).contains("Unknown"));
		
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		assertFalse(callScreenPage.isLeadImageVisible(driver4));
		assertTrue(callScreenPage.getForeGroundCallerName(driver4).contains("Unknown"));

		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);

		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_unknown_caller_group_call-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_no_lead_create_when_lead_creation_off_calling_from_lead_caller_campaign_call() {
		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_lead_caller_campaign_call-- started ");
		
		// updating the driver used
		driverUsed.put("driver4", true);

		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.disableDisableLeadCreationSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		dashboard.switchToTab(driver4, 1);
		
		//delete contact
		deleteContact(driver4, agentDriver, CONFIG.getProperty("qa_agent_user_number"),  CONFIG.getProperty("qa_agent_user_name"));
		
		// Adding lead if not there
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		callScreenPage.switchToTab(driver4, 1);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver4);

		callScreenPage.addCallerAsLead(driver4, CONFIG.getProperty("qa_agent_user_name"),
				CONFIG.getProperty("contact_account_name"));

		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		callScreenPage.reloadSoftphone(driver4);
		
		//taking call flow number with campaign number
		String callFlowNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
		
		// calling to call flow so that new lead gets created
		String callerName = CONFIG.getProperty("qa_agent_user_name").concat(" Automation");
		softPhoneCalling.softphoneAgentCall(agentDriver, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), "Incoming: " + callerName);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		assertEquals(callScreenPage.getCallerName(driver4), callerName);
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), callerName);

		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.hangupIfInActiveCall(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case --verify_no_lead_create_when_lead_creation_off_calling_from_lead_caller_campaign_call-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_no_lead_create_when_lead_creation_on_calling_from_unknown_caller_campaign_call() {
		System.out.println("Test case --verify_no_lead_create_when_lead_creation_on_calling_from_unknown_caller_campaign_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableDisableLeadCreationSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		dashboard.switchToTab(driver4, 1);
		
		// Deleting contact if exist driver1
		callScreenPage.switchToTab(driver4, 1);
		deleteContact(driver4, driver1, CONFIG.getProperty("qa_user_1_number"),
				CONFIG.getProperty("qa_user_1_name"));
		
		// calling to call flow from unknown contact
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));
		softPhoneCalling.isCallHoldButtonVisible(driver1);
		
		softPhoneCalling.isAcceptCallButtonVisible(driver4);
		assertEquals(callScreenPage.getCallerName(driver4), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), "Incoming: Unknown Unknown");
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.isCallHoldButtonVisible(driver4);
		
		assertEquals(callScreenPage.getCallerName(driver4), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), "Unknown Unknown");
		assertTrue(callScreenPage.isLeadImageVisible(driver4));

		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneCalling.hangupActiveCall(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);

		System.out.println("Test case --verify_no_lead_create_when_lead_creation_on_calling_from_unknown_caller_campaign_call-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_lead_create_when_lead_creation_off_calling_from_unknown_caller_campaign_call() {
		System.out.println("Test case --verify_lead_create_when_lead_creation_off_calling_from_unknown_caller_campaign_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		dashboard.switchToTab(driver4, 1);
	  	
		//adding campaign to call flow number
		String callFlowNumber = CONFIG.getProperty("qa_call_flow_call_conference_smart_number");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver4)) {
			callScreenPage.deleteCallerObject(driver4);
			reloadSoftphone(driver4);
		}	
		
		//dialing from caller to call flow number
		softPhoneCalling.softphoneAgentCall(driver1, callFlowNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);
		
		//picking up call
		softPhoneCalling.pickupIncomingCall(driver4);
		callScreenPage.idleWait(2);
		assertTrue(callScreenPage.isLeadImageVisible(driver4));
		assertEquals(callScreenPage.getCallerName(driver4), "Unknown Unknown");
		assertEquals(callScreenPage.getForeGroundCallerName(driver4), "Unknown Unknown");
		
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify the data for lead is same as contact in sfdc
		String user = CONFIG.getProperty("qa_user_2_name");
		String dueDate = new SimpleDateFormat("M/d/yyyy").format(Calendar.getInstance().getTime());
		
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver4);
		assertEquals(contactDetailPage.getCallerName(driver4), "Unknown Unknown");
		assertEquals(contactDetailPage.getCallerCompanyName(driver4), "Unknown");
		assertEquals(contactDetailPage.getLeadOwner(driver4), user);
		
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		assertEquals(sfTaskDetailPage.getAssignedToUser(driver4), user); 
	    assertEquals(sfTaskDetailPage.getSubject(driver4), callSubject);
	    sfTaskDetailPage.verifyCallStatus(driver4, "Connected");	
	    assertEquals(sfTaskDetailPage.getDueDate(driver4), dueDate);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//add caller as contact
		softPhoneContactsPage.deleteAndAddContact(driver4, CONFIG.getProperty("qa_user_1_number"), CONFIG.getProperty("qa_user_1_name"));
		
		dashboard.switchToTab(driver4, dashboard.getTabCount(driver4));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);

		System.out.println("Test case --verify_lead_create_when_lead_creation_off_calling_from_unknown_caller_campaign_call-- passed ");
	}
	
	@AfterClass(groups = {"MediumPriority"})
	public void afterClass(){
		
		//initialising the driver
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		softPhoneSettingsPage.switchToTab(driver4, 1);
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.enableRecordCallsSetting(driver4);
		
		//selecting unavailable flow at account level
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		dashboard.isPaceBarInvisible(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.unLockUnavailableFlow(driver4);
		accountIntelligentDialerTab.selectUnvailableFlowSetting(driver4);
		seleniumBase.idleWait(2);
		accountIntelligentDialerTab.selectdefaultLPFlowSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		accountOverviewtab.closeTab(driver4);
		accountOverviewtab.switchToTab(driver4, 1);
		
		// disabling call forwarding
		initializeDriverSoftphone("webSupportDriver");
		dashboard.switchToTab(webSupportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(webSupportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(webSupportDriver);

		//selecting unavailable flow at user level
		loginSupport(webSupportDriver);
		dashboard.clickOnUserProfile(webSupportDriver);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(webSupportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(webSupportDriver);
		seleniumBase.idleWait(2);
		userIntelligentDialerTab.selectNoUnvailableFlow(webSupportDriver);
		userIntelligentDialerTab.saveAcccountSettings(webSupportDriver);
		
		// undeleting user
		dashboard.openManageUsersPage(webSupportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(webSupportDriver, "Test test", "test@test.com", "005900000029wZ3AAI");
		if(usersPage.isUnDeleteBtnVisible(webSupportDriver)){
			usersPage.clickUnDeleteBtn(webSupportDriver);
		}
	}

	public void deleteContact() {
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		softPhoneContactsPage.addContactIfNotExist(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver1);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
			softPhoneCalling.isCallHoldButtonVisible(driver1);
			
			softPhoneCalling.pickupIncomingCall(driver2);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(driver2);
		}
	}
	
	public void deleteContact(WebDriver caller, WebDriver agentToDelete, String agentSmartNumber, String agentName) {
		softPhoneContactsPage.addContactIfNotExist(caller, agentSmartNumber, agentName);

		// Calling from Agent's SoftPhone
		softPhoneCalling.switchToTab(caller, 1);
		softPhoneCalling.hangupIfInActiveCall(caller);
		softPhoneCalling.switchToTab(agentToDelete, 1);
		softPhoneCalling.hangupIfInActiveCall(agentToDelete);
		
		softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
		softPhoneCalling.isCallHoldButtonVisible(caller);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(agentToDelete);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(agentToDelete);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(caller)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(caller);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(caller);
			reloadSoftphone(caller);
			softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
			softPhoneCalling.isCallHoldButtonVisible(caller);
			softPhoneCalling.pickupIncomingCall(agentToDelete);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(agentToDelete);
		}
		
		softPhoneCalling.hangupIfInActiveCall(caller);
		softPhoneCalling.hangupIfInActiveCall(agentToDelete);
	}
	
	public void addCallerMultiple(WebDriver caller, WebDriver agentToMultiple, String agentSmartNumber) {
		String contactFirstName = "Contact_Existing";

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(caller, agentSmartNumber);
		softPhoneCalling.isCallHoldButtonVisible(caller);
		
		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(agentToMultiple);
		softPhoneCalling.hangupActiveCall(caller);

		// Deleting contact if exist
		if (!callScreenPage.isCallerMultiple(caller) && callScreenPage.isCallerUnkonwn(caller)) {
			// add caller as Lead
			callScreenPage.addCallerAsLead(caller, "AutomationLead", CONFIG.getProperty("contact_account_name"));
		}

		if (!callScreenPage.isCallerMultiple(caller)) {
			// Update caller to be contact
			callScreenPage.clickOnUpdateDetailLink(caller);
			callScreenPage.addContactForExistingCaller(caller, contactFirstName,
					CONFIG.getProperty("contact_account_name"));
			reloadSoftphone(caller);

			// sync time from sfdc to ringdna
			seleniumBase.idleWait(30);

			softPhoneContactsPage.searchUntilContacIsMultiple(caller, agentSmartNumber);
		}
	}
	
}
