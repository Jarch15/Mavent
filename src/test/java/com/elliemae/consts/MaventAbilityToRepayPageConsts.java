package com.elliemae.consts;

public class MaventAbilityToRepayPageConsts
{
	// Ability to Repay Page Link
		public static final String AbilityToRepayQM_LINK=".//*[@id='tbLinks']//a/font[contains(text(),'Ability To Repay/QM')]";
		
		
	//	Ability to Repay/Qualified Mortgage Type
		public static final String ATR_LoanType="//select[@class='inputfont' and @name='ATRType']";
		//public static final String ATR_QualifiedMortgage="//select[@class='inputfont' and @name='QMTypeDD']";
		public static final String ATR_transactionExemptFromATRReqFlag="//select[@class='inputfont' and @name='transactionExemptFromATRRequirementsFlag']";
		public static final String ATR_loanSubToForwardCommitmentFlag="//select[@class='inputfont' and @name='loanSubjectToForwardCommitmentFlag']";
		public static final String ATR_loanSoldOrTransAfterCons="//select[@class='inputfont' and @name='loanSoldOrTransferredAfterConsummation']";
		public static final String ATR_creditPurpose="//select[@class='inputfont' and @name='creditPurpose']";
		
	// Proposed Housing
		
		public static final String ATR_SimultaneousLPA="//input[@class='currencyInputCell' and @name='LOSCalculatedMonthlyPaymentAmount']";
		public static final String ATR_hazardInsAmt="//input[@class='currencyInputCell' and @name='hazardInsuranceAmount']";
		public static final String ATR_realEstTaxAmt="//input[@class='currencyInputCell' and @name='realEstateTaxAmount']";
		public static final String ATR_MIAmt="//input[@class='currencyInputCell' and @name='mIAmount']";
		public static final String ATR_HOAAmt="//input[@class='currencyInputCell' and @name='hOAAmount']";
		public static final String ATR_OtherAmount="//input[@class='currencyInputCell' and @name='otherAmount']";
		public static final String ATR_NetRentalIncome="//input[@class='currencyInputCell' and @name='netRentalIncome']";
		
		
		//Agency/GSE Eligibility
		
		public static final String ATR_ComplianceAtCons="//select[@class='inputfont' and @name='complianceAtConsummation']";		
		public static final String ATR_varFromStandardsAgreement="//select[@class='inputfont' and @name='variationFromStandardsAgreement']";
		public static final String ATR_loanWaiverGranted="//select[@class='inputfont' and @name='loanWaiverGranted']";
		public static final String ATR_agencyCaseNumber="//input[@class='inputfont' and @name='agencyCaseNumber']";
		public static final String ATR_agencyAUSType="//select[@class='inputfont' and @name='agencyAUSType']";
		public static final String ATR_UWRecommendation="//select[@class='inputfont' and @name='UWRecommendation']";
		public static final String ATR_scoredByFHATScard="//select[@class='inputfont' and @name='scoredByFHATotalScorecard']";
		public static final String ATR_FHATotalSCRiskClass="//select[@class='inputfont' and @name='FHATotalScorecardRiskClass']";
		public static final String ATR_FHATotalSCEligibilityA="//select[@class='inputfont' and @name='FHATotalScorecardEligibilityAssessment']";
		public static final String ATR_desktopUwrUWR="//select[@class='inputfont' and @name='desktopUnderwriterUWRecommendation']";
		public static final String ATR_duCaseID="//input[@class='inputfont' and @name='duCaseID']";
		public static final String ATR_loanProspUWR="//select[@class='inputfont' and @name='loanProspectorUWRecommendation']";
		public static final String ATR_lpAUSKey="//input[@class='inputfont' and @name='lpAUSKey']";
		public static final String ATR_LPPurchaseElig="//select[@class='inputfont' and @name='LPPurchaseEligibility']";
		public static final String ATR_otherUWSystemType="//select[@class='inputfont' and @name='otherUWSystemType']";
		public static final String ATR_otherUWRecommendation="//select[@class='inputfont' and @name='otherUWRecommendation']";
		
		//Underwriting Factors
		
		public static final String ATR_monthlyCoveredLP="//input[@type='checkbox' and @name='monthlyCoveredLoanPaymentEvalFlagCB']";
		public static final String ATR_monthlySimulLoanPayEFlagCB="//input[@name='monthlySimultaneousLoanPaymentEvalFlagCB']";
		public static final String ATR_mortgageRelatedOblEvalFlagCB="//input[@name='mortgageRelatedObligationsEvalFlagCB']";
		public static final String ATR_debtObligationsEvalFlagCB="//input[@name='debtObligationsEvalFlagCB']";
		public static final String ATR_childSupportOblEvalFlagCB="//input[@name='childSupportObligationsEvalFlagCB']";
		public static final String ATR_alimonyObligationsEvalFlagCB="//input[@name='alimonyObligationsEvalFlagCB']";
		public static final String ATR_currentEmploymentStatEvFlagCB="//input[@name='currentEmploymentStatusEvalFlagCB']";
		public static final String ATR_currentOrExpectedIncoEvalFlagCB="//input[@name='currentOrExpectedIncomeEvalFlagCB']";
		public static final String ATR_currentOrExpectedAssetsEvalFlagCB="//input[@tabindex='39']";
		public static final String ATR_creditHistoryEvalFlagCB="//input[@tabindex='40']";
		public static final String ATR_DTIRatioEvalFlagCB="//input[@tabindex='41']";
		public static final String ATR_residualIncomeEvalFlagCB="//input[@tabindex='42']";
		public static final String ATR_approvedBy="//input[@name='approvedBy']";
		
}

