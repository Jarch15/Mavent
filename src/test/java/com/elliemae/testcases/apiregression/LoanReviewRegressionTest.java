package com.elliemae.testcases.apiregression;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;

import Exception.FilloException;
import jcifs.smb.SmbFile;

/**
 * <b>Name:</b> LoanReviewRegressionTest 
 * <b>Description: </b>This is Test class for regression testing of loan review service.
 * This processes the loan review input xmls and validate the ouput xmls file.
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class LoanReviewRegressionTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(LoanReviewRegressionTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	String decryptedPassword;
	private HashMap<String, String> environmentData= new HashMap<String, String>();

	/**
	 * <b>Name:</b>processInputXML<br>
	 * <b>Description:</b> Reads the file to be processed from shared path, process files
	 * against loan review service and saves output xmls<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void processInputXML(HashMap<String, String> testData) {

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

		// Decrypt the password
		if (testData.get("Network_Password") != null) {
			decryptedPassword = CommonUtility.decryptData(testData.get("Network_Password"));
		}

		// Read SOAP Header and Trail from input folder
		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");
		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"LoanReviewRegressionTest_data.xlsx");

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
				try {
					EllieMaeLog.log(_log, currentThread + "Processing input xml file : " + fileToProcess,
							EllieMaeLogLevel.reporter);

					responseMap = ObjSOAPMaventWebServiceCall.httpPost(apiMethodName, request);

				} catch (Exception e) {
					e.printStackTrace();
					EllieMaeLog.log(_log, currentThread + "Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
							EllieMaeLogLevel.reporter);
					sAssert.fail(currentThread + "Loan Review SOAP SERVICE exception occured : " + fileToProcess);
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
	 * <b>Name:</b>verifyOutputXML<br>
	 * <b>Description:</b> Reads the output xml files from shared path and
	 * validates with provided validation content.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void verifyOutputXML(HashMap<String, String> testData) 
	{
		int THREAD_COUNT=1;
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}
		
		// Get the file path using the utility command
		String additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"LoanReviewRegressionTest_data.xlsx");

		// Create the query for the data from test data sheet
		String strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
		HashMap<String, HashMap<String, String>> testCaseData = null;

		try {
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		} catch (FilloException e1) {
			e1.printStackTrace();
		}

		HashMap<String, String> testCaseMethodData = testCaseData.get("1");
		
		// Get list of files to be validated from shared path
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
		
		// validate each output file
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();
		for (String fileToProcess : filesToProcess) {
			Future<?> future = executor.submit(() -> {
				String currentThread = Thread.currentThread().getName() + ":::::";
				String outputXMLString="";
				
				// read file
				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					outputXMLString = CommonUtility.readFile(testData.get("Input_File_Path")+File.separator+fileToProcess);
				}
				else
				{
					// Read input xml file from network location
					try {
						outputXMLString = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path")+"/",fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// validate
				if(outputXMLString.contains(testCaseMethodData.get("ValidationContent")))
				{
					sAssert.fail(currentThread + "Validation failed for file : " + fileToProcess);
					EllieMaeLog.log(_log,
							currentThread + "Validation failed for file : " + fileToProcess,
							EllieMaeLogLevel.reporter);
					
					// copy failed file to failed files directory
					if (CommonUtilityApplication.isExecutedFromEMDomain()) 
					{
						CommonUtility.copyFilesOrFolder(testData.get("Input_File_Path")+File.separator+fileToProcess, testData.get("Output_File_Path")+File.separator+fileToProcess, FileType.FILE);
					}
					else
					{
						try {
							CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), testData.get("Input_File_Path")+File.separator+fileToProcess, testData.get("Output_File_Path"), fileToProcess);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
				else
				{
					EllieMaeLog.log(_log,
							currentThread + "Validation successfull for file : " + fileToProcess,
							EllieMaeLogLevel.reporter);
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
				File directory = new File(result_output_Folder);
				   if (! directory.exists()){
				       directory.mkdir();
				   }
				CommonUtilityApplication.createOutputFile(result_output_Folder+File.separator+outputXMLFileName, responseXMLBody);				
			}
			else
			{
				CommonUtilityApplication.writeFileToNetworkSharedLocation(result_output_Folder, outputXMLFileName, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
