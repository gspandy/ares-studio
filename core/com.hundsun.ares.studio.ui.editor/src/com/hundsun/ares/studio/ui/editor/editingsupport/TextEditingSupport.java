/**
 * Դ�������ƣ�TextEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

public class TextEditingSupport extends EMFEditingSupport {

	/**
	 * @param viewer
	 * @param feature
	 */
	public TextEditingSupport(ColumnViewer viewer, EStructuralFeature feature) {
		super(viewer, feature);
	}

	/**
	 * @param viewer
	 * @param featureProvider
	 */
	public TextEditingSupport(ColumnViewer viewer,
			IEStructuralFeatureProvider featureProvider) {
		super(viewer, featureProvider);
	}

	@Override
	protected CellEditor createCellEditor() {
		return new TextCellEditor((Composite) getViewer().getControl());
	}
	
	@Override
	protected Object getValue(Object element) {
		// ��ֹnull���õ�Text��ȥ
		return ObjectUtils.defaultIfNull(super.getValue(element), "");
	}

}
