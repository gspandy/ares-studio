/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.scripting.AresScriptEngineManager;
import com.hundsun.ares.studio.jres.script.ScriptPlugin;
import com.hundsun.ares.studio.jres.script.api.wrap.IScriptCmdBuilderWarp;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionConfigReader;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionRoot;
import com.hundsun.ares.studio.jres.script.util.wizard.ScriptGenInteWizardModel;

/**
 * �����б���ű���װ
 * @author liaogc
 *
 */
public class ScriptCmdBuilderWarp implements IScriptCmdBuilderWarp{

	private static final Logger console = ConsoleHelper.getLogger();/*ARES����̨*/
	private static Logger logger = Logger.getLogger(ScriptCallScriptWrap.class);/*��־*/
    private IARESProject project ;/*�ű�����*/
    private List<ScriptGenInteWizardModel> scriptModelList = new ArrayList<ScriptGenInteWizardModel>();/*�ű�*/
    private Map<IARESResource,Map<String, Object>>contexts =  new HashMap<IARESResource,Map<String, Object>>();// �ܵ�������
	public ScriptCmdBuilderWarp(IARESProject project ){
		this.project = project;
	}
	
	@Override
	public void build(String[][] scripts,Map<String,Object> parameters) {
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
		loadConfigXML();//�ӽű���Ӧ��������Ϣ
		
		for(ScriptGenInteWizardModel model :scriptModelList){
			IARESResource calledJSResource = model.getJsResource();
			try {
				excuteJS(calledJSResource, null, null, getClass().getClassLoader(), contexts.get(calledJSResource));
			} catch (NoSuchMethodException e1) {
				String baseScriptMessage = "ִ�нű�����("+calledJSResource.getElementName()+")";
				logger.error(baseScriptMessage, e1);
			} catch (Exception e) {
				String baseScriptMessage = "ִ�нű�����("+calledJSResource.getElementName()+")";
				logger.error(baseScriptMessage, e);
			}
		
		}
		
	}
	/**
	 * ���ؽű��ű���Ӧ��������Ϣ
	 */
	private void loadConfigXML(){
		if(scriptModelList==null || scriptModelList.size()==0){
			return ;
		}
		for(ScriptGenInteWizardModel model :scriptModelList ){
			Map<String, Object>context = new HashMap<String, Object>();
			String filepath = String.format("/%s.xml", model.getJsResource().getName());
			
			//2013��5��13��9:27:22 �ű���ģ�飬xml�ļ����ܲ���Ĭ��ģ�����棬��Ҫȡ�ű�ֱ�����ڵ�ģ��Ŀ¼
			IFolder rootFolder = (IFolder) model.getJsResource().getParent().getResource();
			
			if(rootFolder == null){
				console.error(String.format(".respath�в�������չ��[%s]��Ӧ��ģ������á�", "com.hundsun.ares.studio.jres.moduleroot.tools"));
				return ;
			}
			IFile file =  rootFolder.getFile(filepath);
			Map<String, Object>jsContext = new HashMap<String, Object>();
			if(!file.exists()){
				if(context!=null){
					jsContext.putAll(context);
					contexts.put(model.getJsResource(), jsContext);
				}
				continue;
			}
			UserOptionConfigReader instance = new UserOptionConfigReader();
			try {
				final UserOptionRoot root = instance.read(file.getContents());
				root.setDefaultValue();
				model.setOptionRoot(root);
				jsContext.putAll(root.getOptionValue());
				contexts.put(model.getJsResource(), jsContext);
				
			} catch (Exception e) {
				e.printStackTrace();
				console.error(String.format("��ȡ�û�����ʧ�ܣ���ϸ��Ϣ:%s", e.getMessage()));
			}
				
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
	 * ִ��js�ű��е�main����
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
	private void excuteJS( final IARESResource jsResource,
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
			final Map<String, Object> defaultContext = ScriptUtils.createDefaultScriptContext(ScriptUtils.MODE_CMD_BUILDER,jsResource, res,info, loader);
			if (extContext != null)
				defaultContext.putAll(extContext);
			defaultContext.put(ScriptUtils.MODE, ScriptUtils.MODE_CMD_BUILDER);
			engine.setBindings(ScriptUtils.toScriptBindings(defaultContext), ScriptContext.ENGINE_SCOPE);
			engine.eval(script);
			

			try {
				console.info(jsRes.getName() +"   ����ִ��...");
				((Invocable) engine).invokeFunction(ScriptUtils.MAIN, defaultContext);
				console.info(jsRes.getName() +"   ���");
			} catch (ScriptException e) {
				logger.error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
				ConsoleHelper.getLogger().error("ִ�нű�����...", e);
			} catch (NoSuchMethodException e) {
				logger.error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
				ConsoleHelper.getLogger().error("�ű�ִ�г���"+"("+jsResource.getElementName()+")...", e);
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	


}
