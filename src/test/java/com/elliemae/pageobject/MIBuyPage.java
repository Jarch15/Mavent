package com.elliemae.pageobject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MiBuyConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.asserts.Assert;


public class MIBuyPage
{
	
	public static Logger _log = Logger.getLogger(JMSQueueMetricsPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MIBuyPage(WebDriver driver)
	{
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);

	}
	
	public boolean navigateToMIBuyPage() 
	{
		EllieMaeLog.log(_log, "Navigating to MI-Buy page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MiBuyConsts.MI_BUY_DOWN_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	 
	public void verifyMIbuylink(HashMap<String, String> testData)
	{
	
		EllieMaeLog.log(_log, "Populating data on MI Buy Page...",EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitForPageToLoad("5000");
		
		//driver.findElement(By.xpath(MiBuyConsts.miFlag_radio1)).click();
		
		identifyelement(MiBuyConsts.miFlag_radio1).click();
			
		CommonUtilityApplication.threadWait(2000);
		
		Select dropDown = new Select(driver.findElement(By.xpath(MiBuyConsts.MI_TYPE_DROPDOWN_XPATH)));
		dropDown.selectByValue(testData.get("MI_TYPE"));
		
		identifyelement(MiBuyConsts.UPFRONT_MIP_FACTOR_TEXT_XPATH).sendKeys(testData.get("UPFRONT_MIP_FACTOR"));
		
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Months_txt1)).sendKeys(testData.get("miInfo_Months1"));
		
		identifyelement(MiBuyConsts.miInfo_Months_txt1).sendKeys(testData.get("miInfo_Months1"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Months_txt2)).sendKeys(testData.get("miInfo_Months2"));
		
		identifyelement(MiBuyConsts.miInfo_Months_txt2).sendKeys(testData.get("miInfo_Months2"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Months_txt3)).sendKeys(testData.get("miInfo_Months3"));
		
		identifyelement(MiBuyConsts.miInfo_Months_txt3).sendKeys(testData.get("miInfo_Months3"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Months_txt4)).sendKeys(testData.get("miInfo_Months4"));
		
		identifyelement(MiBuyConsts.miInfo_Months_txt4).sendKeys(testData.get("miInfo_Months4"));
		
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Rate_txt1)).sendKeys(testData.get("miInfo_Rate1"));
		
		identifyelement(MiBuyConsts.miInfo_Rate_txt1).sendKeys(testData.get("miInfo_Rate1"));
		
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Rate_txt2)).sendKeys(testData.get("miInfo_Rate2"));
		
		identifyelement(MiBuyConsts.miInfo_Rate_txt2).sendKeys(testData.get("miInfo_Rate2"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Rate_txt3)).sendKeys(testData.get("miInfo_Rate3"));
		
		identifyelement(MiBuyConsts.miInfo_Rate_txt3).sendKeys(testData.get("miInfo_Rate3"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.miInfo_Rate_txt4)).sendKeys(testData.get("miInfo_Rate4"));
		
		identifyelement(MiBuyConsts.miInfo_Rate_txt4).sendKeys(testData.get("miInfo_Rate4"));

		
		//BuyDown data 
		
		driver.findElement(By.xpath(MiBuyConsts.MI_BUYDOWNDATA_RADIO)).click();
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Txt1)).sendKeys(testData.get("ButDowndata_Txt1"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Txt1).sendKeys(testData.get("ButDowndata_Txt1"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Txt2)).sendKeys(testData.get("ButDowndata_Txt2"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Txt2).sendKeys(testData.get("ButDowndata_Txt2"));
	
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Txt3)).sendKeys(testData.get("ButDowndata_Txt3"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Txt3).sendKeys(testData.get("ButDowndata_Txt3"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Txt4)).sendKeys(testData.get("ButDowndata_Txt4"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Txt4).sendKeys(testData.get("ButDowndata_Txt4"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Rate_Txt1)).sendKeys(testData.get("miInfo_Months4"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Rate_Txt1).sendKeys(testData.get("Buydownrate_Txt1"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Rate_Txt2)).sendKeys(testData.get("Buydownrate_Txt2"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Rate_Txt2).sendKeys(testData.get("Buydownrate_Txt1"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Rate_Txt3)).sendKeys(testData.get("Buydownrate_Txt3"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Rate_Txt3).sendKeys(testData.get("Buydownrate_Txt3"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.BUYDOWNDATA_Rate_Txt4)).sendKeys(testData.get("Buydownrate_Txt4"));
		
		identifyelement(MiBuyConsts.BUYDOWNDATA_Rate_Txt4).sendKeys(testData.get("Buydownrate_Txt4"));
		

		//USDA/RHS
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Months_Txt1)).sendKeys(testData.get("Usda_Month_txt1"));
		
		identifyelement(MiBuyConsts.USDARHS_Months_Txt1).sendKeys(testData.get("Usda_Month_txt1"));
		
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Months_Txt2)).sendKeys(testData.get("Usda_Month_txt2"));
		
		identifyelement(MiBuyConsts.USDARHS_Months_Txt2).sendKeys(testData.get("Usda_Month_txt2"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Months_Txt3)).sendKeys(testData.get("Usda_Month_txt3"));
		
		identifyelement(MiBuyConsts.USDARHS_Months_Txt3).sendKeys(testData.get("Usda_Month_txt3"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Months_Txt4)).sendKeys(testData.get("Usda_Month_txt4"));
		
		identifyelement(MiBuyConsts.USDARHS_Months_Txt4).sendKeys(testData.get("Usda_Month_txt4"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Rate_Txt1)).sendKeys(testData.get("Usda_Rate_txt1"));
		
		identifyelement(MiBuyConsts.USDARHS_Rate_Txt1).sendKeys(testData.get("Usda_Rate_txt1"));
		
		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Rate_Txt2)).sendKeys(testData.get("Usda_Rate_txt2"));
		
		identifyelement(MiBuyConsts.USDARHS_Rate_Txt2).sendKeys(testData.get("Usda_Rate_txt2"));

		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Rate_Txt3)).sendKeys(testData.get("Usda_Rate_txt3"));
		
		identifyelement(MiBuyConsts.USDARHS_Rate_Txt3).sendKeys(testData.get("Usda_Rate_txt3"));

		CommonUtilityApplication.threadWait(3000);
		//driver.findElement(By.xpath(MiBuyConsts.USDARHS_Rate_Txt4)).sendKeys(testData.get("Usda_Rate_txt4"));
		
		identifyelement(MiBuyConsts.USDARHS_Rate_Txt4).sendKeys(testData.get("Usda_Rate_txt4"));
		
		EllieMaeLog.log(_log, "Populating data on MI Buy Page Complete",EllieMaeLogLevel.reporter);
	
	}
	
	/* Author : Nidhi Khandelwal
	 * Description : This is to verify data present in MIdata page without saving 
	 * the data and clicking on TILHUB page.
	 *  */	
	public void verfifyMIdata_AfterTILHuBclick(HashMap<String, String> testData)
	  {
		  	CommonUtilityApplication.threadWait(3000);
		  	driver.findElement(By.xpath(MiBuyConsts.MI_BUY_DOWN_LINK)).click();
		    CommonUtilityApplication.threadWait(3000);
			String monthtxtdat = identifyelement(MiBuyConsts.miInfo_Months_txt1).getText();
			Assert.assertEquals(monthtxtdat,testData.get("miInfo_Months1"),"Data not matches on MI Buy page");
	  }
	
	
	public WebElement identifyelement(String xpath)
	{
		
		WebElement element = driver.findElement(By.xpath(xpath));
		
		return element;
	
	}
	
	public void setMiFlag(boolean flag)
	{
		if(flag)
		{
			driver.findElement(By.xpath(MiBuyConsts.miFlag_radio1)).click();
		}
		else
		{
			driver.findElement(By.xpath(MiBuyConsts.miFlag_radio_NO)).click();
		}
		CommonUtilityApplication.threadWait(2000);
	}
	
	public void setBuyDownFlag(boolean flag)
	{
		if(flag)
		{
			driver.findElement(By.xpath(MiBuyConsts.buyDownFlag_radio_Yes)).click();
		}
		else
		{
			driver.findElement(By.xpath(MiBuyConsts.buyDownFlag_radio_NO)).click();
		}
		CommonUtilityApplication.threadWait(2000);
	}

}
