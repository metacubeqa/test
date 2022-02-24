/**
 * 
 */
package softphone.cases.softPhoneFunctional.callRecording;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import support.source.admin.AccountCallRecordingTab;
import support.source.admin.AccountCallRecordingTab.AreaRecordOptions;
import utility.HelperFunctions;

import org.json.JSONObject;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import base.TestRailUpdate;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import softphone.base.SoftphoneBase;

/**
 * @author admin
 *
 */
public class CallRecordingSheet extends SoftphoneBase implements ITest{
	
	private ThreadLocal<String> testName = new ThreadLocal<>();
	private List<String> reocrdmethodNameList = new ArrayList<String>();
	String sessionId = null;
	
	private static String excelFilePath = System.getProperty("user.dir").concat("\\src\\test\\resources\\RecordingAutomationSheet.xls");
	boolean globalAgentOnly 		= false;
	boolean usGranular				= false;
	boolean inGranular				= false;
	
	AreaRecordOptions originUsStateSetting 	= null;
	AreaRecordOptions targetUsStateSetting 	= null;
	AreaRecordOptions originINSetting		= null;
	AreaRecordOptions targetInSetting		= null;
	String expectedResult					= null;
	
	String usOriginState	= "California";
	String usTargetState	= "Connecticut";
	String originInCountry	= "United Kingdom";
	String targetInCountry	= "Finland";
	
	String methodNameTemp = null;
	int scenarioCount = 1;
	
	OkHttpClient client = null;
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		
		driver4 = getDriver();
		SFLP.supportLogin(driver4, CONFIG.getProperty("qa_support_tool_site"), CONFIG.getProperty("qa_user_2_username"), CONFIG.getProperty("qa_user_2_password"));
		accountCallRecordingTab.setDefaultCallRecordingSettings(driver4);
		driver4.quit();
		driver4 = null;
		
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		getSessionID();
		
		if(seleniumBase.getTabCount(driver4) < 2) {
			loginSupport(driver4);
			accountCallRecordingTab.openCallRecordingTab(driver4);
		}
		
		
		driverUsed.put("driver4", false);
	}
	
	public void openCallReacordingTab() {
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		getSessionID();
		
		if(seleniumBase.getTabCount(driver4) < 2) {
			loginSupport(driver4);
			accountCallRecordingTab.openCallRecordingTab(driver4);
		}
		
		driverUsed.put("driver4", false);
	}
	
	@DataProvider
	public Object[][] callRecordingUStoUSDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 1);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		Object [][] objArray = new Object[totalNumberOfRows-2][];
		@SuppressWarnings("unchecked")
		ArrayList<String>[] testObjArray =  new ArrayList[totalNumberOfRows - 2];

		for (int i = 2; i < totalNumberOfRows; i++) {
			objArray[i-2] = new Object[1];
			testObjArray[i-2] = new ArrayList<String>();
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 1));						//globalAgentOnly
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 2));						//USGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 3));						//INGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 4));						//originUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 5));						//targetUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 6));						//originINSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 7));						//targetInSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 8));						//targetInSetting
			
			objArray[i-2][0] = testObjArray[i-2];
		}
		  return objArray;
	}
	
	@Test(groups = { "Regression" }, dataProvider = "callRecordingUStoUSDP")
	public void verify_us_to_us_call_recording(ArrayList<String> variableList) {
		
		System.out.println("Test case --verify_us_to_us_call_recording_scenario_" + scenarioCount  + "-- started ");
		
		//Set global variables
		setVariables(variableList);
		
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    openCallReacordingTab();
	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver1, "408");
	    String fromNumber = softPhoneSettingsPage.getSelectedOutboundNumber(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver3, "475");
	    String contactNumber = softPhoneSettingsPage.getSelectedOutboundNumber(driver3); 
	    
	    if(globalAgentOnly)
	    	accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
	    else
	    	accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(usGranular) {
	    	accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, usOriginState, originUsStateSetting);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, usTargetState, targetUsStateSetting);
	    }else {
	    	accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
	    	if(originUsStateSetting == AccountCallRecordingTab.AreaRecordOptions.NoConsent) {
	    		softPhoneSettingsPage.selectDefaultNumber(driver1);
	    		fromNumber = CONFIG.getProperty("qa_user_1_number"); 
	    	}
	    	
	    	if(targetUsStateSetting == AccountCallRecordingTab.AreaRecordOptions.NoConsent) {
	    		softPhoneSettingsPage.selectDefaultNumber(driver3);
	    		contactNumber = CONFIG.getProperty("qa_user_3_number"); 
	    	}
	    }
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(inGranular) {
	    	accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, originInCountry, originINSetting);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, targetInCountry, targetInSetting);
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }else {
	    	accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }
	    
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

 		// receiving call from receiver
 		softPhoneCalling.pickupIncomingCall(driver3);
 		callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_3_name") + " Additional", CONFIG.getProperty("contact_account_name"));
 		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
 		
 		//hanging up with caller 1
 	    System.out.println("hanging up with caller 1");
 	    softPhoneCalling.hangupActiveCall(driver3);
 	    
 		//Call is removing from softphone
 	    System.out.println("Call is removing from softphone");
 	    softPhoneCalling.isCallBackButtonVisible(driver1);
 	   
 		// Verifying that call recording URL is not created in salesforce for above call
 	    callScreenPage.openCallerDetailPage(driver1);
 		contactDetailPage.openRecentCallEntry(driver1, callSubject);
 	    String callObjectID = sfTaskDetailPage.getCallObjectId(driver1);
 	    assertTrue(contactNumber.contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(fromNumber.contains(sfTaskDetailPage.getFromNumber(driver1)));
	    
 	    if(expectedResult.equals("NoRecording")) {
 	    	sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
 	    }else {
 	    	 assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
 	    }
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);
 	    
 	    assertEquals(getRecordingMode(callObjectID), expectedResult);
	    
 		// Setting driver used to false as this test case is pass
 	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@DataProvider
	public Object[][] callRecordingUStoINDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 2);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		Object [][] objArray = new Object[totalNumberOfRows-2][];
		@SuppressWarnings("unchecked")
		ArrayList<String>[] testObjArray =  new ArrayList[totalNumberOfRows - 2];

		for (int i = 2; i < totalNumberOfRows; i++) {
			objArray[i-2] = new Object[1];
			testObjArray[i-2] = new ArrayList<String>();
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 1));						//globalAgentOnly
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 2));						//USGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 3));						//INGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 4));						//originUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 5));						//targetUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 6));						//originINSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 7));						//targetInSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 8));						//Expected Result
			
			objArray[i-2][0] = testObjArray[i-2];
		}
		  return objArray;
	}
	
	@Test(groups = { "Regression" }, dataProvider = "callRecordingUStoINDP")
	public void verify_us_to_in_call_recording(ArrayList<String> variableList) {
		
		System.out.println("Test case --verify_us_to_in_call_recording_scenario_" + scenarioCount  + "-- started ");
		
		//Set global variables
		setVariables(variableList);
		
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    openCallReacordingTab();
	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver1, "408");
	    String fromNumber = softPhoneSettingsPage.getSelectedOutboundNumber(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    int index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver3, "+441344203892");
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver3, index);
	    
	    String contactNumber = "+441344203892"; 
	    
	    if(globalAgentOnly)
	    	accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
	    else
	    	accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(usGranular) {
	    	accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, usOriginState, originUsStateSetting);
	    }else {
	    	accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
	    	if(originUsStateSetting == AccountCallRecordingTab.AreaRecordOptions.NoConsent) {
	    		softPhoneSettingsPage.selectDefaultNumber(driver1);
	    		fromNumber = CONFIG.getProperty("qa_user_1_number"); 
	    	}
	    }
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(inGranular) {
	    	accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, originInCountry, originINSetting);
	    	accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }else {
	    	accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }
	    
	    reloadSoftphone(driver1);
	    
	    // Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);

 		// receiving call from receiver
 		softPhoneCalling.pickupIncomingCall(driver3);
 		callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_3_name") + " UK", CONFIG.getProperty("contact_account_name"));
 		String callSubject = callToolsPanel.changeAndGetCallSubject(driver1);
 		
 		//hanging up with caller 1
 	    System.out.println("hanging up with caller 1");
 	    softPhoneCalling.hangupActiveCall(driver3);
 	    
 		//Call is removing from softphone
 	    System.out.println("Call is removing from softphone");
 	    softPhoneCalling.isCallBackButtonVisible(driver1);
 	   
 		// Verifying that call recording URL is not created in salesforce for above call
 	    callScreenPage.openCallerDetailPage(driver1);
 		contactDetailPage.openRecentCallEntry(driver1, callSubject);
 	    String callObjectID = sfTaskDetailPage.getCallObjectId(driver1);
 	    assertTrue(contactNumber.contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(fromNumber.contains(sfTaskDetailPage.getFromNumber(driver1)));
 	    if(expectedResult.equals("NoRecording")) {
 	    	sfTaskDetailPage.isCallRecordingURLInvisible(driver1);
 	    }else {
 	    	 assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver1).contains("recordings"));
 	    }
 	    seleniumBase.closeTab(driver1);
 	    seleniumBase.switchToTab(driver1, 1);
 	    
 	    assertEquals(getRecordingMode(callObjectID), expectedResult);
	    
 		// Setting driver used to false as this test case is pass
 	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@DataProvider
	public Object[][] callRecordingIntoUSDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 3);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		Object [][] objArray = new Object[totalNumberOfRows-2][];
		@SuppressWarnings("unchecked")
		ArrayList<String>[] testObjArray =  new ArrayList[totalNumberOfRows - 2];

		for (int i = 2; i < totalNumberOfRows; i++) {
			objArray[i-2] = new Object[1];
			testObjArray[i-2] = new ArrayList<String>();
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 1));						//globalAgentOnly
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 2));						//USGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 3));						//INGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 4));						//originUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 5));						//targetUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 6));						//originINSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 7));						//targetInSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 8));						//Expected Result
			
			objArray[i-2][0] = testObjArray[i-2];
		}
		  return objArray;
	}
	
	@Test(groups = { "Regression" }, dataProvider = "callRecordingUStoINDP")
	public void verify_in_to_us_call_recording(ArrayList<String> variableList) {
		
		System.out.println("Test case --verify_in_to_us_call_recording_scenario_" + scenarioCount  + "-- started ");
		
		//Set global variables
		setVariables(variableList);
		
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    openCallReacordingTab();
	    
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    int index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver3, "+441344203892");
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver3, index);
	    String fromNumber = "+441344203892"; 
	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    softPhoneSettingsPage.selectAdditionalNumberAccToAreaCode(driver1, "408");
	    String contactNumber = softPhoneSettingsPage.getSelectedOutboundNumber(driver1);
	    
	    if(globalAgentOnly)
	    	accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
	    else
	    	accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(usGranular) {
	    	accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, usOriginState, originUsStateSetting);
	    }else {
	    	accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
	    	if(originUsStateSetting == AccountCallRecordingTab.AreaRecordOptions.NoConsent) {
	    		softPhoneSettingsPage.selectDefaultNumber(driver1);
	    		contactNumber = CONFIG.getProperty("qa_user_1_number"); 
	    	}
	    }
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(inGranular) {
	    	accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, originInCountry, originINSetting);
	    	accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }else {
	    	accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }
	    
	    reloadSoftphone(driver3);
	    
	    // Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);

 		// receiving call from receiver
 		softPhoneCalling.pickupIncomingCall(driver1);
 		callScreenPage.addCallerAsContact(driver3, CONFIG.getProperty("qa_user_1_name") + " Additional", CONFIG.getProperty("contact_account_name"));
 		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);
 		
 		//hanging up with caller 1
 	    System.out.println("hanging up with caller 1");
 	    softPhoneCalling.hangupActiveCall(driver1);
 	    
 		//Call is removing from softphone
 	    System.out.println("Call is removing from softphone");
 	    softPhoneCalling.isCallBackButtonVisible(driver3);
 	   
 		// Verifying that call recording URL is not created in salesforce for above call
 	    callScreenPage.openCallerDetailPage(driver3);
 		contactDetailPage.openRecentCallEntry(driver3, callSubject);
 	    String callObjectID = sfTaskDetailPage.getCallObjectId(driver3);
 	    assertTrue(contactNumber.contains(sfTaskDetailPage.getToNumber(driver3)));
	    assertTrue(fromNumber.contains(sfTaskDetailPage.getFromNumber(driver3)));
 	    if(expectedResult.equals("NoRecording")) {
 	    	sfTaskDetailPage.isCallRecordingURLInvisible(driver3);
 	    }else {
 	    	 assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
 	    }
 	    seleniumBase.closeTab(driver3);
 	    seleniumBase.switchToTab(driver3, 1);
 	    
 	    assertEquals(getRecordingMode(callObjectID), expectedResult);
	    
 		// Setting driver used to false as this test case is pass
 	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	@DataProvider
	public Object[][] callRecordingIntoInDP() throws Exception {
		excelDataManager.openExcelFile(excelFilePath, 4);

		int totalNumberOfRows = excelDataManager.getTotalNumberOfRows();

		Object [][] objArray = new Object[totalNumberOfRows-2][];
		@SuppressWarnings("unchecked")
		ArrayList<String>[] testObjArray =  new ArrayList[totalNumberOfRows - 2];

		for (int i = 2; i < totalNumberOfRows; i++) {
			objArray[i-2] = new Object[1];
			testObjArray[i-2] = new ArrayList<String>();
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 1));						//globalAgentOnly
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 2));						//USGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 3));						//INGranular
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 4));						//originUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 5));						//targetUsStateSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 6));						//originINSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 7));						//targetInSetting
			testObjArray[i-2].add(excelDataManager.getCellValue(i, 8));						//Expected Result
			
			objArray[i-2][0] = testObjArray[i-2];
		}
		  return objArray;
	}
	
	@Test(groups = { "Regression" }, dataProvider = "callRecordingIntoInDP")
	public void verify_in_to_in_call_recording(ArrayList<String> variableList) {
		
		System.out.println("Test case --verify_in_to_in_call_recording_scenario_" + scenarioCount  + "-- started ");
		
		//Set global variables
		setVariables(variableList);
		
	    //updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    openCallReacordingTab();
	    
	    String contactNumber = CONFIG.getProperty("qa_user_1_fin_number");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver1);
	    int index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver1, contactNumber);
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, index);
	    
	    String fromNumber = CONFIG.getProperty("qa_user_3_uk_number");
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.clickOutboundNumbersTab(driver3);
	    index = softPhoneSettingsPage.getAdditionalNumbersIndex(driver3, fromNumber);
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver3, index);
	    
	    if(globalAgentOnly)
	    	accountCallRecordingTab.enableSingleChannelRecordingsSetting(driver4);
	    else
	    	accountCallRecordingTab.disableSingleChannelRecordingsSetting(driver4);
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(usGranular) {
	    	accountCallRecordingTab.enableAllowGranularControlSetting(driver4);
	    }else {
	    	accountCallRecordingTab.disableAllowGranularControlSetting(driver4);
	    }
	    accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    
	    if(inGranular) {
	    	accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, originInCountry, originINSetting);
	    	accountCallRecordingTab.selectAreaRecordingDropdownOption(driver4, targetInCountry, targetInSetting);
	    	accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }else {
	    	accountCallRecordingTab.disableCountryGranularControlSetting(driver4);
	    	accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
	    }
	    
	    reloadSoftphone(driver3);
	    
	    // Calling from Agent's SoftPhone
 		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);

 		// receiving call from receiver
 		softPhoneCalling.pickupIncomingCall(driver1);
 		callScreenPage.addCallerAsContact(driver3, CONFIG.getProperty("qa_user_1_name") + " Finland", CONFIG.getProperty("contact_account_name"));
 		String callSubject = callToolsPanel.changeAndGetCallSubject(driver3);
 		
 		//hanging up with caller 1
 	    System.out.println("hanging up with caller 1");
 	    softPhoneCalling.hangupActiveCall(driver1);
 	    
 		//Call is removing from softphone
 	    System.out.println("Call is removing from softphone");
 	    softPhoneCalling.isCallBackButtonVisible(driver3);
 	   
 		// Verifying that call recording URL is not created in salesforce for above call
 	    callScreenPage.openCallerDetailPage(driver3);
 		contactDetailPage.openRecentCallEntry(driver3, callSubject);
 	    String callObjectID = sfTaskDetailPage.getCallObjectId(driver3);
 	    assertTrue(contactNumber.contains(sfTaskDetailPage.getToNumber(driver3)));
	    assertTrue(fromNumber.contains(sfTaskDetailPage.getFromNumber(driver3)));
 	    if(expectedResult.equals("NoRecording")) {
 	    	sfTaskDetailPage.isCallRecordingURLInvisible(driver3);
 	    }else {
 	    	 assertTrue(sfTaskDetailPage.getCallRecordingUrl(driver3).contains("recordings"));
 	    }
 	    seleniumBase.closeTab(driver3);
 	    seleniumBase.switchToTab(driver3, 1);
 	    
 	    assertEquals(getRecordingMode(callObjectID), expectedResult);
	    
 		// Setting driver used to false as this test case is pass
 	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);

		System.out.println("Test case is pass");
	}
	
	public void setVariables(List<String> variableList) {
		globalAgentOnly = variableList.get(0).equals("ON");
		usGranular		= variableList.get(1).equals("ON");
		inGranular		= variableList.get(2).equals("ON");
		
		originUsStateSetting =  variableList.get(3).equals("N/A") == false ? AccountCallRecordingTab.AreaRecordOptions.valueOf(variableList.get(3)): AreaRecordOptions.NoRecording;
		targetUsStateSetting =  variableList.get(4).equals("N/A") == false ? AccountCallRecordingTab.AreaRecordOptions.valueOf(variableList.get(4)): AreaRecordOptions.NoRecording;
	
		originINSetting =  variableList.get(5).equals("N/A") == false ? AccountCallRecordingTab.AreaRecordOptions.valueOf(variableList.get(5)): AreaRecordOptions.NoRecording;
		targetInSetting =  variableList.get(6).equals("N/A") == false ? AccountCallRecordingTab.AreaRecordOptions.valueOf(variableList.get(6)): AreaRecordOptions.NoRecording;
	
		expectedResult = variableList.get(7);
	}
	
	public void getSessionID() {
		try {
			
			client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://qa.ringdna.com/api/v2/session/authenticateUsernamePassword" + "?username="
							+ CONFIG.get("qa_support_user_username") + "&password="
							+ CONFIG.get("qa_support_user_password") + CONFIG.get("qa_support_user_token"))
					.method("POST", body).build();

			Response response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject Jobject = new JSONObject(jsonData);
			sessionId = Jobject.get("sessionId").toString();
			System.out.println(sessionId);
			assertTrue(Strings.isNotNullAndNotEmpty(sessionId), "Session ID is null");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRecordingMode(String callObjectID) {
		String timeStamp = String.valueOf(HelperFunctions.GetCurrentDateTimeObj().getTime());
		String recordingMode = null;
		
		try {
			Request request = new Request.Builder().url("https://qa.ringdna.com/api/v2/app/calls/" + callObjectID + "?_=" + timeStamp).addHeader("sessionid", sessionId).build();
			Response response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject Jobject = new JSONObject(jsonData);
			JSONObject callobject = (JSONObject)Jobject.get("call");
			try {																										//for all party recordings
				recordingMode = ((JSONObject) callobject.get("playableRecording")).get("recordingMode").toString();
			}catch(Exception e) {
				try {																									//for agent only recordings
					recordingMode = ((JSONObject) callobject.get("recordings")).get("recordingMode").toString();
				}catch (Exception e1) {																					//for No recording
					return "NoRecording";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordingMode;
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
		
		if(method.getName().equals(methodNameTemp)) {
			scenarioCount++;
		}else {
			scenarioCount = 1;
		}
		
		String methodName = method.getName() + "_scenario_" + scenarioCount;
		testName.set(methodName);
		reocrdmethodNameList.add(methodName);
		
		methodNameTemp = method.getName();
		if (ctcSuiteID.equals("0") || System.getenv("BUILD_NUMBER") == null) {
			return;
		}
		TestRailUpdate.suiteWiseMethods.clear();
		TestRailUpdate.suiteWiseCases.clear();
		TestRailUpdate.suiteWiseMethods.put(Integer.parseInt(ctcSuiteID), reocrdmethodNameList);
		TestRailUpdate.suiteWiseCases.put(Integer.parseInt(ctcSuiteID), testRailUpdate.getCaseIdsFromMethodName(reocrdmethodNameList));
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
	
	@AfterClass(groups = { "Regression" })
	public void AfterClass() {
		initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		loginSupport(driver4);
		accountCallRecordingTab.setDefaultCallRecordingSettings(driver4);
		
		accountCallRecordingTab.enableCountryGranularControlSetting(driver4);
		accountCallRecordingTab.expandCountyContinent(driver4, "Europe");
		accountCallRecordingTab.checkAreasForCallRecording(driver4, originInCountry);
		accountCallRecordingTab.collapseCountyContinent(driver4, "Europe");
		accountCallRecordingTab.saveCallRecordingTabSettings(driver4);
		driverUsed.put("driver4", false);
	}
}