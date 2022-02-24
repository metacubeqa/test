package softphone.source;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import base.SeleniumBase;

public class SoftPhoneTeamPage extends SeleniumBase{

	By teamIconInactive				= By.cssSelector(".team");
	By agentName 					= By.cssSelector(".media-heading.team-name");
	By noAgentName                  = By.cssSelector(".team-no-item");
	String agentLastCallDuration 	= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='last-call-duration']";
	String agentLastCallNumber		= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='last-call-number']";
	String agentLastCallCallerName	= "//b[contains(@class,'team-name')][text()='$$User$$']/..//a[@class='last-caller-name']";
	String agentMonitorButton   	= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@data-action='enable-continuous-listen']";
	String agentMonitorStopButton	= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@data-action='disable-continuous-listen']";
	String agentListenButton		= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@data-action='connect']";
	String agentDisconnectButton	= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@data-action='disconnect']";
	String agentMuteButton			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@id='muteCall']";
	String agentUnmuteButton		= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@id='unmuteCall']";
	String agentNotesButton			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//button[@data-action='add-note']";
	String staticCallerName			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='staticCallerName']";
	String callerTimer				= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='timer is-countdown']";
	String callerTimerValue			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='timer is-countdown']//span[contains(@class,'countdown-amount')]";
	String callerName				= "//b[contains(@class,'team-name')][text()='$$User$$']/..//a[@class='callerName']";
	String callerCompany			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='callerCompany'][not(@style='display: none;')]|//b[contains(@class,'team-name')][text()='$$User$$']/..//a[@class='callerCompanyLinked'][not(@style='display: none;')]";
	String callerTitle				= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='callerTitle']";
	String callDurationHeader		= "//b[contains(@class,'team-name')][text()='$$User$$']/..//label[text()='Call Duration:']";
	String teamMemberStatus			= "//b[contains(@class,'team-name')][text()='$$User$$']/..//span[@class='user-presence']";
	String teamMemberName			= "//b[contains(@class,'team-name')][text()='$User$']";
	
	//Supervisor notes 
	By notesWindowHeading			= By.xpath("//h4[text()='Supervisor Notes']");
	By callerNameOnNotesWindow		= By.className("dialogCallerName");
	By callerCompanyOnNotesWindow	= By.className("dialogCallerCompanyLinked");
	By callerTitleOnNotesWindow		= By.className("dialogCallerTitle");
	By supervisorNotesTextBox		= By.className("notes");
	By supervisorNotesCounter		= By.className("notesCounter");
	By supervisorNotesSaveButton 	= By.cssSelector("[data-action='save']");
	By notesWindowCloseIcon			= By.cssSelector(".close[data-dismiss='modal']");
	
	//Team search and filter locators
	By teamUserSearchTextBox 		= By.cssSelector("input.user-name");
	By teamUserSearchButton			= By.cssSelector("[data-action='search-users']");
	By statusFilterSelect			= By.className("filter-status");
	By teamFilterSelect				= By.className("filter-teams");
	By orderBySelect				= By.className("order-by");
	
	public enum Status{all, Offline, online, Available, offline, OnCall, Connecting};
	public enum Sort{recent, asc, desc};	
	
	public boolean isTeamSectionVisible(WebDriver driver) {
		if(getWebelementIfExist(driver, teamIconInactive)!=null && getWebelementIfExist(driver, teamIconInactive).isDisplayed()) {
			return true;
		}
		return false;
	}
	
	//This method is to get the list of agents present in team
	public List<String> getAgents(WebDriver driver) {
		return getTextListFromElements(driver, agentName);
	}
	
	public List<String> getAgentsFirstName(WebDriver driver) {
		List<String> agentNames = getTextListFromElements(driver, agentName);
		List<String> firstNames	= new ArrayList<>();
		for (String agentName : agentNames) {
			firstNames.add(agentName.split(" ")[0]);
		}
		return firstNames;
	}
	
	//for getting caller name
	public String getCallerName(WebDriver driver, String agent) {
		By callerName = By.xpath(this.callerName.replace("$$User$$", agent));
		return getElementsText(driver, callerName);
	}

	//this method is to verify the callerName is expected or not
	public void verifyCallerName(WebDriver driver, String agent, String callerName) {
		By callerNameLocator = By.xpath(this.callerName.replace("$$User$$", agent));
		waitUntilVisible(driver, callerNameLocator);
		waitUntilTextPresent(driver, callerNameLocator, callerName);
	}

	public void verifyTeamCallRemoved(WebDriver driver, String agent) {
		verifyListenButtonNotVisible(driver, agent);
		verifyUnmuteButtonNotVisible(driver, agent);
		verifyMuteButtonNotVisible(driver, agent);
	}

	public boolean isMonitorEnabled(WebDriver driver, String agent) {
		By agentMonitorButton = By.xpath(this.agentMonitorButton.replace("$$User$$", agent));
		return findElement(driver, agentMonitorButton).isEnabled();
	}

	public boolean isListenEnabled(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		return findElement(driver, agentListenButton).isEnabled();
	}

	//click on monitor button for given agent
	public void clickMonitor(WebDriver driver, String agent) {
		By agentMonitorButton = By.xpath(this.agentMonitorButton.replace("$$User$$", agent));
		clickElement(driver, agentMonitorButton);
		verifyMonitorStopButtonVisible(driver, agent);
		verifyMonitorButtonNotVisible(driver, agent);
	}

	//verify that monitoring is started for any user or not
	public void verifyMonitoring(WebDriver driver, String agent) {
		verifyUnmuteButtonVisible(driver, agent);
		verifyNotesButtonVisible(driver, agent);
		System.out.println("Caller is added as single known");
		verifyListenButtonNotVisible(driver, agent);
		verifyListenDisconnectButtonNotVisible(driver, agent);
		System.out.println("Monitoring is started for caller");
	}

	//click on listen button 
	public void clickListen(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		clickElement(driver, agentListenButton);
	}

	//verify listening started 
	public void verifyListening(WebDriver driver, String agent) {
		verifyUnmuteButtonVisible(driver, agent);
		verifyNotesButtonVisible(driver, agent);
		System.out.println("Caller is added as single known");
		verifyListenDisconnectButtonVisible(driver, agent);
		verifyMonitorButtonVisible(driver, agent);
		System.out.println("Listening is connected for caller");
	}

	//for getting caller's company name
	public String getCallerCompanyName(WebDriver driver, String agent) {
		By callerCompany = By.xpath(this.callerCompany.replace("$$User$$", agent));
		return getElementsText(driver, callerCompany);
	}

	//To get callers title
	public String getCallerTitle(WebDriver driver, String agent) {
		By callerTitle = By.xpath(this.callerTitle.replace("$$User$$", agent));
		return getElementsText(driver, callerTitle);
	}

	//To get supervisor notes from supervisor notes textbox
	public String getSupervisorNotes(WebDriver driver) {
		return findElement(driver, supervisorNotesTextBox).getAttribute("value");
	}

	public String getCallerTimer(WebDriver driver, String agent) {
		By callerTimerValue = By.xpath(this.callerTimerValue.replace("$$User$$", agent));
		waitUntilVisible(driver, callerTimerValue);
		return getElementsText(driver, callerTimerValue);
	}

	//This method is to open team section 
	public void openTeamSection(WebDriver driver) {
		waitUntilInvisible(driver, spinnerWheel);
		if(findElement(driver, teamIconInactive).isDisplayed()) {
			waitUntilVisible(driver, teamIconInactive);
			clickElement(driver, teamIconInactive);
			waitUntilInvisible(driver, spinnerWheel);
			if(getWebelementIfExist(driver, agentName)!=null) {
				waitUntilVisible(driver, agentName, 40);
			} else {
				System.out.println("No agent present into team");
			}
		} else {
			System.out.println("team section alrady opened");
		}
	}

	public void listenAgent(WebDriver driver, String agent) {
		clickListen(driver, agent);
		verifyListening(driver, agent);
	}

	public void monitorAgent(WebDriver driver, String agent) {
		clickMonitor(driver, agent);
		verifyMonitoring(driver, agent);
	}

	public void disconnectListening(WebDriver driver, String agent) {
		By agentDisconnectButton = By.xpath(this.agentDisconnectButton.replace("$$User$$", agent));
		clickElement(driver, agentDisconnectButton);
		verifyListenButtonVisible(driver, agent);
		verifyMonitorButtonVisible(driver, agent);
		verifyUnmuteButtonNotVisible(driver, agent);
		verifyNotesButtonNotVisible(driver, agent);	
		verifyListenDisconnectButtonNotVisible(driver, agent);
		System.out.println("Listening is disconnected for caller");
	}

	public void stopMonitoring(WebDriver driver, String agent) {
		By agentMonitorStopButton = By.xpath(this.agentMonitorStopButton.replace("$$User$$", agent));
		clickElement(driver, agentMonitorStopButton);
		verifyMonitorButtonVisible(driver, agent);
		verifyUnmuteButtonNotVisible(driver, agent);
		verifyNotesButtonNotVisible(driver, agent);	
		verifyMonitorStopButtonNotVisible(driver, agent);
		verifyListenDisconnectButtonNotVisible(driver, agent);
		System.out.println("Monitoring is stopped for caller");
	}

	public boolean isNoAgentPresentInTeam(WebDriver driver) {
		if(getWebelementIfExist(driver, agentName)==null) {
			return true;
		}
		return false;
	}

	public boolean verifyAgentPresent(WebDriver driver, String agent) {
		if(isElementVisible(driver, noAgentName, 6)) {
			return false;
		}else {
		waitUntilVisible(driver, agentName);
		List<WebElement> elements = new ArrayList<WebElement>();
		idleWait(1);
		elements = getElements(driver, agentName);
		for(int i=0; i<elements.size(); i++) {
			if(elements.get(i).getText().trim().equals(agent.trim())) {
				System.out.println("Agent is showing on team section");
				return true;
			}
		}
		System.out.println("Agent is not present in team section");
		return false;
		}
	}

	public void unmuteListening(WebDriver driver, String agent) {
		By agentUnmuteButton = By.xpath(this.agentUnmuteButton.replace("$$User$$", agent));
		clickElement(driver, agentUnmuteButton);
		verifyMuteButtonVisible(driver, agent);
	}

	public void muteListening(WebDriver driver, String agent) {
		By agentMuteButton = By.xpath(this.agentMuteButton.replace("$$User$$", agent));
		clickElement(driver, agentMuteButton);
		verifyUnmuteButtonVisible(driver, agent);
	}


	public void openSupervisorNotes(WebDriver driver, String agent) {
		By agentNotesButton = By.xpath(this.agentNotesButton.replace("$$User$$", agent));
		clickElement(driver, agentNotesButton);
	}

	public void closeSupervisorNotes(WebDriver driver) {
		if(isElementVisible(driver, notesWindowCloseIcon, 5)){
			clickElement(driver, notesWindowCloseIcon);
		}
	}

	public void addSupervisorNotes(WebDriver driver, String agent, String data) {
		openSupervisorNotes(driver, agent);
		waitUntilVisible(driver, supervisorNotesSaveButton);
		enterText(driver, supervisorNotesTextBox, data);
		clickElement(driver, supervisorNotesSaveButton);
		waitUntilInvisible(driver, supervisorNotesTextBox);
	}

	//verifying that caller is added 
	public void verifyCallerIsKnown(WebDriver driver, String agent)
	{
		By callerName = By.xpath(this.callerName.replace("$$User$$", agent));
		waitUntilVisible(driver, callerName);
	}

	//Verifying visibility of buttons
	public void verifyMonitorButtonVisible(WebDriver driver, String agent) {
		By agentMonitorButton = By.xpath(this.agentMonitorButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentMonitorButton);
	}

	public void verifyMonitorStopButtonVisible(WebDriver driver, String agent) {
		By agentMonitorStopButton = By.xpath(this.agentMonitorStopButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentMonitorStopButton);
	}

	public void verifyListenButtonVisible(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentListenButton);
	}
	
	public void verifyListenDisconnectButtonVisible(WebDriver driver, String agent) {
		By agentDisconnectButton = By.xpath(this.agentDisconnectButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentDisconnectButton);
	}

	public void verifyUnmuteButtonVisible(WebDriver driver, String agent) {
		By agentUnmuteButton = By.xpath(this.agentUnmuteButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentUnmuteButton);
	}

	public void verifyMuteButtonVisible(WebDriver driver, String agent) {
		By agentMuteButton = By.xpath(this.agentMuteButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentMuteButton);
	}

	public void verifyNotesButtonVisible(WebDriver driver, String agent) {
		By agentNotesButton = By.xpath(this.agentNotesButton.replace("$$User$$", agent));
		waitUntilVisible(driver, agentNotesButton);
	}

	//Verifying invisibility of buttons
	public void verifyMonitorButtonNotVisible(WebDriver driver, String agent) {
		By agentMonitorButton = By.xpath(this.agentMonitorButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentMonitorButton);
	}

	public void verifyMonitorStopButtonNotVisible(WebDriver driver, String agent) {
		By agentMonitorStopButton = By.xpath(this.agentMonitorStopButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentMonitorStopButton);
	}

	public boolean isMonitorButtonEnable(WebDriver driver, String agent) {
		By agentMonitorButton = By.xpath(this.agentMonitorButton.replace("$$User$$", agent));
		return findElement(driver, agentMonitorButton).isEnabled();
	}

	public void verifyListenButtonNotVisible(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentListenButton);
	}
	
	public boolean isListenButtonVisible(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		return isElementVisible(driver, agentListenButton, 0);
	}

	public boolean isListenButtonEnable(WebDriver driver, String agent) {
		By agentListenButton = By.xpath(this.agentListenButton.replace("$$User$$", agent));
		return findElement(driver, agentListenButton).isEnabled();
	}

	public void verifyListenDisconnectButtonNotVisible(WebDriver driver, String agent) {
		By agentDisconnectButton = By.xpath(this.agentDisconnectButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentDisconnectButton);
		//Verifying first element coming is same which we have searched
	}

	public void verifyUnmuteButtonNotVisible(WebDriver driver, String agent) {
		By agentUnmuteButton = By.xpath(this.agentUnmuteButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentUnmuteButton);
	}

	public void verifyMuteButtonNotVisible(WebDriver driver, String agent) {
		By agentMuteButton = By.xpath(this.agentMuteButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentMuteButton);
	}

	public void verifyNotesButtonNotVisible(WebDriver driver, String agent) {
		By agentNotesButton = By.xpath(this.agentNotesButton.replace("$$User$$", agent));
		waitUntilInvisible(driver, agentNotesButton);
	}
	
	
	// *************************  Search Team Member ***************************** //
	
	//Entering the search term
	public void enterUserSearchTerm(WebDriver driver, String userName) {
		enterText(driver, teamUserSearchTextBox, userName);
	}
	
	//clicking on search button 
	public void clickOnSearchButton(WebDriver driver) {
		clickElement(driver, teamUserSearchButton);
	}
	
	//This method is to search and verify the results 
	public void searchUser(WebDriver driver, String userName) {
		enterUserSearchTerm(driver, userName);
		clickOnSearchButton(driver);
		waitUntilInvisible(driver, spinnerWheel);
		idleWait(1);
		if(getWebelementIfExist(driver, agentName)!=null && getWebelementIfExist(driver, agentName).isDisplayed()) {
			List<WebElement> elements = getElements(driver, agentName);
			for(int i=0; i<elements.size(); i++) {
				assertEquals(elements.get(i).getText().contains(userName), true);
			}
		} else {
			System.out.println("No results are found");
		}
	}

	//This method is to get the selected status 
	public String getSelectedStatus(WebDriver driver) {
		Select select = new Select(findElement(driver, statusFilterSelect)); 
		return select.getFirstSelectedOption().getAttribute("value");
	}

	//This method is to get the selected status 
	public void setStatus(WebDriver driver, Status status) {
		Select select = new Select(findElement(driver, statusFilterSelect)); 
		select.selectByValue(status.toString().toLowerCase());
		idleWait(1);
	}
	
	//This method is to set custom status
	public void setCustomStatus(WebDriver driver, String customStatus) {
		Select select = new Select(findElement(driver, statusFilterSelect)); 
		select.selectByVisibleText(customStatus.trim());
	}
	
	//This method is to set the team 
	public void setTeam(WebDriver driver, String team) {
		Select select = new Select(findElement(driver, teamFilterSelect)); 
		select.selectByVisibleText(team.trim());
		idleWait(1);
	}
	
	//This method is to get the selected team 
	public String getSelectedTeam(WebDriver driver) {
		return getSelectedValueFromDropdown(driver, teamFilterSelect);
		
	}

	//This method is to get the selected status 
	public String getSelectedSorting(WebDriver driver) {
		Select select = new Select(findElement(driver, orderBySelect)); 
		return select.getFirstSelectedOption().getAttribute("value");
	}

	//This method is to get the selected status 
	public void setSorting(WebDriver driver, Sort sortType) {
		Select select = new Select(findElement(driver, orderBySelect)); 
		select.selectByValue(sortType.toString());
		idleWait(1);
	}
	
	//This method is to get team member status
	public String getTeamMemberStatus(WebDriver driver, String agent) {
		waitUntilVisible(driver, agentName);
		By teamMemberStatus =  By.xpath(this.teamMemberStatus.replace("$$User$$", agent));
		return getElementsText(driver, teamMemberStatus);
	}
	
	public boolean isTeamMemberVisible(WebDriver driver, String teamMember){
		enterUserSearchTerm(driver, teamMember);
		clickOnSearchButton(driver);
		waitUntilInvisible(driver, spinnerWheel);
		By teamMemberLoc = By.xpath(teamMemberName.replace("$User$", teamMember));
		return isElementVisible(driver, teamMemberLoc, 5);
	}
}
