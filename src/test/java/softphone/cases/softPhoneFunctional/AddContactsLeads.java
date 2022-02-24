/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class AddContactsLeads extends SoftphoneBase{
	
	@Test(groups={"QuickSanity", "Sanity", "Regression", "MediumPriority", "Product Sanity"}, priority = 1)
	  public void add_contact_verify_task_data_in_salesforce()
	  {
		System.out.println("Test case --add_contact_verify_task_data_in_salesforce-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");
	    String fromNumber	 = CONFIG.getProperty("qa_user_1_number");
	   								  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
	    //Adding Call Notes to Caller
	    String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
	    String relatedTask = CONFIG.getProperty("softphone_task_related_opportunity");
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),relatedTask);
	    callToolsPanel.giveCallRatings(driver1, 5);
	    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
	    reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Call Report Test Notes");
	        
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
		//Opening the caller detail page
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	
		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying Recent Calls Detail
	    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
	    assertEquals(sfTaskDetailPage.getRating(driver1), "5");
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), relatedTask);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
	    assertTrue(contactNumber.contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(fromNumber.contains(sfTaskDetailPage.getFromNumber(driver1)));
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
	  
	  @Test(groups={"QuickSanity", "Regression"}, priority = 2)
	  public void add_lead_verify_task_data_in_salesforce()
	  {
		System.out.println("Test case --add_lead_verify_task_data_in_salesforce-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));	
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), "Open");
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //open recent call entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
	    //Adding Call Notes to Caller
	    String callNotes = "Call Notes on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    callToolsPanel.enterCallNotes(driver1, callNotes, callNotes);
	    String relatedTask = CONFIG.getProperty("softphone_task_related_opportunity");
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),relatedTask);
	    callToolsPanel.giveCallRatings(driver1, 5);
	    String callDisposition = callToolsPanel.selectDisposition(driver1, 0);
	    reportThisCallPage.giveCallReport(driver1, 3, "Audio Latency", "Call Report Test Notes");
	   
		//Opening the caller detail page
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		assertEquals(contactDetailPage.getLeadStatus(driver1), "Open");
		assertTrue(contactDetailPage.getLeadDescription(driver1).contains("Created by RingDNA"));
	
		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//Verifying Recent Calls Detail
	    assertEquals(sfTaskDetailPage.getComments(driver1), callNotes);
	    assertEquals(sfTaskDetailPage.getRating(driver1), "5");
	    assertEquals(sfTaskDetailPage.getDisposition(driver1), callDisposition);
	    assertEquals(sfTaskDetailPage.getTaskRelatedTo(driver1), relatedTask);
	    sfTaskDetailPage.verifyCallNotAbandoned(driver1);
	    sfTaskDetailPage.verifyCallStatus(driver1, "Connected"); 
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

	@Test(groups = { "Regression" }, priority = 5)
	public void add_from_queue_to_new_lead() {
		System.out.println("Test case --add_from_queue_to_new_lead-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String contactFirstName = CONFIG.getProperty("prod_user_3_name");
		String contactName = contactFirstName + " Automation";
		
		// Adding existing contact to multiple contact caller.		
	    String acdQueueName = CONFIG.getProperty("qa_group_1_name").trim();
		
	    //Unsubscribe and subscribe acd queue from user 1
	    softPhoneCallQueues.unSubscribeAllQueues(driver1);
	    softPhoneCallQueues.subscribeQueue(driver1, acdQueueName);
	    softPhoneCallQueues.isQueueSubscribed(driver1, acdQueueName);

		// calling to queue
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_group_1_number"));
		
		// pickup and hangup the call
	    softPhoneCallQueues.pickCallFromQueue(driver1);
		softPhoneCalling.hangupActiveCall(driver4);
		seleniumBase.idleWait(3);
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);

		// Update caller to be contact
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.addCallerToMultipleLead(driver1, contactFirstName, CONFIG.getProperty("contact_account_name"));
		seleniumBase.idleWait(25);
		reloadSoftphone(driver1);
		
		softphoneCallHistoryPage.openRecentGroupCallEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactName, contactDetailPage.getCallerName(driver1));
		assertEquals(CONFIG.getProperty("contact_account_name"), contactDetailPage.getCallerCompanyName(driver1));
		assertTrue(CONFIG.getProperty("qa_user_2_number").contains(contactDetailPage.getCallerPhone(driver1)));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// search the contact with number
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// verify that contact is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	  
	@Test(groups = {"Sanity", "Regression", "Product Sanity"}, priority = 6)
	public void add_number_to_existing_contact() {
		System.out.println("Test case --add_number_to_existing_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String existingContact;
		String contactNumber = CONFIG.getProperty("prod_user_1_number");

		softPhoneContactsPage.addContactIfNotExist(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));
		existingContact = softPhoneContactsPage.getSfdcContactsResultNames(driver1).get(0).getText();
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
			softPhoneCalling.pickupIncomingCall(driver3);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(driver3);
		}

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		seleniumBase.idleWait(3);
		
		callScreenPage.addCallerToExistingContact(driver1, existingContact);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		seleniumBase.idleWait(25);
		reloadSoftphone(driver1);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		 //verify detail in salesforce
	    callScreenPage.openCallerDetailPage(driver1);
	    assertEquals(contactDetailPage.getCallerName(driver1), existingContact);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Add unknown caller as Contact during Inbound call and verify details in SFDC
	@Test(groups = {"Regression", "Sanity", "Product Sanity"}, priority = 3)
	public void inbound_call_add_caller_contact() {
		System.out.println("Test case --inbound_call_add_caller_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
	    String contactName = CONFIG.getProperty("qa_user_2_name");
	    String accountName	= CONFIG.getProperty("contact_account_name");
	    
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		
		//pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);

		// Deleting contact if exist
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
		}
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneSettingsPage.closeErrorMessage(driver1);
		
		//pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver1);
		
		// add caller as Lead
		callScreenPage.addCallerAsContact(driver1, contactName, accountName);
		
		//Hangup Call After that
		softPhoneCalling.hangupActiveCall(driver4);
		
		//Verify account name in salesforce
		callScreenPage.openAccountDetailPage(driver1);
		salesforceAccountPage.verifyAccountsNameHeading(driver1, accountName);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//verify caller details on sfdc page
		callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactName + " Automation", contactDetailPage.getCallerName(driver1));
		assertEquals(accountName, contactDetailPage.getCallerAccountName(driver1));
		assertEquals("QA", contactDetailPage.getCallerTitle(driver1));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "Sanity", "Product Sanity"}, priority = 4)
	public void inbound_call_add_caller_lead() {
		System.out.println("Test case --inbound_call_add_caller_lead-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
	    String leadName = CONFIG.getProperty("prod_user_2_name");
	    String companyName	= CONFIG.getProperty("contact_account_name");

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
		
		//pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver5);

		// Deleting contact if exist
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
		}
		
		// Calling to Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver5, CONFIG.getProperty("qa_user_1_number"));
		softPhoneSettingsPage.closeErrorMessage(driver1);
		
		//pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver1);
		
		//click mute button
		softPhoneCalling.clickMuteButton(driver1);
		softPhoneCalling.isUnMuteButtonVisible(driver1);
		
		// add caller as Lead
		callScreenPage.addCallerAsLead(driver1, leadName, companyName);
		
		//Caller should be on mute after adding as lead
		softPhoneCalling.isUnMuteButtonVisible(driver1);
		softPhoneCalling.ClickUnMuteButton(driver1);
		
		//Hangup Call After that
		softPhoneCalling.hangupActiveCall(driver5);
		
		//verify caller details on sfdc page
		callScreenPage.openCallerDetailPage(driver1);
		assertEquals(leadName + " Automation", contactDetailPage.getCallerName(driver1));
		assertEquals(companyName, contactDetailPage.getCallerCompanyName(driver1));
		assertEquals("QA", contactDetailPage.getCallerTitle(driver1));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	  @Test(groups={"Regression"}, priority = 7)
	  public void verify_lead_status_after_add()
	  {
		System.out.println("Test case --verify_lead_status_after_add-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));	
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Open.toString());
	    
		//Call is removing from softphone
	    System.out.println("Call is removing from softphone");
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //open recent call entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	   
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactDetailPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Open.toString());
		assertTrue(contactDetailPage.getLeadDescription(driver1).contains("Created by RingDNA"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Change the lead Status
		callScreenPage.selectLeadStatus(driver1, CallScreenPage.LeadStatusType.Contacted);
		seleniumBase.idleWait(2);
		
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Contacted.toString());
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactDetailPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Contacted.toString());
		assertTrue(contactDetailPage.getLeadDescription(driver1).contains("Created by RingDNA"));
		contactDetailPage.changeLeadStatus(driver1, CallScreenPage.LeadStatusType.Qualified);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Qualified.toString());
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//Change the lead Status
		callScreenPage.selectLeadStatus(driver1, CallScreenPage.LeadStatusType.Open);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"}, priority = 8)
	  public void verify_lead_status_from_contact_search()
	  {
		System.out.println("Test case --verify_lead_status_from_contact_search-- started ");
  
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
	    
	    String leadName	= CONFIG.getProperty("qa_user_3_name") + HelperFunctions.GetRandomString(2);
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, leadName);	
		
	    //open contact detail
	    softPhoneContactsPage.openSfLeadDetails(driver1, leadName + " Automation");
	    
	    
		//verify lead status on softphone
		assertEquals(callScreenPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Open.toString());
		
		//Change the lead Status
		callScreenPage.selectLeadStatus(driver1, CallScreenPage.LeadStatusType.Contacted);
	   
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactDetailPage.getLeadStatus(driver1), CallScreenPage.LeadStatusType.Contacted.toString());
		assertTrue(contactDetailPage.getLeadDescription(driver1).contains("Created by RingDNA"));
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

	@Test(groups = { "Sanity", "Regression", "Product Sanity" }, priority = 9)
	public void add_caller_from_lead_to_contact() {
		System.out.println("Test case --add_caller_from_lead_to_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String existingContact;
		String leadFirstName = CONFIG.getProperty("qa_user_2_name");
		String leadName = leadFirstName + " Automation";
		String contactFirstName = "Contact_Existing";
		String contactName = contactFirstName + " Automation";
		
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, leadFirstName);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String selectedContact = callScreenPage.getCallerName(driver1);

		//verify call task is assigned properly
	  	String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	  	callScreenPage.idleWait(5);
	  	driver1.navigate().refresh();
	  	contactDetailPage.openRecentCallEntry(driver1, callSubject);
	  	assertTrue(sfTaskDetailPage.getName(driver1).contains(selectedContact));
	  	seleniumBase.switchToTab(driver1, 1);
	  	
		// Update caller to be contact
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.addContactForExistingCaller(driver1, contactFirstName, CONFIG.getProperty("contact_account_name"));
		seleniumBase.idleWait(3);
		
		//verify that contact name is changes in the call task
		selectedContact = callScreenPage.getCallerName(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		seleniumBase.idleWait(15);
		driver1.navigate().refresh();
		assertTrue(sfTaskDetailPage.getName(driver1).contains(contactName));
	  	seleniumBase.closeTab(driver1);
	  	seleniumBase.switchToTab(driver1, 1);
	  	
	  	//verify that created task is under activity feeds
	  	softPhoneActivityPage.verifyTaskInCallsTab(driver1, callSubject);
	  	
		// search until contact in multiple
	  	softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("qa_user_2_number"));

		// search the contact with number
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// verify that lead is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());

		// verify that contact is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

		// Adding existing contact to multiple contact caller.
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver5);

		// add contact if unknown
		if (callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("adding contact");
			// add caller as Contact
			String firstName = "Caller Five";
			callScreenPage.addCallerAsContact(driver1, firstName, CONFIG.getProperty("contact_account_name"));
			existingContact = firstName + " Automation";
		} else {
			existingContact = callScreenPage.getCallerName(driver1);
		}

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);

		// Selecting first caller form multiple contacts
		callScreenPage.selectFirstContactFromMultiple(driver1);
		seleniumBase.idleWait(5);

		// Updating contact to add to existing contact
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.addCallerToExistingContact(driver1, existingContact);
		seleniumBase.idleWait(25);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);

		// search the contact with number
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// verify that contact is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, existingContact, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" }, priority = 10)
	public void add_multi_match_caller_to_existing_contact() {
		System.out.println("Test case --add_multi_match_caller_to_existing_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String existingContact;
		
		// Adding existing contact to multiple contact caller.
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.hangupActiveCall(driver2);

		// Deleting contact if exist
		if (callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("adding contact");
			// add caller as Contact
			String firstName = "Caller Two";
			callScreenPage.addCallerAsContact(driver1, firstName, CONFIG.getProperty("contact_account_name"));
			existingContact = firstName + " Automation";
		} else {
			existingContact = callScreenPage.getCallerName(driver1);
		}
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);
		seleniumBase.idleWait(5);

		// Updating contact to add to existing contact
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.addCallerToExistingContact(driver1, existingContact);
		seleniumBase.idleWait(5);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		
		//search untill caller is multiple
		softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("qa_user_2_number"));
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertEquals(existingContact, contactDetailPage.getCallerName(driver1));
		assertTrue(CONFIG.getProperty("qa_user_2_number").contains(contactDetailPage.getCallerPhone(driver1)));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// search the contact with number
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// verify that contact is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, existingContact, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		
		//verify that no task appears for multiple callers
		//call to the multiple contact
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);
		seleniumBase.idleWait(15);
		
		//open recent call entry and click on call notes icon and click cancel button
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		
		//verify that there are no activities in all activitites section
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		softPhoneActivityPage.verifyNoRecordTextMessage(driver1, "There are no Salesforce activities associated with this record. Future RingDNA calls, emails, tasks, and other activities will appear in this section.");
	
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" }, priority = 11)
	public void add_multi_match_caller_to_new_contact() {
		System.out.println("Test case --add_multi_match_caller_to_new_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String contactFirstName = "Contact_Existing";
		String contactName = contactFirstName + " Automation";
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "",CONFIG.getProperty("prod_user_1_number"));
		
		// Adding existing contact to multiple contact caller.		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.pickupIncomingCall(driver2);
		
		// pickup and hangup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);
		seleniumBase.idleWait(5);

		// Update caller to be contact
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.addNewContact(driver1, contactFirstName, CONFIG.getProperty("contact_account_name"));
		seleniumBase.idleWait(25);
		reloadSoftphone(driver1);

		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		assertEquals(contactName, contactDetailPage.getCallerName(driver1));
		assertEquals(CONFIG.getProperty("contact_account_name"), contactDetailPage.getCallerAccountName(driver1));
		assertTrue(CONFIG.getProperty("qa_user_2_number").contains(contactDetailPage.getCallerPhone(driver1)));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// search the contact with number
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));

		// verify that contact is appearing for the number
		softPhoneContactsPage.getSfdcResultsNameIndex(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("qa_user_2_number").substring(2, 12));
		
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority", "Sanity"}, priority = 12)
	public void update_multiple_caller_after_selecting(){
		System.out.println("Test case --update_multiple_caller_after_selecting()-- started");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);

		String callerPhone = CONFIG.getProperty("prod_user_1_number");
		
		// Setting call forwarding OFF
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		aa_AddCallersAsContactsAndLeads();
		
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
	    softPhoneCalling.pickupIncomingCall(driver6);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    String existingContact = callScreenPage.getCallerName(driver1);
	    
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    seleniumBase.idleWait(4);
	    
	    // Add caller as multiple contact
	    callScreenPage.getCallerName(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    
	    softPhoneContactsPage.searchUntilContacIsMultiple(driver1, callerPhone);
	    
	    //Calling to multiple matched contact
	    softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver2);
	 	softPhoneCalling.clickMuteButton(driver1);
	 	softPhoneCalling.isUnMuteButtonVisible(driver1);
	 	
	 	//verify that multi match required warning message is appearing
	 	callScreenPage.selectFirstContactFromMultiple(driver1);
	 	callToolsPanel.isRelatedRecordsIconVisible(driver1);
	 	String selectedContact = callScreenPage.getCallerName(driver1);
	 	softPhoneCalling.isUnMuteButtonVisible(driver1);
	 	
	 	//Verify the already selected caller and updated selected contact
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    assertEquals(callScreenPage.getSelectedMultipleContact(driver1), selectedContact);
	    callScreenPage.selectSecondContactFromMultiple(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    selectedContact = callScreenPage.getCallerName(driver1);
	    
	    //End call
	 	softPhoneCalling.hangupActiveCall(driver1);
	 	seleniumBase.idleWait(3);
	 	
	    //verify call task is assigned properly
	 	callToolsPanel.isRelatedRecordsIconVisible(driver1);
	 	String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	  	contactDetailPage.openRecentCallEntry(driver1, callSubject);
	  	assertTrue(sfTaskDetailPage.getName(driver1).contains(selectedContact));
	  	seleniumBase.closeTab(driver1);
	  	seleniumBase.switchToTab(driver1, 1);
	  	
	  	//verify that created task is under activity feeds
	  	softPhoneActivityPage.verifyTaskInCallsTab(driver1, callSubject);
	  	
	  	softPhoneContactsPage.deleteContactUsingIndex(driver1, CONFIG.getProperty("prod_user_1_number").substring(2, 12), 0);
	  	
	  	softPhoneContactsPage.addContactIfNotExist(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
	    
	 	//Setting driver used to false as this test case is pass
	  	driverUsed.put("driver1", false);
	  	driverUsed.put("driver2", false);
	}
	
	@Test(groups={"Regression"}, priority = 13)
	  public void verify_contact_card_update_contact_not_present()
	  {
		System.out.println("Test case --verify_contact_card_update_contact_not_present-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//ad contact as contact
	    String contactName = CONFIG.getProperty("qa_user_3_name");
		
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, contactName);						  
		
		// searching contact
		softPhoneContactsPage.openSfContactDetails(driver1, contactName + " Automation");						  
		
		//reloading Softphone
	    callScreenPage.verifyUpdateDetailIconVisible(driver1);
	    
	    //verify add new button is not visible
	    assertFalse(callScreenPage.isAddNewButtonVisible(driver1));
	    
	    reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	  }  
	
	@Test(groups = {"MediumPriority", "Sanity", "ExludeForProd"}, priority = 14)
	public void add_to_existing_advanced_serach_off() {
		System.out.println("Test case --add_number_to_existing_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String existingContact;
		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number"));
		
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableAdvancedSearchSetting(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, contactNumber);
		softPhoneContactsPage.clickSearchContactButton(driver1);
		existingContact = softPhoneContactsPage.getAllResultNamesList(driver1).get(0);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			reloadSoftphone(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
			softPhoneCalling.pickupIncomingCall(driver3);
			System.out.println("hanging up with caller 1");
			softPhoneCalling.hangupActiveCall(driver3);
		}

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
		seleniumBase.idleWait(3);
		
		callScreenPage.addCallerToExistingContact(driver1, existingContact);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		seleniumBase.idleWait(25);
		reloadSoftphone(driver1);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver3);
		assertEquals(callScreenPage.getCallerName(driver1), existingContact);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Can't dial same number with whom agent already on call
	@Test(groups = {"MediumPriority"}, priority = 15)
	public void calling_same_number_again() {
		System.out.println("Test case --calling_same_number_again-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number"));
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//put call on hold
		softPhoneCalling.clickHoldButton(driver1);
		
		//Try to dial the same number again
		softPhoneCalling.enterNumberAndDial(driver1, contactNumber);
		
		//verify that error message appears
		assertEquals(callScreenPage.getErrorText(driver1), "Call failed: you already have a call to " + contactNumber);
		callScreenPage.closeErrorBar(driver1);
		
		//resume the call
		softPhoneCalling.clickOnHoldButton(driver1);
		softPhoneCalling.clickResumeButton(driver1);
		seleniumBase.idleWait(3);
		
		//Try to dial the same number again
		softPhoneCalling.enterNumberAndDial(driver1, contactNumber);
		
		//verify that error message appears
		assertEquals(callScreenPage.getErrorText(driver1), "Call failed: you already have a call to " + contactNumber);
		callScreenPage.closeErrorBar(driver1);
		
		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify add to existing caller page does not refresh after call activity generated
	@Test(groups = {"MediumPriority"}, priority = 16)
	public void add_to_existing_screen_shouldnt_refresh() {
		System.out.println("Test case --add_to_existing_screen_shouldnt_refresh-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver3);
		
		//Opening the caller detail page
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.changeCallSubject(driver1, callSubject);		

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		//Click on update contact button and add to exiting button
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.clickAddToExistingButton(driver1);
		
		//capture the screenshot of caller detail section before task get created
		File callScreenBeforeActivity = callScreenPage.getCallerDetailSectionScreenShot(driver1);
		
		//wait for the task to get created
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, callSubject);
		
		//capture the screenshot of caller detail section after task get created
		File callScreenAfterActivity = callScreenPage.getCallerDetailSectionScreenShot(driver1);
		
		//verify that the add to exiting screen not closed
		assertTrue(HelperFunctions.bufferedImagesEqual(callScreenBeforeActivity, callScreenAfterActivity));

		//cancel the add to existing screen
		callScreenPage.clickCancelAddToExstingButton(driver1);
		callScreenPage.clickCancelContactButton(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify Add new account to existing Contact
	@Test(groups = {"MediumPriority"}, priority = 17)
	public void add_new_account_while_editing_contact() {
		System.out.println("Test case --add_new_account_while_editing_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		
		String company = "New Account "  + HelperFunctions.GetRandomString(5);

		//Open recent contact of lead call history entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		
		//Click on update contact button and add to exiting button
		callScreenPage.clickOnUpdateDetailLink(driver3);
		callScreenPage.clickAddNewButton(driver3);
		
		callScreenPage.enterContactDetailsWithNewAccount(driver3, "Automation Test User", company);
		
		assertEquals(callScreenPage.getSelectedAccount(driver3), company);
		
		callScreenPage.clickSaveContactButton(driver3);
		
		//Verify account name in salesforce
		callScreenPage.openAccountDetailPage(driver3);
		salesforceAccountPage.verifyAccountsNameHeading(driver3, company);
		salesforceAccountPage.deleteAccount(driver3);
		seleniumBase.closeTab(driver3);
		seleniumBase.switchToTab(driver3, 1);
		
		aa_AddCallersAsContactsAndLeads();
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify customization message when user open Lead/contact detail
		@Test(groups = {"MediumPriority"}, priority = 18)
		public void verify_customization_message() {
			System.out.println("Test case --verify_customization_message-- started ");

			// updating the driver used
			initializeDriverSoftphone("driver3");
			driverUsed.put("driver3", true);

			//Open recent contact of lead call history entry
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
			
			//Verify that the customized message is appearing
			callScreenPage.verifyCustomizedMessageVisible(driver3);
			
			//Close the customized message and verify it doesn't appear for other lead contact
			callScreenPage.closeCustomizedMessage(driver3);
			softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver3);
			callScreenPage.verifyCustomizedMessageInvisible(driver3);
			
			//close browser, open agin browser and relogin then verify that the customized message appearing again
			driver3.quit();
			driver3 = null;
			initializeDriverSoftphone("driver3");
			
			//Open recent contact of lead call history entry
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
					
			//Verify that the customized message is appearing
			callScreenPage.verifyCustomizedMessageVisible(driver3);
			
			// Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);
			driverUsed.put("driver2", false);
			driverUsed.put("driver3", false);
			driverUsed.put("driver4", false);
			driverUsed.put("driver5", false);

			System.out.println("Test case is pass");
		}
	
	@AfterMethod(groups={"Regression", "MediumPriority", "Sanity", "Product Sanity"}, dependsOnMethods = {"resetSetupDefault"})
	public void  _afterMethod(ITestResult result){
		if(result.getName().equals("add_to_existing_advanced_serach_off")){
			//updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableAdvancedSearchSetting(driver1);;
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
			
			aa_AddCallersAsContactsAndLeads();
		}else if(result.getName().equals("update_multiple_caller_after_selecting")){
			aa_AddCallersAsContactsAndLeads();
		}
	}
}