package com.elliemae.testcases.MaventPostLaunchRules;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MIBuyPage;
import com.elliemae.pageobject.MaventImportLoansPage;
import com.elliemae.pageobject.MaventPortalLoginTestPage;
import com.elliemae.pageobject.MaventSOPPage;

import Exception.FilloException;
import Fillo.Recordset;

public class PostLaunchImportLoansTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(PostLaunchImportLoansTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for Post launch Import Loan file test case.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyImportLoanFile(HashMap<String, String> testData) 
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);
		
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventImportLoansPage maventImportLoansPage = new MaventImportLoansPage(driver);
		MaventPortalLoginTestPage maventPortalLoginTestPage = new MaventPortalLoginTestPage(driver,30);
		MIBuyPage maventMIBuyPage = new MIBuyPage(driver);
		
		EllieMaeLog.log(_log, "PostLaunchImportLoansTest Starts",EllieMaeLogLevel.reporter);
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		sampleSOPPage.loginToPortal(testData);
		CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_Login_Success", CommonUtility.currentTimeStamp);
		
		try
		{
			// copy Loan file to local input folder
			File f = new File("");
			String inputDirectoryPath = f.getAbsolutePath();
			inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator
					+ "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator
					+ FrameworkConsts.tlResourceFolder.get() + File.separator + "input";
			try 
			{
				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					CommonUtility.copyFilesOrFolder(testData.get("Import_Loan_File_Path")+"/"+testData.get("Import_Loan_File_Name"), inputDirectoryPath+File.separator+testData.get("Import_Loan_File_Name"), FileType.FILE);
				}
				else
				{
					CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testData.get("Import_Loan_File_Path")+"/"+testData.get("Import_Loan_File_Name"),inputDirectoryPath,testData.get("Import_Loan_File_Name"));
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			String importFile1 = inputDirectoryPath + File.separator + testData.get("Import_Loan_File_Name");
			
			// Navigate to Import loans page and import loan file
			maventImportLoansPage.navigateToImportLoansPage();
			CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_Import_Loans_page", CommonUtility.currentTimeStamp);
			maventImportLoansPage.selectDataFile(importFile1);
			maventImportLoansPage.selectFileType();
			maventImportLoansPage.cleckOverwriteCheckBox();
			String loanPoolName =generateLoanPoolName();
			maventImportLoansPage.selectLoanPoolName(loanPoolName);
			maventImportLoansPage.clickOnContinueButton();
			
			CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_LoanPoolContent", CommonUtility.currentTimeStamp);
			
			// Click on loan ID
			String loanID = testData.get("Import_Loan_File_Name").replace(".xml", "");
			driver.findElement(By.partialLinkText(loanID)).click();
			CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_LoanDataPage", CommonUtility.currentTimeStamp);
			
			// Navigage to MI-BuyDown Page and Set MI and BuyDown flag to NO
			maventMIBuyPage.navigateToMIBuyPage();
			maventMIBuyPage.setMiFlag(false);
			maventMIBuyPage.setBuyDownFlag(false);
			
			// Do Loan review
			sampleSOPPage.reviewLoan();
			CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_LoanReview", CommonUtility.currentTimeStamp);
			
			// view review XML and get the content
			String xmlContent = maventPortalLoginTestPage.getReviewXML(testData);
			boolean validationResult = validateXMLContent(xmlContent,testData.get("Import_Loan_File_ValidationContent"));
			sAssert.assertEquals(validationResult, true, "Review XML validation failed");
			
			// View pdf and get PDF URL
			String pdfURL = maventPortalLoginTestPage.getPDFURL(testData);
			sAssert.assertNotNull(pdfURL, "PDF URL cannot be NULL. PDF URL validation failed");
			

		}
		
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception Occurred during PostLaunchImportLoansTest",EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail("Exception Occurred during PostLaunchImportLoansTest");
			CommonUtilityApplication.takeScreenShot(testData, "PostLaunchImportLoansTest_Error", CommonUtility.currentTimeStamp);
		}
		finally
		{
			/*  Log out */
			sampleSOPPage.logOut();
			sAssert.assertAll();
		}
				
		EllieMaeLog.log(_log, "PostLaunchImportLoansTest Ends",EllieMaeLogLevel.reporter);
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
					String Import_Loan_File_Path = recordSet.getField("Import_Loan_File_Path");
					if(Import_Loan_File_Path!=null && !Import_Loan_File_Path.trim().isEmpty())
					{
						testData.put("Import_Loan_File_Path", Import_Loan_File_Path);
					}
					String Import_Loan_File_Name = recordSet.getField("Import_Loan_File_Name");
					if(Import_Loan_File_Name!=null && !Import_Loan_File_Name.trim().isEmpty())
					{
						testData.put("Import_Loan_File_Name", Import_Loan_File_Name);
					}
					String Import_Loan_File_ValidationContent = recordSet.getField("Import_Loan_File_ValidationContent");
					if(Import_Loan_File_ValidationContent!=null && !Import_Loan_File_ValidationContent.trim().isEmpty())
					{
						testData.put("Import_Loan_File_ValidationContent", Import_Loan_File_ValidationContent);
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
	
	
	public boolean validateXMLContent(String xmlContent, String validationContent)
	{
		String source = xmlContent.replaceAll("\\s+","");
		String target = validationContent.replaceAll("\\s+","");
		if(source.contains(target))
		{
			return true;
		}
		return false;
	}
	
	private String generateLoanPoolName()
	{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return "R"+year;
	}

}
