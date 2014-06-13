/**
 * Դ�������ƣ�BizSheetParserFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.factories;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.biz.excel.handlers.ObjSheetHandler;
import com.hundsun.ares.studio.core.excel.BlockTypes;
import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ISheetHandler;
import com.hundsun.ares.studio.core.excel.ISheetParserFactory;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * @author sundl
 *
 */
public class BizSheetParserFactory implements ISheetParserFactory{
	
	public static BizSheetParserFactory INSTANCE = new BizSheetParserFactory();

	public static final Map<String, Integer> BLOCK_TAGS = new HashMap<String, Integer>();
	
	static  {
		// ����ţ����ܺŶ�����key-value block�Ŀ�ʼ
		BLOCK_TAGS.put("�����", BlockTypes.KEY_VALUE);
		BLOCK_TAGS.put("���ܺ�", BlockTypes.KEY_VALUE);
		BLOCK_TAGS.put("������", BlockTypes.KEY_VALUE);
		
		BLOCK_TAGS.put("�������", BlockTypes.TABLE);
		BLOCK_TAGS.put("�������", BlockTypes.TABLE);
		BLOCK_TAGS.put("����", BlockTypes.TABLE);
		BLOCK_TAGS.put("ҵ��������", BlockTypes.TEXT);
		BLOCK_TAGS.put("�ڲ�����", BlockTypes.TEXT);
		BLOCK_TAGS.put("���̱���", BlockTypes.TEXT);

		//2014-01-28 modified by zhuyf ���ҵ��˵�����������ӿڣ�ʱ�ĵ����д˸�ʽ��Ϣ��
		BLOCK_TAGS.put("ҵ��˵��", BlockTypes.TEXT);
		BLOCK_TAGS.put("˵��", BlockTypes.KEY_VALUE);
		BLOCK_TAGS.put("����˵��", BlockTypes.TABLE);
		//2014-04-17 modified by zhuyf ��Ӳ�����ʾ��������ڲ�Ʒ����ϵͳ06��۰����д���Ϣ����ȥ������
		BLOCK_TAGS.put("������ʾ", BlockTypes.TEXT);
		BLOCK_TAGS.put("�޸ļ�¼", BlockTypes.TEXT);
		// ��������Ƿ�������ܼ�����������������У����ԣ�������Ϊ������block����
		//TASK #9511 ��������Ϊ������������
		BLOCK_TAGS.put("��������", BlockTypes.KEY_VALUE);
		BLOCK_TAGS.put("��������", BlockTypes.KEY_VALUE);
		//�����Կ��� ��ɾ��ԭ��������
		BLOCK_TAGS.put("�����Ƿ�����", BlockTypes.KEY_VALUE);
		BLOCK_TAGS.put("����Ƿ�����", BlockTypes.KEY_VALUE);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetParserFactory#createParser(com.hundsun.ares.studio.biz.excel.ExcelParser, org.apache.poi.hssf.usermodel.HSSFSheet, com.hundsun.ares.studio.biz.excel.Log)
	 */
	@Override
	public SheetParser createParser(ExcelParser exlParser, HSSFSheet sheet, Log log) {
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		if (sheetName.equals("ҵ�������") 
				|| StringUtils.equals(sheetName, "����")
				|| StringUtils.startsWith(sheetName, "����-") 
				|| StringUtils.startsWith(sheetName, "ҵ�����-")) {
			SheetParser parser = new SheetParser();
			parser.exlParser = exlParser;
			parser.log = log;
			parser.areaTags.add("������");
			parser.blocks.putAll(BizSheetParserFactory.BLOCK_TAGS);
			parser.blocks.put("��������", BlockTypes.TABLE);
			
			ISheetHandler handler = new ObjSheetHandler();
			handler.init(parser, log);
			parser.handlers.add(handler);
			
			return parser;
		}
		return null;
	}
	
	
}
