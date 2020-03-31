package com.elliemae.pageobject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.consts.MaventAbilityToRepayPageConsts;
import com.elliemae.consts.MaventAdvanceSettingConsts;
import com.elliemae.consts.MaventLoanPageConsts;
import com.elliemae.consts.MaventRESPAPageConsts;
import com.elliemae.consts.MaventTILHUDPageConsts;
import com.elliemae.consts.MiBuyConsts;
import com.elliemae.core.Actions.EllieMaeApplicationActions;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.asserts.Assert;


public class MaventUIStaticTabVerify
{
	
	public static Logger _log = Logger.getLogger(JMSQueueMetricsPage.class);

	WebDriver driver;
	EllieMaeApplicationActions objEllieMaeActions;

	protected HashMap<String, String> envData = new HashMap<>();
	protected HashMap<String, String> userData = new HashMap<>();
	List<Integer> tabIndex = new ArrayList<Integer>();
	

	public MaventUIStaticTabVerify(WebDriver driver)
	{
		this.driver = driver;
		envData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		objEllieMaeActions = new EllieMaeApplicationActions(this.driver);

	}
	
	public void verifyTabForLoanDataPage()
	{
		//Review Data 
		verifyElementTab(MaventLoanPageConsts.SERVICE_CODE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.SELLER_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.ORIGINATOR_DROP_DOWN_NAME);
		
		//Loan Application
		verifyElementTab(MaventLoanPageConsts.FIRST_NAME_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.MIDDLE_NAME_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.LAST_NAME_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.SUFFIX_TEXTBOX_NAME);			
		verifyElementTab(MaventLoanPageConsts.GFE_APPLICATION_DATE_NAME);		
		verifyElementTab(MaventLoanPageConsts.ADDRESS_LINE1_NAME);
		verifyElementTab(MaventLoanPageConsts.ADDRESS_LINE2_NAME);
		verifyElementTab(MaventLoanPageConsts.CITY_NAME);
		verifyElementTab(MaventLoanPageConsts.COUNTY_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.STATE_DROP_DOWN_NAME);		
		verifyElementTab(MaventLoanPageConsts.CREDITOR_APPLICATION_DATE_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.REGISTRATION_LOCK_DATE_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.ZIPCODE_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.FHAASSIGNMENT_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.FHAENDORSEMENT_DATE_NAME);		
		verifyElementTab(MaventLoanPageConsts.LIEN_POSITION_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.MORTGAGE_TYPE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.ORIGINATION_TYPE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.TRANSACTION_TYPE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PURPOSEOF_REFIANANCECODEDD_NAME);		
		verifyElementTab(MaventLoanPageConsts.DOCUMENTATION_TYPE_NAME);
		verifyElementTab(MaventLoanPageConsts.PROPERTY_TYPE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.OCCUPANCY_TYPE_DROP_DOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.APPRAISED_VALUE_NAME);
		verifyElementTab(MaventLoanPageConsts.SALE_PRICE_NAME);
		verifyElementTab(MaventLoanPageConsts.ANNUAL_INCOME_AMT_NAME);
		verifyElementTab(MaventLoanPageConsts.GROSSMONTHLY_SALARY_NAME);
		verifyElementTab(MaventLoanPageConsts.NETMONTHLY_SALARY_NAME);
		verifyElementTab(MaventLoanPageConsts.OTHERMONTHLY_INCOME_NAME);
		verifyElementTab(MaventLoanPageConsts.OTHERMONTHLY_LIABILITY_NAME);
		verifyElementTab(MaventLoanPageConsts.MONTHLY_MAINTENANCE_NAME);
		verifyElementTab(MaventLoanPageConsts.NUMBERSOF_HOUSEHOLDMEM_NAME);		
		verifyElementTab(MaventLoanPageConsts.LoanA_DEBTRATIO);
		verifyElementTab(MaventLoanPageConsts.LOANA_LTVPERCENT);
		verifyElementTab(MaventLoanPageConsts.LOANA_CLVPERCENT);
		
				
		
	//	construction
		verifyElementTab(MaventLoanPageConsts.COMMITMENT_AMT_NAME);
		verifyElementTab(MaventLoanPageConsts.CONS_AMORTIZATION_TYPE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONS_PAYMENT_AMT_NAME);
		verifyElementTab(MaventLoanPageConsts.CONS_NOTERATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_UNDISCOUNTRATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_INITADJUSTEDRATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_POINTS_INITADJUSTEDRATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_DISPOINTS_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_AMORTERM_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_LOANTERM_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_FIRSTPAY_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_INTERESTFROM_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_INTERESTTO_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCTION_PHASECTPFLAG_NAME);
		verifyElementTab(MaventLoanPageConsts.INITIALACQ_LAND_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_INSFEE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_OTHERAMT_NAME);
		verifyElementTab(MaventLoanPageConsts.REQ_INTERESTRES_NAME);		
		verifyElementTab(MaventLoanPageConsts.CRED_PROH_INTEREST_PAYFLAG_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_PERIODTERM_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_DAYS_PERYEAR_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_PERIOD_BALTYPE_NAME);
		verifyElementTab(MaventLoanPageConsts.CONSTRUCT_PERIOD_END_DATE_NAME);
		
		
		//Construction ARM
		
		verifyElementTab(MaventLoanPageConsts.INDEX_CODE_NAME);		
		verifyElementTab(MaventLoanPageConsts.INDEX_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.INDEX_RATE_NAME);
		verifyElementTab(MaventLoanPageConsts.MARGIN_RATE_NAME);
		verifyElementTab(MaventLoanPageConsts.UNDISCOUNT_MARGINRATE_NAME);
		verifyElementTab(MaventLoanPageConsts.ROUNDING_CODE_NAME);
		verifyElementTab(MaventLoanPageConsts.ROUNDING_FACTOR_NAME);
		verifyElementTab(MaventLoanPageConsts.INITIAL_ADJPERIOD_NAME);
		verifyElementTab(MaventLoanPageConsts.SUBS_ADJPERIOD_NAME);
		verifyElementTab(MaventLoanPageConsts.SUBS_RATE_ADJ_NAME);
		verifyElementTab(MaventLoanPageConsts.INITIAL_DEC_ADJ_NAME);
		verifyElementTab(MaventLoanPageConsts.INITIAL_INC_ADJ_NAME);
		verifyElementTab(MaventLoanPageConsts.LIFETIME_FLOOR_NAME);
		verifyElementTab(MaventLoanPageConsts.LIFETIME_MAXRATE_NAME);
		verifyElementTab(MaventLoanPageConsts.LIFETIME_MAXRATE_CAPONLY_NAME);
		
		
	//	Note
		verifyElementTab(MaventLoanPageConsts.NOTE_BASELOAN_AMT_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_GROSSLOAN_AMT_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_NOTERATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_UNDISCOUNTEDE_RATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_INITIAL_ADJ_RATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_POINTS_INITIAL_ADJ_RATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_CUST_BASIS_POINT_RED_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_DISCOUNT_POINTS_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_PPP_PLAN_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_DOCUMENT_SIGN_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_FIRST_PAY_DATE_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_AMORTIZATION_TYPE_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_LOAN_MONTHS_TEXTBOX_NAME);
		verifyElementTab(MaventLoanPageConsts.NOTE_INTEREST_ONLY_TERM_NAME);
		
		
		//Prepayment Penalty 
		
		verifyElementTab(MaventLoanPageConsts.PP_PPP_PLANCODE_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_WAIVERFLAG_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_PREPAY_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_MAXTERM_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_EFF_DATE_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_CALC_DATE_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_NEWLOAN_REF_DROPDOWN_NAME);
		verifyElementTab(MaventLoanPageConsts.PP_PPP_AMT_DROPDOWN_NAME);
		
		
		
		//Lender Profile 
		verifyElementTab(MaventLoanPageConsts.LP_TYPEOFLENDER);
		verifyElementTab(MaventLoanPageConsts.LP_HOMETATECODE);
		
		
		//Loan Originator 
		verifyElementTab(MaventLoanPageConsts.NMLS_FIRSTNAME_NAME);
		verifyElementTab(MaventLoanPageConsts.NMLS_MIDDLENAME_NAME);
		verifyElementTab(MaventLoanPageConsts.NMLS_LASTNAME_NAME);
		verifyElementTab(MaventLoanPageConsts.NMLS_SUFFIX_NAME);
		verifyElementTab(MaventLoanPageConsts.NMLS_ID_NAME);
		verifyElementTab(MaventLoanPageConsts.NMLS_COMPANYNAME_NAME);
		verifyElementTab(MaventLoanPageConsts.COMPANY_NMLS_ID_NAME);
		verifyElementTab(MaventLoanPageConsts.REVIEW_DATE_NAME);
		
		
	}
	
	
	public void verifyTabForTILHUDPage()
	{
		//Navigate to TILHUD page
		navigateToTILAHUDPage();
		
		//Last Disclosure
		verifyElementTab(MaventTILHUDPageConsts.LD_APR);
		verifyElementTab(MaventTILHUDPageConsts.LD_FINANCECHARGEAMOUNT);
		verifyElementTab(MaventTILHUDPageConsts.LD_FINANCEAMOUNT);
		verifyElementTab(MaventTILHUDPageConsts.LD_TOTALINTERESTPERCENTAGE);
		verifyElementTab(MaventTILHUDPageConsts.LD_DISCLOSEDLOANPRODUCT);
		verifyElementTab(MaventTILHUDPageConsts.LD_DISCLOSEDPREPAYMENTPEN_FLAG);
		verifyElementTab(MaventTILHUDPageConsts.LD_ESTIMATED_ESCROW);
		
		//Disclosure Timing
		
		
		verifyElementTab(MaventTILHUDPageConsts.DT_initialDisclosedDate);
		verifyElementTab(MaventTILHUDPageConsts.DT_LE_SEND);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_SENDDate);
		verifyElementTab(MaventTILHUDPageConsts.DT_LD_SENDDate);
		verifyElementTab(MaventTILHUDPageConsts.DT_LE_RECEIVEDDATE);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_RECEIVEDDATE);
		verifyElementTab(MaventTILHUDPageConsts.DT_LD_RECEIVEDDATE);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_APR);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_FINANCEAMOUNT);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_ODDDAYS_INTERESTAMOUNT);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_LOANPRODUCT);
		verifyElementTab(MaventTILHUDPageConsts.DT_PD_PREPAYMENTPENALTY_FLAG);
		
		
		
		//HUD - 1
		
		verifyElementTab(MaventTILHUDPageConsts.HUD_PPPAMOUNT);
		verifyElementTab(MaventTILHUDPageConsts.HUD_CURRENTRATESSETDATE);
		verifyElementTab(MaventTILHUDPageConsts.HUD_DISBURSEMENT_DATE);
		verifyElementTab(MaventTILHUDPageConsts.HUD_INTEREST_FROM_DATE);
		verifyElementTab(MaventTILHUDPageConsts.HUD_INTEREST_TO_DATE);
		verifyElementTab(MaventTILHUDPageConsts.HUD_IMPOND_AMOUNT);
		
		//TIlROR 
		
				
		verifyElementTab(MaventTILHUDPageConsts.TIL_ROR_DISCLOSURESIGNED);
		verifyElementTab(MaventTILHUDPageConsts.TIL_ROR_RIGHTTOCANCELSIGNEDDATE);
		verifyElementTab(MaventTILHUDPageConsts.TIL_ROR_RIGHTTOCANCELEXPIREDATE);
		
		
		
		//GFE Disclosure (Dates)
		
		verifyElementTab(MaventTILHUDPageConsts.HUD_GFE_DISCLOSURE_DATE);
		verifyElementTab(MaventTILHUDPageConsts.HUD_FINAL_DISCLOSURE_DATE);
		
	
		
		
	}
	
	public void verifyTabForMIBUYDOWNPage()
	{
		navigateToMIBuyPage();
		CommonUtilityApplication.threadWait(5000);
		verifyXpathElementTab(MiBuyConsts.miFlag_radio_NO,"miFlag");
		verifyXpathElementTab(MiBuyConsts.buyDownFlag_radio_NO,"buyDownFlag");
		verifyXpathElementTab(MiBuyConsts.Upfront_Guarantee_Fee,"upfrontGuaranteeFee");
		verifyXpathElementTab(MiBuyConsts.monthsOfAnnualFeeAtClosing,"monthsOfAnnualFeeAtClosing");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Months_Txt1,"usdaPeriod");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Rate_Txt1,"usdaRate");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Months_Txt2,"usdaPeriod");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Rate_Txt2,"usdaRate");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Months_Txt3,"usdaPeriod");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Rate_Txt3,"usdaRate");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Months_Txt4,"usdaPeriod");
		verifyXpathElementTab(MiBuyConsts.USDARHS_Rate_Txt3,"usdaRate");
	}
	
	
	public void verifyTabRespaPage(HashMap<String, String>testdata)
	{
	
		navigateToRESPAPage();
		
		// 	Charges That Cannot Increase 
//		verifyXpathElementTab(MaventRESPAPageConsts.GRE1_TXT,"dynagfeAmount1");
//		verifyXpathElementTab(MaventRESPAPageConsts.HUD1_TXT,"dynahudAmount1");
//		
//		verifyXpathElementTab(MaventRESPAPageConsts.GRE2_TXT,"dynagfeAmount1");
//		verifyXpathElementTab(MaventRESPAPageConsts.HUD2_TXT,"dynahudAmount1");
//		
//		verifyXpathElementTab(MaventRESPAPageConsts.GRE3_TXT,"dynagfeAmount1");
//		verifyXpathElementTab(MaventRESPAPageConsts.HUD3_TXT,"dynahudAmount1");
//		
//		verifyXpathElementTab(MaventRESPAPageConsts.GRE4_TXT,"dynagfeAmount1");
//		verifyXpathElementTab(MaventRESPAPageConsts.HUD4_TXT,"dynahudAmount1");
//		
		
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE1_TXT,testdata.get("RESPA_GRE_TXT"));
		verifyXpathElementTab(MaventRESPAPageConsts.HUD1_TXT,testdata.get("RESPA_HUD1_TXT"));
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE2_TXT,testdata.get("RESPA_GRE_TXT"));
		verifyXpathElementTab(MaventRESPAPageConsts.HUD2_TXT,testdata.get("RESPA_HUD1_TXT"));
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE3_TXT,testdata.get("RESPA_GRE_TXT"));
		verifyXpathElementTab(MaventRESPAPageConsts.HUD3_TXT,testdata.get("RESPA_HUD1_TXT"));
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE4_TXT,testdata.get("RESPA_GRE_TXT"));
		verifyXpathElementTab(MaventRESPAPageConsts.HUD4_TXT,testdata.get("RESPA_HUD1_TXT"));
		
		
	 	//Charges In Total Cannot Increase More Than 10% 
		verifyXpathElementTab(MaventRESPAPageConsts.GRE5_TXT,testdata.get("RESPA_GRE_TXT"));
		verifyXpathElementTab(MaventRESPAPageConsts.HUD5_TXT,testdata.get("RESPA_HUD1_TXT"));
		
		verifyXpathElementTab(MaventRESPAPageConsts.ADD_CHARGES_FEETEXTOPTION_RADIOBTN_XPATH,"FirstRadioGroup1");
		verifyXpathElementTab(MaventRESPAPageConsts.FEENAMETEXT1_TXT,"feeNameText");
		verifyXpathElementTab(MaventRESPAPageConsts.HUDID1_TXT,"hudId");
		
		
		
		// 	Charges That Can Change 
		
		verifyXpathElementTab(MaventRESPAPageConsts.GFEAMT1_TXT,"gfeAmount");
		verifyXpathElementTab(MaventRESPAPageConsts.HUDAMT1_TXT,"hud1Amount");
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE7_TXT,"dynagfeAmount1");
		verifyXpathElementTab(MaventRESPAPageConsts.HUD7_TXT,"dynahudAmount1");
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE8_TXT,"dynagfeAmount1");
		verifyXpathElementTab(MaventRESPAPageConsts.HUD8_TXT,"dynahudAmount1");
		
		
		
		verifyXpathElementTab(MaventRESPAPageConsts.GRE9_TXT,"dynagfeAmount1");
		verifyXpathElementTab(MaventRESPAPageConsts.HUD9_TXT,"dynahudAmount1");
		
		
		verifyXpathElementTab(MaventRESPAPageConsts.FEENAMETEXT2_TXT,"feeNameText");
		verifyXpathElementTab(MaventRESPAPageConsts.HUDID2,"hudId");
		
		verifyXpathElementTab(MaventRESPAPageConsts.GFEAMT2_TXT,"gfeAmount");
		verifyXpathElementTab(MaventRESPAPageConsts.HUDAMT2_TXT,"hud1Amount");
		
		
	}
	
	public void VerifyAbilityToRepayQM ()
	{
		navigateToAbilityToRepagePage();
		
		//Ability to Repay/Qualified Mortgage Type
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_LoanType,"ATRType");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_transactionExemptFromATRReqFlag,"transactionExemptFromATRRequirementsFlag");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_loanSubToForwardCommitmentFlag,"loanSubjectToForwardCommitmentFlag");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_loanSoldOrTransAfterCons,"loanSoldOrTransferredAfterConsummation");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_creditPurpose,"creditPurpose");
		
		//Proposed Housing
		
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_SimultaneousLPA,"LOSCalculatedMonthlyPaymentAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_hazardInsAmt,"hazardInsuranceAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_realEstTaxAmt,"realEstateTaxAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_MIAmt,"mIAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_HOAAmt,"hOAAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_OtherAmount,"otherAmount");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_NetRentalIncome,"netRentalIncome");
		
		//Agency/GSE Eligibility
		
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_ComplianceAtCons,"complianceAtConsummation");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_varFromStandardsAgreement,"variationFromStandardsAgreement");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_loanWaiverGranted,"loanWaiverGranted");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_agencyCaseNumber,"agencyCaseNumber");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_agencyAUSType,"agencyAUSType");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_UWRecommendation,"UWRecommendation");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_scoredByFHATScard,"scoredByFHATotalScorecard");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_FHATotalSCRiskClass,"FHATotalScorecardRiskClass");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_FHATotalSCEligibilityA,"FHATotalScorecardEligibilityAssessment");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_desktopUwrUWR,"desktopUnderwriterUWRecommendation");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_duCaseID,"duCaseID");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_loanProspUWR,"loanProspectorUWRecommendation");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_lpAUSKey,"lpAUSKey");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_LPPurchaseElig,"LPPurchaseEligibility");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_otherUWSystemType,"otherUWSystemType");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_otherUWRecommendation,"otherUWRecommendation");
		
		//Underwriting Factors
		
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_monthlyCoveredLP,"monthlyCoveredLoanPaymentEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_monthlySimulLoanPayEFlagCB,"monthlySimultaneousLoanPaymentEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_mortgageRelatedOblEvalFlagCB,"mortgageRelatedObligationsEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_debtObligationsEvalFlagCB,"debtObligationsEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_childSupportOblEvalFlagCB,"childSupportObligationsEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_alimonyObligationsEvalFlagCB,"alimonyObligationsEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_currentEmploymentStatEvFlagCB,"currentEmploymentStatusEvalFlagCB");
		
		verifyXpathElementTab1(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedIncoEvalFlagCB,"currentOrExpectedIncomeEvalFlagCB");
		verifyXpathElementTab1(MaventAbilityToRepayPageConsts.ATR_currentOrExpectedAssetsEvalFlagCB,"currentOrExpectedAssetsEvalFlagCB");
		
		verifyXpathElementTab1(MaventAbilityToRepayPageConsts.ATR_creditHistoryEvalFlagCB,"creditHistoryEvalFlagCB");
		verifyXpathElementTab1(MaventAbilityToRepayPageConsts.ATR_DTIRatioEvalFlagCB,"dTIRatioEvalFlagCB");
		verifyXpathElementTab1(MaventAbilityToRepayPageConsts.ATR_residualIncomeEvalFlagCB,"residualIncomeEvalFlagCB");
		verifyXpathElementTab(MaventAbilityToRepayPageConsts.ATR_approvedBy,"approvedBy");
		
		
	}
	
	
	public void VerifynavigateToAdvanceSettingPage()
	{
		navigateToAdvanceSettingPage();
		
		//Advanced Settings
		
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_calculationMethod,"calculationMethod");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_calcDaysInYear,"calcDaysInYear");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_paymentsInYear,"paymentsInYear");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_daysInYear,"daysInYear");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_oddDaysInterest,"oddDaysInterest");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_interestCredit,"interestCredit");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_firstPaymentPeriodTreatment,"firstPaymentPeriodTreatment");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_paymentRoundingTreatment,"paymentRoundingTreatment");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_paymentRounding,"paymentRounding");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_indexMarginRoundingTreatment,"indexMarginRoundingTreatment");
		verifyXpathElementTab(MaventAdvanceSettingConsts.AS_indexMarginRounding,"indexMarginRounding");
		
	}
	
	
	/* Author : Nidhi Khandelwal
	 * Description : This method identify element and verify Tab ,also it assert the Tab Name . 
	 * It returns void to the caller after Tabing on the element.
	 *  
	 *  */
		
	public void verifyElementTab(String nameLocator)
	{
		WebElement ElemenetName = driver.findElement(By.name(nameLocator));
		
		if(ElemenetName.isEnabled() && ElemenetName.isDisplayed() )
		{
			ElemenetName.sendKeys(Keys.TAB);
			String name = ElemenetName.getAttribute("name");
			
			EllieMaeLog.log(_log, "Tab Press on  : "+name, EllieMaeLogLevel.reporter);
									
			Assert.assertEquals(nameLocator, name);
		}
	}
	
	
	
	
	public void verifyXpathElementTab(String xpath,String text)
	{
		WebElement elementName = driver.findElement(By.xpath(xpath));
		
		if(elementName.isEnabled() && elementName.isDisplayed() )
		{
			elementName.sendKeys(Keys.TAB);
			CommonUtilityApplication.threadWait(2000);
			String name = elementName.getAttribute("name");
			
			EllieMaeLog.log(_log, "Tab Press on  : "+name, EllieMaeLogLevel.reporter);
									
			Assert.assertEquals(text, name);
		}
	}
	
	public void verifyXpathElementTab1(String xpath,String text)
	{
		WebElement elementName = driver.findElement(By.xpath(xpath));
		
		if(elementName.isEnabled() && elementName.isDisplayed() )
		{
			elementName.sendKeys(Keys.TAB);
			CommonUtilityApplication.threadWait(2000);
			String name = elementName.getAttribute("name");
			
			EllieMaeLog.log(_log, "Tab Press on  : "+name, EllieMaeLogLevel.reporter);
									
			//Assert.assertEquals(text, name);
		}
	}
	
	
	
	public boolean navigateToRESPAPage() 
	{
		EllieMaeLog.log(_log, "Navigating to RESPA page", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventRESPAPageConsts.RESPA_LINK_PARTIAL_LINKTEXT)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	
	
	public boolean navigateToTILAHUDPage() 
	{
		EllieMaeLog.log(_log, "Navigating to TILA-HUD page", EllieMaeLogLevel.reporter);
		driver.findElement(By.partialLinkText(MaventTILHUDPageConsts.TIL_HUD_LINK_PARTIAL_LINKTEXT)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		
		return true;
	}
	
	public boolean navigateToMIBuyPage() 
	{
		EllieMaeLog.log(_log, "Navigating to MI-Buy page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MiBuyConsts.MI_BUY_DOWN_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean navigateToAbilityToRepagePage() 
	{
		EllieMaeLog.log(_log, "Navigating to Ability To Repay page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventAbilityToRepayPageConsts.AbilityToRepayQM_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	public boolean navigateToAdvanceSettingPage() 
	{
		EllieMaeLog.log(_log, "Navigating to Ability To Repay page", EllieMaeLogLevel.reporter);
		driver.findElement(By.xpath(MaventAdvanceSettingConsts.AdvancedSettings_LINK)).click();
		objEllieMaeActions.waitForPageToLoad("5000");
		CommonUtilityApplication.threadWait(3000);
		return true;
	}
	
	
	
	
	
}