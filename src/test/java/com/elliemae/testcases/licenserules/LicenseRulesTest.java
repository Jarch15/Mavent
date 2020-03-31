package com.elliemae.testcases.licenserules;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.testcases.generateandverify.GenerateInputFileAndVerifyUtility;

import Exception.FilloException;

/*
 * Author : Jayesh Bhapkar
 * Description : Test Class for validation of License Engine regression test cases.
 */
public class LicenseRulesTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(LicenseRulesTest.class);

 	/**
 	 * <b>Author: Jayesh Bhapkar
 	 * <b>Name:</b>verifyLicenseRules
 	 * <b>Description:</b> This is a test method to process the input
 	 * xml file and verify expected output with actual result for License Rules regression
 	 * test cases. 
 	 * 
 	 * @param testData 
 	 * @param context
 	 * 
 	 **/
	@Test(dataProvider = "get-test-data-method")
	public void verifyLicenseRules(HashMap<String, String> testData, ITestContext context)
	{

		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		
		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"LicenseRulesTest_data.xlsx");

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
		
		File f = new File("");
		String inputDirectoryPath = f.getAbsolutePath();
		inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"licenserules"+ File.separator +"input";

		// Call verifyActualResult to process input xmls and verify actual result with expected result
		GenerateInputFileAndVerifyUtility.verifyActualResult(testData, testCaseData, ObjSOAPMaventWebServiceCall, sAssert, inputDirectoryPath);
		
		// Actual Test Count
		@SuppressWarnings("unchecked")
		Map<String,String> testStatusMap = (Map<String, String>) context.getAttribute("testStatusMap");
		testStatusMap.putAll(sAssert.getTestStatus());
		sAssert.getTestStatus().clear(); // clear the status to avoid printing status on console
		context.setAttribute("testStatusMap", testStatusMap);

		sAssert.assertAll();
		
	}
	
}

