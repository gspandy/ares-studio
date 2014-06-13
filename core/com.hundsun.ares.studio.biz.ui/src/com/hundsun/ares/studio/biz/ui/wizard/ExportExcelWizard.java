/**
 * 
 */
package com.hundsun.ares.studio.biz.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.progress.IProgressService;

import com.hundsun.ares.studio.biz.provider.BizUI;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.ui.util.DialogHelper;

/**
 * @author yanwj06282
 *
 */
public abstract class ExportExcelWizard extends Wizard implements IExportWizard {

	protected IStructuredSelection selection;
	protected ExportWizardPage page;
	protected IARESProject project;
	protected String moduleRootName;
	
	protected List<String> fileNames = new ArrayList<String>();
	
	public void setModuleRootName(String moduleRootName){
		this.moduleRootName = moduleRootName;
	}
	
	public void setFileName(List<String> fileNames){
		this.fileNames = fileNames;
	}
	
	public ExportExcelWizard() {
		// 2014-4-28 sundl ����Ĵ����Ǿ޴��bug....
//		IFolder rootFolder = ARESElementUtil.getModuleRootFolder(this.project,"com.hundsun.ares.studio.jres.moduleroot.database");
//		if( rootFolder != null){
//			this.moduleRootName = rootFolder.getName();//�������ͨ����չ��õ��ļ�������δ����database
//		}
	}
	
	public ExportExcelWizard(String moduleRootName) {
		this.moduleRootName = moduleRootName;
	}

	@Override
	public void addPages() {
		addPage(page = new ExportWizardPage("", selection ,moduleRootName));
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		Object obj = selection.getFirstElement();
		if (obj instanceof IARESElement) {
			project = ((IARESElement) obj).getARESProject();
		} else if (obj instanceof IResource) {
			project = ARESElementUtil.toARESElement(obj).getARESProject();
		}
		setNeedsProgressMonitor(true);
		
		IDialogSettings settings = BizUI.getPlugin().getDialogSettings().getSection("exportwizard");
		if (settings == null)
			settings = BizUI.getPlugin().getDialogSettings().addNewSection("exportwizard");
		setDialogSettings(settings);
	}
	
	public IARESProject getProject() {
		return project;
	}

	@Override
	public boolean performFinish() {
		if (this.project != null) {
			
			fileNames.add(page.getPath());
			List<IARESResource> results = page.getResult();
			final IWorkspaceRunnable operation = createExportOperation(project , results , page.getPath());
			
			{

				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						try {
							ResourcesPlugin.getWorkspace().run(operation, monitor);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				};
				try {
					MessageDialog msgdialog = null;
					try {
						IProgressService progress = MetadataUI.getDefault().getWorkbench()
								.getProgressService();
						progress.run(true, false, runnable);
					} catch (InvocationTargetException e) {
						msgdialog = new MessageDialog(null, "����ʧ��", null,
								e.getTargetException().getMessage(), MessageDialog.WARNING,
								new String[] { "ȷ��" }, 0);
						e.getTargetException().printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (null != msgdialog){
						msgdialog.open();
						return false;
					}else {
						StringBuffer filesPath = new StringBuffer();
						for(int index=0; index<fileNames.size(); index++){
							String file = fileNames.get(index);
							if(index > 0){
								filesPath.append(";\r\n" + file);
							}else{
								filesPath.append(file);
							}
						}
						msgdialog = new MessageDialog(null, "�����ɹ�", null,
								String.format("����·��Ϊ%s,�Ƿ���ļ���", filesPath.toString()), MessageDialog.INFORMATION, new String[] {
										"ȷ��","ȡ��"}, 0);
						if(Window.OK == msgdialog.open()){
							for(String file : fileNames){
								openFile(file);
							}
						}
						return true;
					}
				} catch (Exception e) {
					DialogHelper.showErrorMessage(e.getMessage());
					return false;
				}	
			
			}
		} else {
			MessageDialog.openError(getShell(), "����", "�޷���λ��Ŀ");
		}
		return true;
	}
	
	protected void openFile(final String path) {    
	    Runtime rn = Runtime.getRuntime();    
	    String cmd="cmd.exe /c start \"\" \"" + path + "\"";  
	    try {    
	        rn.exec(cmd);  
	    } catch (Exception e) { 
	    	e.printStackTrace();
	    }    
	}

	/**
	 * �����̲߳���
	 * 
	 * @param project ARES���̶���
	 * @param results ��Ҫ������ģ��
	 * @param file ����·��
	 * @return
	 */
	public abstract IWorkspaceRunnable createExportOperation(IARESProject project ,List<IARESResource> results ,String file);
	
}
