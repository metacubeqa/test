package guidedSelling.source.pageClasses;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.SeleniumBase;
import guidedSelling.source.pageClasses.ActionsPage.ActionsTypesEnum;

public class SequencesPage extends SeleniumBase{
	
	public static enum SequenceMenuTriggerOptions{
		Edit,
		Delete,
		Clone,
		Share;
	}
	
	public static enum RecordTypes{
		Lead,
		Contact,
		Opportunity,
		Campaign
	}
	
	public static enum ActivationType{
		Manual,
		Automatic
	}
	
	public static enum SeqReportCards{
		ActiveParticipants("Active Participants"),
		MetExitCriteria("Met Exit Criteria"),
		AvgTouchPoints("Average TouchPoints"),
		CompletedSequence("Completed Sequence")
		;
		
		private String value;

		SeqReportCards(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum CriteriaFields{
		EntryCriteria,
		EntryOperator,
		EntryValue,
		
		ExitOperator,
		ExitCriteria,
		ExitValue
	}
	
	public static enum EntryExitFields{
		FirstName("First Name"),
		LastName("Last Name"),
		Equals("Equals"),
		Includes("Includes"),
		DoesNotInclude("Does Not Include"),
		On("On"),
		DoesNotEqual("Does not equal"),
		CloseDate("Close Date"),
		Name("Name"),
		Stage("Stage"),
		ClosedWon("Closed Won"),
		ClosedLost("Closed Lost"),
		MeetingBookedbyAE("0 - Meeting Booked by AE")
		;
		
		
		private String value;

		EntryExitFields(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum FieldUpdatesEnum {
		NoFieldUpdates("No Field Updates"), FieldUpdatesRequired("Field Updates Required");

		private String value;

		FieldUpdatesEnum(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum FieldOpTionsEnum {
		Company("Company"), FirstName("First Name"), LastName("Last Name"),
		Industry("Industry"), FaxOptOut("Fax Opt Out");

		public String value;

		public String getValue() {
			return value;
		}

		FieldOpTionsEnum(String envValue) {
			this.value = envValue;
		}
	}
	
	public static enum CriteriaConditions {
		NoAdditionalCriteria("No Additional Criteria"), ConditionsAreMet("Conditions are met");

		private String value;

		CriteriaConditions(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	By sequenceTab					 = By.xpath("//*[contains(@title, 'Sequences Tab')]");
	By createSeqBtn 				 = By.xpath("//div/*[text()='Create Sequence']");	
	By sequenceInputTab  			 = By.xpath("//label[text()='Sequence Name']/following-sibling::div/input");
	By saveBtn 			   			 = By.xpath("//button[text()='Save']");
	By saveAndFinishBtn  			 = By.xpath("//button[text()='Save and Finish']");
	By fieldSelect		    		 = By.xpath("//label[text()='Field']/..//input/..");
	By operatorSelect	    		 = By.xpath("//label[text()='Operator']/..//input/..");
	By valueTab		 	    		 = By.xpath("//label[text()='Value']/..//input");
	
	By searchAction      			 = By.cssSelector("[name='search-action']");
  	By showAdvanceSettingBtn		 = By.xpath("//button[text()='Show Advance Settings']");
  	
	String recordTypeSelect 		 = "//legend[text()='Record Type']/..//input[@value='$recordType$']/following-sibling::button/ancestor::span[2]";
	String selectFieldOption 		 = "//li[contains(@class,'MuiListItem-button') and text()='$field$']";
	String activationTypeSelect 	 = "//legend[text()='Activation Type']/..//input[@type='radio' and @value='$activationType$']/../..";
	String priorityTypeSelect		 = "//legend[text()='Activation Type']/../..//input[@type='radio' and @value='$priorityType$']/../..";
	String fieldUpdateSelect    	 = "//legend[text()='Field Update']/../..//input[@type='radio' and @value='$fieldUpdate$']/../..";
    String criteriaConditionSelect   = "//legend[text()='Criteria Condition ']/../..//input[@type='radio' and @value='$criteriaCondition$']/../..";
	String chooseActionType          = "//div[contains(@class,'ActionsLibraryPanel')]//p[text()='$actionType$']/preceding-sibling::button";
	String libraryActionName       	 = "//div[contains(@class, 'ActionsLibraryPanel') or contains(@class, 'ActionPanelCard')]//h6[text()='$actionName$']";
	
	By dragAndDropLoc          		 = By.xpath("//h6[text()='Drag and drop action here.']/../../..");
    By libraryActionAddButton  		 = By.xpath("/ancestor::div[contains(@class, 'ActionsLibraryPanel__CardTitle')]//button//div[@shape='circle']");	
    By removeIcon 			         = By.xpath("//button[text()='Remove']");

    By draggedActionLoc    		 	 = By.xpath("//button[text()='Remove']/..");
    String actionDraggedLoc    		 = "//button[text()='Remove']/..//span[text()='$actionName$']";
    String sequencedropdownMenu      = "//span[text()='$sequenceName$']/../../../following-sibling::span//div[@aria-label='rdna-menu-trigger']//*[local-name()='svg']";
    
    By sequenceNamesLink             = By.xpath("//span[contains(@class, 'RdnaSmartTable__StyledTableCell')]//a/span");
	By menuTriggerOptionsList		 = By.cssSelector("ul li[role='menuitem'] div");
	
    String sequenceNameLink   		 = "//span[text()='$sequenceName$']";
    String sequenceCheckBox	  		 = "//span[text()='$sequenceName$']/ancestor::span[contains(@class, 'StyledTableCell')]/..//input[@type='checkbox']/ancestor::span[2]";
    String sequenceNameHeader 		 = "//h1[text()='$sequenceName$']";
   
    By participantsCount 			 = By.xpath("//div[@role='tablist']//button/span[contains(text(), 'Participants')]//span");
    By deactivateBtn	 			 = By.xpath("//button[text()='Deactivate']");
    By activateBtn		 			 = By.xpath("//button[text()='Activate']");
    By editBtn			 	 		 = By.xpath("//button[text()='Edit']");
    By deleteSequenceBtn 		     = By.xpath("//button[text()='Delete Sequences']");
    By confirmBtn        			 = By.xpath("//button[@data-testid='ui-dialog.primary-btn']");
    By exitCriteriaHeader   		 = By.xpath("//span[text()='Exit Criteria']");
    
    //contact/lead add seq btns
    By noMatchingSeqMsg        		 = By.xpath("//p[@class='rdna-text-style' and contains(text(), 'Currently no matching sequences are present, please create sequence')]");
    By associateBtn      	   		 = By.xpath("//button[text()='Yes, Associate!']");
    By redirectingBtn				 = By.xpath(".//*[text() = 'Redirecting...']");
    String seqAssociateBtnAdd  		 = "//a/span[text()='$sequenceName$']/ancestor::div[@data-testid='collapse-container']//button[text()='Associate']";
  
    By reportsTabOld	   			 = By.xpath("//a[text()='Reports']");
    By skippedReportsTable 			 = By.xpath("//div[@class='ringdna-table-head']//span[text()='Skipped']");
    By buttontemplateIcon  			 = By.xpath("//*[@data-rbd-draggable-id]//div[contains(@class, 'ActionsLibraryPanel') or contains(@class, 'ActionPanelCard')]//button[not(@disabled)]");
    
    String seqReportLoc 			 = "//p[text()='$report$']/preceding-sibling::h1";
    By saveAndFinishToolTip 		 = By.cssSelector(".MuiTooltip-tooltip.MuiTooltip-tooltipPlacementTop");

//Update field
    By fieldUpdatesRequired 		 = By.xpath("input[@value='Field Updates Required']");
    String updateFielName    		 = 	"//li[contains(@class,'MuiButtonBase-root MuiListItem-root MuiMenuItem-root MuiMenuItem-gutters MuiListItem-gutters MuiListItem-button') and text()='$field$']";
    By updateTextFieldvalue			 = By.xpath("//input[@placeholder='Enter Field Value']");
    By updateDdlFieldvalue			 = By.xpath("//span[text()='Select Field Value']");
    By participantCount				 = By.xpath("//*[@aria-label='Vertical Tabs']//*[contains(text(),'�    Participants')]//span[text()='1']");	
    
    /**
     * @param driver
     */
    public void navigateToSequencetab(WebDriver driver) {
    	waitUntilVisible(driver, sequenceTab);
    	clickElement(driver, sequenceTab);
    	isSpinnerWheelInvisible(driver);
    }
    
    /**
     * @param driver
     */
    public void clickCreateSeqBtn(WebDriver driver) {
    	waitUntilVisible(driver, createSeqBtn);
    	clickElement(driver, createSeqBtn);
    	isSpinnerWheelInvisible(driver);
    }
    
    /**
     * @param driver
     * @param sequenceName
     * @param recordType
     * @param activeType
     * @param criteriaMapData TODO
     * @param actionType
     * @param actionName
     */
    public void addEntryExitCriteriaSequence(WebDriver driver, String sequenceName, RecordTypes recordType, ActivationType activeType, HashMap<CriteriaFields, String> criteriaMapData, ActionsTypesEnum actionType, String actionName) {
    	
    	waitUntilVisible(driver, createSeqBtn);
    	clickElement(driver, createSeqBtn);
    	isSpinnerWheelInvisible(driver);
    	
    	waitUntilVisible(driver, sequenceInputTab);
    	enterText(driver, sequenceInputTab, sequenceName);
    	
    	By recordTypeLoc = By.xpath(recordTypeSelect.replace("$recordType$", recordType.name()));
    	waitUntilVisible(driver, recordTypeLoc);
    	clickElement(driver, recordTypeLoc);
    	
    	By activationTypeLoc = By.xpath(activationTypeSelect.replace("$activationType$", activeType.name()));
    	waitUntilVisible(driver, activationTypeLoc);
    	clickElement(driver, activationTypeLoc);
    	
    	scrollTillEndOfPage(driver);
    	
		if (activeType.equals(ActivationType.Automatic)) {
			By priorityValueLoc = By.xpath(priorityTypeSelect.replace("$priorityType$", "P1"));
			waitUntilVisible(driver, priorityValueLoc);
			clickElement(driver, priorityValueLoc);
		}
 	
    	clickSaveBtn(driver);
    	
    	//entrance criteria
    	if (criteriaMapData.get(CriteriaFields.EntryCriteria) != null) {
    		
    		waitUntilVisible(driver, fieldSelect);
    		clickElement(driver, fieldSelect);
    		isSpinnerWheelInvisible(driver);
    		
    		By selectFieldLoc = By.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.EntryCriteria)));
    		waitUntilVisible(driver, selectFieldLoc);
    		clickElement(driver, selectFieldLoc);
    		
    		waitUntilVisible(driver, operatorSelect);
    		clickElement(driver, operatorSelect);
    		
    		By operatorFieldLoc = By.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.EntryOperator)));
    		waitUntilVisible(driver, operatorFieldLoc);
    		clickElement(driver, operatorFieldLoc);
    		
			// check value tab is text or list type
			if (getAttribue(driver, findElement(driver, valueTab), ElementAttributes.Class).contains("MuiSelect")) {
				WebElement valueTabLoc = findElement(driver, valueTab).findElement(By.xpath(".."));
				waitUntilVisible(driver, valueTabLoc);
				clickElement(driver, valueTabLoc);
				isSpinnerWheelInvisible(driver);

				By valueFieldLoc = By
						.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.EntryValue)));
				waitUntilVisible(driver, valueFieldLoc);
				clickElement(driver, valueFieldLoc);
				pressEscapeKey(driver);
			}

			else {

				// put name of contact/lead
				enterTextInValueField(driver, criteriaMapData.get(CriteriaFields.EntryValue));
			}
			
			clickSaveBtn(driver);
		}
		
       	//exit criteria
    	if (criteriaMapData.get(CriteriaFields.ExitCriteria) != null) {
    		
    		waitUntilVisible(driver, fieldSelect);
    		clickElement(driver, fieldSelect);
    		isSpinnerWheelInvisible(driver);
    		
    		By selectFieldLoc = By.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.ExitCriteria)));
    		waitUntilVisible(driver, selectFieldLoc);
    		clickElement(driver, selectFieldLoc);
    		
    		waitUntilVisible(driver, operatorSelect);
    		clickElement(driver, operatorSelect);
    		
    		By operatorFieldLoc = By.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.ExitOperator)));
    		waitUntilVisible(driver, operatorFieldLoc);
    		clickElement(driver, operatorFieldLoc);
    	
			// check value tab is text or list type
			if (getAttribue(driver, findElement(driver, valueTab), ElementAttributes.Class).contains("MuiSelect")) {
				WebElement valueTabLoc = findElement(driver, valueTab).findElement(By.xpath(".."));
				waitUntilVisible(driver, valueTabLoc);
				clickElement(driver, valueTabLoc);
				isSpinnerWheelInvisible(driver);

				By valueFieldLoc = By
						.xpath(selectFieldOption.replace("$field$", criteriaMapData.get(CriteriaFields.ExitValue)));
				waitUntilVisible(driver, valueFieldLoc);
				clickElement(driver, valueFieldLoc);
				pressEscapeKey(driver);
			}

			else {

				//put name of contact/lead
				enterTextInValueField(driver, criteriaMapData.get(CriteriaFields.ExitValue));
			}
			
    		clickSaveBtn(driver);
		}

    	//choose library panel action type
		verifySaveAndFinishToolTip(driver, "Must add at least one action.");
    	By chooseActionTypeLoc = By.xpath(chooseActionType.replace("$actionType$", actionType.name()));
    	waitUntilVisible(driver, chooseActionTypeLoc);
    	clickElement(driver, chooseActionTypeLoc);
    	isSpinnerWheelInvisible(driver);
    	
    	waitUntilVisible(driver, searchAction);
    	enterText(driver, searchAction, actionName);
    	
    	By libActionName = By.xpath(libraryActionName.replace("$actionName$", actionName));
    	waitUntilVisible(driver, libActionName);
    	
		hoverElement(driver, libActionName);
		
		waitUntilVisible(driver, buttontemplateIcon);
		hoverElement(driver, buttontemplateIcon);
		clickElement(driver, buttontemplateIcon);
	
    	waitUntilVisible(driver, removeIcon);
    	waitUntilVisible(driver, By.xpath(actionDraggedLoc.replace("$actionName$", actionName)));
	 
											  
    	clickSaveBtn(driver);
    }
    
    
    public void addAction(WebDriver driver ,  ActionsTypesEnum actionType, String[] actionName) {
    	
    	for(String actName: actionName) {
	    	waitUntilVisible(driver, searchAction);
	    	enterText(driver, searchAction, actName);
	    	
	    	By libActionName = By.xpath(libraryActionName.replace("$actionName$", actName));
	    	waitUntilVisible(driver, libActionName);
	    	
			hoverElement(driver, libActionName);
			
			waitUntilVisible(driver, buttontemplateIcon);
			hoverElement(driver, buttontemplateIcon);
			clickElement(driver, buttontemplateIcon);
			
			waitUntilVisible(driver, removeIcon);
	    	waitUntilVisible(driver, By.xpath(actionDraggedLoc.replace("$actionName$", actName)));
		}
    	clickSaveBtn(driver);
    }
    /**
     * @param driver
     * @param fieldUpdate
     * @param fieldOption
     * @param criteriaConditions
     */
    public void addAdvanceSettings(WebDriver driver, FieldUpdatesEnum fieldUpdate, FieldOpTionsEnum fieldOption, String fieldValue, CriteriaConditions criteriaConditions) {
    	
    	clickElement(driver, draggedActionLoc);
    	waitUntilVisible(driver, showAdvanceSettingBtn);
    	clickElement(driver, showAdvanceSettingBtn);
    	
		// Field Update
		if (fieldUpdate != null) {
			By fieldUpdateLoc = By.xpath(fieldUpdateSelect.replace("$fieldUpdate$", fieldUpdate.getValue()));
    		waitUntilVisible(driver, fieldUpdateLoc);
    		clickElement(driver, fieldUpdateLoc);
    		isSpinnerWheelInvisible(driver);
    		
    		waitUntilVisible(driver, fieldSelect);
    		clickElement(driver, fieldSelect);
    		isSpinnerWheelInvisible(driver);
    		
    		By selectFieldLoc = By.xpath(selectFieldOption.replace("$field$", fieldOption.getValue()));
    		waitUntilVisible(driver, selectFieldLoc);
    		clickElement(driver, selectFieldLoc);
    		
    		//check value tab is text or list type
    		if(getAttribue(driver, findElement(driver, valueTab), ElementAttributes.Class).contains("MuiSelect")) {
    			WebElement valueTabLoc = findElement(driver, valueTab).findElement(By.xpath(".."));
    			waitUntilVisible(driver, valueTabLoc);
        		clickElement(driver, valueTabLoc);
        		isSpinnerWheelInvisible(driver);
        		
        		By valueFieldLoc = By.xpath(selectFieldOption.replace("$field$", fieldValue));
        		waitUntilVisible(driver, valueFieldLoc);
        		clickElement(driver, valueFieldLoc);
        		pressEscapeKey(driver);
    		}
    		
    		else {
    			enterTextInValueField(driver, fieldValue);
    		}
    		
    	}
    	
    	//Criteria Condition
		if (criteriaConditions != null) {
			By criteriaConditionLoc = By.xpath(criteriaConditionSelect.replace("$criteriaCondition$", fieldUpdate.getValue()));
    		waitUntilVisible(driver, criteriaConditionLoc);
    		clickElement(driver, criteriaConditionLoc);
    	}
		clickSaveBtn(driver);
    	saveCompleteSequence(driver);
    }
	
    
    /**
     * @param driver
     * @param seqReportCards
     * @return
     */
    public String getSequenceReportCard(WebDriver driver, SeqReportCards seqReportCards) {
    	By seqReportLocValue = By.xpath(seqReportLoc.replace("$report$", seqReportCards.getValue()));
    	return getElementsText(driver, seqReportLocValue);
    }
    
    public void verifyInitialCreatedSeqReport(WebDriver driver) {
    	assertEquals(getSequenceReportCard(driver, SeqReportCards.ActiveParticipants), "0");
    	assertEquals(getSequenceReportCard(driver, SeqReportCards.MetExitCriteria), "0");
    	assertEquals(getSequenceReportCard(driver, SeqReportCards.AvgTouchPoints), "0");
    	assertEquals(getSequenceReportCard(driver, SeqReportCards.CompletedSequence), "0");
    }
    
    public void verifySequenceCreated(WebDriver driver, String sequenceName) {
    	By sequenceHeaderLoc = By.xpath(sequenceNameHeader.replace("$sequenceName$", sequenceName));
    	waitUntilVisible(driver, sequenceHeaderLoc);
    	verifyInitialCreatedSeqReport(driver);
    }
    
	public int getParticipantsCount(WebDriver driver) {
		waitUntilVisible(driver, participantsCount);
		return Integer.parseInt(getElementsText(driver, participantsCount));
	}
	
	public void waitUntilParticipantCount(WebDriver driver, int count) {
		for(int i = 0; i < 5; i++) {
			idleWait(10);
			if(getElementsText(driver, participantsCount).equals(String.valueOf(count))) {
				return;	
			}
			refreshCurrentDocument(driver);
		}
		Assert.fail();
	}
    
    public void clikActivateBtn(WebDriver driver) {
    	waitUntilVisible(driver, activateBtn);
    	clickElement(driver, activateBtn);
    	waitUntilVisible(driver, confirmBtn);
    	clickElement(driver, confirmBtn);
    	waitUntilInvisible(driver, activateBtn);
    	waitUntilVisible(driver, deactivateBtn);
    	isSpinnerWheelInvisible(driver);
    }
	
    public void clickSaveBtn(WebDriver driver) {
    	waitUntilVisible(driver, saveBtn);
    	clickElement(driver, saveBtn);
    	isSpinnerWheelInvisible(driver);
    }
    
    public void clickSaveAndFinishBtn(WebDriver driver) {
    	waitUntilVisible(driver, saveAndFinishBtn);
    	scrollToElement(driver, saveAndFinishBtn);
    	clickElement(driver, saveAndFinishBtn);
    	waitUntilInvisible(driver, saveAndFinishBtn);
    	isSpinnerWheelInvisible(driver);
    }
	
    public void saveCompleteSequence(WebDriver driver) {
 //   	clickSaveBtn(driver);
    	clickSaveAndFinishBtn(driver);
    }
    
	public void clickSequenceLink(WebDriver driver, String sequenceName) {
		By sequenceLoc = By.xpath(sequenceNameLink.replace("$sequenceName$", sequenceName));
		waitUntilVisible(driver, sequenceLoc);
		clickElement(driver, sequenceLoc);
		isSpinnerWheelInvisible(driver);
	}

	public void clickexitCriteriaHeader(WebDriver driver) {
		waitUntilVisible(driver, exitCriteriaHeader);
		clickElement(driver, exitCriteriaHeader);
	}
	
	public void enterTextInValueField(WebDriver driver, String value) {
		waitUntilVisible(driver, valueTab);
		clearAll(driver, valueTab);
    	enterText(driver, valueTab, value);	
	}
	
	public void clickEditSequenceBtn(WebDriver driver) {
		waitUntilVisible(driver, editBtn);
		clickElement(driver, editBtn);
		isSpinnerWheelInvisible(driver);
	}
	
	public void deleteSequenceByBtn(WebDriver driver, String sequenceName) {
		By sequenceLoc = By.xpath(sequenceCheckBox.replace("$sequenceName$", sequenceName));
		waitUntilVisible(driver, sequenceLoc);
		clickElement(driver, sequenceLoc);
		waitUntilVisible(driver, deleteSequenceBtn);
		clickElement(driver, deleteSequenceBtn);
		waitUntilVisible(driver, confirmBtn);
		clickElement(driver, confirmBtn);
		assertFalse(isElementVisible(driver, sequenceLoc, 5));
	}
	
	public void associateSequenceFromParticipant(WebDriver driver, String sequenceName) {
		isSpinnerWheelInvisible(driver);
		By seqAssociateBtnAddLoc = By.xpath(seqAssociateBtnAdd.replace("$sequenceName$", sequenceName));
		waitUntilVisible(driver, seqAssociateBtnAddLoc);
		clickElement(driver, seqAssociateBtnAddLoc);
		waitUntilVisible(driver, associateBtn);
		clickElement(driver, associateBtn);
		isElementVisible(driver, redirectingBtn, 10);
		waitUntilInvisible(driver, redirectingBtn);
		isSpinnerWheelInvisible(driver);
	}
	
	/**
	 * @param driver
	 * @param index
	 */
	public void clickSequenceLinkAccToIndex(WebDriver driver, int index) {
		isListElementsVisible(driver, sequenceNamesLink, 5);
		clickElement(driver, getElements(driver, sequenceNamesLink).get(index));
	}
	
	public void verifyReportsNotVisible(WebDriver driver) {
		waitUntilInvisible(driver, reportsTabOld);
		waitUntilInvisible(driver, skippedReportsTable);
	}
	
	/**
	 * @param driver
	 * @param sequenceName
	 * @param triggerOptions
	 */
	public void selectSequenceMenuFilter(WebDriver driver, String sequenceName, SequenceMenuTriggerOptions triggerOptions) {
		By saveSearchDropdownMenuLoc = By.xpath(sequencedropdownMenu.replace("$sequenceName$", sequenceName));
		clickAndSelectFromDropDown(driver, saveSearchDropdownMenuLoc, menuTriggerOptionsList, triggerOptions.name());
		isSpinnerWheelInvisible(driver);
	}
	
	public void verifySaveAndFinishToolTip(WebDriver driver, String toolTipText) {
    	   	waitUntilVisible(driver, saveAndFinishBtn);
    	   	
    	   	List<WebElement> save = getElements(driver, saveAndFinishBtn);
    	   	hoverElement(driver, save.get(0));
	    	waitUntilTextPresent(driver, saveAndFinishToolTip, toolTipText);
		}  
}