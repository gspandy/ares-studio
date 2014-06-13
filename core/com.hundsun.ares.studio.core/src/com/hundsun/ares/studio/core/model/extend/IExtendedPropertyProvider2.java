/**
 * Դ�������ƣ�IExtendPropertyProvider2.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * ͨ�������ṩ���ʵ��Provider
 * @author sundl
 */
public interface IExtendedPropertyProvider2 {

	/**
	 * �����ṩ��Element, EClass�������������չ�����б�
	 * @param element  ����Ϊnull, Ϊnullʱ������δ������ɾ������Ƽ���������
	 * @param clazz Host Eclass
	 * @return
	 */
	IBasicExtendPropertyDescriptor[] getExtendProperties(IARESElement element, EClass clazz);
}
