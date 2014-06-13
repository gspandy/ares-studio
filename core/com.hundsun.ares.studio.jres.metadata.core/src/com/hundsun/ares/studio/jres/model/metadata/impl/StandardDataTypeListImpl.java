/**
 * Դ�������ƣ�StandardDataTypeListImpl.java
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
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Standard Data Type List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class StandardDataTypeListImpl extends MetadataResourceDataImpl<StandardDataType> implements StandardDataTypeList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StandardDataTypeListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.STANDARD_DATA_TYPE_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<StandardDataType> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<StandardDataType>(StandardDataType.class, this, MetadataPackage.STANDARD_DATA_TYPE_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public StandardDataType find(String name) {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-27 #5 #5#�����֣����ұ�׼�������ͣ���Ҫ����
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		for(StandardDataType type:getItems()){
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
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-29 #8 #8 #��ǰģ�͵�������Ϣ�ռ�����
		EList<Reference> references=new BasicEList<Reference>();
		for(StandardDataType type:getItems()){
			if(type.getRefId()!=null && !StringUtils.isBlank(type.getRefId())){
				Reference stdTypeRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.StdType,type, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
					references.add(stdTypeRef);	
			}
		}
		return references;
	}

} //StandardDataTypeListImpl
