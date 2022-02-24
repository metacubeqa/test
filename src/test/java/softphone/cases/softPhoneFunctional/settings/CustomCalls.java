package softphone.cases.softPhoneFunctional.settings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

public class CustomCalls extends SoftphoneBase{

	@BeforeClass(groups = {"MediumPriority"})
	public void enableCustomCalls() {
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Support Page and set custom object
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableCustomActivityObjectState(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
	}
	
	@Test(groups = {"MediumPriority" })
	public void custom_calls_for_oubound_inbound_call() {
		System.out.println("Test case --custom_calls_for_oubound_inbound_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_user_1_number"));

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// Taking incoming call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver1).contains(contactNumber));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver1), "Direct");
		assertEquals(sfTaskDetailPage.getLPRouting(driver1), " ");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void custom_calls_call_flow() {
		System.out.println("Test case --custom_calls_call_flow()-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String callFlowNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_call_flow_call_conference_smart_number"));

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, callFlowNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver4);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver4);
		callScreenPage.openCallerDetailPage(driver4);
		contactDetailPage.openLatestCallActivities(driver4);
		sfTaskDetailPage.verifyCallStatus(driver4, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver4), "Inbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver4).contains(callFlowNumber));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver4), "Tracking");
		assertEquals(sfTaskDetailPage.getLPRouting(driver4), " ");
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void custom_calls_local_presence_call() {
		System.out.println("Test case --custom_calls_local_presence_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_1_number"));
		
		// Select local presence number as outbound Number
		System.out.println("enabling local presence setting");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableLocalPresenceSetting(driver1);

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver2);
		String localPresenceNumber = callScreenPage.getOutboundNumber(driver1);
		System.out.println("Call is removing from caller");
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// Taking incoming call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, localPresenceNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver1).contains(localPresenceNumber));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver1), "Local Presence");
		assertEquals(sfTaskDetailPage.getLPRouting(driver1), "Direct");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Disable Local Presence Setting
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void custom_calls_other_number() {
		System.out.println("Test case --custom_calls_local_presence_call-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 1);

		// Making an outbound call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// Taking incoming call
		System.out.println("Taking an incoming call");
		softPhoneCalling.softphoneAgentCall(driver2, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		// verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openLatestCallActivities(driver1);
		sfTaskDetailPage.verifyCallStatus(driver1, "Connected");
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		assertTrue(sfTaskDetailPage.getLPNumDialed(driver1).contains(contactNumber));
		assertEquals(sfTaskDetailPage.getLPDialedType(driver1), "Other");
		assertEquals(sfTaskDetailPage.getLPRouting(driver1), " ");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);

		System.out.println("Test case is pass");
	}
	 
	
 @AfterClass(groups={"Regression", "MediumPriority"})
	public void disableCustomCallObject() {

		// open Support Page and disable custom object
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		dashboard.isPaceBarInvisible(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.disableCustomActivityObjectState(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
  }
}
