package support.cases.importCases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.callTools.CallToolsPanel;
import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountBlockedNumbersTab.BlockType;
import support.source.commonpages.Dashboard;
import support.source.importPage.ImportPage;
import support.source.system.SystemPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class ImportCases extends SupportBase{

	Dashboard dashboard = new Dashboard();
	ImportPage importPage = new ImportPage();
	SoftPhoneSettingsPage softphoneSettings = new SoftPhoneSettingsPage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	UsersPage usersPage = new UsersPage();
	UserIntelligentDialerTab userIntelligent = new UserIntelligentDialerTab();
	SystemPage systemPage = new SystemPage();
	AccountBlockedNumbersTab blockNumberTab = new AccountBlockedNumbersTab();
	
	private final String outBoundNumber = "+13084084058";
	private final String outBoundNumberCSV = System.getProperty("user.dir")+"\\src\\test\\resources\\csvToImport\\OutBoundNumber.csv";
	private final String importUserCSV = System.getProperty("user.dir")+"\\src\\test\\resources\\csvToImport\\ImportUser.csv";
	private final String importBlockedNumberCSV = System.getProperty("user.dir")+"\\src\\test\\resources\\csvToImport\\ImportBlockedNumber.csv";
	
	@Test(groups = {"Regression", "SupportOnly"})
	public void import_outbound_numbers() {
		System.out.println("Test case --import_outbound_numbers-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Checking in number inspector
		HelperFunctions.closeDesktopWindow();
		dashboard.switchToTab(supportDriver, 2);
		dashboard.navigateToNumberInspectorPage(supportDriver);
		systemPage.searchNumberInspector(supportDriver, outBoundNumber);
		
		if(!systemPage.isUsersLinkEmpty(supportDriver, CONFIG.getProperty("qa_support_user_email"))){
			systemPage.clickUsersLink(supportDriver);
			//deleting outbound number
			userIntelligent.openIntelligentDialerTab(supportDriver);
			userIntelligent.deleteOutBoundNumber(supportDriver, outBoundNumber);
		}
		
		//navigating to outbound numbers tab and importing csv
		dashboard.navigateToOutBoundNumbers(supportDriver);
		importPage.uploadOutBoundNumber(supportDriver, outBoundNumberCSV, outBoundNumber);
		
		// softphone verification
		importPage.switchToTab(supportDriver, 1);
		softphoneSettings.clickSettingIcon(supportDriver);
		softphoneSettings.clickOutboundNumbersTab(supportDriver);
		int numberExists = softphoneSettings.isNumberExistInOutboundNumber(supportDriver, outBoundNumber);
		assertTrue(numberExists >= 0, String.format("Out Bound number: %s does not exists", outBoundNumber));
		
		//deleting outbound number
		softphoneSettings.deleteOutboundNumberIfExist(supportDriver, outBoundNumber);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --import_outbound_numbers-- passed ");
	}
	
	@Test(groups = {"Regression", "SupportOnly"})
	public void import_users() {
		System.out.println("Test case --import_users-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//navigating to manage users
		String qa_user_name = "lokesh gupta1";
		String qa_user_email = "lokesh.gupta1@metacube.com";
		String qa_salesForce_id = "005j000000EhZNFAA3";
		
		//Checking user is not active
		HelperFunctions.closeDesktopWindow();
		dashboard.switchToTab(supportDriver, 2);
		dashboard.openManageUsersPage(supportDriver);
		usersPage.searchUserWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
	
		if (usersPage.isUserActive(supportDriver, qa_salesForce_id)) {
			usersPage.selectActiveUser(supportDriver, qa_salesForce_id);
			usersPage.deleteUser(supportDriver, qa_user_name);
		}
		
		//navigating to import users section and importing csv
		dashboard.navigateToImportUsers(supportDriver);
		importPage.uploadUser(supportDriver, importUserCSV, qa_salesForce_id);

		//checking and deleting active user
		dashboard.openManageUsersPage(supportDriver);
		usersPage.searchUserWithSalesForceId(supportDriver, qa_user_name, qa_user_email, qa_salesForce_id);
		assertTrue(usersPage.isUserActive(supportDriver, qa_salesForce_id));
		usersPage.selectActiveUser(supportDriver, qa_salesForce_id);
		usersPage.deleteUser(supportDriver, qa_user_name);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --import_users-- passed ");
	}

	@Test(groups = {"Regression", "SupportOnly"})
	public void import_blocked_numbers() {
		System.out.println("Test case --import_blocked_numbers-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		HelperFunctions.closeDesktopWindow();
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		String blockedNumber = CONFIG.getProperty("qa_agent_user_number");
		String blockedNumberInSimpleFormat = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_agent_user_number"));
		String numberToDial = CONFIG.getProperty("qa_support_user_number");
		
		// navigating to import blocked numbers section and importing csv
		dashboard.navigateToImportBlockedNumbers(supportDriver);
		importPage.uploadBlockedNumber(supportDriver, importBlockedNumberCSV, blockedNumberInSimpleFormat);
		
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, blockedNumber));
		
		// Searching Type of Recording
		blockNumberTab.searchBlockedNumberByType(supportDriver, BlockType.Recording);

		// Verifying only recording number visible
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, blockedNumber));
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to admin/support
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(agentDriver);

		// verifying on support that no accept button is visible
		blockNumberTab.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));

		// verifying on agent that call is disconnected
		blockNumberTab.switchToTab(agentDriver, 1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(agentDriver));

		// deleting blocked number
		blockNumberTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);		
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --import_blocked_numbers-- passed ");
	}
	
	@AfterClass(groups = {"Regression", "SupportOnly"})
	public void afterClass(){
		
		// deleting blocked number
		HelperFunctions.closeDesktopWindow();
		initializeSupport();
		driverUsed.put("supportDriver", true);
	
		blockNumberTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		driverUsed.put("supportDriver", false);
	
	}
}
