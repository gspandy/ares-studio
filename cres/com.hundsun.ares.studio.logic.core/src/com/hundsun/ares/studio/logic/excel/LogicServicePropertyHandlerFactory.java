/**
 * Դ�������ƣ�LogicServicePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.logic.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.logic.excel;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.logic.LogicPackage;

/**
 * @author sundl
 *
 */
public class LogicServicePropertyHandlerFactory extends LogicPropertyHandlerFactory {

	public static final LogicServicePropertyHandlerFactory INSTANCE = new LogicServicePropertyHandlerFactory();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return LogicPackage.Literals.LOGIC_SERVICE;
	}

}
