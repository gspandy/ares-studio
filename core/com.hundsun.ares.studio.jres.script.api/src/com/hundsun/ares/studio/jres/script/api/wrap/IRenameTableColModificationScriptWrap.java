/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColModifyDetailScriptWrap;

/**
 * ���������ֶ�
 * 
 * @author yanwj06282
 *
 */
public interface IRenameTableColModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ���������ֶε���ϸ��¼
	 * 
	 * @return
	 */
	public ITableColModifyDetailScriptWrap[] getDetails();
	
}
