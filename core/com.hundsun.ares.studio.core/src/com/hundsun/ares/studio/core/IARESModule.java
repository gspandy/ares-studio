/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.resources.IFile;


/**
 * ͨ�õ�ģ�顣
 * @author sundl
 */
public interface IARESModule extends IARESElement, IParent, IOpenable, ISourceManipulation {

	String DEFAULT_MODULE_NAME = "";
	String MODULE_PROPERTY_FILE = "module.xml";
	
	/**
	 * ������ģ�飬����ֱ�ӣ���ӵġ�
	 * @return ��ģ��
	 * @throws ARESModelException 
	 */
	IARESModule[] getSubModules() throws ARESModelException;
	
	/**
	 * ����ģ��ĸ�ģ�顣
	 * ����Ŀǰ����ģ�����ڴ��ж���ƽ���ṹ������ģ���getParent()����ģ�����
	 * ���Ը��ӹ�ϵ������ͨ��������ܵó���
	 * ע�����������getParent()������
	 * @return
	 */
	IARESModule getParentModule();
	
	String getShortName();
	
	/**
	 * ��ȡ��Ares��Դ���ļ���
	 * @return
	 */
	IFile[] getNonARESFiles();
	
	/**
	 * ȡ�����ģ���µ�������Դ��(��������ģ��)
	 * @return ������Դ�б�
	 */
	IARESResource[] getARESResources();
	
	/**
	 * ������ģ�飨������ģ�飬����ֱ�Ӻͼ�ӵģ�
	 * @param recursion
	 * @return
	 */
	IARESResource[] getARESResources(boolean recursion);
	
	/**
	 * ����ģ���¾���ָ�������ֵ���Դ; �����ڲ��᷵��null�����ص�ares resource��Ҫ��exists()�����ж��Ƿ���ڡ�
	 * @param name Ӧ���Ǵ���չ������Դ��������xxx.action, xxx.table
	 * @return ģ���¾���ָ�������ֵ���Դ
	 */
	IARESResource getARESResource(String name);
	
	/**
	 * ������Դ��û���򷵻�null
	 * @param name ����չ��
	 * @return
	 */
	IARESResource findResource(String name);
	
	/**
	 * ����ģ���¾���ָ�������ֵ���Դ; �����ڲ��᷵��null�����ص�ares resource��Ҫ��exists()�����ж��Ƿ���ڡ�
	 * @param name ���֣�Ӧ���ǲ�����չ���Ķ�����
	 * @param type ����
	 * @return ģ���¾���ָ�������ֵ���Դ
	 */
	IARESResource getARESResource(String name, String type);
	
	/**
	 *  ������Դ��û���򷵻�null
	 * @param name
	 * @param type
	 * @return
	 */
	IARESResource findResource(String name, String type);
	
	IARESResource[] getARESResources(String type);
	
	/**
	 * ��ȡָ�����͵���Դ������ָ���Ƿ�ݹ���ģ��
	 * @param type ��Դ����
	 * @param recursion �Ƿ�ݹ�
	 * @return ģ��������ָ�����͵���Դ
	 */
	IARESResource[] getARESResources(String type, boolean recursion);
	
	/**
	 * ��ȡָ�����͵���Դ(����ݹ���ģ��)
	 * @param types 
	 * @return
	 */
	IARESResource[] getARESResources(String[] types);
	
	/**
	 * ���Եݹ��ȡ��Դ(�ݹ鼴�Ƿ������ģ�������Դ)��û�еڶ���������Ĭ��Ϊ���ݹ�
	 * @param types
	 * @param recursion �Ƿ�ݹ�
	 * @return ģ�������е�ָ�����͵���Դ�����ݵڶ������������Ƿ�ݹ���ģ�飩
	 */
	IARESResource[] getARESResources(String[] types, boolean recursion);
	
	/**
	 * ����ģ�����ڵĸ�
	 * @return ģ�����ڵ�ģ���
	 */
	IARESModuleRoot getRoot();
	
	/**
	 * ���ڵ����ð�������ģ���������ð��������������塣���򷵻�null
	 * @return ģ�����ڵ����ð�
	 */
	IReferencedLibrary getLib();
	
	boolean isDefaultModule();
	
	/**
	 * ������Դ,��Դ���ͱ������Ѿ�ע�����; ���۴����ɹ���񶼻᷵�ض�Ӧ��ARESResource����
	 * @param name ����չ��,���ļ���ȫ�� 
	 * @param info ��Դ��Ϣ
	 * @return
	 */
	IARESResource createResource(String name, Object info) throws ARESModelException;
}
