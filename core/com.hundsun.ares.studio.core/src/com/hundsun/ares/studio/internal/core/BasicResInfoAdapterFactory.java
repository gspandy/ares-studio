/**
 * Դ�������ƣ�BasicResInfoAdapterFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.model.core.util
 * ����˵�������䵽CommonModelʵ�������ͼ������֧��
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.internal.core;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IAdapterFactory;

import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.CommonModel;
import com.hundsun.ares.studio.core.model.ICommonModel;

public class BasicResInfoAdapterFactory implements IAdapterFactory {

	private static Class[] ADAPTERS = new Class[] {ICommonModel.class}; 
	
	private static class BasicInfoCommonModel extends CommonModel {

		private BasicResourceInfo info;
		BasicInfoCommonModel(BasicResourceInfo basicInfo) {
			this.info = basicInfo;			
		}
		
		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.core.model.ICommonModel#getValue(java.lang.String)
		 */
		@Override
		public Object getValue(String key) {
			if (StringUtils.equals(ICommonModel.CNAME, key)) {
				return info.getChineseName();
			} else if (StringUtils.equals(ICommonModel.NAME, key)) {
				return info.getName();
			} else if (StringUtils.equals(ICommonModel.ID, key)) {
				return info.getObjectId();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.core.model.ICommonModel#setValue(java.lang.String, java.lang.Object)
		 */
		@Override
		public void setValue(String key, Object value) {
			if (StringUtils.equals(ICommonModel.CNAME, key)) {
				info.setChineseName(String.valueOf(value));
			} else if (StringUtils.equals(ICommonModel.NAME, key)) {
				info.setName(String.valueOf(value));
			}
		}
		
	}
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ICommonModel.class && adaptableObject instanceof BasicResourceInfo) {
			return new BasicInfoCommonModel((BasicResourceInfo) adaptableObject);
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return ADAPTERS;
	}

}
