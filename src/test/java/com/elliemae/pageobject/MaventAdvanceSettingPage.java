package com.elliemae.pageobject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventAdvanceSettingConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

/**
 * <b>Name:</b> MaventTILHUDPage</br>
 * <b>Description: </b>This page object class for Mavent Portal Abiloty To Repay Page.</br>
 * 
 * @author <i>Nidhi Khandelwal</i>
 */
public class MaventAdvanceSettingPage {

	public static Logger _log = Logger.getLogger(MaventAdvanceSettingPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventAdvanceSettingPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	public boolean navigateToAdvanceSettingPage() 
	{
		EllieMaeLog.log(_log, "Navigating to Ability To Repay page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventAdvanceSettingConsts.AdvancedSettings_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean populateAdvanceSettingField(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Advance Setting page...", EllieMaeLogLevel.reporter);
		Select AS_calculationMethod = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_calculationMethod)));		
		AS_calculationMethod.selectByValue(testData.get("AS_calculationMethod"));

		Select AS_calcDaysInYear = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_calcDaysInYear)));		
		AS_calcDaysInYear.selectByValue(testData.get("AS_calcDaysInYear"));
		
		Select AS_paymentsInYear = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_paymentsInYear)));		
		AS_paymentsInYear.selectByValue(testData.get("AS_paymentsInYear"));
		
		Select AS_daysInYear = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_daysInYear)));		
		AS_daysInYear.selectByValue(testData.get("AS_daysInYear"));
		
		Select AS_oddDaysInterest = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_oddDaysInterest)));		
		AS_oddDaysInterest.selectByValue(testData.get("AS_oddDaysInterest"));
		
		
		Select AS_interestCredit = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_interestCredit)));		
		AS_interestCredit.selectByValue(testData.get("AS_interestCredit"));

		Select AS_firstPaymentPeriodTreatment = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_firstPaymentPeriodTreatment)));		
		AS_firstPaymentPeriodTreatment.selectByValue(testData.get("AS_firstPaymentPeriodT"));
		
		Select AS_paymentRoundingTreatment = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_paymentRoundingTreatment)));		
		AS_paymentRoundingTreatment.selectByValue(testData.get("AS_paymentRTreatment"));
		
		Select AS_paymentRounding = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_paymentRounding)));		
		AS_paymentRounding.selectByValue(testData.get("AS_paymentRounding"));
		
		Select AS_indexMarginRoundingTreatment = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_indexMarginRoundingTreatment)));		
		AS_indexMarginRoundingTreatment.selectByValue(testData.get("AS_indexMarginRTreatment"));
		
		Select AS_indexMarginRounding = new Select(driver.findElement(By.xpath(MaventAdvanceSettingConsts.AS_indexMarginRounding)));		
		AS_indexMarginRounding.selectByValue(testData.get("AS_indexMarginRounding"));

		
		CommonUtilityApplication.threadWait(3000);
					
		EllieMaeLog.log(_log, "Populating ATRQualifiedMortgage filds on Ability To Repay page complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	
	public void clickInputDataLoanData() 
	{
		driver.findElement(By.xpath(MaventPortalMenuConsts.MaventUIRegression_InputData)).click();
		CommonUtilityApplication.threadWait(3000);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.MaventUIRegression_EditLoanData)).click();
		CommonUtilityApplication.threadWait(3000);
	}
}