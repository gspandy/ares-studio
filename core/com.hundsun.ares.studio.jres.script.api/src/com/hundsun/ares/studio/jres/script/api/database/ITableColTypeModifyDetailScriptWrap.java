/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * �޸ı��ֶ����͵���ϸ���ݵķ�װ����
 * 
 * @author yanwj06282
 *
 */
public interface ITableColTypeModifyDetailScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getNewType();
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getMark();
	
}
