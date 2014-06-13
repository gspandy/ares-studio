/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ��Ŀ��
 * @author sundl
 */
public interface IARESProject extends IARESElement, IOpenable, IParent, IARESBundle{

	IARESElement findElement(IPath path);
	
	IProject getProject();
	
	String[] getRequiredProjectNames();
	
	/**
	 * �������е�ģ����������������ð����ģ���
	 * @return ���еĸ�
	 */
	IARESModuleRoot[] getAllModuleRoots();
	
	IARESModuleRoot getModuleRoot(IResource resource);
	IARESModuleRoot getModuleRoot(IResPathEntry entry);
	
	/**
	 * ���е�ģ��
	 * @return
	 */
	IARESModule[] getModules() throws ARESModelException;
	
	IResPathEntry[] getRawResPath();
	
	void setRawResPath(IResPathEntry[] entries, IProgressMonitor monitor);
	
	IProjectProperty getProperty();
	IARESProjectProperty getProjectProperty() throws ARESModelException;
	
	/**
	 * ����һ��ģ���
	 * @param type
	 * @param path
	 * @return
	 */
	IARESModuleRoot createRoot(String type, String path, IProgressMonitor monitor) throws ARESModelException;

	IReferencedLibrary getReferencedLibrary(IResPathEntry entry);
	IReferencedLibrary getReferencedLibrary(IPath path);
	IReferencedLibrary[] getReferencedLibs() throws ARESModelException;
	
	IARESProject[] getRequiredProjects();
	public List<IDependencyUnit> getDependencies();
}
