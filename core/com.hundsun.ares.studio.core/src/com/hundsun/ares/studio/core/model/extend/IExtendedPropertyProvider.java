/**
 * Դ�������ƣ�IExtendPropertyProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * ��չ�����ṩ��.��չ���ԵĽṹһ���������ģ�
 * һ��Provider��Ӧ�ṩһ��EObject����Data2���map�У����EObject��һЩ���Ծ���Ϊ�������չ����
 * @author sundl
 */
public interface IExtendedPropertyProvider {
	
	/**
	 * �Ƿ�����
	 * @param element ����Ϊnull
	 * @param clazz
	 * @return
	 */
	boolean isEnabled(IARESElement element, EClass clazz);

	/**
	 * ��չ��Ӧ�Ķ�����Data2���Map�е�key
	 * @return
	 */
	String getKey();
	
	/**
	 * ����һ�����ڱ༭�Ķ���������󽫴洢��{@link ExtensibleModel#getData2()}��map��
	 * @return
	 */
	EObject createMapValueObject();
	
	/**
	 * �����ṩ��Щ����
	 * @return
	 */
	IBasicExtendPropertyDescriptor[] getExtendProperties();
	
}
