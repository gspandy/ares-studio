/**
 * Դ�������ƣ�TypeDefaultValue.java
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
 * A representation of the model object '<em><b>Type Default Value</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getTypeDefaultValue()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='name refId'"
 *        annotation="http://www.hundsun.com/ares/studio/jres/references refId='com.hundsun.ares.studio.jres.model.metadata.util.MetadataReferenceParser jres.md.typedef'"
 * @generated
 */
public interface TypeDefaultValue extends MetadataItem {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String getValue(String typeId);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setValue(String typeId, String value);

} // TypeDefaultValue
