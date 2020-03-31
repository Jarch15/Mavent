package com.elliemae.pageobject;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.TPOMVPConsts;
import com.elliemae.core.Actions.EllieMaeActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.EnvironmentData;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;


/**
 * <b>Name:</b> SamplePage 
 * <b>Description: </b>This page object class is used to open google.com page.
 * 
 *  @author <i>Supreet Singh</i>
 */
public class TPOMVPPage {
	public static Logger _log=Logger.getLogger(TPOMVPPage.class);
	
	WebDriver driver;
	EllieMaeActions objEllieMaeActions;
	NgWebDriver ngDriver;
	
	WebDriverWait wait;
	
	public HashMap<String, String> environmentData=new HashMap<String, String>();
	
	public TPOMVPPage(WebDriver driver){
		this.driver = driver;
		this.ngDriver= new NgWebDriver((JavascriptExecutor) driver);		
		
		PageFactory.initElements(driver, this);
		objEllieMaeActions = new EllieMaeActions(this.driver);
		
		environmentData=EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);

		for (Map.Entry<String, String> entry : environmentData.entrySet())
		{
			EllieMaeLog.log(_log,"Key: " + entry.getKey() +";    Value:"+ entry.getValue(),EllieMaeLogLevel.reporter);
		}

	}
	
	@FindBy(linkText = TPOMVPConsts.TPO_PIPELINE_LINK)
	WebElement pipeLineLink;
	
	@FindBy(linkText = TPOMVPConsts.TPO_ADDNEWLOAN_LINK)
	WebElement addNewLoanLink;
		
	/**
	 * <b>Name:</b> getTitle<br>
	 * <b>Description:</b> This method is used to get Title
	 * field
	 * 
	 */
	public String getTitle() {
		return driver.getTitle();	
	}
	
	/**
	 * <b>Name:</b> navigateToPage<br>
	 * <b>Description:</b> This method is used to open the URL field
	 * 	
	 */
	public void navigateToPage() {
		EllieMaeLog.log(_log, "Entering URL: "+environmentData.get("TPOMVPURL").trim(),EllieMaeLogLevel.reporter);				
		driver.get(environmentData.get("TPOMVPURL").trim());
		ngDriver.waitForAngularRequestsToFinish();
	}
	
	/**
	 * <b>Name:</b> navigateToPage<br>
	 * <b>Description:</b> This method is used to open the URL
	 * field
	 * 
	 * @param URL
	 */
	public void navigateToPage(String URL) {
		EllieMaeLog.log(_log, "Entering URL: "+URL,EllieMaeLogLevel.reporter);
		URL = (environmentData.get("TPOMVPURL").trim() == "" ? URL : environmentData.get("TPOMVPURL").trim());		
		driver.get(URL);
		ngDriver.waitForAngularRequestsToFinish();
	}
	
	public void setLoginUserName(String userName){
		EllieMaeLog.log(_log, "Entering UserName: "+userName,EllieMaeLogLevel.reporter);
		try{
			driver.findElement(ByAngular.model(TPOMVPConsts.LOGIN_USER_ID_FLD)).sendKeys(userName);			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		EllieMaeLog.log(_log, "Entered UserName",EllieMaeLogLevel.reporter);
	}
	
	public void setLoginPassword(String password){
		EllieMaeLog.log(_log, "Entering Password: "+password,EllieMaeLogLevel.reporter);
		driver.findElement(ByAngular.model(TPOMVPConsts.LOGIN_PWD_FLD)).sendKeys(password);
		//ngDriver.findElement(By.id("logPassword")).sendKeys(password);//buttonText(userName));
		EllieMaeLog.log(_log, "Entered Password",EllieMaeLogLevel.reporter);
	}
	
	public boolean loginButtonEnabled(){
		return driver.findElement(ByAngular.buttonText(TPOMVPConsts.LOGIN_BTN)).isEnabled();
	}
	
	public void clickLoginButton(){
		EllieMaeLog.log(_log, "Click Login Button",EllieMaeLogLevel.reporter);
		WebElement element = driver.findElement(ByAngular.buttonText(TPOMVPConsts.LOGIN_BTN));		
		element.click();
		ngDriver.waitForAngularRequestsToFinish();
		
		EllieMaeLog.log(_log, "Clicked Login Button",EllieMaeLogLevel.reporter);
		
	}
	
	public void clickPipeLineMenuButton(){
		EllieMaeLog.log(_log, "Click Pipeline Button",EllieMaeLogLevel.reporter);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pipeLineLink.click();
		ngDriver.waitForAngularRequestsToFinish();
		EllieMaeLog.log(_log, "Clicked Pipeline Button",EllieMaeLogLevel.reporter);
	}
	
	public void clickNewLoanButton(){
		EllieMaeLog.log(_log, "Click New Loan Button",EllieMaeLogLevel.reporter);
		addNewLoanLink.click();
		ngDriver.waitForAngularRequestsToFinish();
		EllieMaeLog.log(_log, "Clicked New Loan Button",EllieMaeLogLevel.reporter);
	}
	
	public boolean isCreateNewLoanButtonDisplayed(){
		EllieMaeLog.log(_log, "Check Create New Loan Button Displayed or not",EllieMaeLogLevel.reporter);
		return driver.findElement(ByAngular.buttonText("Create New Loan")).isDisplayed();		
	}
	
	public void clickCancelNewLoanButton(){
		EllieMaeLog.log(_log, "Click Cancel New Loan Button",EllieMaeLogLevel.reporter);
		driver.findElement(ByAngular.buttonText("Cancel")).click();
		EllieMaeLog.log(_log, "Clicked Cancel New Loan Button",EllieMaeLogLevel.reporter);
		ngDriver.waitForAngularRequestsToFinish();
	}
	
	
	public String getCurrentUrl(){
		return driver.getCurrentUrl();		
	}
	
	public String getWelcomeText(){
		WebElement element = driver.findElement(By.linkText("Welcome"));	
		return element.getText();		
	}
	
	public void getPipelineGridColumns(){
		Enumeration<WebElement> columnList = Collections.enumeration(driver.findElements(ByAngular.repeater("col in colContainer.renderedColumns track by col.uid")));
		EllieMaeLog.log(_log, "Grid Column Name using Repeater: ",EllieMaeLogLevel.reporter);
		while (columnList.hasMoreElements()){
			WebElement currentCol = columnList.nextElement();
			EllieMaeLog.log(_log, currentCol.getText(),EllieMaeLogLevel.reporter);
		
		}
	}
	
	public void clickFiltersDropdown(){
		EllieMaeLog.log(_log, "Click Filters DropDown",EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(".//*[@id='search']/button")).click();
		EllieMaeLog.log(_log, "Clicked Filters DropDown",EllieMaeLogLevel.reporter);
	}
	
	
	public void selectLoanNumberFilter(){
		EllieMaeLog.log(_log, "Select Loan Number Filter",EllieMaeLogLevel.reporter);
		WebElement element = driver.findElement(By.xpath(".//*[@id='search']/ul/li[5]/a"));
		element.click();
		ngDriver.waitForAngularRequestsToFinish();
		EllieMaeLog.log(_log, "Selected Loan Number Filter",EllieMaeLogLevel.reporter);
	}
	
	public void setFilterTextBoxValue(String filterValue){
		EllieMaeLog.log(_log, "Set Filter TextBox Value",EllieMaeLogLevel.reporter);
		WebElement element = driver.findElement(ByAngular.model("vm.valueFrom"));
		element.sendKeys(filterValue);
		ngDriver.waitForAngularRequestsToFinish();
		EllieMaeLog.log(_log, "Entered FilterTextbox value",EllieMaeLogLevel.reporter);
	}
	
	public void clickApplyFilterBtn(){
		EllieMaeLog.log(_log, "Click Apply Filter Button",EllieMaeLogLevel.reporter);
		driver.findElement(ByAngular.buttonText("Apply Filter")).click();
		EllieMaeLog.log(_log, "Clicked Apply Filter Button",EllieMaeLogLevel.reporter);
		ngDriver.waitForAngularRequestsToFinish();
	}
	
	public String getLoanNumberBinding(String loanNumber){
		return driver.findElement(ByAngular.binding(loanNumber)).getText();		
	}
	
	public String getUserName(){
		return driver.findElement(By.cssSelector(".tpo-user-name.theme-ul-parent-link-color")).getText();
		//return driver.findElement(By.className("tpo-user-name")).getText();
	}
	
	public void clickLoan(String loanNumber){
		EllieMaeLog.log(_log, "Click Loan with loanNumber: "+loanNumber,EllieMaeLogLevel.reporter);
		WebElement element = getLoanElement(loanNumber);
		element.click();
		EllieMaeLog.log(_log, "Clicked Loan",EllieMaeLogLevel.reporter);
	}
	
	private WebElement getLoanElement(String loanNumber){
		Enumeration<WebElement> loanList = Collections.enumeration(driver.findElements(By.cssSelector("span.loan-number-text")));
		WebElement expectedLoan = null;
		while (loanList.hasMoreElements()){
			expectedLoan = loanList.nextElement();
			EllieMaeLog.log(_log, "Current Loan: "+expectedLoan.getText(),EllieMaeLogLevel.reporter);
			if (expectedLoan.getText().equalsIgnoreCase(loanNumber) ){
				break;
			}			
		}		
		//List<WebElement> elements = driver.findElements(By.xpath("//div[@class='tpo-pipeline-loan-number-text']"));
		return expectedLoan;
	}

	public String getCurrentLoan(){
		ngDriver.waitForAngularRequestsToFinish();
		return driver.findElements(By.xpath("//div[@class='header-section ls-first-column']/div/div")).get(1).getText();		
	}
	
	public String getCurrentLoanAmount(){
		return driver.findElements(By.xpath("//div[@class='header-section ls-first-column']/div/div")).get(3).getText();	
	}
	
	public String getAbsoluteUrl(){
		return ngDriver.getLocationAbsUrl();
	}
	
	public String getLenderAccountManager(){
		return driver.findElement(By.cssSelector(".form-control.em-txt-lg")).getAttribute("value");
	}
	
	public void getLoanOfficer(){
		List<WebElement> elements = driver.findElements(ByAngular.options("vm.optBranch"));
		System.out.println(elements.size());
		
		
		System.out.println(elements.get(0).getText());
	}
	
	public void clickTpoTopLoginButton(){
		EllieMaeLog.log(_log, "Click TPO Top Login Button",EllieMaeLogLevel.reporter);
		WebElement element = driver.findElement(By.cssSelector(TPOMVPConsts.TPO_TOP_LOGIN_BTN));		
		element.click();
		ngDriver.waitForAngularRequestsToFinish();
		
		EllieMaeLog.log(_log, "Clicked TPO Top Login Button",EllieMaeLogLevel.reporter);		
	}
	
	public void clickLogoutLink(){
		EllieMaeLog.log(_log, "Click Logout Link",EllieMaeLogLevel.reporter);
		WebElement element = driver.findElement(By.linkText(TPOMVPConsts.TPO_LOGOUT_LINK));		
		element.click();
		ngDriver.waitForAngularRequestsToFinish();
		
		EllieMaeLog.log(_log, "Clicked Logout Button",EllieMaeLogLevel.reporter);
		
	}
	
}