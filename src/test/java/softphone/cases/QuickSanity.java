/**
 * 
 */
package softphone.cases;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;

/**
 * @author Abhishek
 *
 */
public class QuickSanity extends SoftphoneBase{

	  //in this test case we are verifying add/remove groups with outbound conference calling  
	  @Test(groups={"QuickSanity", "Sanity"})
	  public void navigate_to_softphone_tabs()
	  {
	    System.out.println("Test case --navigate_to_softphone_tabs()-- started ");
			
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    
	    //navigate To Messaging Tabs
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.navigateToAllMessages(driver1);
	    softPhoneMessagePage.navigateToReadMessages(driver1);
	    softPhoneMessagePage.navigateToUnreadMessages(driver1);
	    
	    //Navigate to WebLeads Page
	    softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
	    
	    //Navigate to Setting Page tabs
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.navigateToVoicemailDropTab(driver1);
	    softPhoneSettingsPage.navigateToCustomGreetingTab(driver1);
	    softPhoneSettingsPage.clickAddNewCustomGreetingButton(driver1);
	    softPhoneSettingsPage.closeCustomGreetingDialogueBox(driver1);
	    seleniumBase.idleWait(2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}

	//@Test(groups = { "Sanity", "ExludeForProd"}, priority=4)
	public void verify_lightning_email_template() {
		System.out.println("Test case --verify_lightning_email_template-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Opening the support tool
		loginSupport(driver1);

		// open Support Page and enable send email through salesfore setting
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableSendEmailUsingSalesforce(driver1);
		accountIntelligentDialerTab.enableLightningEmailTemplates(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		dashboard.openManageUsersPage(driver1);
		usersPage.OpenUsersSettings(driver1, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("qa_user_1_email"));
		userIntelligentDialerTab.isOverviewTabHeadingPresent(driver1);
		userIntelligentDialerTab.openIntelligentDialerTab(driver1);
		userIntelligentDialerTab.enableSendEmailUsingSalesforce(driver1);
		userIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		// Making first outbound call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_2_number"));

		// picking up call
		System.out.println("picking up second incoming call");
		softPhoneCalling.pickupIncomingCall(driver4);

		// open email template
		callScreenPage.clickEmailButton(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));

		// Verifying email page has opened
		sfLightningEmailTemplate.verifyPageHeading(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
}