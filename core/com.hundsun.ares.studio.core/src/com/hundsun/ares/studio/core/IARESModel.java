/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ģ�͵ĸ�Ԫ�أ���Ӧ�������䡣
 * ÿ������ARESCore��ģ��������Ӧ���и�ʵ��/�̳�����ӿڵ�ʵ������Ϊ�Լ���ģ�����ĵ�����
 * ��Ҫ�ṩ�Զ��ARESԪ�صĸ���/�ƶ�/������/ɾ��������
 * @author sundl
 */
public interface IARESModel extends IARESElement, IOpenable, IParent{

	/**
	 * ��ָ����Ԫ�ظ��Ƶ�ָ����������
	 * ���ָֻ����һ��container����ô�Ͱ����е�Ԫ�ظ��Ƶ����container���档���ָ���˶��container��
	 * Ԫ������ĳ��ȱ������������ĳ���ƥ�䣬����ÿ��Ԫ�ظ��Ƶ���Ӧ��container.
	 * <p>
	 * ÿ�����Ƶ�Ԫ�ض�����ѡ���Ƿ������������������ָ��Ϊ<code>null</code>���򲻻���������
	 * </p>
	 * <p>
	 * ����ָ�������Ŀ��λ���Ѿ���������ͬ���ֵ�Ԫ�أ��Ƿ񸲸ǡ������ͬ���Ҳ����ǣ�����׳���Ӧ���쳣��
	 * </p>
	 * @param elements ��Ҫ���Ƶ�Ԫ��
	 * @param containers ���Ƶ����������������б�
	 * @param renamings �����ֵ��б������Ԫ�ض�������<code>null</code>������������<code>null</code>
	 * @param replace ���Ŀ��λ��ͬ��Ԫ���Ѿ����ڣ��Ƿ񸲸�
	 * @param monitor The progress monitor
	 * @throws ARESModelException ���ĳ��Ԫ�ز��ܱ����ƣ�ԭ����ܰ���:
	 * <ul>
	 * <li> û��Ҫ�����Ԫ��(NO_ELEMENTS_TO_PROCESS)��elementsΪnull���߳���Ϊ0 </li>
	 * <li> ĳ��Ԫ�ػ������������� (ELEMENT_DOES_NOT_EXIST)</li>
	 * <li> ���µײ���Դ��ʱ������<code>CoreException</code></li>
	 * <li> ĳ�����������Ͳ�֧��(INVALID_DESTINATION)</li>
	 * <li> ĳ��ָ�����µ����ֲ��Ϸ�(INVALID_NAME)</li>
	 * <li> ĳ��ͬ��Ԫ�ش��ڣ�����<code>replace</code>ָ����Ϊ<code>false</code></li>
	 * </ul>
	 */
	void copy(IARESElement[] elements, IARESElement[] containers, /*sibling?*/String[] renamings, boolean replace, IProgressMonitor monitor) throws ARESModelException;

	/**
	 * ɾ��ָ����Ԫ�ء�
	 * @param elements
	 * @param force
	 * @param monitor
	 */
	void delete(IARESElement[] elements, boolean force, IProgressMonitor monitor) throws ARESModelException;
	
	/**
	 * ���ؾ���ָ�����ֵ�ARES��Ŀ��������ص���Ŀ���ܲ����ڡ�
	 * @param name ����
	 * @return ����ָ�����ֵ�ARES��Ŀ
	 */
	IARESProject getARESProject(String name);
	
	/**
	 * ����ģ�������е�ARES��Ŀ��
	 * @return ģ�������е�ARES��Ŀ
	 * @throws ARESModelException
	 */
	IARESProject[] getARESProjects() throws ARESModelException;
	
	IWorkspace getWorkspace();
	
	void move(IARESElement[] elements, IARESElement[] container, String[] renamings, boolean replace, IProgressMonitor monitor) throws ARESModelException;
	
	void rename(IARESElement[] elements, String[] names, boolean replace, IProgressMonitor monitor) throws ARESModelException;
}
