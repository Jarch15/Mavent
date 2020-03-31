package com.elliemae.testcases.MaventPostLaunch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.pageobject.MaventPortalLoginTestPage;

import Exception.FilloException;
import Fillo.Recordset;

public class MaventPortalLoginTest2 extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventPortalLoginTest2.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent SOP test case,
	 * This does loan creation, review a loan, search a loan, view PDF and activity report
	 * in a Mavent Portal.
	 *  
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyLogin(HashMap<String, String> testData) 
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);
		
		// read environment data
		HashMap<String, String> envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		EllieMaeLog.log(_log, "Mavent Portal Login test Starts",EllieMaeLogLevel.reporter);
		
		// Logic to run the URL of specific available Machines
		EllieMaeLog.log(_log, "Available Machines : "+testData.get("AVAILABLE_MACHINES"),EllieMaeLogLevel.reporter);
		// If Available machines is not blank, map the URLs to machines
		Map<String,String> URLToMachineMap = new HashMap<String,String>();		
		if(testData.get("AVAILABLE_MACHINES")!=null && !testData.get("AVAILABLE_MACHINES").isEmpty())
		{
        	String[] availableMachineArr = testData.get("AVAILABLE_MACHINES").split(","); 
        	int j=0;
			for(int i=1; i<=8; i++)
			{
				if(envData.get("MAVENTURL_"+i)!=null && !envData.get("MAVENTURL_"+i).isEmpty())
				{
					if(availableMachineArr.length>j)
					{
						URLToMachineMap.put(envData.get("MAVENTURL_"+i), availableMachineArr[j]);
						j++;
					}
					else
					{
						j=0;
						URLToMachineMap.put(envData.get("MAVENTURL_"+i), availableMachineArr[j]);
						j++;
					}
				}
			}
			EllieMaeLog.log(_log, "URL To Available Machines Mapping : "+URLToMachineMap,EllieMaeLogLevel.reporter);
		}
		
		
		// Normal Flow (Without specific machine)
		if(URLToMachineMap.isEmpty())
		{
			
			for(int i=1; i<=8; i++)
			{
				if(envData.get("MAVENTURL_"+i)!=null && !envData.get("MAVENTURL_"+i).isEmpty())
				{
					try
					{
						performTestSteps(testData,envData.get("MAVENTURL_"+i));
					}
					catch(Exception e)
					{
						// In case of exception continue with for loop for other URLs
						continue;
					}
				}
			}
		}
		// Run on specific Machine
		else
		{
			String hostName = getHostName();
			
			for(int i=1; i<=8; i++)
			{
				if(envData.get("MAVENTURL_"+i)!=null && !envData.get("MAVENTURL_"+i).isEmpty()
						&& hostName.equalsIgnoreCase(URLToMachineMap.get(envData.get("MAVENTURL_"+i))))
				{					
					try
					{
						performTestSteps(testData,envData.get("MAVENTURL_"+i));
					}
					catch(Exception e)
					{
						// In case of exception continue with for loop for other URLs
						continue;
					}
				}
			}

		}
		
		
		EllieMaeLog.log(_log, "Mavent Portal Login test Ends",EllieMaeLogLevel.reporter);
		sAssert.assertAll();
	}
	
	// Function to Perform Actual Test Steps for the Test
	private void performTestSteps(HashMap<String, String> testData, String maventURL)
	{

		boolean loginSuccess = false;
		long implicitWait = 30;
		if(testData.get("IMPLICIT_WAIT")!=null && !testData.get("IMPLICIT_WAIT").isEmpty())
		{
			implicitWait = Long.parseLong(testData.get("IMPLICIT_WAIT"));
		}
		
		HashMap<String, String> userData = new HashMap<>();
		// Get user data of the specific environment
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		MaventPortalLoginTestPage maventPortalLoginTestPage = new MaventPortalLoginTestPage(driver,implicitWait);
		try
		{
				// Navigate
				String title = maventPortalLoginTestPage.navigateToPortal(maventURL);			
				sAssert.assertEquals(title, "Login", "Navigation to the Mavent Portal Failed");
				EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);			
				//CommonUtilityApplication.threadWait(2000);
	
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
				
				// View pdf and get its content
//						String pdfContent = maventPortalLoginTestPage.viewPDFAndReturnPDFContent(testData);
//						if(pdfContent!=null)
//						{
//							// Validate if LOAN ID presents in PDF 
//							sAssert.assertEquals(pdfContent.contains(testData.get("LOAN_ID")), true, "Review PDF does not contain LOAN ID");
//						}
				
				// View pdf and get PDF URL
				String pdfURL = maventPortalLoginTestPage.getPDFURL(testData);
				sAssert.assertNotNull(pdfURL, "PDF URL cannot be NULL. PDF URL validation failed for Mavent URL : "+maventURL);
				
				// View Review xml and validate code version and rule version
				String xmlContent = maventPortalLoginTestPage.getReviewXML(testData);
				boolean validationResult = validateVersion(xmlContent,testData);
				sAssert.assertEquals(validationResult, true, "XML ruleVersion and codeVersion validation failed");
				
				// Add Comment
				String comment = CommonUtilityApplication.getIpAddress(driver.getCurrentUrl());
				maventPortalLoginTestPage.addComment(testData,comment);
				
				// Logout
				maventPortalLoginTestPage.logOut();
		}
		catch(Exception e)
		{
			sAssert.assertFalse(true, "Exception occurred for URL : "+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()));
			e.printStackTrace();
			CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_Error_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
			if(loginSuccess)
			{
				try
				{
					maventPortalLoginTestPage.logOut();
				}
				catch (Exception exception)
				{
					EllieMaeLog.log(_log, "Exception during logging out...", EllieMaeLogLevel.reporter);
				}
			}
			throw e;
		}
	
	}
	
	public boolean validateVersion(String xmlContent, HashMap<String, String> testData)
	{
		String ruleVersion = null;
		String codeVersion = null;
		if(xmlContent!=null && xmlContent.contains("ruleVersion=\""))
		{
			String stringSplit[] = xmlContent.split("ruleVersion=\"");
			String stringSplit2[] = stringSplit[1].split("\"");
			ruleVersion = stringSplit2[0];
		}
		
		if(xmlContent!=null && xmlContent.contains("version=\""))
		{
			String stringSplit3[] = xmlContent.split("version=\"");
			String stringSplit4[] = stringSplit3[1].split("\"");
			codeVersion = stringSplit4[0];	
		}
		
		EllieMaeLog.log(_log, "Rule Version : "+ruleVersion,EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Code Version : "+codeVersion,EllieMaeLogLevel.reporter);
		
		if(ruleVersion!=null && ruleVersion.equalsIgnoreCase(testData.get("RULE_VERSION")) 
				&& codeVersion!=null && codeVersion.equalsIgnoreCase(testData.get("CODE_VERSION")))
		{
			return true;
		}
		else
		{
			return false;
		}
		
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
					String RULE_VERSION = recordSet.getField("RULE_VERSION");
					if(RULE_VERSION!=null && !RULE_VERSION.trim().isEmpty())
					{
						testData.put("RULE_VERSION", RULE_VERSION);
					}
					String CODE_VERSION = recordSet.getField("CODE_VERSION");
					if(CODE_VERSION!=null && !CODE_VERSION.trim().isEmpty())
					{
						testData.put("CODE_VERSION", CODE_VERSION);
					}
					String LOAN_ID = recordSet.getField("LOAN_ID");
					if(LOAN_ID!=null && !LOAN_ID.trim().isEmpty())
					{
						testData.put("LOAN_ID", LOAN_ID);
					}
					String IMPLICIT_WAIT = recordSet.getField("IMPLICIT_WAIT");
					if(IMPLICIT_WAIT!=null && !IMPLICIT_WAIT.trim().isEmpty())
					{
						testData.put("IMPLICIT_WAIT", IMPLICIT_WAIT);
					}
					String AVAILABLE_MACHINES = recordSet.getField("AVAILABLE_MACHINES");
					if(AVAILABLE_MACHINES!=null && !AVAILABLE_MACHINES.trim().isEmpty())
					{
						testData.put("AVAILABLE_MACHINES", AVAILABLE_MACHINES);
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
	
	private static String getHostName()
	{
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
	    else
	        return "Unknown Computer";
	}

}
