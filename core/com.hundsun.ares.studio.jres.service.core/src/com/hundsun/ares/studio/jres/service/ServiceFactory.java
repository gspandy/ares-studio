/**
 * Դ�������ƣ�${file_name}
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�${project_name}
 * ����˵����$$desc
 * ����ĵ���
 * ���ߣ�${user}
 */
package com.hundsun.ares.studio.jres.service;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.hundsun.ares.studio.jres.service.ServicePackage
 * @generated
 */
public interface ServiceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ServiceFactory eINSTANCE = com.hundsun.ares.studio.jres.service.impl.ServiceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Service</em>'.
	 * @generated
	 */
	Service createService();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ServicePackage getServicePackage();

} //ServiceFactory
