package com.elliemae.testcases.highcostrules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;

/*
 * Author : Nidhi Khandelwal
 * Description : Test Class for High Cost Rules Regression Test automation.
 * It contains test cases for validating rules of different states for high cost.
 */
public class RI_HighCostRulesRegressionTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(RI_HighCostRulesRegressionTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	SoftAssert softAssert = new SoftAssert();

	/*
	 * Author : Nidhi Khandelwal
	 * Description : This is automated test method for
	 * verifying if input xml file for loan review is getting processed by
	 * calling a loan review web services. It verifies the respons	e received
	 * from Web services with the expected response.
	 */
	@Test(dataProvider = "get-test-data-method")
	public void verifyHighCostRules_CE_16172(HashMap<String, String> testData, ITestContext context) 
	{
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		Map<String,List<List<String>>> actualXpathMap = new HashMap<String,List<List<String>>>();

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"RI_HighCostRulesRegressionTest_data.xlsx");

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
		
		for(int i=1;i<=testCaseData.size();i++)
			{
				HashMap<String, String> testCaseMethodData = testCaseData.get(""+i);
				List<List<String>> actualXPathList = new ArrayList<List<String>>();
				
				if(testCaseMethodData.get("Execute")!=null && testCaseMethodData.get("Execute").equalsIgnoreCase("Yes"))
				{
			
					// Call Web service method with testData and testCase data
					try 
					{
						responseMap = ObjSOAPMaventWebServiceCall.webServiceHTTPTest(testData,testCaseMethodData);
			
					} catch (Exception e) 
					{
						e.printStackTrace();
						EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
						Assert.assertTrue(false, "Test case failed due to exception");
					}
			
			
					// Assert the web services response for status code and xpath
					//Assert.assertEquals(responseMap.get("STATUSCODE"), testCaseMethodData.get("BaseLineStatusCode"));
					
					// Do XPath validation if Status code is 200
					if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
					{
			        if (!(testCaseMethodData.get("ValidationMethod").isEmpty()))
			        {
			        	try 
			        	{
			        		actualXPathList = apiValidationMethods.xPathValidationExtension(ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap, testCaseMethodData, testData), 
			        				testCaseMethodData.get("ValidationContent"),sAssert);
							actualXpathMap.put(sAssert.getCurrJIRA_ID(), actualXPathList);
					
						} 
			        	catch (IOException e) 
			        	{
							
							e.printStackTrace();
						}
			        }
			        
					}
					
					else
					{
						// Put JIRAID and FAIL status for reporting
						sAssert.getTestStatus().put(testCaseMethodData.get("JIRAID"), "Response code is : "+ responseMap.get("STATUSCODE")+" : FAIL");						
						// Do SoftAssert FAIL for TestNG result
						softAssert.fail("Found Internal Server Error, hence failed "+testCaseMethodData.get("JIRAID"));
					}
					
				}
			
		}
		
		// Update the test data sheet with test statuses and actual results		
		File file = new File(additionalDataFilePath);
		file.setWritable(true);
		
		CommonUtilityApplication.updateResultToExcelFile(additionalDataFilePath,testData,sAssert.getTestStatus());
		CommonUtilityApplication.updateActualResultToExcelFile(additionalDataFilePath,testData, actualXpathMap);
		
		// Actual Test Count
		@SuppressWarnings("unchecked")
		Map<String,String> testStatusMap = (Map<String, String>) context.getAttribute("testStatusMap");
		testStatusMap.putAll(sAssert.getTestStatus());
		context.setAttribute("testStatusMap", testStatusMap);
		
		// Assertion for Xpath Validations
		sAssert.assertAll();
		
		// Assertion for failed test cases due to server error
		softAssert.assertAll();
		
	}
	
}
