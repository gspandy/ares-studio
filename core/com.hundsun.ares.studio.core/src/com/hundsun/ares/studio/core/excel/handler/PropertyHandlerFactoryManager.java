/**
 * Դ�������ƣ�PropertyHandlerFactoryManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.model.extend.IPropertyHandlerFactoryProvider;
import com.hundsun.ares.studio.internal.core.registry.PropertyHandlerFactoryDescriptor;
import com.hundsun.ares.studio.internal.core.registry.PropertyHandlerFactoryRegistry;

/**
 * 
 * @author sundl
 */
public class PropertyHandlerFactoryManager {
	
	private static final Logger LOGGER = Logger.getLogger(PropertyHandlerFactoryManager.class);

	public static IPropertyHandlerFactory getPropertyHandlerFactory(EClass eClass) {
		Collection<PropertyHandlerFactoryDescriptor> descriptors = PropertyHandlerFactoryRegistry.INSTANCE.getDescriptors();
		for (PropertyHandlerFactoryDescriptor desc : descriptors) {
			IPropertyHandlerFactoryProvider provider = desc.createProvider();
			if (provider == null) {
				continue;
			}
			IPropertyHandlerFactory factory = provider.getFactory(eClass);
			if (factory != null) 
				return factory;
			else {
				LOGGER.error("�Ҳ���Eclass: " + eClass +"��PropertyHandlerFactory");
			}
		}
		return null;
	}
	
}
