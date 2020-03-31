package com.elliemae.testcases.WebsiteTest;

public class WebsiteConsts
{
	//Reports
	public static final String Mavent_Home = "//td/a[2]";
	public static final String Reports = ".//*[@id='m0i0i']/a";
	public static final String Reports_activity = ".//*[@id='m0i1i']/a";
	public static final String LOGIN_USER_ID_FLD = "userName";
	public static final String LOGIN_PWD_FLD = "password";
	public static final String LOGIN_COMPANY = "companyName";
	public static final String LOGIN_BTN = "//input[@class='button2' and @value='LOGIN']";
	public static final String LOGGED_USER_XPATH ="//td[@class='tableHeading']/span";
	
	
	//super admin
	
	
	public static final String JURIDICTION_LINK =".//*[@id='m0i29i']/a/img";
	public static final String MAINTAIN_JURIDICTION ="//select[@name='jurisdictionCode']";
	public static final String ADMINISTRATION_LINK = ".//*[@id='m0i16i']/a/img";	
	public static final String MAINTAIN_CRA_PROFILE ="//a[contains(@href,'showAssessmentProfileList')]";
	public static final String CREATE_NEW ="//input[@class='button2' and @value='Create New']";
	public static final String LICENSE_LINK =".//*[@id='m0i23i']/a/img";
	public static final String LICENSE_SEARCH ="//a[contains(@href,'LicenseSearch')]";
	public static final String DISPLAY_LICENSE ="//a[contains(@href,'listLicenses')]";
	public static final String ALERT_CANCEL_BTN ="//td[@class='endValueCellCenter']//a/input[@value='CANCEL']";
	public static final String DISPLAY_EXPDATE_FROM ="//input[@name='licenseExpDateFrom']";
	public static final String DISPLAY_EXPDATE_TO ="//input[@name='licenseExpDateTo']";
	public static final String CUSTOMER_PROFILE =".//*[@id='m0i27i']/a/img";
	public static final String LIST_CUSTOMER_PROFILE ="//a[contains(@href,'listCustomerProfiles')]";
	public static final String CUSTID_DROPDOWN ="//select[@name='custId']";
	public static final String CP_ADD =".//*[@id='custProfileSummary']//input[@class='button1' and @value='ADD']";
	public static final String AGENCY_LINK =".//*[@id='m0i31i']/a/img";
	public static final String ADD_AGENCY ="//a[contains(@href,'showAgency')]";
	public static final String DISPLAY_AGENCY ="//a[contains(@href,'listAgencies')]";
	public static final String ACT_LINK =".//*[@id='m0i34i']/a/img";
	public static final String ADD_ACT ="//a[contains(@href,'showAct')]";
	public static final String DISPLAY_ACT ="//a[contains(@href,'listActs')]";
	public static final String TAG_LINK =".//*[@id='m0i37i']/a/img";
	public static final String ADD_TAG ="//a[contains(@href,'showTag')]";
	public static final String DISPLA_TAG ="//a[contains(@href,'listTags')]";
	
	public static final String RULE_LINK =".//*[@id='m0i40i']/a/img";
	public static final String ADD_RULE ="//a[contains(@href,'showRule')]";
	public static final String DISPLAY_RULE ="//a[contains(@href,'listRules')]";
	
	public static final String RULE_HEADER_LINK =".//*[@id='m0i43i']/a/img";
	public static final String ADD_RULE_HEADER ="//a[contains(@href,'showRuleHeader')]";
	public static final String DISPLAY_RULE_HEADER ="//a[contains(@href,'listRuleHeaders')]";
	
	public static final String LISENCE_SEARCH_BTN ="//input[@class='button2' and @value='SEARCH']";
	
	
	
	//qtpditemplate.manage user flow
	public static final String TEMP_USER_REPORTS =".//*[@id='m0i0i']/a/img";
	public static final String TEMP_USER_ACTIVITY ="//a[contains(@href,'ActivityReportQuery')]";
	public static final String TEMP_USER_SUMMARY ="//a[contains(@href,'SummaryReportQuery')]";
	public static final String TEMP_USER_FROMRECEIVEDATE ="//input[@name='fromReceiveDate']";
	public static final String TEMP_USER_TORECEIVEDATE ="//input[@name='toReceiveDate']";	
	
	public static final String TEMP_USER_LOANID ="//input[@class='inputfont' and @name='loanId']";
	public static final String TEMP_USER_SEARCH_BTN ="//input[@class='button2' and @value='SEARCH']";
	
	public static final String TEMP_USER_FROMRECEIVEDATE_BTN ="//input[@name='fromReceiveDate']";
	public static final String TEMP_USER_TORECEIVEDATE_BTN ="//input[@name='toReceiveDate']";
	
	public static final String DIRECT_INPUT_LINK =".//*[@id='m0i3i']/a/img";
	public static final String DIRECT_INPUT_IMPORT_LINK ="//a[contains(@href,'Import')]";
	public static final String IMPORT_LINK ="//a[contains(@href,'loadLoanGroup')]";
	public static final String CREATE_LINK =".//*[@id='m0i5i']/a/img";
	public static final String CREATELOAN_LINK ="//a[contains(@href,'createLoan')]";
	
	public static final String CREATEPOOL_LINK ="//a[contains(@href,'createLoanGroup')]";
	public static final String SEARCH_LINK =".//*[@id='m0i8i']/a/img";
	public static final String SEARCH_LOAN_LINK ="//a[contains(@href,'loanSearch')]";
	public static final String SEARCH_BATCH_REVIEW_LINK ="//a[contains(@href,'reviewBatchSearch')]";
	public static final String QTM_USER_ADMINISTRATION_LINK =".//*[@id='m0i13i']/a/img";
	public static final String TEMPLATES_LINK ="//a[contains(@href,'templateList')]";
	public static final String RESET_PASSWORD_LINK ="//a[contains(@href,'resetPassword')]";
	public static final String HELP_LINK =".//*[@id='m0i17i']/a/img";
	public static final String CONTACTUS_LINK ="//a[contains(@href,'contactus')]";	
	public static final String DATAFILE_BTN =".//*[@id='uploadForm']//input[@name='batchFile']";
	
	
	//qtpmavnetadmin flow
	
	public static final String MAVENT_ADMIN_REPORTS =".//*[@id='m0i0i']/a/img";
	public static final String MAVENT_ADMIN_SYSTEM =".//*[@id='m0i4i']/a/img";
	public static final String MAVENT_ADMIN_SERVICEPERFORMANCE ="//a[contains(@href,'ServicePerformanceQuery')]";
	public static final String MAVENT_ADMIN_SERVICEPERFORMANCE_FROMRECEIVEDATE ="//input[@name='fromReceiveDate']";
	public static final String MAVENT_ADMIN_SERVICEPERFORMANCE_TORECEIVEDATE ="//input[@name='toReceiveDate']";
	public static final String MAVENT_ADMIN_SERVICEPERFORMANCE_SEARCH_BTN ="//input[@class='button2' and @value='SEARCH']";
	public static final String MAVENT_ADMIN_ADMINISTATION =".//*[@id='m0i16i']/a/img";
	public static final String MAVENT_ADMIN_ADMIN_LoanPError ="//a[contains(@href,'processingError')]";
	public static final String MAVENT_ADMIN_ADMIN_CUSTOMERSETUP ="//a[contains(@href,'customerSetup')]";
	public static final String MAVENT_ADMIN_ADMIN_CUSTSETUP_NEW_BTN ="//input[@name='New']";
	public static final String MAVENT_ADMIN_ADMIN_CUSTSETUP_CANCEL_BTN ="//input[@type='submit' and @value='Cancel']";
	public static final String MAVENT_ADMIN_ADMIN_CUSTSETUP_UPDATE_BTN ="//input[@name='Update']";
	public static final String MA_ADMIN_CUSTSETUP_UPDATE_VIEWUSER_BTN ="//input[@class='button3' and @value='VIEW USERS']";
	public static final String MA_ADMIN_CUSTSETUP_LASTNAME ="//input[@name='userLastName']";
	public static final String MA_ADMIN_CUSTSETUP_FIRSTNAME ="//input[@name='userFirstName']";
	public static final String MA_ADMIN_LOCATIONSUSER_LINK ="//a/font[@class='diMenuLink']";
	public static final String MA_ADMIN_CRETENEWLOC_CANCEL_BTN ="//input[@value='Cancel']";
	public static final String MA_ADMIN_HELP_LINK =".//*[@id='m0i22i']/a/img";
	public static final String MA_ADMIN_HELP_ABOUT ="//a[contains(@href,'about')]";
	public static final String LOGOUT_LINK ="//a[contains(@href,'logout')]";
}
