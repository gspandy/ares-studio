/**
 * Դ�������ƣ�IProcedureScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�qinyuan
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.biz.cres.IProcedureWrap;

/**
 * @author qinyuan
 *
 */
public interface IProcedureScriptWrap {
	
	/**
	 * ��ȡ���д洢����
	 * @return
	 */
	IProcedureWrap[] getProcedures();
	
	
	/**
	 * ������������ȡ�洢����
	 * @return
	 */
	IProcedureWrap getProcedureByCName(String name);
}
