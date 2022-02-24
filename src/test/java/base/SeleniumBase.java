package base;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.util.Strings;

import utility.HelperFunctions;


/**
 * @author Abhishek
 *
 */
public class SeleniumBase {
	
	public static int timeOutInSecs = 60;
	public By spinnerWheel 			= By.xpath("//*[contains(@id, 'spinner') or contains(@class, 'spinner')]|.//*[@src='images/ringdna-icon-animated.gif']|.//*[@class='logo-loader'] | //div[contains(@class, 'MuiCircularProgress')]");
	public By backGroundFade 		= By.cssSelector(".modal-backdrop.fade");
	public By overlayWorkSpace 		= By.cssSelector(".overlay-workspace[style='display: block;']");
	
	By salesForceTitle				= By.cssSelector("[title='Salesforce.com']");
	By recommendationWindowFrame 	= By.cssSelector("iframe.apt-recommendation-bubble-frame");
	By recommendationWindowClose 	= By.cssSelector(".frame-content .recommendation-close");
	
	public enum ElementAttributes{
		value("value"),
		ariaControls("aria-controls"),
		AriaExpanded("aria-expanded"),
		AriaDescribedBy("aria-describedby"),
		AriaDisable("aria-disabled"),
		AriaValueNow("aria-valuenow"),
		checked("checked"),
		color("color"),
		Class("class"),
		dataValue("data-value"),
		dataOriginalTitle("data-original-title"),
		dataIndeterMinate("data-indeterminate"),
		dataTip("data-tip"),
		disabled("disabled"),
		fill("fill"),
		stroke("stroke"),
		href("href"),
		innerHTML("innerHTML"),
		outerHTML("outerHTML"),
		Placeholder("placeholder"),
		src("src"),
		Title("title"),
		height("height"),
		maxlength("maxlength"),
		Style("style");
		
		private String displayName;

		ElementAttributes(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() { return displayName; }

		@Override
		public String toString() { return displayName; }
	}
	
	public enum CssValues{
		Color("color"),
		PointerEvents("pointer-events"),
		BackgroundColor("background-color"),
		Background("background"),
		FontWeight("font-weight"),
		BorderColor("border-color"),
		BorderRadius("border-radius"),
		BorderBotton("border-bottom"),
		Width("width"),
		maxWidth("max-width");
		
		
		private String displayName;

		CssValues(String displayName) {
	        this.displayName = displayName;
		}
	        
		public String displayName() { return displayName; }

		@Override 
		public String toString() { return displayName; }
	}
	
	public enum SelectTypes{
		visibleText,
		value
	}

	public WebElement findElement(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
			return null;
		}
	}

	public boolean isListElementsVisible(WebDriver driver, By locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementPresent(WebDriver driver, By locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementVisible(WebDriver driver, By locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementVisible(WebDriver driver, WebElement locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.visibilityOf(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementInvisible(WebDriver driver, By locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElementClickable(WebDriver driver, By locator, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementDisabled(WebDriver driver, By locator, int timeOut) {
		try {
			isElementVisible(driver, locator, timeOut);
			return isAttributePresent(driver, locator, "disabled");
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementDisabled(WebDriver driver, WebElement element, int timeOut) {
		try {
			isElementVisible(driver, element, timeOut);
			return isAttributePresent(driver, element, "disabled");
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isChildElementPresent(WebDriver driver, By parent, By child) {
		try {
			WebElement parentElement = findElement(driver, parent);
			parentElement.findElement(child);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isChildElementPresent(WebDriver driver, WebElement parent, By child) {
		try {
			parent.findElement(child);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isAttributePresent(WebDriver driver, By locator, String attribute) {
		Boolean result = false;
		try {
			String value = driver.findElement(locator).getAttribute(attribute);
			if (value != null) {
				result = true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
		return result;
	}
	
	public boolean isAttributePresent(WebDriver driver, WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				result = true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
		return result;
	}

	
	public boolean isAttributePresentInList(WebDriver driver, By locator, String attribute) {
		Boolean result = false;
		try {
			for(int i=0;i<driver.findElements(locator).size();i++) {
				String value = driver.findElements(locator).get(i).getAttribute(attribute);
				if (value != null) {
					result = true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
		return result;
	}
	
	public List<WebElement> getElements(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
			return null;
		}
	}

	public List<WebElement> getInactiveElements(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
			return null;
		}
	}

	public void clickElement(WebDriver driver, By locator) {
		try {
			isElementInvisible(driver, spinnerWheel, timeOutInSecs);
			closeRecommendationWindow(driver);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			element.click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void clickElement(WebDriver driver, WebElement element) {
		try {
			isElementInvisible(driver, spinnerWheel, timeOutInSecs);
			closeRecommendationWindow(driver);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * Wait until Spinner wheel in invisible
	 * @param driver
	 */
	public void isSpinnerWheelInvisible(WebDriver driver) {
		isElementVisible(driver, spinnerWheel, 2);
		isElementInvisible(driver, spinnerWheel, timeOutInSecs);
	}
	
	/**
	 * Method to click Specifically for GS and wait for Spinner wheel to be invisible
	 * @param driver
	 * @param locator
	 */
	public void clickElement_GS(WebDriver driver, By locator) {
		try {
			isElementInvisible(driver, spinnerWheel, timeOutInSecs);
			closeRecommendationWindow(driver);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			element.click();
			isSpinnerWheelInvisible(driver);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * Method to click Specifically for GS and wait for Spinner wheel to be invisible
	 * @param driver
	 * @param element
	 */
	public void clickElement_GS(WebDriver driver, WebElement element) {
		try {
			isElementInvisible(driver, spinnerWheel, timeOutInSecs);
			closeRecommendationWindow(driver);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
			isSpinnerWheelInvisible(driver);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}


	public void checkCheckBox(WebDriver driver, By chkbx) {
		if (!findElement(driver, chkbx).isSelected()) {
			findElement(driver, chkbx).click();
		}
 	}
	
	public void unCheckCheckBox(WebDriver driver, By chkbx) {
		if (findElement(driver, chkbx).isSelected()) {
			findElement(driver, chkbx).click();
		}
 	}
	
	public void enterText(WebDriver driver, By locator, String text) {
		try {
			WebElement element = findElement(driver, locator);
			element.click();
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void enterText(WebDriver driver, WebElement element, String text) {
		try {
			closeRecommendationWindow(driver);
			element.click();
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void appendText(WebDriver driver, By locator, String text) {
		try {
			WebElement element = findElement(driver, locator);
			closeRecommendationWindow(driver);
			waitUntilVisible(driver, element);
			element.click();
			element.sendKeys(Keys.END);
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public WebElement getWebelementIfExist(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			return null;
		}
	}

	public void waitUntilVisible(WebDriver driver, By locator){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void waitUntilAttribute(WebDriver driver, By locator, ElementAttributes attribute, String value){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.attributeToBe(locator, attribute.displayName(), value));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void waitUntilVisible(WebDriver driver, WebElement element){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}


	public void waitUntilVisible(WebDriver driver, By locator, int time){
		try{
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void waitUntilInvisible(WebDriver driver, By locator){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void waitUntilInvisible(WebDriver driver, By locator, int time){
		try{
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void waitUntilInvisible(WebDriver driver, WebElement element){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.invisibilityOf(element));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void waitUntilTextPresent(WebDriver driver, By locator, String text){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void waitUntilTextPresent(WebDriver driver, WebElement element, String text){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.textToBePresentInElement(element, text));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void waitUntilTextNotPresent(WebDriver driver, By locator, String text){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}																			  
 
	public void waitUntilClickable(WebDriver driver, By locator){
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void scrollToElement(WebDriver driver, By locator){
		try{
			Coordinates coordinate = ((Locatable) findElement(driver, locator)).getCoordinates();
			coordinate.inViewPort();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void scrollToElement(WebDriver driver, WebElement element){
		try{
			Coordinates coordinate = ((Locatable) element).getCoordinates();
			coordinate.inViewPort();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void scrollIntoView(WebDriver driver, By locator){
		Point hoverItem = findElement(driver, locator).getLocation();
		((JavascriptExecutor)driver).executeScript("return window.title;");    
		idleWait(1);
		((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+(hoverItem.getY()-400)+");"); 
	}
	
	public void scrollTillEndOfPage(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
	}
	
	public int getScrollPositionOfCurrentPage(WebDriver driver) {
		waitForPageLoaded(driver);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Long longValue = (Long) executor.executeScript("return window.pageYOffset;");
		int intPosition = longValue.intValue(); 
		return intPosition;
	}
	
	public boolean isScrollPresentAtTopOfPage(WebDriver driver) {
		waitForPageLoaded(driver);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Long value = (Long) executor.executeScript("return window.pageYOffset;");
		return (value == 0);
	}
	
	public String getElementsText(WebDriver driver, By locator){
		try {
			return findElement(driver, locator).getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
			return null;
		}
	}

	public String getElementsText(WebDriver driver, WebElement element) {
		try{
			waitUntilVisible(driver, element);
			return element.getText();
		}
		catch(Exception e){
			e.getMessage();
			Assert.fail();
			return null;
		}
	}
	public void waitForElementEnabled(WebDriver driver, By locator) {
	    try {
	    	WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
	    	wait.until((ExpectedCondition<Boolean>) driverToWait -> findElement(driver, locator).isEnabled());
	    } catch (Exception e) {
	    	System.out.println("Timed out waiting for element enabled");
	    }
	}
	
	public void waitForElementAttributeContainsString(WebDriver driver, By locator, ElementAttributes attribute, String expectedString) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			ExpectedCondition<Boolean> elementAttributeContainsString = arg0 -> findElement(driver, locator).getAttribute(attribute.displayName)
					.contains(expectedString);
			wait.until(elementAttributeContainsString);
		}
		catch (Exception e) {
			System.out.println("Exception occured" + e);
		}
	}			

	public void waitUntiTextAppearsInListElements(WebDriver driver, By locator){
		try {
			idleWait(1);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
			ExpectedCondition<Boolean> elementTextAppearsInList = arg0 -> getInactiveElements(driver, locator).get(0).getText().length() > 0;
			wait.until(elementTextAppearsInList);
		}
		catch (Exception e) {
			System.out.println("Exception occured" + e);
		}
	}
	
	public String getAttribue(WebDriver driver, By locator, ElementAttributes attribute){
		try{
			return findElement(driver, locator).getAttribute(attribute.displayName.toString());
		}
		catch(Exception e){
			System.out.println("Not able to fetch attribute due to error" + e.getMessage());
			Assert.fail();
			return null;
		}
	}
	
	public String getAttribue(WebDriver driver, WebElement element, ElementAttributes attribute){
		try{
			return element.getAttribute(attribute.displayName.toString());
		}
		catch(Exception e){
			System.out.println("Not able to fetch attribute due to error" + e.getMessage());
			Assert.fail();
			return null;
		}
	}
	
	public List<String> getAttributeValueList(WebDriver driver, By locator, ElementAttributes attribute) {
		
		List<String> textList = new ArrayList<String>();
		try {
			for(int i=0;i<driver.findElements(locator).size();i++) {
				String value = driver.findElements(locator).get(i).getAttribute(attribute.toString());
				textList.add(value);
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
		return textList;
	}
	
	public String getCssValue(WebDriver driver, WebElement element, CssValues cssValue){
		try{
			return element.getCssValue(cssValue.displayName.toString());
		}
		catch(Exception e){
			System.out.println("Not able to fetch Css Value due to error" + e.getMessage());
			Assert.fail();
			return null;
		}
	}
	
	public String getAllCssValueOfAnElemenet(WebDriver driver, WebElement element) {
		String script = "var s = ''; var o = getComputedStyle(arguments[0]); for(var i = 0; i < o.length; i++){ s+=o[i] + ':' + o.getPropertyValue(o[i])+';';} return s;";
		String  allCssValues = ((JavascriptExecutor) driver).executeScript(script, element).toString();
		return allCssValues;
	}

	public void switchToTab(WebDriver driver, int tabNumber){
		try{
			idleWait(1);
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			if(tabNumber<=tabs.size())
			{
				// change focus to new tab
				System.out.println(tabs.get(tabNumber-1));
				driver.switchTo().window(tabs.get(tabNumber-1));
				System.out.println("Switched to " + driver.getTitle());
			}
			else
			{
				System.out.println("No such tab exists");
				return;
			}
			isSpinnerWheelInvisible(driver);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void switchToTabWindow(WebDriver driver, String windowHandle) {
		try {
			driver.switchTo().window(windowHandle);
			System.out.println("Switched to " + driver.getTitle());
			isSpinnerWheelInvisible(driver);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void switchToSFTab(WebDriver driver, int tabNumber){
		try{
			idleWait(2);
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			if(tabNumber<=tabs.size())
			{
				// change focus to new tab
				System.out.println("Switched to " + driver.getTitle());
				driver.switchTo().window(tabs.get(tabNumber-1));
				waitForPageLoaded(driver);
				System.out.println("Switched to " + driver.getTitle());
				
				//closing any other tabs
//				if(!isElementVisible(driver, salesForceTitle, 5)){
//					closeTab(driver);
//					switchToTab(driver, 1);
//				}
			}
			else
			{
				System.out.println("No such tab exists");
				return;
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void closeTabUsingTitle(WebDriver driver, String windowTitle){
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		String currentWindow = driver.getWindowHandle();
		try {
			for (int i = 0; i < tabs.size(); i++) {
				// change focus to new tab
				if (driver.switchTo().window(tabs.get(i)).getTitle().contains(windowTitle)) {
					driver.close();
					driver.switchTo().window(currentWindow);
					return;
				}
			}
			driver.switchTo().window(currentWindow);
			System.out.println("No such tab exists");
			return;
		} catch (Exception e) {
			driver.switchTo().window(currentWindow);
			System.out.println(e.getMessage());
		}
	}

	
	/** This method uses the windowTitleContains method to switch
	 * @param title The title of the window
	 * @param driver 
	 * @return
	 */
	public WebDriver switchToWindowTitleContains(String title, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		return wait.until(windowTitleContains(title));
	}
	
	
	
	/** This method switches to a window containing the specified title
	 * @param winTitle The title of the window
	 * 
	 */
	public ExpectedCondition<WebDriver> windowTitleContains(final String winTitle) {
		return new ExpectedCondition<WebDriver>() {
			@Override
			public WebDriver apply(WebDriver driver) {
				return getWindowHandleByTitle(driver, winTitle);
			}
		};
	}
	
	//This method gets window handle based on the title
	public WebDriver getWindowHandleByTitle(WebDriver driver, String winTitle) {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String handle : winHandles) {
				WebDriver winHandle = driver.switchTo().window(handle);
				if (winHandle.getTitle().contains(winTitle)) {
					return winHandle;
				}
			}
		} catch(UnhandledAlertException uaex) {
			try {
				driver.switchTo().alert().dismiss();
			} catch (Exception ex) {}
			return null;
		}
		return null;
	}
	
	/** These methods are for switching to extension and closing extensions
	 * @param driver 
	 * @return
	 */
	public void switchToExtension(WebDriver driver) {
		switchToTab(driver, getTabCount(driver));
		isElementInvisible(driver, spinnerWheel, 60);
		driver.manage().window().maximize();
	}

	public void closeExtensionAndSwitchDefault(WebDriver driver, String parentWindow){
		driver.close();
		driver.switchTo().window(parentWindow);
	}
	
	public void acceptAlert(WebDriver driver) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public String gettAlertsText(WebDriver driver) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.alertIsPresent());
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
			return null;
		}

	}

	public int getTabCount(WebDriver driver) {
		try {
			return driver.getWindowHandles().size();
		} catch (Exception e) {
			System.out.println(e.getMessage());;
			Assert.fail();
			return 0;
		}
	}

	public void closeTab(WebDriver driver) {
		try {
										   
			if (driver.getWindowHandles().size() > 1 && !(driver.getTitle().equals("ringDNA (qa)") || driver.getTitle().equals("ringDNA"))) {
				HelperFunctions.captureScreenshot(driver, "Closing Tab " + driver.getTitle(), 1);
				System.out.println("Tab Closed " + driver.getTitle());
				driver.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			;
			Assert.fail();
		}
	}

	public void clickEnter(WebDriver driver, By locator){
		try {
			WebElement element = findElement(driver, locator);
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void clearAll(WebDriver driver, By locator){
		try {
			WebElement element = findElement(driver, locator);
			element.sendKeys(Keys.chord(Keys.CONTROL,"a"));
			element.sendKeys(Keys.BACK_SPACE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void clearAll(WebDriver driver, WebElement element){
		try {
			element.sendKeys(Keys.chord(Keys.CONTROL,"a"));
			element.sendKeys(Keys.BACK_SPACE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
	
	public void enterBackspace(WebDriver driver, By locator){
		try {
			WebElement element = findElement(driver, locator);
			element.sendKeys(Keys.BACK_SPACE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void enterBackspace(WebDriver driver, WebElement element){
		try {
			element.sendKeys(Keys.BACK_SPACE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	public void openNewBlankTab(WebDriver driver){
		((JavascriptExecutor)driver).executeScript("window.open();");
	}

	public void idleWait(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (Exception e) {
			System.out.println("Not able to wait due to following error =" + e);
		}
	}
	
	public void idleWaitInMs(int ms){
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			System.out.println("Not able to wait due to following error =" + e);
		}
	}

	// This method will return text list from the given locator of webelement list
	public List<String> getTextListFromElements(WebDriver driver, By locator) {
		if (getWebelementIfExist(driver, locator) == null) {
			return null;
		}
		
		// handling stale element conditions
		List<String> textList = new ArrayList<String>();
		for (int i = 1; i <= 3; i++) {
			try {
				isListElementsVisible(driver, locator, 5);
				List<WebElement> list = getInactiveElements(driver, locator);
				for (WebElement element : list) {
					if(Strings.isNotNullAndNotEmpty(element.getText())){
						textList.add(element.getText());
						
					}
				}
				break;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				idleWait(1);
			}
		}
		return textList;
	}

	public boolean isListSameAfterSorting(List<String> list1, List<String> list2) {
		Collections.sort(list1);
		Collections.sort(list2);
		return list1.equals(list2);
	}

	//hover an element
	public void hoverElement(WebDriver driver, By locator) {
		Actions action = new Actions(driver);
		try {
			action.moveToElement(findElement(driver, locator)).perform();
		
		} catch (Exception e) {
			System.out.println("Not able to hover on element due to following error = "+ e);		}
	}
	
	public void hoverElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		try {
			action.moveToElement(element).perform();
		
		} catch (Exception e) {
			System.out.println("Not able to hover on element due to following error = "+ e);		}
	}

	public void moveAndClick(WebDriver driver, By locator) {
		Actions action = new Actions(driver);
		try {
			action.moveToElement(findElement(driver, locator));
			idleWait(1);
			action.click().build().perform();
		} catch (Exception e) {
			System.out.println("Not able to click on element due to following error = "+ e);		}
	}

	public void clickElementCordinates(WebDriver driver, By locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = findElement(driver, locator);
		try {
			action.click(element).moveByOffset(x, y);
			action.click().build().perform();
		} catch (Exception e) {
			System.out.println("Not able to click on element due to following error = "+ e);		}
	}
	
	public void clickAndHold(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		try {
			action.clickAndHold(element).build().perform();
		} catch (Exception e) {
			System.out.println("Not able to click on element due to following error = "+ e);
		}
	}

	public void executeJavaSctipt(WebDriver driver, String javaScript){
		((JavascriptExecutor) driver).executeScript(javaScript);
	}

	public void clickByJs(WebDriver driver, By locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("return arguments[0].click();", findElement(driver, locator));
	}
	
	public void clickByJs(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("return arguments[0].click();", element);
	}										  

	public void reloadSoftphone(WebDriver driver) {
		driver.navigate().refresh();
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("No alert took place");
		}
		waitUntilInvisible(driver, spinnerWheel);

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 0)", new Object[0]);
		if (getWebelementIfExist(driver, By.cssSelector("img.keypad")) == null || (getWebelementIfExist(driver, By.cssSelector("img.keypad")) != null && !(getWebelementIfExist(driver, By.cssSelector("img.keypad")).isDisplayed()))) {
			System.out.println("Browser is not loaded properly");
		}
	}

	public void selectFromDropdown(WebDriver driver, By selectDropdownLoc, SelectTypes selectType, String text) {
		try {
			WebElement dropdown = findElement(driver, selectDropdownLoc);
			Select selectDropdown = new Select(dropdown);

			if (selectType == SelectTypes.visibleText) {
				selectDropdown.selectByVisibleText(text);
			} else if (selectType == SelectTypes.value) {
				selectDropdown.selectByValue(text);
			}
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param driver
	 * @param locator
	 * @return List<String>
	 * @Desc: get text from drop down list
	 */
	public List<String> getElementsTextFromDropDownList(WebDriver driver, By locator) {
		Select dropdown = new Select(findElement(driver, locator));
		List<String> dropDownList = new ArrayList<String>();
		
		// Get all options
		List<WebElement> optionsList = dropdown.getOptions();

		// Loop to print one by one
		for (int loop = 0; loop < optionsList.size(); loop++) {
			dropDownList.add(loop, (optionsList.get(loop).getText()));
		}
		return dropDownList;
	}
	
	public List<WebElement> getAllSelectOptions(WebDriver driver, By selectDropdownLoc) {
		try {
			WebElement dropdown = findElement(driver, selectDropdownLoc);
			Select selectDropdown = new Select(dropdown);
			List<WebElement> allOptions = selectDropdown.getOptions();
			return allOptions;
		}catch (Exception e) {
			System.out.println("Not able to get elements from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getSelectedValueFromDropdown(WebDriver driver, By selectDropdownLoc) {
		try {
			WebElement dropdown = findElement(driver, selectDropdownLoc);
			Select selectDropdown = new Select(dropdown);

			return selectDropdown.getFirstSelectedOption().getText().trim();
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getSelectedValueFromDropdown(WebDriver driver, WebElement selectDropdown) {
		try {
			Select dropdown = new Select(selectDropdown);
			return dropdown.getFirstSelectedOption().getText().trim();
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public void selectFromDropdown(WebDriver driver, WebElement selectDropdownLoc, SelectTypes selectType, String text) {
		try {
			Select selectDropdown = new Select(selectDropdownLoc);

			if (selectType == SelectTypes.visibleText) {
				selectDropdown.selectByVisibleText(text);
			} else if (selectType == SelectTypes.value) {
				selectDropdown.selectByValue(text);
			}
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getSelectClassDefaultValue(WebDriver driver, By selectDropDownLoc) {
		Select selectDropdown = new Select(findElement(driver, selectDropDownLoc));
		return selectDropdown.getFirstSelectedOption().getAttribute(ElementAttributes.value.displayName);
	}

	//Verify token exists in List
	public boolean isTextPresentInList(WebDriver driver, List<WebElement> list, String token) {
		for (int j = 0; j < list.size(); j++) {
			if (list.get(j).getText().toString().trim().equals(token.trim())) {
				return true;
			}
		}
		return false;
	}
	
	//Verify token exists in List of String
	public boolean isTextPresentInStringList(WebDriver driver, List<String> list, String token) {
		return list.contains(token);
	}

	//Verify token exists within a List content
	public boolean isListContainsText(WebDriver driver, List<WebElement> list,	String token) {
		for (int j = 0; j < list.size(); j++) {
			if (list.get(j).getText().toString().trim().contains(token.trim())) {
				return true;
			}
		}
		return false;
	}

	//Verify all List contents contains token
	public boolean verifyAllListItemsContains(WebDriver driver, List<String> list, String token) {
		for (String str : list) {
	        if (!str.toLowerCase().contains(token)) {
	            return false;
	        }
	    }
	    return true;
	}

	public boolean isAllWebElementsListContainsText(List<WebElement> list, String token) {
		for (WebElement element : list) {
	        if (!element.getText().toLowerCase().contains(token.toLowerCase())) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public List<String> convertListToUpperCase(List<String> inList) {
		return inList.stream().map(String::toUpperCase).collect(Collectors.toList());
	}
	
	public List<String> convertListToLowerCase(List<String> inList) {
		return inList.stream().map(String::toLowerCase).collect(Collectors.toList());
	}
	
	public Select selectFromDropdownList(WebDriver driver, By dropdown, SelectTypes selectType, String text) {
		Select selectDropdown = null; 
		try {
			for (int i = 0; i <= getElements(driver, dropdown).size() - 1; i++) {
				selectDropdown = new Select(getElements(driver, dropdown).get(i));
				if (selectType == SelectTypes.visibleText) {
					selectDropdown.selectByVisibleText(text);
				} else if (selectType == SelectTypes.value) {
					selectDropdown.selectByValue(text);
				}
			}
		} catch (Exception e) {
			System.out.println("Not able to select element from the Dropdown due to error " + e.getMessage());
			e.printStackTrace();
		}
		return selectDropdown;
	}

	public boolean isFileDownloaded(String downloadPath, String startFileName, String extension) {
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();
		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].getName().startsWith(startFileName) && dirContents[i].getName().endsWith(extension) && dirContents[i].length() > 0) {
				// File has been found, it can now be deleted:
				dirContents[i].delete();
				return true;
			}
		}return false;
	}
	
	public boolean isExtensionFileDownloaded(String downloadPath, String extension) {
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();
		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].getName().endsWith(extension) && dirContents[i].length() > 0) {
				// File has been found, it can now be deleted:
				dirContents[i].delete();
				return true;
			}
		}
		return false;
	}
	
	/**wait for file to download with complete name and extension
	 * @param driver
	 * @param downloadPath
	 * @param startFileName
	 * @param extension
	 */
	public void waitForFileToDownload(WebDriver driver, String downloadPath, String startFileName, String extension) {
		String fullPathToFile = new StringBuilder(downloadPath).append("//").append(startFileName).append(extension)
				.toString();
		File file = new File(fullPathToFile);

		try {
			FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
			Duration timeOut = Duration.ofSeconds(5000);
			Duration pollingTimeOut = Duration.ofSeconds(2);
			fluentWait.withTimeout(timeOut).pollingEvery(pollingTimeOut);
			fluentWait.until((webDriver) -> file.exists());
		} catch (Exception e) {
			System.out.println("File not exists after waiting for given time in seconds:" + 5000);
		}
	}

	/**wait for file to download with partial name and extension name
	 * @param downloadPath
	 * @param startFileName
	 * @param extension
	 */
	public void waitForFileToDownloadWithPartialName(String downloadPath, String startFileName, String extension) {

		File dir = new File(downloadPath);
		int attempts = 1;
		while (attempts <= 3) {
			File[] dirContents = dir.listFiles();
			for (int i = 0; i < dirContents.length; i++) {
				if (dirContents[i].getName().startsWith(startFileName) && dirContents[i].getName().endsWith(extension)) {
					return;
				}
			}
			idleWait(5);
			attempts++;
		}
	}
	
	
	/**get file which is download with partial name and extension name
	 * @param downloadPath
	 * @param startFileName
	 * @param extension
	 * @return
	 */
	public File getFileDownloadedWithPartialName(String downloadPath, String startFileName, String extension) {

		File dir = new File(downloadPath);
		int attempts = 1;
		while (attempts <= 3) {
			File[] dirContents = dir.listFiles();
			for (int i = 0; i < dirContents.length; i++) {
				if (dirContents[i].getName().startsWith(startFileName) && dirContents[i].getName().endsWith(extension)) {
					return dirContents[i];
				}
			}
			idleWait(5);
			attempts++;
		}
		return null;
	}
	
	/**wait for file to download with extension name
	 * @param downloadPath
	 * @param startFileName
	 * @param extension
	 */
	public void waitForFileToDownloadWithExtension(String downloadPath, String extension) {

		File dir = new File(downloadPath);
		int attempts = 1;
		while (attempts <= 3) {
			File[] dirContents = dir.listFiles();
			for (int i = 0; i < dirContents.length; i++) {
				if (dirContents[i].getName().endsWith(extension)) {
					return;
				}
			}
			idleWait(5);
			attempts++;
		}
	}

	public void waitUntillNumberOfElementsAreMore(WebDriver driver, By locator, int expectedCount) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, expectedCount));
	}

	public void waitUntillNumberOfElementsAreEquals(WebDriver driver, By locator, int expectedCount) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
		wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
	}

	public void clickAndSelectFromDropDown(WebDriver driver, By dropDownBox,  By dropDownItems, String itemToSearch){
		waitUntilVisible(driver, dropDownBox);
		clickElement(driver, dropDownBox);
		idleWait(2);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, dropDownItems);
		for (WebElement searchedItem : searchedDropDownItems) {
			if(searchedItem.getText().trim().contains(itemToSearch.trim())){
				searchedItem.click();
				break;
			}
		}
	}
	
	/**
	 * @param driver
	 * This method waits for complete page load and refreshes if not loaded
	 */
	public void waitForPageLoaded(WebDriver driver) {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            idleWait(1);
            waitUntilInvisible(driver, spinnerWheel);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println("Timeout waiting for Page Load Request to complete.");
        }
    }
	
	public void closeRecommendationWindow(WebDriver driver) {
		if(isElementVisible(driver, recommendationWindowFrame, 0)) {
			switchToIframe(driver, recommendationWindowFrame);
			if (isElementVisible(driver, recommendationWindowClose, 0)) {
				clickElement(driver, recommendationWindowClose);
			}
			driver.switchTo().defaultContent();
		}
	}
	
	public void refreshCurrentDocument(WebDriver driver) {
		driver.switchTo().defaultContent();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.location.reload(true);");
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("No alert took place");
		}
		
		waitForPageLoaded(driver);
		closeRecommendationWindow(driver);
		isSpinnerWheelInvisible(driver);
	}
	
	public void enterTextAndSelectFromDropDown(WebDriver driver, By dropDownBox, By dropDownTextBox,  By dropDownItems, String itemToSearch){
		waitUntilVisible(driver, dropDownBox);
		clickElement(driver, dropDownBox);
		enterBackspace(driver, dropDownTextBox);
		enterText(driver, dropDownTextBox, itemToSearch);
		idleWait(2);
		List<WebElement> searchedDropDownItems = getInactiveElements(driver, dropDownItems);
		for (WebElement searchedItem : searchedDropDownItems) {
			if(searchedItem.getText().trim().contains(itemToSearch.trim())){
				searchedItem.click();
				break;
			}
		}
	}
	
	public void pressEscapeKey(WebDriver driver){
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
	}
	
	public void pressUpArrowKey(WebDriver driver){
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_UP).build().perform();
	}
	
	public void pressDownArrowKey(WebDriver driver){
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN).build().perform();
	}
	
	public void longPressElement(WebDriver driver, By locator){
		Actions action = new Actions(driver);
		action.clickAndHold(findElement(driver, locator)).pause(60).build().perform();
	}
	
	/**
	 * @param driver
	 * @param sourceLocator
	 * @param destinationLocator
	 * 
	 * This method is used to drag and drop an element from a source to a destination
	 */
	public void dragAndDropElement(WebDriver driver, By sourceLocator, By destinationLocator){
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, sourceLocator);
		Actions actions = new Actions(driver);
		scrollToElement(driver, destinationLocator);
		executeJavaSctipt(driver, "window.scrollBy(0,50)");
		idleWait(1);
		/*actions.dragAndDrop(findElement(driver, sourceLocator), findElement(driver, destinationLocator)).build().perform();*/
		actions.clickAndHold(findElement(driver, sourceLocator)).build().perform();
		actions.moveToElement(findElement(driver, destinationLocator)).build().perform();
		actions.release().build().perform();
		isElementVisible(driver, spinnerWheel, 1);
		idleWait(1);
	}
	
	/**
	 * @param driver
	 * @param sourceLocator
	 * @param destinationLocator
	 * 
	 * This method is used to drag and drop an element from a source to a destination
	 */
	public void dragAndDropElement(WebDriver driver, WebElement sourceLocator, WebElement destinationLocator){
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, sourceLocator);
		Actions actions = new Actions(driver);
		scrollToElement(driver, destinationLocator);
		executeJavaSctipt(driver, "window.scrollBy(0,50)");
		idleWait(1);
		/*actions.dragAndDrop(findElement(driver, sourceLocator), findElement(driver, destinationLocator)).build().perform();*/
		actions.clickAndHold(sourceLocator).build().perform();
		actions.moveToElement(destinationLocator).build().perform();
		actions.release().build().perform();
		isElementVisible(driver, spinnerWheel, 1);
		idleWait(1);
	}
	
	public void dragAndDropElementWithOffSet(WebDriver driver, WebElement sourceLocator, WebElement destinationLocator, int xOffset, int yOffset){
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, sourceLocator);
		Actions actions = new Actions(driver);
		scrollToElement(driver, destinationLocator);
		executeJavaSctipt(driver, "window.scrollBy(0,50)");
		idleWait(1);
		actions.clickAndHold(sourceLocator).build().perform();
		actions.moveToElement(destinationLocator).moveByOffset(xOffset, yOffset).build().perform();
		actions.release().build().perform();
		idleWait(1);
	}
	
	/**
	 * @param driver
	 * @param sourceLocator
	 * @param destinationLocator
	 * 
	 * This method is used to drag and drop an element from a source to a destination
	 */
	public void dragAndDropElement(WebDriver driver, By sourceLocator, int xOffset, int yOffset){
		waitUntilVisible(driver, sourceLocator);
		Actions actions = new Actions(driver);
		idleWait(1);
		actions.clickAndHold(findElement(driver, sourceLocator)).build().perform();
		actions.moveByOffset(xOffset, yOffset);
		actions.release().build().perform();
		idleWait(1);
	}
	
	public void slideElement(WebDriver driver, WebElement sliderBar, WebElement fromRangeSlider, WebElement toRangeSlider, int fromRange, int toRange){
		float width = sliderBar.getSize().getWidth();
		
		float minLimit =Integer.parseInt(fromRangeSlider.getAttribute("aria-valuemin"));
		float maxLimit = Integer.parseInt(toRangeSlider.getAttribute("aria-valuemax"));
		float gapInPixel = (width / (maxLimit - minLimit));
		
		
		Actions actions = new Actions(driver);
		actions.dragAndDropBy(fromRangeSlider, (int) (fromRange * gapInPixel), 0).build().perform();;
		actions.dragAndDropBy(toRangeSlider, (int) (((maxLimit - toRange) * -1) * gapInPixel), 0).build().perform();
	}
	
  public void enterTextandSelect(WebDriver driver,By locator,String agent){
		clickElement(driver, locator);
		enterText(driver, locator, agent);
		idleWait(2);
		clickEnter(driver, locator);
	}
	
	/**
	 * @param date (keep format as MM/dd/yyyy)
	 */
	public void selectDateFromDatePicker(WebDriver driver, String date){
		By monthSwitcher	= By.cssSelector(".datepicker-days .datepicker-switch");
		By yearSwitcher		= By.cssSelector(".datepicker-months .datepicker-switch");
		By prevDecade		= By.cssSelector(".datepicker-years .prev");
		By nextDecade		= By.cssSelector(".datepicker-years .next");
		By yearsList		= By.cssSelector(".datepicker-years .year");
		By monthList		= By.cssSelector(".datepicker-months .month");
		By dayList			= By.cssSelector(".datepicker-days .day:not(.old):not(.new)");
		
		clickElement(driver, monthSwitcher);
		clickElement(driver, yearSwitcher);
		int month	= Integer.parseInt(date.split("/")[0]);
		int day		= Integer.parseInt(date.split("/")[1]);
		int year	= Integer.parseInt(date.split("/")[2]);
		
		//select year
		for (int i = 0; i < 5; i++) {
			List<WebElement> yearsElements = getElements(driver, yearsList);
			List<String> years = getTextListFromElements(driver, yearsList);
			if(year >= Integer.parseInt(years.get(0)) && year <= Integer.parseInt(years.get(years.size() - 1))){
				yearsElements.get(years.indexOf(String.valueOf(year))).click();;
				break;
			}else if(year > Integer.parseInt(years.get(years.size() - 1))){
				clickElement(driver, nextDecade);
				continue;
			}else if(year < Integer.parseInt(years.get(0))){
				clickElement(driver, prevDecade);
				continue;
			}
			Assert.fail();
		}
		
		//select month
		List<WebElement> monthsList = getElements(driver, monthList);
		monthsList.get(month-1).click();
		
		//select day
		List<WebElement> daysList = getElements(driver, dayList);
		daysList.get(day - 1).click();
	}
	
	//Switch to iframe
	public void switchToIframe(WebDriver driver,By locator){
		driver.switchTo().frame(findElement(driver, locator));
		idleWait(2);
	}
	
	//Clear browser cache
	public void clearBrowserCache(WebDriver driver) {
		driver.manage().deleteAllCookies();
		String winHandleBefore = driver.getWindowHandle();
		openNewBlankTab(driver);
		switchToTab(driver, getTabCount(driver));
		driver.get("chrome://settings/clearBrowserData");
		driver.findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);
		closeTab(driver);
		switchToTabWindow(driver, winHandleBefore);
	}
}
