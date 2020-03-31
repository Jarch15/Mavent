package com.elliemae.main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;

import Fillo.Recordset;

/**
 * <b>Name:</b> MaventAutomationRunner 
 * <b>Description: </b>This class is used to create TestNG xml programmatically by reading excel data input from shared path
 *  and will be used to trigger test cases.
 * 
 * @author <i>Jayesh Bhapkar/i>
 */

public class MaventAutomationRunner {

	private static final String COL_ENVIRONMENT_NAME = "EnvironmentName";

	private static final String CHAR_EQUAL = "=";
	
	private static final String CHAR_EQUAL_START_SQUARE_BRACKET = "=[";
	
	private static final String CHAR_END_SQUARE_BRACKET = "]";

	private static final String TEST_METHOD_NAME = "TestMethodName";

	private static final String CLASS_NAME = "ClassName";

	private static final String CHAR_$$ = "$$";

	private static final String ENVIRONMENT_NAME = "environmentName";

	private static final String INPUT_EXCEL_PATH = "excelFilePath";
	
	private static final String INPUT_EXCEL_NAME = "excelFileName";
	
	private static final String NETWORK_DOMAIN = "networkDomain";
	
	private static final String NETWORK_USERNAME = "networkUsername";
	
	private static final String NETWORK_PASSOWRD = "networkPassword";

	private static final String EMAILABLE_REPORT_HTML = "emailable-report.html";

	private static final String AUTOMATION_OUTPUT = "AutomationOutput";

	private static final String USER_DIR = "user.dir";

	private static final String XML_ALLOW_RETURN_VALUES = "XmlAllowReturnValues";

	private static final String XML_SUITE_THREAD_COUNT = "XmlSuiteThreadCount";

	private static final String XML_SUITE_PARALLEL_MODE = "XmlSuiteParallelMode";

	private static final String TEST_SUITE_NAME = "Test_Suite_Name";
	
	private static final String THREAD_COUNT = "threadCount";
	
	private static final String PARALLEL_MODE = "parallel"; 
	
	public static Logger _log=Logger.getLogger(MaventAutomationRunner.class);
	
	private static String timeStamp = CommonUtility.currentTimeStamp;
	
	public static void main(String args[]) throws Exception {
		//argument : "-excelFilePath=[//irvinefs/irv_public/IT/QA/Automation] -excelFileName=[Mavent_Automation.xlsx] -networkDomain=[EM] -networkUsername=[ecsauto] -networkPassword=[W288X7UqZ0ki9AlKPjCjiA==]"
		
		System.setProperty("logfolder", timeStamp);
		System.setProperty("logfilename", "AutomationLog_"+timeStamp);
		DOMConfigurator.configure("src"+File.separator+"main"+File.separator+"resources"+File.separator+"log4j.xml");
		
		EllieMaeLog.log(_log,"Started Main Method");
		
		String hostName = getHostName();

		MaventAutomationRunner mainObj = new MaventAutomationRunner();

		StringBuilder strTestClassInfoQueryBuilder = new StringBuilder();
		StringBuilder strTestMethodInfoQueryBuilder = new StringBuilder();		
		Map<String, String> parameters;	
		Map<String, String> argumentKeyValue = new HashMap<String,String>();
		//Create an instance on TestNG
		TestNG myTestNG = new TestNG();
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		
		// Map can hold TestNG Parameters. 
		Map<String,Map<String,String>> testngParams = new HashMap<String, Map<String,String>>();
		List<XmlClass> testClasses= new ArrayList<>();
		EllieMaeLog.log(_log,"############################################################################################");
		EllieMaeLog.log(_log,"Started Main method to create TestNG.xml programmatically");
		
		/** Code to process command-line arguments. */
		processInputArguments(args, argumentKeyValue);
		
		// Copy Excel file to Local path
		String excelFileLocalPath = copyExcelToLocal(argumentKeyValue);
		
		String strQuerySuiteName="Select Test_Suite_Name, EnvironmentName from Automation where ExecuteClass= 'yes' and Jenkins_Slave='"+hostName+"'";
		
		// Get distinct Test Suite Names
		List<String> suiteNamesList = new ArrayList<String>();
		EllieMaeLog.log(_log, "Query to fetch suite details");
		Recordset recordSetSuiteInfo = CommonUtility.getRecordSetUsingFillo(excelFileLocalPath,strQuerySuiteName);
		EllieMaeLog.log(_log, "SuiteInfo data retrieved");
		if(recordSetSuiteInfo!=null)
		{
			while(recordSetSuiteInfo.next())
			{
				if(StringUtils.isNotBlank(recordSetSuiteInfo.getField(TEST_SUITE_NAME))
						&& StringUtils.isNotBlank(recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME))) 
				{
					suiteNamesList.add(recordSetSuiteInfo.getField(TEST_SUITE_NAME)+" - "+recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME));
				}
			}
			
			Set<String> suiteNamesSet = new HashSet<String>(suiteNamesList);
			
			for(String suiteName : suiteNamesSet)
			{
				Map<String, String> suiteParameter = new HashMap<>();
				String environmentName = suiteName.split(" - ")[1];
				String suiteNameString = suiteName.split(" - ")[0];
				suiteParameter.put(ENVIRONMENT_NAME, environmentName.trim());
				
				Map<String, String> suiteInfo = new HashMap<>();
				suiteInfo.put(TEST_SUITE_NAME, suiteName.trim());
				//If Parallel Mode is available in the input arguments, Input argument (Parallel Mode) will be considered for generating the TestNG XML, else it will be picked up from the input file.
				if(argumentKeyValue.containsKey(PARALLEL_MODE))
				{
					suiteInfo.put(XML_SUITE_PARALLEL_MODE, argumentKeyValue.get(PARALLEL_MODE).trim());
				}
				//If Thread Count is available in the input arguments, Input argument (Thread Count) will be considered for generating the TestNG XML, else it will be picked up from the input file.
				if(argumentKeyValue.containsKey(THREAD_COUNT))
				{
					suiteInfo.put(XML_SUITE_THREAD_COUNT, argumentKeyValue.get(THREAD_COUNT).trim());
				}
				suiteParameter.put("startLoggingFromMain", "true");
				Validate.notBlank(suiteParameter.get(ENVIRONMENT_NAME), "The supplied Environment Name i.e. '" + argumentKeyValue.get(ENVIRONMENT_NAME) + "' entry is missing in configuration file.");
				
				/** Create TestNG parameters collection. */
				
				parameters = new HashMap<>();		
				parameters.put("platform", "APIWEB");
				testngParams.put("APIWEB", parameters);
				parameters.put("browserName",  "ie");
				testngParams.put("ie", parameters);
				
				/** Gets testcases matching the input criteria.*/
				strTestClassInfoQueryBuilder.append("Select ClassName from Automation where ExecuteClass= 'yes' and Jenkins_Slave='"+hostName+"' and Test_Suite_Name='"+suiteNameString+"' and EnvironmentName='"+environmentName+"'");
				strTestMethodInfoQueryBuilder.append("Select TestMethodName from Automation where Test_Suite_Name='"+suiteNameString+"' and EnvironmentName='"+environmentName+"' and ExecuteClass= 'yes' and Jenkins_Slave='"+hostName+"' and ClassName ='" ).append( CHAR_$$ ).append( "'");
				testClasses = mainObj.getTestCasesForCriteria(argumentKeyValue, excelFileLocalPath, strTestClassInfoQueryBuilder.toString(), strTestMethodInfoQueryBuilder.toString());
						
				EllieMaeLog.log(_log,"*******************************TESTCASE EXECUTION STARTED********************************");
				
				//mainObj.generateXMLfile(suiteInfo,suiteParameter, testngParams,testClasses);
	
				XmlSuite suite = new XmlSuite ();
				suite = mainObj.getXMLSuite(suiteInfo,suiteParameter, testngParams,testClasses);
			    
			    EllieMaeLog.log(_log,"Printing TestNG Suite Xml");
			    EllieMaeLog.log(_log, "\n"+suite.toXml());
			    System.out.println(suite.toXml());
				
				//Add the suite to the list of suites.
			    mySuites.add(suite);
			    
			    //Set the list of Suites to the testNG object you created earlier.
			    myTestNG.setXmlSuites(mySuites);
			    
				strTestClassInfoQueryBuilder = new StringBuilder();
				strTestMethodInfoQueryBuilder = new StringBuilder();	
				
	
			}
			
		    //Set OutputFolder Path for TestNG test-output files
		    String finalPath = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT + File.separator + timeStamp + File.separator + "TestNG_Reports";
		    myTestNG.setOutputDirectory(finalPath);
		    
		    //invoke run() - this will run your class.
		    EllieMaeLog.log(_log,"Run TestNG xml created");
		    myTestNG.run();
		    
			EllieMaeLog.log(_log,"*******************************TESTCASE EXECUTION FINISHED*******************************");
			
			EllieMaeLog.log(_log,"############################################################################################");
			
			/** Generate TestNG created result artifact archives. */ 
			//mainObj.generateLogAndReportFiles(argumentKeyValue.get(OUTPUT_FOLDER));
			mainObj.generateLogAndReportFiles();
		}
		else
		{
			EllieMaeLog.log(_log, "No Test Suite to execute for given criteria.", EllieMaeLogLevel.reporter);
		}

		EllieMaeLog.log(_log,"Completed Main Method.");
	}

	/**
	 * <b>Name:</b> processInputArguments
	 * <b>Description:</b> Method to process the input command-line arguments.
	 * 
	 * @param args The input argument array
	 * @param argumentKeyValue The argument key-value collection.
	 * @throws Exception The exception instance.
	 */
	protected static void processInputArguments(String[] args, Map<String, String> argumentKeyValue) throws Exception {
		String value;
		
		for(String arg: args){
			value = null;
			
			if(StringUtils.isNotBlank(arg)) {
				arg = arg.trim();
				
				if(arg.contains("-")) {
					arg = arg.substring(arg.indexOf('-')+1);
				}
				else {
					EllieMaeLog.log(_log, "Invalid argument specified.");
					throw new Exception("Invalid argument specified.");
				}
				
				if(arg.contains(CHAR_EQUAL_START_SQUARE_BRACKET)) 
				{
					arg = arg.replace(CHAR_END_SQUARE_BRACKET, "");
					value = arg.split("=\\[")[1];					
					arg = arg.split("=\\[")[0];
					arg = arg.trim();
				}
				else {
					EllieMaeLog.log(_log, "Value expected for specified argument '"+ arg + "'.");
					throw new Exception("Value expected for specified argument '"+ arg + "'.");					
				}
				
				Validate.isTrue(StringUtils.isNotBlank(value), "Value is missing for specified argument '"+ arg + "'.");
				value = value.trim();
				
				if(INPUT_EXCEL_PATH.equalsIgnoreCase(arg)){
					argumentKeyValue.put(INPUT_EXCEL_PATH, value);
				}
				
				if(INPUT_EXCEL_NAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(INPUT_EXCEL_NAME, value);
				}
				
				if(NETWORK_DOMAIN.equalsIgnoreCase(arg)){
					argumentKeyValue.put(NETWORK_DOMAIN, value);
				}
				
				if(NETWORK_USERNAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(NETWORK_USERNAME, value);
				}
				
				if(NETWORK_PASSOWRD.equalsIgnoreCase(arg)){
					argumentKeyValue.put(NETWORK_PASSOWRD, value);
				}
			}
		}
	}

	/**
	 * <b>Name:</b> generateLogAndReportFiles
	 * <b>Description:</b> Method to generate the log and report files.
	 * 
	 * @param outputFolder The destination folder where the 
	 */
	protected void generateLogAndReportFiles() {
		String sourceFilePath;
		String destinationFilePath, tempDestinationFilePath = "";
		File file = null;
		FileWriter fileWriter = null;
		
		try{
			sourceFilePath = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT+ File.separator + timeStamp;
				
			destinationFilePath= FrameworkConsts.OUTPUTPATH + "/" + FrameworkConsts.ENVIRONMENTNAME + "/" + FrameworkConsts.JENKINSJOBNAME + "/" +timeStamp;
			tempDestinationFilePath = destinationFilePath.replace("/", "\\");
			
			EllieMaeLog.log(_log, "Shared folder path:" + tempDestinationFilePath);
			
			String linkFolder = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT ; 
			file = new File(linkFolder);
			file.mkdirs();
			EllieMaeLog.log(_log, "Automation Output Link folder path:" + linkFolder);
			
			file = new File(linkFolder + File.separator + "OutputLink.txt");
			file.createNewFile();
			
			fileWriter = new FileWriter(file);
			fileWriter.write(tempDestinationFilePath);
			fileWriter.flush();

			CommonUtility.copyFilesOrFolder(sourceFilePath + File.separator + "TestNG_Reports" + File.separator + EMAILABLE_REPORT_HTML, System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT+ File.separator +EMAILABLE_REPORT_HTML, FileType.FILE);
			
			String inputFileName = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT+ File.separator +EMAILABLE_REPORT_HTML;
			String outputReportZipFilePath = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT+ File.separator +"emailableReport.zip";
			String outputLogZipFilePath = sourceFilePath+"_logs.zip";
			String outputScreenshotZipFilePath = sourceFilePath+ "_screenShots.zip";
						
			EllieMaeLog.log(_log, "Zip the emailable-report.html.");
			CommonUtility.zipFile(new File(inputFileName), outputReportZipFilePath);
			
			EllieMaeLog.log(_log, "Zip the AutomationOutput Folder for logs.");
			CommonUtility.zipFolder(new File(sourceFilePath), "", outputLogZipFilePath);
			
			File screenShotPath = new File(sourceFilePath + File.separator + "Screenshots");
			if(screenShotPath.exists()){
				EllieMaeLog.log(_log, "Zip the AutomationOutput Folder for Screenshots.");
				CommonUtility.zipFolder(new File(sourceFilePath + File.separator + "Screenshots"), "", outputScreenshotZipFilePath);				
			}
			
			EllieMaeLog.log(_log, "Copy the Zip files at shared folder.");

			CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), outputReportZipFilePath, destinationFilePath, "emailableReport.zip");
			CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), outputLogZipFilePath, destinationFilePath, timeStamp + "_logs.zip");
			if(screenShotPath.exists()){
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), outputScreenshotZipFilePath, destinationFilePath, timeStamp + "_screenShots.zip");
			}
		}
		catch(Exception e){
			EllieMaeLog.log(_log, "Failed to copy AutomationOutput Folder at specified Path. Exception: "+e.getMessage());
		}
		finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}
	/**
	 * <b>Name:</b> getTestCasesForCriteria
	 * <b>Description:</b> Method to get test cases matching the given criteria.
	 * 
	 * @param argumentKeyValue The input argument Map collection.
	 * @param additionalDataFilePath The Data file path.
	 * @param strTestClassInfoQuery The Test class information retrieving query.
	 * @param strTestMethodInfoQuery The Test method information retrieving query.
	 * @return testClasses The List of XmlClass instances , that is, test cases to be run.
	 */
	protected List<XmlClass> getTestCasesForCriteria(Map<String, String> argumentKeyValue,
			String additionalDataFilePath, String strTestClassInfoQuery, String strTestMethodInfoQuery) {
		List<XmlClass> testClasses = new ArrayList<>();
		List<String> testClassesName = new ArrayList<String>();
		Recordset recordSetTestMethodInfo=null;
		XmlClass testClass;
		List<XmlInclude> includes;
		Fillo.Connection conn = null;
		Recordset recordSetTestClassInfo= null;

		try {
			EllieMaeLog.log(_log, "Query to fetch ClassName(s): "+strTestClassInfoQuery);
			/** Gets the Fillo connection instance. */
			conn = CommonUtility.getConnectionUsingFillo(additionalDataFilePath);
			
			/** Fetches records matching the criteria. */
			recordSetTestClassInfo=CommonUtility.getRecordSetUsingFilloConnection(conn, strTestClassInfoQuery);
			
			if(recordSetTestClassInfo!=null)
			{
				EllieMaeLog.log(_log, "Number of records for TestClassInfo: "+recordSetTestClassInfo.getCount());
				
				while(recordSetTestClassInfo.next()){
					/** Filter the recordsets not matching the criteria. */
					if(StringUtils.isNotBlank(recordSetTestClassInfo.getField(CLASS_NAME)))
						testClassesName.add(recordSetTestClassInfo.getField(CLASS_NAME));
				}
				
				testClassesName = new ArrayList<String>(new LinkedHashSet<String>(testClassesName));			
				//Collections.sort(testClassesName);
				
				int totalTestMethodsinExcel=0;
				int countTestMethods=0;
				// Loop through testClassesName.
				for(String testClassName: testClassesName){
				    testClass = new XmlClass();
				    testClass.setName(testClassName.trim());
				    EllieMaeLog.log(_log, "TestClassName: " + testClassName.trim());
				    EllieMaeLog.log(_log, "Query to fetch TestMethodName(s): "+strTestMethodInfoQuery.replace(CHAR_$$, testClassName.trim()));
				   
				    /** Fetches records matching the criteria. */
				    recordSetTestMethodInfo=CommonUtility.getRecordSetUsingFilloConnection(conn, strTestMethodInfoQuery.replace(CHAR_$$, testClassName.trim()));
				    includes = new ArrayList<XmlInclude>();
				    
				    if(recordSetTestMethodInfo==null)
				    {
				    	continue;
				    }
				    while(recordSetTestMethodInfo.next()){
				    	/** Filter the recordsets not matching the criteria. */
				    	  if(StringUtils.isNotBlank(recordSetTestMethodInfo.getField(TEST_METHOD_NAME))) {	
				    		includes.add (new XmlInclude (recordSetTestMethodInfo.getField(TEST_METHOD_NAME).trim()));
							EllieMaeLog.log(_log, "TestMethodName Included: "+recordSetTestMethodInfo.getField(TEST_METHOD_NAME).trim());
							totalTestMethodsinExcel++;
						}
				    }
				    
				    includes = new ArrayList<XmlInclude>(new LinkedHashSet<XmlInclude>(includes));
				    
				    testClass.setIncludedMethods (includes);
				    testClasses.add(testClass);
				    
				    countTestMethods+= includes.size();
				}
	
				EllieMaeLog.log(_log, "Number of test methods (as per filter criteria) in Excel: "+totalTestMethodsinExcel);		    
			    EllieMaeLog.log(_log, "Number of duplicate test methods (as per filter criteria) in Excel: "+(totalTestMethodsinExcel - countTestMethods));
			    EllieMaeLog.log(_log, "Number of test methods (as per filter criteria) in TestNG xml (after removing duplicates): "+countTestMethods);	
			}
			else
			{
				EllieMaeLog.log(_log, "No Test Class to execute for given criteria.", EllieMaeLogLevel.reporter);
			}
		}
		catch(Exception ex) {
			EllieMaeLog.log(_log,"Exception while reading data from TestNG_XMLData.xlsx : (Check data in excel with respect to filter criteria requested.)"+ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if(null != conn) {
				conn.close();
			}
		}
		
		return testClasses;
	}		
	
	/**
	 * <b>Name:</b> generateXMLfile
	 * <b>Description:</b> Method to generate the testNG.xml.
	 * 
	 * @param suiteInfo The SuiteInfo collection.
	 * @param suiteParameter The suite parameter-value collection.
	 * @param testNGParams The testNG parameter-value collection.
	 * @param testClasses The List of XMLClasses i.e. test classes.
	 */
	public void generateXMLfile(Map<String, String> suiteInfo, Map<String, String> suiteParameter, Map<String,Map<String,String>> testNGParams,List<XmlClass> testClasses) {
		Map<String, String> parameters;
		XmlTest test;
		//Create an instance on TestNG
		TestNG myTestNG = new TestNG();
		XmlSuite suite = new XmlSuite ();		
		
		suite.setName(StringUtils.isBlank(suiteInfo.get(TEST_SUITE_NAME)) ? "Suite": suiteInfo.get(TEST_SUITE_NAME));
	    suite.setAllowReturnValues(StringUtils.isBlank(suiteInfo.get(XML_ALLOW_RETURN_VALUES)) ? false: Boolean.parseBoolean(suiteInfo.get(XML_ALLOW_RETURN_VALUES)));
	    
	    suite.setParallel(StringUtils.isBlank(suiteInfo.get(XML_SUITE_PARALLEL_MODE)) ? XmlSuite.ParallelMode.FALSE : XmlSuite.ParallelMode.getValidParallel(suiteInfo.get(XML_SUITE_PARALLEL_MODE)));

	    if(suite.getParallel() != XmlSuite.ParallelMode.FALSE && suite.getParallel() != XmlSuite.ParallelMode.NONE)
    		suite.setThreadCount(StringUtils.isBlank(suiteInfo.get(XML_SUITE_THREAD_COUNT)) ? 1 : Integer.parseInt(suiteInfo.get(XML_SUITE_THREAD_COUNT)));
	    
	    List<String> listeners = new LinkedList<>();
	    listeners.add("com.elliemae.core.listener.RetryListener");
	    listeners.add("com.elliemae.core.listener.PostTestListener");
	    listeners.add("com.elliemae.core.listener.SummaryReport");
	    listeners.add("com.elliemae.core.listener.TestStatistics");
	    
	    suite.setListeners(listeners);
	    	    
	    suite.setParameters(suiteParameter);
	    
	    /* Commented below code for adding each test class as separate Test for TestNG reports
	    for(Map.Entry<String, Map<String,String>> entry : testNGParams.entrySet()){
	    	test = new XmlTest (suite);
	    	//test.setVerbose(10); //[Uncomment this flag only in case of debugging because this setting generates a lot of logging resulting in slowing the jobs]
		    test.setName (entry.getKey());
		    test.setPreserveOrder ("true");
		    		    
		    parameters = new HashMap<>();
	    	parameters= entry.getValue();
			test.setParameters(parameters);
	    } */
	    
	    /* Add each test class as separate Test for TestNG reports */
	    for(XmlClass xmlClass : testClasses)
	    {
	    	test = new XmlTest (suite);
	    	for(Map.Entry<String, Map<String,String>> entry : testNGParams.entrySet())
	    	{
		    	// set test name as its class name (for TestNG report)
			   if(generateTestNameFromTestClass(xmlClass)!=null && !generateTestNameFromTestClass(xmlClass).isEmpty())
			   {
				   test.setName(generateTestNameFromTestClass(xmlClass));
			   }
			   else
			   {
				   test.setName (entry.getKey());
			   }
	    		test.setPreserveOrder ("true");
			    parameters = new HashMap<>();
		    	parameters= entry.getValue();
				test.setParameters(parameters);
				List<XmlClass> list = new ArrayList<XmlClass>();
				list.add(xmlClass);
				test.setXmlClasses (list);
	    	}

	    }
	    	    
	    EllieMaeLog.log(_log,"Printing TestNG Suite Xml");
	    EllieMaeLog.log(_log, "\n"+suite.toXml());
	    System.out.println(suite.toXml());
	    
	    //Add the suite to the list of suites.
	    List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
	    mySuites.add(suite);
	    
	    //Set the list of Suites to the testNG object you created earlier.
	    myTestNG.setXmlSuites(mySuites);
	    
	    //Set OutputFolder Path for TestNG test-output files
	    String finalPath = System.getProperty(USER_DIR) + File.separator + AUTOMATION_OUTPUT + File.separator + timeStamp + File.separator + "TestNG_Reports";
	    myTestNG.setOutputDirectory(finalPath);
	    
	    //invoke run() - this will run your class.
	    EllieMaeLog.log(_log,"Run TestNG xml created");
	    myTestNG.run();
	}	
	
	
	/**
	 * <b>Name:</b> generateXMLfile
	 * <b>Description:</b> Method to generate the testNG.xml.
	 * 
	 * @param suiteInfo The SuiteInfo collection.
	 * @param suiteParameter The suite parameter-value collection.
	 * @param testNGParams The testNG parameter-value collection.
	 * @param testClasses The List of XMLClasses i.e. test classes.
	 */
	public XmlSuite getXMLSuite(Map<String, String> suiteInfo, Map<String, String> suiteParameter, Map<String,Map<String,String>> testNGParams,List<XmlClass> testClasses) {
		Map<String, String> parameters;
		XmlTest test;
		XmlSuite suite = new XmlSuite ();		
		
		suite.setName(StringUtils.isBlank(suiteInfo.get(TEST_SUITE_NAME)) ? "Suite": suiteInfo.get(TEST_SUITE_NAME));
	    suite.setAllowReturnValues(StringUtils.isBlank(suiteInfo.get(XML_ALLOW_RETURN_VALUES)) ? false: Boolean.parseBoolean(suiteInfo.get(XML_ALLOW_RETURN_VALUES)));
	    
	    suite.setParallel(StringUtils.isBlank(suiteInfo.get(XML_SUITE_PARALLEL_MODE)) ? XmlSuite.ParallelMode.FALSE : XmlSuite.ParallelMode.getValidParallel(suiteInfo.get(XML_SUITE_PARALLEL_MODE)));

	    if(suite.getParallel() != XmlSuite.ParallelMode.FALSE && suite.getParallel() != XmlSuite.ParallelMode.NONE)
    		suite.setThreadCount(StringUtils.isBlank(suiteInfo.get(XML_SUITE_THREAD_COUNT)) ? 1 : Integer.parseInt(suiteInfo.get(XML_SUITE_THREAD_COUNT)));
	    
	    List<String> listeners = new LinkedList<>();
	    listeners.add("com.elliemae.core.listener.RetryListener");
	    listeners.add("com.elliemae.core.listener.PostTestListener");
	    listeners.add("com.elliemae.core.listener.SummaryReport");
	    listeners.add("com.elliemae.core.listener.TestStatistics");
	    
	    suite.setListeners(listeners);
	    	    
	    suite.setParameters(suiteParameter);
	    
	    /* Add each test class as separate Test for TestNG reports */
	    for(XmlClass xmlClass : testClasses)
	    {
	    	test = new XmlTest (suite);
	    	for(Map.Entry<String, Map<String,String>> entry : testNGParams.entrySet())
	    	{
		    	// set test name as its class name (for TestNG report)
			   if(generateTestNameFromTestClass(xmlClass)!=null && !generateTestNameFromTestClass(xmlClass).isEmpty())
			   {
				   test.setName(generateTestNameFromTestClass(xmlClass));
			   }
			   else
			   {
				   test.setName (entry.getKey());
			   }
	    		test.setPreserveOrder ("true");
			    parameters = new HashMap<>();
		    	parameters= entry.getValue();
				test.setParameters(parameters);
				List<XmlClass> list = new ArrayList<XmlClass>();
				list.add(xmlClass);
				test.setXmlClasses (list);
	    	}

	    }
	    	    
	    EllieMaeLog.log(_log,"Printing TestNG Suite Xml");
	    EllieMaeLog.log(_log, "\n"+suite.toXml());
	    
	    return suite;
	}
	
	public String generateTestNameFromTestClass(XmlClass testClass)
	{
		String testName = "";
		
		if(testClass!=null && testClass.getName()!=null &&  !testClass.getName().isEmpty())
		{
			String [] strSplit = testClass.getName().split("\\.");
			if(strSplit!=null && strSplit.length>0)
				testName = strSplit[strSplit.length-1];
		}
		
		return testName;
	}
	
	private static String getHostName()
	{
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
	    else
	        return "Unknown Computer";
	}
	
	private static String copyExcelToLocal(Map<String, String> argumentKeyValue) throws Exception
	{
		String excelFileLocalPath="";
		String excelFilePath = argumentKeyValue.get(INPUT_EXCEL_PATH).trim();
		String excelFileName = argumentKeyValue.get(INPUT_EXCEL_NAME).trim();
		String networkDomain = argumentKeyValue.get(NETWORK_DOMAIN).trim();
		String networkUsername= argumentKeyValue.get(NETWORK_USERNAME).trim();
		String networkPassword = argumentKeyValue.get(NETWORK_PASSOWRD).trim();
		
		
		//Copy Excel file to local data folder
		EllieMaeLog.log(_log, "Copying excel file to local data folder", EllieMaeLogLevel.reporter);
 		String decryptedPassword = CommonUtility.decryptData(networkPassword);
 		
		File f = new File("");
		String dataDirectoryPath = f.getAbsolutePath();

		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				CommonUtility.copyFilesOrFolder(excelFilePath+"/"+excelFileName, dataDirectoryPath+File.separator+excelFileName, FileType.FILE);
			}
			else
			{
				CommonUtilityApplication.copyFileFromNetworkLocation(networkDomain,networkUsername,decryptedPassword,excelFilePath+"/"+excelFileName,dataDirectoryPath,excelFileName);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception during copying excel sheet to data folder", EllieMaeLogLevel.reporter);
			e.printStackTrace();
			throw e;
		}
		
		excelFileLocalPath = dataDirectoryPath+File.separator+excelFileName;
		
		return excelFileLocalPath;
		
	}
}