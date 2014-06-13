/**
 * Դ�������ƣ�ControlManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.internal.useroption.control;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.registry.CommonMapRegistry;
import com.hundsun.ares.studio.jres.script.ScriptPlugin;

/**
 * �û�ѡ��������� �ؼ����͹���
 * @author sundl
 *
 */
public class ControlManager extends CommonMapRegistry<ControlType>{
	
	private static final Logger logger = Logger.getLogger(ControlManager.class);
	
	private static ControlManager INSTANCE = null;
	
	public static ControlManager getInstance() {
//		if (INSTANCE == null) {
			INSTANCE = new ControlManager();
//		}
		return INSTANCE;
	}
	
	private  ControlManager () {
		init();
	}

	protected String getExtensionPointPluginId() {
		return ScriptPlugin.PLUGIN_ID;
	}
	
	public String getExtensionPointId() {
		return "userOptionControlTypeProvider";
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.registry.CommonMapRegistry#handleConfigElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected void handleConfigElement(IConfigurationElement element) {
		ControlType type = new ControlType(element);
		map.put(type.getId(), type);
	}
	
	public IUserOptionControlProvider getTypeProvider(String type) {
		Collection<ControlType> controlTypes = map.get(type);
		if (controlTypes.size() > 0) {
			ControlType controlType = controlTypes.iterator().next();
			return controlType.getControlProvider();
		}
		return null;
	}
	
}
