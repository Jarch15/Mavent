package com.elliemae.testcases.maintenance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import Fillo.Recordset;
import jcifs.smb.SmbFile;

public class OFACPostLaunchTest2 extends EllieMaeApplicationBase
{
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(OFACPostLaunchTest2.class);
	
	@Test(dataProvider = "get-test-data-method")
	public void loanReview(HashMap<String,String> testData){
		
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
	    HashMap<String, String> responseMap= new HashMap<>();
		
		try {
			
			readFolderPathsFromExcel(testData);
			
			//Get the file path using the utility command
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "OFACPostLaunchTest2_Data.xlsx");
			
			//Create the query for the data from test data sheet 
			strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"' order by SequenceID";
			
			//Execute the query and save it in a hash map testCaseData			
            HashMap<String, HashMap<String, String>> testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
            
            HashMap<String, String> testCaseMethodData = testCaseData.get("1");
            
            String inputData = testCaseMethodData.get("InputData");
            String[] fileNamePaterns = inputData.split(",");
               
            String dir = testData.get("Input_File_Path") + testData.get("Date")+"/";
            EllieMaeLog.log(_log, "Directory path: " +dir, EllieMaeLogLevel.reporter);
            
            //Code for processing files from network
            SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(dir, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            List<String> filesToProcessFromNetwork = new ArrayList<String>();
            for (SmbFile networkFile : listOfFilesFromNetwork)
			{
            	for(String fileNamePattern : fileNamePaterns)
            	{
					if(networkFile.getName().contains(fileNamePattern))
					{
						filesToProcessFromNetwork.add(networkFile.getName());
					}
            	}
			        
			}
            for(String fileToProcess : filesToProcessFromNetwork)
            {
            	responseMap = ObjSOAPMaventWebServiceCall.loanReview(testData, testCaseMethodData, fileToProcess);
            	if(responseMap.get("STATUSCODE").equals("200"))
				{
            		EllieMaeLog.log(_log, "Loan review web service successful for file : "+fileToProcess, EllieMaeLogLevel.reporter);
				}
            	else
            	{
            		Assert.assertTrue(false,"Test failed due to Server Error : "+responseMap.get("STATUSCODE")+" for file : "+fileToProcess);
            	}
            }
            
		} 
		
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in loanReview method: "+e.getMessage(), EllieMaeLogLevel.reporter);
			Assert.assertTrue(false,"test failed due to exception");
		}

	}
	
	/* Read the folders details from excel sheet and populate testData map */
	private void readFolderPathsFromExcel(HashMap<String,String> testData)
	{
		//Copy Excel file to local input folder
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
					String Input_File_Path = recordSet.getField("Input_File_Path");
					if(Input_File_Path!=null && !Input_File_Path.trim().isEmpty())
					{
						testData.put("Input_File_Path", Input_File_Path);
					}
					String Date = recordSet.getField("Date");
					if(Date!=null && !Date.trim().isEmpty())
					{
						testData.put("Date", Date);
					}
					String Output_File_Path = recordSet.getField("Output_File_Path");
					if(Output_File_Path!=null && !Output_File_Path.trim().isEmpty())
					{
						testData.put("Output_File_Path", Output_File_Path);
					}
					String Output_Directory = recordSet.getField("Output_Directory");
					if(Output_Directory!=null && !Output_Directory.trim().isEmpty())
					{
						testData.put("Output_Directory", Output_Directory);
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
