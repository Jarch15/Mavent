package com.elliemae.testcases.maintenance;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.asserts.Assert;

import jcifs.smb.SmbFile;

public class OFACPostLaunchTest extends EllieMaeApplicationBase
{
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(OFACPostLaunchTest.class);
	
	@Test(dataProvider = "get-test-data-method")
	
	public void loanReview(HashMap<String,String> testData){
		
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		String additionalDataFilePath= "";
	    String strTestDataQuery="";
	    HashMap<String, String> responseMap= new HashMap<>();
		
		try {
			
			//Get the file path using the utility command
			additionalDataFilePath= CommonUtilityApplication.getRelativeFilePath("data", "OFACPostLaunchTest_Data.xlsx");
			
			//Create the query for the data from test data sheet 
			strTestDataQuery="Select * from "+testData.get("TestDataSheet")+" where Test_Case_Name = '"+testData.get("Test_Case_Name")+"' order by SequenceID";
			
			//Execute the query and save it in a hash map testCaseData			
            HashMap<String, HashMap<String, String>> testCaseData = CommonUtilityApplication.getAdditionalDataInMap(additionalDataFilePath, strTestDataQuery);
            
            HashMap<String, String> testCaseMethodData = testCaseData.get("1");
            
            String inputData = testCaseMethodData.get("InputData");
            String[] fileNamePaterns = inputData.split(",");
               
            String dir = testData.get("Input_File_Path") + testData.get("Date")+"/";
            EllieMaeLog.log(_log, "Directory path: " +dir, EllieMaeLogLevel.reporter);
            
            //Code for processing files from network
            SmbFile[] listOfFilesFromNetwork = CommonUtilityApplication.listOfFileFromNetworkSharedLocation(dir, FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));
            List<String> filesToProcessFromNetwork = new ArrayList<String>();
            for (SmbFile networkFile : listOfFilesFromNetwork)
			{
            	for(String fileNamePattern : fileNamePaterns)
            	{
					if(networkFile.getName().contains(fileNamePattern))
					{
						filesToProcessFromNetwork.add(networkFile.getName());
					}
            	}
			        
			}
            for(String fileToProcess : filesToProcessFromNetwork)
            {
            	responseMap = ObjSOAPMaventWebServiceCall.loanReview(testData, testCaseMethodData, fileToProcess);
            }
            
            
            
            // file filter to process only those files which contain the input Data string
            /*FilenameFilter mp3Filter = new FilenameFilter() 
            {
                public boolean accept(File file, String name) 
                {
                	for(String fileNamePattern : fileNamePaterns)
                	{
                		 if (name.contains(fileNamePattern)) 
                		 {
                             return true;
                         } 
                	}
                	
                	return false;

                }
            };
            
            List<String> filesToProcess = new ArrayList<String>();         
            
            File[] files = new File(dir).listFiles(mp3Filter);
            
            for (File file : files) 
            {
                if (file.isFile()) 
                {
                	filesToProcess.add(file.getName());
                }
            }
            
            for(String fileToProcess : filesToProcess)
            {
            	responseMap = ObjSOAPMaventWebServiceCall.loanReview(testData, testCaseMethodData, fileToProcess);
            } */
            
		} 
		
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in loanReview method: "+e.getMessage(), EllieMaeLogLevel.reporter);
			Assert.assertTrue(false,"test failed due to exception");
		}

	}

}
