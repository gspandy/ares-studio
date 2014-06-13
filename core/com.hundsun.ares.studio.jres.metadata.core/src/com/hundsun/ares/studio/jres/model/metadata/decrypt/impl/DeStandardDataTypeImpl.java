/**
 * Դ�������ƣ�DeStandardDataTypeImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt.impl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeStandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>De Standard Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DeStandardDataTypeImpl extends DeMetadataItemImpl<StandardDataType> implements DeStandardDataType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeStandardDataTypeImpl() {
		super();
	}

	/**
	 * @param proxyItem
	 * @param resource
	 */
	public DeStandardDataTypeImpl(StandardDataType proxyItem,
			IARESResource resource) {
		super(proxyItem, resource);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DecryptPackage.Literals.DE_STANDARD_DATA_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue(final String typeId) {
		return getDataMapValue(typeId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRealType(final String typeId, final String length, final String precision) {
		return StringUtils.replaceEach(getValue(typeId), 
						new String[]{"$L", "$P"}, 
						new String[]{length, precision});
	}

} //DeStandardDataTypeImpl
