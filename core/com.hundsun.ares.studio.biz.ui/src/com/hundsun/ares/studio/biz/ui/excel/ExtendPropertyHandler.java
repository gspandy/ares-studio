/**
 * Դ�������ƣ�ExtendPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.excel;

import org.apache.commons.lang.ObjectUtils;

import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * @author sundl
 *
 */
public class ExtendPropertyHandler implements IPropertyHandler {
	
	private String key;

	public ExtendPropertyHandler(String key) {
		this.key = key;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		if (obj instanceof ExtensibleModel) {
			((ExtensibleModel) obj).getData().put(this.key, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#getValue(java.lang.Object)
	 */
	@Override
	public String getValue(Object obj) {
		if (obj instanceof ExtensibleModel) {
			ExtensibleModel model = (ExtensibleModel) obj;
			Object value = model.getData().get(key);
			return ObjectUtils.toString(value);
		}
		return null;
	}

}
