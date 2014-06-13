package com.hundsun.ares.studio.internal.ui;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hundsun.ares.studio.ui.ARESUI;

public class ARESPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setDescription("ARES��ѡ��");
		setPreferenceStore(ARESUI.INSTANCE.getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		Composite lbGroupParent = getFieldEditorParent();
		lbGroupParent.setLayout(new FillLayout());
		Group group = new Group(lbGroupParent, SWT.None);
		group.setText("��Դ������ͼ�ڵ�������ʾ����");
		
		StringFieldEditor lbModule = new StringFieldEditor("label.module", "ģ��:", group);
		addField(lbModule);
		
		StringFieldEditor lbResource = new StringFieldEditor("label", "��Դ:", group);
		addField(lbResource);
		
		String[][] charset = new String[4][2];
		charset[0][0] = "GB2312";
		charset[0][1] = "GB2312";
		charset[1][0] = "GBK";
		charset[1][1] = "GBK";
		charset[2][0] = "UTF-8";
		charset[2][1] = "UTF-8";
		charset[3][0] = "UNICODE";
		charset[3][1] = "UNICODE";
		addField(new DirectoryFieldEditor(ARESUI.PRE_GENERATE_PATH,"��������·����", getFieldEditorParent()));
		addField(new ComboFieldEditor(ARESUI.PRE_GENERATE_CHARSET,"�������ɱ���:",charset,getFieldEditorParent()));
		
		addField(new RadioGroupFieldEditor(ARESUI.PRE_CELLEDITOR_ACTIVE_MODE, "���༭��ʽ", 2, new String[][]{
				{"�����༭", ARESUI.PRE_CELLEDITOR_ACTIVE_MODE_SINGLECLICK}, {"˫���༭", ARESUI.PRE_CELLEDITOR_ACTIVE_MODE_DOUBLECLICK}
		},getFieldEditorParent(),true));
	}

}
