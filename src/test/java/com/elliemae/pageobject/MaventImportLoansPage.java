package com.elliemae.pageobject;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventImportLoansPageConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

/**
 * <b>Name:</b> MaventImportLoansPage</br>
 * <b>Description: </b>This is page object class for Mavent Import Loans Page.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventImportLoansPage {

	public static Logger _log = Logger.getLogger(MaventImportLoansPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();

	public MaventImportLoansPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	public boolean navigateToImportLoansPage() 
	{
		EllieMaeLog.log(_log, "Navigating to Import Loans page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventImportLoansPageConsts.DIRECT_INPUT_LINK)).click();
		driver.findElement(By.xpath(MaventImportLoansPageConsts.DIRECT_INPUT_IMPORT_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean loginToPortal(HashMap<String, String> testData) {

		CommonUtilityApplication.threadWait(3000);
		EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);	
		
		WebElement Loginelement = driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD));
		Loginelement.clear();
		Loginelement.sendKeys(testData.get("ImportUser"));
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).clear();
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(testData.get("Password"));
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).clear();
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(testData.get("Company"));		
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN)).click();
		
		CommonUtilityApplication.threadWait(3000);
		
		return true ;

	}
	
	public boolean selectDataFile(String fileToImport) throws AWTException
	{
		EllieMaeLog.log(_log, "Selecting loan file to import..."+fileToImport, EllieMaeLogLevel.reporter);

	    /* Handle Windows Pop up for Browse before */
		handleWindowsPopUp();
	    driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).sendKeys(fileToImport);
	    //CommonUtilityApplication.threadWait(3000);
	    
	    if(!driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).getText().equals(fileToImport))
	    {
	    	driver.findElement(By.xpath(MaventImportLoansPageConsts.INPUT_FILE_PATH_TEXTBOX_XPATH)).sendKeys(fileToImport);
	    	CommonUtilityApplication.threadWait(3000);
	    }
	    
		return true;
	}
	
	public boolean selectFileType()
	{
		EllieMaeLog.log(_log, "Selecting File Type...", EllieMaeLogLevel.reporter);
	    Select dropDown= new Select(driver.findElement(By.name(MaventImportLoansPageConsts.FILE_FORMAT_DROPDOWN_NAME)));
	    dropDown.selectByVisibleText("Mavent Loan XML");
	    CommonUtilityApplication.threadWait(1000);
		return true;
	}
	
	public boolean cleckOverwriteCheckBox()
	{
		EllieMaeLog.log(_log, "Select overwrite check box...", EllieMaeLogLevel.reporter);
	    if(!driver.findElement(By.name(MaventImportLoansPageConsts.OVERWRITE_CHKBOX_NAME)).isSelected())
	    {
	    	driver.findElement(By.name(MaventImportLoansPageConsts.OVERWRITE_CHKBOX_NAME)).click();
	    }

	    CommonUtilityApplication.threadWait(1000); 
		return true;
	}
	
	public boolean clickOnContinueButton()
	{
		EllieMaeLog.log(_log, "Click on continue...", EllieMaeLogLevel.reporter);
	    driver.findElement(By.xpath(MaventImportLoansPageConsts.CONTINUE_BTN_XPATH)).click();
	    CommonUtilityApplication.threadWait(10000);
	    
		return true;
	}
	
	public boolean loanReviewAll(HashMap<String, String> testData)
	{
		// Select All records
		EllieMaeLog.log(_log, "Selecting all records ...", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventImportLoansPageConsts.SELECT_ALL_BTN_XPATH)).click();
		CommonUtilityApplication.threadWait(1000);
		
		// Loan Review
		EllieMaeLog.log(_log, "Click on Loan Review ...", EllieMaeLogLevel.reporter);
		
		driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_REVIEW_BTN_XPATH)).click();
		CommonUtilityApplication.threadWait(5000);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_ReviewingLoans", CommonUtility.currentTimeStamp);
		//CommonUtilityApplication.threadWait(30000);
		// Wait until the element exist
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
		        .withTimeout(120, TimeUnit.SECONDS)
		        .pollingEvery(2000, TimeUnit.MILLISECONDS)
		        .ignoring(NoSuchElementException.class);
		
		fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(MaventImportLoansPageConsts.LOAN_FILE_NAME_LINK_XPATH)));
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIImportLoansTest_LoanReview_Results", CommonUtility.currentTimeStamp);
		
		return true;
	}
	
	// Selects loan pool name if exists, if not exists it creates new loan pool
	public boolean selectLoanPoolName(String loanPoolName)
	{
		EllieMaeLog.log(_log, "Select Loan Pool Name ...", EllieMaeLogLevel.reporter);
	    Select loanPoolDropDown= new Select(driver.findElement(By.name(MaventImportLoansPageConsts.LOAN_POOL_DROPDOWN_NAME)));
	    if(checkIfLoanPoolExists(loanPoolName))
	    {
		    if(!driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_POOL_RADIO_BUTTON_EXISTING_XPATH)).isSelected())
		    {
		    	driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_POOL_RADIO_BUTTON_EXISTING_XPATH)).click();
		    }
	    	loanPoolDropDown.selectByVisibleText(loanPoolName);
	    }
	    else
	    {
		    if(!driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_POOL_RADION_BUTTON_NEW_XPATH)).isSelected())
		    {
		    	driver.findElement(By.xpath(MaventImportLoansPageConsts.LOAN_POOL_RADION_BUTTON_NEW_XPATH)).click();
		    }
		    CommonUtilityApplication.threadWait(1000);
	    	createLoanPool(loanPoolName);
	    }
		return true;
	}
	
	public boolean checkIfLoanPoolExists(String loanPoolName)
	{
		EllieMaeLog.log(_log, "Checking if loan pool : "+loanPoolName+ " exists..", EllieMaeLogLevel.reporter);
	    Select loanPoolDropDown= new Select(driver.findElement(By.name(MaventImportLoansPageConsts.LOAN_POOL_DROPDOWN_NAME)));
	    List<WebElement> allOptions = loanPoolDropDown.getOptions();
	    // Check if loan pool exists
	    for(WebElement element : allOptions)
	    {
	    	if(element.getText().equalsIgnoreCase(loanPoolName))
	    	{
	    		EllieMaeLog.log(_log, "Loan pool : "+loanPoolName+ " exists..", EllieMaeLogLevel.reporter);
	    		return true;
	    	}
	    }
	    EllieMaeLog.log(_log, "Loan pool : "+loanPoolName+ " not exists..", EllieMaeLogLevel.reporter);
	    return false;
	}
	
	public void createLoanPool(String loanPoolName)
	{
		EllieMaeLog.log(_log, "Creating Loan pool : "+loanPoolName, EllieMaeLogLevel.reporter);
		driver.findElement(By.name(MaventImportLoansPageConsts.NEW_LOAN_POOL_NAME_NAME)).sendKeys(loanPoolName);
		driver.findElement(By.name(MaventImportLoansPageConsts.NEW_LOAN_POOL_DESCRIPTION_NAME)).sendKeys(loanPoolName);
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