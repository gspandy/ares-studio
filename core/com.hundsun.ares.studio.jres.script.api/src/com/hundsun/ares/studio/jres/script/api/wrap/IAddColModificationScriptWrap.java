/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * ���ӱ��ֶ�
 * 
 * @author yanwj06282
 *
 */
public interface IAddColModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ���ӱ��ֶε���ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
