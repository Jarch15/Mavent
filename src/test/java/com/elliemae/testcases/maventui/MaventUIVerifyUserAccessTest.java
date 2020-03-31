package com.elliemae.testcases.maventui;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventSOPPage;
import com.elliemae.pageobject.MaventVerifyUserAccessPage;

public class MaventUIVerifyUserAccessTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventUIVerifyUserAccessTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent UI verification
	 * of test user access
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyTestUserAccess(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventVerifyUserAccessPage maventVerifyUserAccessPage = new MaventVerifyUserAccessPage(driver);
		EllieMaeApplicationActions objEllieMaeActions = new EllieMaeApplicationActions(driver);
		
		EllieMaeLog.log(_log, "Mavent UI verify Test user access test Starts",EllieMaeLogLevel.reporter);
		
		/* Verify Access for test user */
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		maventVerifyUserAccessPage.loginToPortal(testData.get("Username"),testData.get("Password"),testData.get("Company"));
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_Login_Success", CommonUtility.currentTimeStamp);
		
		try
		{
			//Activity Report Query
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_ACTIVITY)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Activity Report Query","Activity Report Query Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_ActivityReportQuery", CommonUtility.currentTimeStamp);
			
			//Summary Report Query
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_SUMMARY)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Summary Report Query","Summary Report Query Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_SummaryReportQuery", CommonUtility.currentTimeStamp);
			
			// Direct Input - Import Loans
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT_IMPORT)).click(); 
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Import Loans","Import Loans Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_ImportLoans", CommonUtility.currentTimeStamp);
			
			// Direct Input - Create - Loan
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE_LOAN)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Create Loan","Create Loan Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_CreateLoan", CommonUtility.currentTimeStamp);
			
			// Direct Input - Create - Pool
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE_POOL)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Create Loan Pool","Create Loan Pool Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_CreateLoanPool", CommonUtility.currentTimeStamp);
			
			// Direct Input - Search - Loan
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH_LOAN)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Loan Search","Create Loan Pool Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_LoanSearch", CommonUtility.currentTimeStamp);
			
			// Direct Input - Search - Pool
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT_SEARCH_POOL)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Loan Pool Search","Loan Pool Search Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_LoanPoolSearch", CommonUtility.currentTimeStamp);
			
			// Direct Input - Search - Import Batch
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT_SEARCH_IMPORT_BATCH)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Loan Batch Search","Loan Batch Search Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_LoanBatchSearch", CommonUtility.currentTimeStamp);
			
			// Direct Input - Search - Batch Reviewed
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT_SEARCH_BATCH_REVIEWED)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Batch Review Result Search","Batch Review Result Search Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_BatchReviewResultSearch", CommonUtility.currentTimeStamp);
			
			// Administration - Templates
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_TEMPLATES)).click();
			//CommonUtilityApplication.threadWait(10000);
			objEllieMaeActions.waitForPageToLoad("120000");
			sAssert.assertEquals(driver.getTitle(), "Template List","Template List Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_TemplateList", CommonUtility.currentTimeStamp);
			
			// Administration - Reset Password
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_RESET_PASSWORD)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Change Your Password","Change Your Password Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_ChangeYourPassword", CommonUtility.currentTimeStamp);
			
			// Administration - Report Navigation Mode
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_REPORT_NAVIGATION_MODE)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Report Navigation Mode","Report Navigation Mode Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_ReportNavigationMode", CommonUtility.currentTimeStamp);
			
			// Administration - Loan Processing Errors
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_LOAN_PROCESSING_ERRORS)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Processing Error Search","Processing Error Search Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyTestUserAccess_ProcessingErrorSearch", CommonUtility.currentTimeStamp);
		}
		
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception Occurred during Mavent UI verify Test User access Test",EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail("Exception Occurred during Mavent UI verify Test User access Test");
		}
		finally
		{
			/*  Log out */
			sampleSOPPage.logOut();
			sAssert.assertAll();
		}
				
		EllieMaeLog.log(_log, "Mavent UI Verify Test user access test Ends",EllieMaeLogLevel.reporter);
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent UI verification
	 * of qtp management user access
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyQTPManagementUserAccess(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventVerifyUserAccessPage maventVerifyUserAccessPage = new MaventVerifyUserAccessPage(driver);
		EllieMaeApplicationActions objEllieMaeActions = new EllieMaeApplicationActions(driver);
		
		EllieMaeLog.log(_log, "Mavent UI verify QTPManagementUser access test Starts",EllieMaeLogLevel.reporter);
		
		/* Verify Access for test user */
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		maventVerifyUserAccessPage.loginToPortal(testData.get("Username"),testData.get("Password"),testData.get("Company"));
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_Login_Success", CommonUtility.currentTimeStamp);
		
		try
		{
			//Activity Report Query
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_ACTIVITY)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Activity Report Query","Activity Report Query Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_ActivityReportQuery", CommonUtility.currentTimeStamp);
			
			//Summary Report Query
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_SUMMARY)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Summary Report Query","Summary Report Query Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_SummaryReportQuery", CommonUtility.currentTimeStamp);
			
			// Administration - Reset Password
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_RESET_PASSWORD)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Change Your Password","Change Your Password Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_ChangeYourPassword", CommonUtility.currentTimeStamp);
			
			// Administration - Report Navigation Mode
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_REPORT_NAVIGATION_MODE)).click();
			//CommonUtilityApplication.threadWait(3000);
			objEllieMaeActions.waitForPageToLoad("5000");
			sAssert.assertEquals(driver.getTitle(), "Report Navigation Mode","Report Navigation Mode Title doesn't match");
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_ReportNavigationMode", CommonUtility.currentTimeStamp);
			
			// Help
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_HELP)).click();
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyUserAccessTest_verifyQTPManagementUserAccess_Help", CommonUtility.currentTimeStamp);
		}
		
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception Occurred during Mavent UI verify QTPManagementUser access Test",EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail("Exception Occurred during Mavent UI verify QTPManagementUser access Test");
		}
		finally
		{
			/*  Log out */
			sampleSOPPage.logOut();
			sAssert.assertAll();
		}
				
		EllieMaeLog.log(_log, "Mavent UI Verify QTPManagementUser access test Ends",EllieMaeLogLevel.reporter);
	}

}
