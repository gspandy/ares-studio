/**
 * 
 */
package com.hundsun.ares.studio.jres.clearinghouse;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelCellEditor;

/**
 * @author gongyf
 *
 */
public class ModifyHistoryDialogCellEditor extends DialogCellEditor implements IExtensibleModelCellEditor{

	private IARESResource resource;
	RevisionHistory model;
	
	// ��ǰ���ڱ༭�Ķ���Ķ������: econtainer().eContainer().eContainer()..... ֱ��null
	private TableResourceData tableData;
	private static ILabelProvider provider = new ModifyHistoryDialogLabelProvider();
	
	public ModifyHistoryDialogCellEditor(Composite parent, IARESResource resource) {
		super(parent);
		this.resource = resource;
	}

	/* (non-Javadoc)8
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	@Override
	protected void updateContents(Object value) {
		// �޸ļ���celleditor����ʾ���ı�
		super.updateContents(provider.getText(value));
	}
	
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// Ĭ������ӱ��ֶ�
		Modification value = ChouseFactory.eINSTANCE.createAddTableColumnModification();
		
		if (getValue() != null && getValue() instanceof Modification) {
			value =  EcoreUtil.copy((Modification)getValue()) ;
		}
		HisModifyActionColumnDialog dialog = new HisModifyActionColumnDialog(cellEditorWindow.getShell(), tableData, value, resource , model);
		int returnCode = dialog.open();
		return (returnCode == Window.OK) ? dialog.getResult() : null;
	}
	
	@Override
	public void setModel(ExtensibleModel model) {
		this.model = (RevisionHistory) model;
		if (model.eContainer() instanceof TableResourceData) {
			tableData = (TableResourceData) model.eContainer();
		}
	}
	
}
