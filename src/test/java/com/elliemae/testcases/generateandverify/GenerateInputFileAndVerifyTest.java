package com.elliemae.testcases.generateandverify;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.elliemae.apimethods.SOAPMaventWebServiceCall;
import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;

import Exception.FilloException;

/*
 * Author : Jayesh Bhapkar
 * Description : Test Class for generating input xml files
 * and verify actual response with expected result.
 */
public class GenerateInputFileAndVerifyTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(GenerateInputFileAndVerifyTest.class);

 	/**
 	 * <b>Author: Jayesh Bhapkar
 	 * <b>Name:</b>generateInputXMLFile
 	 * <b>Description:</b> This is a test method to generate an input xml file
 	 * based on provided test template. 
 	 * It creates an input xml file based on provided template file
 	 * and create an input xml file on shared path.
 	 * 
 	 * @param testData 
 	 * 
 	 **/
	@Test(dataProvider = "get-test-data-method")
	public void generateInputXMLFile(HashMap<String, String> testData) 
	{
		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		
		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"GenerateInputFileAndVerifyTest_data.xlsx");

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
			
				if(testCaseMethodData.get("Execute")!=null && testCaseMethodData.get("Execute").equalsIgnoreCase("Yes"))
				{
					// Get Template File name
					String [] testCaseTemplateFile = testCaseMethodData.get("InputData").split("/");
					String testCaseTemplateFileName = testCaseTemplateFile[testCaseTemplateFile.length-1];
					
					//Copy template file to local input folder
					EllieMaeLog.log(_log, "Copying Test Case template file to local input folder", EllieMaeLogLevel.reporter);

					File f = new File("");
					String inputDirectoryPath = f.getAbsolutePath();
					inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"generateandverify"+ File.separator +"input";;

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
					
					// Read folder data
					String template_Folder = testCaseMethodData.get("Template_Folder");
					template_Folder = template_Folder.replace("\\", "/");
					String test_Files_Folder = testCaseMethodData.get("Test_Loan_Folder");
					test_Files_Folder = test_Files_Folder.replace("\\", "/");
					
					
					// Read data from copied excel file
					EllieMaeLog.log(_log, "Reading Test Case data from Test Case template file", EllieMaeLogLevel.reporter);
					String query = "Select * from  \"Test Case\"";
					HashMap<String, HashMap<String, String>> excelData = GenerateInputFileAndVerifyUtility.getExcelData(inputDirectoryPath+File.separator+testCaseTemplateFileName, query);

		     		for(String key : excelData.keySet())
		     		{
		     			HashMap<String, String> rowData = excelData.get(key);
		     			
			     		// Create template file locally in input folder
						// delete the file if exists already
		     			EllieMaeLog.log(_log, "Deleting the template xml file if already exists", EllieMaeLogLevel.reporter);
						File file = new File(inputDirectoryPath+File.separator+rowData.get("Template_File_Name"));
						file.setWritable(true);
						CommonUtility.deleteFile(rowData.get("Template_File_Name"), "input");
			     		
						// create template file at input folder
						EllieMaeLog.log(_log, "Creating template xml file in local input fodler", EllieMaeLogLevel.reporter);
			     		String templateFileContent ="";
			     		try
			     		{
			     			if(CommonUtilityApplication.isExecutedFromEMDomain())
			     			{
			     				// Read file locally
			     				templateFileContent = CommonUtility.readFile(template_Folder+File.separator+rowData.get("Template_File_Name"));			     				
			     			}
			     			else
			     			{
			     				// Read  file from network location
			     				templateFileContent = CommonUtility.readFileFromNetworkSharedLocation(template_Folder+"/",rowData.get("Template_File_Name"),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));

			     			}
			     		}
			    		catch (Exception e) 
			    		{
			    			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
			    			e.printStackTrace();
			    		}
						CommonUtility.createFileWriteLine(inputDirectoryPath+File.separator+rowData.get("Template_File_Name"), templateFileContent);					

						String templateFileRelativePath = CommonUtility.getRelativeFilePath("input", rowData.get("Template_File_Name"));
						
						EllieMaeLog.log(_log, "Generating input XML file", EllieMaeLogLevel.reporter);
						String inputXMLFileName = rowData.get("Test_File_Name")+".xml";
						inputXMLFileName = StringUtils.deleteWhitespace(inputXMLFileName);
		     			GenerateInputFileAndVerifyUtility.generateInputXML(testData, templateFileRelativePath, inputDirectoryPath,test_Files_Folder, inputXMLFileName, rowData.get("XPath"));
		     			
			             // delete local template file after input file generation
		     			EllieMaeLog.log(_log, "Deleting the template xml file from local input folder", EllieMaeLogLevel.reporter);
			     		CommonUtility.deleteFile(rowData.get("Template_File_Name"), "input");
		     		}
		     		
		     			// delete local template file at the end of test case
		     			EllieMaeLog.log(_log, "Deleting the Test Case template file", EllieMaeLogLevel.reporter);
		     			CommonUtility.deleteFile(testCaseTemplateFileName, "input");
		     		
				}
				
		}
		
	}
	
 	/**
 	 * <b>Author: Jayesh Bhapkar
 	 * <b>Name:</b>verify
 	 * <b>Description:</b> This is a test method to process the input
 	 * xml file and verify expected output with actual result. 
 	 * It processes the input xml file placed at share path,
 	 * generate a output xml file and verify the actual response
 	 * with expected output.
 	 * 
 	 * @param testData 
 	 * 
 	 **/
	@Test(dataProvider = "get-test-data-method")
	public void verify(HashMap<String, String> testData, ITestContext context)
	{

		String additionalDataFilePath = "";
		String strTestDataQuery = "";
		SOAPMaventWebServiceCall ObjSOAPMaventWebServiceCall = new SOAPMaventWebServiceCall(dataBeanMap);
		
		// Get the file path using the utility command
		additionalDataFilePath = CommonUtilityApplication.getRelativeFilePath("data",
				"GenerateInputFileAndVerifyTest_data.xlsx");

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
		
		File f = new File("");
		String inputDirectoryPath = f.getAbsolutePath();
		inputDirectoryPath = inputDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +"generateandverify"+ File.separator +"input";;

		// Call verifyActualResult to process input xmls and verify actual result with expected result
		GenerateInputFileAndVerifyUtility.verifyActualResult(testData, testCaseData, ObjSOAPMaventWebServiceCall, sAssert, inputDirectoryPath);
		
		// Actual Test Count
		@SuppressWarnings("unchecked")
		Map<String,String> testStatusMap = (Map<String, String>) context.getAttribute("testStatusMap");
		testStatusMap.putAll(sAssert.getTestStatus());
		sAssert.getTestStatus().clear(); // clear the status to avoid printing status on console
		context.setAttribute("testStatusMap", testStatusMap);

		sAssert.assertAll();
		
	}
	
}

