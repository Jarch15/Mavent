package com.elliemae.pageobject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.consts.MaventTILHUDPageConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

/**
 * <b>Name:</b> MaventTILHUDPage</br>
 * <b>Description: </b>This page object class for Mavent Portal TILA HUD Page.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventTILHUDPage {

	public static Logger _log = Logger.getLogger(MaventTILHUDPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventTILHUDPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	public boolean navigateToTILAHUDPage() 
	{
		EllieMaeLog.log(_log, "Navigating to TILA-HUD page", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventTILHUDPageConsts.TIL_HUD_LINK_PARTIAL_LINKTEXT)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean populateMandatoryFields(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Populating the Mandatory Fields on TIL-HUD page...", EllieMaeLogLevel.reporter);
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.INTEREST_FROM_DATE_TEXTBOX_NAME)))
		{
			driver.findElement(By.name(MaventPortalMenuConsts.INTEREST_FROM_DATE_TEXTBOX_NAME))
					.sendKeys(testData.get("INTEREST_FROM_DATE"));
		}
		
		if(objEllieMaeActions.isWebElementExist(By.name(MaventPortalMenuConsts.INTEREST_TO_DATE_TEXTBOX_NAME)))
		{		
			driver.findElement(By.name(MaventPortalMenuConsts.INTEREST_TO_DATE_TEXTBOX_NAME))
					.sendKeys(testData.get("INTEREST_TO_DATE"));
		}
		CommonUtilityApplication.threadWait(3000);
		
		EllieMaeLog.log(_log, "Populating the Mandatory Fields on TIL-HUD page complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method searches a loan by providing loan id.
	 * It returns true after successful search.
	 *  
	 *  */
	public boolean searchLoan() 
	{
		EllieMaeLog.log(_log, "Loan Search", EllieMaeLogLevel.reporter);

		/* On Loan Search page populate Loan ID and Create Date From */

		driver.findElement(By.id(MaventPortalMenuConsts.LOAN_SEARCH_TEXTBOX_ID))
				.sendKeys("ReverseProxy_180110115841");

		driver.findElement(By.id(MaventPortalMenuConsts.SEARCH_BUTTON_ID)).click();
		
		CommonUtilityApplication.threadWait(3000);
		
		EllieMaeLog.log(_log, "Loan Search complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public int getNumberOfFeesOnPage()
	{
		List<WebElement> listDeleteFeesCheckBox = driver.findElements(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_CHECKBOX_XPATH));
		int numberOfFessAlreadyPresent = listDeleteFeesCheckBox.size()-2;
		return numberOfFessAlreadyPresent;
	}
	
	public boolean addFee(HashMap<String, String> testData)
	{
		// click on Second Pass radio button if exist and if not checked already
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)))
		{
			if(!driver.findElement(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)).isSelected())
			{
				driver.findElement(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)).click();
				objEllieMaeActions.waitForPageToLoad("5000");
				CommonUtilityApplication.threadWait(5000);
			}
		}
		
		// check how many fees already there
		List<WebElement> listDeleteFeesCheckBox = driver.findElements(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_CHECKBOX_XPATH));
		int numberOfFessAlreadyPresent = listDeleteFeesCheckBox.size()-1;
		if(numberOfFessAlreadyPresent == 1)
		{
			addFee1(testData);
		}
		else if(numberOfFessAlreadyPresent == 2)
		{
			addFee2(testData);
		}
		else if(numberOfFessAlreadyPresent == 3)
		{
			addFee3(testData);
		}
		else if(numberOfFessAlreadyPresent == 4)
		{
			addFee4(testData);
		}
		else if(numberOfFessAlreadyPresent == 5)
		{
			addFee5(testData);
		}
		else if(numberOfFessAlreadyPresent == 6)
		{
			addFee6(testData);
		}
		
		return true;
	}
	
	public boolean addFeesDynamically(HashMap<String, String> testData)
	{
		// click on Second Pass radio button if exist and if not checked already
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)))
		{
			if(!driver.findElement(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)).isSelected())
			{
				driver.findElement(By.xpath(MaventTILHUDPageConsts.SECONDPASS_RADIO_BUTTON_XPATH)).click();
				objEllieMaeActions.waitForPageToLoad("5000");
				CommonUtilityApplication.threadWait(5000);
			}
		}
		
		// check how many fees already there
		List<WebElement> listDeleteFeesCheckBox = driver.findElements(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_CHECKBOX_XPATH));
		int numberOfFessAlreadyPresent = listDeleteFeesCheckBox.size()-1;
		
		EllieMaeLog.log(_log, "Adding Fee "+numberOfFessAlreadyPresent+" ...", EllieMaeLogLevel.reporter);
		
		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH.replace("<ID>", ""+numberOfFessAlreadyPresent))));
		dropDown.selectByVisibleText(testData.get("FEE_ID_"+numberOfFessAlreadyPresent));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH.replace("<ID>", ""+numberOfFessAlreadyPresent))));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH.replace("<ID>", ""+numberOfFessAlreadyPresent))));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH.replace("<ID>", ""+numberOfFessAlreadyPresent))).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH.replace("<ID>", ""+numberOfFessAlreadyPresent)))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		// click ADD fees button
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)))
		{
			driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		}
		
		EllieMaeLog.log(_log, "Fee "+numberOfFessAlreadyPresent+" Added", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean addFee1(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 1 ...", EllieMaeLogLevel.reporter);
		
		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_1)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_1"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_1)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_1)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_1)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_1))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		// click ADD fees button
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)))
		{
			driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		}
		
		EllieMaeLog.log(_log, "Fee 1 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean addFee2(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 2 ...", EllieMaeLogLevel.reporter);

		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_2)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_2"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_2)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_2)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));

		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_2)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_2))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		// click ADD fees button
		driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		
		EllieMaeLog.log(_log, "Fee 2 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean addFee3(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 3 ...", EllieMaeLogLevel.reporter);

		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_3)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_3"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_3)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_3)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_3)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_3))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		
		EllieMaeLog.log(_log, "Fee 3 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean addFee4(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 4 ...", EllieMaeLogLevel.reporter);

		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_4)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_4"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_4)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_4)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_4)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_4))
					.sendKeys(testData.get("FEE_AMOUNT"));

		// click ADD fees button
		driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		
		EllieMaeLog.log(_log, "Fee 4 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean addFee5(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 5 ...", EllieMaeLogLevel.reporter);

		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_5)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_5"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_5)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_5)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		

		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_5)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_5))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		// click ADD fees button
		driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		
		EllieMaeLog.log(_log, "Fee 5 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean addFee6(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Adding Fee 6 ...", EllieMaeLogLevel.reporter);

		Select dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_ID_DROPDOWN_XPATH_6)));
		//dropDown.selectByVisibleText(testData.get("LOCATION"));
		dropDown.selectByVisibleText(testData.get("FEE_ID_6"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDTOCODE_DROPDOWN_XPATH_6)));
		dropDown.selectByVisibleText(testData.get("PAID_TO_CODE"));
		
		dropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.PAIDBYCODE_DROPDOWN_XPATH_6)));
		dropDown.selectByVisibleText(testData.get("PAID_BY_CODE"));
		
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_6)).clear();
		driver.findElement(By.xpath(MaventTILHUDPageConsts.FEE_AMOUNT_TEXTBOX_XPATH_6))
					.sendKeys(testData.get("FEE_AMOUNT"));
		
		// click ADD fees button
//		driver.findElement(By.xpath(MaventTILHUDPageConsts.ADD_FEES_BUTTON_XPATH)).click();
		
		EllieMaeLog.log(_log, "Fee 6 Added", EllieMaeLogLevel.reporter);
		return true;
	}
	
	public boolean deleteFee5(HashMap<String, String> testData) 
	{
		EllieMaeLog.log(_log, "Deleting Fee 5 ...", EllieMaeLogLevel.reporter);
		
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_CHECKBOX_XPATH_5)))
		{
			// click on checkbox to delete
			driver.findElement(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_CHECKBOX_XPATH_5)).click();
			CommonUtilityApplication.threadWait(2000);
	
			// click DELETE fees button
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MaventTILHUDPageConsts.DELETE_FEES_BUTTON_XPATH)));
			element.click();
	        
	        Alert alert = driver.switchTo().alert();	
	        String alertMessage= alert.getText();	
	        EllieMaeLog.log(_log, "Delete Alert : "+alertMessage, EllieMaeLogLevel.reporter);
	        alert.accept();
	
			EllieMaeLog.log(_log, "Fee 5 Deleted", EllieMaeLogLevel.reporter);
			return true;
		}
		else
		{
			EllieMaeLog.log(_log, "Not able to delete Fee 5, as it is not exists", EllieMaeLogLevel.reporter);
			return false;
		}
	
		
	}
	
	public boolean applyTemplate(HashMap<String, String> testData)
	{
		
		EllieMaeLog.log(_log, "Applying Template ...", EllieMaeLogLevel.reporter);
		
		// Click on TILA MDIA DISCLOSURE  checkbox if not selected
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)))
		{
			if(!driver.findElement(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)).isSelected())
			{
				driver.findElement(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)).click();
				CommonUtilityApplication.threadWait(1000);
			}
		}
		
		// Select Fixed rate template
		Select templateDropDown = new Select(driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_DROPDOWN_XPATH)));
		templateDropDown.selectByValue(testData.get("APPLY_TEMPLATE"));
		
		// Apply template
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_APPLY_BTN_XPATH)).click();
		CommonUtilityApplication.threadWait(1000);
		
		EllieMaeLog.log(_log, "Applying Template Complete ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean resetTemplate(HashMap<String, String> testData)
	{
		
		EllieMaeLog.log(_log, "Resetting Template ...", EllieMaeLogLevel.reporter);
		
		// Click on TILA MDIA DISCLOSURE  checkbox if not selected
		if(objEllieMaeActions.isWebElementExist(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)))
		{
			if(!driver.findElement(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)).isSelected())
			{
				driver.findElement(By.xpath(MaventTILHUDPageConsts.TILA_MDIA_DISC_CHECKBOX_XPATH)).click();
				CommonUtilityApplication.threadWait(1000);
			}
		}
		
		// Reset template
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_RESET_BTN_XPATH)).click();		
		CommonUtilityApplication.threadWait(2000);
		
        Alert alert = driver.switchTo().alert();	
        String alertMessage= alert.getText();	
        EllieMaeLog.log(_log, "Resetting Template Alert Message : "+alertMessage, EllieMaeLogLevel.reporter);
        alert.accept();
        CommonUtilityApplication.threadWait(3000);
        
        EllieMaeLog.log(_log, "Resetting Template Complete ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean addColumnBeforeForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding Column Before ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH));
		if(!chkBoxesBefore.get(0).isSelected())
		{
			chkBoxesBefore.get(0).click();
		}
		
		// click on add before
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_COLUMN_BEFORE_BTN_XPATH)).click();

		Select templateTypeDropDown = new Select(driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_TYPE_DROPDOWN_XPATH)).get(0));
		templateTypeDropDown.selectByValue(testData.get("TEMPLATE_TYPE"));
		
		EllieMaeLog.log(_log, "Adding Column Before Complete  ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean addColumnAfterForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding Column After ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH));
		if(!chkBoxesBefore.get(0).isSelected())
		{
			chkBoxesBefore.get(0).click();
		}
		
		// click on add before
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_COLUMN_AFTER_BTN_XPATH)).click();
		
		Select templateTypeDropDown = new Select(driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_TYPE_DROPDOWN_XPATH)).get(1));
		templateTypeDropDown.selectByValue(testData.get("TEMPLATE_TYPE"));
		
		EllieMaeLog.log(_log, "Adding Column After Complete ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean deleteColumnForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Deleting Column For Template ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH));
		if(!chkBoxesBefore.get(0).isSelected())
		{
			chkBoxesBefore.get(0).click();
		}
		
		// click on add before
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_DELETE_COLUMN_BTN_XPATH)).click();
		
		EllieMaeLog.log(_log, "Deleting Column For Template complete...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean swapColumnsForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Swapping Columns ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH));
		if(!chkBoxesBefore.get(0).isSelected())
		{
			chkBoxesBefore.get(0).click();
		}
		if(!chkBoxesBefore.get(1).isSelected())
		{
			chkBoxesBefore.get(1).click();
		}
		
		// click on add before
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_SWAP_COLUMN_BTN_XPATH)).click();
		
		EllieMaeLog.log(_log, "Swapping Columns Complete ...", EllieMaeLogLevel.reporter);
		
		
		return true;
	}
	
	public boolean addRowBeforeForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding Row before ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ROW_SELECT_CHKBOX_XPATH));
		int numberOfCheckBoxesBefore = chkBoxesBefore.size();
		if(!chkBoxesBefore.get(numberOfCheckBoxesBefore-1).isSelected())
		{
			chkBoxesBefore.get(numberOfCheckBoxesBefore-1).click();
		}
		
		// click on add before
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_ROW_BEFORE_BTN_XPATH)).click();
		
		List<WebElement> rowDropDownList = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_ROW_TYPE_DROPDOWN_XPATH));
		Select rowDropDown = new Select(rowDropDownList.get(rowDropDownList.size()-2));
		rowDropDown.selectByValue(testData.get("TEMPLATE_ROW_TYPE1"));
		
		EllieMaeLog.log(_log, "Adding Row before complete ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean addRowAfterForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Adding Row After ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ROW_SELECT_CHKBOX_XPATH));
		int numberOfCheckBoxesBefore = chkBoxesBefore.size();
		if(!chkBoxesBefore.get(numberOfCheckBoxesBefore-1).isSelected())
		{
			chkBoxesBefore.get(numberOfCheckBoxesBefore-1).click();
		}
		
		// click on add after
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_ROW_AFTER_BTN_XPATH)).click();
		
		List<WebElement> rowDropDownList = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_ROW_TYPE_DROPDOWN_XPATH));
		Select rowDropDown = new Select(rowDropDownList.get(rowDropDownList.size()-1));
		rowDropDown.selectByValue(testData.get("TEMPLATE_ROW_TYPE2"));
		
		EllieMaeLog.log(_log, "Adding Row After Complete ...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean deleteRowForTemplate(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Deleting a row ...", EllieMaeLogLevel.reporter);
		
		// click on one column checkbox
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ROW_SELECT_CHKBOX_XPATH));
		int numberOfCheckBoxesBefore = chkBoxesBefore.size();
		if(!chkBoxesBefore.get(numberOfCheckBoxesBefore-1).isSelected())
		{
			chkBoxesBefore.get(numberOfCheckBoxesBefore-1).click();
		}
		
		// click on add after
		driver.findElement(By.xpath(MaventTILHUDPageConsts.TEMPLATE_DELETE_ROW_BTN_XPATH)).click();
		
		EllieMaeLog.log(_log, "Deleting row complete...", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public String getColumnTypeForTemplate()
	{
		Select templateTypeDropDown = new Select(driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ADD_TYPE_DROPDOWN_XPATH)).get(0));
		WebElement option = templateTypeDropDown.getFirstSelectedOption();
		String value = option.getText();
		EllieMaeLog.log(_log, "templateTypeDropDown value : " +value , EllieMaeLogLevel.reporter);
		return value;
	}
	
	public int getNumberOfTemplateColumnsOnPage()
	{
		List<WebElement> chkBoxesAfter = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH));
		return chkBoxesAfter.size();
	}
	
	public int getNumberOfTemplateRowsOnPage()
	{
		List<WebElement> chkBoxesBefore = driver.findElements(By.xpath(MaventTILHUDPageConsts.TEMPLATE_ROW_SELECT_CHKBOX_XPATH));
		return chkBoxesBefore.size();
	}
}