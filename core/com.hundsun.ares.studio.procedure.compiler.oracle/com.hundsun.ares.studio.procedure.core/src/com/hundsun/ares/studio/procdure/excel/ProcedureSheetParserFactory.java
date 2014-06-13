package com.hundsun.ares.studio.procdure.excel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.biz.excel.factories.BizSheetParserFactory;
import com.hundsun.ares.studio.core.excel.BlockTypes;
import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ISheetHandler;
import com.hundsun.ares.studio.core.excel.ISheetParserFactory;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.util.log.Log;

public class ProcedureSheetParserFactory implements ISheetParserFactory {
	
	public static final ProcedureSheetParserFactory INSTANCE = new ProcedureSheetParserFactory();

	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		SheetParser parser = new SheetParser();
		parser.exlParser = exlParser;
		parser.log = log;
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		if (sheetName.startsWith("ԭ�ӹ���-")) {
			parser.areaTags.add("�����");
			parser.blocks.putAll(BizSheetParserFactory.BLOCK_TAGS);
			//--����֤ȯ����
			parser.blocks.put("ǰ�ô���", BlockTypes.TEXT);
			parser.blocks.put("���ô���", BlockTypes.TEXT);
			//
			ISheetHandler handler = new ProcedureSheetHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			return parser;
		}
		
		return null;
	}

}
