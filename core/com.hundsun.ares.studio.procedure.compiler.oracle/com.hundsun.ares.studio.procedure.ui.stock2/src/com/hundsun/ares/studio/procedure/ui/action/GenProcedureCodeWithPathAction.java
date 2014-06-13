package com.hundsun.ares.studio.procedure.ui.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.engin.logic.ResourceEngin;
import com.hundsun.ares.studio.jres.script.util.IScriptStringUtil;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.ui.extend.gencode.GenProcedureCode;
import com.hundsun.ares.studio.ui.ARESUI;
import com.hundsun.ares.studio.ui.action.PopupAction;

public class GenProcedureCodeWithPathAction extends PopupAction{

	public GenProcedureCodeWithPathAction() {
	}
	protected boolean isHeadCode = false;
	
	protected boolean isEndCode = false;
	static boolean running = false;//�Ƿ���������
	private IARESProject project;
	private Set<IARESResource> procs = new HashSet<IARESResource>();
	private Set<IARESModule> modules = new HashSet<IARESModule>();

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
					monitor.beginTask("���ɹ��̴���", procs.size());
					StringBuffer proBuf = new StringBuffer();
					Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
					//�ȶ�ѡ�еĴ洢���̽������򣬰��ն���Ŵ�С��������
					List<IARESResource> proList = new ArrayList<IARESResource>(procs);
					Collections.sort(proList, new ObjectIdCompartor());
					
					//������Դ�б�
					String resBuf = getResourceList(proList);
					
					//�����޶���¼
					String revHisBuf = getRevHistoryList(proList);
					
					proBuf.append("set define off;\r\n");
					proBuf.append("set feedback off;\r\n");
					proBuf.append("set serveroutput on;\r\n\r\n");
					
					proBuf.append(resBuf);
					
					proBuf.append(revHisBuf);
					
					for (IARESResource proc : proList) {
						StringBuffer errLog = new StringBuffer();
						GenProcedureCode genCode = new GenProcedureCode();
						genCode.setIsHeadCode(isHeadCode);
						genCode.setIsEndCode(isEndCode);
						genCode.setErrLog(errLog);
						
						String proStr = genCode.genProcedureCode(proc, isGenCodeWithPath(), isGenCodeWithCNamePath(), new SubProgressMonitor(monitor, 1));
						msgQueue.addAll(genCode.getARESProblems());
						
						if (StringUtils.isNotBlank(proStr)) {
							proBuf.append(proStr);
						}
						if(monitor.isCanceled()){
							break;
						}
						monitor.worked(1);
						openErrorLog(errLog, true);
					}
					
					proBuf.append("set define on;\r\n");
					proBuf.append("set feedback on;\r\n");
					
					monitor.subTask("�ļ�д��");
					monitor.worked(1);
					//д���ļ�
					IPreferencesService service = Platform.getPreferencesService();
					String charset = service.getString(ARESUI.PLUGIN_ID, ARESUI.PRE_GENERATE_CHARSET, StringUtils.EMPTY, null);
					String newPath = ModuleGeneratorHelper.getModuleGenCodePath(project);
					String fileName = "allproc.sql";
					if (modules.size() == 1) {
						fileName = getModuleChineseName(modules.toArray(new IARESModule[1])[0]) + ".sql";
					}
					String codeFileName = newPath +"\\" + fileName;
					writeToFile(codeFileName, proBuf.toString(),charset);
					//д�������Ϣ
					String errLogFileName = "s_as_proc.errorlog";
					createErrLog(msgQueue, errLogFileName,newPath,charset);
					
					
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
	
	private static String getModuleChineseName (IARESModule module ){
		if (module != null) {
			IARESResource property = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
			if (property != null && property.exists()) {
				try {
					ModuleProperty info = property.getInfo(ModuleProperty.class);
					if (info != null) {
						Object obj = info.getValue(ICommonModel.CNAME);
						if (obj != null) {
							return obj.toString();
						}
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	private String getResourceList(List<IARESResource> resList){
		List<List<String>> totleList = new ArrayList<List<String>>();
		List<String> descBegin = new ArrayList<String>();
		descBegin.add("/********************		");
		descBegin.add("*****************************************		");
		descBegin.add("********************************************************************************		*");
		totleList.add(descBegin);
		List<String> _desc = new ArrayList<String>();
		_desc.add("*--------------------		");
		_desc.add("-----------------------------------------		");
		_desc.add("---------------------------------------------------------------------------------		*");
		List<String> title = new ArrayList<String>();
		title.add("* "+"���̱��		");
		title.add("��������		");
		title.add("��������		");
		totleList.add(title);
		totleList.add(_desc);
		for (IARESResource res : resList) {
			try {
				Procedure procedure = res.getInfo(Procedure.class);
				if (procedure != null) {
					List<String> item = new ArrayList<String>();
					item.add("* "+procedure.getObjectId() + "		");
					item.add(procedure.getName() + "		");
					item.add(procedure.getChineseName() + "		*");
					totleList.add(item);
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		List<String> descEnd = new ArrayList<String>();
		descEnd.add("*********************		");
		descEnd.add("*****************************************		");
		descEnd.add("********************************************************************************		*/");
		totleList.add(descEnd);
		return IScriptStringUtil.instance.genStringTable(totleList)+"\r\n"; 
	}
	
	private String getRevHistoryList(List<IARESResource> resList){
		List<List<String>> totleList = new ArrayList<List<String>>();
		List<String> title = new ArrayList<String>();
		title.add("--"+"�޶��汾		");
		title.add("�޶�ʱ��		");
		title.add("�޶�����		");
		title.add("������		");
		title.add("������		");
		title.add("�޸�����		");
		totleList.add(title);
		for (IARESResource res : resList) {
			try {
				Procedure procedure = res.getInfo(Procedure.class);
				if (procedure != null) {
					for (RevisionHistory rh : procedure.getHistories()) {
						List<String> item = new ArrayList<String>();
						item.add("--" + rh.getVersion() + "		");
						item.add(rh.getModifiedDate() + "		");
						item.add(rh.getOrderNumber() + "		");
						item.add(rh.getModifiedBy() + "		");
						item.add(rh.getCharger() + "		");
						String modifyCont = "[" + procedure.getName() + "-" + rh.getModified() + "]";
						item.add(modifyCont + "		");
						totleList.add(item);
					}
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		return IScriptStringUtil.instance.genStringTable(totleList)+"\r\n"; 
	}
	
	/**
	 * ���ַ�����д��ָ���ļ�
	 * @param fileName
	 * @param contant
	 */
	protected void writeToFile(String fileName,String contant,String charset) {
		File codeFile = new File(fileName);
		try {
			if (!codeFile.exists()) {
				codeFile.createNewFile();
			}
			if(StringUtils.isBlank(charset)){
				charset = "GB2312";
			}
			FileOutputStream os = new FileOutputStream(codeFile);
			OutputStreamWriter wrinter = new OutputStreamWriter(os, charset);
			PrintWriter out = new PrintWriter(wrinter);
			try {
				out.print(contant);
			} catch (Exception ex) {
				out.println(ex.getMessage());
			}
			out.flush();
			out.close();
			os.close();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	/**
	 * @param msgQueue
	 * @param errorLogFileName
	 * @param path 
	 * @param charset 
	 */
	protected void createErrLog(Queue<IARESProblem> msgQueue,
			String errorLogFileName, String path,String charset) {
		StringBuffer errBuffer = new StringBuffer();
		if(msgQueue.size() > 0){
			
			errBuffer.append("/***" + "\r\n");
			errBuffer.append("������ʾ��Ϣ��\r\n");
			for(IARESProblem pItem: msgQueue){
				errBuffer.append(ResourceEngin.instance.getCodeErrorMessage(pItem));
				errBuffer.append("\r\n");
			}
			errBuffer.append("***/" + "\r\n");
			writeToFile(path + errorLogFileName, errBuffer.toString(),charset);
		}else{
			//û�д�����Ϣ��ɾ����־�ļ�
			File errorLogFile = new File(path + errorLogFileName);
			if(errorLogFile.exists()){
				errorLogFile.delete();
			}
		}
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
			String fileName = "genProcedureCodeReport" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt";
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
		procs.clear();
		modules.clear();
		project = null;
		super.selectionChanged(action, selection);
		
		Iterator i = ((IStructuredSelection)selection).iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj instanceof IARESResource) {
				IARESResource res = (IARESResource)obj;
				if (project == null) {
					project = res.getARESProject();
				}
				if(StringUtils.equals(res.getType(),"procedure")){
					procs.add(res);
					modules.add(res.getModule());
				}
			}
		}
		if(procs.size() <= 0) {
			action.setEnabled(false);
		}else {
			action.setEnabled(true);
		}
	}

	class ObjectIdCompartor implements Comparator<IARESResource>{

		@Override
		public int compare(IARESResource o1, IARESResource o2) {
			try {
				Procedure p1 = o1.getInfo(Procedure.class);
				Procedure p2 = o2.getInfo(Procedure.class);
				
				return Integer.parseInt(p1.getObjectId()) - Integer.parseInt(p2.getObjectId());
			} catch (ARESModelException e) {
				e.printStackTrace();
			}catch (NumberFormatException e) {
			}
			return 0;
		}
		
	}
	
}
