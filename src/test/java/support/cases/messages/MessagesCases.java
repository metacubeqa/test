package support.cases.messages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;
import org.testng.util.Strings;

import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneMessagePage;
import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.messages.MessagesPage;
import utility.HelperFunctions;

public class MessagesCases extends SupportBase {

	Dashboard dashboard = new Dashboard();
	MessagesPage messagesPage = new MessagesPage();
	SoftPhoneMessagePage sms = new SoftPhoneMessagePage();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String messagesFileNameDownload = "message-export";

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_message_download_link_successfully() {
		System.out.println("Test case --verify_message_download_link_successfully-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.navigateToMessagesSection(supportDriver);

		// downloading and verifying csv
		HelperFunctions.deletingExistingFiles(downloadPath, messagesFileNameDownload);
		messagesPage.clickDownloadLink(supportDriver);

		boolean fileDownloaded = messagesPage.downloadRecordsSuccessfullyOrNot(supportDriver, downloadPath, messagesFileNameDownload,
				".csv");
		assertTrue(fileDownloaded, "file is not downloaded");

		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_message_search_functionality() {
		System.out.println("Test case --verify_message_search_functionality-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.navigateToMessagesSection(supportDriver);
		
		String sid = messagesPage.getTextFromListFromIndex(supportDriver, MessagesPage.SID, 0);
		messagesPage.enterText_SearchInSection(supportDriver, MessagesPage.SID, sid);
		assertTrue(messagesPage.verifyListContainsText(supportDriver, MessagesPage.SID, sid));
		messagesPage.clearSections(supportDriver, MessagesPage.SID);
		
		String userName = messagesPage.getTextFromListFromIndex(supportDriver, MessagesPage.USER_NAME, 0);
		messagesPage.enterText_SearchInSection(supportDriver, MessagesPage.USER_NAME, userName);
		assertTrue(messagesPage.verifyListContainsText(supportDriver, MessagesPage.USER_NAME, userName));
		messagesPage.clearSections(supportDriver, MessagesPage.USER_NAME);

		String accountName = messagesPage.getTextFromListFromIndex(supportDriver, MessagesPage.ACCOUNT_NAME, 0);
		messagesPage.enterText_SearchInSection(supportDriver, MessagesPage.ACCOUNT_NAME, accountName);
		assertTrue(messagesPage.verifyListContainsText(supportDriver, MessagesPage.ACCOUNT_NAME, accountName));
		messagesPage.clearSections(supportDriver, MessagesPage.ACCOUNT_NAME);
		
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_message_sent_functionality() throws Exception {
		System.out.println("Test case --verify_message_sent_functionality-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		messagesPage.switchToTab(supportDriver, 1);
		
		//open sms page from the dial pad
		String toNumber = CONFIG.getProperty("qa_admin_user_number");
		String fromNumber = CONFIG.getProperty("qa_support_user_number");
		String inboundOwner = CONFIG.getProperty("qa_admin_user_name");
		String outboundOwner = CONFIG.getProperty("qa_support_user_name");
		
	    String message = "AutoSMSMsg".concat(HelperFunctions.GetRandomString(3));
		System.out.println("open sms page from the caller agent");
	    softPhoneCalling.softphoneAgentSMS(supportDriver, toNumber);
	    messagesPage.idleWait(1);
	    softPhoneActivityPage.enterMessageText(supportDriver, message);
	    messagesPage.idleWait(1);
	    softPhoneActivityPage.clickSendButton(supportDriver);
	    messagesPage.idleWait(1);
		
	    //open message from message tab
	    sms.clickMessageIcon(supportDriver);
	    sms.idleWait(1);
	    sms.verifyMessageHeadingPresent(supportDriver);
	    sms.isMsgPresentInAllTab(supportDriver, message);
	    String createdDateTime = sms.getMsgDateTime(supportDriver, message);
	    
	    SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yyyy, H:mm");  
	    Date oldDate = oldFormat.parse(createdDateTime);  
	    SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    String newDate = newFormat.format(oldDate);
	    System.out.println(newDate);
	    if(createdDateTime.contains("PM")) {
	    	newDate = newDate.concat(" pm");
	    }
	    else {
	    	newDate = newDate.concat(" am");
	    }
	    //
	    messagesPage.switchToTab(supportDriver, 2);
	    messagesPage.idleWait(2);
	    dashboard.navigateToMessagesSection(supportDriver);
	    dashboard.refreshCurrentDocument(supportDriver);
	    
	    // Clicking sid of outbound owner
	    String sid = messagesPage.clickSidWithOwner_Direction(supportDriver, outboundOwner, "Outbound", newDate);
	    
	    // Verifying outbound owner details
	    messagesPage.idleWait(1);
	    assertEquals(messagesPage.getMsgInspctTextMsg(supportDriver), message);
	    assertEquals(messagesPage.getMsgInspctSID(supportDriver), sid);
	    assertEquals(messagesPage.getMsgInspctToNumber(supportDriver), toNumber);
	    assertEquals(messagesPage.getMsgInspctFromNumber(supportDriver), fromNumber);
	    assertEquals(messagesPage.getMsgInspctDirection(supportDriver), "Outbound");
	    assertEquals(messagesPage.getMsgInspctOwner(supportDriver), outboundOwner);
	    assertTrue(Strings.isNullOrEmpty(messagesPage.getMsgInspctErrorCode(supportDriver)));
	    assertEquals(messagesPage.getMsgInspctMsgSid(supportDriver), sid);
	    
	    // Clicking sid of inbound owner
	    dashboard.navigateToMessagesSection(supportDriver);
	    sid = messagesPage.clickSidWithOwner_Direction(supportDriver, inboundOwner, "Inbound", newDate);
	    
	    // Verifying inbound owner details
	    messagesPage.idleWait(1);
	    assertEquals(messagesPage.getMsgInspctTextMsg(supportDriver), message);
	    assertEquals(messagesPage.getMsgInspctSID(supportDriver), sid);
	    assertEquals(messagesPage.getMsgInspctToNumber(supportDriver), toNumber);
	    assertEquals(messagesPage.getMsgInspctFromNumber(supportDriver), fromNumber);
	    assertEquals(messagesPage.getMsgInspctDirection(supportDriver), "Inbound");
	    assertEquals(messagesPage.getMsgInspctStatus(supportDriver), "Received");
	    assertEquals(messagesPage.getMsgInspctOwner(supportDriver), inboundOwner);
	    assertTrue(Strings.isNullOrEmpty(messagesPage.getMsgInspctErrorCode(supportDriver)));
	    assertEquals(messagesPage.getMsgInspctMsgSid(supportDriver), sid);
	    
		// Setting driver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case is pass");
	}
}
