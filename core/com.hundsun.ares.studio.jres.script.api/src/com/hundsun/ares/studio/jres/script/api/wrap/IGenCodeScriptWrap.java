/**
 * Դ�������ƣ�IGenCodeScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

/**
 * @author liaogc
 *
 */
public interface IGenCodeScriptWrap {
	/**
	 * 
	 * @param type ����:1:ԭ��,2ҵ���߼�
	 * @param type genType:1:������Ŀ¼,2:��Ӣ��Ŀ¼,3:����Ŀ¼
	 */
	void genModuleCode(int resType,int genType);
}
