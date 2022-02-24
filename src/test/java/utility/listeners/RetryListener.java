package utility.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {

	public static int count = 0;
	public static int maxTry = 1;

	public boolean retry(ITestResult iTestResult) {
		if (!iTestResult.isSuccess()) {                      						//Check if test not succeed
			if (count < maxTry) {                            						//Check if maxtry count is reached
				System.out.println("In retry loop");
				count++;                                     						//Increase the maxTry count by 1
				iTestResult.setStatus(ITestResult.FAILURE);  						//Mark test as failed
				return true;                                 						//Tells TestNG to re-run the test
			} else {
				iTestResult.setStatus(ITestResult.FAILURE);  						//If maxCount reached,test marked as failed
			}
		} else {
			iTestResult.setStatus(ITestResult.SUCCESS);      						//If test passes, TestNG marks it as passed
		}
		System.out.println("outside retry loop");
		count = 0;
		return false;
	}

}
