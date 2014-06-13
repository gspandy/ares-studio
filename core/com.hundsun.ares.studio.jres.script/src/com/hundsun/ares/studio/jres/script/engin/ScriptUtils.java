/**
 * Դ�������ƣ�ScriptUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.engin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.scripting.AresScriptEngineManager;
import com.hundsun.ares.studio.jres.script.util.impl.ARESProjectScriptWrapImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptCalendarUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptFileUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptInUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptOutUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptReferenceUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptStringUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptSysUtilImpl;
import com.hundsun.ares.studio.jres.script.util.impl.ScriptXMLUtilImpl;

/**
 * �ű������ࡣ
 * 
 * @author mawb
 * @author sundl
 *
 */
public class ScriptUtils {
	
	private static Logger logger = Logger.getLogger(ScriptUtils.class);
	public static final String MAIN = "main";
	public static final String MODE = "mode";
	public static final int MODE_CONTEXT_MENU = 1;
	public static final int MODE_EDITOR_BUTTON = 2;
	public static final int MODE_BUILDER = 3;
	public static final int MODE_CMD_BUILDER = 4;//�����б���
	
	private static final ScriptUtils INSTANCE = new ScriptUtils();
	
	static final Logger console = ConsoleHelper.getLogger();
	
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
	public static void excuteJS(  int mode,
									IARESResource jsResource,
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
			ScriptInUtilImpl input = (ScriptInUtilImpl) defaultContext.get("input");
			if (extContext != null)
				defaultContext.putAll(extContext);
			defaultContext.put(ScriptUtils.MODE, mode);
			engine.setBindings(ScriptUtils.toScriptBindings(defaultContext), ScriptContext.ENGINE_SCOPE);
			engine.eval(script);
			if(input.getInput()==Window.CANCEL){
				return;
			}
			Job job = new Job("") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask(jsRes.getName() +"   ����ִ��...", IProgressMonitor.UNKNOWN);
						((Invocable) engine).invokeFunction(MAIN, defaultContext);
						monitor.done();
					} catch (ScriptException e) {
						logger.error("�ű�ִ�г���...", e);
						ConsoleHelper.getLogger().error("ִ�нű�����...", e);
					} catch (NoSuchMethodException e) {
						logger.error("�ű�ִ�г���...", e);
						ConsoleHelper.getLogger().error("ִ�нű�����...", e);
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
	 * ����һ��Ĭ�ϵĽű�������
	 * @return
	 */
	public static Map<String, Object> createDefaultScriptContext(int mode,IARESResource jsResource,IARESResource res, JRESResourceInfo info, ClassLoader loader) {
		Map<String, Object> context = new HashMap<String, Object>();

		
		if (jsResource != null){
			context.put("project",new ARESProjectScriptWrapImpl( jsResource.getARESProject()));
		}
	
//		model = 3;
		context.put("stringutil", ScriptStringUtilImpl.instance);
		context.put("input", new ScriptInUtilImpl(mode, jsResource,context));
		context.put("output",new ScriptOutUtilImpl(mode));
		context.put("file", ScriptFileUtilImpl.instance);
		context.put("xml", ScriptXMLUtilImpl.instance);
		context.put("sys", ScriptSysUtilImpl.instance);
		context.put("calendar", ScriptCalendarUtilImpl.instance);
		context.put("reference", ScriptReferenceUtilImpl.instance);//�������ù�����
		context.put("logger", logger);
		return context;
	}
	
	/**
	 * ת��Ϊ�ű�����ʹ�õ�������
	 * @param context
	 * @return
	 */
	public static Bindings toScriptBindings(Map<String, Object> context) {
		return new SimpleBindings(context);
	}
	


    
}
