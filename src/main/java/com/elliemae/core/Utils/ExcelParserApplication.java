package com.elliemae.core.Utils;

import org.apache.log4j.Logger;

/**
 * <b>Name:</b> ExcelParser  
 * <b>Description: </b>This class is extending ExcelParser of EllieMaeATF and used to create methods related to  Excel workbook.
 *
 */
public class ExcelParserApplication extends ExcelParser {

	public static Logger _log = Logger.getLogger(ExcelParserApplication.class);
	
	public ExcelParserApplication(String dataFilePath, String testName) throws Exception {
		super(dataFilePath, testName);

	}
}