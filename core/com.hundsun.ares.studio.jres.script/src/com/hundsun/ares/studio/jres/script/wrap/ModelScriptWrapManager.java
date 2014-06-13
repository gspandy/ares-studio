/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.wrap;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.jres.script.ScriptPlugin;
import com.hundsun.ares.studio.jres.script.api.factory.IScriptPoxyFactory;

/**
 * @author lvgao
 *
 */
public class ModelScriptWrapManager {
	
	private static Logger logger = Logger.getLogger(ModelScriptWrapManager.class);
		
	
	private static ModelScriptWrapManager instance = null;
	
	private ModelScriptWrapManager(){
		init();
	}
	
	public static ModelScriptWrapManager getInstance(){
		synchronized (ModelScriptWrapManager.class) {
			if(null == instance){
				instance = new ModelScriptWrapManager();
			}
			return instance;
		}
	}
	
	Map<String, ModelScripWrapItem> fMap = new HashMap<String, ModelScripWrapItem>();
	
	
	private void init(){
		logger.info("���ؽű�ģ�ͷ�װ��չ�㡣");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ScriptPlugin.PLUGIN_ID , IModelScriptWrapExtentionPoint.EP_NAME);
		for (IConfigurationElement element : elements) {
			try {
				ModelScripWrapItem tmp = new ModelScripWrapItem();
				tmp.id = element.getAttribute(IModelScriptWrapExtentionPoint.EP_ATTR_ID);
				tmp.type = element.getAttribute(IModelScriptWrapExtentionPoint.EP_ATTR_TYPE);
				tmp.adapter = (IScriptPoxyFactory)element.createExecutableExtension(IModelScriptWrapExtentionPoint.EP_ATTR_CLASS);
				fMap.put(tmp.id, tmp);
			}catch (Exception e) {
				logger.error(String.format("��ȡ��չ��%s.%sʧ��", ScriptPlugin.PLUGIN_ID,IModelScriptWrapExtentionPoint.EP_NAME));
			}
		}
	}
	
	
	/**
	 * ��ȡ������
	 * @param id
	 * @return
	 */
	public IScriptPoxyFactory getScriptPoxyFactory(String id){
		if(fMap.containsKey(id)){
			return fMap.get(id).adapter;
		}
		return null;
	}

	
}
		
		
class ModelScripWrapItem{
	public String id;
	public String type;
	public IScriptPoxyFactory adapter;
}
