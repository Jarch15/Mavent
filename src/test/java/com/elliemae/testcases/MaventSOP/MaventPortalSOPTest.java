package com.elliemae.testcases.MaventSOP;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.asserts.Assert;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.pageobject.MaventPortalLoginTestPage;

import Exception.FilloException;
import Fillo.Recordset;

public class MaventPortalSOPTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventPortalSOPTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent SOP test case,
	 * This does loan search, review a loan, search a loan, view PDF and activity report
	 * in a Mavent Portal.
	 *  
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyLoanSearch_CE_12855(HashMap<String, String> testData) 
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);
		
		HashMap<String, String> envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		long implicitWait = 30;
		if(testData.get("IMPLICIT_WAIT")!=null && !testData.get("IMPLICIT_WAIT").isEmpty())
		{
			implicitWait = Long.parseLong(testData.get("IMPLICIT_WAIT"));
		}
		MaventPortalLoginTestPage maventPortalLoginTestPage = new MaventPortalLoginTestPage(driver,implicitWait);
		
		HashMap<String, String> userData = new HashMap<>();
		
		// Get user data of the speific environment
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		EllieMaeLog.log(_log, "Mavent Portal Login test Starts",EllieMaeLogLevel.reporter);
		
				boolean loginSuccess = false;
				try
				{
						// Navigate
						String title = maventPortalLoginTestPage.navigateToPortal(envData.get("MAVENTURL"));			
						sAssert.assertEquals(title, "Login", "Navigation to the Mavent Portal Failed");
						EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);			
			
						// Login
						String loggedUser = null;
						EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);
						loggedUser = maventPortalLoginTestPage.loginToPortal(testData, userData);
						sAssert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
						sAssert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
						EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
						loginSuccess = true;
						
						// Search Loan
						boolean searchLoan = maventPortalLoginTestPage.searchLoan(testData);
						sAssert.assertEquals(searchLoan, true, "Search loan failed");
						
						// Loan Review
						String titleLoanReview = maventPortalLoginTestPage.reviewLoan(testData);
						sAssert.assertEquals(titleLoanReview, "Loan Review Summary Report", "Loan Review Failed");	
						
						// View pdf and get PDF URL
						String pdfURL = maventPortalLoginTestPage.getPDFURL(testData);
						sAssert.assertNotNull(pdfURL, "PDF URL cannot be NULL. PDF URL validation failed for Mavent URL : "+envData.get("MAVENTURL"));
						
						// Activity Report
						String loanIDFromReport = maventPortalLoginTestPage.reportsActivity(testData,testData.get("LOAN_ID"));
						Assert.assertNotNull(loanIDFromReport, "Activity Report Failed");
						Assert.assertTrue(loanIDFromReport.contains(testData.get("LOAN_ID")), "Activity Report Failed");

						// Logout
						maventPortalLoginTestPage.logOut();
				}
				catch(Exception e)
				{
					sAssert.assertFalse(true, "Exception occurred for URL : "+envData.get("MAVENTURL"));
					e.printStackTrace();
					if(loginSuccess)
					{
						maventPortalLoginTestPage.logOut();
					}
				}

		
		
		EllieMaeLog.log(_log, "Mavent Portal Login test Ends",EllieMaeLogLevel.reporter);
		sAssert.assertAll();
	}
	
	
	/* Read the folders details from excel sheet from shared path */
	private void readFolderPathsFromExcel(HashMap<String,String> testData)
	{
		//Copy Excel file to local data folder
		EllieMaeLog.log(_log, "Copying excel file to local data folder", EllieMaeLogLevel.reporter);
		File f = new File("");
		String dataDirectoryPath = f.getAbsolutePath();
		dataDirectoryPath = dataDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"data";

		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				CommonUtility.copyFilesOrFolder(testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"), dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"), FileType.FILE);
			}
			else
			{
				CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"),dataDirectoryPath,testData.get("FolderDataSheetFile"));
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception during copying excel sheet to data folder", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading Folders data from excel sheet", EllieMaeLogLevel.reporter);
		String query = "Select * from  \"Folders\"";
		getFolderData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query);

		
	}
	
	/* read folder data from excel and populate testData hashmap*/
	private void getFolderData(HashMap<String,String> testData,String excelSheetFilePath, String query )
	{
		
		try
		{
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(excelSheetFilePath,query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				while(recordSet.next())
				{
					String LOAN_ID = recordSet.getField("LOAN_ID");
					if(LOAN_ID!=null && !LOAN_ID.trim().isEmpty())
					{
						testData.put("LOAN_ID", LOAN_ID);
					}
				}
			}
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
	}

}
