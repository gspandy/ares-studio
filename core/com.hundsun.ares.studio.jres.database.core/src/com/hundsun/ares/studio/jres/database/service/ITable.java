/**
 * Դ�������ƣ�ITable.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.service;

import java.util.List;

/**
 * @author gongyf
 *
 */
public interface ITable {
	
	String getName();
	String getChineseName();
	String getDescription();
	
	List<? extends ITableColumn> getColumnList();
	ITableColumn getColum(String name);
	
	List<? extends ITableIndex> getIndexList();
	ITableIndex getIndex(String name);
}
