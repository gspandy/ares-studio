/**
 * Դ�������ƣ�OracleUserPrivilegeEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.jres.model.database.oracle.OraclePrivilege;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleUserResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.impl.OracleUserImpl;

/**
 * @author wangbin
 *
 */
public class OracleUserPrivilegeEditor extends OracleUserPrivilegeSelectionCellEditor {
	

	/**
	 * @param parent
	 * @param fromText
	 * @param toText
	 */
	private ColumnViewer columnViewer;
	public OracleUserPrivilegeEditor(ColumnViewer columnViewer, String fromText,
			String toText) {
		super((Composite) columnViewer.getControl(), fromText, toText);
		this.columnViewer = columnViewer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		OracleUserImpl index = (OracleUserImpl)((StructuredSelection)columnViewer.getSelection()).getFirstElement();
		
		EList<OraclePrivilege> oldList =((OracleUserResourceData) index.eContainer()).getPrivileges();
		List<OraclePrivilege> resultList = new ArrayList<OraclePrivilege>();
		Object object = super.doGetValue();
		if(object instanceof String){
			String value =  (String) object;
			String[] items = value.split(",");
			for(int i=0; i<items.length; i++){
				for(OraclePrivilege pri : oldList){
					if(!resultList.contains(pri) && pri.getName().equals(items[i])){
						resultList.add(pri);
						break;
					}
				}
			}
			return resultList;
		}
		return object;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		if(value instanceof List){
			List<OraclePrivilege> privilege = (List<OraclePrivilege>) value;
			StringBuffer buffer = new StringBuffer();
			for (int i = 0, length = privilege.size(); i < length; i++) {
				buffer.append(privilege.get(i).getName());
				if (i < length - 1)
					buffer.append(",");
			}
			super.doSetValue(buffer.toString());
		}
		else{
			super.doSetValue(value);
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		OracleUserPrivilegeSelectionDialog selectionDialog = new OracleUserPrivilegeSelectionDialog(cellEditorWindow.getShell(), 
				"Oracle�û�Ȩ��ѡ��", 
				columnViewer);
		selectionDialog.setBlockOnOpen(true);
		int returnCode = selectionDialog.open();
		return (returnCode == Window.OK) ? selectionDialog.getResult() : null;
	}

}
