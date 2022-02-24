package softphone.cases.softPhoneFunctional.callTools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import softphone.base.SoftphoneBase;
import softphone.source.callTools.SoftPhoneNewEvent;
import softphone.source.callTools.SoftPhoneNewEvent.eventFields;

/**
 * @author Vishal
 *
 */
public class CallToolsNewEvent extends SoftphoneBase {

	@BeforeClass(groups = { "Regression", "Sanity", "QuickSanity", "Product Sanity" })
	public void beforeClass() {

		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		initializeDriverSoftphone("driver2");
		driverUsed.put("driver2", true);

		// add number as contact
	    String contactNumber = CONFIG.getProperty("prod_user_1_number");	  
		softPhoneContactsPage.deleteAndAddContact(driver1, contactNumber, CONFIG.getProperty("prod_user_1_name"));

		// Setting driver used to false as this test case is pass
		driverUsed.put("driver1", false);
		driverUsed.put("driver2", false);
	}

	@Test(groups = { "Regression" })
	public void call_tools_new_event_defaults() {

		System.out.println("Test Case Started-Call Tool Event, Default UI");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);

		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Task Icon and Open Event
		softPhoneNewEvent.clickEventTab(driver1);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, 1);
		c.set(Calendar.MINUTE, 0);

		// Start Date as current Date and Start Time as Current Hour +1 and Minutes as 0
		String startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		String endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		c.add(Calendar.HOUR, 1);
		String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());

		// All Day check box is by default unchecked
		assertEquals(softPhoneNewEvent.getAllDayStatus(driver1), false);

		// Default Start Date and End Date should be current Date
		assertEquals(softPhoneNewEvent.getStartDate(driver1), startDate);
		assertEquals(softPhoneNewEvent.getEndDate(driver1), endDate);

		// Default Start Time should be next hour then current hour with minutes as 0
		assertEquals(softPhoneNewEvent.getStartTime(driver1), startTime);

		// Default End Time should be next hour then Start time hour with minutes as 0
		assertEquals(softPhoneNewEvent.getEndTime(driver1), endTime);

		// Reminder checkbox is by default selected
		assertEquals(softPhoneNewEvent.getReminderStatus(driver1), true);

		// Default Reminder should be 15 Minutes
		assertEquals(softPhoneNewEvent.getReminderText(driver1), "15 minutes");

		// Validation on Subject, When Enabled

		softPhoneNewEvent.eventSubjectRequired(driver1);

		// When All Days checked, Start/End Time and Reminder option gets disabled
		softPhoneNewEvent.enableAllDayStatus(driver1);

		// Uncheck All Day checkbox again
		softPhoneNewEvent.clickAllDayCheckBox(driver1);

		// Set input parameters before Saving
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);

		// Save Event
		softPhoneNewEvent.saveEvent(driver1);

		// Check Created Date, should be todays date
		String createdDate = new SimpleDateFormat("MM/dd/yy").format(c.getTime());
		assertEquals(softPhoneActivityPage.getEventCreationDate(driver1, eventSubject).contains(createdDate), true);

		// Check Future Event icon on All Activity Tab
		softPhoneActivityPage.isEventFutureIconVisible(driver1, eventSubject);

		// Open Event In Salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, eventSubject);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);

		// Verify Subject on SFDC side
		assertEquals(sfTaskDetailPage.getSubject(driver1), eventSubject);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Create event with past date check Past Event Icon
		c.add(Calendar.DATE, -1);
		startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		eventSubject = "Past Event" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		softPhoneNewEvent.addNewEventWithAllFields(driver1, eventDetails);
		softPhoneActivityPage.isEventPastIconVisible(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void call_tools_event_date_validations() {

		System.out.println("Test Case Started-Call Tool Event->Default Validations");

		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
	
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Event Tab
		softPhoneNewEvent.clickEventTab(driver1);

		// Set Start Date to next day date and Verify that same should be populated in EndDate
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String startDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());

		// Set Start Date as value of startDate variable and check value of EndDate, it should exactly same
		softPhoneNewEvent.enterStartDate(driver1, startDate);
		assertEquals(softPhoneNewEvent.getStartDate(driver1), softPhoneNewEvent.getEndDate(driver1));

		// Set Start Time to more then 2 hrs then current Start Time hr
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		softPhoneNewEvent.enterStartTime(driver1, startTime);
		softPhoneNewEvent.clickDescription(driver1);

		// If Start and End Date are same, End Time should be Start time + 1 hr
		if (softPhoneNewEvent.getStartDate(driver1).trim().equals(softPhoneNewEvent.getEndDate(driver1).trim())) {
			c.add(Calendar.HOUR, 1);
			String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
			assertEquals(softPhoneNewEvent.getEndTime(driver1), endTime);
		}
		
		driverUsed.put("driver1", false);
		System.out.println("Test Case is Passed");
	}

	@Test(groups = { "Regression", "Product Sanity" })
	public void call_tools_new_event_with_all_fields() {
		System.out.println("Test Case Started-Call Tool Event->New Event Creation and Update");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Add Event with all fields
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		// Current Date
		Calendar c = Calendar.getInstance();

		// Start Date is Next Day date
		c.add(Calendar.DATE, 1);
		String startDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		String expectedStartDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());

		// End Date is Day after tomorrow date
		c.add(Calendar.DATE, 2);
		String endDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		String expectedEndDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());

		// Start Time is current hour + 2 hrs
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartTime, startTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndTime, endTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Reminder, SoftPhoneNewEvent.reminderTime.twodays.toString());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.addNewEventWithAllFields(driver1, eventDetails);

		// Open Event activity in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, eventSubject);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);

		// Verify all data correctly persists in Salesforce
		assertEquals(sfTaskDetailPage.getSubject(driver1), eventSubject);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(expectedStartDate.trim()), true);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startTime.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(expectedEndDate.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endTime.trim()), true);
		assertEquals(sfTaskDetailPage.getTaskReminderTime(driver1).trim().contains(eventDetails.get(eventFields.Reminder)), true);
		assertEquals(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription), true);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Edit Event  now
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);

		// Set new data which need to be provided while updating event
		eventSubject = "Updated_Subject" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		// Current Date
		c = Calendar.getInstance();

		// Start Date is yesterday
		c.add(Calendar.DATE, -1);
		startDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		expectedStartDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		

		// End Date is Day after tomorrow date
		c.add(Calendar.DATE, 8);
		endDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		expectedEndDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());

		// Start Time is current hour + 4 hrs
		c.add(Calendar.HOUR, 4);
		c.set(Calendar.MINUTE, 0);
		startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());

		// End Time Start Hr + 6 hrs
		c.add(Calendar.HOUR, 6);
		c.set(Calendar.MINUTE, 0);
		endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		eventDescription = "Updated_Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		System.out.println(eventSubject);
		System.out.println(eventDescription);
		softPhoneNewEvent.enterStartDate(driver1, startDate);
		softPhoneNewEvent.enterStartDate(driver1, startDate);
		softPhoneNewEvent.enterEventSubject(driver1, eventSubject);
		softPhoneNewEvent.enterStartTime(driver1, startTime);
		softPhoneNewEvent.enterEndTime(driver1, endTime);
		softPhoneNewEvent.enterReminder(driver1, SoftPhoneNewEvent.reminderTime.twodays.toString());
		softPhoneNewEvent.enterDescription(driver1, eventDescription);
		softPhoneNewEvent.enterEndDate(driver1, endDate);
		softPhoneNewEvent.enterEndDate(driver1, endDate);

		// Save
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);
		seleniumBase.idleWait(2);

		// Open Event activity in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, eventSubject);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);

		// Verify all updated data correctly persists in Salesforce
		assertEquals(sfTaskDetailPage.getSubject(driver1), eventSubject);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(expectedStartDate.trim()), true);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startTime.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(expectedEndDate.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endTime.trim()), true);
		assertEquals(sfTaskDetailPage.getTaskReminderTime(driver1).trim().contains(eventDetails.get(eventFields.Reminder)), true);
		assertEquals(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription), true);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		driverUsed.put("driver1", false);
		System.out.println("Test Case is Passed");
	}

	//Verify ReadyOnly fields should not be displayed on New Event when user clicks on "Complete & New Task"  already created New Event.
	@Test(groups = { "Regression" })
	public void call_tools_save_event_with_save_new() {
		System.out.println("Test Case Started-Call Tool Event->Create Event with Save New");

		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
				
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Add Event with all fields
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		// Current Date
		Calendar c = Calendar.getInstance();

		// Start Date is Next Day date
		c.add(Calendar.DATE, 1);
		String startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());

		// End Date is Day after tomorrow date
		c.add(Calendar.DATE, 2);
		String endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());

		// Start Time is current hour + 2 hrs
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		c.add(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartTime, startTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndTime, endTime);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Reminder, SoftPhoneNewEvent.reminderTime.twodays.toString());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.addNewEventWithAllFields(driver1, eventDetails);

		// Open Event activity in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, eventSubject);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);

		// Verify all data correctly persists in Salesforce
		assertEquals(sfTaskDetailPage.getSubject(driver1), eventSubject);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startDate.trim()), true);
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startTime.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endDate.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endTime.trim()), true);
		assertEquals(sfTaskDetailPage.getTaskReminderTime(driver1).trim().contains(eventDetails.get(eventFields.Reminder)), true);
		assertEquals(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription), true);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);

		// Edit Task now
		softPhoneActivityPage.clickTaskEditButton(driver1, eventSubject);

		// Save
		softPhoneActivityPage.saveAndOpenNewTaskWindow(driver1);
		softPhoneNewEvent.clickCancel(driver1);
		
		//verify that task is appearing under All Tasks tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test Case is Passed");
	}

	@Test(groups = { "Regression" })
	public void call_tools_new_event_allday() {

		System.out.println("Test Case Started-Call Tool Event->Event with All Day check box");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Event Tab
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets
		// disabled
		softPhoneNewEvent.enableAllDayStatus(driver1);
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		// Current Date
		Calendar c = Calendar.getInstance();

		// Start Date is Next Day date
		c.add(Calendar.DATE, 1);
		String startDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		String verifyStartDate = new SimpleDateFormat("MM/dd/yy").format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);

		// Check Event Date in Container, no time should be there
		assertEquals(softPhoneActivityPage.getEventDate(driver1, eventSubject), "EVENT: " + verifyStartDate);

		//Check Event Owner in Container, Should be logged in user
		assertEquals(softPhoneActivityPage.getEventOwner(driver1, eventSubject), CONFIG.getProperty("qa_user_1_name"));

		//Check Event Description in Container, Should be logged in user
		assertEquals(softPhoneActivityPage.getEventDescription(driver1, eventSubject),eventDescription);

		// Delete Task
		softPhoneActivityPage.deleteTask(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}

	@Test(groups = { "Regression" })
	public void call_tools_save_new_event() {

		System.out.println("Test Case Started-Call Tool Event->Save New Event");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Event Tab
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets
		// disabled
		softPhoneNewEvent.enableAllDayStatus(driver1);
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		// Current Date
		Calendar c = Calendar.getInstance();

		// Set Start Date as Next Day date
		c.add(Calendar.DATE, 1);
		String startDate = new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);

		// Click To Edit Task
		softPhoneActivityPage.editTask(driver1, eventSubject);

		// Click Save and Open new window
		softPhoneActivityPage.saveAndOpenNewTaskWindow(driver1);

		//Check that Event Subject get copied in new task window
		startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		c.add(Calendar.HOUR, 1);
		c.set(Calendar.MINUTE, 0);
		String startTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		c.add(Calendar.HOUR, 1);
		String endTime = new SimpleDateFormat(CONFIG.getProperty("softphone_reminder_time_format")).format(c.getTime());
		assertEquals(softPhoneActivityPage.getTaskSubject(driver1),eventSubject);
		assertEquals(softPhoneActivityPage.getEventStartDate(driver1),startDate);
		assertEquals(softPhoneActivityPage.getEventEndDate(driver1),startDate);
		assertEquals(softPhoneActivityPage.getEventStartTime(driver1),startTime);
		assertEquals(softPhoneActivityPage.getEventEndTime(driver1),endTime);
		assertEquals(softPhoneNewEvent.getReminderText(driver1),"15 minutes");
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_tools_event_disable_subject() {

		System.out.println("Test Case Started-call_tools_event_disable_subject");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// navigating to support page
		loginSupport(driver1);
		dashboard.clickAccountsLink(driver1);
		System.out.println("Account editor is opened ");

		// disabling Task due date option
		accountSalesforceTab.openSalesforceTab(driver1);
		accountSalesforceTab.navigateToEventFieldsSection(driver1);
		accountSalesforceTab.disableTaskSubjectRequiredSetting(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Task Icon
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets
		// disabled
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

		// Current Date
		Calendar c = Calendar.getInstance();

		// Set Start Date as Next Day date
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);
		
		// Start Date is Next Day date
		String startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		String endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
	  
		// Open task in salesforce from All Activity feeds section
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    int index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, eventDescription);
		assertTrue(index >= 0);
		softPhoneActivityPage.openNoSubjectEventInSalesforce(driver1, index);
	    
	    // Check task Reminder Time
 		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startDate.trim()), true);
 		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endDate.trim()), true);
 		assertEquals(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription), true);
 		seleniumBase.closeTab(driver1);
 		seleniumBase.switchToTab(driver1, 1);
	    
	    c.add(Calendar.DATE, 1);
	    HashMap<SoftPhoneNewEvent.eventFields, String> eventUpdates = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		eventUpdates.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventUpdates.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventUpdates.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
	    
		softPhoneActivityPage.editNoSubjectTask(driver1, index);
		softPhoneNewEvent.editEventWithAllFields(driver1, eventUpdates);
		softPhoneActivityPage.clickTaskEditSaveButton(driver1);

		// Open task in salesforce from All Activity feeds section
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, eventDescription);
		assertTrue(index >= 0);
		softPhoneActivityPage.openNoSubjectEventInSalesforce(driver1, index);
		
		// Check task Reminder Time
		assertEquals(sfTaskDetailPage.getStartDate(driver1).trim().contains(startDate.trim()), true);
		assertEquals(sfTaskDetailPage.getEndDate(driver1).trim().contains(endDate.trim()), true);
		assertEquals(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription), true);
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		//Deleting task from receiver
	    softPhoneActivityPage.navigateToAllActivityTab(driver1);
	    index = softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, eventDescription);
	    softPhoneActivityPage.deleteAllNoSubjectTask(driver1);
	    
	    //verify that task has been deleted
	    assertEquals(softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, eventDescription), -1);
	    
		callScreenPage.clickCallerName(driver1);
		callScreenPage.switchToSFTab(driver1, callScreenPage.getTabCount(driver1));
		callScreenPage.reloadSoftphone(driver1);
	    contactDetailPage.verifyRecentActivityNotPresent(driver1, "_");
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
	    //no subject activity should not be there on all history tab after navigating 
	    softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);
	    assertEquals(softPhoneActivityPage.getNoSubjectActivityIndexUsingComments(driver1, eventDescription), -1);
		
		// navigating to support page
		seleniumBase.switchToTab(driver1, seleniumBase.getTabCount(driver1));
		accountSalesforceTab.enableTaskSubjectRequiredSetting(driver1);	
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		reloadSoftphone(driver1);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_tools_new_event_green_border() {

		System.out.println("Test Case Started-Call Tool Event->Save New Event");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Task Icon
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets disabled
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);

		softPhoneActivityPage.verifyOpenTasksGreenBorder(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	
	@Test(groups = { "MediumPriority" })
	public void call_tools_new_event_black_border() {

		System.out.println("Test Case Started call_tools_new_event_black_border");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Task Icon
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets disabled
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, -1);
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		String endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);

		softPhoneActivityPage.verifyOpenTasksNoBorder(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
	
	//Verify agent able to create new event for contact with script in subject
	@Test(groups = { "Regression"})
	public void call_tools_new_event_with_scripts() {
		System.out.println("Test case --call_tools_new_event_with_scripts()-- started ");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Add Event with all fields
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		
		String eventSubject = "<script>alert('Test!!!!" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "')</script>";
		String eventDescription = "<script>alert('Test!!!!')</script>";

		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.addNewEventWithAllFields(driver1, eventDetails);
		
		//verify that task is appearing under All Tasks tab
		softPhoneActivityPage.verifyTaskInAllTaskTab(driver1, eventSubject);

		// Open Event activity in salesforce
		softPhoneActivityPage.openTaskInSalesforce(driver1, eventSubject);
		sfTaskDetailPage.closeLightningDialogueBox(driver1);

		// Verify all data correctly persists in Salesforce
		assertEquals(sfTaskDetailPage.getSubject(driver1), eventSubject);
		assertTrue(sfTaskDetailPage.getDescription(driver1).trim().contains(eventDescription));
		seleniumBase.closeTab(driver1);
		seleniumBase.switchToTab(driver1, 1);
		
		driverUsed.put("driver1", false);
		System.out.println("Test Case is Passed");
	}
	
	//Verify agent able to create new event for existing lead/contact using new event icon
	@Test(groups = { "MediumPriority" })
	public void call_tools_new_event_existing_caller() {

		System.out.println("Test Case Started call_tools_new_event_existing_caller");
		
		// updating the driver used 
		initializeDriverSoftphone("driver1");
		driverUsed.put("driver1", true);
		
		// open Recent call
		softphoneCallHistoryPage.openRecentContactCallHistoryEntry(driver1);

		// Click Task Icon
		softPhoneNewEvent.clickEventTab(driver1);

		// When All Days checked, Start/End Time and Reminder option gets disabled
		HashMap<SoftPhoneNewEvent.eventFields, String> eventDetails = new HashMap<SoftPhoneNewEvent.eventFields, String>();
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, -1);
		String eventSubject = "Subject" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String eventDescription = "Description" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String startDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		String endDate = new SimpleDateFormat("M/d/yyyy").format(c.getTime());
		eventDetails.put(SoftPhoneNewEvent.eventFields.StartDate, startDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.EndDate, endDate);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Subject, eventSubject);
		eventDetails.put(SoftPhoneNewEvent.eventFields.Description, eventDescription);
		softPhoneNewEvent.enterEventDetails(driver1, eventDetails);
		softPhoneNewEvent.saveEvent(driver1);

		softPhoneActivityPage.verifyOpenTasksNoBorder(driver1, eventSubject);
		
		driverUsed.put("driver1", false);
		System.out.println("Test case is pass");
	}
}