/**
 * 
 */
package com.hundsun.ares.studio.jres.clearinghouse.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;

import com.hundsun.ares.studio.jres.clearinghouse.ui.wizard.HisModifyWizard;
import com.hundsun.ares.studio.jres.database.ui.editors.DBTableEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author gongyf
 *
 */
public class ModifyWizardAction extends Action {
	
	private IEditorPart editorPart;
	
	public ModifyWizardAction(IEditorPart editorPart) {
		this.editorPart = editorPart;
		setText("���޸���");
		//setImageDescriptor(DatabaseUI.getImageDescriptor("icons/table.gif"));
	}

	@Override
	public void run() {
		HisModifyWizard wizard = new HisModifyWizard((EMFFormEditor)editorPart);
		WizardDialog dlg = new WizardDialog(editorPart.getEditorSite().getShell(), wizard);
		dlg.setTitle("���޸�����");
		dlg.setMinimumPageSize(320, 240);
		dlg.open();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
//		editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editorPart instanceof DBTableEditor){
			return true;
		}
		return super.isEnabled();
	}
	
	
}
