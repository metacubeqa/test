package utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.testng.annotations.Test;

import utility.testrail.APIClient;
import utility.testrail.APIException;

public class TestRailManager {

	

	public static List<Integer> runCaseIds = new ArrayList<Integer>();
	public String[] scriptNames = {"test_case_1", "test_case_2", "test_case_3", "test_case_4", "test_case_5"}; 
	public HashMap<Object, Object> cases = new HashMap<Object, Object>(); 
	public static APIClient client = null;

	public TestRailManager() {
		try {
			client = new APIClient(TESTRAIL_URL);
			client.setUser(TESTRAIL_USERNAME);
			client.setPassword(TESTRAIL_PASSWORD);
		} catch (Exception e) {
			System.out.println("Not able to authenticate with testrail due to following error = "+e);
		}
	}
	
	@Test
	public void closeTestRuns() {
		int[] milestoneIDs = new int[]{1507};
		
		for(int j=0; j < milestoneIDs.length; j++) {
			try {
				Object obj1 = client.sendGet("get_runs/" + 1 + "&is_completed=0" + "&milestone_id=" + milestoneIDs[j]);
				JSONObject jsonObject = new JSONObject(obj1.toString());
				org.json.JSONArray jsonArray = jsonObject.getJSONArray("runs");
				for(int i=0; i<jsonArray.length(); i++) {
					System.out.println("Test Run "+ jsonArray.getJSONObject(i).getString("id")+ " closing");
					closeRun(Integer.parseInt(jsonArray.getJSONObject(i).getString("id").toString()));
					System.out.println("Test Run "+ jsonArray.getJSONObject(i).getString("id")+ " closed");
				}
			}catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
	
	@Test
	public void copyRunRestuls() {
		int[] fromRunIDs = new int[]{14530, 14529, 14503, 14524, 14483, 14482, 14539};
		int toRunID = 7347;
		String sprintID = "Sprint v242";
		
		List<Integer> testCasesListToRun = getRunTestCaseIDList(toRunID);
		
		for(int i=0; i < fromRunIDs.length; i++) {
			updateStatus(toRunID, sprintID, getRunTestData(fromRunIDs[i], testCasesListToRun));
		}
	}

	//This method is to get the suite name
	public String getSuiteName(int suiteId) {
		try {
			Object c = client.sendGet("get_suite/"+suiteId);
			org.json.JSONArray jsonArray = new org.json.JSONArray("["+c.toString()+"]");
			System.out.println("Suite name is = "+(String) jsonArray.getJSONObject(0).getString("name") );
			return (String) jsonArray.getJSONObject(0).getString("name");
		} catch (Exception e) {
			System.out.println("Not able to get the test rail name due to following error = "+e);
			return null;
		}
	}

	//This method is to verify that caseId present in the suite
	public HashMap<Integer, List<Integer>> getSuiteAllCases(Set<Integer> suiteIds, int projectId) {
		HashMap<Integer, List<Integer>> suiteCases = new HashMap<Integer, List<Integer>>();
		try {
			int count = 0;
			List<Integer> cases= new ArrayList<Integer>();
			for(Integer i: suiteIds) {
				List<org.json.JSONArray> r = getTestCaseList(projectId, i);
				for(int j=0; j<r.size(); j++) {
					cases.addAll(getCaseIdsFromJsonArray(r.get(j)));
				}
				suiteCases.put(i, cases);				
				count++;
			}
			System.out.println("getSuiteAllCases:" + count);
			return suiteCases;
		} catch (Exception e) {
			System.out.println("Not able to get the cases due to following error = " + e);
			return null;
		}
	}
	
	//This method is to add test run into TestRail
	public JSONObject addRun(int projectId, int suiteId, int milestoneId, List<Integer> caseIds, String runName) {
		try {
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.put("suite_id", suiteId);
			data.put("name", runName);
			data.put("description", "This build is against Jenkins build");
			data.put("milestone_id", milestoneId);
			data.put("assignedto_id", 2);
			data.put("include_all", false);
			
			List<Object> lstCaseIds = new ArrayList<Object>();
		    lstCaseIds.addAll(caseIds);
		    
			data.put("case_ids", lstCaseIds);
			Object obj1 = client.sendPost("add_run/"+projectId, data);
			JSONObject jsonObject = new JSONObject(obj1.toString());
			return jsonObject;
		} catch (Exception e) {
			System.out.println("User not able to create run due to following error"+ e);
			return null;
		}
	}
	
	private HashMap<Integer, Integer>  getRunTestData(int runID, List<Integer> expectedTestCaseList) {
		try {
			HashMap<Integer, Integer> casesWithStatus = new HashMap<>();
			int offsetCount = 0;
			org.json.JSONArray jsonArray = new org.json.JSONArray();
			List<org.json.JSONArray> testRailList = new ArrayList<org.json.JSONArray>();
			do {
				Object obj1 = client.sendGet("get_tests/" + runID + "&offset=" + offsetCount + "&limit=250");
				JSONObject jsonObject = new JSONObject(obj1.toString());
				jsonArray = jsonObject.getJSONArray("tests");
				testRailList.add(jsonArray);
				offsetCount = offsetCount + 250;
			} while (jsonArray.length() == 250 && offsetCount < 15000);

			for (int i = 0; i < testRailList.size(); i++) {
				org.json.JSONArray obj = testRailList.get(i);
				for (int j = 0; j < obj.length(); j++) {
				int caseID = Integer.parseInt(obj.getJSONObject(j).getString("case_id").toString());
				int statusID = Integer.parseInt(obj.getJSONObject(j).getString("status_id").toString());
				if(statusID==1 && expectedTestCaseList.contains(caseID)) {
					statusID = 1;
					/*
					 * } else if(statusID==5) { statusID = 2; } else if(statusID==4) { statusID = 3;
					 */
				}else {
					continue;
				}
				casesWithStatus.put(caseID, statusID);
				}
			}

			return casesWithStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Integer> getRunTestCaseIDList(int runID) {
		try {
			List<Integer> casesList = new ArrayList<>();
			org.json.JSONArray jsonArray = new org.json.JSONArray();
			List<org.json.JSONArray> testRailList = new ArrayList<org.json.JSONArray>();
			int offsetCount = 0;
			do {
				//String temp = client.sendGet("get_tests/" + runID + "&offset=" + offsetCount + "&limit=250").toString();
				//String jsonDataVal = temp.substring(1, temp.length()-1);
				//JSONObject json1 = new JSONObject(jsonDataVal);
				//JSONArray jsonarray = json1.getJSONArray(json1.keys());
				
				Object obj1 = client.sendGet("get_tests/" + runID + "&offset=" + offsetCount + "&limit=250");
				JSONObject jsonObject = new JSONObject(obj1.toString());
				jsonArray = jsonObject.getJSONArray("tests");
				testRailList.add(jsonArray);
				offsetCount = offsetCount + 250;
			} while (jsonArray.length() == 250 && offsetCount < 15000);
			
			for (int i = 0; i < testRailList.size(); i++) {
				org.json.JSONArray obj = testRailList.get(i);
				for (int j = 0; j < obj.length(); j++) {
					int caseID = Integer.parseInt(obj.getJSONObject(j).getString("case_id").toString());
					casesList.add(caseID);
				}
			}

			return casesList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//This method is to update status of particular run
	public void updateStatus(Object runId, String sprintId, HashMap<Integer, Integer> casesWithStatus) {
		try {
			System.out.println("Results are = "+createResultList(casesWithStatus,sprintId));
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.put("results", createResultList(casesWithStatus, sprintId));
			client.sendPost("add_results_for_cases/"+runId, data);
		} catch (Exception e) {
			System.out.println("Failed due to ="+ e);
		}
	}

	//This method is to close test run into TestRail
	public void closeRun(int runID) {
		try {
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.put("assignedto_id", 1);
			client.sendPost("close_run/" + runID, data);
		} catch (Exception e) {
			System.out.println("User not able to create run due to following error" + e);
		}
	}
	
	public void addIdsFromString(String str) {
		String[] cases = str.split(",");
		for(int i=0; i<cases.length; i++) {
			runCaseIds.add(Integer.parseInt(cases[i].trim()));
		}
	}

	public void addTestRun() throws Exception {
		Map<Object, Object> data = new HashMap<Object, Object>();
		List<Integer> cases = new ArrayList<Integer>();
		cases.add(6);
		cases.add(7);
		cases.add(8);
		data.put("suite_id", 2);
		data.put("name", "Sumit Automation Run 5");
		data.put("description", "This build is against Jenkins build ");
		data.put("milestone_id", 2);
		data.put("assignedto_id", 1);
		data.put("include_all", false);

		data.put("case_ids", getAutomatedCasesForSuite(2));
		//JSONObject r = (JSONObject) client.sendPost("add_run/1", data);
	}

	
	public List<org.json.JSONArray> getTestCaseList(int projectId, int suiteId) {
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray();
			List<org.json.JSONArray> testRailList = new ArrayList<org.json.JSONArray>();
			int offsetCount = 0;
			do {
				Object obj1 = client.sendGet("get_cases/" + projectId + "&suite_id="+ suiteId + "&offset=" + offsetCount + "&limit=250");
				JSONObject jsonObject = new JSONObject(obj1.toString());
				jsonArray = jsonObject.getJSONArray("cases");
				testRailList.add(jsonArray);
				offsetCount = offsetCount + 250;
			}while(jsonArray.length() == 250 && offsetCount < 15000);
			return testRailList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public HashMap<Object, Object> getSectionsList(int suiteId, int projectID) {
		try {
			HashMap<Object, Object> sectionData = new HashMap<Object, Object>();
			int offsetCount = 0;
			Object obj1 = new Object();
			org.json.JSONArray jsonArray = new org.json.JSONArray();
			List<org.json.JSONArray> testRailList = new ArrayList<org.json.JSONArray>();
			do {
				obj1 = client.sendGet("get_sections/" + projectID + "&suite_id=" + suiteId + "&offset=" + offsetCount + "&limit=250");
				JSONObject jsonObject = new JSONObject(obj1.toString());
				jsonArray = jsonObject.getJSONArray("sections");
				testRailList.add(jsonArray);
				offsetCount = offsetCount + 250;
			}while(jsonArray.length() == 250 && offsetCount < 1500);
			
			for (int i = 0; i < testRailList.size(); i++) {
				org.json.JSONArray obj = testRailList.get(i);
				for (int j = 0; j < obj.length(); j++) {
					sectionData.put(obj.getJSONObject(j).getString("id"), obj.getJSONObject(j).getString("name"));
				}
			}
			System.out.println(sectionData);
			return sectionData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getAutomatedCasesForSuite(int suiteId) throws MalformedURLException, IOException, APIException, Exception {
		JSONArray casesJsonArray = new JSONArray();
		List<Integer> caseIds = new ArrayList<Integer>();
		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("project_id", 1);
		data.put("suite_id", suiteId);
		List<org.json.JSONArray> r = getTestCaseList(1, suiteId);
		System.out.println("json array" +r);
		for(int i=0; i<r.size(); i++) {
			org.json.JSONArray obj = r.get(i);
			int automation_status = Integer.parseInt(obj.getJSONObject(i).getString("custom_automation_status").toString());
			if(automation_status==1){
				casesJsonArray.add(obj);
				caseIds.add(Integer.parseInt(obj.getJSONObject(i).getString("id").toString()));
			}
		}
		return caseIds;
	}

	//This method is to get caseIds from given array
	public List<Integer> getCaseIdsFromJsonArray(org.json.JSONArray r) throws Exception {
		List<Integer> caseIds = new ArrayList<Integer>();
		for(int i=0; i<r.length(); i++) {
			caseIds.add(Integer.parseInt(r.getJSONObject(i).getString("id").toString()));
		}
		return caseIds;
	}
	

	public List<HashMap<Object, Object>> createResultList(HashMap<Integer, Integer> casesWithStatus,String sprintId) {

		try {
			List<HashMap<Object, Object>> casesResults = new ArrayList<HashMap<Object, Object>>();
			for(Integer i: casesWithStatus.keySet()) {
				HashMap<Object, Object> caseResult = new HashMap<Object, Object>();
				caseResult.put("case_id", i);
				if(casesWithStatus.get(i)==1) {
					caseResult.put("status_id", 1);
				} 
				if(casesWithStatus.get(i)==2) {
					caseResult.put("status_id", 5);
				}
				if(casesWithStatus.get(i)==3) {
					caseResult.put("status_id", 4);
				}
				caseResult.put("comment", "This is run by automation");
				caseResult.put("version", sprintId);
				casesResults.add(caseResult);
			}
			return casesResults;
		} catch (Exception e) {
			System.out.println("Not able to get resutls due to following error = "+e);
			return null;
		}			
	}
	
	/**
	 * Use this method to get priorities JSON array for the project
	 * @param prirotiesNameList list of priorities name for which priority id is needed
	 * @return integer list of the priority IDs
	 */
	public List<Integer> getPrioritiesList(List<String> prirotiesNameList) {
		List<Integer> priorityIds = new ArrayList<>();
		try {
			
			Object obj1 = client.sendGet("get_priorities/1");
			org.json.JSONArray jsonArray = new org.json.JSONArray(obj1.toString());
			
			for (int i = 0; i < jsonArray.length(); i++) {
				if (prirotiesNameList.contains(jsonArray.getJSONObject(i).getString("name").toString().trim())) {
					priorityIds.add(Integer.parseInt(jsonArray.getJSONObject(i).getString("id").toString()));
					System.out.println("Running Automation for  Priorities: " + Integer.parseInt(jsonArray.getJSONObject(i).getString("id").toString())
							+ jsonArray.getJSONObject(i).getString("name").toString());
				}
			}
			return priorityIds;
		} catch (Exception e) {
			System.out.println("Not able to fetch priorities due to error: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Use this method to get priorities hash map using of ID and NAme
	 * @return hashmap of the priority ID and priority name
	 */
	@Test
	public HashMap<Integer, String> getPriorityName() {
		int prirotyID = -1;
		String prirotyName;
		HashMap<Integer, String> priorityMap = new HashMap<Integer, String>();
		try {
			Object obj1 =  client.sendGet("get_priorities/1");
			//JSONObject jsonObject = new JSONObject(obj1.toString());
			org.json.JSONArray jsonArray = new org.json.JSONArray(obj1.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				prirotyID = Integer.parseInt((jsonArray.getJSONObject(i).getString("id").toString().trim()));
				prirotyName = jsonArray.getJSONObject(i).getString("name").toString();
						
				priorityMap.put(prirotyID, prirotyName);
			}
			System.out.println(priorityMap);
			return priorityMap;
		} catch (Exception e) {
			System.out.println("Not able to fetch priorities due to error: " + e.getMessage());
			return null;
		}
	}
}
