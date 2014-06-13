/**
 * Դ�������ƣ�IContextProviderFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

import java.util.Map;



/**
 * �������ṩ������
 * @author lvgao
 *
 */
public interface IContextProviderFactory {

	/**
	 * ������Դͳ���ṩ��
	 * @param key       ������ID
	 * @param context   ��Ҫ����
	 * @return
	 */
	public Object createContext(String key,Map<Object, Object> context);
	
}
