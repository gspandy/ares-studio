/**
 * Դ�������ƣ�ExcelSheetStructEntity.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */
package com.hundsun.ares.studio.biz.ui.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Excel sheet ʵ��
 * @author yanwj06282
 *
 */
public class ExcelSheetStructEntity {

	//ģ��������ǰ׺
	private String cnamePrefix = "";
	//ģ��������
	private String sheetCName ;
	//ģ��Ӣ����
	private String sheetEName;
	
	/**
	 * sheet����ţ���0��ʼ��0��ʾ��һ��sheet(�˵�ҳһ������Ϊ0,�����ģ�����ԣ���Ҫ������󣬿�������һ���ܴ��ֵ)
	 * ���Ϊ-1����˳�����ɣ��������⴦��
	 */
	private int sheetIndex = -1;
	
	/**
	 * Ĭ���п�
	 */
	private int defaultColumnWidth = 6000;
	
	/**
	 * �п������鳤�ȱ����menuItems���ȱ���һ�£�����ȡĬ���п�
	 * 
	 */
	private int[] columnWidths;
	
	/**
	 * ÿ��sheet�а���N����Դʵ��
	 * 
	 */
	private List<BizResExcelStructEntity> entityList = new ArrayList<BizResExcelStructEntity>();

	public String getCnamePrefix() {
		return cnamePrefix;
	}

	public void setCnamePrefix(String cnamePrefix) {
		this.cnamePrefix = cnamePrefix;
	}

	public String getSheetCName() {
		return sheetCName;
	}

	public void setSheetCName(String sheetCName) {
		this.sheetCName = sheetCName;
	}

	public String getSheetEName() {
		return sheetEName;
	}

	public void setSheetEName(String sheetEName) {
		this.sheetEName = sheetEName;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public List<BizResExcelStructEntity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<BizResExcelStructEntity> entityList) {
		this.entityList = entityList;
	}

	public int[] getColumnWidths() {
		return columnWidths;
	}

	public void setColumnWidths(int[] columnWidths) {
		this.columnWidths = columnWidths;
	}

	public int getDefaultColumnWidth() {
		return defaultColumnWidth;
	}
	
}
