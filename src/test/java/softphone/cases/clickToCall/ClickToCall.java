package softphone.cases.clickToCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.TestRailUpdate;
import softphone.base.SoftphoneBase;
import utility.HelperFunctions;

public class ClickToCall extends SoftphoneBase implements ITest {

	private String contactName;
	private String contactNumber;
	private String leadName;
	private String leadNumber;
	private String accountName;
	private String accountNumber;
	private String clickToCallSubject;
	private ThreadLocal<String> testName = new ThreadLocal<>();
	private List<String> ctcmethodNameList = new ArrayList<String>();
	private static String excelFilePath = System.getProperty("user.dir").concat("\\src\\test\\resources\\ClickToCallData.xls");
	private static WebDriver ctcDriver = null;
	private static String ctcDriverString;
	
	@Parameters({"ctcRunType"})
	@BeforeClass(groups = { "Regression" })
	public void beforeClass(@Optional() String ctcRunType) {
		if(ctcRunType.equals("classic")) {
			ctcDriverString = "driver1";
			initializeDriverSoftphone(ctcDriverString);
			driverUsed.put(ctcDriverString, true);
			ctcDriver = driver1;
		}
		else {
			ctcDriverString = "adminDriver";
			initializeDriverSoftphone(ctcDriverString);
			driverUsed.put(ctcDriverString, true);
			ctcDriver = adminDriver;
		}
		
		softPhoneSettingsPage.clickSettingIcon(ctcDriver);
		softPhoneSettingsPage.disableclickToCallSetting(ctcDriver);
	}
	
	@DataProvider
	public Object[][] clickToCallDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 1);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		String[][] testObjArray = new String[totalNumberOfRows - 1][2];

		for (int i = 1; i <= totalNumberOfRows - 1; i++) {

			contactName = excelDataManager.getCellValue(i, 0);
			contactNumber = excelDataManager.getCellValue(i, 1);
			testObjArray[i - 1][0] = contactName;
			testObjArray[i - 1][1] = contactNumber;
		}
		return (testObjArray);
	}

	@Test(groups = { "Regression" }, dataProvider = "clickToCallDP")
	public void click_to_call_contact(String contactName, String contactNumber) {
		System.out.println("Test case --click_to_call_contact-- started ");

		// updating the driver used
		initializeDriverSoftphone(ctcDriverString);
		driverUsed.put(ctcDriverString, true);

		// Switch to salesforce tab
		sfCampaign.openSalesforceCampaignPage(ctcDriver);
		
		// verifying in lightning or classic
		if (ctcDriverString.equals("driver1")) {
			assertFalse(salesforceHomePage.isSFPageInLightningMode(ctcDriver));
			
			// Search Contact for classic
			sfSearchPage.enterGlobalSearchText(ctcDriver, contactName);
			sfSearchPage.clickGlobalSearchButton(ctcDriver);
			assertTrue(sfSearchPage.isSearchedContactVisible(ctcDriver));
		} else {
			salesforceHomePage.switchToLightningMode(ctcDriver);
			assertTrue(salesforceHomePage.isSFPageInLightningMode(ctcDriver));

			// Search Contact for lightning
			sfSearchPage.enterSearchTextandSelect(ctcDriver, contactName);
		}

		// Read Contact details from excel file
		String contactNumberSimpleFormat = HelperFunctions.getNumberInSimpleFormat(contactNumber);
		String sfSearchPageWindow = ctcDriver.getWindowHandle();
		
		//verify csv format and sfdc format
		String sfdcContctNumber = sfSearchPage.getSFDCContactPhone(ctcDriver);
		assertEquals(contactNumber, sfdcContctNumber);
		
		// Click on Contact phone number from List View
		sfSearchPage.clickContactPhoneLink(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToExtension(ctcDriver);
		String extensionWindow = ctcDriver.getWindowHandle();

		// Verify contact details (Name, phone number) on Softphone
		assertEquals(callScreenPage.getCallerName(ctcDriver), contactName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), contactNumberSimpleFormat);

		// Open call history on softphone
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);

		// Switch to Salesforce tab and open contact details
		sfSearchPage.switchToTab(ctcDriver, 2);
		sfSearchPage.clickContactNameLink(ctcDriver, contactName);

		// Click on Contact phone number from Details View
		contactDetailPage.clickContactPhoneLinkDetailsExt(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify contact details (Name, phone number) on Softphone
		assertEquals(callScreenPage.getCallerName(ctcDriver), contactName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), contactNumberSimpleFormat);

		// Dial call and Verify user image busy
		softPhoneCalling.clickCallBackButton(ctcDriver);
		softPhoneCalling.waitUntilInvisible(ctcDriver, seleniumBase.spinnerWheel);
		callScreenPage.verifyUserImageBusy(ctcDriver);
		
		// Verify contact details (Name, phone number) on Softphone during call
		assertEquals(callScreenPage.getCallerName(ctcDriver), contactName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), contactNumberSimpleFormat);

		// Update and save call notes
		callToolsPanel.clickCallNotesIcon(ctcDriver);
		clickToCallSubject = callToolsPanel.getCallNotesSubject(ctcDriver)
				+ " ctc".concat(HelperFunctions.GetRandomString(3));
		String clickToCallNotes = "CTCnote".concat(HelperFunctions.GetRandomString(3));
		callToolsPanel.clickCallNotesCancelBtn(ctcDriver);
		callToolsPanel.enterCallNotes(ctcDriver, clickToCallSubject, clickToCallNotes);

		// Hang up Active call
		softPhoneCalling.hangupIfInActiveCall(ctcDriver);

		// Open call entry in SFDC and navigate to Call History
		softPhoneActivityPage.openTaskInSalesforce(ctcDriver, clickToCallSubject);
		String sfTaskPageWindow = ctcDriver.getWindowHandle();
		
		//switching to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);
		
		// Switch to Task page salesforce
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);
		
		// Click on Contact phone number from Task View (Subject)
		sfTaskDetailPage.clickTaskSubjectPhoneNumber(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify contact details (Name, phone number) on Softphone
		assertEquals(callScreenPage.getCallerName(ctcDriver), contactName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), contactNumberSimpleFormat);
		ctcDriver.close();
		
		// Closing salesforce search page
		sfSearchPage.switchToTabWindow(ctcDriver, sfSearchPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		// Closing salesforce task page window
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		sfSearchPage.switchToTab(ctcDriver, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put(ctcDriverString, false);
		System.out.println("Test case --click_to_call_contact-- passed ");
	}

	@DataProvider
	public Object[][] clickToLeadDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 2);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		String[][] testObjArray = new String[totalNumberOfRows - 1][2];

		for (int i = 1; i <= totalNumberOfRows - 1; i++) {
			leadName = excelDataManager.getCellValue(i, 0);
			leadNumber = excelDataManager.getCellValue(i, 1);
			testObjArray[i - 1][0] = leadName;
			testObjArray[i - 1][1] = leadNumber;
		}
		return (testObjArray);
	}

	@Test(groups = { "Regression" }, dataProvider = "clickToLeadDP")
	public void click_to_call_lead(String leadName, String leadNumber) {
		System.out.println("Test case --click_to_call_lead-- started ");
		
		// updating the driver used
		initializeDriverSoftphone(ctcDriverString);
		driverUsed.put(ctcDriverString, true);

		// Switch to salesforce tab
		sfCampaign.openSalesforceCampaignPage(ctcDriver);
		
		// verifying in lightning or classic
		if (ctcDriverString.equals("driver1")) {
			assertFalse(salesforceHomePage.isSFPageInLightningMode(ctcDriver));
			
			// Search Lead for classic
			sfSearchPage.enterGlobalSearchText(ctcDriver, leadName);
			sfSearchPage.clickGlobalSearchButton(ctcDriver);
			assertTrue(sfSearchPage.isSearchedLeadVisible(ctcDriver));
		} else {
			salesforceHomePage.switchToLightningMode(ctcDriver);
			assertTrue(salesforceHomePage.isSFPageInLightningMode(ctcDriver));

			// Search Lead for lightning
			sfSearchPage.enterSearchTextandSelect(ctcDriver, leadName);
		}
		
		// Read Lead details from excel file
		String leadNumberSimpleFormat = HelperFunctions.getNumberInSimpleFormat(leadNumber);
		String sfSearchPageWindow = ctcDriver.getWindowHandle();
		
		//verify csv format and sfdc format
		String sfdcLeadNumber = sfSearchPage.getSFDCLeadPhone(ctcDriver);
		assertEquals(leadNumber, sfdcLeadNumber);
		
		// Click on Lead phone number from List View
		sfSearchPage.clickLeadPhoneLink(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToExtension(ctcDriver);
		String extensionWindow = ctcDriver.getWindowHandle();

		// Verify Lead details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(ctcDriver), leadName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), leadNumberSimpleFormat);

		// Open call history on softphone
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);

		// Switch to Salesforce tab and open Lead details
		sfSearchPage.switchToTab(ctcDriver, 2);
		sfSearchPage.clickLeadNameLink(ctcDriver, leadName);

		// Click on Lead phone number from Details View
		contactDetailPage.clickContactPhoneLinkDetailsExt(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify Lead details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(ctcDriver), leadName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), leadNumberSimpleFormat);

		// Dial call and Verify user image busy
		softPhoneCalling.clickCallBackButton(ctcDriver);
		softPhoneCalling.waitUntilInvisible(ctcDriver, seleniumBase.spinnerWheel);
		callScreenPage.verifyUserImageBusy(ctcDriver);

		// Verify lead details (Name, phone number) on extension during call
		assertEquals(callScreenPage.getCallerName(ctcDriver), leadName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), leadNumberSimpleFormat);

		// Update and save call notes
		callToolsPanel.clickCallNotesIcon(ctcDriver);
		clickToCallSubject = callToolsPanel.getCallNotesSubject(ctcDriver)
				+ " ctc".concat(HelperFunctions.GetRandomString(3));
		String clickToCallNotes = "CTCnote".concat(HelperFunctions.GetRandomString(3));
		callToolsPanel.clickCallNotesCancelBtn(ctcDriver);
		callToolsPanel.enterCallNotes(ctcDriver, clickToCallSubject, clickToCallNotes);

		// Hang up Active call
		softPhoneCalling.hangupIfInActiveCall(ctcDriver);

		// Open call entry in SFDC and navigate to Call History
		softPhoneActivityPage.openTaskInSalesforce(ctcDriver, clickToCallSubject);
		String sfTaskPageWindow = ctcDriver.getWindowHandle();
		
		//switching to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);

		// Switch to Task page salesforce
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);

		// Click on Lead phone number from Task View (Subject)
		sfTaskDetailPage.clickTaskSubjectPhoneNumber(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify lead details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerName(ctcDriver), leadName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), leadNumberSimpleFormat);
		ctcDriver.close();
		
		// Closing salesforce search page
		sfSearchPage.switchToTabWindow(ctcDriver, sfSearchPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		// Closing salesforce task page window
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		sfSearchPage.switchToTab(ctcDriver, 1);

		// Setting driver used to false as this test case is pass
		driverUsed.put(ctcDriverString, false);
		System.out.println("Test case --click_to_call_lead-- passed ");
	}

	@DataProvider
	public Object[][] clickToAccountDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 3);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		String[][] testObjArray = new String[totalNumberOfRows - 1][2];

		for (int i = 1; i <= totalNumberOfRows - 1; i++) {

			accountName = excelDataManager.getCellValue(i, 0);
			accountNumber = excelDataManager.getCellValue(i, 1);
			testObjArray[i - 1][0] = accountName;
			testObjArray[i - 1][1] = accountNumber;
		}
		return (testObjArray);
	}

	@Test(groups = { "Regression" }, dataProvider = "clickToAccountDP")
	public void click_to_call_account(String accountName, String accountNumber) {
		System.out.println("Test case --click_to_call_account-- started ");
		
		// updating the driver used
		initializeDriverSoftphone(ctcDriverString);
		driverUsed.put(ctcDriverString, true);

		// Switch to salesforce tab
		sfCampaign.openSalesforceCampaignPage(ctcDriver);
		
		// verifying in lightning or classic
		if (ctcDriverString.equals("driver1")) {

			assertFalse(salesforceHomePage.isSFPageInLightningMode(ctcDriver));

			// Search Account for classic
			sfSearchPage.enterGlobalSearchText(ctcDriver, accountName);
			sfSearchPage.clickGlobalSearchButton(ctcDriver);
			assertTrue(sfSearchPage.isSearchedAccountVisible(ctcDriver));
		} else {
			salesforceHomePage.switchToLightningMode(ctcDriver);
			assertTrue(salesforceHomePage.isSFPageInLightningMode(ctcDriver));

			// Search Account for lightning
			sfSearchPage.enterSearchTextandSelect(ctcDriver, accountName);
		}

		// Read Contact details from excel file
		String accountNumberSimpleFormat = HelperFunctions.getNumberInSimpleFormat(accountNumber);
		String sfSearchPageWindow = ctcDriver.getWindowHandle();
		
		//verify csv format and sfdc format
		String sfdcAccountNumber = sfSearchPage.getSFDCAccountPhone(ctcDriver);
		assertEquals(accountNumber, sfdcAccountNumber);
		
		// Click on Account phone number from List View
		sfSearchPage.clickAccountPhoneLink(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToExtension(ctcDriver);
		String extensionWindow = ctcDriver.getWindowHandle();

		// Verify Account details (Name, phone number) on Softphone
		assertEquals(callScreenPage.getCallerCompany(ctcDriver), accountName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), accountNumberSimpleFormat);

		// Open call history on softphone
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);

		// Switch to Salesforce tab and open account details
		sfSearchPage.switchToTab(ctcDriver, 2);
		sfSearchPage.clickAccountNameLink(ctcDriver, accountName);

		// Click on Account phone number from Details View
		contactDetailPage.clickContactPhoneLinkDetailsExt(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify account details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerCompany(ctcDriver), accountName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), accountNumberSimpleFormat);

		// Dial call and Verify user image busy
		softPhoneCalling.clickCallBackButton(ctcDriver);
		softPhoneCalling.waitUntilInvisible(ctcDriver, seleniumBase.spinnerWheel);
		callScreenPage.verifyUserImageBusy(ctcDriver);

		// Verify account details (Name, phone number) on Softphone during call
		assertEquals(callScreenPage.getCallerCompany(ctcDriver), accountName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), accountNumberSimpleFormat);

		// Update and save call notes
		callToolsPanel.clickCallNotesIcon(ctcDriver);
		clickToCallSubject = callToolsPanel.getCallNotesSubject(ctcDriver)
				+ " ctc".concat(HelperFunctions.GetRandomString(3));
		String clickToCallNotes = "CTCnote".concat(HelperFunctions.GetRandomString(3));
		callToolsPanel.clickCallNotesCancelBtn(ctcDriver);
		callToolsPanel.enterCallNotes(ctcDriver, clickToCallSubject, clickToCallNotes);

		// Hang up Active call
		softPhoneCalling.hangupIfInActiveCall(ctcDriver);

		// Open call entry in SFDC and navigate to Call History
		softPhoneActivityPage.openTaskInSalesforce(ctcDriver, clickToCallSubject);
		String sfTaskPageWindow = ctcDriver.getWindowHandle();
		
		// switching to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);
		softphoneCallHistoryPage.openCallsHistoryPage(ctcDriver);

		// Switch to Task page salesforce
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);

		// Click on Lead phone number from Task View (Subject)
		sfTaskDetailPage.clickTaskSubjectPhoneNumber(ctcDriver);

		// Switch to extension
		sfSearchPage.switchToTabWindow(ctcDriver, extensionWindow);

		// Verify lead details (Name, phone number) on extension
		assertEquals(callScreenPage.getCallerCompany(ctcDriver), accountName);
		assertEquals(callScreenPage.getCallerNumber(ctcDriver), accountNumberSimpleFormat);
		ctcDriver.close();
		
		// Closing salesforce search page
		sfSearchPage.switchToTabWindow(ctcDriver, sfSearchPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		// Closing salesforce task page window
		sfSearchPage.switchToTabWindow(ctcDriver, sfTaskPageWindow);
		sfSearchPage.closeTab(ctcDriver);

		sfSearchPage.switchToTab(ctcDriver, 1);
		// Setting driver used to false as this test case is pass
		driverUsed.put(ctcDriverString, false);
		System.out.println("Test case --click_to_call_account-- passed");
	}
	
	/*
	 * This method is used for over riding the current test rail mapping since we need to update test according to data provider.
	 * 
	 */
	@BeforeMethod(groups = { "Regression" })
	public void beforeMethod(Method method, Object[] testData) {

		if (method.getName().equals("aa_AddCallersAsContactsAndLeads")) {
			testName.set(method.getName());
			return;
		}
		String methodName = method.getName() + "_" + testData[0].toString().replace(" Auto", "Auto");
		testName.set(methodName);
		ctcmethodNameList.add(methodName);
		if (ctcSuiteID.equals("0") || System.getenv("BUILD_NUMBER") == null) {
			return;
		}
		TestRailUpdate.suiteWiseMethods.clear();
		TestRailUpdate.suiteWiseCases.clear();
		TestRailUpdate.suiteWiseMethods.put(Integer.parseInt(ctcSuiteID), ctcmethodNameList);
		TestRailUpdate.suiteWiseCases.put(Integer.parseInt(ctcSuiteID), testRailUpdate.getCaseIdsFromMethodName(ctcmethodNameList));
	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
