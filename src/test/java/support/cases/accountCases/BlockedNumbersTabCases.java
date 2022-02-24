package support.cases.accountCases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.source.CallScreenPage;
import softphone.source.SoftPhoneActivityPage;
import softphone.source.SoftPhoneCalling;
import softphone.source.SoftPhoneMessagePage;
import softphone.source.SoftphoneCallHistoryPage;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.salesforce.TaskDetailPage;
import support.base.SupportBase;
import support.source.accounts.AccountBlockedNumbersTab;
import support.source.accounts.AccountBlockedNumbersTab.BlockType;
import support.source.accounts.AccountBlockedNumbersTab.Direction;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class BlockedNumbersTabCases extends SupportBase {

	AccountBlockedNumbersTab blockNumberTab = new AccountBlockedNumbersTab();
	SoftPhoneCalling softPhoneCalling = new SoftPhoneCalling();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	Dashboard dashboard = new Dashboard();
	CallScreenPage callScreenPage = new CallScreenPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	SoftPhoneActivityPage softPhoneActivityPage = new SoftPhoneActivityPage();
	SoftPhoneMessagePage messagingPage = new SoftPhoneMessagePage();
	TaskDetailPage sfTaskDetailPage = new TaskDetailPage();
	
	private String numberToBlock;
	private String numberToDial;
	private String contactFirstName;
	private String contactAccountName;

	@BeforeClass(groups = { "Regression", "MediumPriority" })
	public void beforeClass() {
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			numberToDial = CONFIG.getProperty("qa_admin_user_number");
		} else if (SupportBase.drivername.toString().equals("supportDriver")) {
			numberToDial = CONFIG.getProperty("qa_support_user_number");
		}
		numberToBlock = CONFIG.getProperty("qa_agent_user_number");
		contactFirstName = CONFIG.getProperty("contact_first_name");
		contactAccountName = CONFIG.getProperty("contact_account_name");	
	}

	@Test(groups = { "Regression" })
	public void add_update_delete_block_number_and_verify() {
		System.out.println("Test case --add_update_delete_block_number_and_verify-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to blocked numbers tab
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.disableCallDispositionRequiredSetting(supportDriver);

		//Adding blocked number
		blockNumberTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Call, "", "");
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//calling from agent to admin/support
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, numberToDial);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);
		
		//verifying on admin/support that no accept button is visible
		blockNumberTab.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		
		//verifying on agent that call is disconnected
		blockNumberTab.switchToTab(agentDriver, 1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(agentDriver));
		
		String newDriverStringInitialize;
		WebDriver newDriverInitialize;
		String newNumberToBlock;
		
		if (SupportBase.drivername.toString().equals("adminDriver")) {
			newDriverStringInitialize = "webSupportDriver";
			newDriverInitialize = webSupportDriver;
			newNumberToBlock = CONFIG.getProperty("qa_support_user_number");
		}
		else{
			newDriverStringInitialize = "adminDriver";
			newDriverInitialize = adminDriver;
			newNumberToBlock = CONFIG.getProperty("qa_admin_user_number");
		}
		
		//Updating and changing number and block type
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.editBlockNumber(supportDriver, numberToBlock, newNumberToBlock, "All", BlockType.Call);

		// initializing cai driver
		initializeSupport(newDriverStringInitialize);
		driverUsed.put(newDriverStringInitialize, true);
		
		// calling from cai driver to admin/support
		blockNumberTab.switchToTab(newDriverInitialize, 1);
		softPhoneCalling.hangupIfInActiveCall(newDriverInitialize);
		softPhoneCalling.softphoneAgentCall(newDriverInitialize, numberToDial);
		callToolsPanel.callNotesSectionVisible(newDriverInitialize);

		// verifying on admin/support that no accept button is visible
		blockNumberTab.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));

		// verifying on agent that call is disconnected
		blockNumberTab.switchToTab(newDriverInitialize, 1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(newDriverInitialize));
				
		//deleting blocked number
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		//verifying now agent user able to make call to admin/support
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(agentDriver);

		// verifying on admin/support that accept button is visible
		blockNumberTab.switchToTab(supportDriver, 1);
		assertTrue(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		
		// verifying on agent that call is connected
		blockNumberTab.switchToTab(agentDriver, 1);
		assertTrue(softPhoneCalling.isCallHoldButtonVisible(agentDriver));
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("agentDriver", false);
		driverUsed.put("supportDriver", false);
		driverUsed.put("newDriverIntialize", false);
		System.out.println("Test case --add_update_delete_block_number_and_verify-- passed ");
	}

	@Test(groups = { "MediumPriority" })
	public void enable_setting_disposition_operations() {
		System.out.println("Test case --enable_setting_disposition_operations-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to blocked numbers tab
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.enableBlockCallByDispositionSetting(supportDriver);

		//verifying block for details
		blockNumberTab.verifyBlockForDetails(supportDriver, BlockType.Recording);

		//Adding blocked number
		blockNumberTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Call, "", "");
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//calling from agent to admin/support
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, numberToDial);
		callToolsPanel.callNotesSectionVisible(agentDriver);
		
		//verifying on admin/support that no accept button is visible
		blockNumberTab.switchToTab(supportDriver, 1);
		assertFalse(softPhoneCalling.isDeclineButtonVisible(supportDriver));
		
		//verifying on agent that call is disconnected
		blockNumberTab.switchToTab(agentDriver, 1);
		assertFalse(softPhoneCalling.isCallHoldButtonVisible(agentDriver));
		
		//deleting blocked number
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("agentDriver", false);
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --enable_setting_disposition_operations-- passed ");
	}

	@Test(groups = { "Regression" })
	public void add_recording_type_block_and_verify_url_in_sfdc() {
		System.out.println("Test case --add_recording_type_block_and_verify_url_in_sfdc-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to blocked numbers tab
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.disableCallDispositionRequiredSetting(supportDriver);

		//Adding blocked number of recording type
		blockNumberTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Recording, "", "");
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// calling from agent to admin/support
		String callSubject = "AutoCallSubject".concat(HelperFunctions.GetRandomString(3));
		String callNotes = "AutoCallNote".concat(HelperFunctions.GetRandomString(3));

		//dialing number from agent
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		softPhoneCalling.softphoneAgentCall(agentDriver, numberToDial);
		softPhoneCalling.isCallHoldButtonVisible(agentDriver);

		//picking up call on admin/support
		blockNumberTab.switchToTab(supportDriver, 1);
		softPhoneCalling.pickupIncomingCall(supportDriver);
		callToolsPanel.enterCallNotes(supportDriver, callSubject, callNotes);
		blockNumberTab.idleWait(3);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//saving contact if not already
		if (callScreenPage.isCallerUnkonwn(supportDriver)) {
			callToolsPanel.callNotesSectionVisible(supportDriver);
			callScreenPage.addCallerAsLead(supportDriver, contactFirstName.concat(HelperFunctions.GetRandomString(2)), contactAccountName);
		}

		//Navigating to task detail page to check url
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(supportDriver);
		softPhoneActivityPage.openTaskInSalesforce(supportDriver, callSubject);
		softPhoneActivityPage.waitForPageLoaded(supportDriver);
		sfTaskDetailPage.closeLightningDialogueBox(supportDriver);
		sfTaskDetailPage.isCallRecordingURLInvisible(supportDriver);
	    blockNumberTab.closeTab(supportDriver);
	    blockNumberTab.switchToTab(supportDriver, 1);
	    softPhoneCalling.hangupIfInActiveCall(supportDriver);
	    
		// deleting blocked number
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		//hanging up active call
		blockNumberTab.switchToTab(agentDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(agentDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --add_recording_type_block_and_verify_url_in_sfdc-- passed ");
	}

	@Test(groups = { "Regression" })
	public void set_disposition_value_spam_and_verify_number_blocked() {
		System.out.println("Test case --set_disposition_value_spam_and_verify_number_blocked-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		//deleting all block numbers
		blockNumberTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.enableBlockCallByDispositionSetting(supportDriver);
		
		//calling and setting disposition value as "spam"
		blockNumberTab.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
		softPhoneCalling.softphoneAgentCall(supportDriver, numberToBlock);
		callToolsPanel.selectDispositionUsingText(supportDriver, "spam");
		callToolsPanel.idleWait(2);
		callScreenPage.verifyUnblockBtnVisible(supportDriver);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);

		//verifying in blocked numbers tab that number is added as block
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, numberToBlock));
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.disableCallDispositionRequiredSetting(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --set_disposition_value_spam_and_verify_number_blocked-- passed");
	}
	
	@Test(groups = { "Regression" })
	public void block_number_for_messaging_for_all_users() {
		System.out.println("Test case --block_number_for_messaging_for_all_users-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// deleting all block numbers
		blockNumberTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);

		// Adding blocked number of recording type for direction outbound
		blockNumberTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Message, "All", Direction.Outbound.toString());

		//Sending message through quick link from message tab
		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		blockNumberTab.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
	    softPhoneCalling.softphoneAgentSMS(supportDriver, numberToBlock);
	    blockNumberTab.idleWait(2);
	    softPhoneActivityPage.openMessageTab(supportDriver);
	    softPhoneActivityPage.enterMessageText(supportDriver, message);
		softPhoneActivityPage.clickSendButton(supportDriver);
		
		// verify restrict sms msg appears
		softPhoneActivityPage.verifyRestrictSmsMsg(supportDriver);
		blockNumberTab.idleWait(2);

	    //open message from message tab in agent driver and verify msg not present
	    blockNumberTab.switchToTab(agentDriver, 1);
	    softPhoneCalling.hangupIfInActiveCall(agentDriver);
	    messagingPage.clickMessageIcon(agentDriver);
	    assertFalse(messagingPage.isMsgPresentInAllTab(agentDriver, message));
		
		// deleting all block numbers
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --block_number_for_messaging_for_all_users-- passed");
	}
	
	@Test(groups = { "MediumPriority"})
	public void block_number_for_messaging_for_selected_user() {
		System.out.println("Test case --block_number_for_messaging_for_selected_user-- started");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		// initializing agent driver
		initializeSupport("agentDriver");
		driverUsed.put("agentDriver", true);

		// deleting all block numbers
		blockNumberTab.switchToTab(supportDriver, 2);
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);

		// Adding blocked number of recording type for direction outbound
		blockNumberTab.addBlockedNumber(supportDriver, numberToBlock, BlockType.Message, CONFIG.getProperty("qa_agent_user_name"), Direction.Outbound.toString());

		//Sending message through quick link from message tab
		String message = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		blockNumberTab.switchToTab(supportDriver, 1);
		softPhoneCalling.hangupIfInActiveCall(supportDriver);
	    softPhoneCalling.softphoneAgentSMS(supportDriver, numberToBlock);
	    blockNumberTab.idleWait(2);
	    softPhoneActivityPage.openMessageTab(supportDriver);
	    softPhoneActivityPage.enterMessageText(supportDriver, message);
		softPhoneActivityPage.clickSendButton(supportDriver);
		
		// verify restrict sms msg appears
		softPhoneActivityPage.verifyRestrictSmsMsg(supportDriver);
		blockNumberTab.idleWait(2);

	    //open message from message tab in agent driver and verify msg not present
	    blockNumberTab.switchToTab(agentDriver, 1);
	    softPhoneCalling.hangupIfInActiveCall(agentDriver);
	    messagingPage.clickMessageIcon(agentDriver);
	    assertFalse(messagingPage.isMsgPresentInAllTab(agentDriver, message));
		
		// deleting all block numbers
		blockNumberTab.switchToTab(supportDriver, 2);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		driverUsed.put("agentDriver", false);
		System.out.println("Test case --block_number_for_messaging_for_selected_user-- passed");
	}
	
	@Test(groups = { "MediumPriority" })
	public void search_blocked_number_by_user_type_user_name() {
		System.out.println("Test case --search_blocked_number_by_user_type_user_name-- started ");
		
		// updating the supportDriver used
		System.out.println("in before method");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//Navigating to blocked numbers tab
		dashboard.clickAccountsLink(supportDriver);
		blockNumberTab.navigateToBlockedNumbersTab(supportDriver);
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		blockNumberTab.disableCallDispositionRequiredSetting(supportDriver);

		//Adding blocked number of recording type
		String recordingNumber = HelperFunctions.generateTenDigitNumber();
		blockNumberTab.addBlockedNumber(supportDriver, recordingNumber, BlockType.Recording, "", "");
		
		//Adding blocked number of call type
		String callNumber = HelperFunctions.generateTenDigitNumber();
		blockNumberTab.addBlockedNumber(supportDriver, callNumber, BlockType.Call, "", "");
		
		// Adding blocked number of recording type for direction outbound
		String messageNumber = numberToBlock;
		String messageAgent = CONFIG.getProperty("qa_agent_user_name");
		blockNumberTab.addBlockedNumber(supportDriver, messageNumber, BlockType.Message, messageAgent, Direction.Outbound.toString());
		
		//Searching Type of Recording 
		blockNumberTab.searchBlockedNumberByType(supportDriver, BlockType.Recording);
		
		//Verifying only recording number visible
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, recordingNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, callNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, messageNumber));
		
		// Searching Type of Call
		blockNumberTab.searchBlockedNumberByType(supportDriver, BlockType.Call);

		//Verifying only call number visible
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, callNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, recordingNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, messageNumber));

		// Searching Type of Message
		blockNumberTab.searchBlockedNumberByType(supportDriver, BlockType.Message);

		//Verifying only message number visible
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, messageNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, recordingNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, callNumber));

		//Searching back to All type
		blockNumberTab.searchBlockedNumberByType(supportDriver, BlockType.All);
		
		//Searching by username
		blockNumberTab.searchBlockedNumberByUserName(supportDriver, messageAgent);

		//Verifying only message number visible
		assertTrue(blockNumberTab.isNumberBlocked(supportDriver, messageNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, recordingNumber));
		assertFalse(blockNumberTab.isNumberBlocked(supportDriver, callNumber));

		blockNumberTab.searchBlockedNumberByUserName(supportDriver, "");
		blockNumberTab.deleteAllBlockedNumber(supportDriver);
		
		// updating the supportDriver used
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --search_blocked_number_by_user_type_user_name-- passed ");
	}
}
