/**
 * Դ�������ƣ�ExcelParser.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * ����Excel
 * @author sundl
 *
 */
public class ExcelParser {
	
	private static final Logger logger = Logger.getLogger(ExcelParser.class);
	
	public Log log;
	public File file;
	public ISheetParserFactory factory;
	
	/** ģ����Ӣ�Ķ��ձ�ʹ��BiMap,���Է������ */
	public BiMap<String, String> moduleNameMap = HashBiMap.create();
	/** �洢������� */
	public Multimap<Module, Resource> resources = ArrayListMultimap.create();
	
	/** ��������չ�ֶ� */
	public Map<String, Object> context = new HashMap<String, Object>();
	
	/** ����� --> �޸ļ�¼ */
	public Multimap<String, RevisionHistory> histories = ArrayListMultimap.create();
	
	/** ������ɺ���Ҫ���еĲ���
	 *  Ŀǰ����������ƽ���XML��ǩ��������չ���ԵĴ���
	 */
	public PostParseOperation postParseOperation = null; // ����������Operationһ����operation�����󴫹�����
//	public MetadataModifyOperation<StandardField> stdModifyOperation = null;
	
	public ExcelParser(File file, Log log) {
		this.file = file;
		this.log = log;
	}
	
	public void parse() {
		if (factory == null) {
			// �����ϲ�Ӧ�÷����������
			log.error("�ڲ�����ISheetParserFactoryΪ��", null);
			return;
		}
		
		if (file == null || !file.exists()) {
			log.error(String.format("�Ҳ����ļ���%s", file.getAbsolutePath()), null);
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream(this.file);
			HSSFWorkbook wb = new HSSFWorkbook(is);
			int sheetCount = wb.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				SheetParser parser = factory.createParser(this, sheet, this.log);
				if (parser == null) {
					log.info(String.format("�ڲ�����,Sheetҳ��%s��û���ҵ���Ӧ�Ľ��������д���!", sheet.getSheetName()), null);
					continue;
				}
				try {
					parser.evaluator = wb.getCreationHelper().createFormulaEvaluator();
					parser.parse(sheet);
				} catch (Exception e) {
					log.error(String.format("����Sheetҳ��%s����ʱ�����ڲ��쳣. %s", sheet.getSheetName(), e.getMessage()), null);
					logger.error(String.format("����Sheetҳ��%s����ʱ�����ڲ��쳣.", sheet.getSheetName()), e);
				}
			}
		} catch (FileNotFoundException e) {
			log.error(String.format("�Ҳ����ļ���%s", file.getAbsolutePath()), null);
		} catch (IOException e) {
			log.error(String.format("��ȡ�ļ�%s����%s", file.getAbsolutePath(), e.getMessage()), null);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
