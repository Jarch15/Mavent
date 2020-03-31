package com.elliemae.main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.MobileUtility;

import Fillo.Recordset;

/**
 * <b>Name:</b> TestNGRunner 
 * <b>Description: </b>This class is used to create TestNG xml programmatically by reading excel data input and will be used to trigger test cases
 * 
 * @author <i>Supreet Singh</i>
 */

public class TestNGRunner {

	private static final String TESTING_VERSION = "testingVersion";
	private static final String JIRA_PROJECT_NAME = "jiraProjectName";
	
	private static final String TEST_CYCLE_VERSION = "testCycleVersion";

	private static final String YES = "yes";

	private static final String COL_ENVIRONMENT_CLIENT_ID = "EnvironmentClientID";

	private static final String COL_ENVIRONMENT_PASSWORD = "EnvironmentPassword";

	private static final String COL_ENVIRONMENT_USER_NAME = "EnvironmentUserName";

	private static final String COL_ENVIRONMENT_NAME = "EnvironmentName";

	private static final String HEALTH_CHECK = "healthCheck";
	
	private static final String ENVIRONMENT_TYPE = "environmentType";

	private static final String CHAR_EQUAL = "=";

	private static final String CHAR_UNDERSCORE = "_";
	
	private static final String CHAR_COMMA = ",";
	
	private static final String DEFAULT_PLATFORM = "Web";

	private static final String COLUMN_LAYER = "Layer";

	private static final String COLUMN_TEAM_NAME = "TeamName";

	private static final String COLUMN_PRIORITY = "Priority";

	private static final String ALL = "ALL";

	private static final String TEST_METHOD_NAME = "TestMethodName";

	private static final String CLASS_NAME = "ClassName";

	private static final String CHAR_$$ = "$$";

	private static final String PLATFORM = "Platform";

	private static final String BROWSER_NAME = "BrowserName";

	private static final String DEVICE_USER_NAME = "DeviceUserName";

	private static final String DEVICE_TYPE = "DeviceType";

	private static final String ENVIRONMENT_CLIENT_ID = "environmentClientID";

	private static final String ENVIRONMENT_PASSWORD = "environmentPassword";

	private static final String ENVIRONMENT_USER_NAME = "environmentUserName";

	private static final String ENVIRONMENT_NAME = "environmentName";

	private static final String JENKINS_JOB_NAME = "jenkinsJobName";

	private static final String TESTING_TYPE = "testingType";

	private static final String INPUT_EXCEL = "inputexcel";

	private static final String DEVICE_NAME = "DeviceName";

	private static final String VERSION = "Version";

	private static final String EMAILABLE_REPORT_HTML = "emailable-report.html";

	private static final String AUTOMATION_OUTPUT = "AutomationOutput";

	private static final String USER_DIR = "user.dir";

	private static final String APPIUM_PORT = "AppiumPort";

	private static final String XML_ALLOW_RETURN_VALUES = "XmlAllowReturnValues";

	private static final String XML_SUITE_THREAD_COUNT = "XmlSuiteThreadCount";

	private static final String XML_SUITE_PARALLEL_MODE = "XmlSuiteParallelMode";

	private static final String XML_SUITE_NAME = "XmlSuiteName";

	private static final String PRIORITY = "priority";

	private static final String TEAM_NAME = "teamName";

	private static final String LAYER = "layer";
	
	private static final String THREAD_COUNT = "threadCount";
	
	private static final String PARALLEL_MODE = "parallel"; 
	
	private static final String UPDATE_TEST_STATUS_TO_JIRA = "updateTestStatusToJIRA";
	
	private static final String RETRY_COUNT = "retryCount";
	
	public static Logger _log=Logger.getLogger(TestNGRunner.class);
	
	private static String timeStamp = CommonUtility.currentTimeStamp;
	
	public static void main(String args[]) throws Exception {
		//"-inputexcel=TestNG_XMLData.xlsx -environmentName=QA1 -environmentUserName=userName -environmentPassword=password -environmentClientID=clientID -jenkinsJobName=SmokeTest_QA1 -priority=P1 -testingType=Smoke -teamName=TeamName -layer=LayerName -environmentType=EnvironmentTypeName -healthCheck=yes -parallel=true -threadCount=4 -outputFolder=OutputFolderLocation -updateTestStatusToJIRA=no"
		
		System.setProperty("logfolder", timeStamp);
		System.setProperty("logfilename", "AutomationLog_"+timeStamp);
		DOMConfigurator.configure("src"+File.separator+"main"+File.separator+"resources"+File.separator+"log4j.xml");
		
		EllieMaeLog.log(_log,"Started Main Method");

		TestNGRunner mainObj = new TestNGRunner();

		Boolean emulatorStart= false;
		StringBuilder strTestClassInfoQueryBuilder = new StringBuilder();
		StringBuilder strTestMethodInfoQueryBuilder = new StringBuilder();		
		Map<String, String> parameters;	
		Map<String, String> argumentKeyValue = new HashMap<String,String>();
		Map<String, String> suiteInfo = new HashMap<>();
		Map<String, String> suiteParameter = new HashMap<>();
		// Map can hold TestNG Parameters. 
		Map<String,Map<String,String>> testngParams = new HashMap<String, Map<String,String>>();
		Map<String,String> appiumPorts= new HashMap<>();
		List<XmlClass> testClasses= new ArrayList<>();
		EllieMaeLog.log(_log,"############################################################################################");
		EllieMaeLog.log(_log,"Started Main method to create TestNG.xml programmatically");
		
		/** Code to process command-line arguments. */
		processInputArguments(args, argumentKeyValue);
		
		String additionalDataFilePath= CommonUtility.getRelativeFilePath("config", argumentKeyValue.get(INPUT_EXCEL)== null? "TestNG_XMLData.xlsx":argumentKeyValue.get(INPUT_EXCEL).trim());
		String strSuiteInfoQuery="Select * from SuiteInfo";
		String strDriverInfoQuery="Select * from DriverInfo where ExecuteDevice= 'yes'";
		
		/** Code to populate respective queries with argument values. */
		if(argumentKeyValue.containsKey(TESTING_TYPE)){
			strTestClassInfoQueryBuilder.append("Select ClassName from TestClasses where ExecuteClass= 'yes' and ").append(argumentKeyValue.get(TESTING_TYPE)).append(" ='yes'");
			strTestMethodInfoQueryBuilder.append("Select TestMethodName from TestClasses where ExecuteClass= 'yes' and ClassName ='" ).append( CHAR_$$ ).append( "' and ").append(argumentKeyValue.get(TESTING_TYPE)).append(" ='yes'");
		}
		else{
			strTestClassInfoQueryBuilder.append("Select ClassName from TestClasses where ExecuteClass= 'yes'");
			strTestMethodInfoQueryBuilder.append("Select TestMethodName from TestClasses where ExecuteClass= 'yes' and ClassName ='" ).append( CHAR_$$ ).append( "'");
		}	
		
		if(argumentKeyValue.containsKey(TESTING_VERSION)){
			strTestClassInfoQueryBuilder.append(" and ").append(argumentKeyValue.get(TESTING_VERSION)).append(" !='no'");
			strTestMethodInfoQueryBuilder.append(" and ClassName ='" ).append( CHAR_$$ ).append( "' and ").append(argumentKeyValue.get(TESTING_VERSION)).append(" !='no'");
			}
		
		if(argumentKeyValue.containsKey(ENVIRONMENT_TYPE)) {
			strTestClassInfoQueryBuilder.append(" and ").append(argumentKeyValue.get(ENVIRONMENT_TYPE)).append(" !='no'");
			strTestMethodInfoQueryBuilder.append(" and ").append(argumentKeyValue.get(ENVIRONMENT_TYPE)).append(" !='no'");
		}

		if(argumentKeyValue.containsKey(HEALTH_CHECK)) {
			strTestClassInfoQueryBuilder.append(" and HealthCheck = '").append(argumentKeyValue.get(HEALTH_CHECK)).append("'");
			strTestMethodInfoQueryBuilder.append(" and HealthCheck = '").append(argumentKeyValue.get(HEALTH_CHECK)).append("'");
		}

		if(StringUtils.isBlank(argumentKeyValue.get(UPDATE_TEST_STATUS_TO_JIRA)) || !YES.equalsIgnoreCase(argumentKeyValue.get(UPDATE_TEST_STATUS_TO_JIRA).trim())) {
			suiteParameter.put(UPDATE_TEST_STATUS_TO_JIRA, "no");
		}
		else {
			suiteParameter.put(UPDATE_TEST_STATUS_TO_JIRA, YES);
		}
		
		if(StringUtils.isNotBlank(argumentKeyValue.get(JIRA_PROJECT_NAME))) {
			suiteParameter.put(JIRA_PROJECT_NAME, argumentKeyValue.get(JIRA_PROJECT_NAME));
		}
		
		if(StringUtils.isNotBlank(argumentKeyValue.get(TEST_CYCLE_VERSION))) {
			suiteParameter.put(TEST_CYCLE_VERSION, argumentKeyValue.get(TEST_CYCLE_VERSION));
		}
		
		if(argumentKeyValue.containsKey(RETRY_COUNT)) {
			suiteParameter.put(RETRY_COUNT, argumentKeyValue.get(RETRY_COUNT));
		}
		
		/** Create TestNG XML Suite data collection. */
		EllieMaeLog.log(_log, "Query SuiteInfo sheet to fetch suite details");
		Recordset recordSetSuiteInfo = CommonUtility.getRecordSetUsingFillo(additionalDataFilePath,strSuiteInfoQuery);
		EllieMaeLog.log(_log, "SuiteInfo data retrieved");
		
		suiteParameter.put("startLoggingFromMain", "true");
		
		while(recordSetSuiteInfo.next()){	
			if(StringUtils.isNotBlank(recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME)) && argumentKeyValue.get(ENVIRONMENT_NAME).trim().equalsIgnoreCase(recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME).trim())) 
			{
				// Set Suite name as Team Name (to appear in TestNG report)
				if(argumentKeyValue.containsKey(TEAM_NAME))
				{
					suiteInfo.put(XML_SUITE_NAME, argumentKeyValue.get(TEAM_NAME).trim()+" Suite");
				}
				else
				{
					suiteInfo.put(XML_SUITE_NAME, null == recordSetSuiteInfo.getField(XML_SUITE_NAME)? null: recordSetSuiteInfo.getField(XML_SUITE_NAME).trim());
				}
				//If Parallel Mode is available in the input arguments, Input argument (Parallel Mode) will be considered for generating the TestNG XML, else it will be picked up from the input file.
				if(argumentKeyValue.containsKey(PARALLEL_MODE))
					suiteInfo.put(XML_SUITE_PARALLEL_MODE, argumentKeyValue.get(PARALLEL_MODE).trim());
				else				
					suiteInfo.put(XML_SUITE_PARALLEL_MODE, null == recordSetSuiteInfo.getField(XML_SUITE_PARALLEL_MODE)? null: recordSetSuiteInfo.getField(XML_SUITE_PARALLEL_MODE).trim());
				//If Thread Count is available in the input arguments, Input argument (Thread Count) will be considered for generating the TestNG XML, else it will be picked up from the input file.
				if(argumentKeyValue.containsKey(THREAD_COUNT))
					suiteInfo.put(XML_SUITE_THREAD_COUNT, argumentKeyValue.get(THREAD_COUNT).trim());
				else			
					suiteInfo.put(XML_SUITE_THREAD_COUNT, null == recordSetSuiteInfo.getField(XML_SUITE_THREAD_COUNT)? null: recordSetSuiteInfo.getField(XML_SUITE_THREAD_COUNT).trim());

				suiteInfo.put(XML_ALLOW_RETURN_VALUES, null == recordSetSuiteInfo.getField(XML_ALLOW_RETURN_VALUES)? null: recordSetSuiteInfo.getField(XML_ALLOW_RETURN_VALUES).trim());
				suiteParameter.put(ENVIRONMENT_NAME, null == recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME)? null: recordSetSuiteInfo.getField(COL_ENVIRONMENT_NAME).trim());
				suiteParameter.put(ENVIRONMENT_USER_NAME, null == recordSetSuiteInfo.getField(COL_ENVIRONMENT_USER_NAME)? null:recordSetSuiteInfo.getField(COL_ENVIRONMENT_USER_NAME).trim());
				suiteParameter.put(ENVIRONMENT_PASSWORD, null == recordSetSuiteInfo.getField(COL_ENVIRONMENT_PASSWORD)? null:recordSetSuiteInfo.getField(COL_ENVIRONMENT_PASSWORD).trim());
				suiteParameter.put(ENVIRONMENT_CLIENT_ID, null == recordSetSuiteInfo.getField(COL_ENVIRONMENT_CLIENT_ID)? null:recordSetSuiteInfo.getField(COL_ENVIRONMENT_CLIENT_ID).trim());
			}
		}
		
		suiteParameter.put(JENKINS_JOB_NAME, null == argumentKeyValue.get(JENKINS_JOB_NAME)? "":argumentKeyValue.get(JENKINS_JOB_NAME).trim());
		
		Validate.notBlank(suiteParameter.get(ENVIRONMENT_NAME), "The supplied Environment Name i.e. '" + argumentKeyValue.get(ENVIRONMENT_NAME) + "' entry is missing in configuration file.");
		
		/** Create TestNG parameters collection. */
		EllieMaeLog.log(_log, "Query DriverInfo sheet to fetch driver details");
        Recordset recordSetDriverInfo=CommonUtility.getRecordSetUsingFillo(additionalDataFilePath,strDriverInfoQuery);
        EllieMaeLog.log(_log, "DriverInfo data retrieved");
		       
        if(argumentKeyValue.containsKey(BROWSER_NAME))
        {
        	String[] browserArgs = argumentKeyValue.get(BROWSER_NAME).split(CHAR_COMMA);
        	for(int browserCount = 0; browserCount < browserArgs.length; browserCount++)
        	{
        		parameters = new HashMap<>();	
        		parameters.put("browserName", browserArgs[browserCount].trim());
        		parameters.put("platform", DEFAULT_PLATFORM);              
        		testngParams.put(browserArgs[browserCount], parameters);
        	}
        }
        
		while(recordSetDriverInfo.next()){			
			parameters = new HashMap<>();		
		    parameters.put("deviceUserName", recordSetDriverInfo.getField(DEVICE_USER_NAME).trim());
		    parameters.put("platform", recordSetDriverInfo.getField(PLATFORM).trim());
		    parameters.put("version", recordSetDriverInfo.getField(VERSION).trim());
		    parameters.put("deviceName", recordSetDriverInfo.getField(DEVICE_NAME).trim());
		    parameters.put("deviceOrientation", recordSetDriverInfo.getField("DeviceOrientation").trim());
		    parameters.put("port", recordSetDriverInfo.getField(APPIUM_PORT).trim());
		    parameters.put("deviceType", recordSetDriverInfo.getField(DEVICE_TYPE).trim());
			parameters.put("loanFolder", recordSetDriverInfo.getField("LoanFolder").trim());
									
			if(recordSetDriverInfo.getField(PLATFORM).trim().equalsIgnoreCase("API"))				
				testngParams.put(recordSetDriverInfo.getField(PLATFORM).trim(), parameters);
			else if(recordSetDriverInfo.getField(PLATFORM).trim().equalsIgnoreCase("Web") || recordSetDriverInfo.getField(PLATFORM).trim().equalsIgnoreCase("APIWeb") || recordSetDriverInfo.getField(PLATFORM).trim().equalsIgnoreCase("Android") || recordSetDriverInfo.getField(PLATFORM).trim().equalsIgnoreCase("IOS") )
			{	
				if(!argumentKeyValue.containsKey(BROWSER_NAME))
				{
					  testngParams.put(recordSetDriverInfo.getField(BROWSER_NAME).trim(), parameters);
					  parameters.put("browserName",  recordSetDriverInfo.getField(BROWSER_NAME).trim());
				}
			}
			else
				testngParams.put(recordSetDriverInfo.getField(BROWSER_NAME).trim() + CHAR_UNDERSCORE+ recordSetDriverInfo.getField(DEVICE_USER_NAME) + CHAR_UNDERSCORE+ recordSetDriverInfo.getField(DEVICE_NAME) + CHAR_UNDERSCORE+ recordSetDriverInfo.getField(VERSION), parameters);
		    
		    if(recordSetDriverInfo.getField(DEVICE_TYPE).equalsIgnoreCase("Emulator")){
		    	EllieMaeLog.log(_log,"");
				EllieMaeLog.log(_log,"-----------------------------------------------------------------");
				EllieMaeLog.log(_log,"Starting new Android Device Emulator..");
				try{
					MobileUtility.startAndroidDeviceEmulator(recordSetDriverInfo.getField(DEVICE_USER_NAME).trim(),recordSetDriverInfo.getField(DEVICE_NAME).trim());
					Thread.sleep(60000);
					emulatorStart=true;
					EllieMaeLog.log(_log,"Started new Android Device Emulator.");
				}
				catch(Exception e){
					EllieMaeLog.log(_log,"Exception while starting new Android Device Emulator: "+e.getMessage());
				}
		    }
		    
		    if(!recordSetDriverInfo.getField(APPIUM_PORT).isEmpty())
		    	appiumPorts.put(recordSetDriverInfo.getField(APPIUM_PORT),recordSetDriverInfo.getField("BrowserDriverPort"));		    
		}
					
		if(!(appiumPorts == null)){
			for(Map.Entry<String,String> entry: appiumPorts.entrySet()){
				if(!recordSetDriverInfo.getField(DEVICE_TYPE).equalsIgnoreCase("SauceLab")){
			    	EllieMaeLog.log(_log,"");
			    	EllieMaeLog.log(_log,"-----------------------------------------------------------------");
					EllieMaeLog.log(_log,"Starting new Appium Session on port: "+entry.getKey());
					try{
						MobileUtility.startAppiumSession(entry.getKey(),entry.getValue(),recordSetDriverInfo.getField(PLATFORM));
						Thread.sleep(10000);
						EllieMaeLog.log(_log,"Started new Appium Session.");
					}
					catch(Exception e){
						EllieMaeLog.log(_log,"Exception while starting new Appium Session: "+e.getMessage());
					}
			    }
			}			
		}
		
		/** Gets testcases matching the input criteria.*/
		testClasses = mainObj.getTestCasesForCriteria(argumentKeyValue, additionalDataFilePath, strTestClassInfoQueryBuilder.toString(), strTestMethodInfoQueryBuilder.toString());
				
		EllieMaeLog.log(_log,"*******************************TESTCASE EXECUTION STARTED********************************");
		
		mainObj.generateXMLfile(suiteInfo,suiteParameter, testngParams,testClasses);	
		
		EllieMaeLog.log(_log,"*******************************TESTCASE EXECUTION FINISHED*******************************");
		
		if(emulatorStart==true){
			EllieMaeLog.log(_log,"Killing Android AVD Device..");
			try{
				MobileUtility.killAndroidDeviceEmulator();
				EllieMaeLog.log(_log,"Killed Android AVD Device.");
			}
			catch(Exception e){
				EllieMaeLog.log(_log,"Exception while killing Android AVD Device: "+e.getMessage());
			}
			EllieMaeLog.log(_log,"");
		}
		
		if(!(appiumPorts==null)){
			for(int i = 0; i<appiumPorts.size(); i++){
				EllieMaeLog.log(_log,"Killing Appium Session..");
				try{
					MobileUtility.killAppiumSession();
					EllieMaeLog.log(_log,"Killed Appium Session.");
				}
				catch(Exception e){
					EllieMaeLog.log(_log,"Exception while killing Appium Session: "+e.getMessage());
				}
			}
		}
				
		EllieMaeLog.log(_log,"############################################################################################");
		
		/** Generate TestNG created result artifact archives. */ 
		//mainObj.generateLogAndReportFiles(argumentKeyValue.get(OUTPUT_FOLDER));
		mainObj.generateLogAndReportFiles();
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
				
				if(arg.contains(CHAR_EQUAL)) {
					if(arg.lastIndexOf('=')+1 == arg.length()) {
						arg = arg.replace(CHAR_EQUAL, "");
						
						EllieMaeLog.log(_log, "Value expected for specified argument '"+ arg + "'.");
						throw new Exception("Value expected for specified argument '"+ arg + "'.");
					}
					else {
						value = arg.split(CHAR_EQUAL)[1];
					}
					
					arg = arg.split(CHAR_EQUAL)[0];
					arg = arg.trim();
				}
				else {
					EllieMaeLog.log(_log, "Value expected for specified argument '"+ arg + "'.");
					throw new Exception("Value expected for specified argument '"+ arg + "'.");					
				}
				
				Validate.isTrue(StringUtils.isNotBlank(value), "Value is missing for specified argument '"+ arg + "'.");
				value = value.trim();
				
				if(INPUT_EXCEL.equalsIgnoreCase(arg)){
					argumentKeyValue.put(INPUT_EXCEL, value);
				}
				else if(JENKINS_JOB_NAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(JENKINS_JOB_NAME, value);
				}	
				else if(ENVIRONMENT_NAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(ENVIRONMENT_NAME, value);
				}
				else if(ENVIRONMENT_USER_NAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(ENVIRONMENT_USER_NAME, value);
				}
				else if(ENVIRONMENT_PASSWORD.equalsIgnoreCase(arg)){
					argumentKeyValue.put(ENVIRONMENT_PASSWORD, value);
				}
				else if(ENVIRONMENT_CLIENT_ID.equalsIgnoreCase(arg)){
					argumentKeyValue.put(ENVIRONMENT_CLIENT_ID, value);
				}
				else if(PRIORITY.equalsIgnoreCase(arg)){
					argumentKeyValue.put(PRIORITY, value);
				}
				else if(TESTING_TYPE.equalsIgnoreCase(arg)){
					argumentKeyValue.put(TESTING_TYPE, value);
				}		
				else if(TEAM_NAME.equalsIgnoreCase(arg)){
					argumentKeyValue.put(TEAM_NAME, value);
				}			
				else if(LAYER.equalsIgnoreCase(arg)){
					argumentKeyValue.put(LAYER, value);
				}
				else if(ENVIRONMENT_TYPE.equalsIgnoreCase(arg)){
					argumentKeyValue.put(ENVIRONMENT_TYPE, value);
				}
				else if(HEALTH_CHECK.equalsIgnoreCase(arg)){
					argumentKeyValue.put(HEALTH_CHECK, value);
				}	
				else if(BROWSER_NAME.equalsIgnoreCase(arg))
				{
					argumentKeyValue.put(BROWSER_NAME, value);
				}
				else if(PARALLEL_MODE.equalsIgnoreCase(arg))
				{
					argumentKeyValue.put(PARALLEL_MODE, value);
				}
				else if(THREAD_COUNT.equalsIgnoreCase(arg))
				{
					argumentKeyValue.put(THREAD_COUNT, value);
				}
//				else if(OUTPUT_FOLDER.equalsIgnoreCase(arg)) {
//					argumentKeyValue.put(OUTPUT_FOLDER, value);
//				}
				else if(UPDATE_TEST_STATUS_TO_JIRA.equalsIgnoreCase(arg)) {
					argumentKeyValue.put(UPDATE_TEST_STATUS_TO_JIRA, value);
				}
				else if(JIRA_PROJECT_NAME.equalsIgnoreCase(arg)) {
					argumentKeyValue.put(JIRA_PROJECT_NAME, value);
				}
				else if(TEST_CYCLE_VERSION.equalsIgnoreCase(arg)) {
					argumentKeyValue.put(TEST_CYCLE_VERSION, value);
				}
				else if(RETRY_COUNT.equalsIgnoreCase(arg)) {
					argumentKeyValue.put(RETRY_COUNT, value);
				}
				
				else if(TESTING_VERSION.equalsIgnoreCase(arg)){
					argumentKeyValue.put(TESTING_VERSION, value);
					}
			}
		}
		
		if(!argumentKeyValue.containsKey(PRIORITY)) 
			argumentKeyValue.put(PRIORITY, ALL);
		
		if(!argumentKeyValue.containsKey(TEAM_NAME))
			argumentKeyValue.put(TEAM_NAME, ALL);
		
		if(!argumentKeyValue.containsKey(LAYER))
			argumentKeyValue.put(LAYER, ALL);			
		
		Validate.notBlank(argumentKeyValue.get(ENVIRONMENT_NAME), "The Environment Name value is required as input argument.");
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
			EllieMaeLog.log(_log, "Number of records for TestClassInfo: "+recordSetTestClassInfo.getCount());
			
			EllieMaeLog.log(_log,"---------------------------------------------------------------");
			EllieMaeLog.log(_log, "Priority requested			: "+argumentKeyValue.get(PRIORITY));
			EllieMaeLog.log(_log, "TeamName requested			: "+argumentKeyValue.get(TEAM_NAME));
			EllieMaeLog.log(_log, "Layer requested			: "+argumentKeyValue.get(LAYER));
			EllieMaeLog.log(_log, "TestingType requested		: "+argumentKeyValue.get(TESTING_TYPE));
			EllieMaeLog.log(_log, "EnvironmentType requested		: "+argumentKeyValue.get(ENVIRONMENT_TYPE));
			EllieMaeLog.log(_log, "HealthCheck requested		: "+argumentKeyValue.get(HEALTH_CHECK));
			//EllieMaeLog.log(_log, "OutputFolder requested		: "+argumentKeyValue.get(OUTPUT_FOLDER));
			EllieMaeLog.log(_log, "TestingVersion requested : "+argumentKeyValue.get(TESTING_VERSION));
			EllieMaeLog.log(_log,"---------------------------------------------------------------");
			
			while(recordSetTestClassInfo.next()){
				/** Filter the recordsets not matching the criteria. */
				if(StringUtils.isNotBlank(recordSetTestClassInfo.getField(CLASS_NAME)) && ((StringUtils.isNotBlank(recordSetTestClassInfo.getField(COLUMN_PRIORITY)) && argumentKeyValue.get(PRIORITY).toLowerCase().contains(recordSetTestClassInfo.getField(COLUMN_PRIORITY).toLowerCase())) || argumentKeyValue.get(PRIORITY).equalsIgnoreCase(ALL)) && ((StringUtils.isNotBlank(recordSetTestClassInfo.getField(TEAM_NAME)) && argumentKeyValue.get(TEAM_NAME).toLowerCase().contains(recordSetTestClassInfo.getField(COLUMN_TEAM_NAME).toLowerCase())) || argumentKeyValue.get(TEAM_NAME).equalsIgnoreCase(ALL)) && ((StringUtils.isNotBlank(recordSetTestClassInfo.getField(COLUMN_LAYER)) && argumentKeyValue.get(LAYER).toLowerCase().contains(recordSetTestClassInfo.getField(COLUMN_LAYER).toLowerCase())) || argumentKeyValue.get(LAYER).equalsIgnoreCase(ALL)))
					testClassesName.add(recordSetTestClassInfo.getField(CLASS_NAME));
			}
			
			testClassesName = new ArrayList<String>(new LinkedHashSet<String>(testClassesName));			
			Collections.sort(testClassesName);
			
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
			    
			    while(recordSetTestMethodInfo.next()){
			    	/** Filter the recordsets not matching the criteria. */
			    	//if(StringUtils.isNotBlank(recordSetTestMethodInfo.getField(TEST_METHOD_NAME)) && ((StringUtils.isNotBlank(recordSetTestMethodInfo.getField(COLUMN_PRIORITY)) && (argumentKeyValue.get(PRIORITY).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_PRIORITY).toLowerCase()) || argumentKeyValue.get(PRIORITY).equalsIgnoreCase(ALL))) && (StringUtils.isNotBlank(recordSetTestMethodInfo.getField(TEAM_NAME)) && (argumentKeyValue.get(TEAM_NAME).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_TEAM_NAME).toLowerCase()) || argumentKeyValue.get(TEAM_NAME).equalsIgnoreCase(ALL))) && (StringUtils.isNotBlank(recordSetTestMethodInfo.getField(COLUMN_LAYER)) && (argumentKeyValue.get(LAYER).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_LAYER).toLowerCase()) || argumentKeyValue.get(LAYER).equalsIgnoreCase(ALL))))) {
			    	  if(StringUtils.isNotBlank(recordSetTestMethodInfo.getField(TEST_METHOD_NAME)) && ((StringUtils.isNotBlank(recordSetTestMethodInfo.getField(COLUMN_PRIORITY)) && argumentKeyValue.get(PRIORITY).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_PRIORITY).toLowerCase())) || argumentKeyValue.get(PRIORITY).equalsIgnoreCase(ALL)) && ((StringUtils.isNotBlank(recordSetTestMethodInfo.getField(TEAM_NAME)) && argumentKeyValue.get(TEAM_NAME).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_TEAM_NAME).toLowerCase())) || argumentKeyValue.get(TEAM_NAME).equalsIgnoreCase(ALL)) && ((StringUtils.isNotBlank(recordSetTestMethodInfo.getField(COLUMN_LAYER)) && argumentKeyValue.get(LAYER).toLowerCase().contains(recordSetTestMethodInfo.getField(COLUMN_LAYER).toLowerCase())) || argumentKeyValue.get(LAYER).equalsIgnoreCase(ALL))) {	
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
		
		suite.setName(StringUtils.isBlank(suiteInfo.get(XML_SUITE_NAME)) ? "Suite": suiteInfo.get(XML_SUITE_NAME));
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
}