package com.elliemae.apimethods;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.soap.SOAPException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.APIValidationMethodsApplication;
import com.elliemae.core.APIUtility.HTTPHelperApplication;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.Utils.XMLUtilityApplication;
import com.elliemae.core.asserts.SoftAssert;

public class SOAPMaventWebServiceCall {
	
	
public static Logger _log;
	
	HTTPHelperApplication httpHelper=new HTTPHelperApplication();
	private String URI = "";
	private String inputParam="";
	public HashMap<String, String> dataBeanMap = new HashMap<>();
	private HashMap<String, String> environmentData=new HashMap<String, String>();
	private HashMap<String, String> responseMap;
	private HashMap<String,String> serviceInfoData;
	private HashMap<String,String> headerMap;
	List<List<String>> actualXPathList = new ArrayList<List<String>>();
	
	
	
	public SOAPMaventWebServiceCall(HashMap<String, String> dataBeanMap){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		this.dataBeanMap = dataBeanMap;
		environmentData=EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
	}

	public HashMap<String,String> loanReview(HashMap<String,String> testCaseData, String results) throws SOAPException, IOException, Exception{
		
		//Define the logger
		
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		//Log the API Method name
		
		EllieMaeLog.log(_log, "SOAP API METHODNAME: loanReview", EllieMaeLogLevel.debug);
		
		
		
		// Create the hash map for response
		responseMap = new HashMap<>();
		
		//Create the hash map for header 
		headerMap = new HashMap<>();	
			
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseData.get("APIMethodName"));
		
		//Create URI from ConfigFile for environment
		
		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		
		//Get the data from XML input file
		
	//	if(testCaseData.get("InputData").contains("file;")){
			
			String dir = "\\" + "\\irvinefs\\irv_public\\IT\\QA\\Maintenance\\OFAC\\Test Files\\2017\\" + testCaseData.get("ValidationContent") + "\\" ;
					
	//		String filename = testCaseData.get("InputData").replace("file;", "");
			
			String filename = results;
			
				
			inputParam = CommonUtility.readFile(dir + filename);
			
			String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo.xml","input");
			
			String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo.xml","input");
			
			inputParam = inputParam1 + inputParam + inputParam2;
			
		
		
		inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));
		
		// Log the execution of web service
		
		EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: "+e.getMessage());
			Assert.assertTrue(false,"Loan Review SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
		
		//Convert the XML into the required format	
		
		String responseXML= XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY")); 
		responseXML = StringEscapeUtils.unescapeXml(responseXML);
		
		String splitstring = "\"xsd:string\">" ;
		String[] parts = responseXML.split(splitstring);
		String 	part2 = parts[1]; 
		splitstring = "</reviewDocumentReturn>" ;
		parts = part2.split(splitstring);
		part2 = parts[0];
		
	//	String outputfilename = testCaseData.get("InputData");
		
	//	outputfilename = outputfilename.substring(5);
		
		String outputfilename = results;
		
		String dirname;
		
		if (FrameworkConsts.ENVIRONMENTNAME.equals("QA1"))
			
			dirname = "\\" + "\\irvinefs\\irv_public\\IT\\Product Management\\Releases\\POST LAUNCH PRODUCTION\\OFAC\\2017\\" + testCaseData.get("ValidationMethod") + "\\Test Results\\QA\\" ;
		
		else if(FrameworkConsts.ENVIRONMENTNAME.equals("PROD"))
			
			dirname = "\\" + "\\irvinefs\\irv_public\\IT\\Product Management\\Releases\\POST LAUNCH PRODUCTION\\OFAC\\2017\\" + testCaseData.get("ValidationMethod") + "\\Test Results\\PROD\\" ;
		else
			
			dirname = "\\" + "\\irvinefs\\irv_public\\IT\\Product Management\\Releases\\POST LAUNCH PRODUCTION\\OFAC\\2017\\" + testCaseData.get("ValidationMethod") + "\\Test Results\\BCP\\" ;
		
		File file = new File(dirname);
		
		if (!file.exists()){
			
			if (!file.mkdirs()){
				System.out.println("The Folder path is created for" + FrameworkConsts.ENVIRONMENTNAME );
			}
		}
		
			
		//Create the output file
		FileOutputStream fout=new FileOutputStream(dirname.concat(outputfilename));
		
		//converting string into byte array    
		
		byte b[]= part2.getBytes();
	
		
        fout.write(b);
              
        String dirname2 = "\\" + "\\irvinefs\\irv_public\\IT\\Product Management\\Releases\\POST LAUNCH PRODUCTION\\OFAC\\2017\\" + testCaseData.get("ValidationMethod") + "\\Test Results\\QA\\" ;
        
        APIValidationMethodsApplication xmldiff = new APIValidationMethodsApplication();
        
        if (FrameworkConsts.ENVIRONMENTNAME.equals("PROD") || FrameworkConsts.ENVIRONMENTNAME.equals("DR")){
               
        String responseXMLFile = CommonUtility.readFile(dirname + outputfilename);
        String baseXMLFile = CommonUtility.readFile(dirname2 + outputfilename);
             
        
        boolean response;
			
        response = xmldiff.xmlAttributeComparison(responseXMLFile, baseXMLFile, "<OFACData return=", "</OFACData>");
        
        
      System.out.println("The comparison result is" + response );
			
      Assert.assertEquals(response, true, "The file does not match with QA:"+outputfilename );
      
        }
        
		return responseMap;
		
		
		
	}
	
	public HashMap<String, String> loanReview(HashMap<String, String> testData,
			HashMap<String, String> testCaseMethodData, String fileToProcess)
			throws SOAPException, IOException, Exception {

		// Define the logger

		_log = Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		// Log the API Method name

		EllieMaeLog.log(_log, "SOAP API METHODNAME: loanReview", EllieMaeLogLevel.debug);

		// Create the hash map for response
		responseMap = new HashMap<>();

		// Create the hash map for header
		headerMap = new HashMap<>();

		serviceInfoData = EnvironmentData.getSOAPServiceInfoData(testCaseMethodData.get("APIMethodName"));

		// Create URI from ConfigFile for environment

		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");

		 // Read input xml file from network location
		 
		 String inputFilePath = testData.get("Input_File_Path") + testData.get("Date")+"/";
		 inputParam =	 CommonUtility.readFileFromNetworkSharedLocation(inputFilePath,fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));		 
		 
		//inputParam = CommonUtility.readFile(dir + fileToProcess);

		String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo.xml", "input");

		String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo.xml", "input");

		inputParam = inputParam1 + inputParam + inputParam2;

		inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));

		// Log the execution of web service

		EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start", EllieMaeLogLevel.reporter);
		try {
			responseMap = httpHelper.executePostService(URI, inputParam, headerMap);
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: " + e.getMessage());
			Assert.assertTrue(false, "Loan Review SOAP SERVICE call failed with Exception: " + e.getMessage());
		}

		// Convert the XML into the required format

		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY"));
		responseXML = StringEscapeUtils.unescapeXml(responseXML);

		String splitstring = "\"xsd:string\">";
		String[] parts = responseXML.split(splitstring);
		String part2 = parts[1];
		splitstring = "</reviewDocumentReturn>";
		parts = part2.split(splitstring);
		part2 = parts[0];

		String outputfilename = fileToProcess;
		
		
		 //Create output file at network location	
		String dirname;

		if (FrameworkConsts.ENVIRONMENTNAME.contains("QA"))

			dirname = testData.get("Output_File_Path") + testData.get("Output_Directory")
					+"/"+ "Test Results"+"/"+"QA"+"/";

		else if (FrameworkConsts.ENVIRONMENTNAME.equals("PROD"))

			dirname = testData.get("Output_File_Path") + testData.get("Output_Directory")
			+"/"+ "Test Results"+"/"+"PROD"+"/";
		else

			dirname = testData.get("Output_File_Path") + testData.get("Output_Directory")
			+"/"+ "Test Results"+"/"+"BCP"+"/";
		
		 try 
		 {
			 CommonUtilityApplication.writeFileToNetworkSharedLocation(dirname, outputfilename,
					 FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), part2); 
		 } 
		 catch (Exception e) 
		 {
			 e.printStackTrace();
		 }

		 /*
		String dirname;

		if (FrameworkConsts.ENVIRONMENTNAME.equals("QA1"))

			dirname = "\\" + testData.get("Output_File_Path") + testData.get("Output_Directory")
					+ "\\Test Results\\QA\\";

		else if (FrameworkConsts.ENVIRONMENTNAME.equals("PROD"))

			dirname = "\\" + testData.get("Output_File_Path") + testData.get("Output_Directory")
					+ "\\Test Results\\PROD\\";
		else

			dirname = "\\" + testData.get("Output_File_Path") + testData.get("Output_Directory")
					+ "\\Test Results\\BCP\\";

		File file = new File(dirname);

		if (!file.exists()) {

			if (!file.mkdirs()) {
				System.out.println("The Folder path is created for" + FrameworkConsts.ENVIRONMENTNAME);
			}
		}

		// Create the output file
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(dirname.concat(outputfilename));
			// converting string into byte array
			byte b[] = part2.getBytes();
			fout.write(b);
		} finally {
			if (fout != null)
				fout.close();
		}

		*/
//		String dirname2 = "\\" + testData.get("Output_File_Path") + testData.get("Output_Directory")
//				+ "\\Test Results\\QA\\";
		 
		 String dirname2 = testData.get("Output_File_Path") + testData.get("Output_Directory")
			+"/"+ "Test Results"+"/"+"QA"+"/";

		// Compare result of PROD with that of QA
		if (FrameworkConsts.ENVIRONMENTNAME.equals("PROD") || FrameworkConsts.ENVIRONMENTNAME.equals("DR")) {

//			String responseXMLFile = CommonUtility.readFile(dirname + outputfilename);
//			String baseXMLFile = CommonUtility.readFile(dirname2 + outputfilename);
			
			String responseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(dirname+"/",outputfilename,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
			
			String baseXMLFile = CommonUtility.readFileFromNetworkSharedLocation(dirname2+"/",outputfilename,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));

			boolean response = APIValidationMethodsApplication.compareXMLUsingXMLUnit(responseXMLFile, baseXMLFile,
					"<OFACData return=", "</OFACData>");

			System.out.println("The comparison result is : " + response);

			Assert.assertEquals(response, true, "The file does not match with QA:" + outputfilename);

		}

		return responseMap;

	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This prepares the URI, input xml to be processed and calls the webservice. 
	 * It accepts three parameters, testCaseData hashmap, input xml file name and input xml directory.
	 * It returns the hashmap response back to the caller.
	 *  */
	public HashMap<String,String> webServiceHTTPTest(HashMap<String,String> testData, HashMap<String,String> testCaseMethodData) throws SOAPException, IOException, Exception
	{
				//Define the logger		
				_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
				
				//Log the API Method name				
				EllieMaeLog.log(_log, "SOAP API METHODNAME: loanReview", EllieMaeLogLevel.debug);					
				
				// Create the hash map for response
				responseMap = new HashMap<>();
				
				//Create the hash map for header 
				headerMap = new HashMap<>();	
					
				// Get SOAP service info data from the configuration file
				serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseMethodData.get("APIMethodName"));
				
				//Create URI from ConfigFile for environment				
				URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
				System.out.println("URI : "+URI);
				EllieMaeLog.log(_log, "URI Endpoint: "+URI, EllieMaeLogLevel.debug);

				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					inputParam = CommonUtility.readFile(testData.get("Input_File_Path")+File.separator+testCaseMethodData.get("InputData"));					
				}
				else
				{
					// Read input xml file from network location
					inputParam = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path"),testCaseMethodData.get("InputData"),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				}

				String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo.xml","input");
				
				String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo.xml","input");
				
				// Append header and footer info needed for SOAP web service call
				inputParam = inputParam1 + inputParam + inputParam2;
				
				//EllieMaeLog.log(_log, "Input XML to be processed : "+inputParam, EllieMaeLogLevel.debug);
				
				headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
				headerMap.put("content-type", serviceInfoData.get("ContentType"));
				
				// Log the execution of web service
				
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start",EllieMaeLogLevel.reporter);
				try {			
					responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
				} catch (Exception e) {
					EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: "+e.getMessage());
					Assert.assertTrue(false,"Loan Review SOAP SERVICE call failed with Exception: "+e.getMessage());
				}
				
				return responseMap;
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This prepares the URI, input xml to be processed and calls the webservice. 
	 * It accepts three parameters, testCaseData hashmap, input xml file name and input xml directory.
	 * It returns the hashmap response back to the caller.
	 *  */
	public HashMap<String,String> webServiceHTTPSmokeTest(String apiMethodName, String httpRequest, String URI) throws SOAPException, IOException, Exception
	{
		
		HTTPHelperApplication httpHelperApp =new HTTPHelperApplication();
		String currentThread = Thread.currentThread().getName()+":::::";

		//Define the logger		
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		//Log the API Method name				
		EllieMaeLog.log(_log, currentThread+"SOAP API METHODNAME: "+apiMethodName, EllieMaeLogLevel.debug);					
		
		// Create the hash map for response
		responseMap = new HashMap<>();
		
		//Create the hash map for header 
		headerMap = new HashMap<>();	
			
		// Get SOAP service info data from the configuration file
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(apiMethodName);
		
		//Create URI from ConfigFile for environment				
		URI = URI + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		EllieMaeLog.log(_log, currentThread+"URI Endpoint: "+URI, EllieMaeLogLevel.reporter);
		
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));
		
		// Log the execution of web service
		
		EllieMaeLog.log(_log, currentThread+"Calling HTTP Post SOAP SERVICE "+apiMethodName,EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelperApp.executePostService(URI,httpRequest,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, currentThread+"Exception in "+apiMethodName+ " SOAP Service: "+e.getMessage());
			Assert.assertTrue(false, apiMethodName+" SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
		
		return responseMap;
}
	
	public String getResponseXMLBody(HashMap<String,String> responseMap, HashMap<String, String> testCaseMethodData, HashMap<String, String> testData )
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
		// Create output file path for output directory at shared path. (Output file name is same as input file name)
		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String today= dateFormat.format(new Date());
//		String outputFilePath=testData.get("Output_File_Path")+"/"+today;
		
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFilePath=testData.get("Output_File_Path")+"/"+timeStamp;
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				File directory = new File(outputFilePath);
				   if (! directory.exists()){
				       directory.mkdir();
				   }
				CommonUtilityApplication.createOutputFile(outputFilePath+File.separator+testCaseMethodData.get("InputData"), responseXMLBody);				
			}
			else
			{
				CommonUtilityApplication.writeFileToNetworkSharedLocation(outputFilePath, testCaseMethodData.get("InputData"), FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// Remove xml tags that not needed
		if(responseXMLBody!=null)
		{
			responseXMLBody = responseXMLBody.replace("<?xml version=\"1.0\"?>","");
			responseXMLBody = responseXMLBody.replaceAll("<\\?xml-stylesheet(.*)PrinterFriendly.xsl\"\\?>", "");
			responseXMLBody = responseXMLBody.trim();
		}
		
		//responseXML = StringEscapeUtils.unescapeXml(responseXML);
		//EllieMaeLog.log(_log, "Response XML : " + responseXML, EllieMaeLogLevel.debug);
		
		// Convert response XML Body to UTF-8 encoding 
		try {
			responseXMLBody= new String(responseXMLBody.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return responseXMLBody;
	}
	
	/* Author : Jayesh Bhapkar
	 * Description : This prepares the URI, input xml to be processed and calls the webservice. 
	 * It accepts three parameters, testCaseData hashmap, input xml file name and input xml directory.
	 * It returns the hashmap response back to the caller.
	 *  */
	public HashMap<String,String> processInputXMLFile(HashMap<String,String> testData, HashMap<String,String> testCaseMethodData, String inputXMLFilePath, String inputXMLFileName) throws SOAPException, IOException, Exception
	{
				//Define the logger		
				_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
				
				//Log the API Method name				
				EllieMaeLog.log(_log, "SOAP API METHODNAME: loanReview", EllieMaeLogLevel.debug);					
				
				// Create the hash map for response
				responseMap = new HashMap<>();
				
				//Create the hash map for header 
				headerMap = new HashMap<>();	
					
				// Get SOAP service info data from the configuration file
				serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseMethodData.get("APIMethodName"));
				
				//Create URI from ConfigFile for environment				
				URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
				System.out.println("URI : "+URI);
				EllieMaeLog.log(_log, "URI Endpoint: "+URI, EllieMaeLogLevel.debug);

				if(CommonUtilityApplication.isExecutedFromEMDomain())
				{
					inputParam = CommonUtility.readFile(inputXMLFilePath+File.separator+inputXMLFileName);
				}
				else
				{
					// Read input xml file from network location
					inputParam = CommonUtility.readFileFromNetworkSharedLocation(inputXMLFilePath+"/",inputXMLFileName,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
				}

				String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo.xml","input");
				
				String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo.xml","input");
				
				// Append header and footer info needed for SOAP web service call
				inputParam = inputParam1 + inputParam + inputParam2;
				
				//EllieMaeLog.log(_log, "Input XML to be processed : "+inputParam, EllieMaeLogLevel.debug);
				
				headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
				headerMap.put("content-type", serviceInfoData.get("ContentType"));
				
				// Log the execution of web service
				
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start",EllieMaeLogLevel.reporter);
				try {			
					responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
				} catch (Exception e) {
					EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: "+e.getMessage());
					Assert.assertTrue(false,"Loan Review SOAP SERVICE call failed with Exception: "+e.getMessage());
				}
				
				return responseMap;
	}	
	
	// Save output xml and return response xml body content
	public String saveOuputXMLAndReturnResponseBody(HashMap<String,String> responseMap,HashMap<String, String> testData, String result_output_Folder, String inputXMLFileName )
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
		// Create output file path for output directory at shared path. (Output file name is same as input file name)
		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String today= dateFormat.format(new Date());
//		String outputFilePath=testData.get("Output_File_Path")+"/"+today;
		
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFilePath=result_output_Folder+"/"+timeStamp;
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				File directory = new File(outputFilePath);
				   if (! directory.exists()){
				       directory.mkdir();
				   }
				CommonUtilityApplication.createOutputFile(outputFilePath+File.separator+inputXMLFileName, responseXMLBody);				
			}
			else
			{
				CommonUtilityApplication.writeFileToNetworkSharedLocation(outputFilePath, inputXMLFileName, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// Remove xml tags that not needed
		if(responseXMLBody!=null)
		{
			responseXMLBody = responseXMLBody.replace("<?xml version=\"1.0\"?>","");
			responseXMLBody = responseXMLBody.replaceAll("<\\?xml-stylesheet(.*)PrinterFriendly.xsl\"\\?>", "");
			responseXMLBody = responseXMLBody.trim();
		}
		
		//responseXML = StringEscapeUtils.unescapeXml(responseXML);
		//EllieMaeLog.log(_log, "Response XML : " + responseXML, EllieMaeLogLevel.debug);
		
		// Convert response XML Body to UTF-8 encoding 
		try {
			responseXMLBody= new String(responseXMLBody.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return responseXMLBody;
	}
	
	// Web service call for regression testing for sync services
	public HashMap<String,String> syncWebServiceCall(HashMap<String,String> testData, HashMap<String,String> testCaseMethodData, String serviceName, String fileToProcess ) throws SOAPException, IOException, Exception
	{
		//Define the logger		
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		//Log the API Method name				
		EllieMaeLog.log(_log, "SOAP API METHODNAME: loanReview", EllieMaeLogLevel.debug);					
		
		// Create the hash map for response
		responseMap = new HashMap<>();
		
		//Create the hash map for header 
		headerMap = new HashMap<>();	
			
		// Get SOAP service info data from the configuration file
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseMethodData.get("APIMethodName"));
		
		//Create URI from ConfigFile for environment				
		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		System.out.println("URI : "+URI);
		EllieMaeLog.log(_log, "URI Endpoint: "+URI, EllieMaeLogLevel.debug);

		if(CommonUtilityApplication.isExecutedFromEMDomain())
		{
			inputParam = CommonUtility.readFile(testData.get("Input_File_Path")+File.separator+fileToProcess);					
		}
		else
		{
			// Read input xml file from network location
			inputParam = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path"),fileToProcess,FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
		}

		inputParam = inputParam.replace("<?xml version='1.0' ?>", "");
		inputParam = inputParam.replace("<?xml version=\"1.0\"?>", "");
		inputParam = inputParam.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		
		String inputParam1 = CommonUtility.getStringFromFile("SoapHeaderInfo_"+serviceName+".xml","input");
		
		String inputParam2 = CommonUtility.getStringFromFile("SoapTrailInfo_"+serviceName+".xml","input");
		
		// Append header and footer info needed for SOAP web service call
		inputParam = inputParam1 + inputParam + inputParam2;
		
		//EllieMaeLog.log(_log, "Input XML to be processed : "+inputParam, EllieMaeLogLevel.debug);
		
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));
		
		// Log the execution of web service
		
		EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: "+e.getMessage());
			Assert.assertTrue(false,"Loan Review SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
		
		return responseMap;
	}	
	
	/* Author : Jayesh Bhapkar
	 * Description : This prepares the URI, input xml to be processed and calls the getReviewMobileService. 
	 * It accepts three parameters, testCaseData hashmap and transactionId to process.
	 * It returns the result hashmap response back to the caller.
	 *  */
	public HashMap<String,String> getReviewMobileService(HashMap<String,String> testCaseMethodData, String transactionId) throws SOAPException, IOException, Exception
	{
				//Define the logger		
				_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
				
				//Log the API Method name				
				EllieMaeLog.log(_log, "SOAP API METHODNAME: getReviewMobile", EllieMaeLogLevel.debug);					
				
				// Create the hash map for response
				responseMap = new HashMap<>();
				
				//Create the hash map for header 
				headerMap = new HashMap<>();	
					
				// Get SOAP service info data from the configuration file
				serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseMethodData.get("APIMethodName"));
				
				//Create URI from ConfigFile for environment				
				URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
				System.out.println("URI : "+URI);
				EllieMaeLog.log(_log, "URI Endpoint: "+URI, EllieMaeLogLevel.debug);
				
				String inputParam = CommonUtility.getStringFromFile("SoapInfo_getReviewMobile.xml","input");

				// Append header and footer info needed for SOAP web service call
				inputParam = inputParam.replace("$TRANSACTION_ID$", transactionId);
				
				//EllieMaeLog.log(_log, "Input XML to be processed : "+inputParam, EllieMaeLogLevel.debug);
				
				headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
				headerMap.put("content-type", serviceInfoData.get("ContentType"));
				
				// Log the execution of web service
				
				EllieMaeLog.log(_log, "LOAN REVIEW SOAP SERVICE Start",EllieMaeLogLevel.reporter);
				try {			
					responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
				} catch (Exception e) {
					EllieMaeLog.log(_log, "Exception in Loan Review SOAP Service: "+e.getMessage());
					Assert.assertTrue(false,"Loan Review SOAP SERVICE call failed with Exception: "+e.getMessage());
				}
				
				return responseMap;
	}
	
	// Save output xml and return response xml body content
	public String getResponseXMLBody(HashMap<String,String> responseMap, String fileToProcess, HashMap<String, String> testData, String serviceName )
	{
		// Get the response xml from response Map
		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY"));	
		
		// Extract the response to get DataPackage node inside response xml received
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		HashMap<String, String> xPathKeyValueMap = new HashMap<>();
		if(serviceName.equals("batchReview"))
		{
			xPathKeyMap.put("ResponseBody", "//batchReviewReturn");
		}
		else if(serviceName.equals("nmlsSearch"))
		{
			xPathKeyMap.put("ResponseBody", "//searchReturn");
		}
		else if(serviceName.equals("nmlsReview"))
		{
			xPathKeyMap.put("ResponseBody", "//reviewReturn");
		}
		else if(serviceName.equals("indexLookup"))
		{
			xPathKeyMap.put("ResponseBody", "//lookupReturn");
		}
		else if(serviceName.equals("residualIncomeLookUp"))
		{
			xPathKeyMap.put("ResponseBody", "//getResidualIncomeThresholdReturn");
		}
		else if(serviceName.equals("adminSetup"))
		{
			xPathKeyMap.put("ResponseBody", "//setupReturn");
		}
		else if(serviceName.equals("getReviewMobile"))
		{
			xPathKeyMap.put("ResponseBody", "//getReviewResultReturn");
		}
		try 
		{
			xPathKeyValueMap = XMLUtilityApplication.readXMLtoExtractXPathValue(responseXML,xPathKeyMap);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		String responseXMLBody = xPathKeyValueMap.get("ResponseBody");
		
		/* Create output xml file using the response xml received at network location*/
		// Create output file path for output directory at shared path. (Output file name is same as input file name)		
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFilePath=testData.get("Output_File_Path")+"/"+timeStamp;
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				File directory = new File(outputFilePath);
				   if (! directory.exists()){
				       directory.mkdir();
				   }
				CommonUtilityApplication.createOutputFile(outputFilePath+File.separator+fileToProcess, responseXMLBody);				
			}
			else
			{
				CommonUtilityApplication.writeFileToNetworkSharedLocation(outputFilePath, fileToProcess, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD), responseXMLBody);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// Remove xml tags that not needed
		if(responseXMLBody!=null)
		{
			responseXMLBody = responseXMLBody.replace("<?xml version=\"1.0\"?>","");
			responseXMLBody = responseXMLBody.trim();
		}
		
		// Convert response XML Body to UTF-8 encoding 
		try {
			responseXMLBody= new String(responseXMLBody.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return responseXMLBody;
	}	
	

	/**
	 * <b>Name:</b>httpPost<br>
	 * <b>Description:</b> This method prepares the URI and calls the HTTP post using HTTP helper. 
	 * It accepts two parameters, apiMethodName and httpRequest to process.
	 * It returns the hashmap response back to the caller.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param apiMethodName
	 * @param httpRequest
	 * @return responseMap
	 */
	public HashMap<String,String> httpPost(String apiMethodName, String httpRequest) throws SOAPException, IOException, Exception
	{
		
				HTTPHelperApplication httpHelperApp =new HTTPHelperApplication();
				String currentThread = Thread.currentThread().getName()+":::::";
		
				//Define the logger		
				_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
				
				//Log the API Method name				
				EllieMaeLog.log(_log, currentThread+"SOAP API METHODNAME: "+apiMethodName, EllieMaeLogLevel.debug);					
				
				// Create the hash map for response
				responseMap = new HashMap<>();
				
				//Create the hash map for header 
				headerMap = new HashMap<>();	
					
				// Get SOAP service info data from the configuration file
				serviceInfoData=EnvironmentData.getSOAPServiceInfoData(apiMethodName);
				
				//Create URI from ConfigFile for environment				
				URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
				EllieMaeLog.log(_log, currentThread+"URI Endpoint: "+URI, EllieMaeLogLevel.reporter);
				
				headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
				headerMap.put("content-type", serviceInfoData.get("ContentType"));
				
				// Log the execution of web service
				
				EllieMaeLog.log(_log, currentThread+"Calling HTTP Post SOAP SERVICE "+apiMethodName,EllieMaeLogLevel.reporter);
				try {			
					responseMap = httpHelperApp.executePostService(URI,httpRequest,headerMap);				
				} catch (Exception e) {
					EllieMaeLog.log(_log, currentThread+"Exception in "+apiMethodName+ " SOAP Service: "+e.getMessage(),EllieMaeLogLevel.reporter);
					throw e;
				}
				
				return responseMap;
	}	
	
	/**
	 * <b>Name:</b>processSaveAndValidateXML<br>
	 * <b>Description:</b> This method prepares the request and calls the httpPost to process the input files. 
	 * It's then validates the response with xpath validation.
	 * This method uses multithreading to process the multiple files in paralle.
	 * It returns the hashmap response of actual result back to the caller.<br>
	 * @author <i>Jayesh Bhapkar</i>
	 * @param testData
	 * @param testCaseData
	 * @param sAssert
	 * @param softAssert
	 * @return actualXpathMap
	 */
	public Map<String,List<List<String>>> processSaveAndValidateXML(HashMap<String, String> testData,HashMap<String, HashMap<String, String>> testCaseData, SoftAssert sAssert, SoftAssert softAssert)
	{
		APIValidationMethodsApplication apiValidationMethods = new APIValidationMethodsApplication();
		Map<String,List<List<String>>> actualXpathMap = new HashMap<String,List<List<String>>>();
		
		// Read SOAP Header and Trail from input folder		
		String soapHeaderInfo = CommonUtility.getStringFromFile("SoapHeaderInfo.xml","input");		
		String soapTrailInfo = CommonUtility.getStringFromFile("SoapTrailInfo.xml","input");
		int THREAD_COUNT=1;
		environmentData = EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
		
		if(environmentData.get("THREAD_COUNT") != null && !environmentData.get("THREAD_COUNT").isEmpty())
		{
			THREAD_COUNT = Integer.parseInt(environmentData.get("THREAD_COUNT"));
		}
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final List<Future<?>> futures = new ArrayList<>();		
		for(String key : testCaseData.keySet())
			{
				Future<?> future = executor.submit(() -> {
				String currentThread = Thread.currentThread().getName()+":::::";
				HashMap<String, String> testCaseMethodData = testCaseData.get(key);
				String apiMethodName = testCaseMethodData.get("APIMethodName");
				
				
				if(testCaseMethodData.get("Execute")!=null && testCaseMethodData.get("Execute").equalsIgnoreCase("Yes"))
				{
					HashMap<String, String> responseMap = new HashMap<>();
					// Prepare SOAP Request
					String request ="";
					if(CommonUtilityApplication.isExecutedFromEMDomain())
					{
						request = CommonUtility.readFile(testData.get("Input_File_Path")+File.separator+testCaseMethodData.get("InputData"));					
					}
					else
					{
						// Read input xml file from network location
						try 
						{
							request = CommonUtility.readFileFromNetworkSharedLocation(testData.get("Input_File_Path"),testCaseMethodData.get("InputData"),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
						} 
						catch (Exception e) 
						{
							EllieMaeLog.log(_log, currentThread+"Exception occurred during reading file from Network : "+testCaseMethodData.get("InputData"), EllieMaeLogLevel.reporter);
							e.printStackTrace();
						}
					}

					// Append header and footer info needed for SOAP web service call
					request = soapHeaderInfo + request + soapTrailInfo;
			
					// Call Web service method with testData and testCase data
					try 
					{
						responseMap = httpPost(apiMethodName,request);
			
					} catch (Exception e) 
					{
						EllieMaeLog.log(_log, currentThread + "Loan Review SOAP SERVICE exception occured : "+e.getMessage(),
								EllieMaeLogLevel.reporter);
						e.printStackTrace();
						sAssert.fail(currentThread + "Test case failed due to exception : "+e.getMessage());
					}
			
			
					//Assert.assertEquals(responseMap.get("STATUSCODE"), testCaseMethodData.get("BaseLineStatusCode"));
					
					// Do XPath validation if Status code is 200
					if(responseMap.get("STATUSCODE").equals(testCaseMethodData.get("BaseLineStatusCode")))
					{
				        if (!(testCaseMethodData.get("ValidationMethod").isEmpty()))
				        {
				        	try 
				        	{
				        		actualXPathList = apiValidationMethods.xPathValidationExtension(getResponseXMLBody(responseMap, testCaseMethodData, testData), 
				        				testCaseMethodData.get("ValidationContent"),sAssert);
								actualXpathMap.put(sAssert.getCurrJIRA_ID(), actualXPathList);
						
							} 
				        	catch (IOException e) 
				        	{
								
								e.printStackTrace();
							}
				        }
					}
					else
					{
						// Put JIRAID and FAIL status for reporting
						sAssert.getTestStatus().put(testCaseMethodData.get("JIRAID"), "Response code is : "+ responseMap.get("STATUSCODE")+" : FAIL");						
						// Do SoftAssert FAIL for TestNG result
						softAssert.fail("Found Internal Server Error, hence failed "+testCaseMethodData.get("JIRAID"));
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
		
		return actualXpathMap;
		
	}

}


