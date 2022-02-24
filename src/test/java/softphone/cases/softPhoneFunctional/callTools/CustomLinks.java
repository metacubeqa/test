/**
 * 
 */
package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class CustomLinks extends SoftphoneBase{

	@Test(groups = { "Sanity", "MediumPriority" })
	public void access_custom_links() {
		System.out.println("Test case --access_custom_links()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Click on CustomLink
	    callToolsPanel.clickCustomLinkIcon(driver1);
	    List<String> sortedCustomLinks = callToolsPanel.getCustomLinksList(driver1);
	    Collections.sort(sortedCustomLinks, String.CASE_INSENSITIVE_ORDER);
	    List<String> customLinks = callToolsPanel.getCustomLinksList(driver1);
	    assertTrue(customLinks.equals(sortedCustomLinks));
	    callToolsPanel.selectCustomLinkByText(driver1, CONFIG.getProperty("softphone_custom_link"));
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    assertTrue(driver1.getTitle().contains("Google"));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void create_custom_links() {
		System.out.println("Test case --create_custom_links()-- started ");
		
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String customLinkName = "Selenium" + HelperFunctions.GetRandomString(2);
		String customLinkUrl = "https://www.seleniumhq" + HelperFunctions.GetRandomString(2).toLowerCase() + ".org/";
		
		// navigating to support page
		loginSupport(driver1);

		// opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");

		// disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteRecords(driver1, customLinkName);
		accountIntelligentDialerTab.createCustomLink(driver1, customLinkName, customLinkUrl);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		reloadSoftphone(driver1);

		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
		
	    //hanging up with the call.
	    softPhoneCalling.hangupActiveCall(driver2);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    seleniumBase.idleWait(2);
	    
		//Click on CustomLink
	    callToolsPanel.clickCustomLinkIcon(driver1);
	    callToolsPanel.selectCustomLinkByText(driver1, customLinkName);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
	    assertTrue(customLinkUrl.contains(driver1.getTitle()));
	    
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//navigating to support page 
	    loginSupport(driver1);
						
		//opening up accounts setting for load test account
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
						
		//disabling Task due date option
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteRecords(driver1, customLinkName);
		seleniumBase.idleWait(2);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    
	    System.out.println("Test case is pass");
	}
}