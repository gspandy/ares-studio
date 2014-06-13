/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.List;

/**
 * 
 * @author sundl
 */
public interface IBasicReferencedLibInfo {

	public abstract List<IDependenceDescriptor> getDependencyDescriptors();
	
	public abstract String getPublishTime();

	public abstract String getNote();

	public abstract String getName();

	public abstract String getPublisher();

	public abstract String getContact();

	public abstract String getProvider();

	public abstract String getVersion();

	public abstract String getId();
	
	public abstract String getType();

}