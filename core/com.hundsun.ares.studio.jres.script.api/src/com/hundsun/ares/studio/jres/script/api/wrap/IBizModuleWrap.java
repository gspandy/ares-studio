/**
 * Դ�������ƣ�IBizModuleWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.biz.IBizServiceWrap;

/**
 * @author sundl
 *
 */
public interface IBizModuleWrap {
	
	/**
	 * ��ȡ���еķ�����Դ; �����ò���ָ���Ƿ�ݹ������ģ���µ���Դ���������Ϊfalse��ֻ����ֱ�Ӱ���
	 * �ڵ�ǰģ���µ���Դ�����Ϊtrue����ݹ������ģ�飬����������Դ
	 * @return
	 */
	IBizServiceWrap[] getServices(boolean recursive);
	
}
