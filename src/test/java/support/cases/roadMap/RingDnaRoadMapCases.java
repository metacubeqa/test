package support.cases.roadMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.roadMap.RingDnaRoadMapPage;
import support.source.roadMap.RingDnaRoadMapPage.important;
import utility.HelperFunctions;

public class RingDnaRoadMapCases extends SupportBase {
	
	RingDnaRoadMapPage roadMap = new RingDnaRoadMapPage();
	private String email;
	
	
	@BeforeClass(groups = { "Regression", "MediumPriority" })
	public void searchUser() {
		if(SupportBase.drivername.toString().equals("adminDriver")) {
			email = CONFIG.getProperty("qa_admin_user_email");
		}
		else if(SupportBase.drivername.toString().equals("supportDriver")){
			email = CONFIG.getProperty("qa_support_user_email");
		}
		
	}
	
	//Verify ringDNA Product Board Portal accessibility to Agent,Admin,Support Role
	//Verify ringDNA RoadMap icon and navigation
	//Verify Submit New Idea through authenticated user
	//Verify Submit New Idea in Under Consideration Tab through unauthenticated user
	@Test(groups = { "Regression" })
	public void verify_submit_idea_in_under_consideration() {
		System.out.println("Test case --verify_submit_idea_in_under_consideration-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open road map
		roadMap.openRoadMap(supportDriver);
		
		// click Under Consideration
		roadMap.clickUnderConsideration(supportDriver);
		
		//submit idea
		roadMap.clickSubmitIdea(supportDriver);
		
		//verify submit in submit box
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		//write idea and verify submit button
		roadMap.writeIdeaInTextArea(supportDriver, HelperFunctions.GetRandomString(5));
		roadMap.selectImportanceOption(supportDriver, important.Critical);
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_in_under_consideration-- passed");
	}
	
	//Verify Submit New Idea in Launched Tab through authenticated user
	@Test(groups = { "Regression" })
	public void verify_submit_idea_in_launched_tab() {
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open road map
		roadMap.openRoadMap(supportDriver);
		
		// click Launched Tab
		roadMap.clickLaunchedTab(supportDriver);
		
		//submit idea
		roadMap.clickSubmitIdea(supportDriver);
		
		//verify submit in submit box
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		//write idea and verify submit button
		roadMap.writeIdeaInTextArea(supportDriver, HelperFunctions.GetRandomString(5));
		roadMap.selectImportanceOption(supportDriver, important.Critical);
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- passed");
	}
	
	
	//Verify Submit New Idea in Planned Tab through unauthenticated user
	//Verify Submit New Idea in Planned Tab through authenticated user
	@Test(groups = { "Regression" })
	public void verify_submit_idea_in_planned_tab() {
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);
		
		//open road map
		roadMap.openRoadMap(supportDriver);
		
		// click Planned Tab
		roadMap.clickPlannedTab(supportDriver);
		
		//submit idea
		roadMap.clickSubmitIdea(supportDriver);
		
		//verify submit in submit box
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		//write idea and verify submit button
		roadMap.writeIdeaInTextArea(supportDriver, HelperFunctions.GetRandomString(5));
		roadMap.selectImportanceOption(supportDriver, important.Critical);
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- passed");
	}
	
	//Verify add insight to feature request in Under Consideration Tab through unauthenticated user
	//Verify add insight to feature request in Under Consideration Tab through authenticated user
	@Test(groups = { "Regression" })
	public void verify_add_insight_in_under_consideration() {
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// open road map
		roadMap.openRoadMap(supportDriver);

		// click Under Consideration
		roadMap.clickUnderConsideration(supportDriver);
		
		// click feature blog
		roadMap.clickFeatureBlog(supportDriver, 1);
		
		// write insight and verify submit button
		roadMap.selectImportanceOptionForInsight(supportDriver, important.Important);
		
		// disabled submit button
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		String insight = "insight"+ HelperFunctions.GetRandomString(5);
		roadMap.writeInsightInTextArea(supportDriver, insight);
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));
		roadMap.clickSubmitButton(supportDriver);
		
		//verify insight in feature
		roadMap.clickFeatureBlog(supportDriver, 1);
		String importance = important.Important.getValue().toUpperCase();
		assertEquals(roadMap.checkInsightImportanceLevel(supportDriver, email), importance);
		roadMap.checkInsightMessage(supportDriver, insight);

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- passed");
	}
		
	
	//Verify add insight to feature request in Planned Tab through unauthenticated user
	//Verify add insight to feature request in Planned Tab through authenticated user
	@Test(groups = { "Regression" })
	public void verify_add_insight_in_planned_tab() {
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// open road map
		roadMap.openRoadMap(supportDriver);

		// click Planned Tab
		roadMap.clickPlannedTab(supportDriver);
		
		// click feature blog
		roadMap.clickFeatureBlog(supportDriver, 1);
		
		// write insight and verify submit button
		roadMap.selectImportanceOptionForInsight(supportDriver, important.Important);
		
		// disabled submit button
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		String insight = "insight"+ HelperFunctions.GetRandomString(5);
		roadMap.writeInsightInTextArea(supportDriver, insight);
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));
		roadMap.clickSubmitButton(supportDriver);
		
		//verify insight in feature
		roadMap.clickFeatureBlog(supportDriver, 1);
		String importance = important.Important.getValue().toUpperCase();
		assertEquals(roadMap.checkInsightImportanceLevel(supportDriver, email), importance);
		roadMap.checkInsightMessage(supportDriver, insight);

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- passed");
	}
	
	//Verify add insight to feature request in Launched Tab through unauthenticated user
	//Verify add insight to feature request in Launched Tab through authenticated user
	@Test(groups = { "Regression" })
	public void verify_add_insight_in_launched_tab() {
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// open road map
		roadMap.openRoadMap(supportDriver);

		// click Launched Tab
		roadMap.clickLaunchedTab(supportDriver);
		
		// click feature blog
		roadMap.clickFeatureBlog(supportDriver, 1);
		
		// write insight and verify submit button
		roadMap.selectImportanceOptionForInsight(supportDriver, important.Important);
		
		// disabled submit button
		assertTrue(roadMap.verifySubmitButtonDisabled(supportDriver));
		
		String insight = "insight"+ HelperFunctions.GetRandomString(5);
		roadMap.writeInsightInTextArea(supportDriver, insight);
		
		//verify email text
		assertEquals(roadMap.getEmailText(supportDriver, email), email);
		
		assertFalse(roadMap.verifySubmitButtonDisabled(supportDriver));
		roadMap.clickSubmitButton(supportDriver);
		
		//verify insight in feature
		roadMap.clickFeatureBlog(supportDriver, 1);
		String importance = important.Important.getValue().toUpperCase();
		assertEquals(roadMap.checkInsightImportanceLevel(supportDriver, email), importance);
		roadMap.checkInsightMessage(supportDriver, insight);

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_submit_idea_through_authenticated_user-- passed");
	}
	
	//Verify user can View released features on the "Launched" tab
	//Verify user can add insight , share insight from any of teh tab
	@Test(groups = { "Regression" })
	public void verify_user_can_view_feature_and_share_insight() {
		System.out.println("Test case --verify_user_can_view_feature_and_share_insight-- started ");

		// updating the supportDriver used
		initializeSupport();
		driverUsed.put("supportDriver", true);

		// open road map
		roadMap.openRoadMap(supportDriver);

		// click Planned Tab
		roadMap.clickPlannedTab(supportDriver);
		
		// click feature blog
		roadMap.clickFeatureBlog(supportDriver, 1);
		
		// copy private link
		roadMap.copyPrivateLink(supportDriver);
		
		// get clip board data in String
		String data = null;
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//assert
		assertTrue(data.contains("https://portal.productboard.com/"));
		
		// click Under Consideration Tab
		roadMap.clickUnderConsideration(supportDriver);

		// click feature blog
		roadMap.clickFeatureBlog(supportDriver, 1);

		// copy private link
		roadMap.copyPrivateLink(supportDriver);

		// get clip board data in String
		data = null;
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// assert
		assertTrue(data.contains("https://portal.productboard.com/"));

		// Setting supportDriver used to false as this test case is pass
		driverUsed.put("supportDriver", false);
		System.out.println("Test case --verify_user_can_view_feature_and_share_insight-- passed");
	}

}
