package support.source.conversationAI;

import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;
import softphone.source.SoftPhoneLoginPage;
import softphone.source.callTools.CallToolsPanel;
import support.source.commonpages.Dashboard;

public class LibraryTabPage extends SeleniumBase {

	SeleniumBase seleniumBase = new SeleniumBase();
	Dashboard dashboard = new Dashboard();
	SoftPhoneLoginPage softPhoneLoginPage = new SoftPhoneLoginPage();
	CallToolsPanel callToolsPanel = new CallToolsPanel();
	
	By libraryTabList 		= By.cssSelector(".lib-item");
	By libraryTab 			= By.cssSelector(".cai-tabs [data-tab='library']");
	By librarySavedSearches = By.cssSelector(".lib-saved-searches .saved-search-item h5");
	String libraryNamePage	= ".//*[contains(@class,'lib-list')]//span[@data-args='$$libraryName$$']";
	String libraryItem 		= ".//div[contains(@class,'lib-item') ]/span[@class='item' and text()='$$library_name$$']";
	By searchOrderDisabled = By.cssSelector(".search-direction:disabled");
	By libraryNoResult		= By.cssSelector(".recordings div div");
	
	By addLibraryIcon		= By.cssSelector(".add-library");
	By inputLibrary			= By.cssSelector(".library-name");
	By addLibrary			= By.cssSelector(".save");
	By editLocator			= By.cssSelector(".edit-library");
	By deleteLocator		= By.cssSelector(".delete-library");
	By okLibraryDelete		= By.cssSelector(".btn-primary");
	By cancelLibraryDelete	= By.cssSelector(".btn-default");
	// Open Library tab
	public void clickLibraryTab(WebDriver driver) {
		clickElement(driver, libraryTab);
		dashboard.isPaceBarInvisible(driver);
	}
	
	//Add Library from conversation ai->Library tab in accounts section [RDNA-15476]
	public String addLibrary(WebDriver driver){
		String libDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String libName = "Lib"+libDate;
		clickElement(driver, addLibraryIcon);
		waitUntilVisible(driver, inputLibrary);
		enterText(driver, inputLibrary, libName);
		clickElement(driver, addLibrary);
		return libName;
	}
	
	//Method to update Library->Library Tab
	public String updateLibrary(WebDriver driver, String libName){
		By libLocator = By.xpath(libraryNamePage.replace("$$libraryName$$", libName));
		clickElement(driver, libLocator);
		
		String updatedLibName="Updated"+libName;
		if( isElementVisible(driver, editLocator, 5))
		{
			clickElement(driver, editLocator);
			waitUntilVisible(driver, inputLibrary);
			enterText(driver, inputLibrary, updatedLibName);
			clickElement(driver, addLibrary);
			return updatedLibName;
		}
		else
			return addLibrary(driver);
	}
	
	
	//Method to delete Library
	public void deleteLibrary(WebDriver driver, String libName){
		By libLocator = By.xpath(libraryNamePage.replace("$$libraryName$$", libName));
		clickElement(driver, libLocator);
		if( isElementVisible(driver, deleteLocator, 5))
		{
			clickElement(driver, deleteLocator);
			waitUntilVisible(driver, okLibraryDelete);
			clickElement(driver,okLibraryDelete);
			dashboard.isPaceBarInvisible(driver);
		}

	}

	// Verify given library exist on Library tab library list
	public void verifyLibraryAdded(WebDriver driver, String libName) {
		clickLibraryTab(driver);
		assertTrue(isLibraryExistsOnLibraryTab(driver, libName), "Added Library Not found in List");
	}

	// Added Library in Library Tab
	public boolean isLibraryExistsOnLibraryTab(WebDriver driver, String lib) {
		if (!isElementVisible(driver, libraryTabList, 1)) {
			return false;
		} else {
			List<WebElement> libList = getElements(driver, libraryTabList);
			return isTextPresentInList(driver, libList, lib);
		}
	}

	public void selectLibraryFromLeftMenu(WebDriver driver, String libraryName) {
		By libraryLoc = By.xpath(libraryNamePage.replace("$$libraryName$$", libraryName));
		waitUntilVisible(driver, libraryLoc);
		clickElement(driver, libraryLoc);
		dashboard.isPaceBarInvisible(driver);
	}
}

