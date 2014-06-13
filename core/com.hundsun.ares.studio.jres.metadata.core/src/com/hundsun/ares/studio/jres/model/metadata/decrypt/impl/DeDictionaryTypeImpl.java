/**
 * Դ�������ƣ�DeDictionaryTypeImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.decrypt.impl;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.service.IDictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DecryptPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>De Dictionary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DeDictionaryTypeImpl extends DeMetadataItemImpl<DictionaryType> implements DeDictionaryType {
	
	private EList<DeDictionaryItem> items;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeDictionaryTypeImpl() {
		super();
	}

	/**
	 * @param proxyItem
	 * @param resource
	 */
	public DeDictionaryTypeImpl(DictionaryType proxyItem, IARESResource resource) {
		super(proxyItem, resource);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DecryptPackage.Literals.DE_DICTIONARY_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<DeDictionaryItem> getItems() {
		if (!checkStatus()) {
			return ECollections.emptyEList();
		}
		if (items == null) {
			items = new BasicEList<DeDictionaryItem>();
			for (DictionaryItem item : getResolvedItem().first.getItems()) {
				items.add(new DeDictionaryItemImpl(this, item));
			}
		}
		return items;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.service.IDictionaryType#getItemList()
	 */
	@Override
	public List<IDictionaryItem> getItemList() {
		return (List)getItems();
	}

} //DeDictionaryTypeImpl
