/**
 * Դ�������ƣ�IIndexFieldAddModificationScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;

/**
 * @author liaogc
 *
 */
public interface IAddIndexFieldModificationScriptWrap extends IModificationScriptWrap{

	/**
	 *  ���������ֶ���ϸ
	 * @return
	 */
	public ITableIndexScriptWrap[] getDetails();


}
