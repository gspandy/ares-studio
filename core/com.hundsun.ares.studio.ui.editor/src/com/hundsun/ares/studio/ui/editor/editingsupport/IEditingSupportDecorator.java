/**
 * Դ�������ƣ�IEditingSupportDecorator.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.eclipse.jface.viewers.CellEditor;

/**
 * @author gongyf
 *
 */
public interface IEditingSupportDecorator {
	CellEditor decorateGetCellEditor(CellEditor cellEditor, Object element);
	boolean decorateCanEdit(boolean canEdit, Object element);
}
