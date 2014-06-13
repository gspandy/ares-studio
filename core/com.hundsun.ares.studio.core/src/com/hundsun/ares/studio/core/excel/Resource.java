/**
 * Դ�������ƣ�Resource.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.core.excel;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.util.log.Log.Location;

/**
 * @author sundl
 *
 */
public class Resource {
	
	public String name;
	public String type;
	public Object info;
	public Location startLoc;
	public Location endLoc;
	
	public void create(IARESModule module) throws ARESModelException {
		module.createResource(name + "." + type, info);
	}
	
	/**
	 * ��ȡ��Դ����ϸ��������Ϣ�����磺 (�����)������
	 * @return
	 */
	public String getDescription() {
		if (info instanceof BasicResourceInfo) {
			BasicResourceInfo basicInfo = (BasicResourceInfo) info;
			String id = basicInfo.getObjectId();
			String cName = basicInfo.getChineseName();
			if (!StringUtils.isEmpty(id)) {
				return String.format("(%s)%s", id, cName);
			} else {
				return cName;
			}
		}
		return name;
	}
}
