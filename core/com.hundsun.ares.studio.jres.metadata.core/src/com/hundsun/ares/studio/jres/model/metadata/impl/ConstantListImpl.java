/**
 * Դ�������ƣ�ConstantListImpl.java
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
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.ConstantList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constant List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class ConstantListImpl extends MetadataResourceDataImpl<ConstantItem> implements ConstantList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstantListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.CONSTANT_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<ConstantItem> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<ConstantItem>(ConstantItem.class, this, MetadataPackage.CONSTANT_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.JRESResourceInfoImpl#getReferences()
	 */
	@Override
	public EList<Reference> getReferences() {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-29 #13 #10#��ǰģ�͵�������Ϣ�ռ�����
		EList<Reference> references=new BasicEList<Reference>();
		for(ConstantItem item:getItems()){
			if(item.getRefId()!=null && !StringUtils.isBlank(item.getRefId())){
				Reference constRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.Const,item, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
					references.add(constRef);
			}
			if(item.getDataType()!=null && !StringUtils.isBlank(item.getDataType())){
				Reference dataTypeRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.BizType,item, MetadataPackage.Literals.CONSTANT_ITEM__DATA_TYPE);
					references.add(dataTypeRef);
			}
		}
		return references;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ConstantItem find(String name) {
		/*
		 * TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-27 #5 #5 #�������ֲ��ҳ���
		 * 
		 * ���ҷ�Χ�����з���
		 */
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		for(ConstantItem item:getItems()){
			if(StringUtils.equals(name, item.getName()))
				return item;
		}
		return null;
	}

} //ConstantListImpl
