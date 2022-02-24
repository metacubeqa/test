/**
 * @author Abhishek Gupta
 *
 */
package softphone.source;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.SeleniumBase;
import softphone.source.callTools.CallToolsPanel;
import softphone.source.callTools.SoftPhoneNewTask;

public class SoftphoneTodaysTasksPage extends SeleniumBase{
	CallToolsPanel callToolsPanel	= new CallToolsPanel();
	
	//********Svg icons outer html starts here********//
	String emailIcon				= "<svg width=\"22\" height=\"22\" viewBox=\"0 0 22 16\" fill=\"none\"><path d=\"M.733.5h20.534c.128 0 .233.104.233.233v14.534a.233.233 0 01-.233.233H.733a.233.233 0 01-.233-.233V.733C.5.604.604.5.733.5z\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path><path d=\"M.5.5l9.74 8.35a1.167 1.167 0 001.52 0L21.5.5\" stroke=\"#FFFFFF\"></path></svg>";
	String callIcon					= "<svg width=\"30\" height=\"30\" viewBox=\"0 0 24 24\" fill=\"none\"><path clip-rule=\"evenodd\" d=\"M19.647 16.75a.5.5 0 00-.093-.577L17.61 14.23a.5.5 0 00-.611-.075l-1.267.76a.438.438 0 01-.352.05c-.507-.152-1.99-.712-3.703-2.425-1.713-1.713-2.274-3.196-2.426-3.703a.438.438 0 01.05-.352l.76-1.267a.5.5 0 00-.074-.611L8.043 4.662a.5.5 0 00-.578-.094L5.212 5.695a.453.453 0 00-.259.407c.009 1.063.325 4.988 4.249 8.912 3.924 3.924 7.848 4.24 8.912 4.248a.453.453 0 00.407-.259l1.126-2.253z\" stroke=\"#FFFFFF\"></path></svg>";
	String genericIcon				= "<svg width=\"18\" height=\"18\" viewBox=\"0 0 20 20\" fill=\"none\"><path d=\"M1.2 2h17.6c.11 0 .2.09.2.2v16.6a.2.2 0 01-.2.2H1.2a.2.2 0 01-.2-.2V2.2c0-.11.09-.2.2-.2zM5 1v2M15 1v2\" stroke=\"#FFFFFF\" stroke-linecap=\"round\"></path><path d=\"M15.05 6.93l-6.787 6.788L6 11.455\" stroke=\"#FFFFFF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	String uncheckedCheckBox		= "<svg class=\"MuiSvgIcon-root\" focusable=\"false\" viewBox=\"0 0 24 24\" aria-hidden=\"true\"><path d=\"M19 5v14H5V5h14m0-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z\"></path></svg>";
	String checkedCheckBox			= "<svg class=\"MuiSvgIcon-root\" focusable=\"false\" viewBox=\"0 0 24 24\" aria-hidden=\"true\"><path d=\"M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z\"></path></svg>";
	String contactIconHtml			= "<svg width=\"17\" height=\"17\" viewBox=\"0 0 17 12\" fill=\"none\"><path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M14.29 4.389a.578.578 0 01-.562.56H9.636a.556.556 0 01-.56-.56v-.825c0-.297.263-.561.56-.561h4.125c.297 0 .561.23.561.56v.826h-.033zm0 3.003a.578.578 0 01-.562.561h-2.475a.557.557 0 01-.56-.561v-.825c0-.297.264-.561.56-.561h2.475c.297 0 .561.231.561.561v.825zM7.853 9.306H3.102c-.528 0-.923-.561-.923-1.122.032-.825.89-1.32 1.782-1.716.626-.264.725-.528.725-.792s-.164-.528-.396-.726c-.362-.33-.561-.825-.561-1.353 0-1.056.627-1.914 1.716-1.914s1.716.89 1.716 1.914c0 .56-.198 1.056-.561 1.353-.231.198-.395.429-.395.726 0 .264.066.528.725.759.891.396 1.75.924 1.782 1.749.099.56-.33 1.122-.858 1.122zM14.85 0H1.65C.759 0 0 .726 0 1.65v7.92c0 .891.759 1.65 1.65 1.65h13.2c.924 0 1.65-.726 1.65-1.65V1.65C16.5.726 15.774 0 14.85 0z\" fill=\"#9D97E7\"></path></svg>";
	String leadIconHtml				= "<svg width=\"16\" height=\"16\" viewBox=\"0 0 16 15\" fill=\"none\"><path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M8 .5a2.5 2.5 0 110 5 2.5 2.5 0 010-5zM14.998 6.75H1.002c-.5 0-.687.521-.28.755l3.654 1.953c.188.104.281.286.188.468L3.22 13.727c-.157.417.5.703.874.39l3.56-3.123c.189-.183.564-.183.751 0l3.562 3.124c.343.312.999.026.874-.39L11.436 9.9c-.062-.156.031-.364.188-.469l3.655-1.952c.406-.208.218-.729-.281-.729z\" fill=\"#EA8F6A\"></path></svg>";
	String favIconHtml				= "<svg width=\"14\" height=\"14\" viewBox=\"0 0 12 13\" fill=\"none\"><path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M6 10l-3.078 1.618a.25.25 0 01-.363-.263l.588-3.428L.657 5.5a.25.25 0 01.138-.426l3.442-.5 1.539-3.119a.25.25 0 01.448 0l1.54 3.119 3.441.5a.25.25 0 01.139.426l-2.49 2.428.587 3.428a.25.25 0 01-.363.263L6 10z\" fill=\"#FDD835\" stroke=\"#FDD835\" stroke-linejoin=\"round\"></path></svg>";
	String noTaskIconHtml			= "<svg width=\"75\" height=\"75\" viewBox=\"0 0 20 20\" fill=\"none\"><path d=\"M1.2 2h17.6c.11 0 .2.09.2.2v16.6a.2.2 0 01-.2.2H1.2a.2.2 0 01-.2-.2V2.2c0-.11.09-.2.2-.2zM5 1v2M15 1v2\" stroke=\"#0066FF\" stroke-linecap=\"round\"></path><path d=\"M15.05 6.93l-6.787 6.788L6 11.455\" stroke=\"#0066FF\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path></svg>";
	//********Svg icons outer html ends here********//
	
	//********Today's tasks page locators starts here********//
	By tasksIconLink				= By.cssSelector(".react-todays-tasks");
	
	By taskOverdueCount				= By.xpath(".//span[@color='neutral' and contains(normalize-space(), 'overdue')]");
	By noTaskIcon					= By.cssSelector(".no-search-results svg");
	By noTaskText					= By.cssSelector(".no-search-results h3");
	
	By tasksRow						= By.cssSelector(".MuiPaper-elevation1");	
	By taskCheckBox					= By.cssSelector(".MuiCheckbox-root svg");
	By loadMoreButton				= By.xpath(".//button[text()='Load More']");
	
	String taskNameLocator			= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//a[@color='primary'])[1]";
	String contactNameLocator		= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//a[@color='primary'])[2]";
	String contactIconLocator		= "(((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//a[@color='primary'])[2]/*[local-name()='svg'])[1]";
	String contactFavIconLocator	= "(((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//a[@color='primary'])[2]/*[local-name()='svg'])[2]";
	String contactAccountLocator	= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//a[@color='primary'])[3]";
	String taskIconLocator			= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//*[local-name() = 'svg'])[1]";
	String taskCheckBoxLocator		= "(.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//*[@data-testid='id-dailytasks-completed-checkbox']";
	String contactTitleLocator		= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//span[@color='primary'])[1]";
	String leadCompanyLocator		= "((.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//span[@color='primary' and not(@data-testid='id-dailytasks-contact-link')])[2]";
	String taskDetailsOpenIcon		= "(.//*[contains(@class,'MuiCardContent-root')])[$$Index$$]//*[@data-testid='id-dailytasks-contact-link']";
	//********Today's tasks page locators ends here********//
	
	/**
	 * use this method to open tasks page
	 * @param driver
	 */
	public void navigateToTasksPage(WebDriver driver) {
		if(!getAttribue(driver, tasksIconLink, ElementAttributes.Class).contains("active")) {
			clickElement(driver, tasksIconLink);
		}
	}
	
	/**
	 * Use this method to verify that tasks page is blank by verifying no tasks image and message
	 * @param driver
	 */
	public void verifyNoTasksPage(WebDriver driver) {
		assertEquals(getAttribue(driver, noTaskIcon, ElementAttributes.outerHTML), noTaskIconHtml);
		assertEquals(getElementsText(driver, noTaskText), "Well done, you have no more tasks today.");
	}
	
	/**
	 * user this method to get the number of overdue tasks from the label
	 * @param driver
	 * @return integer number of due tasks
	 */
	public int getTaskOverdueCount(WebDriver driver) {
		String overdueCount = getElementsText(driver, taskOverdueCount).split(" ")[0];
		return Integer.parseInt(overdueCount);
	}
	
	/**
	 * use this method to get the total number of visible tasks listed on todays tasks page
	 * @param driver
	 * @return integer number of tasks listed on todays tasks page
	 */
	public int getTotalTasks(WebDriver driver) {
		if(isElementVisible(driver, taskCheckBox, 10)) {
			List<WebElement> taskRows = getElements(driver, tasksRow);
			return taskRows.size();
		}else {
			return 0;
		}
	}
	
	/**
	 * use this method to get the task row index(starts with 1) in the today's list page based on name of the task
	 * @param driver
	 * @param taskName String name of the task for which the index needs to be found
	 * @return integer index of the row(starts with 1)
	 */
	public int getTaskRowIndex(WebDriver driver, String taskName) {
		if(isElementVisible(driver, tasksRow, 10)) {
			List<WebElement> taskRows = getElements(driver, tasksRow);
			for (int i = 1; i <= taskRows.size(); i++) {
				String taskTitle = getElementsText(driver, By.xpath(taskNameLocator.replace("$$Index$$", String.valueOf(i))));
				if(taskTitle.equals(taskName)) {
					return i;
				}
			}
			return -1;
		}else {
			return -1;
		}
	}
	
	/**
	 * use this method to verify the tasks icon based on it's type
	 * @param driver
	 * @param taskName String task name for which the icon needs to be verify
	 * @param type
	 */
	public void verifyTaskIcon(WebDriver driver, String taskName, SoftPhoneNewTask.TaskTypes type) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		WebElement taskRow = findElement(driver, By.xpath(taskIconLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
		String taskIconOuterHtml = getAttribue(driver, taskRow, ElementAttributes.outerHTML);
		String taskIconcolor = getAttribue(driver, taskRow.findElement(By.xpath("..")), ElementAttributes.color);
		assertTrue(taskIconcolor.equals("#33CC99") || taskIconcolor.equals("#DBDBDB"));
		switch (type) {
		case Email:
			assertEquals(taskIconOuterHtml, emailIcon);
			break;
		case Call:
			assertEquals(taskIconOuterHtml, callIcon);
			break;
		case Other:
		case SMS:
			assertEquals(taskIconOuterHtml, genericIcon);
			break;
		default:
			Assert.fail();
			break;
		}
	}
	
	/**
	 * use this method to verify that the task is completed
	 * @param driver
	 * @param taskName name of the task for which needs to be verified if completed
	 */
	public void verifyTaskIsCompleted(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		WebElement taskRow = getElements(driver, tasksRow).get(taskRowIndex-1);
		String taskIconOuterHtml = getAttribue(driver, taskRow.findElement(taskCheckBox), ElementAttributes.outerHTML);
		
		assertEquals(getCssValue(driver, taskRow, CssValues.BackgroundColor), "rgba(255, 255, 255, 1)");
		assertEquals(taskIconOuterHtml, checkedCheckBox);
		assertTrue(findElement(driver, By.xpath(taskCheckBoxLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))).isSelected());
	}
	
	/**
	 * use this method to verify that the task is not completed
	 * @param driver
	 * @param taskName name of the task for which needs to be verified if not completed
	 */
	public void verifyTaskIsNotCompleted(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		WebElement taskRow = getElements(driver, tasksRow).get(taskRowIndex-1);
		String taskIconOuterHtml = getAttribue(driver, taskRow.findElement(taskCheckBox), ElementAttributes.outerHTML);
		
		assertEquals(getCssValue(driver, taskRow, CssValues.BackgroundColor), "rgba(255, 255, 255, 1)");
		assertEquals(taskIconOuterHtml, uncheckedCheckBox);
		assertFalse(findElement(driver, By.xpath(taskCheckBoxLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))).isSelected());
	}
	
	/**
	 * use this method to check the completed checkbox for the task
	 * @param driver
	 * @param taskName name of the task that needs to be marked completed
	 */
	public void markTaskComplete(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		WebElement taskRow = getElements(driver, tasksRow).get(taskRowIndex-1);
		if(!findElement(driver, By.xpath(taskCheckBoxLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))).isSelected()) {
			scrollToElement(driver, taskRow.findElement(taskCheckBox));
			clickByJs(driver, taskRow.findElement(taskCheckBox).findElement(By.xpath("..")));
		}else {
			System.out.println("Task is already marked complete");
		}
	}
	
	/**
	 * use this method to uncheck the completed checkbox for the task
	 * @param driver
	 * @param taskName name of the task that needs to be marked not completed
	 */
	public void markTaskNotComplete(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		WebElement taskRow = getElements(driver, tasksRow).get(taskRowIndex-1);
		if(findElement(driver, By.xpath(taskCheckBoxLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))).isSelected()) {
			scrollToElement(driver, taskRow.findElement(taskCheckBox));
			clickByJs(driver, taskRow.findElement(taskCheckBox).findElement(By.xpath("..")));
		}else {
			System.out.println("Task is already not completed");
		}
	}
	
	/**
	 * use this method to click on the task and switch to the opened sfdc windows tab
	 * @param driver
	 * @param taskName name of the task that needs to be clicked on
	 */
	public void openTaskInSFDC(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		clickByJs(driver, By.xpath(taskNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
		switchToSFTab(driver, getTabCount(driver));
	}
	
	/**
	 * use this method to click on the task's contact and switch to the opened sfdc windows tab
	 * @param driver
	 * @param taskName name of the task whose associated contact needs to be clicked on
	 */
	public void openTaskContactInSFDC(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		clickByJs(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
		switchToSFTab(driver, getTabCount(driver));
	}
	
	/**
	 * use this method to click on the task's contact's account and switch to the opened sfdc windows tab
	 * @param driver
	 * @param taskName name of the task whose associated contact's account needs to be clicked on
	 */
	public void openTaskAccountInSFDC(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		clickByJs(driver, By.xpath(contactAccountLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
		switchToSFTab(driver, getTabCount(driver));
	}
	
	/**
	 * Use this method to open contact detail of task's associated contact
	 * @param driver
	 * @param taskName name of the task whose associated contact's details needs to be open
	 */
	public void openTaskContactInfo(WebDriver driver, String taskName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		clickByJs(driver, By.xpath(taskDetailsOpenIcon.replace("$$Index$$", String.valueOf(taskRowIndex))));
	}
	
	/**
	 * Use this method to verify associated contact details of the task
	 * @param driver
	 * @param taskName name of the task whose associated contact's details needs to be verified
	 * @param contactName expected contact name
	 * @param accountName expected account name
	 */
	public void verifyContactDetail(WebDriver driver, String taskName, String contactName, String accountName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		assertEquals(getElementsText(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), contactName);
		assertEquals(getAttribue(driver, By.xpath(contactIconLocator.replace("$$Index$$", String.valueOf(taskRowIndex))), ElementAttributes.outerHTML), contactIconHtml);
		assertEquals(getElementsText(driver, By.xpath(contactAccountLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), accountName);
		assertEquals(getElementsText(driver, By.xpath(contactTitleLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), "QA");
		waitUntilInvisible(driver, By.xpath(leadCompanyLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
	}
	
	/**
	 * Use this method to verify associated lead details of the task
	 * @param driver
	 * @param taskName name of the task whose associated lead's details needs to be verified
	 * @param leadName expected lead name
	 * @param companyName expected company name of the lead
	 */
	public void verifyLeadDetail(WebDriver driver, String taskName, String leadName, String companyName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		assertEquals(getElementsText(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), leadName);
		assertEquals(getAttribue(driver, By.xpath(contactIconLocator.replace("$$Index$$", String.valueOf(taskRowIndex))), ElementAttributes.outerHTML), leadIconHtml);
		assertEquals(getElementsText(driver, By.xpath(leadCompanyLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), companyName);
		assertEquals(getElementsText(driver, By.xpath(contactTitleLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), "QA");
		waitUntilInvisible(driver, By.xpath(contactAccountLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
	}
	
	/**
	 * Use this method to verify associated related records details of the task
	 * @param driver
	 * @param taskName name of the task whose associated related record's details needs to be verified
	 * @param leadContactName expected lead or contact name
	 * @param relatedRecord expected related record name
	 */
	public void verifyRelatedRecordTaskDetail(WebDriver driver, String taskName, String leadContactName, String relatedRecord) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		assertEquals(getElementsText(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), leadContactName);
		assertEquals(getElementsText(driver, By.xpath(contactTitleLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), "QA");
		assertEquals(getElementsText(driver, By.xpath(contactAccountLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), relatedRecord);
	}
	
	/**
	 * use this method to verify if the associated contact fav icon is visible
	 * @param driver
	 * @param taskName name of the task whose associated contact's fav icon needs to be verified
	 * @param contactLeadName expected lead or contact name
	 */
	public void verifyContactLeadIsFav(WebDriver driver, String taskName, String contactLeadName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		assertEquals(getElementsText(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), contactLeadName);
		assertEquals(getAttribue(driver, By.xpath(contactFavIconLocator.replace("$$Index$$", String.valueOf(taskRowIndex))), ElementAttributes.outerHTML), favIconHtml);
	}
	
	/**
	 * use this method to verify if the associated contact fav icon is not visible
	 * @param driver
	 * @param taskName name of the task whose associated contact's fav icon needs to be verified
	 * @param contactLeadName expected lead or contact name
	 */
	public void verifyContactLeadIsNotFav(WebDriver driver, String taskName, String contactLeadName) {
		int taskRowIndex = getTaskRowIndex(driver, taskName);
		assertEquals(getElementsText(driver, By.xpath(contactNameLocator.replace("$$Index$$", String.valueOf(taskRowIndex)))), contactLeadName);
		waitUntilInvisible(driver, By.xpath(contactFavIconLocator.replace("$$Index$$", String.valueOf(taskRowIndex))));
	}
	
	/**
	 * use this method to verify that load more button is not visible
	 * @param driver
	 */
	public void verifyLoadMorebtnInvisible(WebDriver driver) {
		waitUntilInvisible(driver, loadMoreButton);
	}
	
	/**
	 * use this method to click load more button
	 * @param driver
	 */
	public void clickLoadMorebtn(WebDriver driver) {
		waitUntilVisible(driver, loadMoreButton);
		clickByJs(driver, loadMoreButton);
	}
}