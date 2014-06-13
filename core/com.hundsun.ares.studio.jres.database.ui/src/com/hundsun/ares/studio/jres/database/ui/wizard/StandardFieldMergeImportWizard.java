/**
 * Դ�������ƣ�StandardFieldMergeImportWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.database.pdm.PDMImportExecel;
import com.hundsun.ares.studio.jres.database.ui.pages.ImportStdMergeWizardPage;

/**
 * @author yanwj06282
 *
 */
public class StandardFieldMergeImportWizard extends Wizard implements
		IImportWizard {

	private IStructuredSelection selection;
	private ImportStdMergeWizardPage page;
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle("���������¼");
	}

	@Override
	public void addPages() {
		addPage(page = new ImportStdMergeWizardPage("", selection));
	}
	
	@Override
	public boolean performFinish() {
		Job job = new Job("�����¼����"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				File file = page.getExcelFile();
				FileInputStream is = null;
				if (file.exists()) {
					try {
						IARESProject project = ARESCore.create(page.getSelectedProject());
						PDMImportExecel xlsImport = new PDMImportExecel();
						xlsImport.importPDMExcel(project ,file.getPath(), monitor);
					} catch (final Exception e1) {
						e1.printStackTrace();
						
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openError(getShell(), "�����¼����", e1.getMessage());
							}
						});
					}finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
	
		return true;
	}

}
