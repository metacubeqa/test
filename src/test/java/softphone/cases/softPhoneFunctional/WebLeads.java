/**
 * 
 */
package softphone.cases.softPhoneFunctional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import softphone.base.SoftphoneBase;
import softphone.source.SoftPhoneContactsPage;
import softphone.source.salesforce.ContactDetailPage;
import utility.HelperFunctions;

/**
 * @author Abhishek
 *
 */
public class WebLeads extends SoftphoneBase{

	String webLeadName 	= null;
	String webLeadPhone = null;
	int oldWebLeadsCount = 0;
	String fieldName 	= null;
	String fieldValue	= null;
	String teamName		= null;
	String leadSource 	= null;
	String description	= null;
	String email		= null;
	String company		= "Metacube";
	String title		= "QA";
	HashMap<ContactDetailPage.LeadDetailsFields, String> leadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
	
	@Test(groups={"Regression", "MediumPriority", "Product Sanity"})
	public void lead_in_web_leads_section(){
		
		System.out.println("Test case --lead_in_web_leads_section-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
	
		webLeadName  = CONFIG.getProperty("prod_user_3_name") + " Automation";
		webLeadPhone = HelperFunctions.getNumberInSimpleFormat(CONFIG.getProperty("prod_user_3_number"));
		fieldName 	 = "City";
		fieldValue	 = "JAIPUR";
		teamName	 = CONFIG.get("qa_group_2_name").toString().trim();
		leadSource	 = "Other";
		description	 = "This is a test Automation web Lead";
		email		 = "abhishek.gupta@metacube.com";
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		softPhoneCalling.pickupIncomingCall(driver6);
	
		// Deleting contact and calling again
		if (!callScreenPage.isCallerUnkonwn(driver1)) {
			callScreenPage.deleteCallerObject(driver1);
			softPhoneCalling.hangupActiveCall(driver1);
			softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
			softPhoneCalling.pickupIncomingCall(driver6);
			softPhoneSettingsPage.closeErrorMessage(driver1);
		}

		// add caller as Lead
		callScreenPage.addCallerAsLead(driver1, CONFIG.getProperty("prod_user_3_name"), company);
		softPhoneSettingsPage.closeErrorMessage(driver1);

		//hanging up with caller 1
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver1);
	
		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver1);
	
		//get old WebLeads Count
		oldWebLeadsCount = softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver1);
		
		//open recent call entry
		seleniumBase.idleWait(5);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);  
		
		//set lead as Web lead
		leadDetails.put(ContactDetailPage.LeadDetailsFields.checkRingDNAWebLeads, "true");
		leadDetails.put(ContactDetailPage.LeadDetailsFields.leadSource, leadSource);
		leadDetails.put(ContactDetailPage.LeadDetailsFields.leadCity, fieldValue);
		leadDetails.put(ContactDetailPage.LeadDetailsFields.description, description);
		leadDetails.put(ContactDetailPage.LeadDetailsFields.email, email);
		callScreenPage.openCallerDetailPage(driver1);
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		softPhoneWebLeadsPage.VerifyWebLeadsNotificationCoutntsIncreased(driver1, oldWebLeadsCount);	  
		
		//Verify that new web lead is on the top
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), 0);
		assertTrue(softPhoneWebLeadsPage.getWebLeadCreateionDate(driver1, webLeadName).equals("in a few seconds")
				|| softPhoneWebLeadsPage.getWebLeadCreateionDate(driver1, webLeadName).equals("a few seconds ago")
				|| softPhoneWebLeadsPage.getWebLeadCreateionDate(driver1, webLeadName).equals("in a minute"));
		
		//navigate to some other tab and go back to web lead page
		softphoneCallHistoryPage.openCallsHistoryPage(driver1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), 0);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver6", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);	
		
		System.out.println("Test case is pass");
	}

	@Test(groups={"Regression"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_search_lead_in_web_leads()
	{
		System.out.println("Test case --verify_search_lead_in_web_leads-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//search web lead using name
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.searchWebLeads(driver1, webLeadName);
		seleniumBase.idleWait(5);
		softPhoneWebLeadsPage.verifyWebLeadSearch(driver1, webLeadName);
		
		//clear the search
		softPhoneWebLeadsPage.searchWebLeads(driver1, "");
		seleniumBase.idleWait(2);
		
		//Search using Lead Source Type
		softPhoneWebLeadsPage.selectLeadSource(driver1, leadSource);
		softPhoneWebLeadsPage.verifyWebLeadsSourceSearch(driver1, leadSource);
		softPhoneWebLeadsPage.selectLeadSource(driver1, "All");
	
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_web_leads_sorted()
	{
		System.out.println("Test case --verify_web_leads_sorted-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//Sort web lead in ascending order
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.selectCreationDate(driver1, "Oldest Leads");
		seleniumBase.idleWait(5);
		List<String> ascendingWebLeadsList = softPhoneWebLeadsPage.getWebLeadsNameList(driver1);
		assertEquals(ascendingWebLeadsList.get(ascendingWebLeadsList.size() - 1), "Piyush Beli Automation");
		
		//sort web leads in descending order
		softPhoneWebLeadsPage.selectCreationDate(driver1, "Newest Leads");
		seleniumBase.idleWait(5);
		List<String> descendingWebLeadsList = softPhoneWebLeadsPage.getWebLeadsNameList(driver1);
		assertEquals(descendingWebLeadsList.get(0), "Todd Bursey Automation");
		
		//verify that the list is reversed
		assertTrue(Lists.reverse(ascendingWebLeadsList).equals(descendingWebLeadsList));
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"Regression", "MediumPriority"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_web_leads_rule()
	{
		System.out.println("Test case --create_web_leads_rule-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		// Opening support app
		loginSupport(driver1);
		
		//removing user from the group
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.deleteMember(driver1, CONFIG.getProperty("qa_user_2_name"));

		// Create a web lead rule on supports tab
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, fieldName, fieldValue, teamName);
		accountIntelligentDialerTab.createWebLeadRule(driver1, fieldName, fieldValue, teamName, "All Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//Verify that web lead is appearing to only group member
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, webLeadName);
	
		//Verify that web leads is not appearing to any other member
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver4, webLeadName), -1);
				
		//adding user to the group
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Verify that web lead is appearing to other group member
		softPhoneWebLeadsPage.verifyWebLeadExists(driver4, webLeadName);
		
		/*//change the visibility of added web lead to owned Records
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "Owned Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//verify that web lead is appearing only for agent 1
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, webLeadName);
	
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver4, webLeadName), -1);
		
		//change the visibility of added web lead to All Records
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "All Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);*/
	    
		//removing user from the group
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.deleteMember(driver1, CONFIG.getProperty("qa_user_2_name"));
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Verify that web leads is not appearing to any other member
	    softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver4, webLeadName), -1);
		
		//deleting web rule
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, fieldName, fieldValue, teamName);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_custom_field_web_leads_rule()
	{
		System.out.println("Test case --verify_custom_field_web_leads_rule-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		String customFieldName = "Web Lead";
		String customFieldValue = "true";
		
		//Making outbound Call
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		seleniumBase.idleWait(5);
		softPhoneCalling.hangupIfInActiveCall(driver1);
		
		//uncheck lead a webLead
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    callScreenPage.openCallerDetailPage(driver1);
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		contactDetailPage.clickContactLeadEditButton(driver1);
		contactDetailPage.uncheckRingDNAWebLeadsCheckBox(driver1);
		contactDetailPage.clickSaveBtn(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);

		// Create a web lead rule on supports tab
		loginSupport(driver1);
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.addMember(driver1, CONFIG.getProperty("qa_user_2_name"));
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, customFieldName, customFieldValue, teamName);
		accountIntelligentDialerTab.createWebLeadRule(driver1, customFieldName, customFieldValue, teamName, "All Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Verify that lead is removed from Web Lead
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, webLeadName), -1);
	    
		//now make lead web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    HashMap<ContactDetailPage.LeadDetailsFields, String> customFieldleadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
	    customFieldleadDetails.put(ContactDetailPage.LeadDetailsFields.checkWebLead, "true");
	    customFieldleadDetails.put(ContactDetailPage.LeadDetailsFields.checkRingDNAWebLeads, "true");
		callScreenPage.openCallerDetailPage(driver1);
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		contactDetailPage.updateSalesForceLeadDetails(driver1, customFieldleadDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
		
		//Verify that web lead is appearing to only group member
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, webLeadName);
				
		//Verify that web lead is appearing to other group member
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver4, webLeadName);
		
		//deleting web rule
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, fieldName, fieldValue, teamName);
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		
		//removing user from the group
		groupsPage.openGroupSearchPage(driver1);
		groupsPage.openGroupDetailPage(driver1, CONFIG.getProperty("qa_group_2_name"), CONFIG.getProperty("qa_user_account"));
		groupsPage.deleteMember(driver1, CONFIG.getProperty("qa_user_2_name"));
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_web_leads_details_page_description()
	{
		System.out.println("Test case --verify_web_leads_details_page_description-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//verify web lead details
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		
		//very web leads description
		softPhoneWebLeadsPage.openWebLeadsContactDetail(driver1, webLeadName);
		softPhoneWebLeadsPage.verifyWebLeadDescription(driver1, description);
			
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_web_leads_description()
	{
		System.out.println("Test case --verify_web_leads_description-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		
		//verify web lead details
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		seleniumBase.idleWait(3);
		assertEquals(softPhoneWebLeadsPage.getWebLeadTitle(driver1, webLeadName), title);
		assertEquals(softPhoneWebLeadsPage.getWebLeadCompany(driver1, webLeadName), company);
		assertEquals(softPhoneWebLeadsPage.getWebLeadPhone(driver1, webLeadName), webLeadPhone);
		assertEquals(softPhoneWebLeadsPage.getWebLeadEmail(driver1, webLeadName), email);
		assertEquals(softPhoneWebLeadsPage.getWebLeadDescription(driver1, webLeadName), description);
		
		//very web leads description
		softPhoneWebLeadsPage.openWebLeadsContactDetail(driver1, webLeadName);
		
		//verify web leads custom fields
		callScreenPage.verifyCustomFields(driver1, "Country (CreatedBy)", "India");
		callScreenPage.verifyCustomFields(driver1, "City (CreatedBy)", "Delhi");
		callScreenPage.verifyCustomFields(driver1, "Company Name (CreatedBy)", "Metacube Software Pvt Ltd");
		callScreenPage.verifyCustomFieldsInvisible(driver1, "Extension (CreatedBy)");
		
		//verify that clicking on email icon doesn't remove web lead
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		seleniumBase.idleWait(3);
		softPhoneWebLeadsPage.clickWebLeadEmailBtn(driver1, webLeadName);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver4, webLeadName);
		
		//verify that clicking on message and sending it doesn't remove web lead
		softPhoneWebLeadsPage.clickWebLeadMessageBtn(driver1, webLeadName);
	    String outboundMessage = "Message" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    softPhoneActivityPage.sendMessage(driver1, outboundMessage, 0);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver4);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver4, webLeadName);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver4", false);
	
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"})
	public void verify_no_web_leads_message(){
		
		System.out.println("Test case --verify_no_web_leads_message-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver4");
		driverUsed.put("driver4", true);
		initializeDriverSoftphone("agentDriver");
		driverUsed.put("agentDriver", true);
		
		//enable web leads setting for user
		loginSupport(driver4);
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.enableWebLeadsSetting(driver4);
		userIntelligentDialerTab.saveAcccountSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
		
		//set lead as Web lead
		softPhoneWebLeadsPage.navigateToWebLeadsPage(agentDriver);
		softPhoneWebLeadsPage.noWebLeadMessage(agentDriver);
		
		//disable web leads setting
		loginSupport(driver4);
		dashboard.openManageUsersPage(driver4);
		usersPage.OpenUsersSettings(driver4, CONFIG.getProperty("qa_agent_user_name"), CONFIG.getProperty("qa_agent_user_email"));
		userIntelligentDialerTab.openIntelligentDialerTab(driver4);
		userIntelligentDialerTab.disableWebLeadsSetting(driver4);
		userIntelligentDialerTab.saveAcccountSettings(driver4);
		seleniumBase.closeTab(driver4);
		seleniumBase.switchToTab(driver4, 1);
		reloadSoftphone(agentDriver);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver4", false);
		driverUsed.put("agentDriver", false);	
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_lead_with_long_comments(){
		
		System.out.println("Test case --verify_lead_with_long_comments-- started ");
		
		//updating the driver used
		initializeDriverSoftphone("driver1"); 
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver2"); 
		driverUsed.put("driver2", true);	
	
		String newWebLeadName 	 = CONFIG.getProperty("prod_user_1_name").trim() + " Automation";
		String longDescription	 = "This is the long description for Automation web Lead. It is created to test the long description that appears on Web Lead Page. This should appear properly on hovering mouse on it. If it doesn't appear properly then it should be a bug. User should be able to see full description on hovering over show description icon.";
		
		//Making outbound Call 
		softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
		softPhoneCalling.pickupIncomingCall(driver2);
	  
		// Deleting contact and calling again if
		if(!callScreenPage.isCallerUnkonwn(driver3)) {
				callScreenPage.deleteCallerObject(driver3);
			softPhoneCalling.hangupActiveCall(driver3);
			softPhoneCalling.softphoneAgentCall(driver3, CONFIG.getProperty("prod_user_1_number"));
			softPhoneCalling.pickupIncomingCall(driver2); 
		}
	  
		// add caller as Lead 
		callScreenPage.addCallerAsLead(driver3,CONFIG.getProperty("prod_user_1_name"), company);
		softPhoneSettingsPage.closeErrorMessage(driver3);
	  
		//hanging up with caller 1 
		System.out.println("hanging up with caller 1");
		softPhoneCalling.hangupActiveCall(driver3);
	  
		//Call is removing from softphone
		System.out.println("Call is removing from softphone");
		softPhoneCalling.isCallBackButtonVisible(driver3);
	  
		//open recent call entry
		seleniumBase.idleWait(5);
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
	  
		//set lead as Web lead 
		HashMap<ContactDetailPage.LeadDetailsFields, String>
		webLeadDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
		webLeadDetails.put(ContactDetailPage.LeadDetailsFields.description, longDescription);
		webLeadDetails.put(ContactDetailPage.LeadDetailsFields.checkRingDNAWebLeads, "true"); 
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.updateSalesForceLeadDetails(driver3, webLeadDetails);
		seleniumBase.closeTab(driver3); 
		seleniumBase.switchToTab(driver3, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, newWebLeadName);
		seleniumBase.idleWait(3);
		 
	    //Verify the description
	    File hiddenWebLeadDescImage	= softPhoneWebLeadsPage.getWebLeadsDescriptionImage(driver3, newWebLeadName);
	    File expectedHiddenWebLeadDescImage = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\imageFiles\\HiddenDescription.png"));
	    assertTrue(HelperFunctions.bufferedImagesEqual(hiddenWebLeadDescImage, expectedHiddenWebLeadDescImage));
	   
	    assertEquals(softPhoneWebLeadsPage.getWebLeadsDescriptionToolTip(driver3, newWebLeadName), longDescription);
		
	    //click on web lead call back button and disconnect it before getting connected and verify that web lead is not removed
	    int webLeadsCount = softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver3);
	   /* softPhoneWebLeadsPage.clickWebLeadCallBackBtn(driver3, newWebLeadName);
	    seleniumBase.clickElement(driver3, softPhoneCalling.hangUpButton);
		seleniumBase.idleWait(2);
		assertEquals(softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver3), webLeadsCount - 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, newWebLeadName);*/
		
		//change the visibility of added web lead to owned Records
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "Owned Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1); 
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		reloadSoftphone(driver3);
		
		//verify that users are able to see their own records only
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, newWebLeadName), -1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, newWebLeadName);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, webLeadName), -1);
		
		//change the visibility of added web lead to All Records
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "All Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
		seleniumBase.closeTab(driver1); 
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver3);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		reloadSoftphone(driver1);
	    
		//click on web Lead Call back button and verify that web lead is removed
		softPhoneWebLeadsPage.clickWebLeadCallBackBtn(driver3, newWebLeadName);
		seleniumBase.idleWait(4);
		seleniumBase.clickElement(driver3, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver3, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, newWebLeadName), -1);
		
		//set lead as Web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver3);
		callScreenPage.openCallerDetailPage(driver3);
		contactDetailPage.updateSalesForceLeadDetails(driver3, webLeadDetails);
		seleniumBase.closeTab(driver3); 
		seleniumBase.switchToTab(driver3, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, newWebLeadName);
		seleniumBase.idleWait(3);
		
		//verify that clicking on phone number should make the call and web lead should be removed
		softPhoneWebLeadsPage.clickWebLeadPhoneNumber(driver3, newWebLeadName);
		seleniumBase.idleWait(3);
		seleniumBase.clickElement(driver3, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver3, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, newWebLeadName), -1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
		driverUsed.put("driver6", false);
		driverUsed.put("driver4", false);
		driverUsed.put("driver5", false);		
		
		System.out.println("Test case is pass");
	}
	
	@Test(groups={"MediumPriority", "Product Sanity"}, dependsOnMethods = "lead_in_web_leads_section")
	public void verify_web_leads_remove_on_call_connect()
	{
		System.out.println("Test case --verify_web_leads_remove_on_call_connect-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver6");
		driverUsed.put("driver6", true);
		
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.enableclickToCallSetting(driver1);
		
		//search the web lead in contact search and make a call to it and verify that web lead is removed
	    int webLeadsCount = softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver1);
		softPhoneContactsPage.salesforceSearchAndCall(driver1, webLeadName, SoftPhoneContactsPage.searchTypes.Leads.toString());
		seleniumBase.idleWait(4);
		seleniumBase.clickElement(driver1, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver1, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), -1);
		
		//set lead as Web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
		seleniumBase.closeTab(driver1); 
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		seleniumBase.idleWait(3);
		
		//Navigate to web lead section and make an outbound call and verify hot lead is removed
		softPhoneCalling.softphoneAgentCall(driver1, CONFIG.getProperty("prod_user_3_number"));
		seleniumBase.idleWait(4);
		seleniumBase.clickElement(driver1, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver1, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), -1);
		
		//set lead as Web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
		seleniumBase.closeTab(driver1); 
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		seleniumBase.idleWait(3);
		
		//open web lead entry from call history page and make a call to it and verify that web lead is removed
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		softPhoneCalling.clickCallBackButton(driver1);
		seleniumBase.idleWait(2);
		seleniumBase.clickElement(driver1, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver1, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), -1);

		//set lead as Web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		//check that web lead checkbox is unchecked
		assertFalse(contactDetailPage.isLeadWebLead(driver1));
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);

		//click the contact phone link to call
		contactDetailPage.clickContactPhoneLinkDetails(driver1, HelperFunctions.getNumberInRDNAFormat(webLeadPhone));
		
		// Switch to extension and verify that web lead is removed
		sfSearchPage.switchToExtension(driver1);
		seleniumBase.clickElement(driver1, softPhoneCalling.hangUpButton);
		softPhoneWebLeadsPage.verifyWebLeadsNotificationCounts(driver1, webLeadsCount-1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, webLeadName), -1);
	 	seleniumBase.closeTab(driver1);
	 	seleniumBase.switchToTab(driver1, 1);
		
	 	//set lead as Web lead again
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
		seleniumBase.closeTab(driver1); 
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		seleniumBase.idleWait(3);
		
		softPhoneSettingsPage.clickSettingIcon(driver1);
		softPhoneSettingsPage.disableclickToCallSetting(driver1);
		
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver6", false);
	
		System.out.println("Test case is pass");
	}
	
	//Verify hot lead reflected immediately on changing owner without need to navigate
	//Verify that changes made to Lead from SFDC as per Hot Lead rule defined reflected instantaneously without need to navigate
	@Test(groups={ "MediumPriority"})
	public void verify_hot_leads_rule_modify_lead()
	{
		System.out.println("Test case --verify_hot_leads_rule_modify_lead-- started ");

		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver3");
		driverUsed.put("driver3", true);
		initializeDriverSoftphone("driver5");
		driverUsed.put("driver5", true);

		String ringdnaWebLeadName  = CONFIG.getProperty("prod_user_3_name") + " Automation";
		String webLeadRuleField = "City";
		String city = "Delhi";
		String userTeam	 = CONFIG.get("qa_group_3_name").toString().trim();
		
		// Opening support app
		loginSupport(driver1);

		// Create a web lead rule on supports tab
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, webLeadRuleField, city, userTeam);
		accountIntelligentDialerTab.createWebLeadRule(driver1, webLeadRuleField, city, userTeam, "All Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
	    //Making outbound Call
  		softPhoneContactsPage.deleteAndAddLead(driver1, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));
  	
  		//get old WebLeads Count
  		int oldWebLeadsCount = softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver1);
  		
  		//open recent call entry
  		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);  
  		
  		//set lead as Web lead
  		HashMap<ContactDetailPage.LeadDetailsFields, String> webleadForRuleDetails = new HashMap<ContactDetailPage.LeadDetailsFields, String>();
  		callScreenPage.openCallerDetailPage(driver1);
  		webleadForRuleDetails.put(ContactDetailPage.LeadDetailsFields.checkRingDNAWebLeads, "true");
  		contactDetailPage.updateSalesForceLeadDetails(driver1, webleadForRuleDetails);
  	    seleniumBase.switchToTab(driver1, 1);
		
		//Verify that web lead is appearing to all members
  	    softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
  	    softPhoneWebLeadsPage.verifyWebLeadExists(driver1, ringdnaWebLeadName);
		softPhoneWebLeadsPage.VerifyWebLeadsNotificationCoutntsIncreased(driver1, oldWebLeadsCount);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver3);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, ringdnaWebLeadName);
		
		//Update the web lead details again	
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		webleadForRuleDetails.remove(ContactDetailPage.LeadDetailsFields.checkRingDNAWebLeads);
		webleadForRuleDetails.put(ContactDetailPage.LeadDetailsFields.leadCity, city);
		contactDetailPage.updateSalesForceLeadDetails(driver1, webleadForRuleDetails);
		seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);
		
		//Verify that web leads is not appearing to other team's member
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, ringdnaWebLeadName), -1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, ringdnaWebLeadName);
		
		//deleting web rule and changing the visibility of records to Owned records
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		dashboard.clickAccountsLink(driver1);
		accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
		accountIntelligentDialerTab.deleteWebLeadRule(driver1, webLeadRuleField, city, userTeam);
		accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "Owned Records");
		accountIntelligentDialerTab.saveAcccountSettings(driver1);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    
		//Verify that web leads is not appearing to other member
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver3, ringdnaWebLeadName), -1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver1, ringdnaWebLeadName);
		
		//open recent call entry
  		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);  
  		
  		//set lead as Web lead
  		callScreenPage.openCallerDetailPage(driver1);
  		seleniumBase.switchToTab(driver1, 1);
  		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
  		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
  		contactDetailPage.setLeadOwner(driver1, CONFIG.getProperty("qa_user_3_name"));
  		seleniumBase.closeTab(driver1);
  	    seleniumBase.switchToTab(driver1, 1);
  	    
		//Verify that web leads is not appearing to the last lead owner
		assertEquals(softPhoneWebLeadsPage.getWebLeadIndex(driver1, ringdnaWebLeadName), -1);
		softPhoneWebLeadsPage.verifyWebLeadExists(driver3, ringdnaWebLeadName);
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver3", false);
		driverUsed.put("driver4", false);
	
		System.out.println("Test case is pass");
	}
	
	@AfterMethod(groups={"Regression", "MediumPriority", "Product Sanity"}, dependsOnMethods = "resetSetupDefault")
	public void setDefaultRuleVisibility(ITestResult result){
		
		if(result.getName().equals("verify_web_leads_rule") || result.getName().equals("verify_lead_with_long_comments")){
			//updating the driver used
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			initializeDriverSoftphone("driver4");
			driverUsed.put("driver4", true);
			initializeDriverSoftphone("driver3");
			driverUsed.put("driver3", true);
			
			//set All Records visibility for Web lead rules
			loginSupport(driver1);
			dashboard.clickAccountsLink(driver1);
			accountIntelligentDialerTab.openIntelligentDialerTab(driver1);
			accountIntelligentDialerTab.selectWebLeadRuleVisibility(driver1, "All Records");
			accountIntelligentDialerTab.deleteWebLeadRule(driver1, fieldName, fieldValue, teamName);
			accountIntelligentDialerTab.deleteWebLeadRule(driver1, "Web Lead", "true", teamName);
			accountIntelligentDialerTab.saveAcccountSettings(driver1);
			seleniumBase.closeTab(driver1); 
			seleniumBase.switchToTab(driver1, 1);
			reloadSoftphone(driver1);
			reloadSoftphone(driver3);	
			reloadSoftphone(driver4);	
		    
			//Setting driver used to false as this test case is pass
			driverUsed.put("driver1", false);
			driverUsed.put("driver3", false);
			driverUsed.put("driver4", false);
		}else if((result.getName().equals("verify_custom_field_web_leads_rule")|| result.getName().equals("verify_web_leads_remove_on_call_connect")) && result.getStatus() == 2) {
			initializeDriverSoftphone("driver1");
			driverUsed.put("driver1", true);
			
			softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		    callScreenPage.openCallerDetailPage(driver1);
			seleniumBase.switchToTab(driver1, 1);
			softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
		    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
			contactDetailPage.clickContactLeadEditButton(driver1);
			contactDetailPage.checkRingDNAWebLeadsCheckBox(driver1);
			contactDetailPage.clickSaveBtn(driver1);
		    seleniumBase.closeTab(driver1);
		    seleniumBase.switchToTab(driver1, 1);
		    
		    driverUsed.put("driver1", false);
		}
		
		//handling special case for depends on methods, so that dependent method doesn't skip in first failure
		if (result.getName().equals("lead_in_web_leads_section")) {	
			result.getTestContext().getSkippedTests().removeResult(result.getMethod());
		}
	}
	
	@AfterClass(groups={"Regression", "MediumPriority", "Product Sanity"})
	public void removeWebLead(){
		//updating the driver used
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		//open recent call entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
		callScreenPage.openCallerDetailPage(driver1);
		
		//remove lead from web leads section
	    leadDetails.clear();
	    leadDetails.put(ContactDetailPage.LeadDetailsFields.uncheckRingDNAWebLeads, "true");
	    contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);	
	    
		//Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
	}
	
	
	public void createWebLead() {
		
		
		//Making outbound Call
		softPhoneContactsPage.deleteAndAddLead(driver1, CONFIG.getProperty("prod_user_2_number"), CONFIG.getProperty("prod_user_2_name"));
	
		//get old WebLeads Count
		oldWebLeadsCount = softPhoneWebLeadsPage.getWebLeadsNotificationCounts(driver1);
		
		//open recent call entry
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);  
		
		//set lead as Web lead
		leadDetails.put(ContactDetailPage.LeadDetailsFields.leadCity, fieldValue);
		callScreenPage.openCallerDetailPage(driver1);
		seleniumBase.switchToTab(driver1, 1);
		softPhoneWebLeadsPage.navigateToWebLeadsPage(driver1);
	    seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		contactDetailPage.updateSalesForceLeadDetails(driver1, leadDetails);
	    seleniumBase.closeTab(driver1);
	    seleniumBase.switchToTab(driver1, 1);
	    softPhoneWebLeadsPage.verifyWebLeadExists(driver1, webLeadName);
		softPhoneWebLeadsPage.VerifyWebLeadsNotificationCoutntsIncreased(driver1, oldWebLeadsCount);	  
	}
}