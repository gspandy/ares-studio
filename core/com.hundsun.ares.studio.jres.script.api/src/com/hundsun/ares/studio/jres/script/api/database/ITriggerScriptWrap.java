/**
 * Դ�������ƣ�ITriggerScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.wrap.IDatabaseResScriptWrap;

/**
 * ��������װ����
 * 
 * @author yanwj06282
 *
 */
public interface ITriggerScriptWrap extends IDatabaseResScriptWrap {

	/**
	 * ���ݿ�sql
	 * 
	 * @return
	 */
	public String getSql();
	
}
