package com.elliemae.consts;

public class MaventPortalMenuConsts {

	    // *****************************************
		// Login Mavent page locators
		// *****************************************
		
	//	public static final String LOGIN_USER_ID_FLD = ".//*[@id='logondiv']/table[2]/tbody/tr[2]/td/form/div/center/table/tbody/tr[3]/td[2]/input";
	    public static final String LOGIN_USER_ID_FLD = "userName";
	//	public static final String LOGIN_PWD_FLD = ".//*[@id='logondiv']/table[2]/tbody/tr[2]/td/form/div/center/table/tbody/tr[4]/td[2]/input";
	    public static final String LOGIN_PWD_FLD = "password";
	//	public static final String LOGIN_COMPANY = ".//*[@id='logondiv']/table[2]/tbody/tr[2]/td/form/div/center/table/tbody/tr[5]/td[2]/input";
	    public static final String LOGIN_COMPANY = "companyName";
	    
	//	public static final String LOGIN_BTN = ".//*[@id='logondiv']/table[2]/tbody/tr[2]/td/form/div/center/input";
	    
	  //  public static final String LOGIN_BTN = "//input[@class='button2']";
	    public static final String LOGIN_BTN = "//input[@class='button2' and @value='LOGIN']";
	    public static final String LOGGED_USER_XPATH ="//td[@class='tableHeading']/span";
		
		  // *****************************************
		 // Menu Locators
		//
		
	    public static final String MNU_DIRECT_INPUT= "//td[contains(text(),'DIRECT INPUT')]";
		public static final String MNU_CREATE = "//td[contains(text(),'Create')]";
		public static final String MNU_CREATE_LOAN = "//td[contains(text(),'Loan')]";
		public static final String MNU_CREATE_POOL = "//td[contains(text(),'Pool')]";
		public static final String MNU_SEARCH = "//td[@class='MenuItem'][contains(text(),'Search')]";
		public static final String MNU_SEARCH_LOAN = "//a[contains(@href, 'loanSearch.do')]";
		
		public static final String MNU_DIRECT_INPUT_SEARCH_POOL = "//a[contains(@href, 'loanGroupSearch.do')]"; 
		public static final String MNU_DIRECT_INPUT_SEARCH_IMPORT_BATCH = "//td[contains(text(),'Import Batch')]";
		public static final String MNU_DIRECT_INPUT_SEARCH_BATCH_REVIEWED = "//td[contains(text(),'Batch Reviewed')]";  
		
		public static final String MNU_REPORTS = "//td[contains(text(),'REPORTS')]";
		public static final String MNU_REPORTS_ACTIVITY ="//td[contains(text(),'Activity')]";
		public static final String MNU_REPORTS_SUMMARY ="//td[contains(text(),'Summary')]";
		public static final String MNU_DIRECT_INPUT_IMPORT ="//a[contains(@href,'Import')]";
		
		public static final String MNU_ADMINISTRATION= "//td[contains(text(),'ADMINISTRATION')]";
		public static final String MNU_ADMINISTRATION_TEMPLATES= "//td[contains(text(),'Templates')]";
		public static final String MNU_ADMINISTRATION_RESET_PASSWORD= "//td[contains(text(),'Reset Password')]";
		public static final String MNU_ADMINISTRATION_LOAN_PROCESSING_ERRORS= "//td[contains(text(),'Loan Processing Errors')]";
		public static final String MNU_ADMINISTRATION_REPORT_NAVIGATION_MODE= "//td[contains(text(),'Report Navigation Mode')]";
		public static final String MNU_ADMINISTRATION_CUSTOMER_SETUP = "//td[contains(text(),'Customer Setup')]";
		public static final String CMPNY_ID_TXTBOX = "/html/body/form/table[1]/tbody/tr[3]/td/input";
		public static final String BTN_UPDATE = "Update";
		public static final String LBL_DEFAULT = "directinputMenuItem";
		public static final String LBL_COMPNYINFO = "//form[@name='companyForm']/table/tbody/tr/td";
		public static final String LBL_ID = "//form[@name='companyForm']/table/tbody/tr[2]/td/font";
		public static final String ID = "//form[@name='companyForm']/table/tbody/tr[2]/td[2]";
		public static final String LBL_Name = "//form[@name='companyForm']/table/tbody/tr[3]/td/font";
		public static final String Name = "//form[@name='companyForm']/table/tbody/tr[3]/td[2]/input";
		public static final String Locations = "//form[@name='companyForm']/table/tbody/tr[10]/td[2]/table/tbody/tr/td/select";;
		
	
	
		
		public static final String MNU_HELP = "//td[contains(text(),'HELP')]";
		
		public static final String LOAN_SEARCH_TEXTBOX_ID ="textSearch";
		public static final String SEARCH_BUTTON_ID ="btnSearch";		
		public static final String REVIEW_BUTTON_ID =".//input[@id='reviewBtn']";
		public static final String REVIEW_BUTTON_CSS ="input[class='button3']";
		public static final String REVIEW_BUTTON_XPATH =".//*[@id='reviewBtn' and @value='LOAN REVIEW']";
		public static final String WAITVERIFY_XPATH ="//div[@id='container']//tr//tr[1]//td[1]";

		public static final String LOG_OUT_LINK_PARTIAL_LINKTEXT ="LOG OUT";
		
		  // *****************************************
		 // Loan Search Page		
		//
		public static final String LOAN_SEARCH_ID_TEXTBOX_NAME = "loanNumber";
		public static final String LOAN_SEARCH_CREATE_DATE_FROM_TEXTBOX_NAME ="createdFrom";
		public static final String LOAN_SEARCH_BUTTON_NAME ="search";
		public static final String LOAN_ID_SEARCH_RESULT_XPATH="//td[contains(text(),'Loan ID')]//following::tr[1]/td[1]/a";
		
		  // *****************************************
		 // Review Search Page		
		//
		public static final String REVIEW_SEARCH_ID_TEXTBOX_NAME = "loanId";
		public static final String REVIEW_SEARCH_RECEIVE_DATE_FROM_TEXTBOX_NAME ="fromReceiveDate";
		public static final String REVIEW_SEARCH_BUTTON_XPATH ="//input[@class='button2']";
		public static final String REVIEW_LOAN_ID_SEARCH_RESULT_XPATH="//span[contains(text(),'Loan ID')]//following::tr[2]/td//a";
		
		  // *****************************************
		 // Report Activity Page		
		//
		public static final String REVIEW_SUMMARY_REPORT_LOANID_LABEL_XPATH = "//td[contains(text(),'Loan ID')]/following-sibling::td[1]";
		public static final String FRAME ="mainFrame";
		
		  // *****************************************
		 // Input Data Page		
		//
		//public static final String LOAN_ID_NAME_LABEL_XPATH = "html/body/form[3]/table[1]/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr[1]/td[2]";
		//public static final String LOAN_ID_NAME_LABEL_XPATH = "//td[contains(text(),'Loan ID')]/following-sibling::td[1]";
		public static final String LOAN_ID_NAME_LABEL_XPATH = "//tr[td[contains(text(),'User Name')]]/preceding-sibling::node()/td[2]";
		
		 // *****************************************
		 // Create Loan Locators
		//
		
		public static final String LOANID_TEXTBOX_NAME = "loanID";
		public static final String LOANID_TEXTBOX_XPATH = "//input[@class='inputfont' and @name='loanID']";
		public static final String LOCATION_DROP_DOWN_NAME="branchID";
		public static final String POOL_DROP_DOWN_NAME ="groupID";
		public static final String TEMPLATE_DROP_DOWN_NAME ="templateID";
		public static final String CONTINUE_BUTTON_XPATH="//input[@class='button3']";
		public static final String CONTINUE_BUTTON_CSS ="input[class='button3']"; 
		public static final String SERVICE_CODE_DROP_DOWN_NAME ="serviceCode"; 
		public static final String FIRST_NAME_TEXTBOX_NAME ="firstName";
		public static final String LAST_NAME_TEXTBOX_NAME ="lastName";
		public static final String CREDITOR_APPLICATION_DATE_TEXTBOX_NAME = "applicationDate";
		public static final String LIEN_POSITION_DROP_DOWN_NAME ="lienPosition";
		public static final String MORTGAGE_TYPE_DROP_DOWN_NAME = "mortgageType";
		public static final String ORIGINATION_TYPE_DROP_DOWN_NAME = "originationType";
		public static final String AMORTIZATION_TYPE_DROP_DOWN_NAME ="amortizationType";
		public static final String TRANSACTION_TYPE_DROP_DOWN_NAME = "transactionType";
		public static final String COUNTY_TEXTBOX_NAME = "propertyCountyName";
		public static final String STATE_DROP_DOWN_NAME ="propertyStateCode";
		public static final String ZIPCODE_TEXTBOX_NAME ="propertyZipCode";
		public static final String PROPERTY_TYPE_DROP_DOWN_NAME ="propertyType";
		public static final String OCCUPANCY_TYPE_DROP_DOWN_NAME ="occupancyType";
		public static final String APPRAISED_VALUE_TEXTBOX_NAME ="appraisedValueAmount";
		public static final String SALES_VALUE_TEXTBOX_NAME ="salesPriceAmount";
		public static final String PIAMOUNT_TEXTBOX_NAME = "PIAmount";
		public static final String NOTE_RATE_TEXTBOX_NAME ="noteRate";
		public static final String DISCOUNT_POINTS_DROP_DOWN_NAME ="highCostBonaFideDiscountFlag";
		public static final String FIRST_PAYMENT_DATE_TEXTBOX_NAME ="firstPaymentDate";
		public static final String AMORTIZATION_MONTHS_TEXTBOX_NAME ="amortizationTerm";
		public static final String LOAN_MONTHS_TEXTBOX_NAME ="loanTerm";
		public static final String LENDER_TYPE_DROP_DOWN_NAME ="lenderType";
		public static final String LENDER_HOME_STATE_DROP_DOWN_NAME ="homeStateCode";
		
		public static final String TIL_HUD_LINK_XPATH =".//*[@id='tbLinks']/tbody/tr/td[2]/a/font";
		public static final String TIL_HUD_LINK_PARTIAL_LINKTEXT ="TIL / HUD-1 / LE / CD";
		
		public static final String INTEREST_FROM_DATE_TEXTBOX_NAME ="interestFromDate";
		public static final String INTEREST_TO_DATE_TEXTBOX_NAME ="interestToDate";
			
		public static final String LOANID = "html/body/form/div/center/table/tbody/tr[4]/td[2]/input";
		public static final String LOCATION = "html/body/form/div/center/table/tbody/tr[5]/td[2]/select";
		public static final String POOL = "html/body/form/div/center/table/tbody/tr[6]/td[2]/select";
		public static final String TEMPLATE = "html/body/form/div/center/table/tbody/tr[7]/td[2]/select";
		
		//******************************************
		// Review Result
		public static final String VIEW_PDF_BUTTON_CSS ="input[class='button2']"; 
		public static final String VIEW_PDF_BUTTON_XPATH="//input[@class='button2']";
		public static final String PRINT_PREVIEW_BUTTON_XPATH ="//input[@class='button3']"; 
		
		
		//******************************************
		// Help Menu
		
		public static final String HELP = ".//*[@id='m0i21i']/a/img" ;
		public static final String ABOUT = ".//*[@id='m0i22i']/a/img" ;
		public static final String FOOTNOTE = "html/body/p[1]/font";
		
	    // *****************************************
		// JMS Admin Portal page locators
		// *****************************************
		public static final String RUNTIME_LINK_XPATH ="//div[@class='header-link-label']/span[contains(text(),'Runtime')]";
		public static final String CONSUMER_LINK_XPATH ="//div[contains(text(),'Consumer')]";
		public static final String COMPLY_IN_QUEUE_LINK_XPATH ="//div[contains(text(),'2complyInQueue')]";
		public static final String COMPLY_IN_QUEUE_LINK_VALUE_XPATH ="//div[@id='main-content-area']/div[2]/div/div[4]/div/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div/table/tbody/tr[5]/td/table/tbody/tr[2]/td/div/div[2]/table/tbody/tr[3]/td/table/tbody/tr[2]/td[2]"; 
		public static final String COMPLY_SAVE_QUEUE_LINK_XPATH ="//div[contains(text(),'2complySaveQueue')]";
		public static final String COMPLY_SAVE_QUEUE_LINK_VALUE_XPATH ="//div[@id='main-content-area']/div[2]/div/div[4]/div/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div/table/tbody/tr[5]/td/table/tbody/tr[2]/td/div/div[2]/table/tbody/tr[3]/td/table/tbody/tr[2]/td[2]";
		public static final String ADMIN_USER_XPATH ="//div[contains(text(),'adminuser')]";
		public static final String ADMIN_USER_LOGOUT_XPATH ="//div[contains(text(),'Logout')]";
		public static final String ADMIN_USER_LOGOUT_CONFIRM_XPATH ="//button[contains(text(),'Confirm')]";
		
		public static final String JMSDESTINATIONS_LINK_XPATH ="//div[contains(text(),'JMS Destinations')]";
		public static final String VIEW_LINK_XPATH ="//a[@class='viewlink-cell']";
		public static final String READ_CONSUMER_COUNT_XPATH ="//td[contains(text(),'Consumer Count')]/following-sibling::td[1]";
		
	    // *****************************************
		// Comment page locators
		// *****************************************
		public static final String COMMENT_LINK_XPATH ="//a/img[@alt='Comments']";
		public static final String COMMENT_TEXTAREA_XPATH ="//input[@name='addComment']//preceding::td//textarea[@name='userUpdatedComment']";
		public static final String COMMENT_ADD_BUTTON_XPATH ="//input[@name='addComment']";
		
		public static final String VIEW_REVIEW_XML_XPATH="//a[contains(text(),'View Loan Document (XML)')]";
		
		
		// *****************************************
				//VerifyLinkTest
		// *****************************************
		
		public static final String Mavent_Home = "//td/a[2]";
		public static final String Reports = ".//*[@id='m0i0i']/a";
		public static final String Reports_activity = ".//*[@id='m0i1i']/a";
		
		
		// *****************************************
				//MaventUIRegression Common Consts
		// 
		
		public static final String MaventUIRegression_InputData = ".//*[@id='mainTable']//a/img[@src='images/loandata-desel.gif']";
		public static final String MaventUIRegression_EditLoanData = "//input[@value='EDIT LOAN DATA']";
		
		
	
		
		
		
	
}
