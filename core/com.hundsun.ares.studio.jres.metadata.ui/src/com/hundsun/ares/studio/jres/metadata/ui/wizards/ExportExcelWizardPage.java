/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.internal.core.ARESProject;

/**
 * @author gongyf
 *
 */
public class ExportExcelWizardPage extends SelectProjectAndExcelFileWizardPage {

	
	
	/**
	 * @param pageName
	 * @param selection
	 */
	public ExportExcelWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName, selection);
		
		setTitle("����Ԫ����");
		setDescription("ѡ����Ҫ����Ԫ���ݵ�JRESģ�鹤�̺͵�����Ԫ���ݶ����ļ�");
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.SelectProjectAndExcelFileWizardPage#newFileDialog(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected FileDialog newFileDialog(Shell shell) {
		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		
		return dlg;
	}
	
	protected void validate() {
		if (project == null || !(ARESProject.hasARESNature(project))) {
			setErrorMessage("��ѡ��һ��Ԫ���ݹ���");
			setPageComplete(false);
		} else if (excelFile == null || StringUtils.isEmpty(excelFile.getName())) {
			setErrorMessage("��ѡ��Ҫ������Ԫ���ݶ����ļ�");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

}
