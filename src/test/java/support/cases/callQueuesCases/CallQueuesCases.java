package support.cases.callQueuesCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import softphone.source.SoftPhoneCallQueues;
import softphone.source.SoftPhoneSettingsPage;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountsEmergencyTab;
import support.source.admin.AdminCallTracking;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.callQueues.CallQueuesPage.CallProcessingType;
import support.source.callQueues.CallQueuesPage.DefaultType;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.smartNumbers.SmartNumbersPage.SmartNumberFields;
import support.source.smartNumbers.SmartNumbersPage.smartNumberType;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class CallQueuesCases extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	SoftPhoneSettingsPage softphoneSettings = new SoftPhoneSettingsPage();
	SoftPhoneCallQueues softPhoneCallQueues = new SoftPhoneCallQueues();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new  UserIntelligentDialerTab();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	GroupsPage groupsPage = new GroupsPage();
	UsersPage usersPage = new UsersPage();
	AddSmartNumberPage addSmartNoPage = new AddSmartNumberPage();
	SmartNumbersPage smartNoPage = new SmartNumbersPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	AccountsEmergencyTab emergencyPage = new AccountsEmergencyTab();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	private String qa_user_name;
	private String qa_user_email;
	private String qa_user_account;
	private String qa_salesforce_id;
	private String callFlowName;
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String callQueueFileNameDownload = "CallQueues";
	
	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			qa_user_name = CONFIG.getProperty("qa_admin_user_name");
			qa_user_email = CONFIG.getProperty("qa_admin_user_email");
			qa_salesforce_id = CONFIG.getProperty("qa_admin_user_salesforce_id");
			qa_user_account = "";
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			qa_user_name = CONFIG.getProperty("qa_support_user_name");
			qa_user_email = CONFIG.getProperty("qa_support_user_email");
			qa_salesforce_id = CONFIG.getProperty("qa_support_user_salesforce_id");
			qa_user_account = CONFIG.getProperty("qa_user_account");
		}
		callFlowName = CONFIG.getProperty("qa_call_flow_new_org");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		driverUsed.put("supportDriver", false);
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_delete_call_queue_number() {
		
		System.out.println("Test case --verify_delete_call_queue_number-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String smartLabel = "AutoSmartlabel".concat(HelperFunctions.GetRandomString(3));
		
		//adding new group details
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openAddNewCallQueue(supportDriver);
		assertEquals(callQueuePage.getDefaultText(supportDriver, DefaultType.CallProcessing), "Simulring");
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		assertTrue(callQueuePage.isAllowRejectCallQueueOn(supportDriver));
		assertEquals(callQueuePage.getDefaultText(supportDriver, DefaultType.CallProcessing), "Simulring");
		callQueuePage.selectDefaultDistributationType(supportDriver);
		callQueuePage.addMember(supportDriver, qa_user_name);
		
		//creating smart number
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String,List<String>> smartNumberPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, "22", smartLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		
		//searching default number type in smart numbers tab
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, smartNumberPair.first());
		String userName = smartNoPage.getUserName(supportDriver, 0);
		smartNoPage.clickSmartNumber(supportDriver, smartNumberPair.first());
		
		String tag = "AutoTag".concat(HelperFunctions.GetRandomString(2));
		String description = "AutoDesc".concat(HelperFunctions.GetRandomString(2));
		String labelName = "AutoLabel".concat(HelperFunctions.GetRandomString(2));

		//updating tag,desc,label
		smartNoPage.updateTagDescrLabel(supportDriver, tag, description, labelName);
		
		//verifying smart number details
		smartNoPage.verifyTagDescLabelUpdated(supportDriver, tag, description, labelName);
		smartNoPage.verifySmartNumberDetails(supportDriver, smartNumberPair.first(), Type.Default, CONFIG.getProperty("qa_user_account"), userName);
		
		//deleting smart number on call queue
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteSmartNoPage(supportDriver, smartNumberPair.first());
		boolean smartNoExist = callQueuePage.verifySmartNoExists(supportDriver, smartNumberPair.first());
		assertFalse(smartNoExist, String.format("Smart number:%s exists after deleting", smartNumberPair.first()));
		
		//verifying add smart number icon present and enabled
		callQueuePage.clickSmartNumberIcon(supportDriver);
		callQueuePage.closeWindow(supportDriver);

		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_delete_call_queue_number-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void add_and_verify_other_call_queue_numbers() {
		// updating the supportDriver used
		System.out.println("Test case --add_and_verify_other_call_queue_numbers-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String smartLabel = "AutoSmartlabel".concat(HelperFunctions.GetRandomString(3));
		
		//adding new group details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		
		//adding call queue details for another queue
		String callQueueName2 = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName2 = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding new group details
		callQueuePage.idleWait(2);
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName2, callQueueDescName2);
		
		//searching number and owner
		callQueuePage.clickSmartNumberIcon(supportDriver);
		String number = addSmartNoPage.searchAndVerifyNumber_Owner(supportDriver, callQueueName);
		addSmartNoPage.clickOnReAssignForNumber(supportDriver, number);
		addSmartNoPage.clickNextButton(supportDriver);
		
		//Entering label and saving the number 
		System.out.println("Saving the number");
		addSmartNoPage.enterLabel(supportDriver, smartLabel, number);
		addSmartNoPage.saveSmartNo(supportDriver);
		boolean smartNoExist = callQueuePage.verifySmartNoExists(supportDriver, number);
		assertTrue(smartNoExist, "Smart number does not exist:"+number);
		
		//deleting smart number
		callQueuePage.deleteSmartNoPage(supportDriver, number);
		
		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		//deleting second call queue
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_and_verify_other_call_queue_numbers-- passed ");
	}

	@Test(groups = { "Regression" })
	public void delete_call_queues_verify_mandatory_fields() {
		// updating the supportDriver used
		System.out.println("Test case --delete_call_queues_verify_mandatory_fields-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.addMember(supportDriver, qa_user_name);
		
		// deleting and verifying group
		callQueuePage.deleteCallQueue(supportDriver);
		callQueuePage.verifyAfterDeleteCallQueueAssertions(supportDriver);
		
		// verifying call queue in users page after deleting
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesforce_id);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentDialerTab.isCallQueuePresent(supportDriver, callQueueName), String.format("call Queue : %s exists even after deleting", callQueueName));

		// search and select call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		
		// drag and drop dial icon
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		callFlowPage.dragAndDropDialImage(supportDriver);

		// verifying call queue do not exists
		boolean callQueueExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertFalse(callQueueExists, String.format("Call Queue:%s exits after deleting", callQueueName));
	
		//verifying queue not present
		callQueuePage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		assertFalse(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName));
		
		//restoring call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);

		// downloading and verifying csv
		HelperFunctions.deletingExistingFiles(downloadPath, callQueueFileNameDownload);
		adminCallTracking.downloadTotalRecords(supportDriver);
		boolean fileDownloaded = adminCallTracking.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, callQueueFileNameDownload, ".csv");
		assertTrue(fileDownloaded, "file is not downloaded");

		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.restoreCallQueue(supportDriver);
		callQueuePage.verifyAfterRestoreCallQueueAssertions(supportDriver);
		
		// verifying After restore call queue in Users page
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesforce_id);
		userIntelligentDialerTab.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentDialerTab.isCallQueuePresent(supportDriver, callQueueName), String.format("call Queue : %s do not exists after restoring", callQueueName));
			
		// search and select call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		// drag and drop dial icon
		callFlowPage.dragAndDropDialImage(supportDriver);

		// verifying call queue exists
		callQueueExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertTrue(callQueueExists, String.format("Call Queue:%s is not present in dial tab", callQueueName));
	
		// verifying After restore call queue in Softphone
		callQueuePage.switchToTab(supportDriver, 1);
		callQueuePage.reloadSoftphone(supportDriver);
		assertTrue(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName));
		
		//searching and deleting call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --delete_call_queues_verify_mandatory_fields-- passed ");
	}

	@Test(groups = {"Regression"})
	public void add_delete_call_flow_widget() {
		// updating the supportDriver used
		System.out.println("Test case --add_delete_call_flow_widget-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new group details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		dashboard.navigateToManageCallFlow(supportDriver);
		
		//search and select call flow
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		//drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		
		//selecting group for dial
		boolean groupExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertTrue(groupExists, String.format("Group:%s is not present in dial tab", callQueueName));
		
		//searching and deleting group
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);

		//navigating to call flow and selecting call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		//drag and drop
		callFlowPage.dragAndDropDialImage(supportDriver);
		
		//verifying group is not present in dial tab
		groupExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertFalse(groupExists, String.format("Group:%s is present in dial tab", callQueueName));
		
		// deleting nested steps
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_call_flow_widget-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void verify_call_queue_removed_from_call_flow_in_emergency_routing() {
		// updating the supportDriver used
		System.out.println("Test case --verify_call_queue_removed_from_call_flow_in_emergency_routing-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callFlowName = CONFIG.getProperty("qa_call_flow_emergency_route");
		String callQueueName = CONFIG.getProperty("automation_emergency_call_queue");
		WebDriver supportDriverToUse;
		
		//restoring call queue if deleted
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		if(!callQueuePage.isDeleteCallQueueBtnVisible(supportDriver)){
			callQueuePage.restoreCallQueue(supportDriver);
		}
		
		//setting in emergency call routing
		if (SupportBase.drivername.toString().equals("adminDriver")) {

			initializeSupport("webSupportDriver");
			driverUsed.put("webSupportDriver", true);
			supportDriverToUse = webSupportDriver;
		}
		else {
			supportDriverToUse = supportDriver;
		}

		dashboard.clickOnUserProfile(supportDriverToUse);
		callFlowPage.clickAccountLink(supportDriverToUse);

		// Opening Emergency Tab
		emergencyPage.navigateToEmergencyTab(supportDriverToUse);
		
		//clean up code
		emergencyPage.cleanUpToggleRoutings(supportDriverToUse);
		emergencyPage.deleteAllRouting(supportDriverToUse);
		
		emergencyPage.clickAddIcon(supportDriverToUse);
		emergencyPage.selectCallFlow(supportDriverToUse, callFlowName);
		emergencyPage.saveEmergencyRouting(supportDriverToUse);
		
		//getting current date
		String createdDate = HelperFunctions.GetCurrentDateTime("MM/dd/yyyy");		

		// Associate call flow
		emergencyPage.toggleStatusOnRouting(supportDriverToUse, "All", callFlowName, createdDate);
		
		//search and select call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);

		//drag and drop dial icon
		callFlowPage.removeAllCallFlowSteps(supportDriver);
		callFlowPage.dragAndDropDialImage(supportDriver);
		
		//selecting group for dial
		boolean groupExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertTrue(groupExists, String.format("Group:%s is not present in dial tab", callQueueName));
		assertFalse(callFlowPage.isAlertMsgVisible(supportDriver));
		callFlowPage.saveCallFlowSettings(supportDriver);
		
		//searching and deleting group
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);

		//navigating to call flow and selecting call flow
		dashboard.navigateToManageCallFlow(supportDriver);
		callFlowPage.searchCallFlow(supportDriver, callFlowName, qa_user_account);
		callFlowPage.clickSelectedCallFlow(supportDriver, callFlowName);
		assertTrue(callFlowPage.isAlertMsgVisible(supportDriver));
		assertTrue(callFlowPage.getAlertMsgText(supportDriver).contains("This call flow is not valid. It cannot be assigned to a ringDNA number until the issues highlighted in red are addressed."));

		//drag and drop 
		callFlowPage.dragAndDropDialImage(supportDriver);
		
		//verifying group is not present in dial tab
		groupExists = callFlowPage.selectGroupFromDialSection(supportDriver, CallFlowPage.DialCallRDNATOCat.CallQueue, callQueueName);
		assertFalse(groupExists, String.format("Group:%s is present in dial tab", callQueueName));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_queue_removed_from_call_flow_in_emergency_routing-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_clone_queue_from_existing_call_queue() {
		// updating the supportDriver used
		System.out.println("Test case --verify_clone_queue_from_existing_call_queue-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String label = "AutoQueueLabel".concat(HelperFunctions.GetRandomString(2));	
		
		//adding new group details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		
		//adding member and smart number
		callQueuePage.addMember(supportDriver, CONFIG.getProperty("qa_user_3_name"));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String, List<String>> smartNo = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, label, Type.Additional.toString(), SmartNumberCount.Single.toString());
		callQueuePage.saveGroup(supportDriver);
		
		// Clicking create queue link and verifying details
		callQueuePage.clickCloneQueueLink(supportDriver);
		callQueuePage.verifyAfterCloneCallQueueDetails(supportDriver, callQueueName, callQueueDescName, CONFIG.getProperty("qa_user_3_name"));
		
		//searching and deleting call queues
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteSmartNoPage(supportDriver, smartNo.first());
		callQueuePage.deleteCallQueue(supportDriver);
		
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName.concat(" Copy"), qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName.concat(" Copy"));
		callQueuePage.deleteCallQueue(supportDriver);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_clone_queue_from_existing_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void verify_create_team_from_call_queue() {
		// updating the supportDriver used
		System.out.println("Test case --verify_create_team_from_call_queue-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new group details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		
		//adding member
		callQueuePage.addMember(supportDriver, CONFIG.getProperty("qa_user_3_name"));
		callQueuePage.saveGroup(supportDriver);
		
		// Clicking create queue link and verifying details
		callQueuePage.clickCreateTeamLink(supportDriver);
		groupsPage.verifyAfterCreateCallTeamDetails(supportDriver, callQueueName, CONFIG.getProperty("qa_user_3_name"));
		
		//searching created group and deleting supervisor
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, callQueueName.concat(" Team"), qa_user_account);
		groupsPage.clickFirstGroup(supportDriver);
		groupsPage.deleteGroup(supportDriver);

		//searching and deleting call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_create_team_from_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_central_to_call_queue() {
		System.out.println("Test case --verify_existing_number_central_to_call_queue-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//adding central number if not present
		if (accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()) {
			String centralLabelName = "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			Pair<String, List<String>> centralNumberPair = accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
			String centralNumber = centralNumberPair.first();
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumber);
			assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));
		}
		
		String centralNumber = accountIntelligentTab.getCentralNoList(supportDriver).get(0);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.saveGroup(supportDriver);
		
		//Central to queue assigning
		String label = "AutoCentralToQueue".concat(HelperFunctions.GetRandomString(3));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, centralNumber, label, null, null);
		
		//verifying default number
		assertTrue(callQueuePage.verifySmartNoExists(supportDriver, centralNumber));
		assertTrue(callQueuePage.verifySmartNoDefault(supportDriver, centralNumber));
		
		//deleting number and call queue
		callQueuePage.deleteSmartNoPage(supportDriver, centralNumber);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_central_to_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_tracking_to_call_queue() {
		System.out.println("Test case --verify_existing_number_tracking_to_call_queue-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//verifying number tracking type
		String trackingNumber = CONFIG.getProperty("automation_tracking_number2");
		String callFlow		  = CONFIG.getProperty("qa_call_flow_new_org");
		
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);
		if (smartNoPage.isUndeleteBtnVisible(supportDriver)) {
			smartNoPage.clickUndeleteBtn(supportDriver);
		}
		
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		
		//converting to tracking type if number not of tracking type
		if (!smartNoPage.getNumberType(supportDriver, trackingNumber).equals(smartNumberType.Tracking.toString())) {
			
			String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));
			dashboard.navigateToManageCallFlow(supportDriver);
			
			//search and select call flow
			callFlowPage.searchCallFlow(supportDriver, callFlow, qa_user_account);
			callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);
		}
		
		//adding and removing salesforce campaign
		dashboard.openSmartNumbersTab(supportDriver);
		smartNoPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNoPage.clickSmartNumber(supportDriver, trackingNumber);

		//verifying salesforce campaign to tracking type
		smartNoPage.deleteFields(supportDriver, SmartNumberFields.Campaign);
		
		String campaignName = CONFIG.getProperty("qa_campaign_for_call_tracking");
		smartNoPage.addSalesForceCampaign(supportDriver, campaignName);
		
		smartNoPage.verifySalesForceCampaignAfterTracking(supportDriver, campaignName);
		smartNoPage.removeCampaign(supportDriver, campaignName);
		
		//creating new campaign and verifying
		smartNoPage.createNewSFCampaign(supportDriver, "AutoCampaignName");
		addSmartNoPage.closeSmartNumberWindow(supportDriver);
		
		// assigning tracking to call queue
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		// adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.saveGroup(supportDriver);

		// Tracking to queue assigning
		String label = "AutoTrackingToQueue".concat(HelperFunctions.GetRandomString(3));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);

		// verifying default number
		assertTrue(callQueuePage.verifySmartNoExists(supportDriver, trackingNumber));
		assertTrue(callQueuePage.verifySmartNoDefault(supportDriver, trackingNumber));

		// deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_tracking_to_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_additional_to_call_queue() {
		System.out.println("Test case --verify_existing_number_additional_to_call_queue-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String smartlabel = "AutoLabelAdditional".concat(HelperFunctions.GetRandomString(2));
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		if(userIntelligentDialerTab.getAdditionalNumberList(supportDriver) == null || userIntelligentDialerTab.getAdditionalNumberList(supportDriver).isEmpty()){
			userIntelligentDialerTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartlabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.saveGroup(supportDriver);

		//getting additional number
		String label = "AutoAdditionalToQueue".concat(HelperFunctions.GetRandomString(3));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.searchSmartNoByOwner(supportDriver, CONFIG.getProperty("qa_admin_user2_name"));
		String additionalNumber = addSmartNoPage.getAdditionalNumberFromReassignList(supportDriver);
		addSmartNoPage.closeSmartNumberWindow(supportDriver);
		
		//Additional to queue assigning
		callQueuePage.idleWait(2);
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, additionalNumber, label, null, null);
		
		//verifying default number
		assertTrue(callQueuePage.verifySmartNoExists(supportDriver, additionalNumber));
		assertTrue(callQueuePage.verifySmartNoDefault(supportDriver, additionalNumber));
		
		//deleting number and call queue
		callQueuePage.deleteSmartNoPage(supportDriver, additionalNumber);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_additional_to_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_default_to_call_queue() {
		System.out.println("Test case --verify_existing_number_default_to_call_queue-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		Pair<String, List<String>> smartNumberPair;
		String defaultNumber;
		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		// Opening Users Tab
		String qa_user2_name = CONFIG.getProperty("qa_admin_user2_name");
		String qa_user2_email = CONFIG.getProperty("qa_admin_user2_email");
		String qa_salesForce2_id = CONFIG.getProperty("qa_admin_user2_salesforce_id");

		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		
		if(!userIntelligentDialerTab.isDefaultSmartNumberPresent(supportDriver)){
			userIntelligentDialerTab.clickSmartNoIcon(supportDriver);
			smartNumberPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
			defaultNumber = smartNumberPair.first();
		}
		else{
			defaultNumber = userIntelligentDialerTab.getDefaultNo(supportDriver);
		}
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);

		//getting additional number
		String label = "AutoDefaultToQueue".concat(HelperFunctions.GetRandomString(3));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, defaultNumber, label, null, null);
		
		//verifying default number
		assertTrue(callQueuePage.verifySmartNoExists(supportDriver, defaultNumber));
		assertTrue(callQueuePage.verifySmartNoDefault(supportDriver, defaultNumber));
		
		//deleting number and call queue
		callQueuePage.deleteSmartNoPage(supportDriver, defaultNumber);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_default_to_call_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_queue_not_cloned_multiple_time() {
		// updating the supportDriver used
		System.out.println("Test case --verify_queue_not_cloned_multiple_time-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDesc = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//cloning queue and verified it is copied
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDesc);
		String headerText = callQueuePage.clickCloneQueueLink(supportDriver);
		assertEquals(headerText, "Call Queue copied.");
		
		//cloning queue and verify already exists msg
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		headerText = callQueuePage.clickCloneQueueLink(supportDriver);
		assertEquals(headerText, String.format("A queue with the name %s Copy already exists", callQueueName));
		
		//deleting queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		//deleting copied queue
		String queueCopied = callQueueName.concat(" Copy"); 
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, queueCopied, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, queueCopied);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_queue_not_cloned_multiple_time-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void create_acd_call_queue_and_verify_details() {
		// updating the supportDriver used
		System.out.println("Test case --create_acd_call_queue_and_verify_details-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDesc = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//cloning queue and verified it is copied
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.enterCallQueueName(supportDriver, callQueueName);
		callQueuePage.enterCallQueueDesc(supportDriver, callQueueDesc);
		callQueuePage.setWrapUpTime(supportDriver, CallQueuesPage.WrapUpTime.OneMin);
		callQueuePage.clickSmartNumberIcon(supportDriver);
		assertEquals(callQueuePage.getHeaderNotification(supportDriver), "Please save queue before current action.");
		callQueuePage.saveGroup(supportDriver);
		
		String label = "AutoLabel".concat(HelperFunctions.GetRandomString(3));
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String, List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, label, Type.Additional.toString(), SmartNumberCount.Single.toString());
		String smartNo = smartNoPair.first();
		
		callQueuePage.selectCallProcessing(supportDriver, CallProcessingType.LongestWaitingAgent);
		callQueuePage.saveGroup(supportDriver);
		
		callQueuePage.refreshCurrentDocument(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);
		
		callQueuePage.verifyCallQueueDetails(supportDriver, callQueueName, callQueueDesc);
		assertTrue(callQueuePage.verifySmartNoExists(supportDriver, smartNo));
		assertEquals(callQueuePage.getDefaultText(supportDriver, DefaultType.WrapUpTime), CallQueuesPage.WrapUpTime.OneMin.displayName());
		assertEquals(callQueuePage.getDefaultValue(supportDriver, DefaultType.CallProcessing), CallProcessingType.LongestWaitingAgent.name());
		
		// deleting smart number
		callQueuePage.deleteSmartNoPage(supportDriver, smartNo);

		//deleting queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --create_acd_call_queue_and_verify_details-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_agent_able_to_order_members_in_queue() {
		// updating the agentDriver used
		System.out.println("Test case --verify_agent_able_to_order_members_in_queue-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//clicking dropdown for member call queue
		String callQueueName = CONFIG.getProperty("new_qa_automation_queue");
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		List<String> beforeSaveMemberList = callQueuePage.clickMembersMenuDownItems(supportDriver);
		callQueuePage.saveGroup(supportDriver);
		assertEquals(callQueuePage.getMembersNameList(supportDriver), beforeSaveMemberList);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_agent_able_to_order_members_in_queue-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_queue_subscribed_status_functionality() {
		// updating the supportDriver used
		System.out.println("Test case --verify_call_queue_subscribed_status_functionality-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDesc = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding queue details and verifying user subscribed
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDesc);
		callQueuePage.addMember(supportDriver, qa_user_name);
		callQueuePage.saveGroup(supportDriver);
		assertTrue(callQueuePage.isMemberSubscribed(supportDriver, qa_user_name));

		//verifying queue subscribed on users page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentDialerTab.isCallQueueSubscribed(supportDriver, callQueueName));
		
		//unsubscribing call queue from softphone
		callQueuePage.switchToTab(supportDriver, 1);
		callQueuePage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softPhoneCallQueues.isQueueSubscribed(supportDriver, callQueueName);
		softPhoneCallQueues.unSubscribeQueue(supportDriver, callQueueName);
		
		// verifying queue unsubscribed on users page
		callQueuePage.switchToTab(supportDriver, 2);
		dashboard.refreshCurrentDocument(supportDriver);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentDialerTab.isCallQueueSubscribed(supportDriver, callQueueName));

		// verifying queue unsubscribed on call queues page
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		assertFalse(callQueuePage.isMemberSubscribed(supportDriver, qa_user_name));
		
		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", true);
		System.out.println("Test case --verify_call_queue_subscribed_status_functionality-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_call_queue_subscription_status_enabled_disabled() {
		// updating the supportDriver used
		System.out.println("Test case --verify_call_queue_subscription_status_enabled_disabled-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding new group details
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDesc = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding queue details and enabling unsubscribe toggle
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDesc);
		callQueuePage.addMember(supportDriver, qa_user_name);
		callQueuePage.enableAllowAgentUnsubscribeToggle(supportDriver);
		callQueuePage.saveGroup(supportDriver);

		//verifying queue not disabled
		callQueuePage.switchToTab(supportDriver, 1);
		callQueuePage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		assertFalse(softPhoneCallQueues.isQueueSubscribed_Disabled(supportDriver, callQueueName));
		
		//disabling unsubscribe toggle
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.disableAllowAgentUnsubscribeToggle(supportDriver);
		callQueuePage.saveGroup(supportDriver);

		//unsubscribing call queue from softphone
		callQueuePage.switchToTab(supportDriver, 1);
		callQueuePage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		assertTrue(softPhoneCallQueues.isQueueSubscribed_Disabled(supportDriver, callQueueName));

		//deleting call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", true);
		System.out.println("Test case --verify_call_queue_subscription_status_enabled_disabled-- passed ");
	}

	@AfterClass(groups = { "Regression", "MediumPriority", "Product Sanity"})
	public void afterClass() {
		try {
			initializeSupport();
			driverUsed.put("supportDriver", true);
			
			dashboard.switchToTab(supportDriver, 2);
			if (SupportBase.drivername.toString().equals("supportDriver")) {
				System.out.println("Cleaning emergency routing if any");
				dashboard.clickOnUserProfile(supportDriver);
				callFlowPage.clickAccountLink(supportDriver);

				// Opening Emergency Tab
				emergencyPage.navigateToEmergencyTab(supportDriver);

				// clean up code
				emergencyPage.cleanUpToggleRoutings(supportDriver);
				emergencyPage.deleteAllRouting(supportDriver);
			}

			System.out.println("Cleaning emergency routing if any");
			callQueuePage.openCallQueueSearchPage(supportDriver);
			callQueuePage.cleanQueues(supportDriver, "AutoCallQueueName");
			driverUsed.put("supportDriver", false);
			
		} catch (Exception e) {
			System.out.println("Exception while cleaning queues");
		}
	}
}