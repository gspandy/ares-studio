/**
 * Դ�������ƣ�IARESModuleRootWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

/**
 * ģ���
 * @author sundl
 *
 */
public interface IARESModuleRootWrap {

	/**
	 * ��ȡ���ģ����µ�������ϵͳ����һ��ģ�飩
	 */
	IARESModuleWrap[] getSubSystems();
	
	/**
	 * ����ģ�鳤������ȡģ��������ģ��
	 * 
	 * @param moduleName
	 * @return
	 */
	public IARESModuleWrap getSubModuleByName(String moduleName);
	
}
