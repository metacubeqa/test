package base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.google.common.base.Strings;

import io.github.bonigarcia.wdm.WebDriverManager;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.SoftPhoneSettingsPage;
import softphone.source.SoftphoneCallHistoryPage;
import utility.HelperFunctions;
import utility.ZipUtils;
import utility.listeners.AnnotationTransformer1;
import utility.listeners.RetryListener;

public abstract class TestBase
{
	public String configFileQA = "configLoadTest.properties";
	public String configFileQaAuto = "configAutomationOrg.properties";
	public String configFileProd = "ConfigProduction.properties";
	public String configFileGS_Sandbox = "ConfigGS_Sandbox.properties";
	public String configFileGS_Production = "ConfigGS_Production.properties";
	public String configFileGS_patch_Sandbox = "ConfigGS_SandboxPatch.properties";
	public static final String configFileMetaOrg = "configMetacubeOrg.properties";
	public static WebDriver driver1 = null;	
	public static WebDriver driver2 = null;
	public static WebDriver driver3 = null;
	public static WebDriver driver4 = null;
	public static WebDriver driver5 = null;
	public static WebDriver driver6 = null;
	public static WebDriver chatterOnlyDriver = null;
	public static WebDriver standardUserDriver = null;	
	public static WebDriver supportDriver = null;
	public static WebDriver webSupportDriver = null;
	public static WebDriver adminDriver = null;
	public static WebDriver qaAdminDriver = null;
	public static WebDriver agentDriver = null;
	public static WebDriver caiCallerDriver = null;
	public static WebDriver caiVerifyDriver = null;
	public static WebDriver caiDriver2 = null;
	public static WebDriver caiDriver1 = null;
	public static WebDriver caiSupportDriver = null;
	public static WebDriver qaV2Driver = null;
	public static WebDriver ringDNAProdDriver = null;
	public static WebDriver callDashboardDriver = null;
	public static List<WebDriver> Drivers = new ArrayList<WebDriver>();
	public static Properties CONFIG = new Properties();
	public static Properties testDataProperties = new Properties();
	public HashMap<String, Boolean> driverUsed = new HashMap<String, Boolean>();
	protected static String ctcSuiteID = null;
	
	public TestRailUpdate testRailUpdate = new TestRailUpdate();
	SeleniumBase SB = new SeleniumBase();
	SoftPhoneLoginPage SFLP = new SoftPhoneLoginPage();
	SoftPhoneSettingsPage softPhoneSettingsPage = new SoftPhoneSettingsPage();
	SoftPhoneCalling softphoneCalling = new SoftPhoneCalling();
	UpdateTestResults updateTestResults = new UpdateTestResults();
	SoftphoneCallHistoryPage softPhoneCallHistoryPage = new SoftphoneCallHistoryPage();

	@Parameters({"metacubeOrg"})
	@BeforeSuite(groups={"Sanity", "Regression", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void preSuiteCheck(@Optional() String isMetacubeOrg) {
		
		System.out.println("-------------------------------------------------------");
		System.out.println("Total Number of Cases in this Run: " + AnnotationTransformer1.count );
		System.out.println("-------------------------------------------------------");
		
		HelperFunctions.deleteAllScreenShots();
		if(Strings.isNullOrEmpty(isMetacubeOrg)){
			if(System.getProperty("environment")==null){
				CONFIG = HelperFunctions.readConfigFile(configFileQA);
				System.out.println("Running build on QA enviroment");
			}else if(System.getProperty("environment").equals("AutoOrg")){
				CONFIG = HelperFunctions.readConfigFile(configFileQaAuto);
				System.out.println("Running build on QA Automation Org");
			}
			else{
				CONFIG = HelperFunctions.readConfigFile(configFileProd);
				System.out.println("Running build on Prod enviroment");
			}			
		}
		else{
			CONFIG = HelperFunctions.readConfigFile(configFileMetaOrg);
			System.out.println("Running build on Metacube Software Pvt Ltd environment");
		}
		
		//handle for GS Env
		if(!Strings.isNullOrEmpty(System.getProperty("salesForceEnv"))) {
			switch (System.getProperty("salesForceEnv")) {
	
			case "Sandbox":
				CONFIG = HelperFunctions.readConfigFile(configFileGS_Sandbox);
				System.out.println("Running build on GS Sandbox org");
				break;
			case "Production":
				CONFIG = HelperFunctions.readConfigFile(configFileGS_Production);
				System.out.println("Running build on GS Production org");
				break;
			case "Patch":
				CONFIG = HelperFunctions.readConfigFile(configFileGS_patch_Sandbox);
				System.out.println("Running build on GS Sandbox patch org");
				break;
			default:
				break;
	
			}
		}
		
		updateTestResults.emptyExcelSheet();
		testRailUpdate.loadTestRailMapping();
	}

	@BeforeMethod(groups={"Sanity", "Regression", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void setDriverUsedToFalse()
	{
		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);
		driverUsed.put("driver6", false);
		driverUsed.put("supportDriver", false);
		driverUsed.put("webSupportDriver", false);
		driverUsed.put("adminDriver", false);
		driverUsed.put("qaAdminDriver", false);
		driverUsed.put("agentDriver", false);
		driverUsed.put("chatterOnlyDriver", false);
		driverUsed.put("standardUserDriver", false);
		driverUsed.put("caiCallerDriver", false);
		driverUsed.put("caiVerifyDriver", false);
		driverUsed.put("caiDriver2", false);
		driverUsed.put("caiDriver1", false);
		driverUsed.put("caiSupportDriver", false);
		driverUsed.put("qaV2Driver", false);
		driverUsed.put("ringDNAProdDriver", false);
		driverUsed.put("callDashboardDriver", false);
	}
	
	@AfterSuite(alwaysRun = true, groups={"Sanity", "Regression", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void afterSuiteSetup(ITestContext context)
	{
		int milestoneID = Integer.parseInt(HelperFunctions.readConfigFile("configLoadTest.properties").getProperty("automation_case_execution_milestone_id").trim());
		String sprintName = HelperFunctions.readConfigFile("configLoadTest.properties").getProperty("sprint_name");
		quitDrivers();
		//TestRail
		testRailUpdate.createTestRuns(Integer.parseInt(CONFIG.getProperty("project_id").trim()), milestoneID);
		testRailUpdate.updateTestRuns(sprintName);
		updateTestResults.putTestResultsInExcel(context, sprintName);
		ZipUtils.ZipFiles();
	}

	@AfterMethod(alwaysRun = true, groups = { "Sanity", "Regression", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void resetSetupDefault(ITestResult result){
		System.out.println("This is in After method of base class");
		
		//TestRail
		testRailUpdate.setStatusWithCaseIds(result);
		updateTestResults.updateTestResultsArray(result);
		// resetting driver user status when test case is pass and taking screenshot when test case is fail
		System.out.println("Ret Count = " + RetryListener.count);						  
		if (result.getStatus() == 1) {			
			RetryListener.count = 0;
		}
		else if (result.getStatus()== 2 || (result.getStatus() == 3 && (RetryListener.count == 1|| RetryListener.count == 0))) {
			System.out.println("Retry count is " + RetryListener.count);
			if (driver1 != null || supportDriver!=null || webSupportDriver!=null || caiCallerDriver!=null || caiVerifyDriver!=null || caiDriver1!=null || caiDriver2!=null) {
				int screentshotCount=1;
				for (int i = 0; i < Drivers.size(); i++) {
					WebDriver driver = (WebDriver) Drivers.get(i);
						 
					if (!(driver.toString().contains("null"))) {
						HelperFunctions.captureScreenshot(driver, result.getName() + screentshotCount, result.getStatus());
							
						screentshotCount++;
					}
				}
			}
		}

		// closing drivers in case test cases are pre-requisites else resetting
		// the test cases
		if (result.getName() == "runPreRequisites") {
			for (int i = 0; i < Drivers.size(); i++) {
				WebDriver driver = (WebDriver) Drivers.get(i);
				if (driver != null)
					driver.quit();
			}
			driver1 = null;
			driver2 = null;
		} else {
			System.out.println("Reset the setup");
			resetApplication();
		}
		setDriverUsedToFalse();
	}

	@Parameters({"suiteId"})
	@BeforeTest(groups={"Regression", "Sanity", "QuickSanity", "MediumPriority", "Product Sanity"})
	public void setDataForTestRail(@Optional("0")String suiteId, ITestContext testListener) {
		ctcSuiteID = suiteId;
		if(suiteId.equals("0") || System.getenv("BUILD_NUMBER")==null) {
			System.out.println("No suite id is available  =" + Integer.parseInt(suiteId));
			return;
		}
		testRailUpdate.setSuiteWiseCaseIds(Integer.parseInt(suiteId), testListener);	
		updateTestResults.prepareTestResultArray(Integer.parseInt(suiteId), Integer.parseInt(CONFIG.getProperty("project_id").trim()));
	}
	
	public abstract void resetApplication();

	/**
	 * using Webdriver Manager to handle Chrome updation automatically
	 * and launching browser with desired capabilities
	 * @return
	 */
	public WebDriver getDriver()
	{
		
		WebDriverManager.chromedriver().setup();
		
		WebDriver driver = null;
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> chromePrefs = new HashMap<String, Object>();
	    chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1 );
	    options.setExperimentalOption("prefs", chromePrefs);

		if (System.getProperty("os.name").contains("Windows"))
		{
			System.setProperty("webdriver.chrome.silentOutput", "true");
			File file = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\RingDNA-QA.crx"));
			options.addExtensions(file);
		}
		
		options.addArguments(new String[] { "--test-type" });
		options.addArguments(new String[] { "--use-fake-device-for-media-stream" });
		options.addArguments(new String[] { "--use-fake-ui-for-media-stream" }); 
		options.addArguments(new String[] { "--no-sandbox"});
		options.addArguments(new String[] { "--disable-notifications"});
		
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		Drivers.add(driver);
		return driver;
	}
	
	public void quitDrivers() {
		System.out.println("Closing the drivers");
		for (int i = 0; i < Drivers.size(); i++) {
			WebDriver driver = (WebDriver) Drivers.get(i);
			if (driver != null) {
				driver.quit();
			}
		}
		driver1 = null;	
		driver2 = null;
		driver3 = null;
		driver4 = null;
		driver5 = null;
		driver6 = null;
		supportDriver = null;
		webSupportDriver = null;
		adminDriver = null;
		agentDriver = null;
		chatterOnlyDriver = null;
		standardUserDriver = null;
		caiCallerDriver = null;
		caiVerifyDriver = null;
		caiDriver2 = null;
		caiDriver1 = null;
		caiSupportDriver = null;
		qaV2Driver  = null;
		ringDNAProdDriver = null;
		callDashboardDriver = null;
	}
}