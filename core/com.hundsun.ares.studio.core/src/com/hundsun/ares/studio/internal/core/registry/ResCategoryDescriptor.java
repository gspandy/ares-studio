/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.registry.IResCategoryDescriptor;

/**
 * ��Դ�����������Ϣ
 * @author sundl
 */
public class ResCategoryDescriptor extends OrderableDescriptor implements IResCategoryDescriptor{

	public ResCategoryDescriptor(IConfigurationElement e) {
		super(e);	
	}


}
