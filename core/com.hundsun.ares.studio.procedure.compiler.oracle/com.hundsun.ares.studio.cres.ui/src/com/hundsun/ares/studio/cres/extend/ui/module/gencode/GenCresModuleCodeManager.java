/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.extend.ui.module.gencode;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.cres.constant.ICresUIConstant;

/**
 * @author qinyuan
 *
 */
public class GenCresModuleCodeManager {
	
	private Logger logger = Logger.getLogger(GenCresModuleCodeManager.class);
	
	private static GenCresModuleCodeManager instance = null;
	
	private GenCresModuleCodeManager() {
		init();
	}
	
	/**
	 * @return the instance
	 */
	public static GenCresModuleCodeManager getInstance() {
		synchronized (GenCresModuleCodeManager.class) {
			if(null == instance){
				instance = new GenCresModuleCodeManager();
			}
			return instance;
		}
	}
	
	private HashSet<IGenCresModuleCode> set = new HashSet<IGenCresModuleCode>();

	private void init(){
		logger.info("����CRESģ�����������չ�㡣");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ICresUIConstant.PLUGIN_ID , IGenCresModuleCodeExtentionPoint.EP_NAME);
		for (IConfigurationElement element : elements) {
			try {
				GenCresModuleCodeItem tmp = new GenCresModuleCodeItem();
				tmp.id = element.getAttribute(IGenCresModuleCodeExtentionPoint.EP_ATTR_ID);
				tmp.type = element.getAttribute(IGenCresModuleCodeExtentionPoint.EP_ATTR_TYPE);
				tmp.adapter = (IGenCresModuleCode)element.createExecutableExtension(IGenCresModuleCodeExtentionPoint.EP_ATTR_CLASS);
				set.add(tmp.adapter);
			}catch (Exception e) {
				logger.error(String.format("��ȡ��չ��%s.%sʧ��", ICresUIConstant.PLUGIN_ID,IGenCresModuleCodeExtentionPoint.EP_NAME));
			}
		}
	}
	
	public HashSet<IGenCresModuleCode> getSet() {
		return set;
	}
}

class GenCresModuleCodeItem{
	public String id;
	public String type;
	public IGenCresModuleCode adapter;
}
