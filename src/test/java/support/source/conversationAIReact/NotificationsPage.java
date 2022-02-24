package support.source.conversationAIReact;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;

public class NotificationsPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	
	By notificationHeader		= By.xpath(".//h2[text()='Notification Settings']");
	
	public boolean isCAINotificationHeaderVisible(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		return isElementVisible(driver, notificationHeader, 5);
	}
	

}
