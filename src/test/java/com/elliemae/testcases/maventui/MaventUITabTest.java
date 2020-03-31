package com.elliemae.testcases.maventui;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventSOPPage;
import com.elliemae.pageobject.MaventUIStaticTabVerify;
import com.elliemae.pageobject.MaventUIVerifyFooterPage;

public class MaventUITabTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventUITabTest.class);
	
	/* Author : Nidhi Khandelwal
	 * Description : This is automated test method for mavent UI page element Tab verification.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyMaventTab(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);	
		MaventUIStaticTabVerify verifyMaventTabStatic = new MaventUIStaticTabVerify(driver);
		MaventUIVerifyFooterPage maventVerifyFooterPage = new MaventUIVerifyFooterPage(driver); 
		
		
		EllieMaeLog.log(_log, "Tab Verification for Loan and TILHUD page starts...",EllieMaeLogLevel.reporter);
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "VerifyTab_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		String loggedUser = sampleSOPPage.loginToPortal(testData);
		Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
		Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		CommonUtilityApplication.takeScreenShot(testData, "VerifyTab_Login_Success", CommonUtility.currentTimeStamp);
		
		
		
		//Search Loan
		maventVerifyFooterPage.searchLoan(testData);
	
		try
		{
			verifyMaventTabStatic.verifyTabForLoanDataPage();
			verifyMaventTabStatic.verifyTabForTILHUDPage();				
			CommonUtilityApplication.takeScreenShot(testData, "VerifyTab_TILHUD_page", CommonUtility.currentTimeStamp);			
			System.out.println(CommonUtility.currentTimeStamp);
			verifyMaventTabStatic.verifyTabForMIBUYDOWNPage();
			verifyMaventTabStatic.verifyTabRespaPage(testData);
			verifyMaventTabStatic.VerifyAbilityToRepayQM();
			verifyMaventTabStatic.VerifynavigateToAdvanceSettingPage();
			
			
			
		
		}
		catch (Exception e)
		{
			CommonUtilityApplication.takeScreenShot(testData, "VerifyTab_Error", CommonUtility.currentTimeStamp);
			Assert.fail("Tab Verification Failed ", e);
		}
		finally
		{
			sampleSOPPage.logOut();
		}
		
		
		EllieMaeLog.log(_log, "Tab Verification for Loan and TILHUD page is successfullly completed", EllieMaeLogLevel.reporter);
	
		
	}

}
