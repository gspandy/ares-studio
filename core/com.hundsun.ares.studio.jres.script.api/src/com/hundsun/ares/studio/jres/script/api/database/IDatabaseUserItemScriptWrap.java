/**
 * Դ�������ƣ�ITableSpaceItemScriptWrap.java
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
 * ���ݿ���Դ--�û���Դ--�û���ϸ
 * 
 * @author yanwj06282
 *
 */
public interface IDatabaseUserItemScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ�û�Ȩ��
	 * 
	 * @return
	 */
	public IDatabaseUserPrivilegesScriptWrap[] getPrivileges();
	
	/**
	 * �Ƿ�����
	 * 
	 * @return
	 */
	public boolean isEnable();
	
	/**
	 * ��ȡ�ļ�����
	 * 
	 * @return
	 */
	public String getAttributes();
	
	/**
	 * ��ȡ˵����Ϣ
	 * 
	 * @return
	 */
	public String getDescription();
	
	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public String getPassword();
	
	/**
	 * ��ȡĬ�ϱ�ռ�
	 * 
	 */
	public String getDefaultTableSpace();
}
