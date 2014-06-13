/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.core.registry.ResCategoryDescriptor;
import com.hundsun.ares.studio.internal.core.registry.ResDescriptor;

/**
 * ��Դ����ע���
 * @author sundl
 */
public class ARESResRegistry {

	private static Logger logger = Logger.getLogger(ARESResRegistry.class.getName());
	
	private static ARESResRegistry instance = null;
	
	// restypes: type-->descriptor
	private Map<String, IResDescriptor> resTypes = new HashMap<String, IResDescriptor>();
	
	// category: id-->descriptor
	private Map<String, IResCategoryDescriptor> categories = new HashMap<String, IResCategoryDescriptor>();
	
	private ARESResRegistry() {
		// ��һ�ε��õ�ʱ���ʼ��
		init();
	}
	
	public static ARESResRegistry getInstance() {
		if (instance == null) {
			instance = new ARESResRegistry();
		}
		return instance;
	}
	
	private void init() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESCore.PLUGIN_ID, ICommonExtensionConstants.EP_ID_RESOUCE);
		for (IConfigurationElement element : elements) {
			if (element.getName().equals(ICommonExtensionConstants.ARES_RESOURCE)) {
				IResDescriptor descriptor = new ResDescriptor(element);
				logger.debug("reading res-type extension: " + descriptor.getId() + " in contributor: " + element.getContributor().getName());
				IResDescriptor exists = resTypes.get(descriptor.getId());
				if (exists != null) {
					logger.warn("res-type: " + descriptor.getId() + " in contibuter: " + element.getContributor().getName() + " is already defined in contributor: " + exists.getConfigurationElement().getContributor().getName());
				} 
				resTypes.put(descriptor.getId(), descriptor);
				logger.debug("res-type extension readed, id: " + descriptor.getId());
			} else if (element.getName().equals(ICommonExtensionConstants.RES_CATEGORY)) {
				ResCategoryDescriptor cate = new ResCategoryDescriptor(element);
				categories.put(cate.getId(), cate);
				logger.debug("res-category found: " + cate.getId() + "--->" + cate.getName());
			}
			
		}
	}
	
	public IResDescriptor getResDescriptor(String id) {
		if (id == null)
			return null;
		
		return resTypes.get(id);
	}
	
	public IResDescriptor getResDescriptor(IARESResource res) {
		IResDescriptor desc = resTypes.get(res.getElementName());
		if (desc == null)
			desc = resTypes.get(res.getType());
		return desc;
	}
	
	public IResDescriptor[] getResDescriptors() {
		return resTypes.values().toArray(new IResDescriptor[0]);
	}
	
	public IResCategoryDescriptor[] getCategories() {
		return this.categories.values().toArray(new IResCategoryDescriptor[0]);
	}
	
	public IResCategoryDescriptor getCategory(String id) {
		return this.categories.get(id);
	}
	
	/**
	 * ��ȡ���������õ���Դ����
	 * @param cate ����ID
	 * @return ָ�����͵ķ����µ���Դ����
	 */
	public String[] getRestypes(String cateId) {
		List<String> types = new ArrayList<String>();
		for (IResDescriptor resDesc : resTypes.values()) {
			String cate = resDesc.getCategory();
			if (cateId.equals(cate)) {
				types.add(resDesc.getId());
			}
		}
		return types.toArray(new String[0]);
	}
	
	/**
	 * �ж�ָ������Դ�����Ƿ���������ָ���ķ������档
	 * @param resType
	 * @param cateId
	 * @return
	 */
	public boolean isResTypeInCategory(String resType, String cateId) {
		IResDescriptor resDesc = resTypes.get(resType);
		if (resDesc != null) {
			String resCate = resDesc.getCategory();
			if (resCate == null)
				resCate = "";
			return resCate.equals(cateId);
		}
		return false;
	}
	
	/**
	 * ����û��ע��������Դ���͡�
	 * @return ����û��ע��������Դ���͡�
	 */
	public List<String> getNonCategoryResTypes() {
		List<String> types = new ArrayList<String>();
		for (IResDescriptor resType : resTypes.values()) {
			if (StringUtils.isEmpty(resType.getCategory())) {
				types.add(resType.getId());
			}
		}
		return types;
	}
	
}
