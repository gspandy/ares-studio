/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.modelconvert;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * ����AnyARESResourceInfo���͵����л���
 * 
 * @author gongyf
 *
 */
public class AnyModelConverter extends AbstractModelConverter {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#read(com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	public Object read(IARESResource resource) throws Exception {
		return new AnyARESResourceInfo();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#write(com.hundsun.ares.studio.core.IARESResource, java.lang.Object)
	 */
	@Override
	public byte[] write(IARESResource resource, Object info) throws Exception {
		throw new UnsupportedOperationException("AnyModelConverter��֧�����л�");
	}

}
