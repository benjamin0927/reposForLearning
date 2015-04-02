package org.benjamin.excel.jxl;
/**
 * http://www.vogella.com/tutorials/JavaExcel/article.html
 * http://www.cnblogs.com/raymond19840709/archive/2008/06/26/1230289.html
 * http://tang.renjihe.com/index.php/archives/24
 * http://www.codepool.biz/excel/how-to-read-and-write-excel-files-in-java.html
 * http://stackoverflow.com/questions/1516144/how-to-read-and-write-excel-file-in-java
 */

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelOperation {
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	 
	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void writeExcel(File file) throws IOException, WriteException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		
		createLabel(excelSheet);
		createContent(excelSheet);
		
		workbook.write();
		workbook.close();
		
	}
	
	public void readExcel(File file) throws IOException{
	    Workbook w;
	    try {
	      w = Workbook.getWorkbook(file);
	      // Get the first sheet
	      Sheet sheet = w.getSheet(0);
	      // Loop over first 10 column and lines

	      for (int j = 0; j < sheet.getColumns(); j++) {
	        for (int i = 0; i < sheet.getRows(); i++) {
	          Cell cell = sheet.getCell(j, i);
	          CellType type = cell.getType();
	          if (type == CellType.LABEL) {
	            System.out.println("I got a label "
	                + cell.getContents());
	          }

	          if (type == CellType.NUMBER) {
	            System.out.println("I got a number "
	                + cell.getContents());
	          }

	        }
	      }
	    } catch (BiffException e) {
	      e.printStackTrace();
	    }
	}
	
	public void createLabel(WritableSheet sheet) throws WriteException{
		// Set create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		
		// Automatically wrap the cells
		times.setWrap(true);
		
	    // create create a bold font with unterlines
	    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
	        UnderlineStyle.SINGLE);
	    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	    // Lets automatically wrap the cells
	    timesBoldUnderline.setWrap(true);
		
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);
		
		// Write a few headers
		addCaption(sheet, 0, 0, "Header 1");
		addCaption(sheet, 1, 0, "This is another header");
	}
	
	private void createContent(WritableSheet sheet) throws RowsExceededException, WriteException {
		
		// Write a few number
		for(int i=1; i< 10; i++) {
			// First column
			addNumber(sheet, 0, i, i + 10);
			// Second column
			addLabel(sheet, 1, i, "Another text " + i);
		}
	}
	
	private void addLabel(WritableSheet sheet, int column, int row, String str) throws RowsExceededException, WriteException {
		Label label;
	    label = new Label(column, row, str, times);
	    sheet.addCell(label);		
	}

	private void addNumber(WritableSheet sheet, int column, int row, Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}
	
	private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
		Label label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	public static void main(String[] args) throws WriteException, IOException {
		ExcelOperation test = new ExcelOperation();
//	    test.setOutputFile("c:/temp/lars.xls");
	    test.writeExcel(new File("D:/lars.xls"));
	    System.out
	        .println("Please check the result file under D:/lars.xls ");
	    
	   test.readExcel(new File("D:/lars.xls"));
	}
}
