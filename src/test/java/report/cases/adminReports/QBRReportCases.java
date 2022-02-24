package report.cases.adminReports;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import report.base.ReportBase;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;

public class QBRReportCases extends ReportBase{
	
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingPage = new CallRecordingReportPage();
	
	private String accountNameOther;
	private static final String downloadPath = System.getenv("userprofile") + "\\Downloads";
	private static final String reportFileNameDownload = "query_result";
	
	@BeforeClass(groups = { "Regression" })
	public void beforeClass() {
		accountNameOther = CONFIG.getProperty("qa_user_load_test_account");
	}
	
	@Test(groups = { "Regression" ,  "SupportOnly" })
	public void verify_and_download_support_QBR_report() {
		System.out.println("Test case --verify_and_download_support_QBR_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToQBRreport(supportDriver);

		callRecordingPage.selectAccount(supportDriver, accountNameOther);
		callRecordingPage.clickRefreshButton(supportDriver);
		
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_and_download_support_QBR_report-- passed ");
	}
	
	@Test(groups = { "AdminOnly", "MediumPriority"})
	public void verify_and_download_admin_QBR_report() {
		System.out.println("Test case --verify_and_download_admin_QBR_report-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);
		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToQBRreport(supportDriver);
		callRecordingPage.switchToReportFrame(supportDriver);
		
		callRecordingPage.multipleDownloadAndVerifyCSV(supportDriver, downloadPath, reportFileNameDownload, ".csv");

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_and_download_admin_QBR_report-- passed ");
	}
}
