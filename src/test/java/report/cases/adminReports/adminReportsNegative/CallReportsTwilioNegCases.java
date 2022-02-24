package report.cases.adminReports.adminReportsNegative;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

import report.base.ReportBase;
import report.source.ReportsNonMetabaseCommonPage;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

public class CallReportsTwilioNegCases extends ReportBase {

	ReportsNonMetabaseCommonPage reportNonMetabasePage =  new ReportsNonMetabaseCommonPage();
	Dashboard dashboard = new Dashboard();
	
	//@Test(groups = { "Regression", "SupportOnly" })
	public void verify_call_reports_twilio_with_invalid_accountName_filter() {
		System.out.println("Test case --verify_call_reports_twilio_with_invalid_accountName_filter-- started ");

		initializeSupport();
		driverUsed.put("supportDriver", true);

		dashboard.switchToTab(supportDriver, 2);
		dashboard.clickOnUserProfile(supportDriver);
		dashboard.navigateToCallReportsTwilio(supportDriver);

		// verifying Account Name exist or not
		assertFalse(reportNonMetabasePage.verifyAccountNameInDropDown(supportDriver,HelperFunctions.GetRandomString(6)));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_call_reports_twilio_with_invalid_accountName_filter-- passed ");
	}
}
