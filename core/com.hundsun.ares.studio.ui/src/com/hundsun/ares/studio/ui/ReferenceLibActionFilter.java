/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IExternalResPathEntry;
import com.hundsun.ares.studio.core.IResPathEntry;
import com.hundsun.ares.studio.core.IResPathEntryElement;

/**
 * ���ð�ר�е�ActionFilter
 * @author sundl
 */
public class ReferenceLibActionFilter extends ARESElementActionFilter {

	// �Ƿ����ⲿ��respath provider �ṩ������
	private static final String EXTERNAL = "external";
	
	private static ReferenceLibActionFilter instance;
	
	public static ReferenceLibActionFilter getInstance() {
		if (instance == null)
			instance = new ReferenceLibActionFilter();
		return instance;
	}
	
	private ReferenceLibActionFilter() {}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.ARESElementActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof IResPathEntryElement) {
			IResPathEntryElement respathElement = (IResPathEntryElement) target;
			IResPathEntry entry = respathElement.getResPathEntry();
			if (StringUtils.equals(EXTERNAL, name)) {
				boolean external = entry instanceof IExternalResPathEntry;
				return StringUtils.equals(value, String.valueOf(external));
			}
		}
		return super.testAttribute(target, name, value);
	}

}
