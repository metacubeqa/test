package support.source.roadMap;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;

public class RingDnaRoadMapPage extends SeleniumBase {
	
	
	By ringDnaRoadMapLink		       = By.cssSelector(".product-portal-link");
	By sumbitIdeaButton                = By.xpath("//div[text()='Submit idea']");
	By disableSubmitButton             = By.xpath("//span[text()='Submit']//parent::div[@disabled]");
	By submitButton                    = By.xpath("//span[text() = 'Submit']/parent::div");
	By browseIdeasButton               = By.xpath("//div[text()='Browse other ideas']");
	
	By ideaTextArea                    = By.xpath("//textarea[contains(@placeholder,'What would you like')]");
	By iframe                          = By.cssSelector(".product-portal-iframe");	
	By underConsideration              = By.xpath("//a[text()='Under consideration']");
	By planned                         = By.xpath("//a[text()='Planned']");
	By launched                        = By.xpath("//a[text()='Launched']");
	By featureBlogsList                = By.xpath("//div[@data-cy='PortalSection-Title']/following-sibling::div");
	By featureCloseButton              = By.xpath("//div[@data-cy='PortalCard-Overlay']/following-sibling::div");
	By insightTextArea                 = By.xpath("//textarea[@placeholder = 'Add new insight...' or 'Why do you need this']");
	By shareIcon                       = By.xpath("//div[@data-cy='PortalCard-Modal']//*[local-name()='svg' and contains(@class, 'pb-icon')]/parent::div");
	By copyPrivateLink                 = By.xpath("//li[@data-cy='PickerMenu-Item']");
	
	String insightMessgae              = "//span/parent::div /following-sibling::div[text()='%message%']";
	String insightImportanceLevel      = "//span[text()='%mail%']//parent::div/following-sibling::span";
	String importantOptions            = "//div[text()='%String%']";
	String email                       = "//span[text()='%email%']";
	
	
	
	public enum important{
		Important("Important"),
		Critical("Critical"),
		NiceToHave("Nice-to-have");
		
		private String value;
		 
		important(String envValue) {
	        this.value = envValue;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	}
	
	public void openRoadMap(WebDriver driver) {
		clickElement(driver, ringDnaRoadMapLink);
	}
	
	public void clickSubmitIdea(WebDriver driver) {
		waitUntilVisible(driver, sumbitIdeaButton);
		clickElement(driver, sumbitIdeaButton);
	}
	
	public boolean verifySubmitButtonDisabled(WebDriver driver) {
		return isElementVisible(driver, disableSubmitButton, 6);
	}
	
	public String getEmailText(WebDriver driver, String emailString) {
		By locator = By.xpath(email.replace("%email%", emailString));
		List<String> emailList = getTextListFromElements(driver, locator);
		return emailList.get(0);
	}
	
	public void writeIdeaInTextArea(WebDriver driver, String text) {
		enterText(driver, ideaTextArea, text);
	}
	
	public void writeInsightInTextArea(WebDriver driver, String text) {
		enterText(driver, insightTextArea, text);
	}
	
	public void selectImportanceOption(WebDriver driver, important imp) {
		By locator = By.xpath(importantOptions.replace("%String%", imp.getValue()));
		clickElement(driver, locator);
	}
	
	public void selectImportanceOptionForInsight(WebDriver driver, important imp) {
		if(isElementVisible(driver, insightTextArea,6)) {
			clickElement(driver, insightTextArea);
		}
		By locator = By.xpath(importantOptions.replace("%String%", imp.getValue()));
		clickElement(driver, locator);
	}
	
	public void clickUnderConsideration(WebDriver driver) {
		driver.switchTo().frame(findElement(driver, iframe));
		clickElement(driver, underConsideration);
	}
	
	public void clickPlannedTab(WebDriver driver) {
		driver.switchTo().frame(findElement(driver, iframe));
		clickElement(driver, planned);
	}
	
	public void clickLaunchedTab(WebDriver driver) {
		driver.switchTo().frame(findElement(driver, iframe));
		clickElement(driver, launched);
	}
	
	public void clickFeatureBlog(WebDriver driver, int index) {
		List<WebElement> blogs = getElements(driver, featureBlogsList);
		clickElement(driver, blogs.get(index-1));
	}
	
	public void clickSubmitButton(WebDriver driver) {
		clickElement(driver, submitButton);
		clickElement(driver, featureCloseButton);
	}
	
	public void checkInsightMessage(WebDriver driver, String insight) {
		By locator = By.xpath(insightMessgae.replace("%message%", insight));
		assertTrue(isElementVisible(driver, locator, 6));
	}
	
	public String checkInsightImportanceLevel(WebDriver driver, String mail) {
		By locator = By.xpath(insightImportanceLevel.replace("%mail%", mail));
		waitUntilVisible(driver, locator);
		return findElement(driver, locator).getText();
	}
	
	/**
	 * @param driver
	 * copy private link
	 */
	public void copyPrivateLink(WebDriver driver) {
		waitUntilVisible(driver, shareIcon);
		clickElement(driver, shareIcon);
		waitUntilVisible(driver, copyPrivateLink);
		clickElement(driver, copyPrivateLink);
	}
	
}
