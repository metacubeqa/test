/**
 * 
 */
package softphone.cases.softPhoneFunctional.settings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class AdditionalNumber extends SoftphoneBase{
	  
	  @Test(groups={"Sanity","Regression", "Product Sanity"})
	  public void calling_additional_number()
	  {
	    System.out.println("Test case --calling_additional_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String additionalNumber = softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 1);
	    
	    //Open Setting page and Verify additoional Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    assertTrue(additionalNumber.contains(softPhoneSettingsPage.getSelectedOutboundNumber(driver1)));
	    
	    //verify that additional number is appearing in outbound numbers dropdown
	    assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver1).contains(additionalNumber));
	    softPhoneCalling.verifyAdditionalNumIconSelected(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Verify that additional number appearing on additional Number bar is correct
	    System.out.println("verifying that call is going from outbound number");
	    assertTrue(additionalNumber.contains(callScreenPage.getOutboundNumber(driver1)));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //Verify that additional number appearing on receiver end
	    assertTrue(additionalNumber.contains(callScreenPage.getOutboundNumber(driver3)));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		// Making an outbound call from softphone
		System.out.println("agent calling back to additional number");
		softPhoneCalling.softphoneAgentCall(driver3, additionalNumber);

		// receiving call from app softphone
		System.out.println("first caller picking up the call");
		softPhoneCalling.pickupIncomingCall(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		seleniumBase.idleWait(5);
		
		//verify data for first Outbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallIsNotLocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1)," ");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	  }
	  
	@Test(groups = { "Sanity", "Regression"})
	public void calling_additional_local_presence_number() {
		System.out.println("Test case --calling_local_presence_number-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String localPresenceCode = CONFIG.getProperty("qa_user_3_number").substring(2, 5);

		// Select local presence number as outbound Number
		System.out.println("enabling local presence setting");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//Get local area number
		List<String> selectedLocalPresenceNumber = softPhoneSettingsPage.getLocalPresenceAdditionalNumbers(driver1, localPresenceCode);

		// Making an outbound call from softphone
		System.out.println("agent making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_3_number"));

		// receiving call from app softphone
		System.out.println("first caller picking up the call");
		softPhoneCalling.pickupIncomingCall(driver3);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// Verify that area code appearing on local presence tab is correct
		String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
		assertTrue( selectedLocalPresenceNumber.contains(localPresenceNumber));

		// hanging up with the second caller
		System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//verify data for first Outbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Disable Local Presence Setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify Add Smart Number Default Label to show in Softphone
	//Verify default number on softphone when no label added to number in admin
	@Test(groups={"Regression"})
	  public void add_default_number_label()
	  {
	    System.out.println("Test case --add_default_number_label-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String labelName = "label " + helperFunctions.GetCurrentDateTime();
	    
	    //Login to admin app and provide label to default number
	    loginSupport(driver3);
	    dashboard.clickOnUserProfile(driver3);
	    userIntelligentDialerTab.openIntelligentDialerTab(driver3);
	    String defaultNumber = HelperFunctions.getNumberInRDNAFormat(userIntelligentDialerTab.getDefaultNo(driver3));
	    userIntelligentDialerTab.clickDefaultSmartNo(driver3);
	    smartNumbersPage.enterLabel(driver3, labelName);
	    smartNumbersPage.clickSaveBtn(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    reloadSoftphone(driver3);
	    
	    //go to setting page and verify the label in outbound numbers tab
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    softPhoneSettingsPage.verifyAdditionalNumberLabel(driver3, defaultNumber, labelName + " (Default)");
	    
	    //Verify the label in outbound numbers dropdown
	    softPhoneCalling.getOutboundNumberFromDropDown(driver3, defaultNumber, labelName);
	    
	    //remove the label for the default number
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    smartNumbersPage.enterLabel(driver3, "");
	    smartNumbersPage.clickSaveBtn(driver3);
	    seleniumBase.closeTab(driver3);

	    //Switch to softphone
	    seleniumBase.switchToTab(driver3, 1);
	    reloadSoftphone(driver3);
	    
	    //go to setting page and verify the default label in outbound numbers tab
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    softPhoneSettingsPage.verifyAdditionalNumberLabel(driver3, defaultNumber, "Smart Number (Default)");
	    
	    //Verify there is no label in outbound numbers dropdown
	    softPhoneCalling.getOutboundNumberFromDropDown(driver3, defaultNumber, null);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	  }
	
	//Verify Add Smart Number Label for non default to show in Softphone
	@Test(groups={"Regression"})
	  public void add_additional_number_label()
	  {
	    System.out.println("Test case --add_default_number_label-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String labelName = "label " + helperFunctions.GetCurrentDateTime();
	    
	    //Go to admin app and provide label to additional number
	    loginSupport(driver3);
	    dashboard.clickOnUserProfile(driver3);
	    userIntelligentDialerTab.openIntelligentDialerTab(driver3);
	    String additionalNumber = HelperFunctions.getNumberInRDNAFormat(userIntelligentDialerTab.getAdditionalNumberList(driver3).get(0));
	    userIntelligentDialerTab.clickAdditionalSmartNo(driver3, 0);
	    String oldLabel = smartNumbersPage.getLabelName(driver3);
	    System.out.println(oldLabel);
	    smartNumbersPage.enterLabel(driver3, labelName);
	    smartNumbersPage.clickSaveBtn(driver3);
	    seleniumBase.switchToTab(driver3, 1);
	    reloadSoftphone(driver3);
	    
	    //verify the label for additional number on outbounds number tab
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    softPhoneSettingsPage.verifyAdditionalNumberLabel(driver3, additionalNumber, labelName);
	    
	    //verify the additional number label in outbound dropdown menu
	    softPhoneCalling.getOutboundNumberFromDropDown(driver3, additionalNumber, labelName);
	    
	    //change back the label to old label name
	    seleniumBase.switchToTab(driver3, seleniumBase.getTabCount(driver3));
	    smartNumbersPage.enterLabel(driver3, oldLabel);
	    smartNumbersPage.clickSaveBtn(driver3);
	    seleniumBase.closeTab(driver3);

	    seleniumBase.switchToTab(driver3, 1);
	    reloadSoftphone(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	  }
}
