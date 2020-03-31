package com.elliemae.testcases.MaventPostLaunchRules;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;
import com.elliemae.core.Utils.CommonUtilityApplication;
import com.elliemae.core.Utils.SQLDBHelper;
import com.elliemae.testcases.generateandverify.GenerateInputFileAndVerifyUtility;

import Exception.FilloException;
import Fillo.Recordset;

/*
 * Author : Jayesh Bhapkar
 * Description : Test Class for Post Launch DataBase Test Cases.
 * To verify specific data from the database.
 */
public class PostLaunchDataBaseTest  extends EllieMaeApplicationBase 
{

	public static Logger _log = Logger.getLogger(PostLaunchDataBaseTest.class);

	/**
	 * <b>Name:</b>validateQuery1
	 * <b>Description:</b> Test method to validate the data from database for
	 * query provided in external excel file.
	 * 
	 * @param testData 
	 * 
	 **/
	@Test(dataProvider = "get-test-data-method")	
	public void validateQuery1(HashMap<String,String> testData)
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);
		Connection conn=null;		
		try
		{
			String query = testData.get("Query1");
			if(query!=null && !query.isEmpty())
			{
				SQLDBHelper sqlDbHelper = new SQLDBHelper();
			
				conn= sqlDbHelper.getDBConnection("ADMIN_SQLDBServerName","ADMIN_SQLUserName","ADMIN_SQLPassword","ADMIN_SQLDBName");
				
				List<HashMap<String, String>> mapList = sqlDbHelper.executeSQLSelect(conn,query);
				
				if(mapList ==null)
				{
					EllieMaeLog.log(_log, "No data found for the query : "+query, EllieMaeLogLevel.reporter);
					sAssert.fail("No data found for the query : "+query);
				}
				else
				{
					
					// Print All Data
					/*
					for(HashMap<String,String> mL : mapList)
					{
						System.out.println(mL);
						System.out.println("------");
					} */
					
					// Validation
					String ValidationContent_Query1 = testData.get("ValidationContent_Query1");
					Map<String, String> validationMap = new HashMap<String,String> ();
					List<String>  validationContentList = new ArrayList<String> ();
					
					if(ValidationContent_Query1!=null && !ValidationContent_Query1.isEmpty())
					{
						validationContentList = GenerateInputFileAndVerifyUtility.loadStringDataToList(ValidationContent_Query1);
						
						for(HashMap<String,String> mL : mapList)
						{
							if(mL.get("VERSION")!=null)
							{
								for(String validationContent : validationContentList)
								{
									if(mL.get("VERSION").equals(validationContent))
									{
										validationMap.put(validationContent, "PASS");
										break;
									}
								}
							}
						}
					}
					
					// put value as FAIL if not found
					for(String validationContent : validationContentList)
					{
						if(validationMap.get(validationContent)==null)
						{
							validationMap.put(validationContent, "FAIL");
						}
					}
					
					// SoftAssert for TestNG report
					EllieMaeLog.log(_log, "------------------------------------------------------------------------------------------------", EllieMaeLogLevel.reporter);
					for(String validationContent : validationMap.keySet())
					{
						EllieMaeLog.log(_log, validationContent+" : "+validationMap.get(validationContent), EllieMaeLogLevel.reporter);
						if(!validationMap.get(validationContent).equalsIgnoreCase("PASS"))
						{
							sAssert.assertTrue(false, "Not found : "+validationContent);
						}
					}
					EllieMaeLog.log(_log, "------------------------------------------------------------------------------------------------", EllieMaeLogLevel.reporter);
				}
			}

		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception occurred :" +e.getMessage(), EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail(e.getMessage());
		}
		finally
		{
			if(conn!=null)
			{
				try 
				{
					conn.close();
				} 
				catch (SQLException e){}				
			}
		}
		
		sAssert.assertAll();
	}
	
	
	/**
	 * <b>Name:</b>validateQuery2
	 * <b>Description:</b> Test method to validate the data from database for
	 * query provided in external excel file.
	 * 
	 * @param testData 
	 * 
	 **/
	@Test(dataProvider = "get-test-data-method")	
	public void validateQuery2(HashMap<String,String> testData)
	{
		// read external data from shared path excel
		readFolderPathsFromExcel(testData);
		Connection conn=null;		
		try
		{
			String query = testData.get("Query2");
			if(query!=null && !query.isEmpty())
			{
				SQLDBHelper sqlDbHelper = new SQLDBHelper();
			
				conn= sqlDbHelper.getDBConnection("ADMIN_SQLDBServerName","ADMIN_SQLUserName","ADMIN_SQLPassword","ADMIN_SQLDBName");
				
				List<HashMap<String, String>> mapList = sqlDbHelper.executeSQLSelect(conn,query);
				
				if(mapList ==null)
				{
					EllieMaeLog.log(_log, "No data found for the query : "+query, EllieMaeLogLevel.reporter);
					sAssert.fail("No data found for the query : "+query);
				}
				else
				{
					// Print All Data
					/*
					for(HashMap<String,String> mL : mapList)
					{
						System.out.println(mL);
						System.out.println("------");
					} */
					
					// Validation
					String ValidationContent_Query1 = testData.get("ValidationContent_Query2");
					Map<String, String> validationMap = new HashMap<String,String> ();
					List<String>  validationContentList = new ArrayList<String> ();
					
					if(ValidationContent_Query1!=null && !ValidationContent_Query1.isEmpty()
							&& mapList != null)
					{
						validationContentList = GenerateInputFileAndVerifyUtility.loadStringDataToList(ValidationContent_Query1);
						
						for(HashMap<String,String> mL : mapList)
						{
							if(mL.get("ID")!=null)
							{
								for(String validationContent : validationContentList)
								{
									if(mL.get("ID").equals(validationContent))
									{
										validationMap.put(validationContent, "PASS");
										break;
									}
								}
							}
							if(mL.get("TAG_NAME")!=null)
							{
								for(String validationContent : validationContentList)
								{
									if(mL.get("TAG_NAME").equals(validationContent))
									{
										validationMap.put(validationContent, "PASS");
										break;
									}
								}
							}
							if(mL.get("LOGIC_TEXT")!=null)
							{
								for(String validationContent : validationContentList)
								{
									if(mL.get("LOGIC_TEXT").equals(validationContent))
									{
										validationMap.put(validationContent, "PASS");
										break;
									}
								}
							}
						}
					}
					
					// put value as FAIL if not found
					for(String validationContent : validationContentList)
					{
						if(validationMap.get(validationContent)==null)
						{
							validationMap.put(validationContent, "FAIL");
						}
					}
					
					// SoftAssert for TestNG report
					EllieMaeLog.log(_log, "------------------------------------------------------------------------------------------------", EllieMaeLogLevel.reporter);
					for(String validationContent : validationMap.keySet())
					{
						EllieMaeLog.log(_log, validationContent+" : "+validationMap.get(validationContent), EllieMaeLogLevel.reporter);
						if(!validationMap.get(validationContent).equalsIgnoreCase("PASS"))
						{
							sAssert.assertTrue(false, "Not found : "+validationContent);
						}
					}
					EllieMaeLog.log(_log, "------------------------------------------------------------------------------------------------", EllieMaeLogLevel.reporter);
					
				}
			}

		}
		catch(Exception e)
		{
			EllieMaeLog.log(_log, "Exception occurred :" +e.getMessage(), EllieMaeLogLevel.reporter);
			e.printStackTrace();
			sAssert.fail(e.getMessage());
		}
		finally
		{
			if(conn!=null)
			{
				try 
				{
					conn.close();
				} 
				catch (SQLException e){}				
			}
		}
		
		sAssert.assertAll();
	}	
	
	
	/* Read the folders details from excel sheet from shared path */
	private void readFolderPathsFromExcel(HashMap<String,String> testData)
	{
		//Copy Excel file to local data folder
		EllieMaeLog.log(_log, "Copying excel file to local data folder", EllieMaeLogLevel.reporter);
		File f = new File("");
		String dataDirectoryPath = f.getAbsolutePath();
		dataDirectoryPath = dataDirectoryPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "com" + File.separator + "elliemae" + File.separator +FrameworkConsts.tlResourceFolder.get()+ File.separator +"data";

		try 
		{
			if(CommonUtilityApplication.isExecutedFromEMDomain())
			{
				CommonUtility.copyFilesOrFolder(testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"), dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"), FileType.FILE);
			}
			else
			{
				CommonUtilityApplication.copyFileFromNetworkLocation(FrameworkConsts.EMUSERDOMAIN, FrameworkConsts.EMNETWORKUSERNAME, CommonUtility.decryptData(FrameworkConsts.EMNETWORKUSERPASSWORD),testData.get("FolderDataSheetPath")+"/"+testData.get("FolderDataSheetFile"),dataDirectoryPath,testData.get("FolderDataSheetFile"));
			}
		} 
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception during copying excel sheet to data folder", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
		// Read data from copied excel file
		EllieMaeLog.log(_log, "Reading Folders data from excel sheet", EllieMaeLogLevel.reporter);
		String query = "Select * from  \"Folders\"";
		getFolderData(testData,dataDirectoryPath+File.separator+testData.get("FolderDataSheetFile"),query);

		
	}
	
	/* read folder data from excel and populate testData hashmap*/
	private void getFolderData(HashMap<String,String> testData,String excelSheetFilePath, String query )
	{
		
		try
		{
			Recordset recordSet=CommonUtility.getRecordSetUsingFillo(excelSheetFilePath,query);
			if(recordSet!=null && recordSet.getCount()>0)
			{
				while(recordSet.next())
				{
					String Query1 = recordSet.getField("Query1");
					if(Query1!=null && !Query1.trim().isEmpty())
					{
						testData.put("Query1", Query1);
					}
					String ValidationContent_Query1 = recordSet.getField("ValidationContent_Query1");
					if(ValidationContent_Query1!=null && !ValidationContent_Query1.trim().isEmpty())
					{
						testData.put("ValidationContent_Query1", ValidationContent_Query1);
					}
					String Query2 = recordSet.getField("Query2");
					if(Query2!=null && !Query2.trim().isEmpty())
					{
						testData.put("Query2", Query2);
					}
					String ValidationContent_Query2 = recordSet.getField("ValidationContent_Query2");
					if(ValidationContent_Query2!=null && !ValidationContent_Query2.trim().isEmpty())
					{
						testData.put("ValidationContent_Query2", ValidationContent_Query2);
					}
				}
			}
		}
		catch (FilloException e) 
		{
			EllieMaeLog.log(_log, "Fillo Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			EllieMaeLog.log(_log, "Exception occurred during reading excel file", EllieMaeLogLevel.reporter);
			e.printStackTrace();
		}
		
	}



}
