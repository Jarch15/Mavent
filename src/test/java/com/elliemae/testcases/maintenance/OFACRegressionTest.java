package com.elliemae.testcases.maintenance;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;

import Exception.FilloException;
import Fillo.Recordset;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/* 1.	Script should copy the after files (ignore *-R.xml files) & regression control files  from last OFAC test date folder 
 * to the new OFAC folder created manually(Do not create this folder by automation) 
 * under below path and use the date as specified under same excel which we use for post launch ofac folder.
 * Add suffix in the name of the After file with -R. 
	
	                  \\irvinefs\irv_public\IT\QA\Maintenance\OFAC\Test Files\2018
	
	2.	Run the files copied in QA1 environment and save the results files under the same date folder(Do not create this folder by automation) inside the below path 
	
	            \\irvinefs\irv_public\IT\QA\Maintenance\OFAC\Test Results\2018
	
	3.	Compare the these files with previous folder and it should match
	
	4.	 Script should run the files from the below folder in QA1
	
	\\irvinefs\irv_public\IT\QA\Maintenance\OFAC\Test Files\REGRESSION
	
	Save the files to same date folder as above and compare with previous run folder. Both should match.
	
	\\irvinefs\irv_public\IT\QA\Maintenance\OFAC\Test Results\2018
*/
public class OFACRegressionTest extends EllieMaeApplicationBase
{
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(OFACRegressionTest.class);
	
	@Test(dataProvider = "get-test-data-method")
	public void verify(HashMap<String,String> testData){
		
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
		
		try {
			
			EllieMaeLog.log(_log, "Reading Folders data from external Excel file...", EllieMaeLogLevel.reporter);
			readFolderPathsFromExcel(testData);
			
			//Get the file path using the utility command
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "OFACRegressionTest_Data.xlsx");
			
			//Create the query for the data from test data sheet 
			strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"' order by SequenceID";
			
			//Execute the query and save it in a hash map testCaseData			
            HashMap<String, HashMap<String, String>> testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
            
            HashMap<String, String> testCaseMethodData = testCaseData.get("1");
            
            String inputData = testCaseMethodData.get("InputData");
            String[] fileNamePaterns = inputData.split(",");
 
            // Get folder name for recent date
	   		EllieMaeLog.log(_log, "Getting the folder name of recent date (previous run)...", EllieMaeLogLevel.reporter);
            String recentDateFolder = getPreviousRunFolderName(testData.get("Input_File_Path"), testData.get("Date"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            EllieMaeLog.log(_log, "Recent Date Folder from "+testData.get("Input_File_Path")+" is : "+recentDateFolder, EllieMaeLogLevel.reporter);
            
            // Copy files from recent date folder to current date folder
            EllieMaeLog.log(_log, "Copying files from recent date (previous run) folder : "+recentDateFolder+" to current date folder : "+testData.get("Date"), EllieMaeLogLevel.reporter);
            SmbFile[] listOfFilesRecentDateFolder = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Input_File_Path")+"/"+recentDateFolder+"/", FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            List<String> filesToCopy = new ArrayList<String>();
            for (SmbFile networkFile : listOfFilesRecentDateFolder)
			{
            	for(String fileNamePattern : fileNamePaterns)
            	{
            		// Get only After and Regression file. Ignore files with *-R.xml
					if(networkFile.getName().contains(fileNamePattern)
							&& !networkFile.getName().contains("-R.xml"))
					{
						filesToCopy.add(networkFile.getName());
					}
            	}
			        
			}
            
            for (String fileToCopy : filesToCopy)
			{
            	String sourceFilePath = testData.get("Input_File_Path")+"/"+recentDateFolder+"/"+fileToCopy;
            	String destinationPath = testData.get("Input_File_Path") + testData.get("Date")+"/";
            	String destinationFileName = fileToCopy;
            	if(destinationFileName.contains("After"))
            	{
            		// For file name having After, suffix it with -R
            		destinationFileName = destinationFileName.replace(".xml", "-R.xml");
            	}
            	CommonUtilityApplication.copyFileFromNetworkLocationToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), sourceFilePath, destinationPath, destinationFileName);
			}
            EllieMaeLog.log(_log, "Copied files from recent date (previous run) folder : "+recentDateFolder+" to current date folder : "+testData.get("Date"), EllieMaeLogLevel.reporter);
            
            /* Process the files on QA1 */
            EllieMaeLog.log(_log, "Processing the input files on QA1...", EllieMaeLogLevel.reporter);
            String inputDir = testData.get("Input_File_Path") + testData.get("Date")+"/";
            EllieMaeLog.log(_log, "Processing the input files from directory path: " +inputDir, EllieMaeLogLevel.reporter);
    		
            // Read SOAP Header and Trail from input folder
    		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");
    		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");
    		String apiMethodName = testCaseMethodData.get("APIMethodName");
            
            //Code for processing files from network

            SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(inputDir, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
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
            
            for (String fileToProcess : filesToProcessFromNetwork) 
            {
    				HashMap<String, String> responseMap = new HashMap<>();
    				// Prepare SOAP Request
    				String request = "";
    				if (CommonUtilityApplication.isExecutedFromEMDomain()) 
    				{
    					request = CommonUtility.readFile(inputDir+ fileToProcess);
    				} 
    				else 
    				{
    					// Read input xml file from network location
    					try 
    					{
    						request = CommonUtility.readFileFromNetworkSharedLocation(inputDir,
    								fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
    					} 
    					catch (Exception e) {
    						EllieMaeLog.log(_log,"Exception occurred during reading file from Network : "
    								+ fileToProcess, EllieMaeLogLevel.reporter);
    						e.printStackTrace();
    					}
    				}
    				// Append header and footer info needed for SOAP web service
    				// call
    				request = soapHeaderInfo + request + soapTrailInfo;
    				try 
    				{
    					EllieMaeLog.log(_log,"Processing input xml file : " + fileToProcess,
    							EllieMaeLogLevel.reporter);

    					responseMap = ObjSOAPMaventWebServiceCall.httpPost(apiMethodName, request);

    				} 
    				catch (Exception e) 
    				{
    					EllieMaeLog.log(_log,"Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
    							EllieMaeLogLevel.reporter);
    					e.printStackTrace();
    					sAssert.fail("Test case failed due to exception : "+e.getMessage());
    				}

    				if (responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode"))) 
    				{
    					// save output xml file
    					EllieMaeLog.log(_log, "Saving output xml file : " +fileToProcess, EllieMaeLogLevel.reporter);
    					saveOuputXML(responseMap, testData,testData.get("Regression_Output_Path")+"/"+testData.get("Date")+"/", fileToProcess);
    				} 
    				else 
    				{
    					// Do SoftAssert FAIL for TestNG result
    					EllieMaeLog.log(_log,"Found Internal Server Error, while processing : " + fileToProcess,
    							EllieMaeLogLevel.reporter);
    					sAssert.fail("Found Internal Server Error, hence failed " + fileToProcess);
    				}
    			}

            /* Compare the files from the 2nd most recent folder (Base File) from output path */
            
            EllieMaeLog.log(_log, "Comparing the files from most recent folder and current run folder...", EllieMaeLogLevel.reporter);
            // Get the recent output folder name
            String recentOutputDateFolder = getPreviousRunFolderName(testData.get("Regression_Output_Path"), testData.get("Date"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            EllieMaeLog.log(_log, "Recent output Date Folder (Baseline): "+testData.get("Regression_Output_Path")+"/"+recentOutputDateFolder+"/", EllieMaeLogLevel.reporter);
            EllieMaeLog.log(_log, "Current output Date Folder : "+testData.get("Regression_Output_Path")+"/"+testData.get("Date")+"/", EllieMaeLogLevel.reporter);
            
            /* Read the files from current Output folder and recent output folder and compare the files */
	   		
	   		// List the files from current output for comparison
	   		SmbFile[] listOfCurrentOutputFiles = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Regression_Output_Path")+"/"+testData.get("Date")+"/", FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
	   		for (SmbFile currentOutputFile : listOfCurrentOutputFiles)
			{
	   			if(currentOutputFile.isFile())
	   			{
	   				EllieMaeLog.log(_log, "Comparing file... : ", EllieMaeLogLevel.reporter);
	   				String outputBaseFileName=currentOutputFile.getName();
	   				if(outputBaseFileName.contains("-R.xml"))
	   				{
	   					// Base file name don't have suffix as -R
	   					outputBaseFileName = outputBaseFileName.replace("-R.xml", ".xml");
	   				}
	   				String outputRecentFileName=currentOutputFile.getName();
	   				EllieMaeLog.log(_log, "Base File: "+outputBaseFileName, EllieMaeLogLevel.reporter);
	   				EllieMaeLog.log(_log, "Target File: "+outputRecentFileName, EllieMaeLogLevel.reporter);
	   				// Compare files from currentOutput and recentOutput
	   				String baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Regression_Output_Path")+"/"+recentOutputDateFolder+"/",outputBaseFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
	   				String recentOutput = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Regression_Output_Path")+"/"+testData.get("Date")+"/",outputRecentFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
	   				boolean result = APIValidationMethodsApplication.compareXMLUsingXMLUnit(baseXMLFile, recentOutput, "<OFACData", "</OFACData>");
	   				EllieMaeLog.log(_log,"XML Comparison result for " + currentOutputFile.getName()+ " is : "+result,
							EllieMaeLogLevel.reporter);
	   				sAssert.assertTrue(result, "XML Comparison for " + currentOutputFile.getName()+ " failed.");
	   			}
			}
	   		
	   		/* Process Regression Files and Validate with previous Run - START */
	   		
	   		EllieMaeLog.log(_log, "Process and validate Regression Files...", EllieMaeLogLevel.reporter);
	   		// List the Files to Process
            SmbFile[] listOfRegressionFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Regression_Input_Files_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            List<String> regressionFilesToProcessFromNetwork = new ArrayList<String>();
            for (SmbFile networkFile : listOfRegressionFilesFromNetwork)
			{
            	regressionFilesToProcessFromNetwork.add(networkFile.getName());
			}
            
            // Process the files
            EllieMaeLog.log(_log, "Processing regression files and saving output files...", EllieMaeLogLevel.reporter);
            for (String fileToProcess : regressionFilesToProcessFromNetwork) 
            {
    				HashMap<String, String> responseMap = new HashMap<>();
    				// Prepare SOAP Request
    				String request = "";
    				if (CommonUtilityApplication.isExecutedFromEMDomain()) 
    				{
    					request = CommonUtility.readFile(testData.get("Regression_Input_Files_Path")+ fileToProcess);
    				} 
    				else 
    				{
    					// Read input xml file from network location
    					try 
    					{
    						request = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Regression_Input_Files_Path"),
    								fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
    					} 
    					catch (Exception e) {
    						EllieMaeLog.log(_log,"Exception occurred during reading file from Network : "
    								+ fileToProcess, EllieMaeLogLevel.reporter);
    						e.printStackTrace();
    					}
    				}
    				// Append header and footer info needed for SOAP web service
    				// call
    				request = soapHeaderInfo + request + soapTrailInfo;
    				try 
    				{
    					EllieMaeLog.log(_log,"Processing input xml file : " + fileToProcess,
    							EllieMaeLogLevel.reporter);

    					responseMap = ObjSOAPMaventWebServiceCall.httpPost(apiMethodName, request);

    				} 
    				catch (Exception e) 
    				{
    					EllieMaeLog.log(_log,"Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
    							EllieMaeLogLevel.reporter);
    					e.printStackTrace();
    					sAssert.fail("Test case failed due to exception : "+e.getMessage());
    				}

    				if (responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode"))) 
    				{
    					// save output xml file
    					saveOuputXML(responseMap, testData,testData.get("Regression_Output_Files_Path")+"/"+testData.get("Date")+"/", fileToProcess);
    				} 
    				else 
    				{
    					// Do SoftAssert FAIL for TestNG result
    					EllieMaeLog.log(_log,"Found Internal Server Error, while processing : " + fileToProcess,
    							EllieMaeLogLevel.reporter);
    					sAssert.fail("Found Internal Server Error, hence failed " + fileToProcess);
    				}
    			}
            
            /* compare the current output files with recent output files */
            // Get the folder name for recent output
            String recentOutputDateFolderRegression = getPreviousRunFolderName(testData.get("Regression_Output_Files_Path"), testData.get("Date"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            EllieMaeLog.log(_log, "Recent output date folder for regression : "+recentOutputDateFolderRegression, EllieMaeLogLevel.reporter);
            
             /* Read the files from current Output folder and recent  output folder and compare the files */
            EllieMaeLog.log(_log, "Comparing the output files from recent output folder (baseline) : "+testData.get("Regression_Output_Files_Path")+"/"+recentOutputDateFolderRegression+"/"+ "  and current output folder "+testData.get("Regression_Output_Files_Path")+"/"+testData.get("Date")+"/", EllieMaeLogLevel.reporter);
 	   		// List the files from current output for comparison
 	   		SmbFile[] listOfCurrentOutputFilesRegression = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Regression_Output_Files_Path")+"/"+testData.get("Date")+"/", FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
 	   		for (SmbFile currentOutputFile : listOfCurrentOutputFilesRegression)
 			{
 	   			if(currentOutputFile.isFile())
 	   			{
 	   				// Compare files from currentOutput and recentOutput
 	   				String baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Regression_Output_Files_Path")+"/"+recentOutputDateFolderRegression+"/",currentOutputFile.getName(),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
 	   				String recentOutput = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Regression_Output_Files_Path")+"/"+testData.get("Date")+"/",currentOutputFile.getName(),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
 	   				boolean result = APIValidationMethodsApplication.compareXMLUsingXMLUnit(baseXMLFile, recentOutput, "<OFACData", "</OFACData>");
 	   				EllieMaeLog.log(_log,"XML Comparison result for " + currentOutputFile.getName()+ " is : "+result,
 							EllieMaeLogLevel.reporter);
 	   				sAssert.assertTrue(result, "XML Comparison for " + currentOutputFile.getName()+ " failed.");
 	   			}
 			}
            
	   		/* Process Regression Files and Validate with previous Run - END */
               
 	   	 EllieMaeLog.log(_log, "OFAC Regression Test Case Completed !", EllieMaeLogLevel.reporter);
            
		} 
		
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception Occurred for OFAC Regression : "+e.getMessage(), EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.assertTrue(false,"test failed due to exception");
		}
		
		sAssert.assertAll();

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
					String Regression_Output_Path = recordSet.getField("Regression_Output_Path");
					if(Regression_Output_Path!=null && !Regression_Output_Path.trim().isEmpty())
					{
						testData.put("Regression_Output_Path", Regression_Output_Path);
					}
					String Regression_Input_Files_Path = recordSet.getField("Regression_Input_Files_Path");
					if(Regression_Input_Files_Path!=null && !Regression_Input_Files_Path.trim().isEmpty())
					{
						testData.put("Regression_Input_Files_Path", Regression_Input_Files_Path);
					}
					String Regression_Output_Files_Path = recordSet.getField("Regression_Output_Files_Path");
					if(Regression_Output_Files_Path!=null && !Regression_Output_Files_Path.trim().isEmpty())
					{
						testData.put("Regression_Output_Files_Path", Regression_Output_Files_Path);
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
	
	
	/**
	 * <b>Name:</b>saveOuputXML<br>
	 * <b>Description:</b> Extracts the output from responseMap and generates the 
	 * response XML file. Saves the XML file on shared path.<br>
	 * @author <i>Jayesh Bhapkar</i> 
	 * @param responseMap
	 * @param testData
	 * @param result_output_Folder
	 * @param outputXMLFileName
	 */
	private void saveOuputXML(HashMap<String,String> responseMap,HashMap<String, String> testData, String result_output_Folder, String outputXMLFileName )
	{
		// Get the response xml from response Map
		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY"));	
		
		// Extract the response to get DataPackage node inside response xml received
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		HashMap<String, String> xPathKeyValueMap = new HashMap<>();
		xPathKeyMap.put("DataPackage", "//reviewDocumentReturn");
		try 
		{
			xPathKeyValueMap = XMLUtilityApplication.readXMLtoExtractXPathValue(responseXML,xPathKeyMap);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		String responseXMLBody = xPathKeyValueMap.get("DataPackage");

		/* Create output xml file using the response xml received at network location*/
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				//Create the folders if not exists
				File directory = new File(result_output_Folder);
				try
				{
				   if (! directory.exists()){
				       directory.mkdirs();
				   }
				}
				catch(Exception e)
				{
					EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
				}
				 // create file
				CommonUtilityApplication.createOutputFile(result_output_Folder+File.separator+outputXMLFileName, responseXMLBody);				
			}
			else
			{
				// Create the folders if not exists
				NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				String sourceFilePath = "smb:" + result_output_Folder;
				SmbFile sFile = new SmbFile(sourceFilePath, auth);
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
				 // create file on network
				CommonUtilityApplication.writeFileToNetworkSharedLocation(result_output_Folder, outputXMLFileName, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during generating output xml file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}

	}
	
	/**
	 * <b>Name:</b>getPreviousRunFolderName<br>
	 * <b>Description:</b> Utility method to get the latest folder name
	 * from the provided network path.<br>
	 * @author <i>Jayesh Bhapkar</i> 
	 * @param directory
	 * @param excludeDirectory
	 * @param network_domain
	 * @param network_username
	 * @param network_decrypted_password
	 * @throws Exception 
	 */
	public String getPreviousRunFolderName(String directory, String excludeDirectory, String network_domain, String network_username, String network_decrypted_password) throws Exception
	{
			String recentDateFolder ="";
        	// list the files(folders) from the Input_File_Path.
	   		SmbFile[] listOfFolders = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(directory, network_domain,network_username, network_decrypted_password);
	   		List<String> listOfFolderNames = new ArrayList<String>();
	   		for (SmbFile networkFile : listOfFolders)
			{
	   			if(networkFile.isDirectory())
	   			{
	   				String networkFileName = networkFile.getName().replace("/", "");
	   				if(!networkFileName.equalsIgnoreCase(excludeDirectory))
	   				{
	   					listOfFolderNames.add(networkFileName);
	   				}
	   			}
			}
          
          // Convert folder names to date (skip the current Date from excel) 
	   		List<Date> dateFolders = new ArrayList<Date>();
	   		for(String folderName : listOfFolderNames)
	   		{
	   			try
	   			{
		   			Date date=new SimpleDateFormat("yyyy-MM-dd").parse(folderName); 
		   			dateFolders.add(date);
	   			}
	   			catch (java.text.ParseException exception)
	   			{
	   				// Suppress the exception occurred during parsing the date
	   				EllieMaeLog.log(_log,"Ignore exception occurred during the parsing the date for folder : "+folderName,EllieMaeLogLevel.reporter);
	   				exception.getMessage();
	   			}
	   		}
	   		
	   		// Get the latest folder
	   		Date recentDate = Collections.max(dateFolders);
	   		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	   		recentDateFolder = dateFormat.format(recentDate);
	   		
		return recentDateFolder;
	}

}
