/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.internal.core.registry.ARESProjectDescriptor;

/**
 * Ares��Ŀ���͵�ע����Ϣ��
 * @author sundl
 */
public class AresProjectRegistry extends CommonMapRegistry<IARESProjectDescriptor>{

	private static AresProjectRegistry instance;
	
	private AresProjectRegistry() {
		super();
	}
	
	public static AresProjectRegistry getInstance() {
		if (instance == null) {
			instance = new AresProjectRegistry();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.registry.CommonRegistry#getExtensionPointId()
	 */
	@Override
	public String getExtensionPointId() {
		return ICommonExtensionConstants.EP_ID_ARESPROJECT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.registry.CommonRegistry#handleConfigElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected void handleConfigElement(IConfigurationElement element) {
		IARESProjectDescriptor descriptor = new ARESProjectDescriptor(element);
		map.put(descriptor.getId(), descriptor);
	}
	
}
