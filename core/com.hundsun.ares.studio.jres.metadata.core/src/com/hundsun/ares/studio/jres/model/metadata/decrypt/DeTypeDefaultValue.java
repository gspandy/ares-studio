/**
 * Դ�������ƣ�DeTypeDefaultValue.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt;

import com.hundsun.ares.studio.jres.metadata.service.ITypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>De Type Default Value</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage#getDeTypeDefaultValue()
 * @model superTypes="com.hundsun.ares.studio.jres.model.metadata.decrypt.DeMetadataItem<com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue> com.hundsun.ares.studio.jres.model.metadata.decrypt.ITypeDefaultValue"
 * @generated
 */
public interface DeTypeDefaultValue extends DeMetadataItem<TypeDefaultValue>, ITypeDefaultValue {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='return getDataMapValue(typeId);'"
	 * @generated
	 */
	String getValue(String typeId);

	/**
	 * һ�������ָ��Ķ���
	 */
	DeTypeDefaultValue NULL = DecryptFactory.eINSTANCE.createDeTypeDefaultValue();
} // DeTypeDefaultValue
