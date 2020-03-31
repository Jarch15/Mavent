package com.elliemae.pageobject;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.elliemae.consts.MaventPortalMenuConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.asserts.Assert;

public class MaventAdminInfoPage {
	
	public static Logger _log = Logger.getLogger(MaventAdminInfoPage.class);
	
	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;
	
	public MaventAdminInfoPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	
	//Methods 
	
	//Navigate to the Admininstration -> Customer Set Up Page 
	
	public String gotoAdminInfoScreenPage(){
		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION)).click();
		driver.findElement(By.xpath(MaventPortalMenuConsts.MNU_ADMINISTRATION_CUSTOMER_SETUP)).click();
		return driver.getTitle();
	}
	
	//Select the company id from Company input box
	
	public void setCompanyName(String CompanyName){
		driver.findElement(By.xpath(MaventPortalMenuConsts.CMPNY_ID_TXTBOX)).sendKeys(CompanyName);
	    driver.findElement(By.name(MaventPortalMenuConsts.BTN_UPDATE)).click();
	}
	
   public void verifyInfoPage(String ID){
	   
	   //Verify the Info Link
	     String stringTxt;
	     stringTxt = driver.findElement(By.className(MaventPortalMenuConsts.LBL_DEFAULT)).getText();
		Assert.assertEquals(stringTxt, "Info", "Verify that Info page lable");
		
		//Verify the Company Info Label
		
		stringTxt = driver.findElement(By.xpath(MaventPortalMenuConsts.LBL_COMPNYINFO)).getText();
		Assert.assertEquals(stringTxt, "COMPANY INFO", "Verify the Company Info Label");
		
		//Verify the ID Label and Value
		
		stringTxt = driver.findElement(By.xpath(MaventPortalMenuConsts.LBL_ID)).getText();
		Assert.assertEquals(stringTxt, "ID:", "Verify the ID Label");
		
		stringTxt = driver.findElement(By.xpath(MaventPortalMenuConsts.ID)).getText();
		Assert.assertEquals(stringTxt, ID, "Verify the ID");
		
		//Validate that ID is not editable
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.ID)).sendKeys("0000012345");
		
		String idVal = driver.findElement(By.xpath(MaventPortalMenuConsts.ID)).getText();
		
		Assert.assertEquals(idVal, "1234567890", "ID Field is editable");		
				
		//Verify the Name Label
		
		stringTxt = driver.findElement(By.xpath(MaventPortalMenuConsts.LBL_Name)).getText();
		Assert.assertEquals(stringTxt, "Name:", "Verify the Name Label");
		
		//Verify the Name of the Company is not editable
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.Name)).clear();
		
		driver.findElement(By.xpath(MaventPortalMenuConsts.Name)).sendKeys("Test Company 2");
		
		idVal = driver.findElement(By.xpath(MaventPortalMenuConsts.Name)).getAttribute("value");
		
		Assert.assertEquals(idVal, "Test Company 2", "Name Field is editable");
		
		//Verify the locations which are deleted are not shown 
		
		for(int i=0; i<=driver.findElements(By.xpath("//form[@name='companyForm']/table/tbody/tr[10]/td[2]/table/tbody/tr/td/select/option")).size();i++){
			
			stringTxt = driver.findElements(By.xpath("//form[@name='companyForm']/table/tbody/tr[10]/td[2]/table/tbody/tr/td/select/option")).get(i).getText();
			
			
			System.out.println(stringTxt);
		
			Assert.assertEquals(stringTxt.contains("Deleted"), false, "Deleted Location Exist");
			
			if (stringTxt.contains("Deleted")){
			
			break;		
			
			}
			
			
		}
			
	   
   }
	
}