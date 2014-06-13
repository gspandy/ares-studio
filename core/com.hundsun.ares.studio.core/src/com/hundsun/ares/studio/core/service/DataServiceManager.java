/**
 * Դ�������ƣ�DataServiceManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.core.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * @author gongyf
 *
 */
public class DataServiceManager {
	
	static private class ServiceFactory {
		public String id;
		public Class<? extends IDataService> type;
		public IDataServiceFactory factory;
	}
	
	
	private static DataServiceManager instance;
	private DataServiceManager() {
		loadFactories();
	}
	
	public static DataServiceManager getInstance() {
		if (instance == null) {
			instance = new DataServiceManager();
		}
		return instance;
	}
	
	private Logger logger = Logger.getLogger(getClass());
	private Map<Class<? extends IDataService>, ServiceFactory> factoryMap = new HashMap<Class<? extends IDataService>, ServiceFactory>();
	private void loadFactories() {
		logger.info("����JRES���ݷ�����չ��");
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(IServiceExtentionPoint.NAMESPACE, IServiceExtentionPoint.EP_Name);
		for (IConfigurationElement element : elements) {
			try {
				ServiceFactory sf = new ServiceFactory();
				
				sf.id = element.getAttribute(IServiceExtentionPoint.EP_Attribute_ID);
				sf.factory = (IDataServiceFactory) element.createExecutableExtension(IServiceExtentionPoint.EP_Attribute_Factory);
				sf.type = (Class<? extends IDataService>) sf.factory.getClass().getClassLoader().loadClass(element.getAttribute(IServiceExtentionPoint.EP_Attribute_Type));
				
				factoryMap.put(sf.type, sf);
			
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	/**
	 * ��ȡһ����Ϣ����ÿ�λ�ȡ���᷵���µķ������<BR>
	 * ���û����Ӧ�Ĵ��������򷵻�null
	 * @param project
	 * @param serviceClass
	 * @return
	 */
	public <T extends IDataService> T getService(IARESProject project, Class<T> serviceClass)  {
		ServiceFactory sf = factoryMap.get(serviceClass);
		if (sf == null || sf.factory == null) {
			throw new UnsupportedOperationException("û���ҵ������ͷ���Ĵ�������");
		}
		return (T) sf.factory.createService(project);
	}
	
	
}


