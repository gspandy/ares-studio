/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.util.StringUtil;

/**
 * ������ģ������ͺ���Դ���͵�ӳ���ϵ
 * @author sundl
 */
public class ModuleRootType2ResTypeMap {

	private static final Logger logger = Logger.getLogger(ModuleRootType2ResTypeMap.class.getName());
	
	private static ModuleRootType2ResTypeMap instance;
	
	// ����ӳ���ϵ��Map��ʹ����apache��MultiMap������һ��key�ж��value
	private Multimap<String, String> map = ArrayListMultimap.create();
	
	private ModuleRootType2ResTypeMap() {
		init();
	}
	
	public static ModuleRootType2ResTypeMap getInstance() {
		if (instance == null) {
			instance = new ModuleRootType2ResTypeMap();
		}
		return instance;
	}
	
	private void init() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESCore.PLUGIN_ID, ICommonExtensionConstants.EP_ID_MODULEROOT_RESTYPE);
		for (IConfigurationElement element : elements) {
			String rootType = element.getAttribute(ICommonExtensionConstants.ROOT_TYPE);
			String restypes = element.getAttribute(ICommonExtensionConstants.RES_TYPES);
			if (!(StringUtil.isEmpty(restypes) || StringUtil.isEmpty(rootType))) {
				// single res-type
				if (restypes.indexOf(',') == -1) {
					map.put(rootType.trim(), restypes.trim());
					logMapFound(rootType, restypes);
				} else {	// multi res-type's
					String[] types = restypes.split(",");
					for (String type : types) {
						map.put(rootType.trim(), type.trim());
						logMapFound(rootType, type);
					}
				}
			}
		}
	}
	
	private void logMapFound(String rootType, String resType) {
		logger.fine("R-R Map found: (" + rootType.trim() + " --> " + resType.trim());
	}
	
	@SuppressWarnings("rawtypes")
	public boolean isAllowed(String rootType, String resType) {
		List allowedTypes = (List)map.get(rootType);
		for (Object type : allowedTypes) {
			if (type.equals(resType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����ָ����ģ����������Դ����
	 * @param rootType ģ���������
	 * @return �������Դ����id
	 */
	public String[] getAllowedResTypes(String rootType) {
		List<String> allowedTypes = (List<String>)map.get(rootType);
		return allowedTypes.toArray(new String[0]);
	}
	
	/**
	 * ����ָ������Դ���͵�ģ�������
	 * @param resType ��Դ����
	 * @return ģ����������
	 */
	public String[] getAllowedRootTypes(String resType) {
		Set<String> rootTypes = new HashSet<String>();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			for (String value : map.get(key)) {
				if (value.equals(resType)) {
					rootTypes.add(key) ;
				}
			}
		}
		return rootTypes.toArray(new String[rootTypes.size()]);
	}
	
}
