/**
 * 
 */
package com.hundsun.ares.studio.jres.database.ui.editors.blocks;

import org.eclipse.jface.viewers.CellEditor;

import com.hundsun.ares.studio.jres.database.utils.DatabaseUtils;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator;

/**
 * �����ݿ���У�������Ψһ�����������ظ�
 * @author yanwj6282
 *
 */
public class DatabaseDefineEditingSupportDecorator implements
		IEditingSupportDecorator {

	
	/**
	 * @param attribute
	 */
	public DatabaseDefineEditingSupportDecorator() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateGetCellEditor(org.eclipse.jface.viewers.CellEditor, java.lang.Object)
	 */
	public CellEditor decorateGetCellEditor(CellEditor cellEditor,
			Object element) {
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateCanEdit(boolean, java.lang.Object)
	 */
	public boolean decorateCanEdit(boolean canEdit, Object element) {
		TableColumn tc = (TableColumn) element;
		//if (tc.isPrimaryKey()) {
		if(DatabaseUtils.isPrimaryKey(tc)){
			return false;
		}
		return true;
	}

}
