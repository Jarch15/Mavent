package com.elliemae.pageobject;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;

/**
 * <b>Name:</b> SampleSOPPage</br>
 * <b>Description: </b>This page object class is used for Mavent Portal SOP Test Case.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventSOPPage {

	public static Logger _log = Logger.getLogger(MaventSOPPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	
	String LOAN_ID;

	public String getLOAN_ID() {
		return LOAN_ID;
	}

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventSOPPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
		this.LOAN_ID=generateLoanID();
	}
	
	// Generate an unique Loan ID
	public String generateLoanID()
	{
		Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmss");
        String datetime = ft.format(dNow);
        //String LOAN_ID = "LOAN_ID_"+datetime+CommonUtilityApplication.generateRandomNumber();//SmokeTestReverseProxy_<TimeStamp>
        String LOAN_ID = "ReverseProxy_"+datetime;
		EllieMaeLog.log(_log, "Generated Loan ID : "+LOAN_ID, EllieMaeLogLevel.reporter);
		return LOAN_ID;
	}

	
	/* Author : Jayesh Bhapkar
	 * Description : This method navigates to the configured Mavent Portal.
	 * It returns the Title of the webpage back to the caller after navigation is complete
	 *  
	 *  */
	public String navigateToPortal() {

		EllieMaeLog.log(_log, "Navigating to the Mavent URL", EllieMaeLogLevel.reporter);

		try
		{
			driver.get(envData.get("MAVENTURL"));		
			objEllieMaeActions.waitForPageToLoad("5000");
		}
		catch(Exception e)
		{
			//EllieMaeLog.log(_log, "Navigating to the Mavent URL Failed", EllieMaeLogLevel.reporter);
			//e.printStackTrace();
		}

		EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.threadWait(3000);
		return driver.getTitle();
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does login of configured user into the Mavent Portal.
	 * It returns back the logged in user name back to the caller.
	 *  
	 *  */
	public String loginToPortal(HashMap<String, String> testData) {

		CommonUtilityApplication.threadWait(3000);
		EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitUntilVisibilityOfElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD),30);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).clear();
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).sendKeys(userData.get("UserName"));
		//driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).sendKeys(testData.get("LOGIN_USER_ID"));
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).clear();
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(userData.get("Password"));
		//driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(testData.get("LOGIN_PWD"));
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).clear();
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(userData.get("Company"));
		//driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(testData.get("LOGIN_COMPANY"));
		CommonUtilityApplication.threadWait(1000);
		
		WebElement element = driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN));
		//System.out.println("element.isDisplayed() : "+element.isDisplayed());
		//System.out.println("element.isEnabled() : "+element.isEnabled());
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN)).click();
		//System.out.println("Login Button Clicked");

		objEllieMaeActions.waitForPageToLoad("30000");
		CommonUtilityApplication.threadWait(10000);
		
		//System.out.println("Title : "+driver.getTitle());
		//System.out.println("Page Source : "+driver.getPageSource());
		String loggedUser = null;
		loggedUser = driver.findElement(By.xpath(MaventPortalMenuConsts.LOGGED_USER_XPATH)).getText();
		EllieMaeLog.log(_log, "Logged user : "+loggedUser, EllieMaeLogLevel.reporter);
		return loggedUser;

	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does logout of the current user from the Mavent Portal.
	 * It returns the webpage title back to the caller once it successfully logs out.
	 *  */
	public String logOut() {
		/* Click on Log out link */
		EllieMaeLog.log(_log, "Logging out of the portal", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventPortalMenuConsts.LOG_OUT_LINK_PARTIAL_LINKTEXT)).click();
		EllieMaeLog.log(_log, "Log out is Successfull", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return driver.getTitle();
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method searches a loan by providing loan id and todays date.
	 * It returns back the loan ID back to the caller after successful search.
	 *  
	 *  */
	public String loanSearch(HashMap<String, String> testData) {
		EllieMaeLog.log(_log, "Loan Search", EllieMaeLogLevel.reporter);

		/* Select Loan Search Menu */
		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH)).click();

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_SEARCH_LOAN)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);

		/* On Loan Search page populate Loan ID and Create Date From */

		driver.findElement(By.name(MaventPortalMenuConsts.LOAN_SEARCH_ID_TEXTBOX_NAME))
				.sendKeys(LOAN_ID);

		driver.findElement(By.name(MaventPortalMenuConsts.LOAN_SEARCH_BUTTON_NAME)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_SearchLoan", CommonUtility.currentTimeStamp);

		/* On Loan List Page select first record */
		//driver.findElement(By.partialLinkText(LOAN_ID)).click();
		driver.findElement(By.xpath(MaventPortalMenuConsts.LOAN_ID_SEARCH_RESULT_XPATH)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);

		String loanIdName = driver.findElement(By.xpath(MaventPortalMenuConsts.LOAN_ID_NAME_LABEL_XPATH)).getText();
		System.out.println("Loan ID copied after loan search : " + loanIdName);
		EllieMaeLog.log(_log, "Loan ID copied after loan search : "+loanIdName, EllieMaeLogLevel.reporter);

		EllieMaeLog.log(_log, "Loan Search complete", EllieMaeLogLevel.reporter);

		return loanIdName;
		

	}

	/* Author : Jayesh Bhapkar
	 * Description : This method navigates to create loan page and then to loan data page,
	 * populates required fields for loan creation.
	 * It returns true once it populates all the required data for loan creation.
	 *  */
	public boolean createLoan(HashMap<String, String> testData) {

		CommonUtilityApplication.threadWait(3000);
		/* Direct Input -> Create -> Loan */

		EllieMaeLog.log(_log, "Create Loan Page Start...", EllieMaeLogLevel.reporter);

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_DIRECT_INPUT)).click();

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE)).click();

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_CREATE_LOAN)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);

		/*
		 * On Create Loan Page enter Loan id and select values from dropdown
		 * boxes
		 */
		System.out.println("Loan ID : "+LOAN_ID);
		//WebElement webElement = driver.findElement(By.name(MaventPortalMenuConsts.LOANID_TEXTBOX_NAME));
		WebElement webElement = driver.findElement(By.xpath(MaventPortalMenuConsts.LOANID_TEXTBOX_XPATH));
		CommonUtilityApplication.threadWait(1000);
		webElement.clear();
		webElement.sendKeys(LOAN_ID);
		
		Select dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.LOCATION_DROP_DOWN_NAME)));
		dropDown.selectByVisibleText(testData.get("LOCATION"));
		
		//objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);

		dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.POOL_DROP_DOWN_NAME)));
		//dropDown.selectByVisibleText(testData.get("POOL"));
		dropDown.selectByIndex(0);

		dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.TEMPLATE_DROP_DOWN_NAME)));
		//dropDown.selectByVisibleText(testData.get("TEMPLATE"));
		dropDown.selectByIndex(0);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_CreateLoanPage", CommonUtility.currentTimeStamp);

		driver.findElement(By.cssSelector(MaventPortalMenuConsts.CONTINUE_BUTTON_CSS)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(5000);

		EllieMaeLog.log(_log, "Create Loan Page Complete", EllieMaeLogLevel.reporter);

		return true;

	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does a loan review and return the title of the webpage back after loan review is completed.
	 *  
	 *  */
	public String reviewLoan() 
	{
		EllieMaeLog.log(_log, "Loan review Starts", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.REVIEW_BUTTON_XPATH),30);
		driver.findElement(By.xpath(MaventPortalMenuConsts.REVIEW_BUTTON_XPATH)).click();
		//objEllieMaeActions.waitForPageToLoad("50000");
		CommonUtilityApplication.threadWait(30000);			
		EllieMaeLog.log(_log, "Loan review Ends", EllieMaeLogLevel.reporter);
		return driver.getTitle();
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does an activity report by searching a loan.
	 * It returns the loan ID back to caller.
	 *  
	 *  */
	public String reportsActivity(HashMap<String, String> testData) {
		EllieMaeLog.log(_log, "Reports Activity", EllieMaeLogLevel.reporter);	

		/* Select Reports-> Activity Menu */
		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();

		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_ACTIVITY)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(2000);

		/* On Review Search page populate loan id, receive date from */
		driver.findElement(By.name(MaventPortalMenuConsts.REVIEW_SEARCH_ID_TEXTBOX_NAME))
				.sendKeys(LOAN_ID);
		driver.findElement(By.xpath(MaventPortalMenuConsts.REVIEW_SEARCH_BUTTON_XPATH)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		//Assert.assertEquals(driver.getTitle(), "Activity Reports - Transaction List");
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_ReportsActivity", CommonUtility.currentTimeStamp);

		/* On Loan List Page select first record */
		//driver.findElement(By.partialLinkText(LOAN_ID)).click();
		driver.findElement(By.xpath(MaventPortalMenuConsts.REVIEW_LOAN_ID_SEARCH_RESULT_XPATH)).click();
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(2000);	
		
		/* Switch to Frame*/
		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		driver.switchTo().frame(MaventPortalMenuConsts.FRAME);
		String loanIdName = driver.findElement(By.xpath(MaventPortalMenuConsts.REVIEW_SUMMARY_REPORT_LOANID_LABEL_XPATH)).getText();
		System.out.println("Loan ID copied from review summary Report : " + loanIdName);
		EllieMaeLog.log(_log, "Loan ID copied from review summary Report : "+loanIdName, EllieMaeLogLevel.reporter);
		
		/* Switch back to main window from frame */
		driver.switchTo().window(parentWindowHandler);

		EllieMaeLog.log(_log, "Reports Activity Complete", EllieMaeLogLevel.reporter);
		
		return loanIdName;
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does a view PDF and takes screenshot of the PDF.
	 *  
	 *  */
	public void viewPDF(HashMap<String, String> testData, String pageName) {
		
		String parentWindowHandler = driver.getWindowHandle(); // Store your
		// parent window
		try
		{		
		EllieMaeLog.log(_log, "View PDF Starts", EllieMaeLogLevel.reporter);
		/* Switch to Frame*/
		driver.switchTo().frame(MaventPortalMenuConsts.FRAME);
		driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH)).click();
		CommonUtilityApplication.threadWait(5000);

		// switch to pop up window
		String subWindowHandler = null;

		Set<String> handles = driver.getWindowHandles(); // get all window
															// handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window

		/* Read the PDF Contents and check if Loan ID is present inside PDF */
		String pdfURL = driver.getCurrentUrl();
		System.out.println("PDF URL : " + pdfURL);
		//Assert.assertEquals(pdfURL.contains(LOAN_ID), true);
		
//		String pdfContents = getPDFContents(pdfURL);
//		if (pdfContents != null) {
//			System.out.println("PDF Contents : " + pdfContents);
//			//Assert.assertEquals(true, pdfContents.contains(LOAN_ID));
//		}

		/* Take Screenshot*/
		try
		{
			//CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_viewPDF", CommonUtility.currentTimeStamp);
		}
		catch (Exception exception)
		{
		}
		driver.switchTo().window(parentWindowHandler); // switch back to parent
														// window
		EllieMaeLog.log(_log, "View PDF Ends", EllieMaeLogLevel.reporter);
		}
		catch (Exception exception)
		{
			driver.switchTo().window(parentWindowHandler); // switch back to parent
			// window
		}
		
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method does a view PDF, takes screenshot of the PDF,
	 * saves the PDF file and read its contents.
	 *  
	 *  */
	public String viewPDFAndReturnPDFContent(HashMap<String, String> testData) {
		
		String parentWindowHandler = driver.getWindowHandle(); // Store parent window
		String pdfFileContent="";
		
		try
		{		
		EllieMaeLog.log(_log, "PDF Validation Starts", EllieMaeLogLevel.reporter);
		/* Switch to Frame*/
		driver.switchTo().frame(MaventPortalMenuConsts.FRAME);
		driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH)).click();
		CommonUtilityApplication.threadWait(3000);

		// switch to pop up window
		for(String newWindow : driver.getWindowHandles())
		{
			if(!newWindow.equals(parentWindowHandler))
			{
				driver.switchTo().window(newWindow);
			}
		}
		
		if(driver.getTitle().equalsIgnoreCase("Certificate Error: Navigation Blocked"))
		{
			driver.get("javascript:document.getElementById('overridelink').click();");
		}

		String pdfURL = driver.getCurrentUrl();
		EllieMaeLog.log(_log, "PDF URL : "+pdfURL, EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.threadWait(5000);

		/* Take Screenshot*/
		try
		{
			CommonUtilityApplication.takeScreenShot(testData, "MaventPortalTest_viewPDF", CommonUtility.currentTimeStamp);
		}
		catch (Exception exception)
		{
		}
		
		/* Save PDF File on local */
		EllieMaeLog.log(_log, "Saving PDF File on input directory...", EllieMaeLogLevel.reporter);
		File f = new File("");
		String inputDirectoryPath = f.getAbsolutePath();
		inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"input";
		String pdfFile= inputDirectoryPath + File.separator +CommonUtility.currentTimeStamp+"-LoanReview.pdf";
		
		Robot rb = new Robot();

		StringSelection pdfFilePath = new StringSelection(pdfFile);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pdfFilePath, null);
		
		// ctrl + Shift + s to open windows dialogue
		rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
		rb.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
		rb.keyPress(java.awt.event.KeyEvent.VK_S);
		rb.keyRelease(java.awt.event.KeyEvent.VK_S);
		rb.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
		rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
		
		CommonUtilityApplication.threadWait(1000);
		
		// ctrl + v to paste pdfFilePath
		rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
		rb.keyPress(java.awt.event.KeyEvent.VK_V);
		rb.keyRelease(java.awt.event.KeyEvent.VK_V);
		rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

		CommonUtilityApplication.threadWait(1000);
		
		// tab two time to go to save button
		rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
		rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
		rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
		rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);

		CommonUtilityApplication.threadWait(1000);
		
		// press enter
		rb.keyPress(java.awt.event.KeyEvent.VK_ENTER);
		rb.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
		
		CommonUtilityApplication.threadWait(2000);		
		
		EllieMaeLog.log(_log, "Reading PDF File Contents...", EllieMaeLogLevel.reporter);
		
		/* Read contents of saved PDF file */ 
		pdfFileContent = CommonUtilityApplication.getPDFContents(pdfFile);
	      
		EllieMaeLog.log(_log, "Copying PDF File to output directory...", EllieMaeLogLevel.reporter);
	    /* Copy PDF File to output folder on shared path */
	      String outputFolderPath=testData.get("Output_File_Path")+"/"+CommonUtility.currentTimeStamp;
	      if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				// Copying File Locally
				CommonUtility.copyFilesOrFolder(pdfFile, outputFolderPath, FileType.FILE);
			}
			else
			{
				// Copying File to network
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),pdfFile,outputFolderPath,CommonUtility.currentTimeStamp+"-LoanReview.pdf");
			}
		
		/* Delete PDF file from local */
	      EllieMaeLog.log(_log, "Deleting PDF file from local input directory...", EllieMaeLogLevel.reporter);
	      CommonUtility.deleteFile(CommonUtility.currentTimeStamp+"-LoanReview.pdf", "input");
		
		/* Close the PDF window */
		if(!driver.getWindowHandle().equals(parentWindowHandler))
			driver.close();
		
		driver.switchTo().window(parentWindowHandler); // switch back to parent
														// window
		EllieMaeLog.log(_log, "PDF Validation Ends", EllieMaeLogLevel.reporter);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			driver.switchTo().window(parentWindowHandler); // switch back to parent window
		}
		
		return pdfFileContent;
	}	

	/* Author : Jayesh Bhapkar
	 * Description : This method populates the required fields needed for loan creation
	 * with the data from test data excel sheet.
	 *  
	 *  */
	public void populateMandatoryFields(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Populating the mandatory Fields on Loan Data page for Loan file creation", EllieMaeLogLevel.reporter);
		
		/* On input data page enter and select values for loan creation */

		Select dropDown;
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.SERVICE_CODE_DROP_DOWN_NAME)))
		{
			dropDown= new Select(driver.findElement(By.name(MaventPortalMenuConsts.SERVICE_CODE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("SERVICE_CODE"));
		}

		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.FIRST_NAME_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.FIRST_NAME_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.FIRST_NAME_TEXTBOX_NAME))
					.sendKeys(testData.get("FIRST_NAME"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.LAST_NAME_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.LAST_NAME_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.LAST_NAME_TEXTBOX_NAME))
					.sendKeys(testData.get("LAST_NAME"));
		}
	
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.CREDITOR_APPLICATION_DATE_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.CREDITOR_APPLICATION_DATE_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.CREDITOR_APPLICATION_DATE_TEXTBOX_NAME))
				.sendKeys(testData.get("CREDITOR_APPLICATION_DATE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.LIEN_POSITION_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.LIEN_POSITION_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("LIEN_POSITION"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.MORTGAGE_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.MORTGAGE_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("MORTGAGE_TYPE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.ORIGINATION_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.ORIGINATION_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("ORIGINATION_TYPE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.AMORTIZATION_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.AMORTIZATION_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("AMORTIZATION_TYPE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.TRANSACTION_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.TRANSACTION_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("TRANSACTION_TYPE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.COUNTY_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.COUNTY_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.COUNTY_TEXTBOX_NAME)).sendKeys(testData.get("COUNTY"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.STATE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.STATE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("STATE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.ZIPCODE_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.ZIPCODE_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.ZIPCODE_TEXTBOX_NAME)).sendKeys(testData.get("ZIPCODE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.PROPERTY_TYPE_DROP_DOWN_NAME)))
		{		
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.PROPERTY_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("PROPERTY_TYPE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.OCCUPANCY_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.OCCUPANCY_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("OCCUPANCY_TYPE"));
		}

		/* double click on textbox, remove the value and enter new value */
		WebElement textBox;
		Actions builder;
		Action seriesOfActions;
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.APPRAISED_VALUE_TEXTBOX_NAME)))
		{
			textBox = driver.findElement(By.name(MaventPortalMenuConsts.APPRAISED_VALUE_TEXTBOX_NAME));
			builder = new Actions(driver);
			seriesOfActions = builder.moveToElement(textBox).click().doubleClick(textBox).build();
			seriesOfActions.perform();
			driver.findElement(By.name(MaventPortalMenuConsts.APPRAISED_VALUE_TEXTBOX_NAME)).sendKeys("");
			driver.findElement(By.name(MaventPortalMenuConsts.APPRAISED_VALUE_TEXTBOX_NAME))
					.sendKeys(testData.get("APPRAISED_VALUE"));
		}

		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.SALES_VALUE_TEXTBOX_NAME)))
		{
			textBox = driver.findElement(By.name(MaventPortalMenuConsts.SALES_VALUE_TEXTBOX_NAME));
			builder = new Actions(driver);
			seriesOfActions = builder.moveToElement(textBox).click().doubleClick(textBox).build();
			seriesOfActions.perform();
			driver.findElement(By.name(MaventPortalMenuConsts.SALES_VALUE_TEXTBOX_NAME)).sendKeys("");
			driver.findElement(By.name(MaventPortalMenuConsts.SALES_VALUE_TEXTBOX_NAME))
					.sendKeys(testData.get("SALES_VALUE"));
		}

		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.PIAMOUNT_TEXTBOX_NAME)))
		{
			textBox = driver.findElement(By.name(MaventPortalMenuConsts.PIAMOUNT_TEXTBOX_NAME));
			builder = new Actions(driver);
			seriesOfActions = builder.moveToElement(textBox).click().doubleClick(textBox).build();
			seriesOfActions.perform();
			driver.findElement(By.name(MaventPortalMenuConsts.PIAMOUNT_TEXTBOX_NAME)).sendKeys("");
			driver.findElement(By.name(MaventPortalMenuConsts.PIAMOUNT_TEXTBOX_NAME)).sendKeys(testData.get("PIAMOUNT"));
		}

		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.NOTE_RATE_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.NOTE_RATE_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.NOTE_RATE_TEXTBOX_NAME)).sendKeys(testData.get("NOTE_RATE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.DISCOUNT_POINTS_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.DISCOUNT_POINTS_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("DISCOUNT_POINTS"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.FIRST_PAYMENT_DATE_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.FIRST_PAYMENT_DATE_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.FIRST_PAYMENT_DATE_TEXTBOX_NAME))
				.sendKeys(testData.get("FIRST_PAYMENT_DATE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.AMORTIZATION_MONTHS_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.AMORTIZATION_MONTHS_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.AMORTIZATION_MONTHS_TEXTBOX_NAME))
					.sendKeys(testData.get("AMORTIZATION_MONTHS"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.LOAN_MONTHS_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.LOAN_MONTHS_TEXTBOX_NAME)).clear();
			driver.findElement(By.name(MaventPortalMenuConsts.LOAN_MONTHS_TEXTBOX_NAME))
					.sendKeys(testData.get("LOAN_MONTHS"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.LENDER_TYPE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.LENDER_TYPE_DROP_DOWN_NAME)));
			dropDown.selectByIndex(1);
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.LENDER_HOME_STATE_DROP_DOWN_NAME)))
		{
			dropDown = new Select(driver.findElement(By.name(MaventPortalMenuConsts.LENDER_HOME_STATE_DROP_DOWN_NAME)));
			dropDown.selectByVisibleText(testData.get("STATE"));
		}

		EllieMaeLog.log(_log, "Populating the Mandatory Fields on Loan Data page complete", EllieMaeLogLevel.reporter);

	}

}