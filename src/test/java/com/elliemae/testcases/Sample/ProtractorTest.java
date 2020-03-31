package com.elliemae.testcases.Sample;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.annotation.ApplyRetryListener;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.ENCWEBPage;
import com.elliemae.pageobject.TPOMVPPage;


public class ProtractorTest extends EllieMaeBase {

	public static String baseUrl = "";
	TPOMVPPage tpoMvpPage;
	ENCWEBPage encWebPage;
		
	/*  ***************************************************
	 *  	          	TPO MVP
	 *  ***************************************************
	 *  
	 *     Protractor Sample for TPO MVP
	 *     Test Case steps
	 * 
	 * 1.  Open TPO MVP URL
	 * 2.  Perform validation on buttons for enable/disable state before login
	 * 3.  Login
	 * 4.  Perform validation on user login name on the top right corner
	 * 5.  Web Page title validation
	 * 6.  Open Pipeline
	 * 7.  Open New Loan window and close
	 * 8.  Read the Column names in the Pipeline Grid header
	 * 9.  Select a filter and apply filter
	 * 10. Select a loan with specific Loan Number
	 * 11. Read the value from the Lender Account Manager field and validate
	 * 12. Logout
	 * 
	 */
	
	@ApplyRetryListener
	@Test(dataProvider = "get-test-data-method")
	public void testTPOMVP(HashMap<String, String> testData) throws InterruptedException{
		Logger _log = Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
	
		EllieMaeLog.log(_log, "TestCase: testTPOMVP Started", EllieMaeLogLevel.reporter);
		tpoMvpPage = new TPOMVPPage(driver);
		
		try{
			tpoMvpPage.navigateToPage();
		}
		catch(Exception e){
			EllieMaeLog.log(_log,"Failed to open page: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}
		
		EllieMaeLog.log(_log, "Validate Page Title after opening URL is Ellie Mae", EllieMaeLogLevel.reporter);
		
		//Extract Jira Id's from the test steps and populate teststatus object with JiraId's
		CommonUtility.populateTestStatusCollectionWithJiraId(testStatus, testData.get(FrameworkConsts.JIRAID));
		
		//set curr Jira Id to validate for Assert
		Assert.setCurrJIRA_ID(CommonUtility.retrieveJIRA_ID(testData.get(FrameworkConsts.JIRAID),1));
		Assert.assertEquals(tpoMvpPage.getTitle(), "Ellie Mae - TPO Connect","Page title failed");
		EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		//set curr Jira Id to validate for Assert
		Assert.setCurrJIRA_ID(CommonUtility.retrieveJIRA_ID(testData.get(FrameworkConsts.JIRAID),2));
		Assert.assertEquals("Test1", "Test1","Test1 failed");
		
		//set curr Jira Id to validate for SoftAssert
		sAssert.setCurrJIRA_ID(CommonUtility.retrieveJIRA_ID(testData.get(FrameworkConsts.JIRAID),3));
		sAssert.assertEquals("Test2", "Test2", "Test2 failed");
		
		sAssert.assertAll();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.setLoginUserName(testData.get("LoginName"));		//"supreet.singh@elliemae.com");
		tpoMvpPage.setLoginPassword(testData.get("Password"));
		
		EllieMaeLog.log(_log, "Validate login button is enabled after entering password", EllieMaeLogLevel.reporter);
		Assert.assertTrue(tpoMvpPage.loginButtonEnabled(),"Login Button Disabled");
		
		tpoMvpPage.clickLoginButton();
	
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Page opens up with title: "+tpoMvpPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
//		EllieMaeLog.log(_log, "Validate Welcome label after login", EllieMaeLogLevel.reporter);
//		EllieMaeLog.log(_log, "Welcome Label text: "+tpoMvpPage.getWelcomeText(),EllieMaeLogLevel.reporter);
//		
//		Assert.assertEquals(tpoMvpPage.getWelcomeText(), "Welcome","Welcome label does not exist");		
		//Assert.assertEquals(tpoMvpPage.getAbsoluteUrl(), "/home");
		
		//EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
//		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
//		EllieMaeLog.log(_log, "Validate LoginName after login", EllieMaeLogLevel.reporter);
//		EllieMaeLog.log(_log, "LoginName after login: " + tpoMvpPage.getUserName(), EllieMaeLogLevel.reporter);
//		Assert.assertEquals(tpoMvpPage.getUserName(), testData.get("NameAfterLogin"),"Failed to match Login Name");
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickPipeLineMenuButton();
		Assert.assertEquals(tpoMvpPage.getAbsoluteUrl(), "/home/pipeline");
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickNewLoanButton();
		
		//Assert.assertTrue(tpoMvpPage.isCreateNewLoanButtonDisplayed(),"Create New Loan Button not displayed");
		
		tpoMvpPage.clickCancelNewLoanButton();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.getPipelineGridColumns();
		
		/*EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickFiltersDropdown();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.selectLoanNumberFilter();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.setFilterTextBoxValue(testData.get("LoanNumber"));
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickApplyFilterBtn();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);*/
		tpoMvpPage.clickLoan("#"+testData.get("LoanNumber"));
		
		EllieMaeLog.log(_log, "Page opens up with Absolute URL: "+tpoMvpPage.getAbsoluteUrl(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "Loan Number: " + tpoMvpPage.getCurrentLoan(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Total Loan Amount: " + tpoMvpPage.getCurrentLoanAmount(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickTpoTopLoginButton();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickLogoutLink();		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Page opens up with title: "+tpoMvpPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "TestCase: testTPOMVP completed", EllieMaeLogLevel.reporter);			
	}

	
	
	/*  ******************************************************* 
	 * 				Encompass Web
	 *  ******************************************************* 
	 * 
	 *     Protractor Sample for Encompass Web
	 *     Test Case steps
	 * 
	 * 1.  Login the Encompass-Web application using url provided by Vikas
	 * 			(http://githubdev.dco.elmae/pages/Elliemae/em-encompass-web-demo/#/login)
	 * 2.  Webpage Title validation
	 * 3.  Validate "Vikas Rao" label next to the user icon
	 * 4.  Click the Leads view
	 * 5.  Run the Carousel left and right
	 * 6.  Getting the details inside carousel and displaying them on the report
	 * 7.  Click the radio buttons and checkboxes on Filter menu
	 * 8.  Search for a Loan
	 * 9.  Open the loan
	 * 10. Get borrower name from the loan
	 * 11. Close the Loan
	 * 12. Changing view from Leads View to Milestone View
	 * 13. Then logout
	 * 
	 */
	
	@Test(dataProvider = "get-test-data-method")
	public void testEncompassWeb(HashMap<String, String> testData) throws InterruptedException{
		Logger _log = Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		EllieMaeLog.log(_log, "TestCase: testEncompassWeb Started", EllieMaeLogLevel.reporter);
		encWebPage = new ENCWEBPage(driver);

		String githubUrl ="http://githubdev.dco.elmae/#/login";
		baseUrl="http://githubdev.dco.elmae/pages/Elliemae/em-encompass-web-demo/#/login";

		try{			
			encWebPage.navigateToGitHubPage(githubUrl);
		}
		catch(Exception e){
			EllieMaeLog.log(_log,"Failed to open page: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}
		
		EllieMaeLog.log(_log, "Use your gitHubDev UserName and Password for this sample test", EllieMaeLogLevel.reporter);
		String gitHubUserName="";
		String gitHubPwd="";
		
		encWebPage.setGitHubLoginUserName(gitHubUserName);
		encWebPage.setGitHubLoginPassword(gitHubPwd);
		encWebPage.clickGitHubLoginButton();
		
		FrameworkConsts.JIRANUMBERTOUPDATE=testData.get("JIRAID");
		EllieMaeLog.log(_log, "Validate Page Title after login is GitHub", EllieMaeLogLevel.reporter);
		Assert.assertEquals(encWebPage.getTitle(), "GitHub","Page title failed");
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		
		try{			
			encWebPage.navigateToPage(baseUrl);
		}
		catch(Exception e){
			EllieMaeLog.log(_log,"Failed to open page: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}

		EllieMaeLog.log(_log, "Validate Page Title after opening URL is EllieMae", EllieMaeLogLevel.reporter);
		Assert.assertEquals(encWebPage.getTitle(), "Ellie Mae","Page title failed");
		
		EllieMaeLog.log(_log, "Current Url: "+encWebPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.setLoginClientID(testData.get("ClientID"));
		encWebPage.setLoginUserName(testData.get("LoginName"));
		encWebPage.setLoginPassword(testData.get("Password"));
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Validate login button is enabled after entering password", EllieMaeLogLevel.reporter);
		Assert.assertTrue(encWebPage.loginButtonEnabled(),"Login Button Disabled");
		
		encWebPage.clickLoginButton();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "Page opens up with title: "+encWebPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Current Url: "+encWebPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		Assert.assertEquals(encWebPage.getAbsoluteUrl(), "/pipeline");
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);		
		EllieMaeLog.log(_log, "Validate LoginName after login", EllieMaeLogLevel.reporter);
		Assert.assertEquals(encWebPage.getUserName(), testData.get("NameAfterLogin"),"Failed to match Login Name");
		EllieMaeLog.log(_log, "LoginName after login: " + encWebPage.getUserName(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickPipeLineMenuButton();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Open Leads View by clicking Leads link", EllieMaeLogLevel.reporter);
		encWebPage.clickLeadsLink();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Carousel List: ",EllieMaeLogLevel.reporter);
		for(int i=1 ; i<8 ;i++){
			encWebPage.getCarouselValues(i);
			EllieMaeLog.log(_log, "Move Carousel right 1 time", EllieMaeLogLevel.reporter);
			encWebPage.clickLeadsMoveRightArrow(1);						
		}
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Move Carousel left 7 times", EllieMaeLogLevel.reporter);
		encWebPage.clickLeadsMoveLeftArrow(7);
		
		EllieMaeLog.log(_log, "Close Leads View by clicking Leads link", EllieMaeLogLevel.reporter);
		encWebPage.clickLeadsLink();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);		
		encWebPage.clickFilterViewAllLoansRdBtn();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickFilterCompanyTPORdBtn();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickFilterChannelAllChannelsChkBox();
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickFilterChannelWholeSaleChkBox();
				
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.setNameSearchTxtBox(testData.get("SearchTextBox"));
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickLoan(testData.get("SearchTextBox"),"3726 Poplar St");
		
		HashMap<String, String> loanDetails = encWebPage.getLoanDetails();
		
		EllieMaeLog.log(_log, "Loan Details: ", EllieMaeLogLevel.reporter);
		
		for(Map.Entry<String,String> entry : loanDetails.entrySet()){
			EllieMaeLog.log(_log, entry.getKey() + " : "+ entry.getValue(),EllieMaeLogLevel.reporter);
		}
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		//encWebPage.clickLoanCloseIcon("Firstimer, Alice");
		encWebPage.clickAllLoansTab();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Open MileStone View", EllieMaeLogLevel.reporter);
		encWebPage.clickMileStoneViewBtn();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Open Leads View", EllieMaeLogLevel.reporter);
		encWebPage.clickLeadsViewBtn();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		encWebPage.clickProfileName();
		encWebPage.clickProfileLogoutLink();
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Page opens up with title: "+encWebPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "TestCase: testEncompassWeb completed", EllieMaeLogLevel.reporter);
	
	}

	
	/*
	 * Protractor Sample for TPO MVP 
	 * Failed scenario for Demo purpose
	 * 
	 
	
	@Test(dataProvider = "get-test-data-method")
	public void testTPOMVP_Fail(HashMap<String, String> testData) throws InterruptedException{
		Logger _log = Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		EllieMaeLog.log(_log, "TestCase: testTPOMVP Started", EllieMaeLogLevel.reporter);
		tpoMvpPage = new TPOMVPPage(driver);

		baseUrl = "http://eq1vwbeui0001.dco.elmae/tpo/#/login";
		
		try{
			tpoMvpPage.navigateToPage(baseUrl);
		}
		catch(Exception e){
			EllieMaeLog.log(_log,"Failed to open page: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}

		FrameworkConsts.JIRANUMBERTOUPDATE = testData.get("JIRAID");
		EllieMaeLog.log(_log, "Validate Page Title after opening URL is Ellie Mae", EllieMaeLogLevel.reporter);
		Assert.assertEquals(tpoMvpPage.getTitle(), "Ellie Mae","Page title failed");
		EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.setLoginUserName(testData.get("LoginName"));
		tpoMvpPage.setLoginPassword(testData.get("Password"));
		
		EllieMaeLog.log(_log, "Validate login button is enabled after entering password", EllieMaeLogLevel.reporter);
		Assert.assertTrue(tpoMvpPage.loginButtonEnabled(),"Login Button Disabled");
		
		tpoMvpPage.clickLoginButton();
	
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Page opens up with title: "+tpoMvpPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "Validate Welcome label after login", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Welcome Label text: "+tpoMvpPage.getWelcomeText(),EllieMaeLogLevel.reporter);
		
		Assert.assertEquals(tpoMvpPage.getWelcomeText(), "Welcome","Welcome label does not exist");		
		Assert.assertEquals(tpoMvpPage.getAbsoluteUrl(), "/home");
		
		EllieMaeLog.log(_log, "Current Url: "+tpoMvpPage.getCurrentUrl(), EllieMaeLogLevel.reporter);
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Validate LoginName after login", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "LoginName after login: " + tpoMvpPage.getUserName(), EllieMaeLogLevel.reporter);
		Assert.assertEquals(tpoMvpPage.getUserName(), testData.get("NameAfterLogin"),"Failed to match Login Name");
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickPipeLineMenuButton();
		Assert.assertEquals(tpoMvpPage.getAbsoluteUrl(), "/home/pipeline");
		
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		tpoMvpPage.clickNewLoanButton();
		
		Assert.assertTrue(tpoMvpPage.isCreateNewLoanButtonDisplayed(),"Create New Loan Button not displayed");
		
				EllieMaeLog.log(_log, "Page opens up with title: "+tpoMvpPage.getTitle(), EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "TestCase: testTPOMVPFail completed", EllieMaeLogLevel.reporter);			
	}*/

}
