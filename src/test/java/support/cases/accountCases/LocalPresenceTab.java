package support.cases.accountCases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.Test;

import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneSettingsPage;
import support.base.SupportBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountLocalPresenceTab;
import support.source.accounts.AccountOverviewTab;
import support.source.admin.AdminCallTracking;
import support.source.commonpages.AddSmartNumberPage;
import support.source.commonpages.AddSmartNumberPage.SearchType;
import support.source.commonpages.AddSmartNumberPage.Type;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import support.source.users.UserIntelligentDialerTab;
import support.source.users.UsersPage;
import utility.HelperFunctions;

public class LocalPresenceTab extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	AccountLocalPresenceTab localPresenceTab = new AccountLocalPresenceTab();
	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	AddSmartNumberPage addSmartNoPage = new AddSmartNumberPage();
	AccountIntelligentDialerTab intelligentTab = new AccountIntelligentDialerTab();
	AccountOverviewTab overViewTab = new AccountOverviewTab();
	AdminCallTracking adminCallTracking = new AdminCallTracking();
	UsersPage usersPage = new UsersPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	SoftPhoneCalling softPhoneCallingPage = new SoftPhoneCalling();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void manually_add_local_presence_from_existing_number() {
		System.out.println("Test case --manually_add_local_presence_from_existing_number-- started ");

		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//adding Offline tracking number
		dashboard.openOfflineCallTrackingPage(supportDriver);
		String offlineTrackName = new StringBuilder("Offline").append(HelperFunctions.GetRandomString(4)).toString();
		HashMap<AdminCallTracking.trackingNoFields, String> offlineTrackingNumberData = new HashMap<AdminCallTracking.trackingNoFields, String>();
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CustomParameterValue, offlineTrackName);
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.Source, AdminCallTracking.offlineTrackSourceTypes.TV.toString());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SmartNumberType,
				AdminCallTracking.newAdvTrackSmartNoTypes.TollFree.displayName());
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.CallFlow, CONFIG.getProperty("qa_call_flow_new_org"));
		offlineTrackingNumberData.put(AdminCallTracking.trackingNoFields.SfdcCampaign,  CONFIG.getProperty("qa_campaign_for_call_tracking"));
		
		//Verifying offline call track smart number present
		adminCallTracking.clickAddNewOfflineTrackNobtn(supportDriver);
		adminCallTracking.enterNewAdvTrackingNumberData(supportDriver, offlineTrackingNumberData);
		adminCallTracking.clickFinishButtonCustom(supportDriver);
		adminCallTracking.searchOfflineCallTrackNumber(supportDriver, offlineTrackName, AdminCallTracking.offlineTrackSourceTypes.TV.toString(), CONFIG.getProperty("qa_call_flow_new_org"));
		int offlineCallSmartNumberIndex = adminCallTracking.getCustomCallSmartNumberIndex(supportDriver, offlineTrackName, CONFIG.getProperty("qa_call_flow_new_org"));
		String trackingNumber = adminCallTracking.getAdvTrackNumberFromTable(supportDriver, offlineCallSmartNumberIndex);
		assertTrue(offlineCallSmartNumberIndex >= 0, "smart number is not present");
		
		String label = "AutoLPLabel".concat(HelperFunctions.GetRandomString(2));

		dashboard.clickAccountsLink(supportDriver);
		localPresenceTab.openlocalPresenceTab(supportDriver);

		localPresenceTab.clickManuallyAddNumber(supportDriver);
		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.Tracking);
		addSmartNoPage.searchSmartNo(supportDriver, trackingNumber);
		addSmartNoPage.clickOnReAssignForNumber(supportDriver, trackingNumber);
		addSmartNoPage.clickNextButton(supportDriver);
		
		// Entering label and saving the number
		System.out.println("Saving the number");
		addSmartNoPage.enterLabel(supportDriver, label, trackingNumber);
		addSmartNoPage.saveSmartNo(supportDriver);

		// verifying local presence type under user page
		dashboard.clickOnUserProfile(supportDriver);
		userIntelligentDialerTab.openIntelligentDialerTab(supportDriver);

		userIntelligentDialerTab.clickSmartNoIcon(supportDriver);
		addSmartNoPage.selectSmartNoSearchType(supportDriver, SearchType.EXISTING);
		addSmartNoPage.clickNextButton(supportDriver);
		addSmartNoPage.idleWait(2);

		addSmartNoPage.selectExistingTypeOfNumber(supportDriver, Type.LocalPresence);
		addSmartNoPage.clickOnSearchButton(supportDriver);
		assertTrue(addSmartNoPage.verifyNumberTypeListContainsText(supportDriver, Type.LocalPresence.name()));
		addSmartNoPage.closeSmartNumberWindow(supportDriver);
		
		//Verifying type under local presence page
		dashboard.clickAccountsLink(supportDriver);
		localPresenceTab.openlocalPresenceTab(supportDriver);
		localPresenceTab.clickManuallyAddNumber(supportDriver);
		String type = addSmartNoPage.getNumberType(supportDriver, trackingNumber);
		assertTrue(addSmartNoPage.isReAssignBtnDisabled(supportDriver, trackingNumber));
		assertEquals(type, "LocalPresence", "Type does not match");
		addSmartNoPage.closeSmartNumberWindow(supportDriver);

		//deleting number
		dashboard.isPaceBarInvisible(supportDriver);
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, trackingNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, trackingNumber);
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(supportDriver);
		
		// updating driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --manually_add_local_presence_from_existing_number-- passed ");
	}

	
	
	@Test(groups= {"Regression", "SupportOnly"})
	public void provision_international_local_presence_number() {
		
		System.out.println("Test case --provision_international_local_presence_number-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Provisioning international number
		dashboard.clickAccountsLink(supportDriver, CONFIG.getProperty("qa_free_user_account"), CONFIG.getProperty("qa_free_user_account_salesForce_id"));
		localPresenceTab.openlocalPresenceTab(supportDriver);
		String number = localPresenceTab.provisionInternationalNumber(supportDriver, "Canada", "542", 0);
		
		//verifying in smart numbers tab
		dashboard.openSmartNumbersTab(supportDriver);
		smartNumbersPage.searchSmartNumber(supportDriver, number);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(supportDriver, number);
		assertTrue(smartNoIndex >= 0);

		// Deleting smart Number
		smartNumbersPage.clickSmartNoByIndex(supportDriver, smartNoIndex);
		smartNumbersPage.deleteSmartNumber(supportDriver);

		// updating driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --provision_international_local_presence_number-- passed ");
	}

	//Local Presence Feature- ON
	//Local Presence Feature- OFF
	@Test(groups= {"Regression", "SupportOnly"})
	public void local_presence_feature_on_off() {
		System.out.println("Test case --local_presence_feature_on_off-- started ");
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open account and local presence tab
		dashboard.clickAccountsLink(supportDriver);
		localPresenceTab.openlocalPresenceTab(supportDriver);
	
		//Enabling local presence setting
		localPresenceTab.enableLocalPresenceSetting(supportDriver);
		localPresenceTab.saveLocalPresenceSettings(supportDriver);
		dashboard.switchToTab(supportDriver, 1);
		
		//verifying that local presence setting visible on settings page
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
	    softPhoneSettingsPage.clickSettingIcon(supportDriver);
	    assertTrue(softPhoneSettingsPage.verifyLocalPresenctOptionIsPresent(supportDriver));
	    dashboard.switchToTab(supportDriver, 2);
	    
	    //Disable local presence setting
		localPresenceTab.disableLocalPresenceSetting(supportDriver);
		localPresenceTab.saveLocalPresenceSettings(supportDriver);
		dashboard.switchToTab(supportDriver, 1);
	  		
		// verifying that local presence setting NOT visible on settings page
		softPhoneSettingsPage.reloadSoftphone(supportDriver);
		softPhoneSettingsPage.clickSettingIcon(supportDriver);
		softPhoneSettingsPage.verifyLocalPresenctOptionNotPresent(supportDriver);
		dashboard.switchToTab(supportDriver, 2);
		
		//Enabling local presence setting
		localPresenceTab.enableLocalPresenceSetting(supportDriver);
		localPresenceTab.saveLocalPresenceSettings(supportDriver);
		
		// updating driver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --local_presence_feature_on_off-- passed ");
	}
	
	
	//@AfterMethod(groups = { "Regression", "SupportOnly" })
	public void deleteLPNumbers(ITestResult result) {

		if (result.getMethod().getMethodName().equals("provision_local_presence_number")) {
			if (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) {

				System.out.println("Running clean up code for failed long presence case");
				// navigating to free user account
				dashboard.switchToTab(supportDriver, 2);
				dashboard.clickAccountsLink(supportDriver,
						CONFIG.getProperty("qa_free_user_account"),
						CONFIG.getProperty("qa_free_user_account_salesForce_id"));
				localPresenceTab.openlocalPresenceTab(supportDriver);

				// deleting LP numbers
				localPresenceTab.clickDeleteNumbersBtn(supportDriver);
			}
		}
	}
}
