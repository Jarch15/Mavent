package com.elliemae.testcases.maventui;

import java.awt.Robot;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventImportLoansPageConsts;
import com.elliemae.consts.MaventUIVerifyFooterConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MaventUIVerifyFooterPage;

public class MaventUIVerifyFooterTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventUIVerifyFooterTest.class);
	EllieMaeApplicationActions objEllieMaeActions;
	Calendar now = Calendar.getInstance();
	
	/* Author : Nidhi Khandelwal
	 * Description : This is automated test method for mavent Website test case,
	 * It captures all the links present on the webpage and navigate to each link.
	 * After navigation to each link it checks if footer text is present on each
	 * page.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyAllLinks(HashMap<String, String> testData) throws InterruptedException 
	{
		MaventUIVerifyFooterPage sampleSOPPage = new MaventUIVerifyFooterPage(driver);
		EllieMaeLog.log(_log, "Mavent Website test Starts ",EllieMaeLogLevel.reporter);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		
		// Navingating to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		System.out.println("titleAfterNavigation : "+titleAfterNavigation);
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
				
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyFooterTest_Login_Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
						
		qtpditemplatemanageFlow(testData);
		qtpmaventadminflow(testData);	
		superAdminFlow(testData);
				
	}
				
	
	public void qtpditemplatemanageFlow(HashMap<String, String> testData)
	{
		
		MaventUIVerifyFooterPage sampleSOPPage = new MaventUIVerifyFooterPage(driver);
		
		EllieMaeLog.log(_log, "Mavent Website test Starts for qtp.superadmin user",EllieMaeLogLevel.reporter);	
				
		// Loging to the Mavent Portal
		String loginUserName = sampleSOPPage.loginToPortal(testData,testData.get("qtp.ditemplateUserName"));
		
		System.out.println(loginUserName);
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIVerifyFooterTest_Login_Page_Success", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
				
		String Username = sampleSOPPage.getloggedUser(testData);
		
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_REPORTS)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_ACTIVITY)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.IMPORT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CREATE_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CREATELOAN_LINK)).click();	
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CREATE_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CREATEPOOL_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.SEARCH_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.SEARCH_LOAN_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);		
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.SEARCH_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.SEARCH_BATCH_REVIEW_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.QTM_USER_ADMINISTRATION_LINK)).click();
	isTextPresent(driver.getTitle(),testData);
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMPLATES_LINK)).click();
	isTextPresent(driver.getTitle(),testData);	
	CommonUtilityApplication.threadWait(7000);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.QTM_USER_ADMINISTRATION_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.RESET_PASSWORD_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.HELP_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	CommonUtilityApplication.threadWait(5000);
	isTextPresent(driver.getTitle(),testData);
	//driver.findElement(By.xpath(WebsiteConsts.CONTACTUS_LINK)).click();	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_REPORTS)).click();
	CommonUtilityApplication.threadWait(5000);
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_ACTIVITY)).click();
	CommonUtilityApplication.threadWait(5000);
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);	
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_LOANID)).sendKeys(testData.get("Temp_User_LoanId"));
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_SEARCH_BTN)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	CommonUtilityApplication.threadWait(5000);
	isTextPresent(driver.getTitle(),testData);
		
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_REPORTS)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_SUMMARY)).click();
	CommonUtilityApplication.threadWait(5000);
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	
		
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_FROMRECEIVEDATE_BTN)).sendKeys(testData.get("FromReceiveDate"));
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_TORECEIVEDATE_BTN)).sendKeys(testData.get("ToReceiveDate"));
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMP_USER_SEARCH_BTN)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
				
	CommonUtilityApplication.threadWait(5000);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	CommonUtilityApplication.threadWait(2000);
	isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DIRECT_INPUT_IMPORT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	CommonUtilityApplication.threadWait(5000);
	isTextPresent(driver.getTitle(),testData);
	
	
	//driver.findElement(By.xpath(WebsiteConsts.DATAFILE_BTN)).click();
	
	
	EllieMaeLog.log(_log, "importing loan file...", EllieMaeLogLevel.reporter);

    File f = new File("");

    String inputDirectoryPath = f.getAbsolutePath();

    inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"input";

    String importFile= inputDirectoryPath + File.separator +"QTP-Import.xml";

    /* Handle Windows Pop up for Browse before */
	handleWindowsPopUp();
    driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).sendKeys(importFile);
    //CommonUtilityApplication.threadWait(3000);
    
    if(!driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).getText().equals(importFile))
    {
    	driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).sendKeys(importFile);
    	CommonUtilityApplication.threadWait(3000);
    }

    CommonUtilityApplication.threadWait(1000);  

    Select dropDown= new Select(driver.findElement(By.name(MaventUIVerifyFooterConsts.FILEFORMAT)));

    dropDown.selectByVisibleText(testData.get("ImportLoan_FileType"));

    CommonUtilityApplication.threadWait(1000);  

    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.IMPORTLOAN_CONTINUE_BTN)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);

    CommonUtilityApplication.threadWait(10000);
    driver.findElement(By.partialLinkText(MaventUIVerifyFooterConsts.LOANPOOL_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(2000);
    isTextPresent(driver.getTitle(),testData);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOANPOOL_DONE_BTN)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(2000);
    isTextPresent(driver.getTitle(),testData);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOANBATCH_LIST_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.BATCHLOAD_RESULT_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(2000);
    isTextPresent(driver.getTitle(),testData);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.GETLOANGROUP_CONTENT_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOANDATA_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
        
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.COMMENT_TAB_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(2000);
    isTextPresent(driver.getTitle(),testData); 
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.QTM_USER_ADMINISTRATION_LINK)).click(); 
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TEMPLATES_LINK)).click();
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(60000);
   // isTextPresent(driver.getTitle(),testData);
    driver.findElement(By.xpath(MaventUIVerifyFooterConsts.QTPTEMPLATE_LINK)).click();  
    CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
    CommonUtilityApplication.threadWait(10000);
    isTextPresent(driver.getTitle(),testData);
	driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOGOUT_LINK)).click();
	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	CommonUtilityApplication.threadWait(2000);
	isTextPresent(driver.getTitle(),testData);
	
	}
	
	
	public void qtpmaventadminflow(HashMap<String, String> testData)
	{
		MaventUIVerifyFooterPage sampleSOPPage = new MaventUIVerifyFooterPage(driver);
		
		EllieMaeLog.log(_log, "Mavent Website test Starts for qtp.maventadmin user",EllieMaeLogLevel.reporter);		
		
		// Loging to the Mavent Portal
		String loginUserName = sampleSOPPage.loginToPortal(testData,testData.get("qtp.maventUserName"));
		
		System.out.println(loginUserName);
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "WebsiteTest_Login_Success", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
			
		
		String Username = sampleSOPPage.getloggedUser(testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_REPORTS)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SYSTEM)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SERVICEPERFORMANCE)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_FROMRECEIVEDATE)).sendKeys(testData.get("FromReceiveDate"));
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_TORECEIVEDATE)).sendKeys(testData.get("ToReceiveDate"));
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_SEARCH_BTN)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMINISTATION)).click();	
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_LoanPError)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMINISTATION)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTOMERSETUP)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);			
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_NEW_BTN)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_CANCEL_BTN)).click();
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_UPDATE_BTN)).click();
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_CUSTSETUP_UPDATE_VIEWUSER_BTN)).click();
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_CUSTSETUP_LASTNAME)).sendKeys("UserDI");
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_CUSTSETUP_FIRSTNAME)).sendKeys("QTP");
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_SERVICEPERFORMANCE_SEARCH_BTN)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMINISTATION)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTOMERSETUP)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_UPDATE_BTN)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_LOCATIONSUSER_LINK)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAVENT_ADMIN_ADMIN_CUSTSETUP_NEW_BTN)).click();
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_CRETENEWLOC_CANCEL_BTN)).click();	
		CommonUtilityApplication.threadWait(2000);
		//CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_HELP_LINK)).click();
		CommonUtilityApplication.threadWait(2000);
	//	CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MA_ADMIN_HELP_ABOUT)).click();	
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOGOUT_LINK)).click();
		
		
		
		
		
	}
	
	
	public void superAdminFlow(HashMap<String, String> testData)
	{
		MaventUIVerifyFooterPage sampleSOPPage = new MaventUIVerifyFooterPage(driver);
		
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
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.JURIDICTION_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		Select dropDown = new Select(driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAINTAIN_JURIDICTION)));		
		dropDown.selectByValue(testData.get("SA_User_Juridiction_Dropd"));	
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADMINISTRATION_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAINTAIN_CRA_PROFILE)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CREATE_NEW)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADMINISTRATION_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.MAINTAIN_CRA_PROFILE)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LICENSE_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LICENSE_SEARCH)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LICENSE_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_LICENSE)).click();
		CommonUtilityApplication.threadWait(3000);
		isTextPresent(driver.getTitle(),testData);
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LICENSE_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LICENSE_SEARCH)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_EXPDATE_FROM)).sendKeys(testData.get("FromReceiveDate"));
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_EXPDATE_TO)).sendKeys(testData.get("ToReceiveDate"));
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LISENCE_SEARCH_BTN)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CUSTOMER_PROFILE)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);	
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LIST_CUSTOMER_PROFILE)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		
		isTextPresent(driver.getTitle(),testData);
		
		CommonUtilityApplication.threadWait(2000);
		
		Select CUSTID_DROPDOWN = new Select(driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CUSTID_DROPDOWN)));
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		CUSTID_DROPDOWN.selectByValue(testData.get("SA_CusID_DropDown"));
		CommonUtilityApplication.threadWait(5000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.CP_ADD)).click();
		CommonUtilityApplication.threadWait(10000);
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		
		//String showLenderEntity =	driver.getWindowHandle();
		
		//driver.switchTo().window("showLenderEntity");
				
		//driver.findElement(By.xpath(WebsiteConsts.ALERT_CANCEL_BTN)).click();
				
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.AGENCY_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADD_AGENCY)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.AGENCY_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_AGENCY)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TAG_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADD_TAG)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.TAG_LINK)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLA_TAG)).click();
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.RULE_LINK)).click();
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(3000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADD_RULE)).click();
		//isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.RULE_LINK)).click();
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_RULE)).click();
		//isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.RULE_HEADER_LINK)).click();
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.ADD_RULE_HEADER)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
	
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.RULE_HEADER_LINK)).click();
		CommonUtilityApplication.threadWait(5000);
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.DISPLAY_RULE_HEADER)).click();
		CommonUtilityApplication.takeScreenShot(testData, driver.getTitle() + "Page", CommonUtility.currentTimeStamp);
		CommonUtilityApplication.threadWait(2000);
		isTextPresent(driver.getTitle(),testData);
		
		driver.findElement(By.xpath(MaventUIVerifyFooterConsts.LOGOUT_LINK)).click();
		isTextPresent(driver.getTitle(),testData);
		
	}
	
	
	
	private void isTextPresent(String webPageTitle,HashMap<String, String> testData)
	{
		try
		{	
			String bodyText = driver.findElement(By.tagName("body")).getText();
			
			int currentDate = now.get(Calendar.YEAR);	
			String currentDate1 =String.valueOf(currentDate).trim();
			//System.out.println("Current Year is : " + now.get(Calendar.YEAR));
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart0")),"Text not found!");
			Assert.assertTrue(bodyText.contains(currentDate1),"Text not found!");
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart1")),"Text not found!");
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart2")),"Text not found!");
			Assert.assertTrue(bodyText.contains(testData.get("FooterTextPart3")),"Text not found!");
			//logger.log(LogStatus.PASS, "Footer text verified on "+webPageTitle);
			//System.out.println(driver.getCurrentUrl() +  "  " + "Footer text verified");	
			
			EllieMaeLog.log(_log,  "  " ,EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log, webPageTitle +  "  " + "Footer text verified",EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log,  "  " ,EllieMaeLogLevel.reporter);
		}
		
		catch (Exception e)
		{
			System.out.println("Exception occurred for URL : "+driver.getCurrentUrl());
		}
	
	}
	
	// Function to perform Threaded keyboard Events to handle Windows pop up for file upload 
	private void handleWindowsPopUp() {

		Runnable r = new Runnable() {

			public void run() {

				try 
				{
					Robot rb = new Robot();
					rb.delay(7000);
					// tab two time to go to save button
					rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
					rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
					rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
					rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
					rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
					rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
			
					
					// press enter
					rb.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					rb.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					
	
				} 
				catch (Exception e) 
				{
					EllieMaeLog.log(_log, "Exception occurred during handling windows pop up for import a file : "+e.getMessage(), EllieMaeLogLevel.reporter);
					//throw e;
				}

			}
		};

		// Wake up the thread and perform the operation
		Thread t = new Thread(r);
		t.start();

    }
	
	
}


