/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.aresaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.ui.ARESUI;
import com.hundsun.ares.studio.ui.aresaction.IARESActionDescriptor;

/**
 * ARES ����ע���
 * @author sundl
 */
public class ARESActionRegistry {

	private static Logger logger = Logger.getLogger(ARESActionDescriptor.class.getName());
	private static final String EXTENSION_POINT_ID = "aresaction";
	
	private static ARESActionRegistry instance;
	
	// id --> declare
	private Map<String, AresActionDelareDescriptor> declaretioins = new HashMap<String, AresActionDelareDescriptor>();
	// id --> implementations
	private Multimap<String, AresActionImplementationDescriptor> implementations = ArrayListMultimap.create();
	
	// ares action id ---> descriptor
	private Multimap<String, IARESActionDescriptor> actions = ArrayListMultimap.create();
	
	public static ARESActionRegistry getInstance() {
		if (instance == null) {
			instance = new ARESActionRegistry();
		}
		return instance;
	}
	
	private ARESActionRegistry() {
		init();
	}
	
	private void init() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESUI.PLUGIN_ID, EXTENSION_POINT_ID);
		for (IConfigurationElement element : elements) {
			if (element.getName().equals("action_declare")) {
				AresActionDelareDescriptor descriptor = new AresActionDelareDescriptor(element);
				declaretioins.put(descriptor.getId(), descriptor);
			} else if (element.getName().equals("action_implementation")) {
				AresActionImplementationDescriptor descriptor = new AresActionImplementationDescriptor(element);
				implementations.put(descriptor.getId(), descriptor);
			}
		}
	}
	
	/**
	 * ����ָ��id�Ĳ�������
	 * @param id
	 * @return
	 */
	public AresActionDelareDescriptor getActionDeclare(String id) {
		return declaretioins.get(id);
	}
	
	public Collection<AresActionDelareDescriptor> getActionDeclaretions() {
		return declaretioins.values();
	}
	
	/**
	 * ����ָ��id�Ĳ���ʵ��
	 * @param id
	 * @return
	 */
	public AresActionImplementationDescriptor[] getActionImplementations(String id) {
		Collection<AresActionImplementationDescriptor> results = implementations.get(id);
		return results.toArray(new AresActionImplementationDescriptor[0]);
	}
	
	/**
	 * ����ָ���Ĳ������ָ������Դ���Ͷ���Ĳ���ʵ��
	 * @param id
	 * @param resType
	 * @return
	 */
	public AresActionImplementationDescriptor getActionImplementation(String id, String resType) {
		for (AresActionImplementationDescriptor desc : getActionImplementations(id)) {
			if (desc.isMatch(resType)) {
				return desc;
			}
		}
		return null;
	}
	
	/**
	 * ����ע��Ĳ���ID
	 * @return
	 */
	public String[] getAllActionIds() {
		return declaretioins.keySet().toArray(new String[0]);
	}
	
	/**
	 * ����ָ������Դ������ʵ������Щ���������ҷ�����Щ����������
	 * @param resTypes
	 * @return
	 */
	public List<AresActionDelareDescriptor> computeActionDeclaretions(String[] resTypes) {
		List<AresActionDelareDescriptor> results = new ArrayList<AresActionDelareDescriptor>();
		for (AresActionDelareDescriptor declare : declaretioins.values()) {
			for (String resType : resTypes) {
				AresActionImplementationDescriptor implementation = getActionImplementation(declare.getId(), resType);
				if (implementation != null) {
					results.add(declare);
					break;
				}
			}
		}
		return results;
	}
	
	/**
	 * �������֧�ֵ���Դ���ͣ�������ʵ��֧������Щ��Դ���ͣ�
	 * @param actionId
	 * @return
	 */
	public String[] computeSupportedResTypes(String actionId) {
		Set<String> resTypes = new HashSet<String>();
		AresActionImplementationDescriptor[] implementations = getActionImplementations(actionId);
		for (AresActionImplementationDescriptor impl : implementations) {
			resTypes.addAll(Arrays.asList(impl.getResTypes()));
		}
		return resTypes.toArray(new String[0]);
	}
	
}
