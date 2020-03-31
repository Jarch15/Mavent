package com.elliemae.testcases.Sample;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.SamplePage;
import com.elliemae.testcases.Sample.SampleTest;


public class SampleTest extends EllieMaeApplicationBase
{
	APIValidationMethodsApplication validation=new APIValidationMethodsApplication();
	SamplePage samplePage;
	
	/**
	 * <b>Name:</b> testSample<br>
	 * <b>Description:</b> 
	 * 
	 */
	@Test(dataProvider="get-test-data-method")
	public void testSample(HashMap<String,String> testData) {
		Logger _log = Logger.getLogger(CommonUtilityApplication.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log,"Start DateTime : "+CommonUtilityApplication.getCurrentDateTimeString("yyyy-MM-dd HH:mm:ss"),EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"**************************************************",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"Start: Description on TestMethod",EllieMaeLogLevel.reporter);

		samplePage = new SamplePage(driver);

		try{
			//Extract Jira Id's from the test steps and populate teststatus object with JiraId's
			CommonUtility.populateTestStatusCollectionWithJiraId(testStatus, testData.get(FrameworkConsts.JIRAID));
			
			samplePage.goToThePage(testData.get("URL"));

			//set curr Jira Id to validate for Assert
			Assert.setCurrJIRA_ID(CommonUtility.retrieveJIRA_ID(testData.get(FrameworkConsts.JIRAID),1));
			Assert.assertEquals(samplePage.getTitle(), "Google","Page title failed");

			samplePage.setSearchTextBox("testng");
			samplePage.clickSearchButton();
			
			Assert.assertEquals(samplePage.getTitle(), "testng - Google Search","Page title failed");
		}
		catch(Exception e){
			Assert.assertTrue(false);
			EllieMaeLog.log(_log,"Testcase testSample failed with exception: "+e.getMessage(),EllieMaeLogLevel.reporter);
		}

		EllieMaeLog.log(_log,"End: Testing testSample",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"**************************************************",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"End DateTime : "+CommonUtilityApplication.getCurrentDateTimeString("yyyy-MM-dd HH:mm:ss"),EllieMaeLogLevel.reporter);
	}

}
