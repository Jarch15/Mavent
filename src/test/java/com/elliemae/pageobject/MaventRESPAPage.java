package com.elliemae.pageobject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventRESPAPageConsts;
import com.elliemae.consts.MaventTILHUDPageConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

/**
 * <b>Name:</b> MaventRESPAPage</br>
 * <b>Description: </b>This page object class for Mavent Portal RESPA Page.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventRESPAPage {

	public static Logger _log = Logger.getLogger(MaventRESPAPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventRESPAPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	public boolean navigateToRESPAPage() 
	{
		EllieMaeLog.log(_log, "Navigating to RESPA page", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventRESPAPageConsts.RESPA_LINK_PARTIAL_LINKTEXT)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean addNewCharges(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding New Charges ... ", EllieMaeLogLevel.reporter);
		// click on radio button
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEETEXTOPTION_RADIOBTN_XPATH)))
		{
			if(!driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEETEXTOPTION_RADIOBTN_XPATH)).isSelected())
			{
				driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEETEXTOPTION_RADIOBTN_XPATH)).click();
				CommonUtilityApplication.threadWait(1000);
			}
		}
		
		// enter charge description, HUDID, GFE amount, HUD amount
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEENAMETEXT_TEXTBOX_XPATH)).sendKeys(testData.get("FEE_NAME_TEXT"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_HUDID_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_HUDID_1"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_GFEAMOUNT_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_GFEAMOUNT_1"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_HUD1AMOUNT_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_HUD1AMOUNT_1"));
		CommonUtilityApplication.threadWait(1000);
		// click on ADD button
		//driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_ADD_BTN_XPATH)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_ADD_BTN_XPATH)));
		element.click();
		CommonUtilityApplication.threadWait(1000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_RESPA_AddCharge", CommonUtility.currentTimeStamp);
		EllieMaeLog.log(_log, "New Charges Added ... ", EllieMaeLogLevel.reporter);

		return true;
	}
	
	public boolean addFromAvailableCharges(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding HUD Charges ... ", EllieMaeLogLevel.reporter);
		
		//Select HUD range, HUD range code and fee name
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEEDD_RADIOBTN_XPATH)).click();
		Select dropDown = new Select(driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_HUDRANGECODE_DROPDOWN_XPATH)));
		dropDown.selectByValue(testData.get("HUD_RANGE_CODE"));
		dropDown = new Select(driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_FEENAMEDD_DROPDOWN_XPATH)));
		dropDown.selectByValue(testData.get("FEE_NAME_DD"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_HUDID_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_HUDID_2"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_GFEAMOUNT_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_GFEAMOUNT_2"));
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_HUD1AMOUNT_TEXTBOX_XPATH)).sendKeys(testData.get("ADD_CHARGES_HUD1AMOUNT_2"));
		// click on ADD button
		//driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_ADD_BTN_XPATH)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_ADD_BTN_XPATH)));
		element.click();
		CommonUtilityApplication.threadWait(1000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_RESPA_AddHUDCharge", CommonUtility.currentTimeStamp);
		EllieMaeLog.log(_log, "HUD Charges Added ... ", EllieMaeLogLevel.reporter);

		return true;
	}
	
	public boolean deleteCharges(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Deleting Charges ... ", EllieMaeLogLevel.reporter);
		
		// Delete the last charge added
		List<WebElement> deleteChkBoxElements = driver.findElements(By.xpath(MaventRESPAPageConsts.DELETE_CHARGES_CHKBOX_XPATH));
		deleteChkBoxElements.get(deleteChkBoxElements.size()-1).click();
		driver.findElement(By.xpath(MaventRESPAPageConsts.ADD_CHARGES_DELETE_BTN_XPATH)).click();
		CommonUtilityApplication.threadWait(1000);
		
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_RESPA_DeleteCharge", CommonUtility.currentTimeStamp);
		EllieMaeLog.log(_log, "Charges Deleted...", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean saveLoanData()
	{
		EllieMaeLog.log(_log, "Saving Loan Data", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventRESPAPageConsts.LOAN_DATA_SAVE_BTN_XPATH)).click();
		objEllieMaeActions.waitForPageToLoad("50000");
		CommonUtilityApplication.threadWait(15000);			
		EllieMaeLog.log(_log, "Loan Data saved", EllieMaeLogLevel.reporter);
		return true;
	}
	
}