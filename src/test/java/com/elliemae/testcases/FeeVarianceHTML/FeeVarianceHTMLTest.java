package com.elliemae.testcases.FeeVarianceHTML;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventHTMLReportConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
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

public class FeeVarianceHTMLTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(FeeVarianceHTMLTest.class);
	EllieMaeApplicationActions objEllieMaeActions;	
	
	@Test(dataProvider = "get-test-data-method")
	public void verifyWorksheet(HashMap<String, String> testData) 
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);	
		
		EllieMaeLog.log(_log, "Fee Variance HTML Test Ends",EllieMaeLogLevel.reporter);
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
		CompareHTMLWorksheetData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query);	
	}
	
	/* read folder data from excel and populate testData hashmap*/
	private void CompareHTMLWorksheetData(HashMap<String,String> testData,String excelSheetFilePath, String query )
	{
		
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
			
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(excelSheetFilePath,query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				
				/* boolean isProcessed=false; */
				String LOAN_IDTemp="";
				String LOAN_ID="";
				
				int CannotIncRow=2;
				int CannotDecRow=2;
				int CanIncBy10Row=2;
				
				while(recordSet.next())
				{
					LOAN_ID = recordSet.getField("LOAN_ID");
					if (LOAN_ID.equalsIgnoreCase(LOAN_IDTemp)==false && !LOAN_ID.isEmpty())
					{
						testData.put("LOAN_ID", LOAN_ID);
						
						if (!LOAN_IDTemp.isEmpty()) {
							driver.switchTo().parentFrame();
							}
						
						// Search Loan
						boolean searchLoan = maventPortalLoginTestPage.searchLoan(testData);
						sAssert.assertEquals(searchLoan, true, "Search loan failed");
						LOAN_IDTemp=LOAN_ID;
						CannotIncRow=2;
						CannotDecRow=2;
						CanIncBy10Row=2;
						
 					// Loan Review
						String titleLoanReview = maventPortalLoginTestPage.reviewLoan(testData);
						sAssert.assertEquals(titleLoanReview, "Loan Review Summary Report", "Loan Review Failed");
						
                    //Wait for the page to load
						
						Thread.sleep(15000);				
					
						
						driver.switchTo().frame(MaventPortalMenuConsts.FRAME);
						
						
					}
										
					List<WebElement> rowsCannotDec = driver.findElements(By.xpath(MaventHTMLReportConsts.CannotDecTableRow));
					int RowcntCannotDec = rowsCannotDec.size();
					if (RowcntCannotDec==3 && CannotDecRow ==2) {
						
						CannotDecRow =3;
					}
					
					List<WebElement> rowsCanIncBy10 = driver.findElements(By.xpath(MaventHTMLReportConsts.CannotIncBy10TableRow));
					
					int RowcntCanIncBy10 = rowsCanIncBy10.size();
					
					if (RowcntCanIncBy10==6 && CanIncBy10Row ==2) {
						CanIncBy10Row =3;
					}
					
					String Category = recordSet.getField("Category");
					String Fee_Name = recordSet.getField("Fee_Name");
					String Baseline_Disclosure = recordSet.getField("Baseline_Disclosure");
					String Baseline_Amount = recordSet.getField("Baseline_Amount");
					String Change_of_Circumstance = recordSet.getField("Change_of_Circumstance");
					String Last_Closing_Disclosure = recordSet.getField("Last_Closing_Disclosure");
					String Actual_Fees = recordSet.getField("Actual_Fees");
					String Difference_CD_Baseline = recordSet.getField("Difference_CD_Baseline");
					String Difference_Actual_Baseline = recordSet.getField("Difference_Actual_Baseline");
						
					if (Category.equals("Cannot Increase")){
						
						String Txt = driver.findElement(By.xpath(MaventHTMLReportConsts.CannotIncLabel)).getText();
						sAssert.assertEquals(Txt, "Charges That Cannot Increase", "Charges That Cannot Increase Lable does not exist");
										
						//List<WebElement> rows = driver.findElements(By.xpath(MaventHTMLReportConsts.CannotIncTableRow));
												
						//int cnt = rows.size();
						int columns = 8;
						
						int col = 1;
						
							//Update the x-path for the cell
							
							while (col < columns) {
							String CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]" ;				
							 sAssert.assertEquals(Fee_Name, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Baseline_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Baseline_Amount, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Change_of_Circumstance, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Last_Closing_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Actual_Fees, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Difference_CD_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotIncCustomColumn + "/tr" + "[" + CannotIncRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Difference_Actual_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());													
							}
							
							CannotIncRow = CannotIncRow + 1;
					}
					
					if (Category.equals("Cannot Decrease")){
														
						
						int columns = 8;
						int col = 1;
						
							//Update the xpath for the cell
							while (col < columns) {
							String CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]" ;				
							sAssert.assertEquals(Fee_Name, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Baseline_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Baseline_Amount, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Change_of_Circumstance, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Last_Closing_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Actual_Fees, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Difference_CD_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());
							col = col + 1;
							CustomXpath = MaventHTMLReportConsts.CannotDecCustomColumn + "/tr" + "[" + CannotDecRow + "]" + "/td" + "[" + col + "]";
							sAssert.assertEquals(Difference_Actual_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());
							}
							if (RowcntCannotDec!=CannotDecRow+2) {
							CannotDecRow = CannotDecRow + 1;
							}
							else
							{
								CannotDecRow = CannotDecRow + 2;
							}
						
				}
					
				if (Category.equals("Can Increase By 10%")){																
										
										int columns = 8;
										int col = 1;
								
						//Update the xpath for the cell
											
						while (col < columns) {
						String CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]" ;				
						sAssert.assertEquals(Fee_Name, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Baseline_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Baseline_Amount, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Change_of_Circumstance, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Last_Closing_Disclosure, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Actual_Fees, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Difference_CD_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());
						col = col + 1;
						CustomXpath = MaventHTMLReportConsts.CannotIncBy10CustomColumn + "/tr" + "[" + CanIncBy10Row + "]" + "/td" + "[" + col + "]";
						sAssert.assertEquals(Difference_Actual_Baseline, driver.findElement(By.xpath(CustomXpath)).getText());		
						
					
						}
											
						if (RowcntCanIncBy10!=CanIncBy10Row+5) {
							CanIncBy10Row = CanIncBy10Row + 1;
						}
						else
						{
								CanIncBy10Row = CanIncBy10Row + 2;
						}
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
