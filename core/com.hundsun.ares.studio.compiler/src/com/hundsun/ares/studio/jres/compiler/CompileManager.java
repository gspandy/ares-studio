/**
 * Դ�������ƣ�CompileManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.compiler;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.ArrayListMultimap;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author gongyf
 *
 */
public class CompileManager {
	
	static private final String PRIORITY[] = {
			"Highest","Higher","High","Normal","Low","Lower","Lowest",
	};
	
//	private static class TEClass {
//		public String uri;
//		public String name;
//	}
	
	private static class CompilerFactoryItem implements Comparable<CompilerFactoryItem> {
		public String id;
		public String types;
		public String name;
		public String priority;
		public IResourceCompilerFactory adapter;
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(CompilerFactoryItem o) {
			return ArrayUtils.indexOf(PRIORITY, priority) - ArrayUtils.indexOf(PRIORITY, o.priority);
		}
	}
	
	private static Logger logger = Logger.getLogger(CompileManager.class);
	
	private CompileManager () {
		
		
		loadFactories();
	}
	
	private static CompileManager instance;
	
	/**
	 * @return the instance
	 */
	public static CompileManager getInstance() {
		if (instance == null) {
			instance = new CompileManager();
		}
		return instance;
	}
	
	
	private ArrayListMultimap<String, CompilerFactoryItem> factories = ArrayListMultimap.create();
	
	private void loadFactories() {
		/*
		 * TODO#��Դ����#��Ҷ��#һ��#����#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #��ȡ��չ����Ϣ
		 *
		 * ��ȡ��չ�����Ѿ�ע�����Դ���빤������ʵ�������뵽֧�ֵ���Դ����mapӳ����ȥ
		 */
		logger.info("����JRES������������չ�㡣");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(CompilerPlugin.PLUGIN_ID , ICompilerFactoriyExtentionPoint.EP_Name);
		for (IConfigurationElement element : elements) {
			try {
				CompilerFactoryItem tmp = new CompilerFactoryItem();
				tmp.id = element.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_ID);
				tmp.types = element.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_Types);
				tmp.name = element.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_Name);
				tmp.priority = element.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_Priority);
				tmp.adapter = (IResourceCompilerFactory)element.createExecutableExtension(ICompilerFactoriyExtentionPoint.EP_Attribute_Class);
				
				// ���ҽڵ��¹��ص�Eclass����
				StringBuffer sb = new StringBuffer(StringUtils.defaultString(tmp.types) );
				for (IConfigurationElement childElement : element.getChildren(ICompilerFactoriyExtentionPoint.EP_Element_EClass)) {
					String uri = childElement.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_EClass_Uri);
					String name = childElement.getAttribute(ICompilerFactoriyExtentionPoint.EP_Attribute_EClass_Name);
					
					sb.append(",");
					sb.append(CompileUtils.getEClassCompileType(uri, name));
				}
				
				tmp.types = sb.toString();
				
				for(String item:StringUtils.split(tmp.types,",")){
					if (StringUtils.isNotBlank(item)) {
						factories.put(item, tmp);
					}
				}
				
				logger.info(String.format("JRES������������չ��%s:%s", tmp.id,tmp.types));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		/*
		 * TODO#��Դ����#��Ҷ��#һ��#����#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #����Դ���빤���б��������
		 *
		 * ʹ�ü�¼����Դ���빤�������ȼ�����map��ÿ����Դ���Ͷ�Ӧ����Դ���빤���б�����������ȼ���ߵ���ǰ��
		 * ���ȼ��� Highest > High > Normal > Low > Lowest
		 */
		
	}
	
	public IResourceCompilerFactory getFactoryByType(String resourceType) {
		List<CompilerFactoryItem> result = factories.get(resourceType);
		for (CompilerFactoryItem item : result) {
			return item.adapter;
		}
		return null;
	}
	
	public IResourceCompilerFactory getFactory(Object resource) {
		String type = getCompileType(resource);
		if(null == type){
			return null;
		}
		/*
		 * TODO#��Դ����#��Ҷ��#һ��#����#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #���Ҷ�Ӧ��Դ����Դ���빤��
		 *
		 * �������ȼ����ζ�ȡ��Դ���빤���б�����ָù����Ƿ�֧�ֵ�ǰ��Դ������Ŀ�����֧���򷵻أ���������ʹ�õ���Դ���빤�������ȼ���ߵ�
		 */

		//��ʱȡ��һ��
		List<CompilerFactoryItem> result = factories.get(type);
		if (result == null || result.isEmpty()) {
			return null;
		}
		
		Collections.sort(result);
		
		for (CompilerFactoryItem item : result) {
			//if (item.adapter.isSupport(project)) {
				return item.adapter;
			//}
		}
		
		return null;
	}
	
	
	/**
	 * ���ݲ�ͬ����Դ���ز�ͬ�ı�������
	 * @param resource
	 * @return
	 */
	private String getCompileType(Object resource){
		if(resource instanceof IARESResource){
			return ((IARESResource)resource).getType();
		}
		
		if(resource instanceof IARESModule){
			return CompilerConstants.Compile_Module;
		}

		if (resource instanceof EObject) {
			return CompileUtils.getEObjectCompileType((EObject) resource);
		}
		
		return null;
	}
}


