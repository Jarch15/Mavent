package com.elliemae.pageobject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventTILHUDPageConsts;
import com.elliemae.consts.MaventUITabConst;
import com.elliemae.consts.MiBuyConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;




public class MaventUITabVerifyPage
{
	
	public static Logger _log = Logger.getLogger(JMSQueueMetricsPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();
	List<Integer> tabIndex = new ArrayList<Integer>();

	public MaventUITabVerifyPage(WebDriver driver)
	{
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);

	}
	
		
	public void verifyTav_loanData()
	{

				verifyMaventTab();		
				System.out.println("Successfully Tab Verified for LoanData Page");	
				System.out.println();
				System.out.println();
	
	}
	
	
	public void VerifyTab_TILHUD()
	{
		driver.findElement(By.xpath(MaventUITabConst.TIL_HUD_LINK)).click();
		verifyMaventTab();
		System.out.println("Successfully Tab Verified for TILHUD Page");
		System.out.println();
		System.out.println();
		
	}
	
	public void VerifyTab_MIBUYDOWN()
	{
		
		driver.findElement(By.xpath(MaventUITabConst.MI_BUY_LINK)).click();			
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		verifyMaventTab();
		System.out.println("Successfully Tab Verified for MIBUYDOWN Page");
		System.out.println();
		System.out.println();
		
	}
	
	public void VerifyTab_RESPA()
	{
		driver.findElement(By.xpath(MaventUITabConst.RESPA_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		verifyMaventTab();
		System.out.println("Successfully Tab Verified for Respa Page");
		System.out.println();
		System.out.println();
		
	}
	
	
	public void VerifyTab_AbilityToRepayQM()
	{
		driver.findElement(By.xpath(MaventUITabConst.AbilityToRepayQM_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		verifyMaventTab();
		System.out.println("Successfully Tab Verified for AbilityToRepayQM Page");
		System.out.println();
		System.out.println();
		
	}
	

	
	public void VerifyTab_AdvanceSetting()
	{
		driver.findElement(By.xpath(MaventUITabConst.AdvanceSetting_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		verifyMaventTab();
		System.out.println("Successfully Tab Verified for AdvanceSetting Page");
		System.out.println();
		System.out.println();
		
	}
	
			
	public void verifyMaventTab()
	{
		List<WebElement> LOANDATA_AllELEMENTS = driver.findElements(By.xpath(MaventUITabConst.LOANDATA_AllELEMENTS));
		Iterator<WebElement> iterator = LOANDATA_AllELEMENTS.iterator();
		
//		while (iterator.hasNext()) {
//		    WebElement name = iterator.next();
//		    //name.sendKeys(Keys.TAB);
//		    
//		    if(name.isEnabled() && name.isDisplayed())
//		    {
//		    System.out.println(name.getAttribute("name") +  " "  + name.getAttribute("TabIndex"));		    
//		    name.sendKeys(Keys.TAB);
//		    }
//		}   
		
		int elesize = LOANDATA_AllELEMENTS.size();
		
			for(int i=0; i<elesize;i++)
			{
			    
			    if(LOANDATA_AllELEMENTS.get(i).isDisplayed() && LOANDATA_AllELEMENTS.get(i).isEnabled())
			    	LOANDATA_AllELEMENTS.get(i).sendKeys(Keys.TAB);
			    		    	
			    tabIndex.add(Integer.parseInt(LOANDATA_AllELEMENTS.get(i).getAttribute("tabindex")));
			    	System.out.println("Tab to element" + LOANDATA_AllELEMENTS.get(i).getAttribute("name") + "   " + "TabIndex" + "  " + LOANDATA_AllELEMENTS.get(i).getAttribute("tabindex"));
			   
			    
			}
			
//			Collections.sort(tabIndex);
//			
//			for(int i=0; i<elesize;i++)
//			{
//			    
//			    if(LOANDATA_AllELEMENTS.get(i).isDisplayed() && LOANDATA_AllELEMENTS.get(i).isEnabled())
//			    LOANDATA_AllELEMENTS.get(i).sendKeys(Keys.TAB);
//			    		    	
//			    
//			    	System.out.println("Tab to element" + LOANDATA_AllELEMENTS.get(i).getAttribute("name") + "   " + "TabIndex" + "  " + tabIndex.get(i));
//			   
//			    
//			}
//					

			
			
		
	}
	
	
}