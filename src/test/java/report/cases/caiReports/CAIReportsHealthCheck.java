package report.cases.caiReports;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import report.base.ReportBase;
import report.source.caiReportPage.CAIReportPage;
import report.source.caiReportPage.CAIReportPage.CreatedDateFilter;
import support.source.commonpages.Dashboard;
import support.source.conversationAI.DashBoardConversationAI;

/**
 * @author Abhishek T
 *
 */
public class CAIReportsHealthCheck extends ReportBase {

	Dashboard dashboard = new Dashboard();
	DashBoardConversationAI dashBoardCAI = new DashBoardConversationAI();
	CAIReportPage caiReportPage = new CAIReportPage();

	CreatedDateFilter filterValue1;
	CreatedDateFilter filterValue2;
	CreatedDateFilter filterValue3;
	CreatedDateFilter filterValue4;
	
	String timeRange = System.getProperty("jenkinsTimeRange");
	
	@AfterMethod(groups = { "Regression" }, dependsOnMethods = "resetSetupDefault")
	public void runAfterMethod(ITestResult result) {

		if(Strings.isNullOrEmpty(timeRange)) {
			if (result.getStatus() == 2) {
				result.setThrowable(new Throwable("Test script " + result.getName()
						+ " has failed. Skipping all other cases, please verify them manually."));
				Assert.fail("Test script " + result.getName()
						+ " has failed. Skipping all other cases, please verify them manually.");
			}
		}
	}

	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {

		if (Strings.isNullOrEmpty(timeRange)) {
			filterValue1 = CreatedDateFilter.Week;
			filterValue2 = CreatedDateFilter.Month;
			filterValue3 = CreatedDateFilter.Last90days;
			filterValue4 = CreatedDateFilter.Annual;
		} else {
			switch (timeRange) {
			case "Weekly":
				filterValue1 = CreatedDateFilter.Week;
				filterValue2 = CreatedDateFilter.Week;
				filterValue3 = CreatedDateFilter.Week;
				filterValue4 = CreatedDateFilter.Week;
				break;
			case "Annually":
				filterValue1 = CreatedDateFilter.Annual;
				filterValue2 = CreatedDateFilter.Annual;
				filterValue3 = CreatedDateFilter.Annual;
				filterValue4 = CreatedDateFilter.Annual;
				break;
			case "Monthly":
				filterValue1 = CreatedDateFilter.Month;
				filterValue2 = CreatedDateFilter.Month;
				filterValue3 = CreatedDateFilter.Month;
				filterValue4 = CreatedDateFilter.Month;
				break;
			case "Quarterly":
				filterValue1 = CreatedDateFilter.Last90days;
				filterValue2 = CreatedDateFilter.Last90days;
				filterValue3 = CreatedDateFilter.Last90days;
				filterValue4 = CreatedDateFilter.Last90days;
				break;
			default:
				break;
			}
		}
	}

	@Test(groups = { "Regression" }, priority = 1)
	public void verify_talking_vs_listening_reports() {
		System.out.println("Test case --verify_talking_vs_listening_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkingVListening(supportDriver);

		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTalkingVsListeningData(supportDriver);
		}

		// verifying data for parameterized build
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTalkingVsListeningData(supportDriver);
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_talking_vs_listening_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 2)
	public void verify_interruptions_reports() {
		System.out.println("Test case --verify_interruptions_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToInterruptions(supportDriver);

		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyInterruptionsData(supportDriver);
		}
		// verifying data for parameterized build
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyInterruptionsData(supportDriver);	
		}
	
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_interruptions_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 3)
	public void verify_agent_monologues_reports() {
		System.out.println("Test case --verify_agent_monologues_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToAgentMonologues(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyAgentMonologuesData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyAgentMonologuesData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_agent_monologues_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 4)
	public void verify_customer_monologues_reports() {
		System.out.println("Test case --verify_customer_monologues_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCustomerMonologues(supportDriver);
		
		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCustomerMonologuesData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCustomerMonologuesData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_customer_monologues_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 5)
	public void verify_silence_reports() {
		System.out.println("Test case --verify_silence_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToSilence(supportDriver);
		
		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifySilenceData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifySilenceData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_silence_reports-- passed ");
	}

	//@Test(groups = { "Regression" }, priority = 6)
	public void verify_interactions_reports() {
		System.out.println("Test case --verify_interactions_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToInteractions(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyInteractionsData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyInteractionsData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_interactions_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 7)
	public void verify_calling_productivity_reports() {
		System.out.println("Test case --verify_calling_productivity_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCallingProductivity(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCallingProductivityData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCallingProductivityData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_calling_productivity_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 8)
	public void verify_talk_time_reports() {
		System.out.println("Test case --verify_talk_time_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTalkTime(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTalkTimeData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTalkTimeData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_talk_time_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 9)
	public void verify_recorded_call_percentage_reports() {
		System.out.println("Test case --verify_recorded_call_percentage_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToRecordedCallPercentage(supportDriver);
		
		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyRecordedCallPercentageData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			supportDriver.switchTo().defaultContent();
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyRecordedCallPercentageData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_recorded_call_percentage_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 10)
	public void verify_time_of_day_reports() {
		System.out.println("Test case --verify_time_of_day_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToTimeOfDay(supportDriver);
		
		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTimeOfDayData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyTimeOfDayData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_time_of_day_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 11)
	public void verify_dispositions_reports() {
		System.out.println("Test case --verify_dispositions_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToDispositions(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyDispositionsData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyDispositionsData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_dispositions_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 12)
	public void verify_call_directions_reports() {
		System.out.println("Test case --verify_call_directions_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCallDirections(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCallDirectionsData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCallDirectionsData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_directions_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 13)
	public void verify_local_presence_reports() {
		System.out.println("Test case --verify_local_presence_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToLocalPresence(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyLocalPresenceData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyLocalPresenceData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_local_presence_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 14)
	public void verify_keyword_impact_reports() {
		System.out.println("Test case --verify_keyword_impact_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToKeywordImpact(supportDriver);
		
		// verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			//caiReportPage.verifyDefaultTimeFilter(supportDriver);
			//caiReportPage.switchToReportFrame(supportDriver);
			//caiReportPage.verifyKeywordImpactData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyKeywordImpactData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_keyword_impact_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 15)
	public void verify_coaching_volume_reports() {
		System.out.println("Test case --verify_coaching_volume_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingVolume(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
//			caiReportPage.verifyDefaultTimeFilter(supportDriver);
//			caiReportPage.switchToReportFrame(supportDriver);
//			caiReportPage.verifyCoachingVolumeData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCoachingVolumeData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_coaching_volume_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 16)
	public void verify_coaching_recieved_reports() {
		System.out.println("Test case --verify_coaching_recieved_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingReceived(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCoachingReceivedData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCoachingReceivedData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_coaching_recieved_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 17)
	public void verify_coaching_given_reports() {
		System.out.println("Test case --verify_coaching_given_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToCoachingGiven(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCoachingGivenData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyCoachingGivenData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_coaching_given_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 18)
	public void verify_peer_coaching_reports() {
		System.out.println("Test case --verify_peer_coaching_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToPeerCoaching(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyPeerCoachingData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifyPeerCoachingData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_peer_coaching_reports-- passed ");
	}

	@Test(groups = { "Regression" }, priority = 19)
	public void verify_self_review_reports() {
		System.out.println("Test case --verify_self_review_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		// navigate to cai reports page if not active
		dashboard.switchToTab(supportDriver, 2);
		dashBoardCAI.navigateToInsightsSection(supportDriver);
		caiReportPage.navigateToSelfReview(supportDriver);
		
		//verifying for default filter
		if (Strings.isNullOrEmpty(timeRange)) {
			caiReportPage.verifyDefaultTimeFilter(supportDriver);
			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifySelfReviewData(supportDriver);
		}
		
		// verifying data for parameterized build 
		else {
			caiReportPage.selectCreatedDateFilter(supportDriver, filterValue1);
			caiReportPage.clickRefreshButton(supportDriver);

			caiReportPage.switchToReportFrame(supportDriver);
			caiReportPage.verifySelfReviewData(supportDriver);	
		}

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_self_review_reports-- passed ");
	}
}
