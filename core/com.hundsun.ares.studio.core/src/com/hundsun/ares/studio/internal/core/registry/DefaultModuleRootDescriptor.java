/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.registry.ICommonExtensionConstants;
import com.hundsun.ares.studio.core.registry.IDefaultModuleRootDescriptor;

/**
 * Ĭ��ģ���ע��������Ϣ
 * @author sundl
 */
public class DefaultModuleRootDescriptor extends CommonDescriptor implements IDefaultModuleRootDescriptor{

	private String projectType;
	private String rootType;
	private String path;
	
	public DefaultModuleRootDescriptor(IConfigurationElement e) {
		super(e);		
	}
	
	protected void loadFromExtension() {
		super.loadFromExtension();
		projectType = configElement.getAttribute(ICommonExtensionConstants.PROJECT_TYPE);
		rootType = configElement.getAttribute(ICommonExtensionConstants.ROOT_TYPE);
		path = configElement.getAttribute(ICommonExtensionConstants.PATH);
	}

	/**
	 * @return the projectType
	 */
	public String getProjectType() {
		return projectType;
	}

	/**
	 * @return the rootType
	 */
	public String getRootType() {
		return rootType;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
}
