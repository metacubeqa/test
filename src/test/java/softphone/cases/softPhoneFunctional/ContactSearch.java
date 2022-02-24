/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.callTools.SoftPhoneNewTask;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class ContactSearch extends SoftphoneBase{
	
	String contactName;
	String leadName;
	
	@BeforeClass(groups = {"QuickSanity" ,"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void beforeClass() {
		contactName = CONFIG.getProperty("prod_user_1_name").trim() + " Automation";
		leadName	= CONFIG.getProperty("qa_user_2_name").trim() + " Automation";

		// updating the driver used	
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//add contact
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");		  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name").trim());
		
	    contactNumber = CONFIG.getProperty("qa_user_2_number");
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_2_name").trim());

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
	}
	
	@Test(groups = {"Regression"})
	public void search_salesforce_contact_name() {
		System.out.println("Test case --search_salesforce_contact_name()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// search a contact and verify that the record is visible
		softPhoneContactsPage.salesforceSearch(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		
		//verify that only one record is there
		assertEquals(softPhoneContactsPage.getSfdcContactsResultNames(driver1).size(), 1);
		
		//Search and verify leads
		softPhoneContactsPage.salesforceSearch(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		
		//verify that only one record is there
		assertEquals(softPhoneContactsPage.getSfdcLeadsResultNames(driver1).size(), 1);
		
		//Search and verify all record has the account name
		softPhoneContactsPage.salesforceSearch(driver1, CONFIG.getProperty("contact_account_name"), SoftPhoneContactsPage.searchTypes.Accounts.toString());
	    		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "Product Sanity"})
	public void search_salesforce_contact() {
		System.out.println("Test case --search_salesforce_contact_search()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// search and make a call
		softPhoneContactsPage.salesforceSearchAndCall(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
	    
		//receiving call from the contact
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hanging up with the active call
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Verifying that call is removed from the called number
		softPhoneCalling.isCallBackButtonVisible(driver2);
		
		//Search and verify leads
		softPhoneContactsPage.salesforceSearch(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		
		//Search and verify accounts
		softPhoneContactsPage.salesforceSearch(driver1, CONFIG.getProperty("contact_account_name"), SoftPhoneContactsPage.searchTypes.Accounts.toString());
		
		//reload softphone
		reloadSoftphone(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority", "Product Sanity" })
	public void search_rdna_contact() {
		System.out.println("Test case --search_rdna_contact_search()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String queueName = CONFIG.getProperty("qa_group_1_name").trim();
		
		// search user, verify and make a call
		softPhoneContactsPage.ringDNASearchAndCall(driver1, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		// search user, verify and make a call
		softPhoneContactsPage.ringDNASearchAndCall(driver4, queueName, SoftPhoneContactsPage.searchTypes.Groups.toString());
		
	    //subscribe a queue
	    softPhoneCallQueues.subscribeQueue(driver1, queueName);
	  
	    //verify that Queue is appearing in subscribed queues list
	    softPhoneCallQueues.isQueueSubscribed(driver1, queueName);
	    
	    //pickup call from the queue
	    softPhoneCallQueues.pickCallFromQueue(driver1);
	    
		//Hangup the calls
	    softPhoneCalling.hangupActiveCall(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity", "MediumPriority" })
	public void add_remove_contacts_as_favorite() {
		System.out.println("Test case --add_remove_contacts_as_favorite()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Set Contact as fav
		softPhoneContactsPage.salesforceSetFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, contactName);
		
		//click on call button for the favourite contact
		softPhoneContactsPage.clickFavConCallBtn(driver1, contactName);
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.isTransferButtonEnable(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		//remove Contact from fav
		softPhoneContactsPage.salesforceRemoveFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

		//Verify that contact is not favorite
		softPhoneContactsPage.isContactNotFavorite(driver1, contactName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity", "Regression" })
	public void add_contact_manually() {
		System.out.println("Test case --add_contact_manually()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String manualContactName 	= "auto_man_cont: " + helperFunctions.GetCurrentDateTime();
		String manualContactNumber 	= "291364782";
		
		//Set Contact as fav
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName, manualContactNumber);
		
		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, manualContactName);
		
		//Remove Contact from fav
		softPhoneContactsPage.removeContactFavorite(driver1, manualContactName);

		//Verify that contact is favorite
		softPhoneContactsPage.isContactNotFavorite(driver1, manualContactName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Sanity" })
	public void add_remove_favorite_from_caller_screen() {
		System.out.println("Test case --add_remove_favorite_from_caller_screen()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Set Contact as fav
		softPhoneContactsPage.openSfContactDetails(driver1, contactName);
		callScreenPage.setAsFavoriteContact(driver1);
		reloadSoftphone(driver1);

		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, contactName);
		
		//remove Contact from fav
		softPhoneContactsPage.openSfContactDetails(driver1, contactName);
		callScreenPage.removeFromFavoriteContact(driver1);
		reloadSoftphone(driver1);

		//Verify that contact is favorite
		softPhoneContactsPage.isContactNotFavorite(driver1, contactName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority" })
	public void add_edit_fav_contact_manually() {
		System.out.println("Test case --add_edit_fav_contact_manually()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String manualContactName 	= "auto_man_cont: " + helperFunctions.GetCurrentDateTime();
		String manualContactNumber 	= "291364782";
		
		//Set Contact as fav
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName, manualContactNumber);
		
		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, manualContactName);
		
		//click on added fav contact to open edit widnow
		softPhoneContactsPage.clickFavoriteContactName(driver1, manualContactName);
		
		//update the fav contact name and number
		String updManualContactName 	= "auto_upd_man_cont: " + helperFunctions.GetCurrentDateTime();
		String updManualContactNumber 	= "291364780";
		softPhoneContactsPage.addFavContactManualDetails(driver1, updManualContactName, updManualContactNumber);
		
		//Verify that contact details has been updated
		softPhoneContactsPage.isContactFavorite(driver1, updManualContactName);
		softPhoneContactsPage.verifyMsgIconOnFavConactVisible(driver1, updManualContactNumber);
		
		//Verify that there is no fav contact with old details
		softPhoneContactsPage.isContactNotFavorite(driver1, manualContactName);
		
		//Remove Contact from fav
		softPhoneContactsPage.removeContactFavorite(driver1, updManualContactName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority" })
	public void open_fav_contact_in_sfdc() {
		System.out.println("Test case --open_fav_contact_in_sfdc()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Set Contact as fav
		softPhoneContactsPage.salesforceSetFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, contactName);
		
		//click on call button for the favourite contact
		softPhoneContactsPage.clickFavoriteContactName(driver1, contactName);
		seleniumBase.switchToSFTab(driver1, seleniumBase.getTabCount(driver1));
		assertTrue(contactDetailPage.getCallerName(driver1).contains(contactName));
		
		//remove Contact from fav
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		softPhoneContactsPage.salesforceRemoveFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());

		//Verify that contact is not favorite
		softPhoneContactsPage.isContactNotFavorite(driver1, contactName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void add_fav_contact_advanced_search_disabled() {
		System.out.println("Test case --add_fav_contact_advanced_search_disabled-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//disable advance search
		seleniumBase.openNewBlankTab(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.get(CONFIG.getProperty("qa_support_tool_site"));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableAdvancedSearchSetting(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		String manualContactName 	= "auto_man_cont: " + helperFunctions.GetCurrentDateTime();
		String manualContactNumber 	= "291364782";
		
		//Set Contact as fav
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName, manualContactNumber);
		
		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, manualContactName);
		
		//click on added fav contact to open edit widnow
		softPhoneContactsPage.clickFavoriteContactName(driver1, manualContactName);
		
		//take an incoming call and connect to it
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupActiveCall(driver1);
		
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.navigateToFavoritePage(driver1);
		
		//Remove Contact from fav
		softPhoneContactsPage.removeContactFavorite(driver1, manualContactName);
		
		//Verify that there is no fav contact with old details
		softPhoneContactsPage.isContactNotFavorite(driver1, manualContactName);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"Sanity", "MediumPriority"})
	  public void search_contact_and_add_task()
	  {
		System.out.println("Test case --search_contact_and_add_task-- started ");
  
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// Set Contact as fav
		softPhoneContactsPage.openSfContactDetails(driver1, contactName);						  
		
		// Adding a new task for receiver
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		String dateFormat = CONFIG.getProperty("softphone_reminder_date_format");
		String timeFormat = CONFIG.getProperty("softphone_reminder_time_format");
		String reminderDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String dueDate = new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
		String reminderTime = new SimpleDateFormat(timeFormat).format(Calendar.getInstance().getTime());
		String taskSubject = "Subject " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar .getInstance().getTime());
		String taskComment = "Comment " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskDetails.put(SoftPhoneNewTask.taskFields.Subject, taskSubject);
		taskDetails.put(SoftPhoneNewTask.taskFields.Comments, taskComment);
		taskDetails.put(SoftPhoneNewTask.taskFields.DueDate, dueDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, reminderDate);
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, reminderTime);
		taskDetails.put(SoftPhoneNewTask.taskFields.Type, SoftPhoneNewTask.TaskTypes.Other.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Priority, SoftPhoneNewTask.TaskPriorities.Normal.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.Status, SoftPhoneNewTask.TaskStatuses.InProgress.displayName());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecordType, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString());
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case"));
		reminderTime = softPhoneNewTask.addNewTaskWithAllFields(driver1, taskDetails);

		// getting reminder date and time in qa user 1 time zone in salesforce
		String userTimeZone = CONFIG.getProperty("qa_user_1_timezone");
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderDate, HelperFunctions.getDateTimeInTimeZone(reminderDate, userTimeZone, dateFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.ReminderTime, HelperFunctions.getDateTimeInTimeZone(reminderTime, userTimeZone, timeFormat));
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));

		// verify that task is appearing under Open task tab
		softPhoneActivityPage.verifyTaskInOpenTaskTab(driver1, taskSubject);

		// Open task in salesforce from All Activity feeds section
		softPhoneActivityPage.openTaskInSalesforce(driver1, taskSubject);

		// Check task data on salesforce
		sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//reloading Softphone
		reloadSoftphone(driver1);
		
		//open contacts detail again
		softPhoneContactsPage.openSfContactDetails(driver1, contactName);	
	    
		//verify call tasks and Links button are visible, report this call button is not visible
		callToolsPanel.verifyTask_EventsVisible(driver1);
		callToolsPanel.verifyCustomLinkVisible(driver1);
		reportThisCallPage.verifyReportThisCallNotVisible(driver1);
		
		//newly created task is visible in acitivity feeds page
		softPhoneActivityPage.isTaskVisibleInActivityList(driver1, taskSubject);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    
	    System.out.println("Test case is pass");
	  }  
	
	@Test(groups = {"Regression"})
	public void preserve_contact_search_page() {
		System.out.println("Test case --preserve_contact_search_page()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Search and verify leads
		softPhoneContactsPage.salesforceSearch(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		
		//Navigate to some other page
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		
		//Search and verify accounts
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.verifySalesforceSearchString(driver1, leadName);
		int searchIndex = softPhoneContactsPage.getSfdcResultsNameIndex(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		assertNotEquals(searchIndex, -1);
		
		//Select ringDNA search option
		softPhoneContactsPage.selectRingDNAOption(driver1);
		
		//reload softphone
		reloadSoftphone(driver1);
		
		//verify ringDNA search option is selected
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.isRingDNASearchOptionSelected(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression", "MediumPriority" })
	public void search_refresh_status_dynamically() {
		System.out.println("Test case --search_refresh_status_dynamically()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		softPhoneSettingsPage.logoutSoftphone(driver4);
		
		String contactName = CONFIG.getProperty("qa_user_2_name");
		String searchType = SoftPhoneContactsPage.searchTypes.Users.toString();
		
		// search user, verify and make a call
		softPhoneContactsPage.ringDNASearch(driver1, contactName, searchType);
		
		softPhoneContactsPage.isResultStatusLoggedOut(driver1, contactName, searchType);
		
		SFLP.softphoneLogin(driver4, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));
		
		softPhoneContactsPage.isResultStatusAvailable(driver1, contactName, searchType);
		
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_group_1_number"));
		
		softPhoneContactsPage.isResultStatusBusyOnCall(driver1, contactName, searchType);
		
		softPhoneCalling.hangupIfInActiveCall(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
		
	    System.out.println("Test case is pass");
	}

	@Test(groups={"Regression", "MediumPriority"})
	  public void search_lead_left_company()
	  {
		System.out.println("Test case --search_lead_left_company-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    leadName	= CONFIG.getProperty("qa_user_2_name").trim() + " Automation";
	    String contactNumber = CONFIG.getProperty("qa_user_2_number");

		// verify that lead is appearing for the number
	    softPhoneContactsPage.convertMultipleToSingle(driver1, leadName);
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_2_name").trim());
		softPhoneContactsPage.salesforceSearch(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
				
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);
	    contactDetailPage.selectLeadCompanyLeft(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    seleniumBase.idleWait(10);
	    
	    // verify that lead is appearing for the number
	 	assertFalse(softPhoneContactsPage.isSalesforceContactPresent(driver1, leadName));
	 	
	 	//disable Sosl search
	 	loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableSOSLSearchSetting(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	 	
	 	seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	 	contactDetailPage.deselectLeadCompanyLeft(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    // verify that lead is appearing for the number
	 	assertTrue(softPhoneContactsPage.isSalesforceContactPresent(driver1, leadName));
	 	
	 	//enable Sosl search
	 	loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableSOSLSearchSetting(driver1);;
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups = {"Regression" })
	public void add_favorite_from_call_history_screen() {
		System.out.println("Test case --add_favorite_from_call_history_screen()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Set Contact as fav
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.setAsFavoriteContact(driver1);
		String contactNameToSearch = callScreenPage.getCallerName(driver1); 

		//Verify that contact is favorite
		softPhoneContactsPage.isContactFavorite(driver1, contactNameToSearch);
		
		//remove Contact from fav
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.removeFromFavoriteContact(driver1);
		reloadSoftphone(driver1);

		//Verify that contact is favorite
		softPhoneContactsPage.isContactNotFavorite(driver1, contactNameToSearch);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"Regression"})
	public void contact_search_advanced_serach_off() {
		System.out.println("Test case --contact_search_advanced_serach_off-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//disable advance search
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableAdvancedSearchSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//verify that sfdc contact can be searched
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, contactName.substring(0, 2));
		softPhoneContactsPage.clickSearchContactButton(driver1);
		assertTrue(softPhoneContactsPage.getAllResultNamesList(driver1).contains(contactName));

		//verify that sfdc contact can be searched
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, contactName);
		softPhoneContactsPage.clickSearchContactButton(driver1);
		List<String> contactResults = softPhoneContactsPage.getAllResultNamesList(driver1);
		for (String contactResult : contactResults) {
			assertEquals(contactResult, contactName);
		}
		
		//verify that RDNA User can be searched
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, CONFIG.getProperty("qa_user_2_name"));
		softPhoneContactsPage.clickSearchContactButton(driver1);
		contactResults = softPhoneContactsPage.getAllRingDnaNamesList(driver1);
		for (String contactResult : contactResults) {
			assertEquals(contactResult, CONFIG.getProperty("qa_user_2_name"));
		}
		//verify no search options available
		softPhoneContactsPage.verifyAdvanceSearchOptionNotAvailable(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		
	    System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	  public void contact_search_other_Number(){
	    System.out.println("Test case --contact_search_other_Number()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String otherNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_admin_user_number"));
	    String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
	    String contactName = CONFIG.getProperty("qa_user_3_name") + " Automation";
		
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    softPhoneContactsPage.deleteAllContacts(driver1, otherNumber, "");
	    
	    //Make call to other number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, otherNumber);
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
	    //add Home number to existing contact
	    callScreenPage.addOtherCallerToExistingContact(driver1, contactNumber);
	    softPhoneContactsPage.searchUntilContactPresent(driver1, HelperFunctions.getNumberInSimpleFormat(otherNumber));
	    
	    //verifying other number is appearing on contact search page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, contactNumber);
	    assertTrue(softPhoneContactsPage.isSalesforceContactPresent(driver1, contactName));
	    assertEquals(softPhoneContactsPage.getOtherNumber(driver1, 0), otherNumber);
	    
	    //Make call to other number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, otherNumber);
	    assertEquals(callScreenPage.getCallerOtherNumber(driver1, 0), otherNumber);
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    assertEquals(callScreenPage.getCallerOtherNumber(driver1, 0), otherNumber);
	    
	    //verifying other number after opening from call history
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(callScreenPage.getCallerOtherNumber(driver1, 0), otherNumber);
	    
	    //verify other number in salesforce
	    callScreenPage.openCallerDetailPage(driver1);
	    assertEquals(contactDetailPage.getCallerOtherPhone(driver1), HelperFunctions.getNumberInSimpleFormat(otherNumber));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify number in add to existing
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.clickAddToExistingButton(driver1);
	    callScreenPage.enterTextAndExistingContact(driver1, otherNumber);
	    assertEquals(callScreenPage.getExistingContactSearchedNumbers(driver1, 3), otherNumber);
	    
	    //reload softphone
	    reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	@Test(groups={"MediumPriority", "Product Sanity"})
	  public void add_contacts_from_search_contact()
	  {
		System.out.println("Test case --add_contacts_from_search_contact-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String contactNumber	= CONFIG.getProperty("prod_user_2_number").trim();
	    String contactName		= CONFIG.getProperty("prod_user_2_name").trim();

	    //enable add new contact from contact search page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountSalesforceTab.selectContactAsAddNewDefault(driver1);;
		accountSalesforceTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// verify that lead is appearing for the number
	    softPhoneContactsPage.deleteAllContacts(driver1, contactNumber, contactName);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//add caller as contact
		softPhoneContactsPage.clickAddNewRecordButton(driver1);
		assertTrue(callScreenPage.isContactOptionSelected(driver1));
		callScreenPage.enterContactDetails(driver1, contactName.trim(), CONFIG.getProperty("contact_account_name"));
		callScreenPage.enterAllAdditionalNumber(driver1, contactNumber, "9876543210", "9876543211", "9876543212", "9876543213");
		callScreenPage.clickCancelContactButton(driver1);
		
		softPhoneContactsPage.clickAddNewRecordButton(driver1);
		assertTrue(callScreenPage.isContactOptionSelected(driver1));
		callScreenPage.enterContactDetails(driver1, contactName.trim(), CONFIG.getProperty("contact_account_name"));
		callScreenPage.enterAllAdditionalNumber(driver1, contactNumber, "9876543210", "9876543211", "9876543212", "9876543213");
		callScreenPage.clickSaveContactButton(driver1);
		String callerName = callScreenPage.getCallerName(driver1);
		
		//verify that caller is added
		assertTrue(callerName.contains(contactName));
		
		//verify that caller is added as contact in salesforce
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.verifyContactPageOpen(driver1);
		assertTrue(contactDetailPage.getCallerName(driver1).contains(callerName));
		contactDetailPage.closeTab(driver1);
		contactDetailPage.switchToTab(driver1, 1);
		
	    //enable add new as lead from contact search page
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountSalesforceTab.selectLeadAsAddNewDefault(driver1);;
		accountSalesforceTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// delete all contacts and open contact search page
	    softPhoneContactsPage.deleteAllContacts(driver1, contactNumber, contactName);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//add caller as Lead
		softPhoneContactsPage.clickAddNewRecordButton(driver1);
		assertTrue(callScreenPage.isLeadOptionSelected(driver1));
		callScreenPage.addLeadDetails(driver1, contactName.trim(), CONFIG.getProperty("contact_account_name"));
		callScreenPage.enterPhoneNumber(driver1, contactNumber);
		callScreenPage.clickSaveContactButton(driver1);
		callerName = callScreenPage.getCallerName(driver1);
		
		//verify caller is added as lead
		assertTrue(callerName.contains(contactName));
		assertTrue(callScreenPage.isLeadImageVisible(driver1));
		
		//verify that caller is added as contact in salesforce
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.verifyLeadPageOpen(driver1);
		assertTrue(contactDetailPage.getCallerName(driver1).contains(callerName));
		contactDetailPage.closeTab(driver1);
		contactDetailPage.switchToTab(driver1, 1);
	 	
	    //enable add new contact from contact search page
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountSalesforceTab.selectContactAsAddNewDefault(driver1);;
		accountSalesforceTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	@Test(groups={"MediumPriority"})
	  public void verify_lead_contacts_fields()
	  {
		System.out.println("Test case --verify_lead_contacts_fields-- started ");

	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
		
		// verify that only editable fields are appearing for contact
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.clickAddNewRecordButton(driver1);
		callScreenPage.verifyAllContactLeadFields(driver1, "Contact");
		
		// verify that only editable fields are appearing for lead
		callScreenPage.selectLeadOption(driver1);
		callScreenPage.verifyAllContactLeadFields(driver1, "Lead");
		
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	//Verify agent able to search favorite contact by name
	@Test(groups = {"Regression"})
	public void search_fav_contact() {
		System.out.println("Test case --search_fav_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//Declaring fav contacts details to add
		String manualContactName1 		= "automation contact " + helperFunctions.GetCurrentDateTime();
		String manualContactNumber1 	= "987654321";
		String manualContactName2 		= "automation User " + helperFunctions.GetCurrentDateTime();
		String manualContactNumber2 	= "987654320";
		
		//Add first contact as favorite manually
		softPhoneContactsPage.addFavContactManually(driver1, manualContactName1, manualContactNumber1);
		
		//Verify that contact is added as favorite
		assertTrue(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName1) >= 0);
		
		//Add second contact as favorite manually
		softPhoneContactsPage.clickAddFavContactBtn(driver1);
		softPhoneContactsPage.addFavContactManualDetails(driver1, manualContactName2, manualContactNumber2);
		
		//Verify that contact is added as favorite
		assertTrue(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName2) >= 0);
		
		//Search first added fav contact partial text search and verify that first contact appears in search ont second one
		softPhoneContactsPage.searchFavContact(driver1, "automation contact");
		assertTrue(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName1) >= 0);
		assertEquals(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName2), -1);
		
		//Searching first contact with full name and verifying that only one search result is there
		softPhoneContactsPage.searchFavContact(driver1, manualContactName1);
		assertEquals(softPhoneContactsPage.getFavoriteContactsList(driver1).size(), 1);
		assertEquals(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName1), 0);
		assertEquals(softPhoneContactsPage.getFavoriteContactsList(driver1).indexOf(manualContactName2), -1);
		
		//Remove Contact froms fav
		softPhoneContactsPage.searchFavContact(driver1, "");
		softPhoneContactsPage.removeContactFavorite(driver1, manualContactName1);
		softPhoneContactsPage.removeContactFavorite(driver1, manualContactName2);
		
		//Verify that favorite contacts have been removed
		softPhoneContactsPage.isContactNotFavorite(driver1, manualContactName1);
		softPhoneContactsPage.isContactNotFavorite(driver1, manualContactName2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	//Verify max 10 favorite contact listed, agent can see more by clicking load more icon
	@Test(groups = {"MediumPriority"})
	public void display_10_fav_contact() {
		System.out.println("Test case --display_10_fav_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Navigating to Favourtie contacts
		List<String> newFavContacts = new ArrayList<>();
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.navigateToFavoritePage(driver1);
		
		//click on load more button if it is already there
		if(softPhoneContactsPage.isLoadMorFavContacteBtnVisible(driver1)) {
			softPhoneContactsPage.clickLoadMorFavContacteBtn(driver1);
		}
		
		//Getting all the favourite contacts list appearing after clicking on load more icon
		List<String> favContactsList = softPhoneContactsPage.getFavoriteContactsList(driver1);
		
		//Verifying that if favorite contacts are less that 20 after clicking load more button then adding fav contacts to take count to 20
		if(!(favContactsList.size() >= 20)) {
			for (int i = 0; i <= 20 - favContactsList.size(); i++) {
				String manualContactName 	= "automation contact " + HelperFunctions.GetRandomString(4);
				String manualContactNumber 	= "98765432" + i;
				softPhoneContactsPage.clickAddFavContactBtn(driver1);
				softPhoneContactsPage.addFavContactManualDetails(driver1, manualContactName, manualContactNumber);
				newFavContacts.add(manualContactName);
			}
		}
		
		//reloading the softphone and navigating to Favorite contacts page
		reloadSoftphone(driver1);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.navigateToFavoritePage(driver1);
		favContactsList = softPhoneContactsPage.getFavoriteContactsList(driver1);
		
		//Verifying that by default only 10 contacts are appearing
		assertEquals(favContactsList.size(), 10);
		
		//Clicking on load more button and verifying that now total 20 favorite contacts are visible
		softPhoneContactsPage.clickLoadMorFavContacteBtn(driver1);
		favContactsList = softPhoneContactsPage.getFavoriteContactsList(driver1);
		assertEquals(favContactsList.size(), 20);
		
		//Removing the fav contacts added above to take the count to 20
		for (@SuppressWarnings("unused") String newFavCon : newFavContacts) {
			String firstFavContact = softPhoneContactsPage.getFavoriteContactsList(driver1).get(0);
			if(newFavContacts.contains(firstFavContact))
			{
				softPhoneContactsPage.clickActiveFavIcon(driver1, 0);
				softPhoneContactsPage.verifyFavContactInvisible(driver1, firstFavContact);
				
			}
		}

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	//Verify view Contact record from 'Frequently Contacted' section show addition field on contact card
	//Verify update contact record from the contact card view from 'Frequently Contacted' list
	@Test(groups = {"Regression"})
	public void open_frequently_contacted_contact() {
		System.out.println("Test case --open_frequently_contacted_contact()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, "");
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Open frequently contacted contact details
		String contactName = softPhoneContactsPage.openFreqCotactedContactDetails(driver1);
		List<String> customStatus = callScreenPage.getAllCustomFieldsNameList(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), contactName);
		callScreenPage.verifyCallerIsContact(driver1);
		assertTrue(customStatus.contains("Account City (Account)"));
		assertTrue(customStatus.contains("Address (CreatedBy)"));
		assertTrue(customStatus.contains("Created Date"));
		
		//edit the contact fields
		String oldTile = callScreenPage.getCallerTitle(driver1);
		callScreenPage.clickOnUpdateDetailLink(driver1);
		String title = "QA".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		callScreenPage.changeTitle(driver1, title);
		callScreenPage.clickSaveFieldsButton(driver1);
		
		assertEquals(callScreenPage.getCallerTitle(driver1), title);
		
		//edit the contact fields
		callScreenPage.clickOnUpdateDetailLink(driver1);
		callScreenPage.changeTitle(driver1, oldTile);
		callScreenPage.clickSaveFieldsButton(driver1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	//Verify view Lead record from 'frequently Contacted' show additional fields on contact card
	@Test(groups = {"Regression"})
	public void open_frequently_contacted_lead() {
		System.out.println("Test case --open_frequently_contacted_lead()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// search and make a call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
	    
		//receiving call from the contact
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hanging up with the active call
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Verifying that call is removed from the called number
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//open contact search page
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		softPhoneContactsPage.enterSearchContactText(driver1, "");
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneContactsPage.clickActiveContactsIcon(driver1);
		
		//Open frequently contacted contact details
		String leadName = softPhoneContactsPage.openFreqCotactedLeadDetails(driver1);;
		List<String> customStatus = callScreenPage.getAllCustomFieldsNameList(driver1);
		assertEquals(callScreenPage.getCallerName(driver1), leadName);
		callScreenPage.verifyCallerIsLead(driver1);
		assertTrue(customStatus.contains("Country (CreatedBy)"));
		assertTrue(customStatus.contains("City (CreatedBy)"));
		assertTrue(customStatus.contains("Company Name (CreatedBy)"));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = {"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void _afterMethod(ITestResult result) {
		if(result.getName().equals("contact_search_advanced_serach_off") || result.getName().equals("add_fav_contact_advanced_search_disabled") ) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableAdvancedSearchSetting(driver1);
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
		}else if(result.getName().equals("search_lead_left_company")) {
			//disable advance search
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.enableSOSLSearchSetting(driver1);;
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.closeTab(driver1);
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
		}
	}
}