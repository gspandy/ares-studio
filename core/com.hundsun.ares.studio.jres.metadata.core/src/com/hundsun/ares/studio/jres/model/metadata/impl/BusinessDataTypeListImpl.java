/**
 * Դ�������ƣ�BusinessDataTypeListImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.impl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;

import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceWithNamespaceImpl;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business Data Type List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class BusinessDataTypeListImpl extends MetadataResourceDataImpl<BusinessDataType> implements BusinessDataTypeList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessDataTypeListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.BUSINESS_DATA_TYPE_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<BusinessDataType> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<BusinessDataType>(BusinessDataType.class, this, MetadataPackage.BUSINESS_DATA_TYPE_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BusinessDataType find(String name) {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-27 #5 #5 #�����Ʋ���ҵ���������ͣ���Ҫ����
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		for(BusinessDataType type:getItems()){
			if(StringUtils.equals(name, type.getName()))
				return type;
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.JRESResourceInfoImpl#getReferences()
	 */
	@Override
	public EList<Reference> getReferences() {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-29#20 #20 #��ǰģ�͵�������Ϣ�ռ�����
		EList<Reference> references=new BasicEList<Reference>();
		for(BusinessDataType type:getItems()){
			if(type.getRefId()!=null && !StringUtils.isBlank(type.getRefId())){
				Reference bizTypeRef = new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.BizType,type, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
					references.add(bizTypeRef);	
			}
			if(type.getStdType()!=null && !StringUtils.isBlank(type.getStdType())){
				Reference  stdTypeRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.StdType,type, MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE);
					references.add(stdTypeRef);
			}
			if(type.getDefaultValue()!=null && !StringUtils.isBlank(type.getDefaultValue())){
				Reference  defaultTypeRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.DefValue,type, MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE);
					references.add(defaultTypeRef);
			}
		}
		return references;
	}

} //BusinessDataTypeListImpl
