package com.elliemae.testcases.maventui;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.MIBuyPage;
import com.elliemae.pageobject.MaventAbilityToRepayPage;
import com.elliemae.pageobject.MaventAdvanceSettingPage;
import com.elliemae.pageobject.MaventRESPAPage;
import com.elliemae.pageobject.MaventSOPPage;
import com.elliemae.pageobject.MaventTILHUDPage;

public class MaventUIRegressionTest extends EllieMaeApplicationBase 
{
	public static Logger _log = Logger.getLogger(MaventUIRegressionTest.class);
	
	/* Author : Jayesh Bhapkar
	 * Description : This is automated test method for mavent UI Regression test case.
	 *  */	
	@Test(dataProvider = "get-test-data-method")
	public void verifyMaventUI(HashMap<String, String> testData) 
	{
		MaventSOPPage sampleSOPPage = new MaventSOPPage(driver);
		MaventTILHUDPage maventTilHudPage = new MaventTILHUDPage(driver);
		MaventAbilityToRepayPage maventAbilityToRPage = new MaventAbilityToRepayPage(driver);
		MIBuyPage maventMIBuyPage = new MIBuyPage(driver);
		MaventRESPAPage maventRESPAPage = new MaventRESPAPage(driver);
		MaventAdvanceSettingPage maventAdvanceSPage = new MaventAdvanceSettingPage(driver);
		
		EllieMaeLog.log(_log, "Mavent UI Regression test Starts",EllieMaeLogLevel.reporter);
		
		// Navigate to the Mavent Portal
		String titleAfterNavigation = sampleSOPPage.navigateToPortal();
		Assert.assertEquals(titleAfterNavigation, "Login", "Navigation to the Mavent Portal Failed");
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_Login_Page", CommonUtility.currentTimeStamp);
		
		// Login to the Mavent Portal
		String loggedUser = sampleSOPPage.loginToPortal(testData);
		Assert.assertNotNull(loggedUser, "Login to the Mavent Portal Failed");
		Assert.assertEquals(loggedUser.isEmpty(), false, "Login to the Mavent Portal Failed");
		EllieMaeLog.log(_log, "Login is Successfull", EllieMaeLogLevel.reporter);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_Login_Success", CommonUtility.currentTimeStamp);
		
		// Create new Loan page
		sampleSOPPage.createLoan(testData);
		//ReverseProxy_180110115841
		//maventTilHudPage.searchLoan();
		
		// Populate mandatory data on loan data page
		sampleSOPPage.populateMandatoryFields(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_LoanData_page", CommonUtility.currentTimeStamp);
		
		/*  TIL-HUD page - test web elements*/
		
		// Navigate to Til-HUD page 
		maventTilHudPage.navigateToTILAHUDPage();
		
		// populate mandatory data on Til-HUD page
		maventTilHudPage.populateMandatoryFields(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TilHUD_page", CommonUtility.currentTimeStamp);
		
		// Template
		maventTilHudPage.applyTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Apply_Template", CommonUtility.currentTimeStamp);
		int numberOfColumnsBefore = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns before : "+numberOfColumnsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.addColumnBeforeForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_AddColumnBefore", CommonUtility.currentTimeStamp);
		int numberOfColumnsAfter = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns After : "+numberOfColumnsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfColumnsBefore+1, numberOfColumnsAfter, "Column not added");
		
		numberOfColumnsBefore = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns before : "+numberOfColumnsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.addColumnAfterForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_AddColumnAfter", CommonUtility.currentTimeStamp);
		numberOfColumnsAfter = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns After : "+numberOfColumnsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfColumnsBefore+1, numberOfColumnsAfter, "Column not added");
		
		numberOfColumnsBefore = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns before : "+numberOfColumnsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.deleteColumnForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_DeleteColumn", CommonUtility.currentTimeStamp);
		numberOfColumnsAfter = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns After : "+numberOfColumnsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfColumnsBefore-1, numberOfColumnsAfter, "Column not deleted");
		
		numberOfColumnsBefore = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns before : "+numberOfColumnsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.swapColumnsForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_SwapColumn", CommonUtility.currentTimeStamp);
		numberOfColumnsAfter = maventTilHudPage.getNumberOfTemplateColumnsOnPage();
		EllieMaeLog.log(_log, "Number of columns After : "+numberOfColumnsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfColumnsBefore, numberOfColumnsAfter, "Swap column failed");
		
		int numberOfRowsBefore = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows before : "+numberOfRowsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.addRowBeforeForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_AddRowBefore", CommonUtility.currentTimeStamp);
		int numberOfRowsAfter = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows After : "+numberOfRowsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfRowsBefore+1, numberOfRowsAfter, "Row not added");
		
		numberOfRowsBefore = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows before : "+numberOfRowsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.addRowAfterForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_AddRowAfter", CommonUtility.currentTimeStamp);
		numberOfRowsAfter = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows After : "+numberOfRowsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfRowsBefore+1, numberOfRowsAfter, "Row not added");
		
		numberOfRowsBefore = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows before : "+numberOfRowsBefore, EllieMaeLogLevel.reporter);
		maventTilHudPage.deleteRowForTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Template_DeleteRow", CommonUtility.currentTimeStamp);
		numberOfRowsAfter = maventTilHudPage.getNumberOfTemplateRowsOnPage();
		EllieMaeLog.log(_log, "Number of rows After : "+numberOfRowsAfter, EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfRowsBefore-1, numberOfRowsAfter, "Row not deleted");
		
		maventTilHudPage.resetTemplate(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Reset_Template", CommonUtility.currentTimeStamp);		
		String columnTypeValueAfterReset = maventTilHudPage.getColumnTypeForTemplate();
		Assert.assertEquals(columnTypeValueAfterReset, "","Column Type value not blank after template reset");
		
		// Fees
		maventTilHudPage.addFee(testData);
		maventTilHudPage.addFee(testData);
		maventTilHudPage.addFee(testData);
		maventTilHudPage.addFee(testData);
		maventTilHudPage.addFee(testData);
		maventTilHudPage.addFee(testData);	
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Add_Fees", CommonUtility.currentTimeStamp);
		maventTilHudPage.deleteFee5(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_TILHUD_Delete_Fees", CommonUtility.currentTimeStamp);
		
		// navigate to MI-Buy page and come back on TIL-HUD Page. Verify all added fees exist
		
		int numberOfFeesOnPagebefore = maventTilHudPage.getNumberOfFeesOnPage();
		EllieMaeLog.log(_log, "numberOfFeesOnPagebefore : "+numberOfFeesOnPagebefore,EllieMaeLogLevel.reporter);
		maventMIBuyPage.navigateToMIBuyPage();		
		maventTilHudPage.navigateToTILAHUDPage();		
		int numberOfFeesOnPageAfter = maventTilHudPage.getNumberOfFeesOnPage();
		EllieMaeLog.log(_log, "numberOfFeesOnPageAfter : "+numberOfFeesOnPageAfter,EllieMaeLogLevel.reporter);
		Assert.assertEquals(numberOfFeesOnPageAfter, numberOfFeesOnPagebefore,"Number of fees not matches");
		
		/*  MI/Buy page - test web elements*/
		maventMIBuyPage.navigateToMIBuyPage();
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_MIBUY_Page", CommonUtility.currentTimeStamp);
		maventMIBuyPage.verifyMIbuylink(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_MIBUY_AddData", CommonUtility.currentTimeStamp);
		
		// navigate to TIL-HUD page and come back on MI-Buy page. Verify if data added exist.
		//maventTilHudPage.navigateToTILAHUDPage();
		//maventMIBuyPage.verfifyMIdata_AfterTILHuBclick(testData);
		
		/* RESPA Page - test web elements */
		maventRESPAPage.navigateToRESPAPage();
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_RESPA_Page", CommonUtility.currentTimeStamp);
		maventRESPAPage.addNewCharges(testData);
		maventRESPAPage.addFromAvailableCharges(testData);
		maventRESPAPage.deleteCharges(testData);

		/* Save Loan Data */
		maventRESPAPage.saveLoanData();
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_SaveLoanData", CommonUtility.currentTimeStamp);
		
		maventAbilityToRPage.navigateToAbilityToRepagePage();		
		maventAbilityToRPage.populateATRQualifiedMortgage(testData);
		maventAbilityToRPage.populateATRProposedHousing(testData);
		maventAbilityToRPage.populateAgencyGSEElig(testData);
		maventAbilityToRPage.populateUnderwritingFactors(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_AbilityToRepayPage", CommonUtility.currentTimeStamp);
		
		CommonUtilityApplication.threadWait(5000);
		
		maventAdvanceSPage.navigateToAdvanceSettingPage();
		maventAdvanceSPage.populateAdvanceSettingField(testData);
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_AdvanceSettingPage", CommonUtility.currentTimeStamp);
		
		/* Do Loan Review */
		sampleSOPPage.reviewLoan();
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_ReviewLoan", CommonUtility.currentTimeStamp);
		maventAdvanceSPage.clickInputDataLoanData();
		
		/* Do Loan Review from TILAHUD page */
		maventTilHudPage.navigateToTILAHUDPage();		
		sampleSOPPage.reviewLoan();
		CommonUtilityApplication.takeScreenShot(testData, "MaventUIRegression_ReviewLoan_on_TILHUDPage", CommonUtility.currentTimeStamp);
		
		/*  Log out */
		String titleLogOut = sampleSOPPage.logOut();
		Assert.assertEquals(titleLogOut, "Login", "Log out from the Mavent Portal Failed");	
		
		
		EllieMaeLog.log(_log, "Mavent UI Regression test Ends",EllieMaeLogLevel.reporter);
	}

}
