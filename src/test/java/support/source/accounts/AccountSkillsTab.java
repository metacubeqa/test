/**
 * 
 */
package support.source.accounts;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.common.collect.Ordering;

import base.SeleniumBase;
import support.source.callFlows.CallFlowPage;
import support.source.commonpages.Dashboard;
import support.source.users.UsersPage;
import utility.HelperFunctions;

/**
 * @author Vishal
 *
 */
public class AccountSkillsTab extends SeleniumBase{
	Dashboard dashboard = new Dashboard();
	CallFlowPage callFlowPage = new CallFlowPage();
	UsersPage userPage		= new UsersPage();

	By skillsHeader				= By.cssSelector(".skills h2");
	By accountSkillsTab 		= By.cssSelector("a[data-tab='skills']"); 
	By skillNameTextBox			= By.cssSelector(".header-search input");
	By updateSkillTextBox		= By.cssSelector(".skill-name-input");
	String saveUpdatedSkill		= ".//*[contains(@class,'skill-name-input')][@value='$$newskillvalue$$']/parent::td/following-sibling::td//button[@data-action='save']";
	By addSkillButton			= By.cssSelector(".header-search .add-skill");
	
	By skillList				= By.xpath(".//thead//th//a[contains(text(),'Skill Name')]/ancestor::table//tbody/tr[not(contains(@class, 'empty'))]/td[1]/span");
	By userList					= By.xpath(".//thead//th//a[contains(text(),'Number of Users')]/ancestor::table//tbody//td[2]");
	By callFlowList				= By.xpath(".//*[@class='skills']//a[contains(@href, '#call-flows')]");
	
	String updateGivenSkill		= ".//tbody//td//span[text()='$$skill$$']/../../td/button[@data-action='edit']";
	String deleteGivenSkill		= ".//tbody//td//span[text()='$$skill$$']/../../td/a[@class='delete']/span";
	String userCountForSkill	= ".//tbody//td//span[text()='$$skill$$']/../..//td[contains(@class,'userSkills.length')]";
	String callFlowForSkill		= ".//tbody//td//span[text()='$$skill$$']/../..//td/div/a[contains(@href,'call')]";
	
	String removeSkillFromUser	= ".//tbody//td[text()='$$skill$$']/..//a[@class='delete']/span";
	String levelForSkill		= ".//tbody//td[text()='$$skill$$']/..//td[contains(@class,'level')]";
	String updateSkill			= ".//tbody//td[text()='$$skill$$']/..//td//button[contains(@class,'update-btn')]";
	
	By duplicateSkillMsg		= By.xpath("//*[@class='toast-message' and contains(text(),'Skill with this name already exists.')]");
	By deleteOk					 = By.cssSelector(".btn-primary[data-bb-handler='confirm']");
	
	By skillToSort				= By.cssSelector(".name a");
	By skillResult				= By.xpath(".//tbody//td[1]/span");
	By userToSort				= By.xpath(".//*[contains(@class,'userSkills')]/a");
	By userCountResult			= By.xpath(".//tbody//td[2]");
	
	By deleteSkillsBtn			= By.cssSelector(".skills a.delete span");
	
	By sortedSkillsNameHeader	= By.cssSelector(".account-tabs th.name"); 
	
	public void navigateToAccountSkillsTab(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		clickElement(driver, accountSkillsTab);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, accountSkillsTab);
	}

	public boolean addNewSkill(WebDriver driver, String skillName) {
		waitUntilVisible(driver, skillNameTextBox);
		clickElement(driver, skillNameTextBox);
		enterText(driver, skillNameTextBox, skillName);
		clickElement(driver, addSkillButton);
		if (isElementVisible(driver, duplicateSkillMsg, 2)){
			return true ;
		}
		else
			return false;
	}

	public boolean deleteOptionForSkillAvailable(WebDriver driver, String skillName) {
		By deleteGivenSkillLoc = By.xpath(deleteGivenSkill.replace("$$skill$$", skillName));
		return isElementVisible(driver, deleteGivenSkillLoc, 2);
	}

	public void deleteSkill(WebDriver driver,String skillName){
		By deleteGivenSkillLoc = By.xpath(deleteGivenSkill.replace("$$skill$$", skillName));
		clickElement(driver, deleteGivenSkillLoc);
		waitUntilVisible(driver, deleteOk);
		clickElement(driver, deleteOk);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean skillExist(WebDriver driver, String skillName) {
		if (isListElementsVisible(driver, skillList, 5)) {
			List<String> skills = getTextListFromElements(driver, skillList);
			return isTextPresentInStringList(driver, skills, skillName);
		}
		return false;
	}
	
	public boolean updateSkill(WebDriver driver, String oldSkillName, String newSkillName) {
		By updateGivenSkillLoc = By.xpath(updateGivenSkill.replace("$$skill$$", oldSkillName));
		waitUntilVisible(driver, updateGivenSkillLoc);
		clickElement(driver, updateGivenSkillLoc);
		findElement(driver,updateSkillTextBox).clear();
		findElement(driver,updateSkillTextBox).sendKeys(newSkillName);

		By saveUpdatedSkillLoc = By.xpath(saveUpdatedSkill.replace("$$newskillvalue$$", newSkillName));
		waitUntilVisible(driver, skillsHeader);
		clickElement(driver, skillsHeader);
		waitUntilVisible(driver, saveUpdatedSkillLoc);
		clickByJs(driver, saveUpdatedSkillLoc);
		if (isElementVisible(driver, duplicateSkillMsg, 3)) {
			waitUntilInvisible(driver, duplicateSkillMsg);
			return true;
		} else
			return false;
	}
	
	public boolean verifySkillExistsInCallFlow(WebDriver driver,String skillName){
		callFlowPage.removeAllCallFlowSteps(driver);
		callFlowPage.dragAndDropDialImage(driver);
		return callFlowPage.selectGroupFromDialSection(driver, CallFlowPage.DialCallRDNATOCat.Skill, skillName);
	}
	
	public List<String> getCallFlowForGivenSkill(WebDriver driver,String skillName){
		By callFlowLoc	=	By.xpath(callFlowForSkill.replace("$$skill$$", skillName));
		return getTextListFromElements(driver, callFlowLoc);
	}
	
	public boolean isCallFlowExistsForSkill(WebDriver driver, String skillName){
		By callFlowLoc	=	By.xpath(callFlowForSkill.replace("$$skill$$", skillName));
		return isElementVisible(driver, callFlowLoc, 5);
	}
	
	public void clickCallFlowForGivenSkill(WebDriver driver,String skillName){
		By callFlowLoc	=	By.xpath(callFlowForSkill.replace("$$skill$$", skillName));
		waitUntilVisible(driver, callFlowLoc);
		clickElement(driver, callFlowLoc);
	}
	
	public String getUserLength(WebDriver driver,String skillName){
		By userLengthLoc	=	By.xpath(userCountForSkill.replace("$$skill$$", skillName));
		waitUntilVisible(driver, userLengthLoc);
		return getElementsText(driver, userLengthLoc);
	}
	
	public void clickSkill(WebDriver driver){
		waitUntilVisible(driver, skillToSort);
		clickElement(driver,skillToSort);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean verifySkillNameSorted(WebDriver driver) {
		List<String> resultList = getTextListFromElements(driver, skillResult);
		boolean sorted;
		if (sorted = Ordering.natural().isOrdered(resultList)) {
			sorted = true;
			return sorted;
		} else if (sorted = Ordering.natural().reverse().isOrdered(resultList)) {
			sorted = false;
			return sorted;
		}
		return sorted;
	}
	
	public void clickNoOfUser(WebDriver driver){
		waitUntilVisible(driver, userToSort);
		clickElement(driver,userToSort);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean verifyNoOfUserSorted(WebDriver driver) {

		List<String> resultList = getTextListFromElements(driver, userCountResult);
		boolean sorted;
		if (sorted = Ordering.natural().isOrdered(resultList)) {
			sorted = true;
			return sorted;
		} else if (sorted = Ordering.natural().reverse().isOrdered(resultList)) {
			sorted = false;
			return sorted;
		}
		return sorted;
	}
	
	public void removeSkillFromUser(WebDriver driver,String skillName){
		By deleteGivenSkillLoc = By.xpath(removeSkillFromUser.replace("$$skill$$", skillName));
		clickElement(driver, deleteGivenSkillLoc);
		waitUntilVisible(driver, deleteOk);
		clickElement(driver, deleteOk);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String getSkillLevel(WebDriver driver,String skillName){
		By levelOfGivenSkill = By.xpath(levelForSkill.replace("$$skill$$", skillName));
		return getElementsText(driver, levelOfGivenSkill);
	}
	
	public String clickUpdateSkill(WebDriver driver,String skillName){
		By levelOfGivenSkill = By.xpath(updateSkill.replace("$$skill$$", skillName));
		return getElementsText(driver, levelOfGivenSkill);
	}
	
	public int getCallFlowSkillLength(WebDriver driver) {
		if (getTextListFromElements(driver, callFlowList) == null) {
			return 0;
		} else {
			return getElements(driver, callFlowList).size();
		}
	}
	
	public boolean verifyAscendingSkillsNameList(WebDriver driver) {
		waitUntilVisible(driver, sortedSkillsNameHeader);
		if (findElement(driver, sortedSkillsNameHeader).getAttribute("class").equals("sortable renderable name")) {
			clickElement(driver, sortedSkillsNameHeader);
		} else if (findElement(driver, sortedSkillsNameHeader).getAttribute("class").contains("descending")) {
			clickElement(driver, sortedSkillsNameHeader);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, sortedSkillsNameHeader);
		}

		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, sortedSkillsNameHeader).getAttribute("class").contains("ascending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, skillList);
		return HelperFunctions.verifyAscendingOrderedList(afterSortElementsList);
	}

	public boolean verifyDescendingSkillsNameList(WebDriver driver) {
		waitUntilVisible(driver, sortedSkillsNameHeader);
		if (findElement(driver, sortedSkillsNameHeader).getAttribute("class").equals("sortable renderable name")) {
			clickElement(driver, sortedSkillsNameHeader);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, sortedSkillsNameHeader);
		} else if (findElement(driver, sortedSkillsNameHeader).getAttribute("class").contains("ascending")) {
			clickElement(driver, sortedSkillsNameHeader);
		}

		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, sortedSkillsNameHeader).getAttribute("class").contains("descending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, skillList);
		return HelperFunctions.verifyDescendingOrderedList(afterSortElementsList);
	}
	
	public void deleteAllSkills(WebDriver driver) {
		if (isListElementsVisible(driver, deleteSkillsBtn, 5)) {
			int i = getElements(driver, deleteSkillsBtn).size() - 1;
			while (i >= 0) {
				clickElement(driver, getElements(driver, deleteSkillsBtn).get(i));
				waitUntilVisible(driver, deleteOk);
				clickElement(driver, deleteOk);
				dashboard.isPaceBarInvisible(driver);
				idleWait(2);
				i--;
			}
		}
	}
}