package com.elliemae.pageobject;

import static com.elliemae.consts.MaventAbilityToRepayPageConsts.ATR_SimultaneousLPA;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventAbilityToRepayPageConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;

/**
 * <b>Name:</b> MaventTILHUDPage</br>
 * <b>Description: </b>This page object class for Mavent Portal Abiloty To Repay Page.</br>
 * 
 * @author <i>Nidhi Khandelwal</i>
 */
public class MaventAbilityToRepayPage {

	public static Logger _log = Logger.getLogger(MaventAbilityToRepayPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;	

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();

	public MaventAbilityToRepayPage(WebDriver driver) {
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		userData = EnvironmentData.getUserListDataPerUserKey(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);
	}
	
	public boolean navigateToAbilityToRepagePage() 
	{
		EllieMaeLog.log(_log, "Navigating to Ability To Repay page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.AbilityToRepayQM_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean populateATRQualifiedMortgage(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Populating the Ability to Repay/Qualified Mortgage Type on ATR page...", EllieMaeLogLevel.reporter);
		Select ATR_LoanTypedropDown = new Select(driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_LoanType)));		
		ATR_LoanTypedropDown.selectByValue(testData.get("ATR_LoanType"));

		Select ATR_transactionExdropDown = new Select(driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_transactionExemptFromATRReqFlag)));		
		ATR_transactionExdropDown.selectByValue(testData.get("ATR_transactionEx"));
		
		Select ATR_loanSubdropDown = new Select(driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_loanSubToForwardCommitmentFlag)));		
		ATR_loanSubdropDown.selectByValue(testData.get("ATR_loanSub"));
		Select ATR_loanSolddropDown = new Select(driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_loanSoldOrTransAfterCons)));		
		
		ATR_loanSolddropDown.selectByValue(testData.get("ATR_loanSold"));
		
		Select ATR_creditPdropDown = new Select(driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_creditPurpose)));		
		ATR_creditPdropDown.selectByValue(testData.get("ATR_creditP"));
		
		CommonUtilityApplication.threadWait(3000);
					
		EllieMaeLog.log(_log, "Populating ATRQualifiedMortgage filds on Ability To Repay page complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	
	public boolean populateATRProposedHousing(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Populating the Ability to Repay/Qualified Mortgage Type on ATR page...", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(ATR_SimultaneousLPA)).click();
		driver.findElement(By.xpath(ATR_SimultaneousLPA)).sendKeys(testData.get("ATR_SimulPA"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_hazardInsAmt)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_hazardInsAmt)).sendKeys(testData.get("ATR_hazardInsAmt"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_realEstTaxAmt)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_realEstTaxAmt)).sendKeys(testData.get("ATR_realEstTaxAmt"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_MIAmt)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_MIAmt)).sendKeys(testData.get("ATR_MIAmt"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_HOAAmt)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_HOAAmt)).sendKeys(testData.get("ATR_HOAAmt"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_OtherAmount)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_OtherAmount)).sendKeys(testData.get("ATR_OtherAmount"));		
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_NetRentalIncome)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_NetRentalIncome)).sendKeys(testData.get("ATR_NetRentalIncome"));
		
		CommonUtilityApplication.threadWait(3000);
					
		EllieMaeLog.log(_log, "Populating ATRQualifiedMortgage filds on Ability To Repay page complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	
	public boolean populateAgencyGSEElig(HashMap<String, String> testData)
	{
		EllieMaeLog.log(_log, "Populating the Ability to Repay/Qualified Mortgage Type on ATR page...", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_ComplianceAtCons)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_ComplianceAtCons)).sendKeys(testData.get("ATR_ComplianceAtCons"));	
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_varFromStandardsAgreement)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_varFromStandardsAgreement)).sendKeys(testData.get("ATR_varFromStandardsAgr"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_loanWaiverGranted)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_loanWaiverGranted)).sendKeys(testData.get("ATR_loanWaiverGrd"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_agencyAUSType)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_agencyAUSType)).sendKeys(testData.get("ATR_agencyAUSType"));
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_desktopUwrUWR)).click();
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_desktopUwrUWR)).sendKeys(testData.get("ATR_desktopUwrUWR"));
		
		
		CommonUtilityApplication.threadWait(3000);
					
		EllieMaeLog.log(_log, "Populating ATRQualifiedMortgage filds on Ability To Repay page complete", EllieMaeLogLevel.reporter);
		
		return true;
	}
	
	public boolean populateUnderwritingFactors(HashMap<String, String> testData)
	{
		WebElement ATR_monthlyCoveredLP = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_monthlyCoveredLP));
		if(!ATR_monthlyCoveredLP.isSelected())
		{
			ATR_monthlyCoveredLP.click();
		}
		
		WebElement ATR_monthlySimulLoanPayEFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_monthlySimulLoanPayEFlagCB));
		if(!ATR_monthlySimulLoanPayEFlagCB.isSelected())
		{
			ATR_monthlySimulLoanPayEFlagCB.click();
		}
		
		WebElement ATR_mortgageRelatedOblEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_mortgageRelatedOblEvalFlagCB));
		if(!ATR_mortgageRelatedOblEvalFlagCB.isSelected())
		{
			ATR_mortgageRelatedOblEvalFlagCB.click();
		}
		
		
		WebElement ATR_debtObligationsEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_debtObligationsEvalFlagCB));
		if(!ATR_debtObligationsEvalFlagCB.isSelected())
		{
			ATR_debtObligationsEvalFlagCB.click();
		}
		
		WebElement ATR_childSupportOblEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_childSupportOblEvalFlagCB));
		if(!ATR_childSupportOblEvalFlagCB.isSelected())
		{
			ATR_childSupportOblEvalFlagCB.click();
		}
		
		WebElement ATR_alimonyObligationsEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_alimonyObligationsEvalFlagCB));
		if(!ATR_alimonyObligationsEvalFlagCB.isSelected())
		{
			ATR_alimonyObligationsEvalFlagCB.click();
		}
		
		WebElement ATR_currentEmploymentStatEvFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentEmploymentStatEvFlagCB));
		if(!ATR_currentEmploymentStatEvFlagCB.isSelected())
		{
			ATR_currentEmploymentStatEvFlagCB.click();
		}
		
		
		WebElement ATR_currentOrExpectedIncoEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedIncoEvalFlagCB));
		if(!ATR_currentOrExpectedIncoEvalFlagCB.isSelected())
		{
			ATR_currentOrExpectedIncoEvalFlagCB.click();
		}
		
		
		WebElement ATR_currentOrExpectedAssetsEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedAssetsEvalFlagCB));
		if(!ATR_currentOrExpectedAssetsEvalFlagCB.isSelected())
		{
			ATR_currentOrExpectedAssetsEvalFlagCB.click();
		}
		
		WebElement ATR_creditHistoryEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_creditHistoryEvalFlagCB));
		if(!ATR_creditHistoryEvalFlagCB.isSelected())
		{
			ATR_creditHistoryEvalFlagCB.click();
		}
		
		
		WebElement ATR_DTIRatioEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_DTIRatioEvalFlagCB));
		if(!ATR_DTIRatioEvalFlagCB.isSelected())
		{
			ATR_DTIRatioEvalFlagCB.click();
		}
		
		WebElement ATR_residualIncomeEvalFlagCB = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_residualIncomeEvalFlagCB));
		if(!ATR_residualIncomeEvalFlagCB.isSelected())
		{
			ATR_residualIncomeEvalFlagCB.click();
		}
		
		
		WebElement ATR_approvedBy = driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_approvedBy));
		ATR_approvedBy.clear();
		ATR_approvedBy.sendKeys(testData.get("ATR_approvedBy"));
		
		
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_monthlySimulLoanPayEFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_mortgageRelatedOblEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_debtObligationsEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_childSupportOblEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_alimonyObligationsEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentEmploymentStatEvFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedIncoEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedAssetsEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_creditHistoryEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_DTIRatioEvalFlagCB)).click();
//		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_residualIncomeEvalFlagCB)).click();
	//	driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.ATR_approvedBy)).sendKeys(testData.get("ATR_approvedBy"));
		return true;
	}
	
	
	
	
	
	
	
	}