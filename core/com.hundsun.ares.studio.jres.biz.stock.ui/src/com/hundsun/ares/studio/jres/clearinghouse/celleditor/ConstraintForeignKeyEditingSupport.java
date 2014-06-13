/**
 * Դ�������ƣ�ConstraintForeignKeyEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.biz.stock.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.clearinghouse.celleditor;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.chouse.ConstraintModifyDetail;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.editingsupport.EMFEditingSupport;

/**
 * @author sundl
 *
 */
public class ConstraintForeignKeyEditingSupport extends EMFEditingSupport {

	IARESResource resource;
	TableResourceData tableData;
	
	
	public ConstraintForeignKeyEditingSupport(ColumnViewer viewer, EStructuralFeature feature, IARESResource resource, TableResourceData tableData) {
		super(viewer, feature);
		this.resource = resource;
		this.tableData = tableData;
	}

	protected CellEditor doGetCellEditor(Object element) {
		return new ConstraintForeignKeyCellEditor((Composite) getViewer().getControl(), this.resource, (ConstraintModifyDetail) element, this.tableData);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.editingsupport.BaseEditingSupport#createCellEditor()
	 */
	@Override
	protected CellEditor createCellEditor() {
		// ��Ϊ������д��doGetCellEditor�������û���á�
		return null;
	}

}
