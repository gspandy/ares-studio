/**
 * Դ�������ƣ�PropertyHandlerFactoryProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.excel.handler.IPropertyHandlerFactory;

/**
 * 
 * @author sundl
 */
public interface IPropertyHandlerFactoryProvider {

	IPropertyHandlerFactory getFactory(EClass eClass);
}
