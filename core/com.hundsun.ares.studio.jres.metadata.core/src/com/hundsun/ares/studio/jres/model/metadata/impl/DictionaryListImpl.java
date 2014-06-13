/**
 * Դ�������ƣ�DictionaryListImpl.java
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
import com.hundsun.ares.studio.jres.model.metadata.DictionaryList;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dictionary List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DictionaryListImpl extends MetadataResourceDataImpl<DictionaryType> implements DictionaryList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionaryListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.DICTIONARY_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<DictionaryType> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<DictionaryType>(DictionaryType.class, this, MetadataPackage.DICTIONARY_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public DictionaryType find(String name) {
		for (DictionaryType type : getItems()) {
			if (StringUtils.equals(name, type.getName())) {
				return type;
			}
		}
		return null;
	}
	
	public EList<Reference> getReferences() {
		EList<Reference> references=new BasicEList<Reference>();
		for(DictionaryType type:getItems()){
			if(type.getRefId()!=null && !StringUtils.isBlank(type.getRefId())){
				Reference constRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.Dict,type, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
					references.add(constRef);
			}
		}
		return references;
	}
		
} //DictionaryListImpl
