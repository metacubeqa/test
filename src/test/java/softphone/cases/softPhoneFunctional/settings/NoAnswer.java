/**
 * 
 */
package softphone.cases.softPhoneFunctional.settings;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
/**
 * @author Abhishek
 *
 */
public class NoAnswer extends SoftphoneBase {

	@BeforeClass(groups = { "Sanity", "Regression", "Product Sanity" })
	public void enableNoAnswerSetting() {

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		softPhoneSettingsPage.setNoAnswerNumber(driver1, CONFIG.getProperty("qa_user_3_number"));
		
		// add caller as Contact
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");	  
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}

	@Test(groups = { "Sanity", "Regression" })
	public void verify_no_answer_number_settings() {
		System.out.println("Test case --verify_no_answer_settings-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		//Logging out from softphone
		softPhoneSettingsPage.logoutSoftphone(driver1);
		
		// Making an inbound call to first caller
		System.out.println("second caller making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));

		// receiving call from no answer Number
		System.out.println("first caller picking up the call");
		softPhoneCalling.declineCall(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//loggin again on the softphone
		SFLP.softphoneLogin(driver1, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("qa_user_1_username"), CONFIG.getProperty("qa_user_1_password"));

		//verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallStatus(driver1, "Missed"); 
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_no_answer_agent_decline() {
		System.out.println("Test case --verify_no_answer_agent_decline-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Making an inbound call to first caller
		System.out.println("second caller making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		
		//declining call
		System.out.println("declining call from agent");
		softPhoneCalling.declineCall(driver1);

		// receiving call from no answer Number
		softPhoneCalling.declineCall(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		
		//verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallStatus(driver1, "Missed"); 
		sfTaskDetailPage.verifyCallAbandoned(driver1);
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression" })
	public void verify_no_answer_agent_ignore() {
		System.out.println("Test case --verify_no_answer_agent_ignore-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Making an inbound call to first caller
		System.out.println("second caller making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
		
		//wait for call timeout
		softPhoneCalling.isDeclineButtonVisible(driver1);
		seleniumBase.idleWait(25);

		// receiving call from no answer Number
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.hangupIfInActiveCall(driver2);
		softPhoneCalling.isCallBackButtonVisible(driver3);
		
		//verify data for inbound call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		String callSubject = callScreenPage.openCallerDetailPageWithCallNotes(driver1);
		contactDetailPage.openRecentCallEntry(driver1, callSubject);
		sfTaskDetailPage.verifyCallStatus(driver1, "Missed"); 
		sfTaskDetailPage.verifyCallAbandoned(driver1);
		assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);

		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_own_number_as_no_answer() {
		System.out.println("Test case --verify_own_number_as_no_answer-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.setNoAnswerNumber(driver1, CONFIG.getProperty("qa_user_1_number"));
		
		// add caller as Contact
		assertEquals(softPhoneSettingsPage.gettAlertsText(driver1), "Cannot set no answer number to your RingDNA number");
		softPhoneSettingsPage.acceptAlert(driver1);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}
	
	@AfterClass(groups = {"Sanity", "Regression", "Product Sanity"})
	public void disableNoAnswerSetting() {
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//disable no answer settings
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableNoAnswerSettings(driver1);

		// update the used drivers
		driverUsed.put("driver1", false);
	}
}
