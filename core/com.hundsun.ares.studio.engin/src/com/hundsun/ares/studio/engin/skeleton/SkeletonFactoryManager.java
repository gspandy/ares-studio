package com.hundsun.ares.studio.engin.skeleton;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineConstant;


public class SkeletonFactoryManager {
	
	private static Logger logger = Logger.getLogger(SkeletonFactoryManager.class);
		
	
//	com.hundsun.ares.studio.uft.macrotokenhandlerfactory
//	com.hundsun.ares.studio.uft.skeletonfactory
	
	private static SkeletonFactoryManager instance = null;
	
	private SkeletonFactoryManager(){
		init();
	}
	
	public static SkeletonFactoryManager getInstance(){
		synchronized (SkeletonFactoryManager.class) {
			if(null == instance){
				instance = new SkeletonFactoryManager();
			}
			return instance;
		}
	}
	
	Map<String, SkeletonFactoryItem> fMap = new HashMap<String, SkeletonFactoryItem>();
	
	
	private void init(){
		logger.info("����UFT��ͼ������չ�㡣");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(IEngineConstant.PLUGIN_ID , ISkeletonFactoryExtentionPoint.EP_NAME);
		for (IConfigurationElement element : elements) {
			try {
				SkeletonFactoryItem tmp = new SkeletonFactoryItem();
				tmp.id = element.getAttribute(ISkeletonFactoryExtentionPoint.EP_ATTR_ID);
				tmp.type = element.getAttribute(ISkeletonFactoryExtentionPoint.EP_ATTR_TYPE);
				tmp.adapter = (ISkeletonFactory)element.createExecutableExtension(ISkeletonFactoryExtentionPoint.EP_ATTR_CLASS);
				fMap.put(tmp.type, tmp);
			}catch (Exception e) {
				logger.error(String.format("��ȡ��չ��%s.%sʧ��", IEngineConstant.PLUGIN_ID,ISkeletonFactoryExtentionPoint.EP_NAME));
			}
		}
	}
	
	/**
	 * ��ȡ��ͼ����
	 * @param resource
	 * @return
	 */
	public String getSkeletonType(Object resource){
		if(resource instanceof IARESResource){
			return ((IARESResource) resource).getType();
		}
		return null;
	}
	
	/**
	 * ��ȡ��ͼ����
	 * @param type
	 * @return
	 */
	public ISkeletonFactory getSkeletonFactory(String type){
		if(fMap.containsKey(type)){
			return fMap.get(type).adapter;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public ISkeletonFactory getSkeletonFactory(Object resource){
		String type = getSkeletonType(resource);
		return getSkeletonFactory(type);
	}
	
}
		
		
class SkeletonFactoryItem{
	public String id;
	public String type;
	public ISkeletonFactory adapter;
}
