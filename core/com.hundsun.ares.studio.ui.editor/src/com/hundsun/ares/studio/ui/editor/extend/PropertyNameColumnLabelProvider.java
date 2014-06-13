/**
 * Դ�������ƣ�PropertyNameColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * @author gongyf
 *
 */
public class PropertyNameColumnLabelProvider extends ColumnLabelProvider {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		
		if (element instanceof ExtensibleModelEditingCategory) {
			return ((ExtensibleModelEditingCategory) element).getName();
		} else if (element instanceof ExtensibleModelEditingEntry) {
			return ((ExtensibleModelEditingEntry) element).getDescriptor().getDisplayName();
		}
		return StringUtils.EMPTY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.Object)
	 */
	@Override
	public String getToolTipText(Object element) {
		if (element instanceof ExtensibleModelEditingGroup) {
			return ((ExtensibleModelEditingGroup) element).getEditingSupport().getName();
		} else if (element instanceof ExtensibleModelEditingEntry) {
			return ((ExtensibleModelEditingEntry) element).getDescriptor().getDescription();
		}
		return StringUtils.EMPTY;
	}
}
