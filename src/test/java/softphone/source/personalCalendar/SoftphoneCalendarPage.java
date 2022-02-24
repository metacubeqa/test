package softphone.source.personalCalendar;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.SeleniumBase;

public class SoftphoneCalendarPage extends SeleniumBase{
	
	// softphone calendar
	By softphoneCalIcon       = By.cssSelector(".react-calendar");
	By calendarSettingIcon    = By.xpath("//button[contains(@data-analyticsid, 'dialer-calendar')]/parent::span/following-sibling::button");
	
	//conflict cal
	By conflictCalCheckBox    = By.xpath("//input[contains(@class , 'jss')]/ancestor::span[contains(@class, 'MuiCheckbox-root')]");
	By checkedConflictCal     = By.xpath("//span[contains(@class, 'MuiCheckbox-root' ) and contains(@class, 'checked')]");
	By uncheckConflictCal     = By.xpath("//span[not(contains(@class, 'checked')) and contains(@class, 'MuiCheckbox')]");
	
	String selectConflictCalByName = "//input[@value= '%name%']/ancestor::span[contains(@class, 'MuiCheckbox-root')]";
	
	/**
	 * @Desc: open Calendar tab
	 * @param driver
	 */
	public void clickCalendarIcon(WebDriver driver) {
		if (isCalIconVisible(driver)) {
			clickElement(driver, softphoneCalIcon);
			waitUntilInvisible(driver, spinnerWheel);
		}
	}
	
	/**
	 * @param driver
	 * @return visiblity of cal icon
	 */
	public boolean isCalIconVisible(WebDriver driver) {
		return isElementVisible(driver, softphoneCalIcon, 10);
	}
	
	/**
	 * @param driver
	 * click calendar settings icon
	 */
	public void clickCalendarSetting(WebDriver driver) {
		waitUntilVisible(driver, calendarSettingIcon);
		clickElement(driver, calendarSettingIcon);
	}
	
	/**
	 * @param driver
	 * @return no of conflict cal selected
	 */
	public int getNumberOfCheckedConflictCalSelected(WebDriver driver){
		if(isElementVisible(driver, checkedConflictCal, 6)) {
			List<WebElement> alreadyChecked = getElements(driver, checkedConflictCal);
			return alreadyChecked.size();	
		}
		return 0;
	}
	
	/**
	 * @param driver
	 * @param count
	 */
	public void checkConflictCalendar(WebDriver driver, int count) {
		int alreadyChecked = 0;
		waitUntilVisible(driver, conflictCalCheckBox);
		if(getElements(driver, conflictCalCheckBox).size() >= 6) {
			if(isElementVisible(driver, checkedConflictCal, 5)){
				alreadyChecked = getElements(driver, checkedConflictCal).size();
			}
			for(int i=alreadyChecked; i< count; i++) {
				waitUntilVisible(driver, uncheckConflictCal);
				clickElement(driver, uncheckConflictCal);
				idleWait(2);
			}
		}
	}
	
	/**
	 * @param driver
	 * @param name
	 */
	public void checkConflictCalByName(WebDriver driver, String name) {
		By locator = By.xpath(selectConflictCalByName.replace("%name%", name));
		waitUntilVisible(driver, locator);
		clickElement(driver, locator);
	}

}
