package report.source.caiReportPage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.internal.collections.Pair;
import org.testng.util.Strings;

import base.SeleniumBase;
import report.base.ReportBase;
import support.source.callRecordings.CallRecordingReportPage.reportDuration;
import support.source.commonpages.Dashboard;
import utility.HelperFunctions;

/**
 * @author Abhishek T
 *
 */
public class CAIReportPage extends SeleniumBase{
	
	Dashboard dashboard = new Dashboard();
	Random random 		= new Random();
	
	By stillWaitingMsg	    		= By.xpath("//div[@class='text-slate']/div[contains(text(),'Still Waiting')]");
	By iframeTable					= By.xpath(".//iframe[contains(@src, 'https://reporting')]");
	By loadingSpinner				= By.cssSelector(".LoadingSpinner");
	By createdDateInputTab			= By.cssSelector("[data-testid='created-input'] #ringdna-select");
	By refreshButton 				= By.cssSelector("button[name= 'buttons-container-submit']");
	By accountHelperText			= By.xpath(".//*[@id='accountId-helper-text'][text()='This field is required.']");
	By listBoxItems					= By.cssSelector("ul[role='listbox'] li");
	By totalSections				= By.cssSelector(".DashCard.react-resizable");
	By dashBoardGridImage		    = By.cssSelector(".DashboardGrid");
	By chartImage				    = By.cssSelector(".dc-chart");
	By pieChartImage				= By.xpath("//div[@class='CardVisualization flex-full flex-basis-none relative']/div");
	
	//expand icons sections  
	By inSightsLink					= By.cssSelector("[href='/insights']");
	By caiEtiquetteExpandLink		= By.xpath("//*[@name='/cai-talk-metrics-individual-insights']//div[contains(@class, 'caret-filled')]");
	By talkingvsListeningContainer	= By.xpath("//*[@name='/etiquette-talking-listening-insights']/../parent::div[contains(@class, 'MuiCollapse-wrapperInner')]/../parent::div[contains(@class, 'MuiCollapse-container')]");
	
	By activityMetricsExpandLink	= By.xpath("//*[@name='/team-engagement-insights']//div[contains(@class, 'caret-filled')]");
	By callingProductivityContainer = By.xpath("//*[@name='/activity-calling-productivity-insights']/../parent::div[contains(@class, 'MuiCollapse-wrapperInner')]/../parent::div[contains(@class, 'MuiCollapse-container')]");
	
	By coachingEventsExpandLink		= By.xpath("//*[@name='/coaching-events-insights']//div[contains(@class, 'caret-filled')]");
	By coachingVolumeContainer		= By.xpath("//*[@name='/coaching-volume-insights']/../parent::div[contains(@class, 'MuiCollapse-wrapperInner')]/../parent::div[contains(@class, 'MuiCollapse-container')]");
	
	//link section
	
	//CAI Etiquete links
	By talkingvListeningLink		= By.cssSelector("[name='/etiquette-talking-listening-insights']");
	By interruptionsLink			= By.cssSelector("[name='/etiquette-interruptions-insights']");
	By agentMonologuesLink			= By.cssSelector("[name='/etiquette-agent-monologues-insights']");
	By customerMonologuesLink		= By.cssSelector("[name='/etiquette-customer-monologues-insights']");
	By silenceLink					= By.cssSelector("[name='/etiquette-silence-insights']");
	By interactionsLink				= By.cssSelector("[name='/etiquette-interactions-insights']");
	
	//Activity Metrics links
	By callingProductivityLink		= By.cssSelector("[name='/activity-calling-productivity-insights']");
	By talkTimeLink					= By.cssSelector("[name='/activity-talk-time-insights']");
	By recordPercentageLink			= By.cssSelector("[name='/activity-record-percent-insights']");
	By timeDayLink					= By.cssSelector("[name='/activity-time-day-insights']");
	By dispositionsLink				= By.cssSelector("[name='/activity-dispositions-insights']");
	By callDirectionsLink			= By.cssSelector("[name='/activity-call-directions-insights']");
	By localPresenceLink			= By.cssSelector("[name='/activity-local-presence-insights']");
	
	//keyword Impact link
	By keywordImpactLink			= By.cssSelector("[name='/keyword-impact-insights']");
	
	//Coaching Events links
	By coachingVolumeLink			= By.cssSelector("[name='/coaching-volume-insights']");
	By coachingReceivedLink			= By.cssSelector("[name='/coaching-received-insights']");
	By coachingGivenLink			= By.cssSelector("[name='/coaching-given-insights']");
	By peerCoachingLink				= By.cssSelector("[name='/peer-coaching-insights']");
	By selfReviewLink				= By.cssSelector("[name='/self-review-insights']");
	
	//Missed meeting
	By missedMeetingLink			= By.cssSelector("[name='/missed-meeting-opportunities-insights']");
	
	//data sections
	//Talking vs Listening
	By talkingKeyMetrics	= By.xpath("//div[text()='Talk Analysis - Org Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By talkPercentageNew	= By.xpath("//div[text()='Org Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By talkChartXAxisLabel	= By.xpath("//div[text()='Org Talk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By talkChatYAxisLabel	= By.xpath("//div[text()='Org Talk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkingChartBody1	= By.xpath("//div[text()='Org Talk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='_1 goal']");
	By talkingChartBody2	= By.xpath("//div[text()='Org Talk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By talkingAgentLevel				 = By.xpath("//div[text()='Talk Analysis - Agent Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By talkingYAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkingXAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By talkingChartBody1AgentLevel		 = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By talkingChartBody2AgentLevel		 = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By talkingXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By talkingYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	By talkingTeamLevel				= By.xpath("//div[text()='Talk Analysis - Team Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='agents with unusual talk percentages']");
	By talkingYAxisTeamLabel		= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkingChartBody3			= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By talkingChartBody4			= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By talkingXAxisTextAnchors 		= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By talkingYAxisTextAnchors 		= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By talkingRectBars				= By.xpath("//div[text()='Agent Talk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	public By noResultsSectionList	= By.xpath(".//*[@class='DashCard react-resizable']//img[contains(@src,'app/dist/')] | //span[text()='No results!']");

	//Interruptions
	By interruptionsMetrics			= By.xpath("//div[text()='Overtalk Analysis - Org Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By interruptionsPercentageNew	= By.xpath("//div[text()='Org Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By interruptionsChartXAxisLabel	= By.xpath("//div[text()='Org Overtalk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By interruptionsChartYAxisLabel	= By.xpath("//div[text()='Org Overtalk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interruptionsChartBody1		= By.xpath("//div[text()='Org Overtalk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='_1 goal']");
	By interruptionsChartBody2		= By.xpath("//div[text()='Org Overtalk Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By interruptionsAgentLevel				 = By.xpath("//div[text()='Overtalk Analysis - Agent Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By interruptionsYAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interruptionsXAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By interruptionsChartBody1AgentLevel	   = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By interruptionsChartBody2AgentLevel	   = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By interruptionsXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By interruptionsYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	By interruptionsTeamLevel		= By.xpath("//div[text()='Overtalk Analysis - Team Level']/ancestor::div[@class='DashCard react-resizable']");
	By interruptionsYAxisTeamLabel	= By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interruptionsChartBody3		= By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By interruptionsChartBody4		= By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By interruptionsXAxisTextAnchors = By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By interruptionsYAxisTextAnchors = By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By interruptionsRectBars		 = By.xpath("//div[text()='Agent Overtalk Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	//Agent Monologues
	By agentMonoMetrics			= By.xpath("//div[text()='Average Streak Analysis - Org Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By agentMonoPercentageNew1	= By.xpath("//div[text()='Org Average Talk Streak - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By agentMonoPercentageNew2	= By.xpath("//div[text()='Org Average Longest Talk Streak - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By agentMonoChartYAxisLabel	= By.xpath("//div[text()='Agent Talk Streaks - Org-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By agentMonoChartBody		= By.xpath("//div[text()='Agent Talk Streaks - Org-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By agentMonoTeamLevel		 = By.xpath("//div[text()='Monologue Analysis - Team Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='agents with unusual talk streaks']");
	By agentMonoYAxisTeamLabel	 = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By agentMonoChartBody3		 = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By agentMonoChartBody4		 = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By agentMonoXAxisTextAnchors = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By agentMonoYAxisTextAnchors = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By agentMonoRectBars		 = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By agentMonoLongestStreakLegendColor = By.xpath("//div[text()='Agent Longest Streak']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(249, 212, 92);')]");
	By agentMonoAverageStreakLegendColor = By.xpath("//div[text()='Agent Average Streak']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(136, 191, 77);')]");
	By agentMonoLongestStreakBar = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#F9D45C']");
	By agentMonoAverageStreakBar = By.xpath("//div[text()='Agent Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _1 enable-dots']//*[name()='rect' and @fill='#88BF4D']");
	
	By agentMonoAgentLevel				 = By.xpath("//div[text()='Monologue Analysis - Agent Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By agentMonoYAxisAgentLabel			 = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By agentMonoXAxisAgentLabel			 = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By agentMonoChartBody1AgentLevel	   = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By agentMonoChartBody2AgentLevel	   = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By agentMonoXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By agentMonoYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Customer Monologues
	By customerMonoPercentageNew1	= By.xpath("//div[text()='Org Customer Average Talk Streak - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By customerMonoPercentageNew2	= By.xpath("//div[text()='Org Customer Average Longest Talk Streak - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By customerMonoChartYAxisLabel	= By.xpath("//div[text()='Customer Talk Streaks - Org-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By customerMonoChartBody		= By.xpath("//div[text()='Customer Talk Streaks - Org-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By customerMonoYAxisTeamLabel	 = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By customerMonoChartBody3		 = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By customerMonoChartBody4		 = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By customerMonoXAxisTextAnchors = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By customerMonoYAxisTextAnchors = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By customerMonoRectBars		 = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By customerMonoLongestStreakLegendColor = By.xpath("//div[text()='Caller Longest Streak']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(152, 217, 217);')]");
	By customerMonoAverageStreakLegendColor = By.xpath("//div[text()='Caller Average Streak']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(242, 168, 111);')]");
	By customerMonoLongestStreakBar = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#98D9D9']");
	By customerMonoAverageStreakBar = By.xpath("//div[text()='Customer Talk Streaks - Team-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _1 enable-dots']//*[name()='rect' and @fill='#F2A86F']");

	By customerMonoYAxisAgentLabel			  = By.xpath("//div[text()='Customer Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By customerMonoChartBody1AgentLevel	   	  = By.xpath("//div[text()='Customer Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By customerMonoChartBody2AgentLevel	   	  = By.xpath("//div[text()='Customer Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By customerMonoXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Customer Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By customerMonoYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Customer Talk Streaks - Agent-level']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Silence
	By silenceMetrics			= By.xpath("//div[text()='Silence Analysis - Org Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By silencePercentageNew		= By.xpath("//div[text()='Org Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By silenceChartXAxisLabel	= By.xpath("//div[text()='Org Silence Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By silenceChartYAxisLabel	= By.xpath("//div[text()='Org Silence Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By silenceChartBody1		= By.xpath("//div[text()='Org Silence Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='_1 goal']");
	By silenceChartBody2		= By.xpath("//div[text()='Org Silence Percentage by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By silenceTeamLevel		 	 = By.xpath("//div[text()='Silence Analysis - Team Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='agents with unusual talk percentages']");
	By silenceYAxisTeamLabel	 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By silenceChartBody3		 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By silenceChartBody4		 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By silenceXAxisTextAnchors 	 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By silenceYAxisTextAnchors 	 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By silenceRectBars			 = By.xpath("//div[text()='Average Silence Ratio - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	By silenceAgentLevel				 = By.xpath("//div[text()='Silence Analysis - Agent Level']/ancestor::div[@class='DashCard react-resizable']//div[text()='key metrics']");
	By silenceYAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By silenceXAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By silenceChartBody1AgentLevel	     = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By silenceChartBody2AgentLevel	     = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By silenceXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By silenceYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Silence Percentage - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Interactions
	By interactionsPercentageNew	= By.xpath("//div[text()='Org Speaker Transitions per Minute - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By interactionsChartYAxisLabel	= By.xpath("//div[text()='Org Speaker Transitions by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interactionsChartBody1		= By.xpath("//div[text()='Org Speaker Transitions by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By interactionsChartBody2		= By.xpath("//div[text()='Org Speaker Transitions by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By interactionsYAxisTeamLabel	= By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interactionsChartBody3		= By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By interactionsChartBody4		= By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By interactionsXAxisTextAnchors = By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By interactionsYAxisTextAnchors = By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By interactionsRectBars			= By.xpath("//div[text()='Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	By interactionsYAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By interactionsXAxisAgentLabel			 = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='x-axis-label']");
	By interactionsChartBody1AgentLevel	     = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By interactionsChartBody2AgentLevel	     = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By interactionsXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By interactionsYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent-level Speaker Transitions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Activity Metrics 
	
	//Calling Productivity
	By callingProdCalls				= By.xpath("//div[text()='Org Total Call Volume - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By callingProdChartYAxisLabel	= By.xpath("//div[text()='Org Call Volume by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By callingProdChartBody1		= By.xpath("//div[text()='Org Call Volume by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By callingProdChartBody2		= By.xpath("//div[text()='Org Call Volume by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By callingProdTeamCalls		 	= By.xpath("//div[text()='Total Team Call Volume - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By callingProdYAxisTeamLabel	 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By callingProdChartBody3		 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By callingProdChartBody4		 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By callingProdXAxisTextAnchors 	 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By callingProdYAxisTextAnchors 	 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By callingProdRectBars			 = By.xpath("//div[text()='Team Call Volume']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	By callingProdAgentCalls		 	 	 = By.xpath("//div[text()='Total Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By callingProdYAxisAgentLabel			 = By.xpath("//div[text()='Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By callingProdChartBody1AgentLevel	     = By.xpath("//div[text()='Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By callingProdChartBody2AgentLevel	     = By.xpath("//div[text()='Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By callingProdXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By callingProdYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Call Count - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Talk Time
	By talkTimeHours			= By.xpath("//div[text()='Org Total Talktime - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By talkTimeChartYAxisLabel	= By.xpath("//div[text()='Org Talktime by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkTimeChartBody1		= By.xpath("//div[text()='Org Talktime by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By talkTimeChartBody2		= By.xpath("//div[text()='Org Talktime by Date - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By talkTimeTeamCalls		 = By.xpath("//div[text()='Total Team Talktime - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By talkTimeYAxisTeamLabel	 = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkTimeChartBody3		 = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By talkTimeChartBody4		 = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By talkTimeXAxisTextAnchors  = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By talkTimeYAxisTextAnchors  = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By talkTimeRectBars			 = By.xpath("//div[text()='Team Talktime']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	By talkTimeAgentCalls		 	 	 = By.xpath("//div[text()='Total Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By talkTimeYAxisAgentLabel			 = By.xpath("//div[text()='Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By talkTimeChartBody1AgentLevel	     = By.xpath("//div[text()='Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By talkTimeChartBody2AgentLevel	     = By.xpath("//div[text()='Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By talkTimeXAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By talkTimeYAxisTextAnchorsAgentLevel = By.xpath("//div[text()='Agent Talktime - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");

	//Recorded Call Percentage
	By notRecordedPercentage	= By.xpath("//div[text()='Not Recorded']");
	By recordedPercentage		= By.xpath("//div[text()='Recorded']");
	By ChartLabel			    = By.cssSelector(".grid-line.horizontal");
	By recordedPieChartPath		= By.xpath("//*[local-name()='g'] [contains(@class, 'enable-dots')][1]");
	By notRecordedPieChartPath	= By.xpath("//*[local-name()='g'] [contains(@class, 'enable-dots')][2]");

	By recordedLine				= By.xpath("//div[contains(@class,'LineAreaBarChart')]//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots']//*[@class='line']");
	By notRecordedLine			= By.xpath("//div[contains(@class,'LineAreaBarChart')]//div[@class='dc-chart']//*[name()='g' and @class='sub _1 enable-dots']//*[@class='line']");
	
	By recordedPerctngeYAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By recordedPerctngeChartBody3		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By recordedPerctngeChartBody4		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By recordedPerctngeXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By recordedPerctngeYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By recordedPerctngeRectBars			 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By recordedPerctngeLegendColor 	  = By.xpath("//div[text()='Recorded']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(80, 158, 227);')]");
	By notRecordedPerctngeLegendColor = By.xpath("//div[text()='Not Recorded']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(239, 140, 140);')]");
	By recordedPerctngeBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#509EE3']");
	By notRecordedPerctngeBar = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#EF8C8C']");

	//Time Of Day
	By timeOfDayYAxis1			= By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By timeOfDayYAxis2			= By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label yr-label']");
	By timeOfDayXAxis			= By.xpath("//div[@class='dc-chart']//*[@class='x-axis-label']");
	By timeOfDayChartBody		= By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

//	number of calls placed
	By timeOfDayYAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By timeOfDayYRAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By timeOfDayChartBody3		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By timeOfDayChartBody4		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By timeOfDayXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By timeOfDayYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By timeOfDayRectBars		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	//Dispositions
	By chartTickListData		= By.xpath("//div[@class='dc-chart']//*[@class='tick']");
	By firstRowYAxis			= By.xpath("//div[@class='dc-chart']//*[@class='row _0']//*[name()='text']");
	By dispositionsRowTextYAxis	= By.xpath("//div[@class='dc-chart']//*[contains(@class,'row')]//*[@text-anchor='end']");
	By dispositionsRowTextYAxisStart = By.xpath("//div[@class='dc-chart']//*[contains(@class,'row')]//*[@text-anchor='start']");
	
	By dispositionsYAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By dispositionsXAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='x-axis-label']");
	By dispositionsChartBody3		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By dispositionsChartBody4		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By dispositionsXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By dispositionsYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By dispositionsRectBars		     = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By dispositionsOtherLegendColor 	  = By.xpath("//div[text()='Other']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(80, 158, 227);')]");
	By dispositionsConnectedLegendColor   = By.xpath("//div[text()='Connected']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(136, 191, 77);')]");
	By dispositionsContactedLegendColor   = By.xpath("//div[text()='Contacted']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(113, 114, 173);')]");
	By dispositionsOtherBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#509EE3']");
	By dispositionsConnectedBar   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#88BF4D']");
	By dispositionsContactedBar   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#7172AD']");
	
	//Call Directions
	By inboundPercentage		= By.xpath("//div[contains(@class, 'flex-row align-center')]//div[text() = 'Inbound']");
	By outboundPercentage		= By.xpath("//div[contains(@class, 'flex-row align-center')]//div[text() = 'Outbound']");
	
	By outboundCallBar			= By.xpath("//div[contains(@class,'LineAreaBarChart')]//div[@class='dc-chart']//*[name()='g' and @class='stack _0']//*[@class='bar']");
	By intboundCallBar			= By.xpath("//div[contains(@class,'LineAreaBarChart')]//div[@class='dc-chart']//*[name()='g' and @class='stack _1']//*[contains(@class,'bar')]");
	
	By directionsYAxisTeamLabel	   = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By directionsXAxisTeamLabel	   = By.xpath("//div[@class='dc-chart']//*[@class='x-axis-label']");
	By directionsChartBody3		   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By directionsChartBody4		   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By directionsXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By directionsYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By directionsRectBars		   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By directionsOutboundLegendColor = By.xpath("//div[text()='Outbound']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(80, 158, 227);')]");
	By directionsInboundLegendColor  = By.xpath("//div[text()='Inbound']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(169, 137, 197);')]");
	By directionsOutboundBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#509EE3']");
	By directionsInboundBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#A989C5']");

	//Local Presence
	By pieChartLabel			    = By.xpath("//div[contains(@class,'layout-centered')]//*[name()='g']");
	By noLocalPresencePercentage	= By.xpath("//div[contains(@class,'EmbedFrame')]//div[contains(@class,'fullscreen-text-small')]/div[1]//div[text()='No Local Presence']/ancestor::li//span[contains(@class, 'LegendItem flex-align-right')]");
	By localPresencePercentage		= By.xpath("//div[contains(@class,'EmbedFrame')]//div[contains(@class,'fullscreen-text-small')]/div[1]//div[text()='Local Presence']/ancestor::li//span[contains(@class, 'LegendItem flex-align-right')]");

	By localPresenceYAxisTeamLabel	  = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By localPresenceXAxisTeamLabel	  = By.xpath("//div[@class='dc-chart']//*[@class='x-axis-label']");
	By localPresenceChartBody3		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By localPresenceChartBody4		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By localPresenceXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By localPresenceYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By localPresenceRectBars1		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");
	By localPresenceRectBars2		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='stack _1']//*[name()='rect']");
	By localPresenceLegendColor 	  = By.xpath("//div[text()='Local Presence']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(80, 158, 227);')]");
	By noLocalPresenceLegendColor     = By.xpath("//div[text()='No Local Presence']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(136, 191, 77);')]");
	By localPresenceBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#509EE3']");
	By noLocalPresenceBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#88BF4D']");

	//keyword Impact
	By mentionsSection 				 = By.xpath("//div[text()='Top Keyword Group Mentions - New']/ancestor::div[@class='DashCard react-resizable']");
	By mentionsNewYAxis			     = By.xpath("//div[text()='Top Keyword Group Mentions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By mentionsNewChartBody		     = By.xpath("//div[text()='Top Keyword Group Mentions - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By mentionsIncreasesNewSection   = By.xpath("//div[text()='Keyword Group Mentions - Increases - New']/ancestor::div[@class='DashCard react-resizable']");
	By mentionsIncreaseNewYAxis	     = By.xpath("//div[text()='Keyword Group Mentions - Increases - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");

	By mentionsDecreasesNewSection   = By.xpath("//div[text()='Keyword Group Mentions - Decreases - New']/ancestor::div[@class='DashCard react-resizable']");
	By mentionsDecreasesNewYAxis	 = By.xpath("//div[text()='Keyword Group Mentions - Decreases - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");

	By topUngroupedNewSection       = By.xpath("//div[text()='Top Ungrouped Keywords - New']/ancestor::div[@class='DashCard react-resizable']");
	By topUngroupedNewYAxis	     	= By.xpath("//div[text()='Top Ungrouped Keywords - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By topUngroupedNewChartBody	 	= By.xpath("//div[text()='Top Ungrouped Keywords - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");

	By groupMentionsOverTimeSection   = By.xpath("//div[text()='Keyword Group Mentions Over Time - WIP']/ancestor::div[@class='DashCard react-resizable']");
	By groupMentionsOverTimeYAxis	  = By.xpath("//div[text()='Keyword Group Mentions Over Time - WIP']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By groupMentionsOverTimeChartBody = By.xpath("//div[text()='Keyword Group Mentions Over Time - WIP']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	//Coaching Events
	
	//Coaching Volume
	By smartRecrdingsActivitiesSection   = By.xpath("//div[text()='Smartrecording Activities - New']/ancestor::div[@class='DashCard react-resizable']");
	By smartRecrdingsActivitiesYAxis	 = By.xpath("//div[text()='Smartrecording Activities - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By smartRecrdingsActivitiesChartBody = By.xpath("//div[text()='Smartrecording Activities - New']/ancestor::div[@class='DashCard react-resizable']//div[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By totalTagsNewData					 = By.xpath("//div[text()='Total Tags - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By totalAnnotationsNewData			 = By.xpath("//div[text()='Total Annotations - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By totalPlaysNewData				 = By.xpath("//div[text()='Total Plays - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	By totalRealtimeListenersNewData	 = By.xpath("//div[text()='Total Realtime Listens - New']/ancestor::div[@class='DashCard react-resizable']//h1");
	
	//Coaching Events Other Data
	By coachingEventsOtherXAxis   		= By.xpath("//*[contains(@class,'LineAreaBarChart')]//*[@class='dc-chart']//*[@class='x-axis-label']");
	By coachingEventsOtherYAxis	 		= By.xpath("//*[contains(@class,'LineAreaBarChart')]//*[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By coachingEventsOtherChartBody 	= By.xpath("//*[contains(@class,'LineAreaBarChart')]//*[@class='dc-chart']//*[name()='g' and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	
	By coachingEventsYAxisTeamLabel	 = By.xpath("//div[@class='dc-chart']//*[@class='y-axis-label y-label']");
	By coachingEventsChartBody3		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='_1 trend']");
	By coachingEventsChartBody4		 = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']");
	By coachingEventsXAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: middle;']");
	By coachingEventsYAxisTextAnchors  = By.xpath("//div[@class='dc-chart']//*[name()='g' ]//*[text() and @style='text-anchor: end;']");
	By coachingEventsRectBars		   = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect']");

	//Coaching Events Legends and Bars
	By coachingEventsAnnotationsLegendColor = By.xpath("//div[text()='Annotations']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(249, 212, 92);')]");
	By coachingEventsPlaysLegendColor       = By.xpath("//div[text()='Plays']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(113, 114, 173);')]");
	By coachingEventsTagsLegendColor        = By.xpath("//div[text()='Tags']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(136, 191, 77);')]");
	By coachingEventsListensRTLegendColor   = By.xpath("//div[text()='Listens (Real-time)']/ancestor::span[contains(@class, 'LegendItem')]/div[contains(@style, 'background-color: rgb(80, 158, 227);')]");
	By coachingEventsAnnotationsBar 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#F9D45C']");
	By coachingEventsPlaysBar 	 		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#7172AD']");
	By coachingEventsTagsBar 	 		  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#88BF4D']");
	By coachingEventsListensRTBar 	 	  = By.xpath("//div[@class='dc-chart']//*[name()='g'  and @class='sub _0 enable-dots-onhover' or @class='sub _0 enable-dots']//*[name()='rect' and @fill='#509EE3']");

	//data verification locators
	By dotList  						= By.xpath("//*[name()='svg' ]//*[name()='g' and @class='dc-tooltip-list']//*[name()='circle']");
	By hoverDateText 					= By.xpath("//td[@class='text-light text-right pr1' and text()='date']/following-sibling::td[@class='text-bold text-left']");
	By xAxisList 						= By.cssSelector(".axis.x [x='0']");
	
	String hoverTextElement				= "//td[@class='text-light text-right pr1' and text()='$hoverText$']/following-sibling::td[@class='text-bold text-left']";
	
	By callContextSelectTab 			= By.cssSelector("[data-testid='callContext-input'] div");
	By manageMentLevelSelectTab 		= By.cssSelector("[data-testid='managementLevel-input'] div");
	By legendItems						= By.xpath("//span[contains(@class, 'LegendItem')]");
	
	static String dateFormat 		   = "MMMMM dd, yyyy";
	static String dateFormatYear       = "MMMMM, yyyy";
	static String dateFormatWeekRange  = "MMMM dd \u2013 dd, yyyy";
	static String dateFormatWeekRange2 = "MMMM dd \u2013 MMMM dd, yyyy";
	static String dateFormatWeekRange3 = "MMMM dd, yyyy \u2013 MMMM dd, yyyy";
	
	List<String> userNameAgentList = new ArrayList<String>();
	List<String> locationAgentList = new ArrayList<String>();
	List<String> teamAgentList = new ArrayList<String>();

	public static enum ReportsType {
		TalkingVsListening, Interruptions, Silence, Interactions, CallingProd, TalkTime, AgentMono, CustomerMono,
		RecordedCallPercentage, Dispositions, CallDirections, LocalPresence, CoachingReceived, CoachingGiven, CoachingVolume,
		PeerCoaching, SelfReview, TimeOfDay;
	}
	
	public static enum FilterType {
		CallContext, ManagementLevel;
	}
	
	// Enum for selecting call duration
	public static enum CreatedDateFilter {
		Week("Last 7 days"), Month("Last 30 days"), Last90days("Last 90 days"), Annual("Last Year");

		private String value;

		CreatedDateFilter(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	//
	public static enum CallContextsEnumList {
		AllCallContexts("All Call Contexts"),
		AccountManagement("Account Management"),
		LeadQualification("Lead Qualification"),
		MarketingLeadQualification("Marketing Lead Qualification"),
		MarketingQualification("Marketing Qualification");

		private String value;

		CallContextsEnumList(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	//
	public static enum ManageMentLevelEnumList {
		AllManageMentLevels("All Management Levels"),
		CSuite("C-Suite"),
		Director("Director"),
		IndividualContributor("Individual Contributor"),
		Manager("Manager"),
		Partner("Partner"),
		Unknown("Unknown"),
		VicePresident("Vice President");

		private String value;

		ManageMentLevelEnumList(String envValue) {
			this.value = envValue;
		}

		public String getValue() {
			return value;
		}
		
	}

	/**Navigate to Insights or CAI Reports Section
	 * @param driver
	 */
	public void openInsightsTab(WebDriver driver) {
		waitUntilVisible(driver, inSightsLink);
		clickByJs(driver, inSightsLink);
	}
	
	/**expand CAI Etiquette section if hidden
	 * @param driver
	 * @param locator
	 */
	public void expandCAIEtiquetteSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (findElement(driver, talkingvsListeningContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, caiEtiquetteExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertTrue(isElementVisible(driver, talkingvListeningLink, 2));
		assertTrue(isElementVisible(driver, talkingvsListeningContainer, 2));
	}
	
	/**collapse CAI Etiquette section
	 * @param driver
	 * @param locator
	 */
	public void collapseCAIEtiquetteSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (!findElement(driver, talkingvsListeningContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, caiEtiquetteExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, talkingvListeningLink, 2));
		assertFalse(isElementVisible(driver, talkingvsListeningContainer, 2));
	}
	
	/**expand Activity Metrics section if hidden
	 * @param driver
	 * @param locator
	 */
	public void expandActivityMetricsSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (findElement(driver, callingProductivityContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, activityMetricsExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertTrue(isElementVisible(driver, callingProductivityLink, 2));
		assertTrue(isElementVisible(driver, callingProductivityContainer, 2));
	}
	
	/**collapse Activity Metrics section
	 * @param driver
	 * @param locator
	 */
	public void collapseActivityMetricsSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (!findElement(driver, callingProductivityContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, activityMetricsExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, callingProductivityLink, 2));
		assertFalse(isElementVisible(driver, callingProductivityContainer, 2));
	}

	/**expand Coaching Events section if hidden
	 * @param driver
	 * @param locator
	 */
	public void expandCoachingEventsSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (findElement(driver, coachingVolumeContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, coachingEventsExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertTrue(isElementVisible(driver, coachingVolumeLink, 2));
		assertTrue(isElementVisible(driver, coachingVolumeContainer, 2));
	}
	
	/**collapse Coaching Events section
	 * @param driver
	 * @param locator
	 */
	public void collapseCoachingEventsSection(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		if (!findElement(driver, coachingVolumeContainer).getAttribute("class").contains("hidden")) {
			clickByJs(driver, coachingEventsExpandLink);
		}
		
		dashboard.isPaceBarInvisible(driver);
		assertFalse(isElementVisible(driver, coachingVolumeLink, 2));
		assertFalse(isElementVisible(driver, coachingVolumeContainer, 2));
	}
	
	/**Navigate to talking vs listening section
	 * @param driver
	 */
	public void navigateToTalkingVListening(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, talkingvListeningLink);
		clickByJs(driver, talkingvListeningLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Interruptions section
	 * @param driver
	 */
	public void navigateToInterruptions(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, interruptionsLink);
		clickByJs(driver, interruptionsLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to AgentMonologues section
	 * @param driver
	 */
	public void navigateToAgentMonologues(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, agentMonologuesLink);
		clickByJs(driver, agentMonologuesLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to CustomerMonologues section
	 * @param driver
	 */
	public void navigateToCustomerMonologues(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, customerMonologuesLink);
		clickByJs(driver, customerMonologuesLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Silence section
	 * @param driver
	 */
	public void navigateToSilence(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, silenceLink);
		clickByJs(driver, silenceLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Interactions section
	 * @param driver
	 */
	public void navigateToInteractions(WebDriver driver) {
		expandCAIEtiquetteSection(driver);
		waitUntilVisible(driver, interactionsLink);
		clickByJs(driver, interactionsLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to CallingProductivity section
	 * @param driver
	 */
	public void navigateToCallingProductivity(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, callingProductivityLink);
		clickByJs(driver, callingProductivityLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to TalkTime section
	 * @param driver
	 */
	public void navigateToTalkTime(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, talkTimeLink);
		clickByJs(driver, talkTimeLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to RecordedCallPercentage section
	 * @param driver
	 */
	public void navigateToRecordedCallPercentage(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, recordPercentageLink);
		clickByJs(driver, recordPercentageLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to TimeOfDay section
	 * @param driver
	 */
	public void navigateToTimeOfDay(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, timeDayLink);
		clickByJs(driver, timeDayLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Dispositions section
	 * @param driver
	 */
	public void navigateToDispositions(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, dispositionsLink);
		clickByJs(driver, dispositionsLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Call Directions section
	 * @param driver
	 */
	public void navigateToCallDirections(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, callDirectionsLink);
		clickByJs(driver, callDirectionsLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Local Presence section
	 * @param driver
	 */
	public void navigateToLocalPresence(WebDriver driver) {
		expandActivityMetricsSection(driver);
		waitUntilVisible(driver, localPresenceLink);
		clickByJs(driver, localPresenceLink);
		dashboard.isPaceBarInvisible(driver);
	}

	/**Navigate to Keyword Impact section
	 * @param driver
	 */
	public void navigateToKeywordImpact(WebDriver driver) {
		waitUntilVisible(driver, keywordImpactLink);
		clickByJs(driver, keywordImpactLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Coaching Volume section
	 * @param driver
	 */
	public void navigateToCoachingVolume(WebDriver driver) {
		expandCoachingEventsSection(driver);
		waitUntilVisible(driver, coachingVolumeLink);
		clickByJs(driver, coachingVolumeLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Coaching Received section
	 * @param driver
	 */
	public void navigateToCoachingReceived(WebDriver driver) {
		expandCoachingEventsSection(driver);
		waitUntilVisible(driver, coachingReceivedLink);
		clickByJs(driver, coachingReceivedLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Coaching Given section
	 * @param driver
	 */
	public void navigateToCoachingGiven(WebDriver driver) {
		expandCoachingEventsSection(driver);
		waitUntilVisible(driver, coachingGivenLink);
		clickByJs(driver, coachingGivenLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Peer Coaching section
	 * @param driver
	 */
	public void navigateToPeerCoaching(WebDriver driver) {
		expandCoachingEventsSection(driver);
		waitUntilVisible(driver, peerCoachingLink);
		clickByJs(driver, peerCoachingLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Self Review section
	 * @param driver
	 */
	public void navigateToSelfReview(WebDriver driver) {
		expandCoachingEventsSection(driver);
		waitUntilVisible(driver, selfReviewLink);
		clickByJs(driver, selfReviewLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Navigate to Missed Meeting section
	 * @param driver
	 */
	public void navigateToMissedMeeting(WebDriver driver) {
		waitUntilVisible(driver, missedMeetingLink);
		clickByJs(driver, missedMeetingLink);
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**wait for loading spinner for invisible
	 * @param driver
	 */
	public void isLoadingSpinnerInVisible(WebDriver driver) {
		isListElementsVisible(driver, loadingSpinner, 5);
		waitUntilInvisible(driver, loadingSpinner, Integer.valueOf(ReportBase.CONFIG.getProperty("qa_loading_spinner_wait_time")));
	}

	/**wait for still waiting msg for invisible
	 * @param driver
	 */
	public void isStillWaitingMsgInVisible(WebDriver driver) {
		isListElementsVisible(driver, stillWaitingMsg, 5);
		waitUntilInvisible(driver, stillWaitingMsg, Integer.valueOf(ReportBase.CONFIG.getProperty("qa_loading_spinner_wait_time")));
	}
	
	
	/**Switch to Report Frame
	 * @param driver
	 */
	public void switchToReportFrame(WebDriver driver) {
		scrollTillEndOfPage(driver);
		waitUntilVisible(driver, iframeTable);
		switchToIframe(driver, iframeTable);
		isLoadingSpinnerInVisible(driver);
		isStillWaitingMsgInVisible(driver);
		isListElementsVisible(driver, totalSections, 5);
	}

	/**click refresh button
	 * @param driver
	 */
	public void clickRefreshButton(WebDriver driver) {
		dashboard.isPaceBarInvisible(driver);
		waitUntilVisible(driver, refreshButton);
		clickByJs(driver, refreshButton);
		dashboard.isPaceBarInvisible(driver);
	}

	public void verifyEmptyAccountError(WebDriver driver) {
		assertTrue(isElementVisible(driver, accountHelperText, 5));

	}
	
	/**Verify Default created Date is Last 7 days
	 * @param driver
	 */
	public void verifyDefaultTimeFilter(WebDriver driver) {
		waitUntilVisible(driver, createdDateInputTab);
		assertEquals(getElementsText(driver, createdDateInputTab), CreatedDateFilter.Week.getValue());
	}
	
	/**Selected Date filter
	 * @param driver
	 * @param duration
	 */
	public void selectCreatedDateFilter(WebDriver driver, CreatedDateFilter duration) {
		clickAndSelectFromDropDown(driver, createdDateInputTab, listBoxItems, duration.getValue());
		dashboard.isPaceBarInvisible(driver);
	}

//////////////////////////////////CAI Etiquette Section Starts//////////////////////////////////////////////////////
	
	/**method to compare images after applying filter and verifying if image changed or not
	 * @param driver
	 * @param chartType
	 * @param reportType
	 */
	public void verifyReportImageDifferentAfterFilter(WebDriver driver, FilterType chartType, ReportsType reportType) {
		
		FilterType filterType = (FilterType) chartType;
		
		ReportsType reportTypeSwitch = (ReportsType) reportType;
		
		driver.switchTo().defaultContent();
		
		// taking initial image captured
		selectCreatedDateFilter(driver, CreatedDateFilter.Annual);
		clickRefreshButton(driver);
		switchToReportFrame(driver);
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 2));
		By imageLocator = null;
		
		if (isElementVisible(driver, dashBoardGridImage, 5))
			imageLocator = dashBoardGridImage;
		else if (isElementVisible(driver, chartImage, 5))
			imageLocator = chartImage;
		else if (isElementVisible(driver, pieChartImage, 5))
			imageLocator = pieChartImage;		
		
		WebElement webElement = findElement(driver, imageLocator);
		File actualImage = HelperFunctions.captureElementScreenShot(driver, webElement);
		
		//applying filter and clicking refresh button
		driver.switchTo().defaultContent();
		switch (filterType) {
		//LeadQualification
		case CallContext:
			verifyOptionsInCallContextFilter(driver);
			selectCallContextFilter(driver, CallContextsEnumList.LeadQualification);
			verifyTextSelectedCallContext(driver, CallContextsEnumList.LeadQualification);
			break;
		case ManagementLevel:
			verifyOptionsInManagementLevelFilter(driver);
			selectManagementLevelFilter(driver, ManageMentLevelEnumList.Unknown);
			verifyTextSelectedManageMentLevel(driver, ManageMentLevelEnumList.Unknown);
			break;

		default:
			break;
		}
		clickRefreshButton(driver);

		//verifying data after filter
		switchToReportFrame(driver);
		switch(reportTypeSwitch) {
		case TalkingVsListening:
			verifyTalkingVsListeningData(driver);
			break;
		case AgentMono:
			verifyAgentMonologuesData(driver);
			break;
		case CustomerMono:
			verifyCustomerMonologuesData(driver);
			break;
		case CallDirections:
			verifyCallDirectionsData(driver);
			break;
		case CallingProd:
			verifyCallingProductivityData(driver);
			break;
		case CoachingGiven:
			verifyCoachingGivenData(driver);
			break;
		case CoachingReceived:
			verifyCoachingReceivedData(driver);
			break;
		case CoachingVolume:
			verifyCoachingVolumeData(driver);
			break;
		case Dispositions:
			verifyDispositionsData(driver);
			break;
		case Interactions:
			verifyInteractionsData(driver);
			break;
		case Interruptions:
			verifyInterruptionsData(driver);
			break;
		case LocalPresence:
			verifyLocalPresenceData(driver);
			break;
		case PeerCoaching:
			verifyPeerCoachingData(driver);
			break;
		case RecordedCallPercentage:
			verifyRecordedCallPercentageData(driver);
			break;
		case SelfReview:
			verifySelfReviewData(driver);
			break;
		case Silence:
			verifySilenceData(driver);
			break;
		case TalkTime:
			verifyTalkTimeData(driver);
			break;
		case TimeOfDay:
			verifyTimeOfDayData(driver);
			break;
		default:
			break;
		
		}
		
		//taking after image captured after filtering
		webElement = findElement(driver, imageLocator);
		waitUntilVisible(driver, webElement);
		File expectedImage = HelperFunctions.captureElementScreenShot(driver, webElement);
		
		assertFalse(HelperFunctions.bufferedImagesEqual(actualImage, expectedImage));
		
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Talking Vs Listening 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyTalkingVsListeningData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 3);
		assertTrue(isElementVisible(driver, talkingKeyMetrics, 5));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, talkPercentageNew)));
		assertEquals(getElementsText(driver, talkChartXAxisLabel), "date");
		assertEquals(getElementsText(driver, talkChatYAxisLabel), "Average Talk Percentage");
		assertTrue(isElementVisible(driver, talkingChartBody1, 5) 
				|| isElementVisible(driver, talkingChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Talking Vs Listening 
	 * and 'No results' section not present at team level filter
	 * 
	 * @param driver
	 */
	public void verifyTalkingVsListeningTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, talkingTeamLevel, 5));
		assertEquals(getElementsText(driver, talkingYAxisTeamLabel), "Agent Talk Percentage");
		assertTrue(isElementVisible(driver, talkingChartBody3, 5) 
				|| isElementVisible(driver, talkingChartBody4, 5));
		assertTrue(isListElementsVisible(driver, talkingRectBars, 5));
		int rectBars = getElements(driver, talkingRectBars).size();
		assertTrue(isListElementsVisible(driver, talkingXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, talkingYAxisTextAnchors, 5));
		assertEquals(getElements(driver, talkingXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, talkingYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Talking Vs Listening 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 */
	public void verifyTalkingVsListeningAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, talkingAgentLevel, 5));
		assertEquals(getElementsText(driver, talkingYAxisAgentLabel), "Percentage");
		assertEquals(getElementsText(driver, talkingXAxisAgentLabel), "date");
		assertTrue(isElementVisible(driver, talkingChartBody1AgentLevel, 5) 
				|| isElementVisible(driver, talkingChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, talkingYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Interruptions 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyInterruptionsData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 3);
		assertTrue(isElementVisible(driver, interruptionsMetrics, 5));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, interruptionsPercentageNew)));
		assertEquals(getElementsText(driver, interruptionsChartXAxisLabel), "date");
		assertEquals(getElementsText(driver, interruptionsChartYAxisLabel), "Avg Overtalk Percentage");
		assertTrue(isElementVisible(driver, interruptionsChartBody1, 5)
				|| isElementVisible(driver, interruptionsChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Interruptions 
	 * and 'No results' section not present at team level filter
	 * 
	 * @param driver
	 */
	public void verifyInterruptionsDataTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, interruptionsTeamLevel, 5));
		assertEquals(getElementsText(driver, interruptionsYAxisTeamLabel), "Agent Overtalk Percentage");
		assertTrue(isElementVisible(driver, interruptionsChartBody3, 5) 
				|| isElementVisible(driver, interruptionsChartBody4, 5));
		assertTrue(isListElementsVisible(driver, interruptionsRectBars, 5));
		int rectBars = getElements(driver, interruptionsRectBars).size();
		assertTrue(isListElementsVisible(driver, interruptionsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, interruptionsYAxisTextAnchors, 5));
		assertEquals(getElements(driver, interruptionsXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, interruptionsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Interruptions 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyInterruptionsAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, interruptionsAgentLevel, 5));
		assertEquals(getElementsText(driver, interruptionsYAxisAgentLabel), "Percentage");
		assertEquals(getElementsText(driver, interruptionsXAxisAgentLabel), "date");
		assertTrue(isElementVisible(driver, interruptionsChartBody1AgentLevel, 5) 
				|| isElementVisible(driver, interruptionsChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, interruptionsYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}


	/**Verify data X-axis,Y-axis, chart body, sections are present for Agent Monologues 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyAgentMonologuesData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 4);
		assertTrue(isElementVisible(driver, agentMonoMetrics, 5));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, agentMonoPercentageNew1)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, agentMonoPercentageNew2)));
		assertEquals(getElementsText(driver, agentMonoChartYAxisLabel), "Seconds");
		assertTrue(isElementVisible(driver, agentMonoChartBody, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Agent Monologues  
	 * and 'No results' section not present at team level filter
	 * 
	 * @param driver
	 */
	public void verifyAgentMonologuesDataTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, agentMonoTeamLevel, 5));
		assertEquals(getElementsText(driver, agentMonoYAxisTeamLabel), "Seconds");
		assertTrue(isElementVisible(driver, agentMonoChartBody3, 5) 
				|| isElementVisible(driver, agentMonoChartBody4, 5));
		assertTrue(isListElementsVisible(driver, agentMonoRectBars, 5));
		int rectBars = getElements(driver, agentMonoRectBars).size();
		assertTrue(isListElementsVisible(driver, agentMonoXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, agentMonoYAxisTextAnchors, 5));
		assertEquals(getElements(driver, agentMonoXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, agentMonoYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for AgentMonologues 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyAgentMonologuesAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, agentMonoAgentLevel, 5));
		assertEquals(getElementsText(driver, agentMonoYAxisAgentLabel), "Seconds");
		assertEquals(getElementsText(driver, agentMonoXAxisAgentLabel), "date");
		assertTrue(isElementVisible(driver, agentMonoChartBody1AgentLevel, 5) 
				|| isElementVisible(driver, agentMonoChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, agentMonoYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Customer Monologues 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCustomerMonologuesData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 3);
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, customerMonoPercentageNew1)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, customerMonoPercentageNew2)));
		assertEquals(getElementsText(driver, customerMonoChartYAxisLabel), "Seconds");
		assertTrue(isElementVisible(driver, customerMonoChartBody, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Customer Monologues  
	 * and 'No results' section not present at team level filter
	 * 
	 * @param driver
	 */
	public void verifyCustomerMonologuesDataTeamLevelFilter(WebDriver driver) {
		assertEquals(getElementsText(driver, customerMonoYAxisTeamLabel), "Seconds");
		assertTrue(isElementVisible(driver, customerMonoChartBody3, 5) 
				|| isElementVisible(driver, customerMonoChartBody4, 5));
		assertTrue(isListElementsVisible(driver, customerMonoRectBars, 5));
		int rectBars = getElements(driver, customerMonoRectBars).size();
		assertTrue(isListElementsVisible(driver, customerMonoXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, customerMonoYAxisTextAnchors, 5));
		assertEquals(getElements(driver, customerMonoXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, customerMonoYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for CustomerMonologues 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyCustomerMonologuesAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertEquals(getElementsText(driver, customerMonoYAxisAgentLabel), "Seconds");
		assertTrue(isElementVisible(driver,  customerMonoChartBody1AgentLevel, 5) 
				|| isElementVisible(driver,  customerMonoChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, customerMonoYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Silence 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifySilenceData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 3);
		assertTrue(isElementVisible(driver, silenceMetrics, 5));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, silencePercentageNew)));
		assertEquals(getElementsText(driver, silenceChartXAxisLabel), "date");
		assertEquals(getElementsText(driver, silenceChartYAxisLabel), "Avg Silence Percentage");
		assertTrue(isElementVisible(driver, silenceChartBody1, 5) 
				|| isElementVisible(driver, silenceChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Silence  
	 * and 'No results' section not present at team level filter
	 * 
	 * @param driver
	 */
	public void verifySilenceDataTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, silenceTeamLevel, 5));
		assertEquals(getElementsText(driver, silenceYAxisTeamLabel), "Silence Percentage");
		assertTrue(isElementVisible(driver, silenceChartBody3, 5) 
				|| isElementVisible(driver, silenceChartBody4, 5));
		assertTrue(isListElementsVisible(driver, silenceRectBars, 5));
		int rectBars = getElements(driver, silenceRectBars).size();
		assertTrue(isListElementsVisible(driver, silenceXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, silenceYAxisTextAnchors, 5));
		assertEquals(getElements(driver, silenceXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, silenceYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Silence 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifySilenceAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, silenceAgentLevel, 5));
		assertEquals(getElementsText(driver, silenceYAxisAgentLabel), "Percentage");
		assertTrue(isElementVisible(driver,  silenceChartBody1AgentLevel, 5) 
				|| isElementVisible(driver,  silenceChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, silenceYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Interactions
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyInteractionsData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 2);
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, interactionsPercentageNew)));
		assertEquals(getElementsText(driver, interactionsChartYAxisLabel), "Transitions per Minute");
		assertTrue(isElementVisible(driver, interactionsChartBody1, 5) 
				|| isElementVisible(driver, interactionsChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Interactions
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyInteractionsDataTeamLevelFilter(WebDriver driver) {
		assertEquals(getElementsText(driver, interactionsYAxisTeamLabel), "Transitions per Minute");
		assertTrue(isElementVisible(driver, interactionsChartBody3, 5) 
				|| isElementVisible(driver, interactionsChartBody4, 5));
		assertTrue(isListElementsVisible(driver, interactionsRectBars, 5));
		int rectBars = getElements(driver, interactionsRectBars).size();
		assertTrue(isListElementsVisible(driver, interactionsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, interactionsYAxisTextAnchors, 5));
		assertEquals(getElements(driver, interactionsXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, interactionsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Interactions 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyInteractionsAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertEquals(getElementsText(driver, interactionsYAxisAgentLabel), "Transitions per Minute");
		assertTrue(isElementVisible(driver,  interactionsChartBody1AgentLevel, 5) 
				|| isElementVisible(driver,  interactionsChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, interactionsYAxisTextAnchorsAgentLevel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
//////////////////////////////////CAI Etiquette Section Ends//////////////////////////////////////////////////////
	
	
//////////////////////////////////Activity Metrics Section Starts//////////////////////////////////////////////////////
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Calling Productivity 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCallingProductivityData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 2);
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, callingProdCalls)));
		assertEquals(getElementsText(driver, callingProdChartYAxisLabel), "Number of Calls");
		assertTrue(isElementVisible(driver, callingProdChartBody1, 5) 
				|| isElementVisible(driver, callingProdChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Calling Productivity
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCallingProductivityDataTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, callingProdTeamCalls, 5));
		String[] calls = getElementsText(driver, callingProdTeamCalls).split(" Calls");
		assertTrue(Integer.parseInt(calls[0])>0);
		
		assertEquals(getElementsText(driver, callingProdYAxisTeamLabel), "Number of Calls");
		assertTrue(isElementVisible(driver, callingProdChartBody3, 5) 
				|| isElementVisible(driver, callingProdChartBody4, 5));
		assertTrue(isListElementsVisible(driver, callingProdRectBars, 5));
		int rectBars = getElements(driver, callingProdRectBars).size();
		assertTrue(isListElementsVisible(driver, callingProdXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, callingProdYAxisTextAnchors, 5));
		assertEquals(getElements(driver, callingProdXAxisTextAnchors).size(), rectBars);
		
		assertTrue(isListElementsVisible(driver, callingProdYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, callingProdYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for CallingProductivity 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyCallingProductivityAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, callingProdAgentCalls, 5));
		
		String[] calls = getElementsText(driver, callingProdAgentCalls).split(" Calls");
		assertTrue(Integer.parseInt(calls[0])>0);
		
		assertEquals(getElementsText(driver, callingProdYAxisAgentLabel), "Number of Calls");
		assertTrue(isElementVisible(driver,  callingProdChartBody1AgentLevel, 5) 
				|| isElementVisible(driver,  callingProdChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, callingProdYAxisTextAnchorsAgentLevel, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, callingProdYAxisTextAnchorsAgentLevel));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Talk Time 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyTalkTimeData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 2);
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, talkTimeHours)));
		assertEquals(getElementsText(driver, talkTimeChartYAxisLabel), "Hours");
		assertTrue(isElementVisible(driver, talkTimeChartBody1, 5) 
				|| isElementVisible(driver, talkTimeChartBody2, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Talk Time
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyTalkTimeDataTeamLevelFilter(WebDriver driver) {
		assertTrue(isElementVisible(driver, talkTimeTeamCalls, 5));
		String[] calls = getElementsText(driver, talkTimeTeamCalls).split(" Hours");
		assertTrue(Float.parseFloat(calls[0])>0);
	
		assertEquals(getElementsText(driver, talkTimeYAxisTeamLabel), "Hours");
		assertTrue(isElementVisible(driver, talkTimeChartBody3, 5) 
				|| isElementVisible(driver, talkTimeChartBody4, 5));
		assertTrue(isListElementsVisible(driver, talkTimeRectBars, 5));
		int rectBars = getElements(driver, talkTimeRectBars).size();
		assertTrue(isListElementsVisible(driver, talkTimeXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, talkTimeYAxisTextAnchors, 5));
		assertEquals(getElements(driver, talkTimeXAxisTextAnchors).size(), rectBars);
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, talkTimeYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for TalkTime 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyTalkTimeAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		assertTrue(isElementVisible(driver, talkTimeAgentCalls, 5));
		
		String[] calls = getElementsText(driver, talkTimeAgentCalls).split(" Hours");
		assertTrue(Float.parseFloat(calls[0])>0);
		
		assertEquals(getElementsText(driver, talkTimeYAxisAgentLabel), "Hours");
		assertTrue(isElementVisible(driver,  talkTimeChartBody1AgentLevel, 5) 
				|| isElementVisible(driver,  talkTimeChartBody2AgentLevel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, talkTimeYAxisTextAnchorsAgentLevel, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, talkTimeYAxisTextAnchorsAgentLevel));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify percentage/pie charts sections for Recorded Call Percentage 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyRecordedCallPercentageData(WebDriver driver) {
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, notRecordedPercentage))
				|| (Strings.isNotNullAndNotEmpty(getElementsText(driver, recordedPercentage))));
		assertTrue(isElementVisible(driver, ChartLabel, 5));
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(reportDuration.Annual, dateFormat, dateStringList);

		// verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);

		sortAndVerifyDateCreated(reportDuration.Annual, dateFormat, xAxisDateList);
	
		assertTrue(isListElementsVisible(driver, recordedPerctngeYAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, recordedPerctngeXAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, recordedPerctngeYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		assertTrue(isElementVisible(driver, recordedLine, 5) || isElementVisible(driver, notRecordedLine, 5));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for RecordedCallPercentage
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyRecordedCallPercentageAgentLevelFilter(WebDriver driver) {
		verifyRecordedCallPercentageData(driver);
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for RecordedCallPercentage
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyRecordedCallPercentageDataTeamLevelFilter(WebDriver driver) {
		waitUntilVisible(driver, recordedPerctngeYAxisTeamLabel);
		assertEquals(getElementsText(driver, recordedPerctngeYAxisTeamLabel), "Percent of Calls");
		assertTrue(isElementVisible(driver, recordedPerctngeChartBody3, 5) 
				|| isElementVisible(driver, recordedPerctngeChartBody4, 5));
		assertTrue(isListElementsVisible(driver, recordedPerctngeXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, recordedPerctngeYAxisTextAnchors, 5));
	
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, recordedPerctngeYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**method to get hover checklist for coaching section
	 * @param driver
	 * @return
	 */
	public List<String> getHoverCheckListForRecordedCallPercentage() {
		List<String> hoverCheckList = new ArrayList<String>();
		
		hoverCheckList.clear();
		hoverCheckList.add("is_recorded");
		hoverCheckList.add("call_count");
		hoverCheckList.add("Percentage");
		
		return hoverCheckList;
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Time Of day 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyTimeOfDayData(WebDriver driver) {
		assertEquals(getElementsText(driver, timeOfDayYAxis1), "number of calls placed");
		assertEquals(getElementsText(driver, timeOfDayYAxis2), "connection rate");
		assertEquals(getElementsText(driver, timeOfDayXAxis), "Hour of Day");
		assertTrue(isElementVisible(driver, timeOfDayChartBody, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}


	/**Verify data X-axis,Y-axis, chart body, sections are present for TimeOfDay
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyTimeOfDayDataAllLevelFilter(WebDriver driver) {
		waitUntilVisible(driver, timeOfDayYAxisTeamLabel);
		assertEquals(getElementsText(driver, timeOfDayYAxisTeamLabel), "number of calls placed");
		assertEquals(getElementsText(driver, timeOfDayYRAxisTeamLabel), "connection rate");
		assertTrue(isElementVisible(driver, timeOfDayChartBody3, 5) 
				|| isElementVisible(driver, timeOfDayChartBody4, 5));
		assertTrue(isListElementsVisible(driver, timeOfDayRectBars, 5));
		int rectBars = getElements(driver, timeOfDayRectBars).size();
		assertTrue(isListElementsVisible(driver, timeOfDayXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, timeOfDayYAxisTextAnchors, 5));
		assertEquals(getElements(driver, timeOfDayXAxisTextAnchors).size(), rectBars);
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for Dispositions 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyDispositionsData(WebDriver driver) {
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, firstRowYAxis)));
		assertTrue(isListElementsVisible(driver, chartTickListData, 5));
	
		assertTrue(isListElementsVisible(driver, dispositionsRowTextYAxis, 5)
				|| isListElementsVisible(driver, dispositionsRowTextYAxisStart, 5));
		
		//assertFalse((getTextListFromElements(driver, dispositionsRowTextYAxis) == null
		//		&& getTextListFromElements(driver, dispositionsRowTextYAxis).isEmpty())
		//		||
		//		(getTextListFromElements(driver, dispositionsRowTextYAxisStart) == null
		//		&& getTextListFromElements(driver, dispositionsRowTextYAxisStart).isEmpty()));
		
		List<Float> xAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, dispositionsXAxisTextAnchors));
		assertTrue(xAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(xAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Dispositions
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyDispositionsDataTeamLevelFilter(WebDriver driver) {
		waitUntilVisible(driver, dispositionsYAxisTeamLabel);
		assertEquals(getElementsText(driver, dispositionsYAxisTeamLabel), "disposition_count");
		assertEquals(getElementsText(driver, dispositionsXAxisTeamLabel), "Hour of Day");
		assertTrue(isElementVisible(driver, dispositionsChartBody3, 5) 
				|| isElementVisible(driver, dispositionsChartBody4, 5));
		assertTrue(isListElementsVisible(driver, dispositionsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, dispositionsYAxisTextAnchors, 5));
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, dispositionsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify percentage/pie charts sections for Call Directions 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCallDirectionsData(WebDriver driver) {
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, inboundPercentage))
				|| (Strings.isNotNullAndNotEmpty(getElementsText(driver, outboundPercentage))));
		assertTrue(isElementVisible(driver, ChartLabel, 5));

		// verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);

		sortAndVerifyDateCreated(reportDuration.Annual, dateFormat, xAxisDateList);
		
		assertTrue(isElementVisible(driver, directionsChartBody3, 5) 
				|| isElementVisible(driver, directionsChartBody4, 5));
	
		assertTrue(isListElementsVisible(driver, directionsYAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, directionsXAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, directionsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		assertTrue(isElementVisible(driver, outboundCallBar, 5) || isElementVisible(driver, intboundCallBar, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}

	/**Verify data X-axis,Y-axis, chart body, sections are present for CallDirections
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCallDirectionsDataTeamLevelFilter(WebDriver driver) {
		waitUntilVisible(driver, directionsYAxisTeamLabel);
		assertEquals(getElementsText(driver, directionsYAxisTeamLabel), "Percent of Calls");
		assertEquals(getElementsText(driver, directionsXAxisTeamLabel), "Date");
		assertTrue(isElementVisible(driver, directionsChartBody3, 5) 
				|| isElementVisible(driver, directionsChartBody4, 5));
		assertTrue(isListElementsVisible(driver, directionsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, directionsYAxisTextAnchors, 5));
		
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, directionsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify percentage/pie charts sections for Local Presence 
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyLocalPresenceData(WebDriver driver) {
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, noLocalPresencePercentage))
				|| (Strings.isNotNullAndNotEmpty(getElementsText(driver, localPresencePercentage))));
		assertTrue(isElementVisible(driver, pieChartLabel, 5));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for CallDirections
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyLocalPresenceDataTeamLevelFilter(WebDriver driver) {
		waitUntilVisible(driver, localPresenceYAxisTeamLabel);
		assertEquals(getElementsText(driver, localPresenceYAxisTeamLabel), "disposition_count");
		assertEquals(getElementsText(driver, localPresenceXAxisTeamLabel), "Hour of Day");
		assertTrue(isElementVisible(driver, localPresenceChartBody3, 5) 
				|| isElementVisible(driver, localPresenceChartBody4, 5));
		assertTrue(isListElementsVisible(driver, localPresenceRectBars1, 5)
				|| isListElementsVisible(driver, localPresenceRectBars2, 5));
		assertTrue(isListElementsVisible(driver, localPresenceXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, localPresenceYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, localPresenceYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
//////////////////////////////////Activity Metrics Section Ends//////////////////////////////////////////////////////

//////////////////////////////////Keyword Impact Section Starts//////////////////////////////////////////////////////
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Keyword Impact data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyKeywordImpactData(WebDriver driver) {
		assertEquals(getElements(driver, totalSections).size(), 5);
		assertTrue(isElementVisible(driver, mentionsSection, 5));
		assertEquals(getElementsText(driver, mentionsNewYAxis), "Percentage Of Calls");
		assertTrue(isElementVisible(driver, mentionsNewChartBody, 5)); 
		
		assertTrue(isElementVisible(driver, mentionsIncreasesNewSection, 5));
		assertEquals(getElementsText(driver, mentionsIncreaseNewYAxis), "Change over Prior Period (%)");
		
		assertTrue(isElementVisible(driver, mentionsDecreasesNewSection, 5));
		assertEquals(getElementsText(driver, mentionsDecreasesNewYAxis), "Change over Prior Period (%)");
		
		assertTrue(isElementVisible(driver, topUngroupedNewSection, 5));
		assertEquals(getElementsText(driver, topUngroupedNewYAxis), "Number of Mentions");
		assertTrue(isElementVisible(driver, topUngroupedNewChartBody, 5)); 
	
		assertTrue(isElementVisible(driver, groupMentionsOverTimeSection, 5));
		assertEquals(getElementsText(driver, groupMentionsOverTimeYAxis), "Number of Calls");
		assertTrue(isElementVisible(driver, groupMentionsOverTimeChartBody, 5)); 
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
//////////////////////////////////Keyword Impact Section Ends//////////////////////////////////////////////////////

//////////////////////////////////Coaching Section Starts//////////////////////////////////////////////////////
	
	/**method to get bars locators of coaching section
	 * @param driver
	 * @return
	 */
	public By getSingularRectBarsOfCoachingSection(WebDriver driver) {
		By rectBars = null;
		waitUntilVisible(driver, coachingEventsOtherYAxis);
		String yAxis = getElementsText(driver, coachingEventsOtherYAxis);
		
		switch(yAxis) {
		case "Annotations":
			rectBars = coachingEventsAnnotationsBar;
			break;
		case "Plays":
			rectBars = coachingEventsPlaysBar;
			break;
		case "Tags":
			rectBars = coachingEventsTagsBar;
			break;
		}
		
		return rectBars;
		
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Coaching Volume data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCoachingVolumeData(WebDriver driver) {
		assertTrue(HelperFunctions.verifyIntegerInGivenRange(4, 5, getElements(driver, totalSections).size()));
		assertTrue(isElementVisible(driver, smartRecrdingsActivitiesSection, 5));
		assertEquals(getElementsText(driver, smartRecrdingsActivitiesYAxis), "Percentage of Calls");
		assertTrue(isElementVisible(driver, smartRecrdingsActivitiesChartBody, 5)); 
		
		//assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, totalTagsNewData)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, totalAnnotationsNewData)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, totalPlaysNewData)));
		assertTrue(Strings.isNotNullAndNotEmpty(getElementsText(driver, totalRealtimeListenersNewData)));
		
		//assertTrue(Float.parseFloat(getElementsText(driver, totalTagsNewData).replace(",", "")) > 0 );
		assertTrue(Float.parseFloat(getElementsText(driver, totalAnnotationsNewData).replace(",", "")) > 0 );
		assertTrue(Float.parseFloat(getElementsText(driver, totalPlaysNewData).replace(",", "")) > 0 );
		assertTrue(Float.parseFloat(getElementsText(driver, totalRealtimeListenersNewData).replace(",", "")) > 0 );
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for TalkTime 
	 * and 'No results' section not present at agent level filter
	 * 
	 * @param driver
	 * @param durationRange
	 */
	public void verifyCoachingVolumeDataAgentLevelFilter(WebDriver driver, reportDuration durationRange ) {
		
		verifyCoachingVolumeData(driver);
		
		//verifying date range for dots by mouse hover
		Pair<String, String> startEndDatePair = getStartEndDateMouseHoverCircle(driver);
		String startDate = startEndDatePair.first();
		String endDate = startEndDatePair.second();

		List<String> dateStringList = new ArrayList<String>();
		dateStringList.add(startDate);
		dateStringList.add(endDate);

		sortAndVerifyDateCreated(durationRange, dateFormat, dateStringList);

		//verifying date range for x axis dates
		List<String> xAxisDateList = getDateListXAxis(driver);
		
		sortAndVerifyDateCreated(durationRange, dateFormat, xAxisDateList);
		
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, coachingEventsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
	
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Coaching Received data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCoachingReceivedData(WebDriver driver) {

		//waitUntilVisible(driver, coachingEventsOtherYAxis);
		//assertTrue(getElementsText(driver, coachingEventsOtherYAxis).equals("sum")
		// || getElementsText(driver, coachingEventsOtherYAxis).equals("Plays"));
		assertTrue(getElementsText(driver, coachingEventsOtherXAxis).equals("reviewer name"));
		assertTrue(isElementVisible(driver, coachingEventsOtherChartBody, 5)); 
		
		assertTrue(isListElementsVisible(driver, coachingEventsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, coachingEventsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Coaching Given data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyCoachingGivenData(WebDriver driver) {

		//waitUntilVisible(driver, coachingEventsOtherYAxis);
		//assertTrue(getElementsText(driver, coachingEventsOtherYAxis).equals("sum")
		// || getElementsText(driver, coachingEventsOtherYAxis).equals("Plays"));
//		assertTrue(getElementsText(driver, coachingEventsOtherXAxis).equals("supervisor_name"));
		
		assertTrue(isElementVisible(driver, coachingEventsOtherChartBody, 5)); 
		
		assertTrue(isListElementsVisible(driver, coachingEventsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, coachingEventsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Peer coaching data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifyPeerCoachingData(WebDriver driver) {
		//waitUntilVisible(driver, coachingEventsOtherYAxis);
		//assertTrue(getElementsText(driver, coachingEventsOtherYAxis).equals("sum")
		// || getElementsText(driver, coachingEventsOtherYAxis).equals("Plays"));
//		assertEquals(getElementsText(driver, coachingEventsOtherXAxis), "reviewer name");
		assertTrue(isElementVisible(driver, coachingEventsOtherChartBody, 5)); 
		
		assertTrue(isListElementsVisible(driver, coachingEventsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, coachingEventsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));
		
		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
	/**Verify data X-axis,Y-axis, chart body, sections are present for Self Review data
	 * and 'No results' section not present
	 * 
	 * @param driver
	 */
	public void verifySelfReviewData(WebDriver driver) {

		//waitUntilVisible(driver, coachingEventsOtherYAxis);
		// assertTrue(getElementsText(driver, coachingEventsOtherYAxis).equals("sum")
		// || getElementsText(driver, coachingEventsOtherYAxis).equals("Plays"));
//		assertEquals(getElementsText(driver, coachingEventsOtherXAxis), "reviewer name");
		assertTrue(isElementVisible(driver, coachingEventsOtherChartBody, 5)); 
		
		assertTrue(isListElementsVisible(driver, coachingEventsXAxisTextAnchors, 5));
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		
		assertTrue(isListElementsVisible(driver, coachingEventsYAxisTextAnchors, 5));
		List<Float> yAxisFloatRange = HelperFunctions.getStringListInNumberFormat(getTextListFromElements(driver, coachingEventsYAxisTextAnchors));
		assertTrue(yAxisFloatRange.get(0) == 0);
		assertTrue(HelperFunctions.verifyAscendingOrderedList(yAxisFloatRange));

		assertFalse(isListElementsVisible(driver, noResultsSectionList, 0));
	}
	
//////////////////////////////////Coaching Section Ends//////////////////////////////////////////////////////
	
	/**Get start and end date by mouse hover on first and last circle
	 * @param driver
	 * @return
	 */
	public Pair<String, String> getStartEndDateMouseHoverCircle(WebDriver driver) {
		List<WebElement> list = getElements(driver, dotList);
		hoverElement(driver, list.get(0));
		idleWait(1);
		waitUntilVisible(driver, hoverDateText);
		String startDate = getElementsText(driver, hoverDateText);

		hoverElement(driver, list.get(list.size() - 1));
		idleWait(1);
		waitUntilVisible(driver, hoverDateText);
		String endDate = getElementsText(driver, hoverDateText);

		return new Pair<String, String>(startDate, endDate);
	}
	
	/**Get start and end date by mouse hover on first and last circle
	 * @param driver
	 * @return
	 */
	public Pair<String, String> getStartEndDateXAxis(WebDriver driver) {
		Pair<String, String> returnValuePair = null;
		if (isListElementsVisible(driver, xAxisList, 5)) {
			if (getElements(driver, xAxisList).size() > 1) {
				List<WebElement> list = getElements(driver, xAxisList);
				String startDate = getElementsText(driver, list.get(0));
				String endDate = getElementsText(driver, list.get(list.size() - 1));
				returnValuePair = new Pair<String, String>(startDate, endDate);
			}
		}
		return returnValuePair;
	}
	
	/**Get start and end date by mouse hover on first and last circle
	 * @param driver
	 * @return
	 */
	public List<String> getDateListXAxis(WebDriver driver) {
		List<String> list = new ArrayList<String>();
		
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		//hack to display x axis 
		if (!isListElementsVisible(driver, xAxisList, 5)) {
			jse.executeScript("document.body.style.zoom = '90%';");
			idleWait(2);
			jse.executeScript("document.body.style.zoom = '100%';");
		}

		if (isListElementsVisible(driver, xAxisList, 5)) {
			list = getTextListFromElements(driver, xAxisList);
		}
	
		if (list == null || list.isEmpty()) {
			Assert.fail("X axis date list is empty");
		}
		
		return list;
	}
	
	/**
	 * Verifying Date Range and Order based on custom date on
	 * both Start & End Date Headers
	 * @param driver
	 * @param inputFormat
	 * @param outputFormat
	 * @param index
	 * @param startDate
	 * @param endDate
	 * 
	 */
	public void sortAndVerifyDateCreated(reportDuration time, String newFormat, List<String> dateStringList) {

		//		convert to month,date,year new format according to old format
		List<String> dateStringListCopy = new ArrayList<String>(dateStringList);
		dateStringList.clear();
		for (String dateString : dateStringListCopy) {
			if (HelperFunctions.isDateInGivenFormat(dateFormatWeekRange, dateString)
					|| HelperFunctions.isDateInGivenFormat(dateFormatWeekRange2, dateString)) {
				String stringDate1[] = dateString.split("\u2013");
				String stringDate2[] = dateString.split(",");
				String dateStringComplete = stringDate1[0].trim() + ", " + (stringDate2[1].trim());
				dateStringList.add(dateStringComplete);
			} 
			else if (HelperFunctions.isDateInGivenFormat(dateFormatWeekRange3, dateString)) {
				String stringDate1[] = dateString.split("\u2013");
				String dateStringComplete = stringDate1[0].trim();
				dateStringList.add(dateStringComplete);
			}
			else if (HelperFunctions.isDateInGivenFormat(dateFormatYear, dateString)) {
				dateString = HelperFunctions.changeDateTimeFormat(dateString, dateFormatYear, newFormat);
				dateStringList.add(dateString);
			}
		}
		
		if (dateStringList == null || dateStringList.isEmpty()) {
			dateStringList = new ArrayList<String>(dateStringListCopy);
		}

		Calendar previousCal = Calendar.getInstance();

		// getting todays date with 1 day ahead
		Calendar currentCal = Calendar.getInstance();
		currentCal.add(Calendar.DAY_OF_YEAR, 2);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(newFormat);
		String startDate = new SimpleDateFormat(newFormat).format(currentCal.getTime());

		Date previousDate = null;
		Date currentDate = null;

		try {
			currentDate = dateTimeFormatter.parse(startDate);
			if (time.equals(reportDuration.Week)) {
				previousCal.add(Calendar.DAY_OF_YEAR, -8);
				String lastDate = new SimpleDateFormat(newFormat).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.equals(reportDuration.Month)) {
				previousCal.add(Calendar.MONTH, -1);
				previousCal.add(Calendar.DAY_OF_YEAR, -8);
				String lastDate = new SimpleDateFormat(newFormat).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.equals(reportDuration.Last90days)) {
				previousCal.add(Calendar.DAY_OF_YEAR, -98);
				String lastDate = new SimpleDateFormat(newFormat).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			} else if (time.equals(reportDuration.Annual)) {
				previousCal.add(Calendar.YEAR, -1);
				previousCal.add(Calendar.DAY_OF_YEAR, -31);
				String lastDate = new SimpleDateFormat(newFormat).format(previousCal.getTime());
				previousDate = dateTimeFormatter.parse(lastDate);
			}

			// getting results
			ArrayList<Date> callTimeList = new ArrayList<>();

			// parsing date string accordingly to format
			for (String dateString : dateStringList) {
				callTimeList.add(dateTimeFormatter.parse(dateString));
			}

			// verifying range
			for (Date callTimeDate : callTimeList) {
				assertTrue(callTimeDate.after(previousDate) && callTimeDate.before(currentDate));
			}

		} catch (Exception e) {
			System.out.println("Not able to parse time" + e.getMessage());
			Assert.fail("Failed due to Date Parse error");
		}
	}
	
	/**method to verify text selected in call context filter
	 * @param driver
	 * @param callContext
	 */
	public void verifyTextSelectedCallContext(WebDriver driver, CallContextsEnumList callContext) {
		waitUntilVisible(driver, callContextSelectTab);
		assertEquals(getElementsText(driver, callContextSelectTab), callContext.getValue());
	}
	
	/**method to verify options in call context filter
	 * @param driver
	 */
	public void verifyOptionsInCallContextFilter(WebDriver driver) {

		waitUntilVisible(driver, callContextSelectTab);
		assertEquals(getElementsText(driver, callContextSelectTab), CallContextsEnumList.AllCallContexts.getValue());
		clickElement(driver, callContextSelectTab);
		dashboard.isPaceBarInvisible(driver);

		isListElementsVisible(driver, listBoxItems, 5);
		List<String> itemsTextList = getTextListFromElements(driver, listBoxItems);
		pressEscapeKey(driver);
		
		List<String> enumStringList = new ArrayList<String>();
		for (CallContextsEnumList CallContextsEnumValue : CallContextsEnumList.values()) {
			enumStringList.add(CallContextsEnumValue.getValue());
		}

		assertEquals(itemsTextList, enumStringList);
	}
	
	/**method to get random call context filter
	 * @return
	 */
	public CallContextsEnumList getRandomCallContext() {
		return CallContextsEnumList.values()[random.nextInt(CallContextsEnumList.values().length)];
	}

	/**method to select call context filter
	 * @param driver
	 * @param sortText
	 */
	public void selectCallContextFilter(WebDriver driver, CallContextsEnumList callContext) {
		clickAndSelectFromDropDown(driver, callContextSelectTab, listBoxItems, callContext.getValue());
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**method to verify selected management filter
	 * @param driver
	 * @param manageMentLevel
	 */
	public void verifyTextSelectedManageMentLevel(WebDriver driver, ManageMentLevelEnumList manageMentLevel) {
		waitUntilVisible(driver, manageMentLevelSelectTab);
		assertEquals(getElementsText(driver, manageMentLevelSelectTab), manageMentLevel.getValue());
	}
	
	/**method to verify options in management filter
	 * @param driver
	 */
	public void verifyOptionsInManagementLevelFilter(WebDriver driver) {

		waitUntilVisible(driver, manageMentLevelSelectTab);
		assertEquals(getElementsText(driver, manageMentLevelSelectTab), ManageMentLevelEnumList.AllManageMentLevels.getValue());
		clickElement(driver, manageMentLevelSelectTab);
		dashboard.isPaceBarInvisible(driver);
		
		isListElementsVisible(driver, listBoxItems, 5);
		List<String> itemsTextList = getTextListFromElements(driver, listBoxItems);
		pressEscapeKey(driver);
		
		List<String> enumStringList = new ArrayList<String>();
		for (ManageMentLevelEnumList ManageMentLevelEnumValue : ManageMentLevelEnumList.values()) {
			enumStringList.add(ManageMentLevelEnumValue.getValue());
		}

		assertEquals(itemsTextList, enumStringList);
	}
	
	/**method to select any random management filter
	 * @return
	 */
	public ManageMentLevelEnumList getRandomManagementLevelEnum() {
		return ManageMentLevelEnumList.values()[random.nextInt(ManageMentLevelEnumList.values().length)];
	}

	/**method to select management filter
	 * @param driver
	 * @param sortText
	 */
	public void selectManagementLevelFilter(WebDriver driver, ManageMentLevelEnumList manageMentLevel) {
		clickAndSelectFromDropDown(driver, manageMentLevelSelectTab, listBoxItems, manageMentLevel.getValue());
		dashboard.isPaceBarInvisible(driver);
	}
	
	/**Method to check whether legend colors/items are visible or not
	 * @param driver
	 * @return
	 */
	public boolean isLegendItemsVisible(WebDriver driver) {
		return isListElementsVisible(driver, legendItems, 5);
	}
	
	/**returns user name list to check on x-axis
	 * @return 
	 * 
	 */
	public List<String> getUserNameAgentsListToVerify() {
		userNameAgentList.clear();
		userNameAgentList.add("Tanvi T Fix Vyas");
		userNameAgentList.add("Deepanker Acharya");
		return userNameAgentList;
	}
	
	/**returns location name list to check on x-axis
	 * @return 
	 * 
	 */
	public List<String> getLocationAgentsListToVerify() {
		locationAgentList.clear();
		locationAgentList.add("Tanvi T Fix Vyas");
		locationAgentList.add("Automation2 Test");
		return locationAgentList;
	}
	
	/**returns team name list to check on x-axis
	 * @return
	 */
	public List<String> getTeamAgentsListToVerify() {
		teamAgentList.clear();
		teamAgentList.add("Deepanker Acharya");
		teamAgentList.add("Tanvi T Fix Vyas");
		teamAgentList.add("Deep Acharya");
		teamAgentList.add("Tanvi T Fix Vyas");
		teamAgentList.add("Neeraj Ghiya (Jaipur)");
		teamAgentList.add("tanvi vyas");
		return teamAgentList;
	}
	
	/**
	 * method to check bar data by hovering and verifying with check list passed for
	 * singular bars
	 * 
	 * @param driver
	 * @param chartType
	 * @param agentList
	 * @param hoverCheckList
	 */
	public void verifySingularBarsData(WebDriver driver, Enum<?> chartType, List<String> agentList,
			List<String> hoverCheckList) {

		ReportsType reportsType = (ReportsType) chartType;

		By rectBars = null;
		By xAxisTextAnchors = null;

		switch (reportsType) {
		case TalkingVsListening:
			rectBars = talkingRectBars;
			xAxisTextAnchors = talkingXAxisTextAnchors;
			break;
		case Interruptions:
			rectBars = interruptionsRectBars;
			xAxisTextAnchors = interruptionsXAxisTextAnchors;
			break;
		case Silence:
			rectBars = silenceRectBars;
			xAxisTextAnchors = silenceXAxisTextAnchors;
			break;
		case Interactions:
			rectBars = interactionsRectBars;
			xAxisTextAnchors = interactionsXAxisTextAnchors;
			break;
		case CallingProd:
			rectBars = callingProdRectBars;
			xAxisTextAnchors = callingProdXAxisTextAnchors;
			break;
		case TalkTime:
			rectBars = talkTimeRectBars;
			xAxisTextAnchors = talkTimeXAxisTextAnchors;
			break;
		case CoachingReceived:
		case CoachingGiven:
		case PeerCoaching:
		case SelfReview:
			rectBars = getSingularRectBarsOfCoachingSection(driver);
			xAxisTextAnchors = coachingEventsXAxisTextAnchors;
		default:
			break;

		}

		assertTrue(isListElementsVisible(driver, rectBars, 5));
		assertTrue(isListElementsVisible(driver, xAxisTextAnchors, 5));

		if (agentList != null && !agentList.isEmpty()) {
			List<String> xAxisTextList = getTextListFromElements(driver, xAxisTextAnchors);
			if (agentList.size() == 1)
				assertEquals(xAxisTextList, agentList);
			else
				assertTrue(HelperFunctions.isList1ContainsValuesOfList2(xAxisTextList, agentList));
		}
		// looping and mouse hover on bars and fetching value
		for (int i = 0; i < getElements(driver, rectBars).size(); i++) {
			for (String hoverText : hoverCheckList) {
				By hoverTextLoc = By.xpath(hoverTextElement.replace("$hoverText$", hoverText));
				int heightOfBar = Integer
						.parseInt(getAttribue(driver, getElements(driver, rectBars).get(i), ElementAttributes.height));
				if (heightOfBar >= 5) {
					hoverElement(driver, getElements(driver, rectBars).get(i));
					waitUntilVisible(driver, hoverTextLoc);
					String hoverTextValue = getElementsText(driver, hoverTextLoc);

					// comparing the value fetched above and verifying with expected
					switch (hoverText) {
					case "displayname":
					case "reviewer name":
						assertEquals(hoverTextValue, getTextListFromElements(driver, xAxisTextAnchors).get(i));
						break;
					case "Agent Talk Percentage":
					case "Agent Overtalk Percentage":
					case "Silence Percentage":
						hoverTextValue = hoverTextValue.replace(",", "");
						assertTrue(Float.parseFloat(hoverTextValue) > 0 && Float.parseFloat(hoverTextValue) <= 100);
						break;
					case "Transitions per Minute":
					case "Seconds":
					case "Hours":
					case "Plays":
						hoverTextValue = hoverTextValue.replace(",", "");
						assertTrue(Float.parseFloat(hoverTextValue) > 0);
						break;
					case "metric":
						String actualValue = getElementsText(driver, coachingEventsYAxisTeamLabel);
						assertEquals(actualValue, hoverTextValue);
					}
				}
			}
		}
	}

	/**
	 * method to check bar data by hovering and verifying with check list and
	 * legends colors passed for multiple and layered bars on top of each other bars
	 * 
	 * @param driver
	 * @param chartType
	 * @param agentList
	 * @param hoverCheckList
	 */
	public void verifyMultipleBarsData(WebDriver driver, Enum<?> chartType, List<String> agentList, List<String> hoverCheckList) {
		ReportsType reportsType = (ReportsType) chartType;

		By xAxisTextAnchors = null;
		By rectBars1 = null;
		By rectBars2 = null;
		By rectBars3 = null;
		By rectBars4 = null;

		By legendColor1 = null;
		By legendColor2 = null;
		By legendColor3 = null;
		By legendColor4 = null;

		List<By> legendColorElementsList = new ArrayList<By>();
		List<By> rectBarsElementsList = new ArrayList<By>();

		switch (reportsType) {
		case AgentMono:
			legendColor1 = agentMonoLongestStreakLegendColor;
			legendColor2 = agentMonoAverageStreakLegendColor;

			rectBars1 = agentMonoLongestStreakBar;
			rectBars2 = agentMonoAverageStreakBar;
			xAxisTextAnchors = agentMonoXAxisTextAnchors;
			break;
		case CustomerMono:
			legendColor1 = customerMonoLongestStreakLegendColor;
			legendColor2 = customerMonoAverageStreakLegendColor;

			rectBars1 = customerMonoLongestStreakBar;
			rectBars2 = customerMonoAverageStreakBar;
			xAxisTextAnchors = customerMonoXAxisTextAnchors;
			break;
		case RecordedCallPercentage:
			legendColor1 = recordedPerctngeLegendColor;
			legendColor2 = notRecordedPerctngeLegendColor;

			rectBars1 = recordedPerctngeBar;
			rectBars2 = notRecordedPerctngeBar;
			xAxisTextAnchors = recordedPerctngeXAxisTextAnchors;
			break;
		case Dispositions:
			legendColor1 = dispositionsOtherLegendColor;
			legendColor2 = dispositionsConnectedLegendColor;
			legendColor3 = dispositionsContactedLegendColor;

			rectBars1 = dispositionsOtherBar;
			rectBars2 = dispositionsConnectedBar;
			rectBars3 = dispositionsContactedBar;

			xAxisTextAnchors = dispositionsXAxisTextAnchors;
			break;
		case CallDirections:
			legendColor1 = directionsOutboundLegendColor;
			legendColor2 = directionsInboundLegendColor;

			rectBars1 = directionsOutboundBar;
			rectBars2 = directionsInboundBar;
			xAxisTextAnchors = directionsXAxisTextAnchors;
			break;
		case LocalPresence:
			legendColor1 = localPresenceLegendColor;
			legendColor2 = noLocalPresenceLegendColor;

			rectBars1 = localPresenceBar;
			rectBars2 = noLocalPresenceBar;
			xAxisTextAnchors = localPresenceXAxisTextAnchors;
			break;
		case CoachingReceived:
		case CoachingGiven:
		case PeerCoaching:
		case SelfReview:
			legendColor1 = coachingEventsAnnotationsLegendColor;
			legendColor2 = coachingEventsPlaysLegendColor;
			legendColor3 = coachingEventsTagsLegendColor;
			legendColor4 = coachingEventsListensRTLegendColor;

			rectBars1 = coachingEventsAnnotationsBar;
			rectBars2 = coachingEventsPlaysBar;
			rectBars3 = coachingEventsTagsBar;
			rectBars4 = coachingEventsListensRTBar;

			xAxisTextAnchors = coachingEventsXAxisTextAnchors;
			break;
		default:
			break;

		}

		legendColorElementsList.clear();
		if (legendColor1 != null)
			legendColorElementsList.add(legendColor1);
		if (legendColor2 != null)
			legendColorElementsList.add(legendColor2);
		if (legendColor3 != null)
			legendColorElementsList.add(legendColor3);
		if (legendColor4 != null)
			legendColorElementsList.add(legendColor4);

		rectBarsElementsList.clear();
		if (rectBars1 != null)
			rectBarsElementsList.add(rectBars1);
		if (rectBars2 != null)
			rectBarsElementsList.add(rectBars2);
		if (rectBars3 != null)
			rectBarsElementsList.add(rectBars3);
		if (rectBars4 != null)
			rectBarsElementsList.add(rectBars4);

		assertTrue(isListElementsVisible(driver, xAxisTextAnchors, 5));

		if (agentList != null && !agentList.isEmpty()) {
			List<String> xAxisTextList = getTextListFromElements(driver, xAxisTextAnchors);
			if (agentList.size() == 1)
				assertEquals(xAxisTextList, agentList);
			else
				assertTrue(HelperFunctions.isList1ContainsValuesOfList2(xAxisTextList, agentList));
		}

		// looping and mouse hover on bars and fetching value
		int index = -1;
		for (By rectBar : rectBarsElementsList) {
			index++;
			By legendColor = legendColorElementsList.get(index);
			if (isListElementsVisible(driver, rectBar, 5)) {
				for (int i = 0; i < getInactiveElements(driver, rectBar).size()-1; i++) {
					for (String hoverText : hoverCheckList) {
						By hoverTextLoc = By.xpath(hoverTextElement.replace("$hoverText$", hoverText));
						int heightOfBar = Integer.parseInt(getAttribue(driver, getInactiveElements(driver, rectBar).get(i), ElementAttributes.height));
						if (heightOfBar >= 5) {
							hoverElement(driver, getInactiveElements(driver, rectBar).get(i));
							waitUntilVisible(driver, hoverTextLoc);
							String hoverTextValue = getElementsText(driver, hoverTextLoc);

							// comparing the value fetched above and verifying with expected
							switch (hoverText) {
							case "displayname":
							case "reviewer name":
							case "name":
								assertEquals(hoverTextValue, getTextListFromElements(driver, xAxisTextAnchors).get(i));
								break;
							case "metric":
							case "is_recorded":
							case "lp_usage":
							case "disposition":
								String actualValue = getElementsText(driver,
										findElement(driver, legendColor).findElement(By.xpath(
												"following-sibling::div/div[contains(@style, 'text-overflow')]")));
								assertEquals(hoverTextValue, actualValue);
								break;
							case "value":
							case "other_count":
							case "Sum":
							case "Plays":
								hoverTextValue = hoverTextValue.replace(",", "");
								assertTrue(Float.parseFloat(hoverTextValue) > 0);
								break;
							case "% call_count":
							case "% disposition_count":
								hoverTextValue = hoverTextValue.replace("%", "");
								assertTrue(Float.parseFloat(hoverTextValue) > 0
										&& Float.parseFloat(hoverTextValue) <= 100);
								break;
							}
						}
					}
				}
			}
		}
	}
}
