/**
 * 
 */
package softphone.cases.softPhoneFunctional.settings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class OutboundNumber extends SoftphoneBase{
	  
	  @Test(groups={"Sanity", "Regression", "Product Sanity"})
	  public void calling_outbound_number()
	  {
	    System.out.println("Test case --calling_outbound_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String outboundNumber = CONFIG.getProperty("prod_user_1_number");
	    
	    //Configuring driver 2's number as outbound number
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver2, outboundNumber, "Prod outbound Number");
	    
	    //verifying that number has been configured as outbound number
	    int addedOutboundNumberIndex = softPhoneSettingsPage.isNumberExistInOutboundNumber(driver1, outboundNumber);
	    assertTrue(addedOutboundNumberIndex >=0);
	    
	    //Selecting the addedOutboundNumber
	    softPhoneSettingsPage.selectOutboundNumberUsingIndex(driver1, addedOutboundNumberIndex);
	    
	    //Open Setting page and Verify outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    assertTrue(outboundNumber.contains(softPhoneSettingsPage.getSelectedOutboundNumber(driver1)));
	    assertTrue(outboundNumber.contains(softPhoneCalling.getOutboundNumberFromDropdown(driver1)));
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
	    //Verify that outbound number appearing on Outbound Number tab is correct
	    System.out.println("verifying that call is going from outbound number");
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver1)));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //Verify that outbound number appearing on receiver end
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver3)));
	    
		//Making second outbound call from softphone
	    System.out.println("agent making call to second caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		//receiving call from app softphone
	    System.out.println("second caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver4);
	    
	    //Verify that outbound number appearing on Outbound Number tab is correct
	    System.out.println("verifying that call is going from outbound number");
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver1)));
	    
	    //Verify that outbound number appearing on receiver end
	    assertTrue(outboundNumber.contains(callScreenPage.getOutboundNumber(driver4)));
	    
	    //click mute button
		softPhoneCalling.clickMuteButton(driver1);
		softPhoneCalling.isUnMuteButtonVisible(driver1);
	    
	    //resuming first call
	    softPhoneCalling.clickOnHoldButton(driver1);
	    softPhoneCalling.clickResumeButton(driver1);
	    
	    //checking that  caller number  is visible
	  	assertTrue(CONFIG.getProperty("qa_user_3_number").contains(HelperFunctions.getNumberInSimpleFormat(callScreenPage.getCallerNumber(driver1))),"call is not resumed");
	    
	  	//verify mute button is enable
	  	assertTrue(softPhoneCalling.isMuteButtonEnables(driver1));
	  	
	  	
		//hanging up with caller 1
	    System.out.println("hanging up with caller 1");
	    softPhoneCalling.hangupActiveCall(driver3);
	    
		//hanging up with the second caller
	    System.out.println("hanging up with the second caller");
	    softPhoneCalling.hangupActiveCall(driver4);
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    softPhoneSettingsPage.deleteOutboundNumberIfExist(driver1, outboundNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	  
	  @Test(groups={"Regression", "MediumPriority"})
	  public void verify_duplicate_outbound_number()
	  {
	    System.out.println("Test case --verify_duplicate_outbound_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String outboundNumber = CONFIG.getProperty("prod_user_1_number");
	    
	    //Configuring driver 2's number as outbound number
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver2, outboundNumber, "Prod outbound Number");
	    
	    //verifying that number has been configured as outbound number
	    int addedOutboundNumberIndex = softPhoneSettingsPage.isNumberExistInOutboundNumber(driver1, outboundNumber);
	    assertTrue(addedOutboundNumberIndex >=0);
	    
	    //Selecting the addedOutboundNumber
	    softPhoneSettingsPage.selectOutboundNumberUsingIndex(driver1, addedOutboundNumberIndex);
	    
	    //verify that user is able to select local presence number from droppdown.
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    assertTrue(softPhoneCalling.isLocalPresenceSelected(driver1));
	    
	    //Verify that duplicate error message appears
	    softPhoneSettingsPage.enterOutboundNumberDetails(driver1, driver2, outboundNumber, "Prod outbound Number");
	    softPhoneSettingsPage.verifyOutboundMessagPresent(driver1, "Phone number is already verified.");
	    
	    //deleting added Outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    softPhoneSettingsPage.deleteOutboundNumberIfExist(driver1, outboundNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	  
	//in this test case we are verifying add/remove groups with outbound conference calling  
	 @Test(groups={"Regression"})
	 public void verify_outbound_number_dropdown_on_softphone()
	 {
	    System.out.println("Test case --verify_outbound_number_dropdown_on_softphone()-- started ");
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    
	    //navigate To Messaging Tabs
		softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
	    //verifying outbound dropdown from Calls History page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
	    //verifying outbound dropdown from Team page
	    softPhoneTeamPage.openTeamSection(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
	    //verifying outbound dropdown from Contact page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
		softPhoneContactsPage.ringDNASearch(driver1, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
	    //Navigate to Setting Page tabs
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	 
	 @Test(groups={"MediumPriority"})
	 public void verify_outbound_number_dropdown_while_on_call()
	 {
	    System.out.println("Test case --verify_outbound_number_dropdown_while_on_call()-- started ");
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
		//Making a call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//put call on hold
		softPhoneCalling.putActiveCallOnHold(driver1);
		
		//open recent call entry from call history
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneCalling.isOutboundDrowdownVisible(driver1);
	    
	    //Click on call back button
	    callToolsPanel.clickRelatedRecordsIcon(driver1);
	    callToolsPanel.clickRelatedRecordsIcon(driver1);
	    softPhoneCalling.clickCallBackButton(driver1);
	    softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
	    
	    //hang up active
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	   
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	 
	 @Test(groups={"Regression", "MediumPriority"})
	  public void select_outbound_number_from_dropdown()
	  {
	    System.out.println("Test case --select_outbound_number_from_dropdown-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String outboundNumber = CONFIG.getProperty("prod_user_1_number");
	    String outnoundNumberLabel = "Prod outbound Number";
	    
	    //Configuring driver 2's number as outbound number
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver2, outboundNumber, outnoundNumberLabel);
	    reloadSoftphone(driver1);
	    
	    //verify that added outbound number exist and select dropdown from the dropdown
	    assertTrue(softPhoneCalling.isOutboundNumExistInDropdown(driver1, outnoundNumberLabel));
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, outnoundNumberLabel);
	    
	    //Open Setting page and Verify outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    assertTrue(outboundNumber.contains(softPhoneSettingsPage.getSelectedOutboundNumber(driver1)));
	    assertTrue(outboundNumber.contains(softPhoneCalling.getOutboundNumberFromDropdown(driver1)));
	    
	    //Verify selected outbound number
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    assertTrue(outboundNumber.contains(softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(driver1)));
	    
	    //open outbound number tab
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    
	    //select some other outbound number from the list
	    String otherOutboundNumber = softPhoneSettingsPage.selectLastOutboundNumber(driver1);
	    
	    //delete the previously selected outbound number
	    softPhoneSettingsPage.deleteOutboundNumber(driver1, outboundNumber);
	    
	    //verify that after other outbound number is deleted the number is still selected in dropdown
	    assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver1).contains(otherOutboundNumber));
	    assertTrue(otherOutboundNumber.contains(softPhoneSettingsPage.getSelectedOutboundNumber(driver1)));
	    
	    //verify that deleted outbound number doesn't exist in the dropdown
	    reloadSoftphone(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    assertFalse(softPhoneCalling.isOutboundNumExistInDropdown(driver1, outnoundNumberLabel));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void enable_local_presence_select_outbound_number()
	  {
	    System.out.println("Test case --enable_local_presence_select_outbound_number-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    softPhoneSettingsPage.idleWait(2);
	    assertEquals(softPhoneCalling.getSelectedOutboundNumber(driver1), "Localpresence");
	    
	    //select dropdown from the dropdown
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    String outboundNumber = softPhoneSettingsPage.selectLastOutboundNumberAndGetNumber(driver1);
	    softPhoneSettingsPage.idleWait(2);
	    assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver1).contains(outboundNumber));
	    
	    //Open Setting page and Verify outbound Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    assertFalse(softPhoneSettingsPage.isLocalPresenceEnable(driver1));
	    
	    //delete outbound number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	  }
	 
	 @Test(groups={"MediumPriority"})
	 public void verify_outbound_number_incoming_calls()
	 {
	    System.out.println("Test case --verify_outbound_number_incoming_calls()-- started ");
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
	    String outboundNumber = CONFIG.getProperty("prod_user_1_number");
	    
		//Configuring driver 2's number as outbound number and select it
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver2, outboundNumber, "Prod outbound Number");
	    int addedOutboundNumberIndex = softPhoneSettingsPage.isNumberExistInOutboundNumber(driver1, outboundNumber);
	    assertTrue(addedOutboundNumberIndex >=0);
	    softPhoneSettingsPage.selectOutboundNumberUsingIndex(driver1, addedOutboundNumberIndex);
		
		//After selecting outbound verified number taking an incoming call and verifying that outbound number bar is not visible 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.isDeclineButtonVisible(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.pickupIncomingCall(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.hangupActiveCall(driver3);
		assertTrue(softPhoneCalling.getSelectedOutboundNumber(driver1).contains(HelperFunctions.getNumberInSimpleFormat(outboundNumber)));
		
		//After selecting Local Presence taking an incoming call and verifying that outbound number bar is not visible 
	    System.out.println("enabling local presence setting");
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, "Local presence");
	    
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.isDeclineButtonVisible(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.pickupIncomingCall(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.hangupActiveCall(driver3);
		
		//After selecting additional number taking an incoming call and verifying that outbound number bar is not visible 
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, "Automation");
	    
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.isDeclineButtonVisible(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.pickupIncomingCall(driver1);
		assertFalse(callScreenPage.isOutboundBarVisible(driver1));
		softPhoneCalling.hangupActiveCall(driver3);

		//Deleting added outbound verified number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    softPhoneSettingsPage.deleteOutboundNumberIfExist(driver1, outboundNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	 
	 @Test(groups={"MediumPriority"})
	 public void verify_outbound_number_icons()
	 {
	    System.out.println("Test case --verify_outbound_number_icons()-- started ");
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
	    String outboundNumber = CONFIG.getProperty("prod_user_2_number");
	    
		//Configuring driver 2's number as outbound number and select it
	    softPhoneSettingsPage.addOutboundNumber(driver1, driver5, outboundNumber, "Prod outbound Number");
	    reloadSoftphone(driver1);

		//After selecting outbound verified number verifying the icon and making a call and verifying that outbound number dropdown is not visible 
	    System.out.println("enabling local presence setting");
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, "Prod outbound Number");
	    softPhoneCalling.verifyOutboundNumIconSelected(driver1);

		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.isDeclineButtonVisible(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		
		//After selecting Local Presence making a call verifying the icon and making a call and verifying that outbound number dropdown is not visible
	    System.out.println("enabling local presence setting");
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, "Local presence");
	    softPhoneCalling.verifyLocalPresenceNumIconSelected(driver1);
	    
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.isDeclineButtonVisible(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		
		//After selecting additional number making a call verifying the icon and making a call and verifying that outbound number dropdown is not visible
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneCalling.selectOutboundNumFromDropdown(driver1, "Automation");
	    seleniumBase.idleWait(2);
	    String additionalNumber = softPhoneCalling.getSelectedOutboundNumber(driver1);
	    softPhoneCalling.verifyAdditionalNumIconSelected(driver1);
	    
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.isDeclineButtonVisible(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.verifyOutboundDrowdownNotVisible(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		
	    //Open Setting page and Verify additional Number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    assertTrue(additionalNumber.contains(softPhoneSettingsPage.getSelectedOutboundNumber(driver1)));
	    assertTrue(additionalNumber.contains(softPhoneSettingsPage.getSelectedOutBoundNumberOnSoftPhone(driver1)));

		//deleting outbound number
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectFirstAdditionalNumberAsDefault(driver1);
	    softPhoneSettingsPage.deleteOutboundNumberIfExist(driver1, outboundNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	  
	@AfterClass(groups = { "Regression", "Sanity", "QuickSanity", "ExludeForProd", "Product Sanity"})
	public void resetSoftphoneSettings() {

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);
		
		// updating the driver used
		driverUsed.put("driver1", false);
	}
}