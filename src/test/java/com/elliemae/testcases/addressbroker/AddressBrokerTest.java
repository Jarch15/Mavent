package com.elliemae.testcases.addressbroker;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.testcases.generateandverify.GenerateInputFileAndVerifyUtility;

import Exception.FilloException;
import Fillo.Recordset;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * <b>Name:</b> AddressBrokerTest 
 * <b>Description: </b>This is Test class for Address Broker Test.
 * This processes the loan review input xmls and validate the ouput xmls file
 * with baseline and validation point from excel file.
 * 
 * @author <i>Jayesh Bhapkar</i>
 */
public class AddressBrokerTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(AddressBrokerTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	String decryptedPassword;
	private HashMap<String, String> environmentData= new HashMap<String, String>();

	/**
	 * <b>Name:</b>processInputXML<br>
	 * <b>Description:</b> Reads the file to be processed from shared path, process files
	 * against loan review service and save the output xml.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void processInputXML(HashMap<String, String> testData) 
	{
		readFolderPathsFromExcel(testData);
		if(testData.get("Baseline_Or_After").equalsIgnoreCase("Baseline"))
		{
			EllieMaeLog.log(_log, "Test Case Running for Baseline...", EllieMaeLogLevel.reporter);
		}
		else
		{
			EllieMaeLog.log(_log, "Test Case Running for After...", EllieMaeLogLevel.reporter);
		}
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		int THREAD_COUNT=1;
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}

		// Read SOAP Header and Trail from input folder
		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");
		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"AddressBrokerTest_data.xlsx");

		// Create the query for the data from test data sheet
		strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
		HashMap<String, HashMap<String, String>> testCaseData = null;

		try {
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		} catch (FilloException e1) {
			e1.printStackTrace();
		}

		HashMap<String, String> testCaseMethodData = testCaseData.get("1");
		String apiMethodName = testCaseMethodData.get("APIMethodName");

		// Get list of files to be processed from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if (CommonUtilityApplication.isExecutedFromEMDomain()) {
			File[] files = new File(testData.get("Input_File_Path")).listFiles();

			for (File file : files) {
				if (file.isFile()) {
					filesToProcess.add(file.getName());
				}
			}
		} else {

			try {
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(
						testData.get("Input_File_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork) {
					filesToProcess.add(networkFile.getName());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Process all files
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();
		for (String fileToProcess : filesToProcess) {
			Future<?> future = executor.submit(() -> {
				String currentThread = Thread.currentThread().getName() + ":::::";
				HashMap<String, String> responseMap = new HashMap<>();
				// Prepare SOAP Request
				String request = "";
				if (CommonUtilityApplication.isExecutedFromEMDomain()) {
					request = CommonUtility.readFile(
							testData.get("Input_File_Path") + File.separator + fileToProcess);
				} else {
					// Read input xml file from network location
					try {
						request = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path"),
								fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} catch (Exception e) {
						EllieMaeLog.log(_log, currentThread + "Exception occurred during reading file from Network : "
								+ fileToProcess, EllieMaeLogLevel.reporter);
						e.printStackTrace();
					}
				}
				// Append header and footer info needed for SOAP web service
				// call
				request = soapHeaderInfo + request + soapTrailInfo;
				try 
				{
					EllieMaeLog.log(_log, currentThread + "Processing input xml file : " + fileToProcess,
							EllieMaeLogLevel.reporter);

					responseMap = ObjSOAPMaventWebServiceCall.httpPost(apiMethodName, request);

				} 
				catch (Exception e) 
				{
					EllieMaeLog.log(_log, currentThread + "Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
							EllieMaeLogLevel.reporter);
					e.printStackTrace();
					sAssert.fail("Processing of file "+fileToProcess+" failed due to exception : "+e.getMessage());
				}

				if (responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode"))) 
				{
					// save output xml file
					String outputFilesPath ="";
					if(testData.get("Baseline_Or_After").equalsIgnoreCase("Baseline"))
					{
						outputFilesPath = testData.get("Output_File_Path")+"/"+testData.get("Output_Folder_Name")+"/"+testData.get("Baseline_Folder_Name")+"/";
					}
					else
					{
						outputFilesPath = testData.get("Output_File_Path")+"/"+testData.get("Output_Folder_Name")+"/"+testData.get("After_Folder_Name")+"/";
					}
					saveOuputXML(responseMap, testData,
							outputFilesPath, fileToProcess);
				} else 
				{
					// Do SoftAssert FAIL for TestNG result
					EllieMaeLog.log(_log,
							currentThread + "Found Internal Server Error, while processing : " + fileToProcess,
							EllieMaeLogLevel.reporter);
					sAssert.fail("Found Internal Server Error, hence failed " + fileToProcess);
				}
			});
			futures.add(future);

		}
		
		executor.shutdown();
		
		try {
	        for (Future<?> future : futures) {
	            future.get();
	            //System.out.println("Future Task Done : " + future.isDone());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		sAssert.assertAll();

	}
	
	/**
	 * <b>Name:</b>verifyAddressBroker<br>
	 * <b>Description:</b> Reads the output xml files from shared path and
	 * validate with Validation points and baseline.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 */
	@Test(dataProvider = "get-test-data-method")
	public void verifyAddressBroker(HashMap<String, String> testData) 
	{
		readFolderPathsFromExcel(testData);
		int THREAD_COUNT=1;
		Map<String,String> resultMapVerification1 = new HashMap<String,String> ();
		Map<String,String> resultMapVerification2 = new HashMap<String,String> ();
		List<String> failedVerficationList = new ArrayList<String>();
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}
		
		String outputFilesPath ="";
		if(testData.get("Baseline_Or_After").equalsIgnoreCase("Baseline"))
		{
			EllieMaeLog.log(_log, "Verification for Baseline...", EllieMaeLogLevel.reporter);
			outputFilesPath = testData.get("Output_File_Path")+"/"+testData.get("Output_Folder_Name")+"/"+testData.get("Baseline_Folder_Name")+"/";
		}
		else
		{
			EllieMaeLog.log(_log, "Verification for After...", EllieMaeLogLevel.reporter);
			outputFilesPath = testData.get("Output_File_Path")+"/"+testData.get("Output_Folder_Name")+"/"+testData.get("After_Folder_Name")+"/";
		}
		
		final String outputFilesFolder = outputFilesPath;

		// Get list of files to be validated from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if (CommonUtilityApplication.isExecutedFromEMDomain()) {
			File[] files = new File(outputFilesFolder).listFiles();

			for (File file : files) {
				if (file.isFile()) {
					filesToProcess.add(file.getName());
				}
			}
		} else {
			try {
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(
						outputFilesFolder, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork) {
					filesToProcess.add(networkFile.getName());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// validate each output file
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();
		for (String fileToProcess : filesToProcess) {
			Future<?> future = executor.submit(() -> {
				String currentThread = Thread.currentThread().getName() + ":::::";
				String outputXMLString="";
				
				// read file
				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					outputXMLString = CommonUtility.readFile(outputFilesFolder+File.separator+fileToProcess);
				}
				else
				{
					// Read input xml file from network location
					try {
						outputXMLString = CommonUtility.readFileFromNetworkSharedLocation(outputFilesFolder+"/",fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// Remove xml tags that not needed
				if(outputXMLString!=null)
				{
					outputXMLString = outputXMLString.replace("<?xml version=\"1.0\"?>","");
					outputXMLString = outputXMLString.replaceAll("<\\?xml-stylesheet(.*)PrinterFriendly.xsl\"\\?>", "");
					outputXMLString = outputXMLString.trim();
				}
				
				// Convert response XML Body to UTF-8 encoding 
				try {
					outputXMLString= new String(outputXMLString.getBytes(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				
				/* validation for Baseline Build - START */
				if(testData.get("Baseline_Or_After").equalsIgnoreCase("Baseline"))
				{
					String expectedResult = testData.get("Verification_Point_1");
					
					// Verify expectedResult xpath is present in output xml file or not.
					
					try 
					{
							List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(outputXMLString,expectedResult);
							if(xPathResultList!=null && !xPathResultList.isEmpty())
							{
								EllieMaeLog.log(_log, currentThread+"Verification_1 Passed for : "+fileToProcess, EllieMaeLogLevel.reporter);
								resultMapVerification1.put(fileToProcess, "PASS");
							}
							else
							{
								sAssert.fail("Actual result doesn't have specified Xpath. Verification_1 Failed for : "+fileToProcess);
								EllieMaeLog.log(_log, currentThread+"Verification_1 Failed for : "+fileToProcess, EllieMaeLogLevel.reporter);
								resultMapVerification1.put(fileToProcess, "FAIL");
								failedVerficationList.add("Verification_1 Failed for : "+fileToProcess);
							}
						
					} 
					catch (XPathExpressionException e) 
					{
						e.printStackTrace();
					} 
					catch (DOMException e) 
					{
						e.printStackTrace();
					}
				}

				
				/* validation for Baseline Build - END */
				
				
				/* validation for After Build - START */
				
				if(testData.get("Baseline_Or_After").equalsIgnoreCase("After"))
				{
					String baseXMLFile ="";
					if(CommonUtilityApplication.isExecutedFromEMDomain())
					{
						
						baseXMLFile = CommonUtility.readFile(testData.get("Output_File_Path")+File.separator+testData.get("Output_Folder_Name")+File.separator+testData.get("Baseline_Folder_Name")+File.separator+fileToProcess);	
					}
					else
					{
						// Read input xml file from network location
						try 
						{
							baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Output_File_Path")+"/"+testData.get("Output_Folder_Name")+"/"+testData.get("Baseline_Folder_Name")+"/",fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}

					}
					// Remove xml tags that not needed
					if(baseXMLFile!=null)
					{
						baseXMLFile = baseXMLFile.replace("<?xml version=\"1.0\"?>","");
						baseXMLFile = baseXMLFile.replaceAll("<\\?xml-stylesheet(.*)PrinterFriendly.xsl\"\\?>", "");
						baseXMLFile = baseXMLFile.trim();
					}
					
					// Convert response XML Body to UTF-8 encoding 
					try {
						baseXMLFile= new String(baseXMLFile.getBytes(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					// Verification_Point_1 - START
					String expectedResult = testData.get("Verification_Point_1");
					
					// Verify expectedResult xpath is present in output xml file or not.
					// If present, verify all attributes from baseline with after files. 
					
					try 
					{
							List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(outputXMLString,expectedResult);
							if(xPathResultList!=null && !xPathResultList.isEmpty())
							{
								// Get the attribute values for Baseline and compare with output xml
								Map<String,List<String>> expectedData = new HashMap<String,List<String>>();
								Map<String,List<String>> actualData = new HashMap<String,List<String>>();
								
								expectedData.put("ServiceGroupID", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/@id"));
								actualData.put("ServiceGroupID", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/@id"));
								
								expectedData.put("ServiceGroupAllowOverride", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/@allowOverride"));
								actualData.put("ServiceGroupAllowOverride", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/@allowOverride"));
								
								expectedData.put("ServiceGroupName", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/@name"));
								actualData.put("ServiceGroupName", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/@name"));
								
								expectedData.put("ServiceGroupReturn", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/@return"));
								actualData.put("ServiceGroupReturn", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/@return"));
								
								expectedData.put("ServiceGroupStatus", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/@status"));
								actualData.put("ServiceGroupStatus", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/@status"));
								
								expectedData.put("ServiceItemID", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@id"));
								actualData.put("ServiceItemID", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@id"));
								
								expectedData.put("ServiceItemMaventStatus", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@maventStatus"));
								actualData.put("ServiceItemMaventStatus", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@maventStatus"));
								
								expectedData.put("ServiceItemName", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@name"));
								actualData.put("ServiceItemName", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@name"));
								
								expectedData.put("ServiceItemRuleSubType", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@ruleSubType"));
								actualData.put("ServiceItemRuleSubType", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@ruleSubType"));
								
								expectedData.put("ServiceItemRuleType", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@ruleType"));
								actualData.put("ServiceItemRuleType", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@ruleType"));
								
								expectedData.put("ServiceItemStatus", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@status"));
								actualData.put("ServiceItemStatus", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/@status"));
								
								expectedData.put("ServiceItemMessageCustomer", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/Message/@customer"));
								actualData.put("ServiceItemMessageCustomer", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/Message/@customer"));
								
								expectedData.put("ServiceItemMessageData", XMLUtility.getXPathValueUsingXPath(baseXMLFile,"//ServiceGroup[@id='GEOCODE']/ServiceItem/Message/@data"));
								actualData.put("ServiceItemMessageData", XMLUtility.getXPathValueUsingXPath(outputXMLString,"//ServiceGroup[@id='GEOCODE']/ServiceItem/Message/@data"));
								
								resultMapVerification1.put(fileToProcess, "PASS");
								for(String key : expectedData.keySet())
								{
									if(expectedData.get(key)!=null)
									{
										EllieMaeLog.log(_log, currentThread+" : "+fileToProcess+": Verification_1 : expectedData : "+key+" -> "+expectedData.get(key), EllieMaeLogLevel.reporter);
										EllieMaeLog.log(_log, currentThread+" : "+fileToProcess+": Verification_1 : actualData : "+key+" -> "+actualData.get(key), EllieMaeLogLevel.reporter);
										if(!expectedData.get(key).containsAll(actualData.get(key)))
										{
											resultMapVerification1.put(fileToProcess, "FAIL");
										}
									}
								}
								
								
								if(resultMapVerification1.get(fileToProcess).equalsIgnoreCase("PASS"))
								{
									EllieMaeLog.log(_log, currentThread+"Verification_1 Passed for : "+fileToProcess, EllieMaeLogLevel.reporter);
								}
								else
								{
									EllieMaeLog.log(_log, currentThread+"Verification_1 Failed for : "+fileToProcess, EllieMaeLogLevel.reporter);
									failedVerficationList.add("Verification_1 Failed for : "+fileToProcess);
								}
							}
							else
							{
								sAssert.fail("Actual result doesn't have specified Xpath. Verification_1 Failed for : "+fileToProcess);
								EllieMaeLog.log(_log, currentThread+"Verification_1 Failed for : "+fileToProcess, EllieMaeLogLevel.reporter);
								resultMapVerification1.put(fileToProcess, "FAIL");
							}
						
					} 
					catch (XPathExpressionException e) 
					{
						e.printStackTrace();
					} 
					catch (DOMException e) 
					{
						e.printStackTrace();
					}

					// Verification_Point_1 - END
				
					// Verification_Point_2 and above - START
					// do xml comparison between actual response and base xml file
					
					for(int i=2; i<=100;i++)
					{
						String verifyElement = testData.get("Verification_Point_"+i);
						if(verifyElement!=null && !verifyElement.trim().isEmpty())
						{
							if(baseXMLFile.contains("<"+verifyElement))
							{
								String attributeEnd = "";
								if(verifyElement.contains("="))
								{
									attributeEnd = "</"+verifyElement.split(" ")[0]+">";
								}
								else
								{
									attributeEnd = "</"+verifyElement+">";
								}
								boolean response = xmlComparison(baseXMLFile, outputXMLString,
										"<"+verifyElement, attributeEnd);
			
								EllieMaeLog.log(_log, currentThread+"The comparison result is : " + response, EllieMaeLogLevel.reporter);
								EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
			
								//sAssert.assertEquals(response, true, "The file does not match for : " + fileToProcess);
								if(response)
								{
									EllieMaeLog.log(_log, currentThread+"Verification_Point_"+i+" Passed for : "+fileToProcess, EllieMaeLogLevel.reporter);
									//resultMapVerification2.put(fileToProcess, "PASS");
								}
								else
								{
									EllieMaeLog.log(_log, currentThread+"Verification_Point_"+i+"  Failed for : "+fileToProcess, EllieMaeLogLevel.reporter);
									sAssert.fail("The file < " + fileToProcess+" > does not match with Baseline file. Verification_Point_"+i+"  Failed.");
									//resultMapVerification2.put(fileToProcess, "FAIL");
									failedVerficationList.add("Verification_Point_"+i+"  Failed for : "+fileToProcess);
								}
							}
							else
							{
								EllieMaeLog.log(_log, currentThread+"Verification_Point_"+i+"  Skipped, since element "+verifyElement+" not present in baseline for file : "+fileToProcess, EllieMaeLogLevel.reporter);
							}
						}
					}
					
//					String verifyElement = testData.get("Verification_Point_2");
//					
//					if(baseXMLFile.contains("<"+verifyElement+">"))
//					{
//						boolean response = xmlComparison(baseXMLFile, outputXMLString,
//								"<"+verifyElement+">", "</"+verifyElement+">");
//	
//						EllieMaeLog.log(_log, currentThread+"The comparison result is : " + response, EllieMaeLogLevel.reporter);
//						EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
//	
//						//sAssert.assertEquals(response, true, "The file does not match for : " + fileToProcess);
//						if(response)
//						{
//							EllieMaeLog.log(_log, currentThread+"Verification_2 Passed for : "+fileToProcess, EllieMaeLogLevel.reporter);
//							resultMapVerification2.put(fileToProcess, "PASS");
//						}
//						else
//						{
//							EllieMaeLog.log(_log, currentThread+"Verification_2 Failed for : "+fileToProcess, EllieMaeLogLevel.reporter);
//							sAssert.fail("The file < " + fileToProcess+" > does not match with Baseline file. Verification_2 Failed.");
//							resultMapVerification2.put(fileToProcess, "FAIL");
//						}
//					}
//					else
//					{
//						EllieMaeLog.log(_log, currentThread+"Verification_2 Skipped, since element "+verifyElement+" not present in baseline for file : "+fileToProcess, EllieMaeLogLevel.reporter);
//					}
				}
				
				// Verification_Point_2 and above - END
				/* validation for After Build - END */
				
			});
			futures.add(future);

		}
		
		executor.shutdown();
		
		try {
	        for (Future<?> future : futures) {
	            future.get();
	            //System.out.println("Future Task Done : " + future.isDone());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		

		/* Print Result of Failed Verification on Console */
//		EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
//		EllieMaeLog.log(_log, "Verification_1 Failed for files:- ", EllieMaeLogLevel.reporter);
//		for(String fileToProcess : resultMapVerification1.keySet())
//		{
//			if(!resultMapVerification1.get(fileToProcess).equalsIgnoreCase("PASS"))
//			{
//				EllieMaeLog.log(_log, fileToProcess+" : "+resultMapVerification1.get(fileToProcess), EllieMaeLogLevel.reporter);
//			}
//		}
//		EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
//		EllieMaeLog.log(_log, "Verification_2 Failed for files:- ", EllieMaeLogLevel.reporter);
//		for(String fileToProcess : resultMapVerification2.keySet())
//		{
//			if(!resultMapVerification2.get(fileToProcess).equalsIgnoreCase("PASS"))
//			{
//				EllieMaeLog.log(_log, fileToProcess+" : "+resultMapVerification2.get(fileToProcess), EllieMaeLogLevel.reporter);
//			}
//		}
		EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"SUMMARY",EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
		for(String message : failedVerficationList)
		{
			EllieMaeLog.log(_log, message, EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
		}
		sAssert.assertAll();

	}
	
	/**
	 * <b>Name:</b>saveOuputXML<br>
	 * <b>Description:</b> Extracts the output from responseMap and generates the 
	 * response XML file. Saves the XML file on shared path.<br>
	 * @author <i>Jayesh Bhapkar</i> 
	 * @param responseMap
	 * @param testData
	 * @param result_output_Folder
	 * @param outputXMLFileName
	 */
	private void saveOuputXML(HashMap<String,String> responseMap,HashMap<String, String> testData, String result_output_Folder, String outputXMLFileName )
	{
		// Get the response xml from response Map
		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY"));	
		
		// Extract the response to get DataPackage node inside response xml received
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		HashMap<String, String> xPathKeyValueMap = new HashMap<>();
		xPathKeyMap.put("DataPackage", "//reviewDocumentReturn");
		try 
		{
			xPathKeyValueMap = XMLUtilityApplication.readXMLtoExtractXPathValue(responseXML,xPathKeyMap);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		String responseXMLBody = xPathKeyValueMap.get("DataPackage");

		/* Create output xml file using the response xml received at network location*/
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				//Create the folders if not exists
				File directory = new File(result_output_Folder);
				try
				{
				   if (! directory.exists()){
				       directory.mkdirs();
				   }
				}
				catch(Exception e)
				{
					EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
				}
				 // create file
				CommonUtilityApplication.createOutputFile(result_output_Folder+File.separator+outputXMLFileName, responseXMLBody);				
			}
			else
			{
				// Create the folders if not exists
				NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				String sourceFilePath = "smb:" + testData.get("Output_File_Path");
				SmbFile sFile = new SmbFile(sourceFilePath, auth);
				try
				{
					if(!sFile.exists()){
						sFile.mkdirs();
					}
				}
				catch(Exception e)
				{
					EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
				}
				 // create file on network
				CommonUtilityApplication.writeFileToNetworkSharedLocation(result_output_Folder, outputXMLFileName, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during generating output xml file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}

	}
	
	/* Read the folders details from excel sheet from shared path */
	private void readFolderPathsFromExcel(HashMap<String,String> testData)
	{
		//Copy Excel file to local data folder
		EllieMaeLog.log(_log, "Copying excel file to local data folder", EllieMaeLogLevel.reporter);
		File f = new File("");
		String dataDirectoryPath = f.getAbsolutePath();
		dataDirectoryPath = dataDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"data";

		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				CommonUtility.copyFilesOrFolder(testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"), dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"), FileType.FILE);
			}
			else
			{
				CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"),dataDirectoryPath,testData.get("FolderDataSheetFile"));
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception during copying excel sheet to data folder", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading Folders data from excel sheet", EllieMaeLogLevel.reporter);
		String query = "Select * from  \"Folders\"";
		getFolderData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query);

		
	}
	
	/* read folder data from excel and populate testData hashmap*/
	private void getFolderData(HashMap<String,String> testData,String excelSheetFilePath, String query )
	{
		
		try
		{
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(excelSheetFilePath,query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				while(recordSet.next())
				{
					String Input_File_Path = recordSet.getField("Input_File_Path");
					if(Input_File_Path!=null && !Input_File_Path.trim().isEmpty())
					{
						testData.put("Input_File_Path", Input_File_Path);
					}
					String Output_File_Path = recordSet.getField("Output_File_Path");
					if(Output_File_Path!=null && !Output_File_Path.trim().isEmpty())
					{
						testData.put("Output_File_Path", Output_File_Path);
					}
					String Baseline_Or_After = recordSet.getField("Baseline/After");
					if(Baseline_Or_After!=null && !Baseline_Or_After.trim().isEmpty())
					{
						testData.put("Baseline_Or_After", Baseline_Or_After);
					}
					String Output_Folder_Name = recordSet.getField("Output_Folder_Name");
					if(Output_Folder_Name!=null && !Output_Folder_Name.trim().isEmpty())
					{
						testData.put("Output_Folder_Name", Output_Folder_Name);
					}
					String Baseline_Folder_Name = recordSet.getField("Baseline_Folder_Name");
					if(Baseline_Folder_Name!=null && !Baseline_Folder_Name.trim().isEmpty())
					{
						testData.put("Baseline_Folder_Name", Baseline_Folder_Name);
					}
					String After_Folder_Name = recordSet.getField("After_Folder_Name");
					if(After_Folder_Name!=null && !After_Folder_Name.trim().isEmpty())
					{
						testData.put("After_Folder_Name", After_Folder_Name);
					}
//					String Verification_Point_1 = recordSet.getField("Verification_Point_1");
//					if(Verification_Point_1!=null && !Verification_Point_1.trim().isEmpty())
//					{
//						testData.put("Verification_Point_1", Verification_Point_1);
//					}
//					String Verification_Point_2 = recordSet.getField("Verification_Point_2");
//					if(Verification_Point_2!=null && !Verification_Point_2.trim().isEmpty())
//					{
//						testData.put("Verification_Point_2", Verification_Point_2);
//					}
					for(int i=1; i<=100; i++)
					{
						try
						{
							String Verification_Point = recordSet.getField("Verification_Point_"+i);
							if(Verification_Point!=null && !Verification_Point.trim().isEmpty())
							{
								testData.put("Verification_Point_"+i, Verification_Point);
							}
						}
						catch(FilloException e)
						{
							// Suppress FilloException if Verification_Point Field not found in excel file
						}
					}
				}
			}
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
	}	
	
	// XML comparison to compare actual response with base response
	public static boolean xmlComparison(String source,String target, String attributeStart, String attributeEnd)
	{
				try
				{
	                    int sourceIndex1= source.indexOf(attributeStart);
	                    int sourceIndex2= source.lastIndexOf(attributeEnd);
	                    String sourceSubstring = source.substring(sourceIndex1, sourceIndex2+attributeEnd.length());
	                    sourceSubstring = sourceSubstring.trim();
	                    
	                    int targetIndex1= target.indexOf(attributeStart);
	                    int targetIndex2= target.lastIndexOf(attributeEnd);
	                    String targetSubstring = target.substring(targetIndex1, targetIndex2+attributeEnd.length());
	                    targetSubstring = targetSubstring.trim();                           
	                    
	                    // Convert String to XML document
	                    Document xmlDocExpectedResult = GenerateInputFileAndVerifyUtility.convertStringToXMLDocument(sourceSubstring);	
	                    Document xmlDocActualResult = GenerateInputFileAndVerifyUtility.convertStringToXMLDocument(targetSubstring);
	                    
	                    // Remove nodes from xml which is to be ignored before comparison
	                    removeNode(xmlDocExpectedResult,"Score");
	                    removeNode(xmlDocActualResult,"Score");
	                    
	                    String sourceXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocExpectedResult);
	                    //EllieMaeLog.log(_log,"Source = " +sourceXMLString,EllieMaeLogLevel.reporter);
	                    String targetXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocActualResult);
	                    //EllieMaeLog.log(_log,"Target = " +targetXMLString,EllieMaeLogLevel.reporter);
	                    
	                    return APIValidationMethodsApplication.compareXMLUsingXMLUnit(sourceXMLString, targetXMLString, attributeStart, attributeEnd);
				}
				catch(Exception e)
				{
						EllieMaeLog.log(_log, "Exception occurred during xmlComparison : "+e.getMessage(), EllieMaeLogLevel.reporter);
						e.printStackTrace();
						return false;
				}
	    }
	   
	   // Remove node from XML document 
		private static void removeNode(Document doc, String elementName) 
		{
			NodeList nodeList = doc.getElementsByTagName(elementName);	
			List<Element> nodesToRemove = new ArrayList<Element>();
			
			for (int num=0; num<nodeList.getLength(); num++) 
	        {
				 	Element node = (Element) nodeList.item(num);
				    nodesToRemove.add(node);
	         }
			
			for(Element node : nodesToRemove)
			{
		           // retrieve the element 'Response'				   
		           Element element = (Element) doc.getElementsByTagName("GeoCodeData").item(0);		  				    
		  
		           // remove the specific node				  
		           element.removeChild(node);
			}
			
		}

}
