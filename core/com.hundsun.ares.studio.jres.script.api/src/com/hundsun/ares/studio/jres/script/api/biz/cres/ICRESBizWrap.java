/**
 * Դ�������ƣ�ICRESBizWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.biz.cres;

import com.hundsun.ares.studio.jres.script.api.biz.IBizServiceWrap;

/**
 * 
 * @author sundl
 *
 */
public interface ICRESBizWrap extends IBizServiceWrap {

	/**
	 * ��ȡ�ڲ������б�
	 * @return
	 */
	IInternalVarWrap[] getInternalVars();
	
	/**
	 * ��ȡԴ���ַ���
	 * @return
	 */
	String getCode();
}
