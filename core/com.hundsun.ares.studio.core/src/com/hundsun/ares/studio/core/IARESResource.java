/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ARES��Դ��ͳһ�ӿڡ�
 * @author sundl
 */
public interface IARESResource extends IARESElement, IOpenable, ISourceManipulation {
	
	Pattern RES_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{0,49}$");
	
	/**
	 * ���д�����
	 * @return ������Ľ�������û�д�����Ӧ�÷���һ���յ����顣������null
	 * @deprecated ʹ��validator��չ����д�����
	 */
	IARESProblem[] check();
	
	/**
	 * ��Դ�����֣�һ���Ƕ���,������չ����
	 * @return ��Դ�Ķ���
	 */
	String getName();
	
	/**
	 * ��Դ��ȫ������������Դ���ô���Դ��ʱ��Ӧ��ʹ�õ����֡�
	 * @return ��Դ��ȫ��(������չ��)
	 */
	String getFullyQualifiedName();
	
	/**
	 * ��Դ�����ͣ�һ������չ����Ϊ���͡�
	 * @return ��Դ������
	 */
	String getType();
	
	/**
	 * ��ȡ��ȡ��Դ����������ʹ���߸���رա�
	 * @return ������
	 * @throws ARESModelException ����������ܵ�ԭ���У�
	 */
	InputStream openStream() throws ARESModelException;
	
	/**
	 * ��ȡ��Դ���ڵ�ģ�����
	 * @return
	 */
	IARESModuleRoot getRoot();
	
	IARESModule getModule();
		
	IReferencedLibrary getLib();
	
	/**
	 * ������Դ���ڵ�Bundle���������������Դ����򷵻�������Դ�������򷵻ص�����Ŀ��
	 * @return
	 */
	IARESBundle getBundle();
	
	/**
	 * ��ȡ������Դ��Ϣ�Ķ���
	 * �����߲�Ӧ����ͼ�޸�������������޸ģ�Ӧ�õ���{@link #getWorkingCopy()}
	 * @return ������Դ��Ϣ�Ķ���
	 * @throws ARESModelException ����������ܵ�ԭ���У�
	 * <ul>
	 *  <li> ���Ԫ�ز����� (ELEMENT_DOES_NOT_EXIST) </li>
	 * </ul> 
	 * @deprecated see {@link #getInfo(Class)}
	 */
	Object getInfo() throws ARESModelException;
	
	/**
	 * ��ȡ������Դ��Ϣ�Ķ���Ĺ��������������޸ĺͱ��档
	 * @return ��Դ��Ϣ�Ķ���Ĺ�������
	 * <ul>
	 *  <li> ���Ԫ�ز����� (ELEMENT_DOES_NOT_EXIST)</li>
	 * </ul> 
	 * @deprecated see {@link #getWorkingCopy(Class)}
	 */
	Object getWorkingCopy() throws ARESModelException;
	
	/**
	 * ���Ի�ȡָ�����͵���Դ��Ϣ����������ģ�͵ļ���
	 * �����߲�Ӧ����ͼ�޸�������������޸ģ�Ӧ�õ���{@link #getWorkingCopy(Class)}
	 * @param <T>
	 * @param clazz ��Ҫ���ص�����
	 * @return ������Դ��Ϣ�Ķ���
	 * @throws ARESModelException ����������ܵ�ԭ���У�
	 * <ul>
	 *  <li> ���Ԫ�ز����� (ELEMENT_DOES_NOT_EXIST) </li>
	 * </ul> 
	 */
	<T> T getInfo(Class<T> clazz) throws ARESModelException;
	
	/**
	 * ���Ի�ȡָ�����͵���Դ��Ϣ���󸱱���������ģ�͵ļ���
	 * <p>ֻ�е�info-class��ע��Ϊ{@link IAdaptable}������ʱ��������ʵ���������������</p>
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws ARESModelException ����������ܵ�ԭ���У�
	 * <ul>
	 *  <li> ���Ԫ�ز����� (ELEMENT_DOES_NOT_EXIST) </li>
	 * </ul> 
	 */
	<T> T getWorkingCopy(Class<T> clazz) throws ARESModelException;
	
	/**
	 * ����Ԫ����Ϣ��
	 * @param info ��Դ��Ϣ����
	 * @param force �Ƿ�ǿ�Ʊ���
	 * @param monitor The Progress monitor
	 * @throws ARESModelException ����������ܵ�ԭ���У�
	 * <ul>
	 * <li> ��Դ������(ELEMENT_DOES_NOT_EXIST)</li>
	 * <li> ��Դ��ֻ����(READ_ONLY)</li>
	 * <li> ��Դ��Ϣ����ȷ���������Ͳ��ԣ����������쳣(INVALID_INFO)</li>
	 * </ul>
	 */
	void save(Object info, boolean force, IProgressMonitor monitor) throws ARESModelException;

}
