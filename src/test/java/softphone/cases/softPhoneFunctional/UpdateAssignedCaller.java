package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

public class UpdateAssignedCaller extends SoftphoneBase{
	
	@Test(groups={"MediumPriority"})
	  public void update_contact_for_single_caller()
	  {
		System.out.println("Test case --update_contact_for_single_caller-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");
	   	
	    //delete the contact and add again
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, "Yamini Ringdna");
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.changeCallSubject(driver1, callSubject);
		
	    //verify that update button is in line with caller name
		File actualImage = callScreenPage.hoverOnNameContainer(driver1);
		File expectedImage = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\imageFiles\\ContactUpdate.png"));
		assertTrue(HelperFunctions.bufferedImagesEqual(actualImage, expectedImage));
	    
	    //click on update button and cancel it and verify caller name is still the same
		String callerName = callScreenPage.getCallerName(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.clickCancelContactButton(driver1);
	    assertTrue(callerName.equals(callScreenPage.getCallerName(driver1)));
	    
	    //update the caller details and verify that call task is still appearing
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.updateContact(driver1, "UpdateTestUser", CONFIG.getProperty("contact_account_name"));
		seleniumBase.idleWait(15);
		softPhoneActivityPage.verifyTaskInCallsTab(driver1, callSubject);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void update_contact_for_multiple_caller()
	  {
		System.out.println("Test case --update_contact_for_multiple_caller-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		checkAndAddCallerMultiple();
		
		String callerPhone 		= CONFIG.getProperty("prod_user_1_number");
		
		//Connect to a call and select first contact from multiple dropdown
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver2);
		callScreenPage.selectFirstContactFromMultiple(driver1);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
		
	    //verify that selected caller is there in multi match dropdown
		String firstCallerName = callScreenPage.getCallerName(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    assertEquals(callScreenPage.getSelectedMultipleContact(driver1), firstCallerName);
	   	
	    //delete the contact and add again		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		firstCallerName = callScreenPage.getCallerName(driver1);
		String callSubject = "Call Notes " + helperFunctions.GetCurrentDateTime();
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		callToolsPanel.changeCallSubject(driver1, callSubject);
	   
	    //update the caller details and verify that call task is still appearing
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    assertEquals(callScreenPage.getSelectedMultipleContact(driver1), firstCallerName);
	    callScreenPage.selectSecondContactFromMultiple(driver1);
	    assertFalse(firstCallerName.equals(callScreenPage.getCallerName(driver1)));
		seleniumBase.idleWait(15);
		softPhoneActivityPage.verifyTaskInCallsTab(driver1, callSubject);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void add_lead_for_multiple_caller()
	  {
		System.out.println("Test case --update_contact_for_multiple_caller-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		checkAndAddCallerMultiple();
	   	
		String callerPhone 		= CONFIG.getProperty("prod_user_1_number");
		String leadFirstName	= "MultiMatchNewLead";
		
		//Connect to a call
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver1);
	   
	    //update the caller details and verify that call task is still appearing
	    callScreenPage.addCallerToMultipleLead(driver1, leadFirstName, "Metacube");
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    
	    //Verify the contact is saved with new lead
	    assertEquals(callScreenPage.getCallerName(driver1), leadFirstName + " Automation");
	    callScreenPage.verifyCallerIsLead(driver1);
	    
	    //Open Caller detail in salesforce
	    callScreenPage.openCallerDetailPage(driver1);
	    assertEquals(contactDetailPage.getCallerName(driver1), leadFirstName + " Automation");
	    contactDetailPage.verifyLeadPageOpen(driver1);
		contactDetailPage.deleteContact(driver1);
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
	
	@Test(groups={"MediumPriority"})
	  public void add_contact_for_multiple_caller()
	  {
		System.out.println("Test case --add_contact_for_multiple_caller-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		checkAndAddCallerMultiple();
	   	
		String ContactFirstName = "MultiMatchNewContact";
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		String queueNumber = CONFIG.getProperty("qa_group_1_number").trim();
		
		//Connect to a call through queue
		softPhoneCallQueues.subscribeQueue(driver1, queueName);
		softPhoneCalling.softphoneAgentCall(driver2, queueNumber);
		softPhoneCallQueues.pickCallFromQueue(driver1);
		softPhoneCallQueues.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	   
	    //update the caller details and verify that call task is still appearing
	    callScreenPage.addNewContact(driver1, ContactFirstName, "Metacube");
	    callToolsPanel.isRelatedRecordsIconVisible(driver1);
	    
	    //Verify the contact is saved with new lead
	    assertEquals(callScreenPage.getCallerName(driver1), ContactFirstName + " Automation");
	    callScreenPage.verifyCallerIsContact(driver1);
	    
	    //Open Caller detail in salesforce
	    callScreenPage.openCallerDetailPage(driver1);
	    assertEquals(contactDetailPage.getCallerName(driver1), ContactFirstName + " Automation");
	    contactDetailPage.verifyContactPageOpen(driver1);
		contactDetailPage.deleteContact(driver1);
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
	
	@Test(groups={"MediumPriority"})
	public void create_contact_call_flow()
	{
		System.out.println("Test case --create_contact_call_flow-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String ContactFirstName = "CallFlowNewContact";
		
		aa_AddCallersAsContactsAndLeads();
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver2);

		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver4)) {
			System.out.println("deleting contact");
			callScreenPage.deleteCallerObject(driver4);
			seleniumBase.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver4);
		}

		// Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver4);

		//calling to call flow number
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_call_flow_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hanging up with caller 1
		System.out.println("hanging up with caller");
		softPhoneCalling.hangupActiveCall(driver4);

		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//update the caller details and verify that call task is still appearing
	    callScreenPage.addNewContact(driver4, ContactFirstName, "Metacube");
	    callToolsPanel.isRelatedRecordsIconVisible(driver4);
	    
	    //Verify the contact is saved with new lead
	    assertEquals(callScreenPage.getCallerName(driver4), ContactFirstName + " Automation");
	    callScreenPage.verifyCallerIsContact(driver4);
	    
	    //Open Caller detail in salesforce
	    callScreenPage.openCallerDetailPage(driver4);
	    assertEquals(contactDetailPage.getCallerName(driver4), ContactFirstName + " Automation");
	    contactDetailPage.verifyContactPageOpen(driver4);
		contactDetailPage.deleteContact(driver4);
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);

	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	  
	  public void checkAndAddCallerMultiple() {

			//updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver2");
			driverUsed.put("driver2", true);
		  
			MultiMatchRequired multiMatchRequired = new MultiMatchRequired();
			
			String callerPhone 		= CONFIG.getProperty("prod_user_1_number");
			String callerFirstName	= CONFIG.getProperty("prod_user_1_name");
			String existingContactName	= CONFIG.getProperty("prod_user_3_name");
			String existingContact 	= CONFIG.getProperty("prod_user_3_number");
			
			//Connect to a call
			softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
			softPhoneCalling.pickupIncomingCall(driver2);
		    softPhoneCalling.hangupActiveCall(driver1);
		    
		    if(!callScreenPage.isCallerMultiple(driver1)) {
		    	multiMatchRequired.addCallerAsMultiple(driver1, driver2, callerPhone, callerFirstName, existingContactName, existingContact);	
		    }
	  }
}
