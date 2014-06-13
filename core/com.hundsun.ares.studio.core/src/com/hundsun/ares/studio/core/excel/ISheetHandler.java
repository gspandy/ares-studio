/**
 * Դ�������ƣ�ExcelHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import org.apache.poi.ss.usermodel.Sheet;

import com.hundsun.ares.studio.core.util.log.Log;


/**
 * 
 * @author sundl
 *
 */
public interface ISheetHandler {
	
	void init(SheetParser sheetParser, Log log);

	void startSheet(Sheet sheet);
	void startArea(String startTag);
	void startBlock(String startTag, int type);
	
	/** key-value������key-value�� */
	void keyValue(String key, String value);
	/** ��������еı�ͷ */
	void header(String[] header);
	/** ��������еı� */
	void row(String[] row);
	
	void endBlock();
	void endArea();
	void endSheet();
}
