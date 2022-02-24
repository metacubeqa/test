package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import support.source.accounts.AccountIntelligentDialerTab;
import support.source.accounts.AccountIntelligentDialerTab.Days;
import utility.HelperFunctions;

public class ComplainceHours extends SoftphoneBase{
	
	private static String indianTimeZone	= "Asia/Calcutta";
	private static String usTimeZone		= "PST";
	private static String compHourErrorMsg 	= "The phone number's local time is set outside of your set compliance hours.";
	
	@BeforeMethod(groups={"Sanity", "Regression", "MediumPriority"})
	public void removeAllComplianceHoursRestriction() {
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //verify that if support tab is not there then open it
		if (seleniumBase.getTabCount(driver1) < 2) {

			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		}
		
		//remove compliance hours set for all days
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1) );
		accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
		accountIntelligentDialerTab.removeComplainceHoursSettings(driver1);
		accountIntelligentDialerTab.verifydefaultComplainceHoursDayTable(driver1);
		
		//disable compliance hour and save the settings
		accountIntelligentDialerTab.disableComplainceHourSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	}
	
	@Test(groups={"MediumPriority"})
	  public void verify_saved_complaince_hours()
	  {
	    System.out.println("Test case --verify_saved_complaince_hours-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    Days day1 = AccountIntelligentDialerTab.Days.Sun;
	    String day1StartTime = "01:00 am";
	    String day1EndTime = "02:00 am";
	    
	    Days day2 = AccountIntelligentDialerTab.Days.Tue;
	    String day2StartTime = "12:25 pm";
	    String day2EndTime =  "04:30 pm";
	    
	    //enable compliance hours and set it for two alternative days. Monday
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.enterComplainceHours(driver1, day1, day1StartTime, day1EndTime, false);
	    accountIntelligentDialerTab.saveComplainceHourSettings(driver1);
	    
	    //Tuesday
	    accountIntelligentDialerTab.enterComplainceHours(driver1, day2, day2StartTime, day2EndTime, false);
	    accountIntelligentDialerTab.saveComplainceHourSettings(driver1);
	    
	    //verify that compliance hours are set and visible on the compliance hours table
	    accountIntelligentDialerTab.verifyComplainceHoursForDay(driver1, day1, day1StartTime, day1EndTime);
	    accountIntelligentDialerTab.verifyComplainceHoursForDay(driver1, day2, day2StartTime, day2EndTime);
	   
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	 @Test(groups={"MediumPriority"})
	  public void verify_invalid_complaince_hours()
	  {
	    System.out.println("Test case --verify_invalid_complaince_hours-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	
	    //Test 
	    //add an invalid date by using the arrow icons
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.enterComplainceHours(driver1, AccountIntelligentDialerTab.Days.Sun, "13:65 am", "", false);
	    
	    //verify that invalid value is not entered
	    assertEquals(accountIntelligentDialerTab.getComplainceHourStartTime(driver1), "13:06");
	    accountIntelligentDialerTab.closeComplainceHourWindow(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_outside_complaince_hours_us()
	  {
	    System.out.println("Test case --verify_outside_complaince_hours_us-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    //enable the compliance hours setting and set a time which make current time out of this range
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.setComplainceDayAndPeriod(driver1, usTimeZone, false);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
	    //switch to softphone and reload it
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //Try calling to a us number
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    
	    //verify that the error message appears
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //Try to send a message and verify that error message appears
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.selectMessagingNumber(driver1, 0);
	    softPhoneActivityPage.enterMessageText(driver1, "test");
	    softPhoneActivityPage.clickSendButton(driver1);
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //verify user is able to take incoming call without any issue
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //no error message shoud appear
	    assertFalse(callScreenPage.verifyErrorPresent(driver1));
	    
	    //remove all restrictions
	    removeAllComplianceHoursRestriction();
	    
	    //verify that no restrictions are there
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.verifydefaultComplainceHoursDayTable(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //After the restrictions are remove, verify that user is able to make outbound calls
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_inside_complaince_hours_us()
	  {
	    System.out.println("Test case --verify_inside_complaince_hours_us-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    //enable the compliance hours setting and set a time range for us time zone which make current time in this range
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.setComplainceDayAndPeriod(driver1, usTimeZone, true);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
	    //switch to softphone and reload it
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //verify that user is able to make outbound call to us number without any issue
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //no error should appear
	    assertFalse(callScreenPage.verifyErrorPresent(driver1));
	    
	    //For Indian time zone numbers call and message should not be able to make
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("local_verified_indian_cell_Number"));
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //verify error message for messaging as well
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.selectMessagingNumber(driver1, 0);
	    softPhoneActivityPage.enterMessageText(driver1, "test");
	    softPhoneActivityPage.clickSendButton(driver1);
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_inside_complaince_hours_sms_india()
	  {
	    System.out.println("Test case --verify_inside_complaince_hours_sms_india-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    
	    accountIntelligentDialerTab.setComplainceDayAndPeriod(driver1, indianTimeZone, true);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("local_verified_indian_cell_Number"));
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	
	    //send a text message to caller 2
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
	    seleniumBase.idleWait(2);
	    
	    assertTrue(softPhoneActivityPage.verifyOutboundMessage(driver1, outboundMessage));
	 
	    assertFalse(callScreenPage.verifyErrorPresent(driver1));
	    
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_outside_complaince_hours_sms_india()
	  {
	    System.out.println("Test case --verify_outside_complaince_hours_sms_india-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    //enable compliance hours setting and set the time range so that current time is out of it. Time zone is of India
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.setComplainceDayAndPeriod(driver1, indianTimeZone, false);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
	    //Switch to softphone and reload the softphone
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //Verify that user is not able to make a call
	    softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("local_verified_indian_cell_Number"));
	    softPhoneContactsPage.clicksfdcResultsCallButton(driver1, 0, SoftPhoneContactsPage.searchTypes.Contacts.toString());
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //user is also not able to send a message
	    softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("local_verified_indian_cell_Number"));
	    softPhoneContactsPage.clickFirstContactsMsgIcon(driver1);
	    softPhoneActivityPage.selectMessagingNumber(driver1, 0);
	    softPhoneActivityPage.enterMessageText(driver1, "test");
	    softPhoneActivityPage.clickSendButton(driver1);
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_all_day_complaince_hours_us()
	  {
	    System.out.println("Test case --verify_outside_complaince_hours_us-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String complaineHoursDateFormatString = "hh:mma dd/MM/yyyy E";
		SimpleDateFormat compainceHourDateFormat = new SimpleDateFormat(complaineHoursDateFormatString);
	    
		//enable complaince hours setting and verify that the table should have no restrictions for all number by default.
	    accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.verifydefaultComplainceHoursDayTable(driver1);
	    
	    //Set compliance hours disabled for all day for Indian time zone
		String indianTime = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(HelperFunctions.GetCurrentDateTimeObj()).toString(), indianTimeZone, complaineHoursDateFormatString);
		String indianDay = indianTime.substring(indianTime.length() - 3, indianTime.length());
		
	    accountIntelligentDialerTab.enterComplainceHours(driver1, AccountIntelligentDialerTab.Days.valueOf(indianDay), "", "", true);
	    accountIntelligentDialerTab.saveComplainceHourSettings(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    
	    //switch to softphone and reload it
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //try to make an outbound call to an Indian number and verify the error message
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("local_verified_indian_cell_Number"));
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //Set compliance hours disabled for all day for Us time zone
		String usTime = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(HelperFunctions.GetCurrentDateTimeObj()).toString(), usTimeZone, complaineHoursDateFormatString);
		String usDay = usTime.substring(usTime.length() - 3, usTime.length());
	    
		removeAllComplianceHoursRestriction();
		accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.enterComplainceHours(driver1, AccountIntelligentDialerTab.Days.valueOf(usDay), "", "", true);
	    accountIntelligentDialerTab.saveComplainceHourSettings(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	   
	    //switch to softphone and reload it
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //try to make an outbound call to an US number and verify the error message
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
	    //Set compliance hours disabled for all day for Australia time zone
	    String ausTime = HelperFunctions.getDateTimeInTimeZone(compainceHourDateFormat.format(HelperFunctions.GetCurrentDateTimeObj()).toString(), "Australia/Sydney", complaineHoursDateFormatString);
		String ausDay = ausTime.substring(ausTime.length() - 3, ausTime.length());
	    
		removeAllComplianceHoursRestriction();
		accountIntelligentDialerTab.enableComplainceHourSetting(driver1);
	    accountIntelligentDialerTab.enterComplainceHours(driver1, AccountIntelligentDialerTab.Days.valueOf(ausDay), "", "", true);
	    accountIntelligentDialerTab.saveComplainceHourSettings(driver1);
	    accountIntelligentDialerTab.saveAcccountSettings(driver1);
	   
	    //switch to softphone and reload it
	    seleniumBase.switchToTab(driver1, 1);
	    reloadSoftphone(driver1);
	    
	    //try to make an outbound call to an Australian number and verify the error message
	    softPhoneCalling.softphoneAgentCall(driver1, "+61386574292"); 
	    assertEquals(callScreenPage.getErrorText(driver1), compHourErrorMsg);
	    callScreenPage.closeErrorBar(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
		System.out.println("Test case is pass");
	}

	// disable complaince hours setting once all cases have been run
	@AfterClass(groups = { "Sanity", "Regression", "MediumPriority" })
	public void disableComplianceHoursRestriction() {
		removeAllComplianceHoursRestriction();
	}
}
