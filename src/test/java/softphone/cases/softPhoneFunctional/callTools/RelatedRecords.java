/**
 * 
 */
package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewTask;
import softphone.source.salesforce.ContactDetailPage;
import softphone.source.salesforce.ContactDetailPage.ContactDetailsFields;
import support.source.accounts.AccountSalesforceTab;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class RelatedRecords extends SoftphoneBase{
	
	boolean additionalFieldsAdded = false;
	
	@BeforeClass(groups = {"Regression", "Sanity", "QuickSanity", "MediumPriority"})
	public void beforeClass() {

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		//add contact
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");		  
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}

	@Test(groups = { "Regression", "MediumPriority"})
	public void related_records_for_an_opportunity() {
		System.out.println("Test case --related_records_for_an_opportunity()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
		String caseName = CONFIG.getProperty("softphone_task_related_case");
		
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), caseName);
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
			
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    
	    //Select related records to filter
	    List<String> relatedRecords = new ArrayList<String>();
	    relatedRecords.add(opprotunity);
	    relatedRecords.add(caseName);
	    callScreenPage.selectRelatedRecordToFilter(driver1, relatedRecords.get(0));
	    callScreenPage.selectRelatedRecordToFilter(driver1, CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case"));
	    
	    //verify that related records are filtered on call activity page
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), opprotunity);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void related_records_campaign() {
		System.out.println("Test case --related_records_campaign()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String campaignName = CONFIG.getProperty("softphone_task_related_campaign");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_campaign"));
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), campaignName);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that relate record is still selected
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, campaignName);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), campaignName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
	    
	    //verify campaign doesn't have arrow icon present
	    callScreenPage.verifyRelatedRecordExpandIconNotPresent(driver1, campaignName);
	    
	    //verify edit icon is not visible
	    int index = callScreenPage.getRelatedRecordIndex(driver1, campaignName);
	    callScreenPage.verifyEditRelatedRecordIconInvisible(driver1, index);
	    
		//Verify call task has related campaign
		softPhoneActivityPage.editTask(driver1, callSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskDetails);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority"})
	public void related_records_account() {
		System.out.println("Test case --related_records_account()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String accountName = CONFIG.getProperty("softphone_task_related_Account");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, accountName);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), accountName);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), accountName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, accountName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void related_records_case() {
		System.out.println("Test case --related_records_case()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		 
		String caseName = CONFIG.getProperty("softphone_task_related_case");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), caseName);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    caseName = CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case");
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), CONFIG.getProperty("softphone_task_related_case"));
	    callScreenPage.verifyRelatedRecordPresent(driver1, caseName);
	    
	    //verify edit icon is not visible
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, caseName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority"})
	public void related_records_favorite_campaign() {
		System.out.println("Test case --related_records_favorite_campaign()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String campaign = CONFIG.getProperty("softphone_task_related_campaign");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Select Campaign as Favorite
		callToolsPanel.selectCampaignAsFavorite(driver1, campaign);
		callToolsPanel.clickRelatedRecordsIcon(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord,campaign);
		callToolsPanel.linkRelatedRecordFromFavorties(driver1, campaign);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), campaign);
	    callScreenPage.verifyRelatedRecordPresent(driver1, campaign);
	    
	    //remove campaign from favorites
		callToolsPanel.removeCampaignAsFavorite(driver1, campaign);
		callToolsPanel.isRecordRemovedFromFavorite(driver1, campaign);
		callToolsPanel.clickRelatedRecordsIcon(driver1);
		
		//Open campaign detail by clicking it from caller detail screen
		callScreenPage.verifyCampaignOpensInSalesforce(driver1, campaign);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void related_records_favorite_button_campaign_only() {
		System.out.println("Test case --related_records_favorite_button_campaign_only()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Verifying that favorite button is not visible for other related records
		callToolsPanel.removeCampaignAsFavorite(driver1, CONFIG.getProperty("softphone_task_related_campaign"));
		callToolsPanel.isRecordRemovedFromFavorite(driver1, CONFIG.getProperty("softphone_task_related_campaign"));
	    callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(),CONFIG.getProperty("softphone_task_related_case"));
	    assertFalse(callToolsPanel.isFavButtonVisible(driver1));
	    callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),CONFIG.getProperty("softphone_task_related_opportunity"));
	    assertFalse(callToolsPanel.isFavButtonVisible(driver1));
	    callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), CONFIG.getProperty("softphone_task_related_Account"));
	    assertFalse(callToolsPanel.isFavButtonVisible(driver1));
	    
	    //verifying that favorite button is visible for Campaign
	    callToolsPanel.searchRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), CONFIG.getProperty("softphone_task_related_campaign"));
	    assertTrue(callToolsPanel.isFavButtonVisible(driver1));
		callToolsPanel.clickRelatedRecordsIcon(driver1);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void related_records_open_record_in_salesforce() {
		System.out.println("Test case --related_records_open_record_in_salesforce()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
		
		//Verifying that favorite button is not visible for other related records
		callToolsPanel.verifyRelatedCampaignOnsalesforce(driver1, CONFIG.getProperty("softphone_task_related_campaign"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void related_records_account_from_suggestions() {
		System.out.println("Test case --related_records_account_from_suggestions()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String accountName = CONFIG.getProperty("contact_account_name");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, accountName);
	    callToolsPanel.selectAccountFromSuggestions(driver1, accountName);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void related_records_for_lead_with_campaign() {
		System.out.println("Test case --related_records_for_lead_with_campaign()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String campaignName = CONFIG.getProperty("softphone_task_related_campaign");
		String opprotunity 	= CONFIG.getProperty("softphone_task_related_opportunity");
		String caseName 	= CONFIG.getProperty("softphone_task_related_case");
		String accountName 	= CONFIG.getProperty("softphone_task_related_Account");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
			softPhoneCalling.pickupIncomingCall(driver4);
		}

		// add caller as Contact
		callScreenPage.addCallerAsLead(driver1, CONFIG.getProperty("contact_first_name"), CONFIG.getProperty("contact_account_name"));
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, campaignName);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(), campaignName);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByEmail, "CreatedByEmail", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByUserName, "CreatedByUserName", true, false, false);
		additionalFieldsAdded = true;
	
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		reloadSoftphone(driver1);
	    
		//Opening the caller detail page
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), campaignName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
	    
	    //verify edit icon is not visible
	    int index = callScreenPage.getRelatedRecordIndex(driver1, campaignName);
	    callScreenPage.verifyEditRelatedRecordIconInvisible(driver1, index);
	    
	    //verify that related record is linked
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, campaignName);
	    
		//Verify call task has related campaign
		softPhoneActivityPage.editTask(driver1, callSubject);
		softPhoneActivityPage.verifyTaskAllFields(driver1, taskDetails);
		
		//Make another call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//verify lasst related record present in the caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
	    
		//link with another related record
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(), opprotunity);
	    callToolsPanel.idleWait(15);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
		//verify both related records present in the caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    
	    //Make another call
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver1);
  		
  		//verify lasst related record present in the caller detail
  	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
  	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
  	    
  		//link with another related record
  	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), caseName);
  	    
  	    //hanging up with the call.
  	    softPhoneCalling.hangupActiveCall(driver4);
  	    softPhoneCalling.isCallBackButtonVisible(driver1);
  	    seleniumBase.idleWait(10);
  	    
  		//verify both related records present in the caller detail
  		caseName = CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case");
  	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
  	    callScreenPage.verifyRelatedRecordPresent(driver1, campaignName);
  	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
  	    callScreenPage.verifyRelatedRecordPresent(driver1, caseName);
  	    
	    //verify edit icon is not visible
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, caseName);
  	    
  	    //Make another call
  		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_1_number"));
  		softPhoneCalling.pickupIncomingCall(driver1);
  		
  		//add an account as related record
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), accountName);
  	    
  	    //hanging up with the call.
  	    softPhoneCalling.hangupActiveCall(driver4);
 	    seleniumBase.idleWait(10);
	    
	    //verify all icons are visible for account and opportunity
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, accountName);
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, opprotunity);
	    
		//verify click to sms for related record
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	    callScreenPage.clickRelatedRecordEmailIcon(driver1, "abhishek.gupta@metacube.com");
	    sfLightningEmailTemplate.sendEmail(driver1, "Test Subject", "This is a test Body");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByEmail");
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByUserName");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		additionalFieldsAdded = false;
		
		reloadSoftphone(driver1);
  	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "MediumPriority"})
	public void related_records_for_new_contact() {
		System.out.println("Test case --related_records_for_new_contact()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);
		
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
		
		softPhoneContactsPage.deleteAllContacts(driver1, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_2_name"), CONFIG.getProperty("no_opportunity_account"));
		callScreenPage.closeErrorBar(driver1);
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		softPhoneContactsPage.searchUntilContactPresent(driver1, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_2_number")));
		
		//verify that no related Records are associated
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver5);
		softPhoneCalling.hangupActiveCall(driver1);
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		callScreenPage.verifyRelatedRecordHeadingInvisible(driver1);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver5);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);		
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver5);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void related_records_filter_records() {
		System.out.println("Test case --related_records_filter_records()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		
	    //Select related records to filter
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
	    String caseName = CONFIG.getProperty("softphone_task_related_case");
		String accountName = CONFIG.getProperty("softphone_task_related_Account");
	    List<String> relatedRecords = new ArrayList<String>();
	    
		// Link related records with the user		
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(),caseName);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(),accountName);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
		softPhoneCalling.hangupActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver1);
		softPhoneActivityPage.idleWait(10);
	    
	    relatedRecords.add(opprotunity);
	    relatedRecords.add(caseName);
	    callScreenPage.selectRelatedRecordToFilter(driver1, relatedRecords.get(0));
	    softPhoneActivityPage.idleWait(5);
	    callScreenPage.selectRelatedRecordToFilter(driver1, CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case"));
	    
	    //verify that related records are filtered on call activity page
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), opprotunity);
	    
	    //Verify filter from the caller detail page
	    //verify that related records are filtered on call activity page
	    relatedRecords.remove(opprotunity);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.selectRelatedRecordToFilter(driver1, CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case"));
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //Verify filter from the contact card page
	    //Verify related record on Call History Page
	    String contactName = CONFIG.getProperty("prod_user_1_name").trim() + " Automation";	    
	    softPhoneContactsPage.openSfContactDetails(driver1, contactName);
	    softPhoneActivityPage.idleWait(5);
	    callScreenPage.selectRelatedRecordToFilter(driver1, CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case"));
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //Verify filter for the opportunity
	    //verify that related records are filtered on call activity page
	    relatedRecords.clear();
	    relatedRecords.add(opprotunity);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.selectRelatedRecordToFilter(driver1, relatedRecords.get(0));
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //Verify filter for the accounts
	    //verify that related records are filtered on call activity page
	    relatedRecords.clear();
	    relatedRecords.add(accountName);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.selectRelatedRecordToFilter(driver1, relatedRecords.get(0));
	    softPhoneActivityPage.idleWait(5);
	    softPhoneActivityPage.verifyRelatedRecordFilters(driver1, relatedRecords);
	    
	    //open related records in salesforce
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openRelatedRecordInSfdc(driver1, CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case"));
	    salesforceHomePage.verifyPageHeadingType(driver1, "Case", CONFIG.getProperty("softphone_task_related_case_number"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    callScreenPage.openRelatedRecordInSfdc(driver1, accountName);
	    salesforceAccountPage.verifyAccountsNameHeading(driver1, accountName);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    callScreenPage.openRelatedRecordInSfdc(driver1, opprotunity);
	    salesforceHomePage.verifyPageHeadingType(driver1, "Opportunity", opprotunity);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority" })
	public void related_records_additional_fields_case() {
		System.out.println("Test case --related_records_additional_fields_case()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		 
		String caseName = CONFIG.getProperty("softphone_task_related_case");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, CONFIG.getProperty("softphone_task_related_case_number"));
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Case.toString(), caseName);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    caseName = CONFIG.getProperty("softphone_task_related_case_number") + " - " + CONFIG.getProperty("softphone_task_related_case");
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), CONFIG.getProperty("softphone_task_related_case"));
	    callScreenPage.expandRelatedRecordDetails(driver1, caseName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, caseName);
	    //callScreenPage.verifyRelatedRecordFields(driver1, "Case Number", "00001005");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Status", "New");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Country (CreatedBy)", "India");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Alias (CreatedBy)", "yring");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Case Type", "Problem");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Priority", "Medium");
	    
	    // Search contact and open details
 		softPhoneContactsPage.openSfContactDetails(driver1, CONFIG.getProperty("prod_user_1_name"));
	    callScreenPage.verifyRelatedRecordPresent(driver1, caseName);
	    callScreenPage.expandRelatedRecordDetails(driver1, caseName);
 		callScreenPage.verifyRelatedRecordFields(driver1, "Status", "New");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Country (CreatedBy)", "India");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Alias (CreatedBy)", "yring");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Case Type", "Problem");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Priority", "Medium");
	    
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	/*
	 * @Test(groups = {"MediumPriority"}) public void
	 * related_records_for_opportunity_additional_fields() { System.out.
	 * println("Test case --related_records_for_opportunity_additional_fields()-- started "
	 * );
	 * 
	 * // updating the driver used initializeDriverSoftphone("driver1");
	 * driverUsed.put("driver1", true); initializeDriverSoftphone("driver2");
	 * driverUsed.put("driver2", true);
	 * 
	 * String opprotunity =
	 * CONFIG.getProperty("softphone_task_related_opportunity"); //Making outbound
	 * Call softPhoneCalling.softphoneAgentCall(driver1,
	 * CONFIG.getProperty("prod_user_1_number"));
	 * softPhoneCalling.pickupIncomingCall(driver2);
	 * 
	 * //Adding a related record to the call HashMap<SoftPhoneNewTask.taskFields,
	 * String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
	 * taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
	 * callToolsPanel.linkRelatedRecords(driver1,
	 * SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	 * 
	 * //hanging up with the call. softPhoneCalling.hangupActiveCall(driver2);
	 * softPhoneCalling.isCallBackButtonVisible(driver1); seleniumBase.idleWait(2);
	 * 
	 * //Opening the caller detail page String callSubject =
	 * callScreenPage.openCallerDetailPageWithCallNotes(driver1);
	 * 
	 * //clicking on recent call entry
	 * contactDetailPage.openRecentCallEntry(driver1, callSubject);
	 * 
	 * //verify related record in salesforce
	 * sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	 * seleniumBase.closeTab(driver1); seleniumBase.switchToTab(driver1, 1);
	 * 
	 * //verify related record on task and caller detail
	 * assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1,
	 * callSubject), opprotunity);
	 * callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	 * callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	 * callScreenPage.verifyRelatedRecordFields(driver1, "Account Name",
	 * "Metacube"); callScreenPage.verifyCustomFields(driver1, "Full Name (Owner)",
	 * "Piyush ringdna"); callScreenPage.verifyCustomFields(driver1, "Stage",
	 * "Prospecting");
	 * 
	 * 
	 * //Setting driver used to false as this test case is pass
	 * driverUsed.put("driver1", false); driverUsed.put("driver2", false);
	 * 
	 * System.out.println("Test case is pass"); }
	 */	
	@Test(groups = {"MediumPriority"})
	public void related_records_for_opportunity_update_additional_fields() {
		System.out.println("Test case --related_records_for_opportunity_update_additional_fields()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String opprotunity = CONFIG.getProperty("softphone_task_additional_opportunity");
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
	    //add caller as contact if unknown
	  	if (callScreenPage.isCallerUnkonwn(driver1)) {
	  		callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_1_name"), CONFIG.getProperty("contact_account_name"));
		}
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordFields(driver1, "Account Name", "Metacube");
	    callScreenPage.verifyRelatedRecordFields(driver1, "Full Name (Owner)", CONFIG.getProperty("qa_user_1_name"));
	    callScreenPage.verifyRelatedRecordFields(driver1, "Stage", "Prospecting");
	    
	    //verify Stage field is required
	    callScreenPage.editRelatedRecord(driver1, opprotunity);
	    callScreenPage.removeOpportunityStage(driver1);
	    seleniumBase.idleWait(2);
	    assertEquals(callScreenPage.getErrorText(driver1), "Please fill in the required fields.");
	    callScreenPage.closeErrorBar(driver1);
	    callScreenPage.cancelEditedRelatedRecord(driver1);
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByEmail, "CreatedByEmail", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByUserName, "CreatedByUserName", true, false, false);
		additionalFieldsAdded = true;
		List<String> webAppFiedlsList = accountSalesforceTab.getAddedSfdcFieldsList(driver1);
		Collections.sort(webAppFiedlsList);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		reloadSoftphone(driver1);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByEmail", "abhishek.gupta@metacube.com");
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByUserName", CONFIG.getProperty("qa_user_1_username"));
	    webAppFiedlsList.remove("Name");
	    assertEquals(callScreenPage.getRelatedRecordsFieldsList(driver1, opprotunity), webAppFiedlsList);
	    
		// Search contact and open details
		softPhoneContactsPage.openSfContactDetails(driver1, CONFIG.getProperty("prod_user_1_name"));
		assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByEmail", "abhishek.gupta@metacube.com");
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByUserName", CONFIG.getProperty("qa_user_1_username"));
	    webAppFiedlsList.remove("Name");
	    assertEquals(callScreenPage.getRelatedRecordsFieldsList(driver1, opprotunity), webAppFiedlsList);
	    
	    //verify click to sms for related record
	    callScreenPage.clickRelatedRecordEmailIcon(driver1, "abhishek.gupta@metacube.com");
	    sfLightningEmailTemplate.sendEmail(driver1, "Test Subject", "This is a test Body");
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByEmail");
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByUserName");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		additionalFieldsAdded = false;
		
		reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void related_records_for_accounts_update_additional_fields() {
		System.out.println("Test case --related_records_for_accounts_update_additional_fields()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String accountName = CONFIG.getProperty("softphone_task_related_Account");

		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, accountName);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), accountName);
	    	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify related record on task and caller detail
	    softphoneCallHistoryPage.openSecondRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), accountName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, accountName);
	    callScreenPage.expandRelatedRecordDetails(driver1, accountName);
	    callScreenPage.verifyRelatedRecordFields(driver1, "Account Phone", "(987) 654-3210");
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToAccountFieldsSection(driver1);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByEmail, "CreatedByEmail", true, false, false);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.CreatedByUserName, "CreatedByUserName", true, false, false);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		additionalFieldsAdded = true;
		
		reloadSoftphone(driver1);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(5);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    
	    //verify related record on task and caller detail
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), accountName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, accountName);
	    callScreenPage.expandRelatedRecordDetails(driver1, accountName);
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByEmail", "abhishek.gupta@metacube.com");
	    callScreenPage.verifyRelatedRecordFields(driver1, "CreatedByUserName", CONFIG.getProperty("qa_user_1_username"));
	    
	    //verify click to sms for related record
	    callScreenPage.verifyRelatedRecordFields(driver1, "Account Phone", "(987) 654-3210");
	    callScreenPage.verifyRelatedRecordSmsIconVisible(driver1, "(987) 654-3210");
	    callScreenPage.clickRelatedRecordSmsIcon(driver1, "(987) 654-3210");
	    
	    assertTrue(softPhoneActivityPage.isMsgTabActive(driver1));
	    assertEquals(callScreenPage.getCallerName(driver1), accountName);
	    
		//navigating to support page 
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToAccountFieldsSection(driver1);
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByEmail");
		accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByUserName");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		additionalFieldsAdded = false;
		
		reloadSoftphone(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = {"MediumPriority"})
	public void related_records_account_name_update() {
		System.out.println("Test case --related_records_account_name_update()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String accountName = CONFIG.getProperty("softphone_task_related_Account_update");
		String updatedaccountName = accountName + "updated";
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, accountName);
	    callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Account.toString(), accountName);
	    String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
	    //verify related record on task and caller detail
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), accountName);
	    callScreenPage.verifyRelatedRecordPresent(driver1, accountName);
	    
	    //verify all icons are visible for related records when it is not edited 
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, accountName);
	    
	    //verify edit icon is not visible for related records when it is edited
	    int index = callScreenPage.getRelatedRecordIndex(driver1, accountName);
	    callScreenPage.editRelatedRecord(driver1, accountName);
	    callScreenPage.verifyEditedRelatedRecordIconsInvisible(driver1, index);
	    
	    // verify all icons are visible when related record edit window is canceled
	    callScreenPage.cancelEditedRelatedRecord(driver1);
	    callScreenPage.verifyAllRelatedRecordIconsVisible(driver1, accountName);
	    
	    //update account name and verify it is updated in related record list
	    callScreenPage.editRelatedRecord(driver1, accountName);
	    callScreenPage.enterRelatedAccountName(driver1, updatedaccountName);
	    callScreenPage.idleWait(2);
	    callScreenPage.openRelatedRecordInSfdc(driver1, updatedaccountName);
	    salesforceAccountPage.verifyAccountsNameHeading(driver1, updatedaccountName);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
	    reloadSoftphone(driver1);
	    
	    //verify updated related record in call history
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), updatedaccountName);
	    
	    // verify updated related record under activity feeds
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(driver1, callSubject), updatedaccountName);
	    
	    //changing account name as previous
	    callScreenPage.editRelatedRecord(driver1, updatedaccountName);
	    callScreenPage.enterRelatedAccountName(driver1, accountName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression"})
	public void related_records_for_new_account_opportunity() {
		System.out.println("Test case --related_records_for_new_account_opportunity()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String opprotunity = "Automation Account Opportunity";
		String opprotunity1 = "Automation Account Opportunity 1";
		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		String contactFirstName = CONFIG.getProperty("prod_user_1_name");
		
		//delete and add caller as contact
		softPhoneContactsPage.deleteAllContacts(driver1, contactNumber, contactFirstName);
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.isMuteButtonEnables(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		callScreenPage.addCallerAsContact(driver1, contactFirstName, "Opportunity Automation Account");
		callScreenPage.closeErrorBar(driver1);
		
		//verify related record on caller detail
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity1);
	    
	    //verify related record in suggestion
	    callToolsPanel.clickRelatedRecordsIcon(driver1);
	    callToolsPanel.verifyOpportunityInSuggestion(driver1, opprotunity);
	    callToolsPanel.verifyOpportunityInSuggestion(driver1, opprotunity);
	    callToolsPanel.clickRelatedRecordsIcon(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void related_records_for_new_contact_through_salesforce() {
		System.out.println("Test case --related_records_for_new_contact_through_salesforce()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
		
		String number = "+536898765";
		String contactFirstName	 = "Automation ";
		String contactLastName	 = "New User";
		HashMap<ContactDetailPage.ContactDetailsFields, String> contactDetails = new HashMap<ContactDetailPage.ContactDetailsFields, String>();
		contactDetails.put(ContactDetailsFields.firstName, contactFirstName);
		contactDetails.put(ContactDetailsFields.lastName, contactLastName);
		contactDetails.put(ContactDetailsFields.phoneNumber, number);
		
		softPhoneContactsPage.deleteAllContacts(driver1, number, contactFirstName + contactLastName);
		softPhoneCalling.softphoneAgentCall(driver1, number);
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		//Add contact from salesforce
		sfCampaign.openSalesforceCampaignPage(driver1);
		contactDetailPage.clickContactTab(driver1);
		contactDetailPage.clickAddNewButton(driver1);
		contactDetailPage.enterSalesForceContactDetails(driver1, contactDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Search until the contact is added
	    softPhoneContactsPage.searchUntilContactPresent(driver1, number);
		
		//verify that no related Records are associated
		reloadSoftphone(driver1);
		softphoneCallHistoryPage.openRecentUnknownCallerEntry(driver1);
		callToolsPanel.isRelatedRecordsIconVisible(driver1);
		
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	
	    
		//Opening the caller detail page
	    callScreenPage.openCallerDetailPage(driver1);

		//clicking on recent call entry
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //verify that related record is selected in related record section
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, opprotunity);
	 
	    //delete the caller data and make it unknown again
	    softPhoneContactsPage.deleteAllContacts(driver1, number, contactFirstName + contactLastName);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void related_records_for_property() {
		System.out.println("Test case --related_records_for_property()-- started ");
		
		String property = "Automation Property1";
		
		//Login into softphone for perf account on which property realted record is ceated
		WebDriver propOtherOrgDriver = getDriver();
		SFLP.softphoneLogin(propOtherOrgDriver, CONFIG.getProperty("qa_test_site_name"), "lokeshperf@ringdna.com", "ebmdna0198");
		softPhoneSettingsPage.clickSettingIcon(propOtherOrgDriver);
		softPhoneSettingsPage.disableCallForwardingSettings	(propOtherOrgDriver);
		seleniumBase.openNewBlankTab(propOtherOrgDriver);
		seleniumBase.switchToTab(propOtherOrgDriver, seleniumBase.getTabCount(propOtherOrgDriver));
		propOtherOrgDriver.get("https://ringdnatestorg.my.salesforce.com/home/home.jsp");
		salesforceHomePage.switchToClassifcSF(propOtherOrgDriver);
		
		// open Support Page and disable call tools setting for per account
		loginSupport(propOtherOrgDriver);
		dashboard.clickAccountsLink(propOtherOrgDriver, "RingDNA Sequence Testing", "00D3i000000DmgzEAC");
		accountIntelligentDialerTab.openIntelligentDialerTab(propOtherOrgDriver);
		accountIntelligentDialerTab.disableCallToolsSetting(propOtherOrgDriver);
		accountIntelligentDialerTab.saveAcccountSettings(propOtherOrgDriver);
	    seleniumBase.closeTab(propOtherOrgDriver);
	    seleniumBase.switchToTab(propOtherOrgDriver, 1);
	    reloadSoftphone(propOtherOrgDriver);
	    
	    //Add a number as contact
		String contactNumber = CONFIG.getProperty("prod_user_1_number");		  
	    softPhoneContactsPage.deleteAndAddContact(propOtherOrgDriver, contactNumber, CONFIG.getProperty("prod_user_1_name"));
	    	
	    //change the subject of call activity
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(propOtherOrgDriver);
		String callSubject = callToolsPanel.changeAndGetCallSubject(propOtherOrgDriver);
		
		//Adding property type related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, property);
		callToolsPanel.linkRelatedRecords(propOtherOrgDriver, SoftPhoneNewTask.taskRelatedRecordsType.Property.toString(),property);
	    
		//Verify that property is associated with call activity and open it from Related records section into sfdc
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(propOtherOrgDriver, callSubject), property);	    
	    callScreenPage.openRelatedRecordInSfdc(propOtherOrgDriver, property);
	    
	    //clicking on recent call entry
		contactDetailPage.openRecentCallEntry(propOtherOrgDriver, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(propOtherOrgDriver, taskDetails);
	    seleniumBase.closeTab(propOtherOrgDriver);
	    seleniumBase.switchToTab(propOtherOrgDriver, 1);
	    
	    //Add a number as lead
	    softPhoneContactsPage.deleteAndAddLead(propOtherOrgDriver, contactNumber, CONFIG.getProperty("prod_user_1_name"));
	    
	    //Change the subject for the recent call activity for the lead
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(propOtherOrgDriver);
		callSubject = callToolsPanel.changeAndGetCallSubject(propOtherOrgDriver);
		
		//Link the property to the call activity
		callToolsPanel.linkRelatedRecords(propOtherOrgDriver, SoftPhoneNewTask.taskRelatedRecordsType.Property.toString(),property);
			    
		//Verify that property is associated with call activity and open it from Related records section into sfdc
	    assertEquals(softPhoneActivityPage.getTaskRelatedRecord(propOtherOrgDriver, callSubject), property);	    
	    callScreenPage.openRelatedRecordInSfdc(propOtherOrgDriver, property);
	    salesforceHomePage.verifyPageHeadingType(propOtherOrgDriver, "Property", property);
	    
	    //clicking on recent call entry
		contactDetailPage.openRecentCallEntry(propOtherOrgDriver, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(propOtherOrgDriver, taskDetails);
	    seleniumBase.closeTab(propOtherOrgDriver);
	    seleniumBase.switchToTab(propOtherOrgDriver, 1);
	    
	    //close the driver
	    propOtherOrgDriver.quit();
	    propOtherOrgDriver = null;
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority"})
	public void related_records_for_unknow_caller() {
		System.out.println("Test case --related_records_for_unknow_caller()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
		
		//delete the conatct detail for the number
		softPhoneContactsPage.deleteAllContacts(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));

		//make a call to the unknown number and verify that number is unknown
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver6);
		assertEquals(callScreenPage.isCallerUnkonwn(driver1), true);
		
		//change the subject of the call notes
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);		
		
		//Adding a related record of opportunity type to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
		
		//hangup the call verify the related record is selcted
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(2);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, opprotunity);
		
		//Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    seleniumBase.idleWait(3);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), opprotunity);
	    
		// open task in salesforce
		softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
		callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //add caller as contact and verify related record is still associated
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openRecentUnknownCallerEntry(driver1);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_3_name"), CONFIG.getProperty("contact_account_name"));
	    seleniumBase.idleWait(2);
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    
	    //delete the contact detail so number again becomes unknown
	    softPhoneContactsPage.deleteAllContacts(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
	    
	    //verify for inbound call
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		assertEquals(callScreenPage.isCallerUnkonwn(driver1), true);
		
		//change the call notes subject
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);		
		
		//Adding a related record to the call
		String capmaign = CONFIG.getProperty("softphone_task_related_campaign");
		HashMap<SoftPhoneNewTask.taskFields, String> inboundtaskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		inboundtaskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, capmaign);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(),capmaign);
	    
		//hangup the call verify the related record is selected
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(2);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, capmaign);
		
		//Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    seleniumBase.idleWait(3);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), capmaign);
	    
		// open task in salesforce
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	    callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, inboundtaskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //add caller as contact and verify related record is still associated
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openRecentUnknownCallerEntry(driver1);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_3_name"), CONFIG.getProperty("contact_account_name"));
	    seleniumBase.idleWait(2);
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, capmaign);
	    callScreenPage.verifyRelatedRecordPresent(driver1, capmaign);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}
	
	//
	@Test(groups = { "MediumPriority"})
	public void related_records_for_associate_unknow_to_existing() {
		System.out.println("Test case --related_records_for_associate_unknow_to_existing()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		aa_AddCallersAsContactsAndLeads();
		
		String opprotunity = CONFIG.getProperty("softphone_task_related_opportunity");
		
		//Make the number unkown type
		softPhoneContactsPage.deleteAllContacts(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));

		//Make a call to the number and verify that called number is unknown
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver6);
		assertEquals(callScreenPage.isCallerUnkonwn(driver1), true);
		
		//change the subject of the call notes
		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);		
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
		
		//hangup the call verify the related record is selected
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(2);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, opprotunity);
		
		//Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    seleniumBase.idleWait(3);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), opprotunity);
	    
		// open task in salesforce
		softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
		callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, taskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //add caller as contact and verify related record is still associated
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openRecentUnknownCallerEntry(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, CONFIG.getProperty("prod_user_1_name") + " Automation");
	    seleniumBase.idleWait(2);
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, opprotunity);
	    callScreenPage.verifyRelatedRecordPresent(driver1, opprotunity);
	    
	    //Make the contact of unknown type
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
	    aa_AddCallersAsContactsAndLeads();
	    softPhoneContactsPage.deleteAllContacts(driver1, CONFIG.getProperty("prod_user_3_number"), CONFIG.getProperty("prod_user_3_name"));
	    
	    //verify for inbound call
	    softPhoneCalling.softphoneAgentCall(driver6, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver1);
		assertEquals(callScreenPage.isCallerUnkonwn(driver1), true);

		//change the subject of the call notes
		callSubject = callToolsPanel.changeAndGetCallSubject(driver1);		
		
		//Adding a related record to the call
		String capmaign = CONFIG.getProperty("softphone_task_related_campaign");
		HashMap<SoftPhoneNewTask.taskFields, String> inboundtaskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		inboundtaskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, capmaign);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Campaign.toString(),capmaign);
	    
		//hangup the call verify the related record is selcted
		softPhoneCalling.hangupActiveCall(driver1);
		seleniumBase.idleWait(2);
		callToolsPanel.verifyRelatedRecordIsSelected(driver1, capmaign);
		
		//Verify related record on Call History Page
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    seleniumBase.idleWait(3);
	    assertEquals(softphoneCallHistoryPage.getHistoryRelatedRecord(driver1, 0), capmaign);
	    
		// open task in salesforce
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 0);
	    callScreenPage.openCallerDetailPage(driver1);
		salesforceHomePage.openTaskFromTaskList(driver1, callSubject);
		
		//verify related record in salesforce
	    sfTaskDetailPage.verifyTaskData(driver1, inboundtaskDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //add caller as contact and verify related record is still associated
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openRecentUnknownCallerEntry(driver1);
	    callScreenPage.addCallerToExistingContact(driver1, CONFIG.getProperty("prod_user_1_name") + " Automation");
	    seleniumBase.idleWait(2);
	    callToolsPanel.verifyRelatedRecordIsSelected(driver1, capmaign);
	    callScreenPage.verifyRelatedRecordPresent(driver1, capmaign);
	    
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	}

	//	Verify I should be able to send SMS to opportunity/account's number from related records section
	@Test(groups = {"MediumPriority"})
	public void related_records_phone_field_sms() {
		System.out.println("Test case --related_records_phone_field_sms()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		String opprotunity 	= "Automation Phone Opportunity";
		String oppNumber	= "(415) 855-9825";
		
		//navigating to support page 
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
		accountSalesforceTab.createAccountSalesForceField(driver1,  AccountSalesforceTab.SalesForceFields.PhoneNo, "PhoneNumber", true, false, false);
		additionalFieldsAdded = true;
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Adding a related record to the call
		HashMap<SoftPhoneNewTask.taskFields, String> taskDetails = new HashMap<SoftPhoneNewTask.taskFields, String>();
		taskDetails.put(SoftPhoneNewTask.taskFields.RelatedRecord, opprotunity);
		callToolsPanel.linkRelatedRecords(driver1, SoftPhoneNewTask.taskRelatedRecordsType.Opportunity.toString(),opprotunity);
	    
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
	    //Expand related record
	    callScreenPage.expandRelatedRecordDetails(driver1, opprotunity);
	    
		//click opportunity sms button
	    String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());	    
	    callScreenPage.clickCustomFieldSMS(driver1, "PhoneNumber", oppNumber);
	    softPhoneActivityPage.sendMessage(driver1, message1, 0);
	    
		//navigating to support page 
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountSalesforceTab.deleteAdditionalField(driver1, "PhoneNumber");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		additionalFieldsAdded = false;
		
		reloadSoftphone(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = {"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void _afterMethod(ITestResult result) {
		if((result.getStatus() == 2 || result.getStatus() == 3) ) {
			if((result.getName().equals("related_records_for_opportunity_update_additional_fields") || result.getName().equals("related_records_for_lead_with_campaign") || result.getName().equals("related_records_phone_field_sms")) && additionalFieldsAdded) {
				//navigating to support page 
				loginSupport(driver1);
				dashboard.clickAccountsLink(driver1);
				System.out.println("Account editor is opened ");
				accountSalesforceTab.openSalesforceTab(driver1);
				accountSalesforceTab.navigateToOpportunityFieldsSection(driver1);
				if(accountSalesforceTab.isAddditionalFieldCreated(driver1, "CreatedByEmail"))
					accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByEmail");
				if(accountSalesforceTab.isAddditionalFieldCreated(driver1, "CreatedByUserName"))
					accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByUserName");
				if(accountSalesforceTab.isAddditionalFieldCreated(driver1, "PhoneNumber"))
					accountSalesforceTab.deleteAdditionalField(driver1, "PhoneNumber");
				seleniumBase.closeTab(driver1);
				seleniumBase.switchToTab(driver1, 1);
				reloadSoftphone(driver1);
				additionalFieldsAdded = false;
			}else if(result.getName().equals("related_records_for_accounts_update_additional_fields") && additionalFieldsAdded) {
				//navigating to support page 
				loginSupport(driver1);
				dashboard.clickAccountsLink(driver1);
				System.out.println("Account editor is opened ");
				accountSalesforceTab.openSalesforceTab(driver1);
				accountSalesforceTab.navigateToAccountFieldsSection(driver1);
				if(accountSalesforceTab.isAddditionalFieldCreated(driver1, "CreatedByEmail"))
					accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByEmail");
				if(accountSalesforceTab.isAddditionalFieldCreated(driver1, "CreatedByUserName"))
					accountSalesforceTab.deleteAdditionalField(driver1, "CreatedByUserName");
				seleniumBase.closeTab(driver1);
				seleniumBase.switchToTab(driver1, 1);
				additionalFieldsAdded = false;
			}
		}
	}
}