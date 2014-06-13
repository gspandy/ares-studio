/**
 * Դ�������ƣ�IEMFExtendPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * ����EMF Feature����չ����
 * @author sundl
 */
public interface IEMFExtendPropertyDescriptor extends IBasicExtendPropertyDescriptor {
	
	String getKey();
	
	/**
	 * �༭������
	 * @return
	 */
	EStructuralFeature getStructuralFeature();
	
}
