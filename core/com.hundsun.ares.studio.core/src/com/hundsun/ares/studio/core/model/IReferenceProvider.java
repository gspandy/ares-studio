/**
 * Դ�������ƣ�IReferenceProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IReference Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.core.model.CorePackage#getIReferenceProvider()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IReferenceProvider extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='BasicEList<Reference> references = new BasicEList<Reference>();\r\n// \u6536\u96c6\u6240\u6709\u5305\u542b\u5185\u5bb9\u7684\u5f15\u7528\r\nfor (TreeIterator<EObject> iterator = eAllContents(); iterator.hasNext(); ) {\r\n\tEObject obj = iterator.next();\r\n\treferences.addAll(ReferenceUtil.INSTANCE.getReferences(obj));\r\n}\r\nreturn references;'"
	 * @generated
	 */
	EList<Reference> getReferences();

} // IReferenceProvider
