/**
 * Դ�������ƣ�MetadataOverviewColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.util.CircularReferenceException;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.viewers.BaseEObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * ����ҳ��ı�ǩ�ṩ��<BR>
 * ����ҳ�����Ŀģ����<code>{@link com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement MetadataOverviewElement}</code>
 * @author gongyf
 *
 */
public class MetadataOverviewColumnLabelProvider extends BaseEObjectColumnLabelProvider {

	/**
	 * @param attribute
	 */
	public MetadataOverviewColumnLabelProvider(EAttribute attribute) {
		super(attribute);
	}

	/**
	 * @param attributeProvider
	 */
	public MetadataOverviewColumnLabelProvider(
			IEStructuralFeatureProvider attributeProvider) {
		super(attributeProvider);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		// �������õĴ���
		MetadataItem item = ((MetadataOverviewElement) element).getItem();
		IARESResource resource = ((MetadataOverviewElement) element).getResource();
		if (MetadataUtil.isReferencableFeature(item, getEStructuralFeature(element))) {
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
