/**
 * Դ�������ƣ�IPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

/**
 * ��������
 * @author sundl
 *
 */
public interface IPropertyHandler {
	
	void setValue(Object obj, String value);
	String getValue(Object obj);
}
