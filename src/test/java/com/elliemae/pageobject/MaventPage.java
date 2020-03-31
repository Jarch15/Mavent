package com.elliemae.pageobject;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.EnvironmentData;



public class MaventPage {

	
	public static Logger _log = Logger.getLogger(MaventPage.class);
	
	WebDriver driver;
	
	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();
	
	public MaventPage(WebDriver driver){
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
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
		
		driver.get(envData.get("MAVENTURL"));
		
		EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);
		
		
		try {
			Thread.sleep(5000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


public void Login(){
	
	try {
		Thread.sleep(2000);
	} 
	
	catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
		EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);
		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD)).sendKeys(userData.get("UserName"));
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(userData.get("Password"));
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys("1234567890");
		driver.findElement(By.className(MaventPortalMenuConsts.LOGIN_BTN)).click();
		
		
		
		EllieMaeLog.log(_log, "Login is complete", EllieMaeLogLevel.reporter);
		
		
		try {
			Thread.sleep(2000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public String ValidateFootNote(){
		
		try {
			Thread.sleep(2000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EllieMaeLog.log(_log, "Click on Help Menu", EllieMaeLogLevel.reporter);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.HELP)).click();
		
		try {
			Thread.sleep(2000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EllieMaeLog.log(_log, "Click on Help Menu", EllieMaeLogLevel.reporter);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.ABOUT)).click();
				
		EllieMaeLog.log(_log, "Validate the footnote on the Portal", EllieMaeLogLevel.reporter);
		
		String FootNote;
		
		FootNote = driver.findElement(By.xpath(MaventPortalMenuConsts.FOOTNOTE)).getText();
		

		try {
			Thread.sleep(2000);
		} 
		
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return FootNote;
		
	}
	
}
