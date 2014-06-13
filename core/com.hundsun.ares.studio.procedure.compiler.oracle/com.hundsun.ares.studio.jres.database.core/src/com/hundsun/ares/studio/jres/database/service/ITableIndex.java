/**
 * Դ�������ƣ�ITableIndex.java
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
public interface ITableIndex {
	String getName();
	boolean isUnique();
	
	List<? extends ITableIndexColumn> getColumnList();
	ITableIndexColumn getColum(String name);
}
