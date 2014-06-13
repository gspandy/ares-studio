/**
 * Դ�������ƣ�IOrcaleSequence.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

/**
 * @author wangxh
 *
 */
public interface IOracleSequence {
	String getName();
	String getChineseName();
	String getDescription();
	
	
	String getTableName();
	String getStart();
	String getIncrement();
	String getMinValue();
	String getMaxValue();
	boolean isCycle();
	boolean isUseCache();
	String getCache();
}
