/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * �޸ı��ֶη�װ����
 * 
 * @author yanwj06282
 *
 */
public interface ITableColModifyDetailScriptWrap extends IScriptModelWrap {

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getNewName ();

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getOldName();
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getMark();
	
}
