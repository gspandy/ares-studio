/**
 * Դ�������ƣ�ILogicServiceWrap.java
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
public interface ILogicServiceWrap extends ICRESBizWrap {
	
	/**
	 * ��ȡ��ʱʱ��
	 * @return
	 */
	String getTimeOut();

	/**
	 * ���ݲ���ID��ɾ���ڲ�����
	 * 
	 * @param id
	 */
	public void deleteInternalVar(String id);
	
}
