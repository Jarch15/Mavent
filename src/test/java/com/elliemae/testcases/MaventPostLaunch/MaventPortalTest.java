package com.elliemae.testcases.MaventPostLaunch;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventSOPPage;
import com.elliemae.pageobject.MaventTILHUDPage;

public class MaventPortalTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventPortalTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent SOP test case,
	 * This does loan creation, review a loan, search a loan, view PDF and activity report
	 * in a Mavent Portal.
	 *  
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyLoanSearch_CE_12855(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventTILHUDPage maventTilHudPage = new MaventTILHUDPage(driver);
		EllieMaeLog.log(_log, "Mavent Portal SOP test Starts",EllieMaeLogLevel.reporter);
		
//		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
//		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");  
//		driver.switchTo().defaultContent();  
		
		// Navingating to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		System.out.println("titleAfterNavigation : "+titleAfterNavigation);
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_Login_Page", CommonUtility.currentTimeStamp);
		
		// Loging to the Mavent Portal
		String loggedUser = sampleSOPPage.loginToPortal(testData);
		Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
		Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_Login_Success", CommonUtility.currentTimeStamp);
		
		// Create new Loan page
		sampleSOPPage.createLoan(testData);
		
		// Populate mandatory data on loan data page
		sampleSOPPage.populateMandatoryFields(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_LoanData_page", CommonUtility.currentTimeStamp);
		
		// Navigate to Til-HUD page and populate mandatory data
		maventTilHudPage.navigateToTILAHUDPage();
		maventTilHudPage.populateMandatoryFields(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_TilHUD_page", CommonUtility.currentTimeStamp);
		
		// Review Loan
		String titleLoanReviewComplete = sampleSOPPage.reviewLoan();
		Assert.assertEquals(titleLoanReviewComplete, "Loan Review Summary Report", "Loan Review Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_Review_Loan", CommonUtility.currentTimeStamp);
		
		// Search a Loan
		String loanID = sampleSOPPage.loanSearch(testData);
		Assert.assertNotNull(loanID, "Loan Search Failed");
		Assert.assertTrue(loanID.contains(sampleSOPPage.getLOAN_ID()), "Loan Search Failed");
		
		// Review Loan
		String titleLoanReview = sampleSOPPage.reviewLoan();
		Assert.assertEquals(titleLoanReview, "Loan Review Summary Report", "Loan Review Failed");
		
		// View PDF
		sampleSOPPage.viewPDF(testData,"");
	
		// View pdf and get its content
//		String pdfContent = sampleSOPPage.viewPDFAndReturnPDFContent(testData);
//		if(pdfContent!=null)
//		{
//			pdfContent.contains(sampleSOPPage.getLOAN_ID());
//			Assert.assertEquals(pdfContent.contains(sampleSOPPage.getLOAN_ID()), true, "Review PDF does not contain LOAN ID");
//		}
		
		// Activity Report
		String loanIDFromReport = sampleSOPPage.reportsActivity(testData);
		//Assert.assertEquals(titleReportActivity, "Loan Review Summary Report", "Activity Report Failed");
		Assert.assertNotNull(loanIDFromReport, "Activity Report Failed");
		Assert.assertTrue(loanIDFromReport.contains(sampleSOPPage.getLOAN_ID()), "Activity Report Failed");
		//sampleSOPPage.viewPDF(testData,"ActivityReport_PDF");
		
		// Log out
		String titleLogOut = sampleSOPPage.logOut();
		Assert.assertEquals(titleLogOut, "Login", "Log out from the Mavent Portal Failed");
		
		EllieMaeLog.log(_log, "Mavent Portal SOP test Ends",EllieMaeLogLevel.reporter);
	}

}
