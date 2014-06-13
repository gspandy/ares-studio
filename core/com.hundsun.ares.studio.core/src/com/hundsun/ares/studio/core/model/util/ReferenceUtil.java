/**
 * Դ�������ƣ�ReferenceUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.IReferenceProvider;
import com.hundsun.ares.studio.core.model.IReferenceProvider2;
import com.hundsun.ares.studio.core.model.Reference;

/**
 * @author gongyf
 *
 */
public class ReferenceUtil {

	public static ReferenceUtil INSTANCE = new ReferenceUtil();
	
	private ReferenceUtil() {
		
	}
	
	/**
	 * ʹ��һ�ֽ�����ֻ��һ��ʵ��
	 */
	private Map<String, IReferenceParser> parserCache = new HashMap<String, IReferenceParser>();
	
	protected IReferenceParser getParser(String className) {
		
		IReferenceParser parser = parserCache.get(className);
		if (parser == null) {
			
//			IExtensionPoint extension = Platform.getExtensionRegistry().getConfigurationElementsFor(namespace, extensionPointName);
//			
//			.getExtensionPoint(PLUGIN_ID + ".adapterPlugins");
//			try {
//				Class<?> clazz = loader.loadClass(className);
//				parserCache.put(className, parser = (IReferenceParser) clazz.newInstance());
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
		}
		return parser;
	}
	
	/**
	 * ���õ�Ԫ��һ����������ж�����õ�Ԫ<BR>
	 * ���õ�Ԫ�ܹ���ȡ�����Ԫ����������������
	 * @author gongyf
	 *
	 */
	static class ReferenceUnit {
		private EStructuralFeature feature;
		private IReferenceParser parser;
		private String[] parameters;
		
		/**
		 * @param parser
		 * @param feature
		 * @param parameters
		 */
		public ReferenceUnit(IReferenceParser parser,
				EStructuralFeature feature, String[] parameters) {
			super();
			this.parser = parser;
			this.feature = feature;
			this.parameters = parameters;
		}

		public List<Reference> analyse(EObject object) {
			return parser.analyse(object, feature, parameters);
		}
	}
	private Multimap<EClass, ReferenceUnit> unitMap = HashMultimap.create();

	/**
	 * ��ȡһ������ӵ�е�����
	 * @param object
	 * @return
	 */
	public List<Reference> getReferences(EObject object) {
		EClass clazz = object.eClass();
		
		Collection<ReferenceUnit> untis = unitMap.get(clazz);
		if (untis == null || untis.isEmpty()) {
			EAnnotation anno = clazz.getEAnnotation(Constants.EANNOTATION_REF_SOURCE);
			if (anno != null) {
				for (Entry<String, String> entry : anno.getDetails().entrySet()) {
					// �������õ��ֶκ����ý�����
					EStructuralFeature feature = clazz.getEStructuralFeature(entry.getKey());
					if (feature != null) {
						String[] strings = StringUtils.split(entry.getValue());
						IReferenceParser parser = getParser(strings[0]);
						String[] parameters = null;
						if (strings.length > 1) {
							parameters = new String[strings.length - 1];
							System.arraycopy(strings, 1, parameters, 0, strings.length - 1);
						}

						ReferenceUnit unit = new ReferenceUnit(parser, feature, parameters);
						unitMap.put(clazz, unit);
					}
				}
			}
			untis = unitMap.get(clazz);
		}
		
		List<Reference> references = Collections.emptyList(); 
		if (untis != null) {
			references = new ArrayList<Reference>();
			for (ReferenceUnit unit : untis) {
				references.addAll(unit.analyse(object));
			}
		}
		
		return references;

	}
	
	/**
	 * ĳЩ��������£�����reference��Ҫproject����������߷����ṩһ��ͳһ�ķ�������ȡreference
	 * @param obj 
	 * @param project ����оʹ��룬û�п���Ϊnull
	 * @return
	 */
	public List<Reference> getReferences(Object obj, IARESProject project) {
		List<Reference> references = null;
		if (project != null) {
			// ��project���������£����ȳ���IReferenceProvider2
			IAdapterManager manager = Platform.getAdapterManager();
			IReferenceProvider2 provider = (IReferenceProvider2) manager.getAdapter(obj, IReferenceProvider2.class);
			if (provider != null) {
				references = provider.getReferences(obj, project);
			}
		} 
		
		// û��project����IReferenceProvider2ʧ�ܣ��ٳ���
		if (references == null) {
			if (obj instanceof IReferenceProvider) {
				references = ((IReferenceProvider) obj).getReferences();
			}
		}
		
		if (references == null) {
			references = Collections.emptyList();
		}
		return references;
	}
	
}
