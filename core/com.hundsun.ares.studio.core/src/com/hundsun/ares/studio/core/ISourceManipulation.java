/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ֧�ָ��ƣ��ƶ�����������ɾ��������Ԫ�ص�ͳһ�ӿڡ�
 * @author sundl
 */
public interface ISourceManipulation {

	/**
	 * Ԫ�ظ��Ƶ�ָ����container�¡�
	 * @param container ���Ƶ�������
	 * @param rename ����ʱָ���������֣��������Ҫ������Ϊ<code>null</code>
	 * @param replace ���������Ԫ�ص�����£��Ƿ񸲸�
	 * @param monitor The progress monitor
	 * @throws ARESModelException 
 	 * <ul>
	 * <li> container,element������(ELEMENT_DOES_NOT_EXIST)</li>
	 * <li> �ڸ��Ƶײ��ļ���ʱ�򣬷���CoreException</li>
	 * <li> Container���Ͳ���(INVALID_DESTINATION)</li>
	 * <li> ָ�������ֲ��Ϸ�(INVALID_NAME)</li>
	 * <li> ָ����Container���Ѿ���ͬ��Ԫ�أ���replaceΪ<code>false</code></li>
	 * <li> container��ֻ���� </li>
	 * </ul>
	 */
	void copy(IARESElement container, String rename, boolean replace, IProgressMonitor monitor) throws ARESModelException;
	
	void delete(boolean force, IProgressMonitor monitor) throws ARESModelException;
	
	void move(IARESElement container, String rename, boolean replace, IProgressMonitor monitor) throws ARESModelException;
	
	void rename(String name, boolean replace, IProgressMonitor monitor) throws ARESModelException;
}
