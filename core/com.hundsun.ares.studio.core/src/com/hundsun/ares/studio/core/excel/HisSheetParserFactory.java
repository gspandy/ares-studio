/**
 * Դ�������ƣ�HisSheetParserFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.core.util.log.Log;

/**
 * 
 * @author sundl
 */
public class HisSheetParserFactory implements ISheetParserFactory{
	
	public static final HisSheetParserFactory INSTANCE = new HisSheetParserFactory();

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.ISheetParserFactory#createParser(com.hundsun.ares.studio.core.excel.ExcelParser, org.apache.poi.hssf.usermodel.HSSFSheet, com.hundsun.ares.studio.core.util.log.Log)
	 */
	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		if (sheet.getSheetName().equals("�汾ҳ")) {
			SheetParser parser = new SheetParser();
			parser.exlParser = exlParser;
			parser.log = log;
			parser.areaTags.add("�޸İ汾");
			parser.blocks.put("�޸İ汾", BlockTypes.TABLE);
			parser.startRow = 11;
			
			ISheetHandler handler = new HisSheetHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			
			return parser;
		}
		return null;
	}

}
