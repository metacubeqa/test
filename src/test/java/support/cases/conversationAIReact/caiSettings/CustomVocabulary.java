package support.cases.conversationAIReact.caiSettings;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.SettingsPage;
import utility.HelperFunctions;

public class CustomVocabulary extends SupportBase {
	Dashboard dashboard = new Dashboard();
	SettingsPage settingsPage = new SettingsPage();
	
	@Test(groups = { "Regression" })
	public void verify_add_delete_custom_vocabulary() {
		System.out.println("Test case --verify_add_delete_custom_vocabulary-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.verifyCustomVocHeadersLabel(caiCallerDriver);
		settingsPage.verifyCustomVocErrorMsg(caiCallerDriver);
		
		String customVoc = "AutoCustomVoc".concat(HelperFunctions.GetRandomString(4));
		settingsPage.createCustomVocabulary(caiCallerDriver, customVoc);
		settingsPage.refreshCurrentDocument(caiCallerDriver);
		
		settingsPage.verifyCustomVocAscendingOrder(caiCallerDriver);
		
		//delete custom voc
		settingsPage.deleteCustomVoc(caiCallerDriver, customVoc);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_add_delete_custom_vocabulary-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_error_msg_after_entering_text_longer_than_64_words() {
		System.out.println("Test case --verify_error_msg_after_entering_text_longer_than_64_words-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		String customVocMoreThan60Char = "cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzuqwr";
		settingsPage.enterTextInCustomVocInputTab(caiCallerDriver, customVocMoreThan60Char);
		settingsPage.verifyToastErrorMsg(caiCallerDriver, SettingsPage.errorMsgCustomVoc);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_error_msg_after_entering_text_longer_than_64_words-- passed ");
	}
}