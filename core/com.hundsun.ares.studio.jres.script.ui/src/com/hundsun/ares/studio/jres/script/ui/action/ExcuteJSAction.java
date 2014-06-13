/**
 * Դ�������ƣ�ExcuteJSAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.script.ui.action;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.jres.script.ui.Activator;
import com.hundsun.ares.studio.ui.console.ARESConsoleFactory;

/**
 * ִ��js�ű��˵�
 * @author wangxh
 */
public class ExcuteJSAction extends AbstractHandler implements IObjectActionDelegate,
		IWorkbenchWindowActionDelegate  {
	private static Logger logger = Logger.getLogger(ExcuteJSAction.class);
	/**
	 * ѡ�����Դ����
	 */
	private IARESResource resource;
	
	@Override
	public void run(IAction action) {
		InputStream in = null;
		
		try {
			// �Զ��򿪿���̨
			try {
				ARESConsoleFactory.openARESConsole();
			}catch (Exception e) {}
			
			ScriptUtils.excuteJS(ScriptUtils.MODE_CONTEXT_MENU, resource, null, null, getClass().getClassLoader(), null);
		} catch (NoSuchMethodException e1) {
			logger.error("ִ�нű�����", e1);
			ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"�ű�ִ�д���",
					"�ű��в�����main����", 
					new Status(Status.ERROR, Activator.PLUGIN_ID, "�ű��в�����main����", e1));
		} catch (Exception e) {
			logger.error("ִ�нű�����", e);
			ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"�ű�ִ�д���", 
					e.getMessage(),
					new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} finally {
			IOUtils.closeQuietly(in);
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		resource = null;
		if(selection instanceof IStructuredSelection){
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			 if(obj instanceof IARESResource){
				resource = (IARESResource) obj;
			}
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		resource = null;
		Object obj = event.getApplicationContext();
		if (obj instanceof EvaluationContext) {
			Object select = ((EvaluationContext) obj).getDefaultVariable();
			if ((select instanceof List) && ((List) select).size() == 1) {
				Object item = ((List) select).get(0);
				if (item instanceof IARESResource && StringUtils.equals(((IARESResource) item).getType(), "js")) {
					resource = (IARESResource) item;
				}
			}
		}
		if (resource != null && resource.exists()) {
			run(null);
		}
		return null;
	}

}
