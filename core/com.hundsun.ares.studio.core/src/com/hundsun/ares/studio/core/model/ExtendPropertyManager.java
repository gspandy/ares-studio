/**
 * Դ�������ƣ�ExtensibleModelManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor;
import com.hundsun.ares.studio.core.model.extend.IExtendedPropertyProvider;
import com.hundsun.ares.studio.core.model.extend.IExtendedPropertyProvider2;

/**
 * ��չģ����ص�
 * @author sundl
 */
public class ExtendPropertyManager {
	
	private static final Logger logger = Logger.getLogger(ExtendPropertyManager.class);

	public List<IBasicExtendPropertyDescriptor> getExtensibleProperties(EClass eClass) {
		List<IBasicExtendPropertyDescriptor> result = new ArrayList<IBasicExtendPropertyDescriptor>();
		return result;
	}
	
	private final static String EP_NAME = "extendPropertyProvider";
	private final static String EP_ATTRIBUTE_ID = "id";
	private final static String EP_ATTRIBUTE_Uri = "uri";
	private final static String EP_ATTRIBUTE_EClass = "eClass";
	private final static String EP_ATTRIBUTE_Order = "order";
	private final static String EP_ATTRIBUTE_Class = "class";
	
	// EClass �� Provider��Map
	private Multimap<EClass, IExtendedPropertyProvider> providerMap = ArrayListMultimap.create();
	// ʵ��Provider2�ӿڵģ�һ�㲻ָ��EClass���Ե�������
	private List<IExtendedPropertyProvider2> provider2List = new ArrayList<IExtendedPropertyProvider2>();
	
	private static ExtendPropertyManager INSTANCE;
	public static ExtendPropertyManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ExtendPropertyManager();
		return INSTANCE;
	}
	
	private ExtendPropertyManager() {
		loadExtensionPoints();
	}
	
	private void loadExtensionPoints() {
		logger.info("��ʼ������չģ�ͱ༭֧����չ��");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESCore.PLUGIN_ID , EP_NAME);
		
		for (IConfigurationElement element : elements) {
			try {
				String id = element.getAttribute(EP_ATTRIBUTE_ID);
				String uri = element.getAttribute(EP_ATTRIBUTE_Uri);
				String eclass = element.getAttribute(EP_ATTRIBUTE_EClass);
				
				//d.order = NumberUtils.toInt(element.getAttribute(EP_ATTRIBUTE_Order), Integer.MAX_VALUE);
				//d.editingSupport = (IExtensibleModelEditingSupport) element.createExecutableExtension(EP_ATTRIBUTE_Class);
				
				IExtendedPropertyProvider obj = (IExtendedPropertyProvider) element.createExecutableExtension(EP_ATTRIBUTE_Class);
				if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(eclass)) {
					if (obj instanceof IExtendedPropertyProvider2) {
						provider2List.add((IExtendedPropertyProvider2) obj);
						continue;
					} else {
						logger.warn(String.format("��չģ��ע��URI��EClass��дΪ�գ����ڲ��:%s, ID:%s", element.getContributor().getName(), id));
						break;
					}
				}
				
				EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(uri);
				if (ePackage == null) {
					logger.warn(String.format("���%sע����չ��%s, �Ҳ�����д��uri��%s", element.getContributor().getName(), id, uri));
				}
				EClass eClass = (EClass) ePackage.getEClassifier(eclass);
				
				providerMap.put(eClass, (IExtendedPropertyProvider) obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("����������չģ�ͱ༭֧����չ��");
	}
	
	public List<IBasicExtendPropertyDescriptor> getExtendedProperties(IARESElement element, EClass eclass) {
		List<IBasicExtendPropertyDescriptor> result = new ArrayList<IBasicExtendPropertyDescriptor>();
		for (IExtendedPropertyProvider provider : providerMap.get(eclass)) {
			if (provider.isEnabled(element, eclass)) {
				result.addAll(Arrays.asList(provider.getExtendProperties()));
			}
		}
		
		for (IExtendedPropertyProvider2 provider : provider2List) {
			result.addAll(Arrays.asList(provider.getExtendProperties(element, eclass)));
		}
		return result;
	}
	
}
