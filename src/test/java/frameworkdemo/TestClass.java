package frameworkdemo;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.beust.jcommander.Parameter;

public class TestClass {
  
  @Parameters({"string"})	
  @Test
  public void checkPalindrome(String string) {
	  String revString = "";
	  for(int i=string.length()-1;i>=0;i--) {
		  revString+=string.charAt(i);
	  }
	  System.out.println(string);
	  System.out.println(revString);
	 
	  Assert.assertEquals(revString, string);
  }

}
