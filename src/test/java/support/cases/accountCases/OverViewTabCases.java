package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftPhoneWebLeadsPage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.accounts.AccountAccountsTab;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLicenseTab;
import support.source.accounts.AccountLogsTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountSkillsTab;
import support.source.accounts.AccountsPage;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.calls.CallInspector;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.system.SystemPage;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class OverViewTabCases extends SupportBase {

	AccountOverviewTab overViewTab = new AccountOverviewTab();
	Dashboard dashboard = new Dashboard();
	UsersPage usersPage = new UsersPage();
	GroupsPage teamsPage = new GroupsPage();
	UserIntelligentDialerTab userIntelligentTab = new UserIntelligentDialerTab();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	AccountsPage accountsPage = new AccountsPage();
	AddSmartNumberPage addsmartNoPage = new AddSmartNumberPage();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	CallInspector callsPage = new CallInspector();
	SoftPhoneWebLeadsPage webLeadsPage = new SoftPhoneWebLeadsPage();
	SoftPhoneContactsPage softPhoneContactsPage = new SoftPhoneContactsPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	AccountSalesforceTab salesForceTab = new AccountSalesforceTab();
	AccountLicenseTab licensePage = new AccountLicenseTab();
	AccountBlockedNumbersTab blockNumberTab = new AccountBlockedNumbersTab();
	AccountLogsTab logsTab = new AccountLogsTab();
	AccountSkillsTab skillsTab = new AccountSkillsTab();
	SystemPage systemPage = new SystemPage();
	AccountAccountsTab accountsTab = new AccountAccountsTab();
	
	private String qa_user_name;
	private String numberToDial;

	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			qa_user_name = CONFIG.getProperty("qa_admin_user_name");
			numberToDial = CONFIG.getProperty("qa_admin_user_number");
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			qa_user_name = CONFIG.getProperty("qa_support_user_name");
			numberToDial = CONFIG.getProperty("qa_support_user_number");
		}
	}
	
	//Verify support / admin user able to Add Location for an account
	//Verify Support/ admin user able to Update Location for an account
	@Test(groups = { "Regression" })
	public void add_update_delete_location() {
		System.out.println("Test case --add_update_delete_location-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		
		String locationName = "AutoLocationName".concat(HelperFunctions.GetRandomString(3));
		String descriptionName = "AutoLocationDesc".concat(HelperFunctions.GetRandomString(3));
		String addressName = "AutoLocationAddress".concat(HelperFunctions.GetRandomString(3));
		
		// creating location and verifying
		System.out.println("Starting creating location");
		overViewTab.createLocation(supportDriver, locationName, descriptionName, addressName);
		boolean locationExists = overViewTab.checkLocationSaved(supportDriver, locationName);
		assertTrue(locationExists, String.format("Location: %s does not exists after creating", locationName));
		dashboard.clickOnUserProfile(supportDriver);		
		assertTrue(usersPage.isUserNameVisible(supportDriver, qa_user_name));
		dashboard.navigateToMySettings(supportDriver);
		assertTrue(usersPage.isUserNameVisible(supportDriver, qa_user_name));
		locationExists = usersPage.isLocationPresentOnUserDetails(supportDriver, locationName);
		assertTrue(locationExists, String.format("Location: %s does not exists after creating on User Details", locationName));
		
		//Check Location list in ascending order on User page
		assertTrue(usersPage.verifySortedLocationList(supportDriver));
		
		System.out.println("Starting updating location");
		String locationNameUpdate = "AutoLocationNameUpdate".concat(HelperFunctions.GetRandomString(3));
		String descriptionNameUpdate = "AutoLocationDescUpdate".concat(HelperFunctions.GetRandomString(3));
		String addressNameUpdate = "AutoLocationAddressUpdate".concat(HelperFunctions.GetRandomString(3));
		
		// updating location and verifying
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.updateLocationRecords(supportDriver, locationName, locationNameUpdate, descriptionNameUpdate, addressNameUpdate);
		locationExists = overViewTab.checkLocationSaved(supportDriver, locationNameUpdate);
		assertTrue(locationExists, String.format("Location: %s does not exists after updating", locationNameUpdate));
		
		//Check Location list in ascending order on Account page
		overViewTab.refreshCurrentDocument(supportDriver);
		assertTrue(HelperFunctions.verifyListAscendingCaseInsensitive(overViewTab.getAllLocationList(supportDriver)));
		
		dashboard.clickOnUserProfile(supportDriver);
		locationExists = usersPage.isLocationPresentOnUserDetails(supportDriver, locationNameUpdate);
		assertTrue(locationExists, String.format("Location: %s does not exists after updating on User Details", locationNameUpdate));
		
		// deleting location
		System.out.println("Starting deleting location");
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.deleteRecords(supportDriver, locationNameUpdate);
		locationExists = overViewTab.checkLocationSaved(supportDriver, locationNameUpdate);
		assertFalse(locationExists,	String.format("Location: %s exists even after deleting", locationNameUpdate));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void add_update_delete_holiday_schedule() {
		System.out.println("Test case --add_update_delete_holiday_schedule-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		
		overViewTab.deleteAllHolidaysIfExists(supportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName2 = "AutoHolidayEvent2".concat(HelperFunctions.GetRandomString(3));
		
		System.out.println("Starting creating holiday");
		overViewTab.createHolidaySchedule(supportDriver, holidayName, holidayDescriptionName, holidayEventName, holidayEventName2);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));

		// updating holiday schedule
		System.out.println("Starting updating holiday");
		String holidayNameUpdate = "AutoHolidayNameUpdate".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionNameUpdate = "AutoHolidayDescUpdate".concat(HelperFunctions.GetRandomString(3));
		String holidayEventUpdate = "AutoHolidayEventUpdate".concat(HelperFunctions.GetRandomString(3));
		
		overViewTab.updateHolidayScheduleRecords(supportDriver, holidayName, holidayNameUpdate, holidayDescriptionNameUpdate, holidayEventUpdate);
		holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayNameUpdate);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after updating", holidayNameUpdate));

		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(supportDriver, holidayNameUpdate);
		holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayNameUpdate);
		assertFalse(holidayScheduleExists, String.format("Holiday Schedule: %s exists even after deleting", holidayNameUpdate));
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "MediumPriority" })
	public void update_delete_event_into_holiday_schedule() {
		System.out.println("Test case --update_delete_event_into_holiday_schedule-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		
		overViewTab.deleteAllHolidaysIfExists(supportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String holidayEventUpdate = "AutoHolidayEventUpdate".concat(HelperFunctions.GetRandomString(3));
		
		System.out.println("Starting creating holiday");
		
		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 1, 1);
		
		overViewTab.createHolidaySchedule(supportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));

		//updating events into holiday
		String newStartDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 2, 2);
		String newEndDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 3, 3, 3);
		overViewTab.updateEventsIntoHolidayScheduleRecords(supportDriver, holidayName, holidayEventName, startDate, endDate, newStartDate, newEndDate, holidayEventUpdate);
	
		//deleting events into holiday
		overViewTab.deleteEventsIntoHolidayScheduleRecords(supportDriver, holidayName, holidayEventUpdate);
		
		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(supportDriver, holidayName);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --update_delete_event_into_holiday_schedule-- passed ");
	}
	
	//Verify validation message for wrong dates ranges (To - From)
	@Test(groups = { "MediumPriority" })
	public void check_validation_msg_when_select_from_date_greater_after_to_date() {
		System.out.println("Test case --check_validation_msg_when_select_from_date_greater_after_to_date-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String holidayEventUpdate = "AutoHolidayEventUpdate".concat(HelperFunctions.GetRandomString(3));
		
		System.out.println("Starting creating holiday");
		
		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 1, 1);
		
		overViewTab.createHolidaySchedule(supportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));

		//updating events into holiday
		String newStartDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 2, 0, 0);  
		String newEndDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy"); 
		overViewTab.verifyValidationMsgEventHoliday(supportDriver, holidayName, holidayEventName, newStartDate, newEndDate, holidayEventUpdate);
	
		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(supportDriver, holidayName);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --check_validation_msg_when_select_from_date_greater_after_to_date-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void add_delete_central_number() {
		System.out.println("Test case --add_delete_central_number-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		// adding and verifying text in transcription blacklist textbox
		String voiceMailTrans = "AutoVoicemailTrans".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.enterTextVoicemailTranscription(supportDriver, voiceMailTrans);
		
		//adding central number
		String centralLabelName =  "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
		Pair<String,List<String>> centralNumberPair = accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
		String centralNumber = centralNumberPair.first();
		boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumber);
		assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));

		//verifying reassign btn disabled for added central number
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		addsmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addsmartNoPage.clickNextButton(supportDriver);
		addsmartNoPage.searchSmartNo(supportDriver, centralNumber);
		assertTrue(addsmartNoPage.isReAssignBtnDisabled(supportDriver, centralNumber));
		addsmartNoPage.closeSmartNumberWindow(supportDriver);
		
		// Opening manage call flow and searching call flow
		String callFlow = CONFIG.getProperty("qa_call_flow_new_org");
		String account = CONFIG.getProperty("qa_user_account");
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlow, account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);

		// Reassigning central number to call flow
		String label = "AutoLabel".concat(HelperFunctions.GetRandomString(2));
		callFlowPage.clickAddSmartNoIcon(supportDriver);
		addsmartNoPage.selectAndReassignNumber(supportDriver, centralNumber, label, null, null);

		//verifying call flow present in smart numbers tab
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, centralNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, centralNumber);
		assertTrue(smartNumbersPage.isCallFlowPresent(supportDriver, callFlow));
				
		//deleting call flow
		smartNumbersPage.deleteOnSmartNumberPage(supportDriver, callFlow);
		assertFalse(smartNumbersPage.isCallFlowPresent(supportDriver, callFlow));
		
		//verifying central number not present after assigning
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumber);
		assertFalse(centralNoExists, String.format("Central number :%s exists after deleting", centralNumber));
		
		//deleting smart number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, centralNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, centralNumber);
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_central_number-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void add_multiple_smart_number() {
		System.out.println("Test case --add_multiple_smart_number-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String centralLabelName =  "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//adding multiple smart numbers
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		Pair<String,List<String>> smartNoPair = addsmartNoPage.addNewSmartNumber(supportDriver, AddSmartNumberPage.Country.UNITED_STATES.displayName(), null, "22", centralLabelName, Type.Additional.toString(), SmartNumberCount.Multiple.toString());
		for(String centralSmartNo: smartNoPair.second()) {
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralSmartNo);
			assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));
		}
		
		//deleting central number
		for(String centralSmartNo: smartNoPair.second()) {
			accountIntelligentTab.deleteCentralNumber(supportDriver, centralSmartNo);
		}

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_multiple_smart_number-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_delete_existing_account() {
		System.out.println("Test case --verify_delete_existing_account-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);

		//opening account
		String accountToDelete = CONFIG.getProperty("test_account");
		String salesForceId = CONFIG.getProperty("test_account_sf_id");
		dashboard.clickAccountsLink(supportDriver, accountToDelete, salesForceId);
		
		if(overViewTab.isUndeleteAccountVisible(supportDriver)){
			overViewTab.unDeleteAccount(supportDriver);
		}
		
		//deleting and verifying account
		overViewTab.deleteAccount(supportDriver);
		String deletedDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		dashboard.navigateAndVerifyAccountDeleted(supportDriver, accountToDelete, salesForceId, deletedDate);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_delete_existing_account-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void existing_number_reassign_queue_number_to_central() {
		
		System.out.println("Test case --existing_number_reassign_queue_number_to_central-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetRandomString(3));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetRandomString(3));
		String smartLabel = "AutoSmartlabel".concat(HelperFunctions.GetRandomString(3));
		
		//adding new group details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		
		//creating smart number
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String,List<String>> smartNumber = addsmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		
		// opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		// converting queue number to central
		String centralLabelName = "AutoQueueToCentral".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		addsmartNoPage.selectAndReassignNumber(supportDriver, smartNumber.first(), centralLabelName, null, null);
		boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, smartNumber.first());
		assertTrue(centralNoExists, String.format("Central number name:%s does not exists", centralLabelName));

		// deleting central number
		accountIntelligentTab.deleteCentralNumber(supportDriver, smartNumber.first());
		centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, smartNumber.first());
		assertFalse(centralNoExists, String.format("Central number :%s exists after deleting", smartNumber.first()));

		//deleting queue
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, CONFIG.getProperty("qa_user_account"));
		callQueuePage.clickFirstCallQueue(supportDriver);
		
		//adding memeber and verifying
		callQueuePage.addMember(supportDriver, CONFIG.getProperty("qa_user_3_name"));
		assertTrue(callQueuePage.isAgentAddedAsMember(supportDriver,  CONFIG.getProperty("qa_user_3_name")));
		
		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_queue_number_to_central-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void existing_number_reassign_users_default_to_central() {
		
		System.out.println("Test case --existing_number_reassign_users_default_to_central-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		if(usersPage.isUnDeleteBtnVisible(supportDriver)) {
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if (!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addsmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
		}

		String defaultSmartNo = userIntelligentTab.getDefaultNo(supportDriver);
		
		// opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		// converting default number to central
		String centralLabelName = "AutoDefaultToCentral".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		addsmartNoPage.selectAndReassignNumber(supportDriver, defaultSmartNo, centralLabelName, null, null);
		boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, defaultSmartNo);
		assertTrue(centralNoExists, String.format("Central number name:%s does not exists", centralLabelName));

		// deleting central number
		accountIntelligentTab.deleteCentralNumber(supportDriver, defaultSmartNo);
		centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, defaultSmartNo);
		assertFalse(centralNoExists, String.format("Central number :%s exists after deleting", defaultSmartNo));

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_users_default_to_central-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void existing_number_reassign_users_additional_to_central() {
		
		System.out.println("Test case --existing_number_reassign_users_additional_to_central-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		if(usersPage.isUnDeleteBtnVisible(supportDriver)) {
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		String smartLabel = "AutoLabel".concat(HelperFunctions.GetRandomString(3));
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addsmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Additional.toString(),
					SmartNumberCount.Single.toString());
		}

		String additionalSmartNo = userIntelligentTab.getAdditionalNumberList(supportDriver).get(0);
		
		// opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		// converting additional number to central
		String centralLabelName = "AutoAdditioanlToCentral".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		addsmartNoPage.selectAndReassignNumber(supportDriver, additionalSmartNo, centralLabelName, null, null);
		boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, additionalSmartNo);
		assertTrue(centralNoExists, String.format("Central number name:%s does not exists", centralLabelName));

		// deleting central number
		accountIntelligentTab.deleteCentralNumber(supportDriver, additionalSmartNo);
		centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, additionalSmartNo);
		assertFalse(centralNoExists, String.format("Central number :%s exists after deleting", additionalSmartNo));

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_users_additional_to_central-- passed ");
	}
	
	//Lock setting not applied for blank call forwarding timeout at Account
	@Test(groups = { "MediumPriority"})
	public void verify_lock_settings_not_applied_for_blank_call_forwarding() {
		System.out.println("Test case --verify_lock_settings_not_applied_for_blank_call_forwarding-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);

		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		//entering call forwarding value and verifying lock not disabled
		accountIntelligentTab.enterCallForwardingTimeOutValue(supportDriver, "1");
		accountIntelligentTab.disableCallForwardingSetting(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.idleWait(2);
		assertFalse(accountIntelligentTab.isCallForwardingLockDisabled(supportDriver));
		
		//clearing value and verifying lock disabled
		accountIntelligentTab.clearCallForwardingTimeOutValue(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.disableCallForwardingSetting(supportDriver);
		accountIntelligentTab.idleWait(2);
		assertTrue(accountIntelligentTab.isCallForwardingLockDisabled(supportDriver));
		accountIntelligentTab.verifyToolTipCallFrwrdingLock(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_lock_settings_not_applied_for_blank_call_forwarding-- passed ");
	}

	@Test(groups = { "MediumPriority"})
	public void reassign_existing_multiple_numbers() {

		System.out.println("Test case --reassign_existing_multiple_numbers-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//adding multiple additional number for user if not present 
		String smartlabel = "AutoLabelAdditional".concat(HelperFunctions.GetRandomString(2));
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		if(usersPage.isUnDeleteBtnVisible(supportDriver)) {
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		if(userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).size() <= 1){
			userIntelligentTab.clickSmartNoIcon(supportDriver);
				addsmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartlabel,
						Type.Additional.toString(), SmartNumberCount.Multiple.toString());
		}
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//reassigning additional number
		String label = "AutoCentral".concat(HelperFunctions.GetRandomString(2));
		accountIntelligentTab.clickCentralNoAddIcon(supportDriver);
		addsmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addsmartNoPage.clickNextButton(supportDriver);
		
		addsmartNoPage.searchSmartNoByOwner(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		List<String> reassignNoList = addsmartNoPage.reassignMultipleAdditionalNumber(supportDriver);
		addsmartNoPage.clickNextButton(supportDriver);
		addsmartNoPage.enterLabelMultiple(supportDriver, label, reassignNoList);
		addsmartNoPage.saveSmartNo(supportDriver);
		
		// deleting reassigned number
		for (String number : reassignNoList) {
			accountIntelligentTab.deleteCentralNumber(supportDriver, number);
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, number);
			assertFalse(centralNoExists, String.format("Central number :%s exists after deleting", number));
		}
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --reassign_existing_multiple_numbers-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void remove_access_to_recordings_from_support() {
		
		System.out.println("Test case --remove_access_to_recordings_from_support-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening Accounts Overview Tab
		dashboard.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.enableRecordCallsSetting(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//adding s3 bucket value
		String s3BucketValue = "ringdna-jackson-test";
		accountIntelligentTab.enterS3RecordingBucketValue(supportDriver, s3BucketValue);
		accountIntelligentTab.enableApplyChangesS3Recording(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		overViewTab.switchToTab(supportDriver, 1);
		overViewTab.reloadSoftphone(supportDriver);
		
		//Initializing admin driver
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		
		//adding call subject
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(2));
		String callNotes = "AutoCallNotes".concat(HelperFunctions.GetRandomString(2));
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("qa_support_user_number"));
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.pickupIncomingCall(supportDriver);
		callToolsPanel.enterCallNotes(supportDriver, callSubject, callNotes);
		callToolsPanel.idleWait(2);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		//Fetching details from sfdc task page
		softPhoneActivityPage.openTaskInSalesforce(supportDriver, callSubject);
		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
		String callKeyId = sfTaskDetailPage.getCallObjectId(supportDriver);
		sfTaskDetailPage.closeTab(supportDriver);
		sfTaskDetailPage.switchToTab(supportDriver, 2);
		
		//Opening Calls Page
		dashboard.openCallInspectorPage(supportDriver);
		callsPage.getCallData(supportDriver, callKeyId);
		
		//verify Url's
		callsPage.verifyRecordingUrlRestricted(supportDriver);
		callsPage.verifyPlayerUrlRestricted(supportDriver);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//to verify url open
		accountIntelligentTab.clearS3RecordingBucketValue(supportDriver);
		accountIntelligentTab.enableApplyChangesS3Recording(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		overViewTab.switchToTab(supportDriver, 1);
		overViewTab.reloadSoftphone(supportDriver);
		
		// Opening Calls Page
		overViewTab.switchToTab(supportDriver, 2);
		dashboard.openCallInspectorPage(supportDriver);
		callsPage.getCallData(supportDriver, callKeyId);
		
		// verify Url's
		callsPage.verifyRecordingUrlOpen(supportDriver);
		callsPage.verifyPlayerUrlOpen(supportDriver);
		callsPage.clickAndVerifyRecordingUrl(supportDriver, s3BucketValue);
		callsPage.switchToTab(supportDriver, 2);
		
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		//updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --remove_access_to_recordings_from_support-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_new_queue_calls_coming_on_live_queue_section() {
		
		System.out.println("Test case --verify_new_queue_calls_coming_on_live_queue_section-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);

		//queue details
		String queueName = CONFIG.getProperty("new_qa_automation_queue");
		String queueNumber = CONFIG.getProperty("new_qa_automation_queue_number");
		
		String newDriverStringInitialize;
		WebDriver newDriverInitialize;
		
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			newDriverStringInitialize = "webSupportDriver";
			newDriverInitialize = webSupportDriver;
		}
		else{
			newDriverStringInitialize = "adminDriver";
			newDriverInitialize = adminDriver;
		}
		
		// initializing new driver
		initializeSupport(newDriverStringInitialize);
		driverUsed.put(newDriverStringInitialize, true);
		
		softPhoneCalling.switchToTab(newDriverInitialize, 1);
		softPhoneCalling.hangupIfInActiveCall(newDriverInitialize);
		softPhoneCalling.softphoneAgentCall(newDriverInitialize, queueNumber);
		
		//verifying live queue count is 1
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		overViewTab.refreshCurrentDocument(supportDriver);
		assertEquals(accountIntelligentTab.getCurrentSizeOfLiveQueue(supportDriver, queueName), "1");
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		
		softPhoneCalling.switchToTab(agentDriver, 2);
		dashboard.clickOnUserProfile(agentDriver);
		
		assertFalse(usersPage.isAddTeamIconVisible(agentDriver));
		assertFalse(usersPage.isAddUserSkillsIconVisible(agentDriver));
		assertTrue(usersPage.isNoUserSKillsTextVisible(agentDriver));
		
		dashboard.clickOnUserProfile(agentDriver);
		userIntelligentTab.openIntelligentDialerTab(agentDriver);
		assertFalse(userIntelligentTab.isAddSmartNoIconVisible(agentDriver));
		assertFalse(userIntelligentTab.isAddCallQueueIconVisible(agentDriver));
		
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, queueNumber);
		
		//verifying live queue count is 2
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		overViewTab.refreshCurrentDocument(supportDriver);
		assertEquals(accountIntelligentTab.getCurrentSizeOfLiveQueue(supportDriver, queueName), "2");
		
		//hanging up call
		softPhoneCalling.hangupIfInActiveCall(newDriverInitialize);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put("newDriverStringInitialize", false);
		System.out.println("Test case --verify_new_queue_calls_coming_on_live_queue_section-- passed ");
	}

	//Verify message when tries to ON Call Forwarding on Softphone
	@Test(groups = { "MediumPriority", "SupportOnly"})
	public void verify_webrtc_on_call_forwarding_off_settings() {
		
		System.out.println("Test case --verify_webrtc_on_call_forwarding_off_settings-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening Accounts Overview Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		//enable webrtc and disable call forwarding on over view tab
		accountIntelligentTab.enablewebRTCSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		accountIntelligentTab.disableCallForwardingSetting(supportDriver);
		accountIntelligentTab.lockCallForwarding(supportDriver);
		assertTrue(accountIntelligentTab.getCallForwardingLockBtnStatus(supportDriver).contains("/lock-active.svg"));
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		//verify call forwarding disabled on user intelligent
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isCallForwardingToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isCallForwardingNumberBoxDisabled(supportDriver));
		userIntelligentTab.verifyCallForwardingLockedText(supportDriver);
		
		//verify call forwarding not visible on settings page
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.verifyCallForwardingIsInvisible(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_webrtc_on_call_forwarding_off_settings-- passed ");
	}
	
	//WebRTC OFF then Call Forwarding ON by default
	//WebRTC ON then Call Forwarding OFF by default
	//WebRTC OFF and Call Forwarding ON: Lock button is set to locked and is disabled from editing.
	//Verify message when tries to OFF Call Forwarding on Softphone
	@Test(groups = { "MediumPriority", "SupportOnly"})
	public void verify_webrtc_on_call_forwarding_on_settings() {
		
		System.out.println("Test case --verify_webrtc_on_call_forwarding_on_settings-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// updating admin driver
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		// Opening Accounts Overview Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//disable wrtc
		accountIntelligentTab.disablewebRTCSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		assertTrue(accountIntelligentTab.getCallForwardingLockBtnStatus(supportDriver).contains("/lock-disabled.svg"));
		
		//verify call forwarding visible on settings page
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(supportDriver);
		
		//disable call forwarding and check error
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		softPhoneSettingsPage.IsNotificationErrorMessageVisible(supportDriver);
		softPhoneSettingsPage.closeErrorMessage(supportDriver);
		softPhoneSettingsPage.enableCallForwardingSettings(supportDriver);
		softPhoneSettingsPage.switchToTab(supportDriver, 2);

		//enable webrtc and call forwarding on over view tab
		accountIntelligentTab.enablewebRTCSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		assertTrue(accountIntelligentTab.getCallForwardingLockBtnStatus(supportDriver).contains("/unlock-disabled.svg"));
		
		//verify call forwarding not disabled on user intelligent
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isCallForwardingToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isCallForwardingNumberBoxDisabled(supportDriver));
		
		// verifying on admin driver's user intelligent
		dashboard.switchToTab(adminDriver, 2);
		dashboard.clickOnUserProfile(adminDriver);
		userIntelligentTab.openIntelligentDialerTab(adminDriver);
		assertTrue(userIntelligentTab.isCallForwardingToggleBtnEditable(adminDriver));
		assertFalse(userIntelligentTab.isCallForwardingNumberBoxDisabled(adminDriver));
		
		userIntelligentTab.disableCallForwarding(adminDriver);
		userIntelligentTab.saveAcccountSettings(adminDriver);

		//verify call forwarding visible on settings page
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(supportDriver);
		
		//disabling call forwarding
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.disableCallForwardingSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_webrtc_on_call_forwarding_on_settings-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void lock_unlock_disable_offline_forwarding_setting_from_account_when_off() {
		
		System.out.println("Test case --lock_unlock_disable_offline_forwarding_setting_from_account_when_off-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// preconditions for intelligent settings
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.enableCallForwarding(supportDriver);
		userIntelligentTab.enableOfflineForwardingDisableSetting(supportDriver);
		userIntelligentTab.saveAcccountSettings(supportDriver);

		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		//entering call forwarding value and verifying lock not disabled
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.disableCallForwardingDisabledOfflineSetting(supportDriver);
		accountIntelligentTab.lockCallForwardingDisabledOffline(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isDisableOfflineForwardingToggleBtnOff(supportDriver));
		userIntelligentTab.verifyDisableOfflineForwardingLockedText(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unlockCallForwardingDisabledOffline(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isDisableOfflineForwardingToggleBtnOff(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_disable_offline_forwarding_setting_from_account_when_off-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void lock_unlock_disable_offline_forwarding_setting_from_account_when_on() {
		
		System.out.println("Test case --lock_unlock_disable_offline_forwarding_setting_from_account_when_on-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for intelligent settings
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.enableCallForwarding(supportDriver);
		userIntelligentTab.disableOfflineForwardingDisableSetting(supportDriver);
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		
		//entering call forwarding value and verifying lock not disabled
		accountIntelligentTab.enableCallForwardingDisabledOfflineSetting(supportDriver);
		accountIntelligentTab.lockCallForwardingDisabledOffline(supportDriver);
		accountIntelligentTab.verifyToolTipDisableOfflineFrwrdingUnLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isDisableOfflineForwardingToggleBtnOff(supportDriver));
		userIntelligentTab.verifyDisableOfflineForwardingLockedText(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unlockCallForwardingDisabledOffline(supportDriver);
		accountIntelligentTab.verifyToolTipDisableOfflineFrwrdingLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isDisableOfflineForwardingToggleBtnOff(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_disable_offline_forwarding_setting_from_account_when_on-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void lock_unlock_call_forwarding_prompt_setting_from_account_when_on() {
		
		System.out.println("Test case --lock_unlock_call_forwarding_prompt_setting_from_account_when_on-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for intelligent settings
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.enableCallForwarding(supportDriver);
		userIntelligentTab.disableCallForwardingPrompt(supportDriver);
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		
		//entering call forwarding value and verifying lock not disabled
		accountIntelligentTab.enableCallForwardingPrompt(supportDriver);
		accountIntelligentTab.lockCallForwardingPrompt(supportDriver);
		accountIntelligentTab.verifyToolTipCallForwardingPromptUnLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isCallForwardingPromptToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isCallForwardingPromptToggleBtnOff(supportDriver));
		userIntelligentTab.verifyCallForwardingPromptLockedText(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unlockCallForwardingPrompt(supportDriver);
		accountIntelligentTab.verifyToolTipCallForwardingPromptLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isCallForwardingPromptToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isCallForwardingPromptToggleBtnOff(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_call_forwarding_prompt_setting_from_account_when_on-- passed ");
	}

	@Test(groups = { "MediumPriority"})
	public void lock_unlock_call_forwarding_prompt_setting_from_account_when_off() {
		
		System.out.println("Test case --lock_unlock_call_forwarding_prompt_setting_from_account_when_off-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for intelligent settings
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.enableCallForwarding(supportDriver);
		userIntelligentTab.enableCallForwardingPrompt(supportDriver);
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		
		//entering call forwarding value and verifying lock not disabled
		accountIntelligentTab.disableCallForwardingPrompt(supportDriver);
		accountIntelligentTab.lockCallForwardingPrompt(supportDriver);
		accountIntelligentTab.verifyToolTipCallForwardingPromptUnLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isCallForwardingPromptToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isCallForwardingPromptToggleBtnOff(supportDriver));
		userIntelligentTab.verifyCallForwardingPromptLockedText(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unlockCallForwardingPrompt(supportDriver);
		accountIntelligentTab.verifyToolTipCallForwardingPromptLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isCallForwardingPromptToggleBtnEditable(supportDriver));
		assertTrue(userIntelligentTab.isCallForwardingPromptToggleBtnOff(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_call_forwarding_prompt_setting_from_account_when_off-- passed ");
	}

	@Test(groups = { "MediumPriority"})
	public void lock_unlock_web_lead_setting_from_account_when_on() {
		
		System.out.println("Test case --lock_unlock_web_lead_setting_from_account_when_on-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for user intelligent settings
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.disableWebLeadsSetting(supportDriver);
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		//preconditions for account intelligent settings
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.enableWebLeadsSetting(supportDriver);
		accountIntelligentTab.lockWebLeadSetting(supportDriver);
		accountIntelligentTab.verifyToolTipWebLeadsUnLock(supportDriver);
		accountIntelligentTab.idleWait(1);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
	
		//verify on user inteligent page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isWebLeadsToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isWebLeadsToggleBtnOff(supportDriver));
		userIntelligentTab.verifyWebLeadsLockedText(supportDriver);
		
		//verifying web leads visible on softphone
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		assertTrue(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		
		//unlock web lead settings
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unlockWebLeadSetting(supportDriver);
		accountIntelligentTab.verifyToolTipWebLeadsLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		//verify on user inteligent page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isWebLeadsToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isWebLeadsToggleBtnOff(supportDriver));
		
		//verifying web leads visible on softphone
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		assertTrue(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_web_lead_setting_from_account_when_on-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void lock_unlock_web_lead_setting_from_account_when_off() {
		
		System.out.println("Test case --lock_unlock_web_lead_setting_from_account_when_off-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for intelligent settings
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.disableWebLeadsSetting(supportDriver);
		accountIntelligentTab.lockWebLeadSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isWebLeadsSectionVisible(supportDriver));
		
		// Opening Accounts Overview Tab
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		assertFalse(webLeadsPage.isWebLeadsSectionVisible(supportDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --lock_unlock_web_lead_setting_from_account_when_off-- passed ");
	}
	
	@Test(groups = { "MediumPriority", "Product Sanity"})
	public void lock_unlock_unavailable_flow_when_set_none() {
		
		System.out.println("Test case --lock_unlock_unavailable_flow_when_set_none-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//preconditions for account overview
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		//Setting unavailable call flow to none
		accountIntelligentTab.unLockUnavailableFlow(supportDriver);
		accountIntelligentTab.selectUnvailableFlowSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		// preconditions for user intelligent settings
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.selectUnavailableCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_new_org"));
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.selectUnvailableFlowSetting(supportDriver);
		accountIntelligentTab.lockUnavailableFlow(supportDriver);
		accountIntelligentTab.verifyToolTipUnavailableFlowUnLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		//verify on user inteligent page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertEquals(userIntelligentTab.getUnavailableCallFlowSelectedFlow(supportDriver), "(none)");
		assertFalse(userIntelligentTab.isUnavailableCallFlowEditable(supportDriver));
		userIntelligentTab.verifyUnavailableCallFlowLockedText(supportDriver);
		
		// Opening Accounts Overview Tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.unLockUnavailableFlow(supportDriver);
		accountIntelligentTab.verifyToolTipUnavailableFlowLock(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		//verify on user inteligent page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isUnavailableCallFlowEditable(supportDriver));
		userIntelligentTab.selectUnavailableCallFlow(supportDriver, CONFIG.getProperty("qa_call_flow_new_org"));
		userIntelligentTab.saveAcccountSettings(supportDriver);
		
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.softphoneAgentCall(adminDriver, numberToDial);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --lock_unlock_unavailable_flow_when_set_none-- passed ");
	}

	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_call_tracking_section_hidden_for_admin_when_disable_show_call_tracking() {
		
		System.out.println("Test case --verify_call_tracking_section_hidden_for_admin_when_disable_show_call_tracking-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		//opening account overview
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.verifyToolTipShowCallTracking(supportDriver);
		
		//disabling call tracking
		overViewTab.disableShowCallTrackingSetting(supportDriver);
		overViewTab.saveAcccountSettings(supportDriver);
		overViewTab.refreshCurrentDocument(supportDriver);
		overViewTab.waitForPageLoaded(supportDriver);
		assertTrue(dashboard.isCallTrackingVisible(supportDriver));

		//verifying on Admin User that call tracking now hidden
		dashboard.switchToTab(adminDriver, 2);
		dashboard.refreshCurrentDocument(adminDriver);
		assertFalse(dashboard.isCallTrackingVisible(adminDriver));
		
		//enabling call tracking on Support
		dashboard.switchToTab(supportDriver, 2);
		overViewTab.enableShowCallTrackingSetting(supportDriver);
		overViewTab.saveAcccountSettings(supportDriver);
		overViewTab.refreshCurrentDocument(supportDriver);
		overViewTab.waitForPageLoaded(supportDriver);
		assertTrue(dashboard.isCallTrackingVisible(supportDriver));

		// verifying on Admin User that call tracking now visible
		dashboard.switchToTab(adminDriver, 2);
		dashboard.refreshCurrentDocument(adminDriver);
		assertTrue(dashboard.isCallTrackingVisible(adminDriver));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --verify_call_tracking_section_hidden_for_admin_when_disable_show_call_tracking-- passed ");
	}
	
	@Test(groups = { "SupportOnly", "MediumPriority"})
	public void verify_account_alias_searchable_in_account_list() {
		
		System.out.println("Test case --verify_account_alias_searchable_in_account_list-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//entering account alias
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		overViewTab.enterAccountAlias(supportDriver, CONFIG.getProperty("qa_free_user_account_alias"));
		overViewTab.saveAcccountSettings(supportDriver);
		
		//refreshing and fetching account alias
		dashboard.refreshCurrentDocument(supportDriver);
		String accountAlias = overViewTab.getAccountAliasName(supportDriver);
		assertEquals(accountAlias, CONFIG.getProperty("qa_free_user_account_alias"));
		
		//opening call queue page
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		//opening call flow page
		dashboard.openCallFlowPage(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		//opening call teams page
		teamsPage.openGroupSearchPage(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		//opening smart numbers page
		dashboard.openSmartNumbersTab(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		//opening manage users page
		dashboard.openManageUsersPage(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		//opening messages page
		dashboard.navigateToMessagesSection(supportDriver);
		callQueuePage.verifyAccountExistsInDropDown(supportDriver, accountAlias);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_account_alias_searchable_in_account_list-- passed ");
	}
	
	//Overview Tab - Enable Settings
	@Test(groups = { "Regression" , "SupportOnly" })
	public void overview_tab_enable_settings() {
		System.out.println("Test case --overview_tab_enable_settings-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//enable settings
		accountIntelligentTab.enableClickToVoicemailSetting(supportDriver);
		accountIntelligentTab.enableCallForwardingSetting(supportDriver);
		accountIntelligentTab.enableStayConnectedSetting(supportDriver);
		accountIntelligentTab.enableAdvancedSearchSetting(supportDriver);
		accountIntelligentTab.enableSipClientSetting(supportDriver);
		accountIntelligentTab.enableCallNotifications(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);

		// verify on softphone
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.verifyCallForwardingIsVisible(supportDriver);
		softPhoneSettingsPage.verifyStayConnectedIsVisible(supportDriver);
		softPhoneSettingsPage.verifyVoicemailIsVisible(supportDriver);
		softPhoneSettingsPage.verifySipClientLabelVisible(supportDriver);
		softPhoneSettingsPage.verifyCallNotificationIsVisible(supportDriver);
		
		softPhoneContactsPage.clickActiveContactsIcon(supportDriver);
		softPhoneContactsPage.verifyAdvanceSearchOptionIsAvailable(supportDriver);
		
		// set default settings
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		accountIntelligentTab.SetDefaultIntelligentDialerSettings(supportDriver, "30", "1", "30");
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.setDefaultAccountOverviewSettings(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --overview_tab_enable_settings-- passed ");
	}
	
	
	// Overview Tab - Disable Settings
	@Test(groups = { "Regression", "SupportOnly"})
	public void overview_tab_disable_settings() {
		System.out.println("Test case --overview_tab_disable_settings-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//disable settings
		accountIntelligentTab.disableClickToVoicemailSetting(supportDriver);
		accountIntelligentTab.disableCallForwardingSetting(supportDriver);
		accountIntelligentTab.disableStayConnectedSetting(supportDriver);
		accountIntelligentTab.disableAdvancedSearchSetting(supportDriver);
		accountIntelligentTab.disableSipClientSetting(supportDriver);
		accountIntelligentTab.disableCallNotifications(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);

		// verify on softphone
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		//softPhoneSettingsPage.verifyCallForwardingIsInvisible(supportDriver);
		softPhoneSettingsPage.verifyStayConnectedIsInvisible(supportDriver);
		softPhoneSettingsPage.verifyVoicemailIsInvisible(supportDriver);
		softPhoneSettingsPage.verifySipClientLabelIsInvisible(supportDriver);
		softPhoneSettingsPage.verifyCallNotificationIsInvisible(supportDriver);
		
		softPhoneContactsPage.clickActiveContactsIcon(supportDriver);
		softPhoneContactsPage.verifyAdvanceSearchOptionNotAvailable(supportDriver);
		
		// set default settings
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		accountIntelligentTab.SetDefaultIntelligentDialerSettings(supportDriver, "30", "1", "30");
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.setDefaultAccountOverviewSettings(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --overview_tab_disable_settings-- passed ");
	}
	
	// Verify caller should route to voicemail after call decline by Agent when voicemail setting enabled for account
	@Test(groups = { "Regression" , "SupportOnly"})
	public void enable_voicemail_setting_and_verify_on_softphone() {
		System.out.println("Test case --enable_voicemail_setting_and_verify_on_softphone-- started ");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		// enable settings
		accountIntelligentTab.enableVoiceMailEnabledSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);

		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.switchToVoiceMailTab(supportDriver);
		int voicemialCount = softphoneCallHistoryPage.getMissedVoicemailCount(supportDriver);

		// created new webdriver 
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		// get smart number from softphone
		String smartNumber = CONFIG.getProperty("qa_support_user_number");
		String agentNumber = CONFIG.getProperty("qa_admin_user_number");
		
		// calling from agent to admin/support
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.softphoneAgentCall(adminDriver, smartNumber);
		softPhoneCalling.isCallHoldButtonVisible(adminDriver);

		// decline call
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneCalling.declineCall(supportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		// hangup from agent
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// verify on softphone
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.switchToVoiceMailTab(supportDriver);
		softphoneCallHistoryPage.isMissedVMCountIncreased(supportDriver, voicemialCount);
		String voicemailNumber = softphoneCallHistoryPage.getMissedVoicemailRecentNumber(supportDriver);
		assertEquals(voicemailNumber, agentNumber);

		// set default settings
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		accountIntelligentTab.SetDefaultIntelligentDialerSettings(supportDriver, "30", "1", "30");
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.setDefaultAccountOverviewSettings(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --enable_voicemail_setting_and_verify_on_softphone-- passed ");
	}
	
	
	// Verify caller should hangup without route to voicemail when voicemail setting disabled for account
	@Test(groups = { "Regression", "SupportOnly" })
	public void disable_voicemail_setting_and_verify_on_softphone() {
		System.out.println("Test case --disable_voicemail_setting_and_verify_on_softphone-- started ");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);

		// enable settings
		accountIntelligentTab.disableVoiceMailEnabledSetting(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);

		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.switchToVoiceMailTab(supportDriver);
		int voicemailCount = softphoneCallHistoryPage.getMissedVoicemailCount(supportDriver);

		// created new webdriver 
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);

		// get smart number from softphone
		String smartNumber = CONFIG.getProperty("qa_support_user_number");
		
		// calling from agent to admin/support
		softPhoneSettingsPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		softPhoneCalling.softphoneAgentCall(adminDriver, smartNumber);
		softPhoneCalling.isCallHoldButtonVisible(adminDriver);

		// decline call
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneCalling.declineCall(supportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		// hangup from agent
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// verify on softphone
		softphoneCallHistoryPage.openCallsHistoryPage(supportDriver);
		softphoneCallHistoryPage.switchToVoiceMailTab(supportDriver);
		int actualCount = softphoneCallHistoryPage.getMissedVoicemailCount(supportDriver);
		assertEquals(actualCount, voicemailCount);

		// set default settings
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		accountIntelligentTab.SetDefaultIntelligentDialerSettings(supportDriver, "30", "1", "30");
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.setDefaultAccountOverviewSettings(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		System.out.println("Test case --disable_voicemail_setting_and_verify_on_softphone-- passed ");
	}
	
	//Alert message upon login for Admin/Support users for holiday event of current date
	//Add holiday event of current date- Alert message will show on all logged in account user
	//Alert message for holiday event of current date shown on all tabs of an account except CAI tab
	//Alert message will remove,if event is deleted
	//Current day holiday alert message removed when agent update Holiday date to a future date
	@Test(groups = { "Regression"})
	public void add_holiday_and_verify_holiday_banner_on_different_tabs() {
		System.out.println("Test case --add_holiday_and_verify_holiday_banner_on_different_tabs-- started ");
		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		overViewTab.switchToTab(supportDriver, 2);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		
		overViewTab.deleteAllHolidaysIfExists(supportDriver);
		
		// creating holiday schedule
		String holidayName = "AutoHolidayName".concat(HelperFunctions.GetRandomString(3));
		String holidayDescriptionName = "AutoHolidayDesc".concat(HelperFunctions.GetRandomString(3));
		String holidayEventName = "AutoHolidayEvent".concat(HelperFunctions.GetRandomString(3));
		String startDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");
		String endDate = HelperFunctions.addMonthYearDateToExisting("MM/dd/yyyy", startDate, 1, 1, 1);
		
		System.out.println("Starting creating holiday");
		overViewTab.createHolidaySchedule(supportDriver, holidayName, holidayDescriptionName, holidayEventName, startDate, endDate);
		boolean holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayName);
		assertTrue(holidayScheduleExists, String.format("Holiday Schedule: %s does not exists after creating", holidayName));
		
		//assert holiday on every tabs header
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		salesForceTab.openSalesforceTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		licensePage.openLicensingTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		logsTab.navigateToAccountLogTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		skillsTab.navigateToAccountSkillsTab(supportDriver);
		overViewTab.verifyHolidayBannerOnHeader(supportDriver, holidayEventName);
		
		dashboard.clickConversationAI(supportDriver);
		overViewTab.verifyNoHolidayBannerOnHeader(supportDriver);
		
		supportDriver.navigate().back();
		dashboard.isPaceBarInvisible(supportDriver);
		
		//opening overview tab
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);

		// deleting holiday schedule
		System.out.println("Starting deleting holiday");
		overViewTab.deleteRecords(supportDriver, holidayName);
		holidayScheduleExists = overViewTab.checkHolidayScheduleSaved(supportDriver, holidayName);
		assertFalse(holidayScheduleExists, String.format("Holiday Schedule: %s exists even after deleting", holidayName));
		
		//assert holiday on every tabs header
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		overViewTab.verifyNoHolidayBannerOnHeader(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify the Sequence toggle renamed to Enable Guided Selling
	@Test(groups = { "Regression"})
	public void verify_sequence_toggle_renamed_guided_selling() {
		System.out.println("Test case --verify_sequence_toggle_renamed_guided_selling-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		overViewTab.switchToTab(supportDriver, 2);
		
		//opening overview tab
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.enableGuidedSellingToggleBtn(supportDriver);
		usersPage.disableGuidedSellingToggleBtn(supportDriver);
		usersPage.savedetails(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Provided create and delete metadata visibility for support
	@Test(groups = { "Regression", "SupportOnly"})
	public void verify_create_and_delete_metadata_visibility_for_support() {
		System.out.println("Test case --verify_create_and_delete_metadata_visibility_for_support-- started ");
		// initializing agent driver
		initializeSupport();
		driverUsed.put("supportDriver", true);
		usersPage.switchToTab(supportDriver, 2);
		
		//active account
		dashboard.clickAccountsLink(supportDriver);
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		//active account
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		
		//inactive account
		dashboard.clickAccountsLink(supportDriver, "Metacube Software Pvt Ltd", "00D0o000000SyHTEA0");
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		//inactive account
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		
		//open user manage
		dashboard.openManageUsersPage(supportDriver);
		usersPage.clickFirstDeletedUserAccountLink(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		assertTrue(usersPage.isDeletedByVisible(supportDriver));
		assertTrue(usersPage.isDateDeletedVisible(supportDriver));
		assertTrue(usersPage.isCreatedByVisible(supportDriver));
		//deleted user
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		
		//open user manage
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		assertTrue(usersPage.isDeletedByVisible(supportDriver));
		assertTrue(usersPage.isDateDeletedVisible(supportDriver));
		assertTrue(usersPage.isCreatedByVisible(supportDriver));
		//active user
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		
		//open smart number tab
		// getting smart number of type local presence
		dashboard.openSmartNumbersTab(supportDriver);
		//active number
		smartNumbersPage.searchSmartNumber(supportDriver, CONFIG.getProperty("qa_cai_caller_smart_no"));
		smartNumbersPage.clickSmartNoByIndex(supportDriver, 0);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		//active number
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		//delete active smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		// inactive number
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		//undelete
		smartNumbersPage.clickUndeleteBtn(supportDriver);
		
		//system > cleanup call recording
		dashboard.navigateToCleanUpRecordings(supportDriver);
		systemPage.setAccountForCleanUpRecording(supportDriver, CONFIG.getProperty("qa_user_account"));
		
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		//active account
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		
		//system > cleanup call recording
		//inactive account
		dashboard.navigateToCleanUpRecordings(supportDriver);
		systemPage.setAccountForCleanUpRecording(supportDriver, "ashokmetacube21");

		assertTrue(usersPage.isDeletedVisible(supportDriver));
		// inactive account
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("agentDriver", false);
		System.out.println("Test case is pass");
	}
	
	//Verify Provided create and delete metadata visibility for admin
	@Test(groups = { "Regression", "AdminOnly"})
	public void verify_create_and_delete_metadata_visibility_for_admin() {
		System.out.println("Test case --verify_create_and_delete_metadata_visibility_for_admin-- started ");
		// initializing agent driver
		initializeSupport();
		driverUsed.put("supportDriver", true);
		usersPage.switchToTab(supportDriver, 2);
		
		//open user manage
		dashboard.openManageUsersPage(supportDriver);
		usersPage.clickFirstDeletedUserAccountLink(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		assertTrue(usersPage.isDeletedByVisible(supportDriver));
		assertTrue(usersPage.isDateDeletedVisible(supportDriver));
		assertTrue(usersPage.isCreatedByVisible(supportDriver));
		//deleted user
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		
		//open user manage
		dashboard.clickOnUserProfile(supportDriver);
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		assertTrue(usersPage.isDeletedByVisible(supportDriver));
		assertTrue(usersPage.isDateDeletedVisible(supportDriver));
		assertTrue(usersPage.isCreatedByVisible(supportDriver));
		//active user
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		
		//open smart number tab
		// getting smart number of type local presence
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, CONFIG.getProperty("qa_cai_caller_smart_no"));
		smartNumbersPage.clickSmartNoByIndex(supportDriver, 0);
		dashboard.isPaceBarInvisible(supportDriver);
		
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		//active number
		assertFalse(usersPage.getisDeletedValue(supportDriver));
		
		smartNumbersPage.deleteSmartNumber(supportDriver);
		//assert
		assertTrue(usersPage.isDeletedVisible(supportDriver));
		// inactive number
		assertTrue(usersPage.getisDeletedValue(supportDriver));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("agentDriver", false);
		System.out.println("Test case is pass");
	}
	
	// Verify proper format of ringDNA brand name in Admin Console
	@Test(groups = { "Regression" })
	public void verify_proper_format_of_ringDNA_brand_name_in_admin_console() {
		System.out.println("Test case --verify_proper_format_of_ringDNA_brand_name_in_admin_console-- started ");

		// updating the supportDriver used
		System.out.println("in before method");

		supportDriver = getDriver();
		setMapDriverWithString();
		supportDriver.get("https://app-qa.ringdna.net/");
		driverUsed.put("supportDriver", true);
		dashboard.isPaceBarInvisible(supportDriver);

		// verify items
		dashboard.verifyWelcomeLoginMessage(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);

		// initializing agent driver
		initializeSupport();
		driverUsed.put("supportDriver", true);
		usersPage.switchToTab(supportDriver, 2);

		// open accounts tab
		dashboard.clickAccountsLink(supportDriver);
		accountsTab.openAccountIntegrationsTab(supportDriver);
		// rindna text on account integration tab
		assertTrue(accountsTab.isAccountIntegrationTextVisible(supportDriver));

		// ringdna footer text
		assertTrue(dashboard.isRingdnaFooterTextVisible(supportDriver));

		dashboard.navigateToManageCallFlow(supportDriver);
		String callFlowName = CONFIG.getProperty("qa_call_flow_to_add_steps");
		String userAccout = CONFIG.getProperty("qa_user_account");
		callFlowPage.searchCallFlow(supportDriver, callFlowName, userAccout);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		// verify ringdna text in error message
		callFlowPage.verifyCallFlowErrorMessage(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("agentDriver", false);
		System.out.println("Test case is pass");
	}
	
	@AfterClass(groups = {"Regression", "MediumPriority", "Product Sanity"})
	public void afterClass(){
		
		//disabling call forwarding on softphone
		initializeSupport();
		driverUsed.put("supportDriver", true);

		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		// deleting all holidays
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		overViewTab.openOverViewTab(supportDriver);
		overViewTab.deleteAllHolidaysIfExists(supportDriver);
		
		//enabling call tracking on support
		if (SupportBase.drivername.toString().equals("supportDriver")) {
			softPhoneSettingsPage.switchToTab(supportDriver, 2);
			dashboard.clickAccountsLink(supportDriver);
			overViewTab.openOverViewTab(supportDriver);
			overViewTab.enableShowCallTrackingSetting(supportDriver);
			overViewTab.saveAcccountSettings(supportDriver);
		}
		
		driverUsed.put("supportDriver", false);
	}
	
}