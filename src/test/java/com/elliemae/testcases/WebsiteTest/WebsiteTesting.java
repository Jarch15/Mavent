package com.elliemae.testcases.WebsiteTest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventTILHUDPageConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

public class WebsiteTesting extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(WebsiteTesting.class);
	EllieMaeApplicationActions objEllieMaeActions;
	
	
	/* Author : Nidhi Khandelwal
	 * Description : This is automated test method for mavent Website test case,
	 * It captures all the links present on the webpage and navigate to each link.
	 * After navigation to each link it checks if footer text is present on each
	 * page.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyAllLinks(HashMap<String, String> testData) throws InterruptedException 
	{
		WebsiteUtility sampleSOPPage = new WebsiteUtility(driver);
		EllieMaeLog.log(_log, "Mavent Website test Starts ",EllieMaeLogLevel.reporter);
		
		// Navingating to the Mavent Portal
				String titleAfterNavigation = sampleSOPPage.navigateToPortal();
				System.out.println("titleAfterNavigation : "+titleAfterNavigation);
				Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
				
				CommonUtilityApplication.takeScreenShot(testData, "WebsiteTest_Login_Page", CommonUtility.currentTimeStamp);
				isTextPresent(driver.getTitle(),testData);
				
		
		 qtpditemplatemanageFlow(testData);
		//qtpmaventadminflow(testData);	
		//superAdminFlow(testData);
				
	}
				
	
	public void qtpditemplatemanageFlow(HashMap<String, String> testData)
	{
		
		WebsiteUtility sampleSOPPage = new WebsiteUtility(driver);
		
		EllieMaeLog.log(_log, "Mavent Website test Starts for qtp.superadmin user",EllieMaeLogLevel.reporter);	
				
		// Loging to the Mavent Portal
		String loginUserName = sampleSOPPage.loginToPortal(testData,testData.get("qtp.ditemplateUserName"));
		
		System.out.println(loginUserName);
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "WebsiteTest_Login_Success", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
				
		String Username = sampleSOPPage.getloggedUser(testData);
		
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_REPORTS)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_ACTIVITY)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.IMPORT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.CREATE_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.CREATELOAN_LINK)).click();	
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.CREATE_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.CREATEPOOL_LINK)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.SEARCH_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.SEARCH_LOAN_LINK)).click();
	isTextPresent(driver.getTitle(),testData);		
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.SEARCH_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.SEARCH_BATCH_REVIEW_LINK)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.QTM_USER_ADMINISTRATION_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMPLATES_LINK)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.QTM_USER_ADMINISTRATION_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.RESET_PASSWORD_LINK)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.HELP_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	//driver.findElement(By.xpath(WebsiteConsts.CONTACTUS_LINK)).click();	
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_REPORTS)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_ACTIVITY)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_LOANID)).sendKeys(testData.get("Temp_User_LoanId"));
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_SEARCH_BTN)).click();
	isTextPresent(driver.getTitle(),testData);
	CommonUtilityApplication.threadWait(5000);	
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_REPORTS)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_SUMMARY)).click();
	isTextPresent(driver.getTitle(),testData);
	CommonUtilityApplication.threadWait(5000);	
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_FROMRECEIVEDATE_BTN)).sendKeys(testData.get("FromReceiveDate"));
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_TORECEIVEDATE_BTN)).sendKeys(testData.get("ToReceiveDate"));
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.TEMP_USER_SEARCH_BTN)).click();
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(WebsiteConsts.DIRECT_INPUT_IMPORT_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	CommonUtilityApplication.threadWait(5000);
	
	//driver.findElement(By.xpath(WebsiteConsts.DATAFILE_BTN)).click();
	
	
	EllieMaeLog.log(_log, "importing loan file...", EllieMaeLogLevel.reporter);

    File f = new File("");

    String inputDirectoryPath = f.getAbsolutePath();

    inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"input";

    String importFile= inputDirectoryPath + File.separator +"QTP-Import.xml";

     driver.findElement(By.xpath("//input[@name='batchFile']")).sendKeys(importFile);

    CommonUtilityApplication.threadWait(1000);  

    Select dropDown= new Select(driver.findElement(By.name("fileFormat")));

    dropDown.selectByVisibleText("Mavent Loan XML");

    CommonUtilityApplication.threadWait(1000);  

    driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();

    CommonUtilityApplication.threadWait(10000);
	
	driver.findElement(By.xpath(WebsiteConsts.LOGOUT_LINK)).click();
	}
	
	
	public void qtpmaventadminflow(HashMap<String, String> testData)
	{
		WebsiteUtility sampleSOPPage = new WebsiteUtility(driver);
		
		EllieMaeLog.log(_log, "Mavent Website test Starts for qtp.maventadmin user",EllieMaeLogLevel.reporter);		
		
		// Loging to the Mavent Portal
		String loginUserName = sampleSOPPage.loginToPortal(testData,testData.get("qtp.maventUserName"));
		
		System.out.println(loginUserName);
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "WebsiteTest_Login_Success", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
			
		
		String Username = sampleSOPPage.getloggedUser(testData);
		
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_REPORTS)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SYSTEM)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SERVICEPERFORMANCE)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_FROMRECEIVEDATE)).sendKeys(testData.get("FromReceiveDate"));
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_TORECEIVEDATE)).sendKeys(testData.get("ToReceiveDate"));
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_SEARCH_BTN)).click();
		isTextPresent(driver.getTitle(),testData);		
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMINISTATION)).click();	
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_LoanPError)).click();
		isTextPresent(driver.getTitle(),testData);		
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMINISTATION)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTOMERSETUP)).click();
		isTextPresent(driver.getTitle(),testData);			
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_NEW_BTN)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_CANCEL_BTN)).click();
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_UPDATE_BTN)).click();
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_CUSTSETUP_UPDATE_VIEWUSER_BTN)).click();
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_CUSTSETUP_LASTNAME)).sendKeys("UserDI");
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_CUSTSETUP_FIRSTNAME)).sendKeys("QTP");
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_SEARCH_BTN)).click();		
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMINISTATION)).click();
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTOMERSETUP)).click();	
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_UPDATE_BTN)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_LOCATIONSUSER_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_NEW_BTN)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_CRETENEWLOC_CANCEL_BTN)).click();	
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_HELP_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.MA_ADMIN_HELP_ABOUT)).click();
		driver.findElement(By.xpath(WebsiteConsts.LOGOUT_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		
		
		
		
		
	}
	
	
	public void superAdminFlow(HashMap<String, String> testData)
	{
		WebsiteUtility sampleSOPPage = new WebsiteUtility(driver);
		
		EllieMaeLog.log(_log, "Mavent Website test Starts for qtpditemplate.manage user",EllieMaeLogLevel.reporter);		
		
		
		// Loging to the Mavent Portal
		String loginUserName = sampleSOPPage.loginToPortal(testData,testData.get("qtp.superUserName"));
		//Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
		//Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
		System.out.println(loginUserName);
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "WebsiteTest_Login_Success", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		
		
		
		String Username = sampleSOPPage.getloggedUser(testData);
		
		driver.findElement(By.xpath(WebsiteConsts.JURIDICTION_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		Select dropDown = new Select(driver.findElement(By.xpath(WebsiteConsts.MAINTAIN_JURIDICTION)));
		
		dropDown.selectByValue(testData.get("SA_User_Juridiction_Dropd"));	
		
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(WebsiteConsts.ADMINISTRATION_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		CommonUtilityApplication.threadWait(3000);
		
		driver.findElement(By.xpath(WebsiteConsts.MAINTAIN_CRA_PROFILE)).click();
		driver.findElement(By.xpath(WebsiteConsts.CREATE_NEW)).click();
		
		driver.findElement(By.xpath(WebsiteConsts.ADMINISTRATION_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.MAINTAIN_CRA_PROFILE)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.LICENSE_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(WebsiteConsts.LICENSE_SEARCH)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.LICENSE_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_LICENSE)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.LICENSE_LINK)).click();
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(WebsiteConsts.LICENSE_SEARCH)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_EXPDATE_FROM)).sendKeys(testData.get("FromReceiveDate"));
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_EXPDATE_TO)).sendKeys(testData.get("ToReceiveDate"));
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.LISENCE_SEARCH_BTN)).click();
		
		driver.findElement(By.xpath(WebsiteConsts.CUSTOMER_PROFILE)).click();
		isTextPresent(driver.getTitle(),testData);	
		driver.findElement(By.xpath(WebsiteConsts.LIST_CUSTOMER_PROFILE)).click();
		isTextPresent(driver.getTitle(),testData);
		
		Select CUSTID_DROPDOWN = new Select(driver.findElement(By.xpath(WebsiteConsts.CUSTID_DROPDOWN)));
		isTextPresent(driver.getTitle(),testData);
		CUSTID_DROPDOWN.selectByValue(testData.get("SA_CusID_DropDown"));
		
		
		CommonUtilityApplication.threadWait(5000);		
		driver.findElement(By.xpath(WebsiteConsts.CP_ADD)).click();
		isTextPresent(driver.getTitle(),testData);
		
		//String showLenderEntity =	driver.getWindowHandle();
		
		//driver.switchTo().window("showLenderEntity");
				
		//driver.findElement(By.xpath(WebsiteConsts.ALERT_CANCEL_BTN)).click();
				
		
		
		driver.findElement(By.xpath(WebsiteConsts.AGENCY_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.ADD_AGENCY)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.AGENCY_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_AGENCY)).click();
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(WebsiteConsts.TAG_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.ADD_TAG)).click();
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(WebsiteConsts.TAG_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.DISPLA_TAG)).click();
		isTextPresent(driver.getTitle(),testData);
		
		
		//driver.findElement(By.xpath(WebsiteConsts.TAG_LINK)).click();
		//driver.findElement(By.xpath(WebsiteConsts.DISPLA_TAG)).click();
		
		
		driver.findElement(By.xpath(WebsiteConsts.RULE_LINK)).click();
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(WebsiteConsts.ADD_RULE)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.RULE_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_RULE)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.RULE_HEADER_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.ADD_RULE_HEADER)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.RULE_HEADER_LINK)).click();
		driver.findElement(By.xpath(WebsiteConsts.DISPLAY_RULE_HEADER)).click();
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(WebsiteConsts.LOGOUT_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		
	}
	
	
	
	private void isTextPresent(String webPageTitle,HashMap<String, String> testData)
	{
		try
		{
			String bodyText = driver.findElement(By.tagName("body")).getText();
			//logger.log(LogStatus.INFO, "Verify if footer text present on "+webPageTitle);
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart1")),"Text not found!");
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart2")),"Text not found!");
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart3")),"Text not found!");
			//logger.log(LogStatus.PASS, "Footer text verified on "+webPageTitle);
			System.out.println("Footer text verified");
		
		}
		
		catch (Exception e)
		{
			System.out.println("Exception occurred for URL : "+driver.getCurrentUrl());
		}
	}

	 
	
}


