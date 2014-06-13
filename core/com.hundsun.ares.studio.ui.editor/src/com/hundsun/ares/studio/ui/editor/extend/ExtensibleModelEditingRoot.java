/**
 * 
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * ��չ��������
 * @author gongyf
 *
 */
public class ExtensibleModelEditingRoot {
	private IARESElement resource;
	private EClass eClass;
	private ExtensibleModel model;
	
	// Groups����ͨ����չ��ע�����Ϣ��group��Ӧһ��EditingSupport��չ��
	private List<ExtensibleModelEditingGroup> groups;
	// category������������ʾ��ʱ��ķ�����Ϣ
	private List<ExtensibleModelEditingCategory> categories;
	
	
	public ExtensibleModelEditingRoot(IARESElement resource, ExtensibleModel model) {
		super();
		this.resource = resource;
		this.eClass = model.eClass();
		this.model = model;
	}

	public IARESElement getARESElement() {
		return resource;
	}
	
	public EClass getEClass() {
		return eClass;
	}
	
	public ExtensibleModel getModel() {
		return model;
	}
	
	public List<ExtensibleModelEditingGroup> getGroups() {
		if (groups == null) {
			init();
		}
		return groups;
	}
	
	/**
	 * �����������ʾ�õķ���
	 * @return
	 */
	public List<ExtensibleModelEditingCategory> getCategories() {
		if (categories == null) {
			init();
		}
		return categories;
	}
	
	/**
	 * ��ʼ��������Groups��Category�ṹ��Ϣ
	 */
	private void init() {
		groups = new ArrayList<ExtensibleModelEditingGroup>();
		categories = new ArrayList<ExtensibleModelEditingCategory>();
		for (IExtensibleModelEditingSupport es : ExtensibleModelUtils.getEndabledEditingSupports(getARESElement(), getEClass())) {
			if (!StringUtils.equals(getARESElement().getElementName(), "module.xml") && !StringUtils.equalsIgnoreCase(es.getKey(), Constants.USER_DATA2_KEY)) {
				continue;
			}
			ExtensibleModelEditingGroup group = new ExtensibleModelEditingGroup(this, es);
			groups.add(group);
			
			// �������е���չ����������������չʾ��ʱ��Ӧ�������ĸ�����
			for (ExtensibleModelEditingEntry entry : group.getEntries()) {
				// �����չ���������Լ��ṩ�˷�����Ϣ����ʹ��������ࣻ���û���ṩ����ʹ��������EditingSupport��name��Ϊ����
				String category = entry.getDescriptor().getCategory();
				if (StringUtils.isEmpty(category)) {
					category = group.getEditingSupport().getName();
				}
				
				ExtensibleModelEditingCategory cate = findCategory(category, categories);
				if (cate == null) {
					cate = new ExtensibleModelEditingCategory(category, this);
					categories.add(cate);
				}
				entry.setCategory(cate);
				cate.getEntries().add(entry);
			}
		}
	}
	
	private ExtensibleModelEditingCategory findCategory(String category, List<ExtensibleModelEditingCategory> catetories) {
		for (ExtensibleModelEditingCategory cate : catetories) {
			if (StringUtils.equals(category, cate.getName()))
				return cate;
		}
		return null;
	}

}
