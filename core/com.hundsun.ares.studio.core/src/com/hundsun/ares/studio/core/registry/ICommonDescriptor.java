/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * ������Descriptor
 * @author sundl
 */
public interface ICommonDescriptor {

	String getId();
	String getName();
	ImageDescriptor getImageDescriptor();
	IConfigurationElement getConfigurationElement();
	
}

