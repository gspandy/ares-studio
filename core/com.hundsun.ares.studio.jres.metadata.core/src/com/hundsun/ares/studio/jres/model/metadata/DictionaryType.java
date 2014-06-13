/**
 * Դ�������ƣ�DictionaryType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dictionary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.DictionaryType#getItems <em>Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getDictionaryType()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='name refId'"
 * @generated
 */
public interface DictionaryType extends MetadataItem {
	/**
	 * Returns the value of the '<em><b>Items</b></em>' containment reference list.
	 * The list contents are of type {@link com.hundsun.ares.studio.jres.model.metadata.DictionaryItem}.
	 * It is bidirectional and its opposite is '{@link com.hundsun.ares.studio.jres.model.metadata.DictionaryItem#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' containment reference list.
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getDictionaryType_Items()
	 * @see com.hundsun.ares.studio.jres.model.metadata.DictionaryItem#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<DictionaryItem> getItems();

} // DictionaryType
