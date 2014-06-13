/**
 * Դ�������ƣ�BusinessDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getStdType <em>Std Type</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getLength <em>Length</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getPrecision <em>Precision</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getBusinessDataType()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='name refId stdType length precision defaultValue'"
 * @generated
 */
public interface BusinessDataType extends MetadataItem {
	/**
	 * Returns the value of the '<em><b>Std Type</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Std Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Std Type</em>' attribute.
	 * @see #setStdType(String)
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getBusinessDataType_StdType()
	 * @model default=""
	 * @generated
	 */
	String getStdType();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getStdType <em>Std Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Std Type</em>' attribute.
	 * @see #getStdType()
	 * @generated
	 */
	void setStdType(String value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(String)
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getBusinessDataType_Length()
	 * @model default=""
	 * @generated
	 */
	String getLength();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(String value);

	/**
	 * Returns the value of the '<em><b>Precision</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Precision</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Precision</em>' attribute.
	 * @see #setPrecision(String)
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getBusinessDataType_Precision()
	 * @model default=""
	 * @generated
	 */
	String getPrecision();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getPrecision <em>Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Precision</em>' attribute.
	 * @see #getPrecision()
	 * @generated
	 */
	void setPrecision(String value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(String)
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getBusinessDataType_DefaultValue()
	 * @model default=""
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.model.metadata.BusinessDataType#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

} // BusinessDataType
