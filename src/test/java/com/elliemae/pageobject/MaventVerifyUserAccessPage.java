package com.elliemae.pageobject;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;

/**
 * <b>Name:</b> MaventVerifyUserAccesPage</br>
 * <b>Description: </b>This is page object class for Mavent User Access Page.</br>
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class MaventVerifyUserAccessPage {

	public static Logger _log = Logger.getLogger(MaventVerifyUserAccessPage.class);

	WebDriver driver;


	public MaventVerifyUserAccessPage(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}
	
	public boolean loginToPortal(String username, String password, String company) {

		CommonUtilityApplication.threadWait(3000);
		EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);	
		
		WebElement Loginelement = driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_USER_ID_FLD));
				Loginelement.sendKeys(username);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(password);
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_PWD_FLD)).sendKeys(Keys.TAB);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(company);		
		driver.findElement(By.name(MaventPortalMenuConsts.LOGIN_COMPANY)).sendKeys(Keys.TAB);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.LOGIN_BTN)).click();
		
		CommonUtilityApplication.threadWait(3000);
		
		return true ;
	}

	
}