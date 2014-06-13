/**
 * Դ�������ƣ�PropertyHandlerFactoryDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.internal.core.registry;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.model.extend.IPropertyHandlerFactoryProvider;
import com.hundsun.ares.studio.core.registry.ICommonExtensionConstants;

/**
 * 
 * @author dollyn
 */
public class PropertyHandlerFactoryDescriptor extends CommonDescriptor{

	Logger logger = Logger.getLogger(PropertyHandlerFactoryDescriptor.class);
	/**
	 * @param e
	 */
	public PropertyHandlerFactoryDescriptor(IConfigurationElement e) {
		super(e);
	}
	
	public IPropertyHandlerFactoryProvider createProvider() {
		try {
			return (IPropertyHandlerFactoryProvider)configElement.createExecutableExtension(ICommonExtensionConstants.CLASS);
		} catch (CoreException e) {
			logger.error(e);
		}
		return null;
	}

}
