/**
 * Դ�������ƣ�IOracleTrigger.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

/**
 * @author gongyf
 *
 */
public interface IOracleTrigger {
	String getName();
	String getChineseName();
	String getDescription();
	
	String getSQL();
}
