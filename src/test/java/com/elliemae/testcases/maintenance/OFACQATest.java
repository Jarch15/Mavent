package com.elliemae.testcases.maintenance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import Fillo.Recordset;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;



public class OFACQATest extends EllieMaeApplicationBase{
	
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(OFACQATest.class);
	
	@Test(dataProvider = "get-test-data-method")
	
	public void OFACTest(HashMap<String,String> testData){
		
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
	    HashMap<String, String> responseMap= new HashMap<>();
	
		//Read the data from previous day folder
		
		try {
			readFolderPathsFromExcel(testData);
			
			//Create a new folder
			String path = testData.get("Input_File_Path");
			path = path + testData.get("NewOFACDate");
			      		    
		      String userDomain = MaventConsts.EMUSERDOMAIN ;
		      String userName = MaventConsts.EMNETWORKUSERNAME ;
		      String password = CommonUtility.decryptData(MaventConsts.EMNETWORKUSERPASSWORD);
		    		  
		      SmbFile sFile;
			  NtlmPasswordAuthentication auth;
		      
		      /* Check if folder is present, else create a folder*/
				String networkLocationDirectory = "smb:" + path;
				auth = new NtlmPasswordAuthentication(userDomain, userName, password);
				sFile = new SmbFile(networkLocationDirectory, auth);
				try
				{
					if(!sFile.exists()){
						sFile.mkdirs();
					}
				}
				catch(Exception e)
				{
					EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
				}
		      
			//Copy the files in the new OFAC Data folder
		      
			
			
			//Get the file path using the utility command
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "OFACQATest_Data.xlsx");
			
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
            SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(dir, userDomain, userName, password);
            List<String> filesToProcessFromNetwork = new ArrayList<String>();
            for (SmbFile networkFile : listOfFilesFromNetwork)
			{
            	for(String fileNamePattern : fileNamePaterns)
            	{
					if(networkFile.getName().contains(fileNamePattern))
					{
						filesToProcessFromNetwork.add(networkFile.getName());
						
						//password = CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD);
						
						CommonUtilityApplication.copyFileFromNetworkLocationToNetworkLocation(userDomain, userName, password, dir, path, networkFile.getName());
					}
            	}
			        
			}
			
		}
		
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in OFACTest method: "+e.getMessage(), EllieMaeLogLevel.reporter);
			Assert.assertTrue(false,"test failed due to exception");
		}
	}
	
	
	
	public void renameRegressionFiles() {
		
	}



	public void createNewTestFiles() {
		
		
		
		
	}


	public void doLoanReviewAndValidation() {
		
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
				CommonUtilityApplication.copyFileFromNetworkLocation(MaventConsts.EMUSERDOMAIN, MaventConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(MaventConsts.EMNETWORKUSERPASSWORD),testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"),dataDirectoryPath,testData.get("FolderDataSheetFile"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading Folders data from excel sheet", EllieMaeLogLevel.reporter);
		String query1 = "Select * from  \"Folders\"";
		getFolderData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query1);
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading Individuals data from excel sheet", EllieMaeLogLevel.reporter);
		String query2 = "Select * from  \"Individuals\"";
		getIndividualData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query2);
		
		

		
	}

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
					
					String NewOFACDate = recordSet.getField("NewOFACDate");
					if(NewOFACDate!=null && !NewOFACDate.trim().isEmpty())
					{
						testData.put("NewOFACDate", NewOFACDate);
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
	
	private void getIndividualData(HashMap<String,String> testData,String excelSheetFilePath, String query )
	{
		
		try
		{
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(excelSheetFilePath,query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				while(recordSet.next())
				{
					String First_Name = recordSet.getField("First_Name");
					if(First_Name!=null && !First_Name.trim().isEmpty())
					{
						testData.put("First_Name", First_Name);
					}
					String Middle_Name = recordSet.getField("Middle_Name");
					if(Middle_Name!=null && !Middle_Name.trim().isEmpty())
					{
						testData.put("Middle_Name", Middle_Name);
					}
					String Last_Name = recordSet.getField("Last_Name");
					if(Last_Name!=null && !Last_Name.trim().isEmpty())
					{
						testData.put("Last_Name", Last_Name);
					}
					String Hit_Type = recordSet.getField("Hit_Type");
					if(Hit_Type!=null && !Hit_Type.trim().isEmpty())
					{
						testData.put("Hit_Type", Hit_Type);
					}
					
					String Change_Type = recordSet.getField("Change_Type");
					if(Change_Type!=null && !Change_Type.trim().isEmpty())
					{
						testData.put("Change_Type", Change_Type);
					}
					
					String Hit_NoHit = recordSet.getField("Change_Type");
					if(Hit_NoHit!=null && !Hit_NoHit.trim().isEmpty())
					{
						testData.put("Hit_NoHit", Hit_NoHit);
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

