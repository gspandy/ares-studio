/**
 * Դ�������ƣ�TableSQLPreviewPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.ProjectPropertyEditor;

/**
 * ��༭���е�Ԥ��ҳ��
 * @author sundl
 *
 */
public class TableSQLPreviewPage extends DBSQLPreviewPage{

	private static final Logger LOGGER = Logger.getLogger(TableSQLPreviewPage.class);
	
	/* ����֧�ֵ����ݿ����� */
	private List<String> dbTypeList = new ArrayList<String>();
	/* ��ǰ��������ݿ����� */
	private String currentDBType;
	
	/* �Ƿ���ʾȫ���ű� */
	private boolean displayFullScript = true;
	/* �Ƿ���ʾ����(����)�ű� */
	private boolean displayPatchScript = true;
	
	/* ȫ���ű� */
	private String fullScript;
	/* �����ű� */
	private String patchScript;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public TableSQLPreviewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public void createPartControl(Composite parent) {
		Composite client = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		GridLayoutFactory.fillDefaults().applyTo(client);
		
		Composite optionParent = new Composite(client, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(optionParent);
		createOptionGroups(optionParent);
		
		Composite textParent = new Composite(client, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(textParent);
		textParent.setLayout(new FillLayout());
		
		super.createPartControl(textParent);
	}
	
	private void createOptionGroups(Composite parent) {
		GridLayoutFactory.fillDefaults().margins(15, 10).applyTo(parent);
		loadDBTypes();
		if (dbTypeList.size() > 0) {
			Group group = new Group(parent, SWT.NONE);
			group.setText("���ݿ�����");
			GridDataFactory.fillDefaults().grab(true, true).applyTo(group);
			GridLayoutFactory.fillDefaults().numColumns(dbTypeList.size() + 1).applyTo(group);
			for (String type : dbTypeList) {
				Button button = new Button(group, SWT.RADIO);
				button.setText(type);
				if (StringUtils.equals(currentDBType, type)) {
					button.setSelection(true);
				}
				button.setEnabled(false);
			}
			Button btSwitch = new Button(group, SWT.PUSH);
			btSwitch.setText("�л�");
			btSwitch.setToolTipText("ת����Ŀ���Ա༭���У�ѡ��ͬ��JS�ű����л����ݿ�����");
			btSwitch.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					IWorkbenchPage page = getEditor().getSite().getPage();
					page.getEditorReferences();
					
					IARESResource resource = getEditor().getARESResource();
					if (resource != null) {
						IARESProject project = resource.getARESProject();
						IFile file = project.getProject().getFile(IARESProjectProperty.PRO_FILE);
						if (file.exists()) {
							try {
								IEditorPart editor = page.openEditor(new FileEditorInput(file), ARESEditorPlugin.PROJECT_PROPERTY_ID, true);
								if (editor instanceof ProjectPropertyEditor) {
									((ProjectPropertyEditor) editor).setActivePage("overview");
								}
							} catch (PartInitException e1) {
								LOGGER.error(e1);
							}
						} else {
							MessageDialog.openError(getEditor().getSite().getShell(), "Error", "�Ҳ�����Ŀ�����ļ�");
						}
					} else {
						MessageDialog.openError(getEditor().getSite().getShell(), "Error", "�޷���λAres��Դ����������Դ���λ�ò���ȷ...");
					}
				}
			});
		} 
		
		Group groupType = new Group(parent, SWT.NONE);
		groupType.setText("�ű�����");
		GridDataFactory.fillDefaults().grab(true, true).applyTo(groupType);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(groupType);
		Button btAllScript = new Button(groupType, SWT.CHECK);
		btAllScript.setText("ȫ���ű�");
		
		String all = getDialogSettings().get("full");
		// ��һ��ʹ��Ĭ�Ϲ�ѡ
		if (all == null) {
			btAllScript.setSelection(true);
		} else {
			btAllScript.setSelection(getDialogSettings().getBoolean("full"));
		}
		displayFullScript = btAllScript.getSelection();
		btAllScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				displayFullScript = !displayFullScript;
				getDialogSettings().put("full", displayFullScript);
				update();
			}
		});
		Button btPatchScript = new Button(groupType, SWT.CHECK);
		btPatchScript.setText("�����ű�");
		if (getDialogSettings().get("patch") == null) {
			btPatchScript.setSelection(true);
		} else {
			btPatchScript.setSelection(getDialogSettings().getBoolean("patch"));
		}
		displayPatchScript = btPatchScript.getSelection();
		btPatchScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				displayPatchScript = !displayPatchScript;
				getDialogSettings().put("patch", displayPatchScript);
				update();
			}
		});
	}
	
	private IDialogSettings getDialogSettings() {
		IDialogSettings settings = DatabaseUI.getDefault().getDialogSettings();
		IDialogSettings dialogSettings = settings.getSection("table_preview");
		if (dialogSettings == null) {
			dialogSettings = settings.addNewSection("table_preview");
		}
		return dialogSettings;
	}
	
	public void setFullScript(String scriptString) {
		this.fullScript = scriptString;
	}

	public void setPatchScript(String patchScript) {
		this.patchScript = patchScript;
	}
	
	public void update() {
		String text = "";
		if (displayFullScript)
			text += fullScript;
		if (displayPatchScript)
			text += patchScript;
		setText(text);
		super.update();
	}
	
	private void loadDBTypes() {
		IARESResource resource = getEditor().getARESResource();
		if (resource != null) {
			IARESProject project = resource.getARESProject();
			IARESProjectProperty property = null;
			try {
				property = project.getProjectProperty();
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
			
			if (property != null) {
				String jsFile = (String) property.getString(IDBConstant.KEY_TABLE_JS_PATH);
				if (StringUtils.isNotEmpty(jsFile)) {
					IFile file = project.getProject().getFile(jsFile);
					String fileName = file.getName();
					fileName = StringUtils.substringBeforeLast(fileName, ".");
					String prefix = StringUtils.substringBeforeLast(fileName, "_");
					currentDBType = StringUtils.substringAfterLast(fileName, "_");
					dbTypeList.add(currentDBType);
					
					IContainer folder = file.getParent();
					if (folder.exists()) {
						try {
							for (IResource res : folder.members()) {
								if (res.getType() == IResource.FILE) {
									String name = StringUtils.substringBeforeLast(res.getName(), ".");
									if (name.startsWith(prefix + "_")) {
										String t = StringUtils.substringAfterLast(name, "_");
										if (!dbTypeList.contains(t))
											dbTypeList.add(t);
									}
								}
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
