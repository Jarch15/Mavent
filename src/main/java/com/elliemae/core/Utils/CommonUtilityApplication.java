package com.elliemae.core.Utils;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;

import Exception.FilloException;
import Fillo.Connection;
import Fillo.Fillo;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/**
 * <b>Name:</b> CommonUtilityApplication  
 * <b>Description: </b>This class is extending CommonUtility class in EllieMaeATF and is used in various classes as a utility 
 * 
 * @author <i>Supreet Singh</i>
 */

public class CommonUtilityApplication extends CommonUtility {
	
	public static Logger _log = Logger.getLogger(CommonUtilityApplication.class);
	
	
	/**
	 * <b>Name:</b> createOutputFile<br>
	 * <b>Description:</b> This method is used to create an output file
	 * @param outputFilePath
	 * @param responseXML
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void createOutputFile(String outputFilePath, String responseXML) {


		PrintWriter out = null;

		try 
		{
			File outFile = new File(outputFilePath);
			out = new PrintWriter(outFile);
			out.println(responseXML);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Exception occurred during creating output xml");
			EllieMaeLog.log(_log, "Exception occurred during creating output xml", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			System.out.println("Exception occurred during creating output xml");
			EllieMaeLog.log(_log, "Exception occurred during creating output xml", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		} 
		finally 
		{
			if (out != null) 
			{
				out.close();
			}
		}
	}
	
	
	/**
	 * <b>Name:</b> writeFileToNetworkSharedLocation<br>
	 * <b>Description:</b> This method is used to write to a file at network shared location 
	 * @param networkLocation
	 * @param fileName
	 * @param userDomain
	 * @param userName
	 * @param password
	 * 
	 * @return String
	 * <b>Author:</b> Jayesh Bhapkar 
	 * Example: writeFileToNetworkSharedLocation("//sac.fs.dco.elmae/int_evp_data/EVPLog/TraceLog/VPS_PPE_CustomEPPS/","evptest.txt","dco","user","pwd")
	 */ 		 
	public static boolean writeFileToNetworkSharedLocation(String networkLocation, String fileName, String userDomain,String userName, String password,String textToWrite) throws Exception
	{
		boolean successful = false;
		SmbFile sFile;
		NtlmPasswordAuthentication auth;
		
		/* Check if folder is present, else create a folder*/
		String networkLocationDirectory = "smb:" + networkLocation;
		auth = new NtlmPasswordAuthentication(userDomain, userName, password);
		sFile = new SmbFile(networkLocationDirectory, auth);
		try
		{
			if(!sFile.exists()){
				sFile.mkdirs();
			}
		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
		}
		
		String smbNetworkLocation = "smb:" + networkLocation + "/" + fileName;
		sFile = new SmbFile(smbNetworkLocation, auth);
		SmbFileOutputStream sFos = new SmbFileOutputStream(sFile);
		
		try{			
			sFos.write(textToWrite.getBytes());
			successful = true;
		}
		catch(Exception e){
			successful = false;
			EllieMaeLog.log(_log, "Exception in writeFileToNetworkSharedLocation: "+e.getMessage());
			throw new Exception ("Exception in writeFileToNetworkSharedLocation: "+e.getMessage());
		}
		finally {
			sFos.close();
		}
		return successful;
	}

	/**
	 * <b>Name:</b> updateResultToExcelFile<br>
	 * <b>Description:</b> This method is used to update the test data spreadsheet with result
	 * of an assertion of test case .
	 * @param excelSheetFilePath
	 * @param testData
	 * @param resultMap
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateResultToExcelFile(String excelSheetFilePath, HashMap<String, String> testData, Map<String, String> resultMap)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the Status Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+""+"' where Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
			connection.executeUpdate(strUpdateQuery);
				
			/* Update the Status Column with the result from resultMap*/
			for(Entry<String,String> result : resultMap.entrySet())
			{
				String resultValue= result.getValue();
				if(result.getValue().equalsIgnoreCase("Success"))
				{
					resultValue = "PASS";
				}
				else if(result.getValue().equalsIgnoreCase("Failure"))
				{
					resultValue = "FAIL";
				}
				String strQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+resultValue+"' where JIRAID='"+result.getKey()+"' and Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
				connection.executeUpdate(strQuery);
				
			}
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
		updateCombineJIRAStatusReport(resultMap);
	}
	
	/**
	 * <b>Name:</b> updateResultToExcelFileUsingInputData<br>
	 * <b>Description:</b> This method is used to update the test data spreadsheet with result
	 * of an assertion of test case .
	 * @param excelSheetFilePath
	 * @param testData
	 * @param resultMap
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateResultToExcelFileUsingInputData(String excelSheetFilePath, HashMap<String, String> testData, Map<String, String> statusMap)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the Status Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+""+"' where Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
			connection.executeUpdate(strUpdateQuery);
				
			/* Update the Status Column with the result from resultMap*/
			for(Entry<String,String> result : statusMap.entrySet())
			{
				String resultValue= result.getValue();
				if(result.getValue().equalsIgnoreCase("Success"))
				{
					resultValue = "PASS";
				}
				else if(result.getValue().equalsIgnoreCase("Failure"))
				{
					resultValue = "FAIL";
				}
				String strQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+resultValue+"' where InputData='"+result.getKey()+"' and Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
				connection.executeUpdate(strQuery);
				
			}
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
		//updateCombineJIRAStatusReport(resultMap);
	}
	

	/**
	 * <b>Name:</b> updateActualResultToExcelFile<br>
	 * <b>Description:</b> This method is used to update the test data spreadsheet with with actual result
	 * and copies the data sheet to shared path
	 * @param excelSheetFilePath
	 * @param testData
	 * @param actualResult
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateActualResultToExcelFile(String excelSheetFilePath, HashMap<String, String> testData, Map<String,List<List<String>>> actualResult)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the actualResult Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set ActualResult='"+""+"' where Test_Case_Name='"
					+ testData.get("Test_Case_Name") + "'";
			connection.executeUpdate(strUpdateQuery);
			
			/* Update the ActualResult Column with the result from actualResult Map*/
			
			for(String jiraID : actualResult.keySet())
			{
				List<List<String>> listOfResult = actualResult.get(jiraID);
				String result = "";
				for(List<String> list :listOfResult)
				{
					if(list!=null && !list.isEmpty())
					{
						result = result + list.toString()+"\n";
					}
					else
					{
						result = result + "Service Response does not contain the XPath provided"+"\n";
					}
				}
				// replace single quotes
				result = result.replace("'","''");
				
				String strQuery="Update " + testData.get("TestDataSheet") + " Set ActualResult='"+result+"' where JIRAID='"+jiraID+"' and Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
				connection.executeUpdate(strQuery);
			}
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
		// Copy data file to output directory on shared path
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFilePath=testData.get("Output_File_Path")+"/"+timeStamp;
		String [] splitResult = excelSheetFilePath.split("\\\\");
		String outputFileName = "temp";
		outputFileName = splitResult[splitResult.length-1];
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				// Copying File Locally
				CommonUtility.copyFilesOrFolder(excelSheetFilePath, outputFilePath+"/"+outputFileName, FileType.FILE);
			}
			else
			{
				// Copying File to network
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),excelSheetFilePath,outputFilePath,outputFileName);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying excel file to output fodler", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <b>Name:</b> updateActualResultToExcelFileUsingInputData<br>
	 * <b>Description:</b> This method is used to update the test data spreadsheet with with actual result
	 * based on input data file and copies the data sheet to shared path
	 * @param excelSheetFilePath
	 * @param testData
	 * @param actualResult
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateActualResultToExcelFileUsingInputData(String excelSheetFilePath, HashMap<String, String> testData, Map<String,List<List<String>>> actualResult)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the actualResult Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set ActualResult='"+""+"' where Test_Case_Name='"
					+ testData.get("Test_Case_Name") + "'";
			connection.executeUpdate(strUpdateQuery);
			
			/* Update the ActualResult Column with the result from actualResult Map*/
			
			for(String inputData : actualResult.keySet())
			{
				List<List<String>> listOfResult = actualResult.get(inputData);
				String result = "";
				for(List<String> list :listOfResult)
				{
					if(list!=null && !list.isEmpty())
					{
						result = result + list.toString()+"\n";
					}
					else
					{
						result = result + "Service Response does not contain the XPath provided"+"\n";
					}
				}
				// replace single quotes
				result = result.replace("'","''");
				
				String strQuery="Update " + testData.get("TestDataSheet") + " Set ActualResult='"+result+"' where InputData='"+inputData+"' and Test_Case_Name='"
						+ testData.get("Test_Case_Name") + "'";
				connection.executeUpdate(strQuery);
			}
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
		// Copy data file to output directory on shared path
		
		String timeStamp = CommonUtility.currentTimeStamp;		
		String outputFilePath=testData.get("Output_File_Path")+"/"+timeStamp;
		String [] splitResult = excelSheetFilePath.split("\\\\");
		String outputFileName = "temp";
		outputFileName = splitResult[splitResult.length-1];
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				// Copying File Locally
				CommonUtility.copyFilesOrFolder(excelSheetFilePath, outputFilePath+"/"+outputFileName, FileType.FILE);
			}
			else
			{
				// Copying File to network
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),excelSheetFilePath,outputFilePath,outputFileName);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying excel file to output fodler", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <b>Name:</b> updateStatusForFailedTestCase<br>
	 * <b>Description:</b> This method is used to update the test data spreadsheet with status
	 * for Failed Test Cases .
	 * @param excelSheetFilePath
	 * @param testData
	 * @param jira_ID
	 * @param status
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateStatusForFailedTestCase(String excelSheetFilePath,HashMap<String, String> testData, String jira_ID, String status)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the received Status and Actual result to blank */
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+status+"',ActualResult='"+""+"' where Test_Case_Name='"
					+ testData.get("Test_Case_Name") + "' and JIRAID='"+jira_ID+"'";
			connection.executeUpdate(strUpdateQuery);
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
	}
	
	public static void clearDataSheet(String excelSheetFilePath, HashMap<String, String> testData)
	{

		Fillo fillo=new Fillo();
		Connection connection = null ;
		
		try 
		{
			connection = fillo.getConnection(excelSheetFilePath);
			
			/* Update the Status Column for all test cases to blank before updating the status*/
			String strUpdateQuery="Update " + testData.get("TestDataSheet") + " Set Status='"+""+"' , ActualResult='"+""+"' where Test_Case_Name='"
					+ testData.get("Test_Case_Name") + "'";
			connection.executeUpdate(strUpdateQuery);
			
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during updating excel file for status", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}
		
	}
	
	/**
	 * <b>Name:</b> updateCombineJIRAStatusReport<br>
	 * <b>Description:</b> This method is used to create a report spreadsheet for JIRA IDs with its status.
	 * @param resultMap
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void updateCombineJIRAStatusReport(Map<String, String> resultMap)
	{
		//Sort The map based on JIRA ID
		Map<String, String> resultMapSorted = new TreeMap<String, String>(resultMap);
		
		String filePath = "";
		//String localFilePath = "";

		File f = new File("");
		filePath = f.getAbsolutePath();
		//filePath = filePath + "\\AutomationOutput\\"+timeStamp+"\\ScreenShots\\";
		filePath = filePath + File.separator + "AutomationOutput" + File.separator + CommonUtility.currentTimeStamp + File.separator +"JIRA_Report.xlsx";
		
		File reportFile = new File(filePath);
		FileOutputStream outputStream =null;
		try 
		{
			// Create excel file if not exist
			if(!reportFile.exists())
			{
				reportFile.createNewFile();
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Report");
				Row row = sheet.createRow(0);
				
				//Create Style for header row
				CellStyle style = workbook.createCellStyle();
			    style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    Font font = workbook.createFont();
		        font.setColor(IndexedColors.BLACK.getIndex());
		        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		        font.setFontHeightInPoints((short)12);
		        style.setFont(font);		               
				
				Cell cell1 = row.createCell(0);
				cell1.setCellValue("JIRA_ID");
				cell1.setCellStyle(style); 
				Cell cell2 = row.createCell(1);
				cell2.setCellValue("Status");
				cell2.setCellStyle(style);
	            outputStream = new FileOutputStream(reportFile);
	            workbook.write(outputStream);
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try {
				if(outputStream!=null)
				outputStream.close();
			} catch (IOException e) {}
		}
		
		// update excel sheet with JIRA ID and status
		Fillo fillo=new Fillo();
		Connection connection = null ;
		try {
			connection = fillo.getConnection(filePath);
			for(Entry<String,String> result : resultMapSorted.entrySet())
			{
				String resultValue= result.getValue();
				if(result.getValue().equalsIgnoreCase("Success"))
				{
					resultValue = "PASS";
				}
				else if(result.getValue().equalsIgnoreCase("Failure"))
				{
					resultValue = "FAIL";
				}
				String strQuery="INSERT INTO Report(JIRA_ID,Status) VALUES('"+result.getKey()+"','"+resultValue+"')";
				connection.executeUpdate(strQuery);
			}
		} 
		catch (FilloException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
				connection.close();
		}

		
	}
	
	/**
	 * <b>Name:</b> readPDF<br>
	 * <b>Description:</b> This method is used to read the PDF contents from
	 * specified URL location.
	 * @param strURL
	 * 
	 * @return void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void readPDF(String strURL)
	{
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		String parsedText = null;

		try {
			URL url = new URL(strURL);
			BufferedInputStream file = new BufferedInputStream(url.openStream());
			PDFParser parser = new PDFParser(file);
			
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(1);
			
			pdDoc = new PDDocument(cosDoc);
			parsedText = pdfStripper.getText(pdDoc);
			System.out.println("Parsed PDF: "+parsedText);
		} catch (MalformedURLException e2) {
			System.err.println("URL string could not be parsed "+e2.getMessage());
		} catch (IOException e) {
			System.err.println("Unable to open PDF Parser. " + e.getMessage());
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <b>Name:</b> getPDFContents<br>
	 * <b>Description:</b> This method is used to read the PDF contents from
	 * specified PDF File location and returns the contents in String format.
	 * @param pdfFilePath
	 * 
	 * @return String
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static String getPDFContents(String pdfFilePath) 
	{
		 String pdfTest = "";
		 
		 try
		 {
		//Loading an existing document
	      File file = new File(pdfFilePath);
	      PDDocument document = PDDocument.load(file);

	      //Instantiate PDFTextStripper class
	      PDFTextStripper pdfStripper = new PDFTextStripper();

	      //Retrieving text from PDF document
	      pdfTest = pdfStripper.getText(document);
	      //System.out.println("PDF Content : "+pdfTest);

	      //Closing the document
	      document.close();
		 }
		 catch(IOException ex)
		 {
			EllieMaeLog.log(_log, "Exception occurred reading PDF file - "+pdfFilePath+" : "+ex.getMessage());
			ex.printStackTrace();
		 }
	      
	      return pdfTest;
	}
	
	
	/**
	 * <b>Name:</b> threadWait<br>
	 * <b>Description:</b> This method is used to add a wait by using thread sleep for milliseconds
	 * provided in input argument.
	 * @param waitTime
	 * 
	 * @return Void
	 * <b>Author:</b> Jayesh Bhapkar 
	 */
	public static void threadWait(int waitTime) {
		try {
			Thread.sleep(waitTime);
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
    /**
    * <b>Name:</b> listOfFileFromNetworkSharedLocation<br>
    * <b>Description:</b> This method is used to get list of Files on network location
     * @param networkLocation
    * @param userDomain
    * @param userName
    * @param password
    *
     * @return SmbFile[]
    * <b>Author:</b> Jayesh Bhapkar
    */                           
    public static SmbFile[] listOfFileFromNetworkSharedLocation(String networkLocation,String userDomain,String userName, String password) throws Exception
    {
                    String smbNetworkLocation = "smb:" + networkLocation;
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(userDomain, userName, password);
                    SmbFile dir = new SmbFile(smbNetworkLocation, auth);
                    return dir.listFiles();
    }
    
    /**
    * <b>Name:</b> generateRandomNumber<br>
    * <b>Description:</b> This method is used to generate a random number
    * between the range 0 to 999.
    *
     * @return int
    * <b>Author:</b> Jayesh Bhapkar
    */                           
    public static int generateRandomNumber()
    {
        Random rand = new Random(); 
        // Generate random integers in range 0 to 999
        return rand.nextInt(1000);
    }
    
	/**
	 * <b>Name:</b> takeScreenShot<br>
	 * <b>Description:</b> This method is used to take screen shot of whole page.
	 * It also copies the image to shared path. 
	 * and copies the image file to shared location
	 * 
	 * @param testData
	 * @param methodName
	 * @param timeStamp
	 * <b>Author:</b> Jayesh Bhapkar
	 */
	public static String takeScreenShot(HashMap<String, String> testData,String methodName,String timeStamp) {
		String filePath = "";
		//String localFilePath = "";

		File f = new File("");
		filePath = f.getAbsolutePath();
		//filePath = filePath + "\\AutomationOutput\\"+timeStamp+"\\ScreenShots\\";
		filePath = filePath + File.separator + "AutomationOutput" + File.separator + timeStamp + File.separator + "ScreenShots" + File.separator;
		String imageFilePath = filePath+methodName+".jpg";
			
		try
		{
			// Create a folder if not exist
		    File directory = new File(filePath);
		    if (! directory.exists()){
		        directory.mkdir();
		    }
		    // Capture screenshot and save on local
		    Robot robot = new Robot();
		    BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		    ImageIO.write(screenShot, "JPG", new File(imageFilePath));
		}
		catch(Exception ex)
		{
			EllieMaeLog.log(_log, "Exception in takeScreenShot method: "+ex.getMessage());
			ex.printStackTrace();
		}
		
		// Copy image file from AutomationOutput to output directory on shared path

		String outputFilePath=testData.get("Output_File_Path")+"/"+timeStamp;
		String [] splitResult = imageFilePath.split("\\\\");
		String outputFileName = "temp";
		outputFileName = splitResult[splitResult.length-1];
		
		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				// Copying File Locally
				CommonUtility.copyFilesOrFolder(imageFilePath, outputFilePath+"/"+outputFileName, FileType.FILE);
			}
			else
			{
				// Copying File to network
				CommonUtility.copyFileToNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),imageFilePath,outputFilePath,outputFileName);
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during copying image file to output folder " + outputFilePath, EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		return imageFilePath;
	}
	
	/**
	 * <b>Name:</b> scrollPage<br>
	 * <b>Description:</b> This method is used to scroll web page
	 * 
	 * @param xPath
	 * @param driver
	 * <b>Author:</b> Nidhi Khandelwal
	 */
	public static void scrollPage(String xPath, WebDriver driver)
	{
		WebElement e = driver.findElement(By.xpath(xPath));
		((JavascriptExecutor) driver).executeScript(
		"arguments[0].scrollIntoView();", e);
	}
	
	/**
	 * <b>Name:</b> copyFileFromNetworkLocation<br>
	 * <b>Description:</b> This method is used to copy file from network
	 * 
	 * @param userDomain
	 * @param userName
	 * @param password
	 * @param  sourceFilePath
	 * @param destinationPath
	 * @param destinationFileName
	 * <b>Author:</b> Jayesh Bhapkar
	 */
	public static boolean copyFileFromNetworkLocation(String userDomain, String userName,String password, String sourceFilePath, String destinationPath,String destinationFileName) throws Exception {
		boolean successful = false;
		NtlmPasswordAuthentication auth;
		SmbFile sFile;
		SmbFileInputStream smbFileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try{
			auth = new NtlmPasswordAuthentication(userDomain, userName, password);
			sourceFilePath = "smb:" + sourceFilePath;
			EllieMaeLog.log(_log, "Source File Path     : " +sourceFilePath);
			EllieMaeLog.log(_log, "Destination Path     : " +destinationPath);
			EllieMaeLog.log(_log, "Destination FileName : " +destinationFileName);
			
			sFile = new SmbFile(sourceFilePath, auth);
			try
			{
				if(!sFile.exists()){
					sFile.mkdirs();
				}
			}
			catch(Exception e)
			{
				EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
			}
			sFile = new SmbFile(sourceFilePath , auth);
			
			smbFileInputStream = new SmbFileInputStream(sFile);
			
			File file = new File(destinationPath+File.separator+destinationFileName);
			file.setWritable(true);
			fileOutputStream = new FileOutputStream(file);

			byte[] buf = new byte[16 * 1024 * 1024];
			int len;
			while ((len = smbFileInputStream.read(buf)) > 0) {
				fileOutputStream.write(buf, 0, len);
			}			
			successful = true;
			EllieMaeLog.log(_log,"File copied to local folder");
		}
		catch(Exception ex){
			successful = false;
			EllieMaeLog.log(_log, "Exception in copyFileToNetworkLocation: " + ex.getMessage());
			throw new Exception ("Exception in copyFileToNetworkLocation: " + ex.getMessage());
		}
		finally{
			if(fileOutputStream !=null)
				fileOutputStream.close();
			if(smbFileInputStream != null)
				smbFileInputStream.close();
		}
		return successful;
	}
	
	/**
	 * <b>Name:</b> isExecutedFromEMDomain<br>
	 * <b>Description:</b> This method is used to identify
	 * if the Test is executed from "EM" domain or not.
	 * 
	 * @return boolean
	 * <b>Author:</b> Jayesh Bhapkar
	 */
	public static boolean isExecutedFromEMDomain()
	{
		if(System.getenv("USERDOMAIN").equals("EM"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String getIpAddress(String URL)
	{
		InetAddress address = null;
		try {
			address = InetAddress.getByName(new URL(URL).getHost());
		} catch (UnknownHostException | MalformedURLException e) {
		}
		return address.getHostAddress();
	}
	
	public static void clearClipboard()
	{
		StringSelection stringSelection = new StringSelection("");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
		            stringSelection, null);
	}
	
	public static String getClipboardData() {
	    Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	    try {
	        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	            return text;
	        }
	    } catch (UnsupportedFlavorException e) {
	    } catch (IOException e) {}
	    return null;
	}
	
	
	/**
	 * <b>Name:</b> copyFileFromNetworkLocationToNetworkLocation<br>
	 * <b>Description:</b> This method is used to copy file from network
	 * to network location.
	 * 
	 * @param userDomain
	 * @param userName
	 * @param password
	 * @param  sourceFilePath
	 * @param destinationPath
	 * @param destinationFileName
	 * <b>Author:</b> Jayesh Bhapkar
	 */
	public static boolean copyFileFromNetworkLocationToNetworkLocation(String userDomain, String userName,String password, String sourceFilePath, String destinationPath,String destinationFileName) throws Exception {
		boolean successful = false;
		NtlmPasswordAuthentication auth;
		SmbFile sourceFile;
		SmbFile smbDestinationPath;
		SmbFile destinationFile;
		SmbFileInputStream smbFileInputStream = null;
		SmbFileOutputStream smbFileOutputStream = null;
		try{
			auth = new NtlmPasswordAuthentication(userDomain, userName, password);
			sourceFilePath = "smb:" + sourceFilePath;
			EllieMaeLog.log(_log, "Source File Path     : " +sourceFilePath, EllieMaeLogLevel.reporter);
			destinationPath = "smb:" + destinationPath;
			EllieMaeLog.log(_log, "Destination Path     : " +destinationPath, EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log, "Destination FileName : " +destinationFileName, EllieMaeLogLevel.reporter);
			
			smbDestinationPath = new SmbFile(destinationPath, auth);
			try
			{
				if(!smbDestinationPath.exists()){
					smbDestinationPath.mkdirs();
				}
			}
			catch(Exception e)
			{
				EllieMaeLog.log(_log, "Exception occurred during creating folder. "+e.getMessage(), EllieMaeLogLevel.reporter);
			}
			sourceFile = new SmbFile(sourceFilePath + "/" + destinationFileName , auth);
			
			smbFileInputStream = new SmbFileInputStream(sourceFile);
			
			destinationFile = new SmbFile(destinationPath + "/" + destinationFileName, auth);
			smbFileOutputStream = new SmbFileOutputStream(destinationFile);


			byte[] buf = new byte[16 * 1024 * 1024];
			int len;
			while ((len = smbFileInputStream.read(buf)) > 0) {
				smbFileOutputStream.write(buf, 0, len);
			}			
			successful = true;
			EllieMaeLog.log(_log,"File copied to local folder", EllieMaeLogLevel.reporter);
		}
		catch(Exception ex){
			successful = false;
			EllieMaeLog.log(_log, "Exception in copyFileToNetworkLocation: " + ex.getMessage(), EllieMaeLogLevel.reporter);
			throw new Exception ("Exception in copyFileToNetworkLocation: " + ex.getMessage());
		}
		finally{
			if(smbFileOutputStream !=null)
				smbFileOutputStream.close();
			if(smbFileInputStream != null)
				smbFileInputStream.close();
		}
		return successful;
	}
}
