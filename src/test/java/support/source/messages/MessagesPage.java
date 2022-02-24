package support.source.messages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import support.source.commonpages.Dashboard;
import base.SeleniumBase;

public class MessagesPage extends SeleniumBase {

	Dashboard dashboard = new Dashboard();

	By downloadLink 	= By.cssSelector(".glyphicon-download-alt");
	By sidTextBox 		= By.cssSelector(".message-sid.form-control");
	By userNameTxtBox 	= By.cssSelector("[placeholder='User name'][type='text']");
	By accountNameTxtBox = By.cssSelector("[type='text'][placeholder='Account name']");
	By searchBtn 		= By.cssSelector(".search");

	By sidMsgList 	   = By.xpath(".//*[contains(@class,'sid')]/a//ancestor::table/tbody//td/a[contains(@href,'messages')]");
	By accountList     = By.xpath(".//*[contains(@class,'account')]/a//ancestor::table/tbody//td/a[contains(@href,'accounts')]");
	By usersList       = By.xpath(".//*[contains(@class,'owner')]/a//ancestor::table/tbody//td/a[contains(@href,'users')]");
	By directionList   = By.xpath(".//*[contains(@class,'direction')]/a//ancestor::table/tbody//td[contains(@class,'direction')]");
	By toNumberList    = By.xpath(".//*[contains(@class,'toNumber')]/a//ancestor::table/tbody//td[contains(@class,'toNumber')]");
	By fromNumberList  = By.xpath(".//*[contains(@class,'fromNumber')]/a//ancestor::table/tbody//td[contains(@class,'fromNumber')]");
	By createdDateList = By.xpath(".//*[contains(@class,'createdAt')]/a//ancestor::table/tbody//td[contains(@class,'createdAt')]");

	String sidMsgPage  = ".//*[contains(@href,'users') and text()='$owner$']/ancestor::tr//td[contains(@class,'direction') and text()='$direction$']/..//td[contains(@class,'createdAt') and text()='$date$']/..//a[contains(@href,'#messages')]";
	
	//Message Inspector Section
	By msgInspctBody	  = By.cssSelector(".panel-body");
	By msgInspctSid		  = By.xpath(".//label[text()='SID']/../span");
	By msgInspctSalesForceActivityId = By.xpath(".//label[text()='Salesforce Activity ID']/../span");
	By msgInspctFromNo 	  = By.xpath(".//label[text()='From Number']/../span");
	By msgInspctToNo 	  = By.xpath(".//label[text()='To Number']/../span");
	By msgInspctStatus	  = By.xpath(".//label[text()='Status']/../span");	
	By msgInspctCreatedAt = By.xpath(".//label[text()='Created At']/../span");
	By msgInspctOwner 	  = By.xpath(".//label[text()='Owner']/../span");
	By msgInspctDirection = By.xpath(".//label[text()='Direction']/../span");
	By msgInspctErrorCode = By.xpath(".//label[text()='Error Code']/../span");
	By msgInspctMsgSid    = By.xpath(".//label[text()='MessageSid']/../span");
	
	
	public static final String SID = "sid";
	public static final String USER_NAME = "userName";
	public static final String ACCOUNT_NAME = "accountName";

	public void clickDownloadLink(WebDriver driver) {
		waitUntilVisible(driver, downloadLink);
		clickElement(driver, downloadLink);
	}

	public boolean downloadRecordsSuccessfullyOrNot(WebDriver driver, String path, String startFileName, String extension) {
		waitForFileToDownload(driver, path, startFileName, extension);
		return isFileDownloaded(path, startFileName, extension);
	}

	public String getTextFromListFromIndex(WebDriver driver, String section, int index) {
		String textValue = "";
		switch (section) {
		case SID:
			textValue = getTextListFromElements(driver, sidMsgList).get(index);
			break;
		case USER_NAME:
			textValue = getTextListFromElements(driver, usersList).get(index);
			break;
		case ACCOUNT_NAME:
			textValue = getTextListFromElements(driver, accountList).get(index);
			break;
		default:
			System.out.println("Invalid data");
		}
		return textValue;
	}

	public void clearSections(WebDriver driver, String section) {
		switch (section) {
		case SID:
			clearAll(driver, sidTextBox);
			break;
		case USER_NAME:
			clearAll(driver, userNameTxtBox);
			break;
		case ACCOUNT_NAME:
			clearAll(driver, accountNameTxtBox);
			break;
		default:
			System.out.println("Invalid data");
		}

	}

	public boolean verifyListContainsText(WebDriver driver, String section, String text) {
		boolean value = false;
		switch (section) {
		case SID:
			value = isListContainsText(driver, getElements(driver, sidMsgList), text);
			break;
		case USER_NAME:
			value = isListContainsText(driver, getElements(driver, usersList), text);
			break;
		case ACCOUNT_NAME:
			value = isListContainsText(driver, getElements(driver, accountList), text);
			break;
		default:
			System.out.println("Invalid data");
		}
		return value;
	}

	public void enterText_SearchInSection(WebDriver driver, String section, String text) {
		switch (section) {
		case SID:
			enterText(driver, sidTextBox, text);
			break;
		case USER_NAME:
			enterText(driver, userNameTxtBox, text);
			break;
		case ACCOUNT_NAME:
			enterText(driver, accountNameTxtBox, text);
			break;
		default:
			System.out.println("Invalid data");
		}
		clickElement(driver, searchBtn);
		dashboard.isPaceBarInvisible(driver);
	}
	
	public void clickSidUsingIndex(WebDriver driver, int index) {
		getElements(driver, sidMsgList).get(index).click();
		dashboard.isPaceBarInvisible(driver);
	}
	

	public String clickSidWithOwner_Direction(WebDriver driver, String owner, String direction, String date) {
		String sid = "";
		dashboard.isPaceBarInvisible(driver);
		By sidMsgLoc = By.xpath(sidMsgPage.replace("$owner$", owner).replace("$direction$", direction).replace("$date$", date));
		isElementVisible(driver, sidMsgLoc, 10);
		waitUntilVisible(driver, sidMsgLoc);
		sid = getElementsText(driver, sidMsgLoc);
		clickElement(driver, sidMsgLoc);
		dashboard.isPaceBarInvisible(driver);
		return sid;
	}
	
	//Message Inspector section
	
	public String getMsgInspctTextMsg(WebDriver driver) {
		waitUntilVisible(driver, msgInspctBody);
		return getElementsText(driver, msgInspctBody);
	}
	
	public String getMsgInspctSID(WebDriver driver) {
		waitUntilVisible(driver, msgInspctSid);
		return getElementsText(driver, msgInspctSid);
	}
	
	public String getMsgInspctSalesForceActivityId(WebDriver driver) {
		waitUntilVisible(driver, msgInspctSalesForceActivityId);
		return getElementsText(driver, msgInspctSalesForceActivityId);
	}
	
	public String getMsgInspctFromNumber(WebDriver driver) {
		waitUntilVisible(driver, msgInspctFromNo);
		return getElementsText(driver, msgInspctFromNo);
	}

	public String getMsgInspctDirection(WebDriver driver) {
		waitUntilVisible(driver, msgInspctDirection);
		return getElementsText(driver, msgInspctDirection);
	}
	
	public String getMsgInspctOwner(WebDriver driver) {
		waitUntilVisible(driver, msgInspctOwner);
		return getElementsText(driver, msgInspctOwner);
	}
	
	public String getMsgInspctToNumber(WebDriver driver) {
		waitUntilVisible(driver, msgInspctToNo);
		return getElementsText(driver, msgInspctToNo);
	}
	
	public String getMsgInspctStatus(WebDriver driver) {
		waitUntilVisible(driver, msgInspctStatus);
		return getElementsText(driver, msgInspctStatus);
	}
	
	public String getMsgInspctCreatedAt(WebDriver driver) {
		waitUntilVisible(driver, msgInspctCreatedAt);
		return getElementsText(driver, msgInspctCreatedAt);
	}
	
	public String getMsgInspctErrorCode(WebDriver driver) {
		waitUntilVisible(driver, msgInspctErrorCode);
		return getElementsText(driver, msgInspctErrorCode);
	}
	
	public String getMsgInspctMsgSid(WebDriver driver) {
		waitUntilVisible(driver, msgInspctMsgSid);
		return getElementsText(driver, msgInspctMsgSid);
	}
}
