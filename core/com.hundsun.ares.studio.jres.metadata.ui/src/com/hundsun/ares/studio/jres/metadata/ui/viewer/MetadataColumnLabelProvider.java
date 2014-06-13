/**
 * Դ�������ƣ�MetadataColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.util.CircularReferenceException;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * ����������״̬����ʾ���õ�����ֵ
 * @author gongyf
 *
 */
public class MetadataColumnLabelProvider extends EObjectColumnLabelProvider {

	private IARESResource resource;
	
	/**
	 * @param attribute
	 * @param resource
	 */
	public MetadataColumnLabelProvider(EAttribute attribute,
			IARESResource resource) {
		super(attribute);
		this.resource = resource;
	}

	/**
	 * @param attributeProvider
	 * @param resource
	 */
	public MetadataColumnLabelProvider(
			IEStructuralFeatureProvider attributeProvider,
			IARESResource resource) {
		super(attributeProvider);
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnLabelProvider#getBackground(java.lang.Object)
	 */
	@Override
	public Color getBackground(Object element) {
//		EObject owner = getOwner(element);
//		if (owner != element) {
//			return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
//		}
		if ( MetadataUtil.isUseRefFeature(resource) ) {
			if (element instanceof MetadataItem) {
				MetadataItem item = (MetadataItem)element;
				if (MetadataUtil.isReferencingItem(item) && MetadataUtil.isReferencableFeature(item, getEStructuralFeature(element))) {
					return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
				}
			}
		}
//		if (resource.isReadOnly()) {
//			return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
//		}
		return super.getBackground(element);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnLabelProvider#getDiagnostic(java.lang.Object)
	 */
	@Override
	protected Diagnostic getDiagnostic(Object element) {
		EObject owner = getOwner(element);
		// ���ò���Ҫ������
		if (owner == element) {
			return super.getDiagnostic(element);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		EObject owner = super.getOwner(element);
		if ( MetadataUtil.isUseRefFeature(resource) ) {
			// �����Ŀ��������������
			
			if (owner instanceof MetadataItem) {
				// �������õĴ���
				MetadataItem item = (MetadataItem)owner;
				if (MetadataUtil.isReferencingItem(item) && MetadataUtil.isReferencableFeature(item, getEStructuralFeature(element))) {
					MetadataItem entity = null;
					try {
						entity = MetadataUtil.defaultResolve(item, resource).first;
					} catch (CircularReferenceException e) {
					}
					return entity == null ? item : entity;
				}
			}
		}

		return owner;
	}
}
