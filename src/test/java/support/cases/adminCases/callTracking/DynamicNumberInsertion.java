package support.cases.adminCases.callTracking;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.admin.AdminCallTracking;
import support.source.commonpages.Dashboard;

public class DynamicNumberInsertion extends SupportBase{
	
	Dashboard dashBoard = new Dashboard();
	AdminCallTracking adminTracking = new AdminCallTracking();
	
	@Test(groups = { "MediumPriority"})
	public void Verify_dynamic_number_insertion_headings() {
		System.out.println("Test case --Verify_dynamic_number_insertion_headings-- started ");
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// Open Dynamic number insertion page
		dashBoard.openDynamicNumberInsertionPage(supportDriver);
		
		//verifying headings on page
		adminTracking.verifyDynamicNumberInsertionPageHeadings(supportDriver);
		
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --Verify_dynamic_number_insertion_headings-- passed ");
	}
}
