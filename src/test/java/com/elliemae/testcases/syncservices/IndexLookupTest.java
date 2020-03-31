package com.elliemae.testcases.syncservices;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.testcases.generateandverify.GenerateInputFileAndVerifyUtility;

import Exception.FilloException;
import jcifs.smb.SmbFile;


public class IndexLookupTest  extends EllieMaeApplicationBase 
{

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(IndexLookupTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	
	/* Author : Jayesh Bhapkar
	 * Description : .
	 *  */
	@Test(dataProvider = "get-test-data-method")	
	public void indexLookup(HashMap<String,String> testData)
	{
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";

		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"IndexLookupTest_data.xlsx");

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
		
		// Get list of files to be processed from shared path
		List<String> filesToProcess = new ArrayList<String>();
		if(CommonUtilityApplication.isExecutedFromEMDomain())
		{
			File[] files = new File(testData.get("Input_File_Path")).listFiles();

			for (File file : files) 
			{
			    if (file.isFile()) 
			    {
			    	filesToProcess.add(file.getName());
			    }
			}
		}
		else
		{
			try 
			{
				SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(testData.get("Input_File_Path"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				for (SmbFile networkFile : listOfFilesFromNetwork)
				{
					filesToProcess.add(networkFile.getName());
				        
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
        for(String fileToProcess : filesToProcess)
        {
			// Call Web service method to process each file
			try 
			{
				responseMap = ObjSOAPMaventWebServiceCall.syncWebServiceCall(testData,testCaseMethodData,"indexLookup",fileToProcess);
	
			} catch (Exception e) 
			{
				e.printStackTrace();
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
				sAssert.assertTrue(false, "Test case failed due to exception");
			}
			
			// Do comparison with base file if Status code is 200
			if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
			{
				String responseBody = ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap, fileToProcess, testData,"indexLookup");
				String baseXMLFile ="";
				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					
					baseXMLFile = CommonUtility.readFile(testData.get("Base_File_Path")+File.separator+fileToProcess);	
				}
				else
				{
					// Read input xml file from network location
					try 
					{
						baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Base_File_Path"),fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}

				}

				// do xml comparison between actual response and base xml file
				boolean response = xmlComparison(baseXMLFile, responseBody,
						"<ServiceData", "</ServiceData>");

				EllieMaeLog.log(_log, "The comparison result is : " + response, EllieMaeLogLevel.reporter);
				EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);

				sAssert.assertEquals(response, true, "The file does not match for : " + fileToProcess);
			}
			else
			{
				// Do SoftAssert FAIL for TestNG result
				sAssert.fail("Found Internal Server Error, hence failed "+fileToProcess);
			}
        }
		
		
        // Assert all
		sAssert.assertAll();

		
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
	                    removeNode(xmlDocExpectedResult,"Return");
	                    removeNode(xmlDocActualResult,"Return");
	                    // remove attributes from xml which is to be ignored
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"ServiceData","transID");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"ServiceData","transID");
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"ServiceData","receiveDate");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"ServiceData","receiveDate");
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"ServiceData","receiveTime");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"ServiceData","receiveTime");
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"ServiceData","sendDate");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"ServiceData","sendDate");
	                    XMLUtilityApplication.removeAttribute(xmlDocExpectedResult,"ServiceData","sendTime");
	                    XMLUtilityApplication.removeAttribute(xmlDocActualResult,"ServiceData","sendTime");
	                    
	                    String sourceXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocExpectedResult);
	                    //EllieMaeLog.log(_log,"Source = " +sourceXMLString,EllieMaeLogLevel.reporter);
	                    String targetXMLString = XMLUtilityApplication.convertDocumentToString(xmlDocActualResult);
	                    //EllieMaeLog.log(_log,"Target = " +targetXMLString,EllieMaeLogLevel.reporter);
	                    
	                    return APIValidationMethodsApplication.compareXMLUsingXMLUnit(sourceXMLString, targetXMLString, attributeStart, attributeEnd);
	    }
	   
	   // Remove node from XML document having attributes index_rate and index_date
		private static void removeNode(Document doc, String elementName) 
		{
			NodeList nodeList = doc.getElementsByTagName(elementName);	
			List<Element> nodesToRemove = new ArrayList<Element>();
			
			for (int num=0; num<nodeList.getLength(); num++) 
	        {
				 	Element node = (Element) nodeList.item(num);

			        // get a map containing the attributes of this node
			        NamedNodeMap attributes = node.getAttributes();

			        // get the number of nodes in this map
			        int numAttrs = attributes.getLength();

			        for (int i = 0; i < numAttrs; i++) 
			        {
				       Attr attr = (Attr) attributes.item(i);
				       if(attr.getValue().equals("index_rate"))
				       {
				    	   nodesToRemove.add(node);
				       }
				       else if(attr.getValue().equals("index_date"))
				       {
				    	   nodesToRemove.add(node);
				       }
				    	 
			        }
	         }
			
			for(Element node : nodesToRemove)
			{
		           // retrieve the element 'Response'				   
		           Element element = (Element) doc.getElementsByTagName("Response").item(0);		  				    
		  
		           // remove the specific node				  
		           element.removeChild(node);
			}
			
		}
		
}
