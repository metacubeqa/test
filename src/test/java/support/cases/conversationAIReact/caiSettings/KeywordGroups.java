package support.cases.conversationAIReact.caiSettings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import support.base.SupportBase;
import support.source.commonpages.Dashboard;
import support.source.conversationAIReact.SettingsPage;
import support.source.conversationAIReact.SettingsPage.SortValueInput;
import utility.HelperFunctions;

public class KeywordGroups extends SupportBase{
	
	Dashboard dashboard = new Dashboard();
	SettingsPage settingsPage = new SettingsPage();
	
	@Test(groups = { "Regression", "Product Sanity" })
	public void verify_add_update_delete_keyword_groups() {
		System.out.println("Test case --verify_add_update_delete_keyword_groups-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickKeywordGroupTab(caiCallerDriver);
		settingsPage.verifyKeywordGroupsHeadersLabel(caiCallerDriver);
		
		String groupName = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		int keywordCount = 3;
		List<String> expectedKeyWordList = settingsPage.createNewGroup(caiCallerDriver, groupName, keywordCount);
		List<String> actualKeywordList  = settingsPage.getKeywordNameListAfterCreating(caiCallerDriver, groupName);
		
		assertTrue(actualKeywordList.size() == expectedKeyWordList.size()
				&& actualKeywordList.containsAll(expectedKeyWordList) && expectedKeyWordList.containsAll(actualKeywordList));
		settingsPage.verifyKeywordNameListAscendingOrder(caiCallerDriver, groupName);
		settingsPage.verifyKeywordListCount(caiCallerDriver, groupName, keywordCount);
		
		//update keywords group
		String newGroupName = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		List<String> keyWordRemoveList = new ArrayList<String>();
		keyWordRemoveList.add(expectedKeyWordList.get(0));
		keyWordRemoveList.add(expectedKeyWordList.get(1));
		
		int newKeywordCount = 1;
		List<String> updatedKeyWordList = settingsPage.editKeyWordGroup(caiCallerDriver, groupName, newGroupName, expectedKeyWordList, keyWordRemoveList, newKeywordCount);
		int updatedSize = updatedKeyWordList.size();
		List<String> actualUpdatedKeywordList  = settingsPage.getKeywordNameListAfterCreating(caiCallerDriver, newGroupName);
		
		assertTrue(updatedKeyWordList.size() == actualUpdatedKeywordList.size()
				&& updatedKeyWordList.containsAll(actualUpdatedKeywordList) && actualUpdatedKeywordList.containsAll(updatedKeyWordList));
		settingsPage.verifyKeywordNameListAscendingOrder(caiCallerDriver, newGroupName);
		settingsPage.verifyKeywordListCount(caiCallerDriver, newGroupName, updatedSize);
		
		assertFalse(settingsPage.isTextPresentInStringList(caiCallerDriver, actualUpdatedKeywordList, keyWordRemoveList.get(0)));
		assertFalse(settingsPage.isTextPresentInStringList(caiCallerDriver, actualUpdatedKeywordList, keyWordRemoveList.get(1)));
		
		//delete keyword group
		settingsPage.deleteGroupKeyword(caiCallerDriver, newGroupName);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_add_update_delete_keyword_groups-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_sort_keyword_groups_all_values() {
		System.out.println("Test case --verify_sort_keyword_groups_all_values-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickKeywordGroupTab(caiCallerDriver);
		
		settingsPage.deleteAllKeywordGroups(caiCallerDriver);
		settingsPage.verifyEmptyKeywordsSectionHeaders(caiCallerDriver);
		
		String groupName1 = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		int keywordCount = 2;
		settingsPage.createNewGroup(caiCallerDriver, groupName1, keywordCount);
		
		String groupName2 = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		settingsPage.createNewGroup(caiCallerDriver, groupName2, keywordCount);
	
		String groupName3 = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		settingsPage.createNewGroup(caiCallerDriver, groupName3, keywordCount);
	
		settingsPage.selectSortValueFilter(caiCallerDriver, SortValueInput.NewestToOldest.getValue());
		assertEquals(settingsPage.getGroupNamesList(caiCallerDriver).get(0), groupName3);
		
		settingsPage.selectSortValueFilter(caiCallerDriver, SortValueInput.OldestToNewest.getValue());
		assertEquals(settingsPage.getGroupNamesList(caiCallerDriver).get(0), groupName1);
		
		settingsPage.selectSortValueFilter(caiCallerDriver, SortValueInput.AlphabeticalAToZ.getValue());		
		settingsPage.verifyGroupNameListAscendingOrder(caiCallerDriver);

		settingsPage.selectSortValueFilter(caiCallerDriver, SortValueInput.AlphabeticalZToA.getValue());
		settingsPage.verifyGroupNameListDescendingOrder(caiCallerDriver);

		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_sort_keyword_groups_all_values-- passed ");
	}
	
	@Test(groups = { "Regression" })
	public void verify_empty_msg_when_search_string_not_match() {
		System.out.println("Test case --verify_empty_msg_when_search_string_not_match-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickKeywordGroupTab(caiCallerDriver);
		
		String groupName1 = "AutoGroupName".concat(HelperFunctions.GetRandomString(4));
		int keywordCount = 2;
		settingsPage.createNewGroup(caiCallerDriver, groupName1, keywordCount);
		
		String invalidKeyword = HelperFunctions.GetRandomString(5);
		settingsPage.enterTextInSearchKeywordTab(caiCallerDriver, invalidKeyword);
		settingsPage.verifyEmptyKeywordsSectionHeaders(caiCallerDriver);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_empty_msg_when_search_string_not_match-- passed ");
	}

	@Test(groups = { "Regression" })
	public void verify_user_cant_add_more_than_60_character_keyword_group_name() {
		System.out.println("Test case --verify_user_cant_add_more_than_60_character_keyword_group_name-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		settingsPage.clickKeywordGroupTab(caiCallerDriver);
		
		//verifying keyword group name cannot be more than 60 characters
		String group60Char = "cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzu";
		String groupMoreThan60Char = "cpxeoxkgnoghshwguejclkqkbsszvxkokihtzmogxrhggzldfimctrddcwzuqwqeef";

		settingsPage.verifyKeywordGroupNameMoreThan60NotAccept(caiCallerDriver, groupMoreThan60Char, group60Char);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_user_cant_add_more_than_60_character_keyword_group_name-- passed ");
	}
	
	//Verify user friendly error message when keyword fails validation due to special character
	@Test(groups = { "Regression" })
	public void verify_error_msg_when_keyword_for_special_characters() {
		System.out.println("Test case --verify_error_msg_when_keyword_for_special_characters-- started ");

		initializeSupport("caiCallerDriver");
		driverUsed.put("caiCallerDriver", true);
		
		dashboard.clickConversationAI(caiCallerDriver);
		dashboard.navigateToCAISetup(caiCallerDriver);
		
		String groupName = "AutoSpecialGroup".concat(HelperFunctions.GetRandomString(3));
		settingsPage.clickKeywordGroupTab(caiCallerDriver);
		settingsPage.verifyValidationMsgForSpecialCharacter(caiCallerDriver, groupName);
		
		driverUsed.put("caiCallerDriver", false);
		System.out.println("Test case --verify_error_msg_when_keyword_for_special_characters-- passed ");
	}

	
}
