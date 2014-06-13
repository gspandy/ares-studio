/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hundsun.ares.studio.model.reference;

import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IObjectProvider;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefName <em>Ref Name</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefNamespace <em>Ref Namespace</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefType <em>Ref Type</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getResource <em>Resource</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getObjectProvider <em>Object Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo()
 * @model
 * @generated
 */
public interface ReferenceInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Ref Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * ��������Դʹ��ʱ��Ҫ�����ƣ����׼�ֶε�Ӣ����(ID)
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Name</em>' attribute.
	 * @see #setRefName(String)
	 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo_RefName()
	 * @model
	 * @generated
	 */
	String getRefName();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefName <em>Ref Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <br/><B>ֻ���ṩ����ʹ�ã�����ʹ���߲�Ӧ��ʹ�ñ�����</B>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Name</em>' attribute.
	 * @see #getRefName()
	 * @generated
	 */
	void setRefName(String value);

	/**
	 * Returns the value of the '<em><b>Ref Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * ��������Դʹ��ʱ��Ҫ�������ռ�
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Namespace</em>' attribute.
	 * @see #setRefNamespace(String)
	 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo_RefNamespace()
	 * @model
	 * @generated
	 */
	String getRefNamespace();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefNamespace <em>Ref Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <br/><B>ֻ���ṩ����ʹ�ã�����ʹ���߲�Ӧ��ʹ�ñ�����</B>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Namespace</em>' attribute.
	 * @see #getRefNamespace()
	 * @generated
	 */
	void setRefNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Ref Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * ��������Դʹ��ʱ��Ҫ���������ͣ�ע���������Ͳ���ͬ����Դ����
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Type</em>' attribute.
	 * @see #setRefType(String)
	 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo_RefType()
	 * @model
	 * @generated
	 */
	String getRefType();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getRefType <em>Ref Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <br/><B>ֻ���ṩ����ʹ�ã�����ʹ���߲�Ӧ��ʹ�ñ�����</B>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Type</em>' attribute.
	 * @see #getRefType()
	 * @generated
	 */
	void setRefType(String value);

	/**
	 * Returns the value of the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * �ṩ������Դ��ARES��Դ
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' attribute.
	 * @see #setResource(IARESResource)
	 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo_Resource()
	 * @model dataType="com.hundsun.ares.studio.model.reference.IARESResource"
	 * @generated
	 */
	IARESResource getResource();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getResource <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <br/><B>ֻ���ṩ����ʹ�ã�����ʹ���߲�Ӧ��ʹ�ñ�����</B>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' attribute.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(IARESResource value);

	/**
	 * Returns the value of the '<em><b>Object Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * ���ڴ���Դ��ȡ���ö�����ṩ����һ�㲻��Ҫ���ñ����������ǵ���{@link #getObject()}
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Provider</em>' attribute.
	 * @see #setObjectProvider(IObjectProvider)
	 * @see com.hundsun.ares.studio.model.reference.ReferencePackage#getReferenceInfo_ObjectProvider()
	 * @model dataType="com.hundsun.ares.studio.model.reference.IObjectProvider"
	 * @generated
	 */
	IObjectProvider getObjectProvider();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.model.reference.ReferenceInfo#getObjectProvider <em>Object Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <br/><B>ֻ���ṩ����ʹ�ã�����ʹ���߲�Ӧ��ʹ�ñ�����</B>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object Provider</em>' attribute.
	 * @see #getObjectProvider()
	 * @generated
	 */
	void setObjectProvider(IObjectProvider value);

	/**
	 * <!-- begin-user-doc -->
	 * ���Ի�ȡ��������Դ��Ϣ��ʹ�õľ�������������û�н��л��棬����ֱ�Ӵ���Դ��ȡ�ģ���Լ���κ�ʱΪ 20��s~40��s
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Object getObject();

} // ReferenceInfo
