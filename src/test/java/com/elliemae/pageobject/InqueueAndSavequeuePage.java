package com.elliemae.pageobject;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.EnvironmentData;
import java.awt.AWTException;


public class InqueueAndSavequeuePage {
	
	
public static Logger _log = Logger.getLogger(MaventPage.class);
	
	WebDriver driver;
	
	protected HashMap<String, String> envData1 = new HashMap<>();
	protected HashMap<String, String> envData2 = new HashMap<>();
	protected HashMap<String, String> envData3 = new HashMap<>();
	protected HashMap<String, String> envData4 = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();
	public InqueueAndSavequeuePage(WebDriver driver){
		
		this.driver = driver;
		
		envData1 = EnvironmentData.getEnvironmentData("JMSPROD1");
		envData2 = EnvironmentData.getEnvironmentData("JMSPROD2");
		envData3 = EnvironmentData.getEnvironmentData("JMSPROD3");
		envData4 = EnvironmentData.getEnvironmentData("JMSPROD4");
		userData = EnvironmentData.getUserListDataPerUserKey("UserJMSTest");

}
	
	
	public void gotoPage(){
		
		try {
			Thread.sleep(5000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			EllieMaeLog.log(_log, "Navigating to the URL", EllieMaeLogLevel.reporter);
			
			driver.get(envData1.get("MAVENTURL"));
			
			EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);
			
			
			try {
				Thread.sleep(2000);
			} 
			
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
	
	
public void Login() throws AWTException, InterruptedException{
		
		try {
			Thread.sleep(5000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			EllieMaeLog.log(_log, "Navigating to the URL", EllieMaeLogLevel.reporter);
			
			Robot rb=new Robot();
			
			StringSelection username = new StringSelection("adminuser");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, null);
			
			rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
	        rb.keyPress(java.awt.event.KeyEvent.VK_V);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_V);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

	        //tab to password entry field
	        rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
	        Thread.sleep(2000);

	        //Enter password by ctrl-v
	        StringSelection pwd = new StringSelection("pass@123");
	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
	        rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
	        rb.keyPress(java.awt.event.KeyEvent.VK_V);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_V);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

	        //press enter
	        rb.keyPress(java.awt.event.KeyEvent.VK_ENTER);
	        rb.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
			
			EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);
			
			
			try {
				Thread.sleep(2000);
			} 
			
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
	
	
	
}
