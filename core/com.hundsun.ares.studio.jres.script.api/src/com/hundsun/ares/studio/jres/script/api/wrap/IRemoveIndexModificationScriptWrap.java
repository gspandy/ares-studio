/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;

/**
 * ɾ��������
 * 
 * @author yanwj06282
 *
 */
public interface IRemoveIndexModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡɾ������������ϸ��¼
	 * 
	 * @return
	 */
	public ITableIndexScriptWrap[] getDetails();
	
}
