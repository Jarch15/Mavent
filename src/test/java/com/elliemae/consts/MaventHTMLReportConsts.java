package com.elliemae.consts;

public class MaventHTMLReportConsts {
	
	
	// Cannot Increase Category
			public static final String CannotIncLabel="//td[contains(text(),'Charges That Cannot Increase')]";
			public static final String CannotIncTableRow = "//*[@id='layerTILARespaReview']//tr//tr[2]//td[1]//table[1]//tbody[1]/tr";
			public static final String CannotIncTableColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[2]//td[1]//table[1]//tbody[1]/tr/td";
			public static final String CannotIncCustomColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[2]//td[1]//table[1]//tbody[1]";
			
			
	// Cannot Decrease Category
			
			public static final String CannotDecLabel="//td[contains(text(),'Charges That Cannot Decrease')]";
			public static final String CannotDecTableRow = "//*[@id='layerTILARespaReview']//tr//tr[5]//td[1]//table[1]//tbody[1]/tr";
			public static final String CannotDecTableColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[5]//td[1]//table[1]//tbody[1]/tr/td";
			
			public static final String CannotDecCustomColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[5]//td[1]//table[1]//tbody[1]";
			
			
	//Cannot Decrease by 10%
			
			public static final String CannotIncBy10Label="//td[contains(text(),'Charges That Can Increase By 10%')]";
			public static final String CannotIncBy10TableRow = "//*[@id='layerTILARespaReview']//tr//tr[7]//td[1]//table[1]//tbody[1]/tr";
			public static final String CannotIncBy10TableColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[7]//td[1]//table[1]//tbody[1]/tr/td";
			public static final String CannotIncBy10CustomColumn = "//body//tr[@id='layerTILARespaReview']//tr//tr[7]//td[1]//table[1]//tbody[1]";
			
}
