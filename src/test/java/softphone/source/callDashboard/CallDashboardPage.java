/**
 * 
 */
package softphone.source.callDashboard;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import base.TestBase;

/**
 * @author Abhishek
 *
 */
public class CallDashboardPage extends SeleniumBase{
	By activeCallerNumbers	= By.cssSelector("[title=\"'Caller'\"] span");
	By activeCallsUser 		= By.cssSelector("[title=\"'User'\"]");
	By activequeueName		= By.cssSelector("[title=\"'Queue'\"]");
	By callDirectionCell	= By.cssSelector("[title=\"'Direction'\"]");
	By incomingCallImage	= By.cssSelector("[ng-src='/images/ico-incoming-call.png']");
	By outgoingCallImage	= By.cssSelector("[ng-src='/images/ico-outgoing-call.png']");
	By sendToLink			= By.cssSelector("[title=\"'User'\"] a[ng-click *= 'SendTo']");
	By sendToAgents			= By.cssSelector("[ng-table='tableSendToAgent'] .agent-name");
	By availableAgents		= By.cssSelector("#availableDiv .agent-name");
	By busyAgents			= By.cssSelector("#busyDiv .agent-name");
	By loadingWheel			= By.className("loader-content");
	
	public void openCallDashboardPage(WebDriver driver){
		openNewBlankTab(driver);
		switchToTab(driver, getTabCount(driver));
		driver.get(TestBase.CONFIG.getProperty("call_dashboard_url"));
		for(int i=0; i < 24; i++) {
			idleWait(5);
			if(!isElementVisible(driver, loadingWheel, 0))
				break;
		}
	}
	
	public Boolean isAgentAvailable(WebDriver driver, String agentName) {
		List<WebElement> agentsList = getElements(driver, availableAgents);
		for (int i = 0; i < agentsList.size(); i++) {
			if (agentsList.get(i).getText().equals(agentName)) {
				return true;
			}
		}
		System.out.println("Expected: " + agentName + " is not found in avaiable agents list");
		return false;
	}
	
	public Boolean isAgentBusy(WebDriver driver, String agentName) {
		List<WebElement> agentsList = getElements(driver, busyAgents);
		for (int i = 0; i < agentsList.size(); i++) {
			if (agentsList.get(i).getText().equals(agentName)) {
				return true;
			}
		}
		System.out.println("Expected: " + agentName + " is not found in Busy agents list");
		return false;
	}
	
	public int getActiveCallUsersIndex(WebDriver driver, String activeUser){
		List<WebElement> activeCallUserList = getElements(driver, activeCallsUser);
		for (int i = 0; i < activeCallUserList.size(); i++) {
			if (activeCallUserList.get(i).getText().contains(activeUser)) {
				return i;
			}
		}
		return -1;
	}
	
	public void verifyOutgoingCallUsingIndex(WebDriver driver, int index){
		assertTrue(getElements(driver, callDirectionCell).get(index).findElement(outgoingCallImage).isDisplayed());
	}
	
	public void verifyIncomingCallUsingIndex(WebDriver driver, int index){
		assertTrue(getElements(driver, callDirectionCell).get(index).findElement(incomingCallImage).isDisplayed());
	}
	
	public int getActiveQueueIndex(WebDriver driver, String activeQueue){
		List<WebElement> activeQueueList = getElements(driver, activequeueName);
		for (int i = 0; i < activeQueueList.size(); i++) {
			if (activeQueueList.get(i).getText().contains(activeQueue)) {
				return i;
			}
		}
		return -1;
	}
	
	public void clickSendToLinkUsingIndex(WebDriver driver, int index){
		getElements(driver, sendToLink).get(index).click();
	}
	
	public void selectSentToAgent(WebDriver driver, String sendToAgentName, int queueIndex){
		int i = -1;
		List<WebElement> sendToAgentsList = getElements(driver, sendToAgents);
		for (i = 0; i < sendToAgentsList.size(); i++) {
			if (sendToAgentsList.get(i).getText().contains(sendToAgentName)) {
				sendToAgentsList.get(i).click();
				break;
			}
		}
		waitUntilInvisible(driver, sendToAgents);
		waitUntilInvisible(driver, sendToLink);
		assertEquals(getElements(driver, activeCallsUser).get(queueIndex).getText(), sendToAgentName);
	}
}
