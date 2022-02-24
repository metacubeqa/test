/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Ordering;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class LoadingDialer extends SoftphoneBase{
	
	String workUserName		 = null;
	String workUserPassword  = null;
	WebDriver workUserdriver = null;
	
	@BeforeClass(groups = { "Regression", "MediumPriority"})
	public void beforeClass(){
		workUserName 	 = CONFIG.getProperty("qa_work_user_username");
		workUserPassword = CONFIG.getProperty("qa_work_user_password");
	}
	
	@Test(groups = { "MediumPriority" })
	public void navigate_with_work_user() {
		System.out.println("Test case --navigate_with_work_user()-- started ");

		// updating the driver used
		workUserdriver = getDriver();
		SFLP.softphoneLogin(workUserdriver, CONFIG.getProperty("qa_test_site_name"), workUserName, workUserPassword);

		// navigate To Messaging Tabs
		softPhoneMessagePage.clickMessageIcon(workUserdriver);
		softPhoneMessagePage.navigateToAllMessages(workUserdriver);
		softPhoneMessagePage.navigateToReadMessages(workUserdriver);
		softPhoneMessagePage.navigateToUnreadMessages(workUserdriver);

		// Navigate to Setting Page tabs
		softPhoneSettingsPage.clickSettingIcon(workUserdriver);
		softPhoneSettingsPage.navigateToVoicemailDropTab(workUserdriver);
		softPhoneSettingsPage.navigateToCustomGreetingTab(workUserdriver);
		softPhoneSettingsPage.clickAddNewCustomGreetingButton(workUserdriver);
		softPhoneSettingsPage.closeCustomGreetingDialogueBox(workUserdriver);
		seleniumBase.idleWait(2);

		// test case is pass
		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void load_more_my_calls_history() {
		System.out.println("Test case --lload_more_my_calls_history-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    
		//open call history page and load more records
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.verifyHistoryCountsEquals(driver1, 10);
	    softphoneCallHistoryPage.clickLoadMoreButton(driver1);
	    softphoneCallHistoryPage.verifyHistoryCountsGreaterThan(driver1, 10);
	    
	    //get the call history date list
	    ArrayList<Date> dateList = HelperFunctions.getDateListFromStringList(softphoneCallHistoryPage.getHistoryCallerTimeList(driver1), "dd/MM/yyyy, hh:mm:ss");
	    
	    //verify that the date list for call history entries should be ordered in descending order
	    Ordering.natural().reverse().isOrdered(dateList);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	 }
	
	@Test(groups = {"MediumPriority"})
	public void verify_general_setting_tool_tips() {
		System.out.println("Test case --verify_general_setting_tool_tips-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    
		//open call history page and load more records
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.verifyAllToolTips(driver1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);

		System.out.println("Test case is pass");
	 }
	
	@Test(groups={"MediumPriority"})
	  public void call_history_select_all_queue_enable()
	  {
		System.out.println("Test case --call_history_select_all_queue_enable -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //select all calls and verify mark as read button on my calls history
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    
	    //select all calls and verify mark as read button on queue calls history
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.clickGroupCallsLink(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void call_history_select_all_queue_disable()
	  {
		System.out.println("Test case --call_history_select_all_queue_disable -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    // open Support Page and enable hide group call history setting
	    loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
	 	accountIntelligentDialerTab.enableHideGroupCallHistory(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);
	    
	    //select all calls and verify mark as read button on my calls history
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToVoiceMailTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void call_history_select_unselect_record()
	  {
		System.out.println("Test case --call_history_select_unselect_record -- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //verify that mark as read button appears for selecting all or single record
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    assertTrue(softphoneCallHistoryPage.isAlertTabDisabled(driver1));
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.switchToMissedCallsTab(driver1);
	    softphoneCallHistoryPage.selectCallEntryByIndex(driver1, 1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	 
	    //verify that records are getting selected and unselected using select all check box
	    softphoneCallHistoryPage.switchToAllCallsTab(driver1);
	    softphoneCallHistoryPage.selectAllHistoryEntry(driver1);
	    softphoneCallHistoryPage.verifyAllRecordsAreSelected(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    softphoneCallHistoryPage.unselectAllHistoryEntry(driver1);
	    softphoneCallHistoryPage.verifyNoRecordsAreSelected(driver1);
	    assertFalse(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    
	    //select all call entry using dropdown
	    softphoneCallHistoryPage.selectCallHistoryByDropDown(driver1, "All");
	    softphoneCallHistoryPage.verifyAllRecordsAreSelected(driver1);
	    assertTrue(softphoneCallHistoryPage.isMarkSelectedCallsReadVisible(driver1));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups = { "MediumPriority"})
	public void delete_contact_Verify_error() {
		System.out.println("Test case --delete_contact_Verify_error-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String contactNumber = CONFIG.getProperty("qa_user_3_number");
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Deleting contact
		System.out.println("deleting contact");
		callScreenPage.deleteCallerObject(driver1);
		
		//
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		assertFalse(callScreenPage.verifyErrorPresent(driver1));
		
	    callToolsPanel.giveCallRatings(driver1, 5);
	    callToolsPanel.selectDisposition(driver1, 0);
		seleniumBase.idleWait(15);
		assertEquals(callScreenPage.getErrorText(driver1), "A Salesforce operation failed because the entity has been deleted.");
		softPhoneSettingsPage.closeErrorMessage(driver1);
		
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_contact_address_field_type() {
		System.out.println("Test case --verify_contact_address_field_type-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String contactNumber = CONFIG.getProperty("qa_user_3_number");
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Deleting contact
		System.out.println("deleting contact");
		callScreenPage.verifyCustomFieldsValueType(driver1, "Address (CreatedBy)","div");
		callScreenPage.verifyCustomFieldsValueType(driver1, "Mailing Address","div");

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_voicemail_greeting_page() {
		System.out.println("Test case --verify_voicemail_greeting_page-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		//navigate to voicemail Tab
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneSettingsPage.navigateToVoicemailDropTab(driver3);		

		//add a voicemail
		softPhoneSettingsPage.clickAddVoicemailButton(driver3);
		assertTrue(softPhoneSettingsPage.isVoiceMailRecordButtonEnable(driver3));
		
		//navigate to voicemail Tab
		softPhoneSettingsPage.closeVoicemailDialogueBox(driver3);
		softPhoneSettingsPage.navigateToCustomGreetingTab(driver3);		

		//add a voicemail
		softPhoneSettingsPage.clickAddNewCustomGreetingButton(driver3);
		assertTrue(softPhoneSettingsPage.isCustomGreetingRecordBtnEnable(driver3));
		
		softPhoneSettingsPage.closeCustomGreetingDialogueBox(driver3);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver3", false);
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void call_lead_after_details_update()
	{
		System.out.println("Test case --call_lead_after_details_update-- started ");
	
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_user_3_number"));
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));	
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
	    //Open caller detail page
	    callScreenPage.openCallerDetailPage(driver1);
	    
	    //click to call the lead number
	    contactDetailPage.clickContactPhoneLinkDetails(driver1, HelperFunctions.getNumberInRDNAFormat(contactNumber));
	    sfSearchPage.switchToExtension(driver1);

		//Change the lead Status
		callScreenPage.selectLeadStatus(driver1, CallScreenPage.LeadStatusType.Contacted);
		seleniumBase.idleWait(2);
		
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Contacted.toString());
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));	
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = {"Regression", "MediumPriority"})
	public void afterMethod(ITestResult result){
		if(result.getName().equals("navigate_with_work_user")){
			workUserdriver.quit();
			workUserdriver = null;
		}else if(result.getName().equals("call_history_select_all_queue_disable")){
		 	//open Support Page and disable hide group call history setting
			loginSupport(driver1);
		 	dashboard.clickAccountsLink(driver1);
		 	System.out.println("Account editor is opened ");
		 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		 	accountIntelligentDialerTab.disableHideGroupCallHistory(driver1);
		 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
		 	seleniumBase.closeTab(driver1);
		 	seleniumBase.switchToTab(driver1, 1);
		 	reloadSoftphone(driver1);
		}
		
	}
}
