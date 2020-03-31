package com.elliemae.testcases.syncservices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import jcifs.smb.SmbFile;


public class BatchReviewTest  extends EllieMaeApplicationBase 
{

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(BatchReviewTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	
	/* Author : Jayesh Bhapkar
	 * Description : .
	 *  */
	@Test(dataProvider = "get-test-data-method")	
	public void batchReview(HashMap<String,String> testData)
	{
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		SoftAssert softAssert = new SoftAssert();

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"BatchReviewTest_data.xlsx");

		// Create the query for the data from test data sheet
		strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
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
		
		// Get list of files to be processed from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if(CommonUtilityApplication.isExecutedFromEMDomain())
		{
			File[] files = new File(testData.get("Input_File_Path")).listFiles();

			for (File file : files) 
			{
			    if (file.isFile()) 
			    {
			    	filesToProcess.add(file.getName());
			    }
			}
		}
		else
		{
			try 
			{
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Input_File_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork)
				{
					filesToProcess.add(networkFile.getName());
				        
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
        for(String fileToProcess : filesToProcess)
        {
			// Call Web service method to process each file
			try 
			{
				responseMap = ObjSOAPMaventWebServiceCall.syncWebServiceCall(testData,testCaseMethodData,"batchReview",fileToProcess);
	
			} catch (Exception e) 
			{
				e.printStackTrace();
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
				Assert.assertTrue(false, "Test case failed due to exception");
			}
			
			// Do XPath validation if Status code is 200
			if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
			{
		        if (!(testCaseMethodData.get("ValidationMethod").isEmpty()))
		        {
		        	try 
		        	{
		        		apiValidationMethods.xPathValidation(ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap, fileToProcess, testData,"batchReview"), 
		        				testCaseMethodData.get("ValidationContent"),sAssert);
				
					} 
		        	catch (IOException e) 
		        	{
						
						e.printStackTrace();
					}
		        }
			}
			else
			{
				// Do SoftAssert FAIL for TestNG result
				softAssert.fail("Found Internal Server Error, hence failed "+fileToProcess);
			}
        }
		
		
        // Assertion for Xpath Validations
		sAssert.assertAll();
		
		// Assertion for failed test cases due to server error
		softAssert.assertAll();
		
	}

}
