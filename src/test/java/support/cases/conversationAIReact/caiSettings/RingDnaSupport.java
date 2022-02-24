package support.cases.conversationAIReact.caiSettings;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.SettingsPage;
import utility.HelperFunctions;

public class RingDnaSupport extends SupportBase {

	Dashboard dashboard = new Dashboard();
	SettingsPage settingsPage = new SettingsPage();
	
	//Verify the message on the page when user enter more then 7 days recording to upload into CAI
	//Verify the validation message when user enter negative/ string/ more then 31 days value to upload CAI
	@Test(groups = { "Regression" })
	public void verify_validation_message_for_enter_days_to_upload_cai() {
		System.out.println("Test case --verify_validation_message_for_enter_days_to_upload_cai-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);

		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);

		settingsPage.clickRingDnaSupportTab(caiCallerDriver);

		// enter days
		String days = "10";
		settingsPage.enterDaysPriorToToday(caiCallerDriver, days);
		//assert
		assertEquals(settingsPage.getErrorMessage(supportDriver), "You have entered to batch upload recordings for more than the past 7 days, this may take a while.");

		// enter days
		days = "-10";
		settingsPage.enterDaysPriorToToday(caiCallerDriver, days);
		//assert
		assertEquals(settingsPage.getErrorMessage(supportDriver), "You have entered to batch upload recordings for more than the past 7 days, this may take a while.");
				
		// enter days
		days = HelperFunctions.GetRandomString(5);
		settingsPage.enterDaysPriorToToday(caiCallerDriver, days);
		settingsPage.clickUploadButton(caiCallerDriver);
		//assert
		assertEquals(settingsPage.getErrorMessage(supportDriver), "{\"code\":\"Bad Request\",\"message\":\"'days' is an incorrect value; 'days' was not provided\",\"httpStatus\":\"400\"}");		
		
		// enter days
		days = "5";
		settingsPage.enterDaysPriorToToday(caiCallerDriver, days);
		settingsPage.clickUploadButton(caiCallerDriver);
		//assert
		assertEquals(settingsPage.getErrorMessage(supportDriver), "Upload job has started. Check papertrail for status: https://papertrailapp.com/events?q=Voicebase");

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_validation_message_for_enter_days_to_upload_cai-- passed ");
	}

}
