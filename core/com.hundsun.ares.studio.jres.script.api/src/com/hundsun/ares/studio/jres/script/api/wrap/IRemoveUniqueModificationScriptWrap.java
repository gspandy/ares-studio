/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * ɾ��ΨһԼ��
 * 
 * @author yanwj06282
 *
 */
public interface IRemoveUniqueModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡɾ��ΨһԼ������ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
