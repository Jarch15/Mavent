package com.elliemae.pageobject;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;

/**
 * <b>Name:</b> MaventPortalLoginTestPage</br>
 * <b>Description: </b>This page object class is used for Mavent Portal Login Test Case.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventPortalLoginTestPage {

	public static Logger _log = Logger.getLogger(MaventPortalLoginTestPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventPortalLoginTestPage(WebDriver driver, long implicitWait ) 
	{
		driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		
		// Updated timeout from 120 sec to 300 sec to accomodate the apache timeout failure in activity report
		driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		this.driver = driver;
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method navigates to the configured Mavent Portal.
	 * It returns the Title of the webpage back to the caller after navigation is complete
	 *  
	 *  */
	public String navigateToPortal(String URL) 
	{

		// Navigate
		EllieMaeLog.log(_log, "Navigating to the Mavent URL", EllieMaeLogLevel.reporter);

		try
		{
			// Handle certification pop up for chrome (before calling get)
			if(FrameworkConsts.CURRENTBROWSERNAME.equals("chrome"))
			{
				handleCertificationPopupForChrome();
			}
			driver.get(URL);
			if(driver.getTitle().equalsIgnoreCase("Certificate Error: Navigation Blocked"))
			{
				driver.get("javascript:document.getElementById('overridelink').click();");
			}
		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception occcurred in navigation : "+e.getMessage(), EllieMaeLogLevel.reporter);
		}
	
		CommonUtilityApplication.threadWait(2000);
		return driver.getTitle();
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does login of configured user into the Mavent Portal.
	 * It returns back the logged in user name back to the caller.
	 *  
	 *  */
	public String loginToPortal(HashMap<String, String> testData, HashMap<String, String> userData) 
	{
		objEllieMaeActions.waitUntilVisibilityOfElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD),30);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).clear();
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).sendKeys(userData.get("UserName"));
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).clear();
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(userData.get("Password"));
		CommonUtilityApplication.threadWait(1000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).clear();
		CommonUtilityApplication.threadWait(2000);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(userData.get("Company"));
		CommonUtilityApplication.threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN)).click();
		CommonUtilityApplication.threadWait(3000);
		String loggedUser = null;
		loggedUser = driver.findElement(By.xpath(MaventPortalMenuConsts.LOGGED_USER_XPATH)).getText();
		EllieMaeLog.log(_log, "Logged user : "+loggedUser, EllieMaeLogLevel.reporter);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_verifyLogin_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
		return loggedUser;

	}

	/* Author : Jayesh Bhapkar
	 * Description : This method does logout of the current user from the Mavent Portal.
	 * It returns the webpage title back to the caller once it successfully logs out.
	 *  */
	public String logOut() {
		/* Click on Log out link */
		EllieMaeLog.log(_log, "Logging out of the portal", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitUntilVisibilityOfElement(By.partialLinkText(MaventPortalMenuConsts.LOG_OUT_LINK_PARTIAL_LINKTEXT),30);
		driver.findElement(By.partialLinkText(MaventPortalMenuConsts.LOG_OUT_LINK_PARTIAL_LINKTEXT)).click();
		EllieMaeLog.log(_log, "Log out is Successfull", EllieMaeLogLevel.reporter);
		CommonUtilityApplication.threadWait(3000);
		return driver.getTitle();
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method searches a loan by providing loan id.
	 * It returns true after successful search.
	 *  
	 *  */
	public boolean searchLoan(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Loan Search", EllieMaeLogLevel.reporter);

		/* On Loan Search page populate Loan ID and Create Date From */
		
		objEllieMaeActions.waitUntilVisibilityOfElement(By.id(MaventPortalMenuConsts.LOAN_SEARCH_TEXTBOX_ID),30);
		driver.findElement(By.id(MaventPortalMenuConsts.LOAN_SEARCH_TEXTBOX_ID))
				.sendKeys(testData.get("LOAN_ID"));
		
		CommonUtilityApplication.threadWait(2000);

		driver.findElement(By.id(MaventPortalMenuConsts.SEARCH_BUTTON_ID)).click();
		
		CommonUtilityApplication.threadWait(5000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_SearchLoan_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
		EllieMaeLog.log(_log, "Loan Search complete", EllieMaeLogLevel.reporter);
		
		return true;
	}


	/* Author : Jayesh Bhapkar
	 * Description : This method does a loan review and return the title of the webpage back after loan review is completed.
	 *  
	 *  */
	public String reviewLoan(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Loan review Starts", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.REVIEW_BUTTON_XPATH),30);
		driver.findElement(By.xpath(MaventPortalMenuConsts.REVIEW_BUTTON_XPATH)).click();
		//Dynamic wait added
		WebDriverWait wait = new WebDriverWait(driver, 200000);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MaventPortalMenuConsts.WAITVERIFY_XPATH)));
			
		//CommonUtilityApplication.threadWait(15000);		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_LoanReview_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
		EllieMaeLog.log(_log, "Loan review Ends", EllieMaeLogLevel.reporter);
		return driver.getTitle();
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method adds a comment on loan review page.
	 *  
	 *  */
	public void addComment(HashMap<String, String> testData, String comment)
	{
		EllieMaeLog.log(_log, "Adding comment", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.COMMENT_LINK_XPATH),30);
		driver.findElement(By.xpath(MaventPortalMenuConsts.COMMENT_LINK_XPATH)).click();
		CommonUtilityApplication.threadWait(2000);
		// write comment
		objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.COMMENT_TEXTAREA_XPATH),30);
		driver.findElement(By.xpath(MaventPortalMenuConsts.COMMENT_TEXTAREA_XPATH)).sendKeys(comment);
		CommonUtilityApplication.threadWait(2000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_AddComment_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.COMMENT_ADD_BUTTON_XPATH)).click();
		CommonUtilityApplication.threadWait(2000);
		
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method does a view review xml, takes screenshot of the XML window
	 * and returns the XML content back to the caller.
	 *  
	 *  */
	public String getReviewXML(HashMap<String, String> testData)
	{
		String xmlContent = "";
		String parentWindowHandler = driver.getWindowHandle(); // Store parent window
		try
		{		
			EllieMaeLog.log(_log, "view review xml starts", EllieMaeLogLevel.reporter);
			
			objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.VIEW_REVIEW_XML_XPATH),30);
			driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_REVIEW_XML_XPATH)).click();
			CommonUtilityApplication.threadWait(3000);
			
			// switch to pop up window
			for(String newWindow : driver.getWindowHandles())
			{
				if(!newWindow.equals(parentWindowHandler))
				{
					driver.switchTo().window(newWindow);
				}
			}

			try
			{
				if(driver.getTitle().equalsIgnoreCase("Certificate Error: Navigation Blocked"))
				{
					driver.get("javascript:document.getElementById('overridelink').click();");
				}
			}
			catch (Exception e)
			{
				// Suppress the certification pop up
				EllieMaeLog.log(_log, "Suppressing the Exception occurred : "+e.getMessage(), EllieMaeLogLevel.reporter);
			}
	
			String xmlURL = driver.getCurrentUrl();
			EllieMaeLog.log(_log, "Review XML URL : "+xmlURL, EllieMaeLogLevel.reporter);
			
			/* Take Screenshot*/
			try
			{
				CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_ViewXML_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
			}
			catch (Exception exception)
			{
			}
			
			// clear clipboard before copying
			CommonUtilityApplication.clearClipboard();
			
			// select all using ctrl+a
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, "a"));			
			CommonUtilityApplication.threadWait(2000);
			
			Robot rb = new Robot();

			// copy using ctrl+c
			rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
			rb.keyPress(java.awt.event.KeyEvent.VK_C);
			rb.keyRelease(java.awt.event.KeyEvent.VK_C);
			rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
			
			CommonUtilityApplication.threadWait(2000);
			
			// get data from clipboard
			xmlContent = CommonUtilityApplication.getClipboardData();
			
			//System.out.println("xmlContent : "+xmlContent);
			
			/* Close the XML window */
			if(!driver.getWindowHandle().equals(parentWindowHandler))
				driver.close();
			
			driver.switchTo().window(parentWindowHandler); // switch back to parent
															// window
			EllieMaeLog.log(_log, "view review xml ends", EllieMaeLogLevel.reporter);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			// switch to pop up window if exist and close the pop up.
			for(String newWindow : driver.getWindowHandles())
			{
				if(!newWindow.equals(parentWindowHandler))
				{
					driver.switchTo().window(newWindow);
					driver.close();
				}
			}
			driver.switchTo().window(parentWindowHandler); // switch back to parent window
		}
		
		return xmlContent;
		
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
		objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH),30);
		driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH)).click();
		CommonUtilityApplication.threadWait(10000);

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
		
		CommonUtilityApplication.threadWait(10000);

		/* Take Screenshot*/
		try
		{
			CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_ViewPDF_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
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
		
		CommonUtilityApplication.threadWait(3000);
		
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
		
		CommonUtilityApplication.threadWait(3000);
		
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
	 * Description : This method does a view PDF, takes screenshot of the PDF,
	 * and returns the PDF URL back to caller.
	 *  
	 *  */
	public String getPDFURL(HashMap<String, String> testData) {
		
		String parentWindowHandler = driver.getWindowHandle(); // Store parent window
		String pdfURL = null;
		
		try
		{		
			EllieMaeLog.log(_log, "PDF Validation Starts", EllieMaeLogLevel.reporter);
			/* Switch to Frame*/
			CommonUtilityApplication.threadWait(5000);
			driver.switchTo().frame(MaventPortalMenuConsts.FRAME);
			CommonUtilityApplication.threadWait(5000);
			objEllieMaeActions.waitUntilVisibilityOfElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH),120);
			driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_PDF_BUTTON_XPATH)).click();
			CommonUtilityApplication.threadWait(10000);
	
			// switch to pop up window
			for(String newWindow : driver.getWindowHandles())
			{
				if(!newWindow.equals(parentWindowHandler))
				{
					driver.switchTo().window(newWindow);
				}
			}
			
			try
			{
					if(driver.getTitle().equalsIgnoreCase("Certificate Error: Navigation Blocked"))
					{
						driver.get("javascript:document.getElementById('overridelink').click();");
					}
			}
			catch (Exception e)
			{
				// Suppress the certification pop up
				EllieMaeLog.log(_log, "Suppressing the Exception occurred : "+e.getMessage(), EllieMaeLogLevel.reporter);
			}
	
			CommonUtilityApplication.threadWait(5000);
			pdfURL = driver.getCurrentUrl();
			EllieMaeLog.log(_log, "PDF URL : "+pdfURL, EllieMaeLogLevel.reporter);
	
			/* Take Screenshot*/
			try
			{
				CommonUtilityApplication.takeScreenShot(testData, "MaventPortalLoginTest_ViewPDF_"+CommonUtilityApplication.getIpAddress(driver.getCurrentUrl()), CommonUtility.currentTimeStamp);
			}
			catch (Exception exception)
			{
			}
			
			/* Close the PDF window */
			if(!driver.getWindowHandle().equals(parentWindowHandler))
				driver.close();
			
			driver.switchTo().window(parentWindowHandler); // switch back to parent
															// window
			EllieMaeLog.log(_log, "PDF URL Validation Ends", EllieMaeLogLevel.reporter);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			driver.switchTo().window(parentWindowHandler); // switch back to parent window
		}
		
		return pdfURL;
	}
	
	
	/* Author : Jayesh Bhapkar
	 * Description : This method does an activity report by searching a loan.
	 * It returns the loan ID back to caller.
	 *  
	 *  */
	public String reportsActivity(HashMap<String, String> testData, String loanID) {
		EllieMaeLog.log(_log, "Reports Activity", EllieMaeLogLevel.reporter);	

		/* Select Reports-> Activity Menu */
		// For Chrome use Actions to click on Menu
		if(FrameworkConsts.CURRENTBROWSERNAME.equals("chrome"))
		{
			WebElement element = driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS));
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
			
			WebElement element2 = driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_ACTIVITY));
			Actions actions2 = new Actions(driver);
			actions2.moveToElement(element2).click().build().perform();
		}
		else
		{
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS)).click();
			driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_REPORTS_ACTIVITY)).click();
		}

		
		
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(2000);

		/* On Review Search page populate loan id, receive date from */
		driver.findElement(By.name(MaventPortalMenuConsts.REVIEW_SEARCH_ID_TEXTBOX_NAME))
				.sendKeys(loanID);
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
	
	
	// Function to perform Threaded Enter keyboard Event to handle Chrome Certification pop up 
	// before calling get method.
	private void handleCertificationPopupForChrome() {

		Runnable r = new Runnable() {

			public void run() {

				try {
					Robot r = new Robot();
					r.delay(3000);
					r.keyPress(KeyEvent.VK_ENTER);
					r.keyRelease(KeyEvent.VK_ENTER);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		};

		// Wake up the thread and perform the operation
		Thread t = new Thread(r);
		t.start();

    }

}