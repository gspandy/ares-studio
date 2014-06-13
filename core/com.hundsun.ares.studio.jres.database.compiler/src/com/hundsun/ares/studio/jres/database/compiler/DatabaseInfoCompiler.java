/**
 * Դ�������ƣ�DatabaseInfoCompiler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.compiler
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.scripting.AresScriptEngineManager;
import com.hundsun.ares.studio.jres.clearinghouse.core.script.impl.SequenceScriptWrapImpl;
import com.hundsun.ares.studio.jres.clearinghouse.core.script.impl.TableScriptWrapImpl;
import com.hundsun.ares.studio.jres.clearinghouse.core.script.impl.TriggerScriptWrapImpl;
import com.hundsun.ares.studio.jres.clearinghouse.core.script.impl.ViewScriptWrapImpl;
import com.hundsun.ares.studio.jres.compiler.CompilationResult;
import com.hundsun.ares.studio.jres.compiler.CompileUtils;
import com.hundsun.ares.studio.jres.compiler.IResourceCompiler;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseResType;
import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;

/**
 * @author gongyf
 *
 */
public class DatabaseInfoCompiler implements IResourceCompiler {

//	ʹ�õĽű����������·���
//	function genDatabaseResource(/*StringBuffer*/ sb, /*DatabaseResourceData*/ info, /*IARESResource*/ res, /*Map<?, ?>*/ context) { ... }
	
	private String functionName;
	
	public DatabaseInfoCompiler(String functionName) {
		super();
		this.functionName = functionName;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.compiler.IResourceCompiler#compile(java.lang.Object, java.util.Map)
	 */
	@Override
	public CompilationResult compile(Object resource,
			Map<Object, Object> context) {

		DBCompilationResult result = new DBCompilationResult();
		
		String database = (String)context.get(IDBConstant.DATABASE_TYPE);
		
		if (database == null || StringUtils.isBlank(database.toString())) {
			result.setSql("���ɴ���,δѡ�����ݿ����ͣ�");
			return result;
		}
		
		IARESResource res = CompileUtils.getARESResource(resource, context);
		DatabaseResourceData info = (DatabaseResourceData) resource;
		
		IARESBundle bundle = res.getBundle();
		if (bundle instanceof IARESProject) {
			IARESProject project = (IARESProject) bundle;
			try {
				Map<String, Object> extContext = new HashMap<String, Object>();
				extContext.put("functionName", functionName);
				
				for (Object key : context.keySet()) {
					if (key instanceof String) {
						extContext.put((String)key, context.get(key));
					}
				}
				
				IARESResource jsResource = DBCompilerUtils.getJSResource(project,resource, database);
				if (jsResource == null) {
					result.setSql("���ݿ�ű�������");
					return result;
				}
				Object returnSql = runScript(ScriptUtils.MODE_BUILDER, jsResource, res, info, getClass().getClassLoader(), extContext);
				result.setSqlByUser((Map<String, StringBuffer>) returnSql);
			} catch (Exception e) {
				result.setSql("�ű�����ʧ�ܣ�" + e.getMessage());
			}
		} else {
			result.setSql("Ŀǰ��֧�������ð�������sql");
		}

		return result;
	}

	@Override
	public void clean(Object resource, Map<Object, Object> context) {
		
	}

	/**
	 * Ԥ�����ݿ�ű�
	 * 
	 * @param mode
	 * @param jsResource
	 * @param res
	 * @param info
	 * @param loader
	 * @param extContext
	 * @throws IOException
	 * @throws CoreException
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	private Object runScript (  int mode,
			IARESResource jsResource,
			IARESResource res,
			JRESResourceInfo info,
			ClassLoader loader, 
			Map<String, Object> extContext)throws IOException, CoreException, ScriptException, NoSuchMethodException{
		
		Object sql = "";
		ScriptEngine engine = AresScriptEngineManager.getScriptEngineManager().getEngineByName(ARESCore.SCRIPT_ENGINE_NAME);
		InputStream stream = null;
		try {
			stream = jsResource.openStream();
			String script = null;
			IResource jsRes = jsResource.getResource();
			
			if (jsRes instanceof IFile) {
				script = IOUtils.toString(stream, ((IFile) jsRes).getCharset());
			} else {
				script = IOUtils.toString(stream, "UTF-8");
			}
			
			// ����Ĭ�Ͻű�������
			Map<String, Object> defaultContext = ScriptUtils.createDefaultScriptContext(mode,jsResource, res,info, loader);
			if (extContext != null)
				defaultContext.putAll(extContext);
			defaultContext.put(ScriptUtils.MODE, mode);
			engine.setBindings(ScriptUtils.toScriptBindings(defaultContext), ScriptContext.ENGINE_SCOPE);
			engine.eval(script);

			if (StringUtils.equals(res.getType(), IDatabaseResType.Table)){
				sql = ((Invocable) engine).invokeFunction(functionName, new TableScriptWrapImpl(res), defaultContext);
			}else if (StringUtils.equals(res.getType(), IDatabaseResType.View)) {
				sql = ((Invocable) engine).invokeFunction(functionName, new ViewScriptWrapImpl(res), defaultContext);
			}else if (StringUtils.equals(res.getType(), IDatabaseResType.Sequence)) {
				sql = ((Invocable) engine).invokeFunction(functionName,new SequenceScriptWrapImpl(res), defaultContext);
			}else if (StringUtils.equals(res.getType(), IDatabaseResType.Trigger)) {
				sql = ((Invocable) engine).invokeFunction(functionName,new TriggerScriptWrapImpl(res), defaultContext);
			}
		}catch (Exception e) {
			//e.printStackTrace();
			ConsoleHelper.getLogger().error("ִ�нű�" + jsResource.getPath() + "����...", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
		if (sql instanceof Map) {
			return (Map<String, StringBuffer>) sql;
		}else {
			Map<String,StringBuffer> rs = new LinkedHashMap<String,StringBuffer>();
			rs.put("", new StringBuffer(sql.toString()));
			return rs;
		}
	}
	
}
