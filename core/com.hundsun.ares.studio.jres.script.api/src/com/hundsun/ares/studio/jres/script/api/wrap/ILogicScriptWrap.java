/**
 * Դ�������ƣ�ICRESScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.biz.cres.ILogicFunctionWrap;
import com.hundsun.ares.studio.jres.script.api.biz.cres.ILogicServiceWrap;

/**
 * @author sundl
 *
 */
public interface ILogicScriptWrap {

	/**
	 * ��ȡ���е�LS
	 * @return
	 */
	ILogicServiceWrap[] getLogicServices();
	
	/**
	 * ��ȡ���е�LF
	 * @return
	 */
	ILogicFunctionWrap[] getLogicFunctions();
	
	/**
	 * ��������������LS
	 * @return
	 */
	ILogicServiceWrap getLSByCName(String name);
	
	/**
	 * ��������������LF
	 * @return
	 */
	ILogicFunctionWrap getLFByCName(String name);
}
