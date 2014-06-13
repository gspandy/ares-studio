/**
 * Դ�������ƣ�TextAttributeReferenceWithNamespaceImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model.impl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.model.ReferenceWithNamespace;

/**
 * ֧�ֶ������ռ������id�ķֱ������������
 * @author gongyf
 *
 */
public class TextAttributeReferenceWithNamespaceImpl extends
		TextAttributeReferenceImpl implements ReferenceWithNamespace {

	private static final String SEPARATOR		= ".";
	
	/**
	 * @param type
	 * @param object
	 * @param attribute
	 */
	public TextAttributeReferenceWithNamespaceImpl(String type, EObject object,
			EAttribute attribute) {
		super(type, object, attribute);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.ReferenceWithNamespace#setNamespace(java.lang.String)
	 */
	@Override
	public void setNamespace(String ns) {
		setValue(ns + SEPARATOR + getId());
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.ReferenceWithNamespace#getNamespace()
	 */
	@Override
	public String getNamespace() {
		String value = getValue();
		if (value.contains(SEPARATOR)) {
			return StringUtils.substringBeforeLast(value, SEPARATOR);
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.ReferenceWithNamespace#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		String ns = getNamespace();
		if (StringUtils.isBlank(ns)) {
			setValue(id);
		} else {
			setValue(ns + SEPARATOR + id);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.ReferenceWithNamespace#getId()
	 */
	@Override
	public String getId() {
		String value = getValue();
		if (value.contains(SEPARATOR)) {
			return StringUtils.substringAfterLast(value, SEPARATOR);
		} else {
			return value;
		}
	}

}
