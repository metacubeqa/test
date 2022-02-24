package support.cases.prerequisite;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.base.Strings;

import softphone.source.SoftPhoneLoginPage;
import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLocalPresenceTab;
import support.source.accounts.AccountOverviewTab;
import support.source.accounts.AccountSalesforceTab;
import support.source.accounts.AccountsEmergencyTab;
import support.source.accounts.AccountsPage;
import support.source.admin.AccountCallRecordingTab;
import support.source.admin.IntelligentDialerTab;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;

public class PreRequisites extends SupportBase{
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	Dashboard supportDashboard = new Dashboard();
	SmartNumbersPage smartNumberPage = new SmartNumbersPage();
	AccountsPage supportAccounts = new AccountsPage();
	AccountOverviewTab supportAccountOverview = new AccountOverviewTab();
	AccountSalesforceTab supportAccountSalesforce = new AccountSalesforceTab();
	AccountIntelligentDialerTab supportAccountInelDialerTab = new AccountIntelligentDialerTab();
	AccountLocalPresenceTab SupportAccountLocalPresenceTab = new AccountLocalPresenceTab();
	AccountCallRecordingTab adminCallRecordingTab = new AccountCallRecordingTab();
	IntelligentDialerTab adminIntelligentDialerTab = new IntelligentDialerTab(); 
	UsersPage supportUsersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();	
	AccountBlockedNumbersTab accountBlockedNumbersTab = new AccountBlockedNumbersTab();
	AccountsEmergencyTab accountEmergencyTab = new AccountsEmergencyTab();
	
	private String supportLoginUserName;
	private String supportLoginPassword;
	private String supportLoginName;
	private String supportLoginEmail;
	private String supportLoginSFId;
	private String adminLoginUserName;
	private String adminLoginPassword;
	private String qaAccount;
	private String qaSalesForceId;
	private String trackingNumber;
	
	@Parameters({"account"})
	@Test(groups={"Sanity", "Regression", "QuickSanity","MediumPriority", "ExludeForProd"})
	public void runPreRequisites(@Optional() String accountName)
	{	
		// Running preflight script
		
		switch (Strings.isNullOrEmpty(accountName) ? "" : accountName) {

		case "QA v2":
			supportLoginUserName 	= CONFIG.getProperty("qa_v2_user_username");
			supportLoginPassword 	= CONFIG.getProperty("qa_v2_user_password");
			supportLoginName 		= CONFIG.getProperty("qa_v2_user_name");
			supportLoginEmail 		= CONFIG.getProperty("qa_v2_user_email");
			supportLoginSFId		= CONFIG.getProperty("qa_v2_user_salesforce_id");
			
			adminLoginUserName 		= CONFIG.getProperty("qa_cai_user_2_username");
			adminLoginPassword 		= CONFIG.getProperty("qa_cai_user_2_password");
			
			qaAccount				= CONFIG.getProperty("qa_user_account_cai_report");
			qaSalesForceId			= CONFIG.getProperty("qa_user_account_qav2_salesforce_id");
			break;
			
		case "metacube pvt":
			supportLoginUserName 	= CONFIG.getProperty("qa_support_user_username");
			supportLoginPassword 	= CONFIG.getProperty("qa_support_user_password");
			supportLoginName 		= CONFIG.getProperty("qa_support_user_name");
			supportLoginEmail 		= CONFIG.getProperty("qa_support_user_email");
			supportLoginSFId		= CONFIG.getProperty("qa_support_user_salesforce_id");
			
			adminLoginUserName 		= CONFIG.getProperty("qa_admin_user_username");
			adminLoginPassword 		= CONFIG.getProperty("qa_admin_user_password");
			
			qaAccount				= CONFIG.getProperty("qa_user_account");
			qaSalesForceId			= CONFIG.getProperty("qa_user_account_salesforrce_id");
			
			trackingNumber			= CONFIG.getProperty("automation_tracking_number2");
			break;
			
		default:
			supportLoginUserName 	= CONFIG.getProperty("qa_user_1_username");
			supportLoginPassword 	= CONFIG.getProperty("qa_user_1_password");
			supportLoginName 		= CONFIG.getProperty("qa_user_1_name");
			supportLoginEmail 		= CONFIG.getProperty("qa_user_1_email");
			supportLoginSFId		= CONFIG.getProperty("qa_user_1_salesforce_id");
			
			adminLoginUserName 		= CONFIG.getProperty("qa_user_2_username");
			adminLoginPassword 		= CONFIG.getProperty("qa_user_2_password");
			
			qaAccount				= CONFIG.getProperty("qa_user_account");
			qaSalesForceId			= CONFIG.getProperty("qa_user_account_salesforrce_id");
			break;
		}
		preFilghtSupportScript();
	}
	
	/**
	 * This method is to set the account's settings to default
	 * 
	 * @throws InterruptedException
	 */
	public void preFilghtSupportScript(){
		String callTimeoutTimeInSeconds = "30";
		String stayConnecttimoutInMinutes = "1";
		String callForwardigtimoutInSeconds = "30";

		System.out.println("Pre filght script is started");
		
		driver1 = getDriver();
		driver2 = getDriver();
		
		// -----------------------------Support User Settings-----------------------------//
		System.out.println("Settting default values with support user");
			
			SFLP.supportLogin(driver1, CONFIG.getProperty("qa_support_tool_site"), supportLoginUserName, supportLoginPassword);
			supportDashboard.clickAccountsLink(driver1, qaAccount, qaSalesForceId);
			System.out.println("Account editor is opened ");
			supportAccountOverview.setDefaultAccountOverviewSettings(driver1);
			supportAccountSalesforce.setDefaultSalesforceSettings(driver1);
			supportAccountInelDialerTab.SetDefaultIntelligentDialerSettings(driver1, callTimeoutTimeInSeconds, stayConnecttimoutInMinutes, callForwardigtimoutInSeconds);
			SupportAccountLocalPresenceTab.setDefaultLocalPresenceSettings(driver1);
			accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
			accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
			accountEmergencyTab.navigateToEmergencyTab(driver1);
			accountEmergencyTab.cleanUpToggleRoutings(driver1);
			accountEmergencyTab.deleteAllRouting(driver1);
			supportDashboard.openManageUsersPage(driver1);
			supportUsersPage.OpenUsersSettingsWithSalesForceId(driver1, supportLoginName, supportLoginEmail, supportLoginSFId);
			userIntelligentDialerTab.openIntelligentDialerTab(driver1);
			userIntelligentDialerTab.enableWebLeadsSetting(driver1);
			userIntelligentDialerTab.saveAcccountSettings(driver1);
			userIntelligentDialerTab.selectNoUnvailableFlow(driver1);
			if (!Strings.isNullOrEmpty(trackingNumber)) {
			supportDashboard.openSmartNumbersTab(driver1);
			smartNumberPage.searchSmartNumber(driver1, trackingNumber);
			smartNumberPage.clickSmartNumber(driver1, trackingNumber);
			if (smartNumberPage.isUndeleteBtnVisible(driver1)) {
				smartNumberPage.clickUndeleteBtn(driver1);
			}
			
			//undeleting user if deleted
			supportDashboard.openManageUsersPage(driver1);
			supportUsersPage.OpenDeletedUsersSettingsWithSalesForceId(driver1, CONFIG.getProperty("qa_admin_user2_name"), CONFIG.getProperty("qa_admin_user2_email"), CONFIG.getProperty("qa_admin_user2_salesforce_id"));

			if(supportUsersPage.isUnDeleteBtnVisible(driver1)){
				supportUsersPage.clickUnDeleteBtn(driver1);
			}
		}

			// -----------------------------Admin User Settings-----------------------------//
			SFLP.supportLogin(driver2, CONFIG.getProperty("qa_support_tool_site"), adminLoginUserName, adminLoginPassword);
			supportDashboard.clickAccountsLink(driver2);
			adminCallRecordingTab.setDefaultCallRecordingSettings(driver2);
			adminIntelligentDialerTab.setDefaultAdminIntelligentDialerSettings(driver2);
			System.out.println("Preflight settings are set to it's default values ");
		}
}