package com.elliemae.testcases.tilarules;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.SoftAssert;
import com.elliemae.testcases.enterpriseengine.EnterpriseEngineTest;

import Exception.FilloException;

/*
 * Author : Jayesh Bhapkar
 * Description : Test Class for TILA ROR rules Test automation.
 */
public class TilaRORRulesTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(EnterpriseEngineTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	HashMap<String, HashMap<String, String>> testCaseData;
	String decryptedPassword;

	/*
	 * Author : Jayesh Bhapkar
	 * Description : This is automated test method for
	 * verifying if input xml file for loan review is getting processed by
	 * calling a loan review web services. It verifies the response received
	 * from Web services with the expected response.
	 */
	@Test(dataProvider = "get-test-data-method")
	public void verifyTilaRORRules(HashMap<String, String> testData, ITestContext context) 
	{
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		Map<String,List<List<String>>> actualXpathMap = new HashMap<String,List<List<String>>>();
		SoftAssert softAssert = new SoftAssert();

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"TilaRORRulesTest_data.xlsx");

		// Create the query for the data from test data sheet
		strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
		
		try
		{
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		}
		catch (FilloException e)
		{
			e.printStackTrace();
		}
		
		actualXpathMap = ObjSOAPMaventWebServiceCall.processSaveAndValidateXML(testData, testCaseData, sAssert, softAssert);
		
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

