/**
 * @author Abhishek Gupta
 *
 */
package utility.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import base.TestRailUpdate;
import utility.HelperFunctions;

public class AlterSuite implements IAlterSuiteListener{
	static String sections  	= System.getProperty("sections")!=null ? System.getProperty("sections"): "";
	static String priorities	= System.getProperty("priorities")!=null ? System.getProperty("priorities"): "";
	public static Types type 	= defineType(System.getProperty("type"));
	
	static String sectionMappingFile = "CustomizedRun/SectionMapping.properties";
	static Properties sectionsMapping = new Properties();
	static HashMap<String, String> suiteParameters	= new HashMap<>();
	static List<String> excludedGroupLists = new ArrayList<>();
	public static List<Integer> testRailSuiteIds = new ArrayList<>();
	
	public static String[] prioritiesList = priorities.split(",");
	public static String[] sectionsList	  = sections.split(",");
	
	public static enum Types{
		Regression,
		Smoke,
		Sanity
	}
	
	public static Types defineType(String testType) {
		if(testType != null && testType.contains("Smoke")){
			type = Types.valueOf(testType.split("@")[1]);
		}else if(testType != null){
			type = Types.valueOf(testType);
		}else {
			type = Types.Regression;
		}
		return type;
	}

	@Override
    public void alter(List<XmlSuite> suites) {
		//Create an instance of XML Suite and assign a name for it.
	    XmlSuite mySuite = suites.get(0);
	    
	    //if suite is not customized-suite.xml then don't alter the suite
	    if(!mySuite.getName().equals("custom-suite")) {
			return;
		}
	    
		System.out.println("Comment For Testing" + sections + " " + priorities);
		
		TestRailUpdate testRailUpdate = new TestRailUpdate();
		sectionsMapping = HelperFunctions.readConfigFile(sectionMappingFile);	
		
		if(type.equals(Types.Smoke)) {
			sectionsList[0] = System.getProperty("type").split("@")[0];
		}

	    //add the list of tests to your Suite.
	     mySuite = getClassesFromSections(mySuite);
	     
	     if(suiteParameters.size() > 0) {
	    	 mySuite.setParameters(suiteParameters);
	     }
	     
	     if(excludedGroupLists.size() > 0) {
	    	 mySuite.setExcludedGroups(excludedGroupLists);
	     }

	     if(type.equals(Types.Regression)) {
	    	 try {
				testRailUpdate.getTestsForCustomizedExecution();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	     else{
	    	try {
				testRailUpdate.getTestsForSmokeSanity();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
	
	public static XmlSuite getClassesFromSections(XmlSuite suite) {
		
		for (String section : sectionsList) {
			XmlTest myTest = new XmlTest(suite);
			myTest.setName(section);
			
			String[] sectionMappingArray = sectionsMapping.getProperty(section).split(Pattern.quote("$$"));
			String sectionMapvalue		 = sectionMappingArray[0].trim();
			
			if(sectionMapvalue.contains("*")) {
				List<XmlPackage> suitePackages = new ArrayList<XmlPackage>();
				XmlPackage sectionPackage = new XmlPackage(sectionMapvalue);
				if(section.equals("Admin_Suite")) {
					List<String> excludeList = new ArrayList<String>();
					excludeList.add(sectionsMapping.getProperty("Admin_Suite_exclude"));
					sectionPackage.setExclude(excludeList);
				}
				suitePackages.add(sectionPackage);
				
				myTest.setXmlPackages(suitePackages);
			}else {
				List<XmlClass> suiteClasses = new ArrayList<XmlClass>();
				suiteClasses.add(new XmlClass(sectionMapvalue));	
				myTest.setXmlClasses(suiteClasses);
			}
			
			int testRailSuiteID = Integer.parseInt(sectionMappingArray[1].trim());
			if(testRailSuiteIds.indexOf(testRailSuiteID) < 0) testRailSuiteIds.add(testRailSuiteID);
			
			getSuiteParameters(sectionMappingArray);
			
			myTest.addParameter("suiteId", sectionMappingArray[1]);
			
			List<String> groups = new ArrayList<>();
			groups.add("RunThisTest");
			myTest.setExcludedGroups(groups);
		}
		
		return suite;
	}
	
	public static void getSuiteParameters(String[] parameterArray){
		if(parameterArray.length > 2) {
			for (int i = 2; i < parameterArray.length; i = i + 2) {
				if(parameterArray[i].equals("excludeGroup") && !excludedGroupLists.contains(parameterArray[i + 1])) {
					excludedGroupLists.add(parameterArray[i + 1]);
				}else if(suiteParameters.get(parameterArray[i]) == null) {
					suiteParameters.put(parameterArray[i], parameterArray[i + 1]);	
				}
			}
		}
	}
}