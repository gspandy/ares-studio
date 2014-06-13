/**
 * Դ�������ƣ�IExtensibleModelEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * ��չģ�͵ı༭֧�֣���Ҫ��֤����״̬�ģ�ֻ�ᱻʵ����һ�β������ʹ��
 * @author gongyf
 * 
 */
public interface IExtensibleModelEditingSupport {
	
	/**
	 * �ж�����չ�Ƿ�������ָ����Դ�ı༭
	 * 
	 * 
	 * @return boolean
	 */
	boolean isEnable(IARESElement aresElement, EClass eClass);

	/**
	 * ��ȡ����,��������չ���Եķ���
	 * @return
	 */
	String getName();
	
	/**
	 * ��map�е�key
	 * @return
	 */
	String getKey();
	
	/**
	 * ����һ�����ڱ༭�Ķ���������󽫴洢��{@link ExtensibleModel#getData2()}��map��
	 * @return
	 */
	EObject createMapValueObject();
	
	/**
	 * ���ؿ��Ա༭����������
	 * @return
	 */
	IExtensibleModelPropertyDescriptor[] getPropertyDescriptors(IARESElement aresElement, EClass eClass);
}
