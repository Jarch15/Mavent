package com.elliemae.testcases.Sample;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

public class OFACTest extends EllieMaeApplicationBase {
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	
	@Test(dataProvider = "get-test-data-method")
	
	public void loanReview(HashMap<String,String> testData){
		
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
	    HashMap<String, String> responseMap= new HashMap<>();
		
		try {
			
			//Get the file path using the utility command
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "OFACTest_Data.xlsx");
			
			//Create the query for the data from test data sheet 
			strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"' order by SequenceID";
			
			//Execute the query and save it in a hash map testCaseData			
            HashMap<String, HashMap<String, String>> testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
            
            //Pass the data from the query in the loan review function
            
        //    String dir = "\\" + "\\irvinefs\\irv_public\\IT\\Product Management\\Releases\\POST LAUNCH PRODUCTION\\ofactest\\" ;
            
            String dir = "\\" + "\\irvinefs\\irv_public\\IT\\QA\\Maintenance\\OFAC\\Test Files\\2017\\" + testData.get("Date");
            EllieMaeLog.log(_log, "Directory path: " +dir, EllieMaeLogLevel.reporter);
                        
            FilenameFilter mp3Filter = new FilenameFilter() {
                public boolean accept(File file, String name) {
                    if (name.contains("After") || name.contains("Control")) {
                        // filters files which contains After
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            
            List<String> results = new ArrayList<String>();         
            
            File[] files = new File(dir).listFiles(mp3Filter);
            
            for (File file : files) {
                if (file.isFile()) {
                    results.add(file.getName());
                }
            }
            
            int count = files.length;
            
          //  int count = new File(dir).listFiles().length;
            
            for (int i=1; i<=count; i++) {
            	String a = Integer.toString(1);
            	responseMap = ObjSOAPMaventWebServiceCall.loanReview(testCaseData.get(a), results.get(i-1));
            }         
            
		} 
		
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in loanReview method: "+e.getMessage(), EllieMaeLogLevel.reporter);
			Assert.assertTrue(false,"test failed due to exception");
		}

	}

}