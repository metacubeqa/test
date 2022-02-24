/**
 * 
 */
package support.source.users;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class UsersPage extends SeleniumBase {

	Dashboard dashboard = new Dashboard();
	AccountIntelligentDialerTab accountDialer = new AccountIntelligentDialerTab();
	UserIntelligentDialerTab userIntelligentDialerTab = new UserIntelligentDialerTab();

	By overViewTabLink				= By.cssSelector(".user-tabs [data-tab='overview']");
	
	//Add New Users Page
	By searchNameTab				= By.cssSelector(".search-field .search-sfdc");
	By searchCityTab				= By.cssSelector(".search-field .city-sfdc");
	By searchCountryTab				= By.cssSelector(".search-field .country-sfdc");
	By searchSFDC					= By.cssSelector(".sfdc-profile-picker input");
	
	By selectAllCheckBox			= By.xpath(".//*[@class='backgrid-select-options']/ancestor::tr//input");
	By selectChckBoxList			= By.cssSelector(".selectedCell");
	By nextBtn						= By.cssSelector(".btn.next");
	By userToHeader            		= By.cssSelector("h4.modal-title");
	String checkBoxUser				= ".//*[@class='selectedCell' and @data-name='$userName$']";
	
	String userNamePage				= ".//*[text()='$$userName$$']/ancestor::tr/td[@class='sortable renderable']";
	String userNamePageAfterAdd		= ".//*[text()='$userName$']/ancestor::tr/td[contains(@class,'name')]";
	String deleteUserAfterAdd		= ".//*[text()='$userName$']/..//td//a[@class='delete']/span";
	String emailPage				= ".//*[text()='$$email$$']/ancestor::tr/td[contains(@class,'email')]";
	String cityPage					= ".//*[text()='$$city$$']/ancestor::tr/td[contains(@class,'city')]";
	String countryPage				= ".//*[text()='$$country$$']/ancestor::tr/td[contains(@class,'country')]";
	String sfdcPage					= ".//*[text()='$$sfdc$$']/ancestor::tr/td[contains(@class,'profile')]";
	String titlePage				= ".//*[text()='$$title$$']/ancestor::tr/td[contains(@class,'title')]";
	
	By logoutUserBtn				= By.xpath(".//label[text()='Presence']/..//button[contains(@class,'logout-user')]");
	By availablePresence			= By.xpath(".//label[text()='Presence']/..//span[text()='Available']");
	By offlinePresence				= By.xpath(".//label[text()='Presence']/..//span[text()='Offline']");
	By oncallPresence				= By.xpath(".//label[text()='Presence']/..//span[text()='OnCall']");
	By logoutUserMsg				= By.cssSelector(".bootbox-body");
	By confirmLogOutBtn 			= By.cssSelector("[data-bb-handler='logout']");
	By userID                       = By.xpath("//label[text()='User ID']/following-sibling::span");
	String logOutMsgPage			= "You are about to log out $userName$. Are you sure you want to log this user out of ringDNA?";

	//manage user
	By manageUserPageTitle			= By.cssSelector(".page-title h1");
	By accountLink 		 			= By.xpath("//label[text()='Account']/..//span/a");
	By licenseStatus				= By.xpath("//label[contains(text(),'License Status')]");
	By userNameTextBox 			  	= By.cssSelector(".form-control.user-name");
	By emailTextBox 				= By.cssSelector(".form-control.email");
	By selectUserStatus				= By.cssSelector(".user-status");
	By salesForceIdTextBox  		= By.cssSelector(".form-control.salesforce-id");
	By acountNameTextBox 			= By.cssSelector("input[placeholder='Account name']");
	By accountNameContent			= By.cssSelector(".account-picker .item");
	By searchButton 				= By.cssSelector(".btn-success.search");
	By usersListLinks 				= By.cssSelector(".user-search tr:not(.deleted) [href*='#users/']");
	By usersListDeletedLinks		= By.cssSelector(".user-search tr.deleted [href*='#users/']");
	By usersListAccountName         = By.cssSelector(".user-search tr:not(.deleted) [href*='#accounts/']");
	By userStatusTable				= By.cssSelector("td.userStatus");
	By userStatusHeader             = By.cssSelector("table.backgrid th.userStatus");
	By userExtensionColumn			= By.cssSelector("td.extension");
	By selectRole 					= By.cssSelector("select.role");
	By selectLocation				= By.cssSelector("select.location");
	By selectLocationList 			= By.cssSelector("select.location option");
	By extensionTextBox 			= By.cssSelector(".form-control.userExtension");
	By saveUserPageBtn 				= By.cssSelector("button.save");
	
	By saveAccountsSettingMessage  	= By.className("toast-message");
	By modalBodyParagraph			= By.xpath(".//*[@class='modal-body']/p");
	By modalBodyNumbersPara			= By.xpath(".//*[@class='modal-body']/div[@class='numbers']/p");
	By closeBtn						= By.cssSelector(".close span");
	By deleteUserBtn				= By.cssSelector(".btn-danger.delete");
	By undeleteBtn					= By.cssSelector("button.undelete");
	By confirmDeleteDangerBtn		= By.cssSelector(".btn-danger.confirm-delete");
	By confirmDeleteBtn 			= By.cssSelector("[data-bb-handler='confirm']");
	String userNameDetails			= "//*[@class='user-overview']//span[contains(text(),\"$$UserName$$\")]";
	String activeUser				= ".//*[@class='user-search']//tbody//td[contains(text(),'Active')]/..//td[contains(@class,'sfUserId') and text()='$sfUserId$']/..//td/a[contains(@href,'users')]";
	String extensionCoulumnUser		= ".//*[text()='$user$']/ancestor::tr//td[contains(@class,'extension')]";
	By downloadButton               = By.cssSelector(".download");
	
	By isDeleted                    = By.xpath("//label[text() = 'Is Deleted']");
	By isDeletedValue               = By.xpath("//label[text() = 'Is Deleted']/following-sibling::span");
	By dateDeleted                  = By.xpath("//label[text() = 'Date Deleted' or text() = 'Deleted Date']");
	By deletedBy                    = By.xpath("//label[text() = 'Deleted By']");
	By createdBy                    = By.xpath("//label[text() = 'Created By']");
	
	//Custom Greetings selectors
	By addCustomGreetingIcon		= By.cssSelector(".voicemail-greeting .glyphicon-plus-sign");
	By newRecordingLink 			= By.className("record-voicemail");
	By inputLabelTab 			   	= By.cssSelector(".voicemail-modals-container .file-label");
	By chooseFolderLink				= By.cssSelector(".voicemail-modals-container .glyphicon-folder-open");
	By uploadFilePlayIcon			= By.cssSelector(".voicemail-modals-container .glyphicon-play-circle"); 
	By outOfOfficeCheckbx			= By.cssSelector(".out-of-office");
	By saveCustomGreetingRecord		= By.cssSelector(".voicemail-modals-container  button.save-record");
	
	//team members
	By superVisorTeamName			= By.xpath(".//*[@class='user-team-container']//td[contains(text(),'Supervisor')]/..//td[contains(@class,'team.name')]");
	By addTeamIcon					= By.cssSelector(".userTeam .glyphicon-plus-sign");
	By userToTeamsAdd				= By.cssSelector(".selectize-control.selector.multi input");
	By userToTeamDropDown			= By.cssSelector(".selectize-dropdown-content .option");
	By teamInput                    = By.cssSelector(".selectize-input.items.not-full");
	By teamPickerItems				= By.cssSelector(".selectize-dropdown-content div");
	By teamNameList					= By.xpath(".//*[@class='userTeam']//td//a[contains(@href,'#teams')]");
	By saveUserTeamsBtn				= By.cssSelector(".btn-success.save-user-teams");
	By deleteTeamIcon				= By.cssSelector(".userTeam .glyphicon-remove-sign");
	String teamNamePage		    	= ".//*[text()='$teamName$']/ancestor::tr//td//a[contains(@href,'#teams')]";
	String teamDeletePage			= ".//*[@class='userTeam']//td//a[text()='$teamName$']/parent::td/following-sibling::td//a[@class='delete']/span";
    String userToTeamDelete         = ".//*[contains(@class,'has-items')]/div[text()='$$groupName$$']/a[@class='remove']";
	String userToTeamsName			= ".//span[@class='teams-picker']//div[@class='selectize-dropdown-content']//div[text()='$team$']";
    
	//Skill
	By addSkillIcon					= By.cssSelector(".add-user-skill .glyphicon-plus-sign");
	By userSkillNameList			= By.xpath(".//td[contains(@class,'skill.name')]");
	By skillDefault					= By.cssSelector(".skill-picker .items");
	By skillDefaultInput			= By.cssSelector(".skill-picker .items input");
	By skillOptions					= By.cssSelector(".skill-picker .selectize-dropdown-content .option");
	By skillLevelDefault			= By.cssSelector(".level-picker .items");
	By skillLevelInput				= By.cssSelector(".level-picker .items input");
	By skillLevelOptions			= By.cssSelector(".level-picker .selectize-dropdown-content .option");	
	By saveUserSkillButton			= By.cssSelector(".save-user-skill");
	By userSkillsSpan				= By.cssSelector(".user-skill-container td span");
	By removeSkillsList				= By.cssSelector(".userSkill .delete span");
	
	String removeSkillFromUser		= ".//tbody//td[text()='$$skill$$']/..//a[@class='delete']/span";
	By deleteOk						= By.cssSelector(".btn-primary[data-bb-handler='confirm']");
	String levelForSkill			= ".//tbody//td[text()='$$skill$$']/..//td[contains(@class,'level')]";
	String updateSkill				= ".//tbody//td[text()='$$skill$$']/..//td//button[contains(@class,'update-btn')]";
	String userSkillPage			= ".//td[contains(@class,'skill.name') and text()='$skillName$']";
	
	String confirmDeletionNumberCheckBox	= ".//*[@id='confirm-deletion-modal']//span//a[@data-phone='$number$ -']/parent::span/preceding-sibling::input[1]";
	
	
	//License locators
	
	//Conversation AI Toggles
	public static By conversationAnalyticsCheckBox			= By.cssSelector(".conversationAnalytics");
	By conversationAnalyticsBtnToggleOff		= By.xpath(".//input[@class= 'conversationAnalytics toggle-switch']/parent::div//*[contains(@class,'toggle-off')]");
	By conversationAnalyticsBtnToggleOn			= By.xpath(".//input[@class= 'conversationAnalytics toggle-switch']/parent::div//*[contains(@class,'toggle-on')]");
	By convAIManagerCheckBox					= By.cssSelector(".conversationAnalyticsMgr");
	By convAIManagerBtnToggleOff				= By.xpath(".//input[contains(@class, 'conversationAnalyticsMgr')]/parent::div//*[contains(@class,'toggle-off')]");
	
	By caiLocationDisplayCallCheckBox			= By.cssSelector(".conversationAnalyticsLoc");
	By caiLocationDisplayCallBtnToggleOff		= By.xpath(".//input[@class= 'conversationAnalyticsLoc toggle-switch']/parent::div//*[contains(@class,'toggle-off')]");
	By caiLocationDisplayCallBtnToggleOn		= By.xpath(".//input[@class= 'conversationAnalyticsLoc toggle-switch']/parent::div//*[contains(@class,'toggle-on')]");
	
	By guidedSellingCheckBox			        = By.cssSelector(".sequence.toggle-switch");
	By guidedSellingBtnToggleOff				= By.xpath(".//input[@class= 'sequence toggle-switch']/parent::div//*[contains(@class,'toggle-off')]");
	By guidedSellingBtnToggleOn					= By.xpath(".//input[@class= 'sequence toggle-switch']/parent::div//*[contains(@class,'toggle-on')]");
	
	
	public enum UserRole {
		Agent, Support, Admin
	};
	
	public enum SkillLevel {
		Level1("Level 1"),
		Level2("Level 2"),
		Level3("Level 3"),
		Level4("Level 4"),
		Level5("Level 5");
	
	    private String displayName;

	    SkillLevel(String displayName) {
	        this.displayName = displayName;
	    }
		    
	    public String displayName() { return displayName; }

	    @Override 
	    public String toString() { return displayName; }

	};
	
	public enum UserStatus{
		All("All Users"),
		Active("Active"),
		Deleted("Deleted");
	
	    private String displayName;

	    UserStatus(String displayName) {
	        this.displayName = displayName;
	    }
		    
	    public String displayName() { return displayName; }

	    @Override 
	    public String toString() { return displayName; }
	}


	public void openOverViewTab(WebDriver driver){
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, overViewTabLink);
		clickByJs(driver, overViewTabLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isOverViewTabVisible(WebDriver driver){
		dashboard.isPaceBarInvisible(driver);
		return isElementVisible(driver, overViewTabLink, 5);
	}
	
	public String getUserAccountName(WebDriver driver){
		waitUntilVisible(driver, accountLink);
		return getElementsText(driver, accountLink);
	}
	
	public String getUserID(WebDriver driver){
		waitUntilVisible(driver, userID);
		return getElementsText(driver, userID);
	}
	
	public void clickAccountLink(WebDriver driver) {
		waitUntilVisible(driver, accountLink);
		clickElement(driver, accountLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isLicenseStatusVisible(WebDriver driver){
		return isElementVisible(driver, licenseStatus, 5);
	}
	
	public void verifyManageUsersPageTitle(WebDriver driver){
		waitUntilVisible(driver, manageUserPageTitle);
		assertEquals(getElementsText(driver, manageUserPageTitle), "Users");
	}
	
	public void enterUserName(WebDriver driver, String userName) {
		idleWait(1);
		waitUntilVisible(driver, userNameTextBox);
		enterText(driver, userNameTextBox, userName);
	}

	public void enterEmail(WebDriver driver, String email) {
		waitUntilVisible(driver, emailTextBox);
		enterText(driver, emailTextBox, email);
	}
	
	public void enterSalesForceId(WebDriver driver, String salesForceId) {
		waitUntilVisible(driver, salesForceIdTextBox);
		enterText(driver, salesForceIdTextBox, salesForceId);
	}

	public String getAccountNameText(WebDriver driver){
		waitUntilVisible(driver, accountNameContent);
		return getElementsText(driver, accountNameContent);
	}
	
	public String getDefaltValueUserStatus(WebDriver driver){
		return getSelectedValueFromDropdown(driver, selectUserStatus);
	}
	
	public void selectUserStatus(WebDriver driver, Enum<?> status){
		selectFromDropdown(driver, selectUserStatus, SelectTypes.visibleText, status.toString());
	}
	
	public boolean verifyUserStatusTableContains(WebDriver driver, Enum<?> status ){
		if(getTextListFromElements(driver, userStatusTable) == null || getTextListFromElements(driver, userStatusTable).isEmpty()){
			return true;
		}
		else{
			return isTextPresentInList(driver, getElements(driver, userStatusTable), status.toString());
		}
	}
	
	public void clickSearchButton(WebDriver driver) {
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
	}

	public void clickFirstUserAccountLink(WebDriver driver) {
		getElements(driver, usersListLinks).get(0).click();
	}
	
	public void clickFirstDeletedUserAccountLink(WebDriver driver){
		waitUntilVisible(driver, userStatusHeader);
		clickElement(driver, userStatusHeader);
		clickElement(driver, userStatusHeader);
		if(isListElementsVisible(driver, usersListDeletedLinks, 5)){
			getElements(driver, usersListDeletedLinks).get(0).click();
		}
	}

	public void OpenUsersSettings(WebDriver driver, String userName, String email) {
		enterUserName(driver, userName);
		enterEmail(driver, email);
		clickSearchButton(driver);
		clickFirstUserAccountLink(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public void searchUserWithSalesForceId(WebDriver driver, String userName, String email, String salesForceId){
		enterUserName(driver, userName);
		enterEmail(driver, email);
		enterSalesForceId(driver, salesForceId);
		clickSearchButton(driver);
	}
	
	public void OpenUsersSettingsWithSalesForceId(WebDriver driver, String userName, String email, String salesForceId) {
		searchUserWithSalesForceId(driver, userName, email, salesForceId);
		clickFirstUserAccountLink(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void OpenDeletedUsersSettingsWithSalesForceId(WebDriver driver, String userName, String email, String salesForceId) {
		searchUserWithSalesForceId(driver, userName, email, salesForceId);
		clickFirstDeletedUserAccountLink(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public String getCurrentUserRole(WebDriver driver){
		return getSelectedValueFromDropdown(driver, selectRole);
	}
	
	public void selectRole(WebDriver driver, UserRole role) {
		waitUntilVisible(driver, selectRole);
		scrollToElement(driver, selectRole);
		Select select = new Select(findElement(driver, selectRole));
		if (role == UserRole.Agent) {
			select.selectByValue("Agent");
		}
		if (role == UserRole.Admin) {
			select.selectByValue("Admin");
		}
		if (role == UserRole.Support) {
			select.selectByValue("Support");
		}
	}
	
	public void verifyUserSavedMsg(WebDriver driver){
		savedetails(driver);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "User saved.");
		userIntelligentDialerTab.isSaveAccountsSettingMessageDisappeared(driver);
	}

	public void selectLocation(WebDriver driver, String location){
		selectFromDropdown(driver, selectLocation, SelectTypes.visibleText, location);
	}
	
	public String getDefaultLocation(WebDriver driver) {
		waitUntilVisible(driver, selectLocation);
		return getSelectedValueFromDropdown(driver, selectLocation);
	}
	
	public boolean isLocationPresentOnUserDetails(WebDriver driver, String locationName) {
		return isTextPresentInList(driver, getElements(driver, selectLocationList), locationName);
	}
	
	public boolean  verifySortedLocationList(WebDriver driver) {
		List<String> dropDownList = getTextListFromElements(driver, selectLocationList);
		dropDownList.remove(0);
		return HelperFunctions.verifyListAscendingCaseInsensitive(dropDownList);
	}
	
	// This method is to save users overview page
	public void saveUserOverviewPage(WebDriver driver) {
		userIntelligentDialerTab.saveAcccountSettings(driver);
	}

	public boolean isUserNameVisible(WebDriver driver, String userName) {
		By userNameLoc = By.xpath(userNameDetails.replace("$$UserName$$", userName));
		return isElementVisible(driver, userNameLoc, 5);
	}

	public boolean isExtensionListCoulumnVisible(WebDriver driver){
		return isListElementsVisible(driver, userExtensionColumn, 5);
	}
	
	public String getExtensionColumnnValue(WebDriver driver, String userName){
		By userExtensionLoc = By.xpath(userNameDetails.replace("$$user$$", userName));
		return getElementsText(driver, userExtensionLoc);
	}
	
	public void addExtension(WebDriver driver, String extensionName) {
		waitUntilVisible(driver, extensionTextBox);
		enterText(driver, extensionTextBox, extensionName);
	}

	public void savedetails(WebDriver driver) {
		waitUntilVisible(driver, saveUserPageBtn);
		clickElement(driver, saveUserPageBtn);
	}

	public void createExtension(WebDriver driver, String extensionName) {
		addExtension(driver, extensionName);
		savedetails(driver);
		waitUntilInvisible(driver, saveAccountsSettingMessage);
		dashboard.isPaceBarInvisible(driver);
	}

	public String getExtensionUpdatedValue(WebDriver driver) {
		waitUntilVisible(driver, extensionTextBox);
		return getAttribue(driver, extensionTextBox, ElementAttributes.value);
	}
	
	public void clickCloseBtn(WebDriver driver) {
		waitUntilVisible(driver, closeBtn);
		clickElement(driver, closeBtn);
	}
	
	// Teams section
	public boolean isAddTeamIconVisible(WebDriver driver){
		return isElementVisible(driver, addTeamIcon, 2);
	}
	
	public void clickAddTeamIcon(WebDriver driver) {
		waitUntilVisible(driver, addTeamIcon);
		scrollToElement(driver, addTeamIcon);
		clickElement(driver, addTeamIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, userToTeamsAdd);
	}
	
	public void clickConfirmDeleteBtn(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteBtn);
		clickElement(driver, confirmDeleteBtn);
		waitUntilInvisible(driver, confirmDeleteBtn);
	}
	
	public void deleteAllTeams(WebDriver driver){
		if(isListElementsVisible(driver, deleteTeamIcon, 5)){
			int i = getElements(driver, deleteTeamIcon).size() - 1;
			while (i >= 0) {
				// delete field
				scrollTillEndOfPage(driver);
				clickElement(driver, getElements(driver, deleteTeamIcon).get(i));
				clickConfirmDeleteBtn(driver);
				dashboard.isPaceBarInvisible(driver);
				idleWait(1);
				i--;
			}
			savedetails(driver);
		}
	}
	
	public void clickTeamNameLink(WebDriver driver, String teamName){
		scrollToElement(driver, addTeamIcon);
		By teamLoc = By.xpath(teamNamePage.replace("$teamName$", teamName));
		waitUntilVisible(driver, teamLoc);
		clickElement(driver, teamLoc);
	}
	
	public boolean isTeamExists(WebDriver driver, String teamName) {
		scrollToElement(driver, addTeamIcon);
		By teamLoc = By.xpath(teamNamePage.replace("$teamName$", teamName));
		if(isElementVisible(driver, teamLoc, 5)){
			return true;
		}
		return false;
	}
	
	public List<String> getTeamNamesListOnAddUserToTeam(WebDriver driver) {
		waitUntilVisible(driver, teamInput);
		clickElement(driver, teamInput);
		return getTextListFromElements(driver, teamPickerItems);
	}

	public List<String> getTeamNamesList(WebDriver driver) {
		return getTextListFromElements(driver, teamNameList);
	}
	
	public boolean verifyUserToTeamSectionContainsTeam(WebDriver driver, String teamName) {
		boolean result = false;
		waitUntilVisible(driver, userToTeamsAdd);
		clickElement(driver, userToTeamsAdd);
		By userToTeamLoc = By.xpath(userToTeamsName.replace("$team$", teamName));
		if(isElementVisible(driver, userToTeamLoc, 5)){
			result = true;
		}
		clickCloseBtn(driver);
		return result;
	}

	public void selectUserFromTeam(WebDriver driver, String teamName){
		clickAndSelectFromDropDown(driver, userToTeamsAdd, userToTeamDropDown, teamName);
		waitUntilVisible(driver, userToHeader);
		clickElement(driver, userToHeader);
	}

	public void clickSaveUserTeamBtn(WebDriver driver){
		waitUntilVisible(driver, saveUserTeamsBtn);
		clickElement(driver, saveUserTeamsBtn);
		waitUntilInvisible(driver, saveUserTeamsBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void addUserToTeams(WebDriver driver, String teamName) {
		selectUserFromTeam(driver, teamName);
		clickSaveUserTeamBtn(driver);
	}
	
	public void deleteTeamMember(WebDriver driver, String teamName) {
		idleWait(1);
		By teamLoc = By.xpath(teamDeletePage.replace("$teamName$", teamName));
		waitUntilVisible(driver, teamLoc);
		clickElement(driver, teamLoc);
		clickConfirmDeleteBtn(driver);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void removeUserToTeam(WebDriver driver, String teamName) {
		By userToTeamLoc = By.xpath(userToTeamDelete.replace("$$groupName$$", teamName));
		waitUntilVisible(driver, userToTeamLoc);
		clickElement(driver, userToTeamLoc);
		assertFalse(isElementVisible(driver, userToTeamLoc, 5));
		clickElement(driver, userToHeader);
		waitUntilVisible(driver, saveUserTeamsBtn);
		clickElement(driver, saveUserTeamsBtn);
		waitUntilInvisible(driver, saveUserTeamsBtn);
	}

	public String getSuperVisorTeamName(WebDriver driver){
		if(isListElementsVisible(driver, superVisorTeamName, 5)){
			return getElementsText(driver, getElements(driver, superVisorTeamName).get(0));
		}
		else{
			return null;
		}
	}
	
	
	
	//Add New Users Section
	public void searchUserInAddNewUser(WebDriver driver, String userName) {
		waitUntilVisible(driver, searchNameTab);
		enterText(driver, searchNameTab, userName);
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By userNameLoc = By.xpath(userNamePage.replace("$$userName$$", userName));
		assertTrue(isListElementsVisible(driver, userNameLoc, 10));
	}
	
	public void searchCityInAddNewUser(WebDriver driver, String city) {
		waitUntilVisible(driver, searchCityTab);
		enterText(driver, searchCityTab, city);
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By cityNameLoc = By.xpath(cityPage.replace("$$city$$", city));
		assertTrue(isListElementsVisible(driver, cityNameLoc, 10));
	}
	
	public void searchCountryInAddNewUser(WebDriver driver, String country) {
		waitUntilVisible(driver, searchCountryTab);
		enterText(driver, searchCountryTab, country);
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By countryNameLoc = By.xpath(countryPage.replace("$$country$$", country));
		assertTrue(isListElementsVisible(driver, countryNameLoc, 10));
	}

	public void searchSFDCInAddNewUser(WebDriver driver, String sfdc){
		waitUntilVisible(driver, searchSFDC);
		enterText(driver, searchSFDC, sfdc);
		clickEnter(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By sfdcLoc = By.xpath(sfdcPage.replace("$$sfdc$$", sfdc));
		assertTrue(isListElementsVisible(driver, sfdcLoc, 10));
	}
	
	public void searchEmailInAddNewUser(WebDriver driver, String email) {
		waitUntilVisible(driver, searchNameTab);
		enterText(driver, searchNameTab, email);
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By emailLoc = By.xpath(emailPage.replace("$$email$$", email));
		assertTrue(isListElementsVisible(driver, emailLoc, 10));
	}
	
	public void searchTitleInAddNewUser(WebDriver driver, String title) {
		waitUntilVisible(driver, searchNameTab);
		enterText(driver, searchNameTab, title);
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
		dashboard.isPaceBarInvisible(driver);
		By titleLoc = By.xpath(titlePage.replace("$$title$$", title));
		assertTrue(isListElementsVisible(driver, titleLoc, 10));
	}
	
	public boolean isUserVisibleAfterAdd(WebDriver driver, String userName){
		By userVisibleLoc = By.xpath(userNamePageAfterAdd.replace("$userName$", userName));
		return isElementVisible(driver, userVisibleLoc, 5);
	}
	
	public void deleteUserAfterAdd(WebDriver driver, String userName){
		By deleteUserLoc = By.xpath(deleteUserAfterAdd.replace("$userName$", userName));
		clickElement(driver, deleteUserLoc);
		clickConfirmDeleteBtn(driver);
	}
	
	public void selectAllCheckBox(WebDriver driver) {
		waitUntilVisible(driver, selectAllCheckBox);
		clickElement(driver, selectAllCheckBox);
	}
	
	public boolean isCheckBoxVisibleForUser(WebDriver driver, String userName){
		By checkBoxUserLoc = By.xpath(checkBoxUser.replace("$userName$", userName));
		return isElementVisible(driver, checkBoxUserLoc, 5);
	}
	
	public void clickCheckBoxVisibleForUser(WebDriver driver, String userName){
		By checkBoxUserLoc = By.xpath(checkBoxUser.replace("$userName$", userName));
		clickElement(driver, checkBoxUserLoc);
	}
	
	public void isAllUsersSelected(WebDriver driver) {
		selectAllCheckBox(driver);
		List<WebElement> checkBoxList = getElements(driver, selectChckBoxList);
		for(WebElement checkBoxElement:checkBoxList) {
			assertTrue(checkBoxElement.isSelected(), "CheckBox not selected");
		}
		assertTrue(isElementVisible(driver, nextBtn, 6));
	}
	
	public boolean isNextBtnDisabled(WebDriver driver) {
		return isElementDisabled(driver, nextBtn, 5);
	}
	
	public void clickNextBtnForAddNewUser(WebDriver driver){
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, nextBtn);
		clickElement(driver, nextBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isUserActive(WebDriver driver, String sfUserId) {
		By activeUserLoc = By.xpath(activeUser.replace("$sfUserId$", sfUserId));
		return isElementVisible(driver, activeUserLoc, 5);
	}
	
	public void selectActiveUser(WebDriver driver, String sfUserId) {
		By activeUserLoc = By.xpath(activeUser.replace("$sfUserId$", sfUserId));
		clickElement(driver, activeUserLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickConfirmDeleteDangerBtn(WebDriver driver) {
		waitUntilVisible(driver, confirmDeleteDangerBtn);
		clickElement(driver, confirmDeleteDangerBtn);
		waitUntilInvisible(driver, confirmDeleteDangerBtn);
	}
	
	public void deleteUser(WebDriver driver, String userName) {
		waitUntilVisible(driver, deleteUserBtn);
		clickElement(driver, deleteUserBtn);
		clickConfirmDeleteDangerBtn(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), String.format("The user %s has been deleted.", userName));
		waitUntilInvisible(driver, saveAccountsSettingMessage);
		waitUntilInvisible(driver, deleteUserBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteUserWithSmartNumber(WebDriver driver, String userName, String defaultNumber, String additionalNumber) {
		waitUntilVisible(driver, deleteUserBtn);
		clickElement(driver, deleteUserBtn);
		waitUntilVisible(driver, modalBodyParagraph);
		assertEquals(getElementsText(driver, modalBodyParagraph), "You are about to delete the ringDNA user: ".concat(userName));
		assertEquals(getElementsText(driver, modalBodyNumbersPara), "The following smart number(s) are currently assigned to this user. Select any smart number(s) to be deleted as well.");
		deleteSmartNumberWhenDeleteUser(driver, defaultNumber);
		deleteSmartNumberWhenDeleteUser(driver, additionalNumber);
		
		//verifying delete color btn is red
		String bckgroundColor = getCssValue(driver, findElement(driver, confirmDeleteDangerBtn), CssValues.BackgroundColor);
		String bckgroundHexColor = Color.fromString(bckgroundColor).asHex();
		assertEquals(bckgroundHexColor, "#cc0000", "color is not red");
		
		clickConfirmDeleteDangerBtn(driver);
		waitUntilVisible(driver, saveAccountsSettingMessage);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), String.format("The user %s and smart numbers %s,%s, have been deleted.", userName,additionalNumber,defaultNumber));
		waitUntilInvisible(driver, deleteUserBtn);
		waitUntilVisible(driver, undeleteBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void deleteSmartNumberWhenDeleteUser(WebDriver driver, String smartNumber){
		By deleteSmartNumberLoc = By.xpath(confirmDeletionNumberCheckBox.replace("$number$", smartNumber));
		waitUntilVisible(driver, deleteSmartNumberLoc);
		checkCheckBox(driver, deleteSmartNumberLoc);
	}
	
	public boolean isUnDeleteBtnVisible(WebDriver driver){
		return isElementVisible(driver, undeleteBtn, 5);
	}
	
	public void clickUnDeleteBtn(WebDriver driver){
		waitUntilVisible(driver, undeleteBtn);
		clickElement(driver, undeleteBtn);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, deleteUserBtn);
		waitUntilInvisible(driver, undeleteBtn);
	}
	
	public boolean isLogOutUserBtnVisible(WebDriver driver){
		return isElementVisible(driver, logoutUserBtn, 5);
	}
	
	public boolean isLogOutUserBtnDisabled(WebDriver driver){
		return isElementDisabled(driver, logoutUserBtn, 5);
	}
	
	public boolean isOnCallPresenceVisible(WebDriver driver){
		return isElementVisible(driver, oncallPresence, 5);
	}
	
	public void clickLogOutUserBtn(WebDriver driver, String userName){
		verifyBeforeLogOutDetails(driver);
		waitUntilVisible(driver, logoutUserBtn);
		clickElement(driver, logoutUserBtn);
		verifyTextOnConfirmLogOut(driver, userName);
		clickConfirmLogOutBtn(driver);
		verifyAfterLogOutDetails(driver);
	}

	public void verifyBeforeLogOutDetails(WebDriver driver) {
		assertFalse(isElementVisible(driver, offlinePresence, 5));
		assertTrue(isElementVisible(driver, availablePresence, 5));
		assertTrue(isElementVisible(driver, logoutUserBtn, 5));
	}

	public void verifyAfterLogOutDetails(WebDriver driver) {
		assertFalse(isElementVisible(driver, availablePresence, 5));
		assertTrue(isElementVisible(driver, offlinePresence, 5));
		assertTrue(isLogOutUserBtnDisabled(driver));
	}
	
	public void clickConfirmLogOutBtn(WebDriver driver){
		waitUntilVisible(driver, confirmLogOutBtn);
		clickElement(driver, confirmLogOutBtn);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "User logged out successfully.");
		waitUntilInvisible(driver, saveAccountsSettingMessage);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyTextOnConfirmLogOut(WebDriver driver, String userName){
		waitUntilVisible(driver, confirmLogOutBtn);
		assertEquals(getElementsText(driver, logoutUserMsg), logOutMsgPage.replace("$userName$", userName));
	}
	
	public void addSkill( WebDriver driver, String skillName, String skillLevel){
		if(Strings.isNullOrEmpty(skillLevel)){
			skillLevel = "Level 4";
		}
		enterTextAndSelectFromDropDown(driver, skillDefault, skillDefaultInput, skillOptions, skillName);
		enterTextAndSelectFromDropDown(driver, skillLevelDefault, skillLevelInput, skillLevelOptions, skillLevel);
		waitUntilVisible(driver, saveUserSkillButton);
		clickElement(driver, saveUserSkillButton);
	}
	
	public void updateSkillLevel(WebDriver driver,String skillLevel){
		enterTextAndSelectFromDropDown(driver, skillLevelDefault, skillLevelInput, skillLevelOptions, skillLevel);
		clickElement(driver, saveUserSkillButton);
		waitUntilVisible(driver, addSkillIcon);
		idleWait(2);
	}
	
	public String getFirstSkillName(WebDriver driver){
		waitUntilVisible(driver, userSkillNameList);
		return getTextListFromElements(driver, userSkillNameList).get(0);
	}
	
	public boolean isGivenSkillExists(WebDriver driver,String skillName){
		waitUntilVisible(driver, skillDefault);
		clickElement(driver, skillDefault);
		List <String> listOfSkills = getTextListFromElements(driver, skillOptions);
		return isTextPresentInStringList(driver, listOfSkills, skillName);
	}
	
	public void clickToAddSkill(WebDriver driver){
		waitUntilVisible(driver, addSkillIcon);
		scrollToElement(driver, addSkillIcon);
		clickByJs(driver, addSkillIcon);
		waitUntilVisible(driver, skillDefault);
	}
	
	public void removeSkillFromUser(WebDriver driver,String skillName){
		By deleteGivenSkillLoc = By.xpath(removeSkillFromUser.replace("$$skill$$", skillName));
		clickElement(driver, deleteGivenSkillLoc);
		waitUntilVisible(driver, deleteOk);
		clickElement(driver, deleteOk);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void removeAllSkillsIfExists(WebDriver driver) {
		while(isElementVisible(driver, removeSkillsList, 0)){
			scrollTillEndOfPage(driver);
			clickElement(driver, removeSkillsList);
			waitUntilVisible(driver, deleteOk);
			clickElement(driver, deleteOk);
			dashboard.isPaceBarInvisible(driver);
		};
	}
	
	public boolean isUserSkillPresent(WebDriver driver, String skillName){
		scrollToElement(driver, addSkillIcon);
		By userSkillNameLoc = By.xpath(userSkillPage.replace("$skillName$", skillName));
		return isElementVisible(driver, userSkillNameLoc, 5);
	}
	
	public String getSkillLevel(WebDriver driver,String skillName){
		By levelOfGivenSkill = By.xpath(levelForSkill.replace("$$skill$$", skillName));
		waitUntilVisible(driver, levelOfGivenSkill);
		scrollToElement(driver, levelOfGivenSkill);
		return getElementsText(driver, levelOfGivenSkill);
	}
	
	public void clickUpdateSkill(WebDriver driver,String skillName){
		By levelOfGivenSkill = By.xpath(updateSkill.replace("$$skill$$", skillName));
		waitUntilVisible(driver, levelOfGivenSkill);
		scrollToElement(driver, levelOfGivenSkill);
		clickElement(driver, levelOfGivenSkill);
	}
	
	public boolean isAddUserSkillsIconVisible(WebDriver driver){
		return isElementVisible(driver, addSkillIcon, 3);
	}

	public boolean isAddUserSkillsIconDisabled(WebDriver driver){
		scrollToElement(driver, addSkillIcon);
		WebElement addSkillsParent = findElement(driver, addSkillIcon).findElement(By.xpath(".."));
		return getAttribue(driver, addSkillsParent, ElementAttributes.Class).contains("disabled");
	}
	
	public void verifyDuplicateSkillMsg(WebDriver driver, String skillName){
		clickToAddSkill(driver);
		addSkill(driver, skillName, null);
		assertEquals(getElementsText(driver, saveAccountsSettingMessage), "User already associated with this skill.");
		waitUntilInvisible(driver, saveAccountsSettingMessage);
		clickCloseBtn(driver);
		dashboard.isPaceBarInvisible(driver);
	}

	public boolean isNoUserSKillsTextVisible(WebDriver driver){
		return getElementsText(driver, userSkillsSpan).equals("No User Skills Found.");
	}
	
	public List<WebElement> getAllUserAccountLink(WebDriver driver) {
		return getElements(driver, usersListLinks);
	}
	
	public boolean isDownloadUsersOptionVisible(WebDriver driver) {
		return isElementVisible(driver, downloadButton, 6);
	}

	public List<String> getAllAccountNameFromList(WebDriver driver){
		return getTextListFromElements(driver, usersListAccountName);
	}
	
	/**
	 * @param driver
	 * @return is Deleted Visible
	 */
	public boolean isDeletedVisible(WebDriver driver) {
		return isElementVisible(driver, isDeleted, 5);
	}
	
	/**
	 * @param driver
	 * @return is Date Deleted Visible
	 */
	public boolean isDateDeletedVisible(WebDriver driver) {
		return isElementVisible(driver, dateDeleted, 5);
	}
	
	/**
	 * @param driver
	 * @return is Deleted By Visible
	 */
	public boolean isDeletedByVisible(WebDriver driver) {
		return isElementVisible(driver, deletedBy, 5);
	}
	
	/**
	 * @param driver
	 * @return is Created By Visible
	 */
	public boolean isCreatedByVisible(WebDriver driver) {
		return isElementVisible(driver, createdBy, 5);
	}
	
	/**
	 * @param driver
	 * @return get the value of is deleted true or false
	 */
	public boolean getisDeletedValue(WebDriver driver) {
		boolean answer = false;
		String text = getElementsText(driver, isDeletedValue);
		if (text.equals("true")) {
			answer = true;
		}

		if (text.equals("false")) {
			answer = false;
		}

		return answer;
	}
  
	//License enable methods
	public void disableConversationAnalyticsBtn(WebDriver driver){
		if(findElement(driver, conversationAnalyticsCheckBox).isSelected()) {
			System.out.println("Checking Conversation Analytics Setting checkbox");
			scrollToElement(driver, conversationAnalyticsBtnToggleOn);
			clickElement(driver, conversationAnalyticsBtnToggleOn);			
		}else {
			System.out.println("Conversation Analytics Setting already disable");
		} 
	}
	
	public void enableConversationAnalyticsBtn(WebDriver driver){
		if(!findElement(driver, conversationAnalyticsCheckBox).isSelected()) {
			System.out.println("Checking Conversation Analytics Setting checkbox");
			scrollToElement(driver, conversationAnalyticsBtnToggleOff);
			clickElement(driver, conversationAnalyticsBtnToggleOff);			
		}else {
			System.out.println("Conversation Analytics Setting already enabled");
		} 
	}
	
	public void disableConversationAIManagerBtn(WebDriver driver){
		scrollToElement(driver, convAIManagerBtnToggleOff);
		if(findElement(driver, convAIManagerCheckBox).isSelected()) {
			System.out.println("Checking Conversation AI Manager Setting checkbox");
			clickElement(driver, convAIManagerBtnToggleOff);			
		}else {
			System.out.println("Conversation AI Manager Setting already disable");
		} 
	}
	
	public void enableConversationAIManagerBtn(WebDriver driver){
		scrollToElement(driver, convAIManagerBtnToggleOff);
		if(!findElement(driver, convAIManagerCheckBox).isSelected()) {
			System.out.println("Checking Conversation AI Manager Setting checkbox");
			clickElement(driver, convAIManagerBtnToggleOff);			
		}else {
			System.out.println("Conversation AI Manager Setting already enable");
		} 
	}

	public void disableCAILocationUserDisplayCallsBtn(WebDriver driver){
		if(findElement(driver, caiLocationDisplayCallCheckBox).isSelected()) {
			System.out.println("Checking CAI Location Setting checkbox");
			scrollToElement(driver, caiLocationDisplayCallBtnToggleOn);
			clickElement(driver, caiLocationDisplayCallBtnToggleOn);			
		}else {
			System.out.println("CAI Location Setting already disable");
		} 
	}
	
	public void enableCAILocationUserDisplayCallsBtn(WebDriver driver){
		if(!findElement(driver, caiLocationDisplayCallCheckBox).isSelected()) {
			System.out.println("Checking CAI Location Setting checkbox");
			scrollToElement(driver, caiLocationDisplayCallBtnToggleOff);
			clickElement(driver, caiLocationDisplayCallBtnToggleOff);			
		}else {
			System.out.println("CAI Location Setting already enabled");
		} 
	}
	
	public void enableGuidedSellingToggleBtn(WebDriver driver){
		if(!findElement(driver, guidedSellingCheckBox).isSelected()) {
			System.out.println("Checking Guided Selling checkbox");
			scrollToElement(driver, guidedSellingBtnToggleOff);
			clickElement(driver, guidedSellingBtnToggleOff);			
		}else {
			System.out.println("Guided Selling Setting already enabled");
		} 
	}
	
	public void disableGuidedSellingToggleBtn(WebDriver driver){
		if(!findElement(driver, guidedSellingCheckBox).isSelected()) {
			System.out.println("Checking Guided Selling checkbox");
			scrollToElement(driver, guidedSellingBtnToggleOn);
			clickElement(driver, guidedSellingBtnToggleOn);			
		}else {
			System.out.println("Guided Selling already disabled");
		} 
	}
}
