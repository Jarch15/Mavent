package com.elliemae.testcases.MaventPostLaunch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;


public class WebServicesSmokeTest  extends EllieMaeApplicationBase 
{

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(WebServicesSmokeTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	String decryptedPassword;
	private HashMap<String, String> environmentData= new HashMap<String, String>();
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for verifying if input xml
	 * file for loan review is getting processed by calling a loan review web
	 * services.It verifies the response received from Web services with the 
	 * expected response.
	 *  */
	@Test(dataProvider = "get-test-data-method")	
	public void sampleReview_200_CE_12857(HashMap<String,String> testData)
	{
		//Define the logger
		_log =  Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		apiValidationMethods = new APIValidationMethodsApplication();
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
	    environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
	    
		//Get the file path using the utility command
		additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "WebServicesSmokeTest_data.xlsx");
		
		//Create the query for the data from test data sheet 
		strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"' order by SequenceID";
		
		//Execute the query and save it in a hash map testCaseData	
		HashMap<String, HashMap<String, String>> testCaseData = null;
		
		try 
		{
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		} 
		catch (FilloException e1) 
		{
			e1.printStackTrace();
		}		
		
		HashMap<String, String> testCaseMethodData = testCaseData.get("1");
		
		// Decrypt the password
		if (testData.get("Network_Password") != null) {
			decryptedPassword = CommonUtility.decryptData(testData.get("Network_Password"));
		}

		// Read SOAP Header and Trail from input folder
		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");
		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");

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
		
		
		for (String fileToProcess : filesToProcess) 
		{
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
						EllieMaeLog.log(_log, "Exception occurred during reading file from Network : "
								+ fileToProcess, EllieMaeLogLevel.reporter);
						e.printStackTrace();
					}
				}
				// Append header and footer info needed for SOAP web service call
				request = soapHeaderInfo + request + soapTrailInfo;
				
				String URI = environmentData.get("SOAPURL");
				if(URI!=null && !URI.isEmpty())
					processAndValidateInputXML(fileToProcess, apiMethodName, request, testData, testCaseData, URI);
				
				String URI2 = environmentData.get("SOAPURL_2");
				if(URI2!=null && !URI2.isEmpty())
					processAndValidateInputXML(fileToProcess, apiMethodName, request, testData, testCaseData, URI2);

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
			result_output_Folder = result_output_Folder+"/"+CommonUtility.currentTimeStamp;
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				//Create the folders if not exists
				File directory = new File(result_output_Folder);
				   if (! directory.exists()){
				       directory.mkdirs();
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
				if(!sFile.exists()){
					sFile.mkdirs();
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
	
	
	private void processAndValidateInputXML(String fileToProcess, String apiMethodName, String request, HashMap<String, String> testData, HashMap<String, HashMap<String, String>> testCaseData, String URI)
	{
		HashMap<String, String> responseMap = new HashMap<>();
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		
		try {
			EllieMaeLog.log(_log, "Processing input xml file : " + fileToProcess,
					EllieMaeLogLevel.reporter);

			responseMap = ObjSOAPMaventWebServiceCall.webServiceHTTPSmokeTest(apiMethodName, request, URI);

		} catch (Exception e) {
			e.printStackTrace();
			EllieMaeLog.log(_log, "Loan Review SOAP SERVICE exception occured",
					EllieMaeLogLevel.reporter);
			Assert.assertTrue(false, "Test case failed due to exception");
		}

		if (responseMap.get("STATUSCODE").equals(testCaseData.get("1").get("BaseLineStatusCode"))) {
			// save output xml file
			saveOuputXML(responseMap, testData,
					testData.get("Output_File_Path"), CommonUtilityApplication.getIpAddress(URI)+"_"+fileToProcess);
			//Convert the XML into the required format				
			String responseXML= XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY")); 	
	        if (!(testCaseData.get("1").get("ValidationMethod").isEmpty()))
	        {
	        	try 
	        	{
					apiValidationMethods.xPathValidation(responseXML, testCaseData.get("1").get("ValidationContent"),sAssert);
			
				} 
	        	catch (IOException e) 
	        	{
					
					e.printStackTrace();
				}
	        }
			
		} else {
			// Do SoftAssert FAIL for TestNG result
			EllieMaeLog.log(_log,
					"Found Internal Server Error, while processing : " + fileToProcess,
					EllieMaeLogLevel.reporter);
			sAssert.fail("Found Internal Server Error, hence failed " + fileToProcess);
		}

	}

}
