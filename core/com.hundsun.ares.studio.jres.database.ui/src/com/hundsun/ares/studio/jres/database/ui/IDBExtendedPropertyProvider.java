/**
 * Դ�������ƣ�IDBExtendedPropertyProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.ui;

/**
 * ���ݿ���չ�����ṩ����������չ�������Ϣ�����ֶΡ�����������ͼ������Ϣ
 * @author gongyf
 *
 */
public interface IDBExtendedPropertyProvider {
	IFormExtendedPropertyDecription[] getTablePropertyDescriptions();
	IFormExtendedPropertyDecription[] getViewPropertyDescriptions();
	IColumnViewerExtendedPropertyDecription[] getIndexPropertyDescriptions();
	IColumnViewerExtendedPropertyDecription[] getColumnPropertyDescriptions();
}
