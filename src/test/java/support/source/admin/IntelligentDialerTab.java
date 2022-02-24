/**
 * 
 */
package support.source.admin;

import org.openqa.selenium.WebDriver;
import support.source.accounts.AccountIntelligentDialerTab;

/**
 * @author Abhishek
 *
 */
public class IntelligentDialerTab extends AccountIntelligentDialerTab{
	public void setDefaultAdminIntelligentDialerSettings(WebDriver driver){
		System.out.println("Setting default settings for intelligent dialer");
		openIntelligentDialerTab(driver);
		enableSMSSetting(driver);
		enableWebLeadsSetting(driver);
		saveAcccountSettings(driver);
	}
}
