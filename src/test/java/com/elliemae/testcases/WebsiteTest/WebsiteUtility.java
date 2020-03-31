package com.elliemae.testcases.WebsiteTest;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

public class WebsiteUtility 
{
	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;
	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();
	
	public static Logger _log = Logger.getLogger(WebsiteUtility.class);
	WebsiteUtility(WebDriver driver)
	{
		this.driver = driver;
		
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
		
	}
	
	
	
	/* Author : Nidhi Khandelwal
	 * Description : This method navigates to the configured Mavent Portal.
	 * It returns the Title of the webpage back to the caller after navigation is complete
	 *  
	 *  */
	public String navigateToPortal() {

		EllieMaeLog.log(_log, "Navigating to the Mavent URL", EllieMaeLogLevel.reporter);

		try
		{
			driver.get(envData.get("MAVENTURL"));		
			//objEllieMaeActions.waitForPageToLoad("5000");
		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Navigating to the Mavent URL Failed", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}

		EllieMaeLog.log(_log, "Navigation is complete", EllieMaeLogLevel.reporter);
		
		//CommonUtilityApplication.threadWait(3000);
		return driver.getTitle();
	}

	/* Author : Nidhi Khandelwal
	 * Description : This method does login of configured user into the Mavent Portal.
	 * It returns back the logged in user name back to the caller.
	 *  
	 *  */
	
	public String loginToPortal(HashMap<String, String> testData,String user) {

		CommonUtilityApplication.threadWait(3000);
		EllieMaeLog.log(_log, "Login to the Portal", EllieMaeLogLevel.reporter);	
		
		String loginUserName = user;
		loginUserName.trim();
		
		System.out.println(loginUserName);
	//	driver.manage().deleteAllCookies();	
		
		WebElement Loginelement = driver.findElement(By.name(WebsiteConsts.LOGIN_USER_ID_FLD));
				Loginelement.sendKeys(loginUserName);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(WebsiteConsts.LOGIN_PWD_FLD)).sendKeys(testData.get("Password"));
		System.out.println(testData.get("Password"));
		driver.findElement(By.name(WebsiteConsts.LOGIN_PWD_FLD)).sendKeys(Keys.TAB);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.name(WebsiteConsts.LOGIN_COMPANY)).sendKeys(testData.get("Company"));		
		System.out.println(testData.get("Company"));
		driver.findElement(By.name(WebsiteConsts.LOGIN_COMPANY)).sendKeys(Keys.TAB);
		
		CommonUtilityApplication.threadWait(1000);
		
		driver.findElement(By.xpath(WebsiteConsts.LOGIN_BTN)).click();
		
		CommonUtilityApplication.threadWait(3000);
		
		return loginUserName ;

	}
	
	

	public String getloggedUser(HashMap<String, String> testData)
	{
		
		
		String loggedUser = null;
		loggedUser = driver.findElement(By.xpath(MaventPortalMenuConsts.LOGGED_USER_XPATH)).getText();
		EllieMaeLog.log(_log, "Logged user : "+loggedUser, EllieMaeLogLevel.reporter);
		return loggedUser;
	}
	
	
	
	/* Author : Nidhi Khandelwal
	 * Description : This method does logout of the current user from the Mavent Portal.
	 * It returns the webpage title back to the caller once it successfully logs out.
	 *  */
	public String logOut() {
		/* Click on Log out link */
		EllieMaeLog.log(_log, "Logging out of the portal", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventPortalMenuConsts.LOG_OUT_LINK_PARTIAL_LINKTEXT)).click();
		EllieMaeLog.log(_log, "Log out is Successfull", EllieMaeLogLevel.reporter);
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return driver.getTitle();
	}


}
