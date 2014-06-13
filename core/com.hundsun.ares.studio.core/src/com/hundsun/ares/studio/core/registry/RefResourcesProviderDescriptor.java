/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.validate.IRefResourceProvider;
import com.hundsun.ares.studio.internal.core.registry.CommonDescriptor;

/**
 * ������Դ�ṩ������
 * @author sundl
 */
public class RefResourcesProviderDescriptor extends CommonDescriptor {

	private static final Logger logger = Logger.getLogger(RefResourcesProviderDescriptor.class);
	private String resTypes;
	private IRefResourceProvider provider;
	
	public RefResourcesProviderDescriptor(IConfigurationElement e) {
		super(e);
	}
	
	protected void loadFromExtension() {
		super.loadFromExtension();
		resTypes = configElement.getAttribute(ICommonExtensionConstants.RES_TYPES);
	}
	
	public String[] getResTypes() {
		return resTypes.split(",");
	}
	
	public IRefResourceProvider getProviderInstance() {
		if (provider == null) {
			try {
				provider = (IRefResourceProvider) configElement.createExecutableExtension(ICommonExtensionConstants.CLASS);
			} catch (CoreException e) {
				logger.warn("����RefResourceProviderʾ������, ", e);
			}
		}
		return provider;
	}
	
}
