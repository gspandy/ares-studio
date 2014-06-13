/**
 * Դ�������ƣ�ComposedSheetHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ISheetParserFactory;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * �ں϶��Factory
 * @author sundl
 *
 */
public class ComposedSheetHandlerFactory implements ISheetParserFactory{

	private List<ISheetParserFactory> factoryList = new ArrayList<ISheetParserFactory>();
	
	public ComposedSheetHandlerFactory(ISheetParserFactory[] factories) {
		this.factoryList.addAll(Arrays.asList(factories));
	}
	
	public void addFactory(ISheetParserFactory factory) {
		factoryList.add(factory);
	}
	
	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		for (ISheetParserFactory factory : factoryList) {
			SheetParser parser = factory.createParser(exlParser, sheet, log);
			if (parser != null)
				return parser;
		}
		return null;
	}

}
