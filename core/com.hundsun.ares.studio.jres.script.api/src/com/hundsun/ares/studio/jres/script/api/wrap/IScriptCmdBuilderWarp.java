/**
 * Դ�������ƣ�IScriptCmdBuilderWarp.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import java.util.Map;

/**
 * �����б���ű��ӿ�
 * @author liaogc
 *
 */
public interface IScriptCmdBuilderWarp {

	/**
	 * 
	 * @param scripts �ű���Ҫ˵���Լ��ű�·��
	 * @param parameters �ű�����������
	 */
	public void build(String[][] scripts,Map<String,Object> parameters);



}
