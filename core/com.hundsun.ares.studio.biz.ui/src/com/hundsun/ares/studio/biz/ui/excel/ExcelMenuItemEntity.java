/**
 * Դ�������ƣ�ExcelMenuItemEntity.java
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
 * @author yanwj06282
 *
 */
public class ExcelMenuItemEntity {

	/**
	 * @see BizResExcelStructEntity#hyperlinkKey
	 * ���Ҫʵ�ֳ����ӣ����������Ա���һ��
	 */
	private String hyprelinkKey;
	
	/**
	 * �˵�ҳ�е���Ŀ
	 */
	private List<String> item = new ArrayList<String>();
	
	public ExcelMenuItemEntity() {
	}

	public ExcelMenuItemEntity(String hyprelinkKey ,List<String> item) {
		this.hyprelinkKey = hyprelinkKey;
		this.item = item;
	}

	public String getHyprelinkKey() {
		return hyprelinkKey;
	}

	public void setHyprelinkKey(String hyprelinkKey) {
		this.hyprelinkKey = hyprelinkKey;
	}

	public List<String> getItem() {
		return item;
	}

	public void setItem(List<String> item) {
		this.item = item;
	}
	
}
