/**
 * Դ�������ƣ�IView.java
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
public interface IView {
	String getName();
	String getChineseName();
	String getDescription();
	
	String getSQL();
}
