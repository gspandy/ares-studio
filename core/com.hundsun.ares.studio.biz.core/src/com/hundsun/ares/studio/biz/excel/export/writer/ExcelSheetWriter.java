/**
 * Դ�������ƣ�SheetWriter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.export.writer;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author sundl
 *
 */
public abstract class ExcelSheetWriter implements Writer{

	
	protected Sheet sheet;
	/** �ӵڼ��п�ʼ */
	protected int startRow;
	/** ռ���˼���, ���ֻ�ڵ�����write()������������� */
	protected int rows;
	
	protected ExcelWriter excelWriter;
	
	public ExcelSheetWriter(ExcelWriter excelWriter, Sheet sheet, int startRow) {
		this.sheet = sheet;
		this.startRow = startRow;
		this.excelWriter = excelWriter;
	}
	
	protected CellStyle getLabelStyle() {
		return excelWriter.getLabelStyle();
	}
	
	protected CellStyle getTextStyle() {
		return excelWriter.getTextStyle();
	}
	
	protected CellStyle getBackgroundStyle() {
		return excelWriter.getBackgroundStyle();
	}
	
}
