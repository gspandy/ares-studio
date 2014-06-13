/**
 * Դ�������ƣ�Test.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author sundl
 *
 */
public class Test {

	public static void main(String[] args) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("test.xls")));
			workbook.getNumberOfSheets();
			HSSFFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			HSSFSheet sheet = workbook.getSheet("Sheet1");
			HSSFCell A1 = sheet.getRow(0).getCell(0);
			HSSFCell A2 = sheet.getRow(1).getCell(0);
			HSSFCell A3 = sheet.getRow(2).getCell(0);
			
			System.out.println(String.format("%s, %s, %s", 
									A1.getNumericCellValue(), 
									A2.getNumericCellValue(), 
									A3.getNumericCellValue()));
			
			A1.setCellValue(100000);
			evaluator.evaluateInCell(A3);
			System.out.println(String.format("%s, %s, %s", 
									A1.getNumericCellValue(), 
									A2.getNumericCellValue(), 
									A3.getNumericCellValue()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
