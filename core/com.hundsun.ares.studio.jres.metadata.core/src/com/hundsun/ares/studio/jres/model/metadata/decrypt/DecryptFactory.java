/**
 * Դ�������ƣ�DecryptFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt;

import org.eclipse.emf.ecore.EFactory;

import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage
 * @generated
 */
public interface DecryptFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DecryptFactory eINSTANCE = com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DecryptFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>De Metadata Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Metadata Item</em>'.
	 * @generated
	 */
	<T extends MetadataItem> DeMetadataItem<T> createDeMetadataItem();

	/**
	 * Returns a new object of class '<em>De Type Default Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Type Default Value</em>'.
	 * @generated
	 */
	DeTypeDefaultValue createDeTypeDefaultValue();

	/**
	 * Returns a new object of class '<em>De Standard Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Standard Data Type</em>'.
	 * @generated
	 */
	DeStandardDataType createDeStandardDataType();

	/**
	 * Returns a new object of class '<em>De Business Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Business Data Type</em>'.
	 * @generated
	 */
	DeBusinessDataType createDeBusinessDataType();

	/**
	 * Returns a new object of class '<em>De Standard Field</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Standard Field</em>'.
	 * @generated
	 */
	DeStandardField createDeStandardField();

	/**
	 * Returns a new object of class '<em>De Dictionary Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Dictionary Type</em>'.
	 * @generated
	 */
	DeDictionaryType createDeDictionaryType();

	/**
	 * Returns a new object of class '<em>De Dictionary Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Dictionary Item</em>'.
	 * @generated
	 */
	DeDictionaryItem createDeDictionaryItem();

	/**
	 * Returns a new object of class '<em>De Constant Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Constant Item</em>'.
	 * @generated
	 */
	DeConstantItem createDeConstantItem();

	/**
	 * Returns a new object of class '<em>De Error No Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>De Error No Item</em>'.
	 * @generated
	 */
	DeErrorNoItem createDeErrorNoItem();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DecryptPackage getDecryptPackage();

} //DecryptFactory
