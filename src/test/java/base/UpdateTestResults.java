/**
 * 
 */
package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import	org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.relevantcodes.extentreports.utils.ExceptionUtil;

import utility.ExcelDataManager;
import utility.HelperFunctions;
import utility.listeners.AlterSuite;

/**
 * @author Abhishek
 *
 */
public class UpdateTestResults extends ExcelDataManager{
	String testResultsWorkbookPath = System.getProperty("user.dir")+"/Spv2RegressionQA-TDD.xls";
	String testMappingFile = "TestCaseMapping.properties";
	String reportPath = System.getProperty("user.dir")+"/test-output/test-Report.html";
	String reportTemplatePath = System.getProperty("user.dir") + "/src/test/resources/reportsTemplate.html";
	int testSheetNumber = 1;
	int classNameColumn = 0;
	int testNameColumn = 1;
	int testScriptColumn = 2;
	int testPriorityColumn = 3;
	int testResultStatusColumn = 4;
	int testResultExceptionColumn = 5;
	int totalTests = 0;
	int passedTests = 0;
	int failedTests = 0;
	int skippedTests = 0;
	
	int sectionID = 0;
	int sectionNameColumn = 1;
	int testCaseIDCol = 2;
	int testRailTcNameCol = 3;
	int testRailPriorityCol = 4;
	
	public HelperFunctions helperFunctions  = new HelperFunctions();
	TestRailUpdate testRailManager = new TestRailUpdate();
	
	public static String[][] testResults = new String[3000][6];
	public static String[][] testResultsNew = new String[3000][10];
	static int testRow = 0;
	
	public enum TestStatus{
		Pass,
		Fail,
		Skipped
	}
	
	public void prepareTestResultArray(int suiteID, int projectID){
		try {
			List<org.json.JSONArray> r = testRailManager.getTestCaseList(projectID, suiteID);
			HashMap<Integer, String> priorityMap = testRailManager.getPriorityName();
			HashMap<Object, Object> sectionData = testRailManager.getSectionsList(suiteID, projectID);
			int count = 0;
			for (int i = 0; i < r.size(); i++) {
				org.json.JSONArray obj = r.get(i);
				
				for(int j=0; j<obj.length(); j++) {
					testResultsNew[count][sectionID] = obj.getJSONObject(j).getString("section_id").toString();
					testResultsNew[count][sectionNameColumn] = sectionData.get(obj.getJSONObject(j).getString("section_id").toString()).toString();
					testResultsNew[count][testCaseIDCol] = obj.getJSONObject(j).getString("id").toString();
					testResultsNew[count][testRailTcNameCol] = obj.getJSONObject(j).getString("title").toString();
					testResultsNew[count][testRailPriorityCol] = priorityMap.get(Integer.parseInt(obj.getJSONObject(j).getString("priority_id").toString()));
					count++;
				}
			}
			System.out.println("count = " + count);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	public void updateTestResultsArray(ITestResult result) {
		Properties testsMapping = TestRailUpdate.testRailMapping;
		if(testsMapping.containsKey(result.getName()) && !(testResultsNew[0][sectionID] == null)) {
		
			String str = testsMapping.getProperty(result.getName());
			String[] cases = str.split(",");
			for(int i=0; i<cases.length; i++) {
				int row = findTestRow(cases[i]);
				if(row >= 0){
					
					//This section removes the cases entry which does not fulfill the run criteria(Combination of type, features, priority)
					if(TestRailUpdate.customExecTestList != null && TestRailUpdate.customExecTestList.size() > 0 && 
							!AlterSuite.testRailSuiteIds.contains(17)) {														//for click to call case
						boolean notfound = true;
						for (List<Integer> idList : TestRailUpdate.customExecTestList.values()) {
							if(idList!= null && idList.contains(Integer.parseInt(testResultsNew[row][testCaseIDCol]))) {
								notfound = false;
								break;
							}
						}
					
						if (notfound) {									
							continue;
						}
					}
					
					testResults[testRow][classNameColumn] = testResultsNew[row][sectionNameColumn];
					testResults[testRow][testNameColumn] = testResultsNew[row][testRailTcNameCol];
					testResults[testRow][testScriptColumn] = result.getName();
					testResults[testRow][testPriorityColumn] = testResultsNew[row][testRailPriorityCol];
					if (result.getStatus() == ITestResult.SUCCESS) {
						testResults[testRow][testResultStatusColumn] = TestStatus.Pass.toString();
					} else if (result.getStatus() == ITestResult.FAILURE) {
						testResults[testRow][testResultStatusColumn] = TestStatus.Fail.toString();
					} else if (result.getStatus() == ITestResult.SKIP) {
						testResults[testRow][testResultStatusColumn] = TestStatus.Skipped.toString();
					}
					if (result.getThrowable() != null) {
						testResults[testRow][testResultExceptionColumn] = ExceptionUtil.getStackTrace(result.getThrowable());
					}
					testRow++;	
				}
			}
		} else if(!(result.getName().contains("aa_AddCallersAsContactsAndLeads"))){
			testResults[testRow][classNameColumn] = result.getTestClass().getName();
			testResults[testRow][testNameColumn] = result.getName();
			testResults[testRow][testScriptColumn] = result.getName();
			testResults[testRow][testPriorityColumn] = "N/A";
			if (result.getStatus() == ITestResult.SUCCESS) {
				testResults[testRow][testResultStatusColumn] = TestStatus.Pass.toString();
			} else if (result.getStatus() == ITestResult.FAILURE) {
				testResults[testRow][testResultStatusColumn] = TestStatus.Fail.toString();
			} else if (result.getStatus() == ITestResult.SKIP) {
				testResults[testRow][testResultStatusColumn] = TestStatus.Skipped.toString();
			}
			if (result.getThrowable() != null) {
				testResults[testRow][testResultExceptionColumn] = ExceptionUtil.getStackTrace(result.getThrowable());
			}
			testRow++;
		}
	}
	
	public void emptyExcelSheet(){
		openExcelFile(testResultsWorkbookPath, testSheetNumber);
		emptyExcel();
	}

	public void putTestResultsInExcel(ITestContext context, String sprintName){
		try{
			openExcelFile(testResultsWorkbookPath, testSheetNumber);
			updateTestResultInExcel();
			generateHTMLTestReport();
			updateAfterSuiteDetails(context, sprintName);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void updateTestResultInExcel(){
		//Add Headers to the Excel sheet
		createRow(0);
		addCellData(0, classNameColumn, "Test Class");
		addCellData(0, testNameColumn, "Test Name");
		addCellData(0, testScriptColumn, "Test Script Name");
		addCellData(0, testPriorityColumn, "Priority");
		addCellData(0, testResultStatusColumn, "Test Result");
		addCellData(0, testResultExceptionColumn, "Test Exception");
		
		//update status of the test results in excel from test result array
		for (int i = 0; i < testRow; i++) {
			String className = testResults[i][classNameColumn];
			String testName = testResults[i][testNameColumn];
			String scriptName = testResults[i][testScriptColumn];
			String testPriority = testResults[i][testPriorityColumn];
			String testStatus = testResults[i][testResultStatusColumn];
			
			//trim the error message if its length is greater than 3000 characters
			String exceptionMessage = testResults[i][testResultExceptionColumn];
			if(exceptionMessage != null) {
				int maxErrorLength = exceptionMessage.length() < 3000 ? exceptionMessage.length(): 3000;
				exceptionMessage = StringEscapeUtils.escapeHtml4(exceptionMessage.substring(0, maxErrorLength));
			}
			
			int rowNumforTestName = getRowNumberForCellValue(testNameColumn, testName, testScriptColumn, scriptName, classNameColumn, className);
			if (rowNumforTestName > 0) {																//if result is already there in the excel
				updateCellData(rowNumforTestName,testResultStatusColumn,testStatus);
				if(exceptionMessage==null){
					updateCellData(rowNumforTestName,testResultExceptionColumn,"");
				}else{
					updateCellData(rowNumforTestName,testResultExceptionColumn,exceptionMessage);
				}
			}else if(!testName.contains("ReportGeneration")){											//for new test results entry
				int newRow = getLastRow()+1;
				createRow(newRow);
				addCellData(newRow, classNameColumn, className);
				addCellData(newRow, testNameColumn, testName);
				addCellData(newRow, testScriptColumn, scriptName);
				addCellData(newRow, testPriorityColumn, testPriority);
				addCellData(newRow, testResultStatusColumn,testStatus);
				addCellData(newRow, testResultExceptionColumn,exceptionMessage);
			}
		}
	}
	
	public void generateHTMLTestReport() {
		System.out.println("Generating Customized Report");
		openExcelFile(testResultsWorkbookPath, testSheetNumber);
		List<String> classesList = getClassesName();
		StringBuilder table = new StringBuilder();
		
		for (int j = 0; j < classesList.size(); j++) {
			
			String className = classesList.get(j);
			table.append("<div class=\"row1\" >"
					+ "<div class=\"text-details-heading-label\">"+className+"</div>"
					+"<table class=\"table-bordered\" id =\"test_result\">"
						+"<thead class=\"text-heading-label\">"
							+"<tr>"
								+"<th class=\"table-cell\" colspan=\"4\" >"
								+"<div class=\"parent_row\">"
									+"<div class=\"div_cell1\">Test Case</div>"
									+"<div class=\"div_cell2\">Automation Test Script</div>"
									+"<div class=\"div_cell5\">Priority</div>"
									+"<div class=\"div_cell3\">Test Result</div>"
									+"<div class=\"div_cell3\">Error Message</div>"
								+"</div>"											
								+"</th>"
								+"</thead>"
						+"<tbody id=\"test_result_body\">");
			
			for (int i = 0; i < getLastRow(); i++) {
				if (className.equals(getCellValue(i + 1, classNameColumn))) {
					table = createTable(i, table);
				}
			}
			table.append("</tbody></table></div>");
		}
		totalTests = passedTests + failedTests + skippedTests;
		String report = readHTMLFile(reportTemplatePath);
		report = report.replace("$$Pass_Count$$", Integer.toString(passedTests));
		report = report.replace("$$Fail_Count$$", Integer.toString(failedTests));
		report = report.replace("$$Skip_Count$$", Integer.toString(skippedTests));
		report = report.replace("$$Total_Tests$$", Integer.toString(totalTests));
		report = report.replace("$$Skipped$$", Integer.toString(skippedTests));
		report = report.replace("$$Test_Result_Table$$", table);
		
		//replace test rail link in the report template
		if(!TestRailUpdate.runWiseSuites.keySet().isEmpty()) {
			int run = TestRailUpdate.runWiseSuites.values().iterator().next();	
			report = report.replace("$$TestRailLink$$", "<a href=\"https://ringdna.testrail.net/index.php?/runs/view/"
					+ run
					+ "\" style=\"font-weight: bold; text-decoration: underline;\" target=\"_blank\">Open In Test Rail</a>");
		}else {
			report = report.replace("$$TestRailLink$$", "");
		}
		
		//replace type of case in report template
		report = report.replace("$$Type$$", AlterSuite.type !=null ? AlterSuite.type.toString():"Other");
		
		saveFile(report);
		System.out.print("");
	}
	
	private StringBuilder createTable(int i, StringBuilder table){
		String testStatus=null;
		String testName = getCellValue(i + 1, testNameColumn);
		String testScriptName = getCellValue(i + 1, testScriptColumn);
		String testPriorityame = getCellValue(i + 1, testPriorityColumn);
		String testStatusExcel = getCellValue(i + 1, testResultStatusColumn);
		if (testStatusExcel.equals(TestStatus.Pass.toString())) {
			testStatus = "<div class=\"div_cell3 pass\">Pass</div>";
			passedTests++;
		} else if (testStatusExcel.equals(TestStatus.Fail.toString())) {
			String testException = getCellValue(i + 1, testResultExceptionColumn);
			testStatus = "<div class=\"div_cell3 fail\">Fail</div>"
					+ "<span class=\"div_cell4\">Error</span>"
					+ "<a style=\"float:left;display:block;\" onclick='showMore(this, \""
					+ testException.replace("\r\n\t", "<br/>").replace("\n\t", "<br/>").replace("\r\n", "<br/>").replace("\n", "<br/>").replace("'", "")
					+ "\")'>"
					+ "<span class=\"glyphicon glyphicon-menu-down\" style=\"width:20px; height:20px;\"></span>"
					+ "</a>"
					+ "<a id=\"menuUp\" style=\"display:none;\" onclick=\"showLess(this)\">"
					+ "<span class=\"glyphicon glyphicon-menu-up\" style=\"width:20px; height:20px;\"></span>"
					+ "</a>" + "</div>";
			failedTests++;
		} else {
			testStatus = "<div class=\"div_cell3 skip\">Skip</div>";
			skippedTests++;
		}
		table.append("<tr class=\"table-row\">" + "<td colspan=\"4\">"
				+ "<div class=\"parent_row\">"
				+ "<div class=\"div_cell1\"><b>" + testName + "</b></div>"
				+ "<div class=\"div_cell2\">"+ testScriptName +"</div>" 
				+ "<div class=\"div_cell5\">"+ testPriorityame +"</div>" 
				+ testStatus
				+ "</td></tr>");	
		return table;
	}
	
	private void updateAfterSuiteDetails(ITestContext context, String sprintName){
		String suiteName = context.getSuite().getName().replace("Failed suite [", "").replace("]", "");
		String report = readHTMLFile(reportPath);
		report = report.replace("$$Suite_Name$$", suiteName);
		report = report.replace("$$Sprint$$",sprintName);
		report = report.replace("$$Execution_Date$$", new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
		saveFile(report);
		System.out.println("Report has been generated");
	}
	
	private List<String> getClassesName(){
		List<String> className = new ArrayList<String>();
		try {
			className.add(getCellValue(1, classNameColumn));
			for (int i = 1; i < getLastRow() + 1; i++) {
				for (int j = 0; j < className.size(); j++) {
					if (className.get(j).equals(getCellValue(i, classNameColumn))) {
						break;
					} else if (j == (className.size() - 1)) {
						className.add(getCellValue(i, classNameColumn));
					}
				}
			}
			return className;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return className;
		}
	}
	
	private int findTestRow(String testCaseID){
		for (int i = 0; i < testResultsNew.length; i++) {
				if(testResultsNew[i][testCaseIDCol] == null){
					return -1;
				}else if(testResultsNew[i][testCaseIDCol].trim().equals(testCaseID.trim())) {
					return i;
				}
			}
		return -1;
	}
	
	@SuppressWarnings("deprecation")
	private String readHTMLFile(String filePath){
		try {
			return Files.toString(new File(filePath), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void saveFile(String strContent) {
		BufferedWriter bufferedWriter = null;
		try {
			File myFile = new File(reportPath);
			// check if file exist, otherwise create the file before writing
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			FileWriter writer = new FileWriter(myFile);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(strContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null)
					bufferedWriter.close();
			} catch (Exception ex) {

			}
		}
	}	
	
	/*private void generateExtentReport(){
		ExtentReports extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/MyOwnReport.html", true);
		ExtentTest test=null;
		for(int i=0; i<getLastRow();i++){
			test = extent.startTest(getCellValue(i+1, 1));
			if(getCellValue(i+1, 2).equals(TestStatus.Pass.toString())){
				test.log(LogStatus.PASS,"");
			}
			else if(getCellValue(i+1, 2).equals(TestStatus.Fail.toString())){
				test.log(LogStatus.FAIL,getCellValue(i+1, 3));
			}
			else{
				test.log(LogStatus.SKIP,getCellValue(i+1, 3));
			}
			extent.endTest(test);
		}
		extent.flush();
	}*/
}