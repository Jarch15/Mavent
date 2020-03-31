package com.elliemae.testcases.maventui;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventImportLoansPageConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventImportLoansPage;
import com.elliemae.pageobject.MaventSOPPage;

public class MaventUIImportLoansTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventUIImportLoansTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent UI Import Loans test case.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyMaventImportLoans(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventImportLoansPage maventImportLoansPage = new MaventImportLoansPage(driver);
		
		EllieMaeLog.log(_log, "Mavent UI Import Loans test Starts",EllieMaeLogLevel.reporter);
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		maventImportLoansPage.loginToPortal(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_Login_Success", CommonUtility.currentTimeStamp);
		
		try
		{
			// Prepare file paths for import loan files
			File f = new File("");
			String inputDirectoryPath = f.getAbsolutePath();
			inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator
					+ "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator
					+ FrameworkConsts.tlResourceFolder.get() + File.separator + "input";
			String importFile1 = inputDirectoryPath + File.separator + "Import-SchemaFail1.xml";
			String importFile2 = inputDirectoryPath + File.separator + "Import-SchemaFail2.xml";
			String importFile3 = inputDirectoryPath + File.separator + "Import-SchemaFix1.xml";
			String importFile4 = inputDirectoryPath + File.separator + "Import-SchemaFix2.xml";
			String importFile5 = inputDirectoryPath + File.separator + "Import-SchemaFix3.xml";
			String importFile6 = inputDirectoryPath + File.separator + "Import-SchemaFix4.xml";
			String importFile7 = inputDirectoryPath + File.separator + "Import-SchemaFix5.xml";
			String importFile8 = inputDirectoryPath + File.separator + "Import-SchemaFix6.xml";
			String importFile9 = inputDirectoryPath + File.separator + "Import-SchemaFix7.xml";
			String importFile10 = inputDirectoryPath + File.separator + "Import-SchemaFix8.xml";
			String importFile11 = inputDirectoryPath + File.separator + "Import-NewDocEng-EleTrue.xml";
			String importFile12 = inputDirectoryPath + File.separator + "Http-ReviewFileSize.xml";
			
			// Navigate to Import loans page and import loan file
			maventImportLoansPage.navigateToImportLoansPage();
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_Import_Loans_page", CommonUtility.currentTimeStamp);
			maventImportLoansPage.selectDataFile(importFile1);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile2);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile3);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile4);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile5);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile6);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile7);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile8);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile9);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile10);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile11);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			maventImportLoansPage.navigateToImportLoansPage();
			maventImportLoansPage.selectDataFile(importFile12);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			maventImportLoansPage.clickOnContinueButton();			
			
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_LoanPoolContent", CommonUtility.currentTimeStamp);
			
			// Loan Review all records
			maventImportLoansPage.loanReviewAll(testData);
			
			// click on on loan file
			driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_FILE_NAME_LINK_XPATH)).click();
			CommonUtilityApplication.threadWait(10000);
			CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_LoanDataPage", CommonUtility.currentTimeStamp);
			
			// On Loan Data Page check review status
			String fileStatusValue = driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_DATA_FILE_STATUS_XPATH)).getText();
			EllieMaeLog.log(_log, "fileStatusValue captured : "+fileStatusValue,EllieMaeLogLevel.reporter);
			Assert.assertEquals(fileStatusValue, "Reviewed","fileStatusValue is "+fileStatusValue+" . It should be 'Reviewed' ");
			
			String maventReviewStatusValue = driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_DATA_REVIEW_STATUS_XPATH)).getText();
			EllieMaeLog.log(_log, "maventReviewStatusValue captured : "+maventReviewStatusValue,EllieMaeLogLevel.reporter);
			Assert.assertNotEquals(maventReviewStatusValue, "", "maventReviewStatusValue should not be blank");
		}
		
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception Occurred during Mavent UI Import Loans Test",EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail("Exception Occurred during Mavent UI Import Loans Test");
		}
		finally
		{
			/*  Log out */
			sampleSOPPage.logOut();
			sAssert.assertAll();
		}
				
		EllieMaeLog.log(_log, "Mavent UI Import Loans test Ends",EllieMaeLogLevel.reporter);
	}

}
