package com.elliemae.testcases.Sample;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.elliemae.apimethods.RESTAPIMethodCall;
import com.elliemae.core.APIUtility.APIValidationMethods;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

public class APISampleScenarioTest extends EllieMaeApplicationBase {	

	HashMap<String, String> dataBeanMap = new HashMap<>();

	APIValidationMethods apiValidationMethods;
	RESTAPIMethodCall restAPIMethodCall;
		
	@Test(dataProvider="get-test-data-method")	
	public void executeTestREST_NewTest_login(HashMap<String,String> testData) throws Exception
	{	
		_log= Logger.getLogger(CommonUtilityApplication.getCurrentClassAndMethodNameForLogger());

		apiValidationMethods = new APIValidationMethods();
		restAPIMethodCall = new RESTAPIMethodCall(dataBeanMap);

		EllieMaeLog.log(_log,"Start DateTime : "+CommonUtilityApplication.getCurrentDateTimeString("yyyy-MM-dd HH:mm:ss"),EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"**********************************************************************************",EllieMaeLogLevel.reporter);

		EllieMaeLog.log(_log,"TestCaseName: executeTestREST_NewTest_login",EllieMaeLogLevel.debug);
		HashMap<String, String> responseMap= new HashMap<>();

		String additionalDataFilePath= "";
		String strTestDataQuery="";

		try{
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "APISampleScenarioTest_Data.xlsx");
			strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"'  order by SequenceID";

			HashMap<String, HashMap<String, String>> testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);

			//Extract Jira Id's from the test steps and populate teststatus object with JiraId's
			CommonUtilityApplication.extractJiraIdAndPopulateTestStatusCollectionWithJiraId(testStatus,testCaseData);

			responseMap = restAPIMethodCall.tpoLoginREST(testCaseData.get("1"));
			//set curr Jira Id to validate for Assert
			Assert.setCurrJIRA_ID(testCaseData.get("1").get("JIRAID"));
			Assert.assertEquals(responseMap.get("STATUSCODE"),testCaseData.get("1").get("ValidationContent"));

			responseMap = restAPIMethodCall.tpoLoginREST(testCaseData.get("2"));
			//set curr Jira Id to validate for SoftAssert
			sAssert.setCurrJIRA_ID(testCaseData.get("2").get("JIRAID"));
			apiValidationMethods.stringComparison(responseMap.get("STATUSCODE"),testCaseData.get("2").get("ValidationContent"), sAssert);

			responseMap = restAPIMethodCall.tpoLoginREST(testCaseData.get("3"));		
			EllieMaeLog.log(_log, "Response Status: "+ responseMap.get("STATUSCODE"));
			apiValidationMethods.jsonValidation(responseMap.get("BODY"), testCaseData.get("3").get("ValidationContent"), sAssert);

			sAssert.assertAll();
		}
		catch(Exception e){
			Assert.assertTrue(false);
			EllieMaeLog.log(_log,"Testcase executeTestREST_NewTest_login failed with exception: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}

		EllieMaeLog.log(_log,"**********************************************************************************",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"End DateTime : "+CommonUtilityApplication.getCurrentDateTimeString("yyyy-MM-dd HH:mm:ss"),EllieMaeLogLevel.reporter);
	}
	
}
