/**
 * Դ�������ƣ�AtomServicePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.atom.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.atom.excel;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.atom.AtomPackage;

/**
 * ԭ�ӷ����Ӧ��PropertyHandlerFactory
 * @author sundl
 *
 */
public class AtomServicePropertyHandlerFactory extends AtomPropertyHandlerFactory {

	public static final AtomServicePropertyHandlerFactory INSTANCE = new  AtomServicePropertyHandlerFactory();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return AtomPackage.Literals.ATOM_SERVICE;
	}

}
