/**
 * Դ�������ƣ�DeDictionaryType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt;

import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.jres.metadata.service.IDictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>De Dictionary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage#getDeDictionaryType()
 * @model superTypes="com.hundsun.ares.studio.jres.model.metadata.decrypt.DeMetadataItem<com.hundsun.ares.studio.jres.model.metadata.DictionaryType> com.hundsun.ares.studio.jres.model.metadata.decrypt.IDictionaryType"
 * @generated
 */
public interface DeDictionaryType extends DeMetadataItem<DictionaryType>, IDictionaryType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<DeDictionaryItem> getItems();

	/**
	 * һ�������ָ��Ķ���
	 */
	DeDictionaryType NULL = DecryptFactory.eINSTANCE.createDeDictionaryType();
	
} // DeDictionaryType
