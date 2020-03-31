package com.elliemae.core.Utils;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <b>Name:</b> XMLUtility  
 * <b>Description: </b>This class is used for storing XML related methods which can be used in various classes as a utility function. 
 * 
 * @author <i>Supreet Singh</i>
 */
public class XMLUtilityApplication extends XMLUtility  {

		public static Logger _log = Logger.getLogger(XMLUtilityApplication.class);
		
		   // function to remove specific attribute from XML document
			public static void removeAttribute(Document doc, String elementName, String attributeToRemove) 
			{
				Element element = (Element) doc.getElementsByTagName(elementName).item(0);
				if(element!=null)
				{
					boolean hasAttribute = element.hasAttribute(attributeToRemove);
					if(hasAttribute)
					{
						element.removeAttribute(attributeToRemove);
		
					}
				}
			}		
		
		// function to convert XML document to String
		 public static String convertDocumentToString(Document doc) 
		 {
		     TransformerFactory tf = TransformerFactory.newInstance();
		     Transformer transformer;
		     try 
		     {
		         transformer = tf.newTransformer();
		         StringWriter writer = new StringWriter();
		         transformer.transform(new DOMSource(doc), new StreamResult(writer));
		         String output = writer.getBuffer().toString();
		         return output;
		     }
		     catch (TransformerException e) 
		     {
		         e.printStackTrace();
		     }
		     
		     return null;
		 }
}
