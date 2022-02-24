/**
 * 
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.conversationAIReact.ConversationDetailPage.actionsDropdownItems;

/**
 * @author admin
 *
 */
public class RestrictRecordingDownload extends SoftphoneBase{
	
	String recordingURl = null;
	String downloadPath = System.getenv("userprofile") + "\\Downloads";
	
	//create a converstaion ai for which we will ve verifying if download option is visible or not
	@BeforeClass(groups={"Regression"})
	public void createConversationAI() {
		
		//updating the driver used
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    // Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(agentDriver, CONFIG.getProperty("qa_user_1_number"));
	
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(agentDriver);
		seleniumBase.idleWait(70);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		// Verifying Recent Calls Detail
		callScreenPage.openCallerDetailPage(agentDriver);
		contactDetailPage.openRecentCallEntry(agentDriver, callSubject);
		recordingURl = sfTaskDetailPage.getCallRecordingUrl(agentDriver);
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
	    
	    driverUsed.put("agentDriver", false);
	    driverUsed.put("driver1", false);
		
	}
	
	@BeforeMethod(groups={"Regression"})
	public void enableRestrictDownloadSetting() {
		
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable call recording restrictions
	    loginSupport(driver4);
	    dashboard.clickAccountsLink(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    driverUsed.put("agentDriver", false);
	    driverUsed.put("driver1", false);
		
	}
	
	//Verify agent not able to download recordings when download setting set to -Admins only
	@Test(groups={"Regression"})
	public void call_recording_download_all_disable_agent(){
		System.out.println("Test case --call_recording_download_all_disable_agent-- started ");

		//updating the driver used
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
		
		//open Conversation AI page and clear all filters
	    loginSupport(agentDriver);
  		dashboard.clickConversationAI(agentDriver);
  		callTabReactPage.navigateToCallsPage(agentDriver);
  		callTabReactPage.clearAllFilters(agentDriver);
  		
  		//open first conversation AI and verify that download option should not be visible
  		callTabReactPage.openConversationDetails(agentDriver, 0);
  		assertFalse(conversationDetailPage.isActionDropDownItemVisible(agentDriver, actionsDropdownItems.DownloadFile));
  		
  		//close admin app and switch to softphone
  		seleniumBase.closeTab(agentDriver);
	  	seleniumBase.switchToTab(agentDriver, 1);
	  	
	  	//open a blank tab and access the recording and verify that download button is not visible
	  	seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver,seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl);
		assertFalse(callRecordingPage.isDownloadRecordingButtonVisible(agentDriver));
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
	    
	    //Setting driver used to false as this test case is pass
	    driverUsed.put("agentDriver", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify support user not able to download recordings when download setting set to -Admins only
	@Test(groups={"Regression"})
	public void call_recording_download_all_disable_support_user(){
		System.out.println("Test case --call_recording_download_all_disable_support_user-- started ");

		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);

	    //enable call recording restrictions
	    loginSupport(driver1);
		
		//open Conversation AI page and clear all filters
  		dashboard.clickConversationAI(driver1);
  		callTabReactPage.navigateToCallsPage(driver1);
  		callTabReactPage.clearAllFilters(driver1);
  		
  		//open first conversation AI and verify that its the same CAI created by agent and download option should not be visible
  		callTabReactPage.openConversationDetails(driver1, 0);
  		assertFalse(conversationDetailPage.isActionDropDownItemVisible(driver1, actionsDropdownItems.DownloadFile));
  		
  		//close admin app and switch to softphone
  		seleniumBase.closeTab(driver1);
	  	seleniumBase.switchToTab(driver1, 1);
	  	
	  	//open a blank tab and access the recording and verify that download button is not visible
	  	seleniumBase.openNewBlankTab(driver1);		
		seleniumBase.switchToTab(driver1,seleniumBase.getTabCount(driver1));
		driver1.get(recordingURl);
		assertFalse(callRecordingPage.isDownloadRecordingButtonVisible(driver1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify when allowed download recordings dropdown - Admins only  then admin type user able to download call recording
	@Test(groups={"Regression"})
	public void call_recording_download_all_disable_admin_user(){
		System.out.println("Test case --call_recording_download_all_disable_support_user-- started ");

		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	  	
	  	//open a blank tab and access the recording and verify that download button is not visible
	  	seleniumBase.openNewBlankTab(driver4);		
		seleniumBase.switchToTab(driver4,seleniumBase.getTabCount(driver4));
		driver4.get(recordingURl);
		assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(driver4));
		
		//download the recording and verify that recording is downloaded
		callRecordingPage.downloadRecording(driver4, 0);
		callRecordingPage.idleWait(3);
		assertTrue(callRecordingPage.isExtensionFileDownloaded(downloadPath, ".wav"));
		
		//switch to softphone app
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
	    //Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	}

	//Verify when allowed download recordings dropdown - All User (Deafult) then agent and support type user able to download call recording
	@Test(groups={"Regression"})
	public void call_recording_download_all_enable(){
		System.out.println("Test case --call_recording_download_all_disable_support_user-- started ");

		//updating the driver used
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    disableRestrictCallRecording();
	  	
	  	//open a blank tab and access the recording and verify that download button is visible
	  	seleniumBase.openNewBlankTab(agentDriver);		
		seleniumBase.switchToTab(agentDriver,seleniumBase.getTabCount(agentDriver));
		agentDriver.get(recordingURl);
		assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(agentDriver));
		
		//download the recording and verify it has been downloaded
		callRecordingPage.downloadRecording(agentDriver, 0);
		callRecordingPage.idleWait(3);
		assertTrue(callRecordingPage.isExtensionFileDownloaded(downloadPath, ".wav"));
		
		//close tab and switch to softphone
	    seleniumBase.closeTab(agentDriver);
	    seleniumBase.switchToTab(agentDriver, 1);
	    
	    //open a blank tab and access the recording and verify that download button is not visible
	  	seleniumBase.openNewBlankTab(driver1);		
		seleniumBase.switchToTab(driver1,seleniumBase.getTabCount(driver1));
		driver1.get(recordingURl);
		assertTrue(callRecordingPage.isDownloadRecordingButtonVisible(driver1));
		
		//download the recording and verify it has been downloaded
		callRecordingPage.downloadRecording(driver1, 0);
		callRecordingPage.idleWait(3);
		assertTrue(callRecordingPage.isExtensionFileDownloaded(downloadPath, ".wav"));
		
		//close tab and switch to softphone
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Setting driver used to false as this test case is pass
	    driverUsed.put("agentDriver", false);
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	}

	@AfterClass(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" }, alwaysRun = true)
	public void disableRestrictCallRecording() {
		
		//initialise drivers
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enaable call recording setting on users settings
	    loginSupport(driver4);
	    accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.disableRestrictCallRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		driverUsed.put("adminDriver", false);
	}
}