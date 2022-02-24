/**
 * 
 */
package support.source.callQueues;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;

/**
 * @author Abhishek
 *
 */
public class CallQueuesPage extends SeleniumBase{

	SmartNumbersPage smartNumbersPage = new SmartNumbersPage();
	Dashboard dashboard = new Dashboard();
	
	By callQueueNavLink 		= By.cssSelector("a[data-target='#nav-Call Queues']");
	By callQueueManageLink		= By.xpath("//a[@href='#call-queues'][text()='Manage']");
	By callQueueAddNewLink		= By.xpath("//a[@href='#call-queue/add-new'][text()='Add New']");
	By callQueueNameInputField  = By.cssSelector("input.queue-name");
	By callQueuePageHeading		= By.xpath("//h1[text()='Call Queues']");
	By accountTextBoxSelectize  = By.cssSelector(".account-picker div.selectize-input.items");
	By accountTextBoxInput      = By.cssSelector(".account-picker div.selectize-input.items input");
	By accountTextBoxOptions    = By.cssSelector(".account-picker .selectize-dropdown-content div[data-selectable] span:not(.picker-value)");
	By membersMenuDownList		= By.cssSelector(".members span.glyphicon-menu-down");
	
	By searchButton			    = By.cssSelector("button.search");
	By searchCallQueuesNames	= By.xpath("//td//a[contains(@href,'#call-queues/')]");
	By notDeletedCallQueueNames = By.xpath("//ancestor::tr[not(contains(@class,'deleted'))]//td//a[contains(@href,'#call-queues/')]");
	By searchAccountsNames	    = By.xpath("//td//a[contains(@href,'#accounts/')]");
	By callQueueDeleteBtn		    = By.cssSelector(".btn-danger.delete");
	By callQueueRestoreBtn		    = By.cssSelector(".btn-success.restore");
	By callQueuDeleteConfirmation   = By.cssSelector("[data-bb-handler='ok']");
	
	String callQueueNamePage		= ".//a[contains(@href,'call-queues/') and text()='$callQueue$']";
	
	//Add New Call Queue Page
	By callQueueDetailNameTab		= By.cssSelector(".call-queue-overview .name[type='text']");
	By callQueueDetailDescripTab	= By.cssSelector(".call-queue-overview .description[type='text']");
	By callQueueStatus				= By.cssSelector(".status");
	By callQueueType				= By.cssSelector(".call-queue-type");
	By allowRejectToggleSwitch		= By.xpath(".//*[@class='allow-reject toggle-switch']/parent::div");
	By allowAgentUnsubscribeToggle	= By.xpath(".//*[@class='allow-unsubscribe toggle-switch']/parent::div");
	By createTeamLink				= By.cssSelector(".create-team");
	By cloneQueueLink				= By.cssSelector(".clone-call-queue");
	
	//Call Queue Detail page
	By callQueueNameHeader			= By.xpath(".//*[@class='call-queue-overview']//h2[text()='Call Queue Details']");
	By membersList                  = By.xpath("//div[@class='members']//a[contains(@href,'#users/')]|//div[@class='members']//td[contains(@class,'displayName')]");
	By membersDeleteIconList 		= By.xpath("//div[@class='members']//span[contains(@class,'remove-sign')]");
	By openBargeSelect 				= By.cssSelector("select.open-barge");
	By saveGroupButton				= By.cssSelector("button.save-call-queue");
	By headerNotification			= By.className("toast-message");
	By addMemberIcon				= By.cssSelector("a.add-members span");
	By addSupervisorIcon			= By.cssSelector("a.add-supervisors span");
	By deleteConfirmationButton		= By.cssSelector("[data-bb-handler='confirm']");
	By wrapUpCheckBox				= By.cssSelector(".allow-wrap-up-time");
	By wrapUpTimeToggleOnBtn		= By.xpath("//input[contains(@class,'allow-wrap-up-time')]/..//label[contains(@class,'toggle-on')]");
	By wrapUpTimeToggleOffBtn		= By.xpath("//input[contains(@class,'allow-wrap-up-time')]/..//label[contains(@class,'toggle-off')]");
	By wrapUpTimeDrpDwn				= By.cssSelector("select.wrap-up-time-limit:not(:disabled)");
	By distributionStrategyDrpDwn	= By.cssSelector(".call-queue-type");
	By maxDialsTextBox				= By.cssSelector("input.maximum-dials");
	By dialTimeoutTextBox			= By.cssSelector("input.dial-timeout");
	By simulringRowMessage			= By.xpath(".//*[@id='simulring-row']//td[2]");
	
	//Add member/supervisor window
	By addUsersWindowTitle			= By.xpath("//h4[text()='Add Member']");
	By userNameSearchInput			= By.cssSelector("input.user-name");
	By userSearchButton				= By.cssSelector(".search");
	By searchUserNameList			= By.cssSelector(".users td.displayName");
	By searchAddUserChkBoxList		= By.cssSelector(".users input[type='checkbox']");
	By searchUserIdList				= By.cssSelector(".users td.id");
	By addUsersButton				= By.cssSelector("button.save");

	//smart number section
	By addSmartNumberIcon			= By.cssSelector("a.assign-call-queue-number span");
	By smartNumberPara				= By.cssSelector(".smart-numbers p");
	String smartNumberPage 		    = "//*[text()='$$smartNumber$$']/ancestor::tr/td[contains(@class,'renderable')]//*[contains(@href,'smart-numbers')] | //*[text()='$$smartNumber$$']/ancestor::tr/td[contains(@class,'renderable') and contains(@class,'number')]";
	String smartNoPageDefault		= ".//*[text()='$smartNumber$']/ancestor::tr/td[contains(@class,'type') and text()='Default']";
	String deleteSmartNoPage		= ".//*[text()='$$smartNumber$$']/ancestor::tr//span[contains(@class,'remove-sign')]";

	By groupTotalRecords		    = By.cssSelector(".total-records");
	By navigateRightIcon			= By.cssSelector(".glyphicon-chevron-right");
	By closeBtn						= By.cssSelector(".close");
	
	//call processing
	By sendAgentAtLastCheckBox      = By.cssSelector(".handle-missed-call-as-answered.toggle-switch");
	By sendAgentAtLastToggleOnBtn   = By.xpath("//input[contains(@class,'handle-missed-call-as-answered toggle-switch')]/..//label[contains(@class,'toggle-on')]");
	By sendAgentAtLastToggleOffBtn  = By.xpath("//input[contains(@class,'handle-missed-call-as-answered toggle-switch')]/..//label[contains(@class,'toggle-off')]");
	
	public enum WrapUpTime{
		ThirtySecs("30 seconds"),
		TwoMins("2 minutes"),
		OneMin("1 minute"),
		FiveMin("5 minutes"),
		Unlimited("Unlimited");
		
		private String displayName;

		WrapUpTime(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() { return displayName; }

		@Override
		public String toString() { return displayName; }
	}
	
	public enum CallProcessingType{
		Default,
		LongestWaitingAgent,
		SequentialDial
	}
	
	public enum DefaultType{
		CallProcessing,
		WrapUpTime
	}
	
	//opening add member window
	public void openAddMemberWindow(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addMemberIcon);
		clickByJs(driver, addMemberIcon);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, addUsersWindowTitle);
		isListElementsVisible(driver, searchUserNameList, 5);
	}
	
	//opening add member window
	public void openAddSupervisorWindow(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, addSupervisorIcon);
		clickByJs(driver, addSupervisorIcon);
		waitUntilVisible(driver, addUsersWindowTitle);
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
		waitUntilVisible(driver, searchUserNameList);
		List<WebElement> elements = getElements(driver, searchAddUserChkBoxList);
		clickElement(driver, elements.get(getTextListFromElements(driver, searchUserNameList).indexOf(agentName)));
	}
	
	//click on add button 
	public void clickAddForUsers(WebDriver driver) {
		clickElement(driver, addUsersButton);
		waitUntilInvisible(driver, addUsersButton);
	}
	
	//This method is to get members name list present
	public List<String> getMembersNameList(WebDriver driver) {
		if(getWebelementIfExist(driver, membersList)==null) {
			return null;
		}
		return getTextListFromElements(driver, membersList);
	}
	
	//This method is to verify member present
	public boolean isAgentAddedAsMember(WebDriver driver, String agentName) {
		if(getMembersNameList(driver)!=null&&getMembersNameList(driver).contains(agentName.trim())) {
			return true;
		}
		return false;
	}
	
	//This method is to add member if not present
	public void addMember(WebDriver driver, String agentName) {
		if(isAgentAddedAsMember(driver, agentName)) {
			System.out.println("User is already added as member");
			return;
		}
		openAddMemberWindow(driver);
		searchUserForAdd(driver, agentName);
		selectUsersToAdd(driver, agentName);
		clickAddForUsers(driver);
		isAgentAddedAsMember(driver, agentName);		
	}

	//This method is to delete member if present
	public void deleteMember(WebDriver driver, String agentName) {
		if(isAgentAddedAsMember(driver, agentName) && getMembersNameList(driver).size()>1) {
			List<WebElement> elements = new ArrayList<WebElement>();
			elements = getElements(driver, membersDeleteIconList);
			clickElement(driver, elements.get(getMembersNameList(driver).indexOf(agentName)));
			clickElement(driver, deleteConfirmationButton);
			waitUntilInvisible(driver, deleteConfirmationButton);
			assertEquals(isAgentAddedAsMember(driver, agentName), false);
		}
	}
	
	public void deleteAgentFromGroup(WebDriver driver, String agentName) {
		deleteMember(driver, agentName);
	}
	
	public boolean isMemberSubscribed(WebDriver driver, String agentName){
		By memberSubscriptionList = By.cssSelector(".members tbody .rememberSubscription");
		int index = -1;
		if(getMembersNameList(driver)!=null && getMembersNameList(driver).contains(agentName.trim())) {
			index = getMembersNameList(driver).indexOf(agentName);
		}
		if(getTextListFromElements(driver, memberSubscriptionList).get(index).equals("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	//This method is to save group
	
	public String getHeaderNotification(WebDriver driver){
		waitUntilVisible(driver, headerNotification);
		return getElementsText(driver, headerNotification);
	}
	
	public void isHeaderNotificationInvisible(WebDriver driver) {
		waitUntilInvisible(driver, headerNotification);
	}
	
	public void saveGroup(WebDriver driver) {
		idleWait(1);
		waitUntilInvisible(driver, headerNotification);
		clickSaveGroupButton(driver);
		waitUntilVisible(driver, headerNotification);
		assertEquals(getElementsText(driver, headerNotification), "Queue info saved.");
		waitUntilInvisible(driver, headerNotification);
	}
	
	public void clickSaveGroupButton(WebDriver driver){
		waitUntilVisible(driver, saveGroupButton);
		clickElement(driver, saveGroupButton);
	}

	//This method is to open groups search page
	public void openCallQueueSearchPage(WebDriver driver) {
		if(getWebelementIfExist(driver, callQueueManageLink)==null || !getWebelementIfExist(driver, callQueueManageLink).isDisplayed()) {
			scrollIntoView(driver, callQueueNavLink);
			clickElement(driver, callQueueNavLink);
		}
		waitUntilVisible(driver, callQueueManageLink);
		scrollIntoView(driver, callQueueManageLink);
		clickElement(driver, callQueueManageLink);
		dashboard.isPaceBarInvisible(driver);
	}

	public void openAddNewCallQueue(WebDriver driver) {
		if(getWebelementIfExist(driver, callQueueAddNewLink)==null || !getWebelementIfExist(driver, callQueueAddNewLink).isDisplayed()) {
			clickElement(driver, callQueueNavLink);
		}
		waitUntilVisible(driver, callQueueAddNewLink);
		clickElement(driver, callQueueAddNewLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void enterCallQueueName(WebDriver driver, String queueName) {
		waitUntilVisible(driver, callQueueDetailNameTab);
		enterText(driver, callQueueDetailNameTab, queueName);
	}

	public void enterCallQueueDesc(WebDriver driver, String queueDesc) {
		waitUntilVisible(driver, callQueueDetailDescripTab);
		enterText(driver, callQueueDetailDescripTab, queueDesc);
	}
	
	public void addNewCallQueueDetails(WebDriver driver, String queueName, String queueDesc) {
		assertFalse(isElementVisible(driver, cloneQueueLink, 5));
		assertFalse(isElementVisible(driver, createTeamLink, 5));
		enterCallQueueName(driver, queueName);
		enterCallQueueDesc(driver, queueDesc);
		saveGroup(driver);
		dashboard.isPaceBarInvisible(driver);
		assertTrue(isElementVisible(driver, cloneQueueLink, 5));
		assertTrue(isElementVisible(driver, createTeamLink, 5));
	}
	
	public boolean isAllowRejectCallQueueOn(WebDriver driver) {
		return !findElement(driver, allowRejectToggleSwitch).getAttribute("class").contains("off");
	}
	
	public void enableAllowRejectQueueCalls(WebDriver driver){
		if(isAllowRejectCallQueueOn(driver)){
			System.out.println("Allow Agents to Reject Calls to Queue is already enabled");
		}else{
			clickElement(driver, allowRejectToggleSwitch);
		}
	}
	
	public void disableAllowRejectQueueCalls(WebDriver driver){
		if(isAllowRejectCallQueueOn(driver)){
			clickElement(driver, allowRejectToggleSwitch);
		}else{
			System.out.println("Allow Agents to Reject Calls to Queue is already disabled");
		}
	}

	public boolean isAllowAgentUnsubscribeCallQueueOn(WebDriver driver) {
		return !findElement(driver, allowAgentUnsubscribeToggle).getAttribute("class").contains("off");
	}
	
	public void enableAllowAgentUnsubscribeToggle(WebDriver driver){
		if(isAllowAgentUnsubscribeCallQueueOn(driver)){
			System.out.println("Allow Agents to Unsubscribe to Queue is already enabled");
		}else{
			clickElement(driver, allowAgentUnsubscribeToggle);
		}
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void disableAllowAgentUnsubscribeToggle(WebDriver driver){
		if(isAllowAgentUnsubscribeCallQueueOn(driver)){
			clickElement(driver, allowAgentUnsubscribeToggle);
		}else{
			System.out.println("Allow Agents to Unsubscribe to Queue is already disabled");
		}
		dashboard.isPaceBarInvisible(driver);
	}

	public String getDefaultText(WebDriver driver, Enum<?> enumType) {
		By fieldLoc = null;
		DefaultType defaultType = (DefaultType) enumType;
		switch (defaultType) {
		case CallProcessing:
			fieldLoc = callQueueType;
			break;
		case WrapUpTime:
			fieldLoc = wrapUpTimeDrpDwn;
		default:
			break;
		}
		return getSelectedValueFromDropdown(driver, fieldLoc);
	}
	
	public String getDefaultValue(WebDriver driver, Enum<?> enumType) {
		By fieldLoc = null;
		DefaultType defaultType = (DefaultType) enumType;
		switch (defaultType) {
		case CallProcessing:
			fieldLoc = callQueueType;
			break;
		case WrapUpTime:
			fieldLoc = wrapUpTimeDrpDwn;
		default:
			break;
		}
		return getSelectClassDefaultValue(driver, fieldLoc);
	}
	
	public void verifyAfterCreateCallQueueDetails(WebDriver driver, String teamName, String member) {
		waitUntilVisible(driver, callQueueDetailNameTab);
		assertEquals(getAttribue(driver, callQueueDetailNameTab, ElementAttributes.value), teamName.concat(" Queue"));
		assertTrue(Strings.isNullOrEmpty(getAttribue(driver, callQueueDetailDescripTab, ElementAttributes.value)));
		assertEquals(getElementsText(driver, smartNumberPara), "No smart number associated.");
		assertTrue(isAgentAddedAsMember(driver, member));
		assertEquals(getSelectedValueFromDropdown(driver, callQueueType), "Simulring"); 
	}

	public void verifyAfterCloneCallQueueDetails(WebDriver driver, String teamName, String description, String member) {
		waitUntilVisible(driver, callQueueDetailNameTab);
		assertEquals(getAttribue(driver, callQueueDetailNameTab, ElementAttributes.value), teamName.concat(" Copy"));
		assertEquals(getAttribue(driver, callQueueDetailDescripTab, ElementAttributes.value), description);
		assertEquals(getElementsText(driver, smartNumberPara), "No smart number associated.");
		assertTrue(isAgentAddedAsMember(driver, member));
		assertEquals(getSelectedValueFromDropdown(driver, callQueueType),"Simulring"); 
	}
	
	public void verifyCallQueueDetails(WebDriver driver, String groupDetailName, String groupDescName){
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
		waitUntilVisible(driver, callQueueDetailNameTab);
		waitUntilVisible(driver, callQueueDetailDescripTab);
		assertEquals(getAttribue(driver, callQueueDetailNameTab, ElementAttributes.value), groupDetailName);
		assertEquals(getAttribue(driver, callQueueDetailDescripTab, ElementAttributes.value), groupDescName);
	}
	
	//It returns groups result list
	public List<WebElement> getCallQueuesResultList(WebDriver driver) {
		return getElements(driver, searchCallQueuesNames);
	}

	//This method is to select account for search
	public void selectAccount(WebDriver driver, String accountName) {
		if (isElementVisible(driver, accountTextBoxInput, 5)) {
			clickElement(driver, accountTextBoxSelectize);
			enterText(driver, accountTextBoxInput, accountName);
			clickElement(driver, accountTextBoxOptions);
		}
	}

	//this method is to click on search
	public void clickSearch(WebDriver driver) {
		waitUntilVisible(driver, searchButton);
		clickElement(driver, searchButton);
	}

	//this method is to set group name text box
	public void setCallQueueNameTextBox(WebDriver driver, String queueName) {
		enterText(driver, callQueueNameInputField, queueName);
	}

	//This method is to search a particular group
	public void searchCallQueues(WebDriver driver, String queueName, String accountName) {
		setCallQueueNameTextBox(driver, queueName);
		if(Strings.isNotNullAndNotEmpty(accountName))
			selectAccount(driver, accountName);
		clickSearch(driver);
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, searchCallQueuesNames);
	}

	public void verifyAccountExistsInDropDown(WebDriver driver, String accountName){
		waitUntilVisible(driver, accountTextBoxInput);
		enterText(driver, accountTextBoxInput, accountName);
		waitUntilVisible(driver, accountTextBoxOptions);
		assertTrue(getTextListFromElements(driver, accountTextBoxOptions).contains(accountName));
	}
	
	public void clickFirstCallQueue(WebDriver driver) {
		getCallQueuesResultList(driver).get(0).click();
		dashboard.isPaceBarInvisible(driver);
	}

	public void selectCallQueue(WebDriver driver, String callQueue){
		By callQueueLoc = By.xpath(callQueueNamePage.replace("$callQueue$", callQueue));
		waitUntilVisible(driver, callQueueLoc);
		clickElement(driver, callQueueLoc);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void openCallQueueDetailPage(WebDriver driver, String queueName, String accountName) {
		searchCallQueues(driver, queueName, accountName);
		clickFirstCallQueue(driver);
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
	}
	
	public void setWrapUpTime(WebDriver driver, WrapUpTime wrapUpTime){
		if(!findElement(driver, wrapUpCheckBox).isSelected()) {
			clickElement(driver, wrapUpTimeToggleOffBtn);
			System.out.println("enabled wrap up time setting");
		}
		waitUntilVisible(driver, wrapUpTimeDrpDwn);
		selectFromDropdown(driver, wrapUpTimeDrpDwn, SelectTypes.visibleText, wrapUpTime.displayName());
	}
	
	public void disableWrapUpTime(WebDriver driver){
		if(findElement(driver, wrapUpCheckBox).isSelected()) {
			clickElement(driver, wrapUpTimeToggleOnBtn);
			System.out.println("disabled wrap up time setting");
		}
	}

	public void clickCreateTeamLink(WebDriver driver){
		waitUntilVisible(driver, createTeamLink);
		clickElement(driver, createTeamLink);
		waitUntilInvisible(driver, createTeamLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public String clickCloneQueueLink(WebDriver driver){
		waitUntilVisible(driver, cloneQueueLink);
		scrollIntoView(driver, cloneQueueLink);
		clickElement(driver, cloneQueueLink);
		waitUntilVisible(driver, headerNotification);
		String headerText = getElementsText(driver, headerNotification);
		waitUntilVisible(driver, callQueueNameHeader);
		scrollIntoView(driver, callQueueNameHeader);
		clickElement(driver, callQueueNameHeader);
		waitUntilInvisible(driver, headerNotification);
		return headerText;
	}
	
	//smart number section
	public void clickSmartNumberIcon(WebDriver driver) {
		waitUntilVisible(driver, addSmartNumberIcon);
		clickElement(driver, addSmartNumberIcon);
	}
	
	public void deleteSmartNoPage(WebDriver driver, String smartNumber) {
		assertFalse(isElementVisible(driver, addSmartNumberIcon, 6));
		By deleteSmartNoLoc = By.xpath(deleteSmartNoPage.replace("$$smartNumber$$", smartNumber));
		waitUntilInvisible(driver, dashboard.paceBar);
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, deleteSmartNoLoc);
		clickElement(driver, deleteSmartNoLoc);
		clickElement(driver, deleteConfirmationButton);
		waitUntilInvisible(driver, deleteConfirmationButton);
		assertTrue(isElementVisible(driver, addSmartNumberIcon, 6));
	}
	
	public void selectCallProcessing(WebDriver driver, CallProcessingType processType){
		waitUntilVisible(driver, callQueueType);
		selectFromDropdown(driver, callQueueType, SelectTypes.value, processType.toString());
	}
	
	public boolean verifySmartNoExists(WebDriver driver, String smartNumber) {
		By smartNoLoc = By.xpath(smartNumberPage.replace("$$smartNumber$$", smartNumber));
		return isElementVisible(driver, smartNoLoc, 5);
	}
	
	public boolean verifySmartNoDefault(WebDriver driver, String smartNumber) {
		By smartNoLoc = By.xpath(smartNoPageDefault.replace("$smartNumber$", smartNumber));
		return isElementVisible(driver, smartNoLoc, 5);
	}
	
	public String addNewSmartNumberForGroups(WebDriver driver, String areaCode, String labelName) {
		waitUntilVisible(driver, addSmartNumberIcon);
		clickElement(driver, addSmartNumberIcon);
		String smartNumber = smartNumbersPage.addNewSmartNumber(driver, areaCode, labelName);
		saveGroup(driver);
		return smartNumber;
	}
	
	public void deleteCallQueue(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, callQueueDeleteBtn);
		clickElement(driver, callQueueDeleteBtn);
		waitUntilVisible(driver, callQueuDeleteConfirmation);
		clickElement(driver, callQueuDeleteConfirmation);
		waitUntilInvisible(driver, callQueueDeleteBtn);
		assertTrue(isElementVisible(driver, callQueueRestoreBtn, 5), "Call Queue not deleted successfully");
	}
	
	public void restoreCallQueue(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, callQueueRestoreBtn);
		clickElement(driver, callQueueRestoreBtn);
		waitUntilInvisible(driver, callQueueRestoreBtn);
		waitUntilVisible(driver, callQueueDeleteBtn);
	}
	
	public void verifyAfterDeleteCallQueueAssertions(WebDriver driver) {
		assertTrue(isElementDisabled(driver, callQueueDetailNameTab, 5));
		assertTrue(isElementDisabled(driver, callQueueDetailDescripTab, 5));
		assertTrue(isElementDisabled(driver, callQueueStatus, 5));
		assertTrue(isElementDisabled(driver, callQueueType, 5));
		assertFalse(isElementVisible(driver, addMemberIcon, 5));
		assertFalse(isElementVisible(driver, addSmartNumberIcon, 5));
	}
	
	public void verifyAfterRestoreCallQueueAssertions(WebDriver driver) {
		assertFalse(isElementDisabled(driver, callQueueDetailNameTab, 5));
		assertFalse(isElementDisabled(driver, callQueueDetailDescripTab, 5));
		assertFalse(isElementDisabled(driver, callQueueStatus, 5));
		assertFalse(isElementDisabled(driver, callQueueType, 5));
		assertTrue(isElementVisible(driver, addMemberIcon, 5));
		assertTrue(isElementVisible(driver, addSmartNumberIcon, 5));
	}
	
	public boolean isDeleteCallQueueBtnVisible(WebDriver driver){
		return isElementVisible(driver, callQueueDeleteBtn, 0);
	}
	
	public void selectSequentialDialDistributationType(WebDriver driver){
		waitUntilVisible(driver, distributionStrategyDrpDwn);
		scrollToElement(driver, distributionStrategyDrpDwn);
		selectFromDropdown(driver, distributionStrategyDrpDwn, SelectTypes.value, CallProcessingType.SequentialDial.name());
		waitUntilVisible(driver, maxDialsTextBox);
		assertEquals(getMaxDials(driver), "10");
		waitUntilVisible(driver, dialTimeoutTextBox);
		assertEquals(getDialTimeout(driver), "10");
	}
	
	public void selectLongestDistributationType(WebDriver driver){
		waitUntilVisible(driver, distributionStrategyDrpDwn);
		scrollToElement(driver, distributionStrategyDrpDwn);
		selectFromDropdown(driver, distributionStrategyDrpDwn, SelectTypes.visibleText, "Longest Waiting Agent");
		waitUntilVisible(driver, maxDialsTextBox);
		waitUntilVisible(driver, dialTimeoutTextBox);
	}
	
	public void selectDefaultDistributationType(WebDriver driver){
		waitUntilVisible(driver, distributionStrategyDrpDwn);
		scrollToElement(driver, distributionStrategyDrpDwn);
		selectFromDropdown(driver, distributionStrategyDrpDwn, SelectTypes.visibleText, "Simulring");
		waitUntilInvisible(driver, maxDialsTextBox);
		waitUntilInvisible(driver, dialTimeoutTextBox);
		List<String> messageTable = getTextListFromElements(driver, By.cssSelector(".call-queue-admin-table tr:not([style='display: none;']) td"));
		assertEquals(messageTable.get(0), "Distribution\nStrategy");
		assertEquals(messageTable.get(1), "Messaging");
		assertEquals(messageTable.get(2), "Simulring");
		assertEquals(messageTable.get(3), "ringDNA will simultaneously forward calls to all available, subscribed members of the call queue.");
	}
	
	public String getMaxDials(WebDriver driver){
		waitUntilVisible(driver, maxDialsTextBox);
		return getAttribue(driver, maxDialsTextBox, ElementAttributes.value);
	}
	
	public String getDialTimeout(WebDriver driver){
		waitUntilVisible(driver, dialTimeoutTextBox);
		return getAttribue(driver, dialTimeoutTextBox, ElementAttributes.value);
	}
	
	public void setDialTimeout(WebDriver driver, String dialTimeout){
		waitUntilVisible(driver, dialTimeoutTextBox);
		enterText(driver, dialTimeoutTextBox, dialTimeout);
	}
	
	public void setMaxDials(WebDriver driver, String maxDials){
		waitUntilVisible(driver, maxDialsTextBox);
		scrollToElement(driver, maxDialsTextBox);
		enterText(driver, maxDialsTextBox, maxDials);
	}
	
	public void closeWindow(WebDriver driver){
		waitUntilVisible(driver, closeBtn);
		clickElement(driver, closeBtn);
	}
	
	public List<String> clickMembersMenuDownItems(WebDriver driver){
		waitUntilVisible(driver, getElements(driver, membersMenuDownList).get(0));
		clickElement(driver, getElements(driver, membersMenuDownList).get(0));
		dashboard.isPaceBarInvisible(driver);
		return getMembersNameList(driver); 
	}
	
	public void cleanQueues(WebDriver driver, String searchKey) {
		try {
			while (isElementVisible(driver, navigateRightIcon, 5)) {
				for (int i = 0; i < driver.findElements(notDeletedCallQueueNames).size(); i++) {
					if (driver.findElements(notDeletedCallQueueNames).get(i).getText().contains(searchKey)) {
						idleWait(1);
						driver.findElements(notDeletedCallQueueNames).get(i).click();
						dashboard.isPaceBarInvisible(driver);
						deleteCallQueue(driver);
						driver.navigate().back();
						dashboard.isPaceBarInvisible(driver);
					}
				}
				driver.findElement(navigateRightIcon).click();
			}
		} catch (Exception e) {
			System.out.println("Exception occured while deleting queues " + e);
		}
	}
	
	// call processing

	public void enableSendAgentAtLast(WebDriver driver) {
		if (!findElement(driver, sendAgentAtLastCheckBox).isSelected()) {
			clickElement(driver, sendAgentAtLastToggleOffBtn);
			System.out.println("enabled Send Agent At Last setting");
		}
	}

	public void disableSendAgentAtLast(WebDriver driver) {
		if (findElement(driver, sendAgentAtLastCheckBox).isSelected()) {
			clickElement(driver, sendAgentAtLastToggleOnBtn);
			System.out.println("disabled Send Agent At Last setting");
		}
	}
}
