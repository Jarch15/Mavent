package com.elliemae.testcases.syncservices;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import Fillo.Recordset;


public class AdminMobileTest  extends EllieMaeApplicationBase 
{

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(AdminMobileTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	
	/* Author : Jayesh Bhapkar
	 * Description : .
	 *  */
	@Test(dataProvider = "get-test-data-method")	
	public void adminSetup(HashMap<String,String> testData)
	{
		readExcelProperties(testData);
		int waitTime = 30000;
		if(testData.get("WAIT_BEFORE_LOAN_REVIEW")!=null && !testData.get("WAIT_BEFORE_LOAN_REVIEW").isEmpty())
		{
			waitTime = Integer.parseInt(testData.get("WAIT_BEFORE_LOAN_REVIEW"))*1000; // Convert into milliseconds
		}
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		SoftAssert softAssert = new SoftAssert();

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"AdminMobileTest_data.xlsx");

		// Create the query for the data from test data sheet
		strTestDataQuery = "Select * from " + testData.get("TestDataSheet") + " where Test_Case_Name = '"
				+ testData.get("Test_Case_Name") + "' order by SequenceID";

		// Execute the query and save it in a hash map testCaseData
		HashMap<String, HashMap<String, String>> testCaseData = null;

		try 
		{
			testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
		} 
		catch (FilloException e1) 
		{
			e1.printStackTrace();
		}
		
		HashMap<String, String> testCaseMethodData = testCaseData.get("1");
		
		
		/* update admin set up test file with new company id */
		
		// Generate new company id 
		
		String companyId= generateCompanyId();		
		
		// update generated companyId into input xml file
		Map<String,String> xPathAndValueMap = new HashMap<String,String>();
 
		xPathAndValueMap.put("/ServiceData/Service[@id='2']/Request/Parameter[@name='companyID']", companyId);
		xPathAndValueMap.put("/ServiceData/Service[@id='2']/Request/Parameter[@name='companyName']", companyId);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String serviceDate = sdf.format(new Date());
		
		xPathAndValueMap.put("/ServiceData/Service[@id='3']/Request/Parameter[@name='serviceDate']", serviceDate);
		xPathAndValueMap.put("/ServiceData/Service[@id='3']/Request/Parameter[@name='companyName']", companyId+"-1");
		
		xPathAndValueMap.put("/ServiceData/Service[@id='4']/Request/Parameter[@name='companyID']", companyId);
		
		xPathAndValueMap.put("/ServiceData/Service[@id='5']/Request/Parameter[@name='companyName']", companyId+"-1");
		
		updateInputXMLFile(testData,"Http-Admin-AssoSerExistingCO.xml",xPathAndValueMap,"value" );

		// Call Web service method to process admin set up file
		try {
			responseMap = ObjSOAPMaventWebServiceCall.syncWebServiceCall(testData, testCaseMethodData, "adminSetup",
					testCaseMethodData.get("InputData"));

		} catch (Exception e) {
			e.printStackTrace();
			EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
			Assert.assertTrue(false, "Test case failed due to exception");
		}

		// Do XPath validation if Status code is 200
		String companyIdFromResponse1="";
		String companyIdFromResponse2="";
		
		if (responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode"))) 
		{
			String responseBody = ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap,testCaseMethodData.get("InputData"), testData, "adminSetup");
			if (!(testCaseMethodData.get("ValidationMethod").isEmpty())) 
			{
				try 
				{
					apiValidationMethods.xPathValidation(responseBody,testCaseMethodData.get("ValidationContent"), sAssert);

				} catch (IOException e) 
				{

					e.printStackTrace();
				}
			}
			// Extract companyId From response
			companyIdFromResponse1 = getXMLAttributeForXpath(convertStringToDocument(responseBody),"/ServiceData/Service[@id='2']/Response/Return[@name='companyID']","value");
			companyIdFromResponse2 = getXMLAttributeForXpath(convertStringToDocument(responseBody),"/ServiceData/Service[@id='3']/Response/Return[@name='companyID']","value");
		} 
		else 
		{
			// Do SoftAssert FAIL for TestNG result
			softAssert.fail("Found Internal Server Error, hence failed " + "Http-Admin-AssoSerExistingCO.xml");
		}
        
		/* Update companyId received from Response into loan review input xml request and review loan */
		xPathAndValueMap.clear();
		xPathAndValueMap.put("/DataPackage", companyIdFromResponse1);
		updateInputXMLFile(testData,"Http-NewCO.xml",xPathAndValueMap,"customerID");
		
		xPathAndValueMap.clear();
		xPathAndValueMap.put("/DataPackage", companyIdFromResponse2);
		updateInputXMLFile(testData,"Http-NewCO-1.xml",xPathAndValueMap,"customerID");
		
		
		/* Added wait before executing get review */
		EllieMaeLog.log(_log, "Waiting before calling loan review", EllieMaeLogLevel.reporter);
		CommonUtilityApplication.threadWait(waitTime);
		
		/* Call loan review for above updated input xml files and do xpath validation */
		for(int i=2;i<=testCaseData.size();i++)
		{
			testCaseMethodData = testCaseData.get(""+i);
			try 
			{
				/* Added additional wait before executing get review */
				//EllieMaeLog.log(_log, "Waiting for 30 Seconds before calling loan review", EllieMaeLogLevel.reporter);
				CommonUtilityApplication.threadWait(30000);
				responseMap = ObjSOAPMaventWebServiceCall.webServiceHTTPTest(testData,testCaseMethodData);
	
			} catch (Exception e) 
			{
				e.printStackTrace();
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
				Assert.assertTrue(false, "Test case failed due to exception");
			}
			
			// Do XPath validation if Status code is 200
			if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
			{
		        if (!(testCaseMethodData.get("ValidationMethod").isEmpty()))
		        {
		        	try 
		        	{
		        		apiValidationMethods.xPathValidation(ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap, testCaseMethodData, testData), 
		        				testCaseMethodData.get("ValidationContent"),sAssert);
				
					} 
		        	catch (IOException e) 
		        	{
						
						e.printStackTrace();
					}
		        }
			}
			else
			{
				// Do SoftAssert FAIL for TestNG result
				softAssert.fail("Found Internal Server Error, hence failed "+testCaseMethodData.get("JIRAID"));
			}
		}
		
		
        // Assertion for Xpath Validations
		sAssert.assertAll();
		
		// Assertion for failed test cases due to server error
		softAssert.assertAll();
		
	}
	
	private static String generateCompanyId()
	{
		Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmss");
        String datetime = ft.format(dNow);
        String companyID = FrameworkConsts.ENVIRONMENTNAME+"-"+datetime;
		EllieMaeLog.log(_log, "Generated company id for admin setup test : "+companyID, EllieMaeLogLevel.reporter);
		return companyID;
	}
	
    private static void updateInputXMLFile(HashMap<String,String> testData, String inputXMLFileName, Map<String,String> xPathAndValueMap, String attributeName)
    {
    	/* copy input xml from shared path to input directory */
		File f = new File("");
		String inputDirectoryPath = f.getAbsolutePath();
		inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"syncservices"+ File.separator +"input";;

		// delete the file if exists already
		EllieMaeLog.log(_log, "Deleting the input xml file if already exists", EllieMaeLogLevel.reporter);
		File file = new File(inputDirectoryPath+File.separator+inputXMLFileName);
		file.setWritable(true);
		CommonUtility.deleteFile(inputXMLFileName, "input");
 		
		EllieMaeLog.log(_log, "Creating input xml file in local input folder", EllieMaeLogLevel.reporter);
 		String templateFileContent ="";
 		try
 		{
 			if(CommonUtilityApplication.isExecutedFromEMDomain())
 			{
 				// Read file locally
 				templateFileContent = CommonUtility.readFile(testData.get("Input_File_Path")+File.separator+inputXMLFileName);			     				
 			}
 			else
 			{
 				// Read  file from network location
 				templateFileContent = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path")+"/",inputXMLFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));

 			}
 		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		CommonUtility.createFileWriteLine(inputDirectoryPath+File.separator+inputXMLFileName, templateFileContent);					

		String inputFileRelativePath = CommonUtility.getRelativeFilePath("input", inputXMLFileName);
    	
		/* convert file to XML document and update xml document using xPath */
        File xmlFile = new File(inputFileRelativePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            // Update the XML for each record in xPathValueMap
            for(String xPathKey : xPathAndValueMap.keySet())
            {
           	 updateXMLAttributeForXpath(doc,xPathKey,attributeName,xPathAndValueMap.get(xPathKey)); 
            }

            /* write the updated document to file */
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(inputDirectoryPath+File.separator +inputXMLFileName));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            EllieMaeLog.log(_log, "XML input file updated successfully", EllieMaeLogLevel.reporter);
            
            
            /* copy generated xml file to shared path */			
     		
     		try 
    		{
     			if(CommonUtilityApplication.isExecutedFromEMDomain())
     			{
     				// Copying File Locally
     				CommonUtility.copyFilesOrFolder(inputDirectoryPath+File.separator +inputXMLFileName, testData.get("Input_File_Path")+File.separator+inputXMLFileName, FileType.FILE);
     			}
     			else
     			{
     				// Copying File to network
     				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),inputDirectoryPath+File.separator +inputXMLFileName,testData.get("Input_File_Path"),inputXMLFileName);
     			}
    		}
    		catch (Exception e) 
    		{
    			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
    			e.printStackTrace();
    		}
     		
     		EllieMaeLog.log(_log, "Generated XML input file copied to shared path successfully", EllieMaeLogLevel.reporter);
     		
             /* delete local file */
     		CommonUtility.deleteFile(inputXMLFileName, "input");
             
         } 
         catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) 
         {
             e1.printStackTrace();
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
    private static void updateXMLAttributeForXpath(Document document, String xPath, String attributeName, String value)
    {
   	 XPath xpath = XPathFactory.newInstance().newXPath();
   	 try 
   	 {
       	 	// update element's attribute value for given xPath
			NodeList nodeList = (NodeList) xpath.evaluate(xPath, document,XPathConstants.NODESET);
			Element element = (Element)nodeList.item(0);
			element.setAttribute(attributeName, value);
        	EllieMaeLog.log(_log, "XML element "+element.getNodeName()+" updated", EllieMaeLogLevel.reporter);
    	
		} 
   	 catch (XPathExpressionException e) 
   	 {
   		 	EllieMaeLog.log(_log, "Error updating XML element", EllieMaeLogLevel.reporter);
   		 	e.printStackTrace();
   	 }
    }
    
    
    private static String getXMLAttributeForXpath(Document document, String xPath, String attribute)
    {
   	 XPath xpath = XPathFactory.newInstance().newXPath();
   	 String valueForXpath="";

     
   	 try 
   	 {
			NodeList nodeList = (NodeList) xpath.evaluate(xPath, document,XPathConstants.NODESET);
			Element element = (Element)nodeList.item(0);
			valueForXpath = element.getAttribute(attribute);
        	EllieMaeLog.log(_log, "XML element "+element.getNodeName()+" attribute "+attribute+ "value is : "+valueForXpath, EllieMaeLogLevel.reporter);
    	
	} 
   	 catch (XPathExpressionException e) 
   	 {
   		 	EllieMaeLog.log(_log, "Error getting value for XML element", EllieMaeLogLevel.reporter);
   		 	e.printStackTrace();
   	 }
   	 return valueForXpath;
    }
    
    
    private static Document convertStringToDocument(String xmlStr) 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
	/* Read the properties from excel sheet from shared path and
	 * add it into testData HashMap */
	private void readExcelProperties(HashMap<String,String> testData)
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
				CommonUtility.copyFilesOrFolder(testData.get("ExcelSheetPath")+"/"+testData.get("ExcelSheetFileName"), dataDirectoryPath+File.separator+testData.get("ExcelSheetFileName"), FileType.FILE);
			}
			else
			{
				CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testData.get("ExcelSheetPath")+"/"+testData.get("ExcelSheetFileName"),dataDirectoryPath,testData.get("ExcelSheetFileName"));
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception during copying excel sheet to data folder", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading properties from excel sheet", EllieMaeLogLevel.reporter);
		String query = "Select * from  \"Properties\"";
		
		try
		{
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(dataDirectoryPath+File.separator+testData.get("ExcelSheetFileName"),query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				while(recordSet.next())
				{
					String WAIT_BEFORE_LOAN_REVIEW = recordSet.getField("WAIT_BEFORE_LOAN_REVIEW");
					if(WAIT_BEFORE_LOAN_REVIEW!=null && !WAIT_BEFORE_LOAN_REVIEW.trim().isEmpty())
					{
						testData.put("WAIT_BEFORE_LOAN_REVIEW", WAIT_BEFORE_LOAN_REVIEW);
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

}
