package com.elliemae.testcases.Sample;

import java.awt.AWTException;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.elliemae.core.Base.EllieMaeApplicationBase;
import com.elliemae.core.asserts.Assert;
import com.elliemae.pageobject.InqueueAndSavequeuePage;
import com.elliemae.pageobject.MaventPage;

public class MaventTest extends EllieMaeApplicationBase {
	
	@Test(dataProvider="get-test-data-method")
	
	
	public void smokeTest(HashMap<String,String> testData){
		
		MaventPage MaventPageObj = new MaventPage(driver);
		
		
		MaventPageObj.gotoPage();
		
		MaventPageObj.Login();
		
		String FootNote;
		
		FootNote = MaventPageObj.ValidateFootNote();
		
		String Part1 = FootNote.substring(0, 24);
		String Part2 = FootNote.substring(25, 44);
		String Part3 = FootNote.substring(45, 67);
		System.out.println(Part1);
		System.out.println(Part2);
		System.out.println(Part3);
		
		/*Assert.assertEquals(Part1, "©2001 - 2017 Mavent Inc.", "FootNote does not Match");
		Assert.assertEquals(Part2, "An EllieMae Company", "FootNote does not Match");
		Assert.assertEquals(Part3, "2comply Expert System®", "FootNote does not Match");*/
	}
	
	@Test(dataProvider="get-test-data-method")
	
	public void InqueueAndSavequeueTest(HashMap<String,String> testData) throws AWTException, InterruptedException{
		
		InqueueAndSavequeuePage InqueueAndSavequeuePageobj = new InqueueAndSavequeuePage(driver);
		
		InqueueAndSavequeuePageobj.gotoPage();
		InqueueAndSavequeuePageobj.Login();
		
		
	}
	
	
}