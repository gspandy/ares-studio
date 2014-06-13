/**
 * Դ�������ƣ�TypeDefaultValueListImpl.java
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
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Default Value List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TypeDefaultValueListImpl extends MetadataResourceDataImpl<TypeDefaultValue> implements TypeDefaultValueList {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeDefaultValueListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.TYPE_DEFAULT_VALUE_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<TypeDefaultValue> getItems() {
		if (items == null) {
			items = new EObjectContainmentWithInverseEList<TypeDefaultValue>(TypeDefaultValue.class, this, MetadataPackage.TYPE_DEFAULT_VALUE_LIST__ITEMS, MetadataPackage.METADATA_ITEM__PARENT);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TypeDefaultValue find(String name) {
		// TODO#ģ�����#��Ҷ��#��ͨ#��С��#�ѱ��� #2011-7-27 #5 #5 #Ч�Ĳ���ָ�����ֵ�Ĭ��ֵ����
		// Ensure that you remove @generated or mark it @generated NOT
		
		//throw new UnsupportedOperationException();
		for(TypeDefaultValue value:getItems()){
			if(StringUtils.equals(name, value.getName()))
				return value;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.JRESResourceInfoImpl#getReferences()
	 */
	@Override
	public EList<Reference> getReferences() {
		// TODO#ģ�����#��Ҷ��#��#��С��#�ѱ��� #2011-7-29 #8#10#��ǰģ�͵�������Ϣ�ռ�����
		EList<Reference> references=new BasicEList<Reference>();
		for(TypeDefaultValue value:getItems()){
			if(value.getRefId()!=null && !StringUtils.isBlank(value.getRefId())){
				Reference DefValueRef=new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.DefValue,value, MetadataPackage.Literals.METADATA_ITEM__REF_ID);
				if(DefValueRef!=null){
					references.add(DefValueRef);	
				}
			}
		}
		return references;
	}

} //TypeDefaultValueListImpl
