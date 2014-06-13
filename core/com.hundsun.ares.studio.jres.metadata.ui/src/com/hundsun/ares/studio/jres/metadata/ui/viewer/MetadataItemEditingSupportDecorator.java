/**
 * Դ�������ƣ�MetadataItemEditingSupportDecorator.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * @author gongyf
 *
 */
public class MetadataItemEditingSupportDecorator implements
		IEditingSupportDecorator {

	protected IEStructuralFeatureProvider provider;
	private IARESResource resource;
	
	public MetadataItemEditingSupportDecorator(EStructuralFeature feature,IARESResource resource) {
		this(new IEStructuralFeatureProvider.Impl(feature));
		this.resource = resource;
		
	}
	
	public MetadataItemEditingSupportDecorator(IEStructuralFeatureProvider provider) {
		super();
		this.provider = provider;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateGetCellEditor(org.eclipse.jface.viewers.CellEditor, java.lang.Object)
	 */
	@Override
	public CellEditor decorateGetCellEditor(CellEditor cellEditor,
			Object element) {
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateCanEdit(boolean, java.lang.Object)
	 */
	@Override
	public boolean decorateCanEdit(boolean canEdit, Object element) {
		if (element instanceof MetadataItem) {
			MetadataItem item = (MetadataItem) element;
			if (MetadataUtil.isUseRefFeature(resource) && MetadataUtil.isReferencingItem(item) ) {
				if (!MetadataUtil.isReferencableFeature(item, provider.getFeature(element)) ) {
					return canEdit;
				}
				return false;
			}
		} else if (element instanceof UncategorizedItemsCategoryImpl) {
			return false;
		}
		return canEdit;
	}

}
