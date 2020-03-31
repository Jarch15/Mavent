package com.elliemae.pageobject;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.asserts.Assert;

/**
 * <b>Name:</b> JMSQueueMetricsPage</br>
 * <b>Description: </b>This page object class is used for verifying JMS Queue metrics Test Case.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class JMSQueueMetricsPage {

	public static Logger _log = Logger.getLogger(JMSQueueMetricsPage.class);

	WebDriver driver;

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public JMSQueueMetricsPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey("UserJMSTest");
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method navigates to the passed environment's JMS Admin Portal.
	 * which is being configured in environment data.
	 *  
	 *  */
	public void navigateToAdminPortal(String ENV) {
		threadWait(2000);
		EllieMaeLog.log(_log, "Navigating to the Admin Portal URL", EllieMaeLogLevel.reporter);

		switch(ENV)
		{
		    case "JMSURL_1": driver.get(envData.get("JMSURL_1"));break;  
		    case "JMSURL_2": driver.get(envData.get("JMSURL_2"));break;   
		    case "JMSURL_3": driver.get(envData.get("JMSURL_3"));break;   
		    case "JMSURL_4": driver.get(envData.get("JMSURL_4"));break;    
		}
		

		EllieMaeLog.log(_log, "Navigation to the Admin Portal URL complete", EllieMaeLogLevel.reporter);

		threadWait(2000);
	}

	/* Author : Jayesh Bhapkar
	 * Description : This method logins to the JMS Admin Portal
	 * by reading credentials from user data configured.
	 *  
	 *  */
	public void loginToAdminPortal() 
	{
		threadWait(2000);
		EllieMaeLog.log(_log, "Loging to the Admin Portal", EllieMaeLogLevel.reporter);

		try 
		{
			Robot rb = new Robot();

			StringSelection username = new StringSelection(userData.get("UserName"));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, null);

			rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
			rb.keyPress(java.awt.event.KeyEvent.VK_V);
			rb.keyRelease(java.awt.event.KeyEvent.VK_V);
			rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

			// tab to password entry field
			rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
			rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);

			threadWait(2000);

			// Enter password by ctrl-v
			StringSelection pwd = new StringSelection(userData.get("Password"));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
			rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
			rb.keyPress(java.awt.event.KeyEvent.VK_V);
			rb.keyRelease(java.awt.event.KeyEvent.VK_V);
			rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

			// press enter
			rb.keyPress(java.awt.event.KeyEvent.VK_ENTER);
			rb.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
		} 
		catch (AWTException exception) 
		{
			EllieMaeLog.log(_log, "AWT Exception occurred while Loging to the Admin Portal", EllieMaeLogLevel.reporter);
			exception.printStackTrace();
			Assert.assertTrue(false,"Login to the amdin portal failed due to exception");
		}

		EllieMaeLog.log(_log, "Loging to the Admin Portal is complete", EllieMaeLogLevel.reporter);

		threadWait(5000);

	}

	/* Author : Jayesh Bhapkar
	 * Description : This method returns the 2ComplyInQueue consumer value back to the caller
	 *  */
	public String get2ComplyInQueueValue() 
	{
		EllieMaeLog.log(_log, "Reading 2ComplyInQueue consumer values", EllieMaeLogLevel.reporter);
		threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.RUNTIME_LINK_XPATH)).click();
		threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.JMSDESTINATIONS_LINK_XPATH)).click();
		threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.VIEW_LINK_XPATH)).click();
		threadWait(2000);		
		driver.findElement(By.xpath(MaventPortalMenuConsts.COMPLY_IN_QUEUE_LINK_XPATH)).click();
		threadWait(2000);		
		CommonUtilityApplication.scrollPage(MaventPortalMenuConsts.READ_CONSUMER_COUNT_XPATH, driver);
		String complyInQueueValue = driver.findElement(By.xpath(MaventPortalMenuConsts.READ_CONSUMER_COUNT_XPATH)).getText();
		System.out.println("complyInQueueValue : "+complyInQueueValue);
		threadWait(2000);
		EllieMaeLog.log(_log, "Reading 2ComplyInQueue consumer values complete", EllieMaeLogLevel.reporter);
		return complyInQueueValue;
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method returns the 2ComplySaveQueue consumer value back to the caller
	 *  */
	public String get2ComplySaveQueueValue() 
	{		
		EllieMaeLog.log(_log, "Reading 2ComplySaveQueue consumer values", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventPortalMenuConsts.COMPLY_SAVE_QUEUE_LINK_XPATH)).click();
		threadWait(2000);		
		String complySaveQueueValue = driver.findElement(By.xpath(MaventPortalMenuConsts.READ_CONSUMER_COUNT_XPATH)).getText();
		System.out.println("complySaveQueueValue : "+complySaveQueueValue);
		threadWait(2000);
		EllieMaeLog.log(_log, "Reading 2ComplySaveQueue consumer values complete", EllieMaeLogLevel.reporter);
		return complySaveQueueValue;
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This method logs out current logged in user from the portal.
	 *  */
	public void logout()
	{
		EllieMaeLog.log(_log, "Logging out of the JMS Admin portal", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventPortalMenuConsts.ADMIN_USER_XPATH)).click();
		threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.ADMIN_USER_LOGOUT_XPATH)).click();
		threadWait(2000);
		driver.findElement(By.xpath(MaventPortalMenuConsts.ADMIN_USER_LOGOUT_CONFIRM_XPATH)).click();
		EllieMaeLog.log(_log, "Logged out of the JMS Admin portal", EllieMaeLogLevel.reporter);

		try
		{
		// If alert is coming, dismiss it
	    WebDriverWait wait = new WebDriverWait(driver, 5);
	    if(wait.until(ExpectedConditions.alertIsPresent())!=null)
	    	driver.switchTo().alert().dismiss();
		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Pop up after log out not present.", EllieMaeLogLevel.reporter);
		}
	}
	

	/*
	 * Author : Jayesh Bhapkar Description : This is an utility method to add a
	 * wait by using thread sleep for milliseconds provided as input argument.
	 */
	public void threadWait(int waitTime) {
		try {
			Thread.sleep(waitTime);
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
