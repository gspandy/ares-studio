/**
 * Դ�������ƣ�ExportExcelEntity.java
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
 * ����Excel��׼��ʽ
 * 
 * @author yanwj06282
 *
 */
public class ExportExcelEntity {
	/**
	 * �˵�ҳ���飬��������
	 */
	private List<ExcelMenuSheetStructEntity> menuList = new ArrayList<ExcelMenuSheetStructEntity>();
	
	/**
	 * ��Դҳ���飬�����ڲ˵�ҳ����
	 */
	private List<ExcelSheetStructEntity> sheetList = new ArrayList<ExcelSheetStructEntity>();

	
	public List<ExcelMenuSheetStructEntity> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<ExcelMenuSheetStructEntity> menuList) {
		this.menuList = menuList;
	}

	public List<ExcelSheetStructEntity> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<ExcelSheetStructEntity> sheetList) {
		this.sheetList = sheetList;
	}
	
}
