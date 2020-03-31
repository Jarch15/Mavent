package com.elliemae.testcases.generateandverify;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.xs.XSModel;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;

import Exception.FilloException;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

/*
 * Author : Jayesh Bhapkar
 * Description : Test Class for generating input xml files
 * from XSD schema.
 */
public class GenerateInputFileFromSchemaTest extends EllieMaeApplicationBase {

	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(GenerateInputFileFromSchemaTest.class);

 	/**
 	 * <b>Author: Jayesh Bhapkar
 	 * <b>Name:</b>generateInputXMLFile
 	 * <b>Description:</b> This is a test method to generate an input xml file
 	 * based on provided XSD schema. 
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
				"GenerateInputFileFromSchemaTest_data.xlsx");

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
					String schema_Folder = testCaseMethodData.get("Schema_Folder");
					schema_Folder = schema_Folder.replace("\\", "/");
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
		     			EllieMaeLog.log(_log, "Deleting the schema XSD file if already exists", EllieMaeLogLevel.reporter);
						File file = new File(inputDirectoryPath+File.separator+rowData.get("Schema_File_Name"));
						file.setWritable(true);
						CommonUtility.deleteFile(rowData.get("Schema_File_Name"), "input");
			     		
						// create template file at input folder
						EllieMaeLog.log(_log, "Creating schema XSD file in local input fodler", EllieMaeLogLevel.reporter);
			     		String schemaFileContent ="";
			     		try
			     		{
			     			if(CommonUtilityApplication.isExecutedFromEMDomain())
			     			{
			     				// Read file locally
			     				schemaFileContent = CommonUtility.readFile(schema_Folder+File.separator+rowData.get("Schema_File_Name"));			     				
			     			}
			     			else
			     			{
			     				// Read  file from network location
			     				schemaFileContent = CommonUtility.readFileFromNetworkSharedLocation(schema_Folder+"/",rowData.get("Schema_File_Name"),FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD));

			     			}
			     		}
			    		catch (Exception e) 
			    		{
			    			EllieMaeLog.log(_log, "Exception occurred during creating schema file in local input folder", EllieMaeLogLevel.reporter);
			    			e.printStackTrace();
			    		}
						CommonUtility.createFileWriteLine(inputDirectoryPath+File.separator+rowData.get("Schema_File_Name"), schemaFileContent);					

						String schemaFileRelativePath = CommonUtility.getRelativeFilePath("input", rowData.get("Schema_File_Name"));
						
						
						// Generate sample xml file with dummy data from Schema file
						EllieMaeLog.log(_log, "Generating sample XML file from Schema file", EllieMaeLogLevel.reporter);
						String generatedSampleXMLPath= "";
						try 
						{
							generatedSampleXMLPath = generateSampleXML(schemaFileRelativePath,inputDirectoryPath,rowData.get("Test_File_Name"));
						} 
						catch (TransformerConfigurationException e) 
						{
							e.printStackTrace();
						} 
						catch (SAXException e) 
						{
							e.printStackTrace();
						}
						
						// clear all dummy data from generated xml file
						
						EllieMaeLog.log(_log, "Updating XML file with blank values (removing dummy values)", EllieMaeLogLevel.reporter);
						File xmlFile = new File(generatedSampleXMLPath);
				        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder dBuilder;
				        try 
				        {
				             dBuilder = dbFactory.newDocumentBuilder();
				             Document doc = dBuilder.parse(xmlFile);
				             doc.getDocumentElement().normalize();
				             Map<String,Set<String>> elementAttributeMap = GenerateInputFileAndVerifyUtility.generateMapOfAllElementsAndAttributes(doc);
				             for(String element : elementAttributeMap.keySet())
					     	{
					     		Set<String> attributes = elementAttributeMap.get(element);
					     		for(String attributeName : attributes)
					     		{
					     			updateXMLAttributeForXpath(doc,"//"+element,attributeName,"");
					     		}
					     	}
				             //write the updated document to file
				             doc.getDocumentElement().normalize();
				             TransformerFactory transformerFactory = TransformerFactory.newInstance();
				             Transformer transformer = transformerFactory.newTransformer();
				             DOMSource source = new DOMSource(doc);
				             StreamResult result = new StreamResult(new File(generatedSampleXMLPath));
				             transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				             transformer.transform(source, result);
				             EllieMaeLog.log(_log, "XML input file updated with blank values", EllieMaeLogLevel.reporter);
				             
				             // copy generated xml file to shared path
				             
							String inputXMLFileName = rowData.get("Test_File_Name")+".xml";
							inputXMLFileName = StringUtils.deleteWhitespace(inputXMLFileName);
							
				      		try 
				     		{
				      			if(CommonUtilityApplication.isExecutedFromEMDomain())
				      			{
				      				// Copying File Locally
				      				CommonUtility.copyFilesOrFolder(inputDirectoryPath+File.separator +inputXMLFileName, test_Files_Folder+File.separator+inputXMLFileName, FileType.FILE);
				      			}
				      			else
				      			{
				      				// Copying File to network
				      				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),inputDirectoryPath+File.separator +inputXMLFileName,test_Files_Folder,inputXMLFileName);
				      			}
				     		}
				     		catch (Exception e) 
				     		{
				     			EllieMaeLog.log(_log, "Exception occurred during copying input xml file to shared path", EllieMaeLogLevel.reporter);
				     			e.printStackTrace();
				     		}
				      		
				      		EllieMaeLog.log(_log, "Generated XML input file copied to shared path successfully", EllieMaeLogLevel.reporter);

				        }
				        catch (Exception e)
				        {
				        	e.printStackTrace();
				        }

						
						// Update sample XML generated using XPaths provided.
						if(rowData.get("XPath")!=null && !rowData.get("XPath").isEmpty())
						{	
							EllieMaeLog.log(_log, "Updating input XML file with Xpath provided", EllieMaeLogLevel.reporter);
							String inputXMLFileName = rowData.get("Test_File_Name")+".xml";
							inputXMLFileName = StringUtils.deleteWhitespace(inputXMLFileName);
							GenerateInputFileAndVerifyUtility.generateInputXML(testData, generatedSampleXMLPath, inputDirectoryPath,test_Files_Folder, inputXMLFileName, rowData.get("XPath"));
						}
		     			
			             // delete local template file after input file generation
		     			EllieMaeLog.log(_log, "Deleting the template xml file from local input folder", EllieMaeLogLevel.reporter);
			     		CommonUtility.deleteFile(rowData.get("Schema_File_Name"), "input");
			     		EllieMaeLog.log(_log, "============================================================================================================", EllieMaeLogLevel.reporter);
		     		}
		     		
		     			// delete local template file at the end of test case
		     			EllieMaeLog.log(_log, "Deleting the Test Case template file", EllieMaeLogLevel.reporter);
		     			CommonUtility.deleteFile(testCaseTemplateFileName, "input");
		     		
				}
				
		}
		
	}
	
	
	public static String generateSampleXML(String schemaFilePath, String inputDirectoryPath, String sampleXMLName) throws TransformerConfigurationException, SAXException {

		//Obtain files from specified folder
		File samplexsd = new File(schemaFilePath);

		//Default options
		XSInstance instance = new XSInstance();
		instance.minimumElementsGenerated = 0;
		instance.maximumElementsGenerated = 0;
		instance.generateDefaultAttributes = true;
		instance.generateOptionalAttributes = true;
		instance.maximumRecursionDepth = 0;
		instance.generateOptionalElements = true;
		
		//Root element to generate
		QName root = new QName("DataPackage");


			if(samplexsd.isFile() && samplexsd.getName().contains("xsd")) 
			{ 
				XSModel xsModel = new XSParser().parse(samplexsd.getAbsolutePath());	
				//Name the result
				XMLDocument sample = new XMLDocument(new StreamResult(inputDirectoryPath+File.separator + sampleXMLName + ".xml"), false, 4, null);
				try 
				{
					instance.generate(xsModel, root, sample);
				} 
				catch (IllegalArgumentException e) 
				{
					e.printStackTrace();
				}
			}
			
		System.out.println("Done");
		
		return inputDirectoryPath+"/" + sampleXMLName + ".xml";
	}
	
    private static void updateXMLAttributeForXpath(Document document, String xPath, String attribute, String value)
    {
   	 XPath xpath = XPathFactory.newInstance().newXPath();
   	 try 
   	 {
       	 	// update element's attribute value for given xPath
			NodeList nodeList = (NodeList) xpath.evaluate(xPath, document,XPathConstants.NODESET);
			for(int i=0;i<nodeList.getLength();i++)
			{
				Element element = (Element)nodeList.item(i);
				element.setAttribute(attribute, value);
	        	EllieMaeLog.log(_log, "XML element "+element.getNodeName()+" updated", EllieMaeLogLevel.reporter);
			}

		} 
   	 catch (XPathExpressionException e) 
   	 {
   		 	EllieMaeLog.log(_log, "Error updating XML element", EllieMaeLogLevel.reporter);
   		 	e.printStackTrace();
   	 }
    }
	
	

}

