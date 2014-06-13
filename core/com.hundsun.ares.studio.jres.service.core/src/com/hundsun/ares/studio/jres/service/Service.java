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

import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hundsun.ares.studio.jres.service.Service#getInterface <em>Interface</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hundsun.ares.studio.jres.service.ServicePackage#getService()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='objectId'"
 * @generated
 */
public interface Service extends JRESResourceInfo {
	/**
	 * Returns the value of the '<em><b>Interface</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface</em>' containment reference.
	 * @see #setInterface(BizInterface)
	 * @see com.hundsun.ares.studio.jres.service.ServicePackage#getService_Interface()
	 * @model containment="true"
	 * @generated
	 */
	BizInterface getInterface();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.service.Service#getInterface <em>Interface</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interface</em>' containment reference.
	 * @see #getInterface()
	 * @generated
	 */
	void setInterface(BizInterface value);

} // Service
