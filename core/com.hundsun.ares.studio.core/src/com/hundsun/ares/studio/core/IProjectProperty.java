/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.CoreException;


/**
 * ��Ŀ���Խڵ�
 * @author sundl
 */
public interface IProjectProperty extends IARESElement, IOpenable {
	
	// ���ڲ�����Դ�����ǻ�����Ҫ��д��Ϣ����������������Ϣ�Ķ�д�ӿ�
	public IARESProjectProperty getInfo() throws ARESModelException;
	public void save(IARESProjectProperty property) throws CoreException;
	
}
