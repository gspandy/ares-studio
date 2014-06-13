/**
 * Դ�������ƣ�IDataServiceFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.core.service;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * ���ݷ��񴴽�����
 * @author gongyf
 *
 */
public interface IDataServiceFactory {
	
	/**
	 * ��ָ���Ĺ��̴������ݷ���
	 * @param project
	 * @return
	 */
	IDataService createService(IARESProject project);
}
