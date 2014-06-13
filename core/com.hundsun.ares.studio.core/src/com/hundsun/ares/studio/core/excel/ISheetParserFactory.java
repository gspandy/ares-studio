/**
 * Դ�������ƣ�IExcelHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.hundsun.ares.studio.core.util.log.Log;

/**
 * @author sundl
 *
 */
public interface ISheetParserFactory {

	/**
	 * @param sheet
	 * @return
	 */
	SheetParser createParser(ExcelParser parser, HSSFSheet sheet, Log log);
	
}
