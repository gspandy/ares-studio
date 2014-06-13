/**
 * Դ�������ƣ�TableIndexColomnsEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.ui.editors.celleditor;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.editors.dialog.IndexSelectEditorDialog;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * �ֶ��б�celleditor
 * 
 * @author wangxh
 * 
 */
public class TableIndexColomnsCellEditor extends DialogCellEditor {

	private ColumnViewer columnViewer;
	private IARESResource resource;
	
	private Collection<TableIndexColumn> indexColumns = new ArrayList<TableIndexColumn>();
	private TableIndex index;
	private int returnCode = Window.CANCEL;
	
	public TableIndexColomnsCellEditor(ColumnViewer columnViewer,IARESResource resource,
			String fromText, String toText) {
		super((Composite) columnViewer.getControl());
		this.columnViewer = columnViewer;
		this.resource = resource;
	}

	@Override
	protected Object doGetValue() {
		return indexColumns;
	}

	@Override
	protected void doSetValue(Object value) {
		if (returnCode == Window.CANCEL) {
			setValueValid(false);
		}else {
			setValueValid(true);
		}
		if (value instanceof EObjectContainmentEList) {
			index = (TableIndex) ((EObjectContainmentEList) value).getEObject();
		}
		
		indexColumns.clear();
		indexColumns.addAll(EcoreUtil.copyAll((Collection<TableIndexColumn>)value));
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		TableResourceData tableResourceData = null;
		EObject obj = index.eContainer();
		if (obj == null || !(obj instanceof TableResourceData)) {
			try {
				tableResourceData = resource.getInfo(TableResourceData.class);
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}else {
			tableResourceData = (TableResourceData)obj;
		}
		IndexSelectEditorDialog dialog = new IndexSelectEditorDialog(
				cellEditorWindow.getShell(), "ѡ����ֶ�", columnViewer, resource,tableResourceData);
		returnCode = dialog.open();
		Object result = (returnCode == Window.OK) ? dialog.getResult() : null;
		if(result == null){
			setValueValid(false);
		}
		return result;
	}

	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		if (returnCode == Window.CANCEL) {
			setValueValid(false);
		}else {
			setValueValid(true);
		}
		return control;
	}
	
}
