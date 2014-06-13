/**
 * Դ�������ƣ�ExtensibleModelManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;

/**
 * @author gongyf
 *
 */
public class ExtensibleModelManager {
	
	private final static String EP_NAME = "ExtensibleModelEditingSupports";
	private final static String EP_ATTRIBUTE_ID = "id";
	private final static String EP_ATTRIBUTE_Uri = "uri";
	private final static String EP_ATTRIBUTE_EClass = "eclass";
	private final static String EP_ATTRIBUTE_Order = "order";
	private final static String EP_ATTRIBUTE_Class = "class";
	
	private final static IExtensibleModelEditingSupport[] EMPTY = new IExtensibleModelEditingSupport[0];
	
	private static class EditingSupportDescription implements Comparable<EditingSupportDescription>{
		public String id;
		public String uri;
		public String eclass;
		public int order;
		public IExtensibleModelEditingSupport editingSupport;
		

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(EditingSupportDescription o) {
			return order - o.order;
		}
	}
	
	private ExtensibleModelManager() {
		loadEditingSupports();
	}
	private static ExtensibleModelManager instance;
	public static ExtensibleModelManager getInstance() {
		if (instance == null) {
			instance = new ExtensibleModelManager();
		}
		return instance;
	}
	
	private Logger logger = Logger.getLogger(getClass());
	/**
	 * �����˶�����չidӵ�е���չ�༭֧���б�
	 */
	private Multimap<EClass, EditingSupportDescription> editingSupportMap = HashMultimap.create();
	
	private void loadEditingSupports() {
		logger.info("��ʼ������չģ�ͱ༭֧����չ��");
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESEditorPlugin.PLUGIN_ID , EP_NAME);
		
		for (IConfigurationElement element : elements) {
			try {
				EditingSupportDescription d = new EditingSupportDescription();
				d.id = element.getAttribute(EP_ATTRIBUTE_ID);
				d.uri = element.getAttribute(EP_ATTRIBUTE_Uri);
				d.eclass = element.getAttribute(EP_ATTRIBUTE_EClass);
				d.order = NumberUtils.toInt(element.getAttribute(EP_ATTRIBUTE_Order), Integer.MAX_VALUE);
				d.editingSupport = (IExtensibleModelEditingSupport) element.createExecutableExtension(EP_ATTRIBUTE_Class);
				
				EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(d.uri);
				if (ePackage == null) {
					logger.warn(String.format("���%sע����չ��%s, �Ҳ�����д��uri��%s", element.getContributor().getName(), d.id, d.uri));
				}
				EClass eClass = (EClass) ePackage.getEClassifier(d.eclass);
				
				editingSupportMap.put(eClass, d);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("����������չģ�ͱ༭֧����չ��");
	}
	
	/**
	 * ��ȡָ����չģ��id����չ�༭֧��
	 * @param modelId
	 * @return
	 */
	public IExtensibleModelEditingSupport[] getExtensibleModelEditingSupports(EClass eClass) {
		Set<EditingSupportDescription> descriptionSet = new HashSet<EditingSupportDescription>();

		// �Ӽ̳����������л������չ���ϲ��󷵻�
		for (EClass superType : eClass.getEAllSuperTypes()) {
			descriptionSet.addAll(editingSupportMap.get(superType));
		}
		
		descriptionSet.addAll(editingSupportMap.get(eClass));

		// ����
		EditingSupportDescription[] descriptions = descriptionSet.toArray(new EditingSupportDescription[descriptionSet.size()]);
		Arrays.sort(descriptions);
		
		IExtensibleModelEditingSupport[] supports = new IExtensibleModelEditingSupport[descriptions.length];
		for (int i = 0; i < supports.length; i++) {
			supports[i] = descriptions[i].editingSupport;
		}

		return supports;
	}
	
	
}
