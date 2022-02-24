package report.cases.adminReports;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import softphone.source.ReportThisCallPage;
import softphone.source.SoftphoneCallHistoryPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallReportsTwilio extends ReportBase{

	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();
	ReportsNonMetabaseCommonPage reportCommonPage = new ReportsNonMetabaseCommonPage();
	SoftphoneCallHistoryPage softphoneCallHistoryPage = new SoftphoneCallHistoryPage();
	ReportThisCallPage reportThisCallPage = new ReportThisCallPage(); 
	
	private String accountName;
	private static final String format = "MM/dd/yyyy";
	private static final String accountId = "460";
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "CallReportsTwilio-".concat(accountId);
	
	@BeforeClass(groups = { "Regression", "SupportOnly"})
	public void beforeClass(){
		accountName = CONFIG.getProperty("qa_user_account_cai_report");
	}
	
	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_call_reports_twilio_download_reports() {
		
		System.out.println("Test case --verify_call_reports_twilio_download_reports-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsTwilio(supportDriver);
		
		//verify By default Start date and End date is selected for 1 month duration
		String currentDate = HelperFunctions.GetCurrentDateTime(format);
		String startDate = HelperFunctions.addMonthYearDateToExisting(format, currentDate, -1, -2, 0);
		String endDate = HelperFunctions.addMonthYearDateToExisting(format, currentDate, -1, 0, 0);
		
		reportCommonPage.enterAndSelectAccountName(supportDriver, accountName);
		
		reportCommonPage.enterStartDateText(supportDriver, startDate);
		reportCommonPage.enterEndDateText(supportDriver, endDate);
		
		reportCommonPage.downloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reports_twilio_download_reports-- passed ");
	}
	
}
