/**
 * 
 */
package softphone.cases.softPhoneFunctional.settings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class LocalPresence extends SoftphoneBase{
	
	 @BeforeClass(groups = { "Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity" })
	 public void beforeClass() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);

		// updating the driver used
		driverUsed.put("driver1", false);
	 }
	  
	 @Test(groups={"Sanity", "Regression", "Product Sanity"})
	  public void calling_local_presence_number()
	  {
	    System.out.println("Test case --calling_local_presence_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber1 = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(CONFIG.getProperty("qa_user_2_number").substring(2, 5) ,localPresenceNumber1.substring(0,3));
	    
		//Making second outbound call from softphone
	    System.out.println("agent making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String localPresenceNumber2 = callScreenPage.getOutboundNumber(driver1);
	  	assertEquals(CONFIG.getProperty("prod_user_1_number").substring(2, 5) ,localPresenceNumber2.substring(0,3));
	  	
	    //resuming first call
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_2_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	  	
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);

	    //hanging up with the second caller
	    System.out.println("hanging up with the second caller");
		softPhoneCalling.hangupActiveCall(driver4);
   
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for first Outbound call
	    reloadSoftphone(driver1);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber1));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify data for second Outbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber2));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 
	    //Verifying call back on local presence number functionality
		//taking incoming call
	    System.out.println("Taking call from twilio");
	    softPhoneCalling.softphoneAgentCall(driver4, localPresenceNumber1);
	    
		//receiving call
	    System.out.println("Picking up call from softphone");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    assertFalse(callScreenPage.isOutboundBarVisible(driver1));
	    seleniumBase.idleWait(10);
	  	 
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(5);
	    
	    //verify data for inbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1)," ");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void local_presence_number_global()
	  {
	    System.out.println("Test case --local_presence_number_global-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(CONFIG.getProperty("prod_user_1_number").substring(2, 5) ,localPresenceNumber.substring(0,3));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for local presence call
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
	    
		// navigating to support page
	    loginSupport(driver1);
		
		dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, localPresenceNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, localPresenceNumber);
		assertTrue(smartNoIndex >= 0);
		assertEquals(smartNumbersPage.getTypeFromTable(driver1, smartNoIndex), SmartNumbersPage.smartNumberType.LocalPresence.toString());
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 
	    //Verifying call back on local presence number functionality
		//taking incoming call
	    System.out.println("Taking call from twilio");
	    softPhoneCalling.softphoneAgentCall(driver2, localPresenceNumber);
	    
		//receiving call
	    System.out.println("Picking up call from softphone");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    assertFalse(callScreenPage.isOutboundBarVisible(driver1));
	    seleniumBase.idleWait(3);
	    callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	  	 
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for inbound call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1)," ");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void local_presence_number_user_verified()
	  {
	    System.out.println("Test case --local_presence_number_user_verified-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String areaCode = CONFIG.getProperty("prod_user_2_number").substring(2, 5);
	    List<String> outboundLocalPresenceNumbers = softPhoneSettingsPage.getLocalPresenceOutboundNumbers(driver1, areaCode);
	    
		// navigating to support page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountLocalPresenceTab.openlocalPresenceTab(driver1);
		accountLocalPresenceTab.enableUserVerifiedNumberSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
		seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver5);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(localPresenceNumber.substring(0,3),areaCode);
	    assertTrue(outboundLocalPresenceNumbers.contains(localPresenceNumber));
	    assertTrue(outboundLocalPresenceNumbers.contains(callScreenPage.getCallerNumber(driver5)));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver5);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for local presence call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void local_presence_number_neighboring_area()
	  {
	    System.out.println("Test case --local_presence_number_neighboring_area-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String areaCode = "575";

		// navigating to support page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountLocalPresenceTab.openlocalPresenceTab(driver1);
		accountLocalPresenceTab.disableUserVerifiedNumberSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
		seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver5);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertEquals(areaCode ,localPresenceNumber.substring(0,3));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver5);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for local presence call
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    //assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verifying call back on local presence number functionality
		// taking incoming call
		System.out.println("Taking call from twilio");
		softPhoneCalling.softphoneAgentCall(driver5, localPresenceNumber);

		// receiving call
		System.out.println("Picking up call from softphone");
		softPhoneCalling.pickupIncomingCall(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		seleniumBase.idleWait(3);
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver5);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallNotAbandoned(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		//assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
		assertEquals(sfTaskDetailPage.getLocalPresenceNumber(driver1), " ");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void disable_local_presence_Setting()
	  {
	    System.out.println("Test case --disable_local_presence_Setting-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
		// navigating to support page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountLocalPresenceTab.openlocalPresenceTab(driver1);
		accountLocalPresenceTab.disableLocalPresenceSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verifying that local presence setting is not visible on settings page
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyLocalPresenctOptionNotPresent(driver1);

		// navigating to support page
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountLocalPresenceTab.enableLocalPresenceSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
		seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	
	    //verifying that local presence setting is visible on settings page
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"MediumPriority"})
	  public void calling_international_local_presence_number()
	  {
	    System.out.println("Test case --calling_international_local_presence_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable international call recordings.
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "United Kingdom");
		accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    reloadSoftphone(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_uk_number"));
	    
		//receiving call from app softphone
	    System.out.println("caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	  	assertEquals(localPresenceNumber.substring(0,4), CONFIG.getProperty("qa_user_3_uk_number").substring(1, 5));
	  	
	  	//add caller as contact if unknown
	  	if (callScreenPage.isCallerUnkonwn(driver1)) {
	  		callScreenPage.addCallerAsContact(driver1, "Ashish Ringdna UK", CONFIG.getProperty("contact_account_name"));
		}
	  
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for first Outbound call
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
	 
	    //Verifying call back on local presence number functionality
		//taking incoming call
	    System.out.println("Taking call from twilio");
	    softPhoneCalling.softphoneAgentCall(driver3, localPresenceNumber);
	    
		//receiving call
	    System.out.println("Picking up call from softphone");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    assertFalse(callScreenPage.isOutboundBarVisible(driver1));
	  	 
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //disable international call recordings.
	    accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	 //@Test(groups={"MediumPriority"})
	  public void calling_international_neighbour_area_code()
	  {
	    System.out.println("Test case --calling_international_local_presence_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //enable international call recordings.
	    loginSupport(driver4);
		accountCallRecordingTab.openCallRecordingTab(driver4);
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, "United Kingdom");
		accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    reloadSoftphone(driver1);
	    
	    //delete the smart number
	    loginSupport(driver1);
	  	dashboard.openSmartNumbersTab(driver1);
	  	smartNumbersPage.searchSmartNumber(driver1, "+441342779014");
	  	int smartNoIndex = smartNumbersPage.getAllSmartNumbersIndex(driver1, "+441342779014");
	  	smartNumbersPage.clickSmartNoByIndex(driver1, smartNoIndex);
	  	if(smartNumbersPage.isdeleteSmartNumberBtnVisible(driver1)){
	  		smartNumbersPage.deleteSmartNumber(driver1);
	  	}
		seleniumBase.switchToTab(driver1, 1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_uk_number"));
	    
		//receiving call from app softphone
	    System.out.println("caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	  	assertEquals("4412" ,localPresenceNumber.substring(0,4));
	  
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //verify data for the call
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	 
	    //Verifying call back on local presence number functionality
		//taking incoming call
	    System.out.println("Taking call from twilio");
	    softPhoneCalling.softphoneAgentCall(driver3, localPresenceNumber);
	    
		//receiving call
	    System.out.println("Picking up call from softphone");
	    softPhoneCalling.pickupIncomingCall(driver1);
	    assertFalse(callScreenPage.isOutboundBarVisible(driver1));
	  	 
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //Un deleting the number
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    smartNumbersPage.clickUndeleteBtn(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//disable international call recording setting on users settings
	    accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void local_presence_user_number()
	  {
	    System.out.println("Test case --local_presence_user_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver6");
	    driverUsed.put("driver6", true);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_no_lp_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver6);
	    
	    //Verify that area code appearing on local presence tab is correct
	    String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(localPresenceNumber));
	    
	    //Verify that area code appearing on local presence tab is correct
	    String CallerNumber = callScreenPage.getCallerNumber(driver6);
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(CallerNumber));
	    
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver6);

		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	@Test(groups = { "MediumPriority" })
	public void verify_tooltip_when_select_lp_number() {

		System.out.println("Test case --verify_tooltip_when_select_lp_number-- started");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//verify that local presence setting is not present
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.verifyLocalPresenceSettingNotPresent(driver1);

		//enable the local presence setting
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
		
		//open latest message
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);

		// selecting first number in number selector
		String message = "AutoSMSMSg".concat(HelperFunctions.GetRandomString(3));
		softPhoneActivityPage.idleWait(2);
		softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.idleWait(2);

		//verify tool tip on message send button when local presence number is selected.
		String tooltipText = softPhoneActivityPage.getToolTipWarningOnSendbtn(driver1);
		assertTrue(tooltipText.contains("Your outbound number is set to Local Presence. Your previous messages we're sent using"));
		softPhoneActivityPage.clickToolTipDismiss(driver1);

		// updating the driver used
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	

	@AfterClass(groups = { "Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity" })
	public void afterClass() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// Disable Local Presence Setting
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

		// updating the driver used
		driverUsed.put("driver1", false);
	}
}
