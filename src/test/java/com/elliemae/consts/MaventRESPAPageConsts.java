package com.elliemae.consts;

public class MaventRESPAPageConsts 
{
	// RESPA Page Link
	public static final String RESPA_LINK_PARTIAL_LINKTEXT ="RESPA";
	
	// Charges
	
	public static final String GRE1_TXT=".//*[@tabindex='1']";
	public static final String HUD1_TXT=".//*[@tabindex='2']";
	public static final String GRE2_TXT=".//*[@tabindex='3']";
	public static final String HUD2_TXT=".//*[@tabindex='4']";
	public static final String GRE3_TXT=".//*[@tabindex='5']";
	public static final String HUD3_TXT=".//*[@tabindex='6']";
	public static final String GRE4_TXT=".//*[@tabindex='7']";
	public static final String HUD4_TXT=".//*[@tabindex='8']";
	public static final String GRE5_TXT=".//*[@tabindex='10']";
	public static final String HUD5_TXT=".//*[@tabindex='11']";
	public static final String FEENAMETEXT1_TXT =".//*[@value='feeTextOption1']//following::input[1]";
	public static final String HUDID1_TXT =".//*[@value='feeTextOption1']//following::input[2]";
	
	public static final String GFEAMT1_TXT=".//*[@tabindex='1003']";
	public static final String HUDAMT1_TXT=".//*[@tabindex='1004']";
	
	public static final String GRE7_TXT=".//*[@tabindex='1006']";
	public static final String HUD7_TXT=".//*[@tabindex='1007']";
	public static final String GRE8_TXT=".//*[@tabindex='1008']";
	public static final String HUD8_TXT=".//*[@tabindex='1009']";
	public static final String GRE9_TXT=".//*[@tabindex='1010']";
	public static final String HUD9_TXT=".//*[@tabindex='1011']";
	public static final String FEENAMETEXT2_TXT =".//*[@tabindex='1012']//following::input[2]";
	public static final String HUDID2 =".//*[@tabindex='1012']//following::input[3]";
	public static final String GFEAMT2_TXT=".//*[@tabindex='2003']";
	public static final String HUDAMT2_TXT=".//*[@tabindex='2004']";
	
	
	
	
	public static final String ADD_CHARGES_FEETEXTOPTION_RADIOBTN_XPATH="//input[@name='FirstRadioGroup1' and @value='feeTextOption1']";
	public static final String ADD_CHARGES_FEENAMETEXT_TEXTBOX_XPATH="//input[@name='feeNameText']";
	public static final String ADD_CHARGES_HUDID_TEXTBOX_XPATH="//input[@name='hudId']";
	public static final String ADD_CHARGES_GFEAMOUNT_TEXTBOX_XPATH="//input[@name='gfeAmount']";
	public static final String ADD_CHARGES_HUD1AMOUNT_TEXTBOX_XPATH="//input[@name='hud1Amount']";
	public static final String ADD_CHARGES_ADD_BTN_XPATH="//input[@id='updateBtn1']";
	
	public static final String ADD_CHARGES_FEEDD_RADIOBTN_XPATH="//input[@name='FirstRadioGroup1' and @value='feeDD1']";
	public static final String ADD_CHARGES_HUDRANGECODE_DROPDOWN_XPATH="//select[@name='hudRangeCode']";
	public static final String ADD_CHARGES_FEENAMEDD_DROPDOWN_XPATH="//select[@name='FeeNameDD']";
	
	public static final String DELETE_CHARGES_CHKBOX_XPATH="//input[@id='del' and @type='checkbox']";
	public static final String ADD_CHARGES_DELETE_BTN_XPATH="//input[@id='deleteBtn1' and @value='DELETE']";
	
	// SAVE button on Loan Data Page
	public static final String LOAN_DATA_SAVE_BTN_XPATH="//input[@class='button2' and @value='SAVE']";
	
	
	
	
}
