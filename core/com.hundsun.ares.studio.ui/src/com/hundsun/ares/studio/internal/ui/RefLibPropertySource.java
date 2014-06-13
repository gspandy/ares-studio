/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.views.properties.FilePropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.hundsun.ares.studio.core.IBasicReferencedLibInfo;
import com.hundsun.ares.studio.core.IDependenceDescriptor;
import com.hundsun.ares.studio.core.IReferencedLibrary;

/**
 * 
 * @author sundl
 */
public class RefLibPropertySource extends FilePropertySource implements IPropertySource {
	
	private static final String PRO_ID = "pro_id";
	private static final String PRO_VERSION = "pro_version";
	private static final String PRO_NAME = "pro_name";
	private static final String PRO_PROVIDER = "pro_provider";
	private static final String PRO_CONTACT = "pro_contact";
	private static final String PRO_NOTE = "pro_note";
	private static final String PRO_PUBLISHER = "pro_publisher";
	private static final String PRO_PUB_TIME = "pro_pub_time";
	private static final String PRO_DEPENDENCIES = "pro_dependencies";
	private static final String PRO_TYPE = "pro_type";
	
	private static final String CATEGORY = "������Դ��";

	private static IPropertyDescriptor[] PRO_DESCRIPTORS = new IPropertyDescriptor[10];
	
	static {
		PropertyDescriptor descriptor = null;
		
		descriptor = new PropertyDescriptor(PRO_ID, "ID");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[0] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_TYPE, "����");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[1] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_NAME, "����");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[2] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_VERSION, "�汾");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[3] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_PROVIDER, "������");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[4] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_CONTACT, "��ϵ��ʽ");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[5] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_NOTE, "˵��");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[6] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_PUBLISHER, "������");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[7] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_PUB_TIME, "����ʱ��");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[8] = descriptor;
		
		descriptor = new PropertyDescriptor(PRO_DEPENDENCIES, "����");
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(CATEGORY);
		PRO_DESCRIPTORS[9] = descriptor;

	}
	
	private IReferencedLibrary lib;
	
	public RefLibPropertySource(IReferencedLibrary lib) {
		super((IFile) lib.getResource());
		this.lib = lib;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return lib;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
        int superLength = superDescriptors.length;
        IPropertyDescriptor[] libDescriptors = new IPropertyDescriptor[superLength + PRO_DESCRIPTORS.length];
        System.arraycopy(superDescriptors, 0, libDescriptors, 0, superLength);
        System.arraycopy(PRO_DESCRIPTORS, 0, libDescriptors, superLength, PRO_DESCRIPTORS.length);
		return libDescriptors;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		Object superValue = super.getPropertyValue(id);
		if (superValue != null) {
			return superValue;
		}
		
		IBasicReferencedLibInfo basicInfo = lib.getBasicInfo();
		if (id.equals(PRO_ID)) {
			return basicInfo.getId();
		} else if (id.equals(PRO_TYPE)) {
			return basicInfo.getType();
		} else if (id.equals(PRO_NAME)) {
			return basicInfo.getName();
		} else if (id.equals(PRO_PROVIDER)) {
			return basicInfo.getProvider();
		} else if (id.equals(PRO_CONTACT)) {
			return basicInfo.getContact();
		} else if (id.equals(PRO_NOTE)) {
			return basicInfo.getNote();
		} else if (id.equals(PRO_PUBLISHER)) {
			return basicInfo.getPublisher();
		} else if (id.equals(PRO_PUB_TIME)) {
			return basicInfo.getPublishTime();
		} else if (id.equals(PRO_VERSION)) {
			return basicInfo.getVersion();
		} else if (id.equals(PRO_DEPENDENCIES)) {
			StringBuffer description = new StringBuffer();
			List<IDependenceDescriptor> dependencies = basicInfo.getDependencyDescriptors();
			if (dependencies != null) {
				for (int i = 0; i < dependencies.size(); i++) {
					IDependenceDescriptor dep = dependencies.get(i);
					description.append(dep.getId());
					if (i != dependencies.size() - 1) {
						description.append(',');
					}
				}
				return description;
			} 
			return StringUtils.EMPTY;
		} 
		
		return "No Value";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object id) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
	}

}
