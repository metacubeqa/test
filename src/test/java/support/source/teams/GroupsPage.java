package support.source.teams;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.accounts.AccountIntelligentDialerTab.dispositionReqStates;
import support.source.admin.AccountCallRecordingTab;
import support.source.admin.AccountCallRecordingTab.CallRecordingOverrideOptions;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

public class GroupsPage extends SeleniumBase{

	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	Dashboard dashboard = new Dashboard();
	
	By teamDetailsHeader	= By.xpath("//*[@class='team-overview']//h2[text()='Team Details']");
	By createCallQueueLink	= By.className("create-call-queue");
	By groupNavLink 		= By.cssSelector("a[data-target='#nav-Teams']");
	By groupsManageLink		= By.xpath("//a[@href='#teams'][text()='Manage']");
	By groupAddNewLink		= By.xpath("//a[@href='#team/add-new'][text()='Add New']");
	By groupNameInputField  = By.cssSelector("input.team-name");
	By groupPageHeading		= By.xpath("//h1[text()='Teams']");
	By searchButton			= By.cssSelector("button.search");
	By notDeletedGroupNames = By.xpath("//ancestor::tr[not(contains(@class,'deleted'))]//td//a[contains(@href,'#teams/')]");
	By searchGroupsNames	= By.xpath("//td//a[contains(@href,'#teams/')]");
	By searchAccountsNames	= By.xpath("//td//a[contains(@href,'#accounts/')]");
	By searchedDescription	= By.xpath("//td[contains(@class,'description')]");
	By searchedCreatedAt	= By.xpath("//td[contains(@class,'createdAt')]");
	By searchedUpdatedAt	= By.xpath("//td[contains(@class,'updatedAt')]");
	By searchedID			= By.xpath("//td[contains(@class,'id')]");
	
	By groupDeleteBtn		= By.cssSelector(".btn-danger.delete");
	By groupRestoreBtn		= By.cssSelector(".btn-success.restore");
	By groupDeleteConfirmation = By.cssSelector("[data-bb-handler='ok']");
	String teamNameSearchPage  = ".//a[contains(@href,'#teams/') and text()='$teamName$']";
	
	//headers notifications
	By nameHeader			= By.xpath(".//*[@class='team-search']//th[contains(@class,'name')]");
	By accountHeader		= By.xpath(".//*[@class='team-search']//th[contains(@class,'account.name')]");
	By descriptionHeader	= By.xpath(".//*[@class='team-search']//th[contains(@class,'description')]");
	By createdDateHeader	= By.xpath(".//*[@class='team-search']//th[contains(@class,'createdAt')]");
	By modifiedDateHeader	= By.xpath(".//*[@class='team-search']//th[contains(@class,'updatedAt')]");
	By idHeader				= By.xpath(".//*[@class='team-search']//th[contains(@class,'id')]");
	
	By callReportNotificationInput 		= By.cssSelector(".mailing-users-picker input");
	By callReportNotificationDropdown 	= By.cssSelector(".mailing-users-picker .selectize-dropdown-content div");
	By callReportNotificationItems		= By.cssSelector(".mailing-users-picker .has-items .item");
	By callReportMailInput				= By.cssSelector(".mailing-emails-picker input");
	By callReportMailDropdown			= By.cssSelector(".mailing-emails-picker .create.active");
	By callReportMailItems				= By.cssSelector(".mailing-emails-picker .has-items .item");
	
	String inboundDisposition	= ".//td[contains(@class,'disposition.disposition') and text()='$disposition$']/following-sibling::td[1]/input";
	String outboundDisposition	= ".//td[contains(@class,'disposition.disposition') and text()='$disposition$']/following-sibling::td[2]/input";
	
	//Add New Groups Page
	By groupDetailNameTab		= By.cssSelector(".team-overview .name[type='text']");
	By groupDetailDescripTab	= By.cssSelector(".team-overview .description[type='text']");
	By cloneTeamLink			= By.cssSelector(".clone-team");
	By cloneLink				= By.cssSelector(".clone-call-queue");
	By titleHeaders				= By.cssSelector(".x-title h2");

	//group override settings
	By callRecordingUnlockedImg 			= By.cssSelector(".changed-setting img[src=\"images/locked-setting/unlock-active.svg\"]");
	By callRecordingLockedImg 				= By.cssSelector(".changed-setting img[src=\"images/locked-setting/lock-active.svg\"]");
	By lockConfirmBtn 						= By.cssSelector(".btn-info.locked");
	By confirmationMessage 					= By.cssSelector(".bootbox-body");
	By callRecordLockUnlockImgPlaceHolder	= By.cssSelector(".changed-setting img");
	By callRecordLockedMessage			 	= By.xpath(".//*[contains(@class, 'callRecordingLockedBy')]/span[text() = 'Locked by Account Admin']");
	By callRecordingOverrideOptionsDropDown	= By.cssSelector(".call-recording-option");
	By callRecordOverrideDropDownDisabled 	= By.cssSelector(".call-recording-option:disabled");
	By lockRecordingToolTip 				= By.cssSelector(".changed-setting .tooltip-inner");
	
	//Group disposition settings
	By callDispostionRequiredLabel 			= By.xpath(".//*[text()='Call Disposition Required ']");
	By callDispositionRequiredDropDown 		= By.cssSelector("select.call-disposition-required-state");
	
	//Group Detail page
	By supervisorsList              = By.xpath("//div[@class='supervisors']//a[contains(@href,'#users/')]");
	By supervisorsNameList          = By.xpath("//div[@class='supervisors']//a[contains(@href,'#users/')]|//div[@class='supervisors']//td[contains(@class,'displayName')]");
	By supervisorsDeleteIconList    = By.xpath("//div[@class='supervisors']//span[contains(@class,'remove-sign')]");
	By membersList                  = By.xpath("//div[@class='members']//a[contains(@href,'#users/')]|//div[@class='members']//td[contains(@class,'displayName')]");
	By membersDeleteIconList 		= By.xpath("//div[@class='members']//span[contains(@class,'remove-sign')]");
	By membersListenInCheckBox		= By.xpath("//div[@class='members']//input[@type='checkbox']");
	By openBargeSelect 				= By.cssSelector("select.open-barge");
	By saveGroupButton				= By.cssSelector("button.save-team");
	By headerNotification			= By.className("toast-message");
	By addMemberIcon				= By.cssSelector("a.add-members span");
	By addSupervisorIcon			= By.cssSelector("a.add-supervisors span");
	By addVoiceMailIcon				= By.cssSelector("a.add-voicemail-drop");
	By superVisorsParagraph			= By.cssSelector(".supervisors p");
	By deleteConfirmationButton		= By.cssSelector("[data-bb-handler='confirm']");
	By wrapUpCheckBox				= By.cssSelector(".allow-wrap-up-time");
	By wrapUpTimeToggleOnBtn		= By.xpath("//input[contains(@class,'allow-wrap-up-time')]/..//label[contains(@class,'toggle-on')]");
	By wrapUpTimeToggleOffBtn		= By.xpath("//input[contains(@class,'allow-wrap-up-time')]/..//label[contains(@class,'toggle-off')]");
	By wrapUpTimeDrpDwn				= By.cssSelector("select.wrap-up-time-limit:not(:disabled)");
	By distributionStrategyDrpDwn	= By.cssSelector(".call-queue-type");
	By MaxDialsTextBox				= By.cssSelector(".longest-waiting-agent input.maximum-dials");
	By dialTimeoutTextBox			= By.cssSelector(".longest-waiting-agent input.dial-timeout");
	By voiceMailDropsRow			= By.cssSelector(".voicemail-team-group  tr span");
	
	//Add member/supervisor window
	By addUsersWindowTitle			= By.xpath("//h4[text()='Add Member']");
	By userNameSearchInput			= By.cssSelector("input.user-name");
	By userSearchButton				= By.cssSelector(".search");
	By searchUserNameList			= By.cssSelector(".users td.displayName");
	By searchAddUserChkBoxList		= By.cssSelector(".users input[type='checkbox']");
	By searchUserIdList				= By.cssSelector(".users td.id");
	By addUsersButton				= By.cssSelector("button.save");

	//smart number section
	By addSmartNumberIcon			= By.cssSelector("a.assign-group-number span");
	String smartNumberPage 		    = "//*[text()='$$smartNumber$$']/ancestor::tr/td[contains(@class,'renderable')]//*[contains(@href,'smart-numbers')] | //*[text()='$$smartNumber$$']/ancestor::tr/td[contains(@class,'renderable') and contains(@class,'number')]";
	String deleteSmartNoPage		= ".//*[text()='$$smartNumber$$']/ancestor::tr//span[contains(@class,'remove-sign')]";
	
	//call dispositions section
	By callDispositionHeading		= By.cssSelector(".disposition-group h2");
	
	//Group Search Account picklist
	By accountTextBoxSelectize = By.cssSelector(".account-picker div.selectize-input.items");
	By accountTextBoxInput  = By.cssSelector(".account-picker div.selectize-input.items input");
	By accountTextBoxOptions = By.cssSelector(".account-picker .selectize-dropdown-content div[data-selectable]");

	By groupTotalRecords		    = By.cssSelector(".total-records");
	By navigateRightIcon			= By.cssSelector(".glyphicon-chevron-right");
	
	public void enableCallRecordingOverrideSetting(WebDriver driver){
		String callRecordingState = getAttribue(driver, callRecordingOverrideOptionsDropDown, ElementAttributes.value);
		if(!callRecordingState.equals(CallRecordingOverrideOptions.All.toString())) {
			selectCallRecordingOverrideType(driver, CallRecordingOverrideOptions.All);
			System.out.println("enabled Call recording override setting");
		} else {
			System.out.println("Call recording override setting already enabled");
		}
	}

	public static enum TeamsHeaders{
		Name,
		Account,
		Description,
		CreatedDate,
		ModifiedDate,
		Id
	}
	
	
	public void disableCallRecordingOverrideSetting(WebDriver driver){
		String callRecordingState = getAttribue(driver, callRecordingOverrideOptionsDropDown, ElementAttributes.value);
		if(!callRecordingState.equals(CallRecordingOverrideOptions.None.toString()) && !isElementVisible(driver, callRecordingUnlockedImg, 0)) {
			selectCallRecordingOverrideType(driver, CallRecordingOverrideOptions.None);
			unlockRecordingSetting(driver);
			System.out.println("Disabled Call recording override setting");
		} else {
			System.out.println("Call recording override setting already disabled");
		}
	}

	public void verifyCallRecordOverrideDisabled(WebDriver driver) {
		waitUntilVisible(driver, callRecordLockedMessage);
		waitUntilVisible(driver, callRecordOverrideDropDownDisabled);
	}

	public void verifyCallRecordOverrideEnabled(WebDriver driver) {
		waitUntilVisible(driver, callRecordLockUnlockImgPlaceHolder);
		waitUntilVisible(driver, callRecordingOverrideOptionsDropDown);
		waitUntilInvisible(driver, callRecordLockedMessage);
	}
	
	public void verifyRecordingOverrideValue(WebDriver driver, CallRecordingOverrideOptions expectedOverrideValue) {
		assertEquals(getAttribue(driver, callRecordingOverrideOptionsDropDown, ElementAttributes.value), expectedOverrideValue.toString());
	}
	
	public void selectCallRecordingOverrideType(WebDriver driver, AccountCallRecordingTab.CallRecordingOverrideOptions reocrdOverrideType){
		selectFromDropdown(driver, callRecordingOverrideOptionsDropDown, SelectTypes.value, reocrdOverrideType.toString());
	}
	
	public void lockRecordingSetting(WebDriver driver) {
		if(isElementVisible(driver, callRecordingUnlockedImg, 0)) {
			String expectedConfirmMessage = null;
			String callRecordingState = getAttribue(driver, callRecordingOverrideOptionsDropDown, ElementAttributes.value);
			clickElement(driver, callRecordingUnlockedImg);
			if(!callRecordingState.equals(CallRecordingOverrideOptions.None.toString())) {
				String forCallType = null;
				if(callRecordingState.equals(CallRecordingOverrideOptions.All.toString())) {
					forCallType = "";
				}else {
					forCallType = callRecordingState.toLowerCase() + " ";
				}
				expectedConfirmMessage = "Call Recording will be turned ON for all " + forCallType + "calls and cannot be modified for members of this team.";
			}else {
				expectedConfirmMessage = "Call Recording will be turned OFF for all calls and cannot be modified for members of this team.";
			}
			waitUntilVisible(driver, confirmationMessage);
			assertEquals(getElementsText(driver, confirmationMessage), expectedConfirmMessage);
			clickElement(driver, lockConfirmBtn);
			waitUntilInvisible(driver, lockConfirmBtn);
		}
	}

	public void unlockRecordingSetting(WebDriver driver) {
		if(isElementVisible(driver, callRecordingLockedImg, 0)) {
			clickElement(driver, callRecordingLockedImg);
			clickElement(driver, lockConfirmBtn);
			waitUntilInvisible(driver, lockConfirmBtn);
		}
	}
	
	public void verifyRecordingLockTooLTip(WebDriver driver) {
		hoverElement(driver, callRecordingLockedImg);
		waitUntilTextPresent(driver, lockRecordingToolTip, "Click to unlock this setting and allow members of this team to set individual Call Recording preferences.");
	}
	
	
	public boolean isCallRecordingOverrideLocked(WebDriver driver) {
		return isElementVisible(driver, callRecordingLockedImg, 0);
	}

	public void clickCreateQueueLink(WebDriver driver){
		waitUntilVisible(driver, createCallQueueLink);
		clickElement(driver, createCallQueueLink);
		waitUntilInvisible(driver, createCallQueueLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void verifyAfterCreateCallTeamDetails(WebDriver driver, String teamName, String member) {
		waitUntilVisible(driver, groupDetailNameTab);
		assertEquals(getAttribue(driver, groupDetailNameTab, ElementAttributes.value), teamName.concat(" Team"));
		assertTrue(Strings.isNullOrEmpty(getAttribue(driver, groupDetailDescripTab, ElementAttributes.value)));
		assertTrue(isAgentAddedAsMember(driver, member));
		assertEquals(getElementsText(driver, superVisorsParagraph), "No supervisors associated.");
		assertEquals(getElementsText(driver, voiceMailDropsRow), "No voicemail drops found.");
	}
	
	//opening add member window
	public void openAddMemberWindow(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addMemberIcon);
		clickElement(driver, addMemberIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, addUsersWindowTitle);
	}
	
	//opening add member window
	public void openAddSupervisorWindow(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addSupervisorIcon);
		clickElement(driver, addSupervisorIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, addUsersWindowTitle);
	}

	public void clickHeaderTeamDetails(WebDriver driver){
		waitUntilVisible(driver, teamDetailsHeader);
		clickElement(driver, teamDetailsHeader);
	}
	
	//searching for user
	public void searchUserForAdd(WebDriver driver, String agentName) {
		enterText(driver, userNameSearchInput, agentName);
		clickElement(driver, userSearchButton);
		waitUntilVisible(driver, searchUserNameList);
		idleWait(1);
	}
	
	//Selecting user 
	public void selectUsersToAdd(WebDriver driver, String agentName) {
		isListElementsVisible(driver, searchAddUserChkBoxList, 5);
		waitUntiTextAppearsInListElements(driver, searchUserNameList);
		List<WebElement> elements = getElements(driver, searchAddUserChkBoxList);
		clickElement(driver, elements.get(getTextListFromElements(driver, searchUserNameList).indexOf(agentName)));
	}
	
	//click on add button 
	public void clickAddForUsers(WebDriver driver) {
		waitUntilVisible(driver, addUsersButton);
		clickElement(driver, addUsersButton);
		waitUntilInvisible(driver, addUsersButton);
	}
	
	//This method is to get members name list present
	public List<String> getMembersNameList(WebDriver driver) {
		if(!isElementPresent(driver, membersList, 2)) {
			return null;
		}
		return getTextListFromElements(driver, membersList);
	}
	
	//This method is to get supervisor name list present
	public List<String> getSupervisorsNameList(WebDriver driver) {
		if(!isElementPresent(driver, supervisorsList, 2) || !isElementPresent(driver, supervisorsNameList, 0)) {
			return null;
		}else if(isElementVisible(driver, supervisorsList,0)) {
			return getTextListFromElements(driver, supervisorsList);
		}
		else if(getInactiveElements(driver, supervisorsNameList)!=null){
			return getTextListFromElements(driver, supervisorsNameList);
		}else {
			return null;
		}		
	}
	
	//This method is to verify member present
	public boolean isAgentAddedAsMember(WebDriver driver, String agentName) {
		List<String> memberList = getMembersNameList(driver);
		if ( memberList != null && memberList.contains(agentName.trim())) {
			return true;
		}
		return false;
	}

	//This method is to verify members present
	public boolean isAgentAddedAsSupervisor(WebDriver driver, String agentName) {
		List<String> supervisorList = getSupervisorsNameList(driver);
		if(supervisorList !=null && supervisorList.contains(agentName.trim())) {
			return true;
		}
		return false;
	}
	
	//This method is to add member if not present
	public void addMember(WebDriver driver, String agentName) {
		if(!isAgentAddedAsMember(driver, agentName)) {
			if(isAgentAddedAsSupervisor(driver, agentName)) {
				deleteSuperviosr(driver, agentName);
			}
		} else {
			System.out.println("User is already added as member");
			return;
		}
		openAddMemberWindow(driver);
		searchUserForAdd(driver, agentName);
		selectUsersToAdd(driver, agentName);
		clickAddForUsers(driver);
		isAgentAddedAsMember(driver, agentName);		
	}
	
	//This method is to add supervisor if not present
	public void addSupervisor(WebDriver driver, String agentName) {
		if(!isAgentAddedAsSupervisor(driver, agentName)) {
			if(isAgentAddedAsMember(driver, agentName)) {
				deleteMember(driver, agentName);
			}
		} else {
			System.out.println("User is already added as supervisor");
			return;
		}
		openAddSupervisorWindow(driver);
		searchUserForAdd(driver, agentName);
		selectUsersToAdd(driver, agentName);
		clickAddForUsers(driver);
		isAgentAddedAsSupervisor(driver, agentName);		
	}

	//This method is to delete member if present
	public void deleteMember(WebDriver driver, String agentName) {
		scrollTillEndOfPage(driver);
		if(isAgentAddedAsMember(driver, agentName) && getMembersNameList(driver).size()>1) {
			List<WebElement> elements = new ArrayList<WebElement>();
			elements = getElements(driver, membersDeleteIconList);
			clickElement(driver, elements.get(getMembersNameList(driver).indexOf(agentName)));
			clickElement(driver, deleteConfirmationButton);
			waitUntilInvisible(driver, deleteConfirmationButton);
			assertEquals(isAgentAddedAsMember(driver, agentName), false);
		}
	}
	
	//This method enables members listen in capability
	public void enableListenInMember(WebDriver driver, String agentName) {
		if(!isAgentAddedAsMember(driver, agentName)) {
			addMember(driver, agentName);
		}
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = getElements(driver, membersListenInCheckBox);
		if(!elements.get(getMembersNameList(driver).indexOf(agentName)).isSelected()) {
			elements.get(getMembersNameList(driver).indexOf(agentName)).click();
		}else {
			System.out.println("Member already has the ability to listen in");
		}
	}
	
	//This method disables members listen in capability
	public void disableListenInMember(WebDriver driver, String agentName) {
		if(isAgentAddedAsMember(driver, agentName)) {
			List<WebElement> elements = new ArrayList<WebElement>();
			elements = getElements(driver, membersListenInCheckBox);
			if(elements.get(getMembersNameList(driver).indexOf(agentName)).isSelected()) {
				elements.get(getMembersNameList(driver).indexOf(agentName)).click();
			}else {
				System.out.println("Member does not has the ability to listen in");
			}
		}
	}
		
		
	//This method is to delete supervisor if present
	public void deleteSuperviosr(WebDriver driver, String agentName) {
		if(isAgentAddedAsSupervisor(driver, agentName)) {
			List<WebElement> elements = new ArrayList<WebElement>();
			elements = getElements(driver, supervisorsDeleteIconList);
			clickElement(driver, elements.get(getSupervisorsNameList(driver).indexOf(agentName)));
			clickElement(driver, deleteConfirmationButton);
			waitUntilInvisible(driver, deleteConfirmationButton);
			assertEquals(isAgentAddedAsSupervisor(driver, agentName), false);
		}
	}
	
	public void deleteAgentFromGroup(WebDriver driver, String agentName) {
		deleteMember(driver, agentName);
		deleteSuperviosr(driver, agentName);
	}
	
	public void selectUserFromCallReportNotification(WebDriver driver, String userName){
		clickAndSelectFromDropDown(driver, callReportNotificationInput, callReportNotificationDropdown, userName);
	}
	
	public List<WebElement> getCallReportsNotificationItems(WebDriver driver){
		return getElements(driver, callReportNotificationItems);
	}
	
	public String getCallReportsMail(WebDriver driver){
		return getElementsText(driver, callReportMailItems);
	}
	
	public void addCallReportsCCMail(WebDriver driver, String mail){
		idleWait(1);
		enterTextAndSelectFromDropDown(driver, callReportMailInput, callReportMailInput, callReportMailDropdown, mail);
	}
	
	public void unCheckInboundCallDisposition(WebDriver driver, String disposition){
		By inboundDispositionLoc = By.xpath(inboundDisposition.replace("$disposition$", disposition));
		waitUntilVisible(driver, inboundDispositionLoc);
		clickElement(driver, inboundDispositionLoc);
	}
	
	public void unCheckOutboundCallDisposition(WebDriver driver, String disposition){
		By outboundDispositionLoc = By.xpath(outboundDisposition.replace("$disposition$", disposition));
		waitUntilVisible(driver, outboundDispositionLoc);
		clickElement(driver, outboundDispositionLoc);
	}
	
	public boolean isInboundCallDispositionChecked(WebDriver driver, String disposition){
		By inboundDispositionLoc = By.xpath(inboundDisposition.replace("$disposition$", disposition));
		waitUntilVisible(driver, inboundDispositionLoc);
		return isAttributePresent(driver, inboundDispositionLoc, "checked");
	}
	
	public boolean isOutboundCallDispositionChecked(WebDriver driver, String disposition) {
		By outboundDispositionLoc = By.xpath(outboundDisposition.replace("$disposition$", disposition));
		waitUntilVisible(driver, outboundDispositionLoc);
		return isAttributePresent(driver, outboundDispositionLoc, "checked");
	}
	
	//This method is to save group
	public void saveGroup(WebDriver driver) {
		idleWait(1);
		waitUntilVisible(driver, saveGroupButton);
		clickElement(driver, saveGroupButton);
		waitUntilVisible(driver, headerNotification);
		assertEquals(getElementsText(driver, headerNotification), "Team info saved.");
		waitUntilInvisible(driver, headerNotification);
	}

	//This method is to open groups search page
	public void openGroupSearchPage(WebDriver driver) {
		if(getWebelementIfExist(driver, groupsManageLink)==null || !getWebelementIfExist(driver, groupsManageLink).isDisplayed()) {
			waitUntilVisible(driver, groupNavLink);
			clickElement(driver, groupNavLink);
		}
		scrollToElement(driver, groupsManageLink);
		waitUntilVisible(driver, groupsManageLink);
		clickElement(driver, groupsManageLink);
		dashboard.isPaceBarInvisible(driver);
	}

	public void openAddNewGroupPage(WebDriver driver) {
		if(getWebelementIfExist(driver, groupAddNewLink)==null || !getWebelementIfExist(driver, groupAddNewLink).isDisplayed()) {
			waitUntilVisible(driver, groupNavLink);
			clickElement(driver, groupNavLink);
		}
		scrollToElement(driver, groupAddNewLink);
		waitUntilVisible(driver, groupAddNewLink);
		clickElement(driver, groupAddNewLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enterGroupDetailDesc(WebDriver driver, String groupDetailName, String groupDetailDesc){
		waitUntilVisible(driver, groupDetailNameTab);
		enterText(driver, groupDetailNameTab, groupDetailName);
		waitUntilVisible(driver, groupDetailDescripTab);
		enterText(driver, groupDetailDescripTab, groupDetailDesc);
		saveGroup(driver);
		dashboard.isPaceBarInvisible(driver);		
	}
	
	public void addNewGroupDetails(WebDriver driver, String groupDetailName, String groupDetailDesc) {
		assertFalse(isElementVisible(driver, cloneTeamLink, 5));
		assertFalse(isElementVisible(driver, createCallQueueLink, 5));
		enterGroupDetailDesc(driver, groupDetailName, groupDetailDesc);
		assertTrue(isElementVisible(driver, cloneTeamLink, 5));
		assertTrue(isElementVisible(driver, createCallQueueLink, 5));
	}
	
	public String clickCloneTeamLink(WebDriver driver){
		waitUntilVisible(driver, cloneTeamLink);
		clickElement(driver, cloneTeamLink);
		waitUntilVisible(driver, headerNotification);
		String headerText = getElementsText(driver, headerNotification);
		clickHeaderTeamDetails(driver);
		waitUntilInvisible(driver, headerNotification);
		return headerText;
	}
	
	public void verifyTitleHeaders(WebDriver driver){
		String[] headerArray = new String[]{"Members", "Supervisors", "Voicemail Drop"};
		List<String> headerList = Arrays.asList(headerArray);
		for(String header:headerList){
			assertTrue(isListContainsText(driver, getElements(driver, titleHeaders), header));
		}
	}
	
	public void verifyGroupDetails(WebDriver driver, String groupDetailName, String groupDescName){
		waitUntilVisible(driver, groupDetailNameTab);
		waitUntilVisible(driver, groupDetailDescripTab);
		idleWait(1);
		assertEquals(getAttribue(driver, groupDetailNameTab, ElementAttributes.value), groupDetailName);
		assertEquals(getAttribue(driver, groupDetailDescripTab, ElementAttributes.value), groupDescName);
		assertTrue(isElementVisible(driver, createCallQueueLink, 5));
		assertTrue(isElementVisible(driver, cloneTeamLink, 5));
		verifyTitleHeaders(driver);
	}
	
	//It returns groups result list
	public List<WebElement> getGroupsResultList(WebDriver driver) {
		return getElements(driver, searchGroupsNames);
	}

	//This method is to select account for search
	public void selectAccount(WebDriver driver, String accountName) {
		if(isElementVisible(driver, accountTextBoxInput, 0)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			clickElement(driver, accountTextBoxOptions);
		}
	}

	//this method is to click on search
	public void clickSearch(WebDriver driver) {
		clickElement(driver, searchButton);
	}

	//this method is to set group name text box
	public void setGroupNameTextBox(WebDriver driver, String groupName) {
		enterText(driver, groupNameInputField, groupName);
	}

	//This method is to search a particular group
	public void searchGroups(WebDriver driver, String groupName, String accountName) {
		setGroupNameTextBox(driver, groupName);
		if(Strings.isNotNullAndNotEmpty(accountName))
			selectAccount(driver, accountName);
		clickSearch(driver);
		waitUntilVisible(driver, searchGroupsNames);
	}

	public String clickTeam(WebDriver driver, String teamName){
		By teamNameLoc = By.xpath(teamNameSearchPage.replace("$teamName$", teamName));
		waitUntilVisible(driver, teamNameLoc);
		clickElement(driver, teamNameLoc);
		dashboard.isPaceBarInvisible(driver);
		return getAttribue(driver, groupDetailNameTab, ElementAttributes.value);
	}
	
	public void clickFirstGroup(WebDriver driver) {
		getGroupsResultList(driver).get(0).click();
		dashboard.isPaceBarInvisible(driver);
	}

	public void openGroupDetailPage(WebDriver driver, String groupName, String accountName) {
		searchGroups(driver, groupName, accountName);
		clickFirstGroup(driver);
	}

	public void setOpenBarge(WebDriver driver, boolean openBarge) {
		waitUntilVisible(driver, openBargeSelect);
		idleWait(1);
		Select select = new Select(findElement(driver, openBargeSelect));
		if(openBarge){
			if(select.getFirstSelectedOption().getText().trim().equalsIgnoreCase("true")) {
				return;
			}
			select.selectByValue("true");
		} else {
			if(select.getFirstSelectedOption().getText().trim().equalsIgnoreCase("false")) {
				return;
			}
			select.selectByValue("false");
		}
		saveGroup(driver);
		scrollTillEndOfPage(driver);
	}

	public boolean getBargeValue(WebDriver driver) {
		return Boolean.parseBoolean(getSelectedValueFromDropdown(driver, openBargeSelect));
	}
	
	//smart number section
	public void clickSmartNumberIcon(WebDriver driver) {
		waitUntilVisible(driver, addSmartNumberIcon);
		clickElement(driver, addSmartNumberIcon);
	}
	
	public void deleteSmartNoPage(WebDriver driver, String smartNumber) {
		By deleteSmartNoLoc = By.xpath(deleteSmartNoPage.replace("$$smartNumber$$", smartNumber));
		waitUntilInvisible(driver, dashboard.paceBar);
		waitUntilVisible(driver, deleteSmartNoLoc);
		clickElement(driver, deleteSmartNoLoc);
		clickElement(driver, deleteConfirmationButton);
		waitUntilInvisible(driver, deleteConfirmationButton);
		assertTrue(isElementVisible(driver, addSmartNumberIcon, 6));
	}
	
	public boolean verifySmartNoExists(WebDriver driver, String smartNumber) {
		By smartNoLoc = By.xpath(smartNumberPage.replace("$$smartNumber$$", smartNumber));
		return isElementVisible(driver, smartNoLoc, 5);
	}
	
	public String addNewSmartNumberForGroups(WebDriver driver, String areaCode, String labelName) {
		waitUntilVisible(driver, addSmartNumberIcon);
		clickElement(driver, addSmartNumberIcon);
		String smartNumber = smartNumbersPage.addNewSmartNumber(driver, areaCode, labelName);
		saveGroup(driver);
		return smartNumber;
	}
	
	public void deleteGroup(WebDriver driver) {
		waitUntilVisible(driver, groupDeleteBtn);
		clickElement(driver, groupDeleteBtn);
		waitUntilVisible(driver, groupDeleteConfirmation);
		clickElement(driver, groupDeleteConfirmation);
		dashboard.isPaceBarInvisible(driver);
		waitUntilInvisible(driver, groupDeleteBtn);
		assertTrue(isElementVisible(driver, groupRestoreBtn, 5), "Group not deleted successfully");
	}
	
	public boolean isDeleteGroupBtnVisible(WebDriver driver){
		return isElementVisible(driver, groupDeleteBtn, 0);
	}
	
	public void selectLongestDistributationType(WebDriver driver){
		waitUntilVisible(driver, distributionStrategyDrpDwn);
		scrollToElement(driver, distributionStrategyDrpDwn);
		selectFromDropdown(driver, distributionStrategyDrpDwn, SelectTypes.visibleText, "Longest Waiting Agent");
		waitUntilVisible(driver, MaxDialsTextBox);
		waitUntilVisible(driver, dialTimeoutTextBox);
	}
	
	public void selectDefaultDistributationType(WebDriver driver){
		waitUntilVisible(driver, distributionStrategyDrpDwn);
		scrollToElement(driver, distributionStrategyDrpDwn);
		selectFromDropdown(driver, distributionStrategyDrpDwn, SelectTypes.visibleText, "Default");
		waitUntilInvisible(driver, MaxDialsTextBox);
		waitUntilInvisible(driver, dialTimeoutTextBox);
	}
	
	public String getMaxDials(WebDriver driver){
		waitUntilVisible(driver, MaxDialsTextBox);
		return getAttribue(driver, MaxDialsTextBox, ElementAttributes.value);
	}
	
	public String getDialTimeout(WebDriver driver){
		waitUntilVisible(driver, dialTimeoutTextBox);
		return getAttribue(driver, dialTimeoutTextBox, ElementAttributes.value);
	}

	public void restoreGroup(WebDriver driver) {
		waitUntilVisible(driver, groupRestoreBtn);
		clickElement(driver, groupRestoreBtn);
		waitUntilInvisible(driver, groupRestoreBtn);
		waitUntilVisible(driver, groupDeleteBtn);
	}
	
	public void verifyAfterDeleteGroupAssertions(WebDriver driver) {
		assertTrue(isAttributePresent(driver, groupDetailNameTab, "disabled"));
		assertTrue(isAttributePresent(driver, groupDetailDescripTab, "disabled"));
		assertTrue(isAttributePresent(driver, openBargeSelect, "disabled"));
		assertFalse(isElementVisible(driver, addMemberIcon, 5));
		assertFalse(isElementVisible(driver, addSupervisorIcon, 5));
		assertFalse(isElementVisible(driver, addVoiceMailIcon, 5));
	}
	
	public void verifyAfterRestoreGroupAssertions(WebDriver driver) {
		assertFalse(isAttributePresent(driver, groupDetailNameTab, "disabled"));
		assertFalse(isAttributePresent(driver, groupDetailDescripTab, "disabled"));
		assertFalse(isAttributePresent(driver, openBargeSelect, "disabled"));
		assertTrue(isElementVisible(driver, addMemberIcon, 5));
		assertTrue(isElementVisible(driver, addSupervisorIcon, 5));
		assertTrue(isElementVisible(driver, addVoiceMailIcon, 5));
	}
	
	public boolean verifyAscendingHeaderTeams(WebDriver driver, Enum<?> headerType){
		By headerLoc = null;
		By headerListLoc = null;
		String headerText = null;
		TeamsHeaders teamHeader = (TeamsHeaders) headerType;
		
		switch(teamHeader){
		case Name:
			headerLoc = nameHeader;
			headerListLoc = searchGroupsNames;
			headerText = "name";
			break;
		case Account:
			headerLoc = accountHeader;
			headerListLoc = searchAccountsNames;
			headerText = "account.name";
			break;
		case Description:
			headerLoc = descriptionHeader;
			headerListLoc = searchedDescription;
			headerText = "description";
			break;
		case CreatedDate:
			headerLoc = createdDateHeader;
			headerListLoc = searchedCreatedAt;
			headerText = "createdAt";
			break;
		case ModifiedDate:
			headerLoc = modifiedDateHeader;
			headerListLoc = searchedUpdatedAt;
			headerText = "updatedAt";
			break;
		case Id:
			headerLoc = idHeader;
			headerListLoc = searchedID;
			headerText = "id";
			break;
				
		default:
			break;
		}
		
		waitUntilVisible(driver, headerLoc);
		if(findElement(driver, headerLoc).getAttribute("class").equals("sortable renderable ".concat(headerText))){
			clickElement(driver, headerLoc);
		}
		else if (findElement(driver, headerLoc).getAttribute("class").contains("descending")){
			clickElement(driver, headerLoc);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, headerLoc);
		}
		
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, headerLoc).getAttribute("class").contains("ascending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, headerListLoc);
		return HelperFunctions.verifyAscendingOrderedList(afterSortElementsList);
	}
	
	public boolean verifyDescendingHeaderTeams(WebDriver driver, Enum<?> headerType){
		By headerLoc = null;
		By headerListLoc = null;
		String headerText = null;
		TeamsHeaders teamHeader = (TeamsHeaders) headerType;
		
		switch(teamHeader){
		case Name:
			headerLoc = nameHeader;
			headerListLoc = searchGroupsNames;
			headerText = "name";
			break;
		case Account:
			headerLoc = accountHeader;
			headerListLoc = searchAccountsNames;
			headerText = "account.name";
			break;
		case Description:
			headerLoc = descriptionHeader;
			headerListLoc = searchedDescription;
			headerText = "description";
			break;
		case CreatedDate:
			headerLoc = createdDateHeader;
			headerListLoc = searchedCreatedAt;
			headerText = "createdAt";
			break;
		case ModifiedDate:
			headerLoc = modifiedDateHeader;
			headerListLoc = searchedUpdatedAt;
			headerText = "updatedAt";
			break;
		case Id:
			headerLoc = idHeader;
			headerListLoc = searchedID;
			headerText = "id";
			break;
		default:
			break;
		}
		
		waitUntilVisible(driver, headerLoc);
		if(findElement(driver, headerLoc).getAttribute("class").equals("sortable renderable ".concat(headerText))){
			clickElement(driver, headerLoc);
			dashboard.isPaceBarInvisible(driver);
			idleWait(1);
			clickElement(driver, headerLoc);
		}
		else if (findElement(driver, headerLoc).getAttribute("class").contains("ascending")){
			clickElement(driver, headerLoc);
		}
		
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		assertTrue(findElement(driver, headerLoc).getAttribute("class").contains("descending"));
		List<String> afterSortElementsList = getTextListFromElements(driver, headerListLoc);
		return HelperFunctions.verifyDescendingOrderedList(afterSortElementsList);
	}
	
	//call disposition section
	public void verifyTeamDispositionSettingPresents(WebDriver driver) {
		waitUntilVisible(driver, callDispostionRequiredLabel);
		waitUntilVisible(driver, callDispositionRequiredDropDown);
		assertEquals(getSelectedValueFromDropdown(driver, callDispositionRequiredDropDown), "None");
	}
	
	public void setCallDispositionRequiredSetting(WebDriver driver, dispositionReqStates dispRequiredStates){
		selectFromDropdown(driver, callDispositionRequiredDropDown, SelectTypes.visibleText, dispRequiredStates.toString());
	}
	
	public void verifyTeamDispositionSettingInvisible(WebDriver driver) {
		waitUntilInvisible(driver, callDispostionRequiredLabel);
		waitUntilInvisible(driver, callDispositionRequiredDropDown);
	}
	
	public void cleanGroups(WebDriver driver, String searchKey) {
		try {
			while (isElementVisible(driver, navigateRightIcon, 5)) {
				for (int i = 0; i < driver.findElements(notDeletedGroupNames).size(); i++) {
					if (driver.findElements(notDeletedGroupNames).get(i).getText().contains(searchKey)) {
						idleWait(1);
						driver.findElements(notDeletedGroupNames).get(i).click();
						dashboard.isPaceBarInvisible(driver);
						deleteGroup(driver);
						driver.navigate().back();
						dashboard.isPaceBarInvisible(driver);
					}
				}
				driver.findElement(navigateRightIcon).click();
			}
		} catch (Exception e) {
			System.out.println("Exception occured while deleting groups " + e);
		}
	}
}
