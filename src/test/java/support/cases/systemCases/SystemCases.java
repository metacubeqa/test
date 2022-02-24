package support.cases.systemCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.accounts.AccountSkillsTab;
import support.source.callFlows.CallFlowPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.system.SystemPage;
import support.source.system.SystemPage.DowntimeReason;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class SystemCases extends SupportBase {
	
	Dashboard dashboard = new Dashboard();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	SystemPage systemPage = new SystemPage();
	UsersPage usersPage = new UsersPage();
	AddSmartNumberPage addSmartNumberPage = new AddSmartNumberPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	AccountSkillsTab skillsTab = new AccountSkillsTab();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	
	private final String softNumber = "+14153225946";
	private final String softDeleteNumberCSV = System.getProperty("user.dir")+"\\src\\test\\resources\\csvToImport\\NumberSoftDelete.csv";
	private String accountName;
	private String accSalesforceId;
	private String accountID;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
		accSalesforceId = CONFIG.getProperty("qa_user_account_qav2_salesforce_id");
		accountID = CONFIG.getProperty("qa_user_account_qav2_id");
	}

	@Test(groups= {"Regression", "SupportOnly"})
	public void upload_soft_number_deletion() {
		System.out.println("Test case --upload_soft_number_deletion-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//checking if number is deleted, if yes then undelete it
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, softNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, softNumber);
		if(smartNumbersPage.isUndeleteBtnVisible(supportDriver)){
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}
		
		dashboard.navigateToDeleteNumberPage(supportDriver);
		systemPage.uploadSoftDeleteNumber(supportDriver, softDeleteNumberCSV, softNumber);

		//Verifying deleted number is of grey color
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, softNumber);
		smartNumbersPage.verifyDeletedSmartNumberColor(supportDriver, softNumber);
		
		// verifying number in inactive state
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, softNumber);
		assertTrue(smartNoIndex < 0, "Number is not in InActive state");
		
		//verifying undelete btn disabled
		smartNumbersPage.clickSmartNumber(supportDriver, softNumber);
		assertFalse(smartNumbersPage.isUndeleteBtnDisabled(supportDriver));
		smartNumbersPage.clickUndeleteBtn(supportDriver);
		
		//updating drivers
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --upload_soft_number_deletion-- started ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void verify_number_inspector_functionality() {
		System.out.println("Test case --verify_number_inspector_functionality-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to number inspector page
		String number = CONFIG.getProperty("skype_number");
		dashboard.navigateToNumberInspectorPage(supportDriver);
		systemPage.searchNumberInspector(supportDriver, number);
		
		//Verifying different sections available
		systemPage.isNumberInspectorSectionsVisible(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_number_inspector_functionality-- passed ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void search_transfer_number() {
		System.out.println("Test case --search_transfer_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// searching user and verify
		dashboard.navigateToNumberTransferPage(supportDriver);
		systemPage.enterUserName(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		systemPage.clickSearchBtn(supportDriver);
		String userName = systemPage.getUserNameFromTable(supportDriver);
		systemPage.verifyUserSearchResult(supportDriver, userName);

		// searching number and verify
		String number = systemPage.getNumberFromTable(supportDriver);
		systemPage.verifyNumberSearchResult(supportDriver, number);

		// searching account and verify
		String account = systemPage.getAccountFromTable(supportDriver);
		systemPage.verifyAccountSearchResult(supportDriver, account);

		//updating drivers used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --search_transfer_number-- passed ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void verify_error_message_when_transferred_into_same_account() {
		System.out.println("Test case --verify_error_message_when_transferred_into_same_account-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String transferNumber  = CONFIG.getProperty("qa_free_user_additional_number");
		String transferAccount = CONFIG.getProperty("qa_free_user_account");
		String transferUser    = CONFIG.getProperty("qa_free_user_name");
		
		//undeleting transfered number
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, transferNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, transferNumber);
		if (smartNoIndex < 0) {
			// UnDeleting smart Number
			smartNumbersPage.clickSmartNumber(supportDriver, transferNumber);
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}
		
		//searching free user number
		dashboard.navigateToNumberTransferPage(supportDriver);
		systemPage.enterTransferPhoneNumber(supportDriver, transferNumber);
		//systemPage.enterUserName(supportDriver, transferUser);
		systemPage.enterAccountName(supportDriver, transferAccount);
		systemPage.clickSearchBtn(supportDriver);
		
		//clicking edit button
		systemPage.clickEditBtn(supportDriver, transferNumber);
		
		//transferring to same account
		systemPage.selectNewAccount(supportDriver, transferAccount);
		systemPage.clickNextBtn(supportDriver);
		systemPage.selectTransferType(supportDriver, Type.Additional.name());
		systemPage.enterOwnerName(supportDriver, transferUser);
		systemPage.clickTransferBtn(supportDriver);
		
		//verifying error message
		systemPage.verifyErrorTransfer(supportDriver);
		systemPage.closeWindow(supportDriver);
		
		System.out.println("Test case --verify_error_message_when_transferred_into_same_account-- passed ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void transfer_smart_numbers_tracking_and_additional() {
		System.out.println("Test case --transfer_smart_numbers_tracking_and_additional-- started ");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String trackingTypeNumber = CONFIG.getProperty("automation_tracking_number");
		
		//checking if number is deleted.if yes, then undelete it 
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingTypeNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingTypeNumber);
		if(smartNumbersPage.isUndeleteBtnVisible(supportDriver)){
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}
		
		Enum<?> trackingType = Type.Additional;
		String trackingAccount = CONFIG.getProperty("qa_free_user_account");
		String trackingUser = CONFIG.getProperty("qa_free_user_name");

		dashboard.navigateToNumberTransferPage(supportDriver);
		systemPage.enterTransferPhoneNumber(supportDriver, trackingTypeNumber);
		systemPage.clickSearchBtn(supportDriver);
		
		//Converting to tracking type if type central number
		if (systemPage.getTransferType(supportDriver, trackingTypeNumber).equals(Type.CentralNumber.name())) {

			String callFlow = CONFIG.getProperty("qa_call_flow_new_org");
			
			//search and select call flow
			dashboard.navigateToManageCallFlow(supportDriver);
			callFlowPage.searchCallFlow(supportDriver, callFlow, CONFIG.getProperty("qa_user_account"));
			callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);

			//deleting number from call flow if present
			if(callFlowPage.isSmartNumberPresent(supportDriver, trackingTypeNumber)){
				callFlowPage.deleteSmartNumberOnPage(supportDriver, trackingTypeNumber);
				callFlowPage.saveCallFlowSettings(supportDriver);
			}
			
			//reassigning number
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNumberPage.selectAndReassignNumber(supportDriver, trackingTypeNumber, null, null, null);

			//Navigating to number transfer
			dashboard.navigateToNumberTransferPage(supportDriver);
			systemPage.enterTransferPhoneNumber(supportDriver, trackingTypeNumber);
			systemPage.clickSearchBtn(supportDriver);
		}
		
		//Check if tracking
		if (systemPage.getTransferType(supportDriver, trackingTypeNumber).equals("AdWords") || systemPage.getTransferType(supportDriver, trackingTypeNumber).equals("Tracking")) {

			// converting tracking type
			if (systemPage.getTransferAccount(supportDriver, trackingTypeNumber).equals(CONFIG.getProperty("qa_user_account"))) {

				// converting tracking account 1 to additional account 2
				systemPage.search_TransferSmartNumber(supportDriver, trackingTypeNumber, trackingAccount, trackingType, trackingUser);
				
				// verify transfered smart Number
				systemPage.verifyTransferedNumber(supportDriver, trackingTypeNumber, trackingType.name(), trackingAccount,	trackingUser);
			}
			else{

				//converting tracking account 2 to additional account 1

				trackingType = Type.Additional;
				trackingAccount = CONFIG.getProperty("qa_user_account");
				trackingUser = CONFIG.getProperty("qa_admin_user2_name");
				
				systemPage.search_TransferSmartNumber(supportDriver, trackingTypeNumber, trackingAccount, trackingType, trackingUser);
				
				// verify transfered smart Number
				systemPage.verifyTransferedNumber(supportDriver, trackingTypeNumber, trackingType.name(), trackingAccount,	trackingUser);
			}
		}
		
		// converting additional type
		else if (systemPage.getTransferAccount(supportDriver, trackingTypeNumber).equals(CONFIG.getProperty("qa_free_user_account"))) {
			
			// converting additional account 2 to tracking type account 1

			trackingAccount = CONFIG.getProperty("qa_user_account");
			trackingUser = CONFIG.getProperty("qa_admin_user2_name");
			trackingType = Type.Tracking;

			systemPage.search_TransferSmartNumber(supportDriver, trackingTypeNumber, trackingAccount, trackingType, trackingUser);

			// verify transfered smart Number
			systemPage.verifyTransferedNumber(supportDriver, trackingTypeNumber, trackingType.name(), trackingAccount, trackingUser);
		}
		
		else{

			// converting additional account 1 to tracking type account 2
			
			trackingAccount = CONFIG.getProperty("qa_free_user_account");
			trackingUser = CONFIG.getProperty("qa_free_user_name");
			trackingType = Type.Tracking;

			systemPage.search_TransferSmartNumber(supportDriver, trackingTypeNumber, trackingAccount, trackingType, trackingUser);

			// verify transfered smart Number
			systemPage.verifyTransferedNumber(supportDriver, trackingTypeNumber, trackingType.name(), trackingAccount, trackingUser);
		}
		
		// updating drivers
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --transfer_smart_numbers_tracking_and_additional-- passed ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void add_delete_domain_blacklist() {
		System.out.println("Test case --add_delete_domain_blacklist-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to domain blackListe
		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToDomainBlacklist(supportDriver);
		systemPage.clickAddDomain(supportDriver);
		systemPage.enterDomain(supportDriver);
		systemPage.deleteDomain(supportDriver);
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_domain_blacklist-- passed ");
	}
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void verify_cleanUp_recordings() {
		System.out.println("Test case --verify_cleanUp_recordings-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to domain blackListe
		dashboard.navigateToCleanUpRecordings(supportDriver);
		systemPage.setAccountForCleanUpRecording(supportDriver, CONFIG.getProperty("qa_user_account"));
		
		//Take count before cleanup
		int initalCount= Integer.parseInt(systemPage.getRecordingCount(supportDriver));
		systemPage.clickCleanUpRecordingBtn(supportDriver);
		int finalCount= Integer.parseInt(systemPage.getRecordingCount(supportDriver));

		assertTrue(initalCount>=finalCount, String.format("IntialCount: %s is less than final count: %s", initalCount,finalCount));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_cleanUp_recordings-- passed ");
	}

	//Move users with same salesforce id to other account
	@Test(groups= {"MediumPriority", "SupportOnly"})
	public void move_existing_user_with_skill_to_existing_account() {
		System.out.println("Test case --move_existing_user_with_skill_to_existing_account-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// navigating to users page
		String defaultAccount;
		String accountToSplitDataTo;
		String userToSplit = "lokesh test";

		//opening user to split user
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, userToSplit, "ringdnaqa@metacube.com", "0057F000000rV9AQAU");
		
		//getting default account and account to split data to details
		if(usersPage.getUserAccountName(supportDriver).equals("qa team")){
			defaultAccount = "qa team";
			accountToSplitDataTo = CONFIG.getProperty("qa_user_account");
		}else{
			defaultAccount = CONFIG.getProperty("qa_user_account");
			accountToSplitDataTo = "qa team";
		}

		// adding skill if not present at account level
		String skillName = "AutoSkill".concat(HelperFunctions.GetRandomString(3));
		usersPage.clickAccountLink(supportDriver);
		skillsTab.navigateToAccountSkillsTab(supportDriver);
		skillsTab.deleteAllSkills(supportDriver);
		skillsTab.addNewSkill(supportDriver, skillName);
		
		// adding skills if not present at user level
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, userToSplit, "ringdnaqa@metacube.com", "0057F000000rV9AQAU");
		usersPage.clickToAddSkill(supportDriver);
		usersPage.addSkill(supportDriver, skillName, null);
		usersPage.saveUserOverviewPage(supportDriver);
		
		assertTrue(usersPage.isUserSkillPresent(supportDriver, skillName));
		
		//Navigating to split account
		dashboard.navigateToSplitAccount(supportDriver);
		systemPage.enterAccountName(supportDriver, defaultAccount);
		systemPage.clickSplitGetDataBtn(supportDriver);

		//selecting account for split data
		systemPage.selectAccountSplitData(supportDriver, accountToSplitDataTo);
		systemPage.selectUserForSplitData(supportDriver, userToSplit);
		systemPage.clickMoveDataSplitBtn(supportDriver);
		
		//navigating to users page
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, userToSplit, "ringdnaqa@metacube.com", "0057F000000rV9AQAU");
		assertEquals(usersPage.getUserAccountName(supportDriver), accountToSplitDataTo);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --move_existing_user_with_skill_to_existing_account-- passed ");
	}
	
	//Verify Downtime Event creation with selected reason from dropdown
	@Test(groups= {"Regression", "SupportOnly"})
	public void verify_downtime_event_creation_with_reason() {
		System.out.println("Test case --verify_downtime_event_creation_with_reason-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to Down time Events
		dashboard.navigateToDowntimeEvents(supportDriver);
		
		//add Down time event 
		systemPage.addDowntimeEvent(supportDriver, DowntimeReason.AWSDowntime);
		assertEquals(DowntimeReason.AWSDowntime.displayName().replace(" ", ""), systemPage.verifyCreatedEventReason(supportDriver));
		
		//verify down time reason drop down text
		systemPage.verifyDownTimeReasonDropDownText(supportDriver);
		
		//delete the down time event
		systemPage.deleteDownTimeEvent(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_downtime_event_creation_with_reason-- passed ");
	}
	
	//Verify support user able to view Service user record for any selected account (if exists)
	@Test(groups= {"Regression", "SupportOnly"})
	public void view_service_user_record_for_any_selected_account() {
		System.out.println("Test case --view_service_user_record_for_any_selected_account-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to service user
		dashboard.navigateToServiceUser(supportDriver);
		
		// Select account
		callRecordingPage.selectAccount(supportDriver, accountName);
		
		assertEquals(systemPage.getAccountId(supportDriver), accountID);
		assertEquals(systemPage.getSalesforceId(supportDriver), accSalesforceId);
		assertNotNull(systemPage.getApiKey(supportDriver));
		assertNotNull(systemPage.getApiSecret(supportDriver));
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --view_service_user_record_for_any_selected_account-- passed ");
	}
	
	//Deployment Github link
	//Verify support can view list of Deployments from System-Deployments tab
	@Test(groups= {"Regression", "SupportOnly"})
	public void naviagte_to_deployments_and_verify_github_links() {
		System.out.println("Test case --naviagte_to_deployments_and_verify_github_links-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to deployments
		dashboard.navigateToDeployments(supportDriver);
		
		systemPage.verifyGithubLinks(supportDriver);
		systemPage.switchToTab(supportDriver, systemPage.getTabCount(supportDriver));
		systemPage.closeTab(supportDriver);
		systemPage.switchToTab(supportDriver, 2);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --naviagte_to_deployments_and_verify_github_links-- passed ");
	}
	
	//Verify Support able to delete a SIP routing
	//Verify Support able to edit a SIP routing
	//Verify Support able to add a SIP routing
	@Test(groups= {"Regression", "SupportOnly"})
	public void verify_support_able_to_add_update_delete_sip_routing() {
		System.out.println("Test case --verify_support_able_to_add_update_delete_sip_routing-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to sip routing
		dashboard.navigateToSipRouting(supportDriver);
		
		String name = "Automation"+HelperFunctions.GetRandomString(3);
		//add
		systemPage.addSipRouting(supportDriver, name);
		
		//edit
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		
		String newName = "Automation"+HelperFunctions.GetRandomString(3);
		systemPage.editSipRouting(supportDriver, name, newName);
		
		//delete
		supportDriver.navigate().refresh();
		dashboard.isPaceBarInvisible(supportDriver);
		systemPage.deleteSipRouting(supportDriver, newName);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_able_to_add_update_delete_sip_routingh-- passed ");
	}
	
	//Split account UI, sections , controls , input fields
	@Test(groups= {"MediumPriority", "SupportOnly"})
	public void verify_account_sections_input_fields_on_account_split_page() {
		System.out.println("Test case --verify_account_sections_input_fields_on_account_split_page-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		
		//Navigating to split account
		dashboard.navigateToSplitAccount(supportDriver);
		systemPage.enterAccountName(supportDriver, CONFIG.getProperty("qa_user_account"));
		systemPage.clickSplitGetDataBtn(supportDriver);

		// verify items on page
		systemPage.verifySplitAccountDataItems(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_account_sections_input_fields_on_account_split_page-- passed ");
	}
	
}
