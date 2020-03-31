package com.elliemae.testcases.generateandverify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.core.asserts.SoftAssert;

import Exception.FilloException;
import Fillo.Connection;
import Fillo.Fillo;
import Fillo.Recordset;

/*
 * Author : Jayesh Bhapkar
 * Description : Utility class to assist GenerateInputFileAndVerifyTest
 */
public class GenerateInputFileAndVerifyUtility {
	
	public static Logger _log = Logger.getLogger(GenerateInputFileAndVerifyUtility.class);
	private static HashMap<String, String> responseMap = new HashMap<>();
	
 	/**
 	 * <b>Name:</b>generateInputXML
 	 * <b>Description:</b> This method is used to generate an input xml file
 	 * based on provided template. 
 	 * It creates an input xml file based on provided template
 	 * and create an xml file on shared path.
 	 * 
 	 * @param testData 
 	 * @param templatePath 
 	 * @param inputDirectoryPath
 	 * @param inputXMLFolder
 	 * @param inputXMLFileName
 	 * @param xPathFromExcel
 	 * 
 	 **/
 	public static void generateInputXML(HashMap<String, String> testData, String templatePath, String inputDirectoryPath, String inputXMLFolder,String inputXMLFileName, String xPathFromExcel)
 	{
         File xmlFile = new File(templatePath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;
         
         try {
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
             
             // update XML using XPath for test data
             
             // Generate xPathValueMap based on excel sheet data
             Map<String,String> xPathValueMap = generateXpathValueMap(xPathFromExcel);
             
             // Update the XML for each record in xPathValueMap
             for(String xPathKey : xPathValueMap.keySet())
             {
            	 if(xPathKey.contains("@"))
            	 {
            		 // Need to update specific attribute of the element
 					String [] tempStringArray = xPathKey.split("/@");
 					String xpath = tempStringArray[0];
 					String attributeToUpdate = tempStringArray[tempStringArray.length-1];
 					updateXMLAttributeForXpath(doc,xpath,attributeToUpdate,xPathValueMap.get(xPathKey));
            	 }
            	 else
            	 {
            		 // update the data attribute of the element for the xpath provided
            		 updateXMLAttributeForXpath(doc,xPathKey,xPathValueMap.get(xPathKey)); 
            	 }
             }
             
             // update XML using XPath for LoanID
             String loanID = inputXMLFileName.replace(".xml", "");
             updateXMLAttributeForXpath(doc,"/DataPackage/LoanID",loanID);
             
             //write the updated document to file
             doc.getDocumentElement().normalize();
             TransformerFactory transformerFactory = TransformerFactory.newInstance();
             Transformer transformer = transformerFactory.newTransformer();
             DOMSource source = new DOMSource(doc);
             StreamResult result = new StreamResult(new File(inputDirectoryPath+File.separator +inputXMLFileName));
             transformer.setOutputProperty(OutputKeys.INDENT, "yes");
             transformer.transform(source, result);
             EllieMaeLog.log(_log, "XML input file generated successfully", EllieMaeLogLevel.reporter);
             
             // copy generated xml file to shared apth
     		try 
    		{
     			if(CommonUtilityApplication.isExecutedFromEMDomain())
     			{
     				// Copying File Locally
     				CommonUtility.copyFilesOrFolder(inputDirectoryPath+File.separator +inputXMLFileName, inputXMLFolder+File.separator+inputXMLFileName, FileType.FILE);
     			}
     			else
     			{
     				// Copying File to network
     				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),inputDirectoryPath+File.separator +inputXMLFileName,inputXMLFolder,inputXMLFileName);
     			}
    		}
    		catch (Exception e) 
    		{
    			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
    			e.printStackTrace();
    		}
     		
     		EllieMaeLog.log(_log, "Generated XML input file copied to shared path successfully", EllieMaeLogLevel.reporter);
     		
             // delete local file
     		CommonUtility.deleteFile(inputXMLFileName, "input");
             
         } 
         catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) 
         {
             e1.printStackTrace();
         }
     }
 	
 	
 	/**
 	 * <b>Name:</b>verifyActualResult
 	 * <b>Description:</b> This method is used to verify actual response with
 	 * expected result. 
 	 * 
 	 * @param testData 
 	 * @param testCaseData 
 	 * @param ObjSOAPMaventWebServiceCall
 	 * @param softAssert
 	 * @param inputDirectoryPath
 	 * 
 	 **/
 	public static void verifyActualResult(HashMap<String, String> testData, HashMap<String, HashMap<String, String>> testCaseData, SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall,  SoftAssert sAssert, String inputDirectoryPath)
 	{
 		
 		HashMap<String, String> environmentData =EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);

		for(int i=1;i<=testCaseData.size();i++)
		{
			HashMap<String, String> testCaseMethodData = testCaseData.get(""+i);
		
			if(testCaseMethodData.get("Execute")!=null && testCaseMethodData.get("Execute").equalsIgnoreCase("Yes"))
			{
				// Get Template File name
				String [] testCaseTemplateFile = testCaseMethodData.get("InputData").split("/");
				String testCaseTemplateFileName = testCaseTemplateFile[testCaseTemplateFile.length-1];
				
				//Copy template file to local input folder
				EllieMaeLog.log(_log, "Copying Test Case template file to local input folder", EllieMaeLogLevel.reporter);
	     		
				try 
				{
					if(CommonUtilityApplication.isExecutedFromEMDomain())
					{
						CommonUtility.copyFilesOrFolder(testCaseMethodData.get("InputData"), inputDirectoryPath+File.separator+testCaseTemplateFileName, FileType.FILE);
					}
					else
					{
						CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testCaseMethodData.get("InputData"),inputDirectoryPath,testCaseTemplateFileName);
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				// read folder data
				String test_Files_Folder = testCaseMethodData.get("Test_Loan_Folder");
				test_Files_Folder = test_Files_Folder.replace("\\", "/");
				final String testFilesFolder = test_Files_Folder;
				String result_output_Folder = testCaseMethodData.get("Results_Output_Folder");
				result_output_Folder = result_output_Folder.replace("\\", "/");
				final String resultOutputFolder = result_output_Folder;
				
				
				// Read data from copied excel file
				EllieMaeLog.log(_log, "Reading Test Case data from Test Case template file", EllieMaeLogLevel.reporter);
				String query = "Select * from  \"Test Case\"";
				HashMap<String, HashMap<String, String>> excelData = GenerateInputFileAndVerifyUtility.getExcelData(inputDirectoryPath+File.separator+testCaseTemplateFileName, query);

				// Store test cast Status and actual result in map with key as Test Case ID
				Map<String,String> testCaseStatusMap = new HashMap<String,String>();
				Map<String,String> actualResultMap = new HashMap<String,String>();
				
				// Read thread count for the environment for multithreading
				int THREAD_COUNT=1;
				environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);				
				if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
				{
					THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
				}
				
				final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
				final List<Future<?>> futures = new ArrayList<>();	
				
				// Read header and tail info from input folder
				String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo.xml","input");				
				String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo.xml","input");
				
	     		for(String key : excelData.keySet())
	     		{
	     				// Executor to run below block of code in parallel using multithreading
						Future<?> future = executor.submit(() -> 
						{
			     			boolean exceptionDuringProcessing = false;
			     			boolean statusCodeOtherThan200 = false;
			     			boolean xmlDocExpectedResultIsNull = false;
			     			
							String currentThread = Thread.currentThread().getName()+":::::";
			     			HashMap<String, String> rowData = excelData.get(key);
							String actualResult="";
							String testCaseStatus="FAIL";
							
			     			String inputXMLFileName = rowData.get("Test_File_Name");
			     			if(!inputXMLFileName.contains(".xml"))
			     			{
			     				inputXMLFileName = rowData.get("Test_File_Name")+".xml";
			     			}
			     			
			     			/* Create SOAP Request - START */
			     			String request ="";
							if(CommonUtilityApplication.isExecutedFromEMDomain())
							{
								try
								{
									request = CommonUtility.readFile(testFilesFolder+File.separator+inputXMLFileName);
								}
								catch (Exception e)
								{
									e.printStackTrace();
									EllieMaeLog.log(_log, currentThread+"Exception occurred during reading the input xml file : "+inputXMLFileName, EllieMaeLogLevel.reporter);
									actualResult=e.getMessage();
									actualResult = actualResult.replace("'", "");
									testCaseStatus="FAIL";
									/* update actual result and status maps*/
									testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
									actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
									sAssert.fail(e.getMessage());
									sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
									// set flag to true
									exceptionDuringProcessing = true;
								}
							}
							else
							{
								// Read input xml file from network location
								try 
								{
									request = CommonUtility.readFileFromNetworkSharedLocation(testFilesFolder+"/",inputXMLFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
								} 
								catch (Exception e) 
								{
									e.printStackTrace();
									EllieMaeLog.log(_log, currentThread+"Exception occurred during reading the input xml file : "+inputXMLFileName, EllieMaeLogLevel.reporter);
									actualResult=e.getMessage();
									actualResult = actualResult.replace("'", "");
									testCaseStatus="FAIL";
									/* update actual result and status maps*/
									testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
									actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
									sAssert.fail(e.getMessage());
									sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
									// set flag to true
									exceptionDuringProcessing = true;
								}
							}
							request=inputParam1+request+inputParam2;
							/* Create SOAP Request - END */
			     			
			     			// Call Web service method to process each input xml
							try 
							{
								responseMap = ObjSOAPMaventWebServiceCall.httpPost(testCaseMethodData.get("APIMethodName"),request);
					
							} 
							catch (Exception e) // Handle in case of exception - update status and skip below code to continue with remaining test cases
							{
								e.printStackTrace();
								EllieMaeLog.log(_log, currentThread+"Exception occurred during procesing the input xml file : "+inputXMLFileName, EllieMaeLogLevel.reporter);
								actualResult=e.getMessage();
								actualResult = actualResult.replace("'", "");
								testCaseStatus="FAIL";
								/* update actual result and status maps*/
								testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
								actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
								sAssert.fail(e.getMessage());
								sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
								// set flag to true
								exceptionDuringProcessing = true;
							}
							
							// if exception occurred skip below code to continue with for loop  
							if(!exceptionDuringProcessing)
							{
								// Incase of service error (status code other than 200) update  actual result and status maps and continue with remaining test cases
								if(responseMap!=null && !responseMap.get("STATUSCODE").equals("200"))
								{
									EllieMaeLog.log(_log, currentThread+"Error "+responseMap.get("STATUSCODE")+" occurred during processing file : "+inputXMLFileName, EllieMaeLogLevel.reporter);
									actualResult="Error "+responseMap.get("STATUSCODE")+" occurred during processing file";
									testCaseStatus="FAIL";
									/* update actual result and status maps*/
									testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
									actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
									sAssert.fail("Error "+responseMap.get("STATUSCODE")+" occurred during processing file : "+inputXMLFileName);
									sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
									// set flag to true
									statusCodeOtherThan200 = true;
								}
								
								// if statusCodeOtherThan200 occurred skip below code to continue with for loop 
								if(!statusCodeOtherThan200)
								{
									String responseXMLBody = ObjSOAPMaventWebServiceCall.saveOuputXMLAndReturnResponseBody(responseMap, testData, resultOutputFolder, inputXMLFileName);
									
									
									String expectedResult = rowData.get("Expected_Results");
									
									/* validate expected result with responseXMLBody (actual result) */
									/* If expected result is xml element validate for that element only*/
									if(expectedResult.trim().startsWith("<"))
									{
									
									if(rowData.get("Fired")!=null)
										{
											// Convert expectedResult from excel to XML document
											Document xmlDocExpectedResult = GenerateInputFileAndVerifyUtility.convertStringToXMLDocument(expectedResult);
											
											// If failed to convert expected result string to XML document - update actual result and status maps and continue with remaining test cases
											if(xmlDocExpectedResult==null)
											{
												EllieMaeLog.log(_log, currentThread+"Exception occurred during converting expectedResult String to XML Document for : "+inputXMLFileName, EllieMaeLogLevel.reporter);
												actualResult="Exception occurred during converting expectedResult String to XML Document for : "+inputXMLFileName;
												testCaseStatus="FAIL";
												/* update actual result and status maps*/
												testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
												actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
												sAssert.fail(actualResult);
												sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
												// set flag to true
												xmlDocExpectedResultIsNull = true;
											}
											
											// if xmlDocExpectedResultIsNull then skip below code to continue with for loop
											if(!xmlDocExpectedResultIsNull)
											{
													/* Get list of all expected result attribute names */
													Set<String> expectedServiceItemAttributeNamesList = GenerateInputFileAndVerifyUtility.getListOfAllAttributeNames(xmlDocExpectedResult,"ServiceItem");
													
													// Validation logic if expected result contains ServiceItem element 
													if(!expectedServiceItemAttributeNamesList.isEmpty())
													{
														Set<String> expectedMessageAttributeNamesList = GenerateInputFileAndVerifyUtility.getListOfAllAttributeNames(xmlDocExpectedResult,"Message");
														List<String> expectedAllAttributesList =  new ArrayList<String>();
														expectedAllAttributesList.addAll(expectedServiceItemAttributeNamesList);
														expectedAllAttributesList.addAll(expectedMessageAttributeNamesList);
														
														Map<String,List<String>> mapActualResult = new HashMap<String,List<String>>();
														Map<String,String> mapExpectedResult = new HashMap<String,String>();
														
														/* Get ServiceItem attributes expected and actual values & put in mapExpectedResult & mapActualResult respectively */
														for(String attributeName : expectedServiceItemAttributeNamesList)
														{
															// Get id value
															String id = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDocExpectedResult,"ServiceItem","id");
															
															// Get attribute value and put in expected result map (mapExpectedResult)
															String attributeValue = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDocExpectedResult,"ServiceItem",attributeName);
															mapExpectedResult.put(attributeName, attributeValue);
															try 
															{
																// Get actual attribute values using XPath and put in actual result map (mapActualResult)
																List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(responseXMLBody,"//ServiceItem[@id='"+id+"']/@"+attributeName);
																mapActualResult.put(attributeName, xPathResultList);
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
														
														/* Get Message attributes expected and actual values & put in mapExpectedResult & mapActualResult respectively */
														for(String attributeName : expectedMessageAttributeNamesList)
														{
															// Get id value
															String id = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDocExpectedResult,"ServiceItem","id");
															
															// Get attribute value and put in expected result map (mapExpectedResult)
															String attributeValue = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDocExpectedResult,"Message",attributeName);
															mapExpectedResult.put(attributeName, attributeValue);
															try 
															{
																// Get actual attribute values using XPath and put in actual result map (mapActualResult)
																List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(responseXMLBody,"//ServiceItem[@id='"+id+"']/Message/@"+attributeName);
																mapActualResult.put(attributeName, xPathResultList);
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
														
														/* Generate Actual result String for updating into excel */
														if(mapActualResult.get("id")!=null)
														{
															for(String attributeName : expectedAllAttributesList)
															{
																actualResult = actualResult+attributeName+"="+mapActualResult.get(attributeName)+"\n";
															}
														}
														else
														{
															actualResult="\""+mapExpectedResult.get("name")+"\" not fired.";
														}
														EllieMaeLog.log(_log, currentThread+"Actual Result : \n"+actualResult, EllieMaeLogLevel.reporter);
														
														
														/* Validate expected result (mapExpectedResult) and actual result (mapActualResult) */
														
														// Ignore $ and % amounts. Replace $ and % amounts in expected and actual results before comparison
														if(mapExpectedResult.get("data")!=null && mapActualResult.get("data")!=null)
														{
															ignoreData(mapExpectedResult,mapActualResult,"data");
														}						
														
														// validation in case of message to be fired
														if(rowData.get("Fired").equals("Y"))
														{
																for(String attributeName : mapExpectedResult.keySet())
																{
																	if(mapActualResult.get(attributeName)!=null && mapActualResult.get(attributeName).contains(mapExpectedResult.get(attributeName)))
																	{
																		testCaseStatus="PASS";
																		sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																	}
																	else
																	{
																		testCaseStatus="FAIL";
																		sAssert.fail("Actual result doesn't match with Expected result");
																		sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																		break;
																	}
																}
														}
														// validation in case of message not to be fired
														else if (rowData.get("Fired").equals("N"))
														{
																if(mapActualResult.get("id") !=null && mapActualResult.get("id").contains(mapExpectedResult.get("id")))
																{
																	testCaseStatus="FAIL";
																	sAssert.fail("The message should not be fired");
																	sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																}
																else
																{
																	testCaseStatus="PASS";
																	sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																}
														}
														
														// Updated the map with test status and actual result with key as Test Case ID
														testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
														actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
														
													}
													// Validation logic if expected result has any element other than ServiceItem element 
													else
													{
														
														// Create element-AttributeNameList map for expected Result
														Map<String,Set<String>> elementAttributeMap = GenerateInputFileAndVerifyUtility.generateMapOfAllElementsAndAttributes(xmlDocExpectedResult);
														String rootElement = getRootElement(xmlDocExpectedResult);
														EllieMaeLog.log(_log, currentThread+"element-AttributeNameList map for expected Result : "+elementAttributeMap, EllieMaeLogLevel.reporter);
														
														Map<String,Map<String,List<String>>> mapActualResult = new HashMap<String,Map<String,List<String>>>();
														Map<String,Map<String,String>> mapExpectedResult = new HashMap<String,Map<String,String>>();
														
														
														/* Loop Through all elements and its attribute. populate mapExpectedResult & mapActualResult */
											     		for(String element : elementAttributeMap.keySet())
											     		{
											     			Set<String> attributes = elementAttributeMap.get(element);
											     			Map<String,String> expectedResultMapAttributeNameValue = new HashMap<String,String>();
											     			Map<String,List<String>> actualResultMapAttributeNameValue = new HashMap<String,List<String>>();
											     			for(String attributeName : attributes)
											     			{
																// Get attribute value for each attribute and put in mapAttributeNameValue
																String attributeValue = GenerateInputFileAndVerifyUtility.getAttributeValue(xmlDocExpectedResult,element,attributeName);
																expectedResultMapAttributeNameValue.put(attributeName, attributeValue);
																
																// Get actual attribute values using XPath and put in actual result map (mapActualResult)
																try 
																{
																	if(rootElement!=null && !rootElement.isEmpty() && !rootElement.equals(element))
																	{
																		List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(responseXMLBody,"//"+rootElement+"//"+element+"/@"+attributeName);
																		actualResultMapAttributeNameValue.put(attributeName, xPathResultList);
																	}
																	else
																	{
																		List<String> xPathResultList = XMLUtility.getXPathValueUsingXPath(responseXMLBody,"//"+element+"/@"+attributeName);
																		actualResultMapAttributeNameValue.put(attributeName, xPathResultList);
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
											     			// Put mapAttributeNameValue for each element in mapExpectedResult
											     			mapExpectedResult.put(element, expectedResultMapAttributeNameValue);
											     			EllieMaeLog.log(_log, currentThread+"mapExpectedResult : "+mapExpectedResult, EllieMaeLogLevel.reporter);
											     			mapActualResult.put(element,actualResultMapAttributeNameValue);
											     			EllieMaeLog.log(_log, currentThread+"mapActualResult : "+mapActualResult, EllieMaeLogLevel.reporter);
											     		}
														
														/* Generate Actual result String for updating into excel */
														if(!mapActualResult.isEmpty())
														{
															for(String element : elementAttributeMap.keySet())
															{
																Set<String> listOfAttributeNames = elementAttributeMap.get(element);
																for(String attributeName : listOfAttributeNames)
																{
																	actualResult = actualResult+element+" -> "+attributeName+"="+mapActualResult.get(element).get(attributeName)+"\n";
																}
															}
														}
														else
														{
															actualResult="Expected Attributes not found in Actual Result";
														}
														EllieMaeLog.log(_log, currentThread+"Actual Result : \n"+actualResult, EllieMaeLogLevel.reporter);
														
														
														/* Validate expected result (mapExpectedResult) and actual result (mapActualResult) */
														
														// validation in case of message to be fired
														EllieMaeLog.log(_log, currentThread+"Validating expected and actual results...", EllieMaeLogLevel.reporter);
														if(rowData.get("Fired").equals("Y"))
														{
															EllieMaeLog.log(_log, currentThread+"Validating for Message should be fired...", EllieMaeLogLevel.reporter);
																outer : for(String element : mapExpectedResult.keySet())
																{
																	Set<String> listOfAttributeNames = elementAttributeMap.get(element);
																	for(String attributeName : listOfAttributeNames)
																	{
																		EllieMaeLog.log(_log, currentThread+"Validating element : "+element+" Attribute : "+attributeName, EllieMaeLogLevel.reporter);
																		EllieMaeLog.log(_log, currentThread+"Expected Result for element : "+element+"->"+attributeName+" is : "+mapExpectedResult.get(element).get(attributeName), EllieMaeLogLevel.reporter);
																		EllieMaeLog.log(_log, currentThread+"Actual Result for element : "+element+"->"+attributeName+" is : "+mapActualResult.get(element).get(attributeName), EllieMaeLogLevel.reporter);
																		if(mapActualResult.get(element).get(attributeName)!= null
																				&& mapActualResult.get(element).get(attributeName).contains(mapExpectedResult.get(element).get(attributeName)))
																		{
																			testCaseStatus="PASS";
																			sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																		}
																		else
																		{
																			testCaseStatus="FAIL";
																			sAssert.fail("Actual result doesn't match with Expected result");
																			sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																			break outer;
																		}
																	}
	
																}
														}
														// validation in case of message not to be fired
														else if (rowData.get("Fired").equals("N"))
														{
															EllieMaeLog.log(_log, currentThread+"Validating for Message should not be fired...", EllieMaeLogLevel.reporter);
															outer : for(String element : mapExpectedResult.keySet())
															{
																Set<String> listOfAttributeNames = elementAttributeMap.get(element);
																for(String attributeName : listOfAttributeNames)
																{
																	EllieMaeLog.log(_log, currentThread+"Validating element : "+element+" Attribute : "+attributeName, EllieMaeLogLevel.reporter);
																	EllieMaeLog.log(_log, currentThread+"Expected Result for element : "+element+"->"+attributeName+" is : "+mapExpectedResult.get(element).get(attributeName), EllieMaeLogLevel.reporter);
																	EllieMaeLog.log(_log, currentThread+"Actual Result for element : "+element+"->"+attributeName+" is : "+mapActualResult.get(element).get(attributeName), EllieMaeLogLevel.reporter);
							
																	if(mapActualResult.get(element).get(attributeName).contains(mapExpectedResult.get(element).get(attributeName)))
																	{
																		testCaseStatus="FAIL";
																		sAssert.fail("Actual result doesn't match with Expected result");
																		sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																		break outer;																	
																	}
																	else
																	{
																		testCaseStatus="PASS";
																		sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
																	}
																}
	
															}
														}
														
														// Updated the map with test status and actual result with key as Test Case ID
														testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
														actualResultMap.put(rowData.get("Test_Case_ID"), actualResult);
														
													}
														
												}
											}
										}
										/* validate expected result xml with actual output xml */
										/* If expected result is path of xml file, then compare the expected xml with actual output xml.*/
										if(expectedResult.trim().startsWith("//"))
										{
											// Read expectedResult (baseline) xml
											String baseXMLFile ="";
											if(CommonUtilityApplication.isExecutedFromEMDomain())
											{
												
												baseXMLFile = CommonUtility.readFile(expectedResult);	
											}
											else
											{
												// Read input xml file from network location
												try 
												{
													String [] splitString = expectedResult.split("/");
													String baseXMLFileName = splitString[splitString.length-1];
													String baseXMLFilePath = expectedResult.replace(baseXMLFileName, "");
													baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(baseXMLFilePath,baseXMLFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
												} 
												catch (Exception e) 
												{
													e.printStackTrace();
												}

											}
											
											// compare baseline xml with actual output xml
											// do xml comparison between actual response and base xml file
											boolean response = xmlComparison(baseXMLFile, responseXMLBody,
													"<ServiceResponse", "</ServiceResponse>");

											EllieMaeLog.log(_log, "The comparison result is : " + response, EllieMaeLogLevel.reporter);
											EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);

											if(response)
											{
												testCaseStatus="PASS";
												sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
												// Update the map with test status and actual result with key as Test Case ID
												testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
												actualResultMap.put(rowData.get("Test_Case_ID"), "Actual Response XML matches with Baseline XML");
											}
											else
											{
												testCaseStatus="FAIL";
												sAssert.fail("Actual result xml doesn't match with Baseline xml file for : "+inputXMLFileName);
												sAssert.getTestStatus().put(rowData.get("Test_Case_ID"), testCaseStatus);
												// Update the map with test status and actual result with key as Test Case ID
												testCaseStatusMap.put(rowData.get("Test_Case_ID"), testCaseStatus);
												actualResultMap.put(rowData.get("Test_Case_ID"), "Actual Response XML does not matches with Baseline XML");
											}
											
										}
									}
								}
							});
		            futures.add(future);
					
	     		}
	     		
	    		executor.shutdown();
	    		
	    		try {
	    	        for (Future<?> future : futures) {
	    	            future.get();
	    	            System.out.println("Future Task Done : " + future.isDone());
	    	        }
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	    }
	     		
				/* update excel with actual value, status */
	     		for(String key : testCaseStatusMap.keySet())
	     		{
	     			String testCaseStatus = testCaseStatusMap.get(key);
	     			String actualResult = actualResultMap.get(key);
	     			GenerateInputFileAndVerifyUtility.updateResultToExcelFileUsingInputData(inputDirectoryPath+File.separator+testCaseTemplateFileName, testCaseStatus, actualResult, key);
	     		}

	     		
	     		// copy updated test case template excel sheet to shared path
	     		boolean isFiledCopiedSuccesfullyToSharedPath = false;
	     		isFiledCopiedSuccesfullyToSharedPath= GenerateInputFileAndVerifyUtility.copyTemplateFileToSharedPath(testData, inputDirectoryPath+File.separator+testCaseTemplateFileName,testCaseMethodData.get("InputData"));
	     		
	     		// delete local template file at the end of test case
	     		if(isFiledCopiedSuccesfullyToSharedPath)
	     		{
		     		EllieMaeLog.log(_log, "Deleting the Local Test Case template file", EllieMaeLogLevel.reporter);
		     		CommonUtility.deleteFile(testCaseTemplateFileName, "input");
	     		}
	     		
			}
			
		}
 	}
 	
	/**
	 * <b>Name:</b>updateXMLAttributeForXpath
	 * <b>Description:</b> This method is used to update the attribute
	 * value for provided xPath.
	 * It takes three parameters XML document object to update, XPath of
	 * element and value to be updated.
	 * 
	 * @param document 
	 * @param xPath 
	 * @param value
	 * 
	 **/
    private static void updateXMLAttributeForXpath(Document document, String xPath, String value)
    {
   	 XPath xpath = XPathFactory.newInstance().newXPath();
   	 try 
   	 {
       	 	// update element's attribute value for given xPath
			NodeList nodeList = (NodeList) xpath.evaluate(xPath, document,XPathConstants.NODESET);
			Element element = (Element)nodeList.item(0);
			element.setAttribute("data", value);
        	EllieMaeLog.log(_log, "XML element "+element.getNodeName()+" updated", EllieMaeLogLevel.reporter);
    	
		} 
   	 catch (XPathExpressionException e) 
   	 {
   		 	EllieMaeLog.log(_log, "Error updating XML element", EllieMaeLogLevel.reporter);
   		 	e.printStackTrace();
   	 }
    }
    
	/**
	 * <b>Name:</b>updateXMLAttributeForXpath
	 * <b>Description:</b> This method is used to update the specific attribute
	 * value for provided xPath.
	 * It takes four parameters XML document object to update, XPath of
	 * element, attribute and value to be updated.
	 * 
	 * @param document 
	 * @param xPath 
	 * @param attribute
	 * @param value
	 * 
	 **/
    private static void updateXMLAttributeForXpath(Document document, String xPath, String attribute, String value)
    {
   	 XPath xpath = XPathFactory.newInstance().newXPath();
   	 try 
   	 {
       	 	// update element's attribute value for given xPath
			NodeList nodeList = (NodeList) xpath.evaluate(xPath, document,XPathConstants.NODESET);
			Element element = (Element)nodeList.item(0);
			element.setAttribute(attribute, value);
        	EllieMaeLog.log(_log, "XML element "+element.getNodeName()+" updated", EllieMaeLogLevel.reporter);
    	
		} 
   	 catch (XPathExpressionException e) 
   	 {
   		 	EllieMaeLog.log(_log, "Error updating XML element", EllieMaeLogLevel.reporter);
   		 	e.printStackTrace();
   	 }
    }    
    
	/**
	 * <b>Name:</b>generateXpathValueMap
	 * <b>Description:</b> This method generates a Map with key as xPath and 
	 * value as element value based on the data from excel data sheet. 
	 * 
	 * @param xPathFromExcel 
	 * 
	 **/
    private static Map<String,String> generateXpathValueMap(String xPathFromExcel)
    {
    	List<String> xPathList = loadStringDataToList(xPathFromExcel);    	
    	
    	Map<String,String> xPathValueMap = new HashMap<String, String>();
    	for(int i=0; i<xPathList.size(); i++)
    	{
    		// Separate out XPath and XPath value
    		String [] xPathAndValue = xPathList.get(i).split("=");
			String xPath = xPathAndValue[0];
			xPath = xPath.replace("[1]", "");
			xPath = xPath.replace("[", "");
			xPath = xPath.replace("]", "");
			
			String xPathValue=xPathAndValue[1];
			xPathValue = xPathValue.replace("[]", "[BLANK]");
			xPathValue = xPathValue.replace("[", "");
			xPathValue = xPathValue.replace("]", "");
    		
    		if(xPathValue.equals("BLANK"))
    		{
    			xPathValue="";
    		}
    		xPathValueMap.put(xPath, xPathValue);
    	}  	
    	return xPathValueMap;
    	
    }
    
	/**
	 * <b>Name:</b>loadStringDataToList
	 * <b>Description:</b> This method creates a List of data by separating 
	 * using delimiter as newline (\n) or ];[
	 * 
	 * @param inputString 
	 * 
	 **/
	public static List<String> loadStringDataToList(String inputString)
	{

		List<String> list=null;
		if(inputString.contains("\n") || inputString.contains(FrameworkConsts.SPLITSEMICOLONPATTERN) )
		{
			String pattern="";
			if(inputString.contains("\n"))
				pattern="\n";
			else
				pattern=FrameworkConsts.SPLITSEMICOLONPATTERN;
			list = new ArrayList<String>(Arrays.asList(inputString.split(Pattern.quote(pattern))));
		}
		else
		{
			list=new ArrayList<String>();
			list.add(inputString);
		}

      
		return list;
	}

	/**
	 * <b>Name:</b>getExcelData
	 * <b>Description:</b> This method returns the data in hashmap format,
	 * by reading an template excel file.
	 * 
	 * @param excelFilePath 
	 * @param query 
	 * 
	 **/
	public static HashMap<String, HashMap<String, String>> getExcelData(String excelFilePath, String query)
	{
		System.setProperty("ROW", "8");
		System.setProperty("COLUMN", "1");
		
		Fillo fillo=new Fillo();
		Connection connection = null;
		Recordset recordSetTestCaseData = null;
		HashMap<String, HashMap<String, String>> allData = new HashMap<String, HashMap<String, String>>();
		
		try {
			try {
				connection = fillo.getConnection(excelFilePath);
			} catch (FilloException e) {
				EllieMaeLog.log(_log,"Exception in Connection in getRecordSet Using Fillo is: "+e.getMessage());
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				EllieMaeLog.log(_log, "Exception StackTrace: "+stackTrace.toString());
			}
	
			try 
			{
				recordSetTestCaseData = connection.executeQuery(query);
				
				HashMap<String, String> testCaseData = new HashMap<>();

				Object[] list= new Object[ recordSetTestCaseData.getFieldNames().size()];

				list=recordSetTestCaseData.getFieldNames().toArray();

				

				while(recordSetTestCaseData.next()){

					for(Object obj : list)
					{
						String key=obj.toString();	
						testCaseData.put(key, recordSetTestCaseData.getField(key));				
					}
					if(!testCaseData.get("Test_Case_ID").equals(""))
					{
						allData.put(testCaseData.get("Test_Case_ID"), new HashMap<>(testCaseData));
						testCaseData.clear();
					}
				}
			} 
			catch (FilloException e) 
			{
				EllieMaeLog.log(_log,"Exception in RecordSet in getRecordSet Using Fillo is: "+e.getMessage());
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				EllieMaeLog.log(_log, "Exception StackTrace: "+stackTrace.toString());
			}
		}
		finally {
			if(null != connection) {
				connection.close();
			}
			// Reset the System property 
			System.setProperty("ROW", "1");
			System.setProperty("COLUMN", "1");
		}
		
		return allData;
		
	}
	
	/**
	 * <b>Name:</b>getFolderDataFromExcel
	 * <b>Description:</b> This method returns the folder data in hashmap format,
	 * by reading an template excel file.
	 * 
	 * @param excelFilePath 
	 * @param query 
	 * 
	 **/
	public static HashMap<String, String> getFolderDataFromExcel(String excelFilePath, String query)
	{
		Fillo fillo=new Fillo();
		Connection connection = null;
		Recordset recordSetTestCaseData = null;
		HashMap<String, String> testCaseData = new HashMap<>();
		
		try 
		{
				try 
				{
					connection = fillo.getConnection(excelFilePath);
				} 
				catch (FilloException e) 
				{
					EllieMaeLog.log(_log,"Exception in Connection in getRecordSet Using Fillo is: "+e.getMessage());
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					EllieMaeLog.log(_log, "Exception StackTrace: "+stackTrace.toString());
				}
	
			try 
			{
				recordSetTestCaseData = connection.executeQuery(query);
				
				

				Object[] list= new Object[ recordSetTestCaseData.getFieldNames().size()];

				list=recordSetTestCaseData.getFieldNames().toArray();

				

				while(recordSetTestCaseData.next()){

					for(Object obj : list)
					{
						String key=obj.toString();	
						testCaseData.put(key, recordSetTestCaseData.getField(key));				
					}
				}
			} 
			catch (FilloException e) 
			{
				EllieMaeLog.log(_log,"Exception in RecordSet in getRecordSet Using Fillo is: "+e.getMessage());
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				EllieMaeLog.log(_log, "Exception StackTrace: "+stackTrace.toString());
			}
		}
		finally {
			if(null != connection) {
				connection.close();
			}
		}
		
		return testCaseData;
		
	}
	
	/**
	 * <b>Name:</b>updateResultToExcelFileUsingInputData
	 * <b>Description:</b> This method updates the excel sheet
	 * with status, actual result in test case template file.
	 * 
	 * @param excelSheetFilePath 
	 * @param status 
	 * @param actualResult 
	 * @param testFileName 
	 * 
	 **/
	public static void updateResultToExcelFileUsingInputData(String excelSheetFilePath, String status, String actualResult, String testCaseID)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		Date today =  new Date();
		String dateToStr = DateFormat.getDateTimeInstance().format(today);  
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			// Escape single quotes from actual result for update query
			actualResult = actualResult.replaceAll("'","''");
			
			/* Update the Status Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update \"Test Case\" Set \"Pass/Fail\" ='"+status+"', Date='"+dateToStr+"', Actual_Results='"+actualResult+"',  Tester='Automated' where Test_Case_ID='"
						+ testCaseID + "'";
			connection.executeUpdate(strUpdateQuery);
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
	}
	
	/**
	 * <b>Name:</b>copyTemplateFileToSharedPath
	 * <b>Description:</b> This method copies the local template file 
	 * to shared path.
	 * 
	 * @param testData 
	 * @param sourceExcelSheetFilePath 
	 * @param destinationPath 
	 * 
	 **/
	public static boolean copyTemplateFileToSharedPath(HashMap<String, String> testData, String sourceExcelSheetFilePath, String destinationPath)
	{
		String [] splitResult = destinationPath.split("/");
		String destainationFileName = "temp";
		destainationFileName = splitResult[splitResult.length-1];		
		String destinationFolderPath = destinationPath.replace(destainationFileName, "");
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				// Copying File Locally
				CommonUtility.copyFilesOrFolder(sourceExcelSheetFilePath, destinationPath, FileType.FILE);
			}
			else
			{
				// Copying File to network
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),sourceExcelSheetFilePath,destinationFolderPath,destainationFileName);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying excel file to output fodler", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * <b>Name:</b>convertStringToXMLDocument
	 * <b>Description:</b> This method converts string to 
	 * XML document.
	 * 
	 * @param xmlStr 
	 * 
	 **/
	public static Document convertStringToXMLDocument(String xmlStr) 
	{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder(); 
            builder.setErrorHandler(new MyErrorHandler());
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } 
        catch (Exception e) 
        {  
            e.printStackTrace();  
        } 
        return null;
    }
	
	/**
	 * <b>Name:</b>attribute
	 * <b>Description:</b> This method is used to get attribute value for given xml document, node and attribute.
	 * 
	 * @param doc 
	 * @param node
	 * @param attribute
	 * 
	 **/
	public static String getAttributeValue(Document doc, String node, String attribute)
	{
		String attributeValue = null;
		try
		{
			doc.getDocumentElement().normalize();	
			NodeList nList = doc.getElementsByTagName(node);	
			Node nNode = nList.item(0);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
	
				Element eElement = (Element) nNode;	
				attributeValue = eElement.getAttribute(attribute);
	
			}
	    } 
		catch (Exception e) 
		{
		e.printStackTrace();
	    }
		
		return attributeValue;
	}
	
	
	private static void ignoreData(Map<String,String> mapExpectedResult,Map<String,List<String>> mapActualResult, String attributeName)
	{
		String expectedValue = mapExpectedResult.get(attributeName);
		List<String> valuesToIgnoreList = getListOfValuesToIgnore(expectedValue);
		String ignoreValue ="<IGNORE>";
		
		// Replace values to Ignore for mapExpectedResult
		String expectedValueModified = expectedValue;
		
		for(String value : valuesToIgnoreList)
		{
			expectedValueModified = expectedValueModified.replace(value, ignoreValue);
		}
		mapExpectedResult.put(attributeName,expectedValueModified);		
		
		// Replace values to Ignore for mapActualResult
		List<String> actualResultListOriginal = mapActualResult.get(attributeName);
		List<String> actualResultListModified = new ArrayList<String>();
		actualResultListModified.addAll(actualResultListOriginal);
		
		// Modified List
		for(String actualResultValue : actualResultListOriginal)
		{
			List<String> actualValuesToIgnoreList = getListOfValuesToIgnore(actualResultValue);
			String actualResultModifiedValue = actualResultValue;
			for(String value : actualValuesToIgnoreList)
			{
				actualResultModifiedValue = actualResultModifiedValue.replace(value, ignoreValue);
				
			}
			actualResultListModified.remove(actualResultValue);
			actualResultListModified.add(actualResultModifiedValue);
		}	

		mapActualResult.put(attributeName, actualResultListModified);
		
	}
	
	private static List<String> getListOfValuesToIgnore(String data)
	{
		List<String> valuesToIgnoreList = new ArrayList<String>();
		
		// Ignore Dollar Amounts
		String regexDollarAmount = "\\(\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?[0-9][0-9]?)\\)";
		Pattern pattern = Pattern.compile(regexDollarAmount, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(data);
		while (matcher.find())
		{
			valuesToIgnoreList.add(matcher.group());
		}
		
		// Ignore Percent Amounts
		String regexPercentAmount = "\\(\\d+(?:\\.\\d+)?%\\)";
		pattern = Pattern.compile(regexPercentAmount, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(data);
		while (matcher.find())
		{
			valuesToIgnoreList.add(matcher.group());
		}
		
		// Ignore date inside brackets () e.g. (05-04-2018), (2018-04-01)
		String regexDate = "\\(\\d{2}-\\d{2}-\\d{4}\\)|\\(\\d{4}-\\d{2}-\\d{2}\\)";
		pattern = Pattern.compile(regexDate, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(data);
		while (matcher.find())
		{
			valuesToIgnoreList.add(matcher.group());
		}
		
		return valuesToIgnoreList;
	}
	
	public static Map<String,Set<String>> generateMapOfAllElementsAndAttributes(Document doc)
	{
		Map<String,Set<String>> resultMap = new HashMap<String,Set<String>>();
		
		Set<String> elementsList = getListOfAllElements(doc);
		for(String element : elementsList)
		{
			Set<String> attributeList = getListOfAllAttributeNames(doc, element);
			resultMap.put(element, attributeList);
			
		}
		
		System.out.println(resultMap);
		return resultMap;
	}
	
	public static Set<String> getListOfAllElements (Document doc)
	{
		Set<String> elementsList = new HashSet<String> ();
		
	    doc.getDocumentElement().normalize();
	   // System.out.println("Root element " + doc.getDocumentElement().getNodeName());

	    NodeList nodeList=doc.getElementsByTagName("*");
	    for (int i=0; i<nodeList.getLength(); i++) 
	    {
	        // Get element
	        Element element = (Element)nodeList.item(i);
	        //System.out.println(element.getNodeName());
	        elementsList.add(element.getNodeName());
	    }
	    
	    return elementsList;
		
	}
	
	public static String getRootElement(Document doc)
	{
	    doc.getDocumentElement().normalize();
	    String rootElement = doc.getDocumentElement().getNodeName();
	   // System.out.println("Root element " + doc.getDocumentElement().getNodeName());
	    EllieMaeLog.log(_log, "Root Element : "+rootElement, EllieMaeLogLevel.reporter);
	    return rootElement;
	}
	
	/**
	 * <b>Name:</b>attribute
	 * <b>Description:</b> This method is used to return List of all attribute names present for an element in xml document.
	 * 
	 * @param doc 
	 * @param elementName
	 * 
	 **/
	public static Set<String> getListOfAllAttributeNames(Document doc, String elementName) 
	{
		Set<String> attributeNameList = new HashSet<String>();
		NodeList entriesServiceItem = doc.getElementsByTagName(elementName);							
		for (int num=0; num<entriesServiceItem.getLength(); num++) 
        {
			 	Element node = (Element) entriesServiceItem.item(num);
				

		        // get a map containing the attributes of this node
		        NamedNodeMap attributes = node.getAttributes();

		        // get the number of nodes in this map
		        int numAttrs = attributes.getLength();

		        for (int i = 0; i < numAttrs; i++) 
		        {
			       Attr attr = (Attr) attributes.item(i);
			       attributeNameList.add(attr.getNodeName());

		        }
         }
		
        return attributeNameList;
	}
	
	// XML comparison to compare actual response with base response
	public static boolean xmlComparison(String source,String target, String attributeStart, String attributeEnd)
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
	                    removeNode(xmlDocExpectedResult,"Attachments");
	                    removeNode(xmlDocActualResult,"Attachments");
	                    
	                    // remove attributes from xml which is to be ignored
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"DataPackage","customerTransID");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"DataPackage","customerTransID");
	                    
	                    String sourceXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocExpectedResult);
	                    //EllieMaeLog.log(_log,"Source = " +sourceXMLString,EllieMaeLogLevel.reporter);
	                    String targetXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocActualResult);
	                    //EllieMaeLog.log(_log,"Target = " +targetXMLString,EllieMaeLogLevel.reporter);
	                    
	                    return APIValidationMethodsApplication.compareXMLUsingXMLUnit(sourceXMLString, targetXMLString, attributeStart, attributeEnd);
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
		           Element element = (Element) doc.getElementsByTagName("ServiceResponse").item(0);		  				    
		  
		           // remove the specific node				  
		           element.removeChild(node);
			}
			
		}
	
}	
	

// Custom error handler class to suppress XML parsing errors
class MyErrorHandler implements ErrorHandler 
{
	  public void warning(SAXParseException e) throws SAXException 
	  {
	  }
	  public void error(SAXParseException e) throws SAXException 
	  {
	  }
	  public void fatalError(SAXParseException e) throws SAXException 
	  {
	  }

	}