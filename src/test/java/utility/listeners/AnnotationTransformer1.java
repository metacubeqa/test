package utility.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import base.TestRailUpdate;

public class AnnotationTransformer1 implements IAnnotationTransformer {
	public static int count = 0;
	TestRailUpdate testRailUpdate = new TestRailUpdate();
	//This is to transform the annotation for listener so need not to mention with every test method
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		String[] groups = {"RunThisTest"};
		List<String> methodsList = new ArrayList<String>();
		methodsList.addAll(TestRailUpdate.customExecTestList.keySet());
		if (!methodsList.contains(testMethod.getName())) {
		     annotation.setGroups(groups);
		}else {
			List<Integer> mappedCasesCount = TestRailUpdate.customExecTestList.get(testMethod.getName());
			count = count + (mappedCasesCount != null ? mappedCasesCount.size() : 0);
		}
		annotation.setRetryAnalyzer(RetryListener.class);
	}
}