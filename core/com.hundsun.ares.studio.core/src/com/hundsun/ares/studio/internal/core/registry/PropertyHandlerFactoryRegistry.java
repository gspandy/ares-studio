/**
 * Դ�������ƣ�PropertyHandlerFactoryRegistry.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.internal.core.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.registry.CommonMapRegistry;

/**
 * 
 * @author dollyn
 */
public class PropertyHandlerFactoryRegistry extends CommonMapRegistry<PropertyHandlerFactoryDescriptor>{

	public static PropertyHandlerFactoryRegistry INSTANCE = new PropertyHandlerFactoryRegistry();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.registry.CommonMapRegistry#getExtensionPointId()
	 */
	@Override
	public String getExtensionPointId() {
		return "propertyHandlerFactory";
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.registry.CommonMapRegistry#handleConfigElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected void handleConfigElement(IConfigurationElement element) {
		PropertyHandlerFactoryDescriptor desc = new PropertyHandlerFactoryDescriptor(element);
		map.put(desc.getId(), desc);
	}

}
