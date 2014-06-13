/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ARES��Ŀ�����ð���ͳһ�ӿ�
 * @author sundl
 */
public interface IARESBundle {

	/**
	 * ��ȡ��Ϣ����������Ի����ID��name������
	 * @return
	 */
	IARESBundleInfo getInfo();
	
	/**
	 * ��ͬ��getInfo().getId(); ����ʹ��
	 * @return
	 */
	String getId();
	
	/**
	 * ��ȡ<b>��Bundle��</b>ȫ����ģ���
	 * @return
	 * @throws ARESModelException
	 */
	IARESModuleRoot[] getModuleRoots() throws ARESModelException;
	
	/**
	 * ����ȫ��������Ŀ�µ���Դ��
	 * @param fullyQualifiedName ȫ��
	 * @param type ��Դ����
	 * @return ƥ��ָ����ȫ������Դ
	 * @throws ARESModelException
	 */
	IARESResource findResource(String fullyQualifiedName, String type) throws ARESModelException;
	
	/**
	 * ��<b>*��Bundle*</b>�и���ȫ��������Դ��
	 * @param fullyQualifiedName ȫ����ģ����.��Դ������: a.b.xxx
	 * @return ������Դ�������������Է�������
	 * @throws ARESModelException
	 */
	IARESResource[] findResource(String fullyQualifiedName) throws ARESModelException;
	
	/**
	 * ���ݶ�����ȡ��Դ����������ж����
	 * @param name ����(test.action)
	 * @return 
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources(String name) throws ARESModelException;
	
	/**
	 * �������Ͳ�����<b>*��Bundle*</b>��ָ�����͵���Դ������ָ��������͡�
	 * ָ������Ϊ���������һ��ԭ���ǣ����ݶ�����ȡ��ԴҲ�᷵�ض����Դ����ɷ�����ͻ��
	 * @param types ��������
	 * @return ָ�����͵���Դ
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources(String[] types) throws ARESModelException;
	
	/**
	 * ��ȡ<b>��Bundle��</b>���е���Դ
	 * @return
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources() throws ARESModelException;
	
	/**
	 * ��ȡָ����ģ�����ֻ������<b>*��Bundle*</b>�е�
	 * @param path
	 * @return
	 * @throws ARESModelException
	 */
	IARESModuleRoot getModuleRoot(String path) throws ARESModelException;
	
	/**
	 * ��ȡ������Bundle�б�
	 * Ŀǰ���ڱ�BundleΪProject��ʱ����Ч��
	 * ����Ϊ�Ժ�֧��������Դ�����������ִ�п�����Ҫ�����Ƚϸ��ӵ����㣬���Բ�Ҫ�������������Ч�ʻ�Ƚϸ�
	 * @return
	 */
	public IARESBundle[] getRequiredBundles();
}
