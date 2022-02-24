package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

public class CallTemplate extends SoftphoneBase{
	
	String callNoteName = "Automation Call Template";
	String callNoteTemplate = "This is a test Call Template";
	
	@BeforeClass
	public void enableCallTemplates() {
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotesTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName, callNoteTemplate, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    reloadSoftphone(driver1);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_notes_templates_unknown() {
		System.out.println("Test case --call_notes_templates_unknown-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");

		// Calling and hanging from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver1);
		
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			System.out.println("deleting contact");
			// add caller as Contact
			callScreenPage.deleteCallerObject(driver1);
			seleniumBase.idleWait(3);
		}
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "MediumPriority" })
	public void call_notes_templates_multiple() {
		System.out.println("Test case --call_notes_templates_multiple-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		addCallerAsMultiple();
	
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//Verifying that user is able to select call templates for multiple matched caller
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		aa_AddCallersAsContactsAndLeads();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_notes_templates_conference_wtih_call_forwarding() {
		System.out.println("Test case --call_notes_templates_conference_wtih_call_forwarding-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Setting call forwarding ON
		System.out.println("Setting call forwarding ON");
		softPhoneSettingsPage.setCallForwardingNumber(driver1, driver2, "", CONFIG.getProperty("prod_user_1_number"));
				
		// Making first call to agent
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.clickHoldButton(driver1);

		// Making outbound call to participant 2
		softPhoneCalling.softphoneAgentCall(driver1,CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);

		// checking hold call actions button resume and merge
		softPhoneCalling.clickOnHoldButtonAndVerifyButtons(driver1);

		// merging the second call with the first call
		softPhoneCalling.clickMergeButton(driver1);
		softPhoneCalling.isConferenceButtonDisplayed(driver1);
		
		//provide call notes using call templates
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
		// hanging up with caller 1
		softPhoneCalling.hangupActiveCall(driver3);
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		// Setting call forwarding ON
		System.out.println("Setting call forwarding OFF");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableCallForwardingSettings(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_notes_templates_disabled() {
		System.out.println("Test case --call_notes_templates_disabled-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("qa_user_1_number");
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallNotesTemplateSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //reload softphone
	    reloadSoftphone(driver1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver5, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver1);
	
		//Verifying that call templates dropdown is not visible
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		assertFalse(callToolsPanel.isTemplatePickerVisible(driver1));
		callToolsPanel.clickCallNotesCancelBtn(driver1);
		
		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		enableCallTemplates();

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	@Test(groups = { "MediumPriority" })
	public void call_templates_agent() {
		System.out.println("Test case --call_templates_agent-- started ");
	
		// updating the driver used
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
	
		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotesTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName, callNoteTemplate, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(agentDriver, contactNumber);
	
		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);
	
		// entering call notes
		softPhoneCalling.hangupActiveCall(agentDriver);
		
		String callSubject = callToolsPanel.changeAndGetCallSubject(agentDriver);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(agentDriver, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(agentDriver), callNoteTemplate);
		callToolsPanel.clickCallNotesSaveBtn(agentDriver);
		
		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(agentDriver);
		callScreenPage.clickCallerName(agentDriver);
		seleniumBase.switchToTab(agentDriver, seleniumBase.getTabCount(agentDriver));
		callScreenPage.closeLightningDialogueBox(agentDriver);
		contactDetailPage.openRecentCallEntry(agentDriver, callSubject);
		assertEquals(sfTaskDetailPage.getComments(agentDriver), callNoteTemplate);
		seleniumBase.closeTab(agentDriver);
		seleniumBase.switchToTab(agentDriver, 1);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("agentDriver", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_notes_multiple_templates() {
		System.out.println("Test case --call_notes_templates-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		String callNoteName1 = "Second Automation Call Template";
		String callNoteTemplate1 = "This is Second Automation Call Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotesTemplateSetting(driver1);
		
		//Create  Template if not created
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName, callNoteTemplate, "");
		}
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName1)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName1, callNoteTemplate1, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplate(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallNotesIcon(driver1);
		callToolsPanel.selectCallTemplate(driver1, callNoteName1);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate + callNoteTemplate1);
		assertEquals(callToolsPanel.getSelectedTemplate(driver1), callNoteName1);
		callToolsPanel.clickCallNotesSaveBtn(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_tools_multiple_templates() {
		System.out.println("Test case --call_tools_multiple_templates-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallToolsSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //reload softphone
	    reloadSoftphone(driver1);

		String contactNumber = CONFIG.getProperty("prod_user_2_number");
		
		String callNoteName = "Automation Call Template";
		String callNoteTemplate = "This is a test Call Template";
		String callNoteName1 = "Second Automation Call Template";
		String callNoteTemplate1 = "This is Second Automation Call Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCallNotesTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName, callNoteTemplate, "");
		}
		if(!accountIntelligentDialerTab.checkCallNotesTemplateSaved(driver1, callNoteName1)){
			accountIntelligentDialerTab.createCallNotesTemplate(driver1, callNoteName1, callNoteTemplate1, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

		// receiving call from receiver
		softPhoneCalling.pickupIncomingCall(driver5);

		// entering call notes
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Verifying that by default call notes are blank
		callToolsPanel.selectCallTemplateFromCallTools(driver1, callNoteName);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate);
		callToolsPanel.clickCallToolsIcon(driver1);
		callToolsPanel.selectCallTemplateFromCallTools(driver1, callNoteName1);
		assertEquals(callToolsPanel.getCallNotesText(driver1), callNoteTemplate + callNoteTemplate1);
		assertEquals(callToolsPanel.getSelectedTemplate(driver1), callNoteName1);
		callToolsPanel.clickCallNotesSaveBtn(driver1);
		
	 	// open Support Page and disable call disposition prompt setting
	 	loginSupport(driver1);
	 	dashboard.clickAccountsLink(driver1);
	 	System.out.println("Account editor is opened ");
	 	accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCallToolsSetting(driver1);
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
	 	reloadSoftphone(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	public void addCallerAsMultiple() {
		
		String callerPhone = CONFIG.getProperty("prod_user_2_number");
	    
	    String existingContact = CONFIG.getProperty("prod_user_3_name").trim() + " Automation";
	    
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_2_name"));
		//calling again
		softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
		softPhoneCalling.pickupIncomingCall(driver5);
		
		//picking up the call
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    // Add caller as multiple contact
	    callScreenPage.closeErrorBar(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    
	   //verify caller is multiple
	   softPhoneContactsPage.searchUntilContacIsMultiple(driver1, CONFIG.getProperty("prod_user_2_number"));
	    
	   // Calling from Agent's SoftPhone
	   softPhoneCalling.softphoneAgentCall(driver1, callerPhone);
	 	softPhoneCalling.pickupIncomingCall(driver5);
	 	softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.idleWait(3);
	 	
	 	softPhoneCalling.clickCallBackButton(driver1);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.pickupIncomingCall(driver5);
	 	softPhoneCalling.idleWait(5);
	 	softPhoneCalling.hangupActiveCall(driver1);											  
	}
}
