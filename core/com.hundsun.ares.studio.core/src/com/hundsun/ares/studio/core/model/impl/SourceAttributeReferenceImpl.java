/**
 * Դ�������ƣ�SourceAttributeReferenceImpl.java
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

/**
 * ���ڶ�����һ�������ı��ڲ���������
 * @author gongyf
 *
 */
public class SourceAttributeReferenceImpl extends ReferenceImpl {
	
	private EObject object;
	private EAttribute attribute;
	private int pos;
	private int length;
	
	/**
	 * @param object
	 * @param attribute
	 */
	public SourceAttributeReferenceImpl(String type, EObject object, EAttribute attribute, int pos, int length) {
		super();
		this.type = type;
		this.object = object;
		this.attribute = attribute;
		this.pos = pos;
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#getValue()
	 */
	@Override
	public String getValue() {
		return StringUtils.substring(String.valueOf(object.eGet(attribute)), pos, pos + length);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		String source = String.valueOf(object.eGet(attribute));
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.substring(source, 0, pos));
		sb.append(value);
		sb.append(StringUtils.substring(source, pos + length));
		object.eSet(attribute, sb.toString());
		
	}
}
