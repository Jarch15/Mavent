package com.elliemae.apimethods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.Assert;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.APIUtility.HTTPHelper;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.EnvironmentData;
import com.elliemae.core.Utils.EnvironmentDataApplication;
import com.elliemae.core.Utils.JSONUtility;

public class RESTAPIMethodCall {

	public static Logger _log;

	HTTPHelper httpHelper=new HTTPHelper();
	private String URI = "";
	private String inputParam="";
	private String contentType ="";
	public HashMap<String, String> dataBeanMap;
	public HashMap<String, String> environmentData=new HashMap<String, String>();

	private HashMap<String, String> responseMap;
	private HashMap<String,String> serviceInfoData;
	private HashMap<String,String> headerMap;
	private HashMap<String, HashMap<String,String>> userList;
	private HashMap<String, String> userList2;
	private HashMap<String, HashMap<String, String>> siteList;

	public RESTAPIMethodCall(HashMap<String, String> dataBeanMap){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());
		this.dataBeanMap = dataBeanMap;
		environmentData=EnvironmentData.getEnvironmentData(FrameworkConsts.ENVIRONMENTNAME);
	}

	/*
	 * Adds elli-session to dataBeanMap
	 */
	public HashMap<String, String> loginREST(HashMap<String, String> testCaseData) {
		_log = Logger.getLogger(RESTAPIMethodCall.class + ": LoginREST");

		EllieMaeLog.log(_log, "REST API METHODNAME: loginREST", EllieMaeLogLevel.debug);

		serviceInfoData = EnvironmentDataApplication.getRESTServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		contentType = serviceInfoData.get("ContentType");

		if (testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtilityApplication.getStringFromFile(testCaseData.get("InputData"), "input");
		else
			inputParam = testCaseData.get("InputData");

		if (inputParam.contains("$Realm"))
			inputParam = inputParam.replace("$Realm", FrameworkConsts.ENVIRONMENTCLIENTID);

		userList = EnvironmentDataApplication.getUserListData();

		for (Map.Entry<String, HashMap<String, String>> entry : userList.entrySet()) {
			if (inputParam.contains(entry.getKey())) {
				inputParam = inputParam.replace("$" + entry.getKey(), entry.getValue().get("UserName"));
				inputParam = inputParam.replace("$Password", entry.getValue().get("Password"));
				break;
			}
		}

		headerMap.put("content-type", contentType);

		EllieMaeLog.log(_log, "LOGIN SERVICE Start", EllieMaeLogLevel.reporter);
		try {
			responseMap = httpHelper.executePostService(URI, inputParam, headerMap);
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in LOGIN Service: " + e.getMessage());
			_log.error("This totally broke:", e);
			Assert.assertTrue(false, "LOGIN SERVICE call failed with Exception: " + e.getMessage());
		}

		dataBeanMap.put("elli-session", extractGuidFromResponseLocation(responseMap));
		EllieMaeLog.log(_log, "elli-session: " + dataBeanMap.get("elli-session"));

		EllieMaeLog.log(_log, "LOGIN SERVICE End", EllieMaeLogLevel.reporter);

		return responseMap;
	}

	public HashMap<String, String> tpoLoginREST(HashMap<String, String> testCaseData) {
		_log = Logger.getLogger(RESTAPIMethodCall.class + ": tpoLoginREST");

		EllieMaeLog.log(_log, "REST API METHODNAME: tpoLoginREST", EllieMaeLogLevel.debug);

		serviceInfoData = EnvironmentDataApplication.getRESTServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		contentType = serviceInfoData.get("ContentType");

		if (testCaseData.containsKey("detail"))
			URI = URI.replace("/v2/auth/sessions", "/v2/auth/sessions?detail=" + testCaseData.get("detail"));

		if (testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtilityApplication.getStringFromFile(testCaseData.get("InputData"), "input");
		else
			inputParam = testCaseData.get("InputData");

		headerMap.put("content-type", contentType);

		if (inputParam.contains("$Realm"))
			inputParam = inputParam.replace("$Realm", FrameworkConsts.ENVIRONMENTCLIENTID);

		if (inputParam.contains("$EmailForLogin")) {
			inputParam = inputParam.replace("$Password", dataBeanMap.get("Password"));
			inputParam = inputParam.replace("$EmailForLogin", dataBeanMap.get("EmailForLogin"));
		}

		userList = EnvironmentDataApplication.getUserListData();

		for (Map.Entry<String, HashMap<String, String>> entry : userList.entrySet()) {
			if (inputParam.contains(entry.getKey()) && entry.getKey() != "") {
				inputParam = inputParam.replace("$" + entry.getKey(), entry.getValue().get("UserName"));
				inputParam = inputParam.replace("$Password", entry.getValue().get("Password"));
				break;
			}
		}

		if ("true".equals(testCaseData.get("custompassword"))) {
			inputParam = inputParam.replace("$ResetPassword", dataBeanMap.get("ResetPassword"));
			inputParam = inputParam.replace("$EmailForLogin", dataBeanMap.get("EmailForLogin"));
		}

		siteList = EnvironmentDataApplication.getSiteListData(FrameworkConsts.ENVIRONMENTNAME);

		// String siteid_authtoken = null;

		for (Map.Entry<String, HashMap<String, String>> entry : siteList.entrySet()) {
			// siteid_authtoken = entry.getValue().get("SiteID");
			if (inputParam.contains(entry.getKey()) && entry.getKey() != "") {
				inputParam = inputParam.replace("$" + entry.getKey(), entry.getValue().get("SiteURL"));
				break;
			}

		}

		EllieMaeLog.log(_log, "LOGIN SERVICE Start", EllieMaeLogLevel.reporter);
		try {
			responseMap = httpHelper.executePostService(URI, inputParam, headerMap);
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in LOGIN Service: " + e.getMessage());
			Assert.assertTrue(false, "LOGIN SERVICE call failed with Exception: " + e.getMessage());
		}
		try {
			dataBeanMap.put("elli-session", extractGuidFromResponseLocation(responseMap));
			EllieMaeLog.log(_log, "elli-session: " + dataBeanMap.get("elli-session"));

		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in elli-session :" + e.getMessage());

		}
		if (testCaseData.get("BaseLineStatusCode").equals("201")) {

			try {
				List<String> userName = JSONUtility.getJSONValueUsingJSONPath(responseMap.get("BODY"),
						"$.TPOLoginResponse.SecurityContext.UserName");
				String[] splitUserName = userName.get(0).split("\\\\");
				String userContactId = splitUserName[(splitUserName.length) - 1];
				dataBeanMap.put("contactId", userContactId);
			} catch (Exception e) {
				EllieMaeLog.log(_log, "Exception in getting Contact ID :" + e.getMessage());
			}

		}

		EllieMaeLog.log(_log, "LOGIN SERVICE End", EllieMaeLogLevel.reporter);

		return responseMap;
	}

	private String extractGuidFromResponseLocation(HashMap<String, String> responseMap) {
		String guID = null;
		try {
			String responseLocation = responseMap.get("LOCATION").toString();
			guID = responseLocation.substring(responseLocation.lastIndexOf('/') + 1, responseLocation.length());

		} catch (Exception e) {
			EllieMaeLog.log(_log, "No Location in response");
		}
		return guID;
	}
	
	/*
	 * Adds elli-session to dataBeanMap
	 */
	public HashMap<String,String> loginREST2(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log, "REST API METHODNAME: loginREST", EllieMaeLogLevel.debug);

		serviceInfoData=EnvironmentData.getRESTServiceInfoData(testCaseData.get("APIMethodName"));

		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		contentType = serviceInfoData.get("ContentType");

		if(testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"),"input");
		else
			inputParam = testCaseData.get("InputData"); 				

		if (inputParam.contains("$Realm"))
			inputParam = inputParam.replace("$Realm", FrameworkConsts.ENVIRONMENTCLIENTID);

		userList2=EnvironmentData.getUserListDataPerUserKey(testCaseData.get("UserKey"));
		
		if(inputParam.contains("$UserName")){
			inputParam= inputParam.replace("$UserName", userList2.get("UserName"));
		}

		if(inputParam.contains("$Password")){
			inputParam= inputParam.replace("$Password", userList2.get("Password"));
		}
		
		headerMap.put("content-type", contentType);

		EllieMaeLog.log(_log, "LOGIN SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostService(URI,inputParam,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in LOGIN Service: "+e.getMessage());
			Assert.assertTrue(false,"LOGIN SERVICE call failed with Exception: "+e.getMessage());
		}

		dataBeanMap.put("elli-session", responseMap.get("LOCATION").split(URI +"/")[1].toString());
		EllieMaeLog.log(_log, "elli-session: " +dataBeanMap.get("elli-session"));

		EllieMaeLog.log(_log, "LOGIN SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}

	public HashMap<String,String> getLoanREST(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log, "REST API METHODNAME: getLoanREST", EllieMaeLogLevel.debug);

		serviceInfoData=EnvironmentData.getRESTServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		serviceInfoData.putAll(CommonUtility.getKeyValue(testCaseData.get("InputData")));

		String loanNumber = (serviceInfoData.get("LoanNumber")==""?dataBeanMap.get("LoanNumber"):serviceInfoData.get("LoanNumber"));
		String format = (serviceInfoData.get("Format")==""?dataBeanMap.get("Format"):serviceInfoData.get("Format"));
		String entities = (serviceInfoData.get("Entities")==""?dataBeanMap.get("Entities"):serviceInfoData.get("Entities"));

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");

		URI= URI.replace("$LoanNumber", loanNumber);
		URI= URI.replace("$Format", format);
		URI= URI.replace("$Entities", entities);

		headerMap.put("elli-session", dataBeanMap.get("elli-session"));

		EllieMaeLog.log(_log, "GETLOAN SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executeGetService(URI,headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in GETLOAN Service: "+e.getMessage());
			Assert.assertTrue(false,"GETLOAN SERVICE call failed with Exception: "+e.getMessage());
		}

		EllieMaeLog.log(_log, "GETLOAN SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}	

	public HashMap<String,String> deletePipelineCursorREST(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		serviceInfoData=EnvironmentData.getRESTServiceInfoData("deletePipelineCursorREST"); 
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();        

		serviceInfoData.putAll(CommonUtility.getKeyValue(testCaseData.get("InputData")));

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		headerMap.put("elli-session", dataBeanMap.get("elli-session"));
		headerMap.put("content-type", serviceInfoData.get("ContentType"));

		String cursorId = (serviceInfoData.get("CursorId")==""?dataBeanMap.get("CursorId"):serviceInfoData.get("CursorId"));             
		URI = URI.replace("$CursorId", cursorId);

		EllieMaeLog.log(_log, "URI: " +URI,EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "elli-session: " +headerMap.get("elli-session"),EllieMaeLogLevel.reporter);


		EllieMaeLog.log(_log, "DELETE PIPELINE CURSOR SERVICE Start");
		try {  
			System.out.println("The input Param details========================= "+headerMap+"URI============"+URI);

			responseMap = httpHelper.executeDELETEService(URI,headerMap);

		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in DELETE PIPELINE CURSOR Service: "+e.getMessage());
			Assert.assertTrue(false,"DELETE PIPELINE CURSOR SERVICE call failed with Exception: "+e.getMessage());
		}
		//        
		//        EllieMaeLog.log(_log, "DELETE PIPELINE CURSOR SERVICE response:",EllieMaeLogLevel.reporter);
		//        for (Map.Entry<String, String> entry : responseMap.entrySet())
		//        {
		//              EllieMaeLog.log(_log,"Key: " + entry.getKey() +";    Value:"+ entry.getValue(),EllieMaeLogLevel.reporter);
		//        }

		EllieMaeLog.log(_log, "DELETE PIPELINE CURSOR SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}


	public HashMap<String,String> rateLockREST(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log, "REST API METHODNAME: rateLockREST", EllieMaeLogLevel.debug);

		serviceInfoData=EnvironmentData.getRESTServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		contentType = serviceInfoData.get("ContentType");					

		headerMap.put("content-type", contentType);
		headerMap.put("elli-session", dataBeanMap.get("elli-session"));

		EllieMaeLog.log(_log, "rateLock REST SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executeOptionsService(URI, headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in rateLock REST Service: "+e.getMessage());
			Assert.assertTrue(false,"rateLock REST SERVICE call failed with Exception: "+e.getMessage());
		}

		EllieMaeLog.log(_log, "rateLock REST SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}

	public HashMap<String,String> inputformsREST(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log, "REST API METHODNAME: inputformsREST", EllieMaeLogLevel.debug);

		serviceInfoData=EnvironmentData.getRESTServiceInfoData(testCaseData.get("APIMethodName"));
		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = environmentData.get("RESTURL") + "/" + serviceInfoData.get("APIURLParameter");
		contentType = serviceInfoData.get("ContentType");					

		headerMap.put("content-type", contentType);
		headerMap.put("elli-session", dataBeanMap.get("elli-session"));

		EllieMaeLog.log(_log, "inputforms SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executeHeadService(URI, headerMap);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in inputforms REST Service: "+e.getMessage());
			Assert.assertTrue(false,"inputforms REST SERVICE call failed with Exception: "+e.getMessage());
		}		

		EllieMaeLog.log(_log, "inputforms REST SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}

	public HashMap<String,String> uploadFileSample(HashMap<String,String> testCaseData){
		_log= Logger.getLogger(CommonUtility.getCurrentClassAndMethodNameForLogger());

		EllieMaeLog.log(_log, "API METHODNAME: uploadFile", EllieMaeLogLevel.debug);

		responseMap = new HashMap<>();
		headerMap = new HashMap<>();

		URI = "http://encompass-ea.qa.dco.elmae/v2/media/vault/files?filexpiration=1440&tokencreator=test&tokenexpires=1609372800&token=haR87hB6OTkF%2BobdMkDrJc13GyVI6bhekgCPQNbFPlKAsUDUwHq8k7ss9S93trbhIO%2BIEyuNDm6PPgWYY8lDXcQaVbh3H%2BujggsolU0h6b79TyfZ4XR7MWsGOdFPKuF9lJSr55NqX0itoPwbWtinkcEcc90eI8HEQK0kSSOgfeY4Tg8oZvvbIadrMO9lly0fYUUKgUDzI%2F61hwbdd1u5cs0svS11IT7C9a5q3bNQ39kU5Pc94E6jtZHPH451LD9e8zS%2BKNmsLcBS%2Bxe6TthyH488huEZVO7XJ6RaIiB0yHFkB%2FCjg1A6M7N6CBh56ILKg322F9ThP1Mh30HY548TPQ%3D%3D";
		
		if (testCaseData.get("InputData").contains("file;"))
			inputParam = CommonUtility.getStringFromFile(testCaseData.get("InputData"), "input");
		else
			inputParam = testCaseData.get("InputData");

		EllieMaeLog.log(_log, "SERVICE Start",EllieMaeLogLevel.reporter);
		try {			
			responseMap = httpHelper.executePostServiceFileUpload(URI,inputParam);				
		} catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in Service: "+e.getMessage());
			Assert.assertTrue(false,"SERVICE call failed with Exception: "+e.getMessage());
		}

		EllieMaeLog.log(_log, "SERVICE End",EllieMaeLogLevel.reporter);

		return responseMap;
	}
}

