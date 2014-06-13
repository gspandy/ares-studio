/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * ͨ�õ�ģ����Ľӿڡ�
 * @author sundl
 */
public interface IARESModuleRoot extends IARESElement, IOpenable, IParent {

	String PROPERTY_FILE = "root.xml";
	
	int KIND_SOURCE = 1;
	
	int KIND_BINARY = 2;
	
	/**
	 * ��ȡģ������������ģ�顣
	 * @return ��ģ����µ����е�ģ�顣
	 */
	IARESModule[] getModules() throws ARESModelException;
	
	/**
	 * ��ȡָ�����ֵ�ģ�顣
	 * @param name ģ������֣�ȫ����
	 * @return ָ�����ֵ�ģ��
	 */
	IARESModule getModule(String name);
	
	/**
	 * @param name
	 * @return
	 */
	IARESModule findModule(String name);
	
	/**
	 * ����ָ�����ֵ�ģ�顣
	 * @param name ģ������֣�ȫ����
	 * @return ָ�����ֵ�ģ��
	 * @throws CoreException 
	 */
	IARESModule createModule(String name) throws CoreException;
	
	public IARESModule createModule(String[] name, String[] cName) throws CoreException;
	
	IARESModule createModule(String name, String cName) throws CoreException;
	
	/**
	 * ��ȡָ�����͵���Դ�б�
	 * @param type
	 * @return
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources(String type) throws ARESModelException;
	IARESResource[] getResources(String[] types) throws ARESModelException;
	IARESResource[] getResources() throws ARESModelException;
	
	int getKind();
	
	/**
	 * ����
	 * @return
	 */
	String getType();
	
	/**
	 * �Ƿ��ǹ鵵�ļ���һ������£����Ƿ���jar����
	 * @return �����һ���鵵�ļ���
	 */
	boolean isArchive();
	
	/**
	 * ���ڵİ����������ģ����������ð��е�����������塣�����������null
	 * @return ģ������ڵ����ð�
	 */
	IReferencedLibrary getLib();
	
	IPath getRootPath();
}
