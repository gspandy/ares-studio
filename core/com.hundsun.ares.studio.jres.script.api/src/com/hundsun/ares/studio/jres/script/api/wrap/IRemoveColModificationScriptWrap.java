/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * ɾ�����ֶ�
 * 
 * @author yanwj06282
 *
 */
public interface IRemoveColModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡɾ�����ֶε���ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
