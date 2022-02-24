package support.source.conversationAI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import support.source.accounts.AccountOverviewTab;
import support.source.commonpages.Dashboard;

public class DashBoardConversationAI extends SeleniumBase {
	
	Dashboard dashboard = new Dashboard();
	AccountOverviewTab accountOverviewTab = new AccountOverviewTab();

	By rdnaLogo				= By.cssSelector(".ringdna-logo");
	By saveSearchTab 		= By.xpath(".//*[@class='rdnaLink' and text()='Saved Search'] | .//*[@href='/saved-search']");
	By libraryTab			= By.xpath(".//*[@class='rdnaLink' and text()='Library'] | .//*[@href='/library']");
	By inboxTab				= By.cssSelector(".rdnaLink[href='/#smart-recordings/inbox'], [href='/inbox']");
	By insightsTab			= By.cssSelector("[href='/#smart-recordings/insights'], [href='/insights']");
	By callsTab				= By.cssSelector("[href='/#smart-recordings/calls'], [href='/conversations']");
	By activityTab			= By.cssSelector("[href='/#smart-recordings/insights']");
	By settingsLink  		= By.cssSelector("[href='/settings']");
	
	public void isCAIPageVisible(WebDriver driver) {
		if (!isElementVisible(driver, callsTab, 1)) {
			dashboard.clickConversationAI(driver);
		}
	}
	
	public boolean isSectionActive(WebDriver driver, By locator) {
		driver.switchTo().defaultContent();
		isCAIPageVisible(driver);

		boolean value = false;

		if (driver.getCurrentUrl().contains("analytics")) {

			String color = getCssValue(driver, findElement(driver, locator), CssValues.BackgroundColor);
			System.out.println("color= " + color);
			if (!color.equals("rgba(94, 58, 187, 1)")) {
				scrollToElement(driver, locator);
				clickElement(driver, locator);
				dashboard.isPaceBarInvisible(driver);
			} else {
				value = true;
			}
		} else {
			String color = getCssValue(driver, findElement(driver, locator), CssValues.Color);
			System.out.println("color= " + color);
			if (!color.equals("rgba(0, 102, 255, 1)")) {
				scrollToElement(driver, locator);
				clickElement(driver, locator);
				dashboard.isPaceBarInvisible(driver);
			} else {
				value = true;
			}
		}
		return value;
	}

	// save search section
	public void navigateToSaveSearchSection(WebDriver driver) {
		isSectionActive(driver, saveSearchTab);
	}

	// library section
	public void navigateToLibrarySection(WebDriver driver) {
		isSectionActive(driver, libraryTab);
	}
	
	// calls section
	public boolean navigateToCallsSection(WebDriver driver) {
		return isSectionActive(driver, callsTab);
	}
	
	// insights section
	public boolean navigateToInsightsSection(WebDriver driver) {
		return isSectionActive(driver, insightsTab);
	}

	// inbox section
	public void navigateToInboxSection(WebDriver driver) {
		isSectionActive(driver, inboxTab);
	}
	
	// activity feed
	public void navigateToActivityFeedSection(WebDriver driver) {
		isSectionActive(driver, activityTab);
	}
	
	public boolean isActivityFeedSectionVisible(WebDriver driver) {
		return isElementVisible(driver, activityTab, 10);
	}
	
	public boolean isCallsSectionVisible(WebDriver driver) {
		return isElementVisible(driver, callsTab, 5);
	}
	
	public boolean isInsightsSectionVisible(WebDriver driver) {
		return isElementVisible(driver, insightsTab, 5);
	}
	
	public boolean isSettingsSectionVisible(WebDriver driver) {
		return isElementVisible(driver, settingsLink, 5);
	}
	
	public void clickRDNALogo(WebDriver driver) {
		waitUntilVisible(driver, rdnaLogo);
		clickElement(driver, rdnaLogo);
		dashboard.isPaceBarInvisible(driver);
		accountOverviewTab.verifyUserOnAccountOverViewPage(driver);
	}
}
