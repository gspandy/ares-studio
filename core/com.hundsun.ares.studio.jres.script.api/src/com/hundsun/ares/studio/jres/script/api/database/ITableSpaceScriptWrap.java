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
 * ���ݿ��ռ��װ����
 * 
 * @author yanwj06282
 *
 */
public interface ITableSpaceScriptWrap extends IScriptModelWrap{

	/**
	 * ��ռ���Ŀ
	 * 
	 * @return
	 */
	public ITableSpaceItemScriptWrap[] getSpace();
	
}
