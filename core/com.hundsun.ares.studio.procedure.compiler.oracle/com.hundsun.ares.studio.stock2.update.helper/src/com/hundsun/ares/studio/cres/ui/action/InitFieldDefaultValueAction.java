package com.hundsun.ares.studio.cres.ui.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.procedure.ui.extend.util.InitResFieldDefaultValueConnectStandFieldDV;
import com.hundsun.ares.studio.ui.action.PopupAction;

public class InitFieldDefaultValueAction extends PopupAction{
	static boolean running = false;//�Ƿ���������
	private IARESProject project;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		
		if (running) {
			return;
		}
		
		Job job = new Job("��ʼ����Դ�ֶ�Ĭ��ֵ") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					
					running = true;
					StringBuffer errLog = new StringBuffer();
					InitResFieldDefaultValueConnectStandFieldDV.initFieldDefaultValue(project,errLog,monitor);
					openErrorLog(errLog, true);
					running = false;
				} catch (Exception e) {
					running = false;
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
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
			errLog.insert(0, "�޸���־��\n");
			String fileName = "initFieldDftValueReport" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt";
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
		project = null;
		super.selectionChanged(action, selection);
		
		Iterator i = ((IStructuredSelection)selection).iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj instanceof IProject) {
				project = ARESCore.create((IProject)obj);
			}
		}
		if(project == null) {
			action.setEnabled(false);
		}else {
			action.setEnabled(true);
		}
	}
}
