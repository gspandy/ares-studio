/**
 * Դ�������ƣ�Data1PropertyProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * 
 * @author sundl
 */
public class Data1PropertyProvider implements IPropertyHandler {
	
	private String key;
	
	public Data1PropertyProvider(String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		if (obj instanceof ExtensibleModel) {
			((ExtensibleModel) obj).getData().put(key, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.IPropertyHandler#getValue(java.lang.Object)
	 */
	@Override
	public String getValue(Object obj) {
		if (obj instanceof ExtensibleModel) {
			return ((ExtensibleModel) obj).getData().get(key);
		}
		return null;
	}

}
