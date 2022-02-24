package base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.util.Strings;

import utility.HelperFunctions;
import utility.TestRailManager;
import utility.listeners.AlterSuite;
import utility.listeners.AlterSuite.Types;
import com.google.gson.JsonObject;

/**
 * @author Sumit
 *
 */
public class TestRailUpdate extends TestRailManager{

	//TestRail 
	public HelperFunctions helperFunctions  = new HelperFunctions();
	public static Properties testRailMapping = new Properties();
	public static HashMap<Integer, String> testIdsMapping = new HashMap<Integer, String>();
	public String testRailMappingFile = "TestRailMapping.properties"; 
	public static HashMap<Integer, List<String>> suiteWiseMethods = new HashMap<Integer, List<String>>();
	public static HashMap<Integer, List<Integer>> suiteWiseCases = new HashMap<Integer, List<Integer>>();
	public static HashMap<Integer, HashMap<Integer, Integer>> runWithCaseStatus = new HashMap<Integer, HashMap<Integer, Integer>>(); 
	public static HashMap<Integer, Integer> casesStatus = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> runWiseSuites = new HashMap<Integer, Integer>();
	public static HashMap<String, List<Integer>> customExecTestList = new HashMap<String, List<Integer>>();
	
	//This method is to load test rail mapping file
	public void loadTestRailMapping() {
		System.out.println("Loading testrail mapping file");
		testRailMapping = HelperFunctions.readConfigFile(testRailMappingFile);
		
		for (final String name: testRailMapping.stringPropertyNames()) {
			String[] cases = testRailMapping.getProperty(name).split(",");
			for(int i=0; i<cases.length; i++) {
				testIdsMapping.put(Integer.parseInt(cases[i].trim()), name);
			}
		}
		System.out.println("Testrail mapping file Loaded");
	}

	//This method is to set suite wise cases ids 
	public void setSuiteWiseCaseIds(int suiteId, ITestContext testListener) {
		try {
			//Getting suite wise methods
			if(suiteWiseMethods.containsKey(suiteId)) {
				suiteWiseMethods.get(suiteId).addAll(getMethodNameListFromTest(testListener));
			} else {
				suiteWiseMethods.put(suiteId, getMethodNameListFromTest(testListener));
			}

			//getting suite wise caseIds
			if(suiteWiseCases.containsKey(suiteId)) {
				suiteWiseCases.get(suiteId).addAll(getCaseIdsFromMethodName(getMethodNameListFromTest(testListener)));
			} else {
				suiteWiseCases.put(suiteId, getCaseIdsFromMethodName(getMethodNameListFromTest(testListener)));
			}

		} catch (Exception e) {
			System.out.println("Not able to set suite wise cases due to following error = "+e);
		}
	}

	public List<String> getMethodNameListFromTest(ITestContext  testListener) {
		try {
			ITestNGMethod[] methodList = testListener.getAllTestMethods();
			List<String> methodNameList = new ArrayList<String>();
			for(int i=0; i<methodList.length; i++) {
				methodNameList.add(methodList[i].getMethodName());
			}
			return methodNameList;
		} catch (Exception e) {
			System.out.println("Not able to get the method names due to following error = "+e);
			return null;
		}
	}

	public List<Integer> getCaseIdsFromMethodName(List<String> methodNameList){
		List<Integer> caseIds = new ArrayList<Integer>();
		for(String str: methodNameList) {
			if(testRailMapping.containsKey(str)) {
				String caseIdString = testRailMapping.getProperty(str);
				caseIds.addAll(getIdsFromString(caseIdString));
			} else {
				System.out.println("Following method is not mapped : " +str);
			}
		}
		return caseIds;
	}

	public List<Integer> getIdsFromString(String str) {
		List<Integer> caseIds = new ArrayList<Integer>();
		String[] cases = str.split(",");
		for(int i=0; i<cases.length; i++) {
			caseIds.add(Integer.parseInt(cases[i].trim()));
		}
		return caseIds;
	}

	//setting status with caseIds 1=Passed, 2=Failed, 3=Skipped
	public void setStatusWithCaseIds(ITestResult result) {
		if(testRailMapping.containsKey(result.getName())) {
			String str = testRailMapping.getProperty(result.getName());
			String[] cases = str.split(",");
			for(int i=0; i<cases.length; i++) {
				casesStatus.put(Integer.parseInt(cases[i].trim()), result.getStatus());
			}
		}
	}

	//This method is to create Test Runs
	public void createTestRuns(int projectId, int milestoneId) {
		try {
			System.out.println("Creating Test Runs ");
			System.out.println("suitewiseCases: "+suiteWiseCases.size());
			synchSuiteWiseCasesWithTestRail(projectId);
			for(Integer i : suiteWiseCases.keySet()) {
				runWiseSuites.put(i, Integer.parseInt(addRun(projectId, i, milestoneId, suiteWiseCases.get(i), testRunName(i)).get("id").toString()));
			}
			System.out.println("Test Runs Created");
		} catch (Exception e) {
			System.out.println("Test Runs not created due to following error = "+e);	
		}
	}

	//this method is to get test run name 
	public String testRunName(int suiteId) {
		DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm");
		Date dateobj = new Date();
		String buildName;
		String buildNumber;
		if(System.getenv("BUILD_NUMBER")!=null) {
			buildNumber = System.getenv("BUILD_NUMBER");
		} else {
			buildNumber="";
		}
		
		if(System.getProperty("environment")==null || System.getProperty("environment").equals("AutoOrg")){
			if(System.getProperty("runName")==null){
				buildName = df.format(dateobj).toString()+ "_" + getSuiteName(suiteId) + "_QA" + "_#" + buildNumber;
			}else {
				buildName = df.format(dateobj).toString()+ "_" + System.getProperty("runName") + "_QA" + "_#" + buildNumber;
			}
		}else{
			if(System.getProperty("runName")==null){
				buildName = df.format(dateobj).toString()+ "_" + getSuiteName(suiteId) + "_PROD" + "_#" + buildNumber;
			} else {
				buildName = df.format(dateobj).toString()+ "_" + System.getProperty("runName") + "_PROD" + "_#" + buildNumber;
			}
		}
		System.out.println("Test run name is = "+ df.format(dateobj).toString() +"_"+ getSuiteName(suiteId)+" - #"+buildNumber);
		return(buildName);
	}

	//updating suiteWiseCases as per testrail 
	public void synchSuiteWiseCasesWithTestRail(int projectId) {
		List<Integer> customRunTestIds = new ArrayList<>();		
		if(customExecTestList != null && customExecTestList.size() > 0 &&
				!AlterSuite.testRailSuiteIds.contains(17)) {
			for (List<Integer> idList : customExecTestList.values()) {
				if(idList!= null)
					customRunTestIds.addAll(idList);
			}
		}
		
		HashMap<Integer, List<Integer>> testRailSuiteWiseCases = getSuiteAllCases(suiteWiseCases.keySet(), projectId);
		for(Integer suiteId : suiteWiseCases.keySet()) {
			for(int i=0; i<suiteWiseCases.get(suiteId).size(); i++) {
				int suiteWiseCaseId = suiteWiseCases.get(suiteId).get(i);
				if(!testRailSuiteWiseCases.get(suiteId).contains(suiteWiseCaseId) || (customRunTestIds.size() > 0 && !customRunTestIds.contains(suiteWiseCaseId))) {
					System.out.println("This case is is not present in suite = "+ suiteWiseCaseId);
					suiteWiseCases.get(suiteId).remove(suiteWiseCases.get(suiteId).get(i));
					i--;
				} 
			}
		}
	}

	//This method is to update the cases of test run
	public void updateTestRuns(String sprintName){
		try {
			System.out.println("Updating test runs");
			setRunWiseCasesStatus(suiteWiseCases, runWiseSuites, casesStatus);
			for(Integer i: runWithCaseStatus.keySet()) {
				updateStatus(i, sprintName, runWithCaseStatus.get(i));
				//closeRun(i);
			}
			System.out.println("Test Runs updated");
		} catch (Exception e) {
			System.out.println("Test Runs not updated due to following error = "+e);
		}
	}

	//Setting up data into runWithCaseStatus
	public void setRunWiseCasesStatus(HashMap<Integer, List<Integer>> suiteWiseCases, HashMap<Integer, Integer> runWiseSuites, HashMap<Integer, Integer> casesStatus) {
		for(Integer i:suiteWiseCases.keySet()) {
			HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
			for(Integer j: suiteWiseCases.get(i)) {
				if(casesStatus.containsKey(j)) {
					temp.put(j, casesStatus.get(j));
				}
			}
			runWithCaseStatus.put(runWiseSuites.get(i), temp);
		}
	}

	public void getTestsForCustomizedExecution() throws Exception {
		loadTestRailMapping();

		HashMap<String, List<Integer>> MethodList = new HashMap<String, List<Integer>>();
		List<Integer> prioritiesIds = getPrioritiesList(Arrays.asList(AlterSuite.prioritiesList));

		// get json array from test rail for each test suite
		List<org.json.JSONArray> testRailJsonArray = new ArrayList<org.json.JSONArray>();
		for (Integer testRailSuite : AlterSuite.testRailSuiteIds) {
			testRailJsonArray.addAll(getTestCaseList(1, testRailSuite));
		}

		// Adding some static methods to run
		MethodList.put("runPreRequisites", null);
		MethodList.put("aa_AddCallersAsContactsAndLeads", null);

		// for click to call case
		if (AlterSuite.testRailSuiteIds.contains(17)) {
			MethodList.put("click_to_call_contact", null);
			MethodList.put("click_to_call_lead", null);
			MethodList.put("click_to_call_account", null);
			customExecTestList = MethodList;
			return;
		}

		// iterate over each test case json entry to get the required test list
		int count = 0;
		for (org.json.JSONArray r : testRailJsonArray) {
			for (int i = 0; i < r.length(); i++) {

				org.json.JSONObject obj = r.getJSONObject(i);
				try {

					int automation_status = (!obj.isNull("custom_automation_status"))
							? Integer.parseInt(obj.get("custom_automation_status").toString())
							: 0;

					int testPriority = Integer.parseInt(obj.get("priority_id").toString());
					int testCaseId = Integer.parseInt(obj.get("id").toString());

					if (automation_status == 1 && prioritiesIds.contains(testPriority)) {
						MethodList = addEntryToCustomiedHashMap(MethodList, testCaseId);

					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}

		System.out.println(count);
		customExecTestList = MethodList;
	}
	
	public void getTestsForSmokeSanity() throws Exception{
		loadTestRailMapping();
		
		HashMap<String, List<Integer>> MethodList = new HashMap<String, List<Integer>>();
		Types type = AlterSuite.type;
		
		//get json array from test rail for each test suite
		List<org.json.JSONArray> testRailJsonArray = new ArrayList<org.json.JSONArray>();
		for (Integer testRailSuite : AlterSuite.testRailSuiteIds) {
			testRailJsonArray.addAll(getTestCaseList(1, testRailSuite));
		}
		
		//Adding some static methods to run
		MethodList.put("runPreRequisites", null);
		if(type.equals(Types.Sanity)) MethodList.put("aa_AddCallersAsContactsAndLeads", null);
		
		//iterate over each test case json entry to get the required test list
		int count = 0;
		for (org.json.JSONArray r : testRailJsonArray) {
			for (int i = 0; i < r.length(); i++) {

				JSONObject obj = (JSONObject) r.get(i);
				int automation_status = obj.get("custom_automation_status")!= null ? Integer.parseInt(obj.get("custom_automation_status").toString()): 0;
				boolean smokeFlag = obj.get("custom_smoke")!= null ? Boolean.parseBoolean(obj.get("custom_smoke").toString()): false;
				boolean SanityFlag = obj.get("custom_sanity_test_case")!= null ? Boolean.parseBoolean(obj.get("custom_sanity_test_case").toString()): false;
				int testCaseId = Integer.parseInt(obj.get("id").toString());

				//Create method list for smoke
				if (automation_status == 1 && type.equals(Types.Smoke) && smokeFlag) {
					MethodList = addEntryToCustomiedHashMap(MethodList, testCaseId);
				}
				
				//Create method list for Sanity
				if (automation_status == 1 && type.equals(Types.Sanity) && SanityFlag) {
					MethodList = addEntryToCustomiedHashMap(MethodList, testCaseId);
				}				
			}
		}
				
		System.out.println(count);
		customExecTestList = MethodList;
	}
	
	private HashMap<String, List<Integer>> addEntryToCustomiedHashMap(HashMap<String, List<Integer>> customMethodHashMap, int testCaseId) {
		if (testIdsMapping.containsKey(testCaseId)) {
			if (!customMethodHashMap.keySet().contains(testIdsMapping.get(testCaseId))) {
				List<Integer> testIds = new ArrayList<>();
				testIds.add(testCaseId);
				customMethodHashMap.put(testIdsMapping.get(testCaseId), testIds);
			}else {
				customMethodHashMap.get(testIdsMapping.get(testCaseId)).add(testCaseId);
			}
		} else {
			System.out.println("Following method is not mapped : " + testCaseId);
		}
		return customMethodHashMap;
	}
}