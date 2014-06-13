/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ����ARESResource��������
 * @author sundl
 */
public interface IARESResourceContainer extends IARESElement, IOpenable, IParent{

	/**
	 * ����ȫ��������Ŀ�µ���Դ��
	 * @param fullyQualifiedName ȫ��
	 * @return ƥ��ָ����ȫ������Դ
	 * @throws ARESModelException 
	 */	
	IARESResource findResource(String fullyQualifiedName, String type) throws ARESModelException;
	
	/**
	 * ֻ��ȫ��������Դ��
	 * @param fullyQualifiedName
	 * @return
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
	 * �������Ͳ��ң�����ָ��������͡�
	 * ָ������Ϊ���������һ��ԭ���ǣ����ݶ�����ȡ��ԴҲ�᷵�ض����Դ����ɷ�����ͻ��
	 * @param types ��������
	 * @return ָ�����͵���Դ
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources(String[] types) throws ARESModelException;
	
	/**
	 * ��ȡ���е���Դ
	 * @return
	 * @throws ARESModelException 
	 */
	IARESResource[] getResources() throws ARESModelException;
	
}
