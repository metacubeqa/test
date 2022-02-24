/**
 * @author Abhishek Gupta
 *
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.admin.AccountCallRecordingTab;
import utility.HelperFunctions;

public class CallRecordingInternational extends SoftphoneBase{
	
	//Verify that international calls not recorded when country unchecked under call recording by country table
	//Verify that international calls recorded when country checked under call recording by country table
	//Verify all countries calls recorded when call recording by Country OFF
	@Test(groups={"MediumPriority"})
	public void call_recording_country_code_disable(){
		System.out.println("Test case --call_recording_country_code_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true); 
	  		
	    //Selecting call recording override option as all for the account
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
	    //Checking country United states so that its call recording will be recorded, All other countries call recording are unchceked
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "United Kingdom");
		accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
		
		 //unchecking play outbound Recording setting for Japan in country granular control setting
		accountCallRecordingTab.expandCountyContinent(driver4, "Asia");
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "Japan");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    	
	    String contactNumber = CONFIG.getProperty("qa_user_2_Japan_number");

		// Calling from Agent's SoftPhone to Japan number
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//verify that the call recording icon is green
		callScreenPage.verifyRecordingisInactive(driver1);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying that call recording URL is not created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		// Calling from Agent's SoftPhone to UK number
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_uk_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Verify the Call Recording icon is red
		callScreenPage.verifyRecordingisActive(driver1);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //disable call recording by country granular setting
	    seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
	    accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
	    //reload the page
	    reloadSoftphone(driver1);

 		// Calling from Agent's SoftPhone to Japan number
 		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
 		softPhoneCalling.pickupIncomingCall(driver4);
 		
 		//verify that the call recording  is inactive
 		callScreenPage.verifyRecordingisInactive(driver1);
 		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
 		
 		// hanging up with caller 1
 		System.out.println("hanging up with caller 1");
 		softPhoneCalling.hangupActiveCall(driver4);
 		System.out.println("Call is removing from softphone");
 		softPhoneCalling.isCallBackButtonVisible(driver1);

 		// Verifying that call recording URL is created in salesforce for above call
 		callScreenPage.openCallerDetailPage(driver1);
 		contactDetailPage.openRecentCallEntry(driver1, callSubject);
 	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
 	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
 	    sfTaskDetailPage.isCallRecordingURLInvisible(driver1);;
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);
 	  
 	   //Verify that play icon is not visible for call activities
	  	softPhoneActivityPage.isTaskVisibleInActivityList(driver1, callSubject);
	  	assertFalse(softPhoneActivityPage.isRecordingPlayBtnVisible(driver1, callSubject));
 	    
	    //Setting Call recording setting to default for the account
 	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}

	//Verify call recording when additional number 0f international (Australia) have selected in softphone but default area US code of user not checked .
	@Test(groups={"MediumPriority"})
	public void call_recording_internation_additional_area_code_checked(){
		System.out.println("Test case --call_recording_internation_additional_area_code_checked-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Defining different values for the test
	    String defaultNumberState = "California";
	    String additionalState = "United Kingdom";
	    String additionalNumber = CONFIG.getProperty("qa_user_2_uk_number");
	    String contactNumber = CONFIG.getProperty("qa_user_1_number");
	    
	    //Checking country United Kingdom from granular control table
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, additionalState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//unchecking US area from granular control table for the default number area code
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//reload the page
		reloadSoftphone(driver4);
		reloadSoftphone(driver1);
		
		//selecting UK number as additional number for the call
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		int index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver4, additionalNumber);
		softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver4, index);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver4).contains(HelperFunctions.getNumberInSimpleFormat(additionalNumber)));
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, contactNumber);
		softPhoneCalling.isCallHoldButtonVisible(driver4);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// verify call recording icon is red
		callScreenPage.verifyRecordingisActive(driver4);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver4);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		//Verifying that call recording URL is created in salesforce for above call
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openRecentCallEntry(driver4, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		sfTaskDetailPage.getCallRecordingUrl(driver4).contains("recordings");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// verify play icon is there for the newly created entry
		assertTrue(softPhoneActivityPage.isPlayRecordingBtnVisible(driver4, callSubject));
	 	
		//Setting default values for Account Call Recording
		loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, defaultNumberState);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		softPhoneSettingsPage.clickSettingIcon(driver4);
		softPhoneSettingsPage.clickOutboundNumbersTab(driver4);
		softPhoneSettingsPage.selectDefaultNumber(driver4);
		reloadSoftphone(driver4);

		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	}
	
	//Verify announcement not audible when "Outbound Call Recording Announcement -ON" and  Country's Play Outbound Recording-OFF
	@Test(groups={"MediumPriority"})
	public void call_recording_country_anouncement_disable(){
		System.out.println("Test case --call_recording_country_anouncement_disable-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	    
	    //Enabling Outbound Call Recording Announcement setting and Recording override type to All
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableOutboundCallRecordingAnnoncement(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.All);
		accountCallRecordingTab.lockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.idleWait(2);
		
	    //unchecking play outbound Recording setting for Japan in country granular control setting
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Asia");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "Japan");
		accountCallRecordingTab.uncheckAreasForPlayOutboundRecording(driver4, "Japan");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.switchToTab(driver4, 1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    //reload the page
	    reloadSoftphone(driver1);
	    	
	    String contactNumber = CONFIG.getProperty("qa_user_2_Japan_number");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//verify that recording icon is red and announcement icon is present
		callScreenPage.verifyRecordingisActive(driver1);
		callScreenPage.verifyRecordingAnouncementIconPresent(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
 	    
	    //Setting default Call Recordings settings for the account
 	   	loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.idleWait(2);
		accountCallRecordingTab.swtichToInternationalTab(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Asia");
		accountCallRecordingTab.uncheckAreasForCallRecording(driver4, "Japan");
		accountCallRecordingTab.uncheckAreasForPlayOutboundRecording(driver4, "Japan");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//Verify Support user should have read only International tab screen
	@Test(groups={"Regression"})
	public void call_recording_disabled_international_page(){
		System.out.println("Test case --call_recording_disabled_international_page-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true); 
	    
	    //enabled granular control setting for Country from admin user
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		//Login to support user and verify that the International tab is disabled for the user
		loginSupport(driver1);
		accountCallRecordingTab.openCallRecordingTab(driver1);
		accountCallRecordingTab.swtichToInternationalTab(driver1);
		accountCallRecordingTab.verifyInterPageToggleButtonsDisabled(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
 	    
	    //Setting default Call Recording Settings for the account
		accountCallRecordingTab.swtichToOverviewTab(driver4);
		accountCallRecordingTab.selectCallRecordingOverrideType(driver4, AccountCallRecordingTab.CallRecordingOverrideOptions.None);
		accountCallRecordingTab.unlockRecordingSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);

		//close the tab
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" })
	public void disableCountryGranularControlSetting(ITestResult result) {

		if(result.getStatus() == 2 || result.getStatus() == 3) {
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver4");
			driverUsed.put("driver4", true);
	
			//Setting default Call Recording Settings for the account
	 	    loginSupport(driver4);
			accountCallRecordingTab.openCallRecordingTab(driver4);
			accountCallRecordingTab.enableCallRecordingSetting(driver4);
			accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
			
			accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
			accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	
			// close the tab
			seleniumBase.closeTab(driver4);
			seleniumBase.switchToTab(driver4, 1);
			seleniumBase.switchToTab(driver1, 1);
	
			// reload the page
			reloadSoftphone(driver4);
			reloadSoftphone(driver1);
	
			// Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);
			driverUsed.put("driver4", false);
		}
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Sanity", "MediumPriority" }, alwaysRun = true)
	public void enableRecording() {
		
		//initialise drivers
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enaable call recording setting on users settings
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
	    accountCallRecordingTab.enableCallRecordingSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		accountCallRecordingTab.swtichToUnitedStateTab(driver4);
		accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "California");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		
		driverUsed.put("adminDriver", false);
	}
}