package com.hundsun.ares.studio.atom.excel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.biz.excel.factories.BizSheetParserFactory;
import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ISheetHandler;
import com.hundsun.ares.studio.core.excel.ISheetParserFactory;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.util.log.Log;

public class AtomServiceSheetParserFactory implements ISheetParserFactory {
	
	public static final AtomServiceSheetParserFactory INSTANCE = new AtomServiceSheetParserFactory();

	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		SheetParser parser = new SheetParser();
		parser.exlParser = exlParser;
		parser.log = log;

		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		if (sheetName.startsWith("ԭ�ӷ���-")) {
			parser.areaTags.add("�����");
			parser.blocks.putAll(BizSheetParserFactory.BLOCK_TAGS);
			ISheetHandler handler = new AtomServiceSheetHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			return parser;
		} else if (sheetName.startsWith("ԭ�Ӻ���-")) {
			parser.areaTags.add("�����");
			parser.blocks.putAll(BizSheetParserFactory.BLOCK_TAGS);
			ISheetHandler handler = new AtomFunctionSheetHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			return parser;
		}
		
		return null;
	}

}
