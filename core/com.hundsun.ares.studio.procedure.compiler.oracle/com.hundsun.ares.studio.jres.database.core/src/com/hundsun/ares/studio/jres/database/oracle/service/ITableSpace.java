/**
 * Դ�������ƣ�IOracleTableSpace.java
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
public interface ITableSpace {

	String getName();
	String getChineseName();
	String getUser();
	String getFile();
	String getSize();
	String getDescription();
}
