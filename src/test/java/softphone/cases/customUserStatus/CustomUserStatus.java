package softphone.cases.customUserStatus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.CustomUserStatusPage.Duration;
import softphone.source.SoftPhoneContactsPage;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountIntelligentDialerTab.CustomStatusFields;
import support.source.accounts.AccountIntelligentDialerTab.CustomStatusTime;
import utility.HelperFunctions;

public class CustomUserStatus extends SoftphoneBase {
	
	String customUserStatusDescription = "Allow users the ability to define free and busy statuses. Status values are reportable within the user presence report to understand how your agents are spending their day.";
	
	@BeforeMethod(groups={"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void removeAllCustomUserStatus() {
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);  
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);  
	    
	    //verify that if support tab is not there then open it
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		if (seleniumBase.getTabCount(driver1) < 2) {

			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
		}else if(!accountIntelligentDialerTab.isIntelligentDialerTabLinkVisible(driver1)) {
			resetApplication();
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
		}
		
		//remove all Users Custom Status for all days
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCustomUserStatusSetting(driver1);
		accountIntelligentDialerTab.disableAllowUsersOverrideTimeSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//verify that if support tab is not there then open it
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4) );
		if (seleniumBase.getTabCount(driver4) < 2) {

			loginSupport(driver4);
			dashboard.clickAccountsLink(driver4);
		}else if(!accountIntelligentDialerTab.isIntelligentDialerTabLinkVisible(driver4)) {
			resetApplication();
			loginSupport(driver4);
			dashboard.clickAccountsLink(driver4);
		}
		
		//open intelligent dialer tab
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.deleteAllCustomUserStatus(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
	}
	
	@Test(groups = { "Regression" })
	public void enable_custom_user_status_setting() {
		System.out.println("Test case --enable_custom_user_status_setting-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);

		// enable custom user status
		accountIntelligentDialerTab.enableCustomUserStatusSetting(driver1);
		
		//verify that custom user status description is there
		assertEquals(accountIntelligentDialerTab.getCustomUserStatusDescription(driver1), customUserStatusDescription);
		
		//verify that Allow User Override Time Setting is off
		assertFalse(accountIntelligentDialerTab.getAllowUsersOverrideTimeSettingStatus(driver1));
		
		//Verify that by default Available and Busy status is there
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver1, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver1, "Busy") == null);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver1, "Available"));
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver1, "Busy"));
		
		//verify add new customs status button visible
		assertTrue(accountIntelligentDialerTab.isAddNewCustomStatusBtnVisible(driver1));
		
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//verify that custom user status description is there
		assertEquals(accountIntelligentDialerTab.getCustomUserStatusDescription(driver1), customUserStatusDescription);
		
		//verify that Allow User Override Time Setting is off
		assertFalse(accountIntelligentDialerTab.getAllowUsersOverrideTimeSettingStatus(driver1));
		
		//Verify that by default Available and Busy status is there
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver1, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver1, "Busy") == null);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver1, "Available"));
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver1, "Busy"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Add New Custom status from admin to all Team for default Timing and busy type, set status on dialer
	@Test(groups = { "Regression" })
	public void add_update_delete_custom_user_status() {
		System.out.println("Test case --enable_custom_user_status_setting-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);

		//Setting the default values for add custom status window
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//click on add new custom status button and that the default values on the dialogue box
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		assertEquals(accountIntelligentDialerTab.getCustomSatusBusyStatus(driver4), "Yes", "default value for Busy status is not Yes");
		assertEquals(accountIntelligentDialerTab.getCustomSatusDefaultTime(driver4), "15 Minutes (Default)", "default time is not 15 Minutes (Default)");
		assertEquals(accountIntelligentDialerTab.getSelectedCustomStatusTeam(driver4), "All (Default)", "defaul team is not All (Default)");
		
		//Enter the values for the new custom status and save it
		String customStatusName = "Lunch Time";
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.enterCustomSatusDesc(driver4, customUserStatusDetail.get(CustomStatusFields.Description));
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver4, customStatusName));
		assertFalse(accountIntelligentDialerTab.isCustomStausUpdateButtonVisible(driver4, customStatusName));
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver4, customStatusName));
		assertFalse(accountIntelligentDialerTab.isCustomStausUpdateButtonVisible(driver4, customStatusName));
		
		//switch to softphone and reload the softphone
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Select custom status which is added above
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//set and verify the the message that appears on hovering mouse pointer on users image
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		seleniumBase.idleWait(5);
		
		//get
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, callHistoryCounts);
		
		//Switch to web app and click on update button of the custom status added above
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		accountIntelligentDialerTab.clickCustomStausUpdateButton(driver4, customStatusName);
		
		//Set the values of custom status to be updated and update the custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> updatedCustomUserStatusDetail = new HashMap<>();
		customStatusName = "Meta Lunch Time";
		updatedCustomUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		updatedCustomUserStatusDetail.put(CustomStatusFields.Busy, "No");
		updatedCustomUserStatusDetail.put(CustomStatusFields.Description, "Meta Lunch Time in mid day");
		updatedCustomUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, updatedCustomUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify the updated details of Custom status
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, updatedCustomUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, updatedCustomUserStatusDetail);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver4, customStatusName));
		assertFalse(accountIntelligentDialerTab.isCustomStausUpdateButtonVisible(driver4, customStatusName));
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, updatedCustomUserStatusDetail);
		
		//verify that delete button is not appearing for Available and Busy status
		assertFalse(accountIntelligentDialerTab.isCustomStausDeleteButtonVisible(driver4, customStatusName));
		assertFalse(accountIntelligentDialerTab.isCustomStausUpdateButtonVisible(driver4, customStatusName));
		
		//switch to softphone and reload the softphone
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//verify the updated custom status on softphone
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, updatedCustomUserStatusDetail);
		customUserStatusPage.clickCustomStatusCancelBtn(driver4);
		
		//switch to web app and delete the custom status
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		accountIntelligentDialerTab.deleteCustomStatus(driver4, customStatusName);	
		assertTrue(accountIntelligentDialerTab.getCustomStatusRow(driver4, customStatusName) == null);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		assertTrue(accountIntelligentDialerTab.getCustomStatusRow(driver4, customStatusName) == null);
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_2_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		assertTrue(accountIntelligentDialerTab.getCustomStatusRow(driver4, customStatusName) == null);
		
		//switch to softphone and reload the softphone
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//verify that deleted custom status is no more appearing on the softphone
		callScreenPage.clickUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusRow(driver4, customStatusName), null);
		customUserStatusPage.clickCustomStatusCancelBtn(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify  status expiration message should not visible if user set status available before time
	@Test(groups = { "Regression" })
	public void add_custom_user_status_override_time() {
		System.out.println("Test case --add_custom_user_status_override_time-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);

	    // enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the default values for add custom status window
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		String customStatusName = "Lunch Time";
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
				
		//Enter the values for the new custom status and save it
	
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.enterCustomSatusDesc(driver4, customUserStatusDetail.get(CustomStatusFields.Description));
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Select custom status which is added above
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		assertEquals(customUserStatusPage.getSelectedDuration(driver4), "15 minutes");
		customUserStatusPage.verifyAllDurationsInDropDown(driver4);
		String message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.five);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);

		//verify that user image is busy and tooltip
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		// dialing and picking up call to agent
		reloadSoftphone(driver4);
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		seleniumBase.idleWait(5);
		
		//get the call history counts
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//verify that call is not appearing 
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//Hang up the call from caller if its still active. Verify that call history count has been increased
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, callHistoryCounts);
		
		//Again set the status with duration as one minutes
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.one);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify that user image is busy and tooltip
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//set the values for the default Available status
		customStatusName = "Available";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> availableStatusDetail = new HashMap<>();
		availableStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		
		//Select the default Available status
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, availableStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Hover on the user image and verify that tooltip is invisible
		callScreenPage.verifyUserImageAvailable(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		customUserStatusPage.verifyCustomStatusToolTipInvisible(driver4);
		
		//Verify that after one minute the expiration message is not appearing.
		seleniumBase.idleWait(70);
		assertFalse(customUserStatusPage.isCustomStatusExpireMsg(driver4, customStatusName));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void update_custom_user_status_busy() {
		System.out.println("Test case --update_custom_user_status_busy-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	
		// enable custom user status setting
		String customStatusName = "Lunch Time";
		
		//Setting the values for custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusCancelBtn(driver4);
		
		//Switch to web app and click on edit button for the custom status added above
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		accountIntelligentDialerTab.clickCustomStausUpdateButton(driver4, customStatusName);
	
		//set the updated value of the custom status
		customUserStatusDetail.remove(CustomStatusFields.Busy);
		customUserStatusDetail.put(CustomStatusFields.Busy, "No");
		
		//update the custom stats
		accountIntelligentDialerTab.selectCustomSatusBusyStatus(driver4, "No");
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify the updated custom status
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//verify the updated custom status on softphone
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Select the updated custom status
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//verify the tooltip on hovering user image
		callScreenPage.hoverOnUserImage(driver4);
		callScreenPage.verifyUserImageAvailable(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void custom_user_status_disable_default_status() {
		System.out.println("Test case --custom_user_status_disable_default_status-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1) );
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCustomUserStatusSetting(driver1);
		accountIntelligentDialerTab.disableCustomUserStatusSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
	    //switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Set the user image busy by clicking on user image and verify user image is busy
		callScreenPage.setUserImageBusy(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		
		//set the user image avaialbe and verify user image is avaialble
		callScreenPage.setUserImageAvailable(driver4);
		callScreenPage.verifyUserImageAvailable(driver4);

		//Verify that on clicking user image modal box for custom status is not visible
		assertFalse(customUserStatusPage.isCustomStatusModalBoxVisible(driver4));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify caller routes to voicemail when user have set default Busy status
	//User status should be set back to Busy custom status after ending the call
	@Test(groups = { "Regression" })
	public void custom_user_status_on_default_busy_status() {
		System.out.println("Test case --custom_user_status_on_default_busy_status-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Set the values of the default custom status busy
		String customStatusName = "Busy";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Default busy status");
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
	
		//Select custom status which is added above
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Hover mouse pointer on the user image and verify that tool tip is invisible
		callScreenPage.hoverOnUserImage(driver4);
		customUserStatusPage.verifyCustomStatusToolTipInvisible(driver4);
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		seleniumBase.idleWait(5);
		
		//get the missed call count
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//verify that call is not appearing
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//hang up if call is still on and verify missed call count increaes
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, callHistoryCounts);
		
		//Verify that the user image remains busy during and after the outbound call ends
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
			
		//verify that call appearing
		seleniumBase.idleWait(5);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver2));
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hang up the call
		softPhoneCalling.hangupActiveCall(driver4);
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	
	@Test(groups = { "Regression" })
	public void custom_user_status_on_default_avialable_status() {
		System.out.println("Test case --custom_user_status_on_default_avialable_status-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Set the values of the default custom status busy
		String customStatusName = "Available";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "No");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Default available status");
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
	
		//Select custom status which is added above
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Hover mouse pointer on the user image and verify that tool tip is invisible
		callScreenPage.hoverOnUserImage(driver4);
		customUserStatusPage.verifyCustomStatusToolTipInvisible(driver4);
		
		//verify that user image is busy
		callScreenPage.verifyUserImageAvailable(driver4);
		
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		
		//get the missed call count
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hang up if call is still on and verify missed call count increaes
		softPhoneCalling.hangupActiveCall(driver4);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");

	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void custom_user_status_busy_hold_resume() {
		System.out.println("Test case --custom_user_status_busy_hold_resume-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "New Automation Custom Status";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		String message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.Fifteen);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);

		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//verify setting icon not visible
		softPhoneSettingsPage.settingIconNotVisible(driver4);
		
		//verify user image is busy and no tool tip is there
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		// Putting first call on hold
		softPhoneCalling.clickHoldButton(driver4);
		softPhoneCalling.isHangUpButtonVisible(driver2);

		//verify user image is busy and tool tip is there
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		assertTrue(softPhoneSettingsPage.isSettingsIconVisible(driver4));
		
		//resume the call
		softPhoneCalling.clickOnHoldButton(driver4);
	    softPhoneCalling.clickResumeButton(driver4);
	    seleniumBase.idleWait(2);
	    softPhoneCalling.isHangUpButtonVisible(driver2);
		
		//verify user image is busy and no tool tip is there
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		softPhoneSettingsPage.settingIconNotVisible(driver4);
	    
		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver4);

		//verify user image is busy and no tool tip is there
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//click setting icon to confirm that it is visible
		assertTrue(softPhoneSettingsPage.isSettingsIconVisible(driver4));
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify agent got notification message after expiration of custom user status
	//Verify user accept incoming call after set user status available from Busy status before status expired
	//Custom user status when busy should be showing on call dashboard
	@Test(groups = { "Regression" })
	public void custom_user_status_change_avaiable() {
		System.out.println("Test case --custom_user_status_change_avaiable-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "Metacube Lunch Time";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Description, "Meta Lunch time in mid day");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above and set the duration to one minute
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName,  customUserStatusDetail);
		String message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.one);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//Verify that after custom status expires the message appear for it
		seleniumBase.idleWait(60);
		assertTrue(customUserStatusPage.isCustomStatusExpireMsg(driver4, customStatusName));
		
		//Close Custom status and verify user image is still busy
		customUserStatusPage.closeWarningBar(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//verify on Call Dashboard that user is in busy section
		seleniumBase.openNewBlankTab(driver1);
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		driver1.get(CONFIG.getProperty("call_dashboard_url"));
		assertTrue(callDashboardReactPage.getBusyAgentsList(driver1).contains(CONFIG.getProperty("qa_user_2_name")));
		
		//Now select the default custom status available and set the duration as one minute
		String avCustomStatusName = "Available";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> avCustomUserStatusDetail = new HashMap<>();
		avCustomUserStatusDetail.put(CustomStatusFields.StatusName, avCustomStatusName);
		avCustomUserStatusDetail.put(CustomStatusFields.Busy, "No");
		avCustomUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.displayName());
		avCustomUserStatusDetail.put(CustomStatusFields.Description, "Default available status");
		
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, avCustomStatusName, avCustomUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//hover on user image and verify that tooltip is not visible
		callScreenPage.verifyUserImageAvailable(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		customUserStatusPage.verifyCustomStatusToolTipInvisible(driver4);
		
		//verify on Call Dashboard that user is in Available section
		assertTrue(callDashboardReactPage.getAvailableAgentsList(driver1).contains(CONFIG.getProperty("qa_user_2_name")));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		// Verify that user is able to make call after status is expired
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver2);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify User image should not set to available if user dial when no expiration custom status set to user
	//Verify admin able to create custom status with duration as  'no expiration'
	@Test(groups = { "Regression" })
	public void custom_user_status_no_expiration() {
		System.out.println("Test case --custom_user_status_no_expiration-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "Not Expire";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.toString());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		
		//verify that on custom status modal box, no expiration is selected in duration dropdown. Save the custom status setting
		assertEquals(customUserStatusPage.getSelectedDuration(driver4), "no expiration");
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Hover on user image and verify that only status name is there as tool tip
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + " since " + dateTimeFormatter.format(dateTime));
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		seleniumBase.idleWait(5);
		
		//get the call history counts
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//verify that call is not appearing 
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//Hang up the call from caller if its still active. Verify that call history count has been increased
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, callHistoryCounts);
		
		//Make an outbound call and verify that custom status has not been removed
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
		seleniumBase.idleWait(5);
		
		//verify that call is not appearing 
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//Hang up the call from caller if its still active. Verify that call history count has been increased
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupActiveCall(driver4);
		
		//Hover on user image and verify that only status name is there as tool tip
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + " since " + dateTimeFormatter.format(dateTime));
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify admin able to pick queue call during custom busy status
	@Test(groups = { "Regression" })
	public void custom_user_status_busy_queue() {
		System.out.println("Test case --custom_user_status_busy_queue-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "New Automation Custom Status";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		String message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.Fifteen);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);

		//Subscribe a queue
		softPhoneCallQueues.subscribeQueue(driver4, CONFIG.getProperty("qa_group_1_name"));
		
		// Making a call to the queue
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_group_1_number"));

		// pick call from the queue
		softPhoneCallQueues.pickCallFromQueue(driver4);
		
		//verify user image is busy and verify the tool tip
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		// hangup from caller
		softPhoneCalling.hangupActiveCall(driver4);

		//verify user image is busy and tool tip is there
		callScreenPage.verifyUserImageBusy(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify team specific custom status should not be visible to agents who are not member of that team
	@Test(groups = { "Regression" })
	public void verify_custom_user_selected_team() {
		System.out.println("Test case --verify_custom_user_selected_team-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String teamName = CONFIG.getProperty("qa_group_3_name");

	    // enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status and add it for a particular team
		String customStatusName = "Lunch";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, teamName);
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Click on the team name so it opens in new tab and verify custom status is there
		accountIntelligentDialerTab.clickCustomStatusTeamName(driver4, customStatusName);
		groupsPage.verifyGroupDetails(driver4, teamName, "Don't delete it");
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//add a supervisor for the team
		groupsPage.addSupervisor(driver4, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		
		//Verify on team page that default Available and Busy status is present but the newly added custom status is not there
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Available") == null);
		assertFalse(accountIntelligentDialerTab.getCustomStatusRow(driver4, "Busy") == null);
		assertTrue(accountIntelligentDialerTab.getCustomStatusRow(driver4, customStatusName) == null);
		
		//switch to softphone and reload it
		seleniumBase.switchToTab(driver4, 1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver4);
		reloadSoftphone(driver1);
		reloadSoftphone(driver3);
		
		//Verify that user status is visible for the member who is part of the team
		callScreenPage.clickUserImage(driver1);
		assertTrue(customUserStatusPage.getCustomStatusRow(driver1, customStatusName) != null);
		customUserStatusPage.selectCustomStatus(driver1, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver1);
		
		//verify custom status added above is visible for the Supervisor
		callScreenPage.clickUserImage(driver4);
		assertTrue(customUserStatusPage.getCustomStatusRow(driver4, customStatusName) != null);
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify custom status added above is not visible for the user who is not the part of the team
		callScreenPage.clickUserImage(driver3);
		assertTrue(customUserStatusPage.getCustomStatusRow(driver3, customStatusName) == null);
		customUserStatusPage.clickCustomStatusCancelBtn(driver3);
		
		//Verify on team page that default Available and Busy status and the newly added custom status is there
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
		groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.deleteSuperviosr(driver4, CONFIG.getProperty("qa_user_2_name"));
		groupsPage.saveGroup(driver4);
		
		//Switch to softphone
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify delete the Team which is associated with custom status
	@Test(groups = { "Regression" })
	public void verify_custom_user_deleted_team() {
		System.out.println("Test case --verify_custom_user_deleted_team-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //Restore the group if it is deleted
		String teamName = "CustomStatustTeam";
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.restoreGroup(driver4);
		
		// enable custom user status setting
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status and add it for the team which will be deleted
		String customStatusName = "Lunch";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, teamName);
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//delete the user group for which custom status is added
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.deleteGroup(driver4);
		groupsPage.openAddNewGroupPage(driver4);
		
		//Open the Intelligent Dialer page
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		
		//since the team is deleted update the custom status detail with all team
		customUserStatusDetail.remove(CustomStatusFields.Team);
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Verify for teams All should appear for the custom status
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Click on update button for the custom statut update above
		accountIntelligentDialerTab.clickCustomStausUpdateButton(driver4, customStatusName);
		
		//verify that the team is changed to All
		assertTrue(accountIntelligentDialerTab.getSelectedCustomStatusTeam(driver4).startsWith("All"));
		accountIntelligentDialerTab.closeComplainceHourWindow(driver4);
		
		//verify custom status added above is visible for the user
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		callScreenPage.clickUserImage(driver4);
		assertTrue(customUserStatusPage.getCustomStatusRow(driver4, customStatusName) != null);
		customUserStatusPage.clickCustomStatusCancelBtn(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//User status on "Status" dropdown on Team tab should be showing, when Team is not All from Admin console
	//Wrong filtration should not be showing when Status dropdown values is selected then wrong result is showing
	//Team members set custom user status on dialer- supervisor can filter members in Team as per custom status
	//Filter members in Team with combine custom status and team filter
	@Test(groups = { "Regression" })
	public void custom_user_status_team_section() {
		System.out.println("Test case --custom_user_status_team_section-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    WebDriver agentDriver = getDriver();
	    SFLP.softphoneLogin(agentDriver,CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_4_username"), CONFIG.getProperty("qa_user_4_password"));
	
		// enable custom user status setting
		String customStatusName = "Lunch Time";
		
		//Setting the values for custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Select the updated custom status
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//verify the tooltip on hovering user image
		callScreenPage.hoverOnUserImage(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(agentDriver);
		customUserStatusPage.clickSelectCustomStatusBtn(agentDriver, "Busy");
		customUserStatusPage.clickCustomStatusSaveBtn(agentDriver);
		
		//Opening team section page
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softPhoneTeamPage.openTeamSection(driver1);
		softPhoneTeamPage.setCustomStatus(driver1, customStatusName);
		
		//Verifying that status of the 2 member is visible as offline now
		String agentName = CONFIG.getProperty("qa_user_2_name");
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), customStatusName);
		assertEquals(softPhoneTeamPage.getAgents(driver1).size(), 1);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), true);
		softPhoneTeamPage.clickMonitor(driver1, agentName);
		
		//Select the updated custom status
		callScreenPage.clickUserImage(agentDriver);
		customUserStatusPage.selectCustomStatus(agentDriver, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(agentDriver, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(agentDriver);
		
		//Opening team section page
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneTeamPage.openTeamSection(driver1);
		softPhoneTeamPage.setCustomStatus(driver1, customStatusName);
		seleniumBase.idleWait(2);
		
		//Verifying that status of the 2 member is visible as offline now
		String agentName2 = CONFIG.getProperty("qa_user_4_name");
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), customStatusName);
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName2), customStatusName);
		assertEquals(softPhoneTeamPage.getAgents(driver1).size(), 2);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), true);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName2), true);
		
		//Now filter base on teams
		softPhoneTeamPage.setTeam(driver1, CONFIG.getProperty("qa_group_2_name"));
		
		//verify that only one agent belong to the filtered team should appear
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName2), customStatusName);
		assertEquals(softPhoneTeamPage.getAgents(driver1).size(), 1);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName2), true);
		
		agentDriver.quit();
		agentDriver = null;
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	//Verify incoming call on softphone in between 'Select a status' window displaying on softphone
	@Test(groups = { "Regression" })
	public void incoming_call_while_status_update() {
		System.out.println("Test case --incoming_call_while_status_update-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
		// enable custom user status setting
		String customStatusName = "Lunch Time";
		
		//Setting the values for custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		
		//take incoming call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		seleniumBase.idleWait(3);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//verify that custom user status window is still open
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusCancelBtn(driver4);
		
		//accept the call
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver4);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	//Verify user can change the custom status during call
	@Test(groups = { "Regression" })
	public void update_status_during_call() {
		System.out.println("Test case --incoming_call_while_status_update-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
		// enable custom user status setting
		String customStatusName = "Lunch Time";
		
		//Setting the values for custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//take incoming call
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
		softPhoneCalling.pickupIncomingCall(driver4);

		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);

		//Select the updated custom status
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//verify the tooltip on hovering user image
		callScreenPage.hoverOnUserImage(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//accept the call
		softPhoneCalling.hangupActiveCall(driver4);
		softPhoneCalling.isCallBackButtonVisible(driver4);
		
		//verify the tooltip on hovering user image
		callScreenPage.hoverOnUserImage(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
	
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	//User status should set back to Free custom status after ending the call
	@Test(groups = { "Regression" })
	public void custom_user_status_avaiable_type_after_call() {
		System.out.println("Test case --custom_user_status_avaiable_type_after_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
	
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
	
		//Setting the values for custom status
		String customStatusName = "Evening Tea";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "No");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.toString());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//Verify that progress circle is appearing over user image
		//assertTrue(callScreenPage.isWrapUpProgressBarVisible(driver4));
		
		//verify that the count down is running
		int customStatusRemainingTime = callScreenPage.getCustomStatusTimeInSecs(driver4);
		seleniumBase.idleWait(6);
		int newRemainingTime = callScreenPage.getCustomStatusTimeInSecs(driver4);
		System.out.println(newRemainingTime + " " + customStatusRemainingTime);
		assertTrue(newRemainingTime <= (customStatusRemainingTime - 5) && newRemainingTime >= (customStatusRemainingTime - 10));
		
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//verify that user image is available
		callScreenPage.verifyUserImageAvailable(driver4);
		
		//Verify that the user image remains busy during the call and free after the outbound call ends
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("prod_user_1_number"));
			
		//verify that call appearing
		seleniumBase.idleWait(2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver2));
		
		//verify that user image is busy
		callScreenPage.verifyUserImageBusy(driver4);
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver2);
		
		//hang up the call
		softPhoneCalling.hangupActiveCall(driver4);
		
		//verify that user image is busy
		callScreenPage.verifyUserImageAvailable(driver4);
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Caller calls via call flow- dialed user busy on Lunch- caller redirect to no-answer step
	@Test(groups = { "Regression" })
	public void custom_user_status_busy_call_flow() {
		System.out.println("Test case --custom_user_status_busy_call_flow-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "New Automation Custom Status";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		String message = customUserStatusPage.selectCustomStatusDuration(driver4, Duration.Fifteen);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);

		
		// Making a call to the call flow
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));

		// dialing and picking up call to agent
		seleniumBase.idleWait(5);
		
		//get the call history counts
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver4);
		
		//verify that call is not appearing 
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//Hang up the call from caller if its still active. Verify that call history count has been increased
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver4, callHistoryCounts);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify call should not come by call flow acd queue when user status set Busy
	@Test(groups = { "Regression" })
	public void custom_user_status_busy_call_flow_no_answer() {
		System.out.println("Test case --custom_user_status_busy_call_flow_no_answer-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver3, 1);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver1, 1);
		
		//Subscribe a queue
		softPhoneCallQueues.unSubscribeQueue(driver1, CONFIG.getProperty("qa_acd_group_1_name"));
		softPhoneCallQueues.subscribeQueue(driver3, CONFIG.getProperty("qa_acd_group_1_name"));
		softPhoneCallQueues.subscribeQueue(driver4, CONFIG.getProperty("qa_acd_group_1_name"));
		
		//Set the values of the default custom status busy
		String customStatusName = "Busy";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.DoesNotExpire.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Default busy status");
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver3);
		customUserStatusPage.selectCustomStatus(driver3, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver3);

		
		// Making a call to the call flow
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_acd_call_flow_number"));

		// dialing and picking up call to agent
		seleniumBase.idleWait(5);
		
		//get the call history counts
		int callHistoryCounts = softphoneCallHistoryPage.getHistoryMissedCallCount(driver3);
		
		//verify that call is not appearing 
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver4));
		assertFalse(softPhoneCalling.isDeclineButtonVisible(driver3));
		
		//Hang up the call from caller if its still active. Verify that call history count has been increased
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softphoneCallHistoryPage.isMissedCallCountIncreased(driver3, callHistoryCounts);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	//Verify Name and Description fields of custom status allowed 255 characters maximum
	@Test(groups = { "MediumPriority" })
	public void verify_custom_user_status_name_description_limit() {
		System.out.println("Test case --verify_custom_user_status_name_description_limit-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//Setting the values for custom status
		String customStatusName = "";
		for (int i = 0; i < 7; i++) {
			customStatusName = customStatusName + HelperFunctions.GetRandomString(35);
		}		 
		customStatusName = customStatusName + HelperFunctions.GetRandomString(10);
		
		String customStatusDescription = "";
		for (int i = 0; i < 7; i++) {
			customStatusDescription = customStatusDescription + HelperFunctions.GetRandomString(35);
		}		 
		customStatusDescription = customStatusDescription + HelperFunctions.GetRandomString(10);
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.enterCustomSatusDesc(driver4, customStatusDescription);

		//try to add extra string after the 255 characters in call notes subject
		accountIntelligentDialerTab.appendCustomSatusName(driver4, "Custom Status Name");
		accountIntelligentDialerTab.appendCustomSatusDesc(driver4, "Custom Status Name");
		
		//
		assertEquals(accountIntelligentDialerTab.getCustomSatusEditName(driver4), customStatusName);
		assertEquals(accountIntelligentDialerTab.getCustomSatusEditDescription(driver4), customStatusDescription);
		
		accountIntelligentDialerTab.closeComplainceHourWindow(driver4);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}

	//Add New Custom status from admin to all Team for default timing and Busy=No , set status on dialer
	@Test(groups = { "MediumPriority" })
	public void custom_user_status_avaiable_type_all_team() {
		System.out.println("Test case --custom_user_status_avaiable_type_all_team-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);
	
		// enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
	
		//Setting the values for custom status
		String customStatusName = "Available All Team";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "No");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.toString());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Verify that the custom user status is appearing for two teams
		//reload softphone
		reloadSoftphone(adminDriver);
		
		//Select custom status which is added above for one user
		callScreenPage.clickUserImage(adminDriver);		
		customUserStatusPage.selectCustomStatus(adminDriver, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(adminDriver);
		
		//Switch to softphone and reload
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above for another user
		callScreenPage.clickUserImage(driver4);		
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//verify the the message that appears on hovering mouse pointer on users image
		callScreenPage.hoverOnUserImage(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//verify that user image is available
		callScreenPage.verifyUserImageAvailable(driver4);
		
		//Verify that the user image remains busy during the call and free after the outbound call ends
		// dialing and picking up call to agent
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_2_number"));
			
		//verify that call appearing
		seleniumBase.idleWait(2);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(driver4));
		
		//pickup the call
		softPhoneCalling.pickupIncomingCall(driver4);
		
		//hang up the call
		softPhoneCalling.hangupActiveCall(driver4);
		
		//Quit Admin driver
		adminDriver.quit();
		adminDriver = null;
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("adminDriver", false);

		System.out.println("Test case is pass");
	}
	
	//Verify on status of members of the team on dialer, after deleting it.
	@Test(groups = { "MediumPriority"})
	public void custom_status_for_deleted_team_member() 
	{		
		System.out.println("Test case --custom_status_for_deleted_team_member-- started ");

		 //updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("agentDriver");
	    driverUsed.put("agentDriver", true);
	    
	    String agentName = CONFIG.getProperty("qa_agent_user_name");
	    String teamName  = CONFIG.getProperty("qa_group_5_name");

    	//Setting the values for custom status and add it for the team which will be deleted
  		String customStatusName = "Lunch";
  		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
  		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
  		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
  		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
  		customUserStatusDetail.put(CustomStatusFields.Team, teamName);
  		
  		//Add the custom user status
  		accountIntelligentDialerTab.addNewCustomStatus(driver4, customUserStatusDetail);
  		
  		//verify that custom user status is added
  		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
  		accountIntelligentDialerTab.saveAcccountSettings(driver4);
  		
  		//verify custom status added above is visible for the user
		reloadSoftphone(agentDriver);
		callScreenPage.clickUserImage(agentDriver);
		assertTrue(customUserStatusPage.getCustomStatusRow(agentDriver, customStatusName) != null);
		customUserStatusPage.clickCustomStatusCancelBtn(agentDriver);
	
	    //adding agent again as supervisor
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.deleteMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
		//verify custom status added above is not visible for the user who is deleted from the team
		reloadSoftphone(agentDriver);
		callScreenPage.clickUserImage(agentDriver);
		assertTrue(customUserStatusPage.getCustomStatusRow(agentDriver, customStatusName) == null);
		customUserStatusPage.clickCustomStatusCancelBtn(agentDriver);
	
  	    //adding agent again as supervisor
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, teamName, "");
		groupsPage.addMember(driver4, agentName);
		groupsPage.saveGroup(driver4);
		
	    seleniumBase.closeTab(driver4);
	    seleniumBase.switchToTab(driver4, 1);
	    
		agentDriver.quit();
		agentDriver = null;
	    
		//Setting up driver used to false state
		driverUsed.put("driver2", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("agentDriver", false);

		System.out.println("Test case is pass");
	}
	
	//Create a status to specific Team, Team members and supervisor able to set Custom status on dialer
	@Test(groups = { "MediumPriority" })
	public void custom_user_status_team_section_open_barge_true() {
		System.out.println("Test case --custom_user_status_team_section_open_barge_true-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	
		// enable custom user status setting
		String customStatusName = "Custom Status Team";
		
		//Setting the values for custom status
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Five.displayName());
		customUserStatusDetail.put(CustomStatusFields.Description, "Lunch time from 12 to 1 PM");
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.setCustomStatusFieldsValues(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//Set open barge setting as true
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.setOpenBarge(driver4, true);
		groupsPage.saveGroup(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Click on user image and on User Custom status modal box verify the added custom status with details
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		
		//Select the updated custom status
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.blank);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("h:mm a");
		Date dateTime = HelperFunctions.GetCurrentDateTimeObj();
		String message = " since " + dateTimeFormatter.format(dateTime);
		
		//verify the tooltip on hovering user image
		callScreenPage.hoverOnUserImage(driver4);
		callScreenPage.verifyUserImageBusy(driver4);
		assertEquals(customUserStatusPage.getCustomStatusToolTip(driver4), customStatusName + message);
		
		//verify custom status added above is visible for the other team member
		reloadSoftphone(driver3);
		callScreenPage.clickUserImage(driver3);
		assertTrue(customUserStatusPage.getCustomStatusRow(driver3, customStatusName) != null);
		customUserStatusPage.clickCustomStatusCancelBtn(driver3);
		
		//Opening team section page
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		softPhoneTeamPage.openTeamSection(driver1);
		seleniumBase.idleWait(2);
		softPhoneTeamPage.setCustomStatus(driver1, customStatusName);
		seleniumBase.idleWait(1);
		
		//Verifying that status of the 1 member is visible as custom status now
		String agentName = CONFIG.getProperty("qa_user_2_name");
		assertEquals(softPhoneTeamPage.getTeamMemberStatus(driver1, agentName), customStatusName);
		assertEquals(softPhoneTeamPage.getAgents(driver1).size(), 1);
		assertEquals(softPhoneTeamPage.verifyAgentPresent(driver1, agentName), true);
		
		//Set open barge setting as true
		seleniumBase.switchToTab(driver4, seleniumBase.getTabCount(driver4));
	    groupsPage.openGroupSearchPage(driver4);
		groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
		groupsPage.setOpenBarge(driver4, false);
		groupsPage.saveGroup(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
	
		System.out.println("Test case is pass");
	}
	
	
	@Test(groups = { "Regression" })
	public void verify_custom_user_status_contact_page() {
		System.out.println("Test case --verify_custom_user_status_contact_page-- started ");
		
		//updating the driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);

	    // enable custom user status setting
		accountIntelligentDialerTab.enableAllowUsersOverrideTimeSetting(driver4);
		
		//Setting the values for custom status
		String customStatusName = "Lunch";
		HashMap<AccountIntelligentDialerTab.CustomStatusFields, String> customUserStatusDetail = new HashMap<>();
		customUserStatusDetail.put(CustomStatusFields.StatusName, customStatusName);
		customUserStatusDetail.put(CustomStatusFields.Busy, "Yes");
		customUserStatusDetail.put(CustomStatusFields.Time, CustomStatusTime.Fifteen.displayName());
		customUserStatusDetail.put(CustomStatusFields.Team, "All");
		
		//Add the custom user status
		accountIntelligentDialerTab.clickAddNewCustomStatusBtn(driver4);
		accountIntelligentDialerTab.enterCustomSatusName(driver4, customStatusName);
		accountIntelligentDialerTab.saveComplainceHourSettings(driver4);
		
		//verify that custom user status is added
		accountIntelligentDialerTab.verifyAddedCustomStatusDetails(driver4, customUserStatusDetail);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		
		//switch to softphone and reload the softphone
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(driver4);
		
		//Select custom status which is added above with duration as five minutes
		callScreenPage.clickUserImage(driver4);
		customUserStatusPage.selectCustomStatus(driver4, customStatusName, customUserStatusDetail);
		customUserStatusPage.selectCustomStatusDuration(driver4, Duration.five);
		customUserStatusPage.clickCustomStatusSaveBtn(driver4);
		
		//Now go to contacts page and serach the contact and verify that the user status is the same as selected above
		softPhoneContactsPage.clickActiveContactsIcon(driver4);
		softPhoneContactsPage.ringDNASearch(driver4, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());
		softPhoneContactsPage.isResultStatusRed(driver4, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString());
		assertEquals(softPhoneContactsPage.getUsersStatus(driver4, CONFIG.getProperty("qa_user_2_name"), SoftPhoneContactsPage.searchTypes.Users.toString()), customStatusName);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups = { "Regression", "MediumPriority", "Sanity", "Product Sanity" })
	public void afterMethod(ITestResult result) {

		if((result.getStatus() == 2 || result.getStatus() == 3)) {
			if(result.getTestName() == "verify_custom_user_selected_team()") {
				initializeDriverSoftphone("driver4");
				driverUsed.put("driver4", true);
		
				//Setting default Call Recording Settings for the account
		 	    loginSupport(driver4);
		 	    
		 	    //Delete the supervisor added in the case verify_custom_user_selected_team	
		 	    loginSupport(driver4);
				groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_3_name"), "");
				groupsPage.deleteSuperviosr(driver4, CONFIG.getProperty("qa_user_2_name"));
				groupsPage.saveGroup(driver4);
				
				//Switch to softphone
				seleniumBase.switchToTab(driver4, 1);
		
				// Setting driver used to false as this test case is pass
				driverUsed.put("driver4", false);
			}else if(result.getTestName() == "custom_status_for_deleted_team_member()") {
				initializeDriverSoftphone("driver4");
				driverUsed.put("driver4", true);
		
				//Setting default Call Recording Settings for the account
		 	    loginSupport(driver4);
		 	    
		 	    //Delete the supervisor added in the case verify_custom_user_selected_team	
		 	    loginSupport(driver4);
				groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_5_name"), "");
				groupsPage.addMember(driver4, CONFIG.getProperty("qa_agent_user_name"));
				groupsPage.saveGroup(driver4);
				
				//Switch to softphone
				seleniumBase.switchToTab(driver4, 1);
		
				// Setting driver used to false as this test case is pass
				driverUsed.put("driver4", false);
			}else if(result.getTestName() == "custom_user_status_team_section_open_barge_true()") {
				initializeDriverSoftphone("driver4");
				driverUsed.put("driver4", true);
		
				//Setting default Call Recording Settings for the account
		 	    loginSupport(driver4);
		 	    
		 	    //Delete the supervisor added in the case verify_custom_user_selected_team	
		 	    loginSupport(driver4);
		 	    groupsPage.openGroupSearchPage(driver4);
				groupsPage.openGroupDetailPage(driver4, CONFIG.getProperty("qa_group_1_name"), "");
				groupsPage.setOpenBarge(driver4, false);
				groupsPage.saveGroup(driver4);
				
				//Switch to softphone
				seleniumBase.switchToTab(driver4, 1);
		
				// Setting driver used to false as this test case is pass
				driverUsed.put("driver4", false);
			}
		}
	}

	@AfterClass(groups={"Sanity", "Regression", "MediumPriority", "Product Sanity"})
	public void disableCustomUserStatus() {
		removeAllCustomUserStatus();
		
		//disable Custom User Status and save the settings
		accountIntelligentDialerTab.disableCustomUserStatusSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	}
}
