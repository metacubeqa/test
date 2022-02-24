package softphone.cases.softPhoneMessaging;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.SoftPhoneMessagePage.MsgType;
import support.source.accounts.AccountBlockedNumbersTab.BlockType;
import support.source.accounts.AccountBlockedNumbersTab.Direction;
import support.source.messages.MessagesPage;
import support.source.smartNumbers.SmartNumbersPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class SoftPhoneMessaging extends SoftphoneBase{
		
	  String unSubscribeMessage = "You have successfully been unsubscribed. You will not receive any more messages from this number. Reply START to resubscribe. ";
	  String reSubscribeMessage	= "You have successfully been re-subscribed to messages from this number. Reply HELP for help. Reply STOP to unsubscribe. Msg&Data Rates May Apply. ";
	  String msgDateFormate	= "MM/dd/yyyy";
	  String msgTimeFormate	= "h:mm a";
	  Boolean isNumberBlocked = false;
	  
	  @BeforeClass(groups={"Sanity", "Regression"})
	  public void addCallerAsContact(){
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);

		//add contact
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");		  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
	  }
	  
	  @Test(groups={"Sanity"})
	  public void softphone_calling_send_receive_message()
	  {
	    System.out.println("Test case --softphone_calling_send_receive_message()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //send a text message to caller 2
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
	    
	    //verify that message is received by caller 2
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, outboundMessage));
	    
	    String previousMessageCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1) ;
	    
	    String InboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //hangup calls
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //send a text message from caller 2
	    reloadSoftphone(driver1);
	    softPhoneActivityPage.sendMessage(driver3, InboundMessage, 0);
	    
	    //wait for message to get received
	    softPhoneMessagePage.idleWait(2);
	    
	    //Get unread Message count
	    String messageCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1) ;
	    
	    //verify unread count is increase
	    assertEquals(Integer.parseInt(messageCount), Integer.parseInt(previousMessageCount)+1);
	    
	    //open recent message
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage));
	    
		//verifying that call is disconnected for both the users
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //Verifying outbound message
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getComments(driver1), outboundMessage);
	    driver1.navigate().back();
	    
	    //Verifying Inbound message
		contactDetailPage.openActivityFromList(driver1, "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getComments(driver1), InboundMessage);
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //@Test(groups={"Sanity", "Regression"})
	  public void start_stop_messages()
	  {
	    System.out.println("Test case --start_stop_messages()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);
		
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//open sms page from caller agent
	    System.out.println("open sms page from caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //unsubscribe messages and verify message
	    softPhoneActivityPage.sendMessage(driver1, "STOP", 0);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, unSubscribeMessage));
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //verify messages are restricted
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, "123");
	    softPhoneActivityPage.clickSendButton(driver4);
	    softPhoneMessagePage.idleWait(2);
	    callScreenPage.verifyErrorPresent(driver4);
	    softPhoneSettingsPage.closeErrorMessage(driver4);
	    
	    //resubscribe sms and verify message
	    softPhoneActivityPage.sendMessage(driver1, "START", 0);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, reSubscribeMessage));
	    
	    //send a text message to caller
	    softPhoneActivityPage.sendMessage(driver4, message, 0);
	    
	    //wait for message to get received
	    softPhoneMessagePage.idleWait(2);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, message));
	    	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver4", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void send_outbound_message_verify_task()
	  {
	    System.out.println("Test case --send_outbound_message_verify_task()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String InboundMessage = "IndboundMessage" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		softPhoneSettingsPage.setDefaultSoftPhoneSettings(driver1);
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    String outboundMsgDate	= HelperFunctions.GetCurrentDateTime(msgDateFormate);
	    String outboundMsgTime	= HelperFunctions.GetCurrentDateTime(msgTimeFormate);
	    String outboundMsgTime1	= HelperFunctions.GetCurrentDateTime(msgTimeFormate, true);
	    String msgDateTimeActivityPage = softPhoneActivityPage.getOutboundMsgDateTime(driver1, message);
	    assertTrue(msgDateTimeActivityPage.contains(outboundMsgDate));
	    assertTrue((msgDateTimeActivityPage.contains(outboundMsgTime) || msgDateTimeActivityPage.contains(outboundMsgTime1)));
	    
	    //open recent message
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.verifyNewMsgDotVisible(driver3, message);
	    String msgDateTimeMessagePage = softPhoneActivityPage.getOutboundMsgDateTime(driver1, message);
	    assertTrue(msgDateTimeMessagePage.contains(HelperFunctions.GetCurrentDateTime(msgDateFormate)));
	    assertTrue(msgDateTimeMessagePage.contains(HelperFunctions.GetCurrentDateTime(msgTimeFormate)) || msgDateTimeMessagePage.contains(HelperFunctions.GetCurrentDateTime(msgTimeFormate, true)));
	    assertEquals(msgDateTimeActivityPage, msgDateTimeMessagePage);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    
	    //verify message on softphone
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message));
	    
	    //Verifying Inbound message on sfdc
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getFromNumber(driver1)));
	    assertEquals(sfTaskDetailPage.getComments(driver1), message);
	    
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    //Send message
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.idleWait(2);
	    String previousMessageTabCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1) ;
	    String previousMessageCount = softPhoneMessagePage.getUnreadMessageCount(driver1) ;
	    softPhoneActivityPage.sendMessage(driver3, InboundMessage, 0);
	    softPhoneMessagePage.idleWait(5);
	    
	    //verify unread count is increased
	    assertEquals(Integer.parseInt(softPhoneMessagePage.getUnreadMessageTabCount(driver1)), Integer.parseInt(previousMessageTabCount) + 1);
	    assertEquals(Integer.parseInt(softPhoneMessagePage.getUnreadMessageCount(driver1)), Integer.parseInt(previousMessageCount) + 1);
	    
	    //verify from message page that message is received by agent
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage));
	    
	    //Verifying Inbound message on salesforce
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getComments(driver1), InboundMessage);
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(sfTaskDetailPage.getFromNumber(driver1)));
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity"})
	  public void softphone_msg_recent_caller()
	  {
	    System.out.println("Test case --softphone_msg_recent_caller()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String InboundMessage  = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //verifying that call is disconnected for both the users
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //send a text message to caller 2
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
	    
	    //verify that message is received by caller 2
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, outboundMessage));
	    
	    String previousMessageCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1) ;
	    
	    //send a text message from caller 2
	    softPhoneActivityPage.sendMessage(driver3, InboundMessage, 0);
	    
	    //wait for message to get received
	    softPhoneMessagePage.idleWait(2);
	    
	    //Get unread Message count
	    String messageCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1) ;
	    
	    //verify unread count is unchanged
	    assertEquals(Integer.parseInt(messageCount), Integer.parseInt(previousMessageCount));
	   
	    //verify that message is received by agent
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage));
	    
	    //open message page and verify that messages are sorted by last date
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.isMessageListSorted(driver1);
	   
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_call_after_opening_msg_history()
	  {
	    System.out.println("Test case --verify_call_after_opening_msg_history()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //send messages
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    
	    //open recent message
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    
	    //call user
	    softPhoneCalling.clickCallBackButton(driver1);
	    
	    //receive call
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //disconnect call
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void send_message_create_lead()
	  {
	    System.out.println("Test case --send_message_create_lead()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //convert caller to single caller
	    aa_AddCallersAsContactsAndLeads();
	    
	    //Making an outbound call from softphone and delete contact if exist
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    if(!callScreenPage.isCallerUnkonwn(driver1)){
	    	callScreenPage.deleteCallerObject(driver1);
	    }
	    
	    reloadSoftphone(driver1);
	    
	    //open sms page from the dial pad
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //verify add new button not appearing
	    assertFalse(callScreenPage.isAddNewButtonVisible(driver1));
	    
	    //send message and add as contact
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    
	    //verify that caller name contains the first name of caller 3
	    assertTrue(callScreenPage.getCallerName(driver1).contains(CONFIG.getProperty("qa_user_3_name")));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	   
	  @Test(groups={"Regression"})
	  public void send_message_add_to_existing()
	  {
	    System.out.println("Test case --send_message_add_to_existing()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    String existingContact;
		String contactNumber = CONFIG.getProperty("prod_user_1_number");

		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));
		existingContact = softPhoneContactsPage.getSfdcContactsResultNames(driver1).get(0).getText();

	    //Making an outbound call from softphone and delete contact if exist
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    if(!callScreenPage.isCallerUnkonwn(driver1)){
	    	callScreenPage.deleteCallerObject(driver1);
	    }
	    
	    //open sms page from the dial pad
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver3);
	    softPhoneActivityPage.sendMessage(driver3, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    
	    //open message from message tab
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, message));
	    
	    //send message and add as contact
	    callScreenPage.addCallerToExistingContact(driver1, existingContact);
	    
	    //verify that caller name contains the first name of caller 3
	    assertTrue(callScreenPage.getCallerName(driver1).contains(existingContact));
	    
	    //verify inbound message
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, message));
	    
	    //wait until the contact is multiple
	    softPhoneContactsPage.searchUntilContacIsMultiple(driver1, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_user_3_number")));
	    
	    //make caller single again
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void send_message_preserve_text()
	  {
	    System.out.println("Test case --send_message_preserve_text()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		String contactNumber = CONFIG.getProperty("prod_user_1_number");

	    //Making an outbound call from softphone and delete contact if exist
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	    softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver2);
	    
	    //open sms page from the dial pad
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
	    
	    //open message from message tab
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneCalling.isCallBackButtonVisible(driver1);
	    
	    //open sms page from the dial pad
	    softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softphoneCallHistoryPage.openCallEntryByIndex(driver1, 1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    assertEquals(softPhoneActivityPage.getMessageText(driver1), message);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }

	  @Test(groups={"Regression"})
	  public void send_msg_to_another_caller()
	  {
	    System.out.println("Test case --softphone_msg_recent_caller()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String outboundMessage = "Outbound Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message));	    
	    
	    //verifying that call is disconnected for both the users
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneCalling.isCallBackButtonVisible(driver3);
	    
	    //Making an outbound call to another user
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_1_number"));
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver2);
	    
	    //send a text message to caller 2
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
	    
	    //verify that message is received by caller 2
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, outboundMessage));
	    
	    //verifying that call is disconnected for both the users
	    softPhoneCalling.hangupActiveCall(driver2);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void contact_card_take_incoming_msg(){
	    System.out.println("Test case --contact_card_take_incoming_msg()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
	    
		//Making an outbound call from softphone
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	    
		//receiving call from app softphone
	    System.out.println("first caller picking up the call");
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    
	    //verifying that call is disconnected for both the users
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //get unread message count
	    String unreadMessageCount = softPhoneMessagePage.getUnreadMessageTabCount(driver3);
	    
	    //Search the contact and open message tab for it
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	    softPhoneContactsPage.clickMessageIconByNumber(driver1, HelperFunctions.getNumberInRDNAFormat(contactNumber));
	    
	    //send an outbound message
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    
	    //verify that message is received
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message));
	    assertTrue(unreadMessageCount.equals(softPhoneMessagePage.getUnreadMessageTabCount(driver3)));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_placeholder_contact_card_mobile_number(){
	    System.out.println("Test case --verify_placeholder_contact_card_mobile_number()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String mobileNumber = "+91912345678";
	    String contactNumber = CONFIG.getProperty("qa_user_3_number");
	    
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    //Search the contact and open message tab then verify that placeholder is default number
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	    softPhoneContactsPage.clickFirstContactsMsgIcon(driver1);
	    assertTrue(softPhoneActivityPage.getMessagePlaceholderText(driver1).contains(HelperFunctions.getNumberInRDNAFormat(contactNumber)));
	    
	    //Make call to mobile number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, mobileNumber);
	    softPhoneMessagePage.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
	    //add mobile number to existing contact
	    callScreenPage.addMobileCallerToExistingContact(driver1, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	    
	    //Search the contact and open message tab for it and verify that placeholder has mobile number
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	    softPhoneContactsPage.clickMessageIconByNumber(driver1, mobileNumber);
	    softPhoneMessagePage.idleWait(1);
	    assertTrue(softPhoneActivityPage.getMessagePlaceholderText(driver1).contains(mobileNumber));
	    
	    //delete and add contact again
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_dropdown_for_single_multiple_agents(){
	    System.out.println("Test case --verify_dropdown_for_single_multiple_agents()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    aa_AddCallersAsContactsAndLeads();
	    
	    String msgReceiver = CONFIG.getProperty("prod_user_1_number");
	    
  		String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		
	    //getting caller name to add as contact
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    String existingContact = callScreenPage.getCallerName(driver1);
  		
	    //delete and add contact again
	    softPhoneContactsPage.deleteAndAddContact(driver1, msgReceiver, CONFIG.getProperty("prod_user_1_name"));
  		
		//Making an outbound call from softphone
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, message1, 0);
	    softPhoneActivityPage.isSpamLinkNotPresent(driver1);
	    
	    //verify that sender agent name appears for the message owner
	    assertEquals(softPhoneActivityPage.getMessageOwnerName(driver1), CONFIG.getProperty("qa_user_1_name"));
	    
	    //adding caller as multiple contact
	 	callScreenPage.clickOnUpdateDetailLink(driver1);
	 	callScreenPage.addCallerToExistingContact(driver1, existingContact);
	 	softPhoneMessagePage.idleWait(10);
	 	softPhoneContactsPage.searchUntilContacIsMultiple(driver1, msgReceiver);
	    
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Making an outbound call from softphone
	    softPhoneCalling.softphoneAgentCall(driver2, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.pickupIncomingCall(driver1);
	    softPhoneActivityPage.openMessageTab(driver2);
	    softPhoneActivityPage.sendMessage(driver2, message2, 0);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that span link is not present for the multi match caller
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.isSpamLinkNotPresent(driver1);
		
	    //Make user single match caller
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("qa_user_1_number"), CONFIG.getProperty("qa_user_1_name"));
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //@Test(groups={"Regression"})
	  public void verify_dropdown_for_unknown_user(){
	    System.out.println("Test case --verify_dropdown_for_unknown_user()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String unknownReceiver = CONFIG.getProperty("qa_user_2_number");
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
	    
	    // Making a unknown receiver contact
  		softPhoneCalling.softphoneAgentCall(driver3, unknownReceiver);
  		softPhoneCalling.pickupIncomingCall(driver4);
  		softPhoneCalling.hangupActiveCall(driver3);

  		// Deleting contact
  		if (!callScreenPage.isCallerUnkonwn(driver3)) {
  			callScreenPage.deleteCallerObject(driver3);
  			softPhoneMessagePage.idleWait(3);
  			softPhoneSettingsPage.closeErrorMessage(driver3);
  		}
	    
  		String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		
		//Send first message
	    softPhoneCalling.softphoneAgentCall(driver1, unknownReceiver);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.sendMessage(driver1, message1, 0);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //verify that sender agent name appears for the message owner
	    assertEquals(softPhoneActivityPage.getMessageOwnerName(driver1), CONFIG.getProperty("qa_user_1_name"));
	    
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		//Making an outbound call from softphone
	    softPhoneCalling.softphoneAgentCall(driver3, unknownReceiver);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneActivityPage.openMessageTab(driver3);
	    softPhoneActivityPage.sendMessage(driver3, message2, 0);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    //verify multiple sender name after adding as contact
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_2_name"), CONFIG.getProperty("contact_account_name"));
	    List<String> messageOwnerNames = softPhoneActivityPage.getMessageOwnerNamesList(driver1);
	    assertTrue(messageOwnerNames.contains(CONFIG.getProperty("qa_user_1_name")) && messageOwnerNames.contains(CONFIG.getProperty("qa_user_3_name")));
		
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void softphone_messaging_global_local_presence()
	  {
	    System.out.println("Test case --softphone_messaging_global_local_presence()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    String areaCode = CONFIG.getProperty("prod_user_1_number").substring(2, 5) ;
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name")); 
	    
	    //Verify global local presene for message
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneMessagePage.idleWait(2);
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, message));
	    
	    //Verifyinh that area code is same
	    String localPresenceNumber= callScreenPage.getCallerNumber(driver2);
	    assertEquals(areaCode, localPresenceNumber.substring(0,3));
	    
	    //Verifying outbound message
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Outbound Message: " + CONFIG.getProperty("prod_user_1_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertEquals(sfTaskDetailPage.getComments(driver1), message);
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver1);
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver1).contains(localPresenceNumber));
	    driver1.navigate().back();
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    // verify that sent number is global local presence number
	    loginSupport(driver1);
 		
 		dashboard.openSmartNumbersTab(driver1);
 		smartNumbersPage.searchSmartNumber(driver1, localPresenceNumber);
 		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, localPresenceNumber);
 		assertTrue(smartNoIndex >= 0);
 		assertEquals(smartNumbersPage.getTypeFromTable(driver1, smartNoIndex), SmartNumbersPage.smartNumberType.LocalPresence.toString());
 	    softPhoneMessagePage.closeTab(driver1);
 	    softPhoneMessagePage.switchToTab(driver1, 1);
 	    
 	    //verify message replied to global local presence number
 	    String InboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //send a text message from caller 2
	    softPhoneActivityPage.sendMessage(driver2, InboundMessage, 0);
	    softPhoneMessagePage.idleWait(2);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage));
	   
	    //disable local presence number as messaging Number
	    System.out.println("disabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity"})
	  public void softphone_messaging_additional_local_presence()
	  {
	    System.out.println("Test case --softphone_messaging_additional_local_presence()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    
	    reloadSoftphone(driver2);
	    
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
	    
	    String areaCode = CONFIG.getProperty("prod_user_1_number").substring(2, 5) ;
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //Verify global local presene for message
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver3);
	    
 	    //getting an lost of additional number with same area code
	    List<String> additionalLocalPresenceNumbers = softPhoneSettingsPage.getLocalPresenceAdditionalNumbers(driver3, areaCode);
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    softPhoneActivityPage.sendMessage(driver3, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, message));
	    
	    //verify that area code is same and message is from additional number of the agent
	    String localPresenceNumber= callScreenPage.getCallerNumber(driver2);
	    assertEquals(areaCode, localPresenceNumber.substring(0,3));
	    assertTrue(additionalLocalPresenceNumbers.contains(localPresenceNumber));
	    
	    //Verifying outbound message
	    callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.openActivityFromList(driver3, "Outbound Message: " + CONFIG.getProperty("prod_user_1_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver3), "Outbound");
	    assertEquals(sfTaskDetailPage.getComments(driver3), message);
	    sfTaskDetailPage.verifyCallHaslocalPresence(driver3);
	    assertTrue(sfTaskDetailPage.getLocalPresenceNumber(driver3).contains(localPresenceNumber));
	    driver3.navigate().back();
	    softPhoneMessagePage.closeTab(driver3);
	    softPhoneMessagePage.switchToTab(driver3, 1);
	    
	    //verify message replied to global local presence number
 	    String InboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //send a text message from caller 2
	    softPhoneActivityPage.sendMessage(driver2, InboundMessage, 0);
	    softPhoneMessagePage.idleWait(3);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, InboundMessage));
	    
	    //disable local presence number as messaging Number
	    System.out.println("disabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver3);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  //@Test(groups={"Regression"})
	  public void softphone_messaging_australia_local_presence()
	  {
	    System.out.println("Test case --softphone_messaging_australia_local_presence()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //Verify global local presene for message
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver1, "+61387379328");
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    
	    //verify that message is sent by agent
	    assertTrue(softPhoneActivityPage.verifyOutboundMessage(driver1, message));
	   
	    //disable local presence number as messaging Number
	    System.out.println("disabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void softphone_messaging_muliple_local_presence()
	  {
	    System.out.println("Test case --softphone_messaging_additional_local_presence()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);

	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //Verify global local presene for message
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver4);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver4);
	    
	    //Select local presence number as messaging Number
	    System.out.println("enabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver3);
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, message));
	    
	    message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneActivityPage.sendMessage(driver4, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, message));
	    
	    message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("prod_user_1_number"));
	    softPhoneActivityPage.sendMessage(driver3, message, 0);
	    softPhoneMessagePage.idleWait(2);
	    softPhoneMessagePage.clickMessageIcon(driver2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver2, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, message));
	    
	    //verify that area code is same and message is from additional number of the agent
	    String localPresenceNumber= callScreenPage.getCallerNumber(driver2);
		  
	    //verify message replied to global local presence number
 	    String InboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 	    
		//Sending an outbound message
	    System.out.println("agent making call to first caller");
	    softPhoneCalling.softphoneAgentSMS(driver2, localPresenceNumber);
	    softPhoneActivityPage.sendMessage(driver2, InboundMessage, 0);
	    softPhoneMessagePage.idleWait(2);
	    
	    //verify that message is received by agent
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, InboundMessage));
	    
	    //verify that message is received by agent
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver4, InboundMessage));
	    
	    //verify that message is received by agent
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver1, InboundMessage));
	    
	    //disable local presence number as messaging Number
	    System.out.println("disabling local presence setting");	    
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    softPhoneSettingsPage.clickSettingIcon(driver3);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver3);
	    softPhoneSettingsPage.clickSettingIcon(driver4);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver4);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_spam_for_unknown_user(){
	    System.out.println("Test case --verify_spam_for_unknown_user()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String blockedNumber = CONFIG.getProperty("qa_user_2_number");
	    isNumberBlocked = false;
	    
	    softPhoneContactsPage.deleteAndAddContact(driver1, blockedNumber, CONFIG.getProperty("qa_user_2_name"));		
	    
	    // Making a unknown receiver contact
  		softPhoneCalling.softphoneAgentCall(driver3, blockedNumber);
  		softPhoneCalling.pickupIncomingCall(driver4);
  		softPhoneCalling.hangupActiveCall(driver4);

  		// Deleting contact
  		if (!callScreenPage.isCallerUnkonwn(driver3)) {
  			callScreenPage.deleteCallerObject(driver3);
  			softPhoneMessagePage.idleWait(3);
  			softPhoneSettingsPage.closeErrorMessage(driver3);
  		}
	    
  		String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		
		//Send first message
	    softPhoneCalling.softphoneAgentCall(driver1, blockedNumber);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.sendMessage(driver4, message1, 0);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //block number for messaging
	    softPhoneActivityPage.reportNumberAsSpam(driver1);
	    isNumberBlocked = true;
	    softPhoneMessagePage.verifyMessageHeadingPresent(driver1);
	    
	    //veify that message has been deleted
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.verifyInboundMessageListNotPresent(driver1);
	    
	    // verify that sent number is present in blocked numbers list
	    loginSupport(driver1);
 		dashboard.clickAccountsLink(driver1);
 		
 		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
 		accountBlockedNumbersTab.isNumberBlocked(driver1, blockedNumber);
 		accountBlockedNumbersTab.editBlockForNumber(driver1, blockedNumber, "All");
 	    softPhoneMessagePage.closeTab(driver1);
 	    softPhoneMessagePage.switchToTab(driver1, 1);
 	    
 	    //inbound message not received
 	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneCalling.pickupIncomingCall(driver3);
	    String unreadMsgCount = softPhoneMessagePage.getUnreadMessageTabCount(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.sendMessage(driver4, "TestMessage", 0);
	    softPhoneMessagePage.idleWait(3);
	    assertFalse(softPhoneActivityPage.verifyInboundMessage(driver3, "TestMessage"));
	    assertEquals(softPhoneMessagePage.getUnreadMessageTabCount(driver3), unreadMsgCount);
	    softPhoneCalling.hangupActiveCall(driver3);
	    
	    deleteBlockedNumber(blockedNumber);
 	    
	    //add caller as contact
	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_spam_for_other_users(){
	    System.out.println("Test case --verify_spam_for_unknown_user()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String blockedNumber = CONFIG.getProperty("qa_user_2_number");
	    
	    // Making a unknown receiver contact
  		softPhoneCalling.softphoneAgentCall(driver1, blockedNumber);
  		softPhoneCalling.pickupIncomingCall(driver4);
  		softPhoneCalling.hangupActiveCall(driver1);

  		// Deleting contact
  		if (!callScreenPage.isCallerUnkonwn(driver1)) {
  			callScreenPage.deleteCallerObject(driver1);
  			softPhoneMessagePage.idleWait(3);
  			softPhoneSettingsPage.closeErrorMessage(driver1);
  		}
	    
  		String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
  		
		//Send first message
	    softPhoneCalling.softphoneAgentCall(driver1, blockedNumber);
	    softPhoneCalling.pickupIncomingCall(driver4);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.sendMessage(driver4, message1, 0);
	    softPhoneCalling.hangupActiveCall(driver1);
	    
	    //block number for messaging
	    softPhoneMessagePage.idleWait(2);
	    softPhoneActivityPage.reportNumberAsSpam(driver1);
	    softPhoneMessagePage.verifyMessageHeadingPresent(driver1);
	    
	    //veify that message has been deleted
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.verifyInboundMessageListNotPresent(driver1);
	    
	    // verify that sent number is present in blocked numbers list
	    loginSupport(driver1);
 		dashboard.clickAccountsLink(driver1);
 		
 		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
 		accountBlockedNumbersTab.isNumberBlocked(driver1, blockedNumber);
 	    softPhoneMessagePage.closeTab(driver1);
 	    softPhoneMessagePage.switchToTab(driver1, 1);
 	    
 	    //inbound message not received
 	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softphoneCallHistoryPage.openCallsHistoryPage(driver3);
	    String unreadMsgCount = softPhoneMessagePage.getUnreadMessageTabCount(driver3);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.sendMessage(driver4, message2, 0);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneMessagePage.idleWait(3);
	    assertEquals(Integer.parseInt(softPhoneMessagePage.getUnreadMessageTabCount(driver3)), Integer.parseInt(unreadMsgCount)+1);
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message2));
	    
 	    //unblock the contact
 	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
 	    callScreenPage.clickUnblockButton(driver1);
 	    callScreenPage.verifyUnblockBtnInvisible(driver1);
	    
	    //add caller as contact
 	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
 	   
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_sms_templates(){
	    System.out.println("Test case --verify_sms_templates()-- started ");
	    
	    // updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		
		String smsTemplateName = "Automation SMS Template";
		String smsTemplate = "This is a test SMS Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableSMSTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkSmsTemplateSaved(driver1, smsTemplateName)){
			accountIntelligentDialerTab.createSMSTemplate(driver1, smsTemplateName, smsTemplate, "");
		}
		
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
		reloadSoftphone(driver1);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver2);
	    softPhoneActivityPage.SelectTemplate(driver1, smsTemplateName);
	    seleniumBase.idleWait(1);
	    assertEquals(softPhoneActivityPage.getMessageText(driver1), smsTemplate);
	    softPhoneActivityPage.clickSendButton(driver1);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, smsTemplate));
		softPhoneCalling.hangupActiveCall(driver1);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity"})
	  public void verify_sms_templates_for_group(){
	    System.out.println("Test case --verify_sms_templates_for_group()-- started ");
	    
	    // updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String contactNumber = CONFIG.getProperty("prod_user_1_number");
		
		String smsTemplateName = "Automation Group specific SMS Template";
		String smsTemplate = "This is a test Group specific SMS Template";
		
		// open Support Page and enable call disposition prompt setting
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.enableSMSTemplateSetting(driver1);
		
		//Create  Template if not creted
		if(!accountIntelligentDialerTab.checkSmsTemplateSaved(driver1, smsTemplateName)){
			accountIntelligentDialerTab.createSMSTemplate(driver1, smsTemplateName, smsTemplate, CONFIG.getProperty("qa_group_3_name"));
		}	
	 	accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		reloadSoftphone(driver3);

		// Calling from Agent's SoftPhone
		softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.openMessageTab(driver2);
	    softPhoneActivityPage.SelectTemplate(driver1, smsTemplateName);
	    assertEquals(softPhoneActivityPage.getMessageText(driver1), smsTemplate);
	    softPhoneActivityPage.clickSendButton(driver1);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver2, smsTemplate));
		softPhoneCalling.hangupActiveCall(driver1);
		
		//verify that template is not visible to other group user
		softPhoneCalling.softphoneAgentCall(driver3, contactNumber);
		softPhoneCalling.pickupIncomingCall(driver2);
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertFalse(softPhoneActivityPage.isTemplateVisible(driver3, smsTemplate));
		softPhoneCalling.hangupActiveCall(driver3);
		
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_messaging_through_quick_icon(){
	    System.out.println("Test case --verify_messaging_through_quick_icon()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
	    
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String leadName = CONFIG.getProperty("qa_user_3_name") + " Automation";
	    
	    //Sending message through quick link from contact search page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, HelperFunctions.getNumberInSimpleFormat(contactNumber));
	    softPhoneContactsPage.clickMessageIconByNumber(driver1, contactNumber);
	    softPhoneActivityPage.sendMessage(driver1, message1, 0);
	    softPhoneMessagePage.idleWait(2);
	    
	    //Verify message has been reeceived
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message1));
	    
	    //Sending message through quick link from fav contact page
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneContactsPage.salesforceSetFavourite(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		softPhoneContactsPage.isContactFavorite(driver1, leadName);
		softPhoneContactsPage.clickFavConMsgIconByNumber(driver1, contactNumber);
	    softPhoneActivityPage.sendMessage(driver1, message2, 0);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message2));
	    softPhoneContactsPage.salesforceRemoveFavourite(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
	    
	    //Sending message through quick link from message tab
	    String message3 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
		callScreenPage.clickMsgIconByNumber(driver1, contactNumber);
	    softPhoneActivityPage.sendMessage(driver1, message3, 0);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message3));
	    
	    //Sending message through quick link from call history tab
	    String message4 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.clickMsgIconByNumber(driver1, contactNumber);
	    softPhoneActivityPage.sendMessage(driver1, message4, 0);
	    softPhoneMessagePage.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message4));
	    
	    //delete and add contact again
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression", "Product Sanity", "ExludeForProd"})
	  public void verify_messaging_through_quick_icon_mobile(){
	    System.out.println("Test case --verify_messaging_through_quick_icon_mobile()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
	    String mobileNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_admin_user_number"));
	    String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
	    String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    String leadName = CONFIG.getProperty("qa_user_3_name") + " Automation";
	    
	    aa_AddCallersAsContactsAndLeads();
	    
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddLead(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    //Make call to mobile number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, mobileNumber);
	    softPhoneMessagePage.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
		// Deleting contact
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
			
		    //Make call to again to other number
		    System.out.println("agent making call to caller");
		    softPhoneCalling.softphoneAgentCall(driver1, mobileNumber);
		    softPhoneCalling.idleWait(5);
		    softPhoneCalling.hangupIfInActiveCall(driver1);
		}
		
	    //add mobile number to existing contact
	    callScreenPage.addMobileCallerToExistingContact(driver1, HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("qa_user_3_number")));
	    softPhoneContactsPage.searchUntilContactPresent(driver1, mobileNumber);
	    
	    //Sending message through quick link from contact search page and verifying quick msg link for default number
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, HelperFunctions.getNumberInSimpleFormat(mobileNumber));
	    softPhoneContactsPage.verifyMsgIconOnContactSearchVisible(driver1, mobileNumber);
	    softPhoneContactsPage.verifyMsgIconOnContactSearchVisible(driver1, contactNumber);
	    softPhoneContactsPage.clickMessageIconByNumber(driver1, mobileNumber);
	    softPhoneActivityPage.sendMessage(driver1, message1, 1);
	    softPhoneMessagePage.idleWait(2);
	    
	    //verifying that message has been received
	    softPhoneCalling.softphoneAgentCall(adminDriver, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneCalling.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(adminDriver);
	    softPhoneActivityPage.openMessageTab(adminDriver);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(adminDriver, message1));
	    
	    //Sending message through quick link from favorite contact page and verifying quick msg link for default number
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneContactsPage.salesforceSetFavourite(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
	    softPhoneContactsPage.isContactFavorite(driver1, leadName);
	    softPhoneContactsPage.verifyMsgIconOnFavConactVisible(driver1, mobileNumber);
	    softPhoneContactsPage.verifyMsgIconOnFavConactVisible(driver1, contactNumber);
		softPhoneContactsPage.clickFavConMsgIconByNumber(driver1, mobileNumber);
	    softPhoneActivityPage.sendMessage(driver1, message2, 1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    seleniumBase.idleWait(2);
	    softPhoneActivityPage.selectAllFromMessageOwnerDropDown(driver3);
	    seleniumBase.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message2));
	    softPhoneContactsPage.salesforceRemoveFavourite(driver1, leadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
	    
	    //Sending message through quick link from message and verifying quick msg link for default number
	    String message3 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    callScreenPage.verifyMsgIconVisible(driver1, mobileNumber);
	    callScreenPage.verifyMsgIconVisible(driver1, contactNumber);
		callScreenPage.clickMsgIconByNumber(driver1, mobileNumber);
	    softPhoneActivityPage.sendMessage(driver1, message3, 1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    seleniumBase.idleWait(2);
	    softPhoneActivityPage.selectAllFromMessageOwnerDropDown(driver3);
	    seleniumBase.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message3));
	    
	    //Sending message through quick link from call history tab and verifying quick msg link for default number
	    String message4 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.verifyMsgIconVisible(driver1, mobileNumber);
	    callScreenPage.verifyMsgIconVisible(driver1, contactNumber);
		callScreenPage.clickMsgIconByNumber(driver1, mobileNumber);
	    softPhoneActivityPage.sendMessage(driver1, message4, 1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    seleniumBase.idleWait(2);
	    softPhoneActivityPage.selectAllFromMessageOwnerDropDown(driver3);
	    seleniumBase.idleWait(2);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message4));
	    
	    //delete and add contact again
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_messaging_through_quick_icon_other_Number(){
	    System.out.println("Test case --verify_messaging_through_quick_icon_other_Number()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String homeNumber = HelperFunctions.getNumberInRDNAFormat("+12017333545");
	    String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
	    String contactName = CONFIG.getProperty("qa_user_3_name") + " Automation";
  		
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    //Make call to Home number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, homeNumber);
	    softPhoneMessagePage.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
  		// Deleting contact
  		if (!callScreenPage.isCallerUnkonwn(driver1)) {
  			callScreenPage.deleteCallerObject(driver1);
  			softPhoneMessagePage.idleWait(3);
  			softPhoneSettingsPage.closeErrorMessage(driver1);
  		}
	    
	    //Make call to Home number
	    System.out.println("agent making call to caller");
	    softPhoneCalling.softphoneAgentCall(driver1, homeNumber);
	    softPhoneMessagePage.idleWait(5);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    
	    //add Home number to existing contact
	    callScreenPage.addHomeCallerToExistingContact(driver1, contactNumber);
	    softPhoneContactsPage.searchUntilContactPresent(driver1, HelperFunctions.getNumberInSimpleFormat(homeNumber));
	    
	    //verifying no quick msg link for any number on contact search page
	    softPhoneContactsPage.clickActiveContactsIcon(driver1);
	    softPhoneContactsPage.searchSalesForce(driver1, contactNumber);
	    assertTrue(softPhoneContactsPage.isSalesforceContactPresent(driver1, contactName));
	    softPhoneContactsPage.verifyMsgIconOnContactSearchInvisible(driver1, homeNumber);
	    softPhoneContactsPage.verifyMsgIconOnContactSearchInvisible(driver1, contactNumber);
	    
	    //verifying no quick msg link for any number on favorite contact page
	    softPhoneContactsPage.salesforceSetFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
		softPhoneContactsPage.isContactFavorite(driver1, contactName);
	    softPhoneContactsPage.verifyMsgIconOnFavConactInvisible(driver1, homeNumber);
	    softPhoneContactsPage.verifyMsgIconOnFavConactInvisible(driver1, contactNumber);
	    softPhoneContactsPage.salesforceRemoveFavourite(driver1, contactName, SoftPhoneContactsPage.searchTypes.Contacts.toString());
	    
	    //verifying no quick msg link for any number on call history page
	    softPhoneCalling.softphoneAgentCall(driver1, contactNumber);
	    softPhoneCalling.pickupIncomingCall(driver3);
	    softPhoneCalling.hangupIfInActiveCall(driver1);
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.verifyMsgIconInvisible(driver1, homeNumber);
	    callScreenPage.verifyMsgIconInvisible(driver1, contactNumber);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	  @Test(groups={"Regression"})
	  public void verify_read_unread_tabs()
	  {
	    System.out.println("Test case --verify_read_unread_tabs()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //open sms page from the caller agent
	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.idleWait(5);
	    softPhoneActivityPage.sendMessage(driver3, message, 0);
	    
	    //verify red dot is appearing for new message on all messages tab
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.verifyNewMsgDotVisible(driver1, message);
	    
	    //verify red dot is appearing for all messages on unread tab
	    softPhoneMessagePage.navigateToUnreadMessages(driver1);
	    softPhoneMessagePage.verifyNewMsgsVisible(driver1);
	    
	    //verify red dot is not appearing for on read tab
	    softPhoneMessagePage.navigateToReadMessages(driver1);
	    softPhoneMessagePage.verifyNewMsgsNotVisible(driver1);
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	  
	//@Test(groups = { "Regression" })
	public void verify_caller_sending_msgs_after_deleting_blocked_numbers() {
		System.out.println("Test case --verify_caller_sending_msgs_after_deleting_blocked_numbers-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// deleting all blocked numbers
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver4);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver4);

		// enabling msg wide-opt settings
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableMsgWideOptSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		softPhoneMessagePage.closeTab(driver4);
		softPhoneMessagePage.switchToTab(driver4, 1);

		String agent_number = CONFIG.getProperty("qa_user_1_number");

		// Sending 'stOP' message to agent from driver3
		String message = "stOP";
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		// updating the drivers used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// verifying block number is there
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		assertTrue(accountBlockedNumbersTab.isNumberBlocked(driver1, agent_number));

		// verifying both directions are there
		assertTrue(accountBlockedNumbersTab.getNumberDirectionList(driver1, agent_number)
				.contains(Direction.Inbound.name()));
		assertTrue(accountBlockedNumbersTab.getNumberDirectionList(driver1, agent_number)
				.contains(Direction.Outbound.name()));

		// removing blocked numbers by deleting them
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
		assertFalse(accountBlockedNumbersTab.isNumberBlocked(driver1, agent_number));

		// sending msg after removing blocked number
		message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		// verifying msg present
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));

		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);

		System.out.println("Test case --verify_caller_sending_msgs_after_deleting_blocked_numbers-- passed");
	}
	  
//	@Test(groups = { "Regression" })
	public void verify_msgs_blocked_on_support_and_softphone_for_agents() {
		System.out.println("Test case --verify_msgs_blocked_on_support_and_softphone_for_agents-- started ");
		// updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("adminDriver");
		driverUsed.put("adminDriver", true);

		// Enabling msg opt settings
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);
		accountIntelligentDialerTab.enableMsgWideOptSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);

		// Sending message 'STOP' from driver4 to driver1
		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneMessagePage.idleWait(3);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.sendMessage(driver4, "STOP", 0);

		// verifying number is blocked now
		dashboard.switchToTab(driver4, 2);
		dashboard.clickAccountsLink(driver4);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver4);
		assertTrue(accountBlockedNumbersTab.isNumberBlocked(driver4, CONFIG.getProperty("qa_user_1_number")));
		softPhoneMessagePage.closeTab(driver4);
		softPhoneMessagePage.switchToTab(driver4, 1);

		// Sending message any msg from driver3 to driver1
		String msg = "AutoSMS".concat(HelperFunctions.GetRandomString(3));
		dashboard.switchToTab(driver3, 1);
		softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneMessagePage.idleWait(5);
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneActivityPage.sendMessage(driver3, msg, 0);
		softPhoneActivityPage.verifyRestrictSmsMsg(driver3);

		// verifying on driver 1 ,no msg is there
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver1, msg));

		// Sending message from driver4 to driver3
		msg = "AutoSMS".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(adminDriver, CONFIG.getProperty("qa_user_1_number"));
		softPhoneMessagePage.idleWait(5);
		softPhoneActivityPage.openMessageTab(adminDriver);
		softPhoneActivityPage.sendMessage(adminDriver, msg, 0);
		softPhoneActivityPage.verifyRestrictSmsMsg(adminDriver);

		// verifying on driver1 no msg is there
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver1, msg));

		// verifying on driver 1 for no msg present
		assertFalse(softPhoneActivityPage.verifyInboundMessage(driver1, msg));

		// Sending message 'Start' from driver1 to driver3
		dashboard.switchToTab(driver4, 1);
		softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_1_number"));
		softPhoneMessagePage.idleWait(5);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.sendMessage(driver4, "Start", 0);

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("adminDriver", false);

		System.out.println("Test case --verify_msgs_blocked_on_support_and_softphone_for_agents-- passed");
	}

	//@Test(groups = { "MediumPriority" })
	public void verify_functionality_of_account_wide_opt_settings() {
		System.out.println("Test case --verify_functionality_of_account_wide_opt_settings-- started");

		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// Opening Intelligent Dialer Tab
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver4);

		// disabling sms settings
		accountIntelligentDialerTab.disableSMSSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);

		// verifying account wide opt setting not visible
		assertFalse(accountIntelligentDialerTab.isAccountWideOptSettingVisible(driver4));

		// enabling sms settings and verifying account opt setting editable
		accountIntelligentDialerTab.enableSMSSetting(driver4);
		accountIntelligentDialerTab.saveAcccountSettings(driver4);
		assertFalse(accountIntelligentDialerTab.isAccountWideOptSettingDisabled(driver4));
		softPhoneMessagePage.closeTab(driver4);
		softPhoneMessagePage.switchToTab(driver4, 1);

		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		assertTrue(accountIntelligentDialerTab.isAccountWideOptSettingDisabled(driver1));
		softPhoneMessagePage.closeTab(driver1);
		softPhoneMessagePage.switchToTab(driver1, 1);

		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
		System.out.println("Test case --verify_functionality_of_account_wide_opt_settings-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_caller_is_clickable_and_redirected_to_sfdc_from_message_history() {
		System.out.println(	"Test case --verify_caller_is_clickable_and_redirected_to_sfdc_from_message_history-- started");
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// clicking contact
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.selectMsgType(driver1, MsgType.Contacts.name());
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);

		//clicking on caller name
		String callerName = callScreenPage.getCallerName(driver1);
		callScreenPage.clickCallerName(driver1);
		callScreenPage.switchToSFTab(driver1, callScreenPage.getTabCount(driver1));
	
		// verifying on salesforce page
		assertTrue(driver1.getCurrentUrl().contains("salesforce.com/"));
		assertEquals(contactDetailPage.getCallerName(driver1), callerName, String.format("Name on sf page: %s do not match with:%s", contactDetailPage.getCallerName(driver1), callerName));
		
		callScreenPage.closeTab(driver1);
		callScreenPage.switchToTab(driver1, callScreenPage.getTabCount(driver1));

		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_caller_is_clickable_and_redirected_to_sfdc_from_message_history-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_quick_sms_link_from_dialpad() {

		System.out.println("Test case --verify_quick_sms_link_from_dialpad-- started");
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// Sending sms and getting number
		softPhoneCalling.switchToSFTab(driver1, 1);
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
		String number = callScreenPage.getCallerNumber(driver1);

		// verifying quick sms link
		callScreenPage.verifyMsgIconVisible(driver1, HelperFunctions.getNumberInRDNAFormat(number));

		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_quick_sms_link_from_dialpad-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_send_message_after_searching() {

		System.out.println("Test case --verify_send_message_after_searching-- started");
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
		String message = "AutoSMS".concat(HelperFunctions.GetRandomString(4));

		// Searching contacts
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_3_name"));
		softPhoneContactsPage.clickMessageIconByNumber(driver1, contactNumber);
		softPhoneActivityPage.sendMessage(driver1, message, 0);
		softPhoneActivityPage.idleWait(2);

		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Verify message has been received
		softPhoneMessagePage.clickMessageIcon(driver3);
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
		softPhoneActivityPage.openMessageTab(driver3);
		assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message));

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_send_message_after_searching-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_message_notification_when_user_on_compose_message_section() {

		System.out.println("Test case --verify_message_notification_when_user_on_compose_message_section-- started");
		
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
		String message = "AutoSMS".concat(HelperFunctions.GetRandomString(4));

		// Searching contacts
		softPhoneContactsPage.searchSalesForce(driver1, CONFIG.getProperty("qa_user_3_name"));
		softPhoneContactsPage.clickMessageIconByNumber(driver1, contactNumber);
		
		//getting unread message count
		String unreadContactsBefore = softPhoneMessagePage.getUnreadMessageTabCount(driver1);
		
		// sending sms from driver3
		softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);
		
		// Verify message has been received but count not increased
		String unreadContactsAfter = softPhoneMessagePage.getUnreadMessageTabCount(driver1);
		assertEquals(unreadContactsBefore, unreadContactsAfter, "message count increased");
		
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.isMsgPresentInAllTab(driver1, message);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_message_notification_when_user_on_compose_message_section-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_task_links_icon_after_open_from_message_history() {

		System.out.println("Test case --verify_task_links_icon_after_open_from_message_history-- started");
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		softPhoneCalling.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);

		// verifying clicking load more btn dispalys 10 more msgs
		int msgCount = softPhoneMessagePage.getTotalMsgsCount(driver1);
		softPhoneMessagePage.clickLoadMoreBtn(driver1);
		softPhoneActivityPage.idleWait(2);
		assertEquals(softPhoneMessagePage.getTotalMsgsCount(driver1), msgCount + 10, "Msg count not increased");

		// open recent message
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);

		// verifying task event and custom link visible
		callToolsPanel.verifyTask_EventsVisible(driver1);
		callToolsPanel.verifyCustomLinkVisible(driver1);

		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_task_links_icon_after_open_from_message_history-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_msg_not_recieved_for_agent_from_blocked_number() {
		System.out.println("Test case --verify_msg_not_recieved_for_agent_from_blocked_number-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		String numberToBlock = CONFIG.getProperty("qa_user_3_number");
		String numberToMsg = CONFIG.getProperty("qa_user_1_number");

		// getting unread count
		softPhoneMessagePage.clickMessageIcon(driver1);
		String previousMessageTabCount = softPhoneMessagePage.getUnreadMessageTabCount(driver1);
		String previousMessageCount = softPhoneMessagePage.getUnreadMessageCount(driver1);

		// deleting all block numbers
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);

		// Adding blocked number driver3
		accountBlockedNumbersTab.addBlockedNumber(driver1, numberToBlock, BlockType.Message, "All", Direction.Inbound.toString());

		// initializing driver
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// Sending message from number blocked
		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.softphoneAgentSMS(driver3, numberToMsg);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);

		// open message from message tab in agent driver and verify msg not present
		softPhoneMessagePage.closeTab(driver1);
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneCalling.hangupIfInActiveCall(driver1);

		softPhoneMessagePage.clickMessageIcon(driver1);
		assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));

		// verify unread count is not increased
		assertEquals(Integer.parseInt(softPhoneMessagePage.getUnreadMessageTabCount(driver1)),
				Integer.parseInt(previousMessageTabCount));
		assertEquals(Integer.parseInt(softPhoneMessagePage.getUnreadMessageCount(driver1)),
				Integer.parseInt(previousMessageCount));

		// deleting all block numbers
		loginSupport(driver3);
		dashboard.clickAccountsLink(driver3);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver3);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver3);
		softPhoneMessagePage.closeTab(driver3);
		softPhoneMessagePage.switchToTab(driver3, 1);

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_msg_not_recieved_for_agent_from_blocked_number-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_no_sms_template_icon_when_setting_off() {
		System.out.println("Test case --verify_no_sms_template_icon_when_setting_off-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);

		// disabling sms template
		accountIntelligentDialerTab.disableSMSTemplateSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);

		// Sending sms and getting number
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneActivityPage.reloadSoftphone(driver1);
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));

		// verifying sms template btn visible
		assertFalse(softPhoneActivityPage.isSMSTemplateBtnVisible(driver1));

		// enabling sms template
		softPhoneActivityPage.switchToTab(driver1, 2);
		accountIntelligentDialerTab.enableSMSTemplateSetting(driver1);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		softPhoneMessagePage.closeTab(driver1);
		softPhoneMessagePage.switchToTab(driver1, 1);

		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_no_sms_template_icon_when_setting_off-- passed");
	}

	//@Test(groups = { "MediumPriority" })
	public void verify_caller_sending_start_stop_msgs_entry_in_blocked_numbers_tab() {
		System.out.println("Test case --verify_caller_sending_start_stop_msgs_entry_in_blocked_numbers_tab-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		// deleting all blocked numbers
		loginSupport(driver4);
		dashboard.clickAccountsLink(driver4);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver4);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver4);

		softPhoneMessagePage.closeTab(driver4);
		softPhoneMessagePage.switchToTab(driver4, 1);

		String agent_number = CONFIG.getProperty("qa_user_1_number");

		// Sending 'stOP' message to agent from driver3
		String message = "stOP";
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		// updating the drivers used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// verifying block number is there
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		assertTrue(accountBlockedNumbersTab.isNumberBlocked(driver1, agent_number));

		// verifying both directions are there
		assertTrue(accountBlockedNumbersTab.getNumberDirectionList(driver1, agent_number).contains(Direction.Inbound.name()));
		assertTrue(accountBlockedNumbersTab.getNumberDirectionList(driver1, agent_number).contains(Direction.Outbound.name()));

		// sending msg after 'Stop' to check msg
		message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		// verifying msg is not present
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertFalse(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));
		
		// Sending 'sTArt' msg to agent from driver3
		message = "sTArt";
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.verifyRestrictSmsMsg(driver4);
		softPhoneActivityPage.idleWait(2);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		// verifying block number is not there
		accountBlockedNumbersTab.switchToTab(driver1, 2);
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		assertFalse(accountBlockedNumbersTab.isNumberBlocked(driver1, agent_number));
		softPhoneMessagePage.closeTab(driver1);
		softPhoneMessagePage.switchToTab(driver1, 1);
		
		// sending msg after 'Start' to check msg is going
		message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver4, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver4);
		softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);

		//verifying msg present
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));

		//sending msg with different user
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver3, agent_number);
		accountBlockedNumbersTab.idleWait(2);
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);
		
		// verifying msg present
		softPhoneMessagePage.switchToTab(driver1, 1);
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));
		
		driverUsed.put("driver4", false);
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		
		System.out.println("Test case --verify_caller_sending_start_stop_msgs_entry_in_blocked_numbers_tab-- passed");
	}

	//@Test(groups = { "MediumPriority" })
	public void verify_default_value_in_msg_drop_down_selector() {
		System.out.println("Test case --verify_default_value_in_msg_drop_down_selector-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
  
		String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("skype_number"));
		softPhoneCalling.idleWait(2);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		assertTrue(callScreenPage.isCallerMultiple(driver1));
		
		//sending message
		softPhoneActivityPage.openMessageTab(driver1);
		softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		softPhoneActivityPage.idleWait(5);
		
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.clickOpenMessageIconByName(driver1, "Multiple");
		callScreenPage.selectFirstContactFromMultiple(driver1);
		callScreenPage.idleWait(2);
		assertEquals(softPhoneActivityPage.getSelectedMessageOwner(driver1), "All", "Default value is not All");
		
		driverUsed.put("driver1", false);
		System.out.println("Test case --verify_default_value_in_msg_drop_down_selector-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void verify_send_btn_disabled_for_number_not_associated() {
		System.out.println("Test case --verify_send_btn_disabled_for_number_not_associated-- started");

		// updating the drivers used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		// SEarching No contact number from contacts tab
		softPhoneContactsPage.openSfContactDetails(driver3, "NoContact");

		// verifying send btn disabled
		String message = "AutoMsg";
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneActivityPage.enterMessageText(driver3, message);
		assertTrue(softPhoneActivityPage.isSendBtnDisabled(driver3));

		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_send_btn_disabled_for_number_not_associated-- passed");
	}
	

	@Test(groups = { "MediumPriority" })
	public void add_caller_as_lead_after_open_msg_history() {
		System.out.println("Test case --add_caller_as_lead_after_open_msg_history- started");

		// updating the drivers used
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String unknownCaller = "918290433518";
		
		softPhoneCalling.softphoneAgentCall(driver3, unknownCaller);
		softPhoneMessagePage.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver3);

		// Deleting contact
		if (!callScreenPage.isCallerUnkonwn(driver3)) {
			callScreenPage.deleteCallerObject(driver3);
			softPhoneMessagePage.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver3);
		}
		
		softPhoneCalling.softphoneAgentCall(driver3, unknownCaller);
		softPhoneMessagePage.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver3);

		String message = "WakeUp_Pranshu_".concat(HelperFunctions.GetRandomString(3));
		softPhoneActivityPage.openMessageTab(driver3);
		softPhoneMessagePage.idleWait(2);
	    softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);
		softPhoneMessagePage.idleWait(2);
		
		// verifying send btn disabled
		softPhoneMessagePage.clickMessageIcon(driver3);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, message));
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);

		//adding as lead
		String newLeadFirstName = "AutoFirstName".concat(HelperFunctions.GetRandomString(2));
		callScreenPage.addCallerAsLead(driver3, newLeadFirstName, CONFIG.getProperty("contact_account_name"));
		
		//verify that lead has been added
		assertEquals(callScreenPage.getCallerName(driver3), newLeadFirstName + " Automation");

		driverUsed.put("driver3", false);
		System.out.println("Test case --add_caller_as_lead_after_open_msg_history-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_text_spam_when_recieve_msg_from_unknown_caller() {
		System.out.println("Test case --verify_text_when_recieve_msg_from_unknown_caller- started");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		
		// Making an outbound call from softphone and delete contact if exist
		System.out.println("agent making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		System.out.println("first caller picking up the call");
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
		}
		if (!callScreenPage.isCallerUnkonwn(driver3)) {
			callScreenPage.deleteCallerObject(driver3);
			softPhoneCalling.idleWait(2);
		}

		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(driver3);
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("qa_user_1_number"));
		softPhoneCalling.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);
		softPhoneActivityPage.isSpamLinkPresent(driver3);
		softPhoneActivityPage.verifySpamNotAssociatedText(driver3);
		
		// verifying send btn disabled
		softPhoneMessagePage.clickMessageIcon(driver1);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver1, message));
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
		softPhoneActivityPage.isSpamLinkPresent(driver1);
		softPhoneActivityPage.verifySpamNotAssociatedText(driver1);
		
		callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_3_name"), CONFIG.getProperty("contact_account_name"));
		callScreenPage.addCallerAsContact(driver3, CONFIG.getProperty("qa_user_1_name"), CONFIG.getProperty("contact_account_name"));

		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_text_when_recieve_msg_from_unknown_caller-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void local_presence_number_for_no_additional_no_global_local_presence_neighbour_exists() {
	    System.out.println("Test case --local_presence_number_for_no_additional_no_global_local_presence_neighbour_exists-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String neighbourSmartNumberLP = "+15755025652";
	    String areaCode = "575";

		// navigating to support page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountLocalPresenceTab.openlocalPresenceTab(driver1);
		accountLocalPresenceTab.disableUserVerifiedNumberSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
		
		dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, neighbourSmartNumberLP);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, neighbourSmartNumberLP);
		if (smartNoIndex < 0) {
			// UnDeleting smart Number
			smartNumbersPage.clickSmartNumber(driver1, neighbourSmartNumberLP);
			smartNumbersPage.clickUndeleteBtn(driver1);
		}
		smartNumbersPage.closeTab(driver1);
		smartNumbersPage.switchToTab(driver1, 1);
	    
	    //Select local presence number as outbound Number
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound call from softphone
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		softPhoneActivityPage.idleWait(5);
	    
		//verifying msg recieved
		softPhoneMessagePage.clickMessageIcon(driver5);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver5, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver5, message));
	    
	    //verify that area code is same
		String localPresenceNumber = callScreenPage.getCallerNumber(driver5);
		assertEquals(areaCode, localPresenceNumber.substring(0, 3));

		 //Unchecking LP SMS check box
	    System.out.println("enabling local presence setting");
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
	    
	    //Making an outbound call from softphone
	    message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		
		//verify that msg recieved from callers smart number
		softPhoneMessagePage.clickMessageIcon(driver5);
		softPhoneCalling.idleWait(2);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver5, 1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver5, message));
		localPresenceNumber= callScreenPage.getCallerNumber(driver5);
	    assertEquals(HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_1_number")), HelperFunctions.getNumberInRDNAFormat(localPresenceNumber));
				
		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver5", false);
		System.out.println("Test case --local_presence_number_for_no_additional_no_global_local_presence_neighbour_exists-- passed");
	}

	@Test(groups = { "MediumPriority" })
	public void local_presence_number_for_no_additional_no_global_local_presence_no_neighbour_exists() {
	    System.out.println("Test case --local_presence_number_for_no_additional_no_global_local_presence_no_neighbour_exists-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String neighbourSmartNumberLP = "+15755025652";
	    
		// navigating to support page
	    loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountLocalPresenceTab.openlocalPresenceTab(driver1);
		accountLocalPresenceTab.disableUserVerifiedNumberSetting(driver1);
		accountLocalPresenceTab.saveLocalPresenceSettings(driver1);
		
		dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, neighbourSmartNumberLP);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, neighbourSmartNumberLP);
		if (smartNoIndex >= 0) {
			// Deleting smart Number
			smartNumbersPage.clickSmartNumber(driver1, neighbourSmartNumberLP);
			smartNumbersPage.deleteSmartNumber(driver1);
		}
		
	    //Enabling local presence sms setting
	    System.out.println("enabling local presence setting");
	    smartNumbersPage.switchToTab(driver1, 1);
	    softPhoneSettingsPage.clickSettingIcon(driver1);
	    softPhoneSettingsPage.enableLocalPresenceSetting(driver1);
	    
		//Making an outbound msg from softphone
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		softPhoneActivityPage.idleWait(5);
	    
		//verifying msg recieved
		softPhoneMessagePage.clickMessageIcon(driver5);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver5, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver5, message));
	    
		//verify that msg recieved from callers smart number
	    String localPresenceNumber= callScreenPage.getCallerNumber(driver5);
	    assertEquals(HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_1_number")), HelperFunctions.getNumberInRDNAFormat(localPresenceNumber));
	    
	    dashboard.switchToTab(driver1, 2);
	    dashboard.openSmartNumbersTab(driver1);
		smartNumbersPage.searchSmartNumber(driver1, neighbourSmartNumberLP);
		smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, neighbourSmartNumberLP);
		if (smartNoIndex < 0) {
			// UnDeleting smart Number
			smartNumbersPage.clickSmartNumber(driver1, neighbourSmartNumberLP);
			smartNumbersPage.clickUndeleteBtn(driver1);
		}
		
		smartNumbersPage.closeTab(driver1);
		dashboard.switchToTab(driver1, 1);
		
		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver5", false);
		System.out.println("Test case --local_presence_number_for_no_additional_no_global_local_presence_no_neighbour_exists-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_lp_off_additional_number_selected_from_outbound() {
	    System.out.println("Test case --verify_lp_off_additional_number_selected_from_outbound-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
		// Disabling local presence sms setting
		System.out.println("disabling local presence setting");
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableLocalPresenceSetting(driver1);

		//selecting additional number
		String selectedAdditionalNumber = softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 1);
		
		//Making an outbound msg from softphone
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("prod_user_2_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		softPhoneCalling.idleWait(2);
	    
		//verifying msg recieved
		softPhoneMessagePage.clickMessageIcon(driver5);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver5, 1);
	    
	    //verify that message is received by agent
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver5, message));
	    
		//verify that msg recieved from additional number
	    String localPresenceNumber= callScreenPage.getCallerNumber(driver5);
	    assertEquals(selectedAdditionalNumber, localPresenceNumber);
	    
	    //selecting default smart number
	    softPhoneSettingsPage.selectAdditionalNumberUsingIndex(driver1, 0);
	    
		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver5", false);
		System.out.println("Test case --verify_lp_off_additional_number_selected_from_outbound-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_sms_link_present_on_last_number_sent() {
	    System.out.println("Test case --verify_sms_link_present_on_last_number_sent-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    softPhoneContactsPage.clickActiveContactsIcon(driver3);
	    softPhoneContactsPage.searchSalesForce(driver3, "Automation Multiple2");
	    softPhoneContactsPage.clickFirstContactsMsgIcon(driver3);
	    
	    //selecting first number in number selector
	    String message = "AutoSMSMSg".concat(HelperFunctions.GetRandomString(3));
	    String selectedNumber = softPhoneActivityPage.selectMessagingNumber(driver3, 0);
	    softPhoneActivityPage.sendMessage(driver3, message, 0);
	    
	    //verify msg icon in selected number
	    softPhoneContactsPage.verifyMsgIconOnContactSearchVisible(driver3, selectedNumber);

		//verifying paceholderText contains number
		assertTrue(softPhoneActivityPage.getMessagePlaceholderText(driver3).contains(selectedNumber));
	    
	    //selecting second number in number selector
	    message = "AutoSMSMSg".concat(HelperFunctions.GetRandomString(3));
	    selectedNumber = softPhoneActivityPage.selectMessagingNumber(driver3, 1);
	    softPhoneActivityPage.sendMessage(driver3, message, 1);
	    
		// verify msg icon in selected number
		softPhoneContactsPage.verifyMsgIconOnContactSearchVisible(driver3, selectedNumber);
		
		// verifying paceholderText contains number
		assertTrue(softPhoneActivityPage.getMessagePlaceholderText(driver3).contains(selectedNumber));

		// updating the driver used
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_sms_link_present_on_last_number_sent-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_during_active_call_when_send_msg_no_blank_screen() {
	    System.out.println("Test case --verify_during_active_call_when_send_msg_no_blank_screen-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //making a call to agent
	    softPhoneCalling.softphoneAgentCall(driver4, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneCalling.idleWait(2);
	    softPhoneCalling.pickupIncomingCall(driver3);
	    
	    //sending a sms during active call to agent
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);
	    
		//verifying on agent sms present and no blank screen
		softPhoneMessagePage.clickMessageIcon(driver3);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, message));
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
		assertTrue(softPhoneActivityPage.getInboundMessageList(driver3).size() > 0);

		//verifying on agent sms present and no blank screen after hanging up
		softPhoneCalling.hangupIfInActiveCall(driver4);
		softPhoneSettingsPage.clickSettingIcon(driver3);
		softPhoneMessagePage.clickMessageIcon(driver3);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, message));
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
		assertTrue(softPhoneActivityPage.getInboundMessageList(driver3).size() > 0);
	    
	    //updating the driver used
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    System.out.println("Test case --verify_during_active_call_when_send_msg_no_blank_screen-- started ");
	}
	
	@Test(groups = { "MediumPriority" })
	public void send_msg_to_multiple_related_caller_and_relate_send_msg() {
		System.out.println("Test case --send_msg_to_multiple_related_caller_and_relate_send_msg-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		//verifying caller is multiple
		String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("skype_number"));
		assertTrue(callScreenPage.isCallerMultiple(driver1));
		
		//sending message
		softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);
		callScreenPage.idleWait(2);
		
		//verifying send message related with selected caller
		callScreenPage.selectFirstContactFromMultiple(driver1);
		String contactName = callScreenPage.getCallerName(driver1);
		callScreenPage.idleWait(2);
		softPhoneActivityPage.navigateToAllActivityTab(driver1);
		softPhoneActivityPage.openMessageTab(driver1);
		callScreenPage.idleWait(3);
		assertTrue(softPhoneActivityPage.verifyOutboundMessage(driver1, message));
		assertTrue(softPhoneActivityPage.getOuboundMessageList(driver1).size() >=0);
		
		//navigating to message icon and verifying caller entry
		softPhoneMessagePage.clickMessageIcon(driver1);
		softPhoneMessagePage.clickOpenMessageIconByName(driver1, contactName);
		assertTrue(softPhoneActivityPage.verifyOutboundMessage(driver1, message));
		
		// updating the driver used
		driverUsed.put("driver1", false);
		System.out.println("Test case --send_msg_to_multiple_related_caller_and_relate_send_msg-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_sms_counter_is_updated_in_ref_to_character_count() {
	    System.out.println("Test case --verify_sms_counter_is_updated_in_ref_to_character_count-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //composing a message with 160 characters and verifying sms counter and characters
	    String message = new String(new char[160]).replace("\0", HelperFunctions.GetRandomString(1));;
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, message);
	    assertEquals(softPhoneActivityPage.getCharacterCount(driver4), 160);
	    assertEquals(softPhoneActivityPage.getSMSCount(driver4), 1);
	    
	    //composing a message with 165 characters and verifying sms counter and characters
	    message = new String(new char[165]).replace("\0", HelperFunctions.GetRandomString(1));;
	    softPhoneActivityPage.clearAllMsgTextBox(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, message);
	    assertEquals(softPhoneActivityPage.getCharacterCount(driver4), 165);
	    assertEquals(softPhoneActivityPage.getSMSCount(driver4), 2);
	    
	    //composing a message with 307 characters and verifying sms counter and characters
	    message = new String(new char[307]).replace("\0", HelperFunctions.GetRandomString(1));;
	    softPhoneActivityPage.clearAllMsgTextBox(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, message);
	    assertEquals(softPhoneActivityPage.getCharacterCount(driver4), 307);
	    assertEquals(softPhoneActivityPage.getSMSCount(driver4), 3);
		softPhoneActivityPage.clickSendButton(driver4);
	    
		//verifying on agent sms present
		softPhoneMessagePage.clickMessageIcon(driver3);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, message));

	    //updating the driver used
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    System.out.println("Test case --verify_sms_counter_is_updated_in_ref_to_character_count-- passed ");
	}
	
//	@Test(groups = { "MediumPriority" })
	public void caller_converted_unkown_after_deleting() {
		System.out.println("Test case --caller_converted_unkown_after_deleting-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);

		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

		// Making an outbound call from softphone and delete contact if exist
		System.out.println("agent making call to first caller");
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("qa_user_3_number"));
		System.out.println("first caller picking up the call");
		softPhoneCalling.pickupIncomingCall(driver3);
		softPhoneCalling.hangupActiveCall(driver1);
		softPhoneCalling.isCallBackButtonVisible(driver3);

		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
		}

		reloadSoftphone(driver1);

		// open sms page from the dial pad
		System.out.println("open sms page from the caller agent");
		softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneMessagePage.idleWait(5);
		//send message and add as contact
	    softPhoneActivityPage.sendMessage(driver1, message, 0);
	    
	    //
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    assertTrue(softPhoneMessagePage.getCallerName(driver1).contains("Unknown"));
	    aa_AddCallersAsContactsAndLeads();
	}

	@Test(groups = { "MediumPriority" })
	public void enter_number_and_hit_sms_button() {
	    System.out.println("Test case --enter_number_and_hit_sms_button-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //composing a message
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(2));
	    
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneActivityPage.idleWait(2);
	    assertTrue(softPhoneActivityPage.isMsgTabActive(driver4));
	    assertTrue(Strings.isNotNullAndNotEmpty(callScreenPage.getCallerName(driver4)));
	    assertTrue(Strings.isNotNullAndNotEmpty(callScreenPage.getCallerNumber(driver4)));
	    assertTrue(Strings.isNotNullAndNotEmpty(callScreenPage.getCallerCompany(driver4)));
	    
	    softPhoneActivityPage.enterMessageText(driver4, message);
	    softPhoneActivityPage.clickSendButton(driver4);
	    
		// verifying on agent sms present
		softPhoneMessagePage.clickMessageIcon(driver3);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver3, message));

		driverUsed.put("driver3", true);
		driverUsed.put("driver4", true);
		System.out.println("Test case --enter_number_and_hit_sms_button-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_msgs_not_sent_after_report_spam() {
		System.out.println("Test case --verify_msgs_not_sent_after_report_spam-- started ");

		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);

		String blockedNumber = CONFIG.getProperty("qa_user_2_number");

		// Making a unknown receiver contact
		softPhoneCalling.softphoneAgentCall(driver1, blockedNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneCalling.hangupActiveCall(driver1);

		// Deleting contact
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneMessagePage.idleWait(3);
			softPhoneSettingsPage.closeErrorMessage(driver1);
		}

		// block number for messaging
		softPhoneCalling.softphoneAgentCall(driver1, blockedNumber);
		softPhoneCalling.pickupIncomingCall(driver4);
		softPhoneActivityPage.openMessageTab(driver1);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		softPhoneMessagePage.idleWait(2);
		
		if(softPhoneActivityPage.isMsgHasMultipleOwner(driver1))
			softPhoneActivityPage.selectAllFromMessageOwnerDropDown(driver1);
		
		softPhoneActivityPage.reportNumberAsSpam(driver1);
		softPhoneMessagePage.verifyMessageHeadingPresent(driver1);

		String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(2));

		// checking btn not visible
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneActivityPage.openMessageTab(driver1);
		softPhoneActivityPage.enterMessageText(driver1, message);
		assertFalse(softPhoneActivityPage.isSendBtnVisible(driver1));

		// add caller as contact
		softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));

		driverUsed.put("driver1", true);
		driverUsed.put("driver4", true);
		System.out.println("Test case --verify_msgs_not_sent_after_report_spam-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_tooltip_warning_when_select_new_number() {

		System.out.println("Test case --verify_tooltip_warning_when_select_new_number-- started");
		
		//updating the driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    softPhoneContactsPage.clickActiveContactsIcon(driver3);
	    softPhoneContactsPage.searchSalesForce(driver3, "Automation Multiple2");
	    softPhoneContactsPage.clickFirstContactsMsgIcon(driver3);
	    
	    //selecting first number in number selector
	    String message = "AutoSMSMSg".concat(HelperFunctions.GetRandomString(3));
	    softPhoneCalling.selectOutboundNumFromDropdown(driver3, "Local presence");
	    softPhoneActivityPage.idleWait(2);
	    softPhoneActivityPage.enterMessageText(driver3, message);
	    softPhoneActivityPage.idleWait(2);

	    String tooltipText = softPhoneActivityPage.getToolTipWarningOnSendbtn(driver3);
	    assertTrue(tooltipText.contains("Your outbound number is set to Local Presence. Your previous messages we're sent using"));
	    softPhoneActivityPage.clickToolTipDismiss(driver3);
	    
	    softPhoneActivityPage.sendMessage(driver3, message, 1);
	    
	    //selecting back default number
	    softPhoneCalling.selectOutboundNumFromDropdown(driver3, HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number")));
	    
		// updating the driver used
		driverUsed.put("driver3", false);
		System.out.println("Test case --verify_tooltip_warning_when_select_new_number-- passed");
	}
	
	@Test(groups = {"MediumPriority"})
	public void verify_different_org_user_not_come_in_message_drop_down_selector() {

		System.out.println("Test case --verify_different_org_user_not_come_in_message_drop_down_selector-- started");
		
		//updating driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //login with driver4 of load test org and send to agent of load test org
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
	    softPhoneCalling.softphoneAgentSMS(driver4, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver4);
	    softPhoneActivityPage.enterMessageText(driver4, message);
		softPhoneActivityPage.clickSendButton(driver4);	
	    
		// updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	    
	    //login with driver1 of load test org and send to agent of load test org
	    message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(driver1);
	    softPhoneActivityPage.enterMessageText(driver1, message);
		softPhoneActivityPage.clickSendButton(driver1);	
	    
		//login with qa v2 user and send to agent of load test org
		initializeDriverSoftphone("qaV2Driver");
		driverUsed.put("qaV2Driver", true);
		String qaV2UserName = CONFIG.getProperty("qa_v2_user_name");
		softPhoneCalling.softphoneAgentSMS(qaV2Driver, CONFIG.getProperty("qa_user_3_number"));
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.openMessageTab(qaV2Driver);
	    callScreenPage.closeErrorBar(qaV2Driver);
	    softPhoneActivityPage.enterMessageText(qaV2Driver, message);
		softPhoneActivityPage.clickSendButton(qaV2Driver);
		
		//now check with driver4 of load test org that in dropdown qa v2 user is not present 
		softPhoneMessagePage.clickMessageIcon(driver4);
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver4, 1);
		
		List<String> messageOwnerNames = softPhoneActivityPage.getMessageOwnerNamesList(driver4);
		assertTrue(messageOwnerNames.contains(CONFIG.getProperty("qa_user_1_name")));
		assertTrue(messageOwnerNames.contains(CONFIG.getProperty("qa_user_2_name")));
		assertFalse(messageOwnerNames.contains(qaV2UserName));
		    
		// updating the driver used
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
		driverUsed.put("qaV2Driver", false);
		
		System.out.println("Test case --verify_different_org_user_not_come_in_message_drop_down_selector-- passed");
	}

	@Test(groups = {"MediumPriority"})
	public void verify_messaging_for_canada_numbers() {

		System.out.println("Test case --verify_messaging_for_canada_numbers-- started");
		
		//updating driver used
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    //canada number on driver4
	    String canadaNumber = CONFIG.getProperty("qa_user_2_canada_number");

		//updating driver used
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);

	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
	    softPhoneCalling.softphoneAgentSMS(driver3, canadaNumber);
		softPhoneCalling.idleWait(2);
	    softPhoneActivityPage.enterMessageText(driver3, message);
		softPhoneActivityPage.clickSendButton(driver3);	
		softPhoneActivityPage.idleWait(2);
		
		// verify that message is sent by agent
		assertTrue(softPhoneActivityPage.verifyOutboundMessage(driver3, message));
		
		// verify that message is recieved
		softPhoneMessagePage.clickMessageIcon(driver4);
		assertTrue(softPhoneMessagePage.isMsgPresentInAllTab(driver4, message));
		
		driverUsed.put("driver4", true);
		driverUsed.put("driver3", true);
		System.out.println("Test case --verify_messaging_for_canada_numbers-- started");
	}
	
	@Test(groups = { "MediumPriority" })
	public void verify_time_format_in_message_history() {

		System.out.println("Test case --verify_time_format_in_message_history-- started");

		// updating driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String InboundMessage = "IndboundMessage" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_2_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(driver1, InboundMessage, 0);

		// verify that message is received
		softPhoneMessagePage.clickMessageIcon(driver4);
		boolean isFormatCorrect = softPhoneMessagePage.verifyMsgDateInGivenFormat("MM/dd/yyyy, hh:mm a", softPhoneMessagePage.getMsgDateTimeList(driver4).get(0));
		assertTrue(isFormatCorrect, "Format is not correct");

		// navigating to msg and verifying
		softPhoneMessagePage.clickOpenMessageIconByIndex(driver4, 1);
		isFormatCorrect = softPhoneActivityPage.verifyInboundMsgDateInGivenFormat("MM/dd/yyyy, hh:mm a", softPhoneActivityPage.getInboundMsgDateTimeList(driver4).get(0));
		assertTrue(isFormatCorrect, "Format is not correct");

		driverUsed.put("driver4", true);
		System.out.println("Test case --verify_time_format_in_message_history-- started");
	}
	
	@Test(groups={"Regression"})
	  public void verify_dropdown_for_logged_in_agents(){
	    System.out.println("Test case --verify_dropdown_for_logged_in_agents()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver4");
	    driverUsed.put("driver4", true);
	    
	    String msgReceiverNumber = CONFIG.getProperty("prod_user_1_number");	    
		
		//Send an message from user 1
	    String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneCalling.softphoneAgentSMS(driver1, msgReceiverNumber);
	    softPhoneActivityPage.sendMessage(driver1, message1, 0);  		
	    	    
	    //Send an message from user 2
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneCalling.softphoneAgentSMS(driver4, msgReceiverNumber);
	    softPhoneActivityPage.sendMessage(driver4, message2, 0);
	    	    
	    //Send an message from user 3
	    String message3 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneCalling.softphoneAgentSMS(driver3, msgReceiverNumber);
	    softPhoneActivityPage.sendMessage(driver3, message3, 0);
	    
	    //Open the recent message for User 1 and verify that selected owner and message owner is the user 1
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    String messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver1);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_1_name"));
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, CONFIG.getProperty("qa_user_1_name"));
	    
	    //Verify that only the User 1's message are there in the message list
	    List<String> messageList = softPhoneActivityPage.getOuboundMessageList(driver1);
	    assertTrue(messageList.contains(message1));
	    assertFalse(messageList.contains(message2));
	    assertFalse(messageList.contains(message3));
	    
	    //Open the recent message for User 2 and verify that selected owner and message owner is the user 2
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver4, 1);
	    messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver4);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_2_name"));
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver4, CONFIG.getProperty("qa_user_2_name"));
	    
	    //Verify that only the User 2's message are there in the message list
	    softPhoneMessagePage.clickMessageIcon(driver4);
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver4);
	    assertFalse(messageList.contains(message1));
	    assertTrue(messageList.contains(message2));
	    assertFalse(messageList.contains(message3));
	    
	    //Open the recent message for User 3 and verify that selected owner and message owner is the user 3
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver3);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_3_name"));
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver3, CONFIG.getProperty("qa_user_3_name"));
	    
	    //Verify that only the User 3's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver3);
	    assertFalse(messageList.contains(message1));
	    assertFalse(messageList.contains(message2));
	    assertTrue(messageList.contains(message3));
	    
	    //Call the caller and verify that selected owner is the user 1
	    softPhoneCalling.softphoneAgentCall(driver1, msgReceiverNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    
	    messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver1);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_1_name"));
	    
	    //Verify that only the User 1's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver1);
	    assertTrue(messageList.contains(message1));
	    assertFalse(messageList.contains(message2));
	    assertFalse(messageList.contains(message3));
	    
	    //Call the caller and verify that selected owner is the user 2
	    softPhoneCalling.softphoneAgentCall(driver4, msgReceiverNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver4);
	    softPhoneActivityPage.openMessageTab(driver4);
	    
	    messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver4);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_2_name"));
	    
	    //Verify that only the User 2's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver4);
	    assertFalse(messageList.contains(message1));
	    assertTrue(messageList.contains(message2));
	    assertFalse(messageList.contains(message3));
	    
	    //Call the caller and verify that selected owner is the user 3
	    softPhoneCalling.softphoneAgentCall(driver3, msgReceiverNumber);
	    seleniumBase.idleWait(5);
	    softPhoneCalling.hangupActiveCall(driver3);
	    softPhoneActivityPage.openMessageTab(driver3);
	    
	    messageOwner = softPhoneActivityPage.getSelectedMessageOwner(driver3);
	    assertEquals(messageOwner, CONFIG.getProperty("qa_user_3_name"));
	    
	    //Verify that only the User 3's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver3);
	    assertFalse(messageList.contains(message1));
	    assertFalse(messageList.contains(message2));
	    assertTrue(messageList.contains(message3));
	    
	    //Select user  from the drop down
	    String owner = CONFIG.getProperty("qa_user_2_name");
	    softPhoneActivityPage.selectMessageOwnerFromDropDown(driver1, owner);
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, owner);
	    
	    //Verify that only the User 2's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver1);
	    assertFalse(messageList.contains(message1));
	    assertTrue(messageList.contains(message2));
	    assertFalse(messageList.contains(message3));
	    
	    //Select user 3 from the drop down
	    owner = CONFIG.getProperty("qa_user_3_name");
	    softPhoneActivityPage.selectMessageOwnerFromDropDown(driver1, owner);
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, owner);
	    
	    //Verify that only the User 3's message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver1);
	    assertFalse(messageList.contains(message1));
	    assertFalse(messageList.contains(message2));
	    assertTrue(messageList.contains(message3));
	    
	    //Select All from the drop down
	    owner = CONFIG.getProperty("qa_user_3_name");
	    softPhoneActivityPage.selectAllFromMessageOwnerDropDown(driver1);
	    
	    //Verify All users message are there in the message list
	    messageList = softPhoneActivityPage.getOuboundMessageList(driver1);
	    assertTrue(messageList.contains(message1));
	    assertTrue(messageList.contains(message2));
	    assertTrue(messageList.contains(message3));
		
	    //Make user single match caller
	    softPhoneContactsPage.deleteAndAddContact(driver3, CONFIG.getProperty("prod_user_1_number"), CONFIG.getProperty("prod_user_1_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	
	 @Test(groups={"MediumPriority"})
	  public void send_messages_verify_task_and_msg_inspector()
	  {
	    System.out.println("Test case --send_messages_verify_task_and_msg_inspector()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("qa_user_3_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
	    String outboundMsgDate	= HelperFunctions.GetCurrentDateTime(msgDateFormate);
	    String msgDateTimeActivityPage = softPhoneActivityPage.getOutboundMsgDateTime(driver1, outboundMessage);
	    assertTrue(msgDateTimeActivityPage.contains(outboundMsgDate));
	    
	    //open recent message from receviver and verify then message has ben recieved
	    softPhoneMessagePage.clickMessageIcon(driver3);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver3, 1);
	    softPhoneActivityPage.openMessageTab(driver3);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, outboundMessage));
	    
	    //Verifying outbound message on sfdc
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Outbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Outbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getFromNumber(driver1)));
	    assertEquals(sfTaskDetailPage.getComments(driver1), outboundMessage);
	    String msgSid = sfTaskDetailPage.getCallObjectId(driver1);
	    
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    //Login into web app and open the message sid
	    loginSupport(driver1);
	    dashboard.navigateToMessagesSection(driver1);
	    messagesPage.enterText_SearchInSection(driver1, MessagesPage.SID, msgSid);
	    messagesPage.clickSidUsingIndex(driver1, 0);
	    
	    //verify to and from numbers
	    assertEquals(messagesPage.getMsgInspctSID(driver1), msgSid);
	    assertEquals(messagesPage.getMsgInspctToNumber(driver1), CONFIG.getProperty("qa_user_3_number"));
	    assertEquals(messagesPage.getMsgInspctFromNumber(driver1), CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    //Verify for inbound
	    String InboundMessage = "IndboundMessage" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver3, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(driver3, InboundMessage, 0);
	    String inboundMsgDate	= HelperFunctions.GetCurrentDateTime(msgDateFormate);
	    msgDateTimeActivityPage = softPhoneActivityPage.getOutboundMsgDateTime(driver3, InboundMessage);
	    assertTrue(msgDateTimeActivityPage.contains(inboundMsgDate));
	    
	    //open recent message from receviver and verify then message has ben recieved
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, InboundMessage));
	    
	    //Verifying outbound message on sfdc
	    callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.openActivityFromList(driver1, "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertEquals(sfTaskDetailPage.getCallDirection(driver1), "Inbound");
	    assertEquals(sfTaskDetailPage.getSubject(driver1), "Inbound Message: " + CONFIG.getProperty("qa_user_3_number").trim());
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getToNumber(driver1)));
	    assertTrue(CONFIG.getProperty("qa_user_3_number").contains(sfTaskDetailPage.getFromNumber(driver1)));
	    assertEquals(sfTaskDetailPage.getComments(driver1), InboundMessage);
	    msgSid = sfTaskDetailPage.getCallObjectId(driver1);
	    
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    //Login into web app and open the message sid
	    loginSupport(driver1);
	    dashboard.navigateToMessagesSection(driver1);
	    messagesPage.enterText_SearchInSection(driver1, MessagesPage.SID, msgSid);
	    messagesPage.clickSidUsingIndex(driver1, 0);
	    
	    //verify to and from numbers
	    assertEquals(messagesPage.getMsgInspctSID(driver1), msgSid);
	    assertEquals(messagesPage.getMsgInspctToNumber(driver1), CONFIG.getProperty("qa_user_1_number"));
	    assertEquals(messagesPage.getMsgInspctFromNumber(driver1), CONFIG.getProperty("qa_user_3_number"));
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);	
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"MediumPriority"})
	  public void verify_task_and_msg_inspector_custom_object_org()
	  {
	    System.out.println("Test case --verify_task_and_msg_inspector_custom_object_org()-- started ");
	    
	    //updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);

	    //login custom object user
	    WebDriver customObjDriver = getDriver();
		SFLP.softphoneLogin(customObjDriver, CONFIG.getProperty("qa_test_site_name"), CONFIG.getProperty("custom_org_user_username"), CONFIG.getProperty("custom_org_user_password"));
		
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(customObjDriver);
	    callScreenPage.openCallerDetailPage(customObjDriver);
		callScreenPage.closeLightningDialogueBox(customObjDriver);
		seleniumBase.closeTab(customObjDriver);
		seleniumBase.switchToTab(customObjDriver, 1);
	    softPhoneContactsPage.deleteAndAddContact(customObjDriver, CONFIG.getProperty("qa_user_1_number"), CONFIG.getProperty("qa_user_1_name"));
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(customObjDriver, CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(customObjDriver, outboundMessage, 0);
	    String outboundMsgDate	= HelperFunctions.GetCurrentDateTime(msgDateFormate);
	    String msgDueDate	= HelperFunctions.GetCurrentDateTime("M/d/yyyy");
	    String msgDateTimeActivityPage = softPhoneActivityPage.getOutboundMsgDateTime(customObjDriver, outboundMessage);
	    assertTrue(msgDateTimeActivityPage.contains(outboundMsgDate));
	    
	    //open recent message from receiver and verify then message has been received
	    softPhoneMessagePage.clickMessageIcon(driver1);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(driver1, 1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver1, outboundMessage));
	    
	    //Verifying outbound message on sfdc
	    callScreenPage.openCallerDetailPage(customObjDriver);
	    contactDetailPage.openLatestCallActivities(customObjDriver);
	    assertEquals(sfTaskDetailPage.getAssignedToUser(customObjDriver), CONFIG.getProperty("custom_org_user_name"));
	    assertEquals(sfTaskDetailPage.getCallDirection(customObjDriver), "Outbound");
	    assertEquals(sfTaskDetailPage.getSubject(customObjDriver), "Outbound Message: " + CONFIG.getProperty("qa_user_1_number").trim());
	    assertTrue(sfTaskDetailPage.getDueDate(customObjDriver).contains(msgDueDate));
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getToNumber(customObjDriver)));
	    assertTrue(CONFIG.getProperty("custom_org_user_number").contains(sfTaskDetailPage.getFromNumber(customObjDriver)));
	    
	    assertEquals(sfTaskDetailPage.getComments(customObjDriver), outboundMessage);
	    String msgSid = sfTaskDetailPage.getCallObjectId(customObjDriver);
	    
	    softPhoneMessagePage.closeTab(customObjDriver);
	    softPhoneMessagePage.switchToTab(customObjDriver, 1);
	    
	    //Login into web app and open the message sid
	    loginSupport(driver1);
	    dashboard.navigateToMessagesSection(driver1);
	    messagesPage.enterText_SearchInSection(driver1, MessagesPage.SID, msgSid);
	    messagesPage.clickSidUsingIndex(driver1, 0);
	    
	    //verify to and from numbers
	    assertEquals(messagesPage.getMsgInspctSID(driver1), msgSid);
	    assertEquals(messagesPage.getMsgInspctToNumber(driver1), CONFIG.getProperty("qa_user_1_number"));
	    assertEquals(messagesPage.getMsgInspctFromNumber(driver1), CONFIG.getProperty("custom_org_user_number"));
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);
	    
	    //Verify for inbound
	    String InboundMessage = "IndboundMessage" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //open sms page from the caller agent
	    System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(driver1, CONFIG.getProperty("custom_org_user_number"));
	    softPhoneMessagePage.idleWait(5);
	    
	    //send and verify the message time
	    softPhoneActivityPage.sendMessage(driver1, InboundMessage, 0);
	    String inboundMsgDate	= HelperFunctions.GetCurrentDateTime(msgDateFormate);
	    msgDueDate	= HelperFunctions.GetCurrentDateTime("M/d/yyyy");
	    msgDateTimeActivityPage = softPhoneActivityPage.getOutboundMsgDateTime(driver1, InboundMessage);
	    assertTrue(msgDateTimeActivityPage.contains(inboundMsgDate));
	    
	    //open recent message from receviver and verify then message has ben recieved
	    softPhoneMessagePage.clickMessageIcon(customObjDriver);
	    softPhoneMessagePage.clickOpenMessageIconByIndex(customObjDriver, 1);
	    softPhoneActivityPage.openMessageTab(customObjDriver);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(customObjDriver, InboundMessage));
	    
	    //Verifying outbound message on sfdc
	    callScreenPage.openCallerDetailPage(customObjDriver);
	    contactDetailPage.openLatestCallActivities(customObjDriver);
	    assertEquals(sfTaskDetailPage.getAssignedToUser(customObjDriver), CONFIG.getProperty("custom_org_user_name"));
	    assertEquals(sfTaskDetailPage.getCallDirection(customObjDriver), "Inbound");
	    assertEquals(sfTaskDetailPage.getSubject(customObjDriver), "Inbound Message: " + CONFIG.getProperty("qa_user_1_number").trim());
	    assertTrue(CONFIG.getProperty("custom_org_user_number").contains(sfTaskDetailPage.getToNumber(customObjDriver)));
	    assertTrue(CONFIG.getProperty("qa_user_1_number").contains(sfTaskDetailPage.getFromNumber(customObjDriver)));
	    assertEquals(sfTaskDetailPage.getComments(customObjDriver), InboundMessage);
	    
	    assertTrue(sfTaskDetailPage.getDueDate(customObjDriver).contains(msgDueDate));	    
	    msgSid = sfTaskDetailPage.getCallObjectId(customObjDriver);
	    
	    softPhoneMessagePage.closeTab(customObjDriver);
	    softPhoneMessagePage.switchToTab(customObjDriver, 1);
	    
	    //Login into web app and open the message sid
	    loginSupport(driver1);
	    dashboard.navigateToMessagesSection(driver1);
	    messagesPage.enterText_SearchInSection(driver1, MessagesPage.SID, msgSid);
	    messagesPage.clickSidUsingIndex(driver1, 0);
	    
	    //verify to and from numbers
	    assertEquals(messagesPage.getMsgInspctSID(driver1), msgSid);
	    assertEquals(messagesPage.getMsgInspctToNumber(driver1), CONFIG.getProperty("custom_org_user_number"));
	    assertEquals(messagesPage.getMsgInspctFromNumber(driver1), CONFIG.getProperty("qa_user_1_number"));
	    softPhoneMessagePage.closeTab(driver1);
	    softPhoneMessagePage.switchToTab(driver1, 1);	
	    
	    customObjDriver.quit();
	    customObjDriver = null;
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver3", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 @Test(groups={"Regression"})
	  public void verify_sender_detail_for_single_multiple_unknown_agents(){
	    System.out.println("Test case --verify_dropdown_for_single_multiple_agents()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver2");
	    driverUsed.put("driver2", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver5");
	    driverUsed.put("driver5", true);
	    
	    String msgReceiver = CONFIG.getProperty("prod_user_1_number");
	    
	    //Verify sender details for unknown contact
 		String outboundMessage1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 		String inboundMessage1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 		
 		// Making a unknown receiver contact
  		softPhoneCalling.softphoneAgentCall(driver1, msgReceiver);
  		softPhoneCalling.pickupIncomingCall(driver2);
  		softPhoneCalling.hangupActiveCall(driver1);

  		// Deleting contact
  		if (!callScreenPage.isCallerUnkonwn(driver1)) {
  			callScreenPage.deleteCallerObject(driver1);
  			softPhoneMessagePage.idleWait(3);
  			softPhoneSettingsPage.closeErrorMessage(driver1);
  			
  			softPhoneCalling.softphoneAgentCall(driver1, msgReceiver);
  	  		softPhoneCalling.pickupIncomingCall(driver2);
  	  		softPhoneCalling.hangupActiveCall(driver1);
  		}
  		
  		//Send message to unknown contact
  		softPhoneActivityPage.openMessageTab(driver1);
 	    softPhoneActivityPage.sendMessage(driver1, outboundMessage1, 0);
  		
 	    //Unknown contact sends back message to the agent
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver2);
	    softPhoneActivityPage.openMessageTab(driver2);
	    softPhoneActivityPage.sendMessage(driver2, inboundMessage1, 0);
	    
	    //Verify the senders detail for respective messages
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, CONFIG.getProperty("qa_user_1_name"));
	    softPhoneActivityPage.verifyInboundMessagesOwner(driver1, "Unknown");
	    
	    //Verify sender details for Single known contact
	    String outboundMessage2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 		String inboundMessage2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    
	    //Now add the caller as contact
	    callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("prod_user_1_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
	    softPhoneContactsPage.searchUntilContactPresent(driver1, HelperFunctions.getNumberInSimpleFormat(msgReceiver));
 		
	    //Send message to single known Contact
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    softPhoneActivityPage.openMessageTab(driver1);
	    seleniumBase.idleWait(2);
 	    softPhoneActivityPage.sendMessage(driver1, outboundMessage2, 0);
  		
 	    //Single known contact sends back message to the agent
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver2);
	    softPhoneActivityPage.openMessageTab(driver2);
	    softPhoneActivityPage.sendMessage(driver2, inboundMessage2, 0);
	    
	    //verify the senders detail for respective messages
	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, CONFIG.getProperty("qa_user_1_name"));
	    softPhoneActivityPage.verifyInboundMessagesOwner(driver1, CONFIG.getProperty("prod_user_1_name") + " " + "Automation");
 		
	    //Verify sender details for Multi known contact
 		//getting caller name to add as contact
	    softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_2_number"));
	    softPhoneCalling.pickupIncomingCall(driver5);
	    softPhoneCalling.hangupActiveCall(driver1);
	    String existingContact = callScreenPage.getCallerName(driver1);
	    
		// Make a call to the agent
  		softPhoneCalling.softphoneAgentCall(driver1, msgReceiver);
  		softPhoneCalling.pickupIncomingCall(driver2);
  		softPhoneCalling.hangupActiveCall(driver1);
  		callToolsPanel.isRelatedRecordsIconVisible(driver1);
  		seleniumBase.idleWait(5);
	    
	    //adding caller as multiple contact
	 	callScreenPage.clickOnUpdateDetailLink(driver1);
	 	callScreenPage.addCallerToExistingContact(driver1, existingContact);
	 	softPhoneMessagePage.idleWait(10);
	 	softPhoneContactsPage.searchUntilContacIsMultiple(driver1, msgReceiver);
	 	
	 	String outboundMessage3 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 		String inboundMessage3 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
 		
	    //Send message to Multi known Contact
 		softPhoneCalling.softphoneAgentCall(driver1, msgReceiver);
  		softPhoneCalling.pickupIncomingCall(driver2);
  		softPhoneCalling.hangupActiveCall(driver1);
 		softPhoneActivityPage.openMessageTab(driver1);
  	    softPhoneActivityPage.sendMessage(driver1, outboundMessage3, 0);
   		
 	    //Multiple known contact sends back message to the agent
 	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver2);
 	    softPhoneActivityPage.openMessageTab(driver2);
 	    softPhoneActivityPage.sendMessage(driver2, inboundMessage3, 0);
 	    
	    //verify the senders detail for respective messages
 	    softPhoneActivityPage.verifyOutboundMessagesOwner(driver1, CONFIG.getProperty("qa_user_1_name"));
 	    softPhoneActivityPage.verifyInboundMessagesOwner(driver1, "Multiple");
		
	    //Make user single match caller
	    aa_AddCallersAsContactsAndLeads();
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("driver5", false);
	    driverUsed.put("driver6", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	 //Verify SMS icon for Phone & Mobile Number on Dialer
	 //erify SMS is icon is not displayed if there no any number type of phone added
	 @Test(groups={"MediumPriority"})
	  public void verify_messaging_phone_mobile_number(){
	    System.out.println("Test case --verify_messaging_phone_mobile_number()-- started ");
	    
		//updating the driver used
	    initializeDriverSoftphone("driver1");
	    driverUsed.put("driver1", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("driver3");
	    driverUsed.put("driver3", true);
	    initializeDriverSoftphone("adminDriver");
	    driverUsed.put("adminDriver", true);
	    
	    String mobileNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_admin_user_number"));
	    String contactNumber = HelperFunctions.getNumberInRDNAFormat(CONFIG.getProperty("qa_user_3_number"));
	    String message1 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());	    
	    
	    //Delete contact and add it as a lead so no message is there for this user
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
	    //add mobile number for the contact
		  softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		  callScreenPage.clickOnUpdateDetailLink(driver1);
		  callScreenPage.enterMobileNumberForContact(driver1, mobileNumber);
		  callScreenPage.clickSaveContactButton(driver1);
		 
	    
	    //Verify that message icon is visible for both contact and mobile numbers.
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.verifyMsgIconVisible(driver1, mobileNumber);
	    callScreenPage.verifyMsgIconVisible(driver1, contactNumber);
		
		//Send message to mobile number
		callScreenPage.clickMsgIconByNumber(driver1, mobileNumber);
	    softPhoneActivityPage.sendMessage(driver1, message1, 1);
	    softPhoneMessagePage.idleWait(2);
	    
	    //verifying that message has been received
	    softPhoneMessagePage.clickMessageIcon(adminDriver);
		softPhoneMessagePage.clickOpenMessageIconByIndex(adminDriver, 1);
	    assertTrue(softPhoneActivityPage.verifyInboundMessage(adminDriver, message1));
	    
	    String message2 = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());	    
	    
	    //Send message to phone number
  		callScreenPage.clickMsgIconByNumber(driver1, contactNumber);
  	    softPhoneActivityPage.sendMessage(driver1, message2, 1);
  	    softPhoneMessagePage.idleWait(2);
  	    
  	    //verifying that message has been received
  	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
  	    softPhoneActivityPage.openMessageTab(driver3);
  	    assertTrue(softPhoneActivityPage.verifyInboundMessage(driver3, message2));
  	    
  	    //remove phone number for the contact
	    callScreenPage.clickOnUpdateDetailLink(driver1);
	    callScreenPage.enterPhoneNumber(driver1, "");
	    callScreenPage.clickSaveContactButton(driver1);
	    
	    //verify that message icon only visible for mobile number as phone number not visible
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.verifyMsgIconVisible(driver1, mobileNumber);
	    callScreenPage.verifyPhoneNumberNotVisible(driver1);
	    
	    //delete and add contact again
	    softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("qa_user_3_name"));
	    
		//Setting driver used to false as this test case is pass
	    driverUsed.put("driver1", false);
	    driverUsed.put("driver2", false);
	    driverUsed.put("driver3", false);
	    driverUsed.put("driver4", false);
	    driverUsed.put("adminDriver", false);
	    
	    System.out.println("Test case is pass");
	  }
	 
	public void deleteBlockedNumber(String blockedNumber) {
		// verify that sent number is present in blocked numbers list
		if (isNumberBlocked) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);

			accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
			assertTrue(accountBlockedNumbersTab.isNumberBlocked(driver1, blockedNumber));
			accountBlockedNumbersTab.deleteBlockedNumber(driver1, blockedNumber);
			softPhoneMessagePage.closeTab(driver1);
			softPhoneMessagePage.switchToTab(driver1, 1);

			reloadSoftphone(driver1);
		}
	}
	
	public void deleteAllBlockedNumber() {
		// verify that sent number is present in blocked numbers list
		if (isNumberBlocked) {
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);

			accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
			accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
			softPhoneMessagePage.closeTab(driver1);
			softPhoneMessagePage.switchToTab(driver1, 1);

			reloadSoftphone(driver1);
		}
	}
	  
	  @AfterMethod(groups = {"Regression", "MediumPriority", "Product Sanity"}, dependsOnMethods={"resetSetupDefault"})
	  public void afterMethod(ITestResult result){
		if (result.getStatus() == 2 || result.getStatus() == 3) {
			switch (result.getName()) {
			case "send_message_create_lead":
				callScreenPage.addCallerAsContact(driver1, CONFIG.getProperty("qa_user_3_name"), SoftphoneBase.CONFIG.getProperty("contact_account_name"));
				break;
			case "send_message_add_to_existing":
				aa_AddCallersAsContactsAndLeads();
				break;
			case"verify_dropdown_for_single_multiple_agents":
				aa_AddCallersAsContactsAndLeads();
				break;
			case "verify_placeholder_contact_card_mobile_number":
				softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_3_number"), CONFIG.getProperty("qa_user_3_name"));
				break;
			case "verify_spam_for_unknown_user":
				deleteAllBlockedNumber();
		 	   softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
		 	   break;
			case "verify_spam_for_other_users":
		 	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		 	    callScreenPage.clickUnblockButton(driver1);
		 	    callScreenPage.verifyUnblockBtnInvisible(driver1);
		 	    softPhoneContactsPage.deleteAndAddContact(driver1, CONFIG.getProperty("qa_user_2_number"), CONFIG.getProperty("qa_user_2_name"));
				break;
			case "softphone_messaging_global_local_presence":
				//disable local presence number as messaging Number
			    System.out.println("disabling local presence setting");	    
			    softPhoneSettingsPage.clickSettingIcon(driver1);
			    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
				break;
			case "softphone_messaging_additional_local_presence":
				//disable local presence number as messaging Number
			    System.out.println("disabling local presence setting");	    
			    softPhoneSettingsPage.clickSettingIcon(driver3);
			    softPhoneSettingsPage.disableLocalPresenceSetting(driver3);
				break;
			case "softphone_messaging_australia_local_presence":
				//disable local presence number as messaging Number
			    System.out.println("disabling local presence setting");	    
			    softPhoneSettingsPage.clickSettingIcon(driver1);
			    softPhoneSettingsPage.disableLocalPresenceSetting(driver1);
				break;
			case "verify_caller_sending_start_stop_msgs_entry_in_blocked_numbers_tab":
			case "verify_msg_not_recieved_for_agent_from_blocked_number":

				//deleting all blocked numbers
				loginSupport(driver1);
				dashboard.clickAccountsLink(driver1);
				accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
				accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
				accountBlockedNumbersTab.closeTab(driver1);
				accountBlockedNumbersTab.switchToTab(driver1, 1);
				break;
			default:
				break;
			}
		}
	}
	  
	@AfterClass(groups = { "Regression", "MediumPriority", "Product Sanity", "ExludeForProd" })
	public void afterClass() {
		// navigating to support page
		loginSupport(driver1);
		dashboard.openSmartNumbersTab(driver1);
		String smartNumber  = "+15755025652";
		smartNumbersPage.searchSmartNumber(driver1, smartNumber);
		int smartNoIndex = smartNumbersPage.getSmartNumbersIndex(driver1, smartNumber);
		if (smartNoIndex < 0) {
			// UnDeleting smart Number
			smartNumbersPage.clickSmartNumber(driver1, smartNumber);
			smartNumbersPage.clickUndeleteBtn(driver1);
		}
		
		// deleting all blocked numbers
		dashboard.clickAccountsLink(driver1);
		accountBlockedNumbersTab.navigateToBlockedNumbersTab(driver1);
		accountBlockedNumbersTab.deleteAllBlockedNumber(driver1);
		accountBlockedNumbersTab.closeTab(driver1);
		accountBlockedNumbersTab.switchToTab(driver1, 1);
	}
}
