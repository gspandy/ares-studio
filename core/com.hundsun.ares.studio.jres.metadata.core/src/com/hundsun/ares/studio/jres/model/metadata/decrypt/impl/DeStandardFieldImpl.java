/**
 * Դ�������ƣ�DeStandardFieldImpl.java
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
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeBusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeStandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>De Standard Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DeStandardFieldImpl extends DeMetadataItemImpl<StandardField> implements DeStandardField {
	
	private DeBusinessDataType dataType;
	private DeDictionaryType dictionaryType;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeStandardFieldImpl() {
		super();
	}

	/**
	 * @param proxyItem
	 * @param resource
	 */
	public DeStandardFieldImpl(StandardField proxyItem, IARESResource resource) {
		super(proxyItem, resource);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DecryptPackage.Literals.DE_STANDARD_FIELD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public DeBusinessDataType getDataType() {
		if (!checkStatus()) {
			return DeBusinessDataType.NULL;
		}
		if (dataType == null) {
			Pair<StandardField, IARESResource> item = getResolvedItem();
			
			// �������õ�Ԫ����
			Pair<MetadataItem, IARESResource> finded = MetadataUtil.findMetadataItem(item.first.getDataType(), 
					IMetadataRefType.BizType, getResource().getARESProject());
			
			if (finded != null) {
				dataType = MetadataUtil.decrypt((BusinessDataType) finded.first, finded.second);
			} else {
				dataType = DeBusinessDataType.NULL;
			}
		}

		return dataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDataTypeId() {
		if (!checkStatus()) return StringUtils.EMPTY;
		return getResolvedItem().first.getDataType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public DeDictionaryType getDictionaryType() {
		if (!checkStatus()) {
			return DeDictionaryType.NULL;
		}
		if (dictionaryType == null) {
			Pair<StandardField, IARESResource> item = getResolvedItem();
			
			// �������õ�Ԫ����
			Pair<MetadataItem, IARESResource> finded = MetadataUtil.findMetadataItem(item.first.getDictionaryType(), 
					IMetadataRefType.Dict, getResource().getARESProject());
			
			if (finded != null) {
				dictionaryType = MetadataUtil.decrypt((DictionaryType) finded.first, finded.second);
			} else {
				dictionaryType = DeDictionaryType.NULL;
			}
		}

		return dictionaryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDictionaryTypeId() {
		if (!checkStatus()) return StringUtils.EMPTY;
		return getResolvedItem().first.getDictionaryType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLength() {
		if (!checkStatus()) return "";
		return getResolvedItem().first.getLength();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrecision() {
		if (!checkStatus()) return "";
		return getResolvedItem().first.getPrecision();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRealType(final String typeId) {
		return getDataType().getStdType().getRealType(typeId, getLength(), getPrecision());
	}

} //DeStandardFieldImpl
