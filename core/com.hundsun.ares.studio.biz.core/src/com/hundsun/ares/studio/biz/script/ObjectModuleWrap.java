/**
 * Դ�������ƣ�ObjectModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.script;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.api.biz.IBizObjectWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.IObjectModuleWrap;

/**
 * @author sundl
 *
 */
public class ObjectModuleWrap implements IObjectModuleWrap {

	private IARESModule module;
	public ObjectModuleWrap(IARESModule module) {
		this.module = module;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.api.wrap.IObjectModuleWrap#getObjects(boolean)
	 */
	@Override
	public IBizObjectWrap[] getObjects(boolean recursive) {
		List<IBizObjectWrap> objects = new ArrayList<IBizObjectWrap>();
		IARESResource[] resources = module.getARESResources(IBizResType.Object, recursive);
		for (IARESResource res : resources) {
			objects.add(new BizObjectWrap(res));
		}
		return objects.toArray(new IBizObjectWrap[0]);
	}

}
