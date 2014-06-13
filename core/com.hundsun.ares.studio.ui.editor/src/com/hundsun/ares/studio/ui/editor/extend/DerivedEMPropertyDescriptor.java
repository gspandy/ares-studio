/**
 * 
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * ΪһЩ�������Ե���������
 * һ���������Բ�ʵ�ʱ�����ģ���У����ǽ�������ļ�����߲��ҵó�
 * @author gongyf
 *
 */
public class DerivedEMPropertyDescriptor extends AbstractEMPropertyDescriptor {

	private ILabelProvider labelProvider;
	
	/**
	 * 
	 * @param structuralFeature ������
	 * @param labelProvider
	 */
	public DerivedEMPropertyDescriptor(EStructuralFeature structuralFeature, ILabelProvider labelProvider) {
		super(structuralFeature);
		this.labelProvider = labelProvider;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#getLabelProvider()
	 */
	@Override
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.AbstractEMPropertyDescriptor#isDerived()
	 */
	@Override
	public boolean isDerived() {
		return true;
	}
}
