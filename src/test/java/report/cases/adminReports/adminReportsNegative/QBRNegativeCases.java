package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportMetabaseCommonPage;
import support.source.callRecordings.CallRecordingReportPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class QBRNegativeCases extends ReportBase {

	ReportMetabaseCommonPage reportCommonPage = new ReportMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	CallRecordingReportPage callRecordingReportPage = new CallRecordingReportPage();

	@Test(groups = { "Regression", "SupportOnly" })
	public void verify_support_user_able_to_access_qbr_report_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_support_user_able_to_access_qbr_report_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToQBRreport(supportDriver);

		// verifying Account Name exist or not
		assertFalse(callRecordingReportPage.verifyAccountExistsInDropDown(supportDriver,HelperFunctions.GetRandomString(5)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_support_user_able_to_access_qbr_report_with_invalid_accountName_filter-- passed ");
	}
}
