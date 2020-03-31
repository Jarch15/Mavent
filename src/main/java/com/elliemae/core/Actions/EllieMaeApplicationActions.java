package com.elliemae.core.Actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.core.Logger.EllieMaeLog;

/**
 * <b>Name:</b> EllieMaeApplicationActions  
 * <b>Description: </b>This class is extending EllieMaeActions class and is used to call Actions related to driver. 
 *
 * @author <i>Supreet Singh</i>
 */

public class EllieMaeApplicationActions extends EllieMaeActions {

	public static Logger _log = Logger.getLogger(EllieMaeApplicationActions.class); 
	
	public EllieMaeApplicationActions(WebDriver driver) {
		super(driver);		
	}
	
	/**
	 * <b>Name:</b> isWebElementExists</br> 
	 * <b>Description: </b>method to check if particular web element is present and displayed on page.</br>
	 * 
	 * @param locator
	 */
	public boolean isWebElementExist(By locator) 
	{
		EllieMaeLog.log(_log, "Check if the Element is present on the Page "+locator.toString());
		if(driver.findElements(locator).size() != 0)
		{
			WebElement webElement = driver.findElement(locator);
			if(webElement.isDisplayed())
			{
				return true;
			}
		}

		return false;

	}
	
	/**
	 * <b>Name:</b> waitUntilVisibilityOfElement</br> 
	 * <b>Description: </b>Method to add an explicit wait until visibility of
	 * element located.</br>
	 * 
	 * @param locator
	 * @param timeout
	 */
	public void waitUntilVisibilityOfElement(By locator, long timeout)
	{
		try
		{
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception occurred during waitUntilVisibilityOfElement "+locator.toString());
			throw e;
		}
	}
	
}