package support.cases.adminCases.callTracking;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.admin.AdminCallTracking;
import support.source.commonpages.Dashboard;

public class GoogleAdwordsCallTracking extends SupportBase {

	Dashboard dashboard = new Dashboard();
	AdminCallTracking adminCallTracking = new AdminCallTracking();

	@Test(groups = { "SupportOnly", "MediumPriority" })
	public void verify_add_icon_disable_for_google_tracking_for_same_and_other_account() {
		System.out.println("Test case --verify_add_icon_disable_for_google_tracking_for_same_and_other_account-- started ");

		// updating the driver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Google Call Tracking page
		dashboard.openGoogleAdwordsCallTrackingPage(supportDriver);
		assertEquals(adminCallTracking.getAccountName(supportDriver), CONFIG.getProperty("qa_user_account"));
		assertTrue(adminCallTracking.isGoogleAdwordsAddIconDisabled(supportDriver));

		// selecting diff account
		adminCallTracking.selectAccount(supportDriver, "QA v3");
		assertTrue(adminCallTracking.isGoogleAdwordsAddIconDisabled(supportDriver));

		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_add_icon_disable_for_google_tracking_for_same_and_other_account-- passed ");
	}
}
