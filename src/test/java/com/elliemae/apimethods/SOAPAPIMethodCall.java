package com.elliemae.apimethods;

import java.io.IOException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.HTTPHelper;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.XMLUtility;

public class SOAPAPIMethodCall {

	public static Logger _log;
	
	HTTPHelper httpHelper=new HTTPHelper();
	private String URI = "";
	private String inputParam="";
	public HashMap<String, String> dataBeanMap = new HashMap<>();
	private HashMap<String, String> environmentData=new HashMap<String, String>();
	private HashMap<String, String> responseMap;
	private HashMap<String,String> serviceInfoData;
	private HashMap<String,String> headerMap;
	
	public SOAPAPIMethodCall(HashMap<String, String> dataBeanMap){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		this.dataBeanMap = dataBeanMap;
		environmentData=EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
	}

	/*
	 * Adds Created and UserName to dataBeanMap
	 */
	public HashMap<String,String> loginSOAP(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		EllieMaeLog.log(_log, "SOAP API METHODNAME: loginSOAP", EllieMaeLogLevel.debug);
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();		
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseData.get("APIMethodName"));
		
		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		
		if(testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"),"input");
		else
			inputParam = testCaseData.get("InputData"); 	
		
		inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));
		
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));
		
		EllieMaeLog.log(_log, "LOGIN SOAP SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in LOGIN SOAP Service: "+e.getMessage());
			Assert.assertTrue(false,"LOGIN SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
		
		System.out.println(responseMap.get("BODY"));
		
		String responseXML= XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY")); 
		
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		
		xPathKeyMap.put("Created", "/s:Envelope/s:Body/AuthenticateClientResponse/SecurityContext/Created");
		xPathKeyMap.put("UserName", "/s:Envelope/s:Body/AuthenticateClientResponse/SecurityContext/UserName");
		
		try {
			dataBeanMap.putAll(XMLUtility.readXMLtoExtractXPathValue(responseXML, xPathKeyMap));
		} catch (IOException e) {
			EllieMaeLog.log(_log, "Exception in readXMLtoExtractXPathValue: "+e.getMessage());
		}
		
		EllieMaeLog.log(_log, "Created: " +dataBeanMap.get("Created"));
		EllieMaeLog.log(_log, "UserName: " +dataBeanMap.get("UserName"));
		
		EllieMaeLog.log(_log, "LOGIN SOAP SERVICE End",EllieMaeLogLevel.reporter);
		
		return responseMap;
	}
		
	/*
	 * Adds SessionId to dataBeanMap
	 */
	public HashMap<String,String> createSessionSOAP(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		EllieMaeLog.log(_log, "SOAP API METHODNAME: createSessionSOAP", EllieMaeLogLevel.debug);
		
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();
		
		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		if(testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"),"input");
		else
			inputParam = testCaseData.get("InputData"); 
		
		inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));
		
		inputParam = inputParam.replace("$Created", dataBeanMap.get("Created"));
		inputParam = inputParam.replace("$UserName", dataBeanMap.get("UserName"));
		
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));
		
		EllieMaeLog.log(_log, "CREATE SESSION SOAP SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in CREATE SESSION SOAP Service: "+e.getMessage());
			Assert.assertTrue(false,"CREATE SESSION SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
	
		String responseXML = XMLUtility.removeXmlNamespaceWithoutPrefix(responseMap.get("BODY")); 
		
		HashMap<String, String> xPathKeyMap = new HashMap<>();
		xPathKeyMap.put("SessionId", "/s:Envelope/s:Body/SessionCreateResponse/SecurityContext/SessionId");
		
		try {
			dataBeanMap.putAll(XMLUtility.readXMLtoExtractXPathValue(responseXML, xPathKeyMap));
		} catch (IOException e) {
			EllieMaeLog.log(_log, "Exception in readXMLtoExtractXPathValue: "+e.getMessage());
		}
		
		EllieMaeLog.log(_log, "SessionId: " +dataBeanMap.get("SessionId"));
		
		EllieMaeLog.log(_log, "CREATE SESSION SOAP SERVICE End",EllieMaeLogLevel.reporter);
		
		return responseMap;
	}
	
	public HashMap<String,String> getLoanSOAP(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		EllieMaeLog.log(_log, "SOAP API METHODNAME: getLoanSOAP", EllieMaeLogLevel.debug);
		
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();
		
		URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");
		if(testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"),"input");
		else
			inputParam = testCaseData.get("InputData"); 
		
		inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));		
		inputParam = inputParam.replace("$Created", dataBeanMap.get("Created"));
		inputParam = inputParam.replace("$SessionId", dataBeanMap.get("SessionId"));
		inputParam = inputParam.replace("$UserName", dataBeanMap.get("UserName"));
		
		headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));

		EllieMaeLog.log(_log, "GETLOAN SOAP SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in GETLOAN SOAP Service: "+e.getMessage());
			Assert.assertTrue(false,"GETLOAN SOAP SERVICE call failed with Exception: "+e.getMessage());
		}
		
		EllieMaeLog.log(_log, "GETLOAN SOAP SERVICE End",EllieMaeLogLevel.reporter);
		
		return responseMap;
	}
	
	public HashMap<String,String> loginSOAPWithSession(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		HashMap<String, String> responseMap = new HashMap<>();
		EllieMaeLog.log(_log, "SOAP API METHODNAME: loginSOAPWithSession", EllieMaeLogLevel.debug);
		
		responseMap = loginSOAP(testCaseData);
		
		HashMap<String,String> serviceInfoData = new HashMap<>();
		serviceInfoData=EnvironmentData.getSOAPServiceInfoData("createSessionSoap");
		createSessionSOAP(serviceInfoData);
		
		return responseMap;
	}
	
	public HashMap<String,String> createLoanSOAP(HashMap<String,String> testCaseData, String isSession){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
        HashMap<String, String> responseMap = new HashMap<>();
        HashMap<String,String> serviceInfoData = new HashMap<>();
        HashMap<String,String> headerMap = new HashMap<>();
        
        EllieMaeLog.log(_log, "SOAP API METHODNAME: createLoanSOAP", EllieMaeLogLevel.debug);
        
        serviceInfoData=EnvironmentData.getSOAPServiceInfoData(testCaseData.get("APIMethodName"));
        
        URI = environmentData.get("SOAPURL") + "/" + serviceInfoData.get("SOAPAPIURLParameter");        
        if(testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"),"input");
		else
			inputParam = testCaseData.get("InputData"); 
        
        inputParam = inputParam.replace("$Header", serviceInfoData.get("SOAPHeader"));
        inputParam = inputParam.replace("$Created", dataBeanMap.get("Created"));
        if (isSession == "true")
        {
               inputParam = inputParam.replace("$SessionId", dataBeanMap.get("SessionId"));
        }
        inputParam = inputParam.replace("$UserName", dataBeanMap.get("UserName"));
        
        System.out.println(inputParam);
        headerMap.put("SOAPAction", serviceInfoData.get("SOAPAction"));
        headerMap.put("content-type", serviceInfoData.get("ContentType"));
        
        EllieMaeLog.log(_log, "CREATELOAN SOAP SERVICE Start",EllieMaeLogLevel.reporter);
        try {                
               responseMap = httpHelper.executePostService(URI,inputParam,headerMap);                          
        } catch (Exception e) {
               EllieMaeLog.log(_log, "Exception in CREATELOAN SOAP Service: "+e.getMessage());
               Assert.assertTrue(false,"CREATELOAN SOAP SERVICE call failed with Exception: "+e.getMessage());
        }
        
        EllieMaeLog.log(_log, "CREATELOAN SOAP SERVICE End",EllieMaeLogLevel.reporter);
        
        return responseMap;
 }

	public HashMap<String,String> getWSDL(String soapServiceName,String SOAPAPIURLParameter){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();
		
		URI = environmentData.get("SOAPURL") + "/" + SOAPAPIURLParameter + "?singleWsdl";	
		
		EllieMaeLog.log(_log, "URI: " +URI);
	
		EllieMaeLog.log(_log, soapServiceName+" WSDL SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executeGetService(URI,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in "+soapServiceName+" Service: "+e.getMessage());
			Assert.assertTrue(false,soapServiceName+" WSDL SERVICE call failed with Exception: "+e.getMessage());
		}
	
		EllieMaeLog.log(_log, soapServiceName+" WSDL SERVICE End",EllieMaeLogLevel.reporter);
		
		return responseMap;
	}	
}

