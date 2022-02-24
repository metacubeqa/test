package report.cases.adminReports;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class UserCountReports extends ReportBase{

	Dashboard dashboard = new Dashboard();
	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	ReportsNonMetabaseCommonPage reportNonCommonPage = new ReportsNonMetabaseCommonPage();
	
	private String accountNameOther;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileName = "user-count-";
	private static final String endFileName = ".csv";
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
	}
	
	@Test(groups="Regression")
	public void verify_user_able_to_download_user_counts_reports() {
		
		System.out.println("Test case --verify_user_able_to_download_user_counts_reports-- started ");

		driverUsed.put("supportDriver", true);
		initializeSupport();
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToUserCountsReport(supportDriver);
		
		//selecting other account if visible
		callRecordingPage.selectAccount(supportDriver, accountNameOther);

		//selecting present date
		String curentDate = HelperFunctions.GetCurrentDateTime("d");
		String currentMonth = HelperFunctions.GetCurrentDateTime("MMMM");
		String currentYear = HelperFunctions.GetCurrentDateTime("yyyy");
		
		reportCommonPage.selectStartEndDate(supportDriver, "Start", curentDate, currentMonth, currentYear);
		reportNonCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileName, endFileName);
		
		//selecting past date
		String startMonth = HelperFunctions.addMonthYearDateToExisting("MMMM", currentMonth,0 , -1 , 0);
		reportCommonPage.selectStartEndDate(supportDriver, "Start", curentDate, startMonth, currentYear);
		reportNonCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileName, endFileName);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_able_to_download_user_counts_reports-- passed ");
	}
	
}
