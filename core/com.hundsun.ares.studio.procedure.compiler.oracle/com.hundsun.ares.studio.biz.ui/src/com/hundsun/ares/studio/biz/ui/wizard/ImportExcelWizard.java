package com.hundsun.ares.studio.biz.ui.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import com.hundsun.ares.studio.biz.excel.BizImportOperation;
import com.hundsun.ares.studio.biz.provider.BizUI;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.ExcelHandlerException;
import com.hundsun.ares.studio.core.util.ARESElementUtil;

public abstract class ImportExcelWizard extends Wizard implements IImportWizard, IOverwriteQuery {

	private static final Logger logger = Logger.getLogger(ImportExcelWizard.class);
	private IWorkbench workbench;
	private IStructuredSelection selection;
	protected ImportWizardPage page;
	protected IARESProject project;
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		Object obj = selection.getFirstElement();
		if (obj instanceof IARESElement) {
			project = ((IARESElement) obj).getARESProject();
		} else if (obj instanceof IResource) {
			project = ARESElementUtil.toARESElement(obj).getARESProject();
		}
		setNeedsProgressMonitor(true);
		
		IDialogSettings settings = BizUI.getPlugin().getDialogSettings().getSection("importwizard");
		if (settings == null)
			settings = BizUI.getPlugin().getDialogSettings().addNewSection("importwizard");
		setDialogSettings(settings);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new ImportWizardPage("ѡ���ļ�", project);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		if (this.project != null) {
			File[] files = page.getFiles();
			boolean needReport = page.btNeedReport.getSelection();
			final BizImportOperation operation = createImportOperation(files);
			operation.project = this.project;
			
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						try {
							ResourcesPlugin.getWorkspace().run(operation, monitor);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				Throwable cause = e.getTargetException().getCause();
				if(cause != null && cause instanceof ExcelHandlerException){
					MessageDialog.openError(getShell(), "������̷����쳣", cause.getMessage());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (needReport) {
				try {
					File file = File.createTempFile("ares_studio_", ".html");
					operation.log.generateReport(new FileOutputStream(file));
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(file.toURI().toURL());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		} else {
			MessageDialog.openError(getShell(), "����", "�޷���λ��Ŀ");
		}
		return true;
	}
	
	protected abstract BizImportOperation createImportOperation(File[] files);
	
	
	public String queryOverwrite(String pathString) {
		String messageString = String.format("��Դ\"%s\"�Ѵ��ڣ��Ƿ񸲸ǣ�", pathString);
		final MessageDialog dialog = new MessageDialog(getShell(), 
				"ȷ��", null,
				messageString, MessageDialog.QUESTION, new String[] {
						IDialogConstants.YES_LABEL,
						IDialogConstants.YES_TO_ALL_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.NO_TO_ALL_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0) {
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}
		};
		String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
		// run in syncExec because callback is from an operation,
		// which is probably not running in the UI thread.
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				dialog.open();
			}
		});
		return dialog.getReturnCode() < 0 ? CANCEL : response[dialog.getReturnCode()];
	}
}
