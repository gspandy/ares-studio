/**
 * Դ�������ƣ�MetadataEStructuralFeatureProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * �ܹ����������������
 * @author gongyf
 *
 */
public class MetadataEStructuralFeatureProvider implements IEStructuralFeatureProvider {

	EStructuralFeature categoryAttribute;
	EStructuralFeature itemAttribute;
	
	

	/**
	 * @param categoryAttribute
	 * @param itemAttribute
	 */
	public MetadataEStructuralFeatureProvider(EStructuralFeature categoryAttribute,
			EStructuralFeature itemAttribute) {
		super();
		this.categoryAttribute = categoryAttribute;
		this.itemAttribute = itemAttribute;
	}



	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.IEAttributeProvider#getAttribute(java.lang.Object)
	 */
	@Override
	public EStructuralFeature getFeature(Object element) {
		if (element instanceof MetadataCategory) {
			return categoryAttribute;
		} else if (element instanceof MetadataItem) {
			return itemAttribute;
		}
		return null;
	}

}
