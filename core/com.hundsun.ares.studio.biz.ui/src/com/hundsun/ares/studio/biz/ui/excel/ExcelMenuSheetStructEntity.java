/**
 * Դ�������ƣ�ExcelMenuSheetStructEntity.java
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
 * �˵�ҳʵ��
 * @author yanwj06282
 *
 */
public class ExcelMenuSheetStructEntity {
	
	private String sheetName;
	/**
	 * sheet����ţ���0��ʼ��0��ʾ��һ��sheet(�˵�ҳһ������Ϊ0,�����ģ�����ԣ���Ҫ������󣬿�������һ���ܴ��ֵ)
	 * ���Ϊ-1����˳�����ɣ��������⴦��
	 */
	private int sheetIndex  = 0;
	
	/**
	 * �������ֶΣ���0��ʼ����Ӧ��Ŀ�����е��±�
	 * ����������Ϊ-1�����ʾû�г�����
	 * 
	 */
	private int hyperlinkIndex = 3;
	
	/**
	 * Ĭ���п�
	 */
	private int defaultColumnWidth = 6000;
	
	/**
	 * �п�
	 * ��������п��menuItems�ڵ���Ŀ�����Ӧ��������Ȳ�һ������ȡĬ�ϳ���
	 * 
	 */
	private int[] columnWidths;
	
	/**
	 * �˵���Ŀ
	 */
	private List<ExcelMenuItemEntity> menuItems = new ArrayList<ExcelMenuItemEntity>();

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public List<ExcelMenuItemEntity> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<ExcelMenuItemEntity> menuItems) {
		this.menuItems = menuItems;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getHyperlinkIndex() {
		return hyperlinkIndex;
	}

	public void setHyperlinkIndex(int hyperlinkIndex) {
		this.hyperlinkIndex = hyperlinkIndex;
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
