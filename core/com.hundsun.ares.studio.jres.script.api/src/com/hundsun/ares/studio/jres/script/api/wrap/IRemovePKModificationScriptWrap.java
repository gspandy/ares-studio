/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * ɾ��������
 * 
 * @author yanwj06282
 *
 */
public interface IRemovePKModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡɾ������������ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
