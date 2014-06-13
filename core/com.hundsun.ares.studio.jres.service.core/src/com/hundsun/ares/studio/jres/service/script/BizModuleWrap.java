/**
 * Դ�������ƣ�ServiceModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.script;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.api.biz.IBizServiceWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.IBizModuleWrap;

/**
 * @author sundl
 *
 */
public class BizModuleWrap implements IBizModuleWrap{
	
	private IARESModule module;
	
	public BizModuleWrap(IARESModule module) {
		this.module = module;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.api.wrap.IBizModuleWrap#getServices(boolean)
	 */
	@Override
	public IBizServiceWrap[] getServices(boolean recursive) {
		List<IBizServiceWrap> services = new ArrayList<IBizServiceWrap>();
		IARESResource[] resources = module.getARESResources(IBizResType.Service, recursive);
		for (IARESResource res : resources) {
			services.add(new BizServiceWrap(res));
		}
		return services.toArray(new IBizServiceWrap[0]);
	}

}
