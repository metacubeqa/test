package support.cases.usersCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;
import org.testng.util.Strings;

import report.source.ReportsNonMetabaseCommonPage;
import softphone.base.SoftphoneBase;
import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneCallQueues;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftPhoneTeamPage;
import softphone.source.SoftphoneCallHistoryPage;
import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountsEmergencyTab;
import support.source.accounts.AccountsPage;
import support.source.callFlows.CallFlowPage;
import support.source.callQueues.CallQueuesPage;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.SmartNumberCount;
import support.source.commonpages.AddSmartNumberPage.SmartNumberHeaders;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.smartNumbers.SmartNumbersPage.smartNumberType;
import support.source.teams.GroupsPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import support.source.users.UsersPage.UserRole;
import support.source.users.UsersPage.UserStatus;
import utility.HelperFunctions;

public class ManageUsers extends SupportBase {

	Dashboard dashboard = new Dashboard();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab  userIntelligentDialerPage = new UserIntelligentDialerTab();
	SoftPhoneSettingsPage softphoneSettings = new SoftPhoneSettingsPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneCallQueues softPhoneCallQueues = new SoftPhoneCallQueues();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	AccountIntelligentDialerTab accountIntelligentTab = new AccountIntelligentDialerTab();
	AddSmartNumberPage addSmartNoPage = new AddSmartNumberPage();
	SoftPhoneTeamPage softPhoneTeamPage = new SoftPhoneTeamPage();
	GroupsPage groupsPage = new GroupsPage();
	CallQueuesPage callQueuePage = new CallQueuesPage();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallScreenPage callScreenPage = new CallScreenPage();
	CallFlowPage callFlowPage = new CallFlowPage();
	AccountsPage accountsPage = new AccountsPage();
	SoftphoneBase softphoneBase  = new SoftphoneBase();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	AccountsEmergencyTab emergencyTab = new AccountsEmergencyTab();
	AccountBlockedNumbersTab blockedNumbersTab = new AccountBlockedNumbersTab();
	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	UserIntelligentDialerTab userIntelligentTab = new UserIntelligentDialerTab();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	
	private String qa_user_name;
	private String qa_user_email;
	private String qa_user_number;
	private String qa_user_account;
	private String qa_salesForce_id;
	private String qa_user2_name;
	private String qa_user2_email;
	private String qa_salesForce2_id;
	
	@BeforeClass(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			qa_user_name = CONFIG.getProperty("qa_admin_user_name");
			qa_user_email = CONFIG.getProperty("qa_admin_user_email");
			qa_salesForce_id = CONFIG.getProperty("qa_admin_user_salesforce_id");
			qa_user_number	= CONFIG.getProperty("qa_admin_user_number");
			qa_user_account = "";
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			qa_user_name = CONFIG.getProperty("qa_support_user_name");
			qa_user_email = CONFIG.getProperty("qa_support_user_email");
			qa_salesForce_id = CONFIG.getProperty("qa_support_user_salesforce_id");
			qa_user_number	= CONFIG.getProperty("qa_support_user_number");
			qa_user_account = CONFIG.getProperty("qa_user_account");
		}
		qa_user2_name = CONFIG.getProperty("qa_admin_user2_name");
		qa_user2_email = CONFIG.getProperty("qa_admin_user2_email");
		qa_salesForce2_id = CONFIG.getProperty("qa_admin_user2_salesforce_id");
		
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);

		//undeleting user if deleted
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		driverUsed.put("supportDriver", false);
	}
	
	@Test(groups = { "Regression" })
	public void add_update_delete_extension() {
		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_extension-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		//creating extension
		usersPage.openOverViewTab(supportDriver);
		boolean userNameVisible = usersPage.isUserNameVisible(supportDriver, qa_user_name);
		assertTrue(userNameVisible, "User name is not visible");
		String extensionName = HelperFunctions.generateTenDigitNumber();
		usersPage.createExtension(supportDriver, extensionName);
		String actualExtension = usersPage.getExtensionUpdatedValue(supportDriver);
		assertEquals(actualExtension, extensionName, String.format("Actual extension: %s does not match with expected extension : %s",actualExtension,extensionName));
		
		//updating extension
		String extensionNameUpdate = HelperFunctions.generateTenDigitNumber();
		usersPage.createExtension(supportDriver, extensionNameUpdate);
		actualExtension = usersPage.getExtensionUpdatedValue(supportDriver);
		assertEquals(actualExtension, extensionNameUpdate, String.format("Actual extension: %s does not match with expected extension : %s",actualExtension,extensionNameUpdate));
		
		//removing extension
		usersPage.createExtension(supportDriver, "");
		actualExtension = usersPage.getExtensionUpdatedValue(supportDriver);
		assertEquals(actualExtension, "", String.format("Actual extension: %s does not match with expected extension : %s",actualExtension,""));
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_extension-- passed ");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void add_update_delete_custom_greeting() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_custom_greeting-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		// adding custom greeting by uploading file
		System.out.println("Starting adding voicemail drop");
		String customGreetingName = "AutoCustomGreeting".concat(HelperFunctions.GetRandomString(3));
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		usersPage.idleWait(2);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerPage.createCustomGreeting(supportDriver, customGreetingName);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToCustomGreetingTab(supportDriver);
		softphoneSettings.playCustomGreeting(supportDriver, customGreetingName);
		softphoneSettings.verifyOOOGreetingCreated(supportDriver, customGreetingName);
		usersPage.switchToTab(supportDriver, 2);
		
		// adding custom greeting by recording file
		String customGreetingRecord = "AutoCustomGreetingRecord".concat(HelperFunctions.GetRandomString(3));
		userIntelligentDialerPage.createCustomGreetingByRecording(supportDriver, customGreetingRecord, 5);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToCustomGreetingTab(supportDriver);
		softphoneSettings.playCustomGreeting(supportDriver, customGreetingRecord);
		softphoneSettings.verifyOOOGreetingCreated(supportDriver, customGreetingRecord);
		usersPage.switchToTab(supportDriver, 2);
		
		// updating custom greeting
		System.out.println("Starting updating voicemail drop");
		String customGreetingNameUpdate = "AutoCustomGreetingUpdate".concat(HelperFunctions.GetRandomString(3));
		usersPage.refreshCurrentDocument(supportDriver);
		userIntelligentDialerPage.updateCustomGreetingRecordsByUploadingFile(supportDriver, customGreetingName, customGreetingNameUpdate);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToCustomGreetingTab(supportDriver);
		softphoneSettings.playCustomGreeting(supportDriver, customGreetingNameUpdate);
		usersPage.switchToTab(supportDriver, 2);
		
		// deleting custom greeting
		System.out.println("Starting deleting voicemail drop");
		accountIntelligentTab.deleteRecords(supportDriver, customGreetingNameUpdate);
		assertFalse(userIntelligentDialerPage.isCustomGreetingVisible(supportDriver, customGreetingNameUpdate));
			
		// deleting custom greeting recording
		accountIntelligentTab.deleteRecords(supportDriver, customGreetingRecord);
		assertFalse(userIntelligentDialerPage.isCustomGreetingVisible(supportDriver, customGreetingRecord));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToCustomGreetingTab(supportDriver);
		assertFalse(softphoneSettings.isCustomGreetingVisible(supportDriver, customGreetingNameUpdate));
		assertFalse(softphoneSettings.isCustomGreetingVisible(supportDriver, customGreetingRecord));
		usersPage.switchToTab(supportDriver, 2);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_custom_greeting-- passed ");
	}
	
	@Test(groups= { "Regression"})
	public void add_update_delete_voicemail_drops() {
		// closing desktop windows if open
		HelperFunctions.closeDesktopWindow();

		// updating the supportDriver used
		System.out.println("Test case --add_update_delete_voicemail_drops-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		// adding voicemail drop by uploading file
		System.out.println("Starting adding voicemail drop");
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		usersPage.idleWait(2);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		String voiceMailName = "AutoVoiceMailDrop".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMailByUploadingFile(supportDriver, voiceMailName);
		boolean voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailName);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after creating", voiceMailName));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailName);
		softphoneSettings.verifyUsersVoicemailDropOnDialer(supportDriver, voiceMailName);
		usersPage.switchToTab(supportDriver, 2);
		
		// adding voicemail drop by recording file
		String voiceMailRecord = "AutoVoiceMailRecord".concat(HelperFunctions.GetRandomString(3));
		accountIntelligentTab.createVoiceMail(supportDriver, voiceMailRecord, 5);
		voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailRecord);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after creating", voiceMailRecord));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailRecord);
		softphoneSettings.verifyUsersVoicemailDropOnDialer(supportDriver, voiceMailRecord);
		usersPage.switchToTab(supportDriver, 2);

		// updating voicemail drop
		System.out.println("Starting updating voicemail drop");
		String voiceMailNameUpdate = "AutoVoiceMailUpdate".concat(HelperFunctions.GetRandomString(3));
		usersPage.refreshCurrentDocument(supportDriver);
		accountIntelligentTab.updateVoiceMailRecordsByUploadingFile(supportDriver, voiceMailName, voiceMailNameUpdate);
		voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailNameUpdate);
		assertTrue(voiceMailPlayable, String.format("Voice mail: %s is not playing after creating", voiceMailNameUpdate));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		softphoneSettings.playVoiceMail(supportDriver, voiceMailNameUpdate);
		softphoneSettings.verifyUsersVoicemailDropOnDialer(supportDriver, voiceMailNameUpdate);
		usersPage.switchToTab(supportDriver, 2);

		// deleting voicemail drop
		System.out.println("Starting deleting voicemail drop");
		accountIntelligentTab.deleteRecords(supportDriver, voiceMailNameUpdate);
		voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailNameUpdate);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailNameUpdate));
		
		// deleting voicemail drop recording
		accountIntelligentTab.deleteRecords(supportDriver, voiceMailRecord);
		voiceMailPlayable = accountIntelligentTab.checkVoiceMailIsPlayable(supportDriver, voiceMailRecord);
		assertFalse(voiceMailPlayable, String.format("Voice mail: %s exists even after deleting", voiceMailRecord));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailNameUpdate));
		assertFalse(softphoneSettings.isVoiceMailVisible(supportDriver, voiceMailRecord));
		softphoneSettings.switchToTab(supportDriver, 2);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_update_delete_voicemail_drops-- passed ");
	}
	
	@Test(groups= {"Regression"})
	public void add_delete_outbound_numbers() {
		// updating the supportDriver used
		System.out.println("Test case --add_delete_outbound_numbers-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Intelligent Dialer Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		// adding outbound number
		System.out.println("Starting adding outbound numbers");
		String phoneNumber = HelperFunctions.generateTenDigitNumber();
		String labelName = "AutoOutBoundLabel" + HelperFunctions.GetRandomString(3);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		usersPage.idleWait(2);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerPage.createOutBoundNumber(supportDriver, phoneNumber, labelName);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.clickOutboundNumbersTab(supportDriver);
		int numberExists = softphoneSettings.isNumberExistInOutboundNumber(supportDriver, phoneNumber);
		assertTrue(numberExists >= 0, String.format("Out Bound number: %s does not exists", phoneNumber));
		usersPage.switchToTab(supportDriver, 2);
		
		//selecting out bound check rule
		userIntelligentDialerPage.selectOutBoundCheckBoxAccToLabel(supportDriver, labelName);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.clickOutboundNumbersTab(supportDriver);
		String actualSelectedOutBoundNumber = softphoneSettings.getSelectedOutBoundNumberOnSoftPhone(supportDriver);
		assertEquals(actualSelectedOutBoundNumber, phoneNumber, String.format("Outbound Number on softphone:%s does not match with expected outbound number:%s", actualSelectedOutBoundNumber, phoneNumber));
		usersPage.switchToTab(supportDriver, 2);
		
		//deleting outbound number
		accountIntelligentTab.deleteRecords(supportDriver, labelName);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.clickOutboundNumbersTab(supportDriver);
		String localPresenceCode = CONFIG.getProperty("qa_admin_user3_number").substring(2, 5);
		List<String> selectedLocalPresenceNumber = softphoneSettings.getLocalPresenceAdditionalNumbers(supportDriver, localPresenceCode);
		assertFalse(selectedLocalPresenceNumber.contains(phoneNumber), String.format("Out Bound number: %s exists even after deleting", phoneNumber));
		usersPage.switchToTab(supportDriver, 2);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_delete_outbound_numbers-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void change_default_smart_number() {
		// updating the supportDriver used
		System.out.println("Test case --change_default_smart_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String smartLabel = "AutoUserSmartLabel".concat(HelperFunctions.GetRandomString(2));
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if(!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)){
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
		}
		
		String previousDefaultSmartNo = userIntelligentTab.getDefaultNo(supportDriver);

		//creating new default number
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		Pair<String, List<String>> currentDefaultSmartNo = addSmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
		
		//verify default and additional type numbers
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, currentDefaultSmartNo.first());
		assertTrue(userIntelligentTab.isDeleteIconDisabled(supportDriver, currentDefaultSmartNo.first()));
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, previousDefaultSmartNo);

		/*
		 * Commenting this portion for now since
		 * Softphone Login not working after changing default number, In discussion with manual team 
		 * 
		 * 
		 * //softphone verification on default and additional number
		WebDriver driver = getDriver();
		usersPage.switchToTab(driver, 2);
		SFLP.softphoneLogin(driver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_admin_user2_username"), CONFIG.getProperty("qa_admin_user2_password"));
		
		usersPage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(driver);
		softphoneSettings.clickOutboundNumbersTab(driver);
		String actualSelectedOutBoundNumber = softphoneSettings.getSelectedOutBoundNumberOnSoftPhone(driver);
	    assertTrue(currentDefaultSmartNo.first().contains(actualSelectedOutBoundNumber), String.format("Outbound Number on softphone:%s does not match with expected outbound number:%s", actualSelectedOutBoundNumber, currentDefaultSmartNo.first()));
		List<String> outBoundNoList = softphoneSettings.getLocalPresenceAdditionalNumbers(driver, HelperFunctions.getRandomAreaCode());
		assertTrue(outBoundNoList.contains(HelperFunctions.getNumberInSimpleFormat(previousDefaultSmartNo)), "Outbound list does not contain previous default number");
		usersPage.switchToTab(supportDriver, 2);
		*
		*
		*/
		
		//deleting smart number
		userIntelligentTab.deleteSmartNumber(supportDriver, previousDefaultSmartNo);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --change_default_smart_number-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void search_existing_number_by_owner_phone_number() {
		// updating the supportDriver used
		System.out.println("Test case --search_existing_number_by_owner_phone_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		//searching owner
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);
		addSmartNoPage.searchSmartNoByOwner(supportDriver, CONFIG.getProperty("qa_agent_user_name"));
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Additional.name()));
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Default.name()));
		assertTrue(addSmartNoPage.verifyReassignNumbersEnabled(supportDriver));
		addSmartNoPage.closeSmartNumberWindow(supportDriver);

		//searching number
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.searchAndVerifyNumber_Owner(supportDriver, CONFIG.getProperty("qa_agent_user_name"));
		addSmartNoPage.closeSmartNumberWindow(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --search_existing_number_by_owner_phone_number-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_existing_numbers_disabled_for_current_user() {
		// updating the supportDriver used
		System.out.println("Test case --verify_existing_numbers_disabled_for_current_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//searching owner
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		String smartLabel = "ÄutoSmartLabel".concat(HelperFunctions.GetRandomString(2));
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}		
		
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);
		addSmartNoPage.searchSmartNoByOwner(supportDriver, qa_user_name);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Additional.name()));
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Default.name()));
		assertFalse(addSmartNoPage.verifyReassignNumbersEnabled(supportDriver));
		addSmartNoPage.closeSmartNumberWindow(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_numbers_disabled_for_current_user-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void assign_additional_type_to_new_number() {
		// updating the supportDriver used
		System.out.println("Test case --assign_additional_type_to_new_number-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String smartLabel = "AutoUserSmartLabel".concat(HelperFunctions.GetRandomString(2));
		String tag = "AutoTag".concat(HelperFunctions.GetRandomString(2));
		String description = "AutoDesc".concat(HelperFunctions.GetRandomString(2));
		String label = "AutoLabel".concat(HelperFunctions.GetRandomString(2));

		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		//Creating additional type number
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		String areaCode = HelperFunctions.getRandomAreaCode();
		Pair<String, List<String>> additionalSmartNo = addSmartNoPage.addNewSmartNumber(supportDriver, null, areaCode, null, smartLabel,
				Type.Additional.toString(), SmartNumberCount.Single.toString());
		
		//verifying additional type
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, additionalSmartNo.first());
		
		//verifying additional number is editable
		dashboard.openSmartNumbersTab(supportDriver);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, additionalSmartNo.first());
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		
		//updating tag,description and label details
		smartNumbersPage.updateTagDescrLabel(supportDriver, tag, description, label);
		
		//verifying the above made changes
		smartNumbersPage.verifyTagDescLabelUpdated(supportDriver, tag, description, label);
		
		// softphone verification on default and additional number
		usersPage.switchToTab(supportDriver, 1);
		usersPage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.clickOutboundNumbersTab(supportDriver);
		
		List<String> outBoundNoList = softphoneSettings.getLocalPresenceAdditionalNumbers(supportDriver, areaCode);
		assertTrue(outBoundNoList.contains(HelperFunctions.getNumberInSimpleFormat(additionalSmartNo.first())),
				"Outbound list does not contain additional number");

		usersPage.switchToTab(supportDriver, 2);
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --assign_additional_type_to_new_number-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void assign_international_additional_number_to_user() {
		// updating the supportDriver used
		System.out.println("Test case --assign_international_additional_number_to_user-- started ");
	
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String smartLabel = "AutoUserSmartLabel".concat(HelperFunctions.GetRandomString(2));

		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		
		//Creating additional type number
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		Pair<String, List<String>> additionalSmartNo = addSmartNoPage.addNewSmartNumber(supportDriver, "Canada", null, null, smartLabel,
				Type.Additional.toString(), SmartNumberCount.Single.toString());
		
		//verifying additional type
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, additionalSmartNo.first());
		
		//verifying and deleting smart number
		dashboard.openSmartNumbersTab(supportDriver);
		
		//searching by user name
		smartNumbersPage.searchSmartNumberByUserName(supportDriver, qa_user_name);
		assertEquals(smartNumbersPage.getUserName(supportDriver, 0), qa_user_name);
		
		// searching by account name
		if (SupportBase.drivername.toString().equals("supportDriver")) {
			smartNumbersPage.searchSmartNumberByAccount(supportDriver, qa_user_account);
			assertEquals(smartNumbersPage.getAccountName(supportDriver, 0), qa_user_account);
		}

		// searching by number
		smartNumbersPage.searchSmartNumber(supportDriver, additionalSmartNo.first());
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, additionalSmartNo.first());
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --assign_international_additional_number_to_user-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void verify_added_queues_on_support_and_softphone() {
		// updating the supportDriver used
		System.out.println("Test case --verify_added_queues_on_support_and_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Users Tab
		String queueName = CONFIG.getProperty("new_qa_automation_queue");
		dashboard.clickOnUserProfile(supportDriver);
		
		//deleting call queue if present
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if(userIntelligentTab.isCallQueuePresent(supportDriver, queueName)){
			userIntelligentTab.deleteCallQueue(supportDriver, queueName);
		}

		//adding call queue
		assertFalse(userIntelligentTab.isCallQueueSubscribed(supportDriver, queueName));
		userIntelligentTab.clickAddCallQueueIcon(supportDriver);
		userIntelligentTab.addUserToCallQueue(supportDriver, queueName);

		//saving list of call queues
		List<String> callQueueList = userIntelligentTab.getQueueMemberList(supportDriver);
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softPhoneCallQueues.reloadSoftphone(supportDriver);
		softPhoneCallQueues.subscribeQueue(supportDriver, queueName);
		softPhoneCallQueues.isQueueSubscribed(supportDriver, queueName);
		List<String> softphoneCallQueueList = softPhoneCallQueues.returnCallQueueList(supportDriver);
		assertTrue(callQueueList.containsAll(softphoneCallQueueList), String.format("Support list:%s does not contains list of softphone:%s", callQueueList, softphoneCallQueueList));
		
		//verification in support page
		usersPage.switchToTab(supportDriver, 2);
		usersPage.refreshCurrentDocument(supportDriver);
		assertTrue(userIntelligentTab.isCallQueueSubscribed(supportDriver, queueName));
		assertTrue(userIntelligentTab.isCallQueueSavedInAddUserToCallQueues(supportDriver, queueName));
		usersPage.clickCloseBtn(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_added_queues_on_support_and_softphone-- passed ");
	}
	
	@Test(groups = {"Regression"})
	public void verify_added_teams_on_support_and_softphone() {
		// updating the supportDriver used
		System.out.println("Test case --verify_added_teams_on_support_and_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// Opening Users Tab
		String teamName = CONFIG.getProperty("qa_automation_team_operations");
		String memberAddedInTeam = CONFIG.getProperty("qa_user_operations_name");
		
		dashboard.clickOnUserProfile(supportDriver);
		
		//deleting Team if present
		if(usersPage.isTeamExists(supportDriver, teamName)){
			usersPage.deleteTeamMember(supportDriver, teamName);
		}

		//adding team
		usersPage.clickAddTeamIcon(supportDriver);
		usersPage.addUserToTeams(supportDriver, teamName);
		assertTrue(usersPage.isTeamExists(supportDriver, teamName));

		//verifying in teams details page
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.openGroupDetailPage(supportDriver, teamName, qa_user_account);
		assertTrue(groupsPage.isAgentAddedAsMember(supportDriver, qa_user_name));
		
		// softphone verification
		usersPage.switchToTab(supportDriver, 1);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertTrue(softPhoneTeamPage.verifyAgentPresent(supportDriver, memberAddedInTeam));
		
		//removing in user to teams section and verifying
		usersPage.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.clickAddTeamIcon(supportDriver);
		usersPage.removeUserToTeam(supportDriver, teamName);
		assertFalse(usersPage.isTeamExists(supportDriver, teamName));

		//verifying in teams details page after removing team
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.openGroupDetailPage(supportDriver, teamName, qa_user_account);
		assertFalse(groupsPage.isAgentAddedAsMember(supportDriver, qa_user_name));
		
		// softphone verification after removing team
		usersPage.switchToTab(supportDriver, 1);
		usersPage.reloadSoftphone(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertFalse(softPhoneTeamPage.verifyAgentPresent(supportDriver, memberAddedInTeam));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_added_teams_on_support_and_softphone-- passed ");
	}
	
	@Test(groups= {"Regression"})
	public void remove_call_queues_from_call_queue_dropdown() {
		System.out.println("Test case --remove_call_queues_from_call_queue_dropdown-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.addMember(supportDriver, qa_user_name);
		assertTrue(callQueuePage.isAgentAddedAsMember(supportDriver, qa_user_name));
		callQueuePage.saveGroup(supportDriver);
		
		// subscribing queue in softphone
		callQueuePage.switchToTab(supportDriver, 1);
		callQueuePage.reloadSoftphone(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softPhoneCallQueues.subscribeQueue(supportDriver, callQueueName);
		softPhoneCallQueues.isQueueSubscribed(supportDriver, callQueueName);
		
		// verifying Group membership in OverView Tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isCallQueuePresent(supportDriver, callQueueName), String.format("call Queue : %s do not exits after creating", callQueueName));
		assertTrue(userIntelligentTab.isCallQueueSubscribed(supportDriver, callQueueName));
	
		// deleting user to call queue
		userIntelligentTab.clickAddCallQueueIcon(supportDriver);
		userIntelligentTab.deleteUserToCallQueue(supportDriver, callQueueName);
		assertFalse(userIntelligentTab.isCallQueuePresent(supportDriver, callQueueName), String.format("call Queue : %s exits after deleting", callQueueName));

		// verifying queue not present in softphone
		callQueuePage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneCallQueues.openCallQueuesSection(supportDriver);
		assertFalse(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName));
			
		//verifying member not present in call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.clickFirstCallQueue(supportDriver);
		assertFalse(callQueuePage.isAgentAddedAsMember(supportDriver, qa_user_name));
		
		//verifying member not present in call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --remove_call_queues_from_call_queue_dropdown-- passed ");
	}
	
	@Test(groups= {"Regression"})
	public void verify_select_all_checkbox_selected() {
		System.out.println("Test case --verify_select_all_checkbox_selected-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String userName = CONFIG.getProperty("new_qa_automation_user_name");
		String title = "qa engg";

		//Search and verify by email, name, title
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openAddNewUsersPage(supportDriver);
		usersPage.searchEmailInAddNewUser(supportDriver, CONFIG.getProperty("new_qa_automation_user_email"));
		usersPage.searchTitleInAddNewUser(supportDriver, title);
		usersPage.searchUserInAddNewUser(supportDriver, userName);
		usersPage.isAllUsersSelected(supportDriver);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_select_all_checkbox_selected-- passed ");
	}
	
	@Test(groups= {"Regression"})
	public void select_and_verify_adding_multiple_smart_numbers() {
		System.out.println("Test case --select_and_verify_adding_multiple_smart_numbers-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String labelName = "AutoUserLabel".concat(HelperFunctions.GetRandomString(3));

		//opening users tab to check group
		String smartLabel = "AutoUserSmartLabel".concat(HelperFunctions.GetRandomString(2));
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		if(!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)){
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
		}
		
		String defaultNumber = userIntelligentTab.getDefaultNo(supportDriver);

		//creating new deafult number
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		Pair<String,List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, AddSmartNumberPage.Country.UNITED_STATES.displayName(), HelperFunctions.getRandomAreaCode(), "22", labelName, Type.Default.toString(), SmartNumberCount.Multiple.toString());
		dashboard.isPaceBarInvisible(supportDriver);
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, smartNoPair.second().get(1).toString());
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, defaultNumber);
		userIntelligentTab.deleteSmartNumber(supportDriver, defaultNumber);
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --select_and_verify_adding_multiple_smart_numbers-- passed ");
	}
	
	@Test(groups = { "Regression" , "SupportOnly"})
	public void change_user_role_admin_to_support() {
		
		// updating the supportDriver used
		System.out.println("Test case --change_user_role_admin_to_support-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening users tab to check group
		String qa_user2_name = CONFIG.getProperty("qa_admin_user2_name");
		String qa_user2_email = CONFIG.getProperty("qa_admin_user2_email");
		String qa_salesForce2_id = CONFIG.getProperty("qa_admin_user2_salesforce_id");
		
		//Navigating to Manage users page
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		
		// Default Changing role to Admin if not found
		if (!usersPage.getCurrentUserRole(supportDriver).equals(UserRole.Admin.toString())) {
			usersPage.selectRole(supportDriver, UserRole.Admin);
			usersPage.saveUserOverviewPage(supportDriver);
			dashboard.isPaceBarInvisible(supportDriver);
		}

		//Changing Admin role to Support
		usersPage.selectRole(supportDriver, UserRole.Support);
		usersPage.saveUserOverviewPage(supportDriver);
		
		//Now login and verifying
		dashboard.idleWait(2);
		WebDriver userRoleDriver = getDriver();
		softPhoneLoginPage.supportLoginWhenSoftphoneLogin(userRoleDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_admin_user2_username"), CONFIG.getProperty("qa_admin_user2_password"));
		dashboard.isPaceBarInvisible(userRoleDriver);
		
		//Verifying for admin assertions
		dashboard.refreshCurrentDocument(userRoleDriver);
		dashboard.waitForPageLoaded(userRoleDriver);
		dashboard.isPaceBarInvisible(userRoleDriver);
		dashboard.idleWait(3); 
		dashboard.assertDashBoardForSupportUser(userRoleDriver);
		
		//Changing back to Admin role 
		usersPage.switchToTab(supportDriver, 2);
		usersPage.selectRole(supportDriver, UserRole.Admin);
		usersPage.verifyUserSavedMsg(supportDriver);				

		//Verifying for suuport assertions
		dashboard.refreshCurrentDocument(userRoleDriver);
		dashboard.waitForPageLoaded(userRoleDriver);
		dashboard.isPaceBarInvisible(userRoleDriver);
		dashboard.assertDashBoardForAdminUser(userRoleDriver);
		System.out.println("Successfully changed back to Admin role");
		userRoleDriver.quit();
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --change_user_role_admin_to_support-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_supervisor_role_not_visible_in_user_to_teams_sections() {

		// updating the supportDriver used
		System.out.println("Test case --verify_supervisor_role_not_visible_in_user_to_teams_sections-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String teamSupervisorName = CONFIG.getProperty("automation_supervisor_group");
		
		//Verifying supervisor group not present
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		
		if(Strings.isNullOrEmpty(usersPage.getSuperVisorTeamName(supportDriver))){
			//adding team
			usersPage.clickAddTeamIcon(supportDriver);
			usersPage.addUserToTeams(supportDriver, teamSupervisorName);

			//verifying team not visible in User To Teams section
			usersPage.clickAddTeamIcon(supportDriver);
			assertFalse(usersPage.verifyUserToTeamSectionContainsTeam(supportDriver, teamSupervisorName));
		}
		else{
			//verifying team not visible in User To Teams section
			teamSupervisorName = usersPage.getSuperVisorTeamName(supportDriver);
			usersPage.clickAddTeamIcon(supportDriver);
			assertFalse(usersPage.verifyUserToTeamSectionContainsTeam(supportDriver, teamSupervisorName));
		}

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_supervisor_role_not_visible_in_user_to_teams_sections-- passed");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_users_outbound_call_with_stay_connected() {

		// updating the supportDriver used
		System.out.println("Test case --verify_users_outbound_call_with_stay_connected-- started");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		softphoneSettings.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		//pre requisites in user intelligent dialer settings
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerPage.disableCallForwardingPrompt(supportDriver);
		userIntelligentDialerPage.saveAcccountSettings(supportDriver);

		//initializing admin driver where call forwarding would be enabled
		initializeSupport("adminDriver");
		driverUsed.put("adminDriver", true);
		softphoneSettings.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);
		
		// initializing agent driver to which call would be made
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		softphoneSettings.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		// Navigating to overview tab
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		usersPage.openOverViewTab(supportDriver);
		
		// Setting call forwarding ON and enable stay connected setting
		System.out.println("Setting call forwarding ON");
		String numberToForward = CONFIG	.getProperty("qa_admin_user_number");
		String numberToCall = CONFIG.getProperty("qa_agent_user_number");
		softphoneSettings.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softphoneSettings.setCallForwardingNumber(supportDriver, adminDriver, "", numberToForward);
		softphoneSettings.idleWait(1);
		softphoneSettings.enableStayConnectedSetting(supportDriver);
		softphoneSettings.idleWait(1);
		
		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToCall);

		// pickup the call
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.isDeclineButtonVisible(agentDriver);
		softPhoneCalling.pickupIncomingCall(agentDriver);

		// Receiving call from call forwarding device
		System.out.println("Receiving call from call forwarding device");
		softPhoneCalling.switchToTab(adminDriver, 1);
		softPhoneCalling.isDeclineButtonVisible(adminDriver);
		softPhoneCalling.pickupIncomingCall(adminDriver);

		// verify on overview tab details during a live call
		usersPage.switchToTab(supportDriver, 2);
		usersPage.refreshCurrentDocument(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.verifyCallProcessingDetailsDuringLiveCall(supportDriver);
		userIntelligentTab.verifyRemoveLiveCallMsgDuringActiveCall(supportDriver);
		userIntelligentTab.verifyRemoveBridgeRefMsgDuringActiveCall(supportDriver);
		
		// hanging up active call on support		
		usersPage.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneCalling.idleWait(2);

		// verifying live call after call end
		usersPage.switchToTab(supportDriver, 2);
		userIntelligentTab.verifyRemoveLiveCallMsgAfterActiveCall(supportDriver);
		
		// hanging up on call forwarding number		
		usersPage.switchToTab(adminDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(adminDriver);

		// verify on overview tab details after a live call
		usersPage.refreshCurrentDocument(supportDriver);
		userIntelligentTab.verifyCallProcessingDetailsBlank(supportDriver);
	
		usersPage.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		// Setting call forwarding OFF and disable stay connected setting
		System.out.println("Setting call forwarding OFF");
		softphoneSettings.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.disableCallForwardingSettings(supportDriver);

		// updating the drivers used
		driverUsed.put("supportDriver", false);
		driverUsed.put("adminDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --verify_users_outbound_call_with_stay_connected-- passed");
	}
	
	@Test(groups = { "Regression" })
	public void verify_smart_number_displayed_on_call_forwarding() {

		// updating the supportDriver used
		System.out.println("Test case --verify_smart_number_displayed_on_call_forwarding-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
				
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.setCallForwardingNumber(supportDriver, agentDriver, "", CONFIG.getProperty("qa_agent_user_number"));

		// Opening Intelligent Dialer Tab
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);

		//enabling settings
		userIntelligentDialerPage.enableCallForwarding(supportDriver);
		userIntelligentDialerPage.enableCallForwardingUsingSmartNumber(supportDriver);
		userIntelligentDialerPage.saveAcccountSettingsDialer(supportDriver);

		System.out.println(userIntelligentDialerPage.getCallForwardingNumber(supportDriver));

		String newDriverStringInitialize;
		WebDriver newDriverInitialize = null;
		String newNumberToDial;

		if (SupportBase.drivername.toString().equals("adminDriver")) {
			newDriverStringInitialize = "webSupportDriver";
			newNumberToDial = CONFIG.getProperty("qa_admin_user_number");
		} else {
			newDriverStringInitialize = "adminDriver";
			newNumberToDial = CONFIG.getProperty("qa_support_user_number");
		}

		// initializing new driver
		initializeSupport(newDriverStringInitialize);
		driverUsed.put(newDriverStringInitialize, true);
		
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			newDriverInitialize = webSupportDriver;
		} else {
			newDriverInitialize = adminDriver;
		} 

		// calling to admin/support driver
		softPhoneCalling.switchToTab(newDriverInitialize, 1);
		softPhoneCalling.softphoneAgentCall(newDriverInitialize, newNumberToDial);
		softPhoneCalling.isCallHoldButtonVisible(newDriverInitialize);

		// verification on agent driver that number displayed of smart number of user
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.pickupIncomingCall(agentDriver);
		String callerNumber = callScreenPage.getCallerNumber(agentDriver);
		assertEquals(callerNumber, HelperFunctions.getNumberInSimpleFormat(newNumberToDial));

		// hanging up calls
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.hangupIfInActiveCall(newDriverInitialize);

		// Setting call forwarding OFF and disable stay connected setting
		System.out.println("Setting call forwarding OFF");
		softphoneSettings.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.disableCallForwardingSettings(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put(newDriverStringInitialize, false);
		System.out.println("Test case --verify_smart_number_displayed_on_call_forwarding-- passed");
	}
	
	@Test(groups = { "Regression", "AdminOnly" })
	public void end_users_session_from_admin_console() {

		// updating the drivers used
		System.out.println("Test case --end_users_session_from_admin_console-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		//hanging up active call if any
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		//hanging up active call if any
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		
		//verifying logout user button not visible on support
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		assertFalse(usersPage.isLogOutUserBtnVisible(webSupportDriver));

		// Opening Overview Tab
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_support_user_name"), CONFIG.getProperty("qa_support_user_email"), CONFIG.getProperty("qa_support_user_salesforce_id"));
		usersPage.openOverViewTab(supportDriver);

		// clicking logout btn and verifying details
		usersPage.clickLogOutUserBtn(supportDriver, CONFIG.getProperty("qa_support_user_name"));

		//verifying on logged out softphone page
		usersPage.switchToTab(webSupportDriver, 1);
		softPhoneLoginPage.verifyForcedLogout(webSupportDriver, CONFIG.getProperty("qa_admin_user_name"));
		
		//logging back support driver
		softPhoneLoginPage.softphoneLogin(webSupportDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_support_user_username"), CONFIG.getProperty("qa_support_user_password"));
		softPhoneLoginPage.switchToTab(webSupportDriver, 2);
		softPhoneLoginPage.refreshCurrentDocument(webSupportDriver);
		
		// updating the drivers used
		driverUsed.put("supportDriver", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --end_users_session_from_admin_console-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_checkbox_disable_for_added_users() {

		// updating the drivers used
		System.out.println("Test case --verify_checkbox_disable_for_added_users-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening New users page
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openAddNewUsersPage(supportDriver);

		usersPage.searchUserInAddNewUser(supportDriver, CONFIG.getProperty("qa_admin_user_name"));
		assertFalse(usersPage.isCheckBoxVisibleForUser(supportDriver, CONFIG.getProperty("qa_admin_user_name")));
		
		usersPage.searchUserInAddNewUser(supportDriver, CONFIG.getProperty("qa_support_user_name"));
		assertFalse(usersPage.isCheckBoxVisibleForUser(supportDriver, CONFIG.getProperty("qa_support_user_name")));
		
		// updating the drivers used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_checkbox_disable_for_added_users-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_checkbox_visible_for_not_added_users() {

		// updating the drivers used
		System.out.println("Test case --verify_checkbox_visible_for_not_added_users-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening New users page
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openAddNewUsersPage(supportDriver);

		String notAddedUser = "Chatter Expert";
		
		//verifying next btn is disabled
		usersPage.searchUserInAddNewUser(supportDriver, notAddedUser);
		assertTrue(usersPage.isCheckBoxVisibleForUser(supportDriver, notAddedUser));
		assertTrue(usersPage.isNextBtnDisabled(supportDriver));
		
		//selecting user and verifying next btn
		usersPage.clickCheckBoxVisibleForUser(supportDriver, notAddedUser);
		assertFalse(usersPage.isNextBtnDisabled(supportDriver));
		
		//clicking next btn , deleting and verifying user
		usersPage.clickNextBtnForAddNewUser(supportDriver);
		assertTrue(usersPage.isUserVisibleAfterAdd(supportDriver, notAddedUser));
		
		usersPage.deleteUserAfterAdd(supportDriver, notAddedUser);
		assertFalse(usersPage.isUserVisibleAfterAdd(supportDriver, notAddedUser));
		
		// updating the drivers used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_checkbox_visible_for_not_added_users-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_search_filter_by_city_country_profile() {

		// updating the drivers used
		System.out.println("Test case --verify_search_filter_by_city_country_profile-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Opening New users page
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openAddNewUsersPage(supportDriver);

		usersPage.searchCityInAddNewUser(supportDriver, "Jaipur");
		usersPage.searchCountryInAddNewUser(supportDriver, "India");
		usersPage.searchSFDCInAddNewUser(supportDriver, "Chatter Free User");
		
		// updating the drivers used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_search_filter_by_city_country_profile-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void add_multiple_additional_smart_number_without_area_code() {
		System.out.println("Test case --add_multiple_additional_smart_number_without_area_code-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		String labelName =  "AutoLabel".concat(HelperFunctions.GetRandomString(3));
		
		//opening overview tab
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		//adding multiple smart numbers
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		Pair<String,List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, AddSmartNumberPage.Country.UNITED_STATES.displayName(), null, null, labelName, Type.Additional.toString(), SmartNumberCount.Multiple.toString());
		for(String additionalSmartNo: smartNoPair.second()) {
			userIntelligentTab.verifyAdditionalTypePage(supportDriver, additionalSmartNo);
		}
		
		//deleting additional number
		for(String additionalSmartNo: smartNoPair.second()) {
			userIntelligentTab.deleteSmartNumber(supportDriver, additionalSmartNo);
		}

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_multiple_smart_number-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_remove_selected_multiple_numbers() {
		System.out.println("Test case --verify_remove_selected_multiple_numbers-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//selecting and removing multiple numbers
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndRemoveMultipleNumbers(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_remove_selected_multiple_numbers-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_address_verification_window_for_country() {
		System.out.println("Test case --verify_address_verification_window_for_country-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//selecting and removing multiple numbers
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.verifyAddressVerificationWindow(supportDriver, "Australia");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_address_verification_window_for_country-- passed ");
	}
	

	@Test(groups = { "MediumPriority", "Product Sanity" })
	public void remove_verify_call_queue_from_user_overview() {
		// updating the supportDriver used
		System.out.println("Test case --remove_verify_call_queue_from_user_overview-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.addMember(supportDriver, qa_user_name);
		
		// verifying call queue in users page after deleting
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.openOverViewTab(supportDriver);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.deleteCallQueue(supportDriver, callQueueName);
		assertFalse(userIntelligentTab.isCallQueuePresent(supportDriver, callQueueName), String.format("call Queue : %s exists even after deleting", callQueueName));

		//verifying queue not present
		callQueuePage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		assertFalse(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName));
		
		//verifying agent removed from call queue
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		assertFalse(callQueuePage.isAgentAddedAsMember(supportDriver, qa_user_name));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --remove_verify_call_queue_from_user_overview-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void existing_number_reassign_users_default_to_other_user() {
		
		System.out.println("Test case --existing_number_reassign_users_default_to_other_user-- started ");

		//updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if (!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, "", HelperFunctions.getRandomAreaCode(), "22", smartLabel, Type.Default.toString(), SmartNumberCount.Single.toString());
		}

		String defaultSmartNo = userIntelligentTab.getDefaultNo(supportDriver);
		
		// opening overview tab and assigning other users default number
		String label = "AutoLabelDefaultUser".concat(HelperFunctions.GetRandomString(3));
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, defaultSmartNo, label, null, Type.Additional);
		
		//verifying other users default added as additional
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, defaultSmartNo);
		
		//verifying default number removed from other user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentTab.isDefaultSmartNumberPresent(supportDriver));
		
		// deleting additional number on user
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.deleteSmartNumber(supportDriver, defaultSmartNo);

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_users_default_to_other_user-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void existing_number_reassign_central_to_users_default() {
		System.out.println("Test case --existing_number_reassign_central_to_users_default-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding central number if not present
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		if( accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()){
			String centralLabelName =  "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			Pair<String,List<String>> centralNumberPair = accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumberPair.first());
			assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));
		}
			
		String centralNumber = accountIntelligentTab.getCentralNoList(supportDriver).get(0);
		
		// opening overview tab of other user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		// Reassigning central to default
		String label = "AutoLabelCentralToDefault".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, centralNumber, label, null, Type.Default);
		
		//verifying number 
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, centralNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_central_to_users_default-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void existing_number_reassign_central_to_users_additional() {
		System.out.println("Test case --existing_number_reassign_central_to_users_additional-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// adding central number if not present
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		if( accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()){
			String centralLabelName =  "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			Pair<String,List<String>> centralNumberPair = accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumberPair.first());
			assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));
		}
			
		String centralNumber = accountIntelligentTab.getCentralNoList(supportDriver).get(0);
		
		//opening overview tab of other user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		//Reassigning central to default
		String label = "AutoLabelCentralToAdditional".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, centralNumber, label, null, Type.Additional);
		
		//verifying number 
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, centralNumber);
		userIntelligentTab.deleteSmartNumber(supportDriver, centralNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_central_to_users_additional-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void existing_number_reassign_additional_to_users_default() {
		System.out.println("Test case --existing_number_reassign_additional_to_users_default-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab of other user
		String smartlabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartlabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}
		
		String additionalSmartNo = userIntelligentTab.getAdditionalNumberList(supportDriver).get(0);
		
		//Reassigning additional to default of other user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		String label = "AutoLabelAddToDefault".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, additionalSmartNo, label, null, Type.Default);
		
		//verifying number 
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, additionalSmartNo);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_additional_to_users_default-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void existing_number_reassign_additional_of_other_user_to_current_user() {
		System.out.println("Test case --existing_number_reassign_additional_of_other_user_to_current_user-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//opening overview tab of other user
		String smartlabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartlabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}
		
		String additionalSmartNo = userIntelligentTab.getAdditionalNumberList(supportDriver).get(0);
		
		//Reassigning additional to current user
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		String label = "AutoLabelAddToAdd".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, additionalSmartNo, label, null, Type.Additional);
		
		//verifying number 
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, additionalSmartNo);
		userIntelligentTab.deleteSmartNumber(supportDriver, additionalSmartNo);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --existing_number_reassign_additional_of_other_user_to_current_user-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_group_number_to_users_default() {
		System.out.println("Test case --verify_existing_number_group_number_to_users_default-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);

		//getting additional number
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String, List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, null, SmartNumberCount.Single.toString());
		
		String defaultNumber = smartNoPair.first();
		
		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		String label = "AutoQueueToDefault".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, defaultNumber, label, null, Type.Default);
		
		//verifying number 
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, defaultNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_group_number_to_users_default-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_group_number_to_users_additional() {
		System.out.println("Test case --verify_existing_number_group_number_to_users_additional-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		String smartLabel = "AutoSmartLabel".concat(HelperFunctions.GetRandomString(3));
		
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);

		//getting additional number
		callQueuePage.clickSmartNumberIcon(supportDriver);
		Pair<String, List<String>> smartNoPair = addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, smartLabel, null, SmartNumberCount.Single.toString());
		
		String defaultNumber = smartNoPair.first();
		
		//deleting call queue
		callQueuePage.deleteCallQueue(supportDriver);
		
		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		String label = "AutoQueueToDefault".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, defaultNumber, label, null, Type.Additional);
		
		//verifying number 
		userIntelligentTab.verifyAdditionalTypePage(supportDriver, defaultNumber);;
		userIntelligentTab.deleteSmartNumber(supportDriver, defaultNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_group_number_to_users_additional-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_existing_number_tracking_number_to_users_default() {
		System.out.println("Test case --verify_existing_number_tracking_number_to_users_default-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// verifying number tracking type
		String trackingNumber = CONFIG.getProperty("automation_tracking_number2");
		String callFlow = CONFIG.getProperty("qa_call_flow_new_org");

		// undeleting smart number if deleted
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNumber(supportDriver, trackingNumber);
		if (smartNumbersPage.isUndeleteBtnVisible(supportDriver)) {
			smartNumbersPage.clickUndeleteBtn(supportDriver);
		}

		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);

		// converting to tracking type if number not of tracking type
		if (!smartNumbersPage.getNumberType(supportDriver, trackingNumber).equals(smartNumberType.Tracking.toString())
				|| smartNumbersPage.isNumberCallFlowEmpty(supportDriver, trackingNumber)) {
			String label = "AutoTypeToTracking".concat(HelperFunctions.GetRandomString(3));
			dashboard.navigateToManageCallFlow(supportDriver);

			// search and select call flow
			callFlowPage.searchCallFlow(supportDriver, callFlow, qa_user_account);
			callFlowPage.clickSelectedCallFlow(supportDriver, callFlow);
			callFlowPage.clickAddSmartNoIcon(supportDriver);
			addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, null);
		}

		// Opening Users Tab
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		String label = "AutoTrackingToDefault".concat(HelperFunctions.GetRandomString(3));
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectAndReassignNumber(supportDriver, trackingNumber, label, null, Type.Default);

		// verifying number
		userIntelligentTab.verifyDefaultSmartNoPage(supportDriver, trackingNumber);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_number_tracking_number_to_users_default-- passed ");
	}
	
	@Test(groups = { "MediumPriority"})
	public void change_user_role_admin_to_agent() {
		
		// updating the supportDriver used
		System.out.println("Test case --change_user_role_admin_to_agent-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// opening users tab to check group
		String user_ops_name = CONFIG.getProperty("qa_user_operations_name");
		String user_ops_email = CONFIG.getProperty("qa_user_operations_email");
		String user_ops_salesForce2_id = CONFIG.getProperty("qa_user_operations_salesforce_id");
		
		//Navigating to Manage users page
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, user_ops_name, user_ops_email, user_ops_salesForce2_id);
		
		//Default Changing role to Admin if not found
		if(!usersPage.getCurrentUserRole(supportDriver).equals(UserRole.Admin.toString())){
			usersPage.selectRole(supportDriver, UserRole.Admin);
			usersPage.saveUserOverviewPage(supportDriver);
			dashboard.isPaceBarInvisible(supportDriver);
		}
		
		//Converting to Agent role
		usersPage.selectRole(supportDriver, UserRole.Agent);
		usersPage.saveUserOverviewPage(supportDriver);
		assertEquals(usersPage.getCurrentUserRole(supportDriver), UserRole.Agent.toString());
		
		//Now login and verifying
		dashboard.idleWait(2);
		WebDriver userRoleDriver = getDriver();
		softPhoneLoginPage.supportLoginWhenSoftphoneLogin(userRoleDriver, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_user_operations_username"), CONFIG.getProperty("qa_user_operations_password"));
		dashboard.isPaceBarInvisible(userRoleDriver);
		
		//Verifying for support assertions
		dashboard.refreshCurrentDocument(userRoleDriver);
		dashboard.isPaceBarInvisible(userRoleDriver);
		dashboard.idleWait(1); 
		dashboard.assertDashBoardForAgentUser(userRoleDriver);
		
		//Changing back to Admin role 
		usersPage.switchToTab(supportDriver, 2);
		usersPage.selectRole(supportDriver, UserRole.Admin);
		usersPage.saveUserOverviewPage(supportDriver);				

		//Verifying for admin assertions
		dashboard.refreshCurrentDocument(userRoleDriver);
		dashboard.isPaceBarInvisible(userRoleDriver);
		dashboard.assertDashBoardForAdminUser(userRoleDriver);
		System.out.println("Successfully changed back to Admin role");
		userRoleDriver.quit();
		
		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --change_user_role_admin_to_agent-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_added_team_visible_in_user_to_team_dropdown() {

		// updating the supportDriver used
		System.out.println("Test case --verify_added_team_visible_in_user_to_team_dropdown-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//adding new team
		dashboard.switchToTab(supportDriver, 2);
		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		
		// verifying team visible in User To Teams section
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.clickAddTeamIcon(supportDriver);
		assertTrue(usersPage.getTeamNamesListOnAddUserToTeam(supportDriver).size() > 0);
		assertFalse(usersPage.getTeamNamesListOnAddUserToTeam(supportDriver).isEmpty());

		assertTrue(usersPage.verifyUserToTeamSectionContainsTeam(supportDriver, teamDetailName));
		
		//deleting team
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_added_team_visible_in_user_to_team_dropdown-- passed");
	}
	
	@Test(groups = { "AdminOnly", "MediumPriority" })
	public void verify_admin_user_cant_logout_user_when_busy_on_call() {

		// updating the drivers used
		System.out.println("Test case --verify_admin_user_cant_logout_user_when_busy_on_call-- started");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//hanging up active call if any
		softPhoneCalling.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.disableCallForwardingSettings(supportDriver);
		
		initializeSupport("webSupportDriver");
		driverUsed.put("webSupportDriver", true);
		
		//hanging up active call if any
		softPhoneCalling.switchToTab(webSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		softphoneSettings.clickSettingIcon(webSupportDriver);
		softphoneSettings.disableCallForwardingSettings(webSupportDriver);
		
		//verifying logout user button not visible on support
		dashboard.switchToTab(webSupportDriver, 2);
		dashboard.clickOnUserProfile(webSupportDriver);
		assertFalse(usersPage.isLogOutUserBtnVisible(webSupportDriver));

		//deleting emergency number or block number if any
		usersPage.clickAccountLink(webSupportDriver);
		emergencyTab.navigateToEmergencyTab(webSupportDriver);

		// clean up code
		emergencyTab.cleanUpToggleRoutings(webSupportDriver);
		emergencyTab.deleteAllRouting(webSupportDriver);
		
		//deleting block number if any
		blockedNumbersTab.navigateToBlockedNumbersTab(webSupportDriver);
		blockedNumbersTab.deleteAllBlockedNumber(webSupportDriver);
		
		// Making admin caller busy
		usersPage.switchToTab(supportDriver, 1);
		softPhoneCalling.softphoneAgentCall(supportDriver, CONFIG.getProperty("qa_support_user_number"));
		softPhoneCalling.isCallHoldButtonVisible(supportDriver);
		
		dashboard.switchToTab(webSupportDriver, 1);
		softPhoneCalling.pickupIncomingCall(webSupportDriver);

		//verifying admin user cant logout
		usersPage.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, CONFIG.getProperty("qa_support_user_name"), CONFIG.getProperty("qa_support_user_email"), CONFIG.getProperty("qa_support_user_salesforce_id"));
		usersPage.openOverViewTab(supportDriver);
		
		assertTrue(usersPage.isOnCallPresenceVisible(supportDriver));
		assertTrue(usersPage.isLogOutUserBtnDisabled(supportDriver));
		
		//hanging up call
		usersPage.switchToTab(webSupportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(webSupportDriver);
		
		// hanging up call
		usersPage.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		// updating the drivers used
		driverUsed.put("supportDriver", false);
		driverUsed.put("webSupportDriver", false);
		System.out.println("Test case --verify_admin_user_cant_logout_user_when_busy_on_call-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void add_verify_multiple_queues_to_user() {
		// updating the supportDriver used
		System.out.println("Test case --add_verify_multiple_queues_to_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// opening overview tab of other user
		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		
		//adding new queue details
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		
		//adding new group details
		String callQueueName2 = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName2 = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName2, callQueueDescName2);
		
		//adding call queue
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickAddCallQueueIcon(supportDriver);
		userIntelligentTab.selectUserFromCallQueue(supportDriver, callQueueName);
		userIntelligentTab.selectUserFromCallQueue(supportDriver, callQueueName2);
		userIntelligentTab.clickSaveUserQueueBtn(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertTrue(userIntelligentTab.isCallQueuePresent(supportDriver, callQueueName));
		assertTrue(userIntelligentTab.isCallQueuePresent(supportDriver, callQueueName2));
		
		// verifying queue present in softphone
		callQueuePage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneCallQueues.openCallQueuesSection(supportDriver);
		assertTrue(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName));
		assertTrue(softPhoneCallQueues.isGroupPresentInJoinQueue(supportDriver, callQueueName2));
		
		//deleting queues
		callQueuePage.switchToTab(supportDriver, 2);
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName);
		callQueuePage.deleteCallQueue(supportDriver);
		
		callQueuePage.openCallQueueSearchPage(supportDriver);
		callQueuePage.searchCallQueues(supportDriver, callQueueName2, qa_user_account);
		callQueuePage.selectCallQueue(supportDriver, callQueueName2);
		callQueuePage.deleteCallQueue(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_verify_multiple_queues_to_user-- passed ");
	}

	@Test(groups = {"MediumPriority"})
	public void add_verify_multiple_teams_to_user() {
		// updating the supportDriver used
		System.out.println("Test case --add_verify_multiple_teams_to_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// opening overview tab of other user
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		groupsPage.openAddNewGroupPage(supportDriver);
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addMember(supportDriver, qa_user_name);
		groupsPage.addSupervisor(supportDriver, CONFIG.getProperty("qa_agent_user_name"));
		groupsPage.setOpenBarge(supportDriver, true);
		
		//adding second team
		String teamDetailName2 = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc2 = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		groupsPage.openAddNewGroupPage(supportDriver);
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName2, teamDetailDesc2);
		groupsPage.addMember(supportDriver, qa_user_name);
		groupsPage.addSupervisor(supportDriver, "Akshita test");
		groupsPage.setOpenBarge(supportDriver, true);
		
		//removing user to team
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.clickAddTeamIcon(supportDriver);
		usersPage.removeUserToTeam(supportDriver, teamDetailName);
		usersPage.clickAddTeamIcon(supportDriver);
		usersPage.removeUserToTeam(supportDriver, teamDetailName2);
		
		//adding multiple users to team
		usersPage.clickAddTeamIcon(supportDriver);
		usersPage.selectUserFromTeam(supportDriver, teamDetailName);
		usersPage.selectUserFromTeam(supportDriver, teamDetailName2);
		usersPage.clickSaveUserTeamBtn(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		assertTrue(usersPage.isTeamExists(supportDriver, teamDetailName));
		assertTrue(usersPage.isTeamExists(supportDriver, teamDetailName2));
		
		// verifying team present in softphone
		callQueuePage.switchToTab(supportDriver, 1);
		softphoneSettings.reloadSoftphone(supportDriver);
		softPhoneTeamPage.openTeamSection(supportDriver);
		assertTrue(softPhoneTeamPage.isTeamMemberVisible(supportDriver, CONFIG.getProperty("qa_agent_user_name")));
		assertTrue(softPhoneTeamPage.isTeamMemberVisible(supportDriver, "Akshita test"));
		
		//deleting teams
		callQueuePage.switchToTab(supportDriver, 2);
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName);
		groupsPage.deleteGroup(supportDriver);
		
		groupsPage.openGroupSearchPage(supportDriver);
		groupsPage.searchGroups(supportDriver, teamDetailName2, qa_user_account);
		groupsPage.clickTeam(supportDriver, teamDetailName2);
		groupsPage.deleteGroup(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --add_verify_multiple_teams_to_user-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_filter_user_by_users_status() {
		// updating the supportDriver used
		System.out.println("Test case --verify_filter_user_by_users_status-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.openManageUsersPage(supportDriver);
		assertEquals(usersPage.getDefaltValueUserStatus(supportDriver), UserStatus.All.displayName());
		
		usersPage.selectUserStatus(supportDriver, UserStatus.Active);
		usersPage.clickSearchButton(supportDriver);
		assertTrue(usersPage.verifyUserStatusTableContains(supportDriver, UserStatus.Active));
		
		usersPage.selectUserStatus(supportDriver, UserStatus.Deleted);
		usersPage.clickSearchButton(supportDriver);
		assertTrue(usersPage.verifyUserStatusTableContains(supportDriver, UserStatus.Deleted));
	
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_filter_user_by_users_status-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_forwarding_smart_number_clickable_enabling_disabling_call_forwarding() {
		// updating the supportDriver used
		System.out.println("Test case --verify_forwarding_smart_number_clickable_enabling_disabling_call_forwarding-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		
		userIntelligentDialerPage.disableCallForwarding(supportDriver);
		assertTrue(userIntelligentDialerPage.isCallForwardingUsingSmartNumberDisabled(supportDriver));
		
		userIntelligentDialerPage.enableCallForwarding(supportDriver);
		assertFalse(userIntelligentDialerPage.isCallForwardingUsingSmartNumberDisabled(supportDriver));
		
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_forwarding_smart_number_clickable_enabling_disabling_call_forwarding-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_disable_offline_forwarding_clickable_enabling_disabling_call_forwarding() {
		// updating the supportDriver used
		System.out.println("Test case --verify_disable_offline_forwarding_clickable_enabling_disabling_call_forwarding-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//enabling disable offline forwarding at account level
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		accountIntelligentTab.enableCallForwardingDisabledOfflineSetting(supportDriver);
		accountIntelligentTab.unlockCallForwardingDisabledOffline(supportDriver);
		accountIntelligentTab.saveAcccountSettings(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		
		userIntelligentDialerPage.disableCallForwarding(supportDriver);
		assertFalse(userIntelligentDialerPage.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		
		userIntelligentDialerPage.enableCallForwarding(supportDriver);
		assertTrue(userIntelligentDialerPage.isDisableOfflineForwardingToggleBtnEditable(supportDriver));
		
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_disable_offline_forwarding_clickable_enabling_disabling_call_forwarding-- passed ");
	}
	
//	@Test(groups = {"MediumPriority"})
	public void verify_extension_of_user_under_users_list() {
		// updating the supportDriver used
		System.out.println("Test case --verify_extension_of_user_under_users_list-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.searchUserWithSalesForceId(supportDriver, "Akshita test", "", "057F000021j2TlQAI");
		assertTrue(usersPage.isExtensionListCoulumnVisible(supportDriver));
		
		String extension= HelperFunctions.generateTenDigitNumber();
		usersPage.clickFirstUserAccountLink(supportDriver);
		usersPage.createExtension(supportDriver, extension);
		
		dashboard.openManageUsersPage(supportDriver);
		usersPage.searchUserWithSalesForceId(supportDriver, "Akshita test", "", "057F000021j2TlQAI");
		
		assertEquals(usersPage.getExtensionColumnnValue(supportDriver, "Akshita test"), extension);
		
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_extension_of_user_under_users_list-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_ability_to_order_voicemail_drops() {
		// updating the supportDriver used
		System.out.println("Test case --verify_ability_to_order_voicemail_drops-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		List<String> supportVoiceMailOrderList = userIntelligentDialerPage.orderDownVoiceMailDrop(supportDriver);
		userIntelligentDialerPage.saveAcccountSettings(supportDriver);
		
		//navigating to softphone and verifying order list
		dashboard.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.navigateToVoicemailDropTab(supportDriver);
		assertEquals(softphoneSettings.getVoiceMailList(supportDriver), supportVoiceMailOrderList);
		
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_ability_to_order_voicemail_drops-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_license_status_not_visible_on_user_overview() {
		// updating the supportDriver used
		System.out.println("Test case --verify_license_status_not_visible_on_user_overview-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		assertFalse(usersPage.isLicenseStatusVisible(supportDriver));
		
		// updating the supportDriver false
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_license_status_not_visible_on_user_overview-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_existing_numbers_sorted_functionality() {
		// updating the supportDriver used
		System.out.println("Test case --verify_existing_numbers_sorted-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// opening overview tab and creating new location
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);
		
		assertTrue(addSmartNoPage.verifyAscendingHeaderType(supportDriver, SmartNumberHeaders.Number));
		assertTrue(addSmartNoPage.verifyAscendingHeaderType(supportDriver, SmartNumberHeaders.Type));
		assertTrue(addSmartNoPage.verifyAscendingHeaderType(supportDriver, SmartNumberHeaders.Owner));
		assertTrue(addSmartNoPage.verifyAscendingHeaderType(supportDriver, SmartNumberHeaders.Label));
		
		assertTrue(addSmartNoPage.verifyDescendingHeaderType(supportDriver, SmartNumberHeaders.Number));
		assertTrue(addSmartNoPage.verifyDescendingHeaderType(supportDriver, SmartNumberHeaders.Type));
		assertTrue(addSmartNoPage.verifyDescendingHeaderType(supportDriver, SmartNumberHeaders.Owner));
		assertTrue(addSmartNoPage.verifyDescendingHeaderType(supportDriver, SmartNumberHeaders.Label));
		
		addSmartNoPage.closeSmartNumberWindow(supportDriver);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_existing_numbers_sorted_functionality-- passed ");
	}
	
	@Test(groups = {"MediumPriority", "SupportOnly"})
	public void verify_missed_call_count_value() {
		// updating the supportDriver used
		System.out.println("Test case --verify_missed_call_count_value-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(supportDriver);
		
		// opening overview tab and creating new location
		dashboard.switchToTab(supportDriver, 2);
		dashboard.refreshCurrentDocument(supportDriver);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertEquals(userIntelligentTab.getMissedCallCount(supportDriver), "0");
		
		dashboard.switchToTab(supportDriver, 1);
		callScreenPage.setUserImageBusy(supportDriver);
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to support
		softPhoneCalling.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, qa_user_number);
		softPhoneCalling.verifyDeclineButtonIsInvisible(agentDriver);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.refreshCurrentDocument(supportDriver);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		assertEquals(userIntelligentTab.getMissedCallCount(supportDriver), "1");
		
		dashboard.switchToTab(supportDriver, 1);
		callScreenPage.setUserImageAvailable(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_missed_call_count_value-- passed ");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_smart_number_box_checked_delete_user() {
		// updating the supportDriver used
		System.out.println("Test case --verify_smart_number_box_checked_delete_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String defaultLabel = "AutoDefaultLabel".concat(HelperFunctions.GetRandomString(3));
		String additionalLabel = "AutoAdditionalLabel".concat(HelperFunctions.GetRandomString(3));
		
		//undeleting user if deleted
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}
		
		// adding default if not present
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		if (!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, defaultLabel, Type.Default.toString(),
					SmartNumberCount.Single.toString());
		}
		
		String defaultSmartNo = userIntelligentTab.getDefaultNo(supportDriver);
		
		// adding additional if not present
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, additionalLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}
		
		String additionalSmartNo = userIntelligentTab.getAdditionalNumberList(supportDriver).get(0);
		
		//deleting user
		usersPage.openOverViewTab(supportDriver);
		usersPage.deleteUserWithSmartNumber(supportDriver, qa_user2_name, defaultSmartNo, additionalSmartNo);
		
		//undeleting user
		usersPage.clickUnDeleteBtn(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_smart_number_box_checked_delete_user-- passed ");
	}

	@Test(groups = {"MediumPriority", "SupportOnly"})
	public void verify_smart_number_box_unchecked_delete_user() {
		// updating the supportDriver used
		System.out.println("Test case --verify_smart_number_box_unchecked_delete_user-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		String defaultLabel = "AutoDefaultLabel".concat(HelperFunctions.GetRandomString(3));
		String additionalLabel = "AutoAdditionalLabel".concat(HelperFunctions.GetRandomString(3));
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);

		//undeleting user if deleted
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
		}
		
		// Opening user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		// adding default if not present
		if (!userIntelligentTab.isDefaultSmartNumberPresent(supportDriver)) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, defaultLabel, Type.Default.toString(),
					SmartNumberCount.Single.toString());
		}
		
		String defaultSmartNo = userIntelligentTab.getDefaultNo(supportDriver);
		
		// adding additional if not present
		if (userIntelligentTab.getAdditionalNumberList(supportDriver) == null || userIntelligentTab.getAdditionalNumberList(supportDriver).isEmpty()) {
			userIntelligentTab.clickSmartNoIcon(supportDriver);
			addSmartNoPage.addNewSmartNumber(supportDriver, null, null, null, additionalLabel, Type.Additional.toString(), SmartNumberCount.Single.toString());
		}
		
		String additionalSmartNo = userIntelligentTab.getAdditionalNumberList(supportDriver).get(0);
		
		//deleting user
		usersPage.openOverViewTab(supportDriver);
		usersPage.deleteUser(supportDriver, qa_user2_name);
		
		//verifying with existing smart number
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		
		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);
		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.Unassigned);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Unassigned.name()));
		
		addSmartNoPage.searchSmartNo(supportDriver, defaultSmartNo);
		assertEquals(addSmartNoPage.getExistingNumberLabelName(supportDriver, defaultSmartNo), "Prior: ".concat(qa_user2_name));
		assertEquals(addSmartNoPage.getExistingNumberType(supportDriver, defaultSmartNo), "Unassigned");

		addSmartNoPage.searchSmartNo(supportDriver, additionalSmartNo);
		assertEquals(addSmartNoPage.getExistingNumberLabelName(supportDriver, additionalSmartNo), "Prior: ".concat(qa_user2_name));
		assertEquals(addSmartNoPage.getExistingNumberType(supportDriver, additionalSmartNo), "Unassigned");
		
		addSmartNoPage.closeSmartNumberWindow(supportDriver);
		
		//searching smart number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.clickSmartNumber(supportDriver, defaultSmartNo);

		// deleting smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		smartNumbersPage.clickSaveBtn(supportDriver);
		
		//searching smart number
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.clickSmartNumber(supportDriver, additionalSmartNo);

		// deleting smart number
		smartNumbersPage.deleteSmartNumber(supportDriver);
		smartNumbersPage.clickSaveBtn(supportDriver);

		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);

		//undeleting user if deleted
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
			usersPage.savedetails(supportDriver);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_smart_number_box_unchecked_delete_user-- passed ");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_call_forwarding_field_not_empty_when_location_changed() {
		// updating the supportDriver used
		System.out.println("Test case --verify_call_forwarding_field_not_empty_when_location_changed-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//saving location with new
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.selectLocation(supportDriver, "AutomationLocation");
		usersPage.savedetails(supportDriver);
	
		//enabling call forwarding
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		userIntelligentDialerPage.enableCallForwarding(supportDriver);
		userIntelligentDialerPage.saveAcccountSettingsDialer(supportDriver);
		
		//updating location
		usersPage.openOverViewTab(supportDriver);
		usersPage.selectLocation(supportDriver, "Jaipur");
		usersPage.savedetails(supportDriver);
		userIntelligentDialerPage.isSaveAccountsSettingMessageDisappeared(supportDriver);
		
		assertEquals(usersPage.getDefaultLocation(supportDriver), "Jaipur");
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		assertFalse(userIntelligentDialerPage.isCallForwardingNumberBoxEmpty(supportDriver));
		userIntelligentDialerPage.disableCallForwarding(supportDriver);
		userIntelligentDialerPage.saveAcccountSettingsDialer(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_forwarding_field_not_empty_when_location_changed-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_type_filter_existing_smart_number_search() {
		// updating the supportDriver used
		System.out.println("Test case --verify_type_filter_existing_smart_number_search-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//adding central if not present
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		accountIntelligentTab.openIntelligentDialerTab(supportDriver);
		if( accountIntelligentTab.getCentralNoList(supportDriver) == null || accountIntelligentTab.getCentralNoList(supportDriver).isEmpty()){
			String centralLabelName =  "AutoCentralNoLabel".concat(HelperFunctions.GetRandomString(3));
			Pair<String,List<String>> centralNumberPair = accountIntelligentTab.addNewCentralNumber(supportDriver, centralLabelName);
			boolean centralNoExists = accountIntelligentTab.isCentralNumberPresent(supportDriver, centralNumberPair.first());
			assertTrue(centralNoExists, String.format("Central number name:%s does not exists after creating", centralLabelName));
		}
			
		//searching owner
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);

		userIntelligentTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);
		assertEquals(addSmartNoPage.getDefaultExistingTypeOfNumber(supportDriver), "All");
		
		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.Additional);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Additional.name()));

		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.Tracking);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Tracking.name()));

		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.CentralNumber);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		addSmartNoPage.idleWait(1);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.CentralNumber.name()));

		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.Default);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		addSmartNoPage.idleWait(1);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.Default.name()));

		addSmartNoPage.closeSmartNumberWindow(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_type_filter_existing_smart_number_search-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_call_queue_name_appear_hyperlink() {
		// updating the supportDriver used
		System.out.println("Test case --verify_call_queue_name_appear_hyperlink-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		String callQueueName = "AutoCallQueueName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String callQueueDescName = "AutoCallQueueDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));

		callQueuePage.openAddNewCallQueue(supportDriver);
		callQueuePage.addNewCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		callQueuePage.addMember(supportDriver, qa_user_name);
		
		// clicking call queue hyperlink
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.openOverViewTab(supportDriver);
		userIntelligentDialerPage.isOverviewTabHeadingPresent(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.clickCallQueueLink(supportDriver, callQueueName);
		usersPage.idleWait(1);
		callQueuePage.verifyCallQueueDetails(supportDriver, callQueueName, callQueueDescName);
		assertTrue(callQueuePage.isAgentAddedAsMember(supportDriver, qa_user_name));
		callQueuePage.deleteCallQueue(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_queue_name_appear_hyperlink-- passed ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_team_name_appear_hyperlink() {
		// updating the supportDriver used
		System.out.println("Test case --verify_team_name_appear_hyperlink-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		groupsPage.openAddNewGroupPage(supportDriver);
		String teamDetailName = "AutoTeamName".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		String teamDetailDesc = "AutoTeamDesc".concat(HelperFunctions.GetCurrentDateTime("dd/MM/yyyy HH:mm:ss.SSS"));
		groupsPage.addNewGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.addMember(supportDriver, qa_user_name);
		
		// clicking team name hyperlink
		dashboard.clickOnUserProfile(supportDriver);
		usersPage.openOverViewTab(supportDriver);
		usersPage.clickTeamNameLink(supportDriver, teamDetailName);
		groupsPage.verifyGroupDetails(supportDriver, teamDetailName, teamDetailDesc);
		groupsPage.deleteGroup(supportDriver);

		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_team_name_appear_hyperlink-- passed ");
	}
	
	
	// Show download icon on User list when Account selected by support role
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_download_icon_on_user_list() {
		// updating the supportDriver used
		System.out.println("Test case --verify_download_icon_on_user_list-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open manage user page
		dashboard.openManageUsersPage(supportDriver);
		assertFalse(usersPage.isDownloadUsersOptionVisible(supportDriver));
		usersPage.clickSearchButton(supportDriver);
		assertFalse(usersPage.isDownloadUsersOptionVisible(supportDriver));
		
		//Select status and user name
		usersPage.selectUserStatus(supportDriver, UserStatus.Active);
		String userName = reportCommonPage.getUserName(supportDriver);
		usersPage.enterUserName(supportDriver, userName);
		usersPage.clickSearchButton(supportDriver);
		assertFalse(usersPage.isDownloadUsersOptionVisible(supportDriver));
		
		//refresh the user page and enter account name
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.openManageUsersPage(supportDriver);
		reportCommonPage.enterAndSelectAccountName(supportDriver, qa_user_account);
		usersPage.clickSearchButton(supportDriver);
		assertTrue(usersPage.isDownloadUsersOptionVisible(supportDriver));
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_download_icon_on_user_list-- passed ");
	}
	
	public void verify_toggle_settings_on_off_on_softphone() {
		// updating the supportDriver used
		System.out.println("Test case --verify_toggle_settings_on_off_on_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open intelligent dialer tab
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerPage.openIntelligentDialerTab(supportDriver);
		
		//enable settings
		userIntelligentDialerPage.enableWebLeadsSetting(supportDriver);
		userIntelligentDialerPage.enableCallForwarding(supportDriver);
		userIntelligentDialerPage.saveAcccountSettings(supportDriver);
		
		assertTrue(userIntelligentTab.isCallForwardingToggleBtnEditable(supportDriver));
		assertFalse(userIntelligentTab.isCallForwardingNumberBoxDisabled(supportDriver));
		
		//disabling call forwarding
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		userIntelligentDialerPage.disableCallForwarding(supportDriver);
		userIntelligentDialerPage.saveAcccountSettings(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_toggle_settings_on_off_on_softphone-- passed ");
	}
	
	// " Do Not forward to device..." setting should in sync with Softphone
	@Test(groups = { "Regression" })
	public void verify_do_not_forward_setting_on_softphone() {
		// updating the supportDriver used
		System.out.println("Test case --verify_do_not_forward_setting_on_softphone-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//verify call forwarding visible on settings page
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		softPhoneSettingsPage.enableForwardWhenIntelligentDialerOffline(supportDriver);
		
		//verify call forwarding visible on settings page on admin site
		softPhoneSettingsPage.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		userIntelligentTab.isCallForwardingEnabled(supportDriver);
		userIntelligentTab.isDisableCallForwardingEnabled(supportDriver);
		
		//disable call forwarding visible on settings page
		softPhoneSettingsPage.switchToTab(supportDriver, 1);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.disableCallForwardingSettings(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_do_not_forward_setting_on_softphone-- passed ");
	}
	
	// Verify Provided create and delete metadata should NOT be available for AGENT
	@Test(groups = { "Regression" })
	public void verify_delete_metadata_should_not_be_available_for_agent() {
		System.out.println("Test case --verify_delete_metadata_should_not_be_available_for_agent-- started ");
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		usersPage.switchToTab(agentDriver, 2);

		// opening overview tab
		dashboard.clickOnUserProfile(agentDriver);
		userIntelligentTab.isOverviewTabHeadingPresent(agentDriver);

		// assert
		assertFalse(usersPage.isDeletedVisible(agentDriver));
		assertFalse(usersPage.isDeletedByVisible(agentDriver));
		assertFalse(usersPage.isDateDeletedVisible(agentDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("agentDriver", false);
		System.out.println("Test case is pass");
	}
	
	// Verify (support role) availability of Move assorted settings groups from User
	// Overview tab to the User Intelligent Dialer Settings tab
	@Test(groups = { "MediumPriority", "SupportOnly" })
	public void verify_user_overview_header_move_to_user_id_tab_for_support() {
		// updating the supportDriver used
		System.out.println("Test case --verify_user_overview_header_move_to_user_id_tab_for_support-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// searching owner
		dashboard.clickOnUserProfile(supportDriver);

		// not visible
		assertFalse(userIntelligentTab.isCallQueuesHeadervisible(supportDriver));
		assertFalse(userIntelligentTab.isCallProcessingHeadervisible(supportDriver));
		assertFalse(userIntelligentTab.isSmartNumbersHeadervisible(supportDriver));

		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);

		// visible
		assertTrue(userIntelligentTab.isCallQueuesHeadervisible(supportDriver));
		assertTrue(userIntelligentTab.isCallProcessingHeadervisible(supportDriver));
		assertTrue(userIntelligentTab.isSmartNumbersHeadervisible(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_overview_header_move_to_user_id_tab_for_support-- passed ");
	}

	// Verify (admin role) availability of Move assorted settings groups from User
	// Overview tab to the User Intelligent Dialer Settings tab
	@Test(groups = { "MediumPriority", "AdminOnly" })
	public void verify_user_overview_header_move_to_user_id_tab_for_admin() {
		// updating the supportDriver used
		System.out.println("Test case --verify_user_overview_header_move_to_user_id_tab_for_admin-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// searching owner
		dashboard.clickOnUserProfile(supportDriver);

		// not visible
		assertFalse(userIntelligentTab.isCallQueuesHeadervisible(supportDriver));
		assertFalse(userIntelligentTab.isSmartNumbersHeadervisible(supportDriver));

		userIntelligentTab.openIntelligentDialerTab(supportDriver);
		dashboard.isPaceBarInvisible(supportDriver);

		// visible
		assertTrue(userIntelligentTab.isCallQueuesHeadervisible(supportDriver));
		assertTrue(userIntelligentTab.isSmartNumbersHeadervisible(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		System.out.println("in after method");
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_overview_header_move_to_user_id_tab_for_admin-- passed ");
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "verify_users_outbound_call_with_stay_connected":
			case "verify_smart_number_displayed_on_call_forwarding":	
				initializeSupport();
				driverUsed.put("supportDriver", true);
			
				// Setting call forwarding OFF and disable stay connected setting
				System.out.println("Setting call forwarding OFF");
				softphoneSettings.switchToTab(supportDriver, 1);
				softphoneSettings.clickSettingIcon(supportDriver);
				softphoneSettings.disableCallForwardingSettings(supportDriver);
				driverUsed.put("supportDriver", false);
				break;
			case "verify_missed_call_count_value":	
				initializeSupport();
				driverUsed.put("supportDriver", true);
			
				dashboard.switchToTab(supportDriver, 1);
				callScreenPage.setUserImageAvailable(supportDriver);
				driverUsed.put("supportDriver", false);
				break;
			}
		}
	}
	
	@AfterClass(groups = { "Regression", "MediumPriority", "Product Sanity" })
	public void afterClass(){
		
		//disable call forwarding if not
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		softphoneSettings.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.disableCallForwardingSettings(supportDriver);
		
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.OpenDeletedUsersSettingsWithSalesForceId(supportDriver, qa_user2_name, qa_user2_email, qa_salesForce2_id);

		//undeleting user if deleted
		if(usersPage.isUnDeleteBtnVisible(supportDriver)){
			usersPage.clickUnDeleteBtn(supportDriver);
		}
		
		driverUsed.put("supportDriver", false);
	}
		
}
