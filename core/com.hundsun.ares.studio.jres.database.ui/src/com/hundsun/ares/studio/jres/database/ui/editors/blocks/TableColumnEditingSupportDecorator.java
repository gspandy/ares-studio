/**
 * Դ�������ƣ�TableColumnEditingSupportDecorator.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors.blocks;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.CellEditor;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator;

/**
 * @author sundl
 *
 */
public class TableColumnEditingSupportDecorator implements IEditingSupportDecorator{

	private IARESProject project;
	private EAttribute attribute;
	
	/**
	 * @param project
	 * @param attribute
	 */
	public TableColumnEditingSupportDecorator(IARESProject project, EAttribute attribute) {
		super();
		this.project = project;
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator#decorateGetCellEditor(org.eclipse.jface.viewers.CellEditor, java.lang.Object)
	 */
	@Override
	public CellEditor decorateGetCellEditor(CellEditor cellEditor, Object element) {
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator#decorateCanEdit(boolean, java.lang.Object)
	 */
	@Override
	public boolean decorateCanEdit(boolean canEdit, Object element) {
		if (element instanceof TableColumn) {
			TableColumn c = (TableColumn) element;
			// �Ǳ�׼�ֶ��������Զ����Ա༭
			if (c.getColumnType() == ColumnType.NON_STD_FIELD) {
				return true;
			} else if (c.getColumnType() == ColumnType.STD_FIELD) {
				// ��׼�ֶ�
				if (attribute.equals(DatabasePackage.Literals.TABLE_COLUMN__CHINESE_NAME)
						|| attribute.equals(DatabasePackage.Literals.TABLE_COLUMN__DATA_TYPE)
						|| attribute.equals(DatabasePackage.Literals.TABLE_COLUMN__DESCRIPTION)) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
}
