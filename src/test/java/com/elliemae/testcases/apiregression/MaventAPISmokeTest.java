package com.elliemae.testcases.apiregression;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.testcases.generateandverify.GenerateInputFileAndVerifyUtility;

import Exception.FilloException;
import Fillo.Recordset;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * <b>Name:</b> MaventAPISmokeTest 
 * <b>Description: </b>This is Test class for Mavent API Smoke Test.
 * This processes the loan review input xmls and validate the ouput xmls file
 * for load balancer.
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventAPISmokeTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(MaventAPISmokeTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	String decryptedPassword;
	private HashMap<String, String> environmentData= new HashMap<String, String>();

	/**
	 * <b>Name:</b>verifyLoadBalancer<br>
	 * <b>Description:</b> Reads the file to be processed from shared path, process files
	 * against loan review service and save the output xml.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void processInputXML(HashMap<String, String> testData) 
	{
		readFolderPathsFromExcel(testData);
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		int THREAD_COUNT=1;
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}

		// Read SOAP Header and Trail from input folder
		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");
		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"MaventAPISmokeTest_data.xlsx");

		// Create the query for the data from test data sheet
		strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
		HashMap<String, HashMap<String, String>> testCaseData = null;

		try {
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		} catch (FilloException e1) {
			e1.printStackTrace();
		}

		HashMap<String, String> testCaseMethodData = testCaseData.get("1");
		String apiMethodName = testCaseMethodData.get("APIMethodName");

		// Get list of files to be processed from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if (CommonUtilityApplication.isExecutedFromEMDomain()) {
			File[] files = new File(testData.get("Input_File_Path")).listFiles();

			for (File file : files) {
				if (file.isFile()) {
					filesToProcess.add(file.getName());
				}
			}
		} else {

			try {
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(
						testData.get("Input_File_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork) {
					filesToProcess.add(networkFile.getName());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Process all files
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();
		for (String fileToProcess : filesToProcess) {
			Future<?> future = executor.submit(() -> {
				String currentThread = Thread.currentThread().getName() + ":::::";
				HashMap<String, String> responseMap = new HashMap<>();
				// Prepare SOAP Request
				String request = "";
				if (CommonUtilityApplication.isExecutedFromEMDomain()) {
					request = CommonUtility.readFile(
							testData.get("Input_File_Path") + File.separator + fileToProcess);
				} else {
					// Read input xml file from network location
					try {
						request = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path"),
								fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} catch (Exception e) {
						EllieMaeLog.log(_log, currentThread + "Exception occurred during reading file from Network : "
								+ fileToProcess, EllieMaeLogLevel.reporter);
						e.printStackTrace();
					}
				}
				// Append header and footer info needed for SOAP web service
				// call
				request = soapHeaderInfo + request + soapTrailInfo;
				try 
				{
					EllieMaeLog.log(_log, currentThread + "Processing input xml file : " + fileToProcess,
							EllieMaeLogLevel.reporter);

					responseMap = ObjSOAPMaventWebServiceCall.httpPost(apiMethodName, request);

				} 
				catch (Exception e) 
				{
					EllieMaeLog.log(_log, currentThread + "Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
							EllieMaeLogLevel.reporter);
					e.printStackTrace();
					sAssert.fail(currentThread + "Test case failed due to exception : "+e.getMessage());
				}

				if (responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode"))) {
					// save output xml file
					saveOuputXML(responseMap, testData,
							testData.get("Output_File_Path"), fileToProcess);
				} else {
					// Do SoftAssert FAIL for TestNG result
					EllieMaeLog.log(_log,
							currentThread + "Found Internal Server Error, while processing : " + fileToProcess,
							EllieMaeLogLevel.reporter);
					sAssert.fail(currentThread + "Found Internal Server Error, hence failed " + fileToProcess);
				}
			});
			futures.add(future);

		}
		
		executor.shutdown();
		
		try {
	        for (Future<?> future : futures) {
	            future.get();
	            //System.out.println("Future Task Done : " + future.isDone());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		sAssert.assertAll();

	}
	
	/**
	 * <b>Name:</b>verifyLoadBalancer<br>
	 * <b>Description:</b> Reads the output xml files from shared path and
	 * validate for load balancer values.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void verifyLoadBalancer(HashMap<String, String> testData) 
	{
		readFolderPathsFromExcel(testData);
		int THREAD_COUNT=1;
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}
		
//		// Get the file path using the utility command
//		String additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
//				"MaventAPISmokeTest_data.xlsx");
//
//		// Create the query for the data from test data sheet
//		String strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
//				+ testData.get("Test_Case_Name") + "' order by SequenceID";
//
//		// Execute the query and save it in a hash map testCaseData
//		HashMap<String, HashMap<String, String>> testCaseData = null;
//
//		try {
//			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
//		} catch (FilloException e1) {
//			e1.printStackTrace();
//		}
//
//		HashMap<String, String> testCaseMethodData = testCaseData.get("1");

		// Get list of files to be validated from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if (CommonUtilityApplication.isExecutedFromEMDomain()) {
			File[] files = new File(testData.get("Output_File_Path")).listFiles();

			for (File file : files) {
				if (file.isFile()) {
					filesToProcess.add(file.getName());
				}
			}
		} else {
			try {
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(
						testData.get("Output_File_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork) {
					filesToProcess.add(networkFile.getName());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// validate each output file
		Map<String,List<String>> engineIdListMap = new HashMap<String,List<String>>();
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();
		Set<String> engineHostSet = new HashSet<String>();
		for (String fileToProcess : filesToProcess) {
			Future<?> future = executor.submit(() -> {
				//String currentThread = Thread.currentThread().getName() + ":::::";
				String outputXMLString="";
				
				// read file
				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					outputXMLString = CommonUtility.readFile(testData.get("Output_File_Path")+File.separator+fileToProcess);
				}
				else
				{
					// Read input xml file from network location
					try {
						outputXMLString = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Output_File_Path")+"/",fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				/* validate */
				// convert to xml and read engineHost
				Document xmlDoc = GenerateInputFileAndVerifyUtility.convertStringToXMLDocument(outputXMLString);
				String engineHost = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDoc, "DataPackage", "engineHost");
				String engineId = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDoc, "DataPackage", "engineId");
				
				if (engineHost==null || engineHost.isEmpty())
				{	
					// files not having engineHost Attribute
					engineHost = "NULL";
				}
				// Populate a engineIdListMap
				if (engineHost!=null && !engineHost.isEmpty())
				{
					engineHostSet.add(engineHost);
					List<String> engineIdList = engineIdListMap.get(engineHost);
					if(engineIdList!=null)
					{
						engineIdList.add(engineId);
						engineIdListMap.put(engineHost, engineIdList);
					}
					else
					{
						engineIdList = new ArrayList<String>();
						engineIdList.add(engineId);
						engineIdListMap.put(engineHost, engineIdList);
					}
					
					//create directory and copy file to the respective engineHost directory
					try 
					{
						if(CommonUtilityApplication.isExecutedFromEMDomain())
						{
							//Create the folders if not exists
							String enginHostDirectory = testData.get("Output_File_Path")+"/"+engineHost;
							File directory = new File(enginHostDirectory);
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
							 // copy file
							CommonUtility.copyFilesOrFolder(testData.get("Output_File_Path")+"/"+fileToProcess, enginHostDirectory+"/"+fileToProcess, FileType.FILE);
						}
						else
						{
							// Create the folders if not exists
							NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
							String enginHostDirectory = "smb:" + testData.get("Output_File_Path")+"/"+engineHost;
							SmbFile sFile = new SmbFile(enginHostDirectory, auth);
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
							 // copy file to engineHost folder on network (Read file from network and write to engine host folder)
							String fileContent = CommonUtilityApplication.readFileFromNetworkSharedLocation(testData.get("Output_File_Path"), fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
							CommonUtilityApplication.writeFileToNetworkSharedLocation(testData.get("Output_File_Path")+"/"+engineHost, fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), fileContent);
						}
					} 
					catch (Exception e) 
					{
						EllieMaeLog.log(_log, "Exception occurred during copying output xml file", EllieMaeLogLevel.reporter);
						e.printStackTrace();
					}
				}
				
				
			});
			futures.add(future);

		}
		
		executor.shutdown();
		
		try {
	        for (Future<?> future : futures) {
	            future.get();
	            //System.out.println("Future Task Done : " + future.isDone());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		//System.out.println("Total Engine Host : "+engineHostSet.size()+" : "+engineHostSet);
		EllieMaeLog.log(_log, "Total Engine Host : "+engineHostSet.size()+" : "+engineHostSet, EllieMaeLogLevel.reporter);
		//System.out.println("!!!!!!!!!!!!!!!!!!!! engineIdListMap : "+engineIdListMap);
		for(String engineHost : engineHostSet)
		{
			if(engineHost!=null && !engineHost.equalsIgnoreCase("null"))
			{
				List<String> engineIdList = engineIdListMap.get(engineHost);
				Set<String>engineIdSet = new HashSet<String>(engineIdList);
				EllieMaeLog.log(_log, "EngineHost : " +engineHost+" processed "+engineIdList.size()+" files using Engine IDs : "+engineIdSet, EllieMaeLogLevel.reporter);
				EllieMaeLog.log(_log, "EngineHost : " +engineHost+" has number of EngineIds : "+engineIdSet.size(), EllieMaeLogLevel.reporter);
				if(engineIdSet.size()<12)
				{
					sAssert.fail("EngineHost : " +engineHost+" has number of EngineIds : "+engineIdSet.size()+". Which is less than 12.");
				}
			}
		}
		sAssert.assertAll();

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
				String sourceFilePath = "smb:" + testData.get("Output_File_Path");
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
					String Input_File_Path = recordSet.getField("Input_File_Path");
					if(Input_File_Path!=null && !Input_File_Path.trim().isEmpty())
					{
						testData.put("Input_File_Path", Input_File_Path);
					}
					String Release_Name = recordSet.getField("Release_Name");
					String Output_File_Path = recordSet.getField("Output_File_Path");
					if(Output_File_Path!=null && !Output_File_Path.trim().isEmpty())
					{
						if(Release_Name!=null && !Release_Name.isEmpty())
						{
							Output_File_Path = Output_File_Path.replace("{RELEASE_NAME}", Release_Name);
						}
						testData.put("Output_File_Path", Output_File_Path);
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
