/**
 * Դ�������ƣ�MetadataCategory.java
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
 * A representation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getChildren <em>Children</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getItems <em>Items</em>}</li>
 *   <li>{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getMetadataCategory()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='name'"
 * @generated
 */
public interface MetadataCategory extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory}.
	 * It is bidirectional and its opposite is '{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getMetadataCategory_Children()
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MetadataCategory> getChildren();

	/**
	 * Returns the value of the '<em><b>Items</b></em>' reference list.
	 * The list contents are of type {@link com.hundsun.ares.studio.jres.model.metadata.MetadataItem}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' reference list.
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getMetadataCategory_Items()
	 * @model
	 * @generated
	 */
	EList<MetadataItem> getItems();

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(MetadataCategory)
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataPackage#getMetadataCategory_Parent()
	 * @see com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getChildren
	 * @model opposite="children" transient="false"
	 * @generated
	 */
	MetadataCategory getParent();

	/**
	 * Sets the value of the '{@link com.hundsun.ares.studio.jres.model.metadata.MetadataCategory#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(MetadataCategory value);

} // MetadataCategory
