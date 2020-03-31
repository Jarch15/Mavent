package com.elliemae.core.APIUtility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.util.Predicate;

import com.elliemae.consts.FrameworkConsts;
import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.JSONUtility;
import com.elliemae.core.Utils.XMLUtility;
import com.elliemae.core.asserts.SoftAssert;
import com.elliemae.core.filter.AttributeFilter;
import com.elliemae.core.filter.NodeFilter;

/**
 * <b>Name:</b> APIValidationMethods  
 * <b>Description: </b>This class is extending APIValidationMethods class in EllieMaeATF and is used to Validate XPath or JSON data. 
 * 
 * @author <i>Supreet Singh</i>
 */
public class APIValidationMethodsApplication extends APIValidationMethods {

	public static Logger _log = Logger.getLogger(APIValidationMethodsApplication.class);
	
	private static final String TOKEN_JIRAID = "JIRAID:";
	private static final String TOKEN_VALIDATION_DESCRIPTION = "ValidationDescription:";
	private static final String STR_ATTRIBUTE_IDENTIFIER = "/@";
	private static final String ENTITIES = "entities";
	private static final String OCCURANCE_COUNT = "occuranceCount";
	private static final String HAS_CONTENT = "hasContent";
	private static final String TOKEN_FILE = "file;";	
	
	public void xmlDiffForXPath(String responseXMLString, String baselineXMLString, String xPath, SoftAssert sAssert) throws SAXException, IOException {
		xmlDiffForXPath(responseXMLString, baselineXMLString, xPath, sAssert, null, null);
	}


	
	
 

	
	
	public void xmlDiffForXPath(String responseXMLString, String baselineXMLString, String xPath, SoftAssert sAssert, List<String> nodeNamesToIgnore, List<String> attributeNamesToIgnore) throws SAXException, IOException
	{
		EllieMaeLog.log(_log,"***********************************************************xmlDiffForXPath VALIDATION START*********************************************************",EllieMaeLogLevel.reporter);
		String xmlFilename="";
		String[] arrayBaselineXMLData, arrayResponseXMLData;
		Node baselineXMLNode = null, responseXMLNode = null;
		Document baselineXMLDoc, responseXMLDoc;
		Diff diff;
		
		try {			
			Validate.notBlank(responseXMLString, "Response XML value must be provided.");	
			responseXMLString = responseXMLString.trim();
			Validate.notBlank(baselineXMLString, "Baseline XML value must be provided.");
			baselineXMLString = baselineXMLString.trim();
			Validate.notBlank(xPath, "XPath value must be provided.");

			xPath = xPath.trim();
			
			if(xPath.matches(".?//@\\*") || xPath.endsWith("/@*") || (xPath.lastIndexOf("/") == xPath.lastIndexOf(STR_ATTRIBUTE_IDENTIFIER) && (xPath.endsWith("]") && xPath.matches("/@\\\\[(\\\\d)+\\\\]$")))) {
				throw new Exception("XPath '" + xPath + "' is not supported.");
			}
			
			if(null == nodeNamesToIgnore) {
				nodeNamesToIgnore = new LinkedList<>();
			}
			
			if(null == attributeNamesToIgnore) {
				attributeNamesToIgnore = new LinkedList<>();
			}
			
			EllieMaeLog.log(_log, "XPath used:'" + xPath + "'");
			
			DocumentBuilder docBuilder = getDocumentBuilderInstance();
			
			//arrayResponseXMLData = splitStringBasedOnPattern(responseXMLString, FrameworkConsts.SPLITSCOMMAPATTERN);
			/*if(arrayResponseXMLData[0].startsWith(TOKEN_FILE)) {	
				xmlFilename =arrayResponseXMLData[0].substring((arrayResponseXMLData[0].indexOf(TOKEN_FILE)+TOKEN_FILE.length()));
				responseXMLDoc = docBuilder.parse(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("classes/","test-classes/com/elliemae/Sample/baseline"),xmlFilename.trim()));
			}
			else {*/
			
			responseXMLDoc = docBuilder.parse(responseXMLString);
			
			//	responseXMLDoc = docBuilder.parse(new InputSource(new StringReader(responseXMLString)));
		/*	}
			*/
			
			//responseXMLDoc = docBuilder.parse(new InputSource(new StringReader(responseXMLString)));

			responseXMLNode = XMLUtility.getNodeForXPath(responseXMLString, xPath);
			
		//	arrayBaselineXMLData = splitStringBasedOnPattern(baselineXMLString, FrameworkConsts.SPLITSCOMMAPATTERN);
		//	if(arrayBaselineXMLData[0].startsWith(TOKEN_FILE)) {	
			//	xmlFilename =arrayBaselineXMLData[0].substring((arrayBaselineXMLData[0].indexOf(TOKEN_FILE)+TOKEN_FILE.length()));
			//	baselineXMLDoc = docBuilder.parse(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("classes/","test-classes/com/elliemae/Sample/baseline"),xmlFilename.trim()));
			//}
	//		else {
				baselineXMLDoc = docBuilder.parse(new InputSource(new StringReader(baselineXMLString)));
		//	}
			
			baselineXMLNode = XMLUtility.getNodeForXPath(baselineXMLDoc, xPath);
			
			diff = getDifferenceObject(responseXMLNode, baselineXMLNode, nodeNamesToIgnore, attributeNamesToIgnore);

			Iterable<Difference> allDifferences = diff.getDifferences();
			EllieMaeLog.log(_log, "Difference exists:" + diff.hasDifferences(), EllieMaeLogLevel.reporter);
			
			for(Object difference : allDifferences) {
					EllieMaeLog.log(_log,difference.toString());				
				}

			EllieMaeLog.log(_log, "******************************************************************************", EllieMaeLogLevel.reporter);
			sAssert.assertFalse(diff.hasDifferences(), "Differences found in XMLs.");
		} 
		catch (Exception e) {
			EllieMaeLog.log(_log, "Exception in xmlDiffForXPath(): "+e.getMessage(),EllieMaeLogLevel.reporter);
			EllieMaeLog.log(_log, "Exception StackTrace: "+e.getStackTrace(),EllieMaeLogLevel.reporter);
			sAssert.assertTrue(false,"Exception in xmlDiffForXPath()"+e.getMessage());
		}
		finally {
			EllieMaeLog.log(_log,"***********************************************************xmlDiffForXPath VALIDATION END***********************************************************",EllieMaeLogLevel.reporter);
		}
	}
	
	
	private String[] splitStringBasedOnPattern(String baselineXMLString, String patternString) {
		String[] array=baselineXMLString.split(Pattern.quote(patternString));
		return array;
	}
	
	
	
	private Diff getDifferenceObject(Object responseXML, Object baselineXML, List<String> nodeNamesToIgnore, List<String> attributeNamesToIgnore)
			throws Exception {
		Diff diff=null;
		Predicate<Node> nodeFilter;
		Predicate<Attr> attributeFilter;
		
		Validate.notNull(responseXML, "The response XML is missing for the request.");
		Validate.notNull(baselineXML, "The baseline XML is missing for the request.");
		
		EllieMaeLog.log(_log, "Response responseXML: " +responseXML.toString());
		EllieMaeLog.log(_log, "Baseline baselineXML: " +baselineXML.toString());
		
		if(CollectionUtils.isEmpty(nodeNamesToIgnore)) {
			nodeFilter = new NodeFilter();
		}
		else {
			nodeFilter = new NodeFilter(nodeNamesToIgnore);
		}

		if(CollectionUtils.isEmpty(attributeNamesToIgnore)) {
			attributeFilter = new AttributeFilter();
		}
		else {
			attributeFilter = new AttributeFilter(attributeNamesToIgnore);
		}
		
		if(responseXML instanceof Node && baselineXML instanceof Node) {
			diff = DiffBuilder.compare(Input.fromNode((Node)responseXML))
					.withTest(Input.fromNode((Node)baselineXML))
					.checkForIdentical()
					.ignoreComments()
					.normalizeWhitespace()
					.withDifferenceEvaluator(DifferenceEvaluators.Default)
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes, ElementSelectors.byNameAndText))
					.withNodeFilter(nodeFilter)
					.withAttributeFilter(attributeFilter)
					.build();
		}
		else if(responseXML instanceof Document && baselineXML instanceof Document) {
			diff = DiffBuilder.compare(Input.fromDocument((Document) responseXML))
					.withTest(Input.fromDocument((Document)baselineXML))
					.checkForIdentical()
					.ignoreComments()
					.normalizeWhitespace()
					.withDifferenceEvaluator(DifferenceEvaluators.Default)
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes, ElementSelectors.byNameAndText))
					.withNodeFilter(nodeFilter)
					.withAttributeFilter(attributeFilter)
					.build();
		}
		else {
			throw new Exception("Invalid responseXML/baselineXML instance found in the request.");
		}

		return diff;
	}
	
	   public static boolean xmlAttributeComparison(String source,String target, String attributeStart, String attributeEnd)
	    {
	                    int sourceIndex1= source.indexOf(attributeStart);
	                    //System.out.println("sourceIndex1 ="+sourceIndex1);
	                    
	                    int sourceIndex2= source.lastIndexOf(attributeEnd);
	                    //System.out.println("sourceIndex2 ="+sourceIndex2);
	                    
	                    String sourceSubstring = source.substring(sourceIndex1, sourceIndex2+attributeEnd.length());
	                    EllieMaeLog.log(_log,"Source substring = " +sourceSubstring,EllieMaeLogLevel.reporter);

	                    int targetIndex1= target.indexOf(attributeStart);
	                   // System.out.println("targetIndex1 ="+targetIndex1);
	                    
	                    int targetIndex2= target.lastIndexOf(attributeEnd);
	                    //System.out.println("targetIndex2 ="+targetIndex2);

	                    String targetSubstring = target.substring(targetIndex1, targetIndex2+attributeEnd.length());
	                    targetSubstring = targetSubstring.trim();                           
	                    EllieMaeLog.log(_log,"Target substring = " +targetSubstring,EllieMaeLogLevel.reporter);
	                    
	                    if(sourceSubstring.trim().equals(targetSubstring))
	                    {
	                    	return true;
	                    }
	                    else
	                    	return false;
	    				}
	   	/* Compare two XMLs using XMLUnit Utility */
	   	public static boolean compareXMLUsingXMLUnit(String source,String target, String attributeStart, String attributeEnd)
	   	{
		   				boolean result =false;
	                    int sourceIndex1= source.indexOf(attributeStart);	                    
	                    int sourceIndex2= source.lastIndexOf(attributeEnd);
	                    
	                    String sourceSubstring = source.substring(sourceIndex1, sourceIndex2+attributeEnd.length());
	                    EllieMaeLog.log(_log,"Source substring = " +sourceSubstring,EllieMaeLogLevel.reporter);

	                    int targetIndex1= target.indexOf(attributeStart);
	                    int targetIndex2= target.lastIndexOf(attributeEnd);

	                    String targetSubstring = target.substring(targetIndex1, targetIndex2+attributeEnd.length());
	                    targetSubstring = targetSubstring.trim();                           
	                    EllieMaeLog.log(_log,"Target substring = " +targetSubstring,EllieMaeLogLevel.reporter);
	                    
	            		DetailedDiff diff = null;
						try 
						{
							diff = new DetailedDiff(XMLUnit.compareXML(sourceSubstring, targetSubstring));
						} 
						catch (SAXException | IOException e) 
						{
							e.printStackTrace();
						}
						if(diff!=null)
						{
							diff.overrideElementQualifier(new ElementNameAndAttributeQualifier());
							result = diff.similar();
						}
							
	                    //System.out.println("XML comparison Result :  " + result);
	                    return result;
	    }	   
	   
	   
		/**
		 * <b>Name:</b>xPathValidation<br>
		 * <b>Description:</b> This is method is used to compare the XPath value with the base value.<br>
		 * 
		 * @param responseXMLString The response XML.
		 * @param baseLineXPathData The baseline XML
		 * @param sAssert The SoftAssert instance.
		 * @throws IOException The IOException instance.
		 */
		public List<List<String>> xPathValidationExtension(String responseXMLString, String baseLineXPathData, SoftAssert sAssert) throws IOException
		{
			EllieMaeLog.log(_log,"***********************************************************XPATH VALIDATION START**********************************************************",EllieMaeLogLevel.reporter);
			List<String[]> baseKVjSONPathList=null;
			String defaultJIRA_ID = sAssert.getCurrJIRA_ID();
			List<List<String>> actualXpathList = new ArrayList<List<String>>();

			try {
				if(baseLineXPathData.startsWith(TOKEN_FILE))
				{
					String[] baseLineContentArray=baseLineXPathData.split(";");
					String filePath=baseLineContentArray[1];
					baseKVjSONPathList=loadStringDataFromFileToList(filePath);
				}
				else
				{
					baseKVjSONPathList=loadStringDataToList(baseLineXPathData);
				}
				String validationTypeKey = getValidationTypeKeyword(ValidationEnum.XPATHVALIDATION.toString());
				List<String> actualXpathValue=null;	
				String xmlContent = "";
				if(responseXMLString.startsWith(TOKEN_FILE))
					xmlContent = CommonUtility.getStringFromFile(responseXMLString,"baseline");		
				else
					xmlContent = responseXMLString;

				for (String[] strArry: baseKVjSONPathList) {				
					actualXpathValue = executeJSONorXPATH(xmlContent, strArry[0],"","",ValidationEnum.XPATHVALIDATION.toString(),strArry[1]);
					doValidation(strArry, actualXpathValue, sAssert, validationTypeKey, defaultJIRA_ID);
					actualXpathList.add(actualXpathValue);
					EllieMaeLog.log(_log,"*******************************************************************************************************************************************************",EllieMaeLogLevel.reporter);
				}	
			} 
			catch (Exception e) {
				EllieMaeLog.log(_log, "Exception in xPathValidation(): "+e.getMessage(),EllieMaeLogLevel.reporter);
				EllieMaeLog.log(_log, "Exception StackTrace: "+e.getStackTrace(),EllieMaeLogLevel.reporter);
				sAssert.assertTrue(false,"Exception in xPathValidation()"+e.getMessage());
			}
			finally {
				EllieMaeLog.log(_log,"***********************************************************XPATH VALIDATION END************************************************************",EllieMaeLogLevel.reporter);
			}
			
			return actualXpathList;
		}
		
		/**
		 * <b>Name:</b>loadStringDataFromFileToList<br>
		 * <b>Description:</b> This is method is used to load String data from a txt file to List.<br>
		 * 
		 * @param fileName
		 * 
		 * @throws IOException 
		 * @return  List
		 **/
		private List<String[]>  loadStringDataFromFileToList(String fileName) throws IOException
		{

			List<String[]> returnList=new ArrayList<String[]>();
			fileName= fileName.replace(TOKEN_FILE, "");

			String filePath = CommonUtility.getRelativeFilePath("baseline", fileName);
			BufferedReader inputReader = new BufferedReader(new FileReader(filePath));		
			String line = "";
			while ((line = inputReader.readLine()) != null) {
				String trimLine = line.substring(1, line.length()-1); 
				String parts[] = trimLine.split(Pattern.quote(FrameworkConsts.SPLITSCOMMAPATTERN));
				returnList.add(parts); 
			}
			inputReader.close();
			return returnList;
		}
		
		/**
		 * <b>Name:</b>loadStringDataToList<br>
		 * <b>Description:</b> This is method is used to load input data from a string to List with respect to delimiters (];[ and ],[).<br>
		 * 
		 * @param inputString
		 * 
		 * @throws IOException 
		 * @return  List
		 **/
		private List<String[]> loadStringDataToList(String inputString)
		{

			List<String> list=null;
			if(inputString.contains("\n") || inputString.contains(FrameworkConsts.SPLITSEMICOLONPATTERN) )
			{
				String pattern="";
				if(inputString.contains("\n"))
					pattern="\n";
				else
					pattern=FrameworkConsts.SPLITSEMICOLONPATTERN;
				list = new ArrayList<String>(Arrays.asList(inputString.split(Pattern.quote(pattern))));
			}
			else
			{
				list=new ArrayList<String>();
				list.add(inputString);
			}

			List<String[]> returnList=new ArrayList<String[]>();
			for(String str : list)
			{
				String inputStringTrim = str.substring(1, str.length()-1); 
				String[] keyValuePairs = splitStringBasedOnPattern(inputStringTrim,FrameworkConsts.SPLITSCOMMAPATTERN); 
				returnList.add(keyValuePairs); 
			}       
			return returnList;
		}
		
		/**
		 * <b>Name:</b>getValidationTypeKeyword<br>
		 * <b>Description:</b> This is method is used to get the String Key(JSONPath or Xpath which is used in logging).<br>
		 * 
		 * @param validationType	
		 * @return  String 
		 */
		private String getValidationTypeKeyword(String validationType) {
			String Key="";
			if(validationType == ValidationEnum.JSONVALIDATION.toString())
				Key="JSONPath";
			else
				Key="XPath";
			return Key;
		}
		
		/**
		 * <b>Name:</b>executeJSONorXPATH<br>
		 * <b>Description:</b> This is method is used to get the actual value from a JSON or XPath.<br>
		 * 
		 * @param respXMLdocument
		 * @param basexPath
		 * @param baseJsonPath
		 * @param respJsonString
		 * @param validationType
		 */
		private List<String> executeJSONorXPATH(String  respXMLdocument, String basexPath, String respJsonString,String baseJsonPath, String validationType, String... validationKey)
				throws XPathExpressionException {
			List<String> list=new ArrayList<String>();

			if(validationType.equals(ValidationEnum.XPATHVALIDATION.toString()))
			{
				if(validationKey.length > 0 && validationKey[0].equals(ValidationEnum.XMLCHILDNODEEXISTS.toString()))  
				    list = XMLUtility.getChildNodes(respXMLdocument, basexPath);   //To know Child Nodes exists or not for XMLNode.
			    else
				     list= XMLUtility.getXPathValueUsingXPath(respXMLdocument,basexPath);
			}
			else
				list= JSONUtility.getJSONValueUsingJSONPath(respJsonString,baseJsonPath);
			
			return list;
		}
		
		/**
		 * <b>Name:</b>doValidation<br>
		 * <b>Description:</b> This is method is used to do the actual validation and assert with base values.<br>
		 * 
		 * @param baseXPathORJSONArray The baseline entry data array.
		 * @param actualXPathORJSONValue The actual values list.
		 * @param sAssert The SoftAssert instance.
		 * @param validationTypeKey The key identifying the validation type.
		 * @param defaultJIRA_ID The default JIRA Id.
		 * @throws Exception The exception instance.
		 */
		@SuppressWarnings("static-access")
		private void doValidation(String[] baseXPathORJSONArray,  List<String> actualXPathORJSONValue, SoftAssert sAssert, String validationTypeKey, String defaultJIRA_ID) throws Exception {
			validationTypeKey = validationTypeKey.trim();

			ValidationEnum validationKeyWordEnum = ValidationEnum.valueOf(baseXPathORJSONArray[1]);	

			int actualListSize=0;

			try {
				/** Populates JIRA ID from base path if present. */
				sAssert.setCurrJIRA_ID("");
				
				for(int i = baseXPathORJSONArray.length-1; 2 < i; i--) {
					if(StringUtils.isNotBlank(baseXPathORJSONArray[i]) && baseXPathORJSONArray[i].contains(TOKEN_JIRAID)) {
						sAssert.setCurrJIRA_ID(baseXPathORJSONArray[i].substring(baseXPathORJSONArray[i].indexOf(":")+1).trim());
						break;
					}
				}
				
				if(StringUtils.isBlank(sAssert.getCurrJIRA_ID())) {
					sAssert.setCurrJIRA_ID(defaultJIRA_ID);	
				}
				
				if (null == actualXPathORJSONValue && !(baseXPathORJSONArray[1].equals(validationKeyWordEnum.KEYEXISTS.toString()) || baseXPathORJSONArray[1].equals(validationKeyWordEnum.ISNULL.toString()))) {
					sAssert.assertTrue(false, "Service Response does not contain the "+ validationTypeKey +" provided!"+ baseXPathORJSONArray[0]);
					throw new Exception("Service Response does not contain the "+ validationTypeKey +" provided!" + baseXPathORJSONArray[0]);
				}
				/*else   if (actualXPathORJSONValue instanceof List && ((List<?>)actualXPathORJSONValue).isEmpty() &&  !baseXPathORJSONArray[1].equals(validationKeyWordEnum.KEYEXISTS.toString())) {
					EllieMaeLog.log(_log, validationTypeKey+" result " + baseXPathORJSONArray[0]+" is empty.",EllieMaeLogLevel.reporter);
					sAssert.assertTrue(false,validationTypeKey+" result " + baseXPathORJSONArray[0]+" is empty.");
				}*/
				else {
					Boolean isEqual = false;
					String expectedCount = "-1";
					Map<String, Object> resultantMap = null;

					try {
						/** Log test case description. */
						for(int i = baseXPathORJSONArray.length-1; 2 < i; i--) {
							if(StringUtils.isNotBlank(baseXPathORJSONArray[i]) && baseXPathORJSONArray[i].contains(TOKEN_VALIDATION_DESCRIPTION)) {
								EllieMaeLog.log(_log, baseXPathORJSONArray[i], EllieMaeLogLevel.reporter);
								break;
							}
						}
						
						if(actualXPathORJSONValue != null)
							actualListSize=actualXPathORJSONValue.size();
						if(!baseXPathORJSONArray[1].equals(validationKeyWordEnum.SCHEMA.toString()))
						{
							if(baseXPathORJSONArray[2].contains(TOKEN_FILE))
								baseXPathORJSONArray[2]=CommonUtility.getStringFromFile(baseXPathORJSONArray[2].replace(TOKEN_FILE, ""),"baseline");
						}
						
						switch(validationKeyWordEnum) {
							case EQUALS:
								String extractedString=baseXPathORJSONArray[2];			
								String [] array =extractedString.split(Pattern.quote("|"));
								List<String> baseList =new ArrayList<String>();
								baseList = Arrays.asList(array);
								
								if(baseXPathORJSONArray[2].contains("|"))
								{							
									List<String> actualList = new ArrayList<String>();						
									for(Object val : actualXPathORJSONValue){
									   actualList.add(val.toString());
									}				
									isEqual = baseList.toString().equals(actualList.toString());						
									sAssert.assertTrue(isEqual, " Query: EQUALS, Base Value: \""+ baseList +"\", Actual Value: \" "+actualXPathORJSONValue.toString()+"\", Base value not equals actual value.");
									
									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: EQUALS,  Base Value: \""+baseList.toString()+"\", Actual Value: \""+ actualXPathORJSONValue.toString()+"\"",EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: EQUALS,  Base Value: \""+baseList.toString()+"\", Actual Value: \""+ actualXPathORJSONValue.toString()+"\", Base value not equals actual value.",EllieMaeLogLevel.reporter);
									}
								}
								else
								{
									isEqual = baseXPathORJSONArray[2].toString().equals(actualXPathORJSONValue.get(0));
									sAssert.assertTrue(isEqual, " Query: EQUALS,  Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0) +"\", Base value not equals actual value.");
									
									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: EQUALS,  Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0)+"\"",EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0]+", Query: EQUALS,  Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0) +"\", Base value not equals actual value.",EllieMaeLogLevel.reporter);
									}
								}
								
								if(baseXPathORJSONArray.length >=4 && !baseXPathORJSONArray[3].trim().isEmpty())
								{
									isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
									sAssert.assertTrue(isEqual," Query: EQUALS,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize+", Base value count not equals to actual value count.");
									
									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: EQUALS,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize,EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + " Query: EQUALS,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize+", Base value count not equals to actual value count.",EllieMaeLogLevel.reporter);
									}
								}					
								break;
			
							case CONTAINS:
								resultantMap = getStringOccuranceData(actualXPathORJSONValue, baseXPathORJSONArray, true);
								
								Validate.notNull(resultantMap);
								Validate.notEmpty(resultantMap);
								
								isEqual = (Boolean) resultantMap.get(HAS_CONTENT);
								actualListSize = (int) resultantMap.get(OCCURANCE_COUNT);
								
								sAssert.assertTrue(isEqual, " Query: CONTAINS, Base Value: \""+baseXPathORJSONArray[2]+",  Actual Value: \""+ actualXPathORJSONValue.toString() + ", Base value not contained in actual value.");

								if(isEqual) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: CONTAINS,  Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.toString() +"\"",EllieMaeLogLevel.reporter);
								}
								else {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: CONTAINS, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.toString() + "\", Base value not contained in actual value.",EllieMaeLogLevel.reporter);
								}
								
								if(3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
									for(int i = baseXPathORJSONArray.length-1; 2 < i; i--) {
										if(StringUtils.isNotBlank(baseXPathORJSONArray[i]) && !baseXPathORJSONArray[i].contains(TOKEN_VALIDATION_DESCRIPTION) && !baseXPathORJSONArray[i].contains(TOKEN_JIRAID)) {
											expectedCount = (null == baseXPathORJSONArray[i])? baseXPathORJSONArray[i]: baseXPathORJSONArray[i].trim();
											break;
										}
									}
									
									isEqual = expectedCount.equals(Integer.toString(actualListSize));
									sAssert.assertTrue(isEqual, " Query: CONTAINS, Base Count: "+expectedCount+", Actual Count:"+ actualListSize + ", Base count value not matching actual count value.");
									
									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: CONTAINS,  Base Count: "+expectedCount+", Actual Count: "+ actualListSize,EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: CONTAINS,  Base Count: "+expectedCount+", Actual Count:"+ actualListSize + ", Base count value not matching actual count value.",EllieMaeLogLevel.reporter);
									}
								}
										
								break;	 
			
							case DOESNOTCONTAIN:
								resultantMap = getStringOccuranceData(actualXPathORJSONValue, baseXPathORJSONArray, false);
								
								Validate.notNull(resultantMap);
								Validate.notEmpty(resultantMap);
								
								isEqual = (Boolean) resultantMap.get(HAS_CONTENT);
								
								sAssert.assertFalse(isEqual, " Query: DOESNOTCONTAIN, Base Value: \""+baseXPathORJSONArray[2]+"\",  Actual Value: \""+ actualXPathORJSONValue.toString() + "\", Base value is present in actual value which is not as Expected.");

								if(!isEqual) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: DOESNOTCONTAIN, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.toString() +"\", Base value is not present in actual value as Expected",EllieMaeLogLevel.reporter);
								}
								else {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: DOESNOTCONTAIN, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.toString() + "\", Base value is present in actual value which is not as Expected.",EllieMaeLogLevel.reporter);
								}
								break;
			
							case ISEMPTY:
								Boolean checkEmptyFlag = false;
			
								//List<String> actualList = new ArrayList<String>();	
								StringBuilder actualValue = new StringBuilder();
								for(Object val : actualXPathORJSONValue){
									//actualList.add(val.toString());
									actualValue.append(val);
								}
								
								if(actualValue.toString().trim().isEmpty())
									checkEmptyFlag=true;
								
			//					for(String l : actualXPathORJSONValue){
			//						checkEmptyFlag = (l.isEmpty());
			//						if(checkEmptyFlag)
			//							break;
			//					}					
										
										isEqual = Boolean.valueOf(baseXPathORJSONArray[2]).equals(checkEmptyFlag);
										sAssert.assertTrue(isEqual, " Query: ISEMPTY, Base Value: " + baseXPathORJSONArray[2] + ",  Actual Value: " + checkEmptyFlag + ", Base value is not matching actual value.");					
			
										if(isEqual) {
											EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: ISEMPTY, Base Value: "+baseXPathORJSONArray[2]+", Actual Value: "+ checkEmptyFlag,EllieMaeLogLevel.reporter);
										}
										else {
											EllieMaeLog.log(_log, validationTypeKey+" is: "  + baseXPathORJSONArray[0] + ", Query: ISEMPTY, Base Value: "+ baseXPathORJSONArray[2] + ", Actual Value: " + checkEmptyFlag + ", Base value is not matching actual value.",EllieMaeLogLevel.reporter);
										}
								break;
			
							case GREATERTHAN:
								isEqual = Double.parseDouble(actualXPathORJSONValue.get(0)) > Double.parseDouble(baseXPathORJSONArray[2]);
								sAssert.assertTrue(isEqual, " Query: GREATERTHAN,  Base Value: \""+ baseXPathORJSONArray[2] +"\", Actual Value: \""+ actualXPathORJSONValue.get(0)+"\", Actual value is not greater than base value.");
								
								if(isEqual) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: GREATERTHAN, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0)+ "\"",EllieMaeLogLevel.reporter);
								}
								else {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: GREATERTHAN, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0) + "\", Actual value is not greater than base value.",EllieMaeLogLevel.reporter);
								}
										
								if(3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty())
								{
									isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
									sAssert.assertTrue(isEqual, " Query: GREATERTHAN,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count:"+ actualListSize+" Base count value is not matching actual count value.");
			
									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: GREATERTHAN, Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize,EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: GREATERTHAN, Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize+" Base count value is not matching actual count value.",EllieMaeLogLevel.reporter);
									}
								}
								break;
			
							case LESSTHAN:
								isEqual = Double.parseDouble(actualXPathORJSONValue.get(0)) < Double.parseDouble(baseXPathORJSONArray[2]);
								sAssert.assertTrue(isEqual, " Query: LESSTHAN,  Base Value: \""+baseXPathORJSONArray[2] +"\", Actual Value: \""+ actualXPathORJSONValue.get(0) + "\", Actual value is not less than base value.");
								
								if(isEqual) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: LESSTHAN, Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0)+ "\"",EllieMaeLogLevel.reporter);
								}
								else {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: LESSTHAN,  Base Value: \""+baseXPathORJSONArray[2]+"\", Actual Value: \""+ actualXPathORJSONValue.get(0) + "\", Actual value is not less than base value.",EllieMaeLogLevel.reporter);
								}
										
								if(3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty())			
								{
									isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
									sAssert.assertTrue(isEqual, " Query: LESSTHAN, Base Count: "+baseXPathORJSONArray[3]+", Actual Count:"+ actualListSize+", Base count value is not matching actual count value.");
									

									if(isEqual) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: LESSTHAN,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count: "+ actualListSize,EllieMaeLogLevel.reporter);
									}
									else {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] + ", Query: LESSTHAN,  Base Count: "+baseXPathORJSONArray[3]+", Actual Count:"+ actualListSize+", Base count value is not matching actual count value.",EllieMaeLogLevel.reporter);
									}
								}
								break;
			
							case KEYEXISTS:
								Boolean isKeyExists=actualXPathORJSONValue==null ? false : true;
								sAssert.assertEquals(isKeyExists, Boolean.valueOf(baseXPathORJSONArray[2]), " Query: KEYEXISTS, Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isKeyExists + ", Base value is not matching actual value.");
							    
			                    if(isKeyExists.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: KEYEXISTS, Base Value: "+baseXPathORJSONArray[2]+", Actual Value: "+ isKeyExists,EllieMaeLogLevel.reporter);
								}
								else {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0]  + ", Query: KEYEXISTS,  Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isKeyExists + ", Base value is not matching actual value.",EllieMaeLogLevel.reporter);
								}
								break;
				
							case XMLCHILDNODEEXISTS:
								if(validationTypeKey.equalsIgnoreCase("JSONPath")) 
								{				
									sAssert.assertTrue(false,"XMLCHILDNODEEXISTS supports only for XPath Validation, not for JSONPath Validation");
									EllieMaeLog.log(_log, "XMLCHILDNODEEXISTS supports only for XPath Validation, not for JSONPath Validation",EllieMaeLogLevel.reporter);
								}
								else
								{
									Boolean isChildNodeExists= Integer.parseInt(actualXPathORJSONValue.get(0)) > 1 ? true : false;
									
									sAssert.assertEquals(isChildNodeExists, Boolean.valueOf(baseXPathORJSONArray[2]), " Query: XMLCHILDNODEEXISTS, Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isChildNodeExists + ", Base value is not matching actual value.");
								    
				                    if(isChildNodeExists.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: XMLCHILDNODEEXISTS, Base Value: "+baseXPathORJSONArray[2]+", Actual Value: "+ isChildNodeExists,EllieMaeLogLevel.reporter);
									}
									else {
										
										EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0]  + ", Query: XMLCHILDNODEEXISTS,  Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isChildNodeExists + ", Base value is not matching actual value.",EllieMaeLogLevel.reporter);
									}						
								}
								break;
						
							case ISNULL:
								Boolean isNull = actualXPathORJSONValue == null ? true : false;							
								sAssert.assertEquals(isNull, Boolean.valueOf(baseXPathORJSONArray[2]), " Query: ISNULL, Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isNull + ", Base value is not matching actual value.");
							    
			                    if(isNull.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0] +", Query: ISNULL, Base Value: "+baseXPathORJSONArray[2]+", Actual Value: "+ isNull,EllieMaeLogLevel.reporter);
								}
								else {
									
									EllieMaeLog.log(_log, validationTypeKey+" is: " + baseXPathORJSONArray[0]  + ", Query: ISNULL,  Base Value: "+baseXPathORJSONArray[2]+", Actual Value:"+ isNull + ", Base value is not matching actual value.",EllieMaeLogLevel.reporter);
								}
								break;
								
							default:
								throw new Exception("QUERY keyword '" + validationKeyWordEnum + "'is not supported by the validaiton API.");				
						}
					}
					finally {
						if(null != resultantMap) {
							resultantMap.clear();
							resultantMap = null;
						}
					}
				}
			} catch (Exception e) {
				EllieMaeLog.log(_log, "Exception in doValidation(): "+e.getMessage(),EllieMaeLogLevel.reporter);
				EllieMaeLog.log(_log, "Exception StackTrace: "+e.getStackTrace(),EllieMaeLogLevel.reporter);
				sAssert.assertTrue(false,"Exception in doValidation()"+e.getMessage());
			}
		}

	}

