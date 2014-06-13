/**
 * Դ�������ƣ�IARESModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.biz.cres.ILogicModuleWrap;


/**
 * @author sundl
 *
 */
public interface IARESModuleWrap  extends IObjectModuleWrap, IBizModuleWrap, ILogicModuleWrap{

	/**
	 * ������Ӣ����
	 */
	public String getName();
	
	/**
	 * ������Ӣ����
	 */
	public String getFullyQualifiedName();
	
	/**
	 * ��ȡ��������������
	 */
	public String getCName();
	
	/**
	 * ��ȡ��ģ�飬����Ѿ��Ƕ���ģ�飬�򷵻�null
	 * @return
	 */
	IARESModuleWrap getParent();
	
//	/**
//	 * ��ȡ��ģ�飨��ֱ����ģ�飩
//	 * @return
//	 */
//	IARESModuleWrap[] getSubModules();
	
	/**
	 * ���������Ķ���ģ�飨һ��Ҳ����ϵͳ����������ģ���Ѿ��Ƕ����ˣ��������Լ���
	 * @return
	 */
	IARESModuleWrap getTopModule();
	
}
