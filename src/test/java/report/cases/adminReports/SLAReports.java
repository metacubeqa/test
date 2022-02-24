package report.cases.adminReports;

import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.commonpages.Dashboard;

public class SLAReports extends ReportBase{
	
	Dashboard dashboard = new Dashboard();
	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileDaily   = "SLA-Daily-report-20";
	private static final String reportFileWeekly  = "SLA-Weekly-report-20";
	private static final String reportFileMonthly = "SLA-Monthly-report-20";
	private static final String endFileName = ".csv";
	
	@Test(groups = { "Regression", "ExcludeRingDNAProd" })
	public void verify_user_able_to_download_sla_reports_with_availability() {
		
		System.out.println("Test case --verify_user_able_to_download_sla_reports_with_availability-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		
		//Daily
		reportCommonPage.verifyDefaultFiltersSLA(supportDriver);
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileDaily, endFileName);
		
		//Weekly
		reportCommonPage.verifyEndDateNotBeforeStartDateWeekly(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		
		reportCommonPage.selectStartEndDateSLAWeekly(supportDriver);
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileWeekly, endFileName);

		//Monthly
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		reportCommonPage.verifyEndDateNotBeforeStartDateMonthly(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		reportCommonPage.selectStartEndDateSLAMonthly(supportDriver);
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileMonthly, endFileName);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_download_sla_reports_with_availability-- passed ");
	}
	
	@Test(groups = { "Regression", "ExcludeRingDNAProd" })
	public void verify_user_able_to_download_sla_reports_without_availability() {
		
		System.out.println("Test case --verify_user_able_to_download_sla_reports_without_availability-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		
		reportCommonPage.verifyDefaultFiltersSLA(supportDriver);
		reportCommonPage.unCheckAvailability(supportDriver);

		//Daily
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileDaily, endFileName);
		
		//Weekly
		reportCommonPage.verifyEndDateNotBeforeStartDateWeekly(supportDriver);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		reportCommonPage.unCheckAvailability(supportDriver);
		reportCommonPage.selectStartEndDateSLAWeekly(supportDriver);
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileWeekly, endFileName);

		//Monthly
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		reportCommonPage.unCheckAvailability(supportDriver);
		reportCommonPage.verifyEndDateNotBeforeStartDateMonthly(supportDriver);

		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToSLAReport(supportDriver);
		reportCommonPage.unCheckAvailability(supportDriver);
		reportCommonPage.selectStartEndDateSLAMonthly(supportDriver);
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileMonthly, endFileName);

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_download_sla_reports_without_availability-- passed ");
	}
}
