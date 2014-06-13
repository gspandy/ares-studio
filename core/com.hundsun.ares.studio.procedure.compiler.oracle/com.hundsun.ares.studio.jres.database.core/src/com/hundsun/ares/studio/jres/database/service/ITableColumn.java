/**
 * Դ�������ƣ�ITableColumn.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.service;

/**
 * @author gongyf
 *
 */
public interface ITableColumn {
	String getName();
	String getChineseName();
	String getType();
	String getDefaultValue();
	
	boolean isPrimaryKey();
	boolean isNullable();
	
	
	
}
