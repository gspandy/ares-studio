/**
 * Դ�������ƣ�StandardFieldAdapterFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�wangxh
 */
package com.hundsun.ares.studio.biz.adapters;

import org.eclipse.core.runtime.IAdapterFactory;

import com.hundsun.ares.studio.biz.BizFactory;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;

/**
 * @author wangxh
 *
 */
public class StandardFieldAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof StandardField) {
			
			StandardField sf = (StandardField) adaptableObject;
			
			if (adapterType.equals(Parameter.class)) {
				Parameter param = BizFactory.eINSTANCE.createParameter();
				param.setId(sf.getName());
				param.setParamType(ParamType.STD_FIELD);
				return param;
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[]{ 
				Parameter.class
		};
	}

}
