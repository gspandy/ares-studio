/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ģ�����ʼ��
 * @author sundl
 */
public interface IModuleRootConstructor {

	/**
	 * ��ʼ������ģ����������쳣������׳�ARESModelException.
	 * @param root ��Ҫ��ʼ����ģ���
	 * @throws ARESModelException �����ʼ�����������쳣����������׳�����쳣
	 */
	public void construct(IARESModuleRoot root) throws ARESModelException;
	
}
