/**
 * Դ�������ƣ�ConstraintModifyEditingSupport.java
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
 * 
 * @author sundl
 *
 */
public class ConstraintColumnsEditingSupport extends EMFEditingSupport {

	private IARESResource resource;
	private TableResourceData tableResourceData;
		public ConstraintColumnsEditingSupport(ColumnViewer viewer, EStructuralFeature feature, IARESResource resource, TableResourceData table) {
		super(viewer, feature);
		this.resource = resource;
		this.tableResourceData = table;
	}

	protected CellEditor doGetCellEditor(Object element) {
		return new ConstraintColumnsCellEditor((Composite) getViewer().getControl(), (ConstraintModifyDetail) element, this.tableResourceData, this.resource);
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
