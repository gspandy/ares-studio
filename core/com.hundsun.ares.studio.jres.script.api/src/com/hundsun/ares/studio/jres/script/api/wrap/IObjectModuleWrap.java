/**
 * Դ�������ƣ�IObjectModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.biz.IBizObjectWrap;

/**
 * @author sundl
 *
 */
public interface IObjectModuleWrap {

	
	/**
	 * ��ȡģ�������еĶ�����Դ�������ò���ָ���Ƿ�ݹ������ģ���µ���Դ,
	 * @return
	 */
	IBizObjectWrap[] getObjects(boolean recursive);
}
