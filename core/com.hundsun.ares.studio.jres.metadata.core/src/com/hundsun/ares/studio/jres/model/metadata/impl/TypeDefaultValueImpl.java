/**
 * Դ�������ƣ�TypeDefaultValueImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.impl;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Default Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TypeDefaultValueImpl extends MetadataItemImpl implements TypeDefaultValue {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeDefaultValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.TYPE_DEFAULT_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getValue(String typeId) {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-29 #1 #1 #��map��ֵ
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		return getData().get(typeId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setValue(String typeId, String value) {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ���#2011-7-29 #1#1 #ֵ�趨��map��ȥ
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		getData().put(typeId, value);
	}

} //TypeDefaultValueImpl
