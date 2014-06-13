/**
 * Դ�������ƣ�IExtendPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import com.hundsun.ares.studio.core.model.ExtensibleModel;


/**
 * ��չ��������
 * @author sundl
 */
public interface IBasicExtendPropertyDescriptor {

	/**
	 * <ul>
	 * <li>������Ϣ���ַ�������ʾ�ã� </li>
	 * <li>�������ͬһ�������µ�������ʾ��ʱ�����ʾ��ͬһ��������</li>
	 * <li>������ﲻ�ṩ������Ϣ(�մ�����null)���ͻᰴ���ṩ���Descriptor��EditingSupport���з�����ʾ</li>
	 * </ul>
	 * @return
	 */
	String getCategory();
	
	/**
	 * ��ʾ�����ƣ�������������������ͷ
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ������Ϣ
	 * @return
	 */
	String getDescription();
	
	/**
	 * getter, setter�����ַ�������ʽ��ͨ�û���ʵ������Ҫ������ʵ�ֵ�ʱ���������ת��
	 * @return
	 */
	String getValue(ExtensibleModel model);
	void setValue(ExtensibleModel model, String value);
	
}
