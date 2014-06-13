/**
 * Դ�������ƣ�ParameterWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.script;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.api.biz.IParameterWrap;

/**
 * @author sundl
 *
 */
public class ParameterWrap extends AttributeWrap implements IParameterWrap{

	/**
	 * @param t
	 * @param resource
	 */
	public ParameterWrap(Parameter p, IARESResource resource) {
		super(p, resource);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.api.biz.IParameterWrap#getFlag()
	 */
	@Override
	public String getFlag() {
		return getOriginalInfo().getFlags();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.api.biz.IParameterWrap#setFlag()
	 */
	@Override
	public void setFlag(String flag) {
		getOriginalInfo().setFlags(flag);
	}
	
}
