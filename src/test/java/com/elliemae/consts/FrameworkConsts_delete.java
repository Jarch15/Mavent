package com.elliemae.consts;

public final class FrameworkConsts_delete {

	/**
	 * <b>Name:</b> FrameworkConsts</br> <b>Description:</b> This is a constant class
	 * includes the following:<br>
	 * 1.General constants.<br>
	 * 2.Browsers constants.<br>
	 * 3.Constants parameters for run a test<br>
	 * 
	 */
	private FrameworkConsts_delete() {
		throw new AssertionError("This is a constant class.");
	}

	// *********************************************
	// General constants
	// *********************************************
	public static final String PAGETOLOAD = "270000";
	public static final String PAGETOLOAD_MAX = "900000";
	public static final long TIMEOUTINMILLISECONDS=90000L;
	public static final int IMPLICIT_WAIT_TIMEOUT_SECONDS = 5;

	public static String OUTPUTPATH="//corpn1fs.corp.elmae/qa80017/AutomationOutput";
	
	// *********************************************
	// Resources Folder
	// *********************************************	
	public static ThreadLocal<String> tlResourceFolder = new ThreadLocal<String>();
	
	// *********************************************
	// Flag to check Environment details logged or not. 
	// *********************************************	
	public static ThreadLocal<Boolean> isEnvDetailsLogged = new ThreadLocal<Boolean>();
	
	// ***Time out for WebDriverWait in second
	public static final int WEBDRIVER_WAIT_TIMEOUT = 60;
		
	// *******************************
	// Titles,messages,link's name and texts
	// *******************************
	public static final String LEFT_SQ_BRACKET = "]";
	public static final String RIGHT_SQ_BRACKET = "[";
	public static final String LEFT_SQ_BRACKET_WITH_QUOTES = "']";

	// *****************************************
	// Browsers constants
	// *****************************************
	public static final String IE = "ie";	
	public static final String IEPROCESS = "iexplore.exe";
	public static final String IEDRIVERP = "IEDriverServer.exe";
	public static final String FF = "firefox";
	public static final String FFPROCESS = "geckodriver.exe";
	public static final String SF = "safari";
	public static final String SFPROCESS = "Safari.exe";
	public static final String CR = "chrome";
	public static final String CRPROCESS = "chrome.exe";
	public static final String CRDRIVERP = "chromedriver.exe";

	// *****************************************
	// Constants for run a test (it should be in a data and config)
	// *****************************************
	/** Definition of the browser type */
	public static String CURRENTBROWSERNAME = "";	
	public static final String BROWSEREXE_PATH = "./src/main/resources";	
	
	// *****************************************
	// Environment constants
	// *****************************************
	public static String LOANFOLDER = "";
	public static String JENKINSJOBNAME="";	
	public static String ENVIRONMENTNAME="";
	public static String ENVIRONMENTUSERNAME="";
	public static String ENVIRONMENTPASSWORD="";
	public static String ENVIRONMENTCLIENTID="";
	
	// *****************************************
	// Gmail User
	// *****************************************
	public static String GMAILUSER = "encompassmobileauto@gmail.com";
	public static String GMAILPASSWORD = "EM@1234$";
	
	// *****************************************
	// Framework Format
	// *****************************************
	public static String SPLITSEMICOLONPATTERN="];[";
	public static String SPLITSCOMMAPATTERN="],[";
	
	// *****************************************
	// TestCase related Constants
	// *****************************************
	public static String JIRANUMBERTOUPDATE="";
	public static ThreadLocal<Float> APIEXPECTEDSLATIMING= new ThreadLocal<>();	
	public static ThreadLocal<String> TESTCASENAME= new ThreadLocal<>();
	public static ThreadLocal<String> APIMETHODNAME = new ThreadLocal<>();
	public static final String JIRAID = "JIRAID";
	
	public static Boolean ALWAYSWRITEPERFORMANCELOGS= false;
	
	// *****************************************
	// Network User
	// *****************************************
	public static String DCOUSERDOMAIN = "dco";
	public static String NETWORKUSERNAME = "EMJenkinsAdmin";
	public static String NETWORKUSERPASSWORD = "ccRiBPtMEFHKPLzeBHROpA==";

	// *****************************************
	// Network User
	// *****************************************
	public static String EMUSERDOMAIN = "em";
	//public static String EMNETWORKUSERNAME = "EMJenkinsAdmin";
	//public static String EMNETWORKUSERPASSWORD = "Z9X3ZPsSCaJIi7lv04lvnTNbG0UpdNYhEq7g3Ax4it4=";
	public static String EMNETWORKUSERNAME = "ecsauto";
	public static String EMNETWORKUSERPASSWORD = "cr9R/YgAEE2ExQoZiplGSw==";	

	// *****************************************
	// JIRA User
	// ***************************************** 
	public static String JIRAUSERNAME = "jiraATFUser";
	public static String JIRAUSERPASSWORD = "NiVuOtXP4cv277OmyPilFQ==";
		
	/* *********************************************
	 * Jira constants.
	 * *********************************************/
	public static final String JIRA_BASE_URL = "https://jira.corp.elmae:8443";
	public static boolean isJiraWorkflowCall = false; 
	public static final boolean isJiraDataLoggingEnabled = false;

	/* *********************************************
	 * Retry feature related constant and variable.
	 * *********************************************/
	public static final String RETRY_COUNT = "retryCount";
	public static Integer MAX_RETRY_COUNT;
	
	/***********************************************
	 * Worker thread constant(s).
	 **********************************************/
	public static Integer MAX_JIRA_WORKER_THREAD_COUNT = new Integer(10);
}