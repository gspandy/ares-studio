package com.hundsun.ares.studio.procedure.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hundsun.ares.studio.procdure.provider.ProcedureUI;

public class ProdcedurePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ProdcedurePreferencePage() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setDescription("�洢����");
		setPreferenceStore(ProcedureUI.getPlugin().getPreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(ProcedureUI.PER_AUTO_DEFINE_PROC_INPARAM, "�Ƿ��Զ��������������ɱ���", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ProcedureUI.PER_NOT_DEFINE_CONNECT_TYPE, "�������������Ƿ񲻴���%type��", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ProcedureUI.PER_RETURN_ERROR_INFO, "������Ϣ����Ҫ����", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ProcedureUI.PER_GEN_RELATED_INFO, "�Ƿ���Ҫ���ɹ�����Ϣ", getFieldEditorParent()));
		
		if(ProcedureUI.isStock2Procedure()){
			addField(new BooleanFieldEditor(ProcedureUI.PER_GEN_BEGIN_CODE, "�Ƿ�����ǰ�ô���", getFieldEditorParent()));
			addField(new BooleanFieldEditor(ProcedureUI.PER_GEN_END_CODE, "�Ƿ����ɺ��ô���", getFieldEditorParent()));
		}
	}

}
