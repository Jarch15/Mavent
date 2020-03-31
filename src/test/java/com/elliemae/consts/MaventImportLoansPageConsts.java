package com.elliemae.consts;

public class MaventImportLoansPageConsts 
{
	// Import Loans Page Menu Link
	public static final String DIRECT_INPUT_LINK ="//td[contains(text(),'DIRECT INPUT')]";
	public static final String DIRECT_INPUT_IMPORT_LINK ="//a[contains(@href,'Import')]";
	
	// input file path textbox
	public static final String INPUT_FILE_PATH_TEXTBOX_XPATH="//input[@name='batchFile']";
	
	// file format drop down 
	public static final String FILE_FORMAT_DROPDOWN_NAME="fileFormat";
	
	// Overwrite checkbox 
	public static final String OVERWRITE_CHKBOX_NAME="overwrite";
	
	// Continue Button
	public static final String CONTINUE_BTN_XPATH="//input[@value='CONTINUE']";
	
	
	// Loan Pool Select
	public static final String LOAN_POOL_DROPDOWN_NAME="groupID";
	public static final String LOAN_POOL_RADIO_BUTTON_EXISTING_XPATH="//input[@title='Existing']";
	public static final String LOAN_POOL_RADION_BUTTON_NEW_XPATH="//input[@title='New']";
	public static final String NEW_LOAN_POOL_NAME_NAME="loanGroupName";
	public static final String NEW_LOAN_POOL_DESCRIPTION_NAME="loanGroupDescription";
	
	// Loan Pool Content Page
	public static final String SELECT_ALL_BTN_XPATH="//input[@class='button2' and @value='SELECT ALL']";
	public static final String LOAN_REVIEW_BTN_XPATH="//input[@class='button3' and @value='LOAN REVIEW']";	
	public static final String LOAN_FILE_NAME_LINK_XPATH ="//a/span[contains(text(),'Import-SchemaFail1')]";
	
	// Loan Data Page 
	public static final String LOAN_DATA_FILE_STATUS_XPATH="//font[@id='fileStatusValue']";
	public static final String LOAN_DATA_REVIEW_STATUS_XPATH="//font[@id='maventReviewStatusValue']";

}
