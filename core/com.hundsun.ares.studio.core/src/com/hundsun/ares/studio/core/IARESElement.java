/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;

/**
 * ����Ԫ�ص�ͳһ�ӿڡ�
 * @author sundl
 */
public interface IARESElement extends IAdaptable{

	int ARES_MODEL = 0;
	int ARES_PROJECT = 1;
	int ARES_RESOURCE = 100;

	int COMMON_MODULE_ROOT = 200;
	int COMMON_MODULE = 300;
	int REF_LIBRAYR = 400;
	int PROJECT_PROPERTY = 500;
	
	/**
	 * ����Ԫ�ص�����
	 * @return Ԫ�ص�����
	 */
	String getElementName();
	
	/**
	 * Ԫ�ص����ͣ������ظ���
	 * @return ÿ������Ԫ�ص�Ψһ����ID������
	 */
	int getElementType();
	
	/**
	 * ����ֱ�Ӹ�Ԫ�ء�
	 * @return Ԫ�ص�ֱ�Ӹ�Ԫ��
	 */
	IARESElement getParent();
	
	/**
	 * ����ָ�����͵����ȡ�
	 * 
	 * @param ancestorType ����Ԫ�ص�����
	 * @return ����ָ�����͵�Ԫ��
	 */
	IARESElement getAncestor(int ancestorType);
	
	/**
	 * ����Ԫ�����ڵ���Ŀ��
	 * @return Ԫ�����ڵ���Ŀ
	 */
	IARESProject getARESProject();
	
	IARESModel getARESModel();
	
	/**
	 * �������Ԫ�صĶ�Ӧ���ַ�����ʾ����Ҫ��ʱ�򣬿��Դ��ַ����ָ����Ԫ�ء�
	 * @return Ԫ�ص��ַ�����ʾ
	 */
	String getHandleIdentifier();
	
	/**
	 * ����Ԫ�ص���ӽ�����Դ��
	 * ���磬������Դ���ͷ�����Դ��Ӧ���ļ���Jar�������Դ������Jar������External�ģ��򷵻��Ǹ�Jar����
	 * @return ����Ԫ�ص���ӽ�����Դ��
	 */
	IResource getResource();
	
	/**
	 * ������ȫ��Ӧ���Ԫ�ص���Դ�����磬һ����Դ��Ӧһ���ļ����������Jar�������Դ������null.
	 * @return ��ȫ��ӦԪ�ص���Դ
	 * @throws ARESModelException
	 */
	IResource getCorrespondingResource() throws ARESModelException;
	
	/**
	 * ����Ԫ�ص���ӽ�����Դ��
	 * ������ⲿ(External)��Jar���е���Դ���򷵻����jar���ļ�ϵͳ�еľ���·����
	 * @return ����Ԫ�ص���ӽ�����Դ��
	 */
	IPath getPath();
	
	/**
	 * �Ƿ�ֻ���������ڶ�Ӧ��Դ�Ƿ�ֻ��������Jar���е�Ԫ�أ�Jar��������ֻ���ģ���Ԫ����ֻ���ġ�
	 * @return ������Ԫ�ز����޸ģ��򷵻�<code>true</code>�����򷵻�<code>false</code>
	 */
	boolean isReadOnly();
	
	/**
	 * �������Ԫ���Ƿ���ʵ���ڵġ�
	 * ����ARESElement������ν��Handle�����Դ��ڻ򲻴��ڶ�Ӧ����ʵԪ�ء�
	 * @return ���Ԫ���Ƿ���ʵ���ڵ�
	 */
	boolean exists();
	
	/**
	 * ����Ԫ�صĽṹ�Ƿ���֪�ġ����磬һ�����������г����쳣��ģ�飬�����������false��
	 * @return ����ṹ����֪�ģ��򷵻�<code>true</code>�����򷵻�<code>false</code>
	 */
	boolean isStructureKnown();
}
