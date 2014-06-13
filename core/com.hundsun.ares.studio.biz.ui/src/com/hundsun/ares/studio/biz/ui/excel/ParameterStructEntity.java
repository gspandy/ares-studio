/**
 * Դ�������ƣ�ParameterStructEntity.java
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
 * ����ʵ��
 * @author yanwj06282
 *
 */
public class ParameterStructEntity {

	/**
	 * �������ֶΣ���0��ʼ����Ӧ��Ŀ�����е��±�
	 * ����������Ϊ-1�����ʾû�г�����
	 * 
	 */
	private int hyperlinkIndex = 3;
	
	public ParameterStructEntity(List<ParameterItemStructEntity> totoleColumns ,List<String> filterTitles){
		this.totoleColumns = totoleColumns;
		this.filterTitles = filterTitles;
	}
	
	/**
	 * �����������飬��һ���Ǳ���
	 */
	private List<ParameterItemStructEntity> totoleColumns = new ArrayList<ParameterItemStructEntity>();
	
	/**
	 * Excel��ʵ�����ɵı��⣬��������˳���������е�˳��
	 * ����������Ϊ�գ���ȫ����ʾ����
	 */
	private List<String> filterTitles = new ArrayList<String>();

	public List<ParameterItemStructEntity> getTotoleColumns() {
		return totoleColumns;
	}

	public void setTotoleColumns(List<ParameterItemStructEntity> totoleColumns) {
		this.totoleColumns = totoleColumns;
	}

	public List<String> getFilterTitles() {
		return filterTitles;
	}

	public void setFilterTitles(List<String> filterTitles) {
		this.filterTitles = filterTitles;
	}

	public int getHyperlinkIndex() {
		return hyperlinkIndex;
	}

	public void setHyperlinkIndex(int hyperlinkIndex) {
		this.hyperlinkIndex = hyperlinkIndex;
	}
	
}
