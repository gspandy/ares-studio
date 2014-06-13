/**
 * Դ�������ƣ�ResourceListSheetParserFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.factories;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.core.excel.BlockTypes;
import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ISheetParserFactory;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.excel.handler.ResourceListHandler;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * ���Դ���������Դ�б�Sheetҳ�Ľ������Ĺ���
 * @author sundl
 *
 */
public class ResourceListSheetParserFactory implements ISheetParserFactory{
	
	public static final ResourceListSheetParserFactory INSTANCE = new ResourceListSheetParserFactory();

	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		if (sheet.getSheetName().equals("�߼������б�")
				|| sheet.getSheetName().equals("���ܽӿ��б�")
				|| sheet.getSheetName().equals("ҵ������б�")) {
			SheetParser parser = new SheetParser();
			parser.areaTags.add("ģ����");
			parser.areaTags.add("ģ��������") ;
			parser.exlParser = exlParser;
			parser.blocks.put("ģ����", BlockTypes.TABLE);
			parser.blocks.put("ģ��������", BlockTypes.TABLE);
			ResourceListHandler handler = new ResourceListHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			return parser;
		}
		return null;
	}

}
