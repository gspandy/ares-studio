/**
 * Դ�������ƣ�IProcedureModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�qinyuan
 */
package com.hundsun.ares.studio.jres.script.api.biz.cres;

/**
 * @author qinyuan
 *
 */
public interface IProcedureModuleWrap {
	
	/**
	 * ��ȡģ���µ����еĴ洢���̣�����ָ���Ƿ�ݹ��ȡ��ģ���µ���Դ���������������ȡ��ǰ�㼶�µ���Դ
	 * @param recursive
	 * @return
	 */
	IProcedureWrap[] getProcedures(boolean recursive);

}
