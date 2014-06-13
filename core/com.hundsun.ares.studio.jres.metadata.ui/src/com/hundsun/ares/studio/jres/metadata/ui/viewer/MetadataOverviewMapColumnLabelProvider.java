/**
 * Դ�������ƣ�MetadataOverviewMapColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.util.CircularReferenceException;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectMapColumnLabelProvider;

/**
 * @author gongyf
 *
 */
public class MetadataOverviewMapColumnLabelProvider extends EObjectMapColumnLabelProvider {

	/**
	 * @param reference
	 * @param key
	 */
	public MetadataOverviewMapColumnLabelProvider(EReference reference,
			Object key) {
		super(reference, key);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		// �������õĴ���
		MetadataItem item = ((MetadataOverviewElement) element).getItem();
		IARESResource resource = ((MetadataOverviewElement) element).getResource();
		if (MetadataUtil.isReferencableFeature(item, getReference())) {
			MetadataItem entity = null;
			try {
				entity = MetadataUtil.defaultResolve(item, resource).first;
			} catch (CircularReferenceException e) {
			}
			return entity == null ? item : entity;
		}
		
		return item;
	}
	
	@Override
	public Color getBackground(Object element) {
		MetadataOverviewElement moe = (MetadataOverviewElement) element;
		if (moe.isConflict()) {
			return Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
		}
		return super.getBackground(element);
	}
}
