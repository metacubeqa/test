package support.source.commonpages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import base.SeleniumBase;
import base.TestBase;
import support.source.accounts.AccountsPage;
import support.source.conversationAIReact.CallsTabReactPage;
import support.source.conversationAIReact.SettingsPage;


public class Dashboard extends SeleniumBase{
	
	AccountsPage supportAccounts = new AccountsPage();
	
	public By paceBar 		= By.cssSelector(".pace.pace-active");
	By caiLogo				= By.xpath(".//*[local-name() ='title' and text() = 'Conversation AI Logo']");
	By expandedSideBar		= By.cssSelector(".side-menu .active");
	By siteTitle			= By.cssSelector("body:not(.conversation-ai) .site-title");
	By accountsLink		 	= By.cssSelector("[href='#accounts']");
	By callDashboardLink	= By.xpath(".//a[img[@alt='ringDNA Live icon' or @src='images/dashboard.svg']]");
	By callsLink			= By.cssSelector("[href='#calls']");
	By userProfileLink		= By.cssSelector(".profile-pic .user-profile-url");
	By messagesLink			= By.cssSelector("[href='#messages']");
	By profilesLink			= By.cssSelector("[href='#salesforce-profiles']");
	By importLink			= By.cssSelector("[data-target='#nav-Import']>span");
	By systemLink			= By.cssSelector("[data-target='#nav-System']>span");
	By mySettingsLink		= By.cssSelector(".open .user-profile-url");
	By adminSettingsLink	= By.xpath("//span[text()='Admin Settings']");
	By logoutLink			= By.cssSelector(".open [data-action='logout']");
	By toolTip              = By.cssSelector(".tooltip-inner");
	By sideMenuList			= By.cssSelector(".nav.side-menu li span");
	
	By settingsLinkAnalytics		= By.cssSelector("[href='/settings']");
	
	By userProfileDropDownToggle 	= By.cssSelector(".user-profile.dropdown-toggle, [aria-label = 'rdna-menu-trigger']");
	By userDropdownCAISetup		 	= By.cssSelector(".dropdown-menu .conversation-ai-setup");
	By userDropDownCAINotifications = By.xpath("//a[@class='conversation-ai-notifications'] | //span[text()='Notifications']");
	
	By callFlowLink			= By.cssSelector("[data-target='#nav-Call Flows']>span");
	By callFlowWexpandBtn	= By.cssSelector("[data-target='#nav-Call Flows'] .fa-chevron-down");
	By callFlowAddNewLink	= By.cssSelector("[href='#call-flow/add-new']");
	By callFlowManageLink	= By.cssSelector("[href='#call-flows']");
	
	By usersLink			= By.cssSelector("[data-target='#nav-Users']>span");
	By usersExpandButton	= By.cssSelector("[data-target='#nav-Users'] .fa-chevron-down");
	By usersManageLink 		= By.cssSelector("[href='#users']");
	By usersAddNewLink		= By.cssSelector("[href='#user/salesforce-users']"); 
	
	By teamsLink            = By.xpath("//a[@data-target='#nav-Teams']/span");
	By teamsAddLink         = By.cssSelector("[href='#team/add-new']");
	By teamsManageLink      = By.cssSelector("[href='#teams']");
	By teamsAddLinkHeading  = By.xpath("//h2[text() = 'Team Details']");
	
	By callTrackingLink		= By.cssSelector("[data-target='#nav-Call Tracking']>span");
	By advanceCallTracking	= By.cssSelector("[href='#call-tracking/advanced']");
	By basicCallTracking	= By.cssSelector("[href='#call-tracking/basic']");
	By customCallTracking	= By.cssSelector("[href='#call-tracking/custom']");
	By offlineCallTracking	= By.cssSelector("[href='#call-tracking/offline']");
	By googleCallTracking	= By.cssSelector("[href='#call-tracking/adwords']");
	By dynamicNumberInsertion	= By.cssSelector("[href='#call-tracking/dynamic-number-insertion']");
	
	By adminAccountLink		= By.cssSelector("[href='#accounts/me']");
	By accountOverviewTitle = By.xpath("//*[@id='main']//h1[text()='Account : ']");
	By callInstpectorTitle	= By.xpath(".//h1[text()='Call Inspector']");
	By smartNumbersLink		= By.cssSelector("[href='#smart-numbers']");
	By homeBars				= By.cssSelector(".fa-bars");
	By homeBarsReact		= By.cssSelector("#menu-toggle-new");
	By rssFeeds				= By.cssSelector(".fa-rss");
	By ringDNALogo			= By.cssSelector(".ringdna-logo");
	By conversationAILink	= By.cssSelector("[href='#smart-recordings'], a.cai");
	By caiLearnMoreLink		= By.cssSelector("[href='https://www.ringdna.com/lp/conversationai']");
	By caiLicenseRequired   = By.xpath("//h3[text()= 'User License Required']");
	By callRecordingLink	= By.cssSelector("[href='#call-recordings']");
	//import section locators
	By outBoundNumbers		= By.cssSelector("[href='#import/outbound-numbers']");
	By importUsers			= By.cssSelector("[href='#import/users']");
	By importBlockedNumbers	= By.cssSelector("[href='#import/blocked-numbers']");
	
	//System section locators
	By deleteNumbers		= By.cssSelector("[href='#system/bulk-number-deleter']");
	By numberInspector		= By.cssSelector("[href='#system/number-inspector']");
	By numberTransfer		= By.cssSelector("[href='#system/number-transfer']");
	By domainBlackList		= By.cssSelector("[href='#system/domain-blacklist']");
	By cleanUpRecording		= By.cssSelector("[href='#system/recordings']");
	By splitAccount			= By.cssSelector("[href='#system/split-account']");
	By serviceUser          = By.cssSelector("[href='#system/service-user']");
	By downtimeEvents       = By.cssSelector("[href='#system/downtime-events']");
	By deployments	        = By.cssSelector("[href='#system/deployments']");
	By sipRouting           = By.cssSelector("[href='#system/sip-routing']");
	
	//Report section Locator
	By reportLink			= By.cssSelector("[data-target='#nav-Reports']>span");
	By callLogsReport   	= By.cssSelector("[href='#reports/call-log']");
	By callDistribution   	= By.cssSelector("[href='#reports/call-distribution']");
	By UserSummaryByDay     = By.cssSelector("[href='#reports/user-busy-presence-summary-day']");
	By avgCallByDay         = By.cssSelector("[href='#reports/avg-call-dur-day']");
	By avgCallByDayTab      = By.cssSelector("[href='#reports/avg-call-dur-day-tab']");
	By qBRreport            = By.cssSelector("[href='#reports/qbr']");
	By newAccounts			= By.cssSelector("[href='#reports/new-accounts']");
	By callTypesByDay       = By.cssSelector("[href='#reports/caller-types-day']");
	By callTypesByDayTab    = By.cssSelector("[href='#reports/caller-types-day-tab']");
	By callDurationByDayTab = By.cssSelector("[href='#reports/call-dur-day-tab']");
	By callDurationByDay    = By.cssSelector("[href='#reports/call-durations']");
	By callVolumeByDay      = By.cssSelector("[href='#reports/call-vol-day']");
	By callVolumeByDayTab   = By.cssSelector("[href='#reports/call-vol-day-tab']");
	By conferenceCallTab    = By.cssSelector("[href='#reports/conference-calls']");
	By supvisorDashboardTab = By.cssSelector("[href='#reports/supervisor']");
	By userBusySummary      = By.cssSelector("[href='#reports/user-busy-presence-summary']");
	By userPresenceDailyTotals = By.cssSelector("[href='#reports/user-presence-daily-totals']");
	
	By callReports   		= By.cssSelector("[href='#reports/call-reports']");
	By callReportsUTC   	= By.cssSelector("[href='#reports/call-reports-utc']");
	By callReportsTwilio   	= By.cssSelector("[href='#reports/call-reports-twilio']");
	By twilioAudit          = By.cssSelector("[href='#reports/twilio-audit']");
	By slaReport   			= By.cssSelector("[href='#reports/sla-report']");
	By userCountsReport  	= By.cssSelector("[href='#reports/user-count']");
	By spamCallsReport  	= By.cssSelector("[href='#reports/spam-calls']");
	By callTransferReport  	= By.cssSelector("[href='#reports/call-transfer']");
	By billingReport  	    = By.cssSelector("[href='#reports/billing']");
	By billingIntReport  	= By.cssSelector("[href='#reports/billing-international']");
	
	//collapse locators
	By collapcallRecordings = By.xpath("//a[@href = '#call-recordings']");
	By collapCallQueues     = By.xpath("//a[@data-target='#nav-Call Queues']");
	By collapTeams          = By.xpath("//a[@data-target='#nav-Teams']");
	By collapUsers          = By.xpath("//a[@data-target='#nav-Users']");
	By collapReports        = By.xpath("//a[@data-target='#nav-Reports']");
	
	// customer data
	By customerDataLink		= By.cssSelector("[href='#cdm']");
	//yoda ai
	By yodaAi               = By.cssSelector("[href='#yoda-ai']");
	
	//login
	By welcomeToLogin       = By.cssSelector(".welcome");
	By signIn               = By.cssSelector(".signIn");
	By ringdnaFooterText    = By.cssSelector("[href='http://www.ringdna.com']");
	
	/**
	 * @param driver
	 * navigate customer data page
	 */
	public void navigateToCustomerDataPage(WebDriver driver) {
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, customerDataLink);
		clickElement(driver, customerDataLink);
		isPaceBarInvisible(driver);
	}
		
	
	public void redirectHomePage(WebDriver driver){
		clickElement(driver, ringDNALogo);
		isPaceBarInvisible(driver);
	}
	
	public void clickHomeBars(WebDriver driver) {
		if(isElementVisible(driver, homeBars, 5)) {
			clickElement(driver, homeBars);
		}
		else {
			clickByJs(driver, homeBarsReact);
		}
		isPaceBarInvisible(driver);
	}
	
	public void openDashBoardOptions(WebDriver driver, By locator){
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		if (findElement(driver, locator).getAttribute("class").contains("down")) {
			clickElement(driver, locator);
		}
	}
	
	
	public List<String> getSideMeduItems(WebDriver driver){
		return getTextListFromElements(driver, sideMenuList);
	}
	
	/*******Call Flow section starts here*******/
	public void openCallFlowPage(WebDriver driver) {
		openDashBoardOptions(driver, callFlowLink);
	}
	
	public void navigateToAddNewCallFlow(WebDriver driver) {
		openCallFlowPage(driver);
		waitUntilVisible(driver, callFlowAddNewLink);
		clickElement(driver, callFlowAddNewLink);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToManageCallFlow(WebDriver driver) {
		openCallFlowPage(driver);
		waitUntilVisible(driver, callFlowManageLink);
		clickElement(driver, callFlowManageLink);
		isPaceBarInvisible(driver);
	}
	
	/*******Call Flow section ends here*******/
	
	/*******Admin section starts here*******/
	public void clickAccountsLink(WebDriver driver) {
		isPaceBarInvisible(driver);
		waitUntilInvisible(driver, spinnerWheel);
		isElementVisible(driver, siteTitle, 10);
		if(isElementVisible(driver, accountsLink, 2)) {
			String qaUserGroup = TestBase.CONFIG.getProperty("qa_user_account");
			String qaUserSaleforceID = TestBase.CONFIG.getProperty("qa_user_account_salesforrce_id");
			waitUntilVisible(driver, accountsLink);
			clickElement(driver, accountsLink);
			supportAccounts.searchAndNavigateToAccount(driver, qaUserGroup, qaUserSaleforceID);
		}else {
			waitUntilVisible(driver, adminAccountLink);
			clickElement(driver, adminAccountLink);
			isPaceBarInvisible(driver);
		}
	}
	
	public void clickAccountsLink(WebDriver driver, String qaAccount, String qaSalesForceId) {
		isPaceBarInvisible(driver);
		waitUntilInvisible(driver, spinnerWheel);
		isElementVisible(driver, siteTitle, 10);
		if(isElementVisible(driver, accountsLink, 5)) {
			waitUntilVisible(driver, accountsLink);
			clickElement(driver, accountsLink);
			isPaceBarInvisible(driver);
			supportAccounts.searchAndNavigateToAccount(driver, qaAccount, qaSalesForceId);
		}else {
			waitUntilVisible(driver, adminAccountLink);
			clickElement(driver, adminAccountLink);
			isPaceBarInvisible(driver);
		}
	}
	
	public void navigateAndVerifyAccountDeleted(WebDriver driver, String accountName, String salesforceID, String deletedDate) {
		isPaceBarInvisible(driver);
		waitUntilInvisible(driver, spinnerWheel);
		isElementVisible(driver, siteTitle, 10);
		waitUntilVisible(driver, accountsLink);
		clickElement(driver, accountsLink);
		isPaceBarInvisible(driver);
		supportAccounts.enterAccountsName(driver, accountName);
		supportAccounts.enterSalesforceId(driver, salesforceID);
		supportAccounts.clickSearchButton(driver);
		isPaceBarInvisible(driver);
		supportAccounts.verifyAccountDeleted(driver, accountName, deletedDate);
	}
	
	public void openCallDashboardPage(WebDriver driver){
		/*
		 * waitUntilVisible(driver, callDashboardLink);
		 * assertTrue(getElementsText(driver,
		 * callDashboardLink).contains("ringDNA Live")); clickByJs(driver,
		 * callDashboardLink);
		 */
		driver.get(TestBase.CONFIG.getProperty("call_dashboard_react_url"));
	}
	
	public void verifyCallDashboardLinkInvisible(WebDriver driver){
		isPaceBarInvisible(driver);
		waitUntilInvisible(driver, callDashboardLink);
	}
	
	public void openDynamicNumberInsertionPage(WebDriver driver){
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, dynamicNumberInsertion);
		clickElement(driver, dynamicNumberInsertion);
		isPaceBarInvisible(driver);
	}
	
	public void openAdvanceCallTrackingPage(WebDriver driver){
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, advanceCallTracking);
		clickElement(driver, advanceCallTracking);
		isPaceBarInvisible(driver);
	}
	
	public void openBasicCallTrackingPage(WebDriver driver) {
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, basicCallTracking);
		clickElement(driver, basicCallTracking);
		isPaceBarInvisible(driver);
	}
	
	public void openCustomCallTrackingPage(WebDriver driver) {
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, customCallTracking);
		clickElement(driver, customCallTracking);
		isPaceBarInvisible(driver);
	}
	
	public void openOfflineCallTrackingPage(WebDriver driver) {
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, offlineCallTracking);
		clickElement(driver, offlineCallTracking);
		isPaceBarInvisible(driver);
	}
	
	public void openGoogleAdwordsCallTrackingPage(WebDriver driver) {
		openDashBoardOptions(driver, callTrackingLink);
		waitUntilVisible(driver, googleCallTracking);
		clickElement(driver, googleCallTracking);
		isPaceBarInvisible(driver);
	}
	
	//Open Conversation AI tab
	public void clickConversationAI(WebDriver driver) {
		driver.switchTo().defaultContent();
		pressEscapeKey(driver);
		isPaceBarInvisible(driver);
		if (!isElementVisible(driver, expandedSideBar, 0)) {
			if(driver.getCurrentUrl().contains("analytics") || isElementVisible(driver, caiLogo, 0)) {
				System.out.println("On CAI Analaytics Page");
				refreshCurrentDocument(driver);
				waitUntilVisible(driver, CallsTabReactPage.callsTabLink);
				clickElement(driver, CallsTabReactPage.callsTabLink);
				return;
			}
			clickHomeBars(driver);
			if((!isElementVisible(driver, expandedSideBar, 0))){
				clickHomeBars(driver);
			}
			waitUntilVisible(driver, conversationAILink);
		}
		clickElement(driver, conversationAILink);
		isPaceBarInvisible(driver);
	}
	
	/**click CAI learn more link tab and verify page
	 * @param driver
	 */
	public void clickCAILearnMoreLink(WebDriver driver) {
		waitUntilVisible(driver, caiLearnMoreLink);
		String text = findElement(driver, caiLearnMoreLink).findElement(By.xpath("..//div")).getText();
		assertEquals(text, "Learn More");
		clickElement(driver, caiLearnMoreLink);
		switchToTab(driver, getTabCount(driver));
		waitForPageLoaded(driver);
		assertEquals(driver.getCurrentUrl(), "https://www.ringdna.com/lp/conversationai");
		waitUntilVisible(driver, findElement(driver, By.xpath("//h1[text()='Unlock the Power of Your ringDNA Call Recording Data Using AI']")));
	}
	
	//Open Conversation AI tab
	public void clickCallRecording(WebDriver driver) {
		isPaceBarInvisible(driver);
		if (!isElementVisible(driver, callRecordingLink, 5)) {
			clickHomeBars(driver);
			waitUntilVisible(driver, callRecordingLink);
		}
		clickByJs(driver, callRecordingLink);
		isPaceBarInvisible(driver);
		idleWait(2);
	}
	
	/*******Admin section ends here*******/
	
	public void clickOnSiteTitle(WebDriver driver){
		waitUntilVisible(driver, siteTitle);
		clickElement(driver, siteTitle);
		isPaceBarInvisible(driver);
	}
	
	public void clickOnUserProfile(WebDriver driver) {
		navigateToMySettings(driver);
	}
	
	public void openUserProfileInNewTab(WebDriver driver) {
		clickOnUserProfileDropDown(driver);
		waitUntilVisible(driver, mySettingsLink);
		Actions actions = new Actions(driver);
		actions.keyDown(Keys.CONTROL).click(findElement(driver, mySettingsLink)).keyUp(Keys.CONTROL).build().perform();
		switchToTab(driver, getTabCount(driver));
		isPaceBarInvisible(driver);
	}
	
	public void clickOnUserProfileDropDown(WebDriver driver) {
		waitUntilVisible(driver, userProfileDropDownToggle);
		clickElement(driver, userProfileDropDownToggle);
	}
	
	public boolean isCAISetupDropDownVisible(WebDriver driver){
		waitUntilVisible(driver, userProfileDropDownToggle);
		clickElement(driver, userProfileDropDownToggle);
		return isElementVisible(driver, userDropdownCAISetup, 5);
	}
	
	public void navigateToCAISetup(WebDriver driver) {
		if (driver.getCurrentUrl().contains("analytics")) {
			waitUntilVisible(driver, settingsLinkAnalytics);
			clickElement(driver, settingsLinkAnalytics);
		} else {
			waitUntilVisible(driver, userProfileDropDownToggle);
			clickElement(driver, userProfileDropDownToggle);
			clickElement(driver, userDropdownCAISetup);
		}
	}
	
	public void navigateToCAINotifications(WebDriver driver) {
		SettingsPage settingsPage = new SettingsPage();
		navigateToCAISetup(driver);
		clickElement(driver, userDropDownCAINotifications);
		settingsPage.clickNotificationSettings(driver);
	}

	public boolean isCAINotificationsDropDownVisible(WebDriver driver){
		waitUntilVisible(driver, userProfileDropDownToggle);
		clickElement(driver, userProfileDropDownToggle);
		return isElementVisible(driver, userDropDownCAINotifications, 5);
	}
	
	public void clickCAINotifications(WebDriver driver) {
		waitUntilVisible(driver, userProfileDropDownToggle);
		clickElement(driver, userProfileDropDownToggle);
		waitUntilVisible(driver, userDropDownCAINotifications);
		clickElement(driver, userDropDownCAINotifications);
	}
	
	/**
	 * @param driver
	 * @return
	 */
	public boolean navigateToMySettings(WebDriver driver) {
		boolean value = false;
		clickOnUserProfileDropDown(driver);
		if (driver.getCurrentUrl().contains("analytics")) {
			waitUntilVisible(driver, adminSettingsLink);
			clickElement(driver, adminSettingsLink);
			switchToTab(driver, getTabCount(driver));
			value = true;
		} else {
			waitUntilVisible(driver, mySettingsLink);
			clickElement(driver, mySettingsLink);
			isPaceBarInvisible(driver);
		}
		return value;
	}
	
	public void logoutAdminApp(WebDriver driver) {
		clickOnUserProfileDropDown(driver);
		waitUntilVisible(driver, logoutLink);
		clickElement(driver, logoutLink);
	}
	
	public void openAddNewUsersPage(WebDriver driver){
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		if (findElement(driver, usersLink).getAttribute("class").contains("down")) {
			clickElement(driver, usersLink);
		}
		waitUntilVisible(driver, usersAddNewLink, 5);
		clickElement(driver, usersAddNewLink);
		isPaceBarInvisible(driver);
	}
	
	public void openManageUsersPage(WebDriver driver){
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		if (!isElementVisible(driver, usersManageLink, 0)) {
			clickElement(driver, usersLink);
		}
		waitUntilVisible(driver, usersManageLink, 5);
		clickElement(driver, usersManageLink);
		isPaceBarInvisible(driver);
	}
	
	public void openSmartNumbersTab(WebDriver driver){
		waitUntilVisible(driver, smartNumbersLink);
		scrollToElement(driver, smartNumbersLink);
		clickElement(driver, smartNumbersLink);
		isPaceBarInvisible(driver);
	}
	
	public void openCallInspectorPage(WebDriver driver){
		clickElement(driver, callsLink);
		waitUntilVisible(driver, callInstpectorTitle);
		isPaceBarInvisible(driver);
	}
	
	public void isPaceBarInvisible(WebDriver driver){
		isElementVisible(driver, paceBar, 2);
		isElementInvisible(driver, paceBar, 60);
	}
	
	public void assertDashBoardForAgentUser(WebDriver driver) {
		assertFalse(isElementVisible(driver, callsLink, 2));
		assertFalse(isElementVisible(driver, messagesLink, 2));
		assertFalse(isElementVisible(driver, systemLink, 2));
		assertFalse(isElementVisible(driver, accountsLink, 2));
		assertFalse(isElementVisible(driver, callFlowLink, 2));
		assertFalse(isElementVisible(driver, smartNumbersLink, 2));
		assertFalse(isCallTrackingVisible(driver));
		assertTrue(isElementVisible(driver, callRecordingLink, 2));
	}
	
	public void assertDashBoardForAdminUser(WebDriver driver) {
		assertFalse(isElementVisible(driver, callsLink, 2));
		assertFalse(isElementVisible(driver, messagesLink, 2));
		assertFalse(isElementVisible(driver, systemLink, 2));
	}
	
	public void assertDashBoardForSupportUser(WebDriver driver) {
		assertTrue(isElementVisible(driver, callsLink, 5));
		assertTrue(isElementVisible(driver, messagesLink, 5));
		assertTrue(isElementVisible(driver, systemLink, 5));
		assertTrue(isElementVisible(driver, smartNumbersLink, 5));
		assertTrue(isElementVisible(driver, usersLink, 5));
		assertTrue(isCallTrackingVisible(driver));
	}
	
	public boolean isCallTrackingVisible(WebDriver driver){
		return isElementVisible(driver, callTrackingLink, 5);
	}
	
	public boolean isConversationAIVisible(WebDriver driver){
		boolean answer = true;
		clickConversationAI(driver);
		if(isElementVisible(driver, caiLicenseRequired, 6)) {
			answer =  false;
		}else {
			answer=  true;
		}
		driver.navigate().back();
		return answer;
	}
	
	/*******Messages section starts here*******/
	
	public void navigateToMessagesSection(WebDriver driver) {
		waitUntilVisible(driver, messagesLink);
		clickElement(driver, messagesLink);
		isPaceBarInvisible(driver);
	}
	
	/*******Messages section ends here*******/
	
	/*******Profiles section starts here*******/
	
	public void navigateToProfilesSection(WebDriver driver) {
		waitUntilVisible(driver, profilesLink);
		scrollToElement(driver, profilesLink);
		clickElement(driver, profilesLink);
		isPaceBarInvisible(driver);
	}
	
	public boolean isProfileSectionVisible(WebDriver driver){
		return isElementVisible(driver, profilesLink, 5);
	}
	
	/*******Profiles section ends here*******/
	
	
	/*******Import section starts here*******/
	
	public void openImportPage(WebDriver driver) {
		openDashBoardOptions(driver, importLink);
	}
	
	public void navigateToOutBoundNumbers(WebDriver driver) {
		openImportPage(driver);
		waitUntilVisible(driver, outBoundNumbers);
		scrollIntoView(driver, outBoundNumbers);
		clickElement(driver, outBoundNumbers);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToImportUsers(WebDriver driver) {
		openImportPage(driver);
		waitUntilVisible(driver, importUsers);
		scrollIntoView(driver, importUsers);
		clickElement(driver, importUsers);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToImportBlockedNumbers(WebDriver driver) {
		openImportPage(driver);
		waitUntilVisible(driver, importBlockedNumbers);
		scrollIntoView(driver, importBlockedNumbers);
		clickElement(driver, importBlockedNumbers);
		isPaceBarInvisible(driver);
	}
	
	/*******Import section ends here*******/
	
	/*******System section starts here*******/
	public void openSystemPage(WebDriver driver) {
		openDashBoardOptions(driver, systemLink);
	}
	
	public void navigateToDeleteNumberPage(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, deleteNumbers);
		clickElement(driver, deleteNumbers);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToNumberInspectorPage(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, numberInspector);
		clickElement(driver, numberInspector);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToNumberTransferPage(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, numberTransfer);
		clickElement(driver, numberTransfer);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToDomainBlacklist(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, domainBlackList);
		clickElement(driver, domainBlackList);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCleanUpRecordings(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, cleanUpRecording);
		clickElement(driver, cleanUpRecording);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToDeployments(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, deployments);
		clickElement(driver, deployments);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToSipRouting(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, sipRouting);
		clickElement(driver, sipRouting);
		isPaceBarInvisible(driver);
	}
		
	public void navigateToSplitAccount(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, splitAccount);
		scrollToElement(driver, splitAccount);
		clickElement(driver, splitAccount);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToServiceUser(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, serviceUser);
		scrollToElement(driver, serviceUser);
		clickElement(driver, serviceUser);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToDowntimeEvents(WebDriver driver) {
		openSystemPage(driver);
		waitUntilVisible(driver, downtimeEvents);
		scrollToElement(driver, downtimeEvents);
		clickElement(driver, downtimeEvents);
		isPaceBarInvisible(driver);
	}
	
	/*******System section ends here*******/

	/*******Reports section stats here*****/
	
	public void openReportPage(WebDriver driver) {
		openDashBoardOptions(driver, reportLink);
	}
	
	public void navigateToCallLogsReport(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, callLogsReport);
		waitUntilVisible(driver, callLogsReport);
		clickElement(driver, callLogsReport);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallReports(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, callReports);
		waitUntilVisible(driver, callReports);
		clickElement(driver, callReports);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallReportsUTC(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, callReportsUTC);
		waitUntilVisible(driver, callReportsUTC);
		clickElement(driver, callReportsUTC);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallReportsTwilio(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, callReportsTwilio);
		waitUntilVisible(driver, callReportsTwilio);
		clickElement(driver, callReportsTwilio);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToSLAReport(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, slaReport);
		waitUntilVisible(driver, slaReport);
		clickByJs(driver, slaReport);
		isPaceBarInvisible(driver);
	}	
	
	public void navigateToUserCountsReport(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, userCountsReport);
		waitUntilVisible(driver, userCountsReport);
		clickElement(driver, userCountsReport);
		isPaceBarInvisible(driver);
	}	
	
	public void navigateToCallDistribution(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callDistribution);
		clickElement(driver, callDistribution);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToUserBusyPresenceSummaryByDay(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, UserSummaryByDay);
		clickElement(driver, UserSummaryByDay);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToAvgCallDurationByDay(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, avgCallByDay);
		clickElement(driver, avgCallByDay);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToAvgCallDurationByDayTabular(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, avgCallByDayTab);
		clickElement(driver, avgCallByDayTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToQBRreport(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, qBRreport);
		clickElement(driver, qBRreport);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToNewAccounts(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, newAccounts);
		clickElement(driver, newAccounts);
		isPaceBarInvisible(driver);
	}

	public void navigateToCallTypesByDay(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callTypesByDay);
		clickElement(driver, callTypesByDay);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallTypesByDayTabular(WebDriver driver) {
		openReportPage(driver);
											 
		waitUntilVisible(driver, callTypesByDayTab);
		clickElement(driver, callTypesByDayTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallDurationByDayTabular(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callDurationByDayTab);
		clickElement(driver, callDurationByDayTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallDurationByDay(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callDurationByDay);
		clickElement(driver, callDurationByDay);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallVolumeByDay(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callVolumeByDay);
		clickElement(driver, callVolumeByDay);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToCallVolumeByDayTab(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, callVolumeByDayTab);
		clickElement(driver, callVolumeByDayTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToConferenceCallTab(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, conferenceCallTab);
		clickElement(driver, conferenceCallTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToSupervisorDashboardTab(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, supvisorDashboardTab);
		clickElement(driver, supvisorDashboardTab);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToUserBusyPresenceSummary(WebDriver driver) {
		openReportPage(driver);
		waitUntilVisible(driver, userBusySummary);
		clickElement(driver, userBusySummary);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToSpamCallsReport(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, spamCallsReport);
		waitUntilVisible(driver, spamCallsReport);
		clickByJs(driver, spamCallsReport);
		isPaceBarInvisible(driver);
	}

	public void navigateToCallTransferReport(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, callTransferReport);
		waitUntilVisible(driver, callTransferReport);
		clickElement(driver, callTransferReport);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToUserPresenceDailyTotals(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, userPresenceDailyTotals);
		waitUntilVisible(driver, userPresenceDailyTotals);
		clickElement(driver, userPresenceDailyTotals);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToTwilioAudit(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, twilioAudit);
		waitUntilVisible(driver, twilioAudit);
		clickElement(driver, twilioAudit);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToBilling(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, billingReport);
		waitUntilVisible(driver, billingReport);
		clickElement(driver, billingReport);
		isPaceBarInvisible(driver);
	}
	
	public void navigateToBillingInternational(WebDriver driver) {
		openReportPage(driver);
		scrollToElement(driver, billingIntReport);
		waitUntilVisible(driver, billingIntReport);
		clickElement(driver, billingIntReport);
		isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @return is tool tip visible
	 */
	public boolean isToolTipVisible(WebDriver driver) {
		return isElementVisible(driver, toolTip, 6);
	}
	
	/**
	 * @param driver
	 * tool tip on Dashboard Links
	 */
	public void verifyToolTipOnDashboardLinks(WebDriver driver) {
		waitUntilVisible(driver, collapcallRecordings);
		hoverElement(driver, collapcallRecordings);
		assertTrue(isToolTipVisible(driver));
		
		waitUntilVisible(driver, collapCallQueues);
		hoverElement(driver, collapCallQueues);
		assertTrue(isToolTipVisible(driver));
		
		waitUntilVisible(driver, collapTeams);
		hoverElement(driver, collapTeams);
		assertTrue(isToolTipVisible(driver));
		
		waitUntilVisible(driver, collapUsers);
		scrollToElement(driver, collapUsers);
		hoverElement(driver, collapUsers);
		assertTrue(isToolTipVisible(driver));
		
		waitUntilVisible(driver, collapReports);
		hoverElement(driver, collapReports);
		assertTrue(isToolTipVisible(driver));
	}
	
	/**
	 * @param driver
	 * open smart number in collapsed mode
	 */
	public void openSmartNumberInCollapsed(WebDriver driver) {
		waitUntilVisible(driver, smartNumbersLink);
		clickElement(driver, smartNumbersLink);
	}
	
	/**
	 * @param driver
	 * open Teams in collapsed mode
	 */
	public void openTeamsInCollapsed(WebDriver driver) {
		waitUntilVisible(driver, collapTeams);
		clickElement(driver, collapTeams);
	}
	
	/**
	 * @param driver
	 * teams sub option visible
	 */
	public void verifyTeamsSubOptionsVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, teamsAddLink, 6));
		assertTrue(isElementVisible(driver, teamsManageLink, 6));
	}
	
	public boolean isExpandedSideBarVisible(WebDriver driver) {
		boolean flag = false;
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		if (findElement(driver, reportLink).getAttribute("class").contains("down")) {
			flag = isElementClickable(driver, reportLink, 6);
		}
		return flag;
	}
	
	public void openAddNewTeamsPage(WebDriver driver){
		isPaceBarInvisible(driver);
		scrollTillEndOfPage(driver);
		if (findElement(driver, teamsLink).getAttribute("class").contains("down")) {
			clickElement(driver, teamsLink);
		}
		waitUntilVisible(driver, teamsAddLink, 5);
		clickElement(driver, teamsAddLink);
		isPaceBarInvisible(driver);
		waitUntilVisible(driver, teamsAddLinkHeading);
	}
	
	//yoda ai
	/**
	 * @param driver
	 */
	public void openYodaAiTab(WebDriver driver) {
		waitUntilVisible(driver, yodaAi);
		clickElement(driver, yodaAi);
		isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 */
	public boolean isYodaAiTabVisible(WebDriver driver) {
		return isElementVisible(driver, yodaAi, 10);
	}
	
	/**
	 * @param driver
	 * verify login page message by ringdna
	 */
	public void verifyWelcomeLoginMessage(WebDriver driver) {
		waitUntilVisible(driver, welcomeToLogin);
		assertTrue(getElementsText(driver, welcomeToLogin).equals("Welcome to ringDNA Console"));
		waitUntilVisible(driver, signIn);
		assertTrue(getElementsText(driver, signIn).equals("Sign into ringDNA Console with your Salesforce username and password."));
	}
	
	/**
	 * @param driver
	 * @return is ringdna footer text visible
	 */
	public boolean isRingdnaFooterTextVisible(WebDriver driver) {
		return isElementPresent(driver, ringdnaFooterText, 10);
	}
}