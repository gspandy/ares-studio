/**
 * Դ�������ƣ�ExtendedPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor;

/**
 * @author sundl
 *
 */
public class ExtendedPropertyHandler implements IPropertyHandler {

	private IBasicExtendPropertyDescriptor descriptor;
	
	public ExtendedPropertyHandler(IBasicExtendPropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		if (obj instanceof ExtensibleModel) {
			descriptor.setValue(((ExtensibleModel) obj), value);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#getValue(java.lang.Object)
	 */
	@Override
	public String getValue(Object obj) {
		if (obj instanceof ExtensibleModel) {
			return descriptor.getValue((ExtensibleModel) obj);
		}
		return null;
	}

}
