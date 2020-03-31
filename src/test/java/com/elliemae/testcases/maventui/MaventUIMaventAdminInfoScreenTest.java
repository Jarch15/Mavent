package com.elliemae.testcases.maventui;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventAdminInfoPage;
import com.elliemae.pageobject.MaventSOPPage;


public class MaventUIMaventAdminInfoScreenTest extends EllieMaeApplicationBase {
	
	public static Logger _log = Logger.getLogger(MaventUIRegressionTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent UI Regression test case.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyMaventAdminInfoScreen(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		
		// Navigate to the Mavent Portal
				String titleAfterNavigation = sampleSOPPage.navigateToPortal();
				Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		
				CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_Login_Page", CommonUtility.currentTimeStamp);
				
				// Login to the Mavent Portal
				String loggedUser = sampleSOPPage.loginToPortal(testData);
				Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
				Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
				CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_Login_Success", CommonUtility.currentTimeStamp);
				
				//Navigate to the Mavent Admin Info Page
				
				MaventAdminInfoPage adminpage = new MaventAdminInfoPage(driver);
				
				String Pagetitle = adminpage.gotoAdminInfoScreenPage();
				
				Assert.assertEquals(Pagetitle, "Setup Home", "Navigation to the Customer Set up Page failed");
				
				//Select test company from the Company Selection Box
				
				CommonUtilityApplication.threadWait(1000);
				adminpage.setCompanyName("Test Company");
				
				CommonUtilityApplication.threadWait(10000);
				
				//Verify the Info Page is loaded by default
				
				String defaultLocTxt;
				defaultLocTxt = driver.findElement(By.className(MaventPortalMenuConsts.LBL_DEFAULT)).getText();
				Assert.assertEquals(defaultLocTxt, "Info", "The default Page");	
				
				//Verify Fields on Info Page
				
				defaultLocTxt = "1234567890";
				
				adminpage.verifyInfoPage(defaultLocTxt);
				
				
	}
	

}
