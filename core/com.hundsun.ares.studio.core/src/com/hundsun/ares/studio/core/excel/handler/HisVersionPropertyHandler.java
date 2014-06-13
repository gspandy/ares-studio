/**
 * Դ�������ƣ�HisVersionPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.model.CorePackage;

/**
 * �޸ļ�¼�İ汾���Ե�PropertyHandler, ���ڴ����vǰ׺�Ͳ���vǰ׺�ļ��ݡ�
 * @author sundl
 */
public class HisVersionPropertyHandler extends EMFPropertyHandler {

	/**
	 * @param feature
	 */
	public HisVersionPropertyHandler() {
		super(CorePackage.Literals.REVISION_HISTORY__VERSION);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		super.setValue(obj, value);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#getValue()
	 */
	@Override
	public String getValue(Object obj) {
		String value = super.getValue(obj);
		if (!StringUtils.startsWithIgnoreCase(value, "v")) {
			value = "V" + value;
		}
		return value;
	}
	
}
