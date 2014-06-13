/**
 * Դ�������ƣ�TextAttributeReferenceImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * @author gongyf
 *
 */
public class TextAttributeReferenceImpl extends ReferenceImpl {
	
	private EObject object;
	private EAttribute attribute;
	
	/**
	 * @param object
	 * @param attribute
	 */
	public TextAttributeReferenceImpl(String type, EObject object, EAttribute attribute) {
		super();
		this.type = type;
		this.object = object;
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#getValue()
	 */
	@Override
	public String getValue() {
		return String.valueOf(object.eGet(attribute));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		object.eSet(attribute, value);
	}
}
