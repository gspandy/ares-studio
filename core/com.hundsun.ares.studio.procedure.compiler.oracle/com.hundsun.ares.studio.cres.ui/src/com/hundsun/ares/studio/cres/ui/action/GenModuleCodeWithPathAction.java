package com.hundsun.ares.studio.cres.ui.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCodeManager;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.IGenCresModuleCode;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.MakeFileGenerator;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.action.PopupAction;

public class GenModuleCodeWithPathAction extends PopupAction{
	
	private static Logger logger = Logger.getLogger(GenModuleCodeWithPathAction.class);
	static final Logger console = ConsoleHelper.getLogger();

	static boolean running = false;//�Ƿ���������
	
	private IARESProject project;
	private Set<IARESModule> modules = new HashSet<IARESModule>();
	
	protected boolean isHeadCode = false;
	
	protected boolean isEndCode = false;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		
		if (running) {
			MessageDialog dialog = new MessageDialog(getShell(), "ģ���������", null, "����ģ������������ɣ����Ժ�������", MessageDialog.INFORMATION, new String[] { "ȷ��" }, 0);
			dialog.open();
			return;
		}
		
		Job job = new Job("����ģ�����") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					running = true;
					HashSet<IGenCresModuleCode> genModuleCodeSet = GenCresModuleCodeManager.getInstance().getSet();
					String newPath = ModuleGeneratorHelper.getModuleGenCodePath(project);
					//�ڲ���Ŀ¼���ɵ�����£�����������ϵȷ������˳��
					if(!isGenCodeWithPath()){
						MakeFileGenerator generator = new MakeFileGenerator(modules);
						generator.createMakeFileWithoutFolder(newPath);
					}
					
					monitor.beginTask("����ģ�����", (modules.size() + genModuleCodeSet.size())*10 + 1);
					monitor.worked(1);
					for (IARESModule module : modules) {
						long l1 = System.currentTimeMillis();
						StringBuffer errLog = new StringBuffer();
						GenCresModuleCodeManager.getInstance();
						for (IGenCresModuleCode genModuleCode : genModuleCodeSet) {
							if(genModuleCode.canGenCode(module)) {
								genModuleCode.setErrLog(errLog);
								genModuleCode.setIsHeadCode(isHeadCode);
								genModuleCode.setIsEndCode(isEndCode);
								genModuleCode.genModuleCode(module, genModuleCode.getContext(project), 
										isGenCodeWithPath(), isGenCodeWithCNamePath(), new SubProgressMonitor(monitor, 10));
							}
							monitor.worked(10);
						} 
						openErrorLog(errLog, true);
						if(monitor.isCanceled()){
							break;
						}
						long l2 = System.currentTimeMillis();
						System.out.println("ģ��[" + module.getElementName() +"]����������ʱ��" + (l2-l1) + " ms");
					}
					
					for (IGenCresModuleCode genModuleCode : genModuleCodeSet) {
						genModuleCode.clearCache();
					}
					monitor.done();
					running = false;
					
				} catch (Exception e) {
					running = false;
				}
				
				openDoneDialog();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
	}
	
	public static void genModuleCode() {
		
	}
	
	/**
	 * ����ɶԻ���
	 */
	protected void openDoneDialog() {
		getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				//���·��������
				String path = ModuleGeneratorHelper.getModuleGenCodePath(project);
				MessageDialog dialogPath = new MessageDialog(getShell(), "�����������", null, "���ɸ�·����" + path, MessageDialog.INFORMATION, new String[] { "ȷ��" }, 0);
				dialogPath.open();
			}
		});
	}
	
	/**
	 * ���������Ƿ��Ŀ¼�ṹ
	 * @return
	 */
	protected boolean isGenCodeWithPath() {
		return true;
	}
	
	/**
	 * ��������Ŀ¼�Ƿ�ʹ��ģ����������ΪĿ¼��
	 * @return
	 */
	protected boolean isGenCodeWithCNamePath() {
		return false;
	}
	
	/**
	 * �򿪴�����־
	 * @param errLog
	 * 			������Ϣ
	 * @param openErrorLog
	 * 			�Ƿ�򿪴�����־�ļ�
	 */
	protected void openErrorLog(StringBuffer errLog,boolean openErrorLog) {
		
		// ������־Ϊ�յĻ�����д�ļ���
		if(!errLog.toString().trim().equals("")) {
			errLog.insert(0, "������־��\n");
			String fileName = "genCodeReport" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt";
			final IFile fReport = project.getProject().getFile(fileName);
			try {
				if (!fReport.exists()) {
					fReport.create(
						new ByteArrayInputStream(errLog.toString().getBytes(project.getProject().getDefaultCharset())), true,
						new NullProgressMonitor());
				} else {
					fReport.setContents(new ByteArrayInputStream(errLog.toString().getBytes(
						project.getProject().getDefaultCharset())), true, false, new NullProgressMonitor());
				}
				if(openErrorLog){
					getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								IDE.openEditor(getWorkbenchPart().getSite().getPage(), fReport, true);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.action.PopupAction#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (running) {
			action.setEnabled(false);
			return;
		}
		modules.clear();
		project = null;
		super.selectionChanged(action, selection);
		
		Iterator i = ((IStructuredSelection)selection).iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj instanceof IARESModule) {
				modules.add((IARESModule) obj);
				if (project == null) {
					project = ((IARESModule) obj).getARESProject();
				}
			}
		}
		//ֻ���߼���ԭ�ӡ����̲ſ���
		boolean isCresModule = false;
		if (modules.size() > 0) {
			for (IARESModule module : modules) {
				if(module.getParent().getElementName().equalsIgnoreCase("logic")||
						module.getParent().getElementName().equalsIgnoreCase("atom")||
						module.getParent().getElementName().equalsIgnoreCase("procedure")) {
					
					isCresModule = true;
					break;
				}
			}
		} 
		if(isCresModule) {
			action.setEnabled(true);
		}else {
			action.setEnabled(false);
		}
	}

}
