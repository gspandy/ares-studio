/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * �޸ı��ֶ�����
 * 
 * @author yanwj06282
 *
 */
public interface ITableColPKModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ�޸ı��ֶ�������ϸ
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
