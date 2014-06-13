package com.hundsun.ares.studio.jres.metadata.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hundsun.ares.studio.jres.metadata.MetadataCore;

public class MetadataPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setDescription("Ԫ������ص���ѡ��");
		setPreferenceStore(MetadataUI.getPlugin().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor(MetadataCore.PRE_APPLICATION_DEPARTMENT, 
				"�˵�Ӧ��ģʽ", 2,new String[][] {{"֤ȯ", MetadataCore.PRE_APPLICATION_DEPARTMENT_STOCK},
						{"����", MetadataCore.PRE_APPLICATION_DEPARTMENT_FINANCE}},getFieldEditorParent(),true));
	}

}
