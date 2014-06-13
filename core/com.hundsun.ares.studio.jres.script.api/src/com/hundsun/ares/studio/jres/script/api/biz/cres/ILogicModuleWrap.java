/**
 * Դ�������ƣ�ILogicModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.biz.cres;


/**
 * @author sundl
 *
 */
public interface ILogicModuleWrap {

	/**
	 * ��ȡģ���µ����е��߼�������Դ������ָ���Ƿ�ݹ��ȡ��ģ���µ���Դ���������������ȡ��ǰ�㼶�µ���Դ
	 * @param recursive
	 * @return
	 */
	ILogicServiceWrap[] getLogicServices(boolean recursive);
	
	/**
	 * ��ȡģ���µ����е��߼�������Դ������ָ���Ƿ�ݹ��ȡ��ģ���µ���Դ���������������ȡ��ǰ�㼶�µ���Դ
	 * @param recursive
	 * @return
	 */
	ILogicFunctionWrap[] getLogicFunctions(boolean recursive);
}
