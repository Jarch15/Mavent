package com.elliemae.testcases.MaventSOP;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.JMSQueueMetricsPage;

public class JMSQueueMetricsTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(JMSQueueMetricsTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for verifying JMS queue metrics.
	 * This method logins to admin portal and verifies the JMS queues consumer values.
	 *  
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyJMSQueureMetrics_CE_12856(HashMap<String, String> testData) 
	{

		JMSQueueMetricsPage jmsQueueMetricsPage = new JMSQueueMetricsPage(driver);
		String complyInQueueValue ="";
		String complySaveQueueValue  ="";
		HashMap<String, String> envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
//		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
//		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");  
//		driver.switchTo().defaultContent();  
		
		// Navigate to first admin URL and assert the values returned with the testData values
		if(envData.get("JMSURL_1")!=null && !envData.get("JMSURL_1").isEmpty())
		{
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal",EllieMaeLogLevel.reporter);
			jmsQueueMetricsPage.navigateToAdminPortal("JMSURL_1");			
			jmsQueueMetricsPage.loginToAdminPortal();
			complyInQueueValue  = jmsQueueMetricsPage.get2ComplyInQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplyInQueueValue_1", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_1"), "2complyInQueue consumer value does not match with expected value."); 
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_QA"), "2complyInQueue consumer value does not match with expected value."); 
			}
			complySaveQueueValue  = jmsQueueMetricsPage.get2ComplySaveQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplySaveQueueValue_1", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_1"), "2complySaveQueue consumer value does not match with expected value.");
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_QA"), "2complySaveQueue consumer value does not match with expected value.");
			}
			jmsQueueMetricsPage.logout();
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal complete",EllieMaeLogLevel.reporter);
		}
		
		// Navigate to Second admin URL and assert the values returned with the testData values
		if(envData.get("JMSURL_3")!=null && !envData.get("JMSURL_3").isEmpty())
		{
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal",EllieMaeLogLevel.reporter);
			jmsQueueMetricsPage.navigateToAdminPortal("JMSURL_3");			
			jmsQueueMetricsPage.loginToAdminPortal();
			complyInQueueValue  = jmsQueueMetricsPage.get2ComplyInQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplyInQueueValue_3", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_1"), "2complyInQueue consumer value does not match with expected value."); 
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_QA"), "2complyInQueue consumer value does not match with expected value."); 
			}
			complySaveQueueValue  = jmsQueueMetricsPage.get2ComplySaveQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplySaveQueueValue_3", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_1"), "2complySaveQueue consumer value does not match with expected value.");
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_QA"), "2complySaveQueue consumer value does not match with expected value.");
			}
			jmsQueueMetricsPage.logout();
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal complete",EllieMaeLogLevel.reporter);
		}
		
		// Navigate to Third admin URL and assert the values returned with the testData values
		if(envData.get("JMSURL_2")!=null && !envData.get("JMSURL_2").isEmpty())
		{
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal",EllieMaeLogLevel.reporter);
			jmsQueueMetricsPage.navigateToAdminPortal("JMSURL_2");			
			jmsQueueMetricsPage.loginToAdminPortal();
			complyInQueueValue  = jmsQueueMetricsPage.get2ComplyInQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplyInQueueValue_2", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_2"), "2complyInQueue consumer value does not match with expected value."); 
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_QA"), "2complyInQueue consumer value does not match with expected value."); 
			}
			complySaveQueueValue  = jmsQueueMetricsPage.get2ComplySaveQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplySaveQueueValue_2", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_2"), "2complySaveQueue consumer value does not match with expected value.");
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_QA"), "2complySaveQueue consumer value does not match with expected value.");
			}
			jmsQueueMetricsPage.logout();
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal complete",EllieMaeLogLevel.reporter);
		}
		
		// Navigate to Fourth admin URL and assert the values returned with the testData values
		if(envData.get("JMSURL_4")!=null && !envData.get("JMSURL_4").isEmpty())
		{
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal",EllieMaeLogLevel.reporter);
			jmsQueueMetricsPage.navigateToAdminPortal("JMSURL_4");			
			jmsQueueMetricsPage.loginToAdminPortal();
			complyInQueueValue  = jmsQueueMetricsPage.get2ComplyInQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplyInQueueValue_4", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_2"), "2complyInQueue consumer value does not match with expected value."); 
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complyInQueueValue, testData.get("2complyInQueue_QA"), "2complyInQueue consumer value does not match with expected value."); 
			}
			complySaveQueueValue  = jmsQueueMetricsPage.get2ComplySaveQueueValue();
			CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH, driver);
			CommonUtilityApplication.takeScreenShot(testData, "JMSQueueMetricsTest_2ComplySaveQueueValue_4", CommonUtility.currentTimeStamp);
			if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("PROD") || FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("DR"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_2"), "2complySaveQueue consumer value does not match with expected value.");
			}
			else if(FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA1")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA2")
					||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA3")||FrameworkConsts.ENVIRONMENTNAME.equalsIgnoreCase("QA4"))
			{
				Assert.assertEquals(complySaveQueueValue, testData.get("2complySaveQueue_QA"), "2complySaveQueue consumer value does not match with expected value.");
			}
			jmsQueueMetricsPage.logout();
			EllieMaeLog.log(_log, "Reading consumer values from JMS portal complete",EllieMaeLogLevel.reporter);
		}
	}

}
