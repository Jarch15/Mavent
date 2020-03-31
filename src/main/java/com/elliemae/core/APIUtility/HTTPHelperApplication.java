package com.elliemae.core.APIUtility;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;

/**
 * <b>Name:</b> HTTPHelperApplication  
 * <b>Description: </b>This class is extending HTTPHelper class in EllieMaeATF and is used for making service calls 
 * 
 * @author <i>Supreet Singh</i>
 */

public class HTTPHelperApplication extends HTTPHelper {
	
	public static Logger _log=Logger.getLogger(HTTPHelperApplication.class);
	
	public HashMap<String, String> executePostServiceIgnoreSSL(String URI, String inputParams, HashMap<String, String> headerMap, boolean isDataLoggingEnabled) throws Exception
	{
		EllieMaeLog.log(_log, "******************************************************************************", EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
		EllieMaeLog.log(_log, "TESTCASE METHOD : "+FrameworkConsts.TESTCASENAME.get(), EllieMaeLogLevel.debug);
		EllieMaeLog.log(_log, "API METHOD NAME : "+FrameworkConsts.APIMETHODNAME.get(), EllieMaeLogLevel.debug);

		HashMap<String, String> postResponseMap = new HashMap<>();
		
		httpClient = HttpClients.custom()
									.setSSLHostnameVerifier(new DefaultHostnameVerifier())
									.setSSLContext(new SSLContextBuilder()
														.loadTrustMaterial(null, new TrustSelfSignedStrategy())
														.build())
									.build();
		
		EllieMaeLog.log(_log, "**************************POST SERVICE (Ignoring SSL) REQUEST*********************************", EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
		
		EllieMaeLog.log(_log, "URI: "+URI, EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);		
		EllieMaeLog.log(_log, "INPUT PARAM: \n"+inputParams, EllieMaeLogLevel.debug);
				
		try {
			HttpPost request = new HttpPost(URI);
			StringEntity params = new StringEntity(inputParams);
			request.setEntity(params);
			
			for(Map.Entry<String, String> entry : headerMap.entrySet()){
				request.addHeader(entry.getKey(),entry.getValue());
				EllieMaeLog.log(_log, entry.getKey().toUpperCase() + ": "+entry.getValue(), EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
			}
			
			EllieMaeLog.log(_log, "Start Executing POST service", EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
			startTime = System.currentTimeMillis();
			httpResponse= httpClient.execute(request);
			endTime = System.currentTimeMillis();
			EllieMaeLog.log(_log, "Executed POST service", EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
			
			postResponseMap = extractResponsetoMap(httpResponse, isDataLoggingEnabled);
		}
		catch (Exception ex) {
			EllieMaeLog.log(_log, "Exception in executePostService: "+ex.getMessage(),EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log, "Exception StackTrace: "+ex.getStackTrace());
			throw ex;
		}
		finally {
			if(null != httpClient) {
				httpClient.close();
			}
		}

		EllieMaeLog.log(_log, "******************************************************************************", EllieMaeLogLevel.reporter,EllieMaeLogLevel.debug);
		return postResponseMap;
	}
}
