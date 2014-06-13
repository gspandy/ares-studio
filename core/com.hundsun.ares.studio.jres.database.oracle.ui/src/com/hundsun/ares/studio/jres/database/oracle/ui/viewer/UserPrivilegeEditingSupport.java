/**
 * Դ�������ƣ�IndexColumnLabelSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.viewer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.jres.database.oracle.ui.editors.OracleUserPrivilegeEditor;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.jres.model.database.oracle.impl.OracleUserImpl;
import com.hundsun.ares.studio.ui.editor.editingsupport.BaseEditingSupport;

/**
 * @author wangbin
 *
 */
public class UserPrivilegeEditingSupport extends BaseEditingSupport {
	
	private int featureID;

	/**
	 * @param viewer
	 */
	public UserPrivilegeEditingSupport(ColumnViewer viewer, int featureID) {
		super(viewer);
		this.featureID = featureID;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.BaseEditingSupport#createCellEditor()
	 */
	@Override
	protected CellEditor createCellEditor() {
		return new OracleUserPrivilegeEditor(getViewer(), "Oracle�û�Ȩ��", "��ѡ�û�Ȩ��");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		if(element instanceof OracleUserImpl){
			return ((OracleUserImpl)element).eGet(featureID, true, false);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(element);
		if(element instanceof OracleUserImpl){
			if (editingDomain != null) {
				Command command = SetCommand.create(editingDomain, element, OraclePackage.Literals.ORACLE_USER__PRIVILEGES, value);
				editingDomain.getCommandStack().execute(command);
			} else {
				((OracleUserImpl)element).eSet(featureID, value);
			}
		}
	}
}
