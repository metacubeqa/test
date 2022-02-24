package support.source.accounts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.util.Strings;

import base.SeleniumBase;
import support.source.commonpages.Dashboard;
import support.source.smartNumbers.SmartNumbersPage;

public class AccountLicenseTab extends SeleniumBase {

	Dashboard dashboard 		 = new Dashboard();
	Random random 				 = new Random();
	SmartNumbersPage smartNoPage = new SmartNumbersPage();
	
	By licensingTab			= By.cssSelector("[data-tab='licensing']");
	By licensingTabParent   = By.xpath(".//*[@data-tab='licensing']/ancestor::li");
	By licensingTabHeading  = By.xpath("//h2[text()='Overview']");
	By successMessage 	    = By.className("toast-message");
	
	By editLicensePencilIcon	= By.cssSelector(".glyphicon-pencil.edit-license");
	By expirationDateLoc		= By.xpath("//label[normalize-space(text()) = 'Expiration Date']/../span");
	By datePickerTab 			= By.cssSelector(".date-picker");
	By savebtn 					= By.cssSelector(".btn-success.persist");
	By smartNumbersLink			= By.xpath(".//*[@class='account-licenses']//a[contains(@href,'smart-numbers')]");
	By adminUsersLink			= By.xpath(".//*[@class='account-licenses']//a[contains(@href,'#users?') and text()='Admin Users']");
	By agentUserLink            = By.xpath(".//*[@class='account-licenses']//a[contains(@href,'#users?') and text()='Agent Users']");
	By sequenceUserLink         = By.xpath(".//*[@class='account-licenses']//a[contains(@href,'#users?') and text()='Sequence Users']");
	By conversationAIUsers      = By.xpath(".//*[@class='account-licenses']//a[contains(@href,'#users?') and text()='Conversation AI Users']");
	By emailCalUsersLink        = By.xpath(".//*[@class='account-licenses']//a[contains(@href,'#users?') and text()='Email / Calendar Users']");
	
	//Add License Selectors
	By licenseUsageTypeHeader      = By.xpath(".//*[contains(@class,'table-striped account-mods')]//th[contains(text(),'Type')]");
	By licenseUsageAllowedHeader   = By.xpath(".//*[contains(@class,'table-striped account-mods')]//th[contains(text(),'Allowed')]");
	By licenseUsageAssignedHeader  = By.xpath(".//*[contains(@class,'table-striped account-mods')]//th[contains(text(),'Assigned')]");
	By licenseUsageRemainingHeader = By.xpath(".//*[contains(@class,'table-striped account-mods')]//th[contains(text(),'Remaining')]");
	By addLicenseBtn			   = By.cssSelector(".create-license");
	By selectLicenseType		   = By.cssSelector(".form-control.type");
	By amountInputBox			   = By.cssSelector(".form-control.amount");
	By selectReasonLoc			   = By.cssSelector(".form-control.reason");
	By notesInputBox			   = By.cssSelector(".form-control.notes");
	By createBtn				   = By.cssSelector(".btn-success.create");
	By closeButton                 = By.cssSelector(".close");
	
	String allowedLicenseCount     = ".//*[contains(@class,'account-mods')]//tbody//a[contains(text(),'$type$')]/parent::td/..//td[2] | .//*[contains(@class,'account-mods')]//tbody//td[contains(text(),'$type$')]/following-sibling::td[1]";
	String assignedLicenseCount    = ".//*[contains(@class,'account-mods')]//tbody//a[contains(text(),'$type$')]/parent::td/..//td[3] | .//*[contains(@class,'account-mods')]//tbody//td[contains(text(),'$type$')]/following-sibling::td[1]";
	String remainingLicenseCount   = ".//*[contains(@class,'account-mods')]//tbody//a[contains(text(),'$type$')]/parent::td/..//td[4] | .//*[contains(@class,'account-mods')]//tbody//td[contains(text(),'$type$')]/following-sibling::td[1]";
	
	String licenseLineItem		   = ".//*[text() = '$notes$']/parent::tr//td";
	
	//License Request Selectors
	By addRequestBtn			   = By.cssSelector(".add-request");
	By licenseRequestHeaderMsg	   = By.cssSelector("#license-request-modal .modal-body p");
	By smartNumbersCheckBox		   = By.cssSelector("input.addSmartNumbers[type='checkbox']");
	By adminUsersCheckBox		   = By.cssSelector("input.addAdminUsers[type='checkbox']");
	By agentUsersCheckBox		   = By.cssSelector("input.addAgentUsers[type='checkbox']");
	By email_calendarsCheckBox	   = By.cssSelector("input.addNylasUsers[type='checkbox']");
	By caiCheckBox		           = By.cssSelector("input.addCaiUsers[type='checkbox']");
	By yodaCheckBox		           = By.cssSelector("input.addYodaAIUsers[type='checkbox']");
	By sequenceUserCheckBox        = By.cssSelector("input.addSequenceUsers[type='checkbox']");
	By effectiveDateTextBox		   = By.xpath("//label[text()='Effective date']/..//input");
	By nextDatePicker			   = By.cssSelector(".datepicker-days .next");
	By additionalDetailsTextBox	   = By.cssSelector(".details");
	By requestBtn				   = By.cssSelector(".btn-success.request");
	By dayList					   = By.cssSelector(".datepicker-days td.day:not(.disabled)");
	By disabledDaysList			   = By.cssSelector(".datepicker-days td.disabled");
	By addMinutesTextBox		   = By.cssSelector(".minutesQuantity");

  By addSmartNumbersTextBox	   = By.cssSelector("input.smartNumbersQuantity");
	By addAdminUsersTextBox		   = By.cssSelector("input.adminUsersQuantity");
	By addAgentUsersTextBox		   = By.cssSelector("input.agentUsersQuantity");
	By addEmailUsersTextBox		   = By.cssSelector("input.nylasUsersQuantity");
	By addCAIUsersTextBox		   = By.cssSelector("input.caiUsersQuantity");
	By addSequenceUsersTextBox	   = By.cssSelector(".sequenceUsersQuantity");
	By addYodaAIUsersTextBox	   = By.cssSelector(".yodaAIUsersQuantity");
	
	
	static final String requestHeaderMsg = "Select which feature you'd like to add and click \"Request\"".concat(". A ringDNA customer support will be in touch.");

	public static enum LicenseType{
		MaxSmartNumbers,
		MaxAgents,
		MaxAdmins,
		MaxNylasUsers,
		MaxConversationAiUsers,
		MaxSequenceUsers,
		MaxYodaAIUsers
	}
	
	public static enum LicenseReason{
		CustomerService,
		SalesRequested,
		TechnicalIssues,
	}
	
	public void openLicensingTab(WebDriver driver) {
		if (!findElement(driver, licensingTabParent).getAttribute("class").contains("active")) {
			waitUntilVisible(driver, licensingTab);
			clickElement(driver, licensingTab);
			dashboard.isPaceBarInvisible(driver);
			findElement(driver, licensingTabHeading);
		}
	}
	
	public String getExpirationDateText(WebDriver driver) {
		return getElementsText(driver, expirationDateLoc);
	}
	
	public void editLicense(WebDriver driver, String newExpirationDate) {
		clickEditLicenseIcon(driver);
		clickElement(driver, datePickerTab);
		enterTextandSelect(driver, datePickerTab, newExpirationDate);
		clickElement(driver, savebtn);
		isSuccessMsgInvisible(driver);
	}
	
	public void clickEditLicenseIcon(WebDriver driver) {
		waitUntilVisible(driver, editLicensePencilIcon);
		clickElement(driver, editLicensePencilIcon);
	}
	
	public void verifyLicenseUsageHeadersVisible(WebDriver driver) {
		assertTrue(isElementVisible(driver, licenseUsageTypeHeader, 5));
		assertTrue(isElementVisible(driver, licenseUsageAllowedHeader, 5));
		assertTrue(isElementVisible(driver, licenseUsageAssignedHeader, 5));
		assertTrue(isElementVisible(driver, licenseUsageRemainingHeader, 5));
	}
	
	public void addLicenseBtn(WebDriver driver) {
		waitUntilVisible(driver, addLicenseBtn);
		clickElement(driver, addLicenseBtn);
	}
	
	public void addRequestBtn(WebDriver driver) {
		waitUntilVisible(driver, addRequestBtn);
		clickElement(driver, addRequestBtn);
	}

	public void selectLicenseType(WebDriver driver, String licenseType) {
		waitUntilVisible(driver, selectLicenseType);
		clickElement(driver, selectLicenseType);
		selectFromDropdown(driver, selectLicenseType, SelectTypes.value, licenseType);
	}
	
	/**
	 * @param driver
	 * @param licenseType
	 */
	public List<String> getLicenseTypeListfromDropDown(WebDriver driver) {
		waitUntilVisible(driver, selectLicenseType);
		clickElement(driver, selectLicenseType);
		return getElementsTextFromDropDownList(driver, selectLicenseType);
	}
	
	public void selectReason(WebDriver driver, String reason) {
		selectFromDropdown(driver, selectReasonLoc, SelectTypes.value, reason);
	}
	
	public void enterLicenseAmount(WebDriver driver, String licenseAmount) {
		waitUntilVisible(driver, amountInputBox);
		enterText(driver, amountInputBox, licenseAmount);
	}
	
	public void enterLicenseNotes(WebDriver driver, String notes) {
		waitUntilVisible(driver, notesInputBox);
		enterText(driver, notesInputBox, notes);
	}
	
	public void clickCreateBtn(WebDriver driver) {
		waitUntilVisible(driver, createBtn);
		clickElement(driver, createBtn);
		waitUntilInvisible(driver, createBtn);
		isSuccessMsgInvisible(driver);
		dashboard.isPaceBarInvisible(driver);
		idleWait(1);
	}
	
	public void addLicense(WebDriver driver, String licenseType, String licenseAmount, String reason, String notes) {
		addLicenseBtn(driver);
		selectLicenseType(driver, licenseType);
		enterLicenseAmount(driver, licenseAmount);
		selectReason(driver, reason);
		enterLicenseNotes(driver, notes);
		clickCreateBtn(driver);
	}
	
	/**
	 * @param driver
	 * close add license form
	 */
	public void closeAddLicenseForm(WebDriver driver) {
		waitUntilVisible(driver, closeButton);
		clickElement(driver, closeButton);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public int getAllowedLicenseCount(WebDriver driver, String type) {
		dashboard.isPaceBarInvisible(driver);
		By allowedLicenseLoc = By.xpath(allowedLicenseCount.replace("$type$", type));
		waitUntilVisible(driver, allowedLicenseLoc);
		scrollIntoView(driver, allowedLicenseLoc);
		idleWait(3);
		String licenseText = getElementsText(driver, allowedLicenseLoc);
		return Integer.parseInt(licenseText);
	}
	
	public int getAssignedLicenseCount(WebDriver driver, String type) {
		dashboard.isPaceBarInvisible(driver);
		By assignedLicenseLoc = By.xpath(assignedLicenseCount.replace("$type$", type));
		waitUntilVisible(driver, assignedLicenseLoc);
		scrollIntoView(driver, assignedLicenseLoc);
		idleWait(3);
		String licenseText = getElementsText(driver, assignedLicenseLoc);
		return Integer.parseInt(licenseText);
	}
	
	public int getRemainingLicenseCount(WebDriver driver, String type) {
		dashboard.isPaceBarInvisible(driver);
		By remainingLicenseLoc = By.xpath(remainingLicenseCount.replace("$type$", type));
		waitUntilVisible(driver, remainingLicenseLoc);
		scrollIntoView(driver, remainingLicenseLoc);
		idleWait(1);
		String licenseText = getElementsText(driver, remainingLicenseLoc);
		idleWait(1);
		return Integer.parseInt(licenseText);
	}
	
	public void verifyAllowedCountIncreased(WebDriver driver, String type, int amount) {
		int allowedCount = getAllowedLicenseCount(driver, type);
		idleWait(1);
		System.out.println("new count: " +allowedCount);
		System.out.println("old count: " +amount);
		assertTrue(allowedCount >  amount);
	}
	
	public void verifyAssignedCountIncreased(WebDriver driver, String type, int amount) {
		int assignedCount = getAssignedLicenseCount(driver, type);
		idleWait(1);
		assertTrue(assignedCount >  amount);
	}
	
	public void verifyRemainingCountIncreased(WebDriver driver, String type, int amount) {
		int remainingCount = getRemainingLicenseCount(driver, type);
		idleWait(1);
		assertTrue(remainingCount >  amount);
	}
	
	public void verifyRemainingCountDecreased(WebDriver driver, String type, int amount) {
		int remainingCount = getRemainingLicenseCount(driver, type);
		idleWait(1);
		assertTrue(remainingCount <  amount);
	}
	
	public void verifyLicenseLineItemSections(WebDriver driver, String notes, String type, String amount, String modifiedBy, String reason) {
		By licenseLineLoc = By.xpath(licenseLineItem.replace("$notes$", notes));
		waitUntilVisible(driver, licenseLineLoc);
		scrollTillEndOfPage(driver);
		assertTrue(isTextPresentInList(driver, getElements(driver, licenseLineLoc), type));
		assertTrue(isTextPresentInList(driver, getElements(driver, licenseLineLoc), amount));
		assertTrue(isTextPresentInList(driver, getElements(driver, licenseLineLoc), modifiedBy));
		assertTrue(isTextPresentInList(driver, getElements(driver, licenseLineLoc), reason));
	}
	
	//License request section starts here
	public void addLicenseRequest(WebDriver driver, String details, String smartNumbers, String adminUsers, String agentUsers, String email_CalendarUsers, String caiUsers, String sequenceUsers, String yodaUsers) {
		verifyLicenseRequestHeader(driver);
		assertFalse(isElementVisible(driver, effectiveDateTextBox, 2));
		assertFalse(isElementVisible(driver, addMinutesTextBox, 2));

		if (Strings.isNotNullAndNotEmpty(smartNumbers)) {
			clickElement(driver, smartNumbersCheckBox);
			enterSmartNumbers(driver, smartNumbers);
		}

		if (Strings.isNotNullAndNotEmpty(adminUsers)) {
			clickElement(driver, adminUsersCheckBox);
			enterAdminUsers(driver, adminUsers);
			assertTrue(isElementVisible(driver, effectiveDateTextBox, 2));
			idleWait(1);
			clickElement(driver, effectiveDateTextBox);
			verifyPreviousDayDisabled(driver);
			waitUntilVisible(driver, nextDatePicker);
			clickElement(driver, nextDatePicker);
			selectDateRandomly(driver);
		}
		
		if(Strings.isNotNullAndNotEmpty(agentUsers)){
			clickElement(driver, agentUsersCheckBox);
			enterAgentUsers(driver, agentUsers);
		}
		
		if(Strings.isNotNullAndNotEmpty(email_CalendarUsers)){
			clickElement(driver, email_calendarsCheckBox);
			enterEmail_CalendarUsers(driver, email_CalendarUsers);
			assertTrue(isElementVisible(driver, effectiveDateTextBox, 2));
			idleWait(1);
			clickElement(driver, effectiveDateTextBox);
			verifyPreviousDayDisabled(driver);
			waitUntilVisible(driver, nextDatePicker);
			clickElement(driver, nextDatePicker);
			selectDateRandomly(driver);
		}
		
		if(Strings.isNotNullAndNotEmpty(caiUsers)){
			clickElement(driver, caiCheckBox);
			enterCAIUsers(driver, caiUsers);
		}
		
		if(Strings.isNotNullAndNotEmpty(yodaUsers)){
			clickYodaCheckBox(driver);
			enterYodaAiUsers(driver, yodaUsers);
		}
		
		if(Strings.isNotNullAndNotEmpty(sequenceUsers)){
			clickElement(driver, sequenceUserCheckBox);
			enterSequenceUsers(driver, sequenceUsers);
			assertTrue(isElementVisible(driver, effectiveDateTextBox, 2));
			idleWait(1);
			clickElement(driver, effectiveDateTextBox);
			verifyPreviousDayDisabled(driver);
			waitUntilVisible(driver, nextDatePicker);
			clickElement(driver, nextDatePicker);
			selectDateRandomly(driver);
		}
		
		enterAdditionalDetails(driver, details);
		clickRequestBtn(driver);
	}
	
	public void verifyLicenseRequestHeader(WebDriver driver){
		waitUntilVisible(driver, licenseRequestHeaderMsg);
		assertEquals(getElementsText(driver, licenseRequestHeaderMsg), requestHeaderMsg);
	}
	
	public void enterMinutes(WebDriver driver, String minutes){
		waitUntilVisible(driver, addMinutesTextBox);
		enterText(driver, addMinutesTextBox, minutes);
	}
	
	public void enterSmartNumbers(WebDriver driver, String smartNumbers){
		waitUntilVisible(driver, addSmartNumbersTextBox);
		enterText(driver, addSmartNumbersTextBox, smartNumbers);
	}
	
	public void enterAdminUsers(WebDriver driver, String adminUsers){
		waitUntilVisible(driver, addAdminUsersTextBox);
		enterText(driver, addAdminUsersTextBox, adminUsers);
	}
	
	public void enterAgentUsers(WebDriver driver, String agentUsers){
		waitUntilVisible(driver, addAgentUsersTextBox);
		enterText(driver, addAgentUsersTextBox, agentUsers);
	}
	
	public void enterEmail_CalendarUsers(WebDriver driver, String emailUsers){
		scrollToElement(driver, addEmailUsersTextBox);
		waitUntilVisible(driver, addEmailUsersTextBox);
		enterText(driver, addEmailUsersTextBox, emailUsers);
	}
	
	public void enterCAIUsers(WebDriver driver, String caiUsers){
		waitUntilVisible(driver, addCAIUsersTextBox);
		enterText(driver, addCAIUsersTextBox, caiUsers);
	}
	
	public void enterSequenceUsers(WebDriver driver, String emailUsers){
		scrollToElement(driver, addSequenceUsersTextBox);
		waitUntilVisible(driver, addSequenceUsersTextBox);
		enterText(driver, addSequenceUsersTextBox, emailUsers);
	}
	
	/**
	 * @param driver
	 * @param yodaUsers
	 * enter yoda ai users
	 */
	public void enterYodaAiUsers(WebDriver driver, String yodaUsers){
		scrollToElement(driver, addYodaAIUsersTextBox);
		waitUntilVisible(driver, addYodaAIUsersTextBox);
		enterText(driver, addYodaAIUsersTextBox, yodaUsers);
	}
	
	/**
	 * @param driver
	 * click yoda check box
	 */
	public void clickYodaCheckBox(WebDriver driver) {
		waitUntilVisible(driver, yodaCheckBox);
		clickElement(driver, yodaCheckBox);
	}
	
	public void selectDay(WebDriver driver, String date) {
		int day = Integer.parseInt(date.split("/")[1]);
		List<WebElement> daysList = getInactiveElements(driver, dayList);
		idleWait(1);
		scrollToElement(driver, daysList.get(day));
		daysList.get(day).click();
	}
	
	public void selectDateRandomly(WebDriver driver) {
		List<WebElement> daysList = getInactiveElements(driver, dayList);
		int dayIndex = random.nextInt(daysList.size());
		idleWait(1);
		scrollToElement(driver, daysList.get(dayIndex));
		daysList.get(dayIndex).click();
	}
	
	public void verifyPreviousDayDisabled(WebDriver driver) {
		List<WebElement> daysList = getInactiveElements(driver, disabledDaysList);
		int dayIndex = random.nextInt(daysList.size());
		idleWait(1);
		scrollToElement(driver, daysList.get(dayIndex));
		assertTrue(daysList.get(dayIndex).getAttribute("class").contains("disabled"));
	}
	
	public void enterAdditionalDetails(WebDriver driver, String details) {
		waitUntilVisible(driver, additionalDetailsTextBox);
		enterText(driver, additionalDetailsTextBox, details);
	}
	
	public void clickRequestBtn(WebDriver driver) {
		waitUntilVisible(driver, requestBtn);
		clickElement(driver, requestBtn);
		waitUntilInvisible(driver, requestBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void isSuccessMsgInvisible(WebDriver driver){
		isElementVisible(driver, successMessage, 1);
		waitUntilInvisible(driver, successMessage);
	}
	
	public void verifyRequestMsg(WebDriver driver) {
		assertTrue(isElementVisible(driver, successMessage , 5));
		assertEquals(getElementsText(driver, successMessage), "Your request was submitted.");
	}
	
	/**
	 * @param driver
	 * @return request message
	 */
	public String getRequestMsg(WebDriver driver) {
		assertTrue(isElementVisible(driver, successMessage , 5));
		return getElementsText(driver, successMessage);
	}
	
	public void clickSmartNumbersLink(WebDriver driver){
		waitUntilVisible(driver, smartNumbersLink);
		clickElement(driver, smartNumbersLink);
		dashboard.isPaceBarInvisible(driver);
		smartNoPage.verifySmartNumbersTitle(driver);
	}
	
	public void clickEmailCalendarUsersLink(WebDriver driver){
		waitUntilVisible(driver, emailCalUsersLink);
		clickElement(driver, emailCalUsersLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickAdminUsersLink(WebDriver driver){
		waitUntilVisible(driver, adminUsersLink);
		clickElement(driver, adminUsersLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickAgentUsersLink(WebDriver driver){
		waitUntilVisible(driver, agentUserLink);
		clickElement(driver, agentUserLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**
	 * @param driver
	 * open agent user link in new tab (manage user page)
	 */
	public void clickAgentUsersLinkInNewTab(WebDriver driver){
		waitUntilVisible(driver, agentUserLink);
		Actions actions = new Actions(driver);
		actions.keyDown(Keys.CONTROL).click(findElement(driver, agentUserLink)).keyUp(Keys.CONTROL).build().perform();
	}
	
	public void clickSequenceUsersLink(WebDriver driver){
		waitUntilVisible(driver, sequenceUserLink);
		clickElement(driver, sequenceUserLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public boolean isSequenceUsersLinkVisible(WebDriver driver){
		return isElementVisible(driver, sequenceUserLink, 6);
	}
	
	public void clickConversationAIUsersLink(WebDriver driver){
		waitUntilVisible(driver, conversationAIUsers);
		clickElement(driver, conversationAIUsers);
		dashboard.isPaceBarInvisible(driver);
	}
}
