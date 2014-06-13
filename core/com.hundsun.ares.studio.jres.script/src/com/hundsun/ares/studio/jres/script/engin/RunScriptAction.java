/**
 * Դ�������ƣ�RunScriptAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.engin;

import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.scripting.AresScriptEngineManager;
import com.hundsun.ares.studio.jres.script.ScriptPlugin;

/**
 * @author gongyf
 */
public class RunScriptAction extends Action  {
	protected String script;
	protected String functionName;
	protected Map<String, Object> context;
	
	@Override
	public String getId() {
		return toString();
	}
	
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	
	public String getScript() {
		return script;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		ScriptEngine engine = AresScriptEngineManager.getScriptEngineManager().getEngineByName(ARESCore.SCRIPT_ENGINE_NAME);
		try {
			engine.setBindings(ScriptUtils.toScriptBindings(context), ScriptContext.ENGINE_SCOPE);
			engine.eval(script);
			if(null!=functionName &&!"".equals(functionName)){
				((Invocable) engine).invokeFunction(functionName, context);
			}
		} catch (Exception e) {
			ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"�ű�ִ�д���", e.getMessage(), new Status(Status.ERROR, ScriptPlugin.PLUGIN_ID, e.getMessage(), e));
		}
	}
	
}
