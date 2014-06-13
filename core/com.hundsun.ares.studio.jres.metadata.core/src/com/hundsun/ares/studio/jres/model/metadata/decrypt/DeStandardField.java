/**
 * Դ�������ƣ�DeStandardField.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt;

import com.hundsun.ares.studio.jres.metadata.service.IStandardField;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>De Standard Field</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage#getDeStandardField()
 * @model superTypes="com.hundsun.ares.studio.jres.model.metadata.decrypt.DeMetadataItem<com.hundsun.ares.studio.jres.model.metadata.StandardField> com.hundsun.ares.studio.jres.model.metadata.decrypt.IStandardField"
 * @generated
 */
public interface DeStandardField extends DeMetadataItem<StandardField>, IStandardField {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	DeBusinessDataType getDataType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (!checkStatus()) return StringUtils.EMPTY;\r\nreturn getResolvedItem().first.getDataType();'"
	 * @generated
	 */
	String getDataTypeId();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	DeDictionaryType getDictionaryType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (!checkStatus()) return StringUtils.EMPTY;\r\nreturn getResolvedItem().first.getDictionaryType();'"
	 * @generated
	 */
	String getDictionaryTypeId();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (!checkStatus()) return \"\";\r\nreturn getResolvedItem().first.getLength();'"
	 * @generated
	 */
	String getLength();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (!checkStatus()) return \"\";\r\nreturn getResolvedItem().first.getPrecision();'"
	 * @generated
	 */
	String getPrecision();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='return getDataType().getStdType().getRealType(typeId, getLength(), getPrecision());'"
	 * @generated
	 */
	String getRealType(String typeId);

	/**
	 * һ�������ָ��Ķ���
	 */
	DeStandardField NULL = DecryptFactory.eINSTANCE.createDeStandardField();
} // DeStandardField
