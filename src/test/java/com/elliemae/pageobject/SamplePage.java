package com.elliemae.pageobject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.elliemae.consts.SampleConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;

/**
 * <b>Name:</b> SamplePage</br> 
 * <b>Description: </b>This page object class is
 * used to open google.com page.</br>
 * 
 *  @author <i>Supreet Singh</i>
 */
public class SamplePage {
	public static Logger _log=Logger.getLogger(SamplePage.class);
	
	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;
	
	public SamplePage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	/**
	 * <b>Name:</b> goToThePage<br>
	 * <b>Description:</b> This method is used to open the URL
	 * field
	 * 
	 * @param URL
	 */
	public void goToThePage(String URL) {
		EllieMaeLog.log(_log, "Entering URL: "+URL,EllieMaeLogLevel.reporter);
		objEllieMaeActions.goToThePage(URL);
	}
	
	/**
	 * <b>Name:</b> getTitle<br>
	 * <b>Description:</b> This method is used to get Title
	 * field
	 * 
	 */
	public String getTitle() {
		return driver.getTitle();	
	}
	
	@FindBy(id = SampleConsts.GOOGLE_SEARCH_TEXTBOX)
	WebElement searchTxtBox;
	@FindBy(id = SampleConsts.GOOGLE_SEARCH_BUTTON)
	WebElement searchBtn;
	
	public void setSearchTextBox(String value) {		
		objEllieMaeActions.type(searchTxtBox,value,"Type in Search TextBox");
	}
	
	public void clickSearchButton() {		
		objEllieMaeActions.click(searchBtn, "Click Search Button");
	}
}