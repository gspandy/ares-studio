/**
 * Դ�������ƣ�ITableSpaceScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * ���ݿ���Դ--�û���Դ
 * 
 * @author yanwj06282
 *
 */
public interface IDatabaseUserScriptWrap extends IScriptModelWrap{

	/**
	 * ���ݿ��û���Ŀ
	 * 
	 * @return
	 */
	public IDatabaseUserItemScriptWrap[] getUsers();
	
	/**
	 * ���ݿ��û�Ȩ����Ŀ
	 * 
	 * @return
	 */
	public IDatabaseUserPrivilegesScriptWrap[] getPrivileges();
	
}
