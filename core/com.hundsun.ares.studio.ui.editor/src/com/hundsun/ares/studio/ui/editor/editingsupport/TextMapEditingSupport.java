/**
 * Դ�������ƣ�TextMapEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Map���͵�EMF�༭֧��
 * @author gongyf
 *
 */
public class TextMapEditingSupport extends EMFMapEditingSupport {

	/**
	 * @param viewer
	 * @param reference
	 * @param key
	 */
	public TextMapEditingSupport(ColumnViewer viewer, EReference reference,
			Object key) {
		super(viewer, reference, key);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.BaseEditingSupport#createCellEditor()
	 */
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
