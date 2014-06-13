/**
 * Դ�������ƣ�StandardFieldListImpl.java
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
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Standard Field List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class StandardFieldListImpl extends MetadataResourceDataImpl<StandardField> implements StandardFieldList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StandardFieldListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.STANDARD_FIELD_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<StandardField> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<StandardField>(StandardField.class, this, MetadataPackage.STANDARD_FIELD_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public StandardField find(String name) {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-27 #5 #5 #��ָ�����Ƶı�׼�ֶΣ�ע�⻺��
		// Ensure that you remove @generated or mark it @generated NOT
		//throw new UnsupportedOperationException();
		for(StandardField field:getItems()){
			if(StringUtils.equals(name, field.getName()))
				return field;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.JRESResourceInfoImpl#getReferences()
	 */
	@Override
	public EList<Reference> getReferences() {
		EList<Reference> references=new BasicEList<Reference>();
		for (StandardField field : getItems()) {
			// ����ֱ�����á�ҵ��������������
			if(field.getRefId()!=null && !StringUtils.isBlank(field.getRefId())){
				Reference StdFieldRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.StdField,field, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
				// TODO#�����߲�-ģ�����#��Ԫ#��#��С��#�˷�����StdFieldRef!=null�����壬
					references.add(StdFieldRef);
			}
			if(field.getDataType()!=null && !StringUtils.isBlank(field.getDataType())){
				Reference dataTypeRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.BizType,field, MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE);
					references.add(dataTypeRef);
			}
			if(field.getDataType()!=null && !StringUtils.isBlank(field.getDictionaryType())){
				Reference dictionaryRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.Dict,field, MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE);
					references.add(dictionaryRef);
			}
		}
		return references;
	}

} //StandardFieldListImpl
