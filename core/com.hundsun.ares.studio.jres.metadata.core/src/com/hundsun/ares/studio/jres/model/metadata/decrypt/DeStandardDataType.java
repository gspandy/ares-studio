/**
 * Դ�������ƣ�DeStandardDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt;

import com.hundsun.ares.studio.jres.metadata.service.IStandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>De Standard Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage#getDeStandardDataType()
 * @model superTypes="com.hundsun.ares.studio.jres.model.metadata.decrypt.DeMetadataItem<com.hundsun.ares.studio.jres.model.metadata.StandardDataType> com.hundsun.ares.studio.jres.model.metadata.decrypt.IStandardDataType"
 * @generated
 */
public interface DeStandardDataType extends DeMetadataItem<StandardDataType>, IStandardDataType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='return getDataMapValue(typeId);'"
	 * @generated
	 */
	String getValue(String typeId);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='return StringUtils.replaceEach(getValue(typeId), \r\n\t\t\t\tnew String[]{\"$L\", \"$P\"}, \r\n\t\t\t\tnew String[]{length, precision});'"
	 * @generated
	 */
	String getRealType(String typeId, String length, String precision);

	/**
	 * һ�������ָ��Ķ���
	 */
	DeStandardDataType NULL = DecryptFactory.eINSTANCE.createDeStandardDataType();
} // DeStandardDataType
