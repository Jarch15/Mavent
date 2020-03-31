package com.elliemae.testcases.asyncservices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.asserts.Assert;

import Exception.FilloException;
import jcifs.smb.SmbFile;


public class AsynReviewServicesTest  extends EllieMaeApplicationBase 
{

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(AsynReviewServicesTest.class);
	APIValidationMethodsApplication apiValidationMethods;
	
	@Test(dataProvider = "get-test-data-method")
	public void verifyAsyncReview(HashMap<String,String> testData)
	{

		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		List<String> responseList = new ArrayList<String>();
		
		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"AsynReviewServicesTest_data.xlsx");

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
		
		for(int i=1;i<=testCaseData.size();i++)
		{
			HashMap<String, String> testCaseMethodData = testCaseData.get(""+i);
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
					responseMap = ObjSOAPMaventWebServiceCall.syncWebServiceCall(testData,testCaseMethodData,testCaseMethodData.get("APIMethodName"),fileToProcess);
		
				} catch (Exception e) 
				{
					e.printStackTrace();
					EllieMaeLog.log(_log, "asynCallbackReviewMobile SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
					Assert.assertTrue(false, "Test case failed due to exception");
				}
				
				
				if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
				{
					// extract response
					String response = getAsynReviewResponse(responseMap);
					responseList.add(response);
				}
				else
				{
					// Do SoftAssert FAIL for TestNG result
					//sAssert.fail("Found Internal Server Error, hence failed "+fileToProcess);			
				}
				
	        }
	        
	        // save responseList in test.xml
	        saveResponseInFile(testData, responseList,testCaseMethodData.get("OutputFileName"));
	        
	        // clear responseList
	        responseList.clear();
		}
		
        sAssert.assertAll();
	
	}
	
	
	@Test(dataProvider = "get-test-data-method")
	public void verifyGetReview(HashMap<String,String> testData)
	{
		/* Added wait for 30 seconds before executing get review */
		CommonUtilityApplication.threadWait(30000);
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		apiValidationMethods = new APIValidationMethodsApplication();
		HashMap<String, String> responseMap = new HashMap<>();
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		
		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"AsynReviewServicesTest_data.xlsx");

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
		
		for(int i=1;i<=testCaseData.size();i++)
		{
			HashMap<String, String> testCaseMethodData = testCaseData.get(""+i);
			
			/* Read transIDpoll.txt file line by line from input directory and add each line in List */
			File f = new File("");
			String inputDirectoryPath = f.getAbsolutePath();
			inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"asyncservices"+ File.separator +"input";;
			List<String> transactionIdList = new ArrayList<String>();
					
			try 
			{
				transactionIdList = FileUtils.readLines(new File(inputDirectoryPath+File.separator+testCaseMethodData.get("InputData")), "utf-8");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			/* for each transactionId call getReview web services, create output file, validate response */
			for(String transactionId : transactionIdList)
			{
				// Call Web service method to process each transactionId
				try 
				{
					responseMap = ObjSOAPMaventWebServiceCall.getReviewMobileService(testCaseMethodData,transactionId);
		
				} catch (Exception e) 
				{
					e.printStackTrace();
					EllieMaeLog.log(_log, "getReviewMobile SOAP SERVICE exception occured", EllieMaeLogLevel.reporter);
					Assert.assertTrue(false, "Test case failed due to exception");
				}
				
				
				if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
				{
					// save output xml file
					String responseXMLBody = ObjSOAPMaventWebServiceCall.getResponseXMLBody(responseMap, transactionId+".xml", testData,"getReviewMobile");
					List<String> xPathResultList = new ArrayList<String>();
					
					/* validate response */
					// Get customerTransID from responseXMLBody using xpath
					try 
					{
						xPathResultList = XMLUtility.getXPathValueUsingXPath(responseXMLBody,"//DataPackage[@customerTransID='"+transactionId+"']/@customerTransID");
					} 
					catch (XPathExpressionException | DOMException e) {
						e.printStackTrace();
					}
					
					// check if transactionId present
					if(xPathResultList == null || !xPathResultList.contains(transactionId))
					{
						sAssert.fail("Transaction id : "+transactionId+" not present in response xml");
					}
					else
					{
						EllieMaeLog.log(_log, "Transaction id : "+transactionId+" verified successfully in response xml", EllieMaeLogLevel.reporter);
					}
				}
				else
				{
					// Do SoftAssert FAIL for TestNG result
					sAssert.fail("Found Internal Server Error, hence failed for transactionId : "+transactionId);			
				}
			}
		}
		
        sAssert.assertAll();
	
	}
	
	// Read response for asynCallbackReviewMobile
	public String getAsynReviewResponse(HashMap<String,String> responseMap)
	{
		// Get the response xml from response Map
		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY"));	
		
		// Extract the response to get DataPackage node inside response xml received
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		HashMap<String, String> xPathKeyValueMap = new HashMap<>();

		xPathKeyMap.put("ResponseBody", "//reviewDocumentReturn");
		
		try 
		{
			xPathKeyValueMap = XMLUtilityApplication.readXMLtoExtractXPathValue(responseXML,xPathKeyMap);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		String responseXMLBody = xPathKeyValueMap.get("ResponseBody");
		
		return responseXMLBody;
	}
	
	// save response in output file. Copy output file to shared path.
	public void saveResponseInFile(HashMap<String,String> testData, List<String> responseList, String fileName)
	{
		/* create/write file in local input folder with response. */
		File f = new File("");
		String inputDirectoryPath = f.getAbsolutePath();
		inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"asyncservices"+ File.separator +"input";;

		String line="";
		for(String response : responseList)
		{
			line=line+response+"\n";
		}
		
		CommonUtility.createFileWriteLine(inputDirectoryPath+File.separator+fileName, line);
		
		/* copy file to shared path */
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFileDirector=testData.get("Output_File_Path")+"/"+timeStamp;
 		try 
		{
 			if(CommonUtilityApplication.isExecutedFromEMDomain())
 			{
 				// Copying File Locally
 				CommonUtility.copyFilesOrFolder(inputDirectoryPath+File.separator+fileName, outputFileDirector+File.separator+fileName, FileType.FILE);
 			}
 			else
 			{
 				// Copying File to network
 				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),inputDirectoryPath+File.separator +fileName,outputFileDirector,fileName);
 			}
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		
// write List<String> to file
//		try
//		{
//		    FileWriter writer = new FileWriter(inputDirectoryPath+File.separator+"test.xml");
//	        int size = responseList.size();
//	        for (int i=0;i<size;i++) 
//	        {
//	            String str = responseList.get(i).toString();
//	            writer.write(str);
//	            if(i < size-1)				//This prevent creating a blank like at the end of the file**
//	                writer.write("\n");
//	        }
//	        writer.close();
//		}
//		catch (IOException ex)
//		{
//			
//		}
		
		// write List data into test. xml file
		
		// copy test.xml file to Shared output directory
	}

}
