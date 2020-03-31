package com.elliemae.testcases.maintenance;



import java.util.HashMap;


import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.Utils.CommonUtility;



public class Resetecsautopass  extends EllieMaeApplicationBase
{
	
	HashMap<String, String> dataBeanMap = new HashMap<>();
	public static Logger _log = Logger.getLogger(OFACPostLaunchTest.class);
	
	@Test(dataProvider = "get-test-data-method")
	
	public void resetPassword(HashMap<String,String> testData){
		
		 String Password = CommonUtility.encryptData("Elliemae2020");
         
         System.out.println("New password is: " + Password + " Please update variable EMNETWORKUSERPASSWORD under FrameworkConsts.java file ");
		

	}
}
