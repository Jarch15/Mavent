package com.elliemae.consts;

public class MaventTILHUDPageConsts 
{
	// TILA HUD Page Link
	public static final String TIL_HUD_LINK_PARTIAL_LINKTEXT ="TIL / HUD-1 / LE / CD";
	
	public static final String SECONDPASS_RADIO_BUTTON_XPATH ="//input[@name='pass' and @value='2']";
	
	
	// Fees - dynamic
	public static final String FEE_ID_DROPDOWN_XPATH="//select[@name='feeId_<ID>']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH="//select[@name='paidToCode_<ID>']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH="//select[@name='paidByCode_<ID>']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH="//input[@name='amount_<ID>']";
	
	
	// Fees - 1
	public static final String FEE_ID_DROPDOWN_XPATH_1="//select[@name='feeId_1']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_1="//select[@name='paidToCode_1']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_1="//select[@name='paidByCode_1']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_1="//input[@name='amount_1']";	
	
	// Fees - 2
	public static final String FEE_ID_DROPDOWN_XPATH_2="//select[@name='feeId_2']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_2="//select[@name='paidToCode_2']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_2="//select[@name='paidByCode_2']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_2="//input[@name='amount_2']";	
	
	// Fees - 3
	public static final String FEE_ID_DROPDOWN_XPATH_3="//select[@name='feeId_3']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_3="//select[@name='paidToCode_3']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_3="//select[@name='paidByCode_3']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_3="//input[@name='amount_3']";	
	
	// Fees - 4
	public static final String FEE_ID_DROPDOWN_XPATH_4="//select[@name='feeId_4']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_4="//select[@name='paidToCode_4']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_4="//select[@name='paidByCode_4']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_4="//input[@name='amount_4']";	
	
	// Fees - 5
	public static final String FEE_ID_DROPDOWN_XPATH_5="//select[@name='feeId_5']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_5="//select[@name='paidToCode_5']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_5="//select[@name='paidByCode_5']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_5="//input[@name='amount_5']";	
	public static final String DELETE_FEES_CHECKBOX_XPATH_5="//input[@name='remove' and @type='checkbox' and @value='5']";
	
	// Fees - 6
	public static final String FEE_ID_DROPDOWN_XPATH_6="//select[@name='feeId_6']";
	public static final String PAIDTOCODE_DROPDOWN_XPATH_6="//select[@name='paidToCode_6']";
	public static final String PAIDBYCODE_DROPDOWN_XPATH_6="//select[@name='paidByCode_6']";
	public static final String FEE_AMOUNT_TEXTBOX_XPATH_6="//input[@name='amount_6']";	
	
	public static final String DELETE_FEES_CHECKBOX_XPATH="//input[@name='remove' and @type='checkbox']";
	public static final String ADD_FEES_BUTTON_XPATH="//input[@type='button' and @value='ADD']";
	public static final String DELETE_FEES_BUTTON_XPATH="//input[@value='DELETE' and @onclick='javascript:onDeleteFeeRow();']";
	
	// Templates
	public static final String TILA_MDIA_DISC_CHECKBOX_XPATH="//input[@name='tilMdiaDisCheckBox']";
	public static final String TEMPLATE_DROPDOWN_XPATH="//select[@name='templateType']";
	public static final String TEMPLATE_APPLY_BTN_XPATH="//input[@class='button2' and @value='APPLY']";
	public static final String TEMPLATE_RESET_BTN_XPATH="//input[@class='button2' and @value='RESET']";
	
	public static final String TEMPLATE_COLUMN_SELECT_CHKBOX_XPATH="//tr[@id='columnCheck']/td/input[@name='columnCB']";
	public static final String TEMPLATE_ADD_COLUMN_BEFORE_BTN_XPATH="//input[@class='button2' and @onClick='javascript:addColumn(true)' and @value='ADD BEFORE']";
	public static final String TEMPLATE_ADD_COLUMN_AFTER_BTN_XPATH="//input[@class='button2' and @onClick='javascript:addColumn(false)' and @value='ADD AFTER']";
	public static final String TEMPLATE_ADD_TYPE_DROPDOWN_XPATH="//select[@name='columnType']";
	public static final String TEMPLATE_DELETE_COLUMN_BTN_XPATH="//input[@class='button2' and @onClick='javascript:deleteColumns()' and @value='DELETE']";
	public static final String TEMPLATE_SWAP_COLUMN_BTN_XPATH="//input[@onClick='javascript:swapColumns()' and @value='SWAP COLUMNS']";
	
	public static final String TEMPLATE_ROW_SELECT_CHKBOX_XPATH="//input[@name='rowCB' and @type='checkbox']";
	public static final String TEMPLATE_ADD_ROW_TYPE_DROPDOWN_XPATH="//select[@name='rowType']";
	public static final String TEMPLATE_ADD_ROW_BEFORE_BTN_XPATH="//input[@name='rowAdd' and @value='ADD BEFORE']";
	public static final String TEMPLATE_ADD_ROW_AFTER_BTN_XPATH="//input[@name='rowAdd' and @value='ADD AFTER']";
	public static final String TEMPLATE_DELETE_ROW_BTN_XPATH="//input[@name='rowAdd' and @value='DELETE']";
	
	
	//Last Disclosure
	public static final String LD_APR="apr";
	public static final String LD_FINANCECHARGEAMOUNT="financeChargeAmount";
	public static final String LD_FINANCEAMOUNT="financedAmount";
	public static final String LD_TOTALINTERESTPERCENTAGE="totalInterestPercentage";
	public static final String LD_DISCLOSEDLOANPRODUCT="disclosedLoanProduct";
	public static final String LD_DISCLOSEDPREPAYMENTPEN_FLAG="disclosedPrepaymentPenaltyFlag";
	public static final String LD_ESTIMATED_ESCROW="estTaxInsImpoundAmount";
	
	
	//Disclosure Timing
	public static final String DT_initialDisclosedDate="initialDisclosedDate";
	public static final String DT_LE_SEND="lastLESentDate";
	public static final String DT_PD_SENDDate="priorDisclosedSentDate";
	public static final String DT_LD_SENDDate="lastDisclosedSentDate";
	public static final String DT_LE_RECEIVEDDATE="lastLEReceivedDate";
	public static final String DT_PD_RECEIVEDDATE="priorDisclosedReceivedDate";
	public static final String DT_LD_RECEIVEDDATE="lastDisclosedReceivedDate";
	public static final String DT_PD_APR="priorDisclosedApr";
	public static final String DT_PD_FINANCEAMOUNT="priorDisclosedFinanceAmount";
	public static final String DT_PD_ODDDAYS_INTERESTAMOUNT="priorDisclosedOddDaysInterestAmount";
	public static final String DT_PD_LOANPRODUCT="priorDisclosedLoanProduct";
	public static final String DT_PD_PREPAYMENTPENALTY_FLAG="priorDisclosedPrepaymentPenaltyFlag";
	
	
	//HUD - 1
	
	public static final String HUD_PPPAMOUNT="pppAmount";
	public static final String HUD_CURRENTRATESSETDATE="currentRateSetDate";
	public static final String HUD_DISBURSEMENT_DATE="disbursementDate";
	public static final String HUD_INTEREST_FROM_DATE="interestFromDate";
	public static final String HUD_INTEREST_TO_DATE="interestToDate";
	public static final String HUD_IMPOND_AMOUNT="tilImpoundAmount";
	
	//TIL ROR(Dates)
	public static final String TIL_ROR_DISCLOSURESIGNED="section32SignDate";
	public static final String TIL_ROR_RIGHTTOCANCELSIGNEDDATE="rightToCancelSignedDate";
	public static final String TIL_ROR_RIGHTTOCANCELEXPIREDATE="rightToCancelExpireDate";
	
	//GFE Disclosure (Dates)
	
	public static final String HUD_GFE_DISCLOSURE_DATE="GFEDisclosureDate";
	public static final String HUD_FINAL_DISCLOSURE_DATE="GFEFinalDisclosureDate";
	
	//fees
	public static final String HUD_FEES_AMOUNT_SETTLEMENT="amount1";
	public static final String HUD_FEES_AMOUNT_CLOSING="amount2";
	public static final String HUD_FEES_AMOUNT_POC="amount8";
	public static final String HUD_FEES_AMOUNT_INTEREST="amount3";
	public static final String HUD_FEES_AMOUNT_INSURANCE="amount4";
	public static final String HUD_FEES_AMOUNT_TAXES="amount5";
	public static final String HUD_FEES_AMOUNT_FEES="amount6";
	public static final String HUD_FEES_AMOUNT_BROKER_FEES="amount7";
	
	
	
	
}
