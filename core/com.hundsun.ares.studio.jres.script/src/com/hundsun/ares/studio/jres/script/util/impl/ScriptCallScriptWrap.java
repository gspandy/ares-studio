/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.scripting.AresScriptEngineManager;
import com.hundsun.ares.studio.jres.script.ScriptPlugin;
import com.hundsun.ares.studio.jres.script.api.wrap.IScriptCallScriptWarp;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.jres.script.util.wizard.ScriptGenInteWizardModel;
import com.hundsun.ares.studio.jres.script.util.wizard.ScriptGenIntegratedWizard;

/**
 * �ű����ýű�API��װ
 * @author liaogc
 *
 */
public class ScriptCallScriptWrap implements IScriptCallScriptWarp{
	private static final Logger console = ConsoleHelper.getLogger();
	private static Logger logger = Logger.getLogger(ScriptCallScriptWrap.class);
	public static final String MAIN = "main";
	public static final String MODE = "mode";
	public static final int MODE_CONTEXT_MENU = 1;
	public static final int MODE_EDITOR_BUTTON = 2;
	public static final int MODE_BUILDER = 3;
    private IARESProject project ;//�����߽ű�
    private ScriptGenIntegratedWizard wizard= null;
    private List<ScriptGenInteWizardModel> scriptModelList = new ArrayList<ScriptGenInteWizardModel>();
	public ScriptCallScriptWrap(IARESProject project ){
		this.project = project;
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.util.IScriptCallScriptUitl#runSrcipt(java.lang.String[])
	 */
	@Override
	public void runSrcipt(String[][] scripts) {
		if(null == scripts){
			return ;
		}
		
		for(String[] script:scripts){
			try {
				IARESResource calledJSResource = getJSResource(script[1]);//��ñ����ýű�
				
				if(calledJSResource==null){
					console.info(script[1]+"�ű�������!");
					return ;
				}
				ScriptGenInteWizardModel scriptModel = new ScriptGenInteWizardModel(script[0],script[1]);
				scriptModel.setJsResource(calledJSResource);
				scriptModelList.add(scriptModel);
				
				
			} catch (Exception e) {
				console.error(e.getMessage());
			}
		}
		
		if (Display.getDefault().getThread() != Thread.currentThread()) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					createIntegratedWizard();
					if (!wizard.isOK()) {
						return;
					}
				}
			});
		} else {

			createIntegratedWizard();
			if (!wizard.isOK()) {
				return;
			}
		}
		if (!wizard.isOK()) {
			return;
		}
		for(ScriptGenInteWizardModel selectedModel :wizard.getSelectedElements()){
			IARESResource calledJSResource = selectedModel.getJsResource();
			try {
				excuteJS(ScriptUtils.MODE_BUILDER, calledJSResource, null, null, getClass().getClassLoader(), wizard.getContextByJsResource(calledJSResource));
			} catch (NoSuchMethodException e1) {
				String baseScriptMessage = "ִ�нű�����("+calledJSResource.getElementName()+")";
				logger.error(baseScriptMessage, e1);
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						baseScriptMessage,
						"�ű��в�����main����", 
						new Status(Status.ERROR, ScriptPlugin.PLUGIN_ID, "�ű��в�����main����", e1));
			} catch (Exception e) {
				String baseScriptMessage = "ִ�нű�����("+calledJSResource.getElementName()+")";
				logger.error("ִ�нű�����", e);
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), baseScriptMessage, 
						e.getMessage(),
						new Status(Status.ERROR, ScriptPlugin.PLUGIN_ID, e.getMessage(), e));
			}
		
		}
		
	}
	
	/**
	 * ִ��js�ű��е�main����
	 * @param mode ģʽ 1�����Ҽ�ִ�У�2����༭���еİ�ť������3����builder
	 * @param jsResource �ű���Դ
	 * @param res        ��Դ��������null�����ض���ģʽ�����ض��Ľű��вŻ���
	 * @param info	  	    ��Դ��������null�����ض���ģʽ�����ض��Ľű��вŻ���
	 * @param loader     classloader
	 * @param extContext �����context����,���û�п���Ϊnull; Ҳ�������ڸ���Ĭ�ϵ�context�е�ֵ�����á�
	 * @throws IOException
	 * @throws CoreException
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 * @see MODE_CONTEXT_MENU
	 * @see MODE_EDITOR_BUTTON
	 * @see MODE_BUILDER
	 */
	private void excuteJS(  int mode,
									final IARESResource jsResource,
									IARESResource res,
									JRESResourceInfo info,
									ClassLoader loader, 
									Map<String, Object> extContext) 
									throws IOException, CoreException, ScriptException, NoSuchMethodException {
		final ScriptEngine engine = AresScriptEngineManager.getScriptEngineManager().getEngineByName(ARESCore.SCRIPT_ENGINE_NAME);
		InputStream stream = null;
		if(jsResource == null || !jsResource.exists()){
			throw new FileNotFoundException("js�ű��ļ������ڣ�");
		}
		try {
			stream = jsResource.openStream();
			String script = null;
			final IResource jsRes = jsResource.getResource();
			
			if (jsRes instanceof IFile) {
				script = IOUtils.toString(stream, ((IFile) jsRes).getCharset());
			} else {
				script = IOUtils.toString(stream, "UTF-8");
			}
			
			// ����Ĭ�Ͻű�������
			final Map<String, Object> defaultContext = ScriptUtils.createDefaultScriptContext(mode,jsResource, res,info, loader);
			if (extContext != null)
				defaultContext.putAll(extContext);
			defaultContext.put(ScriptUtils.MODE, mode);
			engine.setBindings(ScriptUtils.toScriptBindings(defaultContext), ScriptContext.ENGINE_SCOPE);
			engine.eval(script);
	
			
			Job job = new Job("") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask(jsRes.getName() +"   ����ִ��...", IProgressMonitor.UNKNOWN);
						((Invocable) engine).invokeFunction(MAIN, defaultContext);
						monitor.done();
					} catch (ScriptException e) {
						logger.error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
						ConsoleHelper.getLogger().error("ִ�нű�����...", e);
					} catch (NoSuchMethodException e) {
						logger.error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
						ConsoleHelper.getLogger().error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
					}
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	/**
	 * ���ݽű�·�� ��ö�Ӧ�Ľű���Դ
	 * @param path(����ڹ�����Դ)
	 * @return
	 * @throws Exception
	 */
	private   IARESResource getJSResource(String path) throws Exception {
		if(StringUtils.isBlank(path)){
			throw new Exception("�ű�·��Ϊ��!");
		}
		IARESModuleRoot tools = project.getModuleRoot("tools");
		IARESResource[] toolsResources = tools.getResources();
		for(IARESResource resource:toolsResources){
			if(StringUtils.lastIndexOf(resource.getResource().getFullPath().toString(), path)>-1){
				return resource;
			}
		}
	
			return null;
	}
	
	/**
	 * ����ͳһ��
	 */
	private void createIntegratedWizard(){
		wizard = new ScriptGenIntegratedWizard();
		wizard.setInputScriptList(scriptModelList);
		wizard.setProject(project);
		WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		Point point = new Point(640, 480);
		wd.setMinimumPageSize(point);
		wd.setPageSize(point);
		wd.create();
		wd.open();
	}

}
