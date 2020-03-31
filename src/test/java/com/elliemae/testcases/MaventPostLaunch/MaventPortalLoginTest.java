package com.elliemae.testcases.MaventPostLaunch;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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

public class MaventPortalLoginTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventPortalLoginTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent SOP test case,
	 * This does loan creation, review a loan, search a loan, view PDF and activity report
	 * in a Mavent Portal.
	 *  
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyLogin(HashMap<String, String> testData) 
	{
		
		HashMap<String, String> userData = new HashMap<>();
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);		
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		EllieMaeLog.log(_log, "Mavent Portal Login test Starts",EllieMaeLogLevel.reporter);
		
		for(int i=1; i<=8; i++)
		{
			// Navigate
			EllieMaeLog.log(_log, "Navigating to the Mavent URL", EllieMaeLogLevel.reporter);

			try
			{
				driver.get(testData.get("MAVENT_URL_"+i));		
			}
			catch(Exception e)
			{
			}
			if(driver.getTitle().equalsIgnoreCase("Certificate Error: Navigation Blocked"))
			{
				driver.get("javascript:document.getElementById('overridelink').click();");
			}

			Assert.assertEquals(driver.getTitle(), "Login", "Navigation to the Mavent Portal Failed");
			EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);			
			CommonUtilityApplication.threadWait(2000);

			// Login
			EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);
			
			driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).sendKeys(userData.get("UserName"));
			CommonUtilityApplication.threadWait(1000);
			driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(userData.get("Password"));
			CommonUtilityApplication.threadWait(1000);
			driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(userData.get("Company"));
			CommonUtilityApplication.threadWait(2000);
			driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN)).click();
			CommonUtilityApplication.threadWait(5000);
			Assert.assertEquals(driver.getTitle(), "Home", "Login to the Mavent Portal Failed");
			String loggedUser = null;
			loggedUser = driver.findElement(By.xpath(MaventPortalMenuConsts.LOGGED_USER_XPATH)).getText();
			EllieMaeLog.log(_log, "Logged user : "+loggedUser, EllieMaeLogLevel.reporter);
			Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
			Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
			EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
			CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_verifyLogin_"+i, CommonUtility.currentTimeStamp);

			// Logout
			EllieMaeLog.log(_log, "Logging out of the portal", EllieMaeLogLevel.reporter);
			driver.findElement(By.partialLinkText(MaventPortalMenuConsts.LOG_OUT_LINK_PARTIAL_LINKTEXT)).click();
			EllieMaeLog.log(_log, "Log out is Successfull", EllieMaeLogLevel.reporter);
			CommonUtilityApplication.threadWait(2000);
		
		}
		
		
		EllieMaeLog.log(_log, "Mavent Portal Login test Ends",EllieMaeLogLevel.reporter);
	}

}
