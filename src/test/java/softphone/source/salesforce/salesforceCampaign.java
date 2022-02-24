 package softphone.source.salesforce;
 
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import softphone.source.CallScreenPage;
import base.SeleniumBase;
import base.TestBase;

/**
 * @author Abhishek
 *
 */
public class salesforceCampaign extends SeleniumBase{
	CallScreenPage callScreenPage = new CallScreenPage();

	By campaignHeading				= By.className("pageDescription");
	By campaignPageType				= By.className("pageType");
	By campaignLeadsCount			= By.xpath("//*[text()='My Leads in Campaign' or text()='Leads in Campaign' or text()='Leads']/following-sibling::td[1]/div|//*[text()='My Leads in Campaign' or text()='Leads in Campaign'  or text()='Leads']/following-sibling::td[1]");
	By campaignMemberNamesLists		= By.xpath(".//*[contains(@id,'MemberList')]//tr[contains(@class,'dataRow')][1]//td[contains(@class,'dataCell')]/a");
	By ringDNAStatsListEntries 		= By.xpath(".//*[text()='RingDNA Stats']/ancestor::*[contains(@class,'bRelatedList')]//th/a");
	By ringDNAStatsGoToListLink 	= By.xpath(".//*[text()='RingDNA Stats']/ancestor::*[contains(@class,'bRelatedList')]//a[contains(text(),'Go to list')]");
	By ringDNAStatsMoreItemtLink 	= By.cssSelector("[title='Show More']");
	By ringDNAStatsAllListEntries  	= By.xpath(".//*[contains(@class,'listRelatedObject')]//th[contains(@class,'dataCell')]/a");
	By ringDNAStatsCreatedDate		= By.xpath(".//td[text()='Date']/following-sibling::td[1]");
	
	public void verifyCampaignPageOpened(WebDriver driver, String campaignName) {
		waitUntilVisible(driver, campaignHeading);
		assertEquals(getElementsText(driver, campaignPageType), "Campaign");
		assertEquals(getElementsText(driver, campaignHeading), campaignName);
	}
	
	public void openRecentCampaignStatsEntry(WebDriver driver) {
		if(isElementVisible(driver, ringDNAStatsGoToListLink, 0)){
			OpenLatestCampStatsFromAllEntries(driver);
		}else {
			List<WebElement> rdnaStatsEntries = getElements(driver, ringDNAStatsListEntries);
			rdnaStatsEntries.get(rdnaStatsEntries.size() - 1).click();
		}
	}
	
	public boolean openSecondRecentCampaignStatsEntry(WebDriver driver) {
		if(isElementVisible(driver, ringDNAStatsGoToListLink, 0)){
			OpenSecondlatestCampStatsFromAllEntries(driver);
		}else {
			List<WebElement> rdnaStatsEntries = getElements(driver, ringDNAStatsListEntries);
			if(rdnaStatsEntries.size() > 1) {
				rdnaStatsEntries.get(rdnaStatsEntries.size() - 2).click();	
			}else {
				System.out.println("There is only single RingDNA stats entry");
				return false;
			}
		}
		return true;
	}
	
	public void openAllStatsList(WebDriver driver) {
		clickElement(driver, ringDNAStatsGoToListLink);
		isListElementsVisible(driver, ringDNAStatsAllListEntries, 60);
		while(isElementVisible(driver, ringDNAStatsMoreItemtLink, 0))
		{
			clickElement(driver, ringDNAStatsMoreItemtLink);
		}	
	}
	
	public void OpenLatestCampStatsFromAllEntries(WebDriver driver) {
		openAllStatsList(driver);
		List<WebElement> ringdnaStatsEntries = getElements(driver, ringDNAStatsAllListEntries);
		ringdnaStatsEntries.get(ringdnaStatsEntries.size() - 1).click();
	}
	
	public void OpenSecondlatestCampStatsFromAllEntries(WebDriver driver) {
		openAllStatsList(driver);
		List<WebElement> ringdnaStatsEntries = getElements(driver, ringDNAStatsAllListEntries);
		ringdnaStatsEntries.get(ringdnaStatsEntries.size() - 2).click();
	}
	
	public String getRingDNAStatsCreatedDate(WebDriver driver) {
		waitUntilVisible(driver, ringDNAStatsCreatedDate);
		String CreatedDate = getElementsText(driver, ringDNAStatsCreatedDate);
		driver.navigate().back();
		return CreatedDate;
	}
	
	public void openSalesforceCampaignPage(WebDriver driver){
		openNewBlankTab(driver);
		switchToTab(driver, getTabCount(driver));
		driver.get(TestBase.CONFIG.getProperty("salesforce_url") + TestBase.CONFIG.getProperty("softphone_task_related_campaign_sf_id"));
		callScreenPage.closeLightningDialogueBox(driver);
	}
	
	public String getCampaignLeadsCount(WebDriver driver){
		return getElementsText(driver, campaignLeadsCount);
	}
	
	public List<String> getFirstContactName(WebDriver driver){
		return getTextListFromElements(driver, campaignMemberNamesLists);
	}
}
